package openvr

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.Union
import com.sun.jna.ptr.ByteByReference
import com.sun.jna.ptr.FloatByReference
import com.sun.jna.ptr.IntByReference
import com.sun.jna.ptr.PointerByReference
import main.BYTES
import java.util.*

// ivroverlay.h ===================================================================================================================================================

/** The maximum length of an overlay key in bytes, counting the terminating null character. */
val k_unVROverlayMaxKeyLength = 128

/** The maximum length of an overlay name in bytes, counting the terminating null character. */
val k_unVROverlayMaxNameLength = 128

/** The maximum number of overlays that can exist in the system at one time. */
val k_unMaxOverlayCount = 64

/** The maximum number of overlay intersection mask primitives per overlay */
val k_unMaxOverlayIntersectionMaskPrimitivesCount = 32

/** Types of input supported by VR Overlays */
enum class VROverlayInputMethod(@JvmField val i: Int) {

    None(0), //    No input events will be generated automatically for this overlay
    Mouse(1); //   Tracked controllers will get mouse events automatically

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class VROverlayInputMethod_ByReference(var value: VROverlayInputMethod = VROverlayInputMethod.None) : IntByReference(value.i)

/** Allows the caller to figure out which overlay transform getter to call. */
enum class VROverlayTransformType(@JvmField val i: Int) {

    Absolute(0),
    TrackedDeviceRelative(1),
    SystemOverlay(2),
    TrackedComponent(3);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class VROverlayTransformType_ByReference(@JvmField var value: VROverlayTransformType = VROverlayTransformType.Absolute) : IntByReference(value.i)

/** Overlay control settings */
enum class VROverlayFlags(@JvmField val i: Int) {

    None(0),

    // The following only take effect when rendered using the high quality render path (see SetHighQualityOverlay).
    Curved(1),
    RGSS4X(2),

    // Set this flag on a dashboard overlay to prevent a tab from showing up for that overlay
    NoDashboardTab(3),

    // Set this flag on a dashboard that is able to deal with gamepad focus events
    AcceptsGamepadEvents(4),

    // Indicates that the overlay should dim/brighten to show gamepad focus
    ShowGamepadFocus(5),

    // When in VROverlayInputMethod_Mouse you can optionally enable sending VRScroll_t
    SendVRScrollEvents(6),
    SendVRTouchpadEvents(7),

    // If set this will render a vertical scroll wheel on the primary controller), only needed if not using SendVRScrollEvents but you still want
    // to represent a scroll wheel
    ShowTouchPadScrollWheel(8),

    // If this is set ownership and render access to the overlay are transferred to the new scene process on a call to openvr.IVRApplications::LaunchInternalProcess
    TransferOwnershipToInternalProcess(9),

    // If set), renders 50% of the texture in each eye), side by side
    SideBySide_Parallel(10), // Texture is left/right
    SideBySide_Crossed(11), // Texture is crossed and right/left

    Panorama(12), // Texture is a panorama
    StereoPanorama(13), // Texture is a stereo panorama

    // If this is set on an overlay owned by the scene application that overlay will be sorted with the "Other" overlays on top of all other scene overlays
    SortWithNonSceneOverlays(14),

    // If set, the overlay will be shown in the dashboard, otherwise it will be hidden.
    VisibleInDashboard(15);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

enum class VRMessageOverlayResponse(@JvmField val i: Int) {

    ButtonPress_0(0),
    ButtonPress_1(1),
    ButtonPress_2(2),
    ButtonPress_3(3),
    CouldntFindSystemOverlay(4),
    CouldntFindOrCreateClientOverlay(5),
    ApplicationQuit(6);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

open class VROverlayIntersectionParams_t : Structure {

    @JvmField var vSource = HmdVector3_t()
    @JvmField var vDirection = HmdVector3_t()
    @JvmField var eOrigin_internal = 0
    var eOrigin
        set(value) {
            eOrigin_internal = value.i
        }
        get() = ETrackingUniverseOrigin.of(eOrigin_internal)

    constructor()

    constructor(vSource: HmdVector3_t, vDirection: HmdVector3_t, eOrigin: ETrackingUniverseOrigin) {
        this.vSource = vSource
        this.vDirection = vDirection
        this.eOrigin_internal = eOrigin.i
    }

    override fun getFieldOrder(): List<String> = Arrays.asList("vPoint", "vNormal", "eOrigin_internal")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VROverlayIntersectionParams_t(), Structure.ByReference
    class ByValue : VROverlayIntersectionParams_t(), Structure.ByValue
}

open class VROverlayIntersectionResults_t : Structure {

    @JvmField var vPoint = HmdVector3_t()
    @JvmField var vNormal = HmdVector3_t()
    @JvmField var vUVs = HmdVector2_t()
    @JvmField var fDistance = 0f

    constructor()

    constructor(vPoint: HmdVector3_t, vNormal: HmdVector3_t, vUVs: HmdVector2_t, fDistance: Float) {
        this.vPoint = vPoint
        this.vNormal = vNormal
        this.vUVs = vUVs
        this.fDistance = fDistance
    }

    override fun getFieldOrder(): List<String> = Arrays.asList("vPoint", "vNormal", "vUVs", "fDistance")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VROverlayIntersectionResults_t(), Structure.ByReference
    class ByValue : VROverlayIntersectionResults_t(), Structure.ByValue
}

// Input modes for the Big Picture gamepad text entry
enum class EGamepadTextInputMode(@JvmField val i: Int) {

    Normal(0),
    Password(1),
    Submit(2);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

// Controls number of allowed lines for the Big Picture gamepad text entry
enum class EGamepadTextInputLineMode(@JvmField val i: Int) {

    SingleLine(0),
    MultipleLines(1);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Directions for changing focus between overlays with the gamepad */
enum class EOverlayDirection(@JvmField val i: Int) {

    Up(0),
    Down(1),
    Left(2),
    Right(3),

    Count(4);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EVROverlayIntersectionMaskPrimitiveType(@JvmField val i: Int) {

    Rectangle(0),
    Circle(1);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

open class IntersectionMaskRectangle_t : Structure {

    @JvmField var m_flTopLeftX = 0f
    @JvmField var m_flTopLeftY = 0f
    @JvmField var m_flWidth = 0f
    @JvmField var m_flHeight = 0f

    constructor()

    constructor(m_flTopLeftX: Float, m_flTopLeftY: Float, m_flWidth: Float, m_flHeight: Float) {
        this.m_flTopLeftX = m_flTopLeftX
        this.m_flTopLeftY = m_flTopLeftY
        this.m_flWidth = m_flWidth
        this.m_flHeight = m_flHeight
    }

    override fun getFieldOrder(): List<String> = Arrays.asList("m_flTopLeftX", "m_flTopLeftY", "m_flWidth", "m_flHeight")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IntersectionMaskRectangle_t(), Structure.ByReference
    class ByValue : IntersectionMaskRectangle_t(), Structure.ByValue
}

open class IntersectionMaskCircle_t : Structure {

    @JvmField var m_flCenterX = 0f
    @JvmField var m_flCenterY = 0f
    @JvmField var m_flRadius = 0f

    constructor()

    constructor(m_flCenterX: Float, m_flCenterY: Float, m_flRadius: Float) {
        this.m_flCenterX = m_flCenterX
        this.m_flCenterY = m_flCenterY
        this.m_flRadius = m_flRadius
    }

    override fun getFieldOrder(): List<String> = Arrays.asList("m_flCenterX", "m_flCenterY", "m_flRadius")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IntersectionMaskRectangle_t(), Structure.ByReference
    class ByValue : IntersectionMaskRectangle_t(), Structure.ByValue
}

/** NOTE!!! If you change this you MUST manually update openvr_interop.cs.py and openvr_api_flat.h.py */
abstract class VROverlayIntersectionMaskPrimitive_Data_t : Union() {

    class ByValue : VROverlayIntersectionMaskPrimitive_Data_t(), Structure.ByValue

    var m_Rectangle: IntersectionMaskRectangle_t? = null
    var m_Circle: IntersectionMaskCircle_t? = null
}

open class VROverlayIntersectionMaskPrimitive_t : Structure {

    @JvmField var m_nPrimitiveType = 0
    @JvmField var m_Primitive: VROverlayIntersectionMaskPrimitive_Data_t? = null

    constructor()

    constructor(m_nPrimitiveType: EVROverlayIntersectionMaskPrimitiveType, m_Primitive: VROverlayIntersectionMaskPrimitive_Data_t) :
            this(m_nPrimitiveType.i, m_Primitive)

    constructor(m_nPrimitiveType: Int, m_Primitive: VROverlayIntersectionMaskPrimitive_Data_t) {
        this.m_nPrimitiveType = m_nPrimitiveType
        this.m_Primitive = m_Primitive
    }

    override fun getFieldOrder(): List<String> = Arrays.asList("m_nPrimitiveType", "m_Primitive")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VROverlayIntersectionMaskPrimitive_t(), Structure.ByReference
    class ByValue : VROverlayIntersectionMaskPrimitive_t(), Structure.ByValue
}

open class IVROverlay : Structure {

    // ---------------------------------------------
    // Overlay management methods
    // ---------------------------------------------

    /** Finds an existing overlay with the specified key. */
    fun findOverlay(pchOverlayKey: String, pOverlayHandle: VROverlayHandle_t_ByReference) = EVROverlayError.of(FindOverlay!!.invoke(pchOverlayKey, pOverlayHandle))

    @JvmField var FindOverlay: IVROverlay.FindOverlay_callback? = null

    interface FindOverlay_callback : Callback {
        fun invoke(pchOverlayKey: String, pOverlayHandle: VROverlayHandle_t_ByReference): Int
    }

    /** Creates a new named overlay. All overlays start hidden and with default settings. */
    fun createOverlay(pchOverlayKey: String, pchOverlayFriendlyName: String, pOverlayHandle: VROverlayHandle_t_ByReference)
            = EVROverlayError.of(CreateOverlay!!.invoke(pchOverlayKey, pchOverlayFriendlyName, pOverlayHandle))

    @JvmField var CreateOverlay: IVROverlay.CreateOverlay_callback? = null

    interface CreateOverlay_callback : Callback {
        fun invoke(pchOverlayKey: String, pchOverlayFriendlyName: String, pOverlayHandle: VROverlayHandle_t_ByReference): Int
    }

    /** Destroys the specified overlay. When an application calls VR_Shutdown all overlays created by that app are automatically destroyed. */
    fun destroyOverlay(ulOverlayHandle: VROverlayHandle_t) = EVROverlayError.of(DestroyOverlay!!.invoke(ulOverlayHandle))

    @JvmField var DestroyOverlay: IVROverlay.DestroyOverlay_callback? = null

    interface DestroyOverlay_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t): Int
    }

    /** Specify which overlay to use the high quality render path.  This overlay will be composited in during the distortion pass which results in it drawing on
     *  top of everything else, but also at a higher quality as it samples the source texture directly rather than rasterizing into each eye's render texture
     *  first.  Because if this, only one of these is supported at any given time.  It is most useful for overlays that are expected to take up most of the
     *  user's view (e.g. streaming video).
     *  This mode does not support mouse input to your overlay. */
    fun setHighQualityOverlay(ulOverlayHandle: VROverlayHandle_t) = EVROverlayError.of(SetHighQualityOverlay!!.invoke(ulOverlayHandle))

    @JvmField var SetHighQualityOverlay: IVROverlay.SetHighQualityOverlay_callback? = null

    interface SetHighQualityOverlay_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t): Int
    }

    /** Returns the overlay handle of the current overlay being rendered using the single high quality overlay render path.
     *  Otherwise it will return openvr.k_ulOverlayHandleInvalid. */
    fun getHighQualityOverlay() = GetHighQualityOverlay!!.invoke()

    @JvmField var GetHighQualityOverlay: IVROverlay.GetHighQualityOverlay_callback? = null

    interface GetHighQualityOverlay_callback : Callback {
        fun invoke(): VROverlayHandle_t
    }

    /** Fills the provided buffer with the string key of the overlay. Returns the size of buffer required to store the key, including
     *  the terminating null character. openvr.k_unVROverlayMaxKeyLength will be enough bytes to fit the string. */
    fun getOverlayKey(ulOverlayHandle: VROverlayHandle_t, pchValue: String, unBufferSize: Int,
                      pError: EVROverlayError_ByReference? = null)
            = GetOverlayKey!!.invoke(ulOverlayHandle, pchValue, unBufferSize, pError)

    @JvmField var GetOverlayKey: IVROverlay.GetOverlayKey_callback? = null

    interface GetOverlayKey_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pchValue: String, unBufferSize: Int, pError: EVROverlayError_ByReference? = null): Int
    }

    /** Fills the provided buffer with the friendly name of the overlay. Returns the size of buffer required to store the key, including the terminating null
     *  character. openvr.k_unVROverlayMaxNameLength will be enough bytes to fit the string. */
    // TODO add enhance -> return string directly
    fun getOverlayName(ulOverlayHandle: VROverlayHandle_t, pchValue: String, unBufferSize: Int, pError: EVROverlayError_ByReference? = null)
            = GetOverlayName!!.invoke(ulOverlayHandle, pchValue, unBufferSize, pError)

    @JvmField var GetOverlayName: IVROverlay.GetOverlayName_callback? = null

    interface GetOverlayName_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pchValue: String, unBufferSize: Int, pError: EVROverlayError_ByReference? = null): Int
    }

    /** Gets the raw image data from an overlay. Overlay image data is always returned as RGBA data, 4 bytes per pixel. If the buffer is not large enough,
     *  width and height will be set and VROverlayError_ArrayTooSmall is returned. */
    fun getOverlayImageData(ulOverlayHandle: VROverlayHandle_t, pvBuffer: Pointer, unBufferSize: Int, punWidth: IntByReference, punHeight: IntByReference)
            = EVROverlayError.of(GetOverlayImageData!!.invoke(ulOverlayHandle, pvBuffer, unBufferSize, punWidth, punHeight))

    @JvmField var GetOverlayImageData: IVROverlay.GetOverlayImageData_callback? = null

    interface GetOverlayImageData_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pvBuffer: Pointer, unBufferSize: Int, punWidth: IntByReference, punHeight: IntByReference): Int
    }

    /** returns a string that corresponds with the specified overlay error. The string will be the name of the error value value for all valid error codes */
    fun getOverlayErrorNameFromEnum(error: EVROverlayError) = GetOverlayErrorNameFromEnum!!.invoke(error.i)

    @JvmField var GetOverlayErrorNameFromEnum: IVROverlay.GetOverlayErrorNameFromEnum_callback? = null

    interface GetOverlayErrorNameFromEnum_callback : Callback {
        fun invoke(error: Int): String
    }


    // ---------------------------------------------
    // Overlay rendering methods
    // ---------------------------------------------

    /** Sets the pid that is allowed to render to this overlay (the creator pid is always allow to render), by default this is the pid of the process that made
     *  the overlay */
    fun setOverlayRenderingPid(ulOverlayHandle: VROverlayHandle_t, unPID: Int) = EVROverlayError.of(SetOverlayRenderingPid!!.invoke(ulOverlayHandle, unPID))

    @JvmField var SetOverlayRenderingPid: IVROverlay.SetOverlayRenderingPid_callback? = null

    interface SetOverlayRenderingPid_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, unPID: Int): Int
    }

    /** Gets the pid that is allowed to render to this overlay */
    fun getOverlayRenderingPid(ulOverlayHandle: VROverlayHandle_t) = GetOverlayRenderingPid!!.invoke(ulOverlayHandle)

    @JvmField var GetOverlayRenderingPid: IVROverlay.GetOverlayRenderingPid_callback? = null

    interface GetOverlayRenderingPid_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t): Int
    }

    /** Specify flag setting for a given overlay */
    fun setOverlayFlag(ulOverlayHandle: VROverlayHandle_t, eOverlayFlag: VROverlayFlags, bEnabled: Boolean)
            = EVROverlayError.of(SetOverlayFlag!!.invoke(ulOverlayHandle, eOverlayFlag.i, bEnabled))

    @JvmField var SetOverlayFlag: IVROverlay.SetOverlayFlag_callback? = null

    interface SetOverlayFlag_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, eOverlayFlag: Int, bEnabled: Boolean): Int
    }

    /** Sets flag setting for a given overlay */
    fun getOverlayFlag(ulOverlayHandle: VROverlayHandle_t, eOverlayFlag: VROverlayFlags, pbEnabled: BooleanByReference)
            = EVROverlayError.of(GetOverlayFlag!!.invoke(ulOverlayHandle, eOverlayFlag.i, pbEnabled))

    @JvmField var GetOverlayFlag: IVROverlay.GetOverlayFlag_callback? = null

    interface GetOverlayFlag_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, eOverlayFlag: Int, pbEnabled: ByteByReference): Int
    }

    /** Sets the color tint of the overlay quad. Use 0.0 to 1.0 per channel. */
    fun setOverlayColor(ulOverlayHandle: VROverlayHandle_t, fRed: Float, fGreen: Float, fBlue: Float)
            = EVROverlayError.of(SetOverlayColor!!.invoke(ulOverlayHandle, fRed, fGreen, fBlue))

    @JvmField var SetOverlayColor: IVROverlay.SetOverlayColor_callback? = null

    interface SetOverlayColor_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, fRed: Float, fGreen: Float, fBlue: Float): Int
    }

    /** Gets the color tint of the overlay quad. */
    fun getOverlayColor(ulOverlayHandle: VROverlayHandle_t, pfRed: FloatByReference, pfGreen: FloatByReference, pfBlue: FloatByReference)
            = EVROverlayError.of(GetOverlayColor!!.invoke(ulOverlayHandle, pfRed, pfGreen, pfBlue))

    @JvmField var GetOverlayColor: IVROverlay.GetOverlayColor_callback? = null

    interface GetOverlayColor_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pfRed: FloatByReference, pfGreen: FloatByReference, pfBlue: FloatByReference): Int
    }

    /** Sets the alpha of the overlay quad. Use 1.0 for 100 percent opacity to 0.0 for 0 percent opacity. */
    fun setOverlayAlpha(ulOverlayHandle: VROverlayHandle_t, fAlpha: Float) = EVROverlayError.of(SetOverlayAlpha!!.invoke(ulOverlayHandle, fAlpha))

    @JvmField var SetOverlayAlpha: IVROverlay.SetOverlayAlpha_callback? = null

    interface SetOverlayAlpha_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, fAlpha: Float): Int
    }

    /** Gets the alpha of the overlay quad. By default overlays are rendering at 100 percent alpha (1.0). */
    fun getOverlayAlpha(ulOverlayHandle: VROverlayHandle_t, pfAlpha: FloatByReference) = EVROverlayError.of(GetOverlayAlpha!!.invoke(ulOverlayHandle, pfAlpha))

    @JvmField var GetOverlayAlpha: IVROverlay.GetOverlayAlpha_callback? = null

    interface GetOverlayAlpha_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pfAlpha: FloatByReference): Int
    }

    /** Sets the aspect ratio of the texels in the overlay. 1.0 means the texels are square. 2.0 means the texels are twice as wide as they are tall.
     *  Defaults to 1.0. */
    fun setOverlayTexelAspect(ulOverlayHandle: VROverlayHandle_t, fTexelAspect: Float)
            = EVROverlayError.of(SetOverlayTexelAspect!!.invoke(ulOverlayHandle, fTexelAspect))

    @JvmField var SetOverlayTexelAspect: SetOverlayTexelAspect_callback? = null

    interface SetOverlayTexelAspect_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, fTexelAspect: Float): Int
    }

    /** Gets the aspect ratio of the texels in the overlay. Defaults to 1.0 */
    fun getOverlayTexelAspect(ulOverlayHandle: VROverlayHandle_t, pfTexelAspect: FloatByReference)
            = EVROverlayError.of(GetOverlayTexelAspect!!.invoke(ulOverlayHandle, pfTexelAspect))

    @JvmField var GetOverlayTexelAspect: GetOverlayTexelAspect_callback? = null

    interface GetOverlayTexelAspect_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pfTexelAspect: FloatByReference): Int
    }

    /** Sets the rendering sort order for the overlay. Overlays are rendered this order:
     *      Overlays owned by the scene application
     *      Overlays owned by some other application
     *
     *	Within a category overlays are rendered lowest sort order to highest sort order. Overlays with the same
     *	sort order are rendered back to front base on distance from the HMD.
     *
     *	Sort order defaults to 0. */
    fun setOverlaySortOrder(ulOverlayHandle: VROverlayHandle_t, unSortOrder: Int) = EVROverlayError.of(SetOverlaySortOrder!!.invoke(ulOverlayHandle, unSortOrder))

    @JvmField var SetOverlaySortOrder: SetOverlaySortOrder_callback? = null

    interface SetOverlaySortOrder_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, unSortOrder: Int): Int
    }

    /** Gets the sort order of the overlay. See SetOverlaySortOrder for how this works. */
    fun getOverlaySortOrder(ulOverlayHandle: VROverlayHandle_t, punSortOrder: IntByReference)
            = EVROverlayError.of(GetOverlaySortOrder!!.invoke(ulOverlayHandle, punSortOrder))

    @JvmField var GetOverlaySortOrder: GetOverlaySortOrder_callback? = null

    interface GetOverlaySortOrder_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, punSortOrder: IntByReference): Int
    }

    /** Sets the width of the overlay quad in meters. By default overlays are rendered on a quad that is 1 meter across */
    fun setOverlayWidthInMeters(ulOverlayHandle: VROverlayHandle_t, fWidthInMeters: Float)
            = EVROverlayError.of(SetOverlayWidthInMeters!!.invoke(ulOverlayHandle, fWidthInMeters))

    @JvmField var SetOverlayWidthInMeters: SetOverlayWidthInMeters_callback? = null

    interface SetOverlayWidthInMeters_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, fWidthInMeters: Float): Int
    }

    /** Returns the width of the overlay quad in meters. By default overlays are rendered on a quad that is 1 meter across */
    fun getOverlayWidthInMeters(ulOverlayHandle: VROverlayHandle_t, pfWidthInMeters: FloatByReference)
            = EVROverlayError.of(GetOverlayWidthInMeters!!.invoke(ulOverlayHandle, pfWidthInMeters))

    @JvmField var GetOverlayWidthInMeters: GetOverlayWidthInMeters_callback? = null

    interface GetOverlayWidthInMeters_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pfWidthInMeters: FloatByReference): Int
    }

    /** For high-quality curved overlays only, sets the distance range in meters from the overlay used to automatically curve the surface around the viewer.
     *  Min is distance is when the surface will be most curved.  Max is when least curved. */
    fun setOverlayAutoCurveDistanceRangeInMeters(ulOverlayHandle: VROverlayHandle_t, fMinDistanceInMeters: Float, fMaxDistanceInMeters: Float)
            = EVROverlayError.of(SetOverlayAutoCurveDistanceRangeInMeters!!.invoke(ulOverlayHandle, fMinDistanceInMeters, fMaxDistanceInMeters))

    @JvmField var SetOverlayAutoCurveDistanceRangeInMeters: SetOverlayAutoCurveDistanceRangeInMeters_callback? = null

    interface SetOverlayAutoCurveDistanceRangeInMeters_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, fMinDistanceInMeters: Float, fMaxDistanceInMeters: Float): Int
    }

    /** For high-quality curved overlays only, gets the distance range in meters from the overlay used to automatically curve the surface around the viewer.
     *  Min is distance is when the surface will be most curved.  Max is when least curved. */
    fun getOverlayAutoCurveDistanceRangeInMeters(ulOverlayHandle: VROverlayHandle_t, pfMinDistanceInMeters: FloatByReference,
                                                 pfMaxDistanceInMeters: FloatByReference)
            = EVROverlayError.of(GetOverlayAutoCurveDistanceRangeInMeters!!.invoke(ulOverlayHandle, pfMinDistanceInMeters, pfMaxDistanceInMeters))

    @JvmField var GetOverlayAutoCurveDistanceRangeInMeters: GetOverlayAutoCurveDistanceRangeInMeters_callback? = null

    interface GetOverlayAutoCurveDistanceRangeInMeters_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pfMinDistanceInMeters: FloatByReference, pfMaxDistanceInMeters: FloatByReference): Int
    }

    /** Sets the colorspace the overlay texture's data is in.  Defaults to 'auto'.
     *  If the texture needs to be resolved, you should call SetOverlayTexture with the appropriate colorspace instead. */
    fun setOverlayTextureColorSpace(ulOverlayHandle: VROverlayHandle_t, eTextureColorSpace: EColorSpace)
            = EVROverlayError.of(SetOverlayTextureColorSpace!!.invoke(ulOverlayHandle, eTextureColorSpace.i))

    @JvmField var SetOverlayTextureColorSpace: SetOverlayTextureColorSpace_callback? = null

    interface SetOverlayTextureColorSpace_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, eTextureColorSpace: Int): Int
    }

    /** Gets the overlay's current colorspace setting. */
    fun getOverlayTextureColorSpace(ulOverlayHandle: VROverlayHandle_t, peTextureColorSpace: EColorSpace_ByReference)
            = EVROverlayError.of(GetOverlayTextureColorSpace!!.invoke(ulOverlayHandle, peTextureColorSpace))

    @JvmField var GetOverlayTextureColorSpace: GetOverlayTextureColorSpace_callback? = null

    interface GetOverlayTextureColorSpace_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, peTextureColorSpace: EColorSpace_ByReference): Int
    }

    /** Sets the part of the texture to use for the overlay. UV Min is the upper left corner and UV Max is the lower right corner. */
    fun setOverlayTextureBounds(ulOverlayHandle: VROverlayHandle_t, pOverlayTextureBounds: VRTextureBounds_t.ByReference)
            = EVROverlayError.of(SetOverlayTextureBounds!!.invoke(ulOverlayHandle, pOverlayTextureBounds))

    @JvmField var SetOverlayTextureBounds: SetOverlayTextureBounds_callback? = null

    interface SetOverlayTextureBounds_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pOverlayTextureBounds: VRTextureBounds_t.ByReference): Int
    }

    /** Gets the part of the texture to use for the overlay. UV Min is the upper left corner and UV Max is the lower right corner. */
    fun getOverlayTextureBounds(ulOverlayHandle: VROverlayHandle_t, pOverlayTextureBounds: VRTextureBounds_t.ByReference)
            = EVROverlayError.of(GetOverlayTextureBounds!!.invoke(ulOverlayHandle, pOverlayTextureBounds))

    @JvmField var GetOverlayTextureBounds: GetOverlayTextureBounds_callback? = null

    interface GetOverlayTextureBounds_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pOverlayTextureBounds: VRTextureBounds_t.ByReference): Int
    }

    /** Returns the transform value of this overlay. */
    fun getOverlayTransformType(ulOverlayHandle: VROverlayHandle_t, peTransformType: VROverlayTransformType_ByReference)
            = EVROverlayError.of(GetOverlayTransformType!!.invoke(ulOverlayHandle, peTransformType))

    @JvmField var GetOverlayTransformType: GetOverlayTransformType_callback? = null

    interface GetOverlayTransformType_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, peTransformType: VROverlayTransformType_ByReference): Int
    }

    /** Sets the transform to absolute tracking origin. */
    fun setOverlayTransformAbsolute(ulOverlayHandle: VROverlayHandle_t, eTrackingOrigin: ETrackingUniverseOrigin,
                                    pmatTrackingOriginToOverlayTransform: HmdMatrix34_t.ByReference)
            = EVROverlayError.of(SetOverlayTransformAbsolute!!.invoke(ulOverlayHandle, eTrackingOrigin.i, pmatTrackingOriginToOverlayTransform))

    @JvmField var SetOverlayTransformAbsolute: SetOverlayTransformAbsolute_calback? = null

    interface SetOverlayTransformAbsolute_calback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, eTrackingOrigin: Int, pmatTrackingOriginToOverlayTransform: HmdMatrix34_t.ByReference): Int
    }

    /** Gets the transform if it is absolute. Returns an error if the transform is some other value. */
    fun getOverlayTransformAbsolute(ulOverlayHandle: VROverlayHandle_t, peTrackingOrigin: ETrackingUniverseOrigin_ByReference,
                                    pmatTrackingOriginToOverlayTransform: HmdMatrix34_t.ByReference)
            = EVROverlayError.of(GetOverlayTransformAbsolute!!.invoke(ulOverlayHandle, peTrackingOrigin, pmatTrackingOriginToOverlayTransform))

    @JvmField var GetOverlayTransformAbsolute: GetOverlayTransformAbsolute_callback? = null

    interface GetOverlayTransformAbsolute_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, peTrackingOrigin: ETrackingUniverseOrigin_ByReference,
                   pmatTrackingOriginToOverlayTransform: HmdMatrix34_t.ByReference): Int
    }

    /** Sets the transform to relative to the transform of the specified tracked device. */
    fun setOverlayTransformTrackedDeviceRelative(ulOverlayHandle: VROverlayHandle_t, unTrackedDevice: TrackedDeviceIndex_t,
                                                 pmatTrackedDeviceToOverlayTransform: HmdMatrix34_t.ByReference)
            = EVROverlayError.of(SetOverlayTransformTrackedDeviceRelative!!.invoke(ulOverlayHandle, unTrackedDevice, pmatTrackedDeviceToOverlayTransform))

    @JvmField var SetOverlayTransformTrackedDeviceRelative: SetOverlayTransformTrackedDeviceRelative_callback? = null

    interface SetOverlayTransformTrackedDeviceRelative_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, unTrackedDevice: TrackedDeviceIndex_t, pmatTrackedDeviceToOverlayTransform: HmdMatrix34_t.ByReference): Int
    }

    /** Gets the transform if it is relative to a tracked device. Returns an error if the transform is some other value. */
    fun getOverlayTransformTrackedDeviceRelative(ulOverlayHandle: VROverlayHandle_t, punTrackedDevice: TrackedDeviceIndex_t_ByReference,
                                                 pmatTrackedDeviceToOverlayTransform: HmdMatrix34_t.ByReference)
            = ETrackedDeviceClass.of(GetOverlayTransformTrackedDeviceRelative!!.invoke(ulOverlayHandle, punTrackedDevice, pmatTrackedDeviceToOverlayTransform))

    @JvmField var GetOverlayTransformTrackedDeviceRelative: GetOverlayTransformTrackedDeviceRelative_callback? = null

    interface GetOverlayTransformTrackedDeviceRelative_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, punTrackedDevice: TrackedDeviceIndex_t_ByReference,
                   pmatTrackedDeviceToOverlayTransform: HmdMatrix34_t.ByReference): Int
    }

    /** Sets the transform to draw the overlay on a rendermodel component mesh instead of a quad. This will only draw when the system is drawing the device.
     *  Overlays with this transform value cannot receive mouse events. */
    fun setOverlayTransformTrackedDeviceComponent(ulOverlayHandle: VROverlayHandle_t, unDeviceIndex: TrackedDeviceIndex_t, pchComponentName: String)
            = EVROverlayError.of(SetOverlayTransformTrackedDeviceComponent!!.invoke(ulOverlayHandle, unDeviceIndex, pchComponentName))

    @JvmField var SetOverlayTransformTrackedDeviceComponent: SetOverlayTransformTrackedDeviceComponent_callback? = null

    interface SetOverlayTransformTrackedDeviceComponent_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, unDeviceIndex: TrackedDeviceIndex_t, pchComponentName: String): Int
    }

    /** Gets the transform information when the overlay is rendering on a component. */
    fun getOverlayTransformTrackedDeviceComponent(ulOverlayHandle: VROverlayHandle_t, punDeviceIndex: TrackedDeviceIndex_t_ByReference, pchComponentName: String,
                                                  unComponentNameSize: Int)
            = EVROverlayError.of(GetOverlayTransformTrackedDeviceComponent!!.invoke(ulOverlayHandle, punDeviceIndex, pchComponentName, unComponentNameSize))

    @JvmField var GetOverlayTransformTrackedDeviceComponent: GetOverlayTransformTrackedDeviceComponent_callback? = null

    interface GetOverlayTransformTrackedDeviceComponent_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, punDeviceIndex: TrackedDeviceIndex_t_ByReference, pchComponentName: String, unComponentNameSize: Int): Int
    }

    /** Shows the VR overlay.  For dashboard overlays, only the Dashboard Manager is allowed to call this. */
    fun showOverlay(ulOverlayHandle: VROverlayHandle_t) = EVROverlayError.of(ShowOverlay!!.invoke(ulOverlayHandle))

    @JvmField var ShowOverlay: ShowOverlay_callback? = null

    interface ShowOverlay_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t): Int
    }

    /** Hides the VR overlay.  For dashboard overlays, only the Dashboard Manager is allowed to call this. */
    fun hideOverlay(ulOverlayHandle: VROverlayHandle_t) = EVROverlayError.of(HideOverlay!!.invoke(ulOverlayHandle))

    @JvmField var HideOverlay: HideOverlay_callback? = null

    interface HideOverlay_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t): Int
    }

    /** Returns true if the overlay is visible. */
    fun isOverlayVisible(ulOverlayHandle: VROverlayHandle_t) = IsOverlayVisible!!.invoke(ulOverlayHandle)

    @JvmField var IsOverlayVisible: IsOverlayVisible_callback? = null

    interface IsOverlayVisible_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t): Boolean
    }

    /** Get the transform in 3d space associated with a specific 2d point in the overlay's coordinate space (where 0,0 is the lower left). -Z points out of
     *  the overlay */
    fun getTransformForOverlayCoordinates(ulOverlayHandle: VROverlayHandle_t, eTrackingOrigin: ETrackingUniverseOrigin, coordinatesInOverlay: HmdVector2_t,
                                          pmatTransform: HmdMatrix34_t.ByReference)
            = EVROverlayError.of(GetTransformForOverlayCoordinates!!.invoke(ulOverlayHandle, eTrackingOrigin.i, coordinatesInOverlay, pmatTransform))

    @JvmField var GetTransformForOverlayCoordinates: GetTransformForOverlayCoordinates_callback? = null

    interface GetTransformForOverlayCoordinates_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, eTrackingOrigin: Int, coordinatesInOverlay: HmdVector2_t, pmatTransform: HmdMatrix34_t.ByReference): Int
    }

    // ---------------------------------------------
    // Overlay input methods
    // ---------------------------------------------

    /** Returns true and fills the event with the next event on the overlay's event queue, if there is one.
     *  If there are no events this method returns false. uncbVREvent should be the size in bytes of the openvr.VREvent_t struct */
    fun pollNextOverlayEvent(ulOverlayHandle: VROverlayHandle_t, pEvent: VREvent_t.ByReference, uncbVREvent: Int)
            = PollNextOverlayEvent!!.invoke(ulOverlayHandle, pEvent, uncbVREvent)

    @JvmField var PollNextOverlayEvent: PollNextOverlayEvent_callback? = null

    interface PollNextOverlayEvent_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pEvent: VREvent_t.ByReference, uncbVREvent: Int): Boolean
    }

    /** Returns the current input settings for the specified overlay. */
    fun getOverlayInputMethod(ulOverlayHandle: VROverlayHandle_t, peInputMethod: VROverlayInputMethod_ByReference)
            = EVROverlayError.of(GetOverlayInputMethod!!.invoke(ulOverlayHandle, peInputMethod))

    @JvmField var GetOverlayInputMethod: GetOverlayInputMethod_callback? = null

    interface GetOverlayInputMethod_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, peInputMethod: VROverlayInputMethod_ByReference): Int
    }

    /** Sets the input settings for the specified overlay. */
    fun setOverlayInputMethod(ulOverlayHandle: VROverlayHandle_t, eInputMethod: VROverlayInputMethod)
            = EVROverlayError.of(SetOverlayInputMethod!!.invoke(ulOverlayHandle, eInputMethod.i))

    @JvmField var SetOverlayInputMethod: SetOverlayInputMethod_callback? = null

    interface SetOverlayInputMethod_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, eInputMethod: Int): Int
    }

    /** Gets the mouse scaling factor that is used for mouse events. The actual texture may be a different size, but this is typically the size of the
     *  underlying UI in pixels. */
    fun getOverlayMouseScale(ulOverlayHandle: VROverlayHandle_t, pvecMouseScale: HmdVector2_t.ByReference)
            = EVROverlayError.of(GetOverlayMouseScale!!.invoke(ulOverlayHandle, pvecMouseScale))

    @JvmField var GetOverlayMouseScale: GetOverlayMouseScale_callback? = null

    interface GetOverlayMouseScale_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pvecMouseScale: HmdVector2_t.ByReference): Int
    }

    /** Sets the mouse scaling factor that is used for mouse events. The actual texture may be a different size, but this is typically the size of the
     *  underlying UI in pixels (not in world space). */
    fun setOverlayMouseScale(ulOverlayHandle: VROverlayHandle_t, pvecMouseScale: HmdVector2_t.ByReference)
            = EVROverlayError.of(SetOverlayMouseScale!!.invoke(ulOverlayHandle, pvecMouseScale))

    @JvmField var SetOverlayMouseScale: SetOverlayMouseScale_callback? = null

    interface SetOverlayMouseScale_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pvecMouseScale: HmdVector2_t.ByReference): Int
    }

    /** Computes the overlay-space pixel coordinates of where the ray intersects the overlay with the specified settings. Returns false if there is no
     *  intersection. */
    fun computeOverlayIntersection(ulOverlayHandle: VROverlayHandle_t, pParams: VROverlayIntersectionParams_t.ByReference,
                                   pResults: VROverlayIntersectionResults_t.ByReference)
            = ComputeOverlayIntersection!!.invoke(ulOverlayHandle, pParams, pResults)

    @JvmField var ComputeOverlayIntersection: ComputeOverlayIntersection_callback? = null

    interface ComputeOverlayIntersection_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pParams: VROverlayIntersectionParams_t.ByReference, pResults: VROverlayIntersectionResults_t.ByReference)
                : Boolean
    }

    /** Processes mouse input from the specified controller as though it were a mouse pointed at a compositor overlay with the specified settings.
     *  The controller is treated like a laser pointer on the -z axis. The point where the laser pointer would intersect with the overlay is the mouse position,
     *  the trigger is left mouse, and the track pad is right mouse.
     *
     *  Return true if the controller is pointed at the overlay and an event was generated. */
    fun handleControllerOverlayInteractionAsMouse(ulOverlayHandle: VROverlayHandle_t, unControllerDeviceIndex: TrackedDeviceIndex_t)
            = HandleControllerOverlayInteractionAsMouse!!.invoke(ulOverlayHandle, unControllerDeviceIndex)

    @JvmField var HandleControllerOverlayInteractionAsMouse: HandleControllerOverlayInteractionAsMouse_callback? = null

    interface HandleControllerOverlayInteractionAsMouse_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, unControllerDeviceIndex: TrackedDeviceIndex_t): Boolean
    }

    /** Returns true if the specified overlay is the hover target. An overlay is the hover target when it is the last overlay "moused over" by the virtual mouse
     *  pointer */
    fun isHoverTargetOverlay(ulOverlayHandle: VROverlayHandle_t) = IsHoverTargetOverlay!!.invoke(ulOverlayHandle)

    @JvmField var IsHoverTargetOverlay: IsHoverTargetOverlay_callback? = null

    interface IsHoverTargetOverlay_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t): Boolean
    }

    /** Returns the current Gamepad focus overlay */
    fun getGamepadFocusOverlay() = GetGamepadFocusOverlay!!.invoke()

    @JvmField var GetGamepadFocusOverlay: GetGamepadFocusOverlay_callback? = null

    interface GetGamepadFocusOverlay_callback : Callback {
        fun invoke(): VROverlayHandle_t
    }

    /** Sets the current Gamepad focus overlay */
    fun setGamepadFocusOverlay(ulNewFocusOverlay: VROverlayHandle_t) = EVROverlayError.of(SetGamepadFocusOverlay!!.invoke(ulNewFocusOverlay))

    @JvmField var SetGamepadFocusOverlay: SetGamepadFocusOverlay_callback? = null

    interface SetGamepadFocusOverlay_callback : Callback {
        fun invoke(ulNewFocusOverlay: VROverlayHandle_t): Int
    }

    /** Sets an overlay's neighbor. This will also set the neighbor of the "to" overlay to point back to the "from" overlay. If an overlay's neighbor is set
     *  to invalid both ends will be cleared */
    fun setOverlayNeighbor(eDirection: EOverlayDirection, ulFrom: VROverlayHandle_t, ulTo: VROverlayHandle_t)
            = EVROverlayError.of(SetOverlayNeighbor!!.invoke(eDirection.i, ulFrom, ulTo))

    @JvmField var SetOverlayNeighbor: SetOverlayNeighbor_callback? = null

    interface SetOverlayNeighbor_callback : Callback {
        fun invoke(eDirection: Int, ulFrom: VROverlayHandle_t, ulTo: VROverlayHandle_t): Int
    }

    /** Changes the Gamepad focus from one overlay to one of its neighbors. Returns VROverlayError_NoNeighbor if there is no neighbor in that direction */
    fun moveGamepadFocusToNeighbor(eDirection: EOverlayDirection, ulFrom: VROverlayHandle_t)
            = EVROverlayError.of(MoveGamepadFocusToNeighbor!!.invoke(eDirection.i, ulFrom))

    @JvmField var MoveGamepadFocusToNeighbor: MoveGamepadFocusToNeighbor_callback? = null

    interface MoveGamepadFocusToNeighbor_callback : Callback {
        fun invoke(eDirection: Int, ulFrom: VROverlayHandle_t): Int
    }

    // ---------------------------------------------
    // Overlay texture methods
    // ---------------------------------------------

    /** Texture to draw for the overlay. This function can only be called by the overlay's creator or renderer process (see SetOverlayRenderingPid) .
     *
     *  OpenGL dirty state:
     *	    glBindTexture
     */
    fun setOverlayTexture(ulOverlayHandle: VROverlayHandle_t, pTexture: Texture_t.ByReference)
            = EVROverlayError.of(SetOverlayTexture!!.invoke(ulOverlayHandle, pTexture))

    @JvmField var SetOverlayTexture: SetOverlayTexture_callback? = null

    interface SetOverlayTexture_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pTexture: Texture_t.ByReference): Int
    }

    /** Use this to tell the overlay system to release the texture set for this overlay. */
    fun clearOverlayTexture(ulOverlayHandle: VROverlayHandle_t) = EVROverlayError.of(ClearOverlayTexture!!.invoke(ulOverlayHandle))

    @JvmField var ClearOverlayTexture: ClearOverlayTexture_callback? = null

    interface ClearOverlayTexture_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t): Int
    }

    /** Separate interface for providing the data as a stream of bytes, but there is an upper bound on data that can be sent.
     *  This function can only be called by the overlay's renderer process. */
    fun setOverlayRaw(ulOverlayHandle: VROverlayHandle_t, pvBuffer: Pointer, unWidth: Int, unHeight: Int, unDepth: Int)
            = EVROverlayError.of(SetOverlayRaw!!.invoke(ulOverlayHandle, pvBuffer, unWidth, unHeight, unDepth))

    @JvmField var SetOverlayRaw: SetOverlayRaw_callback? = null

    interface SetOverlayRaw_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pvBuffer: Pointer, unWidth: Int, unHeight: Int, unDepth: Int): Int
    }

    /** Separate interface for providing the image through a filename: can be png or jpg, and should not be bigger than 1920x1080.
     * This function can only be called by the overlay's renderer process */
    fun setOverlayFromFile(ulOverlayHandle: VROverlayHandle_t, pchFilePath: String) = EVROverlayError.of(SetOverlayFromFile!!.invoke(ulOverlayHandle, pchFilePath))

    @JvmField var SetOverlayFromFile: SetOverlayFromFile_callback? = null

    interface SetOverlayFromFile_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pchFilePath: String): Int
    }

    /** Get the native texture handle/device for an overlay you have created.
     *  On windows this handle will be a ID3D11ShaderResourceView with a ID3D11Texture2D bound.
     *
     *  The texture will always be sized to match the backing texture you supplied in SetOverlayTexture above.
     *
     *  You MUST call ReleaseNativeOverlayHandle() with pNativeTextureHandle once you are done with this texture.
     *
     *  pNativeTextureHandle is an OUTPUT, it will be a pointer to a ID3D11ShaderResourceView *.
     *  pNativeTextureRef is an INPUT and should be a ID3D11Resource *. The device used by pNativeTextureRef will be used to bind pNativeTextureHandle.     */
    fun getOverlayTexture(ulOverlayHandle: VROverlayHandle_t, pNativeTextureHandle: PointerByReference,
                          pNativeTextureRef: Pointer, pnWidth: IntByReference, pnHeight: IntByReference,
                          pNativeFormat: IntByReference, pAPIType: ETextureType_ByReference, pColorSpace: EColorSpace_ByReference,
                          pTextureBounds: VRTextureBounds_t.ByReference)
            = EVROverlayError.of(GetOverlayTexture!!.invoke(ulOverlayHandle, pNativeTextureHandle, pNativeTextureRef, pnWidth,
            pnHeight, pNativeFormat, pAPIType, pColorSpace, pTextureBounds))

    @JvmField var GetOverlayTexture: GetOverlayTexture_callback? = null

    interface GetOverlayTexture_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pNativeTextureHandle: PointerByReference, pNativeTextureRef: Pointer,
                   pWidth: IntByReference, pHeight: IntByReference, pNativeFormat: IntByReference, pAPIType: ETextureType_ByReference,
                   pColorSpace: EColorSpace_ByReference, pTextureBounds: VRTextureBounds_t.ByReference): Int
    }

    /** Release the pNativeTextureHandle provided from the GetOverlayTexture call, this allows the system to free the underlying GPU resources for this object,
     * so only do it once you stop rendering this texture.     */
    fun releaseNativeOverlayHandle(ulOverlayHandle: VROverlayHandle_t, pNativeTextureHandle: Pointer)
            = EVROverlayError.of(ReleaseNativeOverlayHandle!!.invoke(ulOverlayHandle, pNativeTextureHandle))

    @JvmField var ReleaseNativeOverlayHandle: ReleaseNativeOverlayHandle_callback? = null

    interface ReleaseNativeOverlayHandle_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pNativeTextureHandle: Pointer): Int
    }

    /** Get the size of the overlay texture */
    fun getOverlayTextureSize(ulOverlayHandle: VROverlayHandle_t, pnWidth: IntByReference, pnHeight: IntByReference)
            = EVROverlayError.of(GetOverlayTextureSize!!.invoke(ulOverlayHandle, pnWidth, pnHeight))

    @JvmField var GetOverlayTextureSize: GetOverlayTextureSize_callback? = null

    interface GetOverlayTextureSize_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pWidth: IntByReference, pHeight: IntByReference): Int
    }

    // ----------------------------------------------
    // Dashboard Overlay Methods
    // ----------------------------------------------

    /** Creates a dashboard overlay and returns its handle */
    fun createDashboardOverlay(pchOverlayKey: String, pchOverlayFriendlyName: String, pMainHandle: VROverlayHandle_t_ByReference,
                               pThumbnailHandle: VROverlayHandle_t_ByReference)
            = EVROverlayError.of(CreateDashboardOverlay!!.invoke(pchOverlayKey, pchOverlayFriendlyName, pMainHandle, pThumbnailHandle))

    @JvmField var CreateDashboardOverlay: CreateDashboardOverlay_callback? = null

    interface CreateDashboardOverlay_callback : Callback {
        fun invoke(pchOverlayKey: String, pchOverlayFriendlyName: String, pMainHandle: VROverlayHandle_t_ByReference,
                   pThumbnailHandle: VROverlayHandle_t_ByReference): Int
    }

    /** Returns true if the dashboard is visible */
    fun isDashboardVisible() = IsDashboardVisible!!.invoke()

    @JvmField var IsDashboardVisible: IsDashboardVisible_callback? = null

    interface IsDashboardVisible_callback : Callback {
        fun invoke(): Boolean
    }

    /** returns true if the dashboard is visible and the specified overlay is the active system Overlay */
    fun isActiveDashboardOverlay(ulOverlayHandle: VROverlayHandle_t) = IsActiveDashboardOverlay!!.invoke(ulOverlayHandle)

    @JvmField var IsActiveDashboardOverlay: IsActiveDashboardOverlay_callback? = null

    interface IsActiveDashboardOverlay_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t): Boolean
    }

    /** Sets the dashboard overlay to only appear when the specified process ID has scene focus */
    fun setDashboardOverlaySceneProcess(ulOverlayHandle: VROverlayHandle_t, unProcessId: Int)
            = EVROverlayError.of(SetDashboardOverlaySceneProcess!!.invoke(ulOverlayHandle, unProcessId))

    @JvmField var SetDashboardOverlaySceneProcess: SetDashboardOverlaySceneProcess_callback? = null

    interface SetDashboardOverlaySceneProcess_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, unProcessId: Int): Int
    }

    /** Gets the process ID that this dashboard overlay requires to have scene focus */
    fun getDashboardOverlaySceneProcess(ulOverlayHandle: VROverlayHandle_t, punProcessId: IntByReference)
            = EVROverlayError.of(GetDashboardOverlaySceneProcess!!.invoke(ulOverlayHandle, punProcessId))

    @JvmField var GetDashboardOverlaySceneProcess: GetDashboardOverlaySceneProcess_callback? = null

    interface GetDashboardOverlaySceneProcess_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, punProcessId: IntByReference): Int
    }

    /** Shows the dashboard. */
    fun showDashboard(pchOverlayToShow: String) = ShowDashboard!!.invoke(pchOverlayToShow)

    @JvmField var ShowDashboard: ShowDashboard_callback? = null

    interface ShowDashboard_callback : Callback {
        fun invoke(pchOverlayToShow: String)
    }

    /** Returns the tracked device that has the laser pointer in the dashboard */
    fun getPrimaryDashboardDevice() = GetPrimaryDashboardDevice!!.invoke()

    @JvmField var GetPrimaryDashboardDevice: GetPrimaryDashboardDevice_callback? = null

    interface GetPrimaryDashboardDevice_callback : Callback {
        fun invoke(): TrackedDeviceIndex_t
    }

    // ---------------------------------------------
    // Keyboard methods
    // ---------------------------------------------

    /** Show the virtual keyboard to accept input **/
    fun showKeyboard(eInputMode: EGamepadTextInputMode, eLineInputMode: EGamepadTextInputLineMode, pchDescription: String, unCharMax: Int,
                     pchExistingText: String, bUseMinimalMode: Boolean, uUserValue: Long)
            = EVROverlayError.of(ShowKeyboard!!.invoke(eInputMode.i, eLineInputMode.i, pchDescription, unCharMax, pchExistingText, bUseMinimalMode, uUserValue))

    @JvmField var ShowKeyboard: ShowKeyboard_callback? = null

    interface ShowKeyboard_callback : Callback {
        fun invoke(eInputMode: Int, eLineInputMode: Int, pchDescription: String, unCharMax: Int, pchExistingText: String, bUseMinimalMode: Boolean,
                   uUserValue: Long): Int
    }


    fun showKeyboardForOverlay(ulOverlayHandle: VROverlayHandle_t, eInputMode: EGamepadTextInputMode, eLineInputMode: EGamepadTextInputLineMode,
                               pchDescription: String, unCharMax: Int, pchExistingText: String, bUseMinimalMode: Boolean, uUserValue: Long)
            = EVROverlayError.of(ShowKeyboardForOverlay!!.invoke(ulOverlayHandle, eInputMode.i, eLineInputMode.i, pchDescription, unCharMax, pchExistingText,
            bUseMinimalMode, uUserValue))

    @JvmField var ShowKeyboardForOverlay: ShowKeyboardForOverlay_callback? = null

    interface ShowKeyboardForOverlay_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, eInputMode: Int, eLineInputMode: Int, pchDescription: String, unCharMax: Int, pchExistingText: String,
                   bUseMinimalMode: Boolean, uUserValue: Long): Int
    }

    /** Get the text that was entered into the text input **/
    fun getKeyboardText(pchText: String, cchText: Int) = GetKeyboardText!!.invoke(pchText, cchText)

    @JvmField var GetKeyboardText: GetKeyboardText_callback? = null

    interface GetKeyboardText_callback : Callback {
        fun invoke(pchText: String, cchText: Int): Int
    }

    /** Hide the virtual keyboard **/
    fun hideKeyboard() = HideKeyboard!!.invoke()

    @JvmField var HideKeyboard: HideKeyboard_callback? = null

    interface HideKeyboard_callback : Callback {
        fun invoke()
    }

    /** Set the position of the keyboard in world space **/
    fun setKeyboardTransformAbsolute(eTrackingOrigin: ETrackingUniverseOrigin, pmatTrackingOriginToKeyboardTransform: HmdMatrix34_t.ByReference)
            = SetKeyboardTransformAbsolute!!.invoke(eTrackingOrigin.i, pmatTrackingOriginToKeyboardTransform)

    @JvmField var SetKeyboardTransformAbsolute: SetKeyboardTransformAbsolute_callback? = null

    interface SetKeyboardTransformAbsolute_callback : Callback {
        fun invoke(eTrackingOrigin: Int, pmatTrackingOriginToKeyboardTransform: HmdMatrix34_t.ByReference)
    }

    /** Set the position of the keyboard in overlay space by telling it to avoid a rectangle in the overlay. Rectangle coords have (0,0) in the bottom left **/
    fun setKeyboardPositionForOverlay(ulOverlayHandle: VROverlayHandle_t, avoidRect: HmdRect2_t)
            = SetKeyboardPositionForOverlay!!.invoke(ulOverlayHandle, avoidRect)

    @JvmField var SetKeyboardPositionForOverlay: SetKeyboardPositionForOverlay_callback? = null

    interface SetKeyboardPositionForOverlay_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, avoidRect: HmdRect2_t)
    }


    // ---------------------------------------------
    // Overlay input methods
    // ---------------------------------------------

    /** Sets a list of primitives to be used for controller ray intersection typically the size of the underlying UI in pixels
     *  (not in world space). */
    @JvmOverloads fun setOverlayIntersectionMask(ulOverlayHandle: VROverlayHandle_t,
                                                 pMaskPrimitives: VROverlayIntersectionMaskPrimitive_t.ByReference,
                                                 unNumMaskPrimitives: Int, unPrimitiveSize: Int = Int.BYTES + Pointer.SIZE) =
            SetOverlayIntersectionMask!!.invoke(ulOverlayHandle, pMaskPrimitives, unNumMaskPrimitives, unPrimitiveSize)

    @JvmField var SetOverlayIntersectionMask: SetOverlayIntersectionMask_callback? = null

    interface SetOverlayIntersectionMask_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pMaskPrimitives: VROverlayIntersectionMaskPrimitive_t.ByReference,
                   unNumMaskPrimitives: Int, unPrimitiveSize: Int): EVROverlayError
    }


    fun getOverlayFlags(ulOverlayHandle: VROverlayHandle_t, pFlags: IntByReference)
            = EVROverlayError.of(GetOverlayFlags!!.invoke(ulOverlayHandle, pFlags))

    @JvmField var GetOverlayFlags: GetOverlayFlags_callback? = null

    interface GetOverlayFlags_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pFlags: IntByReference): Int
    }

    // ---------------------------------------------
    // Message box methods
    // ---------------------------------------------

    /** Show the message overlay. This will block and return you a result. **/
    @JvmOverloads fun showMessageOverlay(pchText: String, pchCaption: String, pchButton0Text: String, pchButton1Text: String? = null,
                                         pchButton2Text: String? = null, pchButton3Text: String? = null) =
            VRMessageOverlayResponse.of(ShowMessageOverlay!!.invoke(pchText, pchCaption, pchButton0Text, pchButton1Text,
                    pchButton2Text, pchButton3Text))

    @JvmField var ShowMessageOverlay: ShowMessageOverlay_callback? = null

    interface ShowMessageOverlay_callback : Callback {
        fun invoke(pchText: String, pchCaption: String, pchButton0Text: String, pchButton1Text: String?, pchButton2Text: String?,
                   pchButton3Text: String?): Int
    }

    constructor()

    override fun getFieldOrder(): List<String> = Arrays.asList("FindOverlay", "CreateOverlay", "DestroyOverlay",
            "SetHighQualityOverlay", "GetHighQualityOverlay", "GetOverlayKey", "GetOverlayName", "GetOverlayImageData",
            "GetOverlayErrorNameFromEnum", "SetOverlayRenderingPid", "GetOverlayRenderingPid", "SetOverlayFlag", "GetOverlayFlag",
            "SetOverlayColor", "GetOverlayColor", "SetOverlayAlpha", "GetOverlayAlpha", "SetOverlayTexelAspect",
            "GetOverlayTexelAspect", "SetOverlaySortOrder", "GetOverlaySortOrder", "SetOverlayWidthInMeters",
            "GetOverlayWidthInMeters", "SetOverlayAutoCurveDistanceRangeInMeters", "GetOverlayAutoCurveDistanceRangeInMeters",
            "SetOverlayTextureColorSpace", "GetOverlayTextureColorSpace", "SetOverlayTextureBounds", "GetOverlayTextureBounds",
            "GetOverlayTransformType", "SetOverlayTransformAbsolute", "GetOverlayTransformAbsolute",
            "SetOverlayTransformTrackedDeviceRelative", "GetOverlayTransformTrackedDeviceRelative",
            "SetOverlayTransformTrackedDeviceComponent", "GetOverlayTransformTrackedDeviceComponent", "ShowOverlay", "HideOverlay",
            "IsOverlayVisible", "GetTransformForOverlayCoordinates", "PollNextOverlayEvent", "GetOverlayInputMethod",
            "GetOverlayMouseScale", "SetOverlayMouseScale", "ComputeOverlayIntersection", "HandleControllerOverlayInteractionAsMouse",
            "IsHoverTargetOverlay", "GetGamepadFocusOverlay", "SetGamepadFocusOverlay", "SetOverlayNeighbor",
            "MoveGamepadFocusToNeighbor", "SetOverlayTexture", "ClearOverlayTexture", "SetOverlayRaw", "SetOverlayFromFile",
            "GetOverlayTexture", "ReleaseNativeOverlayHandle", "GetOverlayTextureSize", "CreateDashboardOverlay", "IsDashboardVisible",
            "IsActiveDashboardOverlay", "SetDashboardOverlaySceneProcess", "GetDashboardOverlaySceneProcess", "ShowDashboard",
            "GetPrimaryDashboardDevice", "ShowKeyboard", "ShowKeyboardForOverlay", "GetKeyboardText", "HideKeyboard",
            "SetKeyboardTransformAbsolute", "SetKeyboardPositionForOverlay", "SetOverlayIntersectionMask", "GetOverlayFlags",
            "ShowMessageOverlay")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVROverlay(), Structure.ByReference
    class ByValue : IVROverlay(), Structure.ByValue
}

val IVROverlay_Version = "FnTable:IVROverlay_014"