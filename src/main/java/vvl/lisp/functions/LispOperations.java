package vvl.lisp.functions;

import vvl.lisp.*;
import vvl.lisp.pairs.*;
import vvl.util.ConsList;
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
    public static final LispFunction SET_VAR = new LispFunction(params -> params.getContext().setVar(((LispIdentifier) params.cdr().carNoEval()).getId(), params.cdr().cdr().car(), ((LispBoolean) params.car()).value()), LispBoolean.class, LispIdentifier.class, LispItem.class);
    public static final LispFunction DEFINE = new LispFunction(params -> SET_VAR.apply(params.prepend(LispBoolean.FALSE)), LispIdentifier.class, LispItem.class);
    public static final LispFunction SET = new LispFunction(params -> SET_VAR.apply(params.prepend(LispBoolean.TRUE)), LispIdentifier.class, LispItem.class);
    public static final LispFunction LAMBDA = new LispFunction(params -> new LispLambda(((LispExpression) params.carNoEval()), params.cdr().carNoEval()), LispExpression.class, LispItem.class);
    public static final LispFunction MAP = new LispFunction(params -> {
        var func = (LispLambda) params.car();
        var lists = params.cdr();
        if (func.getNbArgs() != lists.size()) throw LispFunction.INVALID_NUMBER_OF_OPERAND;
        var size = ((LispList) lists.car()).size();
        for (var list : lists) if (((LispList) list).size()!=size) throw new LispError("All lists must be same size");
        var array = new LispItem[size];
        var mapParams = new LispMapParams(params.getContext(), lists.map(item -> (LispList) item), params.getTypes());
        for (var i=0; i<size; i++) {
            array[i]=func.apply(mapParams);
            mapParams.cdrListInPlace();
        }
        return new LispList((ConsListImpl<LispItem>) ConsList.asList(array));
    }, true, LispLambda.class, LispList.class, LispList.class);
}
