// SPDX-License-Identifier: GPL-3.0
pragma solidity ^0.8.0;
pragma experimental ABIEncoderV2;

/**
This code implements the range arguments of Flashproofs.
The code is for research only.
Author: Nan Wang
Date: 30/09/2022
*/

contract Flashproof {

    struct Point {
        uint256 x;
        uint256 y;
    }

    struct Part1 {
        Point cy;
        Point[] cts;
        Point[] css;
        Point[] cqs;
        uint256[] eInvs;
    }

    struct Part2 {
        uint256[] vs;
        uint256 u;
        uint256 epsilon;
    }

    uint256 public constant GX = 1;
    uint256 public constant HX = 9727523064272218541460723335320998459488975639302513747055235660443850046724;
    uint256 public constant GY = 2;
    uint256 public constant HY = 5031696974169251245229961296941447383441169981934237515842977230762345915487;

    uint256 internal constant AA = 0;
    uint256 internal constant BB = 3;
    uint256 public constant PP = 0x30644e72e131a029b85045b68181585d97816a916871ca8d3c208c16d87cfd47;
    uint256 internal constant NN = 0x30644e72e131a029b85045b68181585d2833e84879b9709143e1f593f0000001;

    uint256 private constant G1X = 10634783317254738331264664985092831821376621286957316213969233667217643505748;
    uint256 private constant G1Y = 3593894136463563836927847830567934263138852783181300960240960063050894828762;
    uint256 private constant G2X = 15079475582167754912076991092891442787377018931665830331839902737242049589194;
    uint256 private constant G2Y = 2994786716555744767228318303490139874612995833883024157075109374536749910797;
    uint256 private constant G3X = 5998026607105146355656696147154635391947958755485950806629481563238770197831;
    uint256 private constant G3Y = 9071495078062910622295597859043028737780972268539387614597098676522874141228;
    uint256 private constant G4X = 16523801944179829840198169326704340952379740918805059924608153703458536682146;
    uint256 private constant G4Y = 6038302089791721424956127465108246322480323157317821739578233683122154868638;
    uint256 private constant G5X = 12118397309736405059200255390820856272335184322136103314456373515523090868384;
    uint256 private constant G5Y = 3299068400210186031537495205944296158829957843773210327992629444571521214447;
    uint256 private constant G6X = 13569683714700597868720693802606454625784876902328159148425602640793284360016;
    uint256 private constant G6Y = 6196366122510722436252226379612342063576138922717891371699758504638443909810;
    uint256 private constant G7X = 6931749079582407422004463406148477362357661472338328270508373724106071518625;
    uint256 private constant G7Y = 21191302843410952823242789471696212271196775028224076743387772065534892367978;
    uint256 private constant G8X = 3818775040669594200577093816348913853129511932617816368937601194351220417610;
    uint256 private constant G8Y = 11457440080399767781624111424667375708460721196045291561100911147854033186883;
    uint256 private constant G9X = 16856378328816595579144346936425326333732976124866714942794663389927527942729;
    uint256 private constant G9Y = 8615147240200783032869516315617482186645983361490736606108099797176368213214;
    uint256 private constant G10X = 12329774238033643601677353420174969544762152665590909463734924154613306217386;
    uint256 private constant G10Y = 9349845555551207787110530432977444099219845456919646677037398830926083607248;
    uint256 private constant G11X = 15819385188090994752881147747997045863063544769706761541586812145860428725372;
    uint256 private constant G11Y = 17100833187243109040711678632824159418785961748506921615441797818807296019294;
    uint256 private constant G12X = 6331024371890361853402322881256862237801465545397987055303359505174258940787;
    uint256 private constant G12Y = 18571650698865115063332538959090545189492566919425356834234934402133902141366;
    uint256 private constant G13X = 7105985838448748955209439322478761515339875326287256355331470413548705695561;
    uint256 private constant G13Y = 18438173537644615561966541251456414954420156418562019789648878978333737520283;
    uint256 private constant G14X = 19021747983670190184630514322277256890031751525022704695742421493992173130370;
    uint256 private constant G14Y = 14483250175521306424840452131620408095676413044004913447506078614599443973015;
    uint256 private constant G15X = 12939943061190834182361325225540663473273714337397365880767331596410557127634;
    uint256 private constant G15Y = 21625625012093072106792496546825573499928322833210005959334070047102115545386;
    uint256 private constant G16X = 6590246720507402136630326950618777059699581927789913511934337513344857421023;
    uint256 private constant G16Y = 889573739332574916127485194837452655567667481149713859993862188238306657777;

    function multiply(Point memory p, uint256 k)
        public
        returns (Point memory)
    {
        uint256[3] memory input;
        input[0] = p.x;
        input[1] = p.y;
        input[2] = k;

        bool success;
        uint256[2] memory result;

        assembly {
            success := call(not(0), 0x07, 0, input, 96, result, 64)
        }
        require(success, "elliptic curve multiplication failed");

        return Point(result[0], result[1]);
    }

    function add(Point memory p1, Point memory p2)
        internal
        returns (Point memory)
    {
        uint256[4] memory input;
        input[0] = p1.x;
        input[1] = p1.y;
        input[2] = p2.x;
        input[3] = p2.y;

        bool success;
        uint256[2] memory result;
        assembly {
            success := call(not(0), 0x06, 0, input, 128, result, 64)
        }

        require(success, "bn256 addition failed");
        return Point(result[0], result[1]);
    }

    function sub(Point memory p1, Point memory p2)
        public
        returns (Point memory)
    {
        uint256[4] memory input;
        input[0] = p1.x;
        input[1] = p1.y;
        input[2] = p2.x;
        input[3] = PP - p2.y;

        bool success;
        uint256[2] memory result;
        assembly {
            success := call(not(0), 0x06, 0, input, 128, result, 64)
        }

        require(success, "bn256 subtraction failed");
        return Point(result[0], result[1]);
    }

    function verifyRangeArgument(Part1 memory part1, Part2 memory part2, uint nbits, uint K, uint L)
        external
        returns (uint256)
    {
        uint256[] memory challenges = computeChallenges(part1, K);

        checkCond1(challenges, part1, part2, nbits, K, L);

        checkCond2(challenges, part1.css, part2.vs, part2.epsilon);

        checkCond3(part1.cy, part1.css);
    }

    function generateGS(uint L)
        internal
        returns (Point[] memory)
    {
        Point[] memory gs = new Point[](L);
        gs[0] = Point(G1X, G1Y);
        gs[1] = Point(G2X, G2Y);
        gs[2] = Point(G3X, G3Y);
        gs[3] = Point(G4X, G4Y);
        gs[4] = Point(G5X, G5Y);
        gs[5] = Point(G6X, G6Y);
        gs[6] = Point(G7X, G7Y);
        gs[7] = Point(G8X, G8Y);
        gs[8] = Point(G9X, G9Y);
        gs[9] = Point(G10X, G10Y);
        gs[10] = Point(G11X, G11Y);

        if(L == 16){
            gs[11] = Point(G12X, G12Y);
            gs[12] = Point(G13X, G13Y);
            gs[13] = Point(G14X, G14Y);
            gs[14] = Point(G15X, G15Y);
            gs[15] = Point(G16X, G16Y);
        }

        return gs;
    }

    function computeBaseChallenge(Point memory cy, Point[] memory cts, Point[] memory css, Point[] memory cqs)
        internal
        returns (uint256)
    {
        uint ctlength = cts.length;
        uint cslength = css.length;
        uint cqlength = cqs.length;

        uint256 size = 2 * (ctlength + cslength + cqlength);
        uint256[] memory cs = new uint256[](size);
        uint offset = 0;
        for(uint i=0;i<ctlength;i++) {
            Point memory p = cts[i];
            cs[offset] = p.x;
            offset = offset + 1;
            cs[offset] = p.y;
            offset = offset + 1;
        }
        for(uint i=0;i<cslength;i++) {
            Point memory p = css[i];
            cs[offset] = p.x;
            offset = offset + 1;
            cs[offset] = p.y;
            offset = offset + 1;
        }
        for(uint i=0;i<cqlength;i++) {
            Point memory p = cqs[i];
            cs[offset] = p.x;
            offset = offset + 1;
            cs[offset] = p.y;
            offset = offset + 1;
        }

        return mod(uint256(keccak256(abi.encodePacked(cs))), NN);
    }

    function mod(uint256 a, uint256 b)
        public
        pure
        returns (uint256)
    {
        require(b != 0, "SafeMath: modulo by zero");

        uint256 c;
        assembly {
            c := mod(a, b)
        }

        return c;
    }

    function computeChallenges(Part1 memory part1, uint K)
        internal
        returns (uint256[] memory)
    {
        uint256 challengeNeg1 = part1.eInvs[0];

        uint256 challenge = computeBaseChallenge(part1.cy, part1.cts, part1.css, part1.cqs);
        require (mulmod(challenge, challengeNeg1, NN) == 1);

        uint256[] memory challenges = new uint256[](K);

        uint256 challenge2 = mulmod(challenge, challenge, NN);
        uint256 challenge4 = mulmod(challenge2, challenge2, NN);
        challenges[0] = challengeNeg1;
        challenges[1] = challenge;
        challenges[2] = challenge4;

        if(K == 4) {
            uint256 challenge5 = mulmod(challenge4, challenge, NN);
            challenges[3] = challenge5;
        }

        return challenges;
    }

    function checkCond1(uint256[] memory challenges, Part1 memory part1, Part2 memory part2, uint nbits, uint256 K, uint256 L)
        internal
    {
        uint256[] memory fvs = computeFV(challenges, part2.vs, nbits, K, L);
        (uint256 fvpx, uint256 fvpy) = computeFVPoint(fvs);

        uint256[3] memory mul_input;
        uint256[4] memory add_input;
        bool success;
        mul_input[0] = HX;
        mul_input[1] = HY;
        mul_input[2] = part2.u;
        assembly {
            success := call(not(0), 7, 0, mul_input, 0x80, add(add_input, 0x40), 0x60)
        }

        add_input[0] = fvpx;
        add_input[1] = fvpy;
        assembly {
            success := call(not(0), 6, 0, add_input, 0x80, add_input, 0x60)
        }

        uint256 ret1x = add_input[0];
        uint256 ret1y = add_input[1];

        if(K == 3) {
            (add_input[0], add_input[1]) = computeCTSForK3(challenges, part1.cts);
        } else if(K == 4) {
            (add_input[0], add_input[1]) = computeCTSForK4(challenges, part1.cts);
        }

        (add_input[2], add_input[3]) = computeCQS(challenges, part1.cqs);
        assembly {
            success := call(not(0), 6, 0, add_input, 0x80, add_input, 0x60)
        }

        assert (add_input[0] == ret1x && add_input[1] == ret1y);
    }

    function computeCQS(uint256[] memory challenges, Point[] memory cqs)
        internal
        returns (uint256 x, uint256 y)
    {
        uint size = cqs.length-1;

        uint256[3] memory mul_input;
        uint256[4] memory add_input;
        bool success;

        Point memory p = cqs[0];
        mul_input[0] = p.x;
        mul_input[1] = p.y;
        mul_input[2] = challenges[0];
        assembly {
            success := call(not(0), 7, 0, mul_input, 0x80, add(add_input, 0x0), 0x60)
        }

        for(uint i=1;i<size;i++){
            Point memory p = cqs[i];
            mul_input[0] = p.x;
            mul_input[1] = p.y;
            mul_input[2] = challenges[i];

            assembly {
                success := call(not(0), 7, 0, mul_input, 0x80, add(add_input, 0x40), 0x60)
            }

            assembly {
                success := call(not(0), 6, 0, add_input, 0x80, add_input, 0x60)
            }
        }

        p = cqs[size];
        add_input[2] = p.x;
        add_input[3] = p.y;

        assembly {
            success := call(not(0), 6, 0, add_input, 0x80, add_input, 0x60)
        }

        return (add_input[0], add_input[1]);
    }

    function checkCond2(uint256[] memory challenges, Point[] memory css, uint256[] memory vs, uint256 epsilon)
        internal
        returns (Point memory)
    {
        uint256 vsum = 0;
        uint vsize = vs.length;
        for(uint i=0;i<vsize;i++) {
            vsum = addmod(vsum, vs[i], NN);
        }

        uint256[3] memory mul_input;
        uint256[4] memory add_input;
        bool success;

        mul_input[0] = GX;
        mul_input[1] = GY;
        mul_input[2] = vsum;
        assembly {
            success := call(not(0), 7, 0, mul_input, 0x80, add_input, 0x60)
        }

        mul_input[0] = HX;
        mul_input[1] = HY;
        mul_input[2] = epsilon;
        assembly {
            success := call(not(0), 7, 0, mul_input, 0x80, add(add_input, 0x40), 0x60)
        }

        assembly {
            success := call(not(0), 6, 0, add_input, 0x80, add_input, 0x60)
        }

        uint256 ret1x = add_input[0];
        uint256 ret1y = add_input[1];

        Point memory p = css[0];
        mul_input[0] = p.x;
        mul_input[1] = p.y;
        mul_input[2] = challenges[0];
        assembly {
            success := call(not(0), 7, 0, mul_input, 0x80, add_input, 0x60)
        }

        uint wsize = css.length - 1;
        for(uint i=1;i<wsize;i++) {
            Point memory p = css[i];
            mul_input[0] = p.x;
            mul_input[1] = p.y;
            mul_input[2] = challenges[i];

            assembly {
                success := call(not(0), 7, 0, mul_input, 0x80, add(add_input, 0x40), 0x60)
            }

            assembly {
                success := call(not(0), 6, 0, add_input, 0x80, add_input, 0x60)
            }
        }

        p = css[wsize];
        add_input[2] = p.x;
        add_input[3] = p.y;

        assembly {
            success := call(not(0), 6, 0, add_input, 0x80, add_input, 0x60)
        }

        assert (ret1x == add_input[0] && ret1y == add_input[1]);
    }

    function checkCond3(Point memory cy, Point[] memory css)
        internal
    {
        uint wsize = css.length - 1;
        uint256[4] memory add_input;
        bool success;

        Point memory p = css[0];
        add_input[0] = p.x;
        add_input[1] = p.y;
        for(uint i=1;i<wsize;i++) {
            Point memory p = css[i];
            add_input[2] = p.x;
            add_input[3] = p.y;
            assembly {
                success := call(not(0), 6, 0, add_input, 0x80, add_input, 0x60)
            }
        }

        assert (cy.x == add_input[0] && cy.y == add_input[1]);
    }

    function computeFV(uint256[] memory challenges, uint256[] memory vs, uint nbits, uint K, uint L)
        internal
        returns (uint256[] memory)
    {
        uint256[] memory fvs = new uint256[](L);

        for(uint i=0;i<L;i++) {
            uint256 f = 0;
            uint base = i * K;
            for(uint j=0;j<K;j++) {
                uint idx = base + j;
                if(idx == 0) {
                    f = challenges[0];
                } else if(idx < nbits) {
                    f = addmod(f, mulmod(challenges[j], 2 << (idx-1), NN), NN);
                }
            }
            uint256 v = vs[i];
            if(f >= v) {
                f = f - v;
            } else {
                f = NN - v + f;
            }
            fvs[i] = mulmod(f, v, NN);
        }

        return fvs;
    }

    function computeFVPoint(uint256[] memory fvs)
        internal
        returns (uint256 x, uint256 y)
    {
        uint256[3] memory mul_input;
        uint256[4] memory add_input;
        bool success;

        add_input[0] = 0;
        add_input[1] = 0;
        uint size = fvs.length;

        Point[] memory gs = generateGS(size);
        for(uint i=0;i<size;i++) {
            Point memory g = gs[i];
            mul_input[0] = g.x;
            mul_input[1] = g.y;
            mul_input[2] = fvs[i];

            assembly {
                success := call(not(0), 7, 0, mul_input, 0x80, add(add_input, 0x40), 0x60)
            }

            assembly {
                success := call(not(0), 6, 0, add_input, 0x80, add_input, 0x60)
            }
        }

        return (add_input[0], add_input[1]);
    }

    function computeCTSForK4(uint256[] memory challenges, Point[] memory cts)
        internal
        returns (uint256 x, uint256 y)
    {
        uint256[3] memory mul_input;
        uint256[4] memory add_input;
        bool success;

        Point memory p = cts[0];
        mul_input[0] = p.x;
        mul_input[1] = p.y;
        mul_input[2] = mulmod(challenges[2], challenges[3], NN);
        assembly {
            success := call(not(0), 7, 0, mul_input, 0x80, add_input, 0x60)
        }

        p = cts[1];
        mul_input[0] = p.x;
        mul_input[1] = p.y;
        mul_input[2] = mulmod(challenges[1], challenges[3], NN);
        assembly {
            success := call(not(0), 7, 0, mul_input, 0x80, add(add_input, 0x40), 0x60)
        }

        assembly {
            success := call(not(0), 6, 0, add_input, 0x80, add_input, 0x60)
        }

        p = cts[2];
        mul_input[0] = p.x;
        mul_input[1] = p.y;
        mul_input[2] = mulmod(challenges[0], challenges[2], NN);
        assembly {
            success := call(not(0), 7, 0, mul_input, 0x80, add(add_input, 0x40), 0x60)
        }

        assembly {
            success := call(not(0), 6, 0, add_input, 0x80, add_input, 0x60)
        }

        return (add_input[0], add_input[1]);
    }

    function computeCTSForK3(uint256[] memory challenges, Point[] memory cts)
        internal
        returns (uint256 x, uint256 y)
    {
        uint256[3] memory mul_input;
        uint256[4] memory add_input;
        bool success;

        Point memory p = cts[0];
        mul_input[0] = p.x;
        mul_input[1] = p.y;
        mul_input[2] = mulmod(challenges[0], challenges[2], NN);

        assembly {
            success := call(not(0), 7, 0, mul_input, 0x80, add_input, 0x60)
        }

        p = cts[1];
        mul_input[0] = p.x;
        mul_input[1] = p.y;
        mul_input[2] = mulmod(challenges[1], challenges[2], NN);

        assembly {
            success := call(not(0), 7, 0, mul_input, 0x80, add(add_input, 0x40), 0x60)
        }

        assembly {
            success := call(not(0), 6, 0, add_input, 0x80, add_input, 0x60)
        }

        return (add_input[0], add_input[1]);
    }
}
