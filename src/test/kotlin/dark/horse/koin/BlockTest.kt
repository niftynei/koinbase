package dark.horse.koin

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.math.BigDecimal
import java.math.BigInteger

class BlockTest {

    @Test
    fun `test parsing`() {
        val raw = "020000208ec39428b17323fa0ddec8e887b4a7c53b8c0a0a220cfd0000000000000000005b0750fce0a889502d40508d39576821155e9c9e3f5c3157f961db38fd8b25be1e77a759e93c0118a4ffd71d"
        val block = Block.parse(raw)
        assertEquals(0x20000002L, block.version)
        assertArrayEquals(Endians.hexToBytes("000000000000000000fd0c220a0a8c3bc5a7b487e8c8de0dfa2373b12894c38e"),
                block.prevBlocK)
        assertArrayEquals(Endians.hexToBytes("be258bfd38db61f957315c3f9e9c5e15216857398d50402d5089a8e0fc50075b"),
                block.merkleRoot)
        assertEquals(0x59a7771eL, block.timestamp)
        assertArrayEquals(Endians.hexToBytes("e93c0118"), block.bits)
        assertArrayEquals(Endians.hexToBytes("a4ffd71d"), block.nonce)
    }

    @Test
    fun bip9() {
        val yes = "020000208ec39428b17323fa0ddec8e887b4a7c53b8c0a0a220cfd0000000000000000005b0750fce0a889502d40508d39576821155e9c9e3f5c3157f961db38fd8b25be1e77a759e93c0118a4ffd71d"
        val block = Block.parse(yes)
        assertTrue(block.bip9())

        val no = "0400000039fa821848781f027a2e6dfabbf6bda920d9ae61b63400030000000000000000ecae536a304042e3154be0e3e9a8220e5568c3433a9ab49ac4cbb74f8df8e8b0cc2acf569fb9061806652c27"
        val noBlock = Block.parse(no)
        assertFalse(noBlock.bip9())
    }

    @Test
    fun bip91() {
       val yes = "1200002028856ec5bca29cf76980d368b0a163a0bb81fc192951270100000000000000003288f32a2831833c31a25401c52093eb545d28157e200a64b21b3ae8f21c507401877b5935470118144dbfd1"
       val no = "020000208ec39428b17323fa0ddec8e887b4a7c53b8c0a0a220cfd0000000000000000005b0750fce0a889502d40508d39576821155e9c9e3f5c3157f961db38fd8b25be1e77a759e93c0118a4ffd71d"
        assertTrue(Block.parse(yes).bip91())
        assertFalse(Block.parse(no).bip91())
    }

    @Test
    fun bip141() {
        val yes = "020000208ec39428b17323fa0ddec8e887b4a7c53b8c0a0a220cfd0000000000000000005b0750fce0a889502d40508d39576821155e9c9e3f5c3157f961db38fd8b25be1e77a759e93c0118a4ffd71d"
        val no = "0000002066f09203c1cf5ef1531f24ed21b1915ae9abeb691f0d2e0100000000000000003de0976428ce56125351bae62c5b8b8c79d8297c702ea05d60feabb4ed188b59c36fa759e93c0118b74b2618"
        assertTrue(Block.parse(yes).bip141())
        assertFalse(Block.parse(no).bip141())
    }

    /** WIP
    @Test
    fun target() {
        val raw = "020000208ec39428b17323fa0ddec8e887b4a7c53b8c0a0a220cfd0000000000000000005b0750fce0a889502d40508d39576821155e9c9e3f5c3157f961db38fd8b25be1e77a759e93c0118a4ffd71d"
        val block = Block.parse(raw)
        val target = BigDecimal.valueOf(block.target()).toBigInteger()
        println("${target}")
        //assertEquals(0x13ce9000000000000000000000000000000000000000000L, target)
        assertEquals(888171856257, block.difficulty().toInt())
    }
    **/
}