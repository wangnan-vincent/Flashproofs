package commitment;

import java.math.BigInteger;
import java.util.List;

public interface Commitment {

	public Commitment add(Commitment t);

	public Commitment sub(Commitment t);

	public Commitment mul(BigInteger b);

	public Commitment maskMul(BigInteger b, BigInteger r);

	public List<BigInteger> getCoordList();

	public <T> T getValue();

	public boolean checkValidity(BigInteger x, BigInteger r);

	public default boolean equals(Commitment other) {
		if (this == other) {
			return true;
		}

		return this.getValue().equals(other.getValue());
	}
}
