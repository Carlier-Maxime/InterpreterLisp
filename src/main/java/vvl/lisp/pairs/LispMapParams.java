package vvl.lisp.pairs;

import org.jetbrains.annotations.NotNull;
import vvl.lisp.LispContext;
import vvl.lisp.LispError;
import vvl.lisp.LispItem;
import vvl.lisp.LispRuntimeError;
import vvl.util.ConsList;

import java.util.Objects;

public class LispMapParams extends LispParams {
    private ConsList<LispList> lists;
    private ConsList<LispList> current;

    public LispMapParams(LispContext context, ConsList<LispList> lists, ConsList<Class<? extends LispItem>> types) {
        this(context, lists, types, lists);
    }

    public LispMapParams(LispContext context, ConsList<LispList> lists, ConsList<Class<? extends LispItem>> types, ConsList<LispList> current) {
        super(context, LispList.NIL, types);
        this.lists = lists;
        this.current = current;
    }

    @Override
    protected @NotNull LispItem carRaw() {
        return current.car().car();
    }

    public void cdrListInPlace() {
        lists = lists.map(LispList::cdr);
        current = lists;
    }

    @Override
    @NotNull
    public LispMapParams cdr() {
        var next = current.cdr();
        if (next.isEmpty()) return new LispMapParams(getContext(), lists.map(LispList::cdr), getTypes());
        return new LispMapParams(getContext(), lists, getTypes(), current);
    }

    @Override
    @NotNull
    public LispMapParams prepend(LispItem e) {
        if (!(e instanceof LispList)) throw new LispRuntimeError("param must be LispList");
        return new LispMapParams(getContext(), lists.prepend((LispList) e).map(LispList.class::cast), getTypes());
    }

    @Override
    @NotNull
    public LispMapParams append(LispItem e) {
        if (!(e instanceof LispList)) throw new LispRuntimeError("param must be LispList");
        return new LispMapParams(getContext(), lists.append((LispList) e).map(LispList.class::cast), getTypes());
    }

    @Override
    @NotNull
    public LispItem car() {
        try {
            var item = carRaw().eval(getContext());
            checkType(item);
            return item;
        } catch (LispError e) {
            throw new LispRuntimeError(e);
        }
    }

    @Override
    @NotNull
    public LispItem carNoEval() throws LispError {
        var item = carRaw();
        checkType(item);
        return item;
    }

    @Override
    public int size() {
        return lists.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LispMapParams lispItems = (LispMapParams) o;
        return Objects.equals(lists, lispItems.lists) && Objects.equals(current, lispItems.current);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lists, current);
    }
}
