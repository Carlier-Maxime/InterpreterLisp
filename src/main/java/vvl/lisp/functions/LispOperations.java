package vvl.lisp.functions;

import vvl.lisp.*;
import vvl.lisp.pairs.LispCons;
import vvl.lisp.pairs.LispList;
import vvl.lisp.pairs.LispPair;
import vvl.lisp.pairs.LispParams;
import vvl.util.ConsListImpl;

import java.util.NoSuchElementException;

public class LispOperations {
    private LispOperations() {}
    public static final LispFunction QUOTE = new LispFunction(LispParams::carNoEval, LispItem.class);
    public static final LispFunction IF = new LispFunction(params -> (params.car() == LispBoolean.TRUE) ? params.cdr().car() : params.cdr().cdr().car(), LispBoolean.class, LispItem.class, LispItem.class);
    public static final LispFunction CONS = new LispFunction(params -> {
        var left = params.car();
        var right = params.cdr().car();
        if (LispList.class.isAssignableFrom(right.getClass())) return ((LispList) right).prepend(left);
        return new LispCons(left, right);
    }, LispItem.class, LispItem.class);
    public static final LispFunction LIST = new LispFunction(params -> new LispList((ConsListImpl<LispItem>) params.map(item -> item)), true, LispItem.class);
    public static final LispFunction CAR = new LispFunction(params -> {
        try {
            return ((LispPair) params.car()).car();
        } catch (NoSuchElementException e) {
            return LispList.NIL;
        }
    }, LispPair.class);
    public static final LispFunction CDR = new LispFunction(params -> ((LispPair) params.car()).cdr(), LispPair.class);
    public static final LispFunction DEFINE = new LispFunction(params -> params.getContext().setVar(((LispIdentifier) params.carNoEval()).getId(), params.cdr().car(), false), LispIdentifier.class, LispItem.class);
    public static final LispFunction SET = new LispFunction(params -> params.getContext().setVar(((LispIdentifier) params.carNoEval()).getId(), params.cdr().car(), true), LispIdentifier.class, LispItem.class);
}
