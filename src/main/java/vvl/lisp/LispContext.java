package vvl.lisp;

import org.jetbrains.annotations.NotNull;
import vvl.lisp.functions.ComparisonOperations;
import vvl.lisp.functions.LispOperations;
import vvl.lisp.functions.LogicalOperations;
import vvl.lisp.functions.MathOperations;
import vvl.lisp.pairs.LispList;

import java.util.HashMap;
import java.util.Map;

public class LispContext {
    private static final Map<String, LispItem> BUILTIN = new HashMap<>() {};
    static {
        BUILTIN.put("not", LogicalOperations.NOT);
        BUILTIN.put("and", LogicalOperations.AND);
        BUILTIN.put("or", LogicalOperations.OR);
        BUILTIN.put(">", ComparisonOperations.GREATER);
        BUILTIN.put(">=", ComparisonOperations.GREATER_OR_EQUALS);
        BUILTIN.put("<", ComparisonOperations.LESSER);
        BUILTIN.put("<=", ComparisonOperations.LESSER_OR_EQUALS);
        BUILTIN.put("=", ComparisonOperations.IS_EQUALS);
        BUILTIN.put("+", MathOperations.ADD);
        BUILTIN.put("*", MathOperations.MUL);
        BUILTIN.put("-", MathOperations.SUB);
        BUILTIN.put("/", MathOperations.DIV);
        BUILTIN.put("quote", LispOperations.QUOTE);
        BUILTIN.put("if", LispOperations.IF);
        BUILTIN.put("cons", LispOperations.CONS);
        BUILTIN.put("list", LispOperations.LIST);
        BUILTIN.put("car", LispOperations.CAR);
        BUILTIN.put("cdr", LispOperations.CDR);
        BUILTIN.put("nil", LispList.NIL);
        BUILTIN.put("define", LispOperations.DEFINE);
        BUILTIN.put("set!", LispOperations.SET);
    }

    private final Map<String, LispItem> vars;

    public LispContext() {
        vars = new HashMap<>() {};
    }

    public boolean isBuiltin(String id) {
        return BUILTIN.get(id)!=null;
    }

    public boolean isVars(String id) {
        return vars.get(id)!=null;
    }

    public static @NotNull LispError notValidIdentifier(String id) {
        return new LispError(id+" is not a valid identifier");
    }

    @NotNull
    public LispItem setVar(String id, @NotNull LispItem item, boolean replace) throws LispError {
        if (isBuiltin(id)) throw notValidIdentifier(id);
        if (isVars(id)) {
            if (!replace) throw notValidIdentifier(id);
            vars.replace(id, item);
        } else {
            if (replace) throw new LispError(id+" is undefined");
            vars.put(id, item);
        }
        return item;
    }

    @NotNull
    public LispItem getVar(String id) throws LispError {
        var item = BUILTIN.get(id);
        if (item==null) item = vars.get(id);
        if (item==null) throw notValidIdentifier(id);
        return item;
    }
}
