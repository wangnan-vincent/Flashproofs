package commitment;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import config.BouncyKey;
import config.Config;

public class BouncyCommiter implements Commiter, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient final BouncyKey key = (BouncyKey) Config.getInstance().getKey();

	public BouncyCommiter() {
	}

	@Override
	public BouncyCommitment commitTo(BigInteger x) {
		return this.commitTo(x, this.rand());
	}

	@Override
	public BouncyCommitment commitTo(BigInteger x, BigInteger r) {
		return new BouncyCommitment(this.key.getG().multiply(x).add(this.key.getH().multiply(r)));
	}

	@Override
	public BouncyCommitment mulG(BigInteger x) {
		return new BouncyCommitment(this.key.getG().multiply(x));
	}

	@Override
	public BouncyCommitment mulH(BigInteger x) {
		return new BouncyCommitment(this.key.getH().multiply(x));
	}

	@Override
	public Commitment getIdentity() {
		return new BouncyCommitment(this.key.getG().multiply(BigInteger.ZERO));
	}

	@Override
	public Commitment getG() {
		return new BouncyCommitment(this.key.getG());
	}

	@Override
	public List<Commitment> getGs() {
		return this.key.getGs().stream().map(g -> new BouncyCommitment(g)).collect(Collectors.toList());
	}
}
