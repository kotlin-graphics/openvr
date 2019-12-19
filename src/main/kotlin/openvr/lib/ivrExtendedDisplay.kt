package openvr.lib

import glm_.vec2.Vec2i
import kool.adr
import kool.mInt
import org.lwjgl.openvr.VRExtendedDisplay.*
import org.lwjgl.system.MemoryUtil.memGetInt
import java.nio.IntBuffer

object vrExtendedDisplay : vrInterface {

    /** Size and position that the window needs to be on the VR display. */
    fun getWindowBounds(pos: Vec2i, size: Vec2i) =
            stak {
                val b = it.mInt(4)
                nVRExtendedDisplay_GetWindowBounds(b.adr, (b + 1).adr, (b + 2).adr, (b + 3).adr)
                pos.put(b[0], b[1])
                size.put(b[2], b[3])
            }

    /** Gets the viewport in the frame buffer to draw the output of the distortion into. */
    fun getEyeOutputViewport(eye: VREye, pos: Vec2i, size: Vec2i) =
            stak {
                val v = it.mInt(4)
                nVRExtendedDisplay_GetEyeOutputViewport(eye.i, v.adr, (v + 1).adr, (v + 2).adr, (v + 3).adr)
                pos.put(v[0], v[1])
                size.put(v[2], v[3])
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