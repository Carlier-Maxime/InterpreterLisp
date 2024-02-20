package vvl.lisp;

import org.jetbrains.annotations.NotNull;
import vvl.util.Cons;
import vvl.util.ConsList;

public class LispImpl implements Lisp {

    private ConsList<LispExpression> lists = ConsList.nil();
    private final LispContext context = new LispContext();
    @NotNull
    private LispItem parseSpecialNotation(@NotNull String expr) throws LispError {
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

    @NotNull
    private LispItem parseSingleElement(@NotNull String expr) throws LispError {
        if (expr.charAt(0)=='#') return parseSpecialNotation(expr);
        try {
            return LispNumber.parseNumber(expr);
        } catch (LispParseError e) {
            return new LispIdentifier(expr);
        }
    }

    @NotNull
    private String handleClosingParenthesis(@NotNull String element) {
        while (element.endsWith(")")) {
            lists = lists.prepend(new LispExpression());
            element = element.substring(0, element.length()-1);
        }
        return element;
    }

    @NotNull
    private Cons<Integer, String> handleOpeningParenthesis(@NotNull String element) throws LispError {
        int nbClose = 0;
        while (element.startsWith("(")) {
            if (lists.isEmpty()) throw new LispError("Parenthesis incorrect");
            element = element.substring(1);
            nbClose++;
        }
        return new Cons<>(nbClose, element);
    }

    private LispExpression handleCloseExpression(@NotNull String[] elems, int index, int nbClose) throws LispError {
        for (int j=0; j<nbClose; j++) {
            LispExpression lispExpr = lists.car();
            lists = lists.cdr();
            if (lists.isEmpty()) {
                if (index==0) return lispExpr;
                for (int k=index-1; k>=0; k--) if (!elems[k].isBlank()) throw new LispError("Element outside expression");
                return lispExpr;
            } else lists.car().prepend(lispExpr);
        }
        return null;
    }

    @Override
    @NotNull
    public LispItem parse(@NotNull String expr) throws LispError {
        String[] elems = expr.split("\\s+");
        if (elems.length==0) throw new LispError("Empty expression is invalid !");
        lists = ConsList.nil();
        try {
            for (int i=elems.length-1; i>=0; i--) {
                elems[i] = handleClosingParenthesis(elems[i]);
                var cons = handleOpeningParenthesis(elems[i]);
                int nbClose = cons.left();
                elems[i] = cons.right();
                if (!elems[i].isBlank()) {
                    if (lists.isEmpty()) {
                        if (elems.length==1) return parseSingleElement(elems[i]);
                        throw new LispError("Missing Parenthesis");
                    }
                    lists.car().prepend(parseSingleElement(elems[i]));
                }
                var lispExpr = handleCloseExpression(elems, i, nbClose);
                if (lispExpr!=null) return lispExpr;
            }
        } catch (LispError e) {
            throw e;
        } catch (Exception e) {
            throw new LispError("Parsing expression failed : "+expr, e);
        }
        throw new LispError("Invalid expression : "+expr);
    }

    @Override
    @NotNull
    public LispItem evaluate(@NotNull LispItem ex) throws LispError {
        try {
            return ex.eval(context);
        } catch (Exception e) {
            var exception = e;
            if (!(exception instanceof LispError || exception instanceof LispRuntimeError)) throw e;
            var cause = exception.getCause();
            while (cause instanceof LispError || cause instanceof LispRuntimeError) {
                exception = (Exception) cause;
                cause = exception.getCause();
            }
            if (exception instanceof LispError) throw (LispError) exception;
            else throw new LispError(exception.getMessage());
        }
    }
}
