import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.NativeLibrary
import com.sun.jna.Callback
import com.sun.jna.ptr.FloatByReference
import com.sun.jna.ptr.IntByReference
import com.sun.jna.ptr.LongByReference
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.LongBuffer
import java.util.*

/**
 * Created by GBarbieri on 07.10.2016.
 */


object OpenVR {
    init {
        println("in")
        Native.register(NativeLibrary.getInstance("openvr_api"))
    }
}

/*struct VkDevice_T;
struct VkPhysicalDevice_T;
struct VkInstance_T;
struct VkQueue_T;*/

/** right-handed system
 *  +y is up
 *  +x is to the right
 *  -z is going away from you
 *  Distance unit is meters     */
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

/** Used to return the post-distortion UVs for each color channel.
 *  UVs range from 0 to 1 with 0,0 in the upper left corner of the source render target. The 0,0 to 1,1 range covers a single eye.  */
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
    ColorSpace_Auto(0), //  Assumes 'gamma' for 8-bit per component formats, otherwise 'linear'.  This mirrors the DXGI formats which have _SRGB variants.
    ColorSpace_Gamma(1), // Texture data can be displayed directly on the display without any conversion (a.k.a. display native format).
    ColorSpace_Linear(2) // Same as gamma but has been converted to a linear representation using DXGI's sRGB conversion algorithm.
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
    TrackedDeviceClass_Invalid(0), //           the ID was not valid.
    TrackedDeviceClass_HMD(1), //               Head-Mounted Displays
    TrackedDeviceClass_Controller(2), //        Tracked controllers
    TrackedDeviceClass_TrackingReference(4), // Camera and base stations that serve as tracking reference points

    TrackedDeviceClass_Count(5), //             This isn't a class that will ever be returned. It is used for allocating arrays and such

    TrackedDeviceClass_Other(1000)
}

/** Describes what specific role associated with a tracked device */
enum class ETrackedControllerRole(val i: Int) {
    TrackedControllerRole_Invalid(0), //    Invalid value for controller type
    TrackedControllerRole_LeftHand(1), //   Tracked device associated with the left hand
    TrackedControllerRole_RightHand(2) //   Tracked device associated with the right hand
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
    TrackingUniverseSeated(0), //               Poses are provided relative to the seated zero pose
    TrackingUniverseStanding(1), //             Poses are provided relative to the safe bounds configured by the user
    TrackingUniverseRawAndUncalibrated(2)    // Poses are provided in the coordinate system defined by the driver. You probably don't want this one.
}

/** Each entry in this enum represents a property that can be retrieved about a tracked device.
 *  Many fields are only valid for one ETrackedDeviceClass. */
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
    Prop_Axis0Type_Int32(3002), //          Return value is of type EVRControllerAxisType
    Prop_Axis1Type_Int32(3003), //          Return value is of type EVRControllerAxisType
    Prop_Axis2Type_Int32(3004), //          Return value is of type EVRControllerAxisType
    Prop_Axis3Type_Int32(3005), //          Return value is of type EVRControllerAxisType
    Prop_Axis4Type_Int32(3006), //          Return value is of type EVRControllerAxisType
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
    Prop_IconPathName_String(5000), //                      usually a directory named "icons"
    Prop_NamedIconPathDeviceOff_String(5001), //            PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    Prop_NamedIconPathDeviceSearching_String(5002), //      PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    Prop_NamedIconPathDeviceSearchingAlert_String(5003), // PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    Prop_NamedIconPathDeviceReady_String(5004), //          PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    Prop_NamedIconPathDeviceReadyAlert_String(5005), //     PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    Prop_NamedIconPathDeviceNotReady_String(5006), //       PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    Prop_NamedIconPathDeviceStandby_String(5007), //        PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    Prop_NamedIconPathDeviceAlertLow_String(5008), //       PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others

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

    VREvent_ButtonPress(200), //    data is controller
    VREvent_ButtonUnpress(201), //  data is controller
    VREvent_ButtonTouch(202), //    data is controller
    VREvent_ButtonUntouch(203), //  data is controller

    VREvent_MouseMove(300), //              data is mouse
    VREvent_MouseButtonDown(301), //        data is mouse
    VREvent_MouseButtonUp(302), //          data is mouse
    VREvent_FocusEnter(303), //             data is overlay
    VREvent_FocusLeave(304), //             data is overlay
    VREvent_Scroll(305), //                 data is mouse
    VREvent_TouchPadMove(306), //           data is mouse
    VREvent_OverlayFocusChanged(307), //    data is overlay), global event

    VREvent_InputFocusCaptured(400), //                         data is process DEPRECATED
    VREvent_InputFocusReleased(401), //                         data is process DEPRECATED
    VREvent_SceneFocusLost(402), //                             data is process
    VREvent_SceneFocusGained(403), //                           data is process
    VREvent_SceneApplicationChanged(404), //                    data is process - The App actually drawing the scene changed (usually to or from the compositor)
    VREvent_SceneFocusChanged(405), //                          data is process - New app got access to draw the scene
    VREvent_InputFocusChanged(406), //                          data is process
    VREvent_SceneApplicationSecondaryRenderingStarted(407), //  data is process

    VREvent_HideRenderModels(410), // Sent to the scene application to request hiding render models temporarily
    VREvent_ShowRenderModels(411), // Sent to the scene application to request restoring render model visibility

    VREvent_OverlayShown(500),
    VREvent_OverlayHidden(501),
    VREvent_DashboardActivated(502),
    VREvent_DashboardDeactivated(503),
    VREvent_DashboardThumbSelected(504), //     Sent to the overlay manager - data is overlay
    VREvent_DashboardRequested(505), //         Sent to the overlay manager - data is overlay
    VREvent_ResetDashboard(506), //             Send to the overlay manager
    VREvent_RenderToast(507), //                Send to the dashboard to render a toast - data is the notification ID
    VREvent_ImageLoaded(508), //                Sent to overlays when a SetOverlayRaw or SetOverlayFromFile call finishes loading
    VREvent_ShowKeyboard(509), //               Sent to keyboard renderer in the dashboard to invoke it
    VREvent_HideKeyboard(510), //               Sent to keyboard renderer in the dashboard to hide it
    VREvent_OverlayGamepadFocusGained(511), //  Sent to an overlay when IVROverlay::SetFocusOverlay is called on it
    VREvent_OverlayGamepadFocusLost(512), //    Send to an overlay when it previously had focus and IVROverlay::SetFocusOverlay is called on something else
    VREvent_OverlaySharedTextureChanged(513),
    VREvent_DashboardGuideButtonDown(514),
    VREvent_DashboardGuideButtonUp(515),
    VREvent_ScreenshotTriggered(516), //        Screenshot button combo was pressed), Dashboard should request a screenshot
    VREvent_ImageFailed(517), //                Sent to overlays when a SetOverlayRaw or SetOverlayfromFail fails to load
    VREvent_DashboardOverlayCreated(518),

    // Screenshot API
    VREvent_RequestScreenshot(520), //              Sent by vrclient application to compositor to take a screenshot
    VREvent_ScreenshotTaken(521), //                Sent by compositor to the application that the screenshot has been taken
    VREvent_ScreenshotFailed(522), //               Sent by compositor to the application that the screenshot failed to be taken
    VREvent_SubmitScreenshotToDashboard(523), //    Sent by compositor to the dashboard that a completed screenshot was submitted
    VREvent_ScreenshotProgressToDashboard(524), //  Sent by compositor to the dashboard that a completed screenshot was submitted

    VREvent_Notification_Shown(600),
    VREvent_Notification_Hidden(601),
    VREvent_Notification_BeginInteraction(602),
    VREvent_Notification_Destroyed(603),

    VREvent_Quit(700), //                   data is process
    VREvent_ProcessQuit(701), //            data is process
    VREvent_QuitAborted_UserPrompt(702), // data is process
    VREvent_QuitAcknowledged(703), //       data is process
    VREvent_DriverRequestedQuit(704), //    The driver has requested that SteamVR shut down

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
open class VREvent_Controller_t : VREvent_Data_t {
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
open class VREvent_Mouse_t : VREvent_Data_t {

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
open class VREvent_Scroll_t : VREvent_Data_t {

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
 *  is on the touchpad (or just released from it) */
open class VREvent_TouchPadMove_t : VREvent_Data_t {

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
open class VREvent_Notification_t : VREvent_Data_t {

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
open class VREvent_Process_t : VREvent_Data_t {

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
open class VREvent_Overlay_t : VREvent_Data_t {

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
open class VREvent_Status_t : VREvent_Data_t {

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
open class VREvent_Keyboard_t : VREvent_Data_t {

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

open class VREvent_Ipd_t : VREvent_Data_t {

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

open class VREvent_Chaperone_t : VREvent_Data_t {

    var m_nPreviousUniverse = 0L
    var m_nCurrentUniverse = 0L

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("m_nPreviousUniverse", "m_nCurrentUniverse")

    constructor(m_nPreviousUniverse: Long, m_nCurrentUniverse: Long) : super() {
        this.m_nPreviousUniverse = m_nPreviousUniverse
        this.m_nCurrentUniverse = m_nCurrentUniverse
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Chaperone_t(), Structure.ByReference
    class ByValue : VREvent_Chaperone_t(), Structure.ByValue
}

/** Not actually used for any events */
open class VREvent_Reserved_t : VREvent_Data_t {

    var reserved0 = 0L
    var reserved1 = 0L

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("reserved0", "reserved1")

    constructor(reserved0: Long, reserved1: Long) : super() {
        this.reserved0 = reserved0
        this.reserved1 = reserved1
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Reserved_t(), Structure.ByReference
    class ByValue : VREvent_Reserved_t(), Structure.ByValue
}

open class VREvent_PerformanceTest_t : VREvent_Data_t {

    var m_nFidelityLevel = 0

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("m_nFidelityLevel")

    constructor(m_nFidelityLevel: Int) : super() {
        this.m_nFidelityLevel = m_nFidelityLevel
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_PerformanceTest_t(), Structure.ByReference
    class ByValue : VREvent_PerformanceTest_t(), Structure.ByValue
}

open class VREvent_SeatedZeroPoseReset_t : VREvent_Data_t {

    var bResetBySystemMenu: Byte = 0

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("bResetBySystemMenu")

    constructor(bResetBySystemMenu: Byte) : super() {
        this.bResetBySystemMenu = bResetBySystemMenu
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_SeatedZeroPoseReset_t(), Structure.ByReference
    class ByValue : VREvent_SeatedZeroPoseReset_t(), Structure.ByValue
}

open class VREvent_Screenshot_t : VREvent_Data_t {

    var handle = 0
    var type = 0

    constructor() : super()

    constructor(handle: Int, type: Int) : super() {
        this.handle = handle
        this.type = type
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<*> = Arrays.asList("handle", "type")

    class ByReference : VREvent_Screenshot_t(), Structure.ByReference
    class ByValue : VREvent_Screenshot_t(), Structure.ByValue
}

open class VREvent_ScreenshotProgress_t : VREvent_Data_t {

    var progress = 0f

    constructor() : super()

    constructor(progress: Float) : super() {
        this.progress = progress
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<*> = Arrays.asList("progress")

    class ByReference : VREvent_ScreenshotProgress_t(), Structure.ByReference
    class ByValue : VREvent_ScreenshotProgress_t(), Structure.ByValue
}

open class VREvent_ApplicationLaunch_t : VREvent_Data_t {

    var pid = 0
    var unArgsHandle = 0

    constructor() : super()

    constructor(pid: Int, unArgsHandle: Int) : super() {
        this.pid = pid
        this.unArgsHandle = unArgsHandle
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<*> = Arrays.asList("pid", "unArgsHandle")

    class ByReference : VREvent_ApplicationLaunch_t(), Structure.ByReference
    class ByValue : VREvent_ApplicationLaunch_t(), Structure.ByValue
}

open class VREvent_EditingCameraSurface_t : VREvent_Data_t {

    var overlayHandle = 0L
    var nVisualMode = 0

    constructor() : super()

    constructor(overlayHandle: Long, nVisualMode: Int) : super() {
        this.overlayHandle = overlayHandle
        this.nVisualMode = nVisualMode
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<*> = Arrays.asList("overlayHandle", "nVisualMode")

    class ByReference : VREvent_EditingCameraSurface_t(), Structure.ByReference
    class ByValue : VREvent_EditingCameraSurface_t(), Structure.ByValue
}

/** If you change this you must manually update openvr_interop.cs.py */
abstract class VREvent_Data_t : Structure {
    constructor() : super()
    constructor(peer: Pointer) : super(peer)
}

/** An event posted by the server to all running applications */
open class VREvent_t : Structure {

    // EVREventType enum
    var eventType = 0
    var TrackedDeviceIndex_t = 0
    var eventAgeSeconds = 0f
    // event data must be the end of the struct as its size is variable
    var data: VREvent_Data_t? = null

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("eventType", "trackedDeviceIndex", "eventAgeSeconds", "data")

    constructor(eventType: Int, trackedDeviceIndex: Int, eventAgeSeconds: Float, data: VREvent_Data_t) : super() {
        this.eventType = eventType
        this.TrackedDeviceIndex_t = trackedDeviceIndex
        this.eventAgeSeconds = eventAgeSeconds
        this.data = data
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_t(), Structure.ByReference

    class ByValue : VREvent_t(), Structure.ByValue
}

/** The mesh to draw into the stencil (or depth) buffer to perform early stencil (or depth) kills of pixels that will never appear on the HMD.
 *  This mesh draws on all the pixels that will be hidden after distortion. *
 *  If the HMD does not provide a visible area mesh pVertexData will be NULL and unTriangleCount will be 0. */
open class HiddenAreaMesh_t : Structure {

    var pVertexData: HmdVector2_t.ByReference? = null
    var unTriangleCount = 0

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("pVertexData", "unTriangleCount")

    constructor(pVertexData: HmdVector2_t.ByReference, unTriangleCount: Int) : super() {
        this.pVertexData = pVertexData
        this.unTriangleCount = unTriangleCount
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HiddenAreaMesh_t(), Structure.ByReference
    class ByValue : HiddenAreaMesh_t(), Structure.ByValue
}

/** Identifies what kind of axis is on the controller at index n. Read this type with pVRSystem->Get( nControllerDeviceIndex, Prop_Axis0Type_Int32 + n );   */
enum class EVRControllerAxisType(val i: Int) {
    k_eControllerAxis_None(0),
    k_eControllerAxis_TrackPad(1),
    k_eControllerAxis_Joystick(2),
    k_eControllerAxis_Trigger(3) // Analog trigger data is in the X axis
}

/** contains information about one axis on the controller */
open class VRControllerAxis_t : Structure {

    // Ranges from -1.0 to 1.0 for joysticks and track pads. Ranges from 0.0 to 1.0 for triggers were 0 is fully released.
    var x = 0f
    // Ranges from -1.0 to 1.0 for joysticks and track pads. Is always 0.0 for triggers.
    var y = 0f

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("x", "y")

    constructor(x: Float, y: Float) : super() {
        this.x = x
        this.y = y
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VRControllerAxis_t(), Structure.ByReference
    class ByValue : VRControllerAxis_t(), Structure.ByValue
}

/** the number of axes in the controller state */
const val k_unControllerStateAxisCount = 5;

/** Holds all the state of a controller at one moment in time. */
open class VRControllerState_t : Structure {

    // If packet num matches that on your prior call, then the controller state hasn't been changed since your last call and there is no need to process it.
    var unPacketNum = 0

    // bit flags for each of the buttons. Use ButtonMaskFromId to turn an ID into a mask
    var ulButtonPressed = 0L
    var ulButtonTouched = 0L
    // Axis data for the controller's analog inputs
    var rAxis = arrayOf(VRControllerAxis_t(), VRControllerAxis_t(), VRControllerAxis_t(), VRControllerAxis_t(), VRControllerAxis_t())

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("unPacketNum", "ulButtonPressed", "ulButtonTouched", "rAxis")

    constructor(unPacketNum: Int, ulButtonPressed: Long, ulButtonTouched: Long, rAxis: Array<VRControllerAxis_t>) : super() {
        this.unPacketNum = unPacketNum
        this.ulButtonPressed = ulButtonPressed
        this.ulButtonTouched = ulButtonTouched
        if (rAxis.size != this.rAxis.size) throw IllegalArgumentException("Wrong array size !")
        this.rAxis = rAxis
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VRControllerState_t(), Structure.ByReference
    class ByValue : VRControllerState_t(), Structure.ByValue
}

typealias VRControllerState001_t = VRControllerState_t

/** determines how to provide output to the application of various event processing functions. */
enum class EVRControllerEventOutputType(val i: Int) {
    ControllerEventOutput_OSEvents(0),
    ControllerEventOutput_VREvents(1)
}

/** Collision Bounds Style */
enum class ECollisionBoundsStyle(val i: Int) {
    COLLISION_BOUNDS_STYLE_BEGINNER(0),
    COLLISION_BOUNDS_STYLE_INTERMEDIATE(1),
    COLLISION_BOUNDS_STYLE_SQUARES(2),
    COLLISION_BOUNDS_STYLE_ADVANCED(3),
    COLLISION_BOUNDS_STYLE_NONE(4),

    COLLISION_BOUNDS_STYLE_COUNT(5)
}

/** Allows the application to customize how the overlay appears in the compositor */
open class Compositor_OverlaySettings : Structure {

    var size = 0    // sizeof(Compositor_OverlaySettings)
    var curved: Byte = 0
    var antialias: Byte = 0
    var scale = 0f
    var distance = 0f
    var alpha = 0f
    var uOffset = 0f
    var vOffset = 0f
    var uScale = 0f
    var vScale = 0f
    var gridDivs = 0f
    var gridWidth = 0f
    var gridScale = 0f
    var transform: HmdMatrix44_t? = null

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("size", "curved", "antialias", "scale", "distance", "alpha", "uOffset", "vOffset", "uScale", "vScale",
            "gridDivs", "gridWidth", "gridScale", "transform")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : Compositor_OverlaySettings(), Structure.ByReference
    class ByValue : Compositor_OverlaySettings(), Structure.ByValue
}

/** used to refer to a single VR overlay */
typealias VROverlayHandle_t = Long

const val k_ulOverlayHandleInvalid = 0L

/** Errors that can occur around VR overlays */
enum class EVROverlayError(val i: Int) {
    VROverlayError_None(0),

    VROverlayError_UnknownOverlay(10),
    VROverlayError_InvalidHandle(11),
    VROverlayError_PermissionDenied(12),
    VROverlayError_OverlayLimitExceeded(13), // No more overlays could be created because the maximum number already exist
    VROverlayError_WrongVisibilityType(14),
    VROverlayError_KeyTooLong(15),
    VROverlayError_NameTooLong(16),
    VROverlayError_KeyInUse(17),
    VROverlayError_WrongTransformType(18),
    VROverlayError_InvalidTrackedDevice(19),
    VROverlayError_InvalidParameter(20),
    VROverlayError_ThumbnailCantBeDestroyed(21),
    VROverlayError_ArrayTooSmall(22),
    VROverlayError_RequestFailed(23),
    VROverlayError_InvalidTexture(24),
    VROverlayError_UnableToLoadFile(25),
    VROverlayError_KeyboardAlreadyInUse(26),
    VROverlayError_NoNeighbor(27)
}

/** enum values to pass in to VR_Init to identify whether the application will draw a 3D scene.     */
enum class EVRApplicationType(val i: Int) {
    VRApplication_Other(0), //          Some other kind of application that isn't covered by the other entries
    VRApplication_Scene(1), //          Application will submit 3D frames
    VRApplication_Overlay(2), //        Application only interacts with overlays
    VRApplication_Background(3), //     Application should not start SteamVR if it's not already running), and should not
    //                                  keep it running if everything else quits.
    VRApplication_Utility(4), //        Init should not try to load any drivers. The application needs access to utility
    //                                  interfaces (like IVRSettings and IVRApplications) but not hardware.
    VRApplication_VRMonitor(5), //      Reserved for vrmonitor
    VRApplication_SteamWatchdog(6), //  Reserved for Steam

    VRApplication_Max(7)
}

/** error codes for firmware */
enum class EVRFirmwareError(val i: Int) {
    VRFirmwareError_None(0),
    VRFirmwareError_Success(1),
    VRFirmwareError_Fail(2)
}

/** error codes for notifications */
enum class EVRNotificationError(val i: Int) {
    VRNotificationError_OK(0),
    VRNotificationError_InvalidNotificationId(100),
    VRNotificationError_NotificationQueueFull(101),
    VRNotificationError_InvalidOverlayHandle(102),
    VRNotificationError_SystemWithUserValueAlreadyExists(103)
}

/** error codes returned by Vr_Init */
// Please add adequate error description to https://developer.valvesoftware.com/w/index.php?title=Category:SteamVRHelp
enum class EVRInitError(val i: Int) {
    VRInitError_None(0),
    VRInitError_Unknown(1),

    VRInitError_Init_InstallationNotFound(100),
    VRInitError_Init_InstallationCorrupt(101),
    VRInitError_Init_VRClientDLLNotFound(102),
    VRInitError_Init_FileNotFound(103),
    VRInitError_Init_FactoryNotFound(104),
    VRInitError_Init_InterfaceNotFound(105),
    VRInitError_Init_InvalidInterface(106),
    VRInitError_Init_UserConfigDirectoryInvalid(107),
    VRInitError_Init_HmdNotFound(108),
    VRInitError_Init_NotInitialized(109),
    VRInitError_Init_PathRegistryNotFound(110),
    VRInitError_Init_NoConfigPath(111),
    VRInitError_Init_NoLogPath(112),
    VRInitError_Init_PathRegistryNotWritable(113),
    VRInitError_Init_AppInfoInitFailed(114),
    VRInitError_Init_Retry(115), //                 Used internally to cause retries to vrserver
    VRInitError_Init_InitCanceledByUser(116), //    The calling application should silently exit. The user canceled app startup
    VRInitError_Init_AnotherAppLaunching(117),
    VRInitError_Init_SettingsInitFailed(118),
    VRInitError_Init_ShuttingDown(119),
    VRInitError_Init_TooManyObjects(120),
    VRInitError_Init_NoServerForBackgroundApp(121),
    VRInitError_Init_NotSupportedWithCompositor(122),
    VRInitError_Init_NotAvailableToUtilityApps(123),
    VRInitError_Init_Internal(124),
    VRInitError_Init_HmdDriverIdIsNone(125),
    VRInitError_Init_HmdNotFoundPresenceFailed(126),
    VRInitError_Init_VRMonitorNotFound(127),
    VRInitError_Init_VRMonitorStartupFailed(128),
    VRInitError_Init_LowPowerWatchdogNotSupported(129),
    VRInitError_Init_InvalidApplicationType(130),
    VRInitError_Init_NotAvailableToWatchdogApps(131),
    VRInitError_Init_WatchdogDisabledInSettings(132),

    VRInitError_Driver_Failed(200),
    VRInitError_Driver_Unknown(201),
    VRInitError_Driver_HmdUnknown(202),
    VRInitError_Driver_NotLoaded(203),
    VRInitError_Driver_RuntimeOutOfDate(204),
    VRInitError_Driver_HmdInUse(205),
    VRInitError_Driver_NotCalibrated(206),
    VRInitError_Driver_CalibrationInvalid(207),
    VRInitError_Driver_HmdDisplayNotFound(208),
    VRInitError_Driver_TrackedDeviceInterfaceUnknown(209),
    // VRInitError_Driver_HmdDisplayNotFoundAfterFix(210), // not needed: here for historic reasons
    VRInitError_Driver_HmdDriverIdOutOfBounds(211),
    VRInitError_Driver_HmdDisplayMirrored(212),

    VRInitError_IPC_ServerInitFailed(300),
    VRInitError_IPC_ConnectFailed(301),
    VRInitError_IPC_SharedStateInitFailed(302),
    VRInitError_IPC_CompositorInitFailed(303),
    VRInitError_IPC_MutexInitFailed(304),
    VRInitError_IPC_Failed(305),
    VRInitError_IPC_CompositorConnectFailed(306),
    VRInitError_IPC_CompositorInvalidConnectResponse(307),
    VRInitError_IPC_ConnectFailedAfterMultipleAttempts(308),

    VRInitError_Compositor_Failed(400),
    VRInitError_Compositor_D3D11HardwareRequired(401),
    VRInitError_Compositor_FirmwareRequiresUpdate(402),
    VRInitError_Compositor_OverlayInitFailed(403),
    VRInitError_Compositor_ScreenshotsInitFailed(404),

    VRInitError_VendorSpecific_UnableToConnectToOculusRuntime(1000),

    VRInitError_VendorSpecific_HmdFound_CantOpenDevice(1101),
    VRInitError_VendorSpecific_HmdFound_UnableToRequestConfigStart(1102),
    VRInitError_VendorSpecific_HmdFound_NoStoredConfig(1103),
    VRInitError_VendorSpecific_HmdFound_ConfigTooBig(1104),
    VRInitError_VendorSpecific_HmdFound_ConfigTooSmall(1105),
    VRInitError_VendorSpecific_HmdFound_UnableToInitZLib(1106),
    VRInitError_VendorSpecific_HmdFound_CantReadFirmwareVersion(1107),
    VRInitError_VendorSpecific_HmdFound_UnableToSendUserDataStart(1108),
    VRInitError_VendorSpecific_HmdFound_UnableToGetUserDataStart(1109),
    VRInitError_VendorSpecific_HmdFound_UnableToGetUserDataNext(1110),
    VRInitError_VendorSpecific_HmdFound_UserDataAddressRange(1111),
    VRInitError_VendorSpecific_HmdFound_UserDataError(1112),
    VRInitError_VendorSpecific_HmdFound_ConfigFailedSanityCheck(1113),

    VRInitError_Steam_SteamInstallationNotFound(2000),
}

enum class EVRScreenshotType(val i: Int) {
    VRScreenshotType_None(0),
    VRScreenshotType_Mono(1), // left eye only
    VRScreenshotType_Stereo(2),
    VRScreenshotType_Cubemap(3),
    VRScreenshotType_MonoPanorama(4),
    VRScreenshotType_StereoPanorama(5)
}

enum class EVRScreenshotPropertyFilenames(val i: Int) {
    VRScreenshotPropertyFilenames_Preview(0),
    VRScreenshotPropertyFilenames_VR(1),
}

enum class EVRTrackedCameraError(val i: Int) {
    VRTrackedCameraError_None(0),
    VRTrackedCameraError_OperationFailed(100),
    VRTrackedCameraError_InvalidHandle(101),
    VRTrackedCameraError_InvalidFrameHeaderVersion(102),
    VRTrackedCameraError_OutOfHandles(103),
    VRTrackedCameraError_IPCFailure(104),
    VRTrackedCameraError_NotSupportedForThisDevice(105),
    VRTrackedCameraError_SharedMemoryFailure(106),
    VRTrackedCameraError_FrameBufferingFailure(107),
    VRTrackedCameraError_StreamSetupFailure(108),
    VRTrackedCameraError_InvalidGLTextureId(109),
    VRTrackedCameraError_InvalidSharedTextureHandle(110),
    VRTrackedCameraError_FailedToGetGLTextureId(111),
    VRTrackedCameraError_SharedTextureFailure(112),
    VRTrackedCameraError_NoFrameAvailable(113),
    VRTrackedCameraError_InvalidArgument(114),
    VRTrackedCameraError_InvalidFrameBufferSize(115),
}

enum class EVRTrackedCameraFrameType(val i: Int) {
    VRTrackedCameraFrameType_Distorted(0), //           This is the camera video frame size in pixels), still distorted.
    VRTrackedCameraFrameType_Undistorted(1), //         In pixels), an undistorted inscribed rectangle region without invalid regions. This size is subject to changes shortly.
    VRTrackedCameraFrameType_MaximumUndistorted(2), //  In pixels), maximum undistorted with invalid regions. Non zero alpha component identifies valid regions.
    MAX_CAMERA_FRAME_TYPES(3)
}

typealias TrackedCameraHandle_t = Long
const val INVALID_TRACKED_CAMERA_HANDLE = 0L

open class CameraVideoStreamFrameHeader_t : Structure {

    var eFrameType = 0

    var nWidth = 0
    var nHeight = 0
    var nBytesPerPixel = 0

    var nFrameSequence = 0

    var standingTrackedDevicePose = TrackedDevicePose_t()

    constructor() : super()

    constructor(eFrameType: Int, nWidth: Int, nHeight: Int, nBytesPerPixel: Int, nFrameSequence: Int, standingTrackedDevicePose: TrackedDevicePose_t) : super() {

        this.eFrameType = eFrameType
        this.nWidth = nWidth
        this.nHeight = nHeight
        this.nBytesPerPixel = nBytesPerPixel
        this.nFrameSequence = nFrameSequence
        this.standingTrackedDevicePose = standingTrackedDevicePose
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<*> = Arrays.asList("eFrameType", "nWidth", "nHeight", "nBytesPerPixel", "nFrameSequence", "standingTrackedDevicePose")

    class ByReference : CameraVideoStreamFrameHeader_t(), Structure.ByReference
    class ByValue : CameraVideoStreamFrameHeader_t(), Structure.ByValue
}

// Screenshot types
typealias ScreenshotHandle_t = Int
const val k_unScreenshotHandleInvalid = 0

open class IVRSystem : Structure {

    // ------------------------------------
    // Display Methods
    // ------------------------------------

    /** Suggested size for the intermediate render target that the distortion pulls from. */
    @JvmField var GetRecommendedRenderTargetSize: IVRSystem.GetRecommendedRenderTargetSize_callback? = null

    interface GetRecommendedRenderTargetSize_callback : Callback {
        fun apply(pnWidth: IntByReference, pnHeight: IntByReference)
    }

    /** The projection matrix for the specified eye */
    @JvmField var GetProjectionMatrix: IVRSystem.GetProjectionMatrix_callback? = null

    interface GetProjectionMatrix_callback : Callback {
        fun apply(eEye: Int, fNearZ: Float, fFarZ: Float, eProjType: Int): HmdMatrix44_t.ByValue
    }

    /** The components necessary to build your own projection matrix in case your application is doing something fancy like infinite Z  */
    @JvmField var GetProjectionRaw: IVRSystem.GetProjectionRaw_callback? = null

    interface GetProjectionRaw_callback : Callback {
        fun apply(eEye: Int, pfLeft: FloatByReference, pfRight: FloatByReference, pfTop: FloatByReference, pfBottom: FloatByReference)
    }

    /** Returns the result of the distortion function for the specified eye and input UVs.
     *  UVs go from 0,0 in the upper left of that eye's viewport and 1,1 in the lower right of that eye's viewport.
     *  Values may be NAN to indicate an error has occurred.     */
    @JvmField var ComputeDistortion: IVRSystem.ComputeDistortion_callback? = null

    interface ComputeDistortion_callback : Callback {
        fun apply(eEye: Int, fU: Float, fV: Float)
    }

    /** Returns the transform from eye space to the head space. Eye space is the per-eye flavor of head space that provides stereo disparity.
     *  Instead of Model * View * Projection the sequence is Model * View * Eye^-1 * Projection.
     *  Normally View and Eye^-1 will be multiplied together and treated as View in your application.   */
    @JvmField var GetEyeToHeadTransform: IVRSystem.GetEyeToHeadTransform_callback? = null

    interface GetEyeToHeadTransform_callback : Callback {
        fun apply(eEye: Int): HmdMatrix34_t.ByValue
    }

    /** Returns the number of elapsed seconds since the last recorded vsync event. This will come from a vsync timer event in the timer if possible or from the
     *  application-reported time if that is not available.
     *  If no vsync times are available the function will return zero for vsync time and frame counter and return false from the method.    */
    @JvmField var GetTimeSinceLastVsync: IVRSystem.GetTimeSinceLastVsync_callback? = null

    interface GetTimeSinceLastVsync_callback : Callback {
        fun apply(pfSecondsSinceLastVsync: FloatByReference, pulFrameCounter: LongByReference): Byte
    }

    /** [D3D9 Only]
     * Returns the adapter index that the user should pass into CreateDevice to set up D3D9 in such a way that it can go full screen exclusive on the HMD.
     * Returns -1 if there was an error.     */
    @JvmField var GetD3D9AdapterIndex: IVRSystem.GetD3D9AdapterIndex_callback? = null

    interface GetD3D9AdapterIndex_callback : Callback {
        fun apply(): Int
    }

    /** [D3D10/11 Only]
     * Returns the adapter index that the user should pass into EnumAdapters to create the device and swap chain in DX10 and DX11. If an error occurs the index
     * will be set to -1. The index will also be -1 if the headset is in direct mode on the driver side instead of using the compositor's builtin direct mode
     * support.      */
    @JvmField var GetDXGIOutputInfo: IVRSystem.GetDXGIOutputInfo_callback? = null

    interface GetDXGIOutputInfo_callback : Callback {
        fun apply(pnAdapterIndex: IntByReference)
    }


    // ------------------------------------
    // Display Mode methods
    // ------------------------------------

    /** Use to determine if the headset display is part of the desktop (i.e. extended) or hidden (i.e. direct mode). */
    @JvmField var IsDisplayOnDesktop: IVRSystem.IsDisplayOnDesktop_callback? = null

    interface IsDisplayOnDesktop_callback : Callback {
        fun apply(): Byte
    }

    /** Set the display visibility (true = extended, false = direct mode).  Return value of true indicates that the change was successful. */
    @JvmField var SetDisplayVisibility: IVRSystem.SetDisplayVisibility_callback? = null

    interface SetDisplayVisibility_callback : Callback {
        fun apply(bIsVisibleOnDesktop: Byte): Byte
    }


    // ------------------------------------
    // Tracking Methods
    // ------------------------------------

    /** The pose that the tracker thinks that the HMD will be in at the specified number of seconds into the future. Pass 0 to get the state at the instant the
     *  method is called. Most of the time the application should calculate the time until the photons will be emitted from the display and pass that time into
     *  the method.
     *
     *  This is roughly analogous to the inverse of the view matrix in most applications, though many games will need to do some additional rotation or
     *  translation on top of the rotation and translation provided by the head pose.
     *
     *  For devices where bPoseIsValid is true the application can use the pose to position the device in question. The provided array can be any size up to
     *  k_unMaxTrackedDeviceCount.
     *
     *  Seated experiences should call this method with TrackingUniverseSeated and receive poses relative to the seated zero pose. Standing experiences should
     *  call this method with TrackingUniverseStanding and receive poses relative to the Chaperone Play Area. TrackingUniverseRawAndUncalibrated should probably
     *  not be used unless the application is the Chaperone calibration tool itself, but will provide poses relative to the hardware-specific coordinate system
     *  in the driver.     */
    @JvmField var GetDeviceToAbsoluteTrackingPose: IVRSystem.GetDeviceToAbsoluteTrackingPose_callback? = null

    interface GetDeviceToAbsoluteTrackingPose_callback : Callback {
        fun apply(eOrigin: Int, fPredictedSecondsToPhotonsFromNow: Float, pTrackedDevicePoseArray: TrackedDevicePose_t .ByReference, unTrackedDevicePoseArrayCount: Int)
    }

    /** Sets the zero pose for the seated tracker coordinate system to the current position and yaw of the HMD. After ResetSeatedZeroPose all
     *  GetDeviceToAbsoluteTrackingPose calls that pass TrackingUniverseSeated as the origin will be relative to this new zero pose. The new zero coordinate
     *  system will not change the fact that the Y axis is up in the real world, so the next pose returned from GetDeviceToAbsoluteTrackingPose after a call to
     *  ResetSeatedZeroPose may not be exactly an identity matrix.
     *
     *  NOTE: This function overrides the user's previously saved seated zero pose and should only be called as the result of a user action.
     *  Users are also able to set their seated zero pose via the OpenVR Dashboard.     **/
    @JvmField var ResetSeatedZeroPose: IVRSystem.ResetSeatedZeroPose_callback? = null

    interface ResetSeatedZeroPose_callback : Callback {
        fun apply()
    }

    /** Returns the transform from the seated zero pose to the standing absolute tracking system. This allows applications to represent the seated origin to
     *  used or transform object positions from one coordinate system to the other.
     *
     *  The seated origin may or may not be inside the Play Area or Collision Bounds returned by IVRChaperone. Its position depends on what the user has set
     *  from the Dashboard settings and previous calls to ResetSeatedZeroPose. */
    @JvmField var GetSeatedZeroPoseToStandingAbsoluteTrackingPose: IVRSystem.GetSeatedZeroPoseToStandingAbsoluteTrackingPose_callback? = null

    interface GetSeatedZeroPoseToStandingAbsoluteTrackingPose_callback : Callback {
        fun apply(): HmdMatrix34_t.ByValue
    }

    /** Returns the transform from the tracking origin to the standing absolute tracking system. This allows applications to convert from raw tracking space to
     *  the calibrated standing coordinate system. */
    @JvmField var GetRawZeroPoseToStandingAbsoluteTrackingPose: IVRSystem.GetRawZeroPoseToStandingAbsoluteTrackingPose_callback? = null

    interface GetRawZeroPoseToStandingAbsoluteTrackingPose_callback : Callback {
        fun apply(): HmdMatrix34_t.ByValue
    }

    /** Get a sorted array of device indices of a given class of tracked devices (e.g. controllers).  Devices are sorted right to left relative to the specified
     *  tracked device (default: hmd -- pass in -1 for absolute tracking space).  Returns the number of devices in the list, or the size of the array needed if
     *  not large enough. */
    @JvmField var GetSortedTrackedDeviceIndicesOfClass: IVRSystem.GetSortedTrackedDeviceIndicesOfClass_callback? = null

    interface GetSortedTrackedDeviceIndicesOfClass_callback : Callback {
        fun apply(eTrackedDeviceClass: Int, punTrackedDeviceIndexArray: IntByReference, unTrackedDeviceIndexArrayCount: Int,
                  unRelativeToTrackedDeviceIndex: TrackedDeviceIndex_t = k_unTrackedDeviceIndex_Hmd): Int
    }

    /** Returns the level of activity on the device. */
    @JvmField var GetTrackedDeviceActivityLevel: IVRSystem.GetTrackedDeviceActivityLevel_callback? = null

    interface GetTrackedDeviceActivityLevel_callback : Callback {
        fun apply(unDeviceId: TrackedDeviceIndex_t): Int
    }

    /** Convenience utility to apply the specified transform to the specified pose.
     *  This properly transforms all pose components, including velocity and angular velocity     */
    @JvmField var ApplyTransform: IVRSystem.ApplyTransform_callback? = null

    interface ApplyTransform_callback : Callback {
        fun apply(pOutputPose: TrackedDevicePose_t.ByReference, pTrackedDevicePose: TrackedDevicePose_t.ByReference, pTransform: HmdMatrix34_t .ByReference)
    }

    /** Returns the device index associated with a specific role, for example the left hand or the right hand. */
    @JvmField var GetTrackedDeviceIndexForControllerRole: IVRSystem.GetTrackedDeviceIndexForControllerRole_callback? = null

    interface GetTrackedDeviceIndexForControllerRole_callback : Callback {
        fun apply(unDeviceType: Int): TrackedDeviceIndex_t
    }

    /** Returns the controller type associated with a device index. */
    @JvmField var GetControllerRoleForTrackedDeviceIndex: IVRSystem.GetControllerRoleForTrackedDeviceIndex_callback? = null

    interface GetControllerRoleForTrackedDeviceIndex_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t): Int
    }


    // ------------------------------------
    // Property methods
    // ------------------------------------

    /** Returns the device class of a tracked device. If there has not been a device connected in this slot since the application started this function will
     *  return TrackedDevice_Invalid. For previous detected devices the function will return the previously observed device class.
     *
     * To determine which devices exist on the system, just loop from 0 to k_unMaxTrackedDeviceCount and check the device class. Every device with something
     * other than TrackedDevice_Invalid is associated with an actual tracked device. */
    @JvmField var GetTrackedDeviceClass: IVRSystem.GetTrackedDeviceClass_callback? = null

    interface GetTrackedDeviceClass_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t): Int
    }

    /** Returns true if there is a device connected in this slot. */
    @JvmField var IsTrackedDeviceConnected: IVRSystem.IsTrackedDeviceConnected_callback? = null

    interface IsTrackedDeviceConnected_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t): Byte
    }

    /** Returns a bool property. If the device index is not valid or the property is not a bool type this function will return false. */
    @JvmField var GetBoolTrackedDeviceProperty: IVRSystem.GetBoolTrackedDeviceProperty_callback? = null

    interface GetBoolTrackedDeviceProperty_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: IntByReference = IntByReference(0)): Byte
    }

    /** Returns a float property. If the device index is not valid or the property is not a float type this function will return 0. */
    @JvmField var GetFloatTrackedDeviceProperty: IVRSystem.GetFloatTrackedDeviceProperty_callback? = null

    interface GetFloatTrackedDeviceProperty_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: IntByReference = IntByReference(0)): Float
    }

    /** Returns an int property. If the device index is not valid or the property is not a int type this function will return 0. */
    @JvmField var GetInt32TrackedDeviceProperty: IVRSystem.GetInt32TrackedDeviceProperty_callback? = null

    interface GetInt32TrackedDeviceProperty_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: IntByReference = IntByReference(0)): Int
    }

    /** Returns a uint64 property. If the device index is not valid or the property is not a uint64 type this function will return 0. */
    @JvmField var GetUint64TrackedDeviceProperty: IVRSystem.GetUint64TrackedDeviceProperty_callback? = null

    interface GetUint64TrackedDeviceProperty_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: IntByReference = IntByReference(0)): Long
    }

    /** Returns a matrix property. If the device index is not valid or the property is not a matrix type, this function will return identity. */
    @JvmField var GetMatrix34TrackedDeviceProperty: IVRSystem.GetMatrix34TrackedDeviceProperty_callback? = null

    interface GetMatrix34TrackedDeviceProperty_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: IntByReference = IntByReference(0)): HmdMatrix34_t.ByValue
    }

    /** Returns a string property. If the device index is not valid or the property is not a string type this function will return 0. Otherwise it returns the
     *  length of the number of bytes necessary to hold this string including the trailing null. Strings will generally fit in buffers of k_unTrackingStringSize
     *  characters. */
    @JvmField var GetStringTrackedDeviceProperty: IVRSystem.GetStringTrackedDeviceProperty_callback? = null

    interface GetStringTrackedDeviceProperty_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pchValue: String, unBufferSize: Int, pError: IntByReference = IntByReference(0)): Int
    }

    /** returns a string that corresponds with the specified property error. The string will be the name of the error enum value for all valid error codes */
    @JvmField var GetPropErrorNameFromEnum: IVRSystem.GetPropErrorNameFromEnum_callback? = null

    interface GetPropErrorNameFromEnum_callback : Callback {
        fun apply(error: Int): String
    }


    // ------------------------------------
    // Event methods
    // ------------------------------------

    /** Returns true and fills the event with the next event on the queue if there is one. If there are no events this method returns false. uncbVREvent should
     *  be the size in bytes of the VREvent_t struct */
    @JvmField var PollNextEvent: IVRSystem.PollNextEvent_callback? = null

    interface PollNextEvent_callback : Callback {
        fun apply(pEvent: VREvent_t.ByReference, uncbVREvent: Int): Byte
    }

    /** Returns true and fills the event with the next event on the queue if there is one. If there are no events this method returns false. Fills in the pose
     *  of the associated tracked device in the provided pose struct.
     *  This pose will always be older than the call to this function and should not be used to render the device.
     *  uncbVREvent should be the size in bytes of the VREvent_t struct */
    @JvmField var PollNextEventWithPose: IVRSystem.PollNextEventWithPose_callback? = null

    interface PollNextEventWithPose_callback : Callback {
        fun apply(eOrigin: Int, pEvent: VREvent_t .ByReference, uncbVREvent: Int, pTrackedDevicePose: TrackedDevicePose_t.ByReference): Byte
    }

    /** returns the name of an EVREvent enum value */
    @JvmField var GetEventTypeNameFromEnum: IVRSystem.GetEventTypeNameFromEnum_callback? = null

    interface GetEventTypeNameFromEnum_callback : Callback {
        fun apply(eType: Int): String
    }


    // ------------------------------------
    // Rendering helper methods
    // ------------------------------------

    /** Returns the stencil mesh information for the current HMD. If this HMD does not have a stencil mesh the vertex data and count will be NULL and 0
     *  respectively. This mesh is meant to be rendered into the stencil buffer (or into the depth buffer setting nearz) before rendering each eye's view.
     *  The pixels covered by this mesh will never be seen by the user after the lens distortion is applied and based on visibility to the panels.
     *  This will improve perf by letting the GPU early-reject pixels the user will never see before running the pixel shader.
     *  NOTE: Render this mesh with backface culling disabled since the winding order of the vertices can be different per-HMD or per-eye.     */
    @JvmField var GetHiddenAreaMesh: IVRSystem.GetHiddenAreaMesh_callback? = null

    interface GetHiddenAreaMesh_callback : Callback {
        fun apply(eEye: Int): HiddenAreaMesh_t.ByValue
    }


    // ------------------------------------
    // Controller methods
    // ------------------------------------

    /** Fills the supplied struct with the current state of the controller. Returns false if the controller index is invalid. */
    @JvmField var GetControllerState: IVRSystem.GetControllerState_callback? = null

    interface GetControllerState_callback : Callback {
        fun apply(unControllerDeviceIndex: TrackedDeviceIndex_t, pControllerState: VRControllerState_t.ByReference): Byte
    }

    /** Fills the supplied struct with the current state of the controller and the provided pose with the pose of the controller when the controller state was
     *  updated most recently. Use this form if you need a precise controller pose as input to your application when the user presses or releases a button. */
    @JvmField var GetControllerStateWithPose: IVRSystem.GetControllerStateWithPose_callback? = null

    interface GetControllerStateWithPose_callback : Callback {
        fun apply(eOrigin: Int, unControllerDeviceIndex: TrackedDeviceIndex_t, pControllerState: VRControllerState_t .ByReference,
                  pTrackedDevicePose: TrackedDevicePose_t .ByReference): Byte
    }

    /** Trigger a single haptic pulse on a controller. After this call the application may not trigger another haptic pulse on this controller and axis
     *  combination for 5ms. */
    @JvmField var TriggerHapticPulse: IVRSystem.TriggerHapticPulse_callback? = null

    interface TriggerHapticPulse_callback : Callback {
        fun apply(unControllerDeviceIndex: TrackedDeviceIndex_t, unAxisId: Int, usDurationMicroSec: Short)
    }

    /** returns the name of an EVRButtonId enum value */
    @JvmField var GetButtonIdNameFromEnum: IVRSystem.GetButtonIdNameFromEnum_callback? = null

    interface GetButtonIdNameFromEnum_callback : Callback {
        fun apply(eButtonId: Int): String
    }

    /** returns the name of an EVRControllerAxisType enum value */
    @JvmField var GetControllerAxisTypeNameFromEnum: IVRSystem.GetControllerAxisTypeNameFromEnum_callback? = null

    interface GetControllerAxisTypeNameFromEnum_callback : Callback {
        fun apply(eAxisType: Int): String
    }

    /** Tells OpenVR that this process wants exclusive access to controller button states and button events. Other apps will be notified that they have lost
     *  input focus with a VREvent_InputFocusCaptured event. Returns false if input focus could not be captured for some reason. */
    @JvmField var CaptureInputFocus: IVRSystem.CaptureInputFocus_callback? = null

    interface CaptureInputFocus_callback : Callback {
        fun apply(): Byte
    }

    /** Tells OpenVR that this process no longer wants exclusive access to button states and button events. Other apps will be notified that input focus has
     *  been released with a VREvent_InputFocusReleased event. */
    @JvmField var ReleaseInputFocus: IVRSystem.ReleaseInputFocus_callback? = null

    interface ReleaseInputFocus_callback : Callback {
        fun apply()
    }

    /** Returns true if input focus is captured by another process. */
    @JvmField var IsInputFocusCapturedByAnotherProcess: IVRSystem.IsInputFocusCapturedByAnotherProcess_callback? = null

    interface IsInputFocusCapturedByAnotherProcess_callback : Callback {
        fun apply(): Byte
    }


    // ------------------------------------
    // Debug Methods
    // ------------------------------------

    /** Sends a request to the driver for the specified device and returns the response. The maximum response size is 32k, but this method can be called with
     *  a smaller buffer. If the response exceeds the size of the buffer, it is truncated.
     *  The size of the response including its terminating null is returned. */
    @JvmField var DriverDebugRequest: IVRSystem.DriverDebugRequest_callback? = null

    interface DriverDebugRequest_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t, pchRequest: String, pchResponseBuffer: String, unResponseBufferSize: Int): Int
    }


    // ------------------------------------
    // Firmware methods
    // ------------------------------------

    /** Performs the actual firmware update if applicable.
     *  The following events will be sent, if VRFirmwareError_None was returned: VREvent_FirmwareUpdateStarted, VREvent_FirmwareUpdateFinished
     *  Use the properties Prop_Firmware_UpdateAvailable_Bool, Prop_Firmware_ManualUpdate_Bool, and Prop_Firmware_ManualUpdateURL_String to figure our whether
     *  a firmware update is available, and to figure out whether its a manual update
     *  Prop_Firmware_ManualUpdateURL_String should point to an URL describing the manual update process */
    @JvmField var PerformFirmwareUpdate: IVRSystem.PerformFirmwareUpdate_callback? = null

    interface PerformFirmwareUpdate_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t): Int
    }


    // ------------------------------------
    // Application life cycle methods
    // ------------------------------------

    /** Call this to acknowledge to the system that VREvent_Quit has been received and that the process is exiting.
     *  This extends the timeout until the process is killed. */
    @JvmField var AcknowledgeQuit_Exiting: IVRSystem.AcknowledgeQuit_Exiting_callback? = null

    interface AcknowledgeQuit_Exiting_callback : Callback {
        fun apply()
    }

    /** Call this to tell the system that the user is being prompted to save data. This halts the timeout and dismisses the dashboard (if it was up).
     *  Applications should be sure to actually prompt the user to save and then exit afterward, otherwise the user will be left in a confusing state. */
    @JvmField var AcknowledgeQuit_UserPrompt: IVRSystem.AcknowledgeQuit_UserPrompt_callback? = null

    interface AcknowledgeQuit_UserPrompt_callback : Callback {
        fun apply()
    }

    constructor() : super()

    override fun getFieldOrder(): List<*> = Arrays.asList("GetRecommendedRenderTargetSize", "GetProjectionMatrix", "GetProjectionRaw", "ComputeDistortion",
            "GetEyeToHeadTransform", "GetTimeSinceLastVsync", "GetD3D9AdapterIndex", "GetDXGIOutputInfo", "IsDisplayOnDesktop", "SetDisplayVisibility",
            "GetDeviceToAbsoluteTrackingPose", "ResetSeatedZeroPose", "GetSeatedZeroPoseToStandingAbsoluteTrackingPose",
            "GetRawZeroPoseToStandingAbsoluteTrackingPose", "GetSortedTrackedDeviceIndicesOfClass", "GetTrackedDeviceActivityLevel", "ApplyTransform",
            "GetTrackedDeviceIndexForControllerRole", "GetControllerRoleForTrackedDeviceIndex", "GetTrackedDeviceClass", "IsTrackedDeviceConnected",
            "GetBoolTrackedDeviceProperty", "GetFloatTrackedDeviceProperty", "GetInt32TrackedDeviceProperty", "GetUint64TrackedDeviceProperty",
            "GetMatrix34TrackedDeviceProperty", "GetStringTrackedDeviceProperty", "GetPropErrorNameFromEnum", "PollNextEvent", "PollNextEventWithPose",
            "GetEventTypeNameFromEnum", "GetHiddenAreaMesh", "GetControllerState", "GetControllerStateWithPose", "TriggerHapticPulse", "GetButtonIdNameFromEnum",
            "GetControllerAxisTypeNameFromEnum", "CaptureInputFocus", "ReleaseInputFocus", "IsInputFocusCapturedByAnotherProcess", "DriverDebugRequest",
            "PerformFirmwareUpdate", "AcknowledgeQuit_Exiting", "AcknowledgeQuit_UserPrompt")

    constructor (peer: Pointer) : super(peer) {
        read()
    }

    override fun read() {
        val old = pointer
        val m = autoAllocate(size())
        // horribly inefficient, but it'll do
        m.write(0, old.getByteArray(0, size()), 0, size())
        useMemory(m)
        // Zero out the problematic callbacks
        val problematicFields = mapOf("GetEyeToHeadTransform" to 32, "GetSeatedZeroPoseToStandingAbsoluteTrackingPose" to 96)
//                "GetDXGIOutputInfo" to 56, "GetTrackedDeviceActivityLevel" to 120)
        problematicFields.forEach { field, offset -> m.setPointer(field.toLong(), null) }
        super.read();
        useMemory(old);
    }
}

const val IVRSystem_Version = "IVRSystem_012"


external fun VR_GetGenericInterface(pchInterfaceVersion: String, peError: IntBuffer): Pointer
external fun VR_IsInterfaceVersionValid(pchInterfaceVersion: String): Byte

private external fun VR_InitInternal(peError: IntBuffer, eType: Int): Pointer
private external fun VR_ShutdownInternal()

fun VR_Init(error: IntBuffer, applicationType: Int): IVRSystem {

    var vrSystem: IVRSystem? = null

    VR_InitInternal(error, applicationType)
//    val ctx = COpenVRContext()
//    ctx.clear()

    if (error.get(0) == EVRInitError.VRInitError_None.i) {

        if (VR_IsInterfaceVersionValid(IVRSystem_Version) !== 0.toByte()) {

            vrSystem = IVRSystem(VR_GetGenericInterface("IVRSystem_012", error))

        } else {

            VR_ShutdownInternal()
            error.put(0, EVRInitError.VRInitError_Init_InterfaceNotFound.i)
        }
    }
    return vrSystem!!
}

fun main(args: Array<String>) {

    Native.register(NativeLibrary.getInstance("openvr_api"))
    val error = ByteBuffer.allocateDirect(java.lang.Integer.BYTES).asIntBuffer()
    val c = ByteBuffer.allocateDirect(java.lang.Integer.BYTES).asIntBuffer()
    val a = VR_Init(error, EVRApplicationType.VRApplication_Scene.i)
    println(error.get(0))
    val w = IntByReference(0)
    val h = IntByReference(0)
    a.read()
    a.GetRecommendedRenderTargetSize!!.apply(w, h)
    println("w: $w, h: $h")
}