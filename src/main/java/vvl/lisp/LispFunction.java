package vvl.lisp;

import org.jetbrains.annotations.NotNull;
import vvl.util.ConsList;
import vvl.util.ConsListImpl;
import vvl.util.ConsListLispItem;

import java.math.BigInteger;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
interface LispEvalFunction {
    LispItem apply(ConsList<LispItem> items) throws LispError;
}

class LispFunction implements LispItem {
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
    public static final LispFunction NOT = new LispFunction(items -> LispBoolean.valueOf(items.car()==LispBoolean.FALSE), LispBoolean.class, LispBoolean.class);

    public static final LispFunction AND = new LispFunction(items -> {
        for (LispItem item: items) if (item == LispBoolean.FALSE) return LispBoolean.FALSE;
        return LispBoolean.TRUE;
    }, LispBoolean.class, true, LispBoolean.class);

    public static final LispFunction OR = new LispFunction(items -> {
        for (LispItem item: items) if (item == LispBoolean.TRUE) return LispBoolean.TRUE;
        return LispBoolean.FALSE;
    }, LispBoolean.class, true, LispBoolean.class);

    public static final LispFunction CHECK_CONDITION_FOR_PAIRS = new LispFunction(items -> {
        int size = items.size()-2;
        if (size<0) throw INVALID_NUMBER_OF_OPERAND;
        LispFunction cdn = (LispFunction) items.car();
        items = items.cdr();
        for (var i=0; i<size; i++) if (cdn.eval(ConsList.asList(items.car(), (items = items.cdr()).car()))==LispBoolean.FALSE) return LispBoolean.FALSE;
        return LispBoolean.TRUE;
    }, LispBoolean.class, true, LispFunction.class, LispNumber.class);

    public static final LispFunction GREATER_CONDITION = new LispFunction(items -> LispBoolean.valueOf(((LispNumber) items.car()).compareTo((LispNumber) items.cdr().car()) > 0), LispBoolean.class, LispNumber.class, LispNumber.class);
    public static final LispFunction LESSER_CONDITION = new LispFunction(items -> LispBoolean.valueOf(((LispNumber) items.car()).compareTo((LispNumber) items.cdr().car()) < 0), LispBoolean.class, LispNumber.class, LispNumber.class);
    public static final LispFunction EQUALS_CONDITION = new LispFunction(items -> LispBoolean.valueOf(((LispNumber) items.car()).compareTo((LispNumber) items.cdr().car()) == 0), LispBoolean.class, LispNumber.class, LispNumber.class);
    public static final LispFunction GREATER_OR_EQUALS_CONDITION = new LispFunction(items -> LispBoolean.valueOf(((LispNumber) items.car()).compareTo((LispNumber) items.cdr().car()) >= 0), LispBoolean.class, LispNumber.class, LispNumber.class);
    public static final LispFunction LESSER_OR_EQUALS_CONDITION = new LispFunction(items -> LispBoolean.valueOf(((LispNumber) items.car()).compareTo((LispNumber) items.cdr().car()) <= 0), LispBoolean.class, LispNumber.class, LispNumber.class);
    public static final LispFunction GREATER = new LispFunction(items -> CHECK_CONDITION_FOR_PAIRS.eval(items.prepend(GREATER_CONDITION.quote())), LispBoolean.class, true, LispNumber.class);
    public static final LispFunction LESSER = new LispFunction(items -> CHECK_CONDITION_FOR_PAIRS.eval(items.prepend(LESSER_CONDITION.quote())), LispBoolean.class, true, LispNumber.class);
    public static final LispFunction IS_EQUALS = new LispFunction(items -> CHECK_CONDITION_FOR_PAIRS.eval(items.prepend(EQUALS_CONDITION.quote())), LispBoolean.class, true, LispNumber.class);
    public static final LispFunction GREATER_OR_EQUALS = new LispFunction(items -> CHECK_CONDITION_FOR_PAIRS.eval(items.prepend(GREATER_OR_EQUALS_CONDITION.quote())), LispBoolean.class, true, LispNumber.class);
    public static final LispFunction LESSER_OR_EQUALS = new LispFunction(items -> CHECK_CONDITION_FOR_PAIRS.eval(items.prepend(LESSER_OR_EQUALS_CONDITION.quote())), LispBoolean.class, true, LispNumber.class);

    public static final LispFunction ADD = new LispFunction(items -> {
        var result = new LispNumber(BigInteger.valueOf(0));
        var size = items.size();
        for (var i=0; i<size; i++) {
            result = result.add((LispNumber) items.car());
            items = items.cdr();
        }
        return result;
    }, LispNumber.class, true, LispNumber.class);

    public static final LispFunction MUL = new LispFunction(items -> {
        var result = new LispNumber(BigInteger.valueOf(1));
        var size = items.size();
        for (var i=0; i<size; i++) {
            result = result.mul((LispNumber) items.car());
            items = items.cdr();
        }
        return result;
    }, LispNumber.class, true, LispNumber.class);

    public static final LispFunction SUB = new LispFunction(items -> {
        if (items.isEmpty()) throw INVALID_NUMBER_OF_OPERAND;
        var result = (LispNumber) items.car();
        items = items.cdr();
        var size = items.size();
        for (var i=0; i<size; i++) {
            result = result.sub((LispNumber) items.car());
            items = items.cdr();
        }
        return size==0 ? result.sub(null) : result ;
    }, LispNumber.class, true, LispNumber.class);

    public static final LispFunction DIV = new LispFunction(items -> {
        if (items.size() < 2) throw INVALID_NUMBER_OF_OPERAND;
        var result = (LispNumber) items.car();
        items = items.cdr();
        var size = items.size();
        for (var i=0; i<size; i++) {
            result = result.div((LispNumber) items.car());
            items = items.cdr();
        }
        return result ;
    }, LispNumber.class, true, LispNumber.class);

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
        var noEval = tmp.getClass() == ConsListLispItem.class;
        for (var i=0; i<size; i++) {
            Supplier<LispItem> car = (noEval) ? ((ConsListLispItem) tmp)::carNoEval :  tmp::car;
            var itemType = car.get().outputType(items);
            if (!(types[i >= types.length ? types.length-1 : i].isAssignableFrom(itemType))) throw new LispError("Invalid Type of argument at index "+i+" , expected "+types[i]+", got "+itemType);
            tmp = tmp.cdr();
        }
    }

    @Override
    public LispItem eval(ConsList<LispItem> items) throws LispError {
        checkParameter(items);
        return function.apply(new ConsListLispItem((ConsListImpl<LispItem>) items));
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
