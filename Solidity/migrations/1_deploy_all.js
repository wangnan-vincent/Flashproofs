const Flashproof = artifacts.require("Flashproof");

module.exports = function (deployer) {
    deployer.then(async()=>{
        await deployer.deploy(Flashproof);
    });
};
