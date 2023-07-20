package zkp;

import java.io.Serializable;
import java.math.BigInteger;

import config.Config;
import utils.SerializationUtils;

/**
 * 02-1O-2022
 * 
 * @author nanwang
 *
 */
public abstract class ZKP implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final BigInteger n = Config.getInstance().getKey().getN();

	public abstract boolean verify();

	public byte[] toByteArray() {
		return SerializationUtils.toByteArray(this);
	}
}
