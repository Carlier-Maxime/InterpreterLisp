package vvl.lisp.functions;

import org.jetbrains.annotations.NotNull;
import vvl.lisp.*;
import vvl.util.ConsList;
import vvl.util.ConsListImpl;
import vvl.lisp.LispList;

import java.util.function.Supplier;

public class LispFunction implements LispItem {
    public static final LispError INVALID_NUMBER_OF_OPERAND = new LispError("Invalid number of operands");

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
            var expectedType = types[i >= types.length ? types.length-1 : i];
            if (!(expectedType.isAssignableFrom(itemType))) throw new LispError("Invalid Type of argument at index "+i+" , expected "+expectedType+", got "+itemType);
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
        return new LispExpression(LispOperations.QUOTE, this);
    }
}
