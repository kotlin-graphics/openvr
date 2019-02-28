package openvr.lib

import glm_.b
import kool.Adr
import kool.Ptr
import kool.stak
import org.lwjgl.openvr.VR.nVR_IsInterfaceVersionValid
import org.lwjgl.system.CustomBuffer
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.memPutByte
import org.lwjgl.system.Pointer
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty

interface vrInterface {

    val version: String

    /** Returns whether the interface of the specified version exists.     */
    val isInterfaceVersionValid: Boolean
        get() = stak { nVR_IsInterfaceVersionValid(it.addressOfAscii("FnTable:$version")) }
}

inline val Pointer.adr: Ptr
    get() = address()

//fun bufferOfAscii(string: String, nullTerminated: Boolean = true): ByteBuffer {
//    val bytes = stackGet().malloc(string.length + if (nullTerminated) 1 else 0)
//    for (i in string.indices)
//        bytes[i] = string[i].b
//    if (nullTerminated)
//        bytes[string.length] = 0
//    return bytes
//}

// TODO change String's to CharSequence's
fun MemoryStack.addressOfAscii(chars: CharSequence, nullTerminated: Boolean = true): Adr {
    val adr = nmalloc(1, chars.length + if (nullTerminated) 1 else 0)
    for (i in chars.indices)
        memPutByte(adr + i, chars[i].b)
    if (nullTerminated)
        memPutByte(adr + chars.length, 0)
    return adr
}

inline val <SELF : CustomBuffer<SELF>>CustomBuffer<SELF>.rem
    get() = remaining()

// TODO -> uno
const val NUL = '\u0000'

operator fun <R> KMutableProperty0<R>.setValue(host: Any?, property: KProperty<*>, value: R) = set(value)
operator fun <R> KMutableProperty0<R>.getValue(host: Any?, property: KProperty<*>): R = get()