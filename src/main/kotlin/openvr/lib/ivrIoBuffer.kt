package openvr.lib

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.IntByReference
import glm_.size
import java.nio.ByteBuffer

typealias IOBufferHandle = Long

const val invalidIOBufferHandle = 0

enum class EIOBufferError(val i: Int) {
    Success(0),
    OperationFailed(100),
    InvalidHandle(101),
    InvalidArgument(102),
    PathExists(103),
    PathDoesNotExist(104),
    Permission(105);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EIOBufferMode(val i: Int) {
    Read(0x0001),
    Write(0x0002),
    Create(0x0200);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

open class IVRIOBuffer : Structure {

    /** opens an existing or creates a new IOBuffer of unSize bytes */
    fun open(path: String, mode: EIOBufferError, elementSize: Int, elements: Int, buffer: IOBufferHandle): EIOBufferError {
        return EIOBufferError of Open!!(path, mode.i, elementSize, elements, buffer)
    }

    @JvmField
    var Open: Open_callback? = null

    interface Open_callback : Callback {
        operator fun invoke(pchPath: String, mode: Int, unElementSize: Int, unElements: Int, pulBuffer: IOBufferHandle): Int
    }

    /** closes a previously opened or created buffer */
    fun close(buffer: IOBufferHandle): EIOBufferError {
        return EIOBufferError of Close!!(buffer)
    }

    @JvmField
    var Close: Close_callback? = null

    interface Close_callback : Callback {
        operator fun invoke(ulBuffer: IOBufferHandle): Int
    }

    /** reads up to unBytes from buffer into *pDst, returning number of bytes read in *punRead */
    fun read(buffer: IOBufferHandle, dst: ByteBuffer, read: IntByReference): EIOBufferError {
        return EIOBufferError of Read!!(buffer, dst, dst.size, read)
    }

    @JvmField
    var Read: Read_callback? = null

    interface Read_callback : Callback {
        operator fun invoke(ulBuffer: IOBufferHandle, pDst: ByteBuffer, unBytes: Int, punRead: IntByReference): Int
    }

    /** writes unBytes of data from *pSrc into a buffer. */
    fun write(buffer: IOBufferHandle, src: ByteBuffer): EIOBufferError {
        return EIOBufferError of Write!!(buffer, src, src.size)
    }

    @JvmField
    var Write: Write_callback? = null

    interface Write_callback : Callback {
        operator fun invoke(ulBuffer: IOBufferHandle, pSrc: ByteBuffer, unBytes: Int): Int
    }

    /** retrieves the property container of an buffer. */
    fun propertyContainer(buffer: IOBufferHandle): PropertyContainerHandle {
        return PropertyContainer!!(buffer)
    }

    @JvmField
    var PropertyContainer: PropertyContainer_callback? = null

    interface PropertyContainer_callback : Callback {
        operator fun invoke(ulBuffer: IOBufferHandle): PropertyContainerHandle
    }

    constructor()

    override fun getFieldOrder() = listOf("Open", "Close", "Read", "Write", "PropertyContainer")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRIOBuffer(), Structure.ByReference
    class ByValue : IVRIOBuffer(), Structure.ByValue
}

const val IVRIOBuffer_Version = "IVRIOBuffer_001"