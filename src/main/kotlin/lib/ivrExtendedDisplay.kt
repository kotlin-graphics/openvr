package lib

import glm_.BYTES
import glm_.vec2.Vec2i
import kool.adr
import org.lwjgl.openvr.VRExtendedDisplay.*
import org.lwjgl.system.MemoryStack.stackGet
import org.lwjgl.system.MemoryUtil.memGetInt
import java.nio.IntBuffer

object vrExtendedDisplay : vrInterface {

    /** Size and position that the window needs to be on the VR display. */
    fun getWindowBounds(pos: Vec2i, size: Vec2i) {
        val x = stackGet().nmalloc(1, Vec2i.size * 2)
        val y = x + Int.BYTES
        val width = y + Int.BYTES
        val height = width + Int.BYTES
        nVRExtendedDisplay_GetWindowBounds(x, y, width, height)
        pos.put(memGetInt(x), memGetInt(y))
        size.put(memGetInt(width), memGetInt(height))
    }

    /** Gets the viewport in the frame buffer to draw the output of the distortion into. */
    fun getEyeOutputViewport(eye: VREye, pos: Vec2i, size: Vec2i) {
        val x = stackGet().nmalloc(1, Vec2i.size * 2)
        val y = x + Int.BYTES
        val width = y + Int.BYTES
        val height = width + Int.BYTES
        nVRExtendedDisplay_GetEyeOutputViewport(eye.i, x, y, width, height)
        pos.put(memGetInt(x), memGetInt(y))
        size.put(memGetInt(width), memGetInt(height))
    }

    /**
     * <h3>D3D10/11 Only</h3>
     *
     * <p>Returns the adapter index and output index that the user should pass into {@code EnumAdapters} and {@code EnumOutputs} to create the device and swap
     * chain in DX10 and DX11. If an error occurs both indices will be set to -1.</p>
     */
    fun getDXGIOutputInfo(adapterIndex: IntBuffer, adapterOutputIndex: IntBuffer) =
            nVRExtendedDisplay_GetDXGIOutputInfo(adapterIndex.adr, adapterOutputIndex.adr)

    override val version: String
        get() = "IVRExtendedDisplay_001"
}