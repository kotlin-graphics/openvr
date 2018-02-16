package openvr.lib

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.IntByReference
import glm_.vec4.Vec4i
import java.util.*

// ivrextendeddisplay.h ===========================================================================================================================================

/** NOTE: Use of this interface is not recommended in production applications. It will not work for displays which use
 *  direct-to-display mode. Creating our own window is also incompatible with the VR compositor and is not available when the compositor is running. */
open class IVRExtendedDisplay : Structure {

    /** Size and position that the window needs to be on the VR display. */
    infix fun getWindowBounds(bounds: Vec4i) {
        val x = IntByReference()
        val y = IntByReference()
        val width = IntByReference()
        val height = IntByReference()
        GetWindowBounds!!(x, y, width, height)
        bounds.put(x.value, y.value, width.value, height.value)
    }
    fun getWindowBounds(x: IntByReference, y: IntByReference, width: IntByReference, height: IntByReference) = GetWindowBounds!!(x, y, width, height)

    @JvmField var GetWindowBounds: GetWindowBounds_callback? = null

    interface GetWindowBounds_callback : Callback {
        operator fun invoke(pnX: IntByReference, pnY: IntByReference, pnWidth: IntByReference, pnHeight: IntByReference)
    }

    /** Gets the viewport in the frame buffer to draw the output of the distortion into */
    fun getEyeOutputViewport(eye: EVREye, viewport: Vec4i) {
        val x = IntByReference()
        val y = IntByReference()
        val width = IntByReference()
        val height = IntByReference()
        GetEyeOutputViewport!!(eye.i, x, y, width, height)
        viewport.put(x.value, y.value, width.value, height.value)
    }
    fun getEyeOutputViewport(eye: EVREye, x: IntByReference, y: IntByReference, width: IntByReference, height: IntByReference) = GetEyeOutputViewport!!(eye.i, x, y, width, height)

    @JvmField var GetEyeOutputViewport: GetEyeOutputViewport_callback? = null

    interface GetEyeOutputViewport_callback : Callback {
        operator fun invoke(eEye: Int, pnX: IntByReference, pnY: IntByReference, pnWidth: IntByReference, pnHeight: IntByReference)
    }

    /** [D3D10/11 Only]
     *  Returns the adapter index and output index that the user should pass into EnumAdapters and EnumOutputs to create the device and swap chain in DX10 and
     *  DX11. If an error occurs both indices will be set to -1.     */
    fun getDXGIOutputInfo(adapterIndex: IntByReference, adapterOutputIndex: IntByReference) = GetDXGIOutputInfo!!(adapterIndex, adapterOutputIndex)

    @JvmField var GetDXGIOutputInfo: GetDXGIOutputInfo_callback? = null

    interface GetDXGIOutputInfo_callback : Callback {
        operator fun invoke(pnAdapterIndex: IntByReference, pnAdapterOutputIndex: IntByReference)
    }

    constructor()

    override fun getFieldOrder(): List<String> = Arrays.asList("GetWindowBounds", "GetEyeOutputViewport", "GetDXGIOutputInfo")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRExtendedDisplay(), Structure.ByReference
    class ByValue : IVRExtendedDisplay(), Structure.ByValue
}

val IVRExtendedDisplay_Version = "IVRExtendedDisplay_001"