package vvl.lisp.functions;

import vvl.lisp.LispBoolean;
import vvl.lisp.LispItem;

public class LogicalOperations {
    public static final LispFunction NOT = new LispFunction(items -> LispBoolean.valueOf(items.car()==LispBoolean.FALSE), LispBoolean.class, LispBoolean.class);
    public static final LispFunction AND = new LispFunction(items -> {
        for (LispItem item: items) if (item == LispBoolean.FALSE) return LispBoolean.FALSE;
        return LispBoolean.TRUE;
    }, LispBoolean.class, true, LispBoolean.class);
    public static final LispFunction OR = new LispFunction(items -> {
        for (LispItem item: items) if (item == LispBoolean.TRUE) return LispBoolean.TRUE;
        return LispBoolean.FALSE;
    }, LispBoolean.class, true, LispBoolean.class);
}
