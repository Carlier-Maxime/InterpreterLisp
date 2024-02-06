package vvl.lisp;

import vvl.util.ConsList;
import vvl.util.ConsListImpl;

import java.util.Objects;

public class LispParams extends LispList {
    private ConsList<Class<? extends LispItem>> types;
    public LispParams(ConsListImpl<LispItem> params, ConsList<Class<? extends LispItem>> types) {
        super(params);
        this.types = types;
    }

    private void setTypes(ConsList<Class<? extends LispItem>> types) {
        this.types = types;
    }

    public LispItem carNoEval() {
        return super.car();
    }


    @Override
    public LispItem car() {
        try {
            var item = super.car().eval(null);
            var itemType = item.getClass();
            var expectedType = types.car();
            if (!expectedType.isAssignableFrom(itemType)) throw new LispError("Not a "+expectedType.toString().split("\\.Lisp")[1]);
            return item;
        } catch (LispError e) {
            throw new LispRuntimeError(e);
        }
    }

    @Override
    public LispParams cdr() {
        var out = super.cdr();
        var nextTypes = types.cdr();
        if (nextTypes.isEmpty()) nextTypes = types;
        if (out instanceof LispParams) {
            var params = (LispParams) out;
            params.setTypes(nextTypes);
            return params;
        }
        return new LispParams(out, nextTypes);
    }

    @Override
    public LispParams prepend(LispItem e) {
        return new LispParams(super.prepend(e), types);
    }

    @Override
    public LispParams append(LispItem e) {
        return new LispParams(super.append(e), types);
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
        var sb = new StringBuilder("(").append(carNoEval());
        var next = cdr();
        while (!next.isEmpty()) {
            sb.append(' ').append(next.carNoEval());
            next = next.cdr();
        }
        sb.append(')');
        return sb.toString();
    }
}
