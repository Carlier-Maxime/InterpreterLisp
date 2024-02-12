package vvl.lisp.functions;

import vvl.lisp.LispNumber;

import java.math.BigInteger;

public class MathOperations {
    private MathOperations() {}
    public static final LispFunction ADD = new LispFunction(params -> {
        var result = new LispNumber(BigInteger.valueOf(0));
        var size = params.size();
        for (var i = 0; i < size; i++) {
            result = result.add((LispNumber) params.car());
            params = params.cdr();
        }
        return result;
    }, true, LispNumber.class);
    public static final LispFunction MUL = new LispFunction(params -> {
        var result = new LispNumber(BigInteger.valueOf(1));
        var size = params.size();
        for (var i = 0; i < size; i++) {
            result = result.mul((LispNumber) params.car());
            params = params.cdr();
        }
        return result;
    }, true, LispNumber.class);
    public static final LispFunction SUB = new LispFunction(params -> {
        if (params.size() > 2) throw LispFunction.INVALID_NUMBER_OF_OPERAND;
        var result = (LispNumber) params.car();
        params = params.cdr();
        var size = params.size();
        for (var i = 0; i < size; i++) {
            result = result.sub((LispNumber) params.car());
            params = params.cdr();
        }
        return size == 0 ? result.sub(null) : result;
    }, true, LispNumber.class, LispNumber.class);
    public static final LispFunction DIV = new LispFunction(params -> {
        var result = (LispNumber) params.car();
        params = params.cdr();
        var size = params.size();
        for (var i = 0; i < size; i++) {
            result = result.div((LispNumber) params.car());
            params = params.cdr();
        }
        return result;
    }, LispNumber.class, LispNumber.class);

    public static final LispFunction MAX = new LispFunction(params -> ((LispNumber) params.car()).max((LispNumber) params.cdr().car()), LispNumber.class, LispNumber.class);
    public static final LispFunction MIN = new LispFunction(params -> ((LispNumber) params.car()).min((LispNumber) params.cdr().car()), LispNumber.class, LispNumber.class);
    public static final LispFunction POW = new LispFunction(params -> ((LispNumber) params.car()).pow((LispNumber) params.cdr().car()), LispNumber.class, LispNumber.class);

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
