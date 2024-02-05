package vvl.lisp.functions;

import vvl.lisp.*;
import vvl.util.ConsListImpl;

public class LispOperations {
    private LispOperations() {}
    public static final LispFunction QUOTE = new LispFunction(LispParams::carNoEval, LispItem.class, LispItem.class);
    public static final LispFunction IF = new LispFunction(params -> (params.car() == LispBoolean.TRUE) ? params.cdr().car() : params.cdr().cdr().car(), LispItem.class, LispBoolean.class, LispItem.class, LispItem.class);
    public static final LispFunction CONS = new LispFunction(params -> {
        var left = params.car();
        var right = params.cdr().car();
        if (LispList.class.isAssignableFrom(right.getClass())) return ((LispList) right).prepend(left);
        return new LispCons(left, right);
    }, LispCons.class, LispItem.class, LispItem.class);
    public static final LispFunction LIST = new LispFunction(params -> new LispList((ConsListImpl<LispItem>) params.map(item -> item)), LispList.class, true, LispItem.class);
    public static final LispFunction CAR = new LispFunction(params -> ((LispPair) params.car()).car(), LispItem.class, LispPair.class);
    public static final LispFunction CDR = new LispFunction(params -> ((LispPair) params.car()).cdr(), LispItem.class, LispPair.class);
}
