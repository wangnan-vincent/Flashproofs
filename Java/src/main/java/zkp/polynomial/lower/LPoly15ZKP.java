package zkp.polynomial.lower;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import commitment.Commitment;
import structure.VectorB;
import structure.VectorP;
import utils.HashUtils;
import zkp.PedersenZKP;
import zkp.TestConstants;

/**
 * 02-1O-2022
 * 
 * @author nanwang
 *
 */
public class LPoly15ZKP extends PedersenZKP {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final VectorP gs;

	private Commitment cy;
	private Commitment cx;
	private Commitment cm;

	private Commitment cv0;
	private Commitment cv1;

	private List<Commitment> cws = new LinkedList<>();
	private List<BigInteger> zs = new LinkedList<>();

	private BigInteger t;
	private BigInteger u;
	private BigInteger s;

	public static int counter = 0;
	public static long ptime = 0;
	public static long vtime = 0;

	private final int exponent = 15;

	public LPoly15ZKP(BigInteger x, BigInteger y, int numberOfGs) {

		long s = System.nanoTime();

		this.gs =  VectorP.from(this.commiter.getGs().subList(0, numberOfGs));

		BigInteger ry = this.commiter.rand();
		this.cy = this.commiter.commitTo(y.mod(this.n), ry);

		x = x.mod(this.n);
		BigInteger[] xpowers = new BigInteger[this.exponent - 1];
		xpowers[0] = x;
		for (int i = 1; i < this.exponent - 1; i++) {
			xpowers[i] = xpowers[i - 1].multiply(x).mod(this.n);
		}
		BigInteger x2 = xpowers[1];
		BigInteger x4 = xpowers[3];
		BigInteger x8 = xpowers[7];

		List<BigInteger> ms = new LinkedList<>();
		for (int i = 0; i < 4; i++) {
			ms.add(this.commiter.rand());
		}
		BigInteger m1 = ms.get(0);
		BigInteger m2 = ms.get(1);
		BigInteger m4 = ms.get(2);
		BigInteger m8 = ms.get(3);

		BigInteger rx = this.commiter.rand();
		this.cx = this.commiter.commitTo(x, rx);

		BigInteger rm = this.commiter.rand();
		this.cm = this.commiter.commitTo(m1, rm);

		BigInteger m2_1 = m1.pow(2).mod(this.n);
		BigInteger m2_2 = m2.pow(2).mod(this.n);
		BigInteger m2_3 = m4.pow(2).mod(this.n);
		BigInteger xm1 = m1.multiply(x).multiply(BigInteger.TWO).subtract(m2).mod(this.n);
		BigInteger xm2 = m2.multiply(x2).multiply(BigInteger.TWO).subtract(m4).mod(this.n);
		BigInteger xm3 = m4.multiply(x4).multiply(BigInteger.TWO).subtract(m8).mod(this.n);

		BigInteger rv0 = this.commiter.rand();
		this.cv0 = this.gs.mulBAndSum(VectorB.from(Arrays.asList(m2_1, m2_2, m2_3), this.n))
				.add(this.commiter.mulH(rv0));
		BigInteger rv1 = this.commiter.rand();
		this.cv1 = this.gs.mulBAndSum(VectorB.from(Arrays.asList(xm1, xm2, xm3), this.n)).add(this.commiter.mulH(rv1));

		BigInteger w0 = ms.stream().reduce(BigInteger.ONE, (m_1, m_2) -> m_1.multiply(m_2)).mod(this.n);
		BigInteger rw0 = this.commiter.rand();
		this.cws.add(this.commiter.commitTo(w0, rw0));

		Set<Integer> set = ImmutableSet.of(0, 1, 2, 3);
		List<Integer> weights = Arrays.asList(1, 2, 4, 8);

		List<Set<Integer>> combinations = Sets.combinations(set, 3).stream().collect(Collectors.toList());
		BigInteger w1 = BigInteger.ZERO;
		for (int i = 0; i < combinations.size(); i++) {
			int xidx = this.exponent
					- combinations.get(i).stream().map(idx -> weights.get(idx)).reduce(0, (s1, s2) -> s1 + s2);

			BigInteger prod = combinations.get(i).stream().map(idx -> ms.get(idx)).reduce(BigInteger.ONE,
					(b1, b2) -> b1.multiply(b2));

			w1 = w1.add(xpowers[xidx - 1].multiply(prod));
		}
		w1 = w1.mod(this.n);
		BigInteger rw1 = this.commiter.rand();
		this.cws.add(this.commiter.commitTo(w1, rw1));

		combinations = Sets.combinations(set, 2).stream().collect(Collectors.toList());
		BigInteger w2 = BigInteger.ZERO;
		for (int i = 0; i < combinations.size(); i++) {
			int xidx = this.exponent
					- combinations.get(i).stream().map(idx -> weights.get(idx)).reduce(0, (s1, s2) -> s1 + s2);

			BigInteger prod = combinations.get(i).stream().map(idx -> ms.get(idx)).reduce(BigInteger.ONE,
					(b1, b2) -> b1.multiply(b2));

			w2 = w2.add(xpowers[xidx - 1].multiply(prod));
		}
		w2 = w2.mod(this.n);
		BigInteger rw2 = this.commiter.rand();
		this.cws.add(this.commiter.commitTo(w2, rw2));

		combinations = Sets.combinations(set, 1).stream().collect(Collectors.toList());
		BigInteger w3 = BigInteger.ZERO;
		for (int i = 0; i < combinations.size(); i++) {
			int xidx = this.exponent
					- combinations.get(i).stream().map(idx -> weights.get(idx)).reduce(0, (s1, s2) -> s1 + s2);

			BigInteger prod = combinations.get(i).stream().map(idx -> ms.get(idx)).reduce(BigInteger.ONE,
					(b1, b2) -> b1.multiply(b2));

			w3 = w3.add(xpowers[xidx - 1].multiply(prod));
		}
		w3 = w3.mod(this.n);
		BigInteger rw3 = this.commiter.rand();
		this.cws.add(this.commiter.commitTo(w3, rw3));

		BigInteger challenge = HashUtils.hash(this.cws, this.cx, this.cy, this.cm, this.cv0, this.cv1).mod(this.n);
		BigInteger challenge2 = challenge.multiply(challenge).mod(this.n);
		BigInteger challenge3 = challenge2.multiply(challenge).mod(this.n);
		BigInteger challenge4 = challenge3.multiply(challenge).mod(this.n);

		this.zs.add(m1.add(challenge.multiply(x)).mod(this.n));
		this.zs.add(m2.add(challenge.multiply(x2)).mod(this.n));
		this.zs.add(m4.add(challenge.multiply(x4)).mod(this.n));
		this.zs.add(m8.add(challenge.multiply(x8)).mod(this.n));

		this.t = rm.add(challenge.multiply(rx)).mod(this.n);
		this.u = rv1.multiply(challenge).add(rv0).mod(this.n);
		this.s = ry.multiply(challenge4).add(rw3.multiply(challenge3)).add(rw2.multiply(challenge2)).add(rw1.multiply(challenge)).add(rw0)
				.mod(this.n);

		long e = System.nanoTime();

		if (counter >= TestConstants.WARMUPS) {
			ptime += (e - s);
		}
	}

	@Override
	public boolean verify() {

		long s = System.nanoTime();

		BigInteger challenge = HashUtils.hash(this.cws, this.cx, this.cy, this.cm, this.cv0, this.cv1).mod(this.n);
		BigInteger challenge2 = challenge.multiply(challenge).mod(this.n);
		BigInteger challenge3 = challenge2.multiply(challenge).mod(this.n);
		BigInteger challenge4 = challenge3.multiply(challenge).mod(this.n);

		BigInteger z1 = this.zs.get(0);
		BigInteger z2 = this.zs.get(1);
		BigInteger z4 = this.zs.get(2);
		BigInteger z8 = this.zs.get(3);

		Commitment ret1_1 = this.commiter.commitTo(z1, this.t);
		Commitment ret1_2 = this.cx.mul(challenge).add(this.cm);

		Commitment ret2_1 = this.gs
				.mulBAndSum(VectorB.from(Arrays.asList(z1.pow(2).subtract(z2.multiply(challenge)).mod(this.n),
						z2.pow(2).subtract(z4.multiply(challenge)).mod(this.n),
						z4.pow(2).subtract(z8.multiply(challenge)).mod(this.n)), this.n))
				.add(this.commiter.mulH(this.u));
		Commitment ret2_2 = this.cv1.mul(challenge).add(this.cv0);

		BigInteger zprod = this.zs.stream().reduce(BigInteger.ONE, (z_1, z_2) -> z_1.multiply(z_2)).mod(this.n);
		Commitment ret3_1 = this.commiter.commitTo(zprod, this.s);
		Commitment ret3_2 = this.cy.mul(challenge4).add(this.cws.get(3).mul(challenge3)).add(this.cws.get(2).mul(challenge2))
				.add(this.cws.get(1).mul(challenge)).add(this.cws.get(0));

		boolean b1 = ret1_1.equals(ret1_2);
		boolean b2 = ret2_1.equals(ret2_2);
		boolean b3 = ret3_1.equals(ret3_2);

		long e = System.nanoTime();

		if (counter >= TestConstants.WARMUPS) {
			vtime += (e - s);
		}

		return b1 && b2 && b3;
	}
}
