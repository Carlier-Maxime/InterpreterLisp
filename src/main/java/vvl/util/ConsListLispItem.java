package vvl.util;

import vvl.lisp.LispError;
import vvl.lisp.LispExpression;
import vvl.lisp.LispItem;
import vvl.lisp.LispRuntimeError;

public class ConsListLispItem extends ConsListImpl<LispItem> {
    public ConsListLispItem(ConsListImpl<LispItem> consList) {
        super(consList);
    }
    public ConsListLispItem() {
        this(null, null);
    }
    public ConsListLispItem(LispItem first) {
        this(first, null);
    }

    private ConsListLispItem(LispItem first, ConsList<LispItem> list) {
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
        return out.getClass() == ConsListLispItem.class ? out : new ConsListLispItem((ConsListImpl<LispItem>) out);
    }

    @Override
    public ConsList<LispItem> prepend(LispItem e) {
        return new ConsListLispItem((ConsListImpl<LispItem>) super.prepend(e));
    }

    @Override
    public ConsList<LispItem> append(LispItem e) {
        return new ConsListLispItem((ConsListImpl<LispItem>) super.append(e));
    }
}
