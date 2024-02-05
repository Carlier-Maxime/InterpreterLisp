package vvl.lisp;

import vvl.util.ConsList;
import vvl.util.ConsListImpl;

public class LispParams extends LispList {
    private ConsList<Class<? extends LispItem>> types;
    public LispParams(ConsListImpl<LispItem> params, ConsList<Class<? extends LispItem>> types) {
        super(params);
        this.types = types;
    }

    private void setTypes(ConsList<Class<? extends LispItem>> types) {
        this.types = types;
    }

    @Override
    public LispItem car() {
        try {
            var item = super.car().eval(null);
            var itemType = item.getClass();
            var expectedType = types.car();
            if (!expectedType.isAssignableFrom(itemType)) throw new LispError("Invalid Type of argument, expected "+expectedType+", got "+itemType);
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
}
