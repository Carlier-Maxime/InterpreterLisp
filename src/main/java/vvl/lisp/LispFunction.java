package vvl.lisp;

import org.jetbrains.annotations.NotNull;
import vvl.util.ConsList;

@FunctionalInterface
interface LispEvalFunction {
    LispItem apply(ConsList<LispItem> items) throws LispError;
}

class LispFunction implements LispItem {
    public static final LispFunction NOT = new LispFunction(items -> LispBoolean.valueOf(items.car()==LispBoolean.FALSE), LispBoolean.class);

    public static final LispFunction AND = new LispFunction(items -> {
        for (LispItem item: items) if (item == LispBoolean.FALSE) return LispBoolean.FALSE;
        return LispBoolean.TRUE;
    }, true, LispBoolean.class);

    public static final LispFunction OR = new LispFunction(items -> {
        for (LispItem item: items) if (item == LispBoolean.TRUE) return LispBoolean.TRUE;
        return LispBoolean.FALSE;
    }, true, LispBoolean.class);

    public static final LispFunction COMPARE = new LispFunction(items -> new LispNumber(((LispNumber) items.car()).compareTo((LispNumber) items.cdr().car())), LispNumber.class, LispNumber.class);
    public static final LispFunction GREATER = new LispFunction(items -> LispBoolean.valueOf(((int) ((LispNumber) COMPARE.eval(items)).value())==1), LispNumber.class, LispNumber.class);
    public static final LispFunction LESSER = new LispFunction(items -> LispBoolean.valueOf(((int) ((LispNumber) COMPARE.eval(items)).value())==-1), LispNumber.class, LispNumber.class);
    public static final LispFunction EQUALS = new LispFunction(items -> LispBoolean.valueOf(((int) ((LispNumber) COMPARE.eval(items)).value())==0), LispNumber.class, LispNumber.class);
    public static final LispFunction GREATER_OR_EQUALS = new LispFunction(items -> LispBoolean.valueOf(((int) ((LispNumber) COMPARE.eval(items)).value())>=0), LispNumber.class, LispNumber.class);
    public static final LispFunction LESSER_OR_EQUALS = new LispFunction(items -> LispBoolean.valueOf(((int) ((LispNumber) COMPARE.eval(items)).value())<=0), LispNumber.class, LispNumber.class);

    private final LispEvalFunction function;
    private final int nbArgs;
    private final Class<? extends LispItem>[] types;
    private final boolean lastArgIsVarargs;

    @SafeVarargs
    public LispFunction(@NotNull LispEvalFunction function, Class<? extends LispItem>... types) {
        this(function, false, types);
    }

    @SafeVarargs
    public LispFunction(@NotNull LispEvalFunction function, boolean lastArgIsVarargs, Class<? extends LispItem>... types) {
        this.function = function;
        this.nbArgs = types.length;
        this.types = types;
        this.lastArgIsVarargs = lastArgIsVarargs;
    }


    @Override
    public LispItem eval(ConsList<LispItem> items) throws LispError {
        int size = items.size();
        if ((size!=nbArgs && !lastArgIsVarargs) || (lastArgIsVarargs && size<nbArgs-1)) throw new LispError("Invalid number of operands");
        ConsList<LispItem> tmp = items;
        for (int i=0; i<size; i++) {
            if (tmp.car().getClass() != types[i >= types.length ? types.length-1 : i]) throw new LispError("Invalid Type of argument at index "+i+" , expected "+types[i]+", got "+tmp.car().getClass());
            tmp = tmp.cdr();
        }
        return function.apply(items);
    }
}
