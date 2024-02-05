package vvl.lisp.functions;

import org.jetbrains.annotations.NotNull;
import vvl.lisp.*;
import vvl.util.ConsList;
import vvl.util.ConsListImpl;

public class LispFunction implements LispItem {
    public static final LispError INVALID_NUMBER_OF_OPERAND = new LispError("Invalid number of operands");

    private final LispEvalFunction function;
    private final Class<? extends LispItem> output;
    private final int nbArgs;
    private final ConsList<Class<? extends LispItem>> types;
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
        this.types = ConsList.asList(types);
        this.lastArgIsVarargs = lastArgIsVarargs;
    }

    protected void checkParameter(ConsList<LispItem> items) throws LispError {
        int size = items.size();
        if ((size!=nbArgs && !lastArgIsVarargs) || (lastArgIsVarargs && size<nbArgs-1)) throw INVALID_NUMBER_OF_OPERAND;
    }

    @Override
    public LispItem eval(ConsList<LispItem> items) throws LispError {
        checkParameter(items);
        return function.apply(new LispParams((ConsListImpl<LispItem>) items, types));
    }

    public LispExpression quote() {
        return new LispExpression(LispOperations.QUOTE, this);
    }
}
