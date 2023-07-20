package ZKP;

import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import commitment.Commiter;
import config.BouncyKey;
import config.Config;
import zkp.range.RangeZKPK2;
import zkp.range.RangeZKPK3;
import zkp.range.RangeZKPK4;

/**
 * 02-1O-2022
 * 
 * @author nanwang
 *
 */
public class RangeZKPTest {

	private Commiter commiter;

	private int instances = 1;
	private int warmups = 0;

	@Before
	public void init() {
		Config.getInstance().init(new BouncyKey("bn128"));
		this.commiter = Config.getInstance().getCommiter();
	}

	@Test
	public void testBDRangeZKPK4() {

		int nbits = 64;
		int L = 16;

		for (int i = 0; i < instances; i++) {
			BigInteger x = this.commiter.randWithBits(nbits);

			RangeZKPK4 zkp = new RangeZKPK4(x, nbits, L);

			assertTrue(zkp.verify());

			RangeZKPK4.counter++;
		}

		System.out.println("Range K4 Prove Time:" + RangeZKPK4.ptime / (instances - warmups));
		System.out.println("Range K4 Verify Time:" + RangeZKPK4.vtime / (instances - warmups));
	}

	@Test
	public void testBDRangeZKPK3() {

		int nbits = 32;

		int L = 11;

		for (int i = 0; i < instances; i++) {
			BigInteger x = this.commiter.randWithBits(nbits);

			RangeZKPK3 zkp = new RangeZKPK3(x, nbits, L);

			assertTrue(zkp.verify());

			RangeZKPK3.counter++;
		}

		System.out.println("Range K3 Prove Time:" + RangeZKPK3.ptime / (instances - warmups));
		System.out.println("Range K3 Verify Time:" + RangeZKPK3.vtime / (instances - warmups));
	}

	@Test
	public void testBDRangeZKPK2() {

		int nbits = 8;
		int L = 4;

		for (int i = 0; i < instances; i++) {
			BigInteger x = this.commiter.randWithBits(nbits);

			RangeZKPK2 zkp = new RangeZKPK2(x, nbits, L);

			assertTrue(zkp.verify());

			RangeZKPK2.counter++;
		}

		System.out.println("Range K2 Prove Time:" + RangeZKPK2.ptime / (instances - warmups));
		System.out.println("Range K2 Verify Time:" + RangeZKPK2.vtime / (instances - warmups));
	}
}