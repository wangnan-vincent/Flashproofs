package config;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

public abstract class Key<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// generator g
	protected T g;
	
	protected List<T> gs;

	// generator h
	protected T h;

	// modulus
	protected BigInteger q;

	// order of the group
	protected BigInteger n;

	public T getG() {
		return g;
	}
	
	public List<T> getGs() {
		return gs;
	}

	public T getH() {
		return h;
	}

	public BigInteger getQ() {
		return q;
	}

	public BigInteger getN() {
		return n;
	}
}
