package vr_

import ab.appBuffer
import glm_.buffer.adr
import org.lwjgl.openvr.OpenVR.IVRIOBuffer
import org.lwjgl.openvr.VRIOBuffer
import org.lwjgl.system.NativeType
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.nio.LongBuffer

/**
 * Opens an existing or creates a new {@code IOBuffer} of {@code unSize} bytes.
 *
 * @param mode one of:<br><table><tr><td>{@link VR#EIOBufferMode_IOBufferMode_Read}</td><td>{@link VR#EIOBufferMode_IOBufferMode_Write}</td></tr><tr><td>{@link VR#EIOBufferMode_IOBufferMode_Create}</td></tr></table>
 * @param buffer ~ IOBufferHandle *
 */
fun IVRIOBuffer.open(path: String, mode: EIOBufferMode, elementSize: Int, elements: Int, buffer: LongBuffer): EIOBufferError {
    val pathEncoded = appBuffer.bufferOfAscii(path)
    return EIOBufferError of VRIOBuffer.nVRIOBuffer_Open(pathEncoded.adr, mode.i, elementSize, elements, buffer.adr)
}

/** Closes a previously opened or created buffer. */
infix fun IVRIOBuffer.close(buffer: IOBufferHandle): EIOBufferError {
    return EIOBufferError of VRIOBuffer.VRIOBuffer_Close(buffer)
}

/** Reads up to {@code unBytes} from buffer into {@code *dst}, returning number of bytes read in {@code *read} */
fun IVRIOBuffer.read(buffer: IOBufferHandle, dst: ByteBuffer, @NativeType("uint32_t *") read: IntBuffer): EIOBufferError {
    return EIOBufferError of VRIOBuffer.nVRIOBuffer_Read(buffer, dst.adr, dst.remaining(), read.adr);
}

/** Writes {@code unBytes} of data from {@code *src} into a buffer. */
fun IVRIOBuffer.write(buffer: IOBufferHandle, @NativeType("void *") src: ByteBuffer): EIOBufferError {
    return EIOBufferError of VRIOBuffer.nVRIOBuffer_Write(buffer, src.adr, src.remaining())
}

/** Retrieves the property container of a buffer. */
infix fun IVRIOBuffer.propertyContainer(buffer: IOBufferHandle): PropertyContainerHandle {
    return VRIOBuffer.VRIOBuffer_PropertyContainer(buffer)
}