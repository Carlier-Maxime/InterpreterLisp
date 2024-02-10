package vvl.lisp.functions;

import org.jetbrains.annotations.NotNull;
import vvl.lisp.*;
import vvl.lisp.pairs.LispParams;
import vvl.util.ConsList;
import vvl.util.ConsListImpl;

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

    protected void checkParameter(ConsList<LispItem> items) throws LispError {
        if (items==null) throw INVALID_NUMBER_OF_OPERAND;
        int size = items.size();
        if ((size!=nbArgs && !lastArgIsVarargs) || (lastArgIsVarargs && size<nbArgs-1)) throw INVALID_NUMBER_OF_OPERAND;
    }

    @Override
    public LispItem eval(@NotNull LispContext context) throws LispError {
        return apply(new LispParams(context, LispParams.NIL));
    }

    public LispItem apply(LispContext context, ConsList<LispItem> items) throws LispError {
        checkParameter(items);
        return function.apply(new LispParams(context, (ConsListImpl<LispItem>) items, types));
    }

    public LispItem apply(LispParams params) throws LispError {
        checkParameter(params);
        params.setTypes(types);
        return function.apply(params);
    }

    public LispExpression quote() throws LispError {
        return new LispExpression(new LispIdentifier("quote"), this);
    }
}
