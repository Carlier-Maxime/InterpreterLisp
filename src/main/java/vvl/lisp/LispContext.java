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
        BUILTIN.put("lambda", LispOperations.LAMBDA);
        BUILTIN.put("map", LispOperations.MAP);
        BUILTIN.put("abs", MathOperations.ABS);
        BUILTIN.put("cbrt", MathOperations.CBRT);
        BUILTIN.put("ceil", MathOperations.CEIL);
        BUILTIN.put("floor", MathOperations.FLOOR);
        BUILTIN.put("log10", MathOperations.LOG10);
        BUILTIN.put("cos", MathOperations.COS);
        BUILTIN.put("sin", MathOperations.SIN);
        BUILTIN.put("rint", MathOperations.RINT);
        BUILTIN.put("round", MathOperations.ROUND);
        BUILTIN.put("signum", MathOperations.SIGNUM);
        BUILTIN.put("sqrt", MathOperations.SQRT);
    }

    private final Map<String, LispItem> vars;
    private final Map<String, LispItem> args;

    public LispContext() {
        vars = new HashMap<>() {};
        args = new HashMap<>() {};
    }

    public LispContext(LispContext context) {
        this.vars = new HashMap<>(context.vars);
        this.args = new HashMap<>(context.args);
    }

    public boolean isBuiltin(String id) {
        return BUILTIN.get(id)!=null;
    }

    public boolean isVars(String id) {
        return vars.get(id)!=null;
    }
    public boolean isArgs(String id) {
        return args.get(id)!=null;
    }

    @NotNull
    public static LispError notValidIdentifier(String id) {
        return new LispError(id+" is not a valid identifier");
    }

    @NotNull
    public static LispError undefinedError(String id) {
        return new LispError(id+" is undefined");
    }

    @NotNull
    public LispItem setVar(String id, @NotNull LispItem item, boolean replace) throws LispError {
        if (isBuiltin(id)) throw notValidIdentifier(id);
        if (isVars(id)) {
            if (!replace) throw notValidIdentifier(id);
            vars.replace(id, item);
        } else {
            if (replace) throw undefinedError(id);
            vars.put(id, item);
        }
        return item;
    }

    public void setArg(String id, @NotNull LispItem item) throws LispError {
        if (isBuiltin(id)) throw notValidIdentifier(id);
        if (isArgs(id)) args.replace(id, item);
        else args.put(id, item);
    }

    public Map<String, LispItem> getArgs() {
        return args;
    }

    @NotNull
    public LispItem getValue(String id) throws LispError {
        var item = args.get(id);
        if (item==null) {
            item = vars.get(id);
            if (item==null) {
                item = BUILTIN.get(id);
                if (item==null) throw undefinedError(id);
            }
        }
        return item;
    }
}
