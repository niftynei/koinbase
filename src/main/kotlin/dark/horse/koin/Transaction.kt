package dark.horse.koin

class Transaction(val version: Long, val inputs: List<Input>, val outputs: List<Output>,
                  val locktime: Long, val network: Network) {

    fun isCoinbase(): Boolean {
        if (inputs.size != 1) return false
        val firstInput = inputs[0]
        val size = firstInput.previousTxn.size

        val emptyBytes = ByteArray(size)
        emptyBytes.fill(0.toByte(), 0, size)
        if (!(firstInput.previousTxn contentEquals emptyBytes)) return false
        if (firstInput.previousIndex != 0xffffffff) return false
        return true
    }

    fun coinbaseHeight(): Int? {
        if (!isCoinbase()) return null

        val firstInput = inputs[0]
        val firstElement = firstInput.scriptSig
        // omg
        return null
    }

    enum class Network {
        TESTNET,
        MAINNET,
        REGNET
    }

    companion object {
        fun parse(raw: String, network: Network) : Transaction {
            val byter = Byter(Endians.hexToBytes(raw))
            // version - 4 bytes, little endian
            val version = Endians.toNumber(byter.next(4), Endians.Type.LITTLE)
            val numberInputs = Endians.readVarInt(byter)

            val inputs = ArrayList<Input>()
            for (i in 0 until numberInputs) {
                inputs += (Input.parse(byter))
            }
            val numberOutputs = Endians.readVarInt(byter)
            val outputs = ArrayList<Output>()
            for (i in 0 until numberOutputs) {
                outputs += (Output.parse(byter))
            }

            val locktime = Endians.int32(byter, Endians.Type.LITTLE)

            return Transaction(version, inputs, outputs, locktime, network)
        }
    }

    class Input(val previousTxn: ByteArray, val previousIndex: Long, val scriptSig: ByteArray,
                val sequence: Long) {

        companion object {
            fun parse(byter: Byter) : Input {
                val previousTxn = byter.next(32).reversedArray()
                val previousIndex = Endians.int32(byter, Endians.Type.LITTLE)
                val scriptLen = Endians.readVarInt(byter)
                val scriptSig = byter.next(scriptLen.toInt())
                val sequence = Endians.int32(byter, Endians.Type.LITTLE)
                return Input(previousTxn, previousIndex, scriptSig, sequence)
            }
        }
    }

    class Output(val amount: Long, val pubKey: ByteArray) {
        companion object {
            fun parse(byter: Byter): Output {
                val amount = Endians.toNumber(byter.next(8), Endians.Type.LITTLE)
                val scriptPubkeyLen = Endians.readVarInt(byter)
                val scriptPubkey = byter.next(scriptPubkeyLen.toInt() and 0xFF)
                return Output(amount, scriptPubkey)
            }
        }
    }
}