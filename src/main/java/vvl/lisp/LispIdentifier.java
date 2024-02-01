package vvl.lisp;

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
	private static final Map<String, LispFunction> FUNCTIONS = new HashMap<>() {};
	static {
		FUNCTIONS.put("not", LispFunction.NOT);
		FUNCTIONS.put("and", LispFunction.AND);
		FUNCTIONS.put("or", LispFunction.OR);
		FUNCTIONS.put(">", LispFunction.GREATER);
		FUNCTIONS.put(">=", LispFunction.GREATER_OR_EQUALS);
		FUNCTIONS.put("<", LispFunction.LESSER);
		FUNCTIONS.put("<=", LispFunction.LESSER_OR_EQUALS);
		FUNCTIONS.put("=", LispFunction.IS_EQUALS);
		FUNCTIONS.put("+", LispFunction.ADD);
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
		LispItem func = FUNCTIONS.get(id);
		if (func==null) throw new LispError("Identifier '"+id+"' not implemented");
		return func.eval(items);
	}
}
