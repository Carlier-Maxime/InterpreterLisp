package vvl.lisp.pairs;

import org.jetbrains.annotations.NotNull;
import vvl.lisp.LispContext;
import vvl.lisp.LispError;
import vvl.lisp.LispItem;
import vvl.util.ConsList;

public class LispList implements LispPair, ConsList<LispItem> {
    public static final LispList NIL = new LispList(ConsList.nil());
    private final ConsList<LispItem> list;

    public LispList(@NotNull ConsList<LispItem> consList) {
        this.list = consList;
    }

    @Override
    public LispItem car() {
        return list.car();
    }

    @Override
    @NotNull
    public LispList cdr() {
        var out = list.cdr();
        return out.getClass() == LispList.class ? (LispList) out : new LispList(out);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    @NotNull
    public LispList prepend(LispItem e) {
        return new LispList(list.prepend(e));
    }

    @Override
    @NotNull
    public LispList append(LispItem e) {
        return new LispList(list.append(e));
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    @NotNull
    public LispItem eval(@NotNull LispContext context) throws LispError {
        return this;
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
