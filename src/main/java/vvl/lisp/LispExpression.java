package vvl.lisp;

import org.jetbrains.annotations.NotNull;
import vvl.lisp.functions.LispFunction;
import vvl.lisp.pairs.LispList;
import vvl.util.ConsList;

/**
 * Representation of a Lisp expression (a list of LispItem).
 * 
 * @author leberre
 *
 */
public class LispExpression implements LispItem {

	private ConsList<LispItem> expression;

	public LispExpression() {
		this.expression = ConsList.nil();
	}
	
	public LispExpression(LispItem ... items ) {
		this.expression = ConsList.asList(items);
	}

	public void prepend(@NotNull LispItem item) {
		this.expression = this.expression.prepend(item);
	}
	
	public void append(@NotNull LispItem item) {
		this.expression = this.expression.append(item);
	}
	
	@NotNull
public ConsList<LispItem> values() {
		return expression;
	}

	@Override
	public String toString() {
		return expression.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof LispExpression) {
			LispExpression l = (LispExpression)o;
			return expression.equals(l.expression);
		}
		return false;
    }
    
    @Override
    public int hashCode() {
        return expression.hashCode();
    }

	@Override
	@NotNull
public LispItem eval(@NotNull LispContext context) throws LispError {
		if (values().isEmpty()) return LispList.NIL;
		var func = values().car().eval(context);
		if (func instanceof LispFunction) return ((LispFunction) func).apply(context, values().cdr());
		throw new LispError(values().car().toString()+" is not a valid operator");
	}

}
