package openvr.lib

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.Union
import com.sun.jna.ptr.ByteByReference
import com.sun.jna.ptr.FloatByReference
import com.sun.jna.ptr.IntByReference
import com.sun.jna.ptr.PointerByReference
import glm_.BYTES
import glm_.vec2.Vec2i
import glm_.vec3.Vec3
import glm_.vec4.Vec4
import java.util.*

// ivroverlay.h ===================================================================================================================================================

/** The maximum length of an overlay key in bytes, counting the terminating null character. */
val vrOverlayMaxKeyLength = 128

/** The maximum length of an overlay name in bytes, counting the terminating null character. */
val vrOverlayMaxNameLength = 128

/** The maximum number of overlays that can exist in the system at one time. */
val maxOverlayCount = 64

/** The maximum number of overlay intersection mask primitives per overlay */
val maxOverlayIntersectionMaskPrimitivesCount = 32

/** Types of input supported by VR Overlays */
enum class VROverlayInputMethod {
    /** No input events will be generated automatically for this overlay    */
    None,
    /** Tracked controllers will get mouse events automatically */
    Mouse,
    /** Analog inputs from tracked controllers are turned into DualAnalog events */
    DualAnalog;

    val i = ordinal

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
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
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

class VROverlayTransformType_ByReference(@JvmField var value: VROverlayTransformType = VROverlayTransformType.Absolute) : IntByReference(value.i)

/** Overlay control settings */
enum class VROverlayFlags(@JvmField val i: Int) {

    None(0),

    /** The following only take effect when rendered using the high quality render path (see SetHighQualityOverlay).    */
    Curved(1),
    RGSS4X(2),

    /** Set this flag on a dashboard overlay to prevent a tab from showing up for that overlay  */
    NoDashboardTab(3),

    /** Set this flag on a dashboard that is able to deal with gamepad focus events */
    AcceptsGamepadEvents(4),

    /** Indicates that the overlay should dim/brighten to show gamepad focus    */
    ShowGamepadFocus(5),

    /** When in VROverlayInputMethod_Mouse you can optionally enable sending VRScroll */
    SendVRScrollEvents(6),
    SendVRTouchpadEvents(7),

    /** If set this will render a vertical scroll wheel on the primary controller), only needed if not using
     *  SendVRScrollEvents but you still want to represent a scroll wheel   */
    ShowTouchPadScrollWheel(8),

    /** If this is set ownership and render access to the overlay are transferred to the new scene process on a call
     *  to openvr.lib.IVRApplications::LaunchInternalProcess    */
    TransferOwnershipToInternalProcess(9),

    // If set), renders 50% of the texture in each eye), side by side
    /** Texture is left/right   */
    SideBySide_Parallel(10),
    /** Texture is crossed and right/left   */
    SideBySide_Crossed(11),
    /** Texture is a panorama   */
    Panorama(12),
    /** Texture is a stereo panorama    */
    StereoPanorama(13),

    /** If this is set on an overlay owned by the scene application that overlay will be sorted with the "Other"
     *  overlays on top of all other scene overlays */
    SortWithNonSceneOverlays(14),

    /** If set, the overlay will be shown in the dashboard, otherwise it will be hidden.    */
    VisibleInDashboard(15);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
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
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

open class VROverlayIntersectionParams : Structure {

    @JvmField
    var source = HmdVec3()
    @JvmField
    var direction = HmdVec3()
    @JvmField
    var eOrigin = 0
    var origin
        set(value) {
            eOrigin = value.i
        }
        get() = ETrackingUniverseOrigin.of(eOrigin)

    constructor()

    constructor(source: HmdVec3, direction: HmdVec3, origin: ETrackingUniverseOrigin) {
        this.source = source
        this.direction = direction
        this.eOrigin = origin.i
    }

    override fun getFieldOrder()= listOf("source", "direction", "eOrigin")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VROverlayIntersectionParams(), Structure.ByReference
    class ByValue : VROverlayIntersectionParams(), Structure.ByValue
}

open class VROverlayIntersectionResults : Structure {

    @JvmField
    var point = HmdVec3()
    @JvmField
    var normal = HmdVec3()
    @JvmField
    var uv = HmdVec2()
    @JvmField
    var distance = 0f

    constructor()

    constructor(point: HmdVec3, normal: HmdVec3, uv: HmdVec2, distance: Float) {
        this.point = point
        this.normal = normal
        this.uv = uv
        this.distance = distance
    }

    override fun getFieldOrder()= listOf("point", "normal", "uv", "distance")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VROverlayIntersectionResults(), Structure.ByReference
    class ByValue : VROverlayIntersectionResults(), Structure.ByValue
}

// Input modes for the Big Picture gamepad text entry
enum class EGamepadTextInputMode(@JvmField val i: Int) {

    Normal(0),
    Password(1),
    Submit(2);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

// Controls number of allowed lines for the Big Picture gamepad text entry
enum class EGamepadTextInputLineMode(@JvmField val i: Int) {

    SingleLine(0),
    MultipleLines(1);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
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
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EVROverlayIntersectionMaskPrimitiveType(@JvmField val i: Int) {

    Rectangle(0),
    Circle(1);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

open class IntersectionMaskRectangle : Structure {

    @JvmField
    var topLeftX = 0f
    @JvmField
    var topLeftY = 0f
    @JvmField
    var width = 0f
    @JvmField
    var height = 0f

    constructor()

    constructor(topLeftX: Float, topLeftY: Float, width: Float, height: Float) {
        this.topLeftX = topLeftX
        this.topLeftY = topLeftY
        this.width = width
        this.height = height
    }

    override fun getFieldOrder()= listOf("topLeftX", "topLeftY", "width", "height")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IntersectionMaskRectangle(), Structure.ByReference
    class ByValue : IntersectionMaskRectangle(), Structure.ByValue
}

open class IntersectionMaskCircle : Structure {

    @JvmField
    var centerX = 0f
    @JvmField
    var centerY = 0f
    @JvmField
    var radius = 0f

    constructor()

    constructor(centerX: Float, centerY: Float, radius: Float) {
        this.centerX = centerX
        this.centerY = centerY
        this.radius = radius
    }

    override fun getFieldOrder()= listOf("centerX", "centerY", "radius")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IntersectionMaskRectangle(), Structure.ByReference
    class ByValue : IntersectionMaskRectangle(), Structure.ByValue
}

/** NOTE!!! If you change this you MUST manually update openvr_interop.cs.py and openvr_api_flat.h.py */
abstract class VROverlayIntersectionMaskPrimitive_Data : Union() {

    class ByValue : VROverlayIntersectionMaskPrimitive_Data(), Structure.ByValue

    var rectangle: IntersectionMaskRectangle? = null
    var circle: IntersectionMaskCircle? = null
}

open class VROverlayIntersectionMaskPrimitive : Structure {

    @JvmField
    var primitiveType = 0
    @JvmField
    var primitive: VROverlayIntersectionMaskPrimitive_Data? = null

    constructor()

    constructor(primitiveType: EVROverlayIntersectionMaskPrimitiveType, primitive: VROverlayIntersectionMaskPrimitive_Data) :
            this(primitiveType.i, primitive)

    constructor(m_nPrimitiveType: Int, m_Primitive: VROverlayIntersectionMaskPrimitive_Data) {
        this.primitiveType = m_nPrimitiveType
        this.primitive = m_Primitive
    }

    override fun getFieldOrder()= listOf("primitiveType", "primitive")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VROverlayIntersectionMaskPrimitive(), Structure.ByReference
    class ByValue : VROverlayIntersectionMaskPrimitive(), Structure.ByValue
}

open class IVROverlay : Structure {

    // ---------------------------------------------
    // Overlay management methods
    // ---------------------------------------------

    /** Finds an existing overlay with the specified key. */
    fun findOverlay(overlayKey: String, overlayHandle: VROverlayHandle_ByReference) = EVROverlayError.of(FindOverlay!!(overlayKey, overlayHandle))

    @JvmField
    var FindOverlay: FindOverlay_callback? = null

    interface FindOverlay_callback : Callback {
        operator fun invoke(pchOverlayKey: String, pOverlayHandle: VROverlayHandle_ByReference): Int
    }

    /** Creates a new named overlay. All overlays start hidden and with default settings. */
    fun createOverlay(overlayKey: String, overlayName: String, overlayHandle: VROverlayHandle_ByReference) = EVROverlayError.of(CreateOverlay!!(overlayKey, overlayName, overlayHandle))

    @JvmField
    var CreateOverlay: CreateOverlay_callback? = null

    interface CreateOverlay_callback : Callback {
        operator fun invoke(pchOverlayKey: String, pchOverlayFriendlyName: String, pOverlayHandle: VROverlayHandle_ByReference): Int
    }

    /** Destroys the specified overlay. When an application calls VR_Shutdown all overlays created by that app are automatically destroyed. */
    infix fun destroyOverlay(overlayHandle: VROverlayHandle) = EVROverlayError.of(DestroyOverlay!!(overlayHandle))

    @JvmField
    var DestroyOverlay: DestroyOverlay_callback? = null

    interface DestroyOverlay_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle): Int
    }

    /** Setter: specify which overlay to use the high quality render path.  This overlay will be composited in during
     *  the distortion pass which results in it drawing on top of everything else, but also at a higher quality as it
     *  samples the source texture directly rather than rasterizing into each eye's render texture first.
     *  Because if this, only one of these is supported at any given time.  It is most useful for overlays that are
     *  expected to take up most of the user's view (e.g. streaming video).
     *  This mode does not support mouse input to your overlay.     */
    // NOTE: cant use var because setter return EVROverlayError
    infix fun setHighQualityOverlay(overlayHandle: VROverlayHandle) = EVROverlayError.of(SetHighQualityOverlay!!(overlayHandle))

    @JvmField
    var SetHighQualityOverlay: SetHighQualityOverlay_callback? = null

    interface SetHighQualityOverlay_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle): Int
    }

    /** Returns the overlay handle of the current overlay being rendered using the single high quality overlay render path.
     *  Otherwise it will return openvr.lib.getK_ulOverlayHandleInvalid. */
    val highQualityOverlay get() = GetHighQualityOverlay!!()

    @JvmField
    var GetHighQualityOverlay: GetHighQualityOverlay_callback? = null

    interface GetHighQualityOverlay_callback : Callback {
        operator fun invoke(): VROverlayHandle
    }

    /** Fills the provided buffer with the string key of the overlay. Returns the size of buffer required to store the key, including
     *  the terminating null character. openvr.lib.getVrOverlayMaxKeyLength will be enough bytes to fit the string. */
    fun getOverlayKey(overlayHandle: VROverlayHandle, value: String, bufferSize: Int, error: EVROverlayError_ByReference? = null) = GetOverlayKey!!(overlayHandle, value, bufferSize, error)

    @JvmField
    var GetOverlayKey: GetOverlayKey_callback? = null

    interface GetOverlayKey_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pchValue: String, unBufferSize: Int, pError: EVROverlayError_ByReference? = null): Int
    }

    /** Fills the provided buffer with the friendly name of the overlay. Returns the size of buffer required to store the key, including the terminating null
     *  character. openvr.lib.getVrOverlayMaxNameLength will be enough bytes to fit the string. */
    // TODO add enhance -> return string directly
    fun getOverlayName(overlayHandle: VROverlayHandle, value: String, bufferSize: Int, error: EVROverlayError_ByReference? = null) = GetOverlayName!!(overlayHandle, value, bufferSize, error)

    @JvmField
    var GetOverlayName: GetOverlayName_callback? = null

    interface GetOverlayName_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pchValue: String, unBufferSize: Int, pError: EVROverlayError_ByReference? = null): Int
    }

    /** set the name to use for this overlay */
    fun setOverlayName(overlayHandle: VROverlayHandle, name: String) = EVROverlayError.of(SetOverlayName!!(overlayHandle, name))

    @JvmField
    var SetOverlayName: SetOverlayName_callback? = null

    interface SetOverlayName_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pchName: String): Int
    }

    /** Gets the raw image data from an overlay. Overlay image data is always returned as RGBA data, 4 bytes per pixel. If the buffer is not large enough,
     *  width and height will be set and VROverlayError_ArrayTooSmall is returned. */
    fun getOverlayImageData(overlayHandle: VROverlayHandle, buffer: Pointer, bufferSize: Int, width: IntByReference, height: IntByReference) = EVROverlayError.of(GetOverlayImageData!!(overlayHandle, buffer, bufferSize, width, height))

    @JvmField
    var GetOverlayImageData: GetOverlayImageData_callback? = null

    interface GetOverlayImageData_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pvBuffer: Pointer, unBufferSize: Int, punWidth: IntByReference, punHeight: IntByReference): Int
    }

    /** returns a string that corresponds with the specified overlay error. The string will be the name of the error value value for all valid error codes */
    infix fun getOverlayErrorNameFromEnum(error: EVROverlayError) = GetOverlayErrorNameFromEnum!!(error.i)

    @JvmField
    var GetOverlayErrorNameFromEnum: GetOverlayErrorNameFromEnum_callback? = null

    interface GetOverlayErrorNameFromEnum_callback : Callback {
        operator fun invoke(error: Int): String
    }


    // ---------------------------------------------
    // Overlay rendering methods
    // ---------------------------------------------

    /** Sets the pid that is allowed to render to this overlay (the creator pid is always allow to render), by default this is the pid of the process that made
     *  the overlay */
    fun setOverlayRenderingPid(overlayHandle: VROverlayHandle, pid: Int) = EVROverlayError.of(SetOverlayRenderingPid!!(overlayHandle, pid))

    @JvmField
    var SetOverlayRenderingPid: SetOverlayRenderingPid_callback? = null

    interface SetOverlayRenderingPid_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, unPID: Int): Int
    }

    /** Gets the pid that is allowed to render to this overlay */
    infix fun getOverlayRenderingPid(overlayHandle: VROverlayHandle) = GetOverlayRenderingPid!!(overlayHandle)

    @JvmField
    var GetOverlayRenderingPid: GetOverlayRenderingPid_callback? = null

    interface GetOverlayRenderingPid_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle): Int
    }

    /** Specify flag setting for a given overlay */
    fun setOverlayFlag(overlayHandle: VROverlayHandle, overlayFlag: VROverlayFlags, enabled: Boolean) = EVROverlayError.of(SetOverlayFlag!!(overlayHandle, overlayFlag.i, enabled))

    @JvmField
    var SetOverlayFlag: SetOverlayFlag_callback? = null

    interface SetOverlayFlag_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, eOverlayFlag: Int, bEnabled: Boolean): Int
    }

    /** Sets flag setting for a given overlay */
    fun getOverlayFlag(overlayHandle: VROverlayHandle, overlayFlag: VROverlayFlags, enabled: BooleanByReference) = EVROverlayError.of(GetOverlayFlag!!(overlayHandle, overlayFlag.i, enabled))

    @JvmField
    var GetOverlayFlag: GetOverlayFlag_callback? = null

    interface GetOverlayFlag_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, eOverlayFlag: Int, pbEnabled: ByteByReference): Int
    }

    /** Sets the color tint of the overlay quad. Use 0.0 to 1.0 per channel. */
    fun setOverlayColor(overlayHandle: VROverlayHandle, red: Float, green: Float, blue: Float) = EVROverlayError.of(SetOverlayColor!!(overlayHandle, red, green, blue))

    fun setOverlayColor(overlayHandle: VROverlayHandle, color: Vec3) = EVROverlayError.of(SetOverlayColor!!(overlayHandle, color.r, color.g, color.b))
    fun setOverlayColor(overlayHandle: VROverlayHandle, color: Vec4) = EVROverlayError.of(SetOverlayColor!!(overlayHandle, color.r, color.g, color.b))

    @JvmField
    var SetOverlayColor: SetOverlayColor_callback? = null

    interface SetOverlayColor_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, fRed: Float, fGreen: Float, fBlue: Float): Int
    }

    /** Gets the color tint of the overlay quad. */
    fun getOverlayColor(overlayHandle: VROverlayHandle): Vec3 {
        val red = FloatByReference()
        val green = FloatByReference()
        val blue = FloatByReference()
        EVROverlayError.of(GetOverlayColor!!(overlayHandle, red, green, blue))
        return Vec3(red.value, green.value, blue.value)
    }

    fun getOverlayColor(overlayHandle: VROverlayHandle, red: FloatByReference, green: FloatByReference, blue: FloatByReference) = EVROverlayError.of(GetOverlayColor!!(overlayHandle, red, green, blue))

    @JvmField
    var GetOverlayColor: GetOverlayColor_callback? = null

    interface GetOverlayColor_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pfRed: FloatByReference, pfGreen: FloatByReference, pfBlue: FloatByReference): Int
    }

    /** Sets the alpha of the overlay quad. Use 1.0 for 100 percent opacity to 0.0 for 0 percent opacity. */
    fun setOverlayAlpha(overlayHandle: VROverlayHandle, alpha: Float) = EVROverlayError.of(SetOverlayAlpha!!(overlayHandle, alpha))

    @JvmField
    var SetOverlayAlpha: SetOverlayAlpha_callback? = null

    interface SetOverlayAlpha_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, fAlpha: Float): Int
    }

    /** Gets the alpha of the overlay quad. By default overlays are rendering at 100 percent alpha (1.0). */
    fun getOverlayAlpha(overlayHandle: VROverlayHandle): Float {
        val alpha = FloatByReference()
        EVROverlayError.of(GetOverlayAlpha!!(overlayHandle, alpha))
        return alpha.value
    }

    fun getOverlayAlpha(overlayHandle: VROverlayHandle, alpha: FloatByReference) = EVROverlayError.of(GetOverlayAlpha!!(overlayHandle, alpha))

    @JvmField
    var GetOverlayAlpha: GetOverlayAlpha_callback? = null

    interface GetOverlayAlpha_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pfAlpha: FloatByReference): Int
    }

    /** Sets the aspect ratio of the texels in the overlay. 1.0 means the texels are square. 2.0 means the texels are twice as wide as they are tall.
     *  Defaults to 1.0. */
    fun setOverlayTexelAspect(overlayHandle: VROverlayHandle, texelAspect: Float) = EVROverlayError.of(SetOverlayTexelAspect!!(overlayHandle, texelAspect))

    @JvmField
    var SetOverlayTexelAspect: SetOverlayTexelAspect_callback? = null

    interface SetOverlayTexelAspect_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, fTexelAspect: Float): Int
    }

    /** Gets the aspect ratio of the texels in the overlay. Defaults to 1.0 */
    fun getOverlayTexelAspect(overlayHandle: VROverlayHandle, texelAspect: FloatByReference) = EVROverlayError.of(GetOverlayTexelAspect!!(overlayHandle, texelAspect))

    @JvmField
    var GetOverlayTexelAspect: GetOverlayTexelAspect_callback? = null

    interface GetOverlayTexelAspect_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pfTexelAspect: FloatByReference): Int
    }

    /** Sets the rendering sort order for the overlay. Overlays are rendered this order:
     *      Overlays owned by the scene application
     *      Overlays owned by some other application
     *
     *	Within a category overlays are rendered lowest sort order to highest sort order. Overlays with the same
     *	sort order are rendered back to front base on distance from the HMD.
     *
     *	Sort order defaults to 0. */
    fun setOverlaySortOrder(overlayHandle: VROverlayHandle, sortOrder: Int) = EVROverlayError.of(SetOverlaySortOrder!!(overlayHandle, sortOrder))

    @JvmField
    var SetOverlaySortOrder: SetOverlaySortOrder_callback? = null

    interface SetOverlaySortOrder_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, unSortOrder: Int): Int
    }

    /** Gets the sort order of the overlay. See SetOverlaySortOrder for how this works. */
    fun getOverlaySortOrder(overlayHandle: VROverlayHandle, sortOrder: IntByReference) = EVROverlayError.of(GetOverlaySortOrder!!(overlayHandle, sortOrder))

    @JvmField
    var GetOverlaySortOrder: GetOverlaySortOrder_callback? = null

    interface GetOverlaySortOrder_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, punSortOrder: IntByReference): Int
    }

    /** Sets the width of the overlay quad in meters. By default overlays are rendered on a quad that is 1 meter across */
    fun setOverlayWidthInMeters(overlayHandle: VROverlayHandle, widthInMeters: Float) = EVROverlayError.of(SetOverlayWidthInMeters!!(overlayHandle, widthInMeters))

    @JvmField
    var SetOverlayWidthInMeters: SetOverlayWidthInMeters_callback? = null

    interface SetOverlayWidthInMeters_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, fWidthInMeters: Float): Int
    }

    /** Returns the width of the overlay quad in meters. By default overlays are rendered on a quad that is 1 meter across */
    fun getOverlayWidthInMeters(overlayHandle: VROverlayHandle, widthInMeters: FloatByReference) = EVROverlayError.of(GetOverlayWidthInMeters!!(overlayHandle, widthInMeters))

    @JvmField
    var GetOverlayWidthInMeters: GetOverlayWidthInMeters_callback? = null

    interface GetOverlayWidthInMeters_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pfWidthInMeters: FloatByReference): Int
    }

    /** For high-quality curved overlays only, sets the distance range in meters from the overlay used to automatically curve the surface around the viewer.
     *  Min is distance is when the surface will be most curved.  Max is when least curved. */
    fun setOverlayAutoCurveDistanceRangeInMeters(overlayHandle: VROverlayHandle, minDistanceInMeters: Float, maxDistanceInMeters: Float) = EVROverlayError.of(SetOverlayAutoCurveDistanceRangeInMeters!!(overlayHandle, minDistanceInMeters, maxDistanceInMeters))

    @JvmField
    var SetOverlayAutoCurveDistanceRangeInMeters: SetOverlayAutoCurveDistanceRangeInMeters_callback? = null

    interface SetOverlayAutoCurveDistanceRangeInMeters_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, fMinDistanceInMeters: Float, fMaxDistanceInMeters: Float): Int
    }

    /** For high-quality curved overlays only, gets the distance range in meters from the overlay used to automatically curve the surface around the viewer.
     *  Min is distance is when the surface will be most curved.  Max is when least curved. */
    fun getOverlayAutoCurveDistanceRangeInMeters(overlayHandle: VROverlayHandle, minDistanceInMeters: FloatByReference, maxDistanceInMeters: FloatByReference) = EVROverlayError.of(GetOverlayAutoCurveDistanceRangeInMeters!!(overlayHandle, minDistanceInMeters, maxDistanceInMeters))

    @JvmField
    var GetOverlayAutoCurveDistanceRangeInMeters: GetOverlayAutoCurveDistanceRangeInMeters_callback? = null

    interface GetOverlayAutoCurveDistanceRangeInMeters_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pfMinDistanceInMeters: FloatByReference, pfMaxDistanceInMeters: FloatByReference): Int
    }

    /** Sets the colorspace the overlay texture's data is in.  Defaults to 'auto'.
     *  If the texture needs to be resolved, you should call SetOverlayTexture with the appropriate colorspace instead. */
    fun setOverlayTextureColorSpace(overlayHandle: VROverlayHandle, textureColorSpace: EColorSpace) = EVROverlayError.of(SetOverlayTextureColorSpace!!(overlayHandle, textureColorSpace.i))

    @JvmField
    var SetOverlayTextureColorSpace: SetOverlayTextureColorSpace_callback? = null

    interface SetOverlayTextureColorSpace_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, eTextureColorSpace: Int): Int
    }

    /** Gets the overlay's current colorspace setting. */
    fun getOverlayTextureColorSpace(overlayHandle: VROverlayHandle, textureColorSpace: EColorSpace_ByReference) = EVROverlayError.of(GetOverlayTextureColorSpace!!(overlayHandle, textureColorSpace))

    @JvmField
    var GetOverlayTextureColorSpace: GetOverlayTextureColorSpace_callback? = null

    interface GetOverlayTextureColorSpace_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, peTextureColorSpace: EColorSpace_ByReference): Int
    }

    /** Sets the part of the texture to use for the overlay. UV Min is the upper left corner and UV Max is the lower right corner. */
    fun setOverlayTextureBounds(overlayHandle: VROverlayHandle, overlayTextureBounds: VRTextureBounds.ByReference) = EVROverlayError.of(SetOverlayTextureBounds!!(overlayHandle, overlayTextureBounds))

    @JvmField
    var SetOverlayTextureBounds: SetOverlayTextureBounds_callback? = null

    interface SetOverlayTextureBounds_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pOverlayTextureBounds: VRTextureBounds.ByReference): Int
    }

    /** Gets the part of the texture to use for the overlay. UV Min is the upper left corner and UV Max is the lower right corner. */
    fun getOverlayTextureBounds(overlayHandle: VROverlayHandle, overlayTextureBounds: VRTextureBounds.ByReference) = EVROverlayError.of(GetOverlayTextureBounds!!(overlayHandle, overlayTextureBounds))

    @JvmField
    var GetOverlayTextureBounds: GetOverlayTextureBounds_callback? = null

    interface GetOverlayTextureBounds_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pOverlayTextureBounds: VRTextureBounds.ByReference): Int
    }

    /** Gets render model to draw behind this overlay */
    fun getOverlayRenderModel(overlayHandle: VROverlayHandle, value: String, bufferSize: Int, color: HmdColor.ByReference, error: EVROverlayError_ByReference) = GetOverlayRenderModel!!(overlayHandle, value, bufferSize, color, error)

    @JvmField
    var GetOverlayRenderModel: GetOverlayRenderModel_callback? = null

    interface GetOverlayRenderModel_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pchValue: String, unBufferSize: Int, pColor: HmdColor.ByReference, pError: EVROverlayError_ByReference): Int
    }

    /** Sets render model to draw behind this overlay and the vertex color to use, pass null for color to match the
     *  overlays vertex color.
     *  The model is scaled by the same amount as the overlay, with a default of 1m.    */
    fun setOverlayRenderModel(overlayHandle: VROverlayHandle, renderModel: String, color: HmdColor.ByReference) = EVROverlayError.of(SetOverlayRenderModel!!(overlayHandle, renderModel, color))

    @JvmField
    var SetOverlayRenderModel: SetOverlayRenderModel_callback? = null

    interface SetOverlayRenderModel_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pchRenderModel: String, pColor: HmdColor.ByReference): Int
    }

    /** Returns the transform value of this overlay. */
    fun getOverlayTransformType(overlayHandle: VROverlayHandle, transformType: VROverlayTransformType_ByReference) = EVROverlayError.of(GetOverlayTransformType!!(overlayHandle, transformType))

    @JvmField
    var GetOverlayTransformType: GetOverlayTransformType_callback? = null

    interface GetOverlayTransformType_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, peTransformType: VROverlayTransformType_ByReference): Int
    }

    /** Sets the transform to absolute tracking origin. */
    fun setOverlayTransformAbsolute(overlayHandle: VROverlayHandle, trackingOrigin: ETrackingUniverseOrigin, trackingOriginToOverlayTransform: HmdMat34.ByReference) = EVROverlayError.of(SetOverlayTransformAbsolute!!(overlayHandle, trackingOrigin.i, trackingOriginToOverlayTransform))

    @JvmField
    var SetOverlayTransformAbsolute: SetOverlayTransformAbsolute_calback? = null

    interface SetOverlayTransformAbsolute_calback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, eTrackingOrigin: Int, pmatTrackingOriginToOverlayTransform: HmdMat34.ByReference): Int
    }

    /** Gets the transform if it is absolute. Returns an error if the transform is some other value. */
    fun getOverlayTransformAbsolute(overlayHandle: VROverlayHandle, trackingOrigin: ETrackingUniverseOrigin_ByReference, trackingOriginToOverlayTransform: HmdMat34.ByReference) = EVROverlayError.of(GetOverlayTransformAbsolute!!(overlayHandle, trackingOrigin, trackingOriginToOverlayTransform))

    @JvmField
    var GetOverlayTransformAbsolute: GetOverlayTransformAbsolute_callback? = null

    interface GetOverlayTransformAbsolute_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, peTrackingOrigin: ETrackingUniverseOrigin_ByReference, pmatTrackingOriginToOverlayTransform: HmdMat34.ByReference): Int
    }

    /** Sets the transform to relative to the transform of the specified tracked device. */
    fun setOverlayTransformTrackedDeviceRelative(overlayHandle: VROverlayHandle, trackedDevice: TrackedDeviceIndex, trackedDeviceToOverlayTransform: HmdMat34.ByReference) = EVROverlayError.of(SetOverlayTransformTrackedDeviceRelative!!(overlayHandle, trackedDevice, trackedDeviceToOverlayTransform))

    @JvmField
    var SetOverlayTransformTrackedDeviceRelative: SetOverlayTransformTrackedDeviceRelative_callback? = null

    interface SetOverlayTransformTrackedDeviceRelative_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, unTrackedDevice: TrackedDeviceIndex, pmatTrackedDeviceToOverlayTransform: HmdMat34.ByReference): Int
    }

    /** Gets the transform if it is relative to a tracked device. Returns an error if the transform is some other value. */
    fun getOverlayTransformTrackedDeviceRelative(overlayHandle: VROverlayHandle, trackedDevice: TrackedDeviceIndex_ByReference, trackedDeviceToOverlayTransform: HmdMat34.ByReference) = ETrackedDeviceClass.of(GetOverlayTransformTrackedDeviceRelative!!(overlayHandle, trackedDevice, trackedDeviceToOverlayTransform))

    @JvmField
    var GetOverlayTransformTrackedDeviceRelative: GetOverlayTransformTrackedDeviceRelative_callback? = null

    interface GetOverlayTransformTrackedDeviceRelative_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, punTrackedDevice: TrackedDeviceIndex_ByReference, pmatTrackedDeviceToOverlayTransform: HmdMat34.ByReference): Int
    }

    /** Sets the transform to draw the overlay on a rendermodel component mesh instead of a quad. This will only draw when the system is drawing the device.
     *  Overlays with this transform value cannot receive mouse events. */
    fun setOverlayTransformTrackedDeviceComponent(overlayHandle: VROverlayHandle, deviceIndex: TrackedDeviceIndex, componentName: String) = EVROverlayError.of(SetOverlayTransformTrackedDeviceComponent!!(overlayHandle, deviceIndex, componentName))

    @JvmField
    var SetOverlayTransformTrackedDeviceComponent: SetOverlayTransformTrackedDeviceComponent_callback? = null

    interface SetOverlayTransformTrackedDeviceComponent_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, unDeviceIndex: TrackedDeviceIndex, pchComponentName: String): Int
    }

    /** Gets the transform information when the overlay is rendering on a component. */
    fun getOverlayTransformTrackedDeviceComponent(overlayHandle: VROverlayHandle, deviceIndex: TrackedDeviceIndex_ByReference, componentName: String, componentNameSize: Int) = EVROverlayError.of(GetOverlayTransformTrackedDeviceComponent!!(overlayHandle, deviceIndex, componentName, componentNameSize))

    @JvmField
    var GetOverlayTransformTrackedDeviceComponent: GetOverlayTransformTrackedDeviceComponent_callback? = null

    interface GetOverlayTransformTrackedDeviceComponent_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, punDeviceIndex: TrackedDeviceIndex_ByReference, pchComponentName: String, unComponentNameSize: Int): Int
    }

    /** Gets the transform if it is relative to another overlay. Returns an error if the transform is some other type. */
    fun getOverlayTransformOverlayRelative(overlayHandle: VROverlayHandle, overlayHandleParent: VROverlayHandle_ByReference, parentOverlayToOverlayTransform: HmdMat34.ByReference) = EVROverlayError.of(GetOverlayTransformOverlayRelative!!(overlayHandle, overlayHandleParent,
            parentOverlayToOverlayTransform))

    @JvmField
    var GetOverlayTransformOverlayRelative: GetOverlayTransformOverlayRelative_callback? = null

    interface GetOverlayTransformOverlayRelative_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, ulOverlayHandleParent: VROverlayHandle_ByReference, pmatParentOverlayToOverlayTransform: HmdMat34.ByReference): Int
    }

    /** Sets the transform to relative to the transform of the specified overlay. This overlays visibility will also
     *  track the parents visibility */
    fun setOverlayTransformOverlayRelative(overlayHandle: VROverlayHandle, overlayHandleParent: VROverlayHandle, parentOverlayToOverlayTransform: HmdMat34.ByReference) = EVROverlayError.of(SetOverlayTransformOverlayRelative!!(overlayHandle, overlayHandleParent,
            parentOverlayToOverlayTransform))

    @JvmField
    var SetOverlayTransformOverlayRelative: SetOverlayTransformOverlayRelative_callback? = null

    interface SetOverlayTransformOverlayRelative_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, ulOverlayHandleParent: VROverlayHandle, pmatParentOverlayToOverlayTransform: HmdMat34.ByReference): Int
    }

    /** Shows the VR overlay.  For dashboard overlays, only the Dashboard Manager is allowed to call this. */
    infix fun showOverlay(overlayHandle: VROverlayHandle) = EVROverlayError.of(ShowOverlay!!(overlayHandle))

    @JvmField
    var ShowOverlay: ShowOverlay_callback? = null

    interface ShowOverlay_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle): Int
    }

    /** Hides the VR overlay.  For dashboard overlays, only the Dashboard Manager is allowed to call this. */
    infix fun hideOverlay(overlayHandle: VROverlayHandle) = EVROverlayError.of(HideOverlay!!(overlayHandle))

    @JvmField
    var HideOverlay: HideOverlay_callback? = null

    interface HideOverlay_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle): Int
    }

    /** Returns true if the overlay is visible. */
    infix fun isOverlayVisible(overlayHandle: VROverlayHandle) = IsOverlayVisible!!(overlayHandle)

    @JvmField
    var IsOverlayVisible: IsOverlayVisible_callback? = null

    interface IsOverlayVisible_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle): Boolean
    }

    /** Get the transform in 3d space associated with a specific 2d point in the overlay's coordinate space (where 0,0 is the lower left). -Z points out of
     *  the overlay */
    fun getTransformForOverlayCoordinates(overlayHandle: VROverlayHandle, trackingOrigin: ETrackingUniverseOrigin, coordinatesInOverlay: HmdVec2, transform: HmdMat34.ByReference) = EVROverlayError.of(GetTransformForOverlayCoordinates!!(overlayHandle, trackingOrigin.i, coordinatesInOverlay, transform))

    @JvmField
    var GetTransformForOverlayCoordinates: GetTransformForOverlayCoordinates_callback? = null

    interface GetTransformForOverlayCoordinates_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, eTrackingOrigin: Int, coordinatesInOverlay: HmdVec2, pmatTransform: HmdMat34.ByReference): Int
    }

    // ---------------------------------------------
    // Overlay input methods
    // ---------------------------------------------

    /** Returns true and fills the event with the next event on the overlay's event queue, if there is one.
     *  If there are no events this method returns false. vrEvent should be the size in bytes of the openvr.lib.VREvent struct */
    fun pollNextOverlayEvent(overlayHandle: VROverlayHandle, event: VREvent.ByReference, vrEvent: Int) = PollNextOverlayEvent!!(overlayHandle, event, vrEvent)

    @JvmField
    var PollNextOverlayEvent: PollNextOverlayEvent_callback? = null

    interface PollNextOverlayEvent_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pEvent: VREvent.ByReference, uncbVREvent: Int): Boolean
    }

    /** Returns the current input settings for the specified overlay. */
    fun getOverlayInputMethod(overlayHandle: VROverlayHandle, inputMethod: VROverlayInputMethod_ByReference) = EVROverlayError.of(GetOverlayInputMethod!!(overlayHandle, inputMethod))

    @JvmField
    var GetOverlayInputMethod: GetOverlayInputMethod_callback? = null

    interface GetOverlayInputMethod_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, peInputMethod: VROverlayInputMethod_ByReference): Int
    }

    /** Sets the input settings for the specified overlay. */
    fun setOverlayInputMethod(overlayHandle: VROverlayHandle, inputMethod: VROverlayInputMethod) = EVROverlayError.of(SetOverlayInputMethod!!(overlayHandle, inputMethod.i))

    @JvmField
    var SetOverlayInputMethod: SetOverlayInputMethod_callback? = null

    interface SetOverlayInputMethod_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, eInputMethod: Int): Int
    }

    /** Gets the mouse scaling factor that is used for mouse events. The actual texture may be a different size, but this is typically the size of the
     *  underlying UI in pixels. */
    fun getOverlayMouseScale(overlayHandle: VROverlayHandle, mouseScale: HmdVec2.ByReference) = EVROverlayError.of(GetOverlayMouseScale!!(overlayHandle, mouseScale))

    @JvmField
    var GetOverlayMouseScale: GetOverlayMouseScale_callback? = null

    interface GetOverlayMouseScale_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pvecMouseScale: HmdVec2.ByReference): Int
    }

    /** Sets the mouse scaling factor that is used for mouse events. The actual texture may be a different size, but this is typically the size of the
     *  underlying UI in pixels (not in world space). */
    fun setOverlayMouseScale(overlayHandle: VROverlayHandle, mouseScale: HmdVec2.ByReference) = EVROverlayError.of(SetOverlayMouseScale!!(overlayHandle, mouseScale))

    @JvmField
    var SetOverlayMouseScale: SetOverlayMouseScale_callback? = null

    interface SetOverlayMouseScale_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pvecMouseScale: HmdVec2.ByReference): Int
    }

    /** Computes the overlay-space pixel coordinates of where the ray intersects the overlay with the specified settings. Returns false if there is no
     *  intersection. */
    fun computeOverlayIntersection(overlayHandle: VROverlayHandle, params: VROverlayIntersectionParams.ByReference, results: VROverlayIntersectionResults.ByReference) = ComputeOverlayIntersection!!(overlayHandle, params, results)

    @JvmField
    var ComputeOverlayIntersection: ComputeOverlayIntersection_callback? = null

    interface ComputeOverlayIntersection_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pParams: VROverlayIntersectionParams.ByReference, pResults: VROverlayIntersectionResults.ByReference): Boolean
    }

    /** Returns true if the specified overlay is the hover target. An overlay is the hover target when it is the last overlay "moused over" by the virtual mouse
     *  pointer */
    infix fun isHoverTargetOverlay(overlayHandle: VROverlayHandle) = IsHoverTargetOverlay!!(overlayHandle)

    @JvmField
    var IsHoverTargetOverlay: IsHoverTargetOverlay_callback? = null

    interface IsHoverTargetOverlay_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle): Boolean
    }

    /** Returns the current Gamepad focus overlay */
    val gamepadFocusOverlay get() = GetGamepadFocusOverlay!!()

    @JvmField
    var GetGamepadFocusOverlay: GetGamepadFocusOverlay_callback? = null

    interface GetGamepadFocusOverlay_callback : Callback {
        operator fun invoke(): VROverlayHandle
    }

    /** Sets the current Gamepad focus overlay */
    infix fun setGamepadFocusOverlay(newFocusOverlay: VROverlayHandle) = EVROverlayError.of(SetGamepadFocusOverlay!!(newFocusOverlay))

    @JvmField
    var SetGamepadFocusOverlay: SetGamepadFocusOverlay_callback? = null

    interface SetGamepadFocusOverlay_callback : Callback {
        operator fun invoke(ulNewFocusOverlay: VROverlayHandle): Int
    }

    /** Sets an overlay's neighbor. This will also set the neighbor of the "to" overlay to point back to the "from" overlay. If an overlay's neighbor is set
     *  to invalid both ends will be cleared */
    fun setOverlayNeighbor(direction: EOverlayDirection, from: VROverlayHandle, to: VROverlayHandle) = EVROverlayError.of(SetOverlayNeighbor!!(direction.i, from, to))

    @JvmField
    var SetOverlayNeighbor: SetOverlayNeighbor_callback? = null

    interface SetOverlayNeighbor_callback : Callback {
        operator fun invoke(eDirection: Int, ulFrom: VROverlayHandle, ulTo: VROverlayHandle): Int
    }

    /** Changes the Gamepad focus from one overlay to one of its neighbors. Returns VROverlayError_NoNeighbor if there is no neighbor in that direction */
    fun moveGamepadFocusToNeighbor(direction: EOverlayDirection, from: VROverlayHandle) = EVROverlayError.of(MoveGamepadFocusToNeighbor!!(direction.i, from))

    @JvmField
    var MoveGamepadFocusToNeighbor: MoveGamepadFocusToNeighbor_callback? = null

    interface MoveGamepadFocusToNeighbor_callback : Callback {
        operator fun invoke(eDirection: Int, ulFrom: VROverlayHandle): Int
    }

    /** Sets the analog input to Dual Analog coordinate scale for the specified overlay. */
    fun setOverlayDualAnalogTransform(overlay: VROverlayHandle, which: EDualAnalogWhich, center: HmdVec2, radius: Float) = EVROverlayError.of(SetOverlayDualAnalogTransform!!(overlay, which.i, center, radius))

    @JvmField
    var SetOverlayDualAnalogTransform: SetOverlayDualAnalogTransform_callback? = null

    interface SetOverlayDualAnalogTransform_callback : Callback {
        operator fun invoke(overlay: VROverlayHandle, which: Int, center: HmdVec2, radius: Float): Int
    }

    /** Gets the analog input to Dual Analog coordinate scale for the specified overlay. */
    fun GetOverlayDualAnalogTransform(overlay: VROverlayHandle, which: EDualAnalogWhich, center: HmdVec2.ByReference, radius: FloatByReference) = EVROverlayError.of(GetOverlayDualAnalogTransform!!(overlay, which.i, center, radius))

    @JvmField
    var GetOverlayDualAnalogTransform: GetOverlayDualAnalogTransform_callback? = null

    interface GetOverlayDualAnalogTransform_callback : Callback {
        operator fun invoke(overlay: VROverlayHandle, which: Int, center: HmdVec2.ByReference, radius: FloatByReference): Int
    }

    // ---------------------------------------------
    // Overlay texture methods
    // ---------------------------------------------

    /** Texture to draw for the overlay. This function can only be called by the overlay's creator or renderer process (see SetOverlayRenderingPid) .
     *
     *  OpenGL dirty state:
     *	    glBindTexture
     */
    fun setOverlayTexture(overlayHandle: VROverlayHandle, texture: Texture.ByReference) = EVROverlayError.of(SetOverlayTexture!!(overlayHandle, texture))

    @JvmField
    var SetOverlayTexture: SetOverlayTexture_callback? = null

    interface SetOverlayTexture_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pTexture: Texture.ByReference): Int
    }

    /** Use this to tell the overlay system to release the texture set for this overlay. */
    infix fun clearOverlayTexture(ulOverlayHandle: VROverlayHandle) = EVROverlayError.of(ClearOverlayTexture!!(ulOverlayHandle))

    @JvmField
    var ClearOverlayTexture: ClearOverlayTexture_callback? = null

    interface ClearOverlayTexture_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle): Int
    }

    /** Separate interface for providing the data as a stream of bytes, but there is an upper bound on data that can be sent.
     *  This function can only be called by the overlay's renderer process. */
    fun setOverlayRaw(overlayHandle: VROverlayHandle, buffer: Pointer, width: Int, height: Int, depth: Int) = EVROverlayError.of(SetOverlayRaw!!(overlayHandle, buffer, width, height, depth))

    @JvmField
    var SetOverlayRaw: SetOverlayRaw_callback? = null

    interface SetOverlayRaw_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pvBuffer: Pointer, unWidth: Int, unHeight: Int, unDepth: Int): Int
    }

    /** Separate interface for providing the image through a filename: can be png or jpg, and should not be bigger than 1920x1080.
     * This function can only be called by the overlay's renderer process */
    fun setOverlayFromFile(overlayHandle: VROverlayHandle, filePath: String) = EVROverlayError.of(SetOverlayFromFile!!(overlayHandle, filePath))

    @JvmField
    var SetOverlayFromFile: SetOverlayFromFile_callback? = null

    interface SetOverlayFromFile_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pchFilePath: String): Int
    }

    /** Get the native texture handle/device for an overlay you have created.
     *  On windows this handle will be a ID3D11ShaderResourceView with a ID3D11Texture2D bound.
     *
     *  The texture will always be sized to match the backing texture you supplied in SetOverlayTexture above.
     *
     *  You MUST call ReleaseNativeOverlayHandle() with nativeTextureHandle once you are done with this texture.
     *
     *  nativeTextureHandle is an OUTPUT, it will be a pointer to a ID3D11ShaderResourceView *.
     *  nativeTextureRef is an INPUT and should be a ID3D11Resource *. The device used by nativeTextureRef will be used to bind nativeTextureHandle.     */
    fun getOverlayTexture(overlayHandle: VROverlayHandle, nativeTextureHandle: PointerByReference, nativeTextureRef: Pointer, width: IntByReference, height: IntByReference, nativeFormat: IntByReference,
                          apiType: ETextureType_ByReference, colorSpace: EColorSpace_ByReference, textureBounds: VRTextureBounds.ByReference) =
            EVROverlayError.of(GetOverlayTexture!!(overlayHandle, nativeTextureHandle, nativeTextureRef, width, height, nativeFormat, apiType, colorSpace, textureBounds))

    @JvmField
    var GetOverlayTexture: GetOverlayTexture_callback? = null

    interface GetOverlayTexture_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pNativeTextureHandle: PointerByReference, pNativeTextureRef: Pointer, pWidth: IntByReference, pHeight: IntByReference, pNativeFormat: IntByReference,
                            pAPIType: ETextureType_ByReference, pColorSpace: EColorSpace_ByReference, pTextureBounds: VRTextureBounds.ByReference): Int
    }

    /** Release the nativeTextureHandle provided from the GetOverlayTexture call, this allows the system to free the underlying GPU resources for this object,
     * so only do it once you stop rendering this texture.     */
    fun releaseNativeOverlayHandle(overlayHandle: VROverlayHandle, nativeTextureHandle: Pointer) = EVROverlayError.of(ReleaseNativeOverlayHandle!!(overlayHandle, nativeTextureHandle))

    @JvmField
    var ReleaseNativeOverlayHandle: ReleaseNativeOverlayHandle_callback? = null

    interface ReleaseNativeOverlayHandle_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pNativeTextureHandle: Pointer): Int
    }

    /** Get the size of the overlay texture */
    fun getOverlayTextureSize(overlayHandle: VROverlayHandle, width: IntByReference, height: IntByReference) = EVROverlayError.of(GetOverlayTextureSize!!(overlayHandle, width, height))

    fun getOverlayTextureSize(overlayHandle: VROverlayHandle, size: Vec2i): EVROverlayError {
        val width = IntByReference()
        val height = IntByReference()
        return EVROverlayError.of(GetOverlayTextureSize!!(overlayHandle, width, height)).also { size.put(width.value, height.value) }
    }

    @JvmField
    var GetOverlayTextureSize: GetOverlayTextureSize_callback? = null

    interface GetOverlayTextureSize_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pWidth: IntByReference, pHeight: IntByReference): Int
    }

    // ----------------------------------------------
    // Dashboard Overlay Methods
    // ----------------------------------------------

    /** Creates a dashboard overlay and returns its handle */
    fun createDashboardOverlay(overlayKey: String, overlayFriendlyName: String, mainHandle: VROverlayHandle_ByReference, thumbnailHandle: VROverlayHandle_ByReference) = EVROverlayError.of(CreateDashboardOverlay!!(overlayKey, overlayFriendlyName, mainHandle, thumbnailHandle))

    @JvmField
    var CreateDashboardOverlay: CreateDashboardOverlay_callback? = null

    interface CreateDashboardOverlay_callback : Callback {
        operator fun invoke(pchOverlayKey: String, pchOverlayFriendlyName: String, pMainHandle: VROverlayHandle_ByReference, pThumbnailHandle: VROverlayHandle_ByReference): Int
    }

    /** Returns true if the dashboard is visible */
    val isDashboardVisible get() = IsDashboardVisible!!()

    @JvmField
    var IsDashboardVisible: IsDashboardVisible_callback? = null

    interface IsDashboardVisible_callback : Callback {
        operator fun invoke(): Boolean
    }

    /** returns true if the dashboard is visible and the specified overlay is the active system Overlay */
    infix fun isActiveDashboardOverlay(overlayHandle: VROverlayHandle) = IsActiveDashboardOverlay!!(overlayHandle)

    @JvmField
    var IsActiveDashboardOverlay: IsActiveDashboardOverlay_callback? = null

    interface IsActiveDashboardOverlay_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle): Boolean
    }

    /** Sets the dashboard overlay to only appear when the specified process ID has scene focus */
    fun setDashboardOverlaySceneProcess(overlayHandle: VROverlayHandle, processId: Int) = EVROverlayError.of(SetDashboardOverlaySceneProcess!!(overlayHandle, processId))

    @JvmField
    var SetDashboardOverlaySceneProcess: SetDashboardOverlaySceneProcess_callback? = null

    interface SetDashboardOverlaySceneProcess_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, unProcessId: Int): Int
    }

    /** Gets the process ID that this dashboard overlay requires to have scene focus */
    fun getDashboardOverlaySceneProcess(overlayHandle: VROverlayHandle, processId: IntByReference) = EVROverlayError.of(GetDashboardOverlaySceneProcess!!(overlayHandle, processId))

    @JvmField
    var GetDashboardOverlaySceneProcess: GetDashboardOverlaySceneProcess_callback? = null

    interface GetDashboardOverlaySceneProcess_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, punProcessId: IntByReference): Int
    }

    /** Shows the dashboard. */
    fun showDashboard(overlayToShow: String) = ShowDashboard!!(overlayToShow)

    @JvmField
    var ShowDashboard: ShowDashboard_callback? = null

    interface ShowDashboard_callback : Callback {
        operator fun invoke(pchOverlayToShow: String)
    }

    /** Returns the tracked device that has the laser pointer in the dashboard */
    val primaryDashboardDevice get() = GetPrimaryDashboardDevice!!()

    @JvmField
    var GetPrimaryDashboardDevice: GetPrimaryDashboardDevice_callback? = null

    interface GetPrimaryDashboardDevice_callback : Callback {
        operator fun invoke(): TrackedDeviceIndex
    }

    // ---------------------------------------------
    // Keyboard methods
    // ---------------------------------------------

    /** Show the virtual keyboard to accept input **/
    fun showKeyboard(inputMode: EGamepadTextInputMode, lineInputMode: EGamepadTextInputLineMode, description: String, charMax: Int, existingText: String, useMinimalMode: Boolean, userValue: Long) = EVROverlayError.of(ShowKeyboard!!(inputMode.i, lineInputMode.i, description, charMax, existingText, useMinimalMode, userValue))

    @JvmField
    var ShowKeyboard: ShowKeyboard_callback? = null

    interface ShowKeyboard_callback : Callback {
        operator fun invoke(eInputMode: Int, eLineInputMode: Int, pchDescription: String, unCharMax: Int, pchExistingText: String, bUseMinimalMode: Boolean, uUserValue: Long): Int
    }


    fun showKeyboardForOverlay(overlayHandle: VROverlayHandle, inputMode: EGamepadTextInputMode, lineInputMode: EGamepadTextInputLineMode, description: String, charMax: Int, existingText: String, useMinimalMode: Boolean, userValue: Long) = EVROverlayError.of(ShowKeyboardForOverlay!!(overlayHandle, inputMode.i, lineInputMode.i, description, charMax, existingText, useMinimalMode, userValue))

    @JvmField
    var ShowKeyboardForOverlay: ShowKeyboardForOverlay_callback? = null

    interface ShowKeyboardForOverlay_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, eInputMode: Int, eLineInputMode: Int, pchDescription: String, unCharMax: Int, pchExistingText: String, bUseMinimalMode: Boolean, uUserValue: Long): Int
    }

    /** Get the text that was entered into the text input **/
    fun getKeyboardText(text: String, textSize: Int) = GetKeyboardText!!(text, textSize)

    @JvmField
    var GetKeyboardText: GetKeyboardText_callback? = null

    interface GetKeyboardText_callback : Callback {
        operator fun invoke(pchText: String, cchText: Int): Int
    }

    /** Hide the virtual keyboard **/
    fun hideKeyboard() = HideKeyboard!!()

    @JvmField
    var HideKeyboard: HideKeyboard_callback? = null

    interface HideKeyboard_callback : Callback {
        operator fun invoke()
    }

    /** Set the position of the keyboard in world space **/
    fun setKeyboardTransformAbsolute(trackingOrigin: ETrackingUniverseOrigin, trackingOriginToKeyboardTransform: HmdMat34.ByReference) = SetKeyboardTransformAbsolute!!(trackingOrigin.i, trackingOriginToKeyboardTransform)

    @JvmField
    var SetKeyboardTransformAbsolute: SetKeyboardTransformAbsolute_callback? = null

    interface SetKeyboardTransformAbsolute_callback : Callback {
        operator fun invoke(eTrackingOrigin: Int, pmatTrackingOriginToKeyboardTransform: HmdMat34.ByReference)
    }

    /** Set the position of the keyboard in overlay space by telling it to avoid a rectangle in the overlay. Rectangle coords have (0,0) in the bottom left **/
    fun setKeyboardPositionForOverlay(overlayHandle: VROverlayHandle, avoidRect: HmdRect2) = SetKeyboardPositionForOverlay!!(overlayHandle, avoidRect)

    @JvmField
    var SetKeyboardPositionForOverlay: SetKeyboardPositionForOverlay_callback? = null

    interface SetKeyboardPositionForOverlay_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, avoidRect: HmdRect2)
    }


    // ---------------------------------------------
    // Overlay input methods
    // ---------------------------------------------

    /** Sets a list of primitives to be used for controller ray intersection typically the size of the underlying UI in pixels
     *  (not in world space). */
    @JvmOverloads
    fun setOverlayIntersectionMask(overlayHandle: VROverlayHandle, maskPrimitives: VROverlayIntersectionMaskPrimitive.ByReference, numMaskPrimitives: Int, primitiveSize: Int = Int.BYTES + Pointer.SIZE) = SetOverlayIntersectionMask!!(overlayHandle, maskPrimitives, numMaskPrimitives, primitiveSize)

    @JvmField
    var SetOverlayIntersectionMask: SetOverlayIntersectionMask_callback? = null

    interface SetOverlayIntersectionMask_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pMaskPrimitives: VROverlayIntersectionMaskPrimitive.ByReference, unNumMaskPrimitives: Int, unPrimitiveSize: Int): EVROverlayError
    }


    fun getOverlayFlags(overlayHandle: VROverlayHandle, flags: IntByReference) = EVROverlayError.of(GetOverlayFlags!!(overlayHandle, flags))

    @JvmField
    var GetOverlayFlags: GetOverlayFlags_callback? = null

    interface GetOverlayFlags_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, pFlags: IntByReference): Int
    }

    // ---------------------------------------------
    // Message box methods
    // ---------------------------------------------

    /** Show the message overlay. This will block and return you a result. **/
    @JvmOverloads
    fun showMessageOverlay(text: String, caption: String, button0Text: String, button1Text: String? = null, button2Text: String? = null, button3Text: String? = null) = VRMessageOverlayResponse.of(ShowMessageOverlay!!(text, caption, button0Text, button1Text, button2Text, button3Text))

    @JvmField
    var ShowMessageOverlay: ShowMessageOverlay_callback? = null

    interface ShowMessageOverlay_callback : Callback {
        operator fun invoke(pchText: String, pchCaption: String, pchButton0Text: String, pchButton1Text: String?, pchButton2Text: String?, pchButton3Text: String?): Int
    }

    /** If the calling process owns the overlay and it's open, this will close it. **/
    fun closeMessageOverlay() = CloseMessageOverlay!!()

    @JvmField
    var CloseMessageOverlay: CloseMessageOverlay_callback? = null

    interface CloseMessageOverlay_callback : Callback {
        operator fun invoke()
    }

    constructor()

    override fun getFieldOrder()= listOf("FindOverlay", "CreateOverlay", "DestroyOverlay",
            "SetHighQualityOverlay", "GetHighQualityOverlay", "GetOverlayKey", "GetOverlayName", "SetOverlayName",
            "GetOverlayImageData", "GetOverlayErrorNameFromEnum", "SetOverlayRenderingPid", "GetOverlayRenderingPid", "SetOverlayFlag",
            "GetOverlayFlag", "SetOverlayColor", "GetOverlayColor", "SetOverlayAlpha", "GetOverlayAlpha", "SetOverlayTexelAspect",
            "GetOverlayTexelAspect", "SetOverlaySortOrder", "GetOverlaySortOrder", "SetOverlayWidthInMeters",
            "GetOverlayWidthInMeters", "SetOverlayAutoCurveDistanceRangeInMeters", "GetOverlayAutoCurveDistanceRangeInMeters",
            "SetOverlayTextureColorSpace", "GetOverlayTextureColorSpace", "SetOverlayTextureBounds", "GetOverlayTextureBounds",
            "GetOverlayRenderModel", "SetOverlayRenderModel", "GetOverlayTransformType", "SetOverlayTransformAbsolute",
            "GetOverlayTransformAbsolute", "SetOverlayTransformTrackedDeviceRelative", "GetOverlayTransformTrackedDeviceRelative",
            "SetOverlayTransformTrackedDeviceComponent", "GetOverlayTransformTrackedDeviceComponent",
            "GetOverlayTransformOverlayRelative", "SetOverlayTransformOverlayRelative", "ShowOverlay", "HideOverlay",
            "IsOverlayVisible", "GetTransformForOverlayCoordinates", "PollNextOverlayEvent", "GetOverlayInputMethod",
            "SetOverlayInputMethod", "GetOverlayMouseScale", "SetOverlayMouseScale", "ComputeOverlayIntersection",
            "IsHoverTargetOverlay", "GetGamepadFocusOverlay", "SetGamepadFocusOverlay", "SetOverlayNeighbor",
            "MoveGamepadFocusToNeighbor", "SetOverlayDualAnalogTransform", "GetOverlayDualAnalogTransform", "SetOverlayTexture",
            "ClearOverlayTexture", "SetOverlayRaw", "SetOverlayFromFile", "GetOverlayTexture", "ReleaseNativeOverlayHandle",
            "GetOverlayTextureSize", "CreateDashboardOverlay", "IsDashboardVisible", "IsActiveDashboardOverlay",
            "SetDashboardOverlaySceneProcess", "GetDashboardOverlaySceneProcess", "ShowDashboard", "GetPrimaryDashboardDevice",
            "ShowKeyboard", "ShowKeyboardForOverlay", "GetKeyboardText", "HideKeyboard", "SetKeyboardTransformAbsolute",
            "SetKeyboardPositionForOverlay", "SetOverlayIntersectionMask", "GetOverlayFlags", "ShowMessageOverlay",
            "CloseMessageOverlay")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVROverlay(), Structure.ByReference
    class ByValue : IVROverlay(), Structure.ByValue
}

val IVROverlay_Version = "IVROverlay_018"