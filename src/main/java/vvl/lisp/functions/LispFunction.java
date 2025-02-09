package vvl.lisp.functions;

import org.jetbrains.annotations.NotNull;
import vvl.lisp.*;
import vvl.lisp.pairs.LispList;
import vvl.lisp.pairs.LispParams;
import vvl.util.ConsList;

public class LispFunction implements LispItem {
    public static final LispError INVALID_NUMBER_OF_OPERAND = new LispError("Invalid number of operands");

    private final LispEvalFunction function;
    private final int nbArgs;
    private final ConsList<Class<? extends LispItem>> types;
    private final boolean lastArgIsVarargs;

    @SafeVarargs
    public LispFunction(@NotNull LispEvalFunction function, Class<? extends LispItem>... types) {
        this(function, false, types);
    }

    @SafeVarargs
    public LispFunction(@NotNull LispEvalFunction function, boolean lastArgIsVarargs, Class<? extends LispItem>... types) {
        this.function = function;
        this.nbArgs = types.length;
        this.types = ConsList.asList(types);
        this.lastArgIsVarargs = lastArgIsVarargs;
    }

    protected void checkParameter(@NotNull ConsList<LispItem> items) throws LispError {
        int size = items.size();
        if ((size!=nbArgs && !lastArgIsVarargs) || (lastArgIsVarargs && size<nbArgs-1)) throw INVALID_NUMBER_OF_OPERAND;
    }

    @Override
    @NotNull
    public LispItem eval(@NotNull LispContext context) throws LispError {
        return apply(new LispParams(context, LispList.NIL));
    }

    @NotNull
    public LispItem apply(@NotNull LispContext context, @NotNull ConsList<LispItem> items) throws LispError {
        return apply(new LispParams(context, items, types));
    }

    @NotNull
    public LispItem apply(@NotNull LispParams params) throws LispError {
        checkParameter(params);
        params.setTypes(types);
        return function.apply(params);
    }

    public int getNbArgs() {
        return nbArgs;
    }
}
