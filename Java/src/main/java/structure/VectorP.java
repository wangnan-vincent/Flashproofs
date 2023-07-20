package structure;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Streams;

import commitment.Commiter;
import commitment.Commitment;
import config.Config;

public class VectorP {

	private final List<Commitment> list;

	private final Commiter commiter = Config.getInstance().getCommiter();

	private VectorP(List<Commitment> list) {
		this.list = list;
	}

	public static VectorP from(List<Commitment> list) {
		return new VectorP(list);
	}

	public VectorP subVector(int start, int end) {
		return new VectorP(this.list.subList(start, end));
	}

	public Commitment mulBAndSum(BigInteger b) {
		return this.list.stream().reduce(this.commiter.getIdentity(), (p1, p2) -> p1.add(p2)).mul(b);
	}

	public VectorP mulB(BigInteger b) {
		return VectorP.from(this.list.stream().map(p -> p.mul(b)).collect(Collectors.toList()));
	}

	public VectorP mulB(VectorB v) {
		return new VectorP(
				Streams.zip(this.list.stream(), v.getList().stream(), (p, b) -> p.mul(b)).collect(Collectors.toList()));
	}

	public Commitment mulBAndSum(VectorB v) {
		return Streams.zip(this.list.stream(), v.getList().stream(), (p, b) -> p.mul(b))
				.reduce(this.commiter.getIdentity(), (p1, p2) -> p1.add(p2));
	}

	public VectorP addP(VectorP v) {
		return new VectorP(
				Streams.zip(this.list.stream(), v.list.stream(), (p1, p2) -> p1.add(p2)).collect(Collectors.toList()));
	}

	public Commitment sum() {
		return this.list.stream().reduce(this.commiter.getIdentity(), (c1, c2) -> c1.add(c2));
	}

	public List<Commitment> getList() {
		return list;
	}

	public int size() {
		return this.list.size();
	}
}
