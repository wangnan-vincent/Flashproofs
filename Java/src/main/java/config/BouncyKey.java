package config;

import java.math.BigInteger;
import java.util.Arrays;

import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

public class BouncyKey extends Key<ECPoint> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BouncyKey(String keyName) {
		switch (keyName) {
		case "bn128":
			this.n = new BigInteger("21888242871839275222246405745257275088548364400416034343698204186575808495617");
			this.q = new BigInteger("21888242871839275222246405745257275088696311157297823662689037894645226208583");
			ECCurve curve = new ECCurve.Fp(this.q, BigInteger.ZERO, BigInteger.valueOf(3), this.n, null);

			this.g = curve.validatePoint(BigInteger.ONE, BigInteger.TWO);
			this.h = curve.validatePoint(
					new BigInteger("9727523064272218541460723335320998459488975639302513747055235660443850046724"),
					new BigInteger("5031696974169251245229961296941447383441169981934237515842977230762345915487"));

			this.gs = Arrays.asList(curve.validatePoint(
					new BigInteger("10634783317254738331264664985092831821376621286957316213969233667217643505748"),
					new BigInteger("3593894136463563836927847830567934263138852783181300960240960063050894828762")),
					curve.validatePoint(
							new BigInteger(
									"15079475582167754912076991092891442787377018931665830331839902737242049589194"),
							new BigInteger(
									"2994786716555744767228318303490139874612995833883024157075109374536749910797")),
					curve.validatePoint(
							new BigInteger(
									"5998026607105146355656696147154635391947958755485950806629481563238770197831"),
							new BigInteger(
									"9071495078062910622295597859043028737780972268539387614597098676522874141228")),
					curve.validatePoint(
							new BigInteger(
									"16523801944179829840198169326704340952379740918805059924608153703458536682146"),
							new BigInteger(
									"6038302089791721424956127465108246322480323157317821739578233683122154868638")),
					curve.validatePoint(
							new BigInteger(
									"12118397309736405059200255390820856272335184322136103314456373515523090868384"),
							new BigInteger(
									"3299068400210186031537495205944296158829957843773210327992629444571521214447")),
					curve.validatePoint(
							new BigInteger(
									"13569683714700597868720693802606454625784876902328159148425602640793284360016"),
							new BigInteger(
									"6196366122510722436252226379612342063576138922717891371699758504638443909810")),
					curve.validatePoint(
							new BigInteger(
									"6931749079582407422004463406148477362357661472338328270508373724106071518625"),
							new BigInteger(
									"21191302843410952823242789471696212271196775028224076743387772065534892367978")),
					curve.validatePoint(
							new BigInteger(
									"3818775040669594200577093816348913853129511932617816368937601194351220417610"),
							new BigInteger(
									"11457440080399767781624111424667375708460721196045291561100911147854033186883")),
					curve.validatePoint(
							new BigInteger(
									"16856378328816595579144346936425326333732976124866714942794663389927527942729"),
							new BigInteger(
									"8615147240200783032869516315617482186645983361490736606108099797176368213214")),
					curve.validatePoint(
							new BigInteger(
									"12329774238033643601677353420174969544762152665590909463734924154613306217386"),
							new BigInteger(
									"9349845555551207787110530432977444099219845456919646677037398830926083607248")),
					curve.validatePoint(
							new BigInteger(
									"15819385188090994752881147747997045863063544769706761541586812145860428725372"),
							new BigInteger(
									"17100833187243109040711678632824159418785961748506921615441797818807296019294")),
					curve.validatePoint(
							new BigInteger(
									"6331024371890361853402322881256862237801465545397987055303359505174258940787"),
							new BigInteger(
									"18571650698865115063332538959090545189492566919425356834234934402133902141366")),
					curve.validatePoint(
							new BigInteger(
									"7105985838448748955209439322478761515339875326287256355331470413548705695561"),
							new BigInteger(
									"18438173537644615561966541251456414954420156418562019789648878978333737520283")),
					curve.validatePoint(
							new BigInteger(
									"19021747983670190184630514322277256890031751525022704695742421493992173130370"),
							new BigInteger(
									"14483250175521306424840452131620408095676413044004913447506078614599443973015")),
					curve.validatePoint(
							new BigInteger(
									"12939943061190834182361325225540663473273714337397365880767331596410557127634"),
							new BigInteger(
									"21625625012093072106792496546825573499928322833210005959334070047102115545386")),
					curve.validatePoint(
							new BigInteger(
									"6590246720507402136630326950618777059699581927789913511934337513344857421023"),
							new BigInteger(
									"889573739332574916127485194837452655567667481149713859993862188238306657777")));

			break;
		}
	}
}