package vvl.lisp.functions;

import org.jetbrains.annotations.NotNull;
import vvl.lisp.LispNumber;

import java.math.BigInteger;
import java.util.function.BinaryOperator;

public class MathOperations {
    private MathOperations() {}

    private static LispFunction makeVariadicFunction(@NotNull BinaryOperator<LispNumber> op, @NotNull LispNumber initValue) {
        return new LispFunction(params -> {
            var result = initValue;
            var size = params.size();
            for (var i = 0; i < size; i++) {
                result = op.apply(result, (LispNumber) params.car());
                params = params.cdr();
            }
            return result;
        }, true, LispNumber.class);
    }

    private static LispFunction makeBinaryOperationFunction(@NotNull BinaryOperator<LispNumber> op) {
        return new LispFunction(params -> op.apply(((LispNumber) params.car()), (LispNumber) params.cdr().car()), LispNumber.class, LispNumber.class);
    }
    public static final LispFunction ADD = makeVariadicFunction(LispNumber::add, new LispNumber(BigInteger.valueOf(0)));
    public static final LispFunction MUL =  makeVariadicFunction(LispNumber::mul, new LispNumber(BigInteger.valueOf(1)));
    public static final LispFunction SUB = new LispFunction(params -> {
        if (params.size() > 2) throw LispFunction.INVALID_NUMBER_OF_OPERAND;
        var result = (LispNumber) params.car();
        return result.sub(params.size()==1 ? null : (LispNumber) params.cdr().car());
    }, true, LispNumber.class, LispNumber.class);

    public static final LispFunction DIV = makeBinaryOperationFunction(LispNumber::div);
    public static final LispFunction MAX = makeBinaryOperationFunction(LispNumber::max);
    public static final LispFunction MIN = makeBinaryOperationFunction(LispNumber::min);
    public static final LispFunction POW = makeBinaryOperationFunction(LispNumber::pow);

    public static final LispFunction ABS = new LispFunction(params -> ((LispNumber) params.car()).abs(), LispNumber.class);
    public static final LispFunction CBRT = new LispFunction(params -> ((LispNumber) params.car()).cbrt(), LispNumber.class);
    public static final LispFunction CEIL = new LispFunction(params -> ((LispNumber) params.car()).ceil(), LispNumber.class);
    public static final LispFunction FLOOR = new LispFunction(params -> ((LispNumber) params.car()).floor(), LispNumber.class);
    public static final LispFunction LOG10 = new LispFunction(params -> ((LispNumber) params.car()).log10(), LispNumber.class);
    public static final LispFunction COS = new LispFunction(params -> ((LispNumber) params.car()).cos(), LispNumber.class);
    public static final LispFunction SIN = new LispFunction(params -> ((LispNumber) params.car()).sin(), LispNumber.class);
    public static final LispFunction RINT = new LispFunction(params -> ((LispNumber) params.car()).rint(), LispNumber.class);
    public static final LispFunction ROUND = new LispFunction(params -> ((LispNumber) params.car()).round(), LispNumber.class);
    public static final LispFunction SIGNUM = new LispFunction(params -> ((LispNumber) params.car()).signum(), LispNumber.class);
    public static final LispFunction SQRT = new LispFunction(params -> ((LispNumber) params.car()).sqrt(), LispNumber.class);
}
