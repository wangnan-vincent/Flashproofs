# Flashproofs
This repository contains the source code of the paper `Flashproofs: Efficient Zero-Knowledge Arguments of Range and Polynomial Evaluation with Transparent Setup` at the eprint link: `https://eprint.iacr.org/2022/1251`.

`Note that the code is for research only`.

There are two folders:
* Java Implementation: A maven project of range arguments and polynomial evaluation arguments
* Solidity Implementation: A Truffle project of range arguments

The solidity code evaluates the gas costs of the range arguments of 32-bit and 64-bit range size. The incurred gas costs are as below:
* 32-bit: 233,250
* 64-bit: 314,140
