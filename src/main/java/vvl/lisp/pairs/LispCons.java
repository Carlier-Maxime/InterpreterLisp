package vvl.lisp.pairs;

import org.jetbrains.annotations.NotNull;
import vvl.lisp.LispContext;
import vvl.lisp.LispError;
import vvl.lisp.LispItem;
import vvl.util.Cons;

public class LispCons extends Cons<LispItem, LispItem> implements LispPair {

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
    public LispItem eval(@NotNull LispContext context) throws LispError {
        return this;
    }

    @Override
    public LispItem car() {
        return left();
    }

    @Override
    public LispItem cdr() {
        return right();
    }
}
