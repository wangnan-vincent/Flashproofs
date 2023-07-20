package structure;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.Streams;

import utils.RandGenerator;

public class VectorB {

	private final List<BigInteger> list;
	private final BigInteger n;

	private VectorB(List<BigInteger> list, BigInteger n) {
		this.list = list;
		this.n = n;
	}

	public static VectorB from(List<BigInteger> list, BigInteger n) {
		return new VectorB(list, n);
	}

	public VectorB add(VectorB v) {
		return VectorB.from(Streams.zip(this.list.stream(), v.list.stream(), (b1, b2) -> b1.add(b2).mod(this.n))
				.collect(Collectors.toList()), this.n);
	}

	public VectorB sub(VectorB v) {
		return VectorB.from(Streams.zip(this.list.stream(), v.list.stream(), (b1, b2) -> b1.subtract(b2).mod(this.n))
				.collect(Collectors.toList()), this.n);
	}

	public VectorB mul(VectorB v) {
		return VectorB.from(Streams.zip(this.list.stream(), v.list.stream(), (b1, b2) -> b1.multiply(b2).mod(this.n))
				.collect(Collectors.toList()), this.n);
	}

	public VectorB flatMul(VectorB v) {
		List<BigInteger> list = new LinkedList<>();
		for (int i = 0; i < this.list.size(); i++) {
			BigInteger element = this.list.get(i);
			list.addAll(v.list.stream().map(e -> e.multiply(element).mod(this.n)).collect(Collectors.toList()));
		}

		return VectorB.from(list, this.n);
	}

	public BigInteger innerProd(VectorB v) {
		return Streams.zip(this.list.stream(), v.list.stream(), (b1, b2) -> b1.multiply(b2))
				.reduce(BigInteger.ZERO, (b1, b2) -> b1.add(b2)).mod(this.n);
	}

	public VectorB append(VectorB v) {
		this.list.addAll(v.getList());
		return VectorB.from(this.list, this.n);
	}

	public VectorB prepend(BigInteger b) {
		List<BigInteger> l = new LinkedList<>();
		l.add(b);
		l.addAll(this.list);

		return VectorB.from(l, this.n);
	}

	public static VectorB randoms(int size, BigInteger n) {
		return new VectorB(
				IntStream.range(0, size).mapToObj(i -> RandGenerator.getRandomInZp(n)).collect(Collectors.toList()),
				n);
	}

	public static VectorB ones(int size, BigInteger n) {
		return new VectorB(IntStream.range(0, size).mapToObj(i -> BigInteger.ONE).collect(Collectors.toList()), n);
	}

	public static VectorB sequence(int size, BigInteger n) {
		List<BigInteger> list = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			BigInteger b = BigInteger.valueOf(i + 1);
			for (int j = 0; j < 4; j++) {
				list.add(b);
			}
		}

		return new VectorB(list, n);
	}

	public static VectorB powerN(int size, BigInteger y, BigInteger n) {
		return powerNWithSeed(size, BigInteger.ONE, y, n);
	}

	public static VectorB powerNWithSeed(int size, BigInteger seed, BigInteger y, BigInteger n) {
		BigInteger[] arr = new BigInteger[size];
		arr[0] = seed;

		if (size > 1) {
			arr[1] = seed.multiply(y).mod(n);
			for (int i = 2; i < size; i++) {
				arr[i] = arr[i - 1].multiply(y).mod(n);
			}
		}

		return new VectorB(Arrays.asList(arr), n);
	}

	public VectorB addConstant(BigInteger constant) {
		return new VectorB(this.list.stream().map(b -> b.add(constant).mod(this.n)).collect(Collectors.toList()),
				this.n);
	}

	public VectorB subConstant(BigInteger constant) {
		return addConstant(constant.negate());
	}

	public VectorB mulConstant(BigInteger constant) {
		return new VectorB(this.list.stream().map(b -> b.multiply(constant).mod(this.n)).collect(Collectors.toList()),
				this.n);
	}

	public BigInteger sum() {
		return this.list.stream().reduce(BigInteger.ZERO, (b1, b2) -> b1.add(b2));
	}

	public VectorB inverse() {
		return new VectorB(this.list.stream().map(v -> v.modInverse(this.n)).collect(Collectors.toList()), this.n);
	}

	public VectorB negate() {
		return new VectorB(this.list.stream().map(v -> v.negate()).collect(Collectors.toList()), this.n);
	}

	public VectorB subVector(int start, int end) {
		return new VectorB(this.list.subList(start, end), this.n);
	}

	public static VectorB toBits(int nbits, BigInteger x, BigInteger q) {
		return VectorB.from(IntStream.range(0, nbits).mapToObj(i -> x.testBit(i) ? BigInteger.ONE : BigInteger.ZERO)
				.collect(Collectors.toList()), q);
	}

	public static VectorB constant(int size, BigInteger constant, BigInteger n) {
		return VectorB.from(IntStream.range(0, size).mapToObj(i -> constant).collect(Collectors.toList()), n);
	}

	public List<BigInteger> getList() {
		return list;
	}

	public BigInteger getN() {
		return n;
	}
}
