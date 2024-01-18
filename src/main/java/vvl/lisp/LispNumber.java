package vvl.lisp;

import java.math.BigInteger;
import java.util.regex.Pattern;

/**
 * A number (either an integer or a real number).
 * 
 * @author leberre
 * 
 */
public class LispNumber implements LispItem {
	private static final Pattern intPattern = Pattern.compile("^[+-]?\\d+$");
	private static final Pattern scientificNumberPattern = Pattern.compile("^[+-]?\\d+\\.?\\d*([eE][+-]?\\d+)?$");
	private static final Pattern ratioPattern = Pattern.compile("^[+-]?\\d+\\.?\\d*(/[+-]?\\d+\\.?\\d*)?$");
	private static final Pattern numberPattern = Pattern.compile("^[+-]?\\d+\\.?\\d*([/eE][+-]?\\d+\\.?\\d*)?$");
	private Number element;
	
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
}
