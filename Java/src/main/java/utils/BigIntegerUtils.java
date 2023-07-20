package utils;

import java.math.BigInteger;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 08-12-2020
 * 
 * @author nanwang
 *
 */
public class BigIntegerUtils {

	private BigIntegerUtils() {
	}

	public static BigInteger sum(List<BigInteger> list) {
		return list.stream().reduce(BigInteger.ZERO, (b1, b2) -> b1.add(b2));
	}

	public static List<BigInteger> decompose(BigInteger b, BigInteger max, BigInteger min) {
		if (b.compareTo(max) <= 0 && b.compareTo(min) >= 0) {
			return Lists.newArrayList(b);
		}

		List<BigInteger> list = Lists.newLinkedList();
		if (b.compareTo(max) > 0) {
			while (b.compareTo(max) > 0) {
				list.add(max);
				b = b.subtract(max);
			}
			list.add(b);
		} else if (b.compareTo(min) < 0) {
			while (b.compareTo(min) < 0) {
				list.add(min);
				b = b.subtract(min);
			}
			list.add(b);
		}

		return list;
	}

	public static String toString(List<BigInteger> list) {
		String str = "[";
		for (int i = 0; i < list.size() - 1; i++) {
			str += "\"" + list.get(i) + "\",";
		}
		str += "\"" + list.get(list.size() - 1) + "\"]";

		return str;
	}
}
