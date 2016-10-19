import com.sun.jna.*
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.util.*

/**
 * Created by GBarbieri on 07.10.2016.
 */


class OpenVR : Library {
    companion object {
        init {
            Native.register(NativeLibrary.getInstance("openvr_api"))
        }
    }
}

/*struct VkDevice_T;
struct VkPhysicalDevice_T;
struct VkInstance_T;
struct VkQueue_T;*/

/**
 * right-handed system
 * +y is up
 * +x is to the right
 * -z is going away from you
 * Distance unit is meters
 */
open class HmdMatrix34_t : Structure {

    // C type : float[3][4]
    var m = FloatArray(3 * 4)

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("m")

    constructor(m: FloatArray) : super() {
        if (m.size != this.m.size) throw IllegalArgumentException("Wrong array size !")
        this.m = m
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdMatrix34_t(), Structure.ByReference
    class ByValue : HmdMatrix34_t(), Structure.ByValue
}

open class HmdMatrix44_t : Structure {

    // C type : float[4][4]
    var m = FloatArray(4 * 4)

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("m")

    constructor(m: FloatArray) : super() {
        if (m.size != this.m.size) throw IllegalArgumentException("Wrong array size !")
        this.m = m
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdMatrix44_t(), Structure.ByReference
    class ByValue : HmdMatrix44_t(), Structure.ByValue
}

open class HmdVector3_t : Structure {

    //C type : float[3]
    var v = FloatArray(3)

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("i")

    constructor(v: FloatArray) : super() {
        if (v.size != this.v.size) throw IllegalArgumentException("Wrong array size !")
        this.v = v
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdVector3_t(), Structure.ByReference
    class ByValue : HmdVector3_t(), Structure.ByValue

    fun toDbb(bb: ByteBuffer, offset: Int) {
        // TODO move java.lang.Float.BYTES => glm
        for (i in 0..2) bb.putFloat(offset + i * java.lang.Float.BYTES, v[i])
    }

    companion object {
        val SIZE = 3 * java.lang.Float.BYTES
    }
}

open class HmdVector4_t : Structure {

    // C type : float[4]
    var v = FloatArray(4)

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("i")

    constructor(v: FloatArray) : super() {
        if (v.size != this.v.size) throw IllegalArgumentException("Wrong array size !")
        this.v = v
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdVector4_t(), Structure.ByReference
    class ByValue : HmdVector4_t(), Structure.ByValue
}

open class HmdVector3d_t : Structure {

    //C type : double[3]
    var v = DoubleArray(3)

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("i")

    constructor(v: DoubleArray) : super() {
        if (v.size != this.v.size) throw IllegalArgumentException("Wrong array size !")
        this.v = v
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdVector3d_t(), Structure.ByReference
    class ByValue : HmdVector3d_t(), Structure.ByValue
}

open class HmdVector2_t : Structure {

    // C type : float[2]
    var v = FloatArray(2)

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("i")

    constructor(v: FloatArray) : super() {
        if (v.size != this.v.size) throw IllegalArgumentException("Wrong array size !")
        this.v = v
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdVector2_t(), Structure.ByReference
    class ByValue : HmdVector2_t(), Structure.ByValue
}

open class HmdQuaternion_t : Structure {

    var w: Double = 0.0
    var x: Double = 0.0
    var y: Double = 0.0
    var z: Double = 0.0

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("w", "x", "y", "z")

    constructor(w: Double, x: Double, y: Double, z: Double) : super() {
        this.w = w
        this.x = x
        this.y = y
        this.z = z
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdQuaternion_t(), Structure.ByReference
    class ByValue : HmdQuaternion_t(), Structure.ByValue
}

open class HmdColor_t : Structure {

    var r: Float = 0f
    var g: Float = 0f
    var b: Float = 0f
    var a: Float = 0f

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("r", "g", "b", "a")

    constructor(r: Float, g: Float, b: Float, a: Float) : super() {
        this.r = r
        this.g = g
        this.b = b
        this.a = a
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdColor_t(), Structure.ByReference
    class ByValue : HmdColor_t(), Structure.ByValue
}

open class HmdQuad_t : Structure {

    // C type : HmdVector3_t[4]
    var vCorners = arrayOf(HmdVector3_t(), HmdVector3_t(), HmdVector3_t(), HmdVector3_t())

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("vCorners")

    constructor(vCorners: Array<HmdVector3_t>) : super() {
        if (vCorners.size != this.vCorners.size) throw IllegalArgumentException("Wrong array size !")
        this.vCorners = vCorners
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdQuad_t(), Structure.ByReference
    class ByValue : HmdQuad_t(), Structure.ByValue
}

open class HmdRect2_t : Structure {

    var vTopLeft = HmdVector2_t()
    var vBottomRight = HmdVector2_t()

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("vTopLeft", "vBottomRight")

    constructor(vTopLeft: HmdVector2_t, vBottomRight: HmdVector2_t) : super() {
        this.vTopLeft = vTopLeft
        this.vBottomRight = vBottomRight
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdRect2_t(), Structure.ByReference
    class ByValue : HmdRect2_t(), Structure.ByValue
}

/**
 * Used to return the post-distortion UVs for each color channel.
 * UVs range from 0 to 1 with 0,0 in the upper left corner of the source render target. The 0,0 to 1,1 range covers a single eye.
 */
open class DistortionCoordinates_t : Structure {

    var rfRed = FloatArray(2)
    var rfGreen = FloatArray(2)
    var rfBlue = FloatArray(2)

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("rfRed", "rfGreen", "rfBlue")

    constructor(rfRed: FloatArray, rfGreen: FloatArray, rfBlue: FloatArray) : super() {
        if (rfRed.size != this.rfRed.size) throw IllegalArgumentException("Wrong rfRed array size !")
        this.rfRed = rfRed
        if (rfGreen.size != this.rfGreen.size) throw IllegalArgumentException("Wrong rfGreen array size !")
        this.rfGreen = rfGreen
        if (rfBlue.size != this.rfBlue.size) throw IllegalArgumentException("Wrong rfBlue array size !")
        this.rfBlue = rfBlue
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : DistortionCoordinates_t(), Structure.ByReference
    class ByValue : DistortionCoordinates_t(), Structure.ByValue
}

enum class EVREye(val i: Int) {
    Eye_Left(0),
    Eye_Right(1)
}

enum class EGraphicsAPIConvention(val i: Int) {
    API_DirectX(0), // Normalized Z goes from 0 at the viewer to 1 at the far clip plane
    API_OpenGL(1)   // Normalized Z goes from 1 at the viewer to -1 at the far clip plane
}

enum class EColorSpace(val i: Int) {
    ColorSpace_Auto(0), // Assumes 'gamma' for 8-bit per component formats, otherwise 'linear'.  This mirrors the DXGI formats which have _SRGB variants.
    ColorSpace_Gamma(1), // Texture data can be displayed directly on the display without any conversion (a.k.a. display native format).
    ColorSpace_Linear(2)    // Same as gamma but has been converted to a linear representation using DXGI's sRGB conversion algorithm.
}

open class Texture_t : Structure {

    var handle = 0  // Native d3d texture pointer or GL texture id.
    var eType = 0
    var eColorSpace = 0

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("handle", "eType", "eColorSpace")

    constructor(handle: Int, eType: Int, eColorSpace: Int) : super() {
        set(handle, eType, eColorSpace)
    }

    operator fun set(handle: Int, eType: Int, eColorSpace: Int) {
        this.handle = handle
        this.eType = eType
        this.eColorSpace = eColorSpace
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : Texture_t(), Structure.ByReference
    class ByValue : Texture_t(), Structure.ByValue
}

// Handle to a shared texture (HANDLE on Windows obtained using OpenSharedResource).
typealias SharedTextureHandle_t = Long
const val INVALID_SHARED_TEXTURE_HANDLE = 0L

enum class ETrackingResult(val i: Int) {
    TrackingResult_Uninitialized(1),

    TrackingResult_Calibrating_InProgress(100),
    TrackingResult_Calibrating_OutOfRange(101),

    TrackingResult_Running_OK(200),
    TrackingResult_Running_OutOfRange(201)
}

const val k_unTrackingStringSize = 32
const val k_unMaxDriverDebugResponseSize = 32768

/** Used to pass device IDs to API calls */
typealias TrackedDeviceIndex_t = Int
const val k_unTrackedDeviceIndex_Hmd = 0
const val k_unMaxTrackedDeviceCount = 16
const val k_unTrackedDeviceIndexOther = 0xFFFFFFFE.toInt();
const val k_unTrackedDeviceIndexInvalid = 0xFFFFFFFF.toInt();

/** Describes what kind of object is being tracked at a given ID */
enum class ETrackedDeviceClass(val i: Int) {
    TrackedDeviceClass_Invalid(0), // the ID was not valid.
    TrackedDeviceClass_HMD(1), // Head-Mounted Displays
    TrackedDeviceClass_Controller(2), // Tracked controllers
    TrackedDeviceClass_TrackingReference(4), // Camera and base stations that serve as tracking reference points

    TrackedDeviceClass_Count(5), // This isn't a class that will ever be returned. It is used for allocating arrays and such

    TrackedDeviceClass_Other(1000)
}

/** Describes what specific role associated with a tracked device */
enum class ETrackedControllerRole(val i: Int) {
    TrackedControllerRole_Invalid(0), // Invalid value for controller type
    TrackedControllerRole_LeftHand(1), // Tracked device associated with the left hand
    TrackedControllerRole_RightHand(2) // Tracked device associated with the right hand
}

/** describes a single pose for a tracked object */
open class TrackedDevicePose_t : Structure {

    var mDeviceToAbsoluteTracking = HmdMatrix34_t()
    var vVelocity = HmdVector3_t()          // velocity in tracker space in m/s
    var vAngularVelocity = HmdVector3_t()   // angular velocity in radians/s (?)
    var eTrackingResult = 0
    var bPoseIsValid: Byte = 0
    /**
     * This indicates that there is a device connected for this spot in the pose array.
     * It could go from true to false if the user unplugs the device.
     */
    var bDeviceIsConnected: Byte = 0

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("mDeviceToAbsoluteTracking", "vVelocity", "vAngularVelocity",
            "eTrackingResult", "bPoseIsValid", "bDeviceIsConnected")

    /**
     * @param mDeviceToAbsoluteTracking C type : HmdMatrix34_t<br></br>
     * *
     * @param vVelocity C type : HmdVector3_t<br></br>
     * *
     * @param vAngularVelocity C type : HmdVector3_t<br></br>
     * *
     * @param eTrackingResult @see ETrackingResult<br></br>
     * * C type : ETrackingResult
     */
    constructor(mDeviceToAbsoluteTracking: HmdMatrix34_t, vVelocity: HmdVector3_t, vAngularVelocity: HmdVector3_t,
                eTrackingResult: Int, bPoseIsValid: Byte, bDeviceIsConnected: Byte) : super() {
        this.mDeviceToAbsoluteTracking = mDeviceToAbsoluteTracking
        this.vVelocity = vVelocity
        this.vAngularVelocity = vAngularVelocity
        this.eTrackingResult = eTrackingResult
        this.bPoseIsValid = bPoseIsValid
        this.bDeviceIsConnected = bDeviceIsConnected
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : TrackedDevicePose_t(), Structure.ByReference
    class ByValue : TrackedDevicePose_t(), Structure.ByValue
}

/** Identifies which style of tracking origin the application wants to use for the poses it is requesting */
enum class ETrackingUniverseOrigin(val i: Int) {
    TrackingUniverseSeated(0), // Poses are provided relative to the seated zero pose
    TrackingUniverseStanding(1), // Poses are provided relative to the safe bounds configured by the user
    TrackingUniverseRawAndUncalibrated(2)    // Poses are provided in the coordinate system defined by the driver. You probably don't want this one.
}

/** Each entry in this enum represents a property that can be retrieved about a tracked device.
 * Many fields are only valid for one ETrackedDeviceClass.
 */
enum class ETrackedDeviceProperty(val i: Int) {
    // general properties that apply to all device classes
    Prop_TrackingSystemName_String(1000),
    Prop_ModelNumber_String(1001),
    Prop_SerialNumber_String(1002),
    Prop_RenderModelName_String(1003),
    Prop_WillDriftInYaw_Bool(1004),
    Prop_ManufacturerName_String(1005),
    Prop_TrackingFirmwareVersion_String(1006),
    Prop_HardwareRevision_String(1007),
    Prop_AllWirelessDongleDescriptions_String(1008),
    Prop_ConnectedWirelessDongle_String(1009),
    Prop_DeviceIsWireless_Bool(1010),
    Prop_DeviceIsCharging_Bool(1011),
    Prop_DeviceBatteryPercentage_Float(1012), // 0 is empty), 1 is full
    Prop_StatusDisplayTransform_Matrix34(1013),
    Prop_Firmware_UpdateAvailable_Bool(1014),
    Prop_Firmware_ManualUpdate_Bool(1015),
    Prop_Firmware_ManualUpdateURL_String(1016),
    Prop_HardwareRevision_Uint64(1017),
    Prop_FirmwareVersion_Uint64(1018),
    Prop_FPGAVersion_Uint64(1019),
    Prop_VRCVersion_Uint64(1020),
    Prop_RadioVersion_Uint64(1021),
    Prop_DongleVersion_Uint64(1022),
    Prop_BlockServerShutdown_Bool(1023),
    Prop_CanUnifyCoordinateSystemWithHmd_Bool(1024),
    Prop_ContainsProximitySensor_Bool(1025),
    Prop_DeviceProvidesBatteryStatus_Bool(1026),
    Prop_DeviceCanPowerOff_Bool(1027),
    Prop_Firmware_ProgrammingTarget_String(1028),
    Prop_DeviceClass_Int32(1029),
    Prop_HasCamera_Bool(1030),
    Prop_DriverVersion_String(1031),
    Prop_Firmware_ForceUpdateRequired_Bool(1032),

    // Properties that are unique to TrackedDeviceClass_HMD
    Prop_ReportsTimeSinceVSync_Bool(2000),
    Prop_SecondsFromVsyncToPhotons_Float(2001),
    Prop_DisplayFrequency_Float(2002),
    Prop_UserIpdMeters_Float(2003),
    Prop_CurrentUniverseId_Uint64(2004),
    Prop_PreviousUniverseId_Uint64(2005),
    Prop_DisplayFirmwareVersion_Uint64(2006),
    Prop_IsOnDesktop_Bool(2007),
    Prop_DisplayMCType_Int32(2008),
    Prop_DisplayMCOffset_Float(2009),
    Prop_DisplayMCScale_Float(2010),
    Prop_EdidVendorID_Int32(2011),
    Prop_DisplayMCImageLeft_String(2012),
    Prop_DisplayMCImageRight_String(2013),
    Prop_DisplayGCBlackClamp_Float(2014),
    Prop_EdidProductID_Int32(2015),
    Prop_CameraToHeadTransform_Matrix34(2016),
    Prop_DisplayGCType_Int32(2017),
    Prop_DisplayGCOffset_Float(2018),
    Prop_DisplayGCScale_Float(2019),
    Prop_DisplayGCPrescale_Float(2020),
    Prop_DisplayGCImage_String(2021),
    Prop_LensCenterLeftU_Float(2022),
    Prop_LensCenterLeftV_Float(2023),
    Prop_LensCenterRightU_Float(2024),
    Prop_LensCenterRightV_Float(2025),
    Prop_UserHeadToEyeDepthMeters_Float(2026),
    Prop_CameraFirmwareVersion_Uint64(2027),
    Prop_CameraFirmwareDescription_String(2028),
    Prop_DisplayFPGAVersion_Uint64(2029),
    Prop_DisplayBootloaderVersion_Uint64(2030),
    Prop_DisplayHardwareVersion_Uint64(2031),
    Prop_AudioFirmwareVersion_Uint64(2032),
    Prop_CameraCompatibilityMode_Int32(2033),
    Prop_ScreenshotHorizontalFieldOfViewDegrees_Float(2034),
    Prop_ScreenshotVerticalFieldOfViewDegrees_Float(2035),
    Prop_DisplaySuppressed_Bool(2036),

    // Properties that are unique to TrackedDeviceClass_Controller
    Prop_AttachedDeviceId_String(3000),
    Prop_SupportedButtons_Uint64(3001),
    Prop_Axis0Type_Int32(3002), // Return value is of type EVRControllerAxisType
    Prop_Axis1Type_Int32(3003), // Return value is of type EVRControllerAxisType
    Prop_Axis2Type_Int32(3004), // Return value is of type EVRControllerAxisType
    Prop_Axis3Type_Int32(3005), // Return value is of type EVRControllerAxisType
    Prop_Axis4Type_Int32(3006), // Return value is of type EVRControllerAxisType
    Prop_ControllerRoleHint_Int32(3007), // Return value is of type ETrackedControllerRole

    // Properties that are unique to TrackedDeviceClass_TrackingReference
    Prop_FieldOfViewLeftDegrees_Float(4000),
    Prop_FieldOfViewRightDegrees_Float(4001),
    Prop_FieldOfViewTopDegrees_Float(4002),
    Prop_FieldOfViewBottomDegrees_Float(4003),
    Prop_TrackingRangeMinimumMeters_Float(4004),
    Prop_TrackingRangeMaximumMeters_Float(4005),
    Prop_ModeLabel_String(4006),

    // Properties that are used for user interface like icons names
    Prop_IconPathName_String(5000), // usually a directory named "icons"
    Prop_NamedIconPathDeviceOff_String(5001), // PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    Prop_NamedIconPathDeviceSearching_String(5002), // PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    Prop_NamedIconPathDeviceSearchingAlert_String(5003), // PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    Prop_NamedIconPathDeviceReady_String(5004), // PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    Prop_NamedIconPathDeviceReadyAlert_String(5005), // PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    Prop_NamedIconPathDeviceNotReady_String(5006), // PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    Prop_NamedIconPathDeviceStandby_String(5007), // PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    Prop_NamedIconPathDeviceAlertLow_String(5008), // PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others

    // Vendors are free to expose private debug data in this reserved region
    Prop_VendorSpecific_Reserved_Start(10000),
    Prop_VendorSpecific_Reserved_End(10999)
}

/** No string property will ever be longer than this length */
const val k_unMaxPropertyStringSize = 32 * 1024

/** Used to return errors that occur when reading properties. */
enum class ETrackedPropertyError(val i: Int) {
    TrackedProp_Success(0),
    TrackedProp_WrongDataType(1),
    TrackedProp_WrongDeviceClass(2),
    TrackedProp_BufferTooSmall(3),
    TrackedProp_UnknownProperty(4),
    TrackedProp_InvalidDevice(5),
    TrackedProp_CouldNotContactServer(6),
    TrackedProp_ValueNotProvidedByDevice(7),
    TrackedProp_StringExceedsMaximumLength(8),
    TrackedProp_NotYetAvailable(9) // The property value isn't known yet, but is expected soon. Call again later.
}

/** Allows the application to control what part of the provided texture will be used in the frame buffer. */
open class VRTextureBounds_t : Structure {

    var uMin = 0f
    var vMin = 0f
    var uMax = 0f
    var vMax = 0f

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("uMin", "vMin", "uMax", "vMax")

    constructor(uMin: Float, vMin: Float, uMax: Float, vMax: Float) : super() {
        this.uMin = uMin
        this.vMin = vMin
        this.uMax = uMax
        this.vMax = vMax
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VRTextureBounds_t(), Structure.ByReference
    class ByValue : VRTextureBounds_t(), Structure.ByValue
}

/** Allows the application to control how scene textures are used by the compositor when calling Submit. */
enum class EVRSubmitFlags(val i: Int) {
    // Simple render path. App submits rendered left and right eye images with no lens distortion correction applied.
    Submit_Default(0x00),

    // App submits final left and right eye images with lens distortion already applied (lens distortion makes the images appear
    // barrel distorted with chromatic aberration correction applied). The app would have used the data returned by
    // vr::IVRSystem::ComputeDistortion() to apply the correct distortion to the rendered images before calling Submit().
    Submit_LensDistortionAlreadyApplied(0x01),

    // If the texture pointer passed in is actually a renderbuffer (e.g. for MSAA in OpenGL) then set this flag.
    Submit_GlRenderBuffer(0x02),

    // Handle is pointer to VulkanData_t
    Submit_VulkanTexture(0x04)
}

/** Data required for passing Vulkan textures to IVRCompositor::Submit.
 * Be sure to call OpenVR_Shutdown before destroying these resources. */
/*struct VulkanData_t
{
    uint64_t m_nImage; // VkImage
    VkDevice_T *m_pDevice;
    VkPhysicalDevice_T *m_pPhysicalDevice;
    VkInstance_T *m_pInstance;
    VkQueue_T *m_pQueue;
    uint32_t m_nQueueFamilyIndex;
    uint32_t m_nWidth, m_nHeight, m_nFormat, m_nSampleCount;
};*/

/** Status of the overall system or tracked objects */
enum class EVRState(val i: Int) {
    VRState_Undefined(-1),
    VRState_Off(0),
    VRState_Searching(1),
    VRState_Searching_Alert(2),
    VRState_Ready(3),
    VRState_Ready_Alert(4),
    VRState_NotReady(5),
    VRState_Standby(6),
    VRState_Ready_Alert_Low(7)
}

/** The types of events that could be posted (and what the parameters mean for each event type) */
enum class EVREventType(val i: Int) {
    VREvent_None(0),

    VREvent_TrackedDeviceActivated(100),
    VREvent_TrackedDeviceDeactivated(101),
    VREvent_TrackedDeviceUpdated(102),
    VREvent_TrackedDeviceUserInteractionStarted(103),
    VREvent_TrackedDeviceUserInteractionEnded(104),
    VREvent_IpdChanged(105),
    VREvent_EnterStandbyMode(106),
    VREvent_LeaveStandbyMode(107),
    VREvent_TrackedDeviceRoleChanged(108),
    VREvent_WatchdogWakeUpRequested(109),
    VREvent_LensDistortionChanged(110),

    VREvent_ButtonPress(200), // data is controller
    VREvent_ButtonUnpress(201), // data is controller
    VREvent_ButtonTouch(202), // data is controller
    VREvent_ButtonUntouch(203), // data is controller

    VREvent_MouseMove(300), // data is mouse
    VREvent_MouseButtonDown(301), // data is mouse
    VREvent_MouseButtonUp(302), // data is mouse
    VREvent_FocusEnter(303), // data is overlay
    VREvent_FocusLeave(304), // data is overlay
    VREvent_Scroll(305), // data is mouse
    VREvent_TouchPadMove(306), // data is mouse
    VREvent_OverlayFocusChanged(307), // data is overlay), global event

    VREvent_InputFocusCaptured(400), // data is process DEPRECATED
    VREvent_InputFocusReleased(401), // data is process DEPRECATED
    VREvent_SceneFocusLost(402), // data is process
    VREvent_SceneFocusGained(403), // data is process
    VREvent_SceneApplicationChanged(404), // data is process - The App actually drawing the scene changed (usually to or from the compositor)
    VREvent_SceneFocusChanged(405), // data is process - New app got access to draw the scene
    VREvent_InputFocusChanged(406), // data is process
    VREvent_SceneApplicationSecondaryRenderingStarted(407), // data is process

    VREvent_HideRenderModels(410), // Sent to the scene application to request hiding render models temporarily
    VREvent_ShowRenderModels(411), // Sent to the scene application to request restoring render model visibility

    VREvent_OverlayShown(500),
    VREvent_OverlayHidden(501),
    VREvent_DashboardActivated(502),
    VREvent_DashboardDeactivated(503),
    VREvent_DashboardThumbSelected(504), // Sent to the overlay manager - data is overlay
    VREvent_DashboardRequested(505), // Sent to the overlay manager - data is overlay
    VREvent_ResetDashboard(506), // Send to the overlay manager
    VREvent_RenderToast(507), // Send to the dashboard to render a toast - data is the notification ID
    VREvent_ImageLoaded(508), // Sent to overlays when a SetOverlayRaw or SetOverlayFromFile call finishes loading
    VREvent_ShowKeyboard(509), // Sent to keyboard renderer in the dashboard to invoke it
    VREvent_HideKeyboard(510), // Sent to keyboard renderer in the dashboard to hide it
    VREvent_OverlayGamepadFocusGained(511), // Sent to an overlay when IVROverlay::SetFocusOverlay is called on it
    VREvent_OverlayGamepadFocusLost(512), // Send to an overlay when it previously had focus and IVROverlay::SetFocusOverlay is called on something else
    VREvent_OverlaySharedTextureChanged(513),
    VREvent_DashboardGuideButtonDown(514),
    VREvent_DashboardGuideButtonUp(515),
    VREvent_ScreenshotTriggered(516), // Screenshot button combo was pressed), Dashboard should request a screenshot
    VREvent_ImageFailed(517), // Sent to overlays when a SetOverlayRaw or SetOverlayfromFail fails to load
    VREvent_DashboardOverlayCreated(518),

    // Screenshot API
    VREvent_RequestScreenshot(520), // Sent by vrclient application to compositor to take a screenshot
    VREvent_ScreenshotTaken(521), // Sent by compositor to the application that the screenshot has been taken
    VREvent_ScreenshotFailed(522), // Sent by compositor to the application that the screenshot failed to be taken
    VREvent_SubmitScreenshotToDashboard(523), // Sent by compositor to the dashboard that a completed screenshot was submitted
    VREvent_ScreenshotProgressToDashboard(524), // Sent by compositor to the dashboard that a completed screenshot was submitted

    VREvent_Notification_Shown(600),
    VREvent_Notification_Hidden(601),
    VREvent_Notification_BeginInteraction(602),
    VREvent_Notification_Destroyed(603),

    VREvent_Quit(700), // data is process
    VREvent_ProcessQuit(701), // data is process
    VREvent_QuitAborted_UserPrompt(702), // data is process
    VREvent_QuitAcknowledged(703), // data is process
    VREvent_DriverRequestedQuit(704), // The driver has requested that SteamVR shut down

    VREvent_ChaperoneDataHasChanged(800),
    VREvent_ChaperoneUniverseHasChanged(801),
    VREvent_ChaperoneTempDataHasChanged(802),
    VREvent_ChaperoneSettingsHaveChanged(803),
    VREvent_SeatedZeroPoseReset(804),

    VREvent_AudioSettingsHaveChanged(820),

    VREvent_BackgroundSettingHasChanged(850),
    VREvent_CameraSettingsHaveChanged(851),
    VREvent_ReprojectionSettingHasChanged(852),
    VREvent_ModelSkinSettingsHaveChanged(853),
    VREvent_EnvironmentSettingsHaveChanged(854),
    VREvent_PowerSettingsHaveChanged(855),

    VREvent_StatusUpdate(900),

    VREvent_MCImageUpdated(1000),

    VREvent_FirmwareUpdateStarted(1100),
    VREvent_FirmwareUpdateFinished(1101),

    VREvent_KeyboardClosed(1200),
    VREvent_KeyboardCharInput(1201),
    VREvent_KeyboardDone(1202), // Sent when DONE button clicked on keyboard

    VREvent_ApplicationTransitionStarted(1300),
    VREvent_ApplicationTransitionAborted(1301),
    VREvent_ApplicationTransitionNewAppStarted(1302),
    VREvent_ApplicationListUpdated(1303),
    VREvent_ApplicationMimeTypeLoad(1304),

    VREvent_Compositor_MirrorWindowShown(1400),
    VREvent_Compositor_MirrorWindowHidden(1401),
    VREvent_Compositor_ChaperoneBoundsShown(1410),
    VREvent_Compositor_ChaperoneBoundsHidden(1411),

    VREvent_TrackedCamera_StartVideoStream(1500),
    VREvent_TrackedCamera_StopVideoStream(1501),
    VREvent_TrackedCamera_PauseVideoStream(1502),
    VREvent_TrackedCamera_ResumeVideoStream(1503),
    VREvent_TrackedCamera_EditingSurface(1550),

    VREvent_PerformanceTest_EnableCapture(1600),
    VREvent_PerformanceTest_DisableCapture(1601),
    VREvent_PerformanceTest_FidelityLevel(1602),

    // Vendors are free to expose private events in this reserved region
    VREvent_VendorSpecific_Reserved_Start(10000),
    VREvent_VendorSpecific_Reserved_End(19999)
}

/** Level of Hmd activity */
enum class EDeviceActivityLevel(val i: Int) {
    k_EDeviceActivityLevel_Unknown(-1),
    k_EDeviceActivityLevel_Idle(0),
    k_EDeviceActivityLevel_UserInteraction(1),
    k_EDeviceActivityLevel_UserInteraction_Timeout(2),
    k_EDeviceActivityLevel_Standby(3)
}

/** VR controller button and axis IDs */
enum class EVRButtonId(val i: Int) {
    k_EButton_System(0),
    k_EButton_ApplicationMenu(1),
    k_EButton_Grip(2),
    k_EButton_DPad_Left(3),
    k_EButton_DPad_Up(4),
    k_EButton_DPad_Right(5),
    k_EButton_DPad_Down(6),
    k_EButton_A(7),

    k_EButton_ProximitySensor(31),

    k_EButton_Axis0(32),
    k_EButton_Axis1(33),
    k_EButton_Axis2(34),
    k_EButton_Axis3(35),
    k_EButton_Axis4(36),

    // aliases for well known controllers
    k_EButton_SteamVR_Touchpad(k_EButton_Axis0.i),
    k_EButton_SteamVR_Trigger(k_EButton_Axis1.i),

    k_EButton_Dashboard_Back(k_EButton_Grip.i),

    k_EButton_Max(64)
}

fun buttonMaskFromId(id: EVRButtonId) = (1 shl id.i).toLong()

/** used for controller button events */
open class VREvent_Controller_t : Structure {
    // EVRButtonId enum
    var button = 0

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("button")

    constructor(button: Int) : super() {
        this.button = button
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Controller_t(), Structure.ByReference
    class ByValue : VREvent_Controller_t(), Structure.ByValue
}

/** used for simulated mouse events in overlay space */
enum class EVRMouseButton(val i: Int) {
    VRMouseButton_Left(0x0001),
    VRMouseButton_Right(0x0002),
    VRMouseButton_Middle(0x0004)
}

/** used for simulated mouse events in overlay space */
open class VREvent_Mouse_t : Structure {

    // co-ords are in GL space, bottom left of the texture is 0,0
    var x = 0f
    var y = 0f
    var button = 0  // EVRMouseButton enum

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("x", "y", "button")

    constructor(x: Float, y: Float, button: Int) : super() {
        this.x = x
        this.y = y
        this.button = button
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Mouse_t(), Structure.ByReference
    class ByValue : VREvent_Mouse_t(), Structure.ByValue
}

/** used for simulated mouse wheel scroll in overlay space */
open class VREvent_Scroll_t : Structure {

    // movement in fraction of the pad traversed since last delta, 1.0 for a full swipe
    var xdelta = 0f
    var ydelta = 0f
    var repeatCount = 0

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("xdelta", "ydelta", "repeatCount")

    constructor(xdelta: Float, ydelta: Float, repeatCount: Int) : super() {
        this.xdelta = xdelta
        this.ydelta = ydelta
        this.repeatCount = repeatCount
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Scroll_t(), Structure.ByReference
    class ByValue : VREvent_Scroll_t(), Structure.ByValue
}

/** when in mouse input mode you can receive data from the touchpad, these events are only sent if the users finger
 * is on the touchpad (or just released from it)
 **/
open class VREvent_TouchPadMove_t : Structure {

    // true if the users finger is detected on the touch pad
    var bFingerDown: Byte = 0

    // How long the finger has been down in seconds
    var flSecondsFingerDown = 0f

    // These values indicate the starting finger position (so you can do some basic swipe stuff)
    var fValueXFirst = 0f
    var fValueYFirst = 0f

    // This is the raw sampled coordinate without deadzoning
    var fValueXRaw = 0f
    var fValueYRaw = 0f

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("bFingerDown", "flSecondsFingerDown", "fValueXFirst", "fValueYFirst",
            "fValueXRaw", "fValueYRaw")

    constructor(bFingerDown: Byte, flSecondsFingerDown: Float, fValueXFirst: Float, fValueYFirst: Float, fValueXRaw: Float,
                fValueYRaw: Float) : super() {
        this.bFingerDown = bFingerDown
        this.flSecondsFingerDown = flSecondsFingerDown
        this.fValueXFirst = fValueXFirst
        this.fValueYFirst = fValueYFirst
        this.fValueXRaw = fValueXRaw
        this.fValueYRaw = fValueYRaw
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_TouchPadMove_t(), Structure.ByReference
    class ByValue : VREvent_TouchPadMove_t(), Structure.ByValue
}

/** notification related events. Details will still change at this point */
open class VREvent_Notification_t : Structure {

    var ulUserValue = 0L
    var notificationId = 0

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("ulUserValue", "notificationId")

    constructor(ulUserValue: Long, notificationId: Int) : super() {
        this.ulUserValue = ulUserValue
        this.notificationId = notificationId
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Notification_t(), Structure.ByReference
    class ByValue : VREvent_Notification_t(), Structure.ByValue
}

/** Used for events about processes */
open class VREvent_Process_t : Structure {

    var pid = 0
    var oldPid = 0
    var bForced: Byte = 0

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("pid", "oldPid", "bForced")

    constructor(pid: Int, oldPid: Int, bForced: Byte) : super() {
        this.pid = pid
        this.oldPid = oldPid
        this.bForced = bForced
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Process_t(), Structure.ByReference
    class ByValue : VREvent_Process_t(), Structure.ByValue
}

/** Used for a few events about overlays */
open class VREvent_Overlay_t : Structure {

    var overlayHandle = 0L

    constructor() : super() {
    }

    override fun getFieldOrder(): List<*> = Arrays.asList("overlayHandle")

    constructor(overlayHandle: Long) : super() {
        this.overlayHandle = overlayHandle
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Overlay_t(), Structure.ByReference
    class ByValue : VREvent_Overlay_t(), Structure.ByValue
}

/** Used for a few events about overlays */
open class VREvent_Status_t : Structure {

    var statusState = 0 // EVRState enum

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("statusState")

    constructor(statusState: Int) : super() {
        this.statusState = statusState
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Status_t(), Structure.ByReference
    class ByValue : VREvent_Status_t(), Structure.ByValue
}

/** Used for keyboard events **/
open class VREvent_Keyboard_t : Structure {

    var cNewInput = ""    // Up to 11 bytes of new input
    var uUserValue = 0L // Possible flags about the new input

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("cNewInput", "uUserValue")

    constructor(cNewInput: String, uUserValue: Long) : super() {
        this.cNewInput = cNewInput
        this.uUserValue = uUserValue
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Keyboard_t(), Structure.ByReference
    class ByValue : VREvent_Keyboard_t(), Structure.ByValue
}

open class VREvent_Ipd_t : Structure {

    var ipdMeters = 0f

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("ipdMeters")

    constructor(ipdMeters: Float) : super() {
        this.ipdMeters = ipdMeters
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Ipd_t(), Structure.ByReference
    class ByValue : VREvent_Ipd_t(), Structure.ByValue
}

class IVRSystem : Structure {

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("GetRecommendedRenderTargetSize", "GetProjectionMatrix", "GetProjectionRaw",
            "ComputeDistortion", "GetEyeToHeadTransform", "GetTimeSinceLastVsync", "GetD3D9AdapterIndex",
            "GetDXGIOutputInfo", "IsDisplayOnDesktop", "SetDisplayVisibility", "GetDeviceToAbsoluteTrackingPose",
            "ResetSeatedZeroPose", "GetSeatedZeroPoseToStandingAbsoluteTrackingPose",
            "GetRawZeroPoseToStandingAbsoluteTrackingPose", "GetSortedTrackedDeviceIndicesOfClass",
            "GetTrackedDeviceActivityLevel", "ApplyTransform", "GetTrackedDeviceIndexForControllerRole",
            "GetControllerRoleForTrackedDeviceIndex", "GetTrackedDeviceClass", "IsTrackedDeviceConnected",
            "GetBoolTrackedDeviceProperty", "GetFloatTrackedDeviceProperty", "GetInt32TrackedDeviceProperty",
            "GetUint64TrackedDeviceProperty", "GetMatrix34TrackedDeviceProperty", "GetStringTrackedDeviceProperty",
            "GetPropErrorNameFromEnum", "PollNextEvent", "PollNextEventWithPose", "GetEventTypeNameFromEnum",
            "GetHiddenAreaMesh", "GetControllerState", "GetControllerStateWithPose", "TriggerHapticPulse",
            "GetButtonIdNameFromEnum", "GetControllerAxisTypeNameFromEnum", "CaptureInputFocus",
            "ReleaseInputFocus", "IsInputFocusCapturedByAnotherProcess", "DriverDebugRequest",
            "PerformFirmwareUpdate", "AcknowledgeQuit_Exiting", "AcknowledgeQuit_UserPrompt",
            "PerformanceTestEnableCapture", "PerformanceTestReportFidelityLevelChange")

    constructor (peer: Pointer) : super(peer) {
        read()
    }
}

external fun VR_GetGenericInterface(pchInterfaceVersion: String, peError: IntBuffer): Pointer
external fun VR_IsInterfaceVersionValid(pchInterfaceVersion: String): Byte

external fun VR_InitInternal(peError: IntBuffer, eType: Int): Pointer
external fun VR_ShutdownInternal()

fun VR_Init(error: IntBuffer, applicationType: Int): IVRSystem {

    var vrSystem: IVRSystem? = null

    VR_InitInternal(error, applicationType)
//    val ctx = COpenVRContext()
//    ctx.clear()

//    if (error.get(0) === VRInitError_None) {
    if (error.get(0) === 0) {

//        if (VR_IsInterfaceVersionValid(IVRSystem_Version) !== 0.toByte()) {
        if (VR_IsInterfaceVersionValid("IVRSystem_012") !== 0.toByte()) {

            vrSystem = IVRSystem(VR_GetGenericInterface("IVRSystem_012", error))

        } else {

            VR_ShutdownInternal()
//            error.put(0, EVRInitError.VRInitError_Init_InterfaceNotFound)
            error.put(0, 105)
        }
    }
    return vrSystem!!
}

fun main(args: Array<String>) {

    val b = ByteBuffer.allocateDirect(java.lang.Integer.BYTES).asIntBuffer()
    val a = VR_Init(b, 1)
}