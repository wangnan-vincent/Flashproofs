package commitment;

import java.math.BigInteger;
import java.util.List;

import config.Config;
import utils.RandGenerator;

public interface Commiter {

	public abstract Commitment commitTo(BigInteger x);

	public abstract Commitment commitTo(BigInteger x, BigInteger r);

	public abstract Commitment mulG(BigInteger x);

	public abstract Commitment mulH(BigInteger x);
	
	public abstract Commitment getG();
	
	public abstract List<Commitment> getGs();
	
	public abstract Commitment getIdentity();

	public default BigInteger rand() {
		return RandGenerator.getRandomInZp(Config.getInstance().getKey().getN());
	}
	
	public default BigInteger randBy(BigInteger q) {
		return RandGenerator.getRandomInZp(q);
	}
	
	public default BigInteger randWithBits(int nbits) {
		return RandGenerator.getRandomWithBits(nbits);
	}
}
