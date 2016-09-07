import java.math.BigInteger;


public class BinaryToLong {
	public static void main(String... args) {
	    String s = "0000000011111111000000000000000000000000000000001111111100000000";
	    long l = parseLong(s, 2);
	    System.out.println(s +" => " + l);
	    System.out.print(Long.numberOfLeadingZeros(460039L));
	    System.out.print(Long.toBinaryString(460039L));
	    System.out.print(Long.numberOfTrailingZeros(460039L));
	}

	private static long parseLong(String s, int base) {
	    return new BigInteger(s, base).longValue();
	}
}
