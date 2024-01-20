package vvl.lisp;

import org.jetbrains.annotations.NotNull;
import vvl.util.ConsList;

import java.math.BigInteger;

@FunctionalInterface
interface LispEvalFunction {
    LispItem apply(ConsList<LispItem> items) throws LispError;
}

class LispFunction implements LispItem {
    public static final LispFunction NOT = new LispFunction(items -> LispBoolean.valueOf(items.car()==LispBoolean.FALSE), LispBoolean.class);

    public static final LispFunction AND = new LispFunction(items -> {
        for (LispItem item: items) if (item == LispBoolean.FALSE) return LispBoolean.FALSE;
        return LispBoolean.TRUE;
    }, true, LispBoolean.class);

    public static final LispFunction OR = new LispFunction(items -> {
        for (LispItem item: items) if (item == LispBoolean.TRUE) return LispBoolean.TRUE;
        return LispBoolean.FALSE;
    }, true, LispBoolean.class);

    public static final LispFunction COMPARE = new LispFunction(items -> {
        Number a = ((LispNumber) items.car()).value();
        Number b = ((LispNumber) items.cdr().car()).value();
        Class<? extends Number> classA = a.getClass();
        Class<? extends Number> classB = b.getClass();
        if (classA == classB) {
            if (classA == BigInteger.class) return new LispNumber(((BigInteger) a).compareTo((BigInteger) b));
            else if (classA == Double.class) return new LispNumber(((Double) a).compareTo((Double) b));
            else throw new LispError("LispNumber "+classA+" not supported");
        }
        Double d; BigInteger i;
        int factor = 1;
        if (classA == BigInteger.class) {
            i = (BigInteger) a;
            if (classB != Double.class) throw new LispError("LispNumber "+classB+" not supported");
            d = (Double) b;
        } else if (classA == Double.class) {
            factor = -1;
            i = (BigInteger) b;
            if (classB != BigInteger.class) throw new LispError("LispNumber "+classB+" not supported");
            d = (Double) a;
        } else throw new LispError("LispNumber "+classA+" not supported");
        int r = i.compareTo(BigInteger.valueOf(d.longValue()));
        if (r==0) {
            double decimal = d - ((double) d.longValue());
            if (decimal>0) r=-1;
            else if (decimal<0) r=1;
        }
        return new LispNumber(r*factor);
    }, LispNumber.class, LispNumber.class);

    public static final LispFunction GREATER = new LispFunction(items -> LispBoolean.valueOf(((int) ((LispNumber) COMPARE.eval(items)).value())==1), LispNumber.class, LispNumber.class);
    public static final LispFunction LESSER = new LispFunction(items -> LispBoolean.valueOf(((int) ((LispNumber) COMPARE.eval(items)).value())==-1), LispNumber.class, LispNumber.class);
    public static final LispFunction EQUALS = new LispFunction(items -> LispBoolean.valueOf(((int) ((LispNumber) COMPARE.eval(items)).value())==0), LispNumber.class, LispNumber.class);
    public static final LispFunction GREATER_OR_EQUALS = new LispFunction(items -> LispBoolean.valueOf(((int) ((LispNumber) COMPARE.eval(items)).value())>=0), LispNumber.class, LispNumber.class);
    public static final LispFunction LESSER_OR_EQUALS = new LispFunction(items -> LispBoolean.valueOf(((int) ((LispNumber) COMPARE.eval(items)).value())<=0), LispNumber.class, LispNumber.class);

    private final LispEvalFunction function;
    private final int nbArgs;
    private final Class<? extends LispItem>[] types;
    private final boolean repeatArgs;

    @SafeVarargs
    public LispFunction(@NotNull LispEvalFunction function, Class<? extends LispItem>... types) {
        this(function, false, types);
    }

    @SafeVarargs
    public LispFunction(@NotNull LispEvalFunction function, boolean repeatArgs, Class<? extends LispItem>... types) {
        this.function = function;
        this.nbArgs = types.length;
        this.types = types;
        this.repeatArgs = repeatArgs;
    }


    @Override
    public LispItem eval(ConsList<LispItem> items) throws LispError {
        if ((items.size()!=nbArgs && !repeatArgs) || (repeatArgs && items.size()%nbArgs!=0)) throw new LispError("Invalid number of operands");
        ConsList<LispItem> tmp = items;
        int size = items.size();
        for (int i=0; i<size; i++) {
            if (tmp.car().getClass() != types[i % types.length]) throw new LispError("Invalid Type of argument at index "+i+" , expected "+types[i]+", got "+tmp.car().getClass());
            tmp = tmp.cdr();
        }
        return function.apply(items);
    }
}
