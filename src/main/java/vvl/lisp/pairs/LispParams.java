package vvl.lisp.pairs;

import org.jetbrains.annotations.NotNull;
import vvl.lisp.LispContext;
import vvl.lisp.LispError;
import vvl.lisp.LispItem;
import vvl.lisp.LispRuntimeError;
import vvl.util.ConsList;

import java.util.Objects;

public class LispParams extends LispList {
    private final LispContext context;
    private ConsList<Class<? extends LispItem>> types;
    public LispParams(@NotNull LispContext context, @NotNull ConsList<LispItem> params) {
        this(context, params, ConsList.nil());
    }

    public LispParams(@NotNull LispContext context, @NotNull ConsList<LispItem> params, @NotNull ConsList<Class<? extends LispItem>> types) {
        super(params);
        this.context = context;
        this.types = types;
    }

    public void setTypes(@NotNull ConsList<Class<? extends LispItem>> types) {
        this.types = types;
    }

    protected void checkType(LispItem item) throws LispError {
        var itemType = item.getClass();
        var expectedType = types.car();
        if (!expectedType.isAssignableFrom(itemType)) {
            var type = expectedType.toString().split("\\.Lisp")[1];
            if ("Number".equals(type)) type = type.toLowerCase();
            if ("Pair".equals(type)) type = "Cons";
            if ("Identifier".equals(type)) throw LispContext.notValidIdentifier(item.toString());
            throw new LispError("Not a "+type);
        }
    }

    @NotNull
    protected LispItem carRaw() {
        return super.car();
    }

    @NotNull
    public LispItem carNoEval() throws LispError {
        var item = super.car();
        checkType(item);
        return item;
    }


    @Override
    @NotNull
    public LispItem car() {
        try {
            var item = super.car().eval(context);
            checkType(item);
            return item;
        } catch (LispError e) {
            throw new LispRuntimeError(e);
        }
    }

    @Override
    @NotNull
    public LispParams cdr() {
        var out = super.cdr();
        var nextTypes = types.cdr();
        if (nextTypes.isEmpty()) nextTypes = types;
        return new LispParams(context, out, nextTypes);
    }

    @Override
    @NotNull
    public LispParams prepend(LispItem e) {
        return new LispParams(context, super.prepend(e), types);
    }

    @Override
    @NotNull
    public LispParams append(LispItem e) {
        return new LispParams(context, super.append(e), types);
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        var params = (LispParams) o;
        return types.equals(params.types);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), types);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder("(").append(isEmpty() ? "" : carRaw());
        var next = cdr();
        while (!next.isEmpty()) {
            sb.append(' ').append(next.carRaw());
            next = next.cdr();
        }
        sb.append(')');
        return sb.toString();
    }

    @NotNull
    public LispContext getContext() {
        return context;
    }

    public ConsList<Class<? extends LispItem>> getTypes() {
        return types;
    }
}
