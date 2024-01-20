package vvl.lisp;

import org.jetbrains.annotations.NotNull;
import vvl.util.ConsList;

import java.math.BigInteger;
import java.util.regex.Pattern;

/**
 * A number (either an integer or a real number).
 * 
 * @author leberre
 * 
 */
public class LispNumber implements LispItem, Comparable<LispNumber> {
	private static final Pattern intPattern = Pattern.compile("^[+-]?\\d+$");
	private static final Pattern scientificNumberPattern = Pattern.compile("^[+-]?\\d+\\.?\\d*([eE][+-]?\\d+)?$");
	private static final Pattern ratioPattern = Pattern.compile("^[+-]?\\d+\\.?\\d*(/[+-]?\\d+\\.?\\d*)?$");
	private static final Pattern numberPattern = Pattern.compile("^[+-]?\\d+\\.?\\d*([/eE][+-]?\\d+\\.?\\d*)?$");
	private final Number element;
	
	public LispNumber(Number element) {
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

	public static LispNumber parseBigInteger(String bigInt, int radix) throws LispError {
		try {
			return new LispNumber(new BigInteger(bigInt, radix));
		} catch (Exception e) {
			throw new LispError("Parsing BigInteger failed", e);
		}
	}

	public static LispNumber parseBigInteger(String bigInt) throws LispError {
		return parseBigInteger(bigInt, 10);
	}

	public static LispNumber parseDouble(String str) throws LispError {
		try {
			return new LispNumber(Double.parseDouble(str));
		} catch (Exception e) {
			throw new LispError("Parsing Double failed", e);
		}
	}

	public static LispNumber parseRatio(String ratio) throws LispError {
		try {
			String[] r_part = ratio.split("/");
			return new LispNumber(Double.parseDouble(r_part[0]) / Double.parseDouble(r_part[1]));
		} catch (Exception e) {
			throw new LispError("Parsing Ratio failed", e);
		}
	}

	public static LispNumber parseNumber(String number) throws LispError {
		if (intPattern.matcher(number).matches()) return parseBigInteger(number);
		else if (scientificNumberPattern.matcher(number).matches()) return parseDouble(number);
		else if (ratioPattern.matcher(number).matches()) return parseRatio(number);
		else throw new LispError("Parsing number failed, the number is invalid : "+number);
	}

	public static boolean isNumber(String number) {
		return numberPattern.matcher(number).matches();
	}

	@Override
	public LispItem eval(ConsList<LispItem> items) {
		return this;
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
			else throw new RuntimeException(new LispError("LispNumber "+classA+" not supported"));
		}
		double d; BigInteger i;
		int factor = 1;
		if (classA == BigInteger.class) {
			i = (BigInteger) a;
			if (classB != Double.class) throw new RuntimeException(new LispError("LispNumber "+classB+" not supported"));
			d = (Double) b;
		} else if (classA == Double.class) {
			factor = -1;
			i = (BigInteger) b;
			if (classB != BigInteger.class) throw new RuntimeException(new LispError("LispNumber "+classB+" not supported"));
			d = (Double) a;
		} else throw new RuntimeException(new LispError("LispNumber "+classA+" not supported"));
		int r = i.compareTo(BigInteger.valueOf((long) d));
		if (r==0) {
			double decimal = d - (long) d;
			if (decimal>0) r=-1;
			else if (decimal<0) r=1;
		}
		return r*factor;
	}
}
