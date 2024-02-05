package vvl.lisp.functions;

import vvl.lisp.LispNumber;

import java.math.BigInteger;

public class MathOperations {
    private MathOperations() {}
    public static final LispFunction ADD = new LispFunction(items -> {
        var result = new LispNumber(BigInteger.valueOf(0));
        var size = items.size();
        for (var i = 0; i < size; i++) {
            result = result.add((LispNumber) items.car());
            items = items.cdr();
        }
        return result;
    }, LispNumber.class, true, LispNumber.class);
    public static final LispFunction MUL = new LispFunction(items -> {
        var result = new LispNumber(BigInteger.valueOf(1));
        var size = items.size();
        for (var i = 0; i < size; i++) {
            result = result.mul((LispNumber) items.car());
            items = items.cdr();
        }
        return result;
    }, LispNumber.class, true, LispNumber.class);
    public static final LispFunction SUB = new LispFunction(items -> {
        if (items.isEmpty()) throw LispFunction.INVALID_NUMBER_OF_OPERAND;
        var result = (LispNumber) items.car();
        items = items.cdr();
        var size = items.size();
        for (var i = 0; i < size; i++) {
            result = result.sub((LispNumber) items.car());
            items = items.cdr();
        }
        return size == 0 ? result.sub(null) : result;
    }, LispNumber.class, true, LispNumber.class);
    public static final LispFunction DIV = new LispFunction(items -> {
        if (items.size() < 2) throw LispFunction.INVALID_NUMBER_OF_OPERAND;
        var result = (LispNumber) items.car();
        items = items.cdr();
        var size = items.size();
        for (var i = 0; i < size; i++) {
            result = result.div((LispNumber) items.car());
            items = items.cdr();
        }
        return result;
    }, LispNumber.class, true, LispNumber.class);
}
