package vvl.lisp.functions;

import vvl.lisp.LispBoolean;
import vvl.lisp.LispNumber;

import java.util.function.BiPredicate;

public class ComparisonOperations {
    private ComparisonOperations() {}

    private static LispFunction makeComparison(BiPredicate<LispNumber, LispNumber> cdn) {
        return new LispFunction(params -> {
            var size = params.size() - 1;
            for (var i = 0; i < size; i++) if (!cdn.test((LispNumber) params.car(), (LispNumber) (params = params.cdr()).car())) return LispBoolean.FALSE;
            return LispBoolean.TRUE;
        }, true, LispNumber.class, LispNumber.class);
    }

    public static final LispFunction GREATER = makeComparison((a, b) -> a.compareTo(b) > 0);
    public static final LispFunction LESSER = makeComparison((a, b) -> a.compareTo(b) < 0);
    public static final LispFunction IS_EQUALS = makeComparison((a, b) -> a.compareTo(b) == 0);
    public static final LispFunction GREATER_OR_EQUALS = makeComparison((a, b) -> a.compareTo(b) >= 0);
    public static final LispFunction LESSER_OR_EQUALS = makeComparison((a, b) -> a.compareTo(b) <= 0);
}
