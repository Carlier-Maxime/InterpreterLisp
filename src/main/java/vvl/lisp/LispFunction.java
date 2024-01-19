package vvl.lisp;

import org.jetbrains.annotations.NotNull;
import vvl.util.ConsList;

@FunctionalInterface
interface LispEvalFunction {
    LispItem apply(ConsList<LispItem> items);
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

    private final LispEvalFunction function;
    private final int nbArgs;
    private final Class<? extends LispItem>[] types;
    private final boolean repeatArgs;

    @SafeVarargs
    public LispFunction(@NotNull LispEvalFunction function, Class<? extends LispItem>... types) {
        this(function, false, types);
    }

    @SafeVarargs
    public LispFunction(@NotNull LispEvalFunction function, boolean repeatArgs, Class<? extends LispItem>... types) {
        this.function = function;
        this.nbArgs = types.length;
        this.types = types;
        this.repeatArgs = repeatArgs;
    }


    @Override
    public LispItem eval(ConsList<LispItem> items) throws LispError {
        if ((items.size()!=nbArgs && !repeatArgs) || (repeatArgs && items.size()%nbArgs!=0)) throw new LispError("Invalid number of operands");
        ConsList<LispItem> tmp = items;
        int size = items.size();
        for (int i=0; i<size; i++) {
            if (tmp.car().getClass() != types[i % types.length]) throw new LispError("Invalid Type of argument at index "+i+" , expected "+types[i]+", got "+tmp.car().getClass());
            tmp = tmp.cdr();
        }
        return function.apply(items);
    }
}
