package vvl.lisp;

import vvl.util.ConsList;
import vvl.util.ConsListImpl;

public class LispList extends ConsListImpl<LispItem> implements LispPair {
    public static final LispList NIL = new LispList();

    public LispList(ConsListImpl<LispItem> consList) {
        super(consList);
    }
    private LispList() {
        this(null, null);
    }

    private LispList(LispItem first, ConsList<LispItem> list) {
        super(first, list);
    }

    public LispItem carNoEval() {
        return super.car();
    }

    @Override
    public LispItem car() {
        return super.car();
    }

    @Override
    public LispList cdr() {
        var out = super.cdr();
        return out.getClass() == LispList.class ? (LispList) out : new LispList((ConsListImpl<LispItem>) out);
    }

    @Override
    public LispList prepend(LispItem e) {
        return new LispList((ConsListImpl<LispItem>) super.prepend(e));
    }

    @Override
    public LispList append(LispItem e) {
        return new LispList((ConsListImpl<LispItem>) super.append(e));
    }

    @Override
    public LispItem eval(ConsList<LispItem> items) throws LispError {
        return this;
    }

}
