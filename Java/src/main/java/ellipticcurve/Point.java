package ellipticcurve;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 
 * @author nanwang
 *
 */
public class Point implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final BigInteger x;
	private final BigInteger y;

	public Point(String strX, String strY, int radix) {
		this.x = new BigInteger(strX, radix);
		this.y = new BigInteger(strY, radix);
	}

	public Point(BigInteger x, BigInteger y) {
		this.x = x;
		this.y = y;
	}

	public BigInteger getX() {
		return x;
	}

	public BigInteger getY() {
		return y;
	}

	public boolean isSymmetrical(Point p) {
		return this.x.equals(p.getX()) && this.y.add(p.getY()).equals(BigInteger.ZERO);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o instanceof Point) {
			Point p = (Point) o;

			return this.x.equals(p.getX()) && this.y.equals(p.getY());
		}

		return false;
	}

	@Override
	public String toString() {
		return "[\"" + this.x + "\",\"" + this.y + "\"]";
	}
}
