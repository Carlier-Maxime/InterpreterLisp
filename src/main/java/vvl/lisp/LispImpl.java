package vvl.lisp;

public class LispImpl implements Lisp {
    private LispItem parseSpecialNotation(String expr) throws LispError {
        assert expr.charAt(0)=='#';
        try {
            if ("#t".equals(expr) || "#f".equals(expr)) return LispBoolean.valueOf(expr);
            else if (expr.startsWith("#e")) return LispNumber.parseDouble(expr.substring(2));
            else if (expr.startsWith("#b")) return LispNumber.parseBigInteger(expr.substring(2), 2);
            else if (expr.startsWith("#o")) return LispNumber.parseBigInteger(expr.substring(2), 8);
            else if (expr.startsWith("#d")) return LispNumber.parseBigInteger(expr.substring(2), 10);
            else if (expr.startsWith("#x")) return LispNumber.parseBigInteger(expr.substring(2), 16);
            else if (expr.startsWith("#r")) return LispNumber.parseRatio(expr.substring(2));
            else if ("#()".equals(expr)) return new LispExpression();
        } catch (Exception e) {
            throw new LispError("Parsing special notation failed : "+expr,e);
        }
        throw new LispError("Special notation unknown : "+expr);
    }

    private LispItem parseSingleElement(String expr) throws LispError {
        try {
            if (expr.charAt(0)=='#') return parseSpecialNotation(expr);
            if (LispNumber.isNumber(expr)) return LispNumber.parseNumber(expr);
            return new LispIdentifier(expr);
        } catch (LispError e) {
            throw e;
        } catch (Exception e) {
            throw new LispError("Parsing single element failed",e);
        }
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
