package vvl.lisp;

import vvl.util.ConsList;
import vvl.util.ConsListImpl;

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
        String[] elems = expr.split("\\s+");
        if (elems.length==0) throw new LispError("Empty expression is invalid !");
        ConsList<LispExpression> lists = new ConsListImpl<>();
        for (int i=elems.length-1; i>=0; i--) {
            while (elems[i].endsWith(")")) {
                lists = lists.prepend(new LispExpression());
                elems[i] = elems[i].substring(0, elems[i].length()-1);
            }
            int nbClose = 0;
            while (elems[i].startsWith("(")) {
                if (lists.isEmpty()) throw new LispError("Parenthesis incorrect : "+expr);
                elems[i] = elems[i].substring(1);
                nbClose++;
            }
            if (!elems[i].isBlank()) {
                if (lists.isEmpty()) {
                    if (elems.length==1) return parseSingleElement(elems[i]);
                    throw new LispError("Missing Parenthesis : "+expr);
                }
                lists.car().prepend(parseSingleElement(elems[i]));
            }
            for (int j=0; j<nbClose; j++) {
                LispExpression lispExpr = lists.car();
                lists = lists.cdr();
                if (lists.isEmpty()) {
                    if (i==0) return lispExpr;
                    for (int k=i-1; k>=0; k--) if (!elems[k].isBlank()) throw new LispError("Element outside expression : "+expr);
                    return lispExpr;
                } else lists.car().prepend(lispExpr);
            }
        }
        throw new LispError("Invalid expression : "+expr);
    }

    @Override
    public LispItem evaluate(LispItem ex) throws LispError {
        return null;
    }
}
