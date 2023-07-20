package utils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bouncycastle.jcajce.provider.digest.Keccak;

import com.google.common.collect.Lists;

import commitment.Commitment;
import ellipticcurve.Point;

/**
 * 02-1O-2022
 * 
 * @author nanwang
 *
 */
public class HashUtils {

	private HashUtils() {
	}

	private static byte[] encoded(BigInteger... cs) {
		return encoded(Arrays.asList(cs));
	}

	private static byte[] encoded(List<BigInteger> cs) {
		byte[] buf = new byte[cs.size() * 32];
		int lastPos = 0;
		for (int i = 0; i < cs.size(); i++) {
			String binary = leftPadZero(cs.get(i).toString(2), 256);

			byte[] tmp = new byte[32];
			for (int j = 0; j < 32; j++) {
				tmp[j] = (byte) Integer.parseInt(binary.substring(j * 8, (j + 1) * 8), 2);
			}

			System.arraycopy(tmp, 0, buf, lastPos, tmp.length);
			lastPos += tmp.length;
		}

		return buf;
	}

	private static String leftPadZero(String binary, int length) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < length - binary.length(); i++) {
			sb.append("0");
		}

		return sb.append(binary).toString();
	}

	/**
	 * Creates a hash of the given array of commitments
	 */
	public static BigInteger hash(List<BigInteger> list) {
		if (list.size() > 0) {
			Keccak.DigestKeccak kecc = new Keccak.Digest256();
			return new BigInteger(1, kecc.digest(encoded(list)));
		}

		return BigInteger.ZERO;
	}

	/**
	 * Creates a hash of the given array of commitments
	 */
	public static BigInteger hash(BigInteger... bs) {
		if (bs.length > 0) {
			Keccak.DigestKeccak kecc = new Keccak.Digest256();
			return new BigInteger(1, kecc.digest(encoded(bs)));
		}

		return BigInteger.ZERO;
	}

	/**
	 * Creates a hash of the given array of commitments
	 */
	public static BigInteger hash(Point... cs) {
		if (cs.length > 0) {
			List<BigInteger> list = new LinkedList<>();
			for (Point p : cs) {
				list.add(p.getX());
				list.add(p.getY());
			}
			Keccak.DigestKeccak kecc = new Keccak.Digest256();
			return new BigInteger(1, kecc.digest(encoded(list)));

		}

		return BigInteger.ZERO;
	}

	/**
	 * Creates a hash of the given array of commitments
	 */
	public static BigInteger hash(List<Point> cs1, Point... cs2) {

		List<BigInteger> cs = Lists.newLinkedList();
		for (Point p : cs1) {
			cs.add(p.getX());
			cs.add(p.getY());
		}

		for (Point p : cs2) {
			cs.add(p.getX());
			cs.add(p.getY());
		}

		if (cs.size() > 0) {
			Keccak.DigestKeccak kecc = new Keccak.Digest256();
			return new BigInteger(1, kecc.digest(encoded(cs)));

		}

		return BigInteger.ZERO;
	}

	/**
	 * Creates a hash of the given array of commitments
	 */
	@SafeVarargs
	public static BigInteger hash(Commitment... cs) {
		List<BigInteger> list = Lists.newLinkedList();

		for (Commitment c : cs) {
			list.addAll(c.getCoordList());
		}

		if (list.size() > 0) {
			Keccak.DigestKeccak kecc = new Keccak.Digest256();
			return new BigInteger(1, kecc.digest(encoded(list)));

		}

		return BigInteger.ZERO;
	}

	/**
	 * Creates a hash of the given array of commitments
	 */
	@SafeVarargs
	public static BigInteger hash(List<Commitment> cs1, Commitment... cs2) {

		List<BigInteger> list = Lists.newLinkedList();
		for (Commitment t : cs1) {
			list.addAll(t.getCoordList());
		}

		for (Commitment t : cs2) {
			list.addAll(t.getCoordList());
		}

		if (list.size() > 0) {
			Keccak.DigestKeccak kecc = new Keccak.Digest256();
			return new BigInteger(1, kecc.digest(encoded(list)));
		}

		return BigInteger.ZERO;
	}

	/**
	 * Creates a hash of the given array of commitments
	 */
	public static BigInteger hash(List<Commitment> cs, BigInteger b) {

		List<BigInteger> list = Lists.newLinkedList();
		for (Commitment t : cs) {
			list.addAll(t.getCoordList());
		}

		list.add(b);

		if (list.size() > 0) {
			Keccak.DigestKeccak kecc = new Keccak.Digest256();
			return new BigInteger(1, kecc.digest(encoded(list)));
		}

		return BigInteger.ZERO;
	}

	/**
	 * Creates a hash of the given array of commitments
	 */
	public static BigInteger hash(List<Commitment> cs1, List<Commitment> cs2) {

		List<BigInteger> list = Lists.newLinkedList();
		for (Commitment t : cs1) {
			list.addAll(t.getCoordList());
		}

		for (Commitment t : cs2) {
			list.addAll(t.getCoordList());
		}

		if (list.size() > 0) {
			Keccak.DigestKeccak kecc = new Keccak.Digest256();
			return new BigInteger(1, kecc.digest(encoded(list)));
		}

		return BigInteger.ZERO;
	}

	public static byte[] toByteArray(int value) {
		return new byte[] { (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value };
	}
}
