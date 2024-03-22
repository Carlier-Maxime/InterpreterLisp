package vvl.lisp.functions;

import org.jetbrains.annotations.NotNull;
import vvl.lisp.*;
import vvl.lisp.pairs.LispParams;
import vvl.util.ConsList;

import java.util.Map;

public class LispLambda extends LispFunction {
    private final ConsList<String> args;
    private final Map<String, LispItem> defaultArgs;
    private final LispItem func;

    public LispLambda(LispContext context, LispExpression args, LispItem func) {
        super(params -> null, true, LispItem.class);
        this.func = func;
        this.args = args.values().map(param -> {
            if (param instanceof LispIdentifier) return  param.toString();
            else throw new LispRuntimeError(LispContext.notValidIdentifier(param.toString()));
        });
        this.defaultArgs = context.getArgs();
    }

    @Override
    public @NotNull LispItem apply(@NotNull LispParams params) throws LispError {
        params.setTypes(ConsList.asList(LispItem.class));
        var context = new LispContext(params.getContext());
        for (var arg : defaultArgs.entrySet()) context.setArg(arg.getKey(), arg.getValue());
        int size = params.size();
        var ids = args;
        if (size != args.size()) throw LispFunction.INVALID_NUMBER_OF_OPERAND;
        for (var i=0; i<size; i++) {
            var id = ids.car();
            context.setArg(id, params.car());
            ids = ids.cdr();
            params = params.cdr();
        }
        return func.eval(context);
    }

    @Override
    public String toString() {
        return "lambda "+args+" "+func;
    }
}
