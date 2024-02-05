package vvl.lisp.functions;

import vvl.lisp.*;
import vvl.util.ConsListImpl;

public class LispOperations {
    private LispOperations() {}
    public static final LispFunction QUOTE = new LispFunction(LispParams::carNoEval, LispItem.class, LispItem.class) {
    };
    public static final LispFunction IF = new LispFunction(items -> (items.car() == LispBoolean.TRUE) ? items.cdr().car() : items.cdr().cdr().car(), LispItem.class, LispBoolean.class, LispItem.class, LispItem.class) {
    };
    public static final LispFunction CONS = new LispFunction(items -> {
        var left = items.car();
        var right = items.cdr().car();
        if (LispList.class.isAssignableFrom(right.getClass())) return ((LispList) right).prepend(left);
        return new LispCons(left, right);
    }, LispCons.class, LispItem.class, LispItem.class);
    public static final LispFunction LIST = new LispFunction(items -> new LispList((ConsListImpl<LispItem>) items), LispList.class, true, LispItem.class);
    public static final LispFunction CAR = new LispFunction(items -> ((LispPair) items.car()).car(), LispItem.class, LispPair.class);
    public static final LispFunction CDR = new LispFunction(items -> ((LispPair) items.car()).cdr(), LispItem.class, LispPair.class);
}
