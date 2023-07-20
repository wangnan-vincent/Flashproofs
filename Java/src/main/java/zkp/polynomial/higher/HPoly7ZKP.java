package zkp.polynomial.higher;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;

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
public class HPoly7ZKP extends PedersenZKP {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final VectorP gs;
	private final VectorP gl;

	private Commitment cy;
	private Commitment cx;
	private Commitment cm;
	private Commitment cv0;
	private Commitment cv1;

	private List<Commitment> cws = new LinkedList<>();
	private List<Commitment> cls = new LinkedList<>();
	private List<BigInteger> zs = new LinkedList<>();

	private List<BigInteger> fs = new LinkedList<>();
	private BigInteger t;
	private BigInteger u;
	private BigInteger s;
	private BigInteger q;

	public static int counter = 0;
	public static long ptime = 0;
	public static long vtime = 0;

	public HPoly7ZKP(BigInteger x, BigInteger y, int numOfGs, int numOfGl) {

		long s = System.nanoTime();

		this.gs = VectorP.from(this.commiter.getGs().subList(0, numOfGs));
		this.gl = VectorP.from(this.commiter.getGs().subList(numOfGs, numOfGs + numOfGl));

		BigInteger ry = this.commiter.rand();
		this.cy = this.commiter.commitTo(y.mod(this.n), ry);

		x = x.mod(this.n);
		BigInteger x2 = x.pow(2).mod(this.n);
		BigInteger x3 = x2.multiply(x).mod(this.n);
		BigInteger x4 = x3.multiply(x).mod(this.n);
		BigInteger x5 = x4.multiply(x).mod(this.n);
		BigInteger x6 = x5.multiply(x).mod(this.n);

		List<BigInteger> ms = new LinkedList<>();
		for (int i = 0; i < 3; i++) {
			ms.add(this.commiter.rand());
		}
		BigInteger m1 = ms.get(0);
		BigInteger m2 = ms.get(1);
		BigInteger m4 = ms.get(2);

		BigInteger rx = this.commiter.rand();
		this.cx = this.commiter.commitTo(x, rx);

		BigInteger rm = this.commiter.rand();
		this.cm = this.commiter.commitTo(m1, rm);

		BigInteger m2_1 = m1.pow(2).mod(this.n);
		BigInteger m2_2 = m2.pow(2).mod(this.n);
		BigInteger xm1 = m1.multiply(x).multiply(BigInteger.TWO).subtract(m2).mod(this.n);
		BigInteger xm2 = m2.multiply(x2).multiply(BigInteger.TWO).subtract(m4).mod(this.n);

		BigInteger rv0 = this.commiter.rand();
		this.cv0 = this.gs.mulBAndSum(VectorB.from(Arrays.asList(m2_1, m2_2), this.n)).add(this.commiter.mulH(rv0));
		BigInteger rv1 = this.commiter.rand();
		this.cv1 = this.gs.mulBAndSum(VectorB.from(Arrays.asList(xm1, xm2), this.n)).add(this.commiter.mulH(rv1));

		List<BigInteger> ls = Arrays.asList(this.commiter.rand(), this.commiter.rand());
		List<BigInteger> rls = Arrays.asList(this.commiter.rand(), this.commiter.rand());
		for (int i = 0; i < ls.size(); i++) {
			this.cls.add(this.commiter.commitTo(ls.get(i).negate(), rls.get(i)));
		}

		BigInteger w0 = ms.stream().reduce(BigInteger.ONE, (m_1, m_2) -> m_1.multiply(m_2)).mod(this.n);
		BigInteger rw0 = this.commiter.rand();
		BigInteger w1 = m1.multiply(m2).multiply(x4).add(m2.multiply(m4).multiply(x)).add(m1.multiply(m4).multiply(x2))
				.mod(this.n);

		this.cws.add(this.gl.mulBAndSum(VectorB.from(Arrays.asList(w0.add(ls.get(0)), w1.add(ls.get(1))), this.n))
				.add(this.commiter.mulH(rw0)));

		BigInteger w2 = m1.multiply(x6).add(m2.multiply(x5)).add(m4.multiply(x3)).mod(this.n);

		BigInteger rw1 = this.commiter.rand();
		this.cws.add(this.gl.mulBAndSum(VectorB.from(Arrays.asList(BigInteger.ZERO, w2), this.n))
				.add(this.commiter.mulH(rw1)));

		List<Commitment> cs = Lists.newLinkedList();
		cs.addAll(this.cws);
		cs.addAll(this.cls);

		BigInteger challenge = HashUtils.hash(cs, this.cx, this.cy, this.cm, this.cv0, this.cv1).mod(this.n);
		BigInteger challenge3 = challenge.pow(3).mod(this.n);

		this.zs.add(m1.add(challenge.multiply(x)).mod(this.n));
		this.zs.add(m2.add(challenge.multiply(x2)).mod(this.n));
		this.zs.add(m4.add(challenge.multiply(x4)).mod(this.n));

		this.fs.add(w0.add(ls.get(0)).mod(this.n));
		this.fs.add(w2.multiply(challenge).add(w1).add(ls.get(1)).mod(this.n));

		this.t = challenge.multiply(rx).add(rm).mod(this.n);
		this.u = rv1.multiply(challenge).add(rv0).mod(this.n);
		this.s = rw1.multiply(challenge).add(rw0).mod(this.n);
		this.q = ry.multiply(challenge3).add(rls.get(1).multiply(challenge)).add(rls.get(0)).mod(this.n);

		long e = System.nanoTime();

		if (counter >= TestConstants.WARMUPS) {
			ptime += (e - s);
		}
	}

	@Override
	public boolean verify() {

		long s = System.nanoTime();

		List<Commitment> cs = Lists.newLinkedList();
		cs.addAll(this.cws);
		cs.addAll(this.cls);

		BigInteger challenge = HashUtils.hash(cs, this.cx, this.cy, this.cm, this.cv0, this.cv1).mod(this.n);
		BigInteger challenge3 = challenge.pow(3).mod(this.n);

		BigInteger z1 = this.zs.get(0);
		BigInteger z2 = this.zs.get(1);
		BigInteger z4 = this.zs.get(2);

		Commitment ret1_1 = this.commiter.commitTo(z1, this.t);
		Commitment ret1_2 = this.cx.mul(challenge).add(this.cm);

		Commitment ret2_1 = this.gs
				.mulBAndSum(VectorB.from(Arrays.asList(z1.pow(2).subtract(z2.multiply(challenge)).mod(this.n),
						z2.pow(2).subtract(z4.multiply(challenge)).mod(this.n)), this.n))
				.add(this.commiter.mulH(this.u));
		Commitment ret2_2 = this.cv1.mul(challenge).add(this.cv0);

		Commitment ret3_1 = this.gl.mulBAndSum(VectorB.from(this.fs, this.n)).add(this.commiter.mulH(this.s));
		Commitment ret3_2 = this.cws.get(1).mul(challenge).add(this.cws.get(0));

		BigInteger zprod = this.zs.stream().reduce(BigInteger.ONE, (z_1, z_2) -> z_1.multiply(z_2))
				.subtract(this.fs.get(1).multiply(challenge).add(this.fs.get(0))).mod(this.n);
		Commitment ret4_1 = this.commiter.commitTo(zprod, this.q);
		Commitment ret4_2 = this.cy.mul(challenge3).add(this.cls.get(1).mul(challenge)).add(this.cls.get(0));

		boolean b1 = ret1_1.equals(ret1_2);
		boolean b2 = ret2_1.equals(ret2_2);
		boolean b3 = ret3_1.equals(ret3_2);
		boolean b4 = ret4_1.equals(ret4_2);

		long e = System.nanoTime();

		if (counter >= TestConstants.WARMUPS) {
			vtime += (e - s);
		}

		return b1 && b2 && b3 && b4;
	}
}
