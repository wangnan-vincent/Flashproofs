package utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * 02-1O-2022
 * 
 * @author nanwang
 *
 */
public class RandGenerator {

	private static final Random RANDOM = new SecureRandom();

	private RandGenerator() {
	}

	public static BigInteger getRandomInZp(BigInteger p) {
		BigInteger r = new BigInteger(p.bitLength(), RANDOM);

		if (!generateValueInZp(r, p)) {
			r = r.mod(p);
		}

		return r;
	}

	public static BigInteger getRandomWithBits(int nbits) {
		return new BigInteger(nbits, new Random());
	}

	public static boolean generateValueInZp(BigInteger b, BigInteger p) {
		return (b.compareTo(p) < 0 && b.compareTo(BigInteger.ZERO) >= 0);
	}
}
