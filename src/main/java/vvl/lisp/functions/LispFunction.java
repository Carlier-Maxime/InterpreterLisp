package vvl.lisp.functions;

import org.jetbrains.annotations.NotNull;
import vvl.lisp.*;
import vvl.util.ConsList;
import vvl.util.ConsListImpl;
import vvl.lisp.LispList;

import java.util.function.Supplier;

public class LispFunction implements LispItem {
    public static final LispError INVALID_NUMBER_OF_OPERAND = new LispError("Invalid number of operands");

    public static final LispFunction QUOTE = new LispFunction(ConsList::car, LispItem.class, LispItem.class) {
        @Override
        public LispItem eval(ConsList<LispItem> items) throws LispError {
            checkParameter(items);
            return getFunction().apply(items);
        }

        @Override
        public Class<? extends LispItem> outputType(ConsList<LispItem> items) {
            if (items==null || items.isEmpty()) return LispItem.class;
            return items.car().getClass();
        }
    };

    public static final LispFunction IF = new LispFunction(items -> (items.car() == LispBoolean.TRUE) ? items.cdr().car() : items.cdr().cdr().car(), LispItem.class, LispBoolean.class, LispItem.class, LispItem.class){
        @Override
        protected void checkParameter(ConsList<LispItem> items) throws LispError {
            super.checkParameter(items);
            Class<?> a, b;
            if (items.getClass() == LispList.class) {
                a = ((LispList) (items.cdr())).carNoEval().outputType(null);
                b = ((LispList) (items.cdr().cdr())).carNoEval().outputType(null);
            } else {
                a = items.cdr().car().outputType(null);
                b = items.cdr().cdr().car().outputType(null);
            }
            if (a!=b) throw new LispError("Not same output type for if else : "+a+", "+b);
        }

        @Override
        public Class<? extends LispItem> outputType(ConsList<LispItem> items) {
            if (items == null || items.size() < 3) return super.outputType(items);
            LispItem thenExpr = items.cdr().car(), elseExpr =  items.cdr().cdr().car();
            if (thenExpr.outputType(null) != elseExpr.outputType(null)) return super.outputType(items);
            return thenExpr.outputType(null);
        }
    };

    public static final LispFunction CONS = new LispFunction(items -> new LispCons(items.car(), items.cdr().car()), LispCons.class, LispItem.class, LispItem.class);

    private final LispEvalFunction function;
    private final Class<? extends LispItem> output;
    private final int nbArgs;
    private final Class<? extends LispItem>[] types;
    private final boolean lastArgIsVarargs;

    @SafeVarargs
    public LispFunction(@NotNull LispEvalFunction function, Class<? extends LispItem> output, Class<? extends LispItem>... types) {
        this(function, output, false, types);
    }

    @SafeVarargs
    public LispFunction(@NotNull LispEvalFunction function, Class<? extends LispItem> output, boolean lastArgIsVarargs, Class<? extends LispItem>... types) {
        this.function = function;
        this.output = output;
        this.nbArgs = types.length;
        this.types = types;
        this.lastArgIsVarargs = lastArgIsVarargs;
    }

    protected void checkParameter(ConsList<LispItem> items) throws LispError {
        int size = items.size();
        if ((size!=nbArgs && !lastArgIsVarargs) || (lastArgIsVarargs && size<nbArgs-1)) throw INVALID_NUMBER_OF_OPERAND;
        var tmp = items;
        var noEval = tmp.getClass() == LispList.class;
        for (var i=0; i<size; i++) {
            Supplier<LispItem> car = (noEval) ? ((LispList) tmp)::carNoEval :  tmp::car;
            var itemType = car.get().outputType(items);
            if (!(types[i >= types.length ? types.length-1 : i].isAssignableFrom(itemType))) throw new LispError("Invalid Type of argument at index "+i+" , expected "+types[i]+", got "+itemType);
            tmp = tmp.cdr();
        }
    }

    @Override
    public LispItem eval(ConsList<LispItem> items) throws LispError {
        checkParameter(items);
        return function.apply(new LispList((ConsListImpl<LispItem>) items));
    }

    @Override
    public Class<? extends LispItem> outputType(ConsList<LispItem> items) {
        return output;
    }

    public LispEvalFunction getFunction() {
        return function;
    }

    public LispExpression quote() {
        return new LispExpression(QUOTE, this);
    }
}
