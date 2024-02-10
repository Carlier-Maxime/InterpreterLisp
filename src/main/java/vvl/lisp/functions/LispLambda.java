package vvl.lisp.functions;

import org.jetbrains.annotations.NotNull;
import vvl.lisp.*;
import vvl.lisp.pairs.LispList;
import vvl.lisp.pairs.LispParams;
import vvl.util.ConsList;

public class LispLambda extends LispFunction {
    private final ConsList<String> args;
    private final LispItem func;

    public LispLambda(LispExpression args, LispItem func) {
        super(params -> LispList.NIL, true, LispItem.class);
        this.func = func;
        this.args = args.values().map(param -> {
            if (param instanceof LispIdentifier) return ((LispIdentifier) param).getId();
            else throw new LispRuntimeError(LispContext.notValidIdentifier(param.toString()));
        });
    }

    @Override
    public @NotNull LispItem apply(@NotNull LispParams params) throws LispError {
        checkParameter(params);
        var context = new LispContext(params.getContext());
        int size = params.size();
        var ids = args;
        if (size != args.size()) throw LispFunction.INVALID_NUMBER_OF_OPERAND;
        for (var i=0; i<size; i++) {
            var id = ids.car();
            context.setVar(id, params.car(), context.isVars(id));
            ids = ids.cdr();
            params = params.cdr();
        }
        return func.eval(context);
    }

    @Override
    public String toString() {
        return "lambda "+args.toString()+" "+func.toString();
    }
}
