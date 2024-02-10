package vvl.lisp.pairs;

import org.jetbrains.annotations.NotNull;
import vvl.lisp.LispContext;
import vvl.lisp.LispError;
import vvl.lisp.LispItem;
import vvl.util.ConsList;
import vvl.util.ConsListImpl;

public class LispList extends ConsListImpl<LispItem> implements LispPair {
    public static final LispList NIL = new LispList();

    public LispList(@NotNull ConsListImpl<LispItem> consList) {
        super(consList);
    }
    private LispList() {
        this(null, null);
    }

    private LispList(LispItem first, ConsList<LispItem> list) {
        super(first, list);
    }

    @Override
    @NotNull
    public LispList cdr() {
        var out = super.cdr();
        return out.getClass() == LispList.class ? (LispList) out : new LispList((ConsListImpl<LispItem>) out);
    }

    @Override
    @NotNull
    public LispList prepend(LispItem e) {
        return new LispList((ConsListImpl<LispItem>) super.prepend(e));
    }

    @Override
    @NotNull
    public LispList append(LispItem e) {
        return new LispList((ConsListImpl<LispItem>) super.append(e));
    }

    @Override
    @NotNull
    public LispItem eval(@NotNull LispContext context) throws LispError {
        return this;
    }

}
