# Flashproofs
This repository contains the source code of the paper `Flashproofs: Efficient Zero-Knowledge Arguments of Range and Polynomial Evaluation with Transparent Setup` accepted to 
`the 28th Annual International Conference on the Theory and Application of Cryptology and Information Security. (Asiacrypt 2022)`

`Note that the code is for research only. Please reference our paper and follow the attached license when you use our code.`

There are two folders:
* Java Implementation: A maven project of range arguments and polynomial evaluation arguments
* Solidity Implementation: A Truffle project of range arguments

The solidity code evaluates the gas costs of the range arguments of 32-bit and 64-bit range size. The incurred gas costs are as below:
* 32-bit: 233,250
* 64-bit: 314,140
