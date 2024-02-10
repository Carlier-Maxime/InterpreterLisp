package vvl.lisp.pairs;

import vvl.lisp.LispContext;
import vvl.lisp.LispError;
import vvl.lisp.LispItem;
import vvl.lisp.LispRuntimeError;
import vvl.util.ConsList;
import vvl.util.ConsListImpl;

import java.util.Objects;

public class LispParams extends LispList {
    private final LispContext context;
    private ConsList<Class<? extends LispItem>> types;
    public LispParams(LispContext context, ConsListImpl<LispItem> params) {
        this(context, params, ConsList.nil());
    }

    public LispParams(LispContext context, ConsListImpl<LispItem> params, ConsList<Class<? extends LispItem>> types) {
        super(params);
        this.context = context;
        this.types = types;
    }

    public void setTypes(ConsList<Class<? extends LispItem>> types) {
        this.types = types;
    }

    public LispItem carNoEval() {
        return super.car();
    }


    @Override
    public LispItem car() {
        try {
            var item = super.car().eval(context);
            var itemType = item.getClass();
            var expectedType = types.car();
            if (!expectedType.isAssignableFrom(itemType)) {
                var type = expectedType.toString().split("\\.Lisp")[1];
                if ("Number".equals(type)) type = type.toLowerCase();
                if ("Pair".equals(type)) type = "Cons";
                throw new LispError("Not a "+type);
            }
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
        return new LispParams(context, out, nextTypes);
    }

    @Override
    public LispParams prepend(LispItem e) {
        return new LispParams(context, super.prepend(e), types);
    }

    @Override
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
        var sb = new StringBuilder("(").append(carNoEval());
        var next = cdr();
        while (!next.isEmpty()) {
            sb.append(' ').append(next.carNoEval());
            next = next.cdr();
        }
        sb.append(')');
        return sb.toString();
    }

    public LispContext getContext() {
        return context;
    }
}
