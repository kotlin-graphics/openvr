package lib

import kool.adr
import kool.rem
import org.lwjgl.openvr.VRIOBuffer.*
import org.lwjgl.system.MemoryUtil.NULL
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.nio.LongBuffer

object vrIOBuffer : vrInterface {

    const val invalidHandle = NULL

    enum class Error(@JvmField val i: Int) {
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

    enum class Mode(@JvmField val i: Int) {
        Read(0x0001),
        Write(0x0002),
        Create(0x0200);

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    /**
     * Opens an existing or creates a new {@code IOBuffer} of {@code unSize} bytes.
     *
     * @param mode one of:<br><table><tr><td>{@link VR#EIOBufferMode_IOBufferMode_Read}</td><td>{@link VR#EIOBufferMode_IOBufferMode_Write}</td></tr><tr><td>{@link VR#EIOBufferMode_IOBufferMode_Create}</td></tr></table>
     * @param buffer ~ IOBufferHandle *
     */
    fun open(path: String, mode: Mode, elementSize: Int, elements: Int, buffer: LongBuffer): Error =
            Error of nVRIOBuffer_Open(addressOfAscii(path), mode.i, elementSize, elements, buffer.adr)

    /** Closes a previously opened or created buffer. */
    infix fun close(buffer: IOBufferHandle): Error = Error of VRIOBuffer_Close(buffer)

    /** Reads up to {@code unBytes} from buffer into {@code *dst}, returning number of bytes read in {@code *read} */
    fun read(buffer: IOBufferHandle, dst: ByteBuffer, read: IntBuffer): Error =
            Error of nVRIOBuffer_Read(buffer, dst.adr, dst.rem, read.adr)

    /** Writes {@code unBytes} of data from {@code *src} into a buffer. */
    fun write(buffer: IOBufferHandle, src: ByteBuffer): Error = Error of nVRIOBuffer_Write(buffer, src.adr, src.rem)

    /** Retrieves the property container of a buffer. */
    infix fun propertyContainer(buffer: IOBufferHandle): PropertyContainerHandle = VRIOBuffer_PropertyContainer(buffer)

    override val version: String
        get() = "IVRIOBuffer_001"
}