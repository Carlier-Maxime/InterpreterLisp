package vvl.lisp.functions;

import org.jetbrains.annotations.NotNull;
import vvl.lisp.LispNumber;

import java.math.BigInteger;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class MathOperations {
    private MathOperations() {}

    private static LispFunction makeVariadic(@NotNull BinaryOperator<LispNumber> op, @NotNull LispNumber initValue) {
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

    private static LispFunction makeBinary(@NotNull BinaryOperator<LispNumber> op) {
        return new LispFunction(params -> op.apply(((LispNumber) params.car()), (LispNumber) params.cdr().car()), LispNumber.class, LispNumber.class);
    }

    private static LispFunction makeUnary(@NotNull UnaryOperator<LispNumber> op) {
        return new LispFunction(params -> op.apply((LispNumber) params.car()), LispNumber.class);
    }
    public static final LispFunction ADD = makeVariadic(LispNumber::add, new LispNumber(BigInteger.valueOf(0)));
    public static final LispFunction MUL =  makeVariadic(LispNumber::mul, new LispNumber(BigInteger.valueOf(1)));
    public static final LispFunction SUB = new LispFunction(params -> {
        if (params.size() > 2) throw LispFunction.INVALID_NUMBER_OF_OPERAND;
        var result = (LispNumber) params.car();
        return result.sub(params.size()==1 ? null : (LispNumber) params.cdr().car());
    }, true, LispNumber.class, LispNumber.class);

    public static final LispFunction DIV = makeBinary(LispNumber::div);
    public static final LispFunction MAX = makeBinary(LispNumber::max);
    public static final LispFunction MIN = makeBinary(LispNumber::min);
    public static final LispFunction POW = makeBinary(LispNumber::pow);

    public static final LispFunction ABS = makeUnary(LispNumber::abs);
    public static final LispFunction CBRT = makeUnary(LispNumber::cbrt);
    public static final LispFunction CEIL = makeUnary(LispNumber::ceil);
    public static final LispFunction FLOOR = makeUnary(LispNumber::floor);
    public static final LispFunction LOG10 = makeUnary(LispNumber::log10);
    public static final LispFunction COS = makeUnary(LispNumber::cos);
    public static final LispFunction SIN = makeUnary(LispNumber::sin);
    public static final LispFunction RINT = makeUnary(LispNumber::rint);
    public static final LispFunction ROUND = makeUnary(LispNumber::round);
    public static final LispFunction SIGNUM = makeUnary(LispNumber::signum);
    public static final LispFunction SQRT = makeUnary(LispNumber::sqrt);
}
