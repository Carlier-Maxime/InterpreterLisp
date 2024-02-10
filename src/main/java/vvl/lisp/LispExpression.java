package vvl.lisp;

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

	public void prepend(LispItem item) {
		this.expression = this.expression.prepend(item);
	}
	
	public void append(LispItem item) {
		this.expression = this.expression.append(item);
	}
	
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
	public LispItem eval(ConsList<LispItem> items) throws LispError {
		if (values().isEmpty()) return LispList.NIL;
		var func = values().car().eval(null);
		if (func instanceof LispFunction) return ((LispFunction) func).apply(values().cdr());
		throw new LispError(((LispIdentifier) values().car()).getId()+" is not a valid operator");
	}

}
