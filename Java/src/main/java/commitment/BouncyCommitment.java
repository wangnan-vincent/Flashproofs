package commitment;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.bouncycastle.math.ec.ECPoint;

import config.BouncyKey;
import config.Config;

public class BouncyCommitment implements Commitment, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient final BouncyKey key = (BouncyKey) Config.getInstance().getKey();
	private final ECPoint value;

	public BouncyCommitment(ECPoint value) {
		this.value = value;
	}

	@Override
	public BouncyCommitment add(Commitment other) {
		return new BouncyCommitment(this.value.add(other.getValue()));
	}

	@Override
	public BouncyCommitment sub(Commitment other) {
		return new BouncyCommitment(this.value.subtract(other.getValue()));
	}

	@Override
	public BouncyCommitment mul(BigInteger b) {
		return new BouncyCommitment(this.value.multiply(b));
	}

	@Override
	public BouncyCommitment maskMul(BigInteger other, BigInteger r) {
		return new BouncyCommitment(this.value.multiply(other).add(this.key.getH().multiply(r)));
	}

	@Override
	public List<BigInteger> getCoordList() {
		if (this.value.isInfinity()) {
			return Arrays.asList(BigInteger.ZERO, BigInteger.ZERO);
		}

		return Arrays.asList(this.value.getX().toBigInteger(), this.value.getY().toBigInteger());
	}

	@SuppressWarnings("unchecked")
	@Override
	public ECPoint getValue() {
		return this.value;
	}

	@Override
	public boolean checkValidity(BigInteger x, BigInteger r) {
		return this.key.getG().multiply(x).add(this.key.getH().multiply(r)).equals(this.value);
	}
}
