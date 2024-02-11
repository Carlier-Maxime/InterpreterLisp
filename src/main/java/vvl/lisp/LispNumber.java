package vvl.lisp;

import org.jetbrains.annotations.NotNull;
import vvl.util.Cons;

import java.math.BigInteger;
import java.util.function.DoubleUnaryOperator;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * A number (either an integer or a real number).
 * 
 * @author leberre
 * 
 */
public class LispNumber implements LispItem, Comparable<LispNumber> {
	private static final Pattern INT_PATTERN = Pattern.compile("^[+-]?\\d++$");
	private static final Pattern SCIENTIFIC_NUMBER_PATTERN = Pattern.compile("^[+-]?\\d++(\\.\\d*+)?+([eE][+-]?\\d++)?$");
	private static final Pattern RATIO_PATTERN = Pattern.compile("^[+-]?\\d++(\\.\\d*+)?+(/[+-]?\\d++(\\.\\d*+)?+)?$");
	private static final Pattern NUMBER_PATTERN = Pattern.compile("^[+-]?\\d++(\\.\\d*+)?+([/eE][+-]?\\d++(\\.\\d*+)?+)?$");
	private final Number element;
	
	public LispNumber(@NotNull Number element) {
		this.element = element;
	}
	
	public Number value() {
		return element;
	}
	
	@Override
	public String toString() {
		return element.toString();
	}

	@Override 
	public boolean equals(Object o) {
		if (o instanceof LispNumber) {
			LispNumber e = (LispNumber) o;
			return element.equals(e.element);
		}
		return false;
    }
    
    @Override
    public int hashCode() {
        return element.hashCode();
    }

	public static @NotNull LispNumber parseBigInteger(@NotNull String bigInt, int radix) throws LispError {
		try {
			return new LispNumber(new BigInteger(bigInt, radix));
		} catch (Exception e) {
			throw new LispError("Parsing BigInteger failed", e);
		}
	}

	public static @NotNull LispNumber parseBigInteger(@NotNull String bigInt) throws LispError {
		return parseBigInteger(bigInt, 10);
	}

	public static @NotNull LispNumber parseDouble(@NotNull String str) throws LispError {
		try {
			return new LispNumber(Double.parseDouble(str));
		} catch (Exception e) {
			throw new LispError("Parsing Double failed", e);
		}
	}

	public static @NotNull LispNumber parseRatio(@NotNull String ratio) throws LispError {
		try {
			String[] ratioPart = ratio.split("/");
			return new LispNumber(Double.parseDouble(ratioPart[0]) / Double.parseDouble(ratioPart[1]));
		} catch (Exception e) {
			throw new LispError("Parsing Ratio failed", e);
		}
	}

	public static @NotNull LispNumber parseNumber(@NotNull String number) throws LispError {
		if (INT_PATTERN.matcher(number).matches()) return parseBigInteger(number);
		else if (SCIENTIFIC_NUMBER_PATTERN.matcher(number).matches()) return parseDouble(number);
		else if (RATIO_PATTERN.matcher(number).matches()) return parseRatio(number);
		else throw new LispError("Parsing number failed, the number is invalid : "+number);
	}

	public static boolean isNumber(@NotNull String number) {
		return NUMBER_PATTERN.matcher(number).matches();
	}

	@Override
	@NotNull
	public LispItem eval(@NotNull LispContext context) {
		return this;
	}

	@NotNull
	private static RuntimeException classNotSupported(Class<?> clazz) {
		return new LispRuntimeError("LispNumber "+clazz+" not supported");
	}

	@NotNull
	private Cons<Integer, Cons<BigInteger, Double>> separateBigIntAndDouble(@NotNull Number a, @NotNull Number b) {
		Class<? extends Number> classA = a.getClass();
		Class<? extends Number> classB = b.getClass();
		if (classA == BigInteger.class) {
			if (classB != Double.class) throw classNotSupported(classB);
			return new Cons<>(1, new Cons<>((BigInteger) a, (Double) b));
		} else if (classA == Double.class) {
			if (classB != BigInteger.class) throw classNotSupported(classB);
			return new Cons<>(-1, new Cons<>((BigInteger) b, (Double) a));
		} else throw classNotSupported(classA);
	}

	@Override
	public int compareTo(@NotNull LispNumber o) {
		Number a = this.value();
		Number b = o.value();
		Class<? extends Number> classA = a.getClass();
		Class<? extends Number> classB = b.getClass();
		if (classA == classB) {
			if (classA == BigInteger.class) return ((BigInteger) a).compareTo((BigInteger) b);
			else if (classA == Double.class) return ((Double) a).compareTo((Double) b);
			else throw classNotSupported(classA);
		}
		var cons = separateBigIntAndDouble(a, b);
		int factor = cons.left();
		BigInteger i = cons.right().left();
		double d = cons.right().right();
		int r = i.compareTo(BigInteger.valueOf((long) d));
		if (r==0) {
			double decimal = d - (long) d;
			if (decimal>0) r=-1;
			else if (decimal<0) r=1;
		}
		return r*factor;
	}

	@NotNull
	public LispNumber add(@NotNull LispNumber number) {
		Number a = this.value();
		Number b = number.value();
		Class<? extends Number> classA = a.getClass();
		Class<? extends Number> classB = b.getClass();
		if (classA == classB) {
			if (classA == BigInteger.class) return new LispNumber(((BigInteger) a).add((BigInteger) b));
			else if (classA == Double.class) return new LispNumber((Double) a + (Double) b);
			else throw classNotSupported(classA);
		}
		var cons = separateBigIntAndDouble(a, b);
		BigInteger i = cons.right().left();
		double d = cons.right().right();
        return new LispNumber(i.doubleValue() + d);
    }

	@NotNull
	public LispNumber mul(@NotNull LispNumber number) {
		Number a = this.value();
		Number b = number.value();
		Class<? extends Number> classA = a.getClass();
		Class<? extends Number> classB = b.getClass();
		if (classA == classB) {
			if (classA == BigInteger.class) return new LispNumber(((BigInteger) a).multiply((BigInteger) b));
			else if (classA == Double.class) return new LispNumber((Double) a * (Double) b);
			else throw classNotSupported(classA);
		}
		var cons = separateBigIntAndDouble(a, b);
		BigInteger i = cons.right().left();
		double d = cons.right().right();
		return new LispNumber(i.doubleValue() * d);
	}

	@NotNull
	public LispNumber sub(LispNumber number) {
		if (number==null) return mul(new LispNumber(BigInteger.valueOf(-1)));
		Number a = this.value();
		Number b = number.value();
		Class<? extends Number> classA = a.getClass();
		Class<? extends Number> classB = b.getClass();
		if (classA == classB) {
			if (classA == BigInteger.class) return new LispNumber(((BigInteger) a).subtract((BigInteger) b));
			else if (classA == Double.class) return new LispNumber((Double) a - (Double) b);
			else throw classNotSupported(classA);
		}
		var cons = separateBigIntAndDouble(a, b);
		BigInteger i = cons.right().left();
		double d = cons.right().right();
		if (cons.left() == -1) return new LispNumber(d - i.doubleValue());
		return new LispNumber(i.doubleValue() - d);
	}

	@NotNull
	public LispNumber div(@NotNull LispNumber number) throws LispError {
		if (number.compareTo(new LispNumber(BigInteger.valueOf(0))) == 0) throw new LispError("Division by zero");
		Number a = this.value();
		Number b = number.value();
		Class<? extends Number> classA = a.getClass();
		Class<? extends Number> classB = b.getClass();
		if (classA == classB) {
			if (classA == BigInteger.class) return new LispNumber(((BigInteger) a).divide((BigInteger) b));
			else if (classA == Double.class) return new LispNumber((Double) a / (Double) b);
			else throw classNotSupported(classA);
		}
		var cons = separateBigIntAndDouble(a, b);
		BigInteger i = cons.right().left();
		double d = cons.right().right();
		if (cons.left() == -1) return new LispNumber(d / i.doubleValue());
		return new LispNumber(i.doubleValue() / d);
	}

	@NotNull
	public LispNumber unaryOperation(DoubleUnaryOperator opd, UnaryOperator<BigInteger> opi) {
		var a = value();
		if (a instanceof Double) {
			if (opd==null) throw classNotSupported(a.getClass());
			return new LispNumber(opd.applyAsDouble((Double) a));
		}
		else if (a instanceof BigInteger) {
			if (opi==null) throw classNotSupported(a.getClass());
			return new LispNumber(opi.apply((BigInteger) a));
		}
		else throw classNotSupported(a.getClass());
	}

	@NotNull
	public LispNumber abs() {
		return unaryOperation(Math::abs, BigInteger::abs);
	}

	@NotNull
	public LispNumber cbrt() {
		return unaryOperation(Math::cbrt, null);
	}

	@NotNull
	public LispNumber ceil() {
		return unaryOperation(Math::ceil, null);
	}

	@NotNull
	public LispNumber floor() {
		return unaryOperation(Math::floor, null);
	}

	@NotNull
	public LispNumber log10() {
		return unaryOperation(Math::log10, null);
	}

	@NotNull
	public LispNumber cos() {
		return unaryOperation(Math::cos, null);
	}

	@NotNull
	public LispNumber sin() {
		return unaryOperation(Math::sin, null);
	}
}