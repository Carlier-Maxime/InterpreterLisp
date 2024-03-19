package vvl.lisp;

import org.jetbrains.annotations.NotNull;

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

	private final String id;
	
	public LispIdentifier(@NotNull String id) throws LispError {
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
	@NotNull
public LispItem eval(@NotNull LispContext context) throws LispError {
		return context.getValue(id);
	}
}
