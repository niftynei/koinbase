package dark.horse.koin

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class EndiansTest {

    @ParameterizedTest(name = "little endian, {0} => {1}")
    @CsvSource(
            "'FFFF1234', 65535, 0",
            "'12A49876', 42002, 0",
            "'1234', 13330, 0",
            "'AABB1234', 13330, 2"
    )
    fun `little endian parsing`(input: String, value: Long, start: Int) {
        val bytes = Endians.hexToBytes(input)
        val result = Endians.int16At(bytes, start, Endians.Type.LITTLE)
        assertEquals(value, result)
    }

    @Test
    fun `big endian parsing`() {
        val bytes = Endians.hexToBytes("12345678")
        val result = Endians.toNumber(bytes, 0, 2, Endians.Type.BIG)
        assertEquals(4660, result)
    }

    @Test
    fun `hex string to bytes`() {
        val result = Endians.hexToBytes("1234")
        val expected = ByteArray(2)
        expected.set(0, 18.toByte())
        expected.set(1, 52.toByte())
        assertEquals(2, result.size)
        assertArrayEquals(expected, result)
    }

    @ParameterizedTest(name = "hex to bytes, {0} => {1}")
    @CsvSource(
            "'FF', 255",
            "'AA', 170",
            "'00', 0"
    )
    fun `hex string to bytes one byte`(input: String, expectedVal: Int) {
        val result = Endians.hexToBytes(input)
        val expected = ByteArray(1)
        expected.set(0, expectedVal.toByte())
        assertEquals(1, result.size)
        assertArrayEquals(expected, result)
    }

}
