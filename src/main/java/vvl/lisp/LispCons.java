package vvl.lisp;

import vvl.util.Cons;
import vvl.util.ConsList;

public class LispCons extends Cons<LispItem, LispItem> implements LispItem {

    /**
     * Generic constructor for the cons data structure.
     *
     * @param left  the left hand side.
     * @param right the right hand side.
     */
    public LispCons(LispItem left, LispItem right) {
        super(left, right);
    }

    @Override
    public LispItem eval(ConsList<LispItem> items) throws LispError {
        return this;
    }

    @Override
    public Class<? extends LispItem> outputType(ConsList<LispItem> items) {
        return this.getClass();
    }
}
