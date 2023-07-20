package ZKP;

import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import commitment.Commiter;
import config.BouncyKey;
import config.Config;
import zkp.TestConstants;
import zkp.polynomial.higher.HPoly127ZKP;
import zkp.polynomial.higher.HPoly15ZKP;
import zkp.polynomial.higher.HPoly255ZKP;
import zkp.polynomial.higher.HPoly31ZKP;
import zkp.polynomial.higher.HPoly511ZKP;
import zkp.polynomial.higher.HPoly63ZKP;
import zkp.polynomial.higher.HPoly7ZKP;

/**
 * 02-1O-2022
 * 
 * @author nanwang
 *
 */
public class HigherPolynomialTest {

	private Commiter commiter;

	private int instances = 1;

	@Before
	public void init() {
		Config.getInstance().init(new BouncyKey("bn128"));
		this.commiter = Config.getInstance().getCommiter();
	}

	@Test
	public void testHPoly7ZKP() {

		int number = 3;

		for (int i = 0; i < instances; i++) {
			BigInteger x = this.commiter.rand();
			BigInteger y = x.pow(7);

			HPoly7ZKP zkp = new HPoly7ZKP(x, y, number, 2);
			assertTrue(zkp.verify());
			
			HPoly7ZKP.counter++;
		}

		System.out.println("Higher Poly 7 Prove Time:" + HPoly7ZKP.ptime / (instances - TestConstants.WARMUPS));
		System.out.println("Higher Poly 7 Verify Time:" + HPoly7ZKP.vtime / (instances - TestConstants.WARMUPS));
	}

	@Test
	public void testHPoly15ZKP() {

		int number = 4;

		for (int i = 0; i < instances; i++) {
			BigInteger x = this.commiter.rand();
			BigInteger y = x.pow(15);

			HPoly15ZKP zkp = new HPoly15ZKP(x, y, number, 2);
			assertTrue(zkp.verify());
			
			HPoly15ZKP.counter++;
		}

		System.out.println("Higher Poly 15 Prove Time:" + HPoly15ZKP.ptime / (instances - TestConstants.WARMUPS));
		System.out.println("Higher Poly 15 Verify Time:" + HPoly15ZKP.vtime / (instances - TestConstants.WARMUPS));
	}

	@Test
	public void testHPoly31ZKP() {

		int number = 5;

		for (int i = 0; i < instances; i++) {
			BigInteger x = this.commiter.rand();
			BigInteger y = x.pow(31);

			HPoly31ZKP zkp = new HPoly31ZKP(x, y, number, 2);
			assertTrue(zkp.verify());
			
			HPoly31ZKP.counter++;
		}

		System.out.println("Higher Poly 31 Prove Time:" + HPoly31ZKP.ptime / (instances - TestConstants.WARMUPS));
		System.out.println("Higher Poly 31 Verify Time:" + HPoly31ZKP.vtime / (instances - TestConstants.WARMUPS));
	}

	@Test
	public void testHPoly63ZKP() {

		int number = 6;

		for (int i = 0; i < instances; i++) {
			BigInteger x = this.commiter.rand();
			BigInteger y = x.pow(63);

			HPoly63ZKP zkp = new HPoly63ZKP(x, y, number, 2);
			assertTrue(zkp.verify());
			
			HPoly63ZKP.counter++;
		}

		System.out.println("Higher Poly 63 Prove Time:" + HPoly63ZKP.ptime / (instances - TestConstants.WARMUPS));
		System.out.println("Higher Poly 63 Verify Time:" + HPoly63ZKP.vtime / (instances - TestConstants.WARMUPS));
	}

	@Test
	public void testHPoly127ZKP() {

		int number = 7;

		for (int i = 0; i < instances; i++) {
			BigInteger x = this.commiter.rand();
			BigInteger y = x.pow(127);

			HPoly127ZKP zkp = new HPoly127ZKP(x, y, number, 2);
			assertTrue(zkp.verify());
			
			HPoly127ZKP.counter++;
		}

		System.out.println("Higher Poly 127 Prove Time:" + HPoly127ZKP.ptime / (instances - TestConstants.WARMUPS));
		System.out.println("Higher Poly 127 Verify Time:" + HPoly127ZKP.vtime / (instances - TestConstants.WARMUPS));
	}

	@Test
	public void testHPoly255ZKP() {

		int number = 8;

		for (int i = 0; i < instances; i++) {
			BigInteger x = this.commiter.rand();
			BigInteger y = x.pow(255);

			HPoly255ZKP zkp = new HPoly255ZKP(x, y, number, 2);
			assertTrue(zkp.verify());
			
			HPoly255ZKP.counter++;
		}

		System.out.println("Higher Poly 255 Prove Time:" + HPoly255ZKP.ptime / (instances - TestConstants.WARMUPS));
		System.out.println("Higher Poly 255 Verify Time:" + HPoly255ZKP.vtime / (instances - TestConstants.WARMUPS));
	}

	@Test
	public void testHPoly511ZKP() {

		int number = 9;

		for (int i = 0; i < instances; i++) {
			BigInteger x = this.commiter.rand();
			BigInteger y = x.pow(511);

			HPoly511ZKP zkp = new HPoly511ZKP(x, y, number, 3);
			assertTrue(zkp.verify());
			
			HPoly511ZKP.counter++;
		}

		System.out.println("Higher Poly 511 Prove Time:" + HPoly511ZKP.ptime / (instances - TestConstants.WARMUPS));
		System.out.println("Higher Poly 511 Verify Time:" + HPoly511ZKP.vtime / (instances - TestConstants.WARMUPS));
	}

}