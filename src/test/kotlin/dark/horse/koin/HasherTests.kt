package dark.horse.koin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HasherTests {

    @Test
    fun `1x sha of "hello"`() {
        val hashed = Hasher.hash256("hello")
        assertEquals("2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824".toUpperCase(),
                Hasher.toHex(hashed!!), "One time SHA errored out")
    }

    @Test
    fun `double sha256`() {
        val hashed = Hasher.double256("hello")
        assertEquals("9595c9df90075148eb06860365df33584b75bff782a510c6cd4883a419833d50".toUpperCase(),
                Hasher.toHex(hashed!!), "Double SHA errored out")
    }

    fun ripemd160() {
        val hashed = Hasher.ripemd160("Rosetta Code")
        assertEquals("b3be159860842cebaa7174c8fff0aa9e50a5199f".toUpperCase(),
                Hasher.toHex(hashed), "RipEmd160 not working")
    }

    @Test
    fun `ripemd160 of a sha256 hash`() {
        val hashed = Hasher.hash160("hello")
        assertEquals("B6A9C8C230722B7C748331A8B450F05566DC7D0F",
                Hasher.toHex(hashed), "RipEmd160 not working")
    }
}
