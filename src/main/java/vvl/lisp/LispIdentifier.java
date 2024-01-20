package vvl.lisp;

import vvl.util.ConsList;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Representation of an identifier (a String).
 * 
 * @author leberre
 *
 */
public class LispIdentifier implements LispItem {
	private static final String identifierRegex = "^[!$%&*/:<=>?^\\-+_~a-zA-Z][!$%&*/:<=>?^\\-+_~a-zA-Z0-9]*$";
	private static final Pattern identifierPattern = Pattern.compile(identifierRegex);
	private static final HashMap<String, LispFunction> functions = new HashMap<>() {{
		put("not", LispFunction.NOT);
		put("and", LispFunction.AND);
		put("or", LispFunction.OR);
		put(">", LispFunction.GREATER);
		put(">=", LispFunction.GREATER_OR_EQUALS);
		put("<", LispFunction.LESSER);
		put("<=", LispFunction.LESSER_OR_EQUALS);
		put("=", LispFunction.EQUALS);
	}};
	private final String id;
	
	public LispIdentifier(String id) throws LispError {
		if (!identifierPattern.matcher(id).matches()) throw new LispError("This identifier '"+id+"' not respect regex : "+identifierRegex);
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
		LispItem func = functions.get(id);
		if (func==null) throw new LispError("Identifier '"+id+"' not implemented");
		return func.eval(items);
	}
}
