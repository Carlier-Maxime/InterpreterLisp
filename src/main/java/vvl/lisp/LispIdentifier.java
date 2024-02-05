package vvl.lisp;

import vvl.lisp.functions.*;
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
	private static final Map<String, LispItem> FUNCTIONS = new HashMap<>() {};
	static {
		FUNCTIONS.put("not", LogicalOperations.NOT);
		FUNCTIONS.put("and", LogicalOperations.AND);
		FUNCTIONS.put("or", LogicalOperations.OR);
		FUNCTIONS.put(">", ComparisonOperations.GREATER);
		FUNCTIONS.put(">=", ComparisonOperations.GREATER_OR_EQUALS);
		FUNCTIONS.put("<", ComparisonOperations.LESSER);
		FUNCTIONS.put("<=", ComparisonOperations.LESSER_OR_EQUALS);
		FUNCTIONS.put("=", ComparisonOperations.IS_EQUALS);
		FUNCTIONS.put("+", MathOperations.ADD);
		FUNCTIONS.put("*", MathOperations.MUL);
		FUNCTIONS.put("-", MathOperations.SUB);
		FUNCTIONS.put("/", MathOperations.DIV);
		FUNCTIONS.put("quote", LispOperations.QUOTE);
		FUNCTIONS.put("if", LispOperations.IF);
		FUNCTIONS.put("cons", LispOperations.CONS);
		FUNCTIONS.put("list", LispOperations.LIST);
		FUNCTIONS.put("car", LispOperations.CAR);
		FUNCTIONS.put("cdr", LispOperations.CDR);
		FUNCTIONS.put("nil", LispList.NIL);
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
		var func = FUNCTIONS.get(id);
		if (func==null) throw new LispError("Identifier '"+id+"' not implemented");
		return func.eval(items);
	}

}
