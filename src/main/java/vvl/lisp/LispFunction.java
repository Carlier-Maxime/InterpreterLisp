package vvl.lisp;

import org.jetbrains.annotations.NotNull;
import vvl.util.ConsList;

@FunctionalInterface
interface LispEvalFunction {
    LispItem apply(ConsList<LispItem> items);
}

class LispFunction implements LispItem {
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
