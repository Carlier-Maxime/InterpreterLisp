package vvl.lisp.functions;

import vvl.lisp.LispBoolean;
import vvl.lisp.LispItem;

public class LogicalOperations {
    private LogicalOperations() {}
    public static final LispFunction NOT = new LispFunction(params -> LispBoolean.valueOf(params.car()==LispBoolean.FALSE), LispBoolean.class);
    public static final LispFunction AND = new LispFunction(params -> {
        for (LispItem param: params) if (param == LispBoolean.FALSE) return LispBoolean.FALSE;
        return LispBoolean.TRUE;
    }, true, LispBoolean.class);
    public static final LispFunction OR = new LispFunction(params -> {
        for (LispItem param: params) if (param == LispBoolean.TRUE) return LispBoolean.TRUE;
        return LispBoolean.FALSE;
    }, true, LispBoolean.class);
}
