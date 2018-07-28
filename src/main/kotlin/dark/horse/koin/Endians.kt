package dark.horse.koin

object Endians {
    enum class Type {
        LITTLE,
        BIG
    }

    private val HEX_ARRAY = "0123456789ABCDEF".toCharArray()

    /**
     * Take in a string of hex characters and return it back as a byte array?
     *
     * Assumes some amount of validity in the input
     */
    fun hexToBytes(input: String): ByteArray {
        val arr = ByteArray(input.length / 2)
        val chars = input.toUpperCase().toCharArray()
        for (i in arr.indices) {
            val amount = (HEX_ARRAY.indexOf(chars[i * 2]) * 16 +
                    HEX_ARRAY.indexOf(chars[2 * i + 1]))
            arr[i] = amount.toByte()
        }
        return arr
    }

    fun int32(input: Byter, ness: Type): Long {
        return toNumber(input.next(4), ness)
    }

    fun int16(input: Byter, ness: Type): Long {
        return toNumber(input.next(2), ness)
    }

    fun int32At(input: ByteArray, start: Int, ness: Type): Long {
        return toNumber(input, start, start + 4, ness)
    }

    fun int16At(input: ByteArray, start:Int, ness: Type): Long {
        return toNumber(input, start, start + 2, ness)
    }

    fun toNumber(input: ByteArray, ness: Type): Long {
        return toNumber(input, 0, input.size, ness)
    }

    fun toNumber(input: ByteArray, start: Int, end: Int, ness: Type): Long {
        var result: Long = 0
        if (ness == Type.BIG) {
            var at = start
            while (at < end) {
                result = result * 256 + input[at].toPositiveInt()
                at += 1
            }
        }  else {
            var at = end - 1
            while (at >= start) {
                result = result * 256 + input[at].toPositiveInt()
                at -= 1
            }
        }
        return result
    }

    fun Byte.toPositiveInt() = toInt() and 0xFF

    fun readVarInt(bytes: ByteArray, start: Int): Long {
        val first = bytes[start].toPositiveInt()
        if (first == 0xFD) { // next 2 bytes are the number
            return int16At(bytes, start + 1, Type.LITTLE)
        } else if (first == 0xFE) { // next 4 bytes are number
            return int32At(bytes, start + 1, Type.LITTLE)
        } else if (first == 0xFF) { // next 8 bytes are the number
            return toNumber(bytes, start + 1, start + 9, Type.LITTLE)
        } else {
            return first.toLong()
        }
    }

    fun readVarInt(byter: Byter): Long {
        val first = byter.nextByte().toPositiveInt()
        if (first == 0xFD) { // next 2 bytes are the number
            return toNumber(byter.next(2), Type.LITTLE)
        } else if (first == 0xFE) { // next 4 bytes are number
            return toNumber(byter.next(4), Type.LITTLE)
        } else if (first == 0xFF) { // next 8 bytes are the number
            return toNumber(byter.next(8), Type.LITTLE)
        } else {
            return first.toLong()
        }

    }
}