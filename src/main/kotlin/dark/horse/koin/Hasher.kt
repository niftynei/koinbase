package dark.horse.koin

import org.bouncycastle.crypto.digests.RIPEMD160Digest
import java.security.MessageDigest

object Hasher {

    private val HEX_ARRAY = "0123456789ABCDEF".toCharArray()

    fun hash256(input: String) = hashString(input)

    fun double256(input: String): ByteArray? {
        return hash256(hash256(input))
    }

    fun RIPEMD160Digest.hash(input: ByteArray) : ByteArray {
        val output = ByteArray(digestSize)
        update(input, 0, input.size)
        doFinal(output, 0)
        return output
    }

    fun ripemd160(input: String): ByteArray {
        return RIPEMD160Digest().hash(input.toByteArray())
    }

    fun ripemd160(input: ByteArray): ByteArray {
        return RIPEMD160Digest().hash(input)
    }

    fun hash160(input: String): ByteArray {
        return ripemd160(hash256(input)!!)
    }

    fun toHex(hashed: ByteArray): String {
        val chars = CharArray(hashed.size * 2)
        for (j in hashed.indices) {
            val v = hashed[j].toInt() and 0xFF
            chars[j * 2] = HEX_ARRAY[v.ushr(4)]
            chars[j * 2 + 1] = HEX_ARRAY[v and 0x0F]
        }
        return String(chars)
    }

    private fun hashString(input: String): ByteArray? {
        return hash256(input.toByteArray())
    }

    private fun hash256(input: ByteArray?): ByteArray? {
        return MessageDigest
                .getInstance("SHA-256")
                .digest(input)
    }
}

