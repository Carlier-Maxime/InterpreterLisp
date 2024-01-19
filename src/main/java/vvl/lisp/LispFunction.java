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

    @SafeVarargs
    public LispFunction(@NotNull LispEvalFunction function, Class<? extends LispItem>... types) {
        this.function = function;
        this.nbArgs = types.length;
        this.types = types;
    }


    @Override
    public LispItem eval(ConsList<LispItem> items) throws LispError {
        if (items.size()!=nbArgs) throw new LispError("Invalid Number of Argument, "+nbArgs+" argument required, but "+items.size()+" given");
        ConsList<LispItem> tmp = items;
        for (int i=0; i<types.length; i++) {
            if (tmp.car().getClass() != types[i]) throw new LispError("Invalid Type of argument at index "+i+" , expected "+types[i]+", got "+tmp.car().getClass());
            tmp = tmp.cdr();
        }
        return function.apply(items);
    }
}
