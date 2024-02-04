package vvl.lisp;

import vvl.util.ConsList;
import vvl.util.ConsListImpl;

public class LispList extends ConsListImpl<LispItem> implements LispItem {
    public LispList(ConsListImpl<LispItem> consList) {
        super(consList);
    }
    public LispList() {
        this(null, null);
    }
    public LispList(LispItem first) {
        this(first, null);
    }

    private LispList(LispItem first, ConsList<LispItem> list) {
        super(first, list);
    }

    public LispItem carNoEval() {
        return super.car();
    }

    @Override
    public LispItem car() {
        try {
            var item = super.car();
            return item.getClass() == LispExpression.class ? item.eval(null) : item;
        } catch (LispError e) {
            throw new LispRuntimeError(e);
        }
    }

    @Override
    public ConsList<LispItem> cdr() {
        var out = super.cdr();
        return out.getClass() == LispList.class ? out : new LispList((ConsListImpl<LispItem>) out);
    }

    @Override
    public ConsList<LispItem> prepend(LispItem e) {
        return new LispList((ConsListImpl<LispItem>) super.prepend(e));
    }

    @Override
    public ConsList<LispItem> append(LispItem e) {
        return new LispList((ConsListImpl<LispItem>) super.append(e));
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
