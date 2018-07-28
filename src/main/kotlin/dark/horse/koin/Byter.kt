package dark.horse.koin

/**
 * My very own janky wrapper for tracking byte operations
 */

class Byter(private val bytes: ByteArray) {
    private var pointer: Int = 0;

    /**
     * Return the next X bytes as a byte array and
     * advance the pointer.
     */
    fun next(wanted: Int): ByteArray {
        if (bytesLeft() < wanted)
            throw Error("Too many bytes wanted ${bytesLeft()} < ${wanted}")

        val subsection = bytes.copyOfRange(pointer, (pointer + wanted))
        pointer += wanted
        return subsection
    }

    fun nextByte(): Byte {
        val byte = bytes.get(pointer)
        pointer += 1
        return byte
    }

    fun bytesLeft(): Int {
        return bytes.size - pointer;
    }
}