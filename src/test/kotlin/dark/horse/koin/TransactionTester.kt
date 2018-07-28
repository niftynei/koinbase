package dark.horse.koin

import dark.horse.koin.Transaction.Network.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import sun.security.krb5.EncryptedData

class TransactionTester {
    @Test
    fun `parse the version`() {
        val raw = "0100000001813f79011acb80925dfe69b3def355fe914bd1d96a3f5f71bf8303c6a989c7d1000000006b483045022100ed81ff192e75a3fd2304004dcadb746fa5e24c5031ccfcf21320b0277457c98f02207a986d955c6e0cb35d446a89d3f56100f4d7f67801c31967743a9c8e10615bed01210349fc4e631e3624a545de3f89f5d8684c7b8138bd94bdd531d2e213bf016b278afeffffff02a135ef01000000001976a914bc3b654dca7e56b04dca18f2566cdaf02e8d9ada88ac99c39800000000001976a9141c4bc762dd5423e332166702cb75f40df79fea1288ac19430600"
        val tx = Transaction.parse(raw, TESTNET)
        assertEquals(1, tx.version)
    }

    @Test
    fun `parse inputs`() {
        val raw = "0100000001813f79011acb80925dfe69b3def355fe914bd1d96a3f5f71bf8303c6a989c7d1000000006b483045022100ed81ff192e75a3fd2304004dcadb746fa5e24c5031ccfcf21320b0277457c98f02207a986d955c6e0cb35d446a89d3f56100f4d7f67801c31967743a9c8e10615bed01210349fc4e631e3624a545de3f89f5d8684c7b8138bd94bdd531d2e213bf016b278afeffffff02a135ef01000000001976a914bc3b654dca7e56b04dca18f2566cdaf02e8d9ada88ac99c39800000000001976a9141c4bc762dd5423e332166702cb75f40df79fea1288ac19430600"
        val tx = Transaction.parse(raw, TESTNET)
        assertEquals(1, tx.inputs.size)
        val prevHash = Endians.hexToBytes("d1c789a9c60383bf715f3f6ad9d14b91fe55f3deb369fe5d9280cb1a01793f81")
        assertArrayEquals(prevHash, tx.inputs[0].previousTxn)
        assertEquals(0, tx.inputs[0].previousIndex)
        val scriptSig = Endians.hexToBytes("483045022100ed81ff192e75a3fd2304004dcadb746fa5e24c5031ccfcf21320b0277457c98f02207a986d955c6e0cb35d446a89d3f56100f4d7f67801c31967743a9c8e10615bed01210349fc4e631e3624a545de3f89f5d8684c7b8138bd94bdd531d2e213bf016b278a")
        assertArrayEquals(scriptSig, tx.inputs[0].scriptSig)
        assertEquals(0xfffffffe, tx.inputs[0].sequence)
    }

    @Test
    fun `parse outputs`() {
        val raw = "0100000001813f79011acb80925dfe69b3def355fe914bd1d96a3f5f71bf8303c6a989c7d1000000006b483045022100ed81ff192e75a3fd2304004dcadb746fa5e24c5031ccfcf21320b0277457c98f02207a986d955c6e0cb35d446a89d3f56100f4d7f67801c31967743a9c8e10615bed01210349fc4e631e3624a545de3f89f5d8684c7b8138bd94bdd531d2e213bf016b278afeffffff02a135ef01000000001976a914bc3b654dca7e56b04dca18f2566cdaf02e8d9ada88ac99c39800000000001976a9141c4bc762dd5423e332166702cb75f40df79fea1288ac19430600"
        val tx = Transaction.parse(raw, TESTNET)
        assertEquals(2, tx.outputs.size)
        assertEquals(32454049, tx.outputs[0].amount)
        val pubkeyZero = Endians.hexToBytes("76a914bc3b654dca7e56b04dca18f2566cdaf02e8d9ada88ac")
        assertArrayEquals(pubkeyZero, tx.outputs[0].pubKey)
        assertEquals(10011545, tx.outputs[1].amount)
        val pubkeyOne = Endians.hexToBytes("76a9141c4bc762dd5423e332166702cb75f40df79fea1288ac")
        assertArrayEquals(pubkeyOne, tx.outputs[1].pubKey)
    }

    @Test
    fun locktime() {
        val raw = "0100000001813f79011acb80925dfe69b3def355fe914bd1d96a3f5f71bf8303c6a989c7d1000000006b483045022100ed81ff192e75a3fd2304004dcadb746fa5e24c5031ccfcf21320b0277457c98f02207a986d955c6e0cb35d446a89d3f56100f4d7f67801c31967743a9c8e10615bed01210349fc4e631e3624a545de3f89f5d8684c7b8138bd94bdd531d2e213bf016b278afeffffff02a135ef01000000001976a914bc3b654dca7e56b04dca18f2566cdaf02e8d9ada88ac99c39800000000001976a9141c4bc762dd5423e332166702cb75f40df79fea1288ac19430600"
        val tx = Transaction.parse(raw, TESTNET)
        assertEquals(410393, tx.locktime)

    }

    @Test
    fun `is coinbase?`() {
        val raw = "01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff5e03d71b07254d696e656420627920416e74506f6f6c20626a31312f4542312f4144362f43205914293101fabe6d6d678e2c8c34afc36896e7d9402824ed38e856676ee94bfdb0c6c4bcd8b2e5666a0400000000000000c7270000a5e00e00ffffffff01faf20b58000000001976a914338c84849423992471bffb1a54a8d9b1d69dc28a88ac00000000"
        val tx = Transaction.parse(raw, TESTNET)
        assertTrue(tx.isCoinbase())
    }

    @Test
    fun `coinbase height`() {
        // print script sig
        val raw = "01000000010000000000000000000000000000000000000000000000000000000000000000ffffffff5e03d71b07254d696e656420627920416e74506f6f6c20626a31312f4542312f4144362f43205914293101fabe6d6d678e2c8c34afc36896e7d9402824ed38e856676ee94bfdb0c6c4bcd8b2e5666a0400000000000000c7270000a5e00e00ffffffff01faf20b58000000001976a914338c84849423992471bffb1a54a8d9b1d69dc28a88ac00000000"
        val tx = Transaction.parse(raw, TESTNET)
        println(tx.inputs[0].scriptSig.joinToString("") { "${it.toChar()}"})
    }
}