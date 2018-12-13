package openvr.lib

import glm_.BYTES
import glm_.b
import kool.set
import kool.Adr
import kool.Ptr
import org.lwjgl.openvr.VR.nVR_IsInterfaceVersionValid
import org.lwjgl.system.CustomBuffer
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryStack.stackGet
import org.lwjgl.system.MemoryUtil.*
import org.lwjgl.system.Pointer
import java.nio.ByteBuffer
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

interface vrInterface {

    val version: String

    /** Returns whether the interface of the specified version exists.     */
    val isInterfaceVersionValid: Boolean
        get() = nVR_IsInterfaceVersionValid(addressOfAscii("FnTable:$version"))
}

inline val Pointer.adr: Ptr
    get() = address()

fun bufferOfAscii(string: String, nullTerminated: Boolean = true): ByteBuffer {
    val bytes = stackGet().malloc(string.length + if (nullTerminated) 1 else 0)
    for (i in string.indices)
        bytes[i] = string[i].b
    if (nullTerminated)
        bytes[string.length] = 0
    return bytes
}

// TODO change String's to CharSequence's
fun MemoryStack.addressOfAscii(chars: CharSequence, nullTerminated: Boolean = true): Adr {
    val adr = nmalloc(1, chars.length + if (nullTerminated) 1 else 0)
    for (i in chars.indices)
        memPutByte(adr + i, chars[i].b)
    if (nullTerminated)
        memPutByte(adr + chars.length, 0)
    return adr
}

fun addressOfAscii(chars: CharSequence, nullTerminated: Boolean = true): Adr = stackGet().addressOfAscii(chars, nullTerminated)

inline val <SELF : CustomBuffer<SELF>>CustomBuffer<SELF>.rem
    get() = remaining()

inline fun <R> longAddress(block: (Adr) -> R): Long {
    val adr = stackGet().nmalloc(1, Long.BYTES)
    block(adr)
    return memGetLong(adr)
}

inline fun <R> intAddress(block: (Adr) -> R): Int {
    val adr = stackGet().nmalloc(1, Int.BYTES)
    block(adr)
    return memGetInt(adr)
}

// TODO -> uno
const val NUL = '\u0000'

inline val pInt: Adr
    get() = stackGet().nmalloc(1, Int.BYTES)

fun pIntOf(int: Int): Adr =
        pInt.also {
            memPutInt(it, int)
        }

operator fun <R> KMutableProperty0<R>.setValue(host: Any?, property: KProperty<*>, value: R) = set(value)
operator fun <R> KMutableProperty0<R>.getValue(host: Any?, property: KProperty<*>): R = get()