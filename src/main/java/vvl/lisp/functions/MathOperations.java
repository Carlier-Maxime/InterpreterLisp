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
}
