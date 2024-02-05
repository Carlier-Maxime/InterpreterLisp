package vvl.lisp.functions;

import vvl.lisp.LispBoolean;
import vvl.lisp.LispNumber;
import vvl.util.ConsList;

public class ComparisonOperations {
    private ComparisonOperations() {}
    public static final LispFunction CHECK_CONDITION_FOR_PAIRS = new LispFunction(params -> {
        int size = params.size() - 2;
        if (size < 0) throw LispFunction.INVALID_NUMBER_OF_OPERAND;
        LispFunction cdn = (LispFunction) params.car();
        params = params.cdr();
        for (var i = 0; i < size; i++)
            if (cdn.eval(ConsList.asList(params.car(), (params = params.cdr()).car())) == LispBoolean.FALSE)
                return LispBoolean.FALSE;
        return LispBoolean.TRUE;
    }, LispBoolean.class, true, LispFunction.class, LispNumber.class);
    public static final LispFunction GREATER_CONDITION = new LispFunction(params -> LispBoolean.valueOf(((LispNumber) params.car()).compareTo((LispNumber) params.cdr().car()) > 0), LispBoolean.class, LispNumber.class, LispNumber.class);
    public static final LispFunction GREATER = new LispFunction(params -> CHECK_CONDITION_FOR_PAIRS.eval(params.prepend(GREATER_CONDITION.quote())), LispBoolean.class, true, LispNumber.class);
    public static final LispFunction LESSER_CONDITION = new LispFunction(params -> LispBoolean.valueOf(((LispNumber) params.car()).compareTo((LispNumber) params.cdr().car()) < 0), LispBoolean.class, LispNumber.class, LispNumber.class);
    public static final LispFunction LESSER = new LispFunction(params -> CHECK_CONDITION_FOR_PAIRS.eval(params.prepend(LESSER_CONDITION.quote())), LispBoolean.class, true, LispNumber.class);
    public static final LispFunction EQUALS_CONDITION = new LispFunction(params -> LispBoolean.valueOf(((LispNumber) params.car()).compareTo((LispNumber) params.cdr().car()) == 0), LispBoolean.class, LispNumber.class, LispNumber.class);
    public static final LispFunction IS_EQUALS = new LispFunction(params -> CHECK_CONDITION_FOR_PAIRS.eval(params.prepend(EQUALS_CONDITION.quote())), LispBoolean.class, true, LispNumber.class);
    public static final LispFunction GREATER_OR_EQUALS_CONDITION = new LispFunction(params -> LispBoolean.valueOf(((LispNumber) params.car()).compareTo((LispNumber) params.cdr().car()) >= 0), LispBoolean.class, LispNumber.class, LispNumber.class);
    public static final LispFunction GREATER_OR_EQUALS = new LispFunction(params -> CHECK_CONDITION_FOR_PAIRS.eval(params.prepend(GREATER_OR_EQUALS_CONDITION.quote())), LispBoolean.class, true, LispNumber.class);
    public static final LispFunction LESSER_OR_EQUALS_CONDITION = new LispFunction(params -> LispBoolean.valueOf(((LispNumber) params.car()).compareTo((LispNumber) params.cdr().car()) <= 0), LispBoolean.class, LispNumber.class, LispNumber.class);
    public static final LispFunction LESSER_OR_EQUALS = new LispFunction(params -> CHECK_CONDITION_FOR_PAIRS.eval(params.prepend(LESSER_OR_EQUALS_CONDITION.quote())), LispBoolean.class, true, LispNumber.class);
}
