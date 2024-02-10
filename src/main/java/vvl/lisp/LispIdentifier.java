package vvl.lisp;

import vvl.lisp.functions.*;
import vvl.lisp.pairs.LispList;
import vvl.util.ConsList;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Representation of an identifier (a String).
 * 
 * @author leberre
 *
 */
public class LispIdentifier implements LispItem {
	private static final String IDENTIFIER_REGEX = "^[!$%&*/:<=>?^\\-+_~a-zA-Z][!$%&*/:<=>?^\\-+_~a-zA-Z0-9]*$";
	private static final Pattern IDENTIFIER_PATTERN = Pattern.compile(IDENTIFIER_REGEX);
	private static final Map<String, LispItem> BUILTIN = new HashMap<>() {};
	private static final Map<String, LispItem> VARS = new HashMap<>() {};
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

	private final String id;
	
	public LispIdentifier(String id) throws LispError {
		if (!IDENTIFIER_PATTERN.matcher(id).matches()) throw new LispError("This identifier '"+id+"' not respect regex : "+ IDENTIFIER_REGEX);
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id;
	}
	
	@Override 
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o==null || getClass() != o.getClass()) return false;
		return id.equals(o.toString());
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }

	@Override
	public LispItem eval(ConsList<LispItem> items) throws LispError {
		var func = BUILTIN.get(id);
		if (func==null) func = VARS.get(id);
		if (func==null) throw new LispError("Identifier '"+id+"' not implemented");
		return func;
	}

	public String getId() {
		return id;
	}

	public boolean isBuiltin() {
		return BUILTIN.get(id)!=null;
	}

	public boolean isVars() {
		return VARS.get(id)!=null;
	}

	public static LispError notValidIdentifier(String id) {
		return new LispError(id+" is not a valid identifier");
	}

	public LispItem setVar(LispItem item, boolean replace) throws LispError {
		if (isBuiltin()) throw notValidIdentifier(id);
		if (isVars()) {
			if (!replace) throw notValidIdentifier(id);
			VARS.replace(id, item);
		} else {
			if (replace) throw new LispError(id+" is undefined");
			VARS.put(id, item);
		}
		return item;
	}
}
