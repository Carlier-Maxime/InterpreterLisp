package vvl.lisp;

import java.util.regex.Pattern;

/**
 * Representation of an identifier (a String).
 * 
 * @author leberre
 *
 */
public class LispIdentifier implements LispItem {
	private static final String identifierRegex = "^[!$%&*/:<=>?^\\-_~a-zA-Z][!$%&*/:<=>?^\\-_~a-zA-Z0-9]*$";
	private static final Pattern identifierPattern = Pattern.compile(identifierRegex);
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
		if (o==null) {
			return false;
		}
		return id.equals(o.toString());
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }    
}
