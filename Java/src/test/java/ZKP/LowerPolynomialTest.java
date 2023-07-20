package ZKP;

import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import commitment.Commiter;
import config.BouncyKey;
import config.Config;
import zkp.TestConstants;
import zkp.polynomial.lower.LPoly127ZKP;
import zkp.polynomial.lower.LPoly15ZKP;
import zkp.polynomial.lower.LPoly255ZKP;
import zkp.polynomial.lower.LPoly31ZKP;
import zkp.polynomial.lower.LPoly511ZKP;
import zkp.polynomial.lower.LPoly63ZKP;
import zkp.polynomial.lower.LPoly7ZKP;

/**
 * 02-1O-2022
 * 
 * @author nanwang
 *
 */
public class LowerPolynomialTest {

	private Commiter commiter;

	private int instances = 1;

	@Before
	public void init() {
		Config.getInstance().init(new BouncyKey("bn128"));
		this.commiter = Config.getInstance().getCommiter();
	}

	@Test
	public void testLPoly7ZKP() {

		int number = 3;

		for (int i = 0; i < instances; i++) {

			BigInteger x = this.commiter.rand();
			BigInteger y = x.pow(7);

			LPoly7ZKP zkp = new LPoly7ZKP(x, y, number);
			assertTrue(zkp.verify());
			
			LPoly7ZKP.counter++;
		}

		System.out.println("Lower Poly 7 Prove Time:" + LPoly7ZKP.ptime / (instances - TestConstants.WARMUPS));
		System.out.println("Lower Poly 7 Verify Time:" + LPoly7ZKP.vtime / (instances - TestConstants.WARMUPS));
	}

	@Test
	public void testLPoly15ZKP() {

		int number = 4;

		for (int i = 0; i < instances; i++) {

			BigInteger x = this.commiter.rand();
			BigInteger y = x.pow(15);

			LPoly15ZKP zkp = new LPoly15ZKP(x, y, number);
			assertTrue(zkp.verify());
			
			LPoly15ZKP.counter++;
		}

		System.out.println("Lower Poly 15 Prove Time:" + LPoly15ZKP.ptime / (instances - TestConstants.WARMUPS));
		System.out.println("Lower Poly 15 Verify Time:" + LPoly15ZKP.vtime / (instances - TestConstants.WARMUPS));
	}

	@Test
	public void testLPoly31ZKP() {

		int number = 5;

		for (int i = 0; i < instances; i++) {

			BigInteger x = this.commiter.rand();
			BigInteger y = x.pow(31);

			LPoly31ZKP zkp = new LPoly31ZKP(x, y, number);
			assertTrue(zkp.verify());
			
			LPoly31ZKP.counter++;
		}

		System.out.println("Lower Poly 31 Prove Time:" + LPoly31ZKP.ptime / (instances - TestConstants.WARMUPS));
		System.out.println("Lower Poly 31 Verify Time:" + LPoly31ZKP.vtime / (instances - TestConstants.WARMUPS));
	}

	@Test
	public void testLPoly63ZKP() {

		int number = 6;

		for (int i = 0; i < instances; i++) {

			BigInteger x = this.commiter.rand();
			BigInteger y = x.pow(63);

			LPoly63ZKP zkp = new LPoly63ZKP(x, y, number);
			assertTrue(zkp.verify());
			
			LPoly63ZKP.counter++;
		}

		System.out.println("Lower Poly 63 Prove Time:" + LPoly63ZKP.ptime / (instances - TestConstants.WARMUPS));
		System.out.println("Lower Poly 63 Verify Time:" + LPoly63ZKP.vtime / (instances - TestConstants.WARMUPS));
	}

	@Test
	public void testLPoly127ZKP() {

		int number = 7;

		for (int i = 0; i < instances; i++) {

			BigInteger x = this.commiter.rand();
			BigInteger y = x.pow(127);

			LPoly127ZKP zkp = new LPoly127ZKP(x, y, number);
			assertTrue(zkp.verify());
			
			LPoly127ZKP.counter++;
		}

		System.out.println("Lower Poly 127 Prove Time:" + LPoly127ZKP.ptime / (instances - TestConstants.WARMUPS));
		System.out.println("Lower Poly 127 Verify Time:" + LPoly127ZKP.vtime / (instances - TestConstants.WARMUPS));
	}

	@Test
	public void testLPoly255ZKP() {

		int number = 8;

		for (int i = 0; i < instances; i++) {

			BigInteger x = this.commiter.rand();
			BigInteger y = x.pow(255);

			LPoly255ZKP zkp = new LPoly255ZKP(x, y, number);
			assertTrue(zkp.verify());
			
			LPoly255ZKP.counter++;
		}

		System.out.println("Lower Poly 255 Prove Time:" + LPoly255ZKP.ptime / (instances - TestConstants.WARMUPS));
		System.out.println("Lower Poly 255 Verify Time:" + LPoly255ZKP.vtime / (instances - TestConstants.WARMUPS));
	}

	@Test
	public void testLPoly511ZKP() {

		int number = 9;

		for (int i = 0; i < instances; i++) {

			BigInteger x = this.commiter.rand();
			BigInteger y = x.pow(511);

			LPoly511ZKP zkp = new LPoly511ZKP(x, y, number);
			assertTrue(zkp.verify());
			
			LPoly511ZKP.counter++;
		}

		System.out.println("Lower Poly 511 Prove Time:" + LPoly511ZKP.ptime / (instances - TestConstants.WARMUPS));
		System.out.println("Lower Poly 511 Verify Time:" + LPoly511ZKP.vtime / (instances - TestConstants.WARMUPS));
	}
}