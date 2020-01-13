package openvr.lib

import glm_.b
import glm_.i
import kool.*
import org.lwjgl.system.MathUtil
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.*
import java.nio.ByteBuffer

typealias stak = Stack

//fun bufferOfAscii(string: String, nullTerminated: Boolean = true): ByteBuffer {
//    val bytes = stackGet().malloc(string.length + if (nullTerminated) 1 else 0)
//    for (i in string.indices)
//        bytes[i] = string[i].b
//    if (nullTerminated)
//        bytes[string.length] = 0
//    return bytes
//}

// TODO remove and use kool's one
fun <R> MemoryStack.asciiAdr(size: Int, nullTerminated: Boolean = true, block: (Adr) -> R): String {
    val adr = nmalloc(1, size + nullTerminated.i)
    block(adr)
    return memASCII(adr, size)
}

// TODO -> uno
const val NUL = '\u0000'

/** It mallocs, passes the address and reads the null terminated string
 *  Getter */
inline fun <R> Stack.asciiAdr(maxSize: Int, block: (Adr) -> R): String = with {
    val adr = it.nmalloc(1, maxSize)
    block(adr)
    memASCII(adr, strlen64NT1(adr, maxSize))
}

/** It malloc the buffer, passes it and reads the null terminated string
 *  Getter */
inline fun <R> Stack.asciiBuffer(maxSize: Int, block: (ByteBuffer) -> R): String = with {
    val buf = it.malloc(1, maxSize)
    block(buf)
    memASCII(buf.adr, maxSize)
}

fun strlen64NT1(address: Long, maxLength: Int): Int {
    var i = 0
    if (8 <= maxLength) {
        val misalignment = address.toInt() and 7
        if (misalignment != 0) { // Align to 8 bytes
            val len = 8 - misalignment
            while (i < len) {
                if (UNSAFE.getByte(null, address + i).toInt() == 0)
                    return i
                i++
            }
        }
        // Aligned longs for performance
        while (i <= maxLength - 8) {
            if (MathUtil.mathHasZeroByte(UNSAFE.getLong(null, address + i)))
                break
            i += 8
        }
    }
    // Tail
    while (i < maxLength) {
        if (UNSAFE.getByte(null, address + i).toInt() == 0)
            break
        i++
    }
    return i
}