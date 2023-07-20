package zkp.range;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Streams;

import commitment.Commitment;
import structure.VectorB;
import structure.VectorP;
import utils.BigIntegerUtils;
import utils.HashUtils;
import zkp.PedersenZKP;
import zkp.TestConstants;

/**
 * 02-1O-2022
 * 
 * @author nanwang
 *
 */
public class RangeZKPK3 extends PedersenZKP {

	private static final long serialVersionUID = 1L;

	private final Commitment cy;
	private final List<Commitment> css = new LinkedList<>();
	private final List<BigInteger> vs = new LinkedList<>();

	private final List<Commitment> cts = new LinkedList<>();
	private final List<Commitment> cqs = new LinkedList<>();

	private final BigInteger u;
	private final BigInteger epsilon;

	private final VectorP gs;
	private final int nbits;
	private final int K;
	private final int L;
	private final BigInteger TWO = BigInteger.TWO;

	public static int counter = 0;
	public static long ptime = 0;
	public static long vtime = 0;

	public RangeZKPK3(BigInteger y, int nbits, int L) {

		this.nbits = nbits;
		this.K = 3;
		this.L = L;
		this.gs = VectorP.from(this.commiter.getGs().subList(0, L));

		List<BigInteger> TWOS = Lists.newLinkedList();
		for (int i = 0; i < this.nbits; i++) {
			BigInteger value = TWO.pow(i);
			TWOS.add(value);
		}

		List<List<BigInteger>> bs = Lists.newLinkedList();
		List<List<BigInteger>> bsr = Lists.newLinkedList();

		List<BigInteger> rvs = Lists.newLinkedList();
		for (int i = 0; i < this.L; i++) {
			bs.add(new LinkedList<>());
			bsr.add(new LinkedList<>());
			rvs.add(this.commiter.rand());
		}

		List<List<BigInteger>> ds = Lists.newLinkedList();
		Set<Integer> set = new HashSet<>();
		for (int i = 0; i < this.K; i++) {
			ds.add(new LinkedList<>());
			set.add(i);
		}

		List<BigInteger> rts = Lists.newLinkedList();
		List<BigInteger> rws = Lists.newLinkedList();
		List<BigInteger> rss = Lists.newLinkedList();
		List<BigInteger> challenges = Lists.newLinkedList();
		List<BigInteger> doublechallenges = Lists.newLinkedList();

		long s = System.nanoTime();

		int LK = this.L * this.K;
		for (int i = 0; i < LK; i++) {
			BigInteger b = y.testBit(i) ? BigInteger.ONE : BigInteger.ZERO;
			int idx = i / this.K;
			int mod = i % this.K;
			if (i < this.nbits) {
				BigInteger twopowers = TWOS.get(i);
				BigInteger w = b.multiply(twopowers);
				bs.get(idx).add(w);
				bsr.get(idx).add(twopowers.subtract(w));
				ds.get(mod).add(w);
			} else {
				bs.get(idx).add(BigInteger.ZERO);
				bsr.get(idx).add(BigInteger.ZERO);
				ds.get(mod).add(BigInteger.ZERO);
			}
		}

		List<List<Integer>> combinations = Arrays.asList(Arrays.asList(0, 2), Arrays.asList(1, 2));

		for (int i = 0; i < combinations.size(); i++) {
			List<Integer> l = combinations.get(i);
			int idx1 = l.get(0);
			int idx2 = l.get(1);

			List<BigInteger> ts = Lists.newLinkedList();
			for (int j = 0; j < this.L; j++) {
				BigInteger t = BigInteger.ZERO;
				t = t.add(bs.get(j).get(idx1).multiply(bsr.get(j).get(idx2)));
				t = t.add(bs.get(j).get(idx2).multiply(bsr.get(j).get(idx1)));
				t = (t.compareTo(this.n) < 0 && t.compareTo(BigInteger.ZERO) >= 0) ? t : t.mod(this.n);
				ts.add(t);
			}
			BigInteger rt = this.commiter.rand();
			rts.add(rt);
			this.cts.add(this.gs.mulBAndSum(VectorB.from(ts, this.n)).add(this.commiter.mulH(rt)));
		}

		List<List<BigInteger>> ls = Lists.newLinkedList();
		for (int i = 0; i < this.K; i++) {
			List<BigInteger> ts = Lists.newLinkedList();
			for (int j = 0; j < this.L; j++) {
				BigInteger t = rvs.get(j).multiply(bsr.get(j).get(i).subtract(bs.get(j).get(i)));
				t = (t.compareTo(this.n) < 0 && t.compareTo(BigInteger.ZERO) >= 0) ? t : t.mod(this.n);
				ts.add(t);
			}
			ls.add(ts);

			BigInteger rw = this.commiter.rand();
			rws.add(rw);
			this.css.add(this.commiter
					.commitTo(ds.get(i).stream().reduce(BigInteger.ZERO, (d1, d2) -> d1.add(d2)).mod(this.n), rw));
		}

		for (int i = 0; i < this.K; i++) {
			BigInteger rs = this.commiter.rand();
			rss.add(rs);
			this.cqs.add(this.gs.mulBAndSum(VectorB.from(ls.get(i), this.n)).add(this.commiter.mulH(rs)));
		}

		this.cy = this.css.stream().reduce(this.commiter.getIdentity(), (c1, c2) -> c1.add(c2));

		BigInteger constant = rvs.stream().reduce(BigInteger.ZERO, (r1, r2) -> r1.add(r2)).mod(this.n);
		BigInteger rw = this.commiter.rand();
		rws.add(rw);
		this.css.add(this.commiter.mulG(constant).add(this.commiter.mulH(rw)));

		List<BigInteger> ts = Lists.newLinkedList();
		for (int i = 0; i < this.L; i++) {
			BigInteger t = rvs.get(i).pow(2).negate();
			t = t.add(bs.get(i).get(0).multiply(bsr.get(i).get(1)));
			t = t.add(bs.get(i).get(1).multiply(bsr.get(i).get(0)));
			t = (t.compareTo(this.n) < 0 && t.compareTo(BigInteger.ZERO) >= 0) ? t : t.mod(this.n);
			ts.add(t);
		}
		BigInteger rs = this.commiter.rand();
		rss.add(rs);
		this.cqs.add(this.gs.mulBAndSum(VectorB.from(ts, this.n)).add(this.commiter.mulH(rs)));

		List<Commitment> cs = new LinkedList<>();
		cs.addAll(this.cts);
		cs.addAll(this.css);
		cs.addAll(this.cqs);

		BigInteger challenge = HashUtils.hash(cs).mod(this.n);
		BigInteger challenge3 = challenge.modPow(BigInteger.valueOf(3), this.n);
		BigInteger challenge4 = challenge3.multiply(challenge).mod(this.n);
		BigInteger challenge5 = challenge4.multiply(challenge).mod(this.n);
		BigInteger challengeN1 = challenge.modInverse(this.n);

		challenges.add(challengeN1);
		challenges.add(challenge);
		challenges.add(challenge4);

		doublechallenges.add(challenge3);
		doublechallenges.add(challenge5);

		for (int i = 0; i < this.L; i++) {
			BigInteger v = BigInteger.ZERO;
			for (int j = 0; j < this.K; j++) {
				v = v.add(bs.get(i).get(j).multiply(challenges.get(j)));
			}
			v = v.add(rvs.get(i));
			v = (v.compareTo(this.n) < 0 && v.compareTo(BigInteger.ZERO) >= 0) ? v : v.mod(this.n);
			this.vs.add(v);
		}

		BigInteger localU = BigInteger.ZERO;
		BigInteger localS = BigInteger.ZERO;
		for (int i = 0; i < combinations.size(); i++) {
			localU = localU.add(rts.get(i).multiply(doublechallenges.get(i)));
		}
		for (int i = 0; i < this.K; i++) {
			BigInteger c = challenges.get(i);
			localU = localU.add(rss.get(i).multiply(c));
			localS = localS.add(rws.get(i).multiply(c));
		}
		this.u = localU.add(rss.get(this.K)).mod(this.n);
		this.epsilon = localS.add(rws.get(this.K)).mod(this.n);

		long e = System.nanoTime();

		if (counter >= TestConstants.WARMUPS) {
			ptime += (e - s);
		}

		// string();
	}

	public void string() {

		List<String> total = new LinkedList<>();

		String cyStr = BigIntegerUtils.toString(this.cy.getCoordList());
		total.add(cyStr);

		String ctsStr = "[" + String.join(",",
				this.cts.stream().map(c -> BigIntegerUtils.toString(c.getCoordList())).collect(Collectors.toList()))
				+ "]";
		total.add(ctsStr);

		String cssStr = "[" + String.join(",",
				this.css.stream().map(c -> BigIntegerUtils.toString(c.getCoordList())).collect(Collectors.toList()))
				+ "]";
		total.add(cssStr);

		String cqstr = "[" + String.join(",",
				this.cqs.stream().map(c -> BigIntegerUtils.toString(c.getCoordList())).collect(Collectors.toList()))
				+ "]";
		total.add(cqstr);

		String vStr = "[" + String.join(",", this.vs.stream().map(c -> "\"" + c + "\"").collect(Collectors.toList()))
				+ "]";
		total.add(vStr);

		total.add("\"" + this.u + "\"");

		total.add("\"" + this.epsilon + "\"");

		System.out.println("str:" + String.join(",", total));
	}

	@Override
	public boolean verify() {

		List<BigInteger> TWOS = Lists.newLinkedList();
		for (int i = 0; i < this.nbits; i++) {
			BigInteger value = TWO.pow(i);
			TWOS.add(value);
		}

		List<BigInteger> challenges = Lists.newLinkedList();
		List<BigInteger> fs = Lists.newArrayList();
		Set<Integer> set = new HashSet<>();
		for (int i = 0; i < this.K; i++) {
			set.add(i);
		}
		List<List<Integer>> combinations = Arrays.asList(Arrays.asList(0, 2), Arrays.asList(1, 2));

		List<BigInteger> doublechallenges = Lists.newLinkedList();

		long s = System.nanoTime();

		List<Commitment> cs = new LinkedList<>();
		cs.addAll(this.cts);
		cs.addAll(this.css);
		cs.addAll(this.cqs);

		BigInteger challenge = HashUtils.hash(cs).mod(this.n);
		BigInteger challenge3 = challenge.modPow(BigInteger.valueOf(3), this.n);
		BigInteger challenge4 = challenge3.multiply(challenge).mod(this.n);
		BigInteger challenge5 = challenge4.multiply(challenge).mod(this.n);
		BigInteger challengeN1 = challenge.modInverse(this.n);

		challenges.add(challengeN1);
		challenges.add(challenge);
		challenges.add(challenge4);

		doublechallenges.add(challenge3);
		doublechallenges.add(challenge5);

		for (int i = 0; i < this.L; i++) {
			BigInteger f = BigInteger.ZERO;
			int base = i * this.K;
			for (int j = 0; j < this.K; j++) {
				int idx = base + j;
				if (idx < this.nbits) {
					f = f.add(challenges.get(j).multiply(TWOS.get(idx)));
				}
			}
			fs.add(f.subtract(this.vs.get(i)));
		}

		List<BigInteger> fvs = Streams.zip(fs.stream(), this.vs.stream(), (f, v) -> f.multiply(v).mod(this.n))
				.collect(Collectors.toList());

		Commitment ret1_1 = this.gs.mulBAndSum(VectorB.from(fvs, this.n)).add(this.commiter.mulH(this.u));
		Commitment ret1_2 = this.commiter.getIdentity();
		for (int i = 0; i < combinations.size(); i++) {
			ret1_2 = ret1_2.add(this.cts.get(i).mul(doublechallenges.get(i)));
		}

		Commitment ret2_2 = this.commiter.getIdentity();
		for (int i = 0; i < this.K; i++) {
			BigInteger c = challenges.get(i);
			ret1_2 = ret1_2.add(this.cqs.get(i).mul(c));
			ret2_2 = ret2_2.add(this.css.get(i).mul(c));
		}
		ret1_2 = ret1_2.add(this.cqs.get(this.K));
		ret2_2 = ret2_2.add(this.css.get(this.K));

		boolean b1 = ret1_1.equals(ret1_2);

		Commitment ret2_1 = this.commiter.getIdentity();
		ret2_1 = ret2_1.add(this.commiter
				.commitTo(this.vs.stream().reduce(BigInteger.ZERO, (v1, v2) -> v1.add(v2)).mod(this.n), this.epsilon));

		boolean b2 = ret2_1.equals(ret2_2);

		boolean b3 = this.cy.equals(this.css.subList(0, this.css.size() - 1).stream()
				.reduce(this.commiter.getIdentity(), (c1, c2) -> c1.add(c2)));

		long e = System.nanoTime();

		if (counter >= TestConstants.WARMUPS) {
			vtime += (e - s);
		}

		return b1 && b2 && b3;
	}

	public int numOfElements() {
		return this.cqs.size() + this.css.size() + this.cts.size() + this.vs.size() + 2;
	}
}
