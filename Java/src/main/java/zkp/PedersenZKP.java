package zkp;

import commitment.Commiter;
import config.Config;

/**
 * 02-1O-2022
 * 
 * @author nanwang
 *
 */
public abstract class PedersenZKP extends ZKP {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final Commiter commiter = Config.getInstance().getCommiter();
	
	
}
