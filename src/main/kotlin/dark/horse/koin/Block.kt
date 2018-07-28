package dark.horse.koin

class Block(val version: Long, val prevBlocK: ByteArray, val merkleRoot: ByteArray,
            val timestamp: Long, val bits: ByteArray, val nonce: ByteArray) {
    companion object {
        /**
         * parse a block string
         */
        fun parse(raw: String): Block {
            val input = Endians.hexToBytes(raw)

            // verson - 4 bytes, little endian
            val version = Endians.int32At(input, 0, Endians.Type.LITTLE)
            val prevBlock = input.copyOfRange(4, 4 + 32).reversedArray()
            val merkleRoot = input.copyOfRange(4+32, 4+32+32).reversedArray()
            val timestamp = Endians.int32At(input, 4+32+32, Endians.Type.LITTLE)
            val bits = input.copyOfRange(4+32+32+4, 4+32+32+4+4)
            val nonce = input.copyOfRange(4+32+32+4+4, 4+32+32+4+4+4)

            return Block(version, prevBlock, merkleRoot, timestamp, bits, nonce)
        }
    }

    /**
     * BIP 9 is signaled if the top three bits are 001
     */
    fun bip9(): Boolean {
        return version.ushr(29).toInt() == 0x001
    }

    /**
     * BIP 91 is signaled if the 5th bit from the right is 1
     */
    fun bip91(): Boolean {
        return version.ushr(4).toInt() and 1 == 1
    }

    /**
     * BIP 141 is signaled if the 2nd bit from the right is 1
     */
    fun bip141(): Boolean {
        return version.ushr(1).toInt() and 1 == 1
    }

    // TODO
    /**
    fun target(): Double {
        val exponent = bits.last().toPositiveInt()
        val coefficient = Endians.toNumber(bits, 0, 3, Endians.Type.LITTLE)
        return coefficient * Math.pow(256.toDouble(), (exponent - 3).toDouble())
    }

    fun difficulty(): Double {
        val lowest = 0xFFFF * Math.pow(256.toDouble(), (0x1D - 3).toDouble())
        return lowest / target()
    }

    fun checkProof(): Boolean {
        //return proof < target()
        return true
    }
    fun Byte.toPositiveInt() = toInt() and 0xFF
    **/

}