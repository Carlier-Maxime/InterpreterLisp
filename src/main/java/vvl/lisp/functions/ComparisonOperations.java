package vvl.lisp.functions;

import vvl.lisp.LispBoolean;
import vvl.lisp.LispNumber;
import vvl.util.ConsList;

public class ComparisonOperations {
    private ComparisonOperations() {}
    public static final LispFunction CHECK_CONDITION_FOR_PAIRS = new LispFunction(items -> {
        int size = items.size() - 2;
        if (size < 0) throw LispFunction.INVALID_NUMBER_OF_OPERAND;
        LispFunction cdn = (LispFunction) items.car();
        items = items.cdr();
        for (var i = 0; i < size; i++)
            if (cdn.eval(ConsList.asList(items.car(), (items = items.cdr()).car())) == LispBoolean.FALSE)
                return LispBoolean.FALSE;
        return LispBoolean.TRUE;
    }, LispBoolean.class, true, LispFunction.class, LispNumber.class);
    public static final LispFunction GREATER_CONDITION = new LispFunction(items -> LispBoolean.valueOf(((LispNumber) items.car()).compareTo((LispNumber) items.cdr().car()) > 0), LispBoolean.class, LispNumber.class, LispNumber.class);
    public static final LispFunction GREATER = new LispFunction(items -> CHECK_CONDITION_FOR_PAIRS.eval(items.prepend(GREATER_CONDITION.quote())), LispBoolean.class, true, LispNumber.class);
    public static final LispFunction LESSER_CONDITION = new LispFunction(items -> LispBoolean.valueOf(((LispNumber) items.car()).compareTo((LispNumber) items.cdr().car()) < 0), LispBoolean.class, LispNumber.class, LispNumber.class);
    public static final LispFunction LESSER = new LispFunction(items -> CHECK_CONDITION_FOR_PAIRS.eval(items.prepend(LESSER_CONDITION.quote())), LispBoolean.class, true, LispNumber.class);
    public static final LispFunction EQUALS_CONDITION = new LispFunction(items -> LispBoolean.valueOf(((LispNumber) items.car()).compareTo((LispNumber) items.cdr().car()) == 0), LispBoolean.class, LispNumber.class, LispNumber.class);
    public static final LispFunction IS_EQUALS = new LispFunction(items -> CHECK_CONDITION_FOR_PAIRS.eval(items.prepend(EQUALS_CONDITION.quote())), LispBoolean.class, true, LispNumber.class);
    public static final LispFunction GREATER_OR_EQUALS_CONDITION = new LispFunction(items -> LispBoolean.valueOf(((LispNumber) items.car()).compareTo((LispNumber) items.cdr().car()) >= 0), LispBoolean.class, LispNumber.class, LispNumber.class);
    public static final LispFunction GREATER_OR_EQUALS = new LispFunction(items -> CHECK_CONDITION_FOR_PAIRS.eval(items.prepend(GREATER_OR_EQUALS_CONDITION.quote())), LispBoolean.class, true, LispNumber.class);
    public static final LispFunction LESSER_OR_EQUALS_CONDITION = new LispFunction(items -> LispBoolean.valueOf(((LispNumber) items.car()).compareTo((LispNumber) items.cdr().car()) <= 0), LispBoolean.class, LispNumber.class, LispNumber.class);
    public static final LispFunction LESSER_OR_EQUALS = new LispFunction(items -> CHECK_CONDITION_FOR_PAIRS.eval(items.prepend(LESSER_OR_EQUALS_CONDITION.quote())), LispBoolean.class, true, LispNumber.class);
}
