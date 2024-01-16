package vvl.lisp;

import java.math.BigInteger;

public class LispImpl implements Lisp {
    private LispItem parseSpecialNotation(String expr) throws LispError {
        assert expr.charAt(0)=='#';
        try {
            if ("#t".equals(expr) || "#f".equals(expr)) return LispBoolean.valueOf(expr);
            else if (expr.startsWith("#e")) return new LispNumber(Double.valueOf(expr.substring(2)));
            else if (expr.startsWith("#b")) return new LispNumber(new BigInteger(expr.substring(2),2));
            else if (expr.startsWith("#o")) return new LispNumber(new BigInteger(expr.substring(2),8));
            else if (expr.startsWith("#d")) return new LispNumber(new BigInteger(expr.substring(2),10));
            else if (expr.startsWith("#x")) return new LispNumber(new BigInteger(expr.substring(2),16));
            else if (expr.startsWith("#r")) {
                String[] r_part = expr.substring(2).split("/");
                return new LispNumber(new BigInteger(r_part[0]).divide(new BigInteger(r_part[1])));
            } else if ("#()".equals(expr)) return new LispExpression();
        } catch (Exception e) {
            throw new LispError("Parsing special notation failed : "+expr,e);
        }
        throw new LispError("Special notation unknown : "+expr);
    }

    private LispItem parseSingleElement(String expr) throws LispError {
        try {
            if (expr.charAt(0)=='#') return parseSpecialNotation(expr);
        } catch (LispError e) {
            throw e;
        } catch (Exception e) {
            throw new LispError("Parsing single element failed",e);
        }
        throw new LispError("Parsing single element failed, this element is unrecognized : "+expr);
    }

    @Override
    public LispItem parse(String expr) throws LispError {
        return parseSingleElement(expr);
    }

    @Override
    public LispItem evaluate(LispItem ex) throws LispError {
        return null;
    }
}
