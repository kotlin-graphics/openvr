package main

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.NativeLibrary
import com.sun.jna.Callback
import com.sun.jna.ptr.ByteByReference
import com.sun.jna.ptr.IntByReference
import com.sun.jna.ptr.PointerByReference
import com.sun.jna.ptr.LongByReference
import com.sun.jna.ptr.FloatByReference
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.LongBuffer
import java.util.*

/**
 * Created by GBarbieri on 07.10.2016.
 */

/*struct VkDevice_T;
struct VkPhysicalDevice_T;
struct VkInstance_T;
struct VkQueue_T;*/

typealias glSharedTextureHandle_t = Pointer
typealias glSharedTextureHandle_t_ByReference = PointerByReference
typealias glInt_t = Int
typealias glUInt_t = Int
typealias glUInt_t_ByReference = IntByReference

/** right-handed system
 *  +y is up
 *  +x is to the right
 *  -z is going away from you
 *  Distance unit is meters     */
open class HmdMatrix34_t : Structure {

    var m = FloatArray(3 * 4)

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("m")

    constructor(m: FloatArray) {
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

    var m = FloatArray(4 * 4)

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("m")

    constructor(m: FloatArray) {
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

    var v = FloatArray(3)

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("i")

    constructor(v: FloatArray) {
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

    var v = FloatArray(4)

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("i")

    constructor(v: FloatArray) {
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

    var v = DoubleArray(3)

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("i")

    constructor(v: DoubleArray) {
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

    var v = FloatArray(2)

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("i")

    constructor(v: FloatArray) {
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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("w", "x", "y", "z")

    constructor(w: Double, x: Double, y: Double, z: Double) {
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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("r", "g", "b", "a")

    constructor(r: Float, g: Float, b: Float, a: Float) {
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

    var vCorners = arrayOf(HmdVector3_t(), HmdVector3_t(), HmdVector3_t(), HmdVector3_t())

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("vCorners")

    constructor(vCorners: Array<HmdVector3_t>) {
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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("vTopLeft", "vBottomRight")

    constructor(vTopLeft: HmdVector2_t, vBottomRight: HmdVector2_t) {
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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("rfRed", "rfGreen", "rfBlue")

    constructor(rfRed: FloatArray, rfGreen: FloatArray, rfBlue: FloatArray) {
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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("handle", "eType", "eColorSpace")

    constructor(handle: Int, eType: Int, eColorSpace: Int) {
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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("mDeviceToAbsoluteTracking", "vVelocity", "vAngularVelocity",
            "eTrackingResult", "bPoseIsValid", "bDeviceIsConnected")

    /**
     * @param mDeviceToAbsoluteTracking C type : main.HmdMatrix34_t<br></br>
     * *
     * @param vVelocity C type : main.HmdVector3_t<br></br>
     * *
     * @param vAngularVelocity C type : main.HmdVector3_t<br></br>
     * *
     * @param eTrackingResult @see main.ETrackingResult<br></br>
     * * C type : main.ETrackingResult
     */
    constructor(mDeviceToAbsoluteTracking: HmdMatrix34_t, vVelocity: HmdVector3_t, vAngularVelocity: HmdVector3_t, eTrackingResult: Int, bPoseIsValid: Byte,
                bDeviceIsConnected: Byte) {
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
 *  Many fields are only valid for one main.ETrackedDeviceClass. */
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
    Prop_Axis0Type_Int32(3002), //          Return value is of type main.EVRControllerAxisType
    Prop_Axis1Type_Int32(3003), //          Return value is of type main.EVRControllerAxisType
    Prop_Axis2Type_Int32(3004), //          Return value is of type main.EVRControllerAxisType
    Prop_Axis3Type_Int32(3005), //          Return value is of type main.EVRControllerAxisType
    Prop_Axis4Type_Int32(3006), //          Return value is of type main.EVRControllerAxisType
    Prop_ControllerRoleHint_Int32(3007), // Return value is of type main.ETrackedControllerRole

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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("uMin", "vMin", "uMax", "vMax")

    constructor(uMin: Float, vMin: Float, uMax: Float, vMax: Float) {
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
    // vr::main.IVRSystem::ComputeDistortion() to apply the correct distortion to the rendered images before calling Submit().
    Submit_LensDistortionAlreadyApplied(0x01),

    // If the texture pointer passed in is actually a renderbuffer (e.g. for MSAA in OpenGL) then set this flag.
    Submit_GlRenderBuffer(0x02),

    // Handle is pointer to VulkanData_t
    Submit_VulkanTexture(0x04)
}

/** Data required for passing Vulkan textures to main.IVRCompositor::Submit.
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

    var button = 0  // main.EVRButtonId enum

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("button")

    constructor(button: Int) {
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
    var button = 0  // main.EVRMouseButton enum

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("x", "y", "button")

    constructor(x: Float, y: Float, button: Int) {
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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("xdelta", "ydelta", "repeatCount")

    constructor(xdelta: Float, ydelta: Float, repeatCount: Int) {
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

    var bFingerDown: Byte = 0   // true if the users finger is detected on the touch pad

    var flSecondsFingerDown = 0f    // How long the finger has been down in seconds

    // These values indicate the starting finger position (so you can do some basic swipe stuff)
    var fValueXFirst = 0f
    var fValueYFirst = 0f

    // This is the raw sampled coordinate without deadzoning
    var fValueXRaw = 0f
    var fValueYRaw = 0f

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("bFingerDown", "flSecondsFingerDown", "fValueXFirst", "fValueYFirst",
            "fValueXRaw", "fValueYRaw")

    constructor(bFingerDown: Byte, flSecondsFingerDown: Float, fValueXFirst: Float, fValueYFirst: Float, fValueXRaw: Float, fValueYRaw: Float) {
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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("ulUserValue", "notificationId")

    constructor(ulUserValue: Long, notificationId: Int) {
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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("pid", "oldPid", "bForced")

    constructor(pid: Int, oldPid: Int, bForced: Byte) {
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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("overlayHandle")

    constructor(overlayHandle: Long) {
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

    var statusState = 0 // main.EVRState enum

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("statusState")

    constructor(statusState: Int) {
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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("cNewInput", "uUserValue")

    constructor(cNewInput: String, uUserValue: Long) {
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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("ipdMeters")

    constructor(ipdMeters: Float) {
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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("m_nPreviousUniverse", "m_nCurrentUniverse")

    constructor(m_nPreviousUniverse: Long, m_nCurrentUniverse: Long) {
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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("reserved0", "reserved1")

    constructor(reserved0: Long, reserved1: Long) {
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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("m_nFidelityLevel")

    constructor(m_nFidelityLevel: Int) {
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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("bResetBySystemMenu")

    constructor(bResetBySystemMenu: Byte) {
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

    constructor()

    constructor(handle: Int, type: Int) {
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

    constructor()

    constructor(progress: Float) {
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

    constructor()

    constructor(pid: Int, unArgsHandle: Int) {
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

    constructor()

    constructor(overlayHandle: Long, nVisualMode: Int) {
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

    var eventType = 0   // main.EVREventType enum
    var TrackedDeviceIndex_t = 0
    var eventAgeSeconds = 0f
    var data: VREvent_Data_t? = null    // event data must be the end of the struct as its size is variable

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("eventType", "trackedDeviceIndex", "eventAgeSeconds", "data")

    constructor(eventType: Int, trackedDeviceIndex: Int, eventAgeSeconds: Float, data: VREvent_Data_t) {
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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("pVertexData", "unTriangleCount")

    constructor(pVertexData: HmdVector2_t.ByReference, unTriangleCount: Int) {
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

    var x = 0f  // Ranges from -1.0 to 1.0 for joysticks and track pads. Ranges from 0.0 to 1.0 for triggers were 0 is fully released.
    var y = 0f  // Ranges from -1.0 to 1.0 for joysticks and track pads. Is always 0.0 for triggers.

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("x", "y")

    constructor(x: Float, y: Float) {
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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("unPacketNum", "ulButtonPressed", "ulButtonTouched", "rAxis")

    constructor(unPacketNum: Int, ulButtonPressed: Long, ulButtonTouched: Long, rAxis: Array<VRControllerAxis_t>) {
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

    var size = 0    // sizeof(main.Compositor_OverlaySettings)
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
    var transform = HmdMatrix44_t()

    constructor()

    constructor(size: Int, curved: Byte, antialias: Byte, scale: Float, distance: Float, alpha: Float, uOffset: Float, vOffset: Float, uScale: Float,
                vScale: Float, gridDivs: Float, gridWidth: Float, gridScale: Float, transform: HmdMatrix44_t) {
        this.size = size
        this.curved = curved
        this.antialias = antialias
        this.scale = scale
        this.distance = distance
        this.alpha = alpha
        this.uOffset = uOffset
        this.vOffset = vOffset
        this.uScale = uScale
        this.vScale = vScale
        this.gridDivs = gridDivs
        this.gridWidth = gridWidth
        this.gridScale = gridScale
        this.transform = transform
    }

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
typealias VROverlayHandle_t_ByReference = LongByReference

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

/** enum values to pass in to main.VR_Init to identify whether the application will draw a 3D scene.     */
enum class EVRApplicationType(val i: Int) {
    VRApplication_Other(0), //          Some other kind of application that isn't covered by the other entries
    VRApplication_Scene(1), //          Application will submit 3D frames
    VRApplication_Overlay(2), //        Application only interacts with overlays
    VRApplication_Background(3), //     Application should not start SteamVR if it's not already running), and should not
    //                                  keep it running if everything else quits.
    VRApplication_Utility(4), //        Init should not try to load any drivers. The application needs access to utility
    //                                  interfaces (like main.IVRSettings and main.IVRApplications) but not hardware.
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

    constructor()

    constructor(eFrameType: Int, nWidth: Int, nHeight: Int, nBytesPerPixel: Int, nFrameSequence: Int, standingTrackedDevicePose: TrackedDevicePose_t) {

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

// ivrsystem.h =========================================================================================================
open class IVRSystem : Structure {

    // ------------------------------------
    // Display Methods
    // ------------------------------------

    /** Suggested size for the intermediate render target that the distortion pulls from. */
    @JvmField var GetRecommendedRenderTargetSize: GetRecommendedRenderTargetSize_callback? = null

    interface GetRecommendedRenderTargetSize_callback : Callback {
        fun apply(pnWidth: IntByReference, pnHeight: IntByReference)
    }

    /** The projection matrix for the specified eye */
    @JvmField var GetProjectionMatrix: GetProjectionMatrix_callback? = null

    interface GetProjectionMatrix_callback : Callback {
        fun apply(eEye: Int, fNearZ: Float, fFarZ: Float, eProjType: Int): HmdMatrix44_t.ByValue
    }

    /** The components necessary to build your own projection matrix in case your application is doing something fancy like infinite Z  */
    @JvmField var GetProjectionRaw: GetProjectionRaw_callback? = null

    interface GetProjectionRaw_callback : Callback {
        fun apply(eEye: Int, pfLeft: FloatByReference, pfRight: FloatByReference, pfTop: FloatByReference, pfBottom: FloatByReference)
    }

    /** Returns the result of the distortion function for the specified eye and input UVs.
     *  UVs go from 0,0 in the upper left of that eye's viewport and 1,1 in the lower right of that eye's viewport.
     *  Values may be NAN to indicate an error has occurred.     */
    @JvmField var ComputeDistortion: ComputeDistortion_callback? = null

    interface ComputeDistortion_callback : Callback {
        fun apply(eEye: Int, fU: Float, fV: Float)
    }

    /** Returns the transform from eye space to the head space. Eye space is the per-eye flavor of head space that provides stereo disparity.
     *  Instead of Model * View * Projection the sequence is Model * View * Eye^-1 * Projection.
     *  Normally View and Eye^-1 will be multiplied together and treated as View in your application.   */
    @JvmField var GetEyeToHeadTransform: GetEyeToHeadTransform_callback? = null

    interface GetEyeToHeadTransform_callback : Callback {
        fun apply(eEye: Int): HmdMatrix34_t.ByValue
    }

    /** Returns the number of elapsed seconds since the last recorded vsync event. This will come from a vsync timer event in the timer if possible or from the
     *  application-reported time if that is not available.
     *  If no vsync times are available the function will return zero for vsync time and frame counter and return false from the method.    */
    @JvmField var GetTimeSinceLastVsync: GetTimeSinceLastVsync_callback? = null

    interface GetTimeSinceLastVsync_callback : Callback {
        fun apply(pfSecondsSinceLastVsync: FloatByReference, pulFrameCounter: LongByReference): Byte
    }

    /** [D3D9 Only]
     * Returns the adapter index that the user should pass into CreateDevice to set up D3D9 in such a way that it can go full screen exclusive on the HMD.
     * Returns -1 if there was an error.     */
    @JvmField var GetD3D9AdapterIndex: GetD3D9AdapterIndex_callback? = null

    interface GetD3D9AdapterIndex_callback : Callback {
        fun apply(): Int
    }

    /** [D3D10/11 Only]
     * Returns the adapter index that the user should pass into EnumAdapters to create the device and swap chain in DX10 and DX11. If an error occurs the index
     * will be set to -1. The index will also be -1 if the headset is in direct mode on the driver side instead of using the compositor's builtin direct mode
     * support.      */
    @JvmField var GetDXGIOutputInfo: GetDXGIOutputInfo_callback? = null

    interface GetDXGIOutputInfo_callback : Callback {
        fun apply(pnAdapterIndex: IntByReference)
    }


    // ------------------------------------
    // Display Mode methods
    // ------------------------------------

    /** Use to determine if the headset display is part of the desktop (i.e. extended) or hidden (i.e. direct mode). */
    @JvmField var IsDisplayOnDesktop: IsDisplayOnDesktop_callback? = null

    interface IsDisplayOnDesktop_callback : Callback {
        fun apply(): Byte
    }

    /** Set the display visibility (true = extended, false = direct mode).  Return value of true indicates that the change was successful. */
    @JvmField var SetDisplayVisibility: SetDisplayVisibility_callback? = null

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
     *  main.k_unMaxTrackedDeviceCount.
     *
     *  Seated experiences should call this method with TrackingUniverseSeated and receive poses relative to the seated zero pose. Standing experiences should
     *  call this method with TrackingUniverseStanding and receive poses relative to the Chaperone Play Area. TrackingUniverseRawAndUncalibrated should probably
     *  not be used unless the application is the Chaperone calibration tool itself, but will provide poses relative to the hardware-specific coordinate system
     *  in the driver.     */
    @JvmField var GetDeviceToAbsoluteTrackingPose: GetDeviceToAbsoluteTrackingPose_callback? = null

    interface GetDeviceToAbsoluteTrackingPose_callback : Callback {
        fun apply(eOrigin: Int, fPredictedSecondsToPhotonsFromNow: Float, pTrackedDevicePoseArray: TrackedDevicePose_t.ByReference, unTrackedDevicePoseArrayCount: Int)
    }

    /** Sets the zero pose for the seated tracker coordinate system to the current position and yaw of the HMD. After ResetSeatedZeroPose all
     *  GetDeviceToAbsoluteTrackingPose calls that pass TrackingUniverseSeated as the origin will be relative to this new zero pose. The new zero coordinate
     *  system will not change the fact that the Y axis is up in the real world, so the next pose returned from GetDeviceToAbsoluteTrackingPose after a call to
     *  ResetSeatedZeroPose may not be exactly an identity matrix.
     *
     *  NOTE: This function overrides the user's previously saved seated zero pose and should only be called as the result of a user action.
     *  Users are also able to set their seated zero pose via the main.OpenVR Dashboard.     **/
    @JvmField var ResetSeatedZeroPose: ResetSeatedZeroPose_callback? = null

    interface ResetSeatedZeroPose_callback : Callback {
        fun apply()
    }

    /** Returns the transform from the seated zero pose to the standing absolute tracking system. This allows applications to represent the seated origin to
     *  used or transform object positions from one coordinate system to the other.
     *
     *  The seated origin may or may not be inside the Play Area or Collision Bounds returned by main.IVRChaperone. Its position depends on what the user has set
     *  from the Dashboard settings and previous calls to ResetSeatedZeroPose. */
    @JvmField var GetSeatedZeroPoseToStandingAbsoluteTrackingPose: GetSeatedZeroPoseToStandingAbsoluteTrackingPose_callback? = null

    interface GetSeatedZeroPoseToStandingAbsoluteTrackingPose_callback : Callback {
        fun apply(): HmdMatrix34_t.ByValue
    }

    /** Returns the transform from the tracking origin to the standing absolute tracking system. This allows applications to convert from raw tracking space to
     *  the calibrated standing coordinate system. */
    @JvmField var GetRawZeroPoseToStandingAbsoluteTrackingPose: GetRawZeroPoseToStandingAbsoluteTrackingPose_callback? = null

    interface GetRawZeroPoseToStandingAbsoluteTrackingPose_callback : Callback {
        fun apply(): HmdMatrix34_t.ByValue
    }

    /** Get a sorted array of device indices of a given class of tracked devices (e.g. controllers).  Devices are sorted right to left relative to the specified
     *  tracked device (default: hmd -- pass in -1 for absolute tracking space).  Returns the number of devices in the list, or the size of the array needed if
     *  not large enough. */
    @JvmField var GetSortedTrackedDeviceIndicesOfClass: GetSortedTrackedDeviceIndicesOfClass_callback? = null

    interface GetSortedTrackedDeviceIndicesOfClass_callback : Callback {
        fun apply(eTrackedDeviceClass: Int, punTrackedDeviceIndexArray: IntByReference, unTrackedDeviceIndexArrayCount: Int,
                  unRelativeToTrackedDeviceIndex: TrackedDeviceIndex_t = k_unTrackedDeviceIndex_Hmd): Int
    }

    /** Returns the level of activity on the device. */
    @JvmField var GetTrackedDeviceActivityLevel: GetTrackedDeviceActivityLevel_callback? = null

    interface GetTrackedDeviceActivityLevel_callback : Callback {
        fun apply(unDeviceId: TrackedDeviceIndex_t): Int
    }

    /** Convenience utility to apply the specified transform to the specified pose.
     *  This properly transforms all pose components, including velocity and angular velocity     */
    @JvmField var ApplyTransform: ApplyTransform_callback? = null

    interface ApplyTransform_callback : Callback {
        fun apply(pOutputPose: TrackedDevicePose_t.ByReference, pTrackedDevicePose: TrackedDevicePose_t.ByReference, pTransform: HmdMatrix34_t.ByReference)
    }

    /** Returns the device index associated with a specific role, for example the left hand or the right hand. */
    @JvmField var GetTrackedDeviceIndexForControllerRole: GetTrackedDeviceIndexForControllerRole_callback? = null

    interface GetTrackedDeviceIndexForControllerRole_callback : Callback {
        fun apply(unDeviceType: Int): TrackedDeviceIndex_t
    }

    /** Returns the controller type associated with a device index. */
    @JvmField var GetControllerRoleForTrackedDeviceIndex: GetControllerRoleForTrackedDeviceIndex_callback? = null

    interface GetControllerRoleForTrackedDeviceIndex_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t): Int
    }


    // ------------------------------------
    // Property methods
    // ------------------------------------

    /** Returns the device class of a tracked device. If there has not been a device connected in this slot since the application started this function will
     *  return TrackedDevice_Invalid. For previous detected devices the function will return the previously observed device class.
     *
     * To determine which devices exist on the system, just loop from 0 to main.k_unMaxTrackedDeviceCount and check the device class. Every device with something
     * other than TrackedDevice_Invalid is associated with an actual tracked device. */
    @JvmField var GetTrackedDeviceClass: GetTrackedDeviceClass_callback? = null

    interface GetTrackedDeviceClass_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t): Int
    }

    /** Returns true if there is a device connected in this slot. */
    @JvmField var IsTrackedDeviceConnected: IsTrackedDeviceConnected_callback? = null

    interface IsTrackedDeviceConnected_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t): Byte
    }

    /** Returns a bool property. If the device index is not valid or the property is not a bool type this function will return false. */
    @JvmField var GetBoolTrackedDeviceProperty: GetBoolTrackedDeviceProperty_callback? = null

    interface GetBoolTrackedDeviceProperty_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: IntByReference = IntByReference(0)): Byte
    }

    /** Returns a float property. If the device index is not valid or the property is not a float type this function will return 0. */
    @JvmField var GetFloatTrackedDeviceProperty: GetFloatTrackedDeviceProperty_callback? = null

    interface GetFloatTrackedDeviceProperty_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: IntByReference = IntByReference(0)): Float
    }

    /** Returns an int property. If the device index is not valid or the property is not a int type this function will return 0. */
    @JvmField var GetInt32TrackedDeviceProperty: GetInt32TrackedDeviceProperty_callback? = null

    interface GetInt32TrackedDeviceProperty_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: IntByReference = IntByReference(0)): Int
    }

    /** Returns a uint64 property. If the device index is not valid or the property is not a uint64 type this function will return 0. */
    @JvmField var GetUint64TrackedDeviceProperty: GetUint64TrackedDeviceProperty_callback? = null

    interface GetUint64TrackedDeviceProperty_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: IntByReference = IntByReference(0)): Long
    }

    /** Returns a matrix property. If the device index is not valid or the property is not a matrix type, this function will return identity. */
    @JvmField var GetMatrix34TrackedDeviceProperty: GetMatrix34TrackedDeviceProperty_callback? = null

    interface GetMatrix34TrackedDeviceProperty_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: IntByReference = IntByReference(0)): HmdMatrix34_t.ByValue
    }

    /** Returns a string property. If the device index is not valid or the property is not a string type this function will return 0. Otherwise it returns the
     *  length of the number of bytes necessary to hold this string including the trailing null. Strings will generally fit in buffers of main.k_unTrackingStringSize
     *  characters. */
    @JvmField var GetStringTrackedDeviceProperty: GetStringTrackedDeviceProperty_callback? = null

    interface GetStringTrackedDeviceProperty_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pchValue: String, unBufferSize: Int, pError: IntByReference = IntByReference(0)): Int
    }

    /** returns a string that corresponds with the specified property error. The string will be the name of the error enum value for all valid error codes */
    @JvmField var GetPropErrorNameFromEnum: GetPropErrorNameFromEnum_callback? = null

    interface GetPropErrorNameFromEnum_callback : Callback {
        fun apply(error: Int): String
    }


    // ------------------------------------
    // Event methods
    // ------------------------------------

    /** Returns true and fills the event with the next event on the queue if there is one. If there are no events this method returns false. uncbVREvent should
     *  be the size in bytes of the main.VREvent_t struct */
    @JvmField var PollNextEvent: PollNextEvent_callback? = null

    interface PollNextEvent_callback : Callback {
        fun apply(pEvent: VREvent_t.ByReference, uncbVREvent: Int): Byte
    }

    /** Returns true and fills the event with the next event on the queue if there is one. If there are no events this method returns false. Fills in the pose
     *  of the associated tracked device in the provided pose struct.
     *  This pose will always be older than the call to this function and should not be used to render the device.
     *  uncbVREvent should be the size in bytes of the main.VREvent_t struct */
    @JvmField var PollNextEventWithPose: PollNextEventWithPose_callback? = null

    interface PollNextEventWithPose_callback : Callback {
        fun apply(eOrigin: Int, pEvent: VREvent_t.ByReference, uncbVREvent: Int, pTrackedDevicePose: TrackedDevicePose_t.ByReference): Byte
    }

    /** returns the name of an EVREvent enum value */
    @JvmField var GetEventTypeNameFromEnum: GetEventTypeNameFromEnum_callback? = null

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
    @JvmField var GetHiddenAreaMesh: GetHiddenAreaMesh_callback? = null

    interface GetHiddenAreaMesh_callback : Callback {
        fun apply(eEye: Int): HiddenAreaMesh_t.ByValue
    }


    // ------------------------------------
    // Controller methods
    // ------------------------------------

    /** Fills the supplied struct with the current state of the controller. Returns false if the controller index is invalid. */
    @JvmField var GetControllerState: GetControllerState_callback? = null

    interface GetControllerState_callback : Callback {
        fun apply(unControllerDeviceIndex: TrackedDeviceIndex_t, pControllerState: VRControllerState_t.ByReference): Byte
    }

    /** Fills the supplied struct with the current state of the controller and the provided pose with the pose of the controller when the controller state was
     *  updated most recently. Use this form if you need a precise controller pose as input to your application when the user presses or releases a button. */
    @JvmField var GetControllerStateWithPose: GetControllerStateWithPose_callback? = null

    interface GetControllerStateWithPose_callback : Callback {
        fun apply(eOrigin: Int, unControllerDeviceIndex: TrackedDeviceIndex_t, pControllerState: VRControllerState_t.ByReference,
                  pTrackedDevicePose: TrackedDevicePose_t.ByReference): Byte
    }

    /** Trigger a single haptic pulse on a controller. After this call the application may not trigger another haptic pulse on this controller and axis
     *  combination for 5ms. */
    @JvmField var TriggerHapticPulse: TriggerHapticPulse_callback? = null

    interface TriggerHapticPulse_callback : Callback {
        fun apply(unControllerDeviceIndex: TrackedDeviceIndex_t, unAxisId: Int, usDurationMicroSec: Short)
    }

    /** returns the name of an main.EVRButtonId enum value */
    @JvmField var GetButtonIdNameFromEnum: GetButtonIdNameFromEnum_callback? = null

    interface GetButtonIdNameFromEnum_callback : Callback {
        fun apply(eButtonId: Int): String
    }

    /** returns the name of an main.EVRControllerAxisType enum value */
    @JvmField var GetControllerAxisTypeNameFromEnum: GetControllerAxisTypeNameFromEnum_callback? = null

    interface GetControllerAxisTypeNameFromEnum_callback : Callback {
        fun apply(eAxisType: Int): String
    }

    /** Tells main.OpenVR that this process wants exclusive access to controller button states and button events. Other apps will be notified that they have lost
     *  input focus with a VREvent_InputFocusCaptured event. Returns false if input focus could not be captured for some reason. */
    @JvmField var CaptureInputFocus: CaptureInputFocus_callback? = null

    interface CaptureInputFocus_callback : Callback {
        fun apply(): Byte
    }

    /** Tells main.OpenVR that this process no longer wants exclusive access to button states and button events. Other apps will be notified that input focus has
     *  been released with a VREvent_InputFocusReleased event. */
    @JvmField var ReleaseInputFocus: ReleaseInputFocus_callback? = null

    interface ReleaseInputFocus_callback : Callback {
        fun apply()
    }

    /** Returns true if input focus is captured by another process. */
    @JvmField var IsInputFocusCapturedByAnotherProcess: IsInputFocusCapturedByAnotherProcess_callback? = null

    interface IsInputFocusCapturedByAnotherProcess_callback : Callback {
        fun apply(): Byte
    }


    // ------------------------------------
    // Debug Methods
    // ------------------------------------

    /** Sends a request to the driver for the specified device and returns the response. The maximum response size is 32k, but this method can be called with
     *  a smaller buffer. If the response exceeds the size of the buffer, it is truncated.
     *  The size of the response including its terminating null is returned. */
    @JvmField var DriverDebugRequest: DriverDebugRequest_callback? = null

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
    @JvmField var PerformFirmwareUpdate: PerformFirmwareUpdate_callback? = null

    interface PerformFirmwareUpdate_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t): Int
    }


    // ------------------------------------
    // Application life cycle methods
    // ------------------------------------

    /** Call this to acknowledge to the system that VREvent_Quit has been received and that the process is exiting.
     *  This extends the timeout until the process is killed. */
    @JvmField var AcknowledgeQuit_Exiting: AcknowledgeQuit_Exiting_callback? = null

    interface AcknowledgeQuit_Exiting_callback : Callback {
        fun apply()
    }

    /** Call this to tell the system that the user is being prompted to save data. This halts the timeout and dismisses the dashboard (if it was up).
     *  Applications should be sure to actually prompt the user to save and then exit afterward, otherwise the user will be left in a confusing state. */
    @JvmField var AcknowledgeQuit_UserPrompt: AcknowledgeQuit_UserPrompt_callback? = null

    interface AcknowledgeQuit_UserPrompt_callback : Callback {
        fun apply()
    }

    constructor()

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
}

const val IVRSystem_Version = "FnTable:IVRSystem_012"

/** Used for all errors reported by the main.IVRApplications interface */
enum class EVRApplicationError(val i: Int) {

    VRApplicationError_None(0),

    VRApplicationError_AppKeyAlreadyExists(100), // Only one application can use any given key
    VRApplicationError_NoManifest(101), //          the running application does not have a manifest
    VRApplicationError_NoApplication(102), //       No application is running
    VRApplicationError_InvalidIndex(103),
    VRApplicationError_UnknownApplication(104), //  the application could not be found
    VRApplicationError_IPCFailed(105), //           An IPC failure caused the request to fail
    VRApplicationError_ApplicationAlreadyRunning(106),
    VRApplicationError_InvalidManifest(107),
    VRApplicationError_InvalidApplication(108),
    VRApplicationError_LaunchFailed(109), //        the process didn't start
    VRApplicationError_ApplicationAlreadyStarting(110), // the system was already starting the same application
    VRApplicationError_LaunchInProgress(111), //    The system was already starting a different application
    VRApplicationError_OldApplicationQuitting(112),
    VRApplicationError_TransitionAborted(113),
    VRApplicationError_IsTemplate(114), // error when you try to call LaunchApplication() on a template type app (use LaunchTemplateApplication)

    VRApplicationError_BufferTooSmall(200), //      The provided buffer was too small to fit the requested data
    VRApplicationError_PropertyNotSet(201), //      The requested property was not set
    VRApplicationError_UnknownProperty(202),
    VRApplicationError_InvalidParameter(203)
}

/** The maximum length of an application key */
const val k_unMaxApplicationKeyLength = 128

/** these are the properties available on applications. */
enum class EVRApplicationProperty(val i: Int) {

    VRApplicationProperty_Name_String(0),

    VRApplicationProperty_LaunchType_String(11),
    VRApplicationProperty_WorkingDirectory_String(12),
    VRApplicationProperty_BinaryPath_String(13),
    VRApplicationProperty_Arguments_String(14),
    VRApplicationProperty_URL_String(15),

    VRApplicationProperty_Description_String(50),
    VRApplicationProperty_NewsURL_String(51),
    VRApplicationProperty_ImagePath_String(52),
    VRApplicationProperty_Source_String(53),

    VRApplicationProperty_IsDashboardOverlay_Bool(60),
    VRApplicationProperty_IsTemplate_Bool(61),
    VRApplicationProperty_IsInstanced_Bool(62),

    VRApplicationProperty_LastLaunchTime_Uint64(70)
}

/** These are states the scene application startup process will go through. */
enum class EVRApplicationTransitionState(val i: Int) {

    VRApplicationTransition_None(0),

    VRApplicationTransition_OldAppQuitSent(10),
    VRApplicationTransition_WaitingForExternalLaunch(11),

    VRApplicationTransition_NewAppLaunched(20)
}

open class AppOverrideKeys_t : Structure {

    var pchKey = ""
    var pchValue = ""

    constructor()

    constructor(pchKey: String, pchValue: String) {
        this.pchKey = pchKey
        this.pchValue = pchValue
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<*> = Arrays.asList("pchKey", "pchValue")

    class ByReference : AppOverrideKeys_t(), Structure.ByReference
    class ByValue : AppOverrideKeys_t(), Structure.ByValue
}

/** Currently recognized mime types */
const val k_pch_MimeType_HomeApp = "vr/home"
const val k_pch_MimeType_GameTheater = "vr/game_theater"

// ivrapplications.h ===================================================================================================
open class IVRApplications : Structure {

    // ---------------  Application management  --------------- //

    /** Adds an application manifest to the list to load when building the list of installed applications.
     *  Temporary manifests are not automatically loaded */
    @JvmField var AddApplicationManifest: AddApplicationManifest_callback? = null

    interface AddApplicationManifest_callback : Callback {
        fun apply(pchApplicationManifestFullPath: String, bTemporary: Byte = 0): Int
    }

    /** Removes an application manifest from the list to load when building the list of installed applications. */
    @JvmField var RemoveApplicationManifest: RemoveApplicationManifest_callback? = null

    interface RemoveApplicationManifest_callback : Callback {
        fun apply(pchApplicationManifestFullPath: String): Int
    }

    /** Returns true if an application is installed */
    @JvmField var IsApplicationInstalled: IsApplicationInstalled_callback? = null

    interface IsApplicationInstalled_callback : Callback {
        fun apply(pchAppKey: String): Byte
    }

    /** Returns the number of applications available in the list */
    @JvmField var GetApplicationCount: GetApplicationCount_callback? = null

    interface GetApplicationCount_callback : Callback {
        fun apply(): Int
    }

    /** Returns the key of the specified application. The index is at least 0 and is less than the return value of GetApplicationCount(). The buffer should be
     *  at least main.k_unMaxApplicationKeyLength in order to fit the key. */
    @JvmField var GetApplicationKeyByIndex: GetApplicationKeyByIndex_callback? = null

    interface GetApplicationKeyByIndex_callback : Callback {
        fun apply(unApplicationIndex: Int, pchAppKeyBuffer: String, unAppKeyBufferLen: Int): Int
    }

    /** Returns the key of the application for the specified Process Id. The buffer should be at least main.k_unMaxApplicationKeyLength in order to fit the key. */
    @JvmField var GetApplicationKeyByProcessId: GetApplicationKeyByProcessId_callback? = null

    interface GetApplicationKeyByProcessId_callback : Callback {
        fun apply(unProcessId: Int, pchAppKeyBuffer: String, unAppKeyBufferLen: Int): Int
    }

    /** Launches the application. The existing scene application will exit and then the new application will start.
     *  This call is not valid for dashboard overlay applications. */
    @JvmField var LaunchApplication: LaunchApplication_callback? = null

    interface LaunchApplication_callback : Callback {
        fun apply(pchAppKey: String): Int
    }

    /** Launches an instance of an application of type template, with its app key being pchNewAppKey (which must be unique) and optionally override sections
     *  from the manifest file via main.AppOverrideKeys_t     */
    @JvmField var LaunchTemplateApplication: LaunchTemplateApplication_callback? = null

    interface LaunchTemplateApplication_callback : Callback {
        fun apply(pchTemplateAppKey: String, pchNewAppKey: String, pKeys: AppOverrideKeys_t.ByReference, unKeys: Int): Int
    }

    /** launches the application currently associated with this mime type and passes it the option args, typically the filename or object name of the item being
     *  launched     */
    @JvmField var LaunchApplicationFromMimeType: LaunchApplicationFromMimeType_callback? = null

    interface LaunchApplicationFromMimeType_callback : Callback {
        fun apply(pchMimeType: String, pchArgs: String): Int
    }

    /** Launches the dashboard overlay application if it is not already running. This call is only valid for dashboard overlay applications. */
    @JvmField var LaunchDashboardOverlay: LaunchDashboardOverlay_callback? = null

    interface LaunchDashboardOverlay_callback : Callback {
        fun apply(pchAppKey: String): Int
    }

    /** Cancel a pending launch for an application */
    @JvmField var CancelApplicationLaunch: CancelApplicationLaunch_callback? = null

    interface CancelApplicationLaunch_callback : Callback {
        fun apply(pchAppKey: String): Byte
    }

    /** Identifies a running application. main.OpenVR can't always tell which process started in response to a URL. This function allows a URL handler (or the process
     *  itself) to identify the app key for the now running application. Passing a process ID of 0 identifies the calling process.
     *  The application must be one that's known to the system via a call to AddApplicationManifest. */
    @JvmField var IdentifyApplication: IdentifyApplication_callback? = null

    interface IdentifyApplication_callback : Callback {
        fun apply(unProcessId: Int, pchAppKey: String): Int
    }

    /** Returns the process ID for an application. Return 0 if the application was not found or is not running. */
    @JvmField var GetApplicationProcessId: GetApplicationProcessId_callback? = null

    interface GetApplicationProcessId_callback : Callback {
        fun apply(pchAppKey: String): Int
    }

    /** Returns a string for an applications error */
    @JvmField var GetApplicationsErrorNameFromEnum: GetApplicationsErrorNameFromEnum_callback? = null

    interface GetApplicationsErrorNameFromEnum_callback : Callback {
        fun apply(error: Int): String
    }

    // ---------------  Application properties  --------------- //

    /** Returns a value for an application property. The required buffer size to fit this value will be returned. */
    interface GetApplicationPropertyString_callback : Callback {
        fun apply(pchAppKey: String, eProperty: Int, pchPropertyValueBuffer: String, unPropertyValueBufferLen: Int, peError: IntByReference? = null)
    }

    /** Returns a bool value for an application property. Returns false in all error cases. */
    @JvmField var GetApplicationPropertyBool: GetApplicationPropertyBool_callback? = null

    interface GetApplicationPropertyBool_callback : Callback {
        fun apply(pchAppKey: String, eProperty: Int, peError: IntByReference? = null)
    }

    /** Returns a uint64 value for an application property. Returns 0 in all error cases. */
    @JvmField var GetApplicationPropertyUint64: GetApplicationPropertyUint64_callback? = null

    interface GetApplicationPropertyUint64_callback : Callback {
        fun apply(pchAppKey: String, eProperty: Int, peError: IntByReference? = null): Long
    }

    /** Sets the application auto-launch flag. This is only valid for applications which return true for VRApplicationProperty_IsDashboardOverlay_Bool. */
    @JvmField var SetApplicationAutoLaunch: SetApplicationAutoLaunch_callback? = null

    interface SetApplicationAutoLaunch_callback : Callback {
        fun apply(pchAppKey: String, bAutoLaunch: Byte): Int
    }

    /** Gets the application auto-launch flag. This is only valid for applications which return true for VRApplicationProperty_IsDashboardOverlay_Bool. */
    @JvmField var GetApplicationAutoLaunch: GetApplicationAutoLaunch_callback? = null

    interface GetApplicationAutoLaunch_callback : Callback {
        fun apply(pchAppKey: String): Byte
    }

    /** Adds this mime-type to the list of supported mime types for this application*/
    @JvmField var SetDefaultApplicationForMimeType: SetDefaultApplicationForMimeType_callback? = null

    interface SetDefaultApplicationForMimeType_callback : Callback {
        fun apply(pchAppKey: String, pchMimeType: String): Int
    }

    /** return the app key that will open this mime type */
    @JvmField var GetDefaultApplicationForMimeType: GetDefaultApplicationForMimeType_callback? = null

    interface GetDefaultApplicationForMimeType_callback : Callback {
        fun apply(pchMimeType: String, pchAppKeyBuffer: String, unAppKeyBufferLen: Int): Byte
    }

    /** Get the list of supported mime types for this application, comma-delimited */
    @JvmField var GetApplicationSupportedMimeTypes: GetApplicationSupportedMimeTypes_callback? = null

    interface GetApplicationSupportedMimeTypes_callback : Callback {
        fun apply(pchAppKey: String, pchMimeTypesBuffer: String, unMimeTypesBuffer: Int): Byte
    }

    /** Get the list of app-keys that support this mime type, comma-delimited, the return value is number of bytes you need to return the full string */
    interface GetApplicationsThatSupportMimeType_callback : Callback {
        fun apply(pchMimeType: String, pchAppKeysThatSupportBuffer: String, unAppKeysThatSupportBuffer: Int): Int
    }

    /** Get the args list from an app launch that had the process already running, you call this when you get a VREvent_ApplicationMimeTypeLoad */
    @JvmField var GetApplicationLaunchArguments: GetApplicationLaunchArguments_callback? = null

    interface GetApplicationLaunchArguments_callback : Callback {
        fun apply(unHandle: Int, pchArgs: String, unArgs: Int): Int
    }

    // ---------------  Transition methods --------------- //

    /** Returns the app key for the application that is starting up */
    @JvmField var GetStartingApplication: GetStartingApplication_callback? = null

    interface GetStartingApplication_callback : Callback {
        fun apply(pchAppKeyBuffer: String, unAppKeyBufferLen: Int): Int
    }

    /** Returns the application transition state */
    @JvmField var GetTransitionState: GetTransitionState_callback? = null

    interface GetTransitionState_callback : Callback {
        fun apply(): Int
    }

    /** Returns errors that would prevent the specified application from launching immediately. Calling this function will cause the current scene application to
     *  quit, so only call it when you are actually about to launch something else.
     *  What the caller should do about these failures depends on the failure:
     *      VRApplicationError_OldApplicationQuitting       - An existing application has been told to quit. Wait for a VREvent_ProcessQuit and try again.
     *      VRApplicationError_ApplicationAlreadyStarting   - This application is already starting. This is a permanent failure.
     *      VRApplicationError_LaunchInProgress	            - A different application is already starting. This is a permanent failure.
     *      VRApplicationError_None                         - Go ahead and launch. Everything is clear.
     */
    @JvmField var PerformApplicationPrelaunchCheck: PerformApplicationPrelaunchCheck_callback? = null

    interface PerformApplicationPrelaunchCheck_callback : Callback {
        fun apply(pchAppKey: String): Int
    }

    /** Returns a string for an application transition state */
    @JvmField var GetApplicationsTransitionStateNameFromEnum: GetApplicationsTransitionStateNameFromEnum_callback? = null

    interface GetApplicationsTransitionStateNameFromEnum_callback : Callback {
        fun apply(state: Int): String
    }

    /** Returns true if the outgoing scene app has requested a save prompt before exiting */
    @JvmField var IsQuitUserPromptRequested: IsQuitUserPromptRequested_callback? = null

    interface IsQuitUserPromptRequested_callback : Callback {
        fun apply(): Byte
    }

    /** Starts a subprocess within the calling application. This suppresses all application transition UI and automatically identifies the new executable as part
     *  of the same application. On success the calling process should exit immediately.
     *  If working directory is NULL or "" the directory portion of the binary path will be the working directory. */
    @JvmField var LaunchInternalProcess: LaunchInternalProcess_callback? = null

    interface LaunchInternalProcess_callback : Callback {
        fun apply(pchBinaryPath: String, pchArguments: String, pchWorkingDirectory: String): Int
    }

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("AddApplicationManifest", "RemoveApplicationManifest", "IsApplicationInstalled", "GetApplicationCount",
            "GetApplicationKeyByIndex", "GetApplicationKeyByProcessId", "LaunchApplication", "LaunchTemplateApplication", "LaunchApplicationFromMimeType",
            "LaunchDashboardOverlay", "CancelApplicationLaunch", "IdentifyApplication", "GetApplicationProcessId", "GetApplicationsErrorNameFromEnum",
            "GetApplicationPropertyString", "GetApplicationPropertyBool", "GetApplicationPropertyUint64", "SetApplicationAutoLaunch", "GetApplicationAutoLaunch",
            "SetDefaultApplicationForMimeType", "GetDefaultApplicationForMimeType", "GetApplicationSupportedMimeTypes", "GetApplicationsThatSupportMimeType",
            "GetApplicationLaunchArguments", "GetStartingApplication", "GetTransitionState", "PerformApplicationPrelaunchCheck",
            "GetApplicationsTransitionStateNameFromEnum", "IsQuitUserPromptRequested", "LaunchInternalProcess")

    constructor (peer: Pointer) : super(peer) {
        read()
    }
}

const val IVRApplications_Version = "FnTable:IVRApplications_006"

enum class EVRSettingsError(val i: Int) {

    VRSettingsError_None(0),
    VRSettingsError_IPCFailed(1),
    VRSettingsError_WriteFailed(2),
    VRSettingsError_ReadFailed(3),
    VRSettingsError_JsonParseFailed(4),
    VRSettingsError_UnsetSettingHasNoDefault(5) // This will be returned if the setting does not appear in the appropriate default file and has not been set
}

// The maximum length of a settings key
const val k_unMaxSettingsKeyLength = 128

// ivrsettings.h =======================================================================================================
open class IVRSettings : Structure {

    @JvmField var GetSettingsErrorNameFromEnum: GetSettingsErrorNameFromEnum_callback? = null

    interface GetSettingsErrorNameFromEnum_callback : Callback {
        fun apply(eError: Int): String
    }

    // Returns true if file sync occurred (force or settings dirty)
    @JvmField var Sync: Sync_callback? = null

    interface Sync_callback : Callback {
        fun apply(bForce: Byte = 0, peError: IntByReference? = null): Byte
    }

    @JvmField var SetBool: SetBool_callback? = null

    interface SetBool_callback : Callback {
        fun apply(pchSection: String, pchSettingsKey: String, bValue: Byte, peError: IntByReference? = null)
    }

    @JvmField var SetInt32: SetInt32_callback? = null

    interface SetInt32_callback : Callback {
        fun apply(pchSection: String, pchSettingsKey: String, nValue: Int, peError: IntByReference? = null)
    }

    @JvmField var SetFloat: SetFloat_callback? = null

    interface SetFloat_callback : Callback {
        fun apply(pchSection: String, pchSettingsKey: String, flValue: Float, peError: IntByReference? = null)
    }

    @JvmField var SetString: SetString_callback? = null

    interface SetString_callback : Callback {
        fun apply(pchSection: String, pchSettingsKey: String, pchValue: String, peError: IntByReference? = null)
    }

    // Users of the system need to provide a proper default in default.vrsettings in the resources/settings/ directory of either the runtime or the driver_xxx
    // directory. Otherwise the default will be false, 0, 0.0 or ""
    @JvmField var GetBool: GetBool_callback? = null

    interface GetBool_callback : Callback {
        fun apply(pchSection: String, pchSettingsKey: String, peError: IntByReference? = null): Byte
    }

    @JvmField var GetInt32: GetInt32_callback? = null

    interface GetInt32_callback : Callback {
        fun apply(pchSection: String, pchSettingsKey: String, peError: IntByReference? = null): Int
    }

    @JvmField var GetFloat: GetFloat_callback? = null

    interface GetFloat_callback : Callback {
        fun apply(pchSection: String, pchSettingsKey: String, peError: IntByReference? = null): Float
    }

    @JvmField var GetString: GetString_callback? = null

    interface GetString_callback : Callback {
        fun apply(pchSection: String, pchSettingsKey: String, pchValue: String, unValueLen: Int, peError: IntByReference? = null)
    }

    @JvmField var RemoveSection: RemoveSection_callback? = null

    interface RemoveSection_callback : Callback {
        fun apply(pchSection: String, peError: IntByReference? = null)
    }

    @JvmField var RemoveKeyInSection: RemoveKeyInSection_callback? = null

    interface RemoveKeyInSection_callback : Callback {
        fun apply(pchSection: String, pchSettingsKey: String, peError: IntByReference? = null)
    }


    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("GetSettingsErrorNameFromEnum", "Sync", "SetBool", "SetInt32", "SetFloat", "SetString", "GetBool",
            "GetInt32", "GetFloat", "GetString", "RemoveSection", "RemoveKeyInSection")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRSettings(), Structure.ByReference
    class ByValue : IVRSettings(), Structure.ByValue
}

//-----------------------------------------------------------------------------
const val IVRSettings_Version = "FnTable:IVRSettings_002";

//-----------------------------------------------------------------------------
// steamvr keys

const val k_pch_SteamVR_Section = "steamvr"
const val k_pch_SteamVR_RequireHmd_String = "requireHmd"
const val k_pch_SteamVR_ForcedDriverKey_String = "forcedDriver"
const val k_pch_SteamVR_ForcedHmdKey_String = "forcedHmd"
const val k_pch_SteamVR_DisplayDebug_Bool = "displayDebug"
const val k_pch_SteamVR_DebugProcessPipe_String = "debugProcessPipe"
const val k_pch_SteamVR_EnableDistortion_Bool = "enableDistortion"
const val k_pch_SteamVR_DisplayDebugX_Int32 = "displayDebugX"
const val k_pch_SteamVR_DisplayDebugY_Int32 = "displayDebugY"
const val k_pch_SteamVR_SendSystemButtonToAllApps_Bool = "sendSystemButtonToAllApps"
const val k_pch_SteamVR_LogLevel_Int32 = "loglevel"
const val k_pch_SteamVR_IPD_Float = "ipd"
const val k_pch_SteamVR_Background_String = "background"
const val k_pch_SteamVR_BackgroundCameraHeight_Float = "backgroundCameraHeight"
const val k_pch_SteamVR_BackgroundDomeRadius_Float = "backgroundDomeRadius"
const val k_pch_SteamVR_Environment_String = "environment"
const val k_pch_SteamVR_GridColor_String = "gridColor"
const val k_pch_SteamVR_PlayAreaColor_String = "playAreaColor"
const val k_pch_SteamVR_ShowStage_Bool = "showStage"
const val k_pch_SteamVR_ActivateMultipleDrivers_Bool = "activateMultipleDrivers"
const val k_pch_SteamVR_DirectMode_Bool = "directMode"
const val k_pch_SteamVR_DirectModeEdidVid_Int32 = "directModeEdidVid"
const val k_pch_SteamVR_DirectModeEdidPid_Int32 = "directModeEdidPid"
const val k_pch_SteamVR_UsingSpeakers_Bool = "usingSpeakers"
const val k_pch_SteamVR_SpeakersForwardYawOffsetDegrees_Float = "speakersForwardYawOffsetDegrees"
const val k_pch_SteamVR_BaseStationPowerManagement_Bool = "basestationPowerManagement"
const val k_pch_SteamVR_NeverKillProcesses_Bool = "neverKillProcesses"
const val k_pch_SteamVR_RenderTargetMultiplier_Float = "renderTargetMultiplier"
const val k_pch_SteamVR_AllowReprojection_Bool = "allowReprojection"
const val k_pch_SteamVR_ForceReprojection_Bool = "forceReprojection"
const val k_pch_SteamVR_ForceFadeOnBadTracking_Bool = "forceFadeOnBadTracking"
const val k_pch_SteamVR_DefaultMirrorView_Int32 = "defaultMirrorView"
const val k_pch_SteamVR_ShowMirrorView_Bool = "showMirrorView"
const val k_pch_SteamVR_MirrorViewGeometry_String = "mirrorViewGeometry"
const val k_pch_SteamVR_StartMonitorFromAppLaunch = "startMonitorFromAppLaunch"
const val k_pch_SteamVR_EnableHomeApp = "enableHomeApp"
const val k_pch_SteamVR_SetInitialDefaultHomeApp = "setInitialDefaultHomeApp"
const val k_pch_SteamVR_CycleBackgroundImageTimeSec_Int32 = "CycleBackgroundImageTimeSec"
const val k_pch_SteamVR_RetailDemo_Bool = "retailDemo"


//-----------------------------------------------------------------------------
// lighthouse keys

const val k_pch_Lighthouse_Section = "driver_lighthouse"
const val k_pch_Lighthouse_DisableIMU_Bool = "disableimu"
const val k_pch_Lighthouse_UseDisambiguation_String = "usedisambiguation"
const val k_pch_Lighthouse_DisambiguationDebug_Int32 = "disambiguationdebug"

const val k_pch_Lighthouse_PrimaryBasestation_Int32 = "primarybasestation"
const val k_pch_Lighthouse_DBHistory_Bool = "dbhistory"

//-----------------------------------------------------------------------------
// null keys

const val k_pch_Null_Section = "driver_null"
const val k_pch_Null_EnableNullDriver_Bool = "enable"
const val k_pch_Null_SerialNumber_String = "serialNumber"
const val k_pch_Null_ModelNumber_String = "modelNumber"
const val k_pch_Null_WindowX_Int32 = "windowX"
const val k_pch_Null_WindowY_Int32 = "windowY"
const val k_pch_Null_WindowWidth_Int32 = "windowWidth"
const val k_pch_Null_WindowHeight_Int32 = "windowHeight"
const val k_pch_Null_RenderWidth_Int32 = "renderWidth"
const val k_pch_Null_RenderHeight_Int32 = "renderHeight"
const val k_pch_Null_SecondsFromVsyncToPhotons_Float = "secondsFromVsyncToPhotons"
const val k_pch_Null_DisplayFrequency_Float = "displayFrequency"

//-----------------------------------------------------------------------------
// user interface keys
const val k_pch_UserInterface_Section = "userinterface"
const val k_pch_UserInterface_StatusAlwaysOnTop_Bool = "StatusAlwaysOnTop"
const val k_pch_UserInterface_Screenshots_Bool = "screenshots"
const val k_pch_UserInterface_ScreenshotType_Int = "screenshotType"

//-----------------------------------------------------------------------------
// notification keys
const val k_pch_Notifications_Section = "notifications"
const val k_pch_Notifications_DoNotDisturb_Bool = "DoNotDisturb"

//-----------------------------------------------------------------------------
// keyboard keys
const val k_pch_Keyboard_Section = "keyboard"
const val k_pch_Keyboard_TutorialCompletions = "TutorialCompletions"
const val k_pch_Keyboard_ScaleX = "ScaleX"
const val k_pch_Keyboard_ScaleY = "ScaleY"
const val k_pch_Keyboard_OffsetLeftX = "OffsetLeftX"
const val k_pch_Keyboard_OffsetRightX = "OffsetRightX"
const val k_pch_Keyboard_OffsetY = "OffsetY"
const val k_pch_Keyboard_Smoothing = "Smoothing"

//-----------------------------------------------------------------------------
// perf keys
const val k_pch_Perf_Section = "perfcheck"
const val k_pch_Perf_HeuristicActive_Bool = "heuristicActive"
const val k_pch_Perf_NotifyInHMD_Bool = "warnInHMD"
const val k_pch_Perf_NotifyOnlyOnce_Bool = "warnOnlyOnce"
const val k_pch_Perf_AllowTimingStore_Bool = "allowTimingStore"
const val k_pch_Perf_SaveTimingsOnExit_Bool = "saveTimingsOnExit"
const val k_pch_Perf_TestData_Float = "perfTestData"

//-----------------------------------------------------------------------------
// collision bounds keys
const val k_pch_CollisionBounds_Section = "collisionBounds"
const val k_pch_CollisionBounds_Style_Int32 = "CollisionBoundsStyle"
const val k_pch_CollisionBounds_GroundPerimeterOn_Bool = "CollisionBoundsGroundPerimeterOn"
const val k_pch_CollisionBounds_CenterMarkerOn_Bool = "CollisionBoundsCenterMarkerOn"
const val k_pch_CollisionBounds_PlaySpaceOn_Bool = "CollisionBoundsPlaySpaceOn"
const val k_pch_CollisionBounds_FadeDistance_Float = "CollisionBoundsFadeDistance"
const val k_pch_CollisionBounds_ColorGammaR_Int32 = "CollisionBoundsColorGammaR"
const val k_pch_CollisionBounds_ColorGammaG_Int32 = "CollisionBoundsColorGammaG"
const val k_pch_CollisionBounds_ColorGammaB_Int32 = "CollisionBoundsColorGammaB"
const val k_pch_CollisionBounds_ColorGammaA_Int32 = "CollisionBoundsColorGammaA"

//-----------------------------------------------------------------------------
// camera keys
const val k_pch_Camera_Section = "camera"
const val k_pch_Camera_EnableCamera_Bool = "enableCamera"
const val k_pch_Camera_EnableCameraInDashboard_Bool = "enableCameraInDashboard"
const val k_pch_Camera_EnableCameraForCollisionBounds_Bool = "enableCameraForCollisionBounds"
const val k_pch_Camera_EnableCameraForRoomView_Bool = "enableCameraForRoomView"
const val k_pch_Camera_BoundsColorGammaR_Int32 = "cameraBoundsColorGammaR"
const val k_pch_Camera_BoundsColorGammaG_Int32 = "cameraBoundsColorGammaG"
const val k_pch_Camera_BoundsColorGammaB_Int32 = "cameraBoundsColorGammaB"
const val k_pch_Camera_BoundsColorGammaA_Int32 = "cameraBoundsColorGammaA"
const val k_pch_Camera_BoundsStrength_Int32 = "cameraBoundsStrength"

//-----------------------------------------------------------------------------
// audio keys
const val k_pch_audio_Section = "audio"
const val k_pch_audio_OnPlaybackDevice_String = "onPlaybackDevice"
const val k_pch_audio_OnRecordDevice_String = "onRecordDevice"
const val k_pch_audio_OnPlaybackMirrorDevice_String = "onPlaybackMirrorDevice"
const val k_pch_audio_OffPlaybackDevice_String = "offPlaybackDevice"
const val k_pch_audio_OffRecordDevice_String = "offRecordDevice"
const val k_pch_audio_VIVEHDMIGain = "viveHDMIGain"

//-----------------------------------------------------------------------------
// power management keys
const val k_pch_Power_Section = "power"
const val k_pch_Power_PowerOffOnExit_Bool = "powerOffOnExit"
const val k_pch_Power_TurnOffScreensTimeout_Float = "turnOffScreensTimeout"
const val k_pch_Power_TurnOffControllersTimeout_Float = "turnOffControllersTimeout"
const val k_pch_Power_ReturnToWatchdogTimeout_Float = "returnToWatchdogTimeout"
const val k_pch_Power_AutoLaunchSteamVROnButtonPress = "autoLaunchSteamVROnButtonPress"

//-----------------------------------------------------------------------------
// dashboard keys
const val k_pch_Dashboard_Section = "dashboard"
const val k_pch_Dashboard_EnableDashboard_Bool = "enableDashboard"
const val k_pch_Dashboard_ArcadeMode_Bool = "arcadeMode"

//-----------------------------------------------------------------------------
// model skin keys
const val k_pch_modelskin_Section = "modelskins"

// ivrchaperone.h ======================================================================================================

enum class ChaperoneCalibrationState(val i: Int) {

    // OK!
    ChaperoneCalibrationState_OK(1), //                                 Chaperone is fully calibrated and working correctly

    // Warnings
    ChaperoneCalibrationState_Warning(100),
    ChaperoneCalibrationState_Warning_BaseStationMayHaveMoved(101), //  A base station thinks that it might have moved
    ChaperoneCalibrationState_Warning_BaseStationRemoved(102), //       There are less base stations than when calibrated
    ChaperoneCalibrationState_Warning_SeatedBoundsInvalid(103), //      Seated bounds haven't been calibrated for the current tracking center

    // Errors
    ChaperoneCalibrationState_Error(200), //                            The UniverseID is invalid
    ChaperoneCalibrationState_Error_BaseStationUninitalized(201), //    Tracking center hasn't be calibrated for at least one of the base stations
    ChaperoneCalibrationState_Error_BaseStationConflict(202), //        Tracking center is calibrated), but base stations disagree on the tracking space
    ChaperoneCalibrationState_Error_PlayAreaInvalid(203), //            Play Area hasn't been calibrated for the current tracking center
    ChaperoneCalibrationState_Error_CollisionBoundsInvalid(204)      // Collision Bounds haven't been calibrated for the current tracking center
}

/** HIGH LEVEL TRACKING SPACE ASSUMPTIONS:
 * 0,0,0 is the preferred standing area center.
 * 0Y is the floor height.
 * -Z is the preferred forward facing direction. */

open class IVRChaperone : Structure {

    /** Get the current state of Chaperone calibration. This state can change at any time during a session due to physical base station changes. **/
    @JvmField var GetCalibrationState: GetCalibrationState_callback? = null

    interface GetCalibrationState_callback : Callback {
        fun apply(): Int
    }

    /** Returns the width and depth of the Play Area (formerly named Soft Bounds) in X and Z.
     * Tracking space center (0,0,0) is the center of the Play Area. **/
    @JvmField var GetPlayAreaSize: GetPlayAreaSize_callback? = null

    interface GetPlayAreaSize_callback : Callback {
        fun apply(pSizeX: FloatByReference, pSizeZ: FloatByReference): Byte
    }

    /** Returns the 4 corner positions of the Play Area (formerly named Soft Bounds).
     * Corners are in counter-clockwise order.
     * Standing center (0,0,0) is the center of the Play Area.
     * It's a rectangle.
     * 2 sides are parallel to the X axis and 2 sides are parallel to the Z axis.
     * Height of every corner is 0Y (on the floor). **/
    @JvmField var GetPlayAreaRect: GetPlayAreaRect_callback? = null

    interface GetPlayAreaRect_callback : Callback {
        fun apply(rect: HmdQuad_t.ByReference): Byte
    }

    /** Reload Chaperone data from the .vrchap file on disk. */
    @JvmField var ReloadInfo: ReloadInfo_callback? = null

    interface ReloadInfo_callback : Callback {
        fun apply()
    }

    /** Optionally give the chaperone system a hit about the color and brightness in the scene **/
    @JvmField var SetSceneColor: SetSceneColor_callback? = null

    interface SetSceneColor_callback : Callback {
        fun apply(color: HmdColor_t)
    }

    /** Get the current chaperone bounds draw color and brightness **/
    @JvmField var GetBoundsColor: GetBoundsColor_callback? = null

    interface GetBoundsColor_callback : Callback {
        fun apply(pOutputColorArray: HmdColor_t.ByReference, nNumOutputColors: Int, flCollisionBoundsFadeDistance: Float, pOutputCameraColor: HmdColor_t.ByReference)
    }

    /** Determine whether the bounds are showing right now **/
    interface AreBoundsVisible_callback : Callback {
        fun apply(): Byte
    }

    /** Force the bounds to show, mostly for utilities **/
    @JvmField var ForceBoundsVisible: ForceBoundsVisible_callback? = null

    interface ForceBoundsVisible_callback : Callback {
        fun apply(bForce: Byte)
    }

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("GetCalibrationState", "GetPlayAreaSize", "GetPlayAreaRect", "ReloadInfo", "SetSceneColor",
            "GetBoundsColor", "AreBoundsVisible_callback", "ForceBoundsVisible")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRChaperone(), Structure.ByReference
    class ByValue : IVRChaperone(), Structure.ByValue
}

const val IVRChaperone_Version = "FnTable:IVRChaperone_003";

// ivrcompositor.h =====================================================================================================

/** Errors that can occur with the VR compositor */
enum class EVRCompositorError(val i: Int) {

    VRCompositorError_None(0),
    VRCompositorError_RequestFailed(1),
    VRCompositorError_IncompatibleVersion(100),
    VRCompositorError_DoNotHaveFocus(101),
    VRCompositorError_InvalidTexture(102),
    VRCompositorError_IsNotSceneApplication(103),
    VRCompositorError_TextureIsOnWrongDevice(104),
    VRCompositorError_TextureUsesUnsupportedFormat(105),
    VRCompositorError_SharedTexturesNotSupported(106),
    VRCompositorError_IndexOutOfRange(107)
}

const val VRCompositor_ReprojectionReason_Cpu = 0x01
const val VRCompositor_ReprojectionReason_Gpu = 0x02

/** Provides a single frame's timing information to the app */
open class Compositor_FrameTiming : Structure {

    var m_nSize = 0 // Set to sizeof( main.Compositor_FrameTiming )
    var m_nFrameIndex = 0
    var m_nNumFramePresents = 0 // number of times this frame was presented
    var m_nNumDroppedFrames = 0 // number of additional times previous frame was scanned out
    var m_nReprojectionFlags = 0

    /** Absolute time reference for comparing frames.  This aligns with the vsync that running start is relative to. */
    var m_flSystemTimeInSeconds = 0.0

    /** These times may include work from other processes due to OS scheduling.
     *  The fewer packets of work these are broken up into, the less likely this will happen.
     *  GPU work can be broken up by calling Flush.  This can sometimes be useful to get the GPU started processing that work earlier in the frame. */
    var m_flPreSubmitGpuMs = 0f // time spent rendering the scene (gpu work submitted between WaitGetPoses and second Submit)
    var m_flPostSubmitGpuMs = 0f // additional time spent rendering by application (e.g. companion window)
    var m_flTotalRenderGpuMs = 0f // time between work submitted immediately after present (ideally vsync) until the end of compositor submitted work
    var m_flCompositorRenderGpuMs = 0f // time spend performing distortion correction, rendering chaperone, overlays, etc.
    var m_flCompositorRenderCpuMs = 0f // time spent on cpu submitting the above work for this frame
    var m_flCompositorIdleCpuMs = 0f // time spent waiting for running start (application could have used this much more time)

    /** Miscellaneous measured intervals. */
    var m_flClientFrameIntervalMs = 0f // time between calls to WaitGetPoses
    var m_flPresentCallCpuMs = 0f // time blocked on call to present (usually 0.0, but can go long)
    var m_flWaitForPresentCpuMs = 0f // time spent spin-waiting for frame index to change (not near-zero indicates wait object failure)
    var m_flSubmitFrameMs = 0f // time spent in main.IVRCompositor::Submit (not near-zero indicates driver issue)

    /** The following are all relative to this frame's SystemTimeInSeconds */
    var m_flWaitGetPosesCalledMs = 0f
    var m_flNewPosesReadyMs = 0f
    var m_flNewFrameReadyMs = 0f // second call to main.IVRCompositor::Submit
    var m_flCompositorUpdateStartMs = 0f
    var m_flCompositorUpdateEndMs = 0f
    var m_flCompositorRenderStartMs = 0f

    var m_HmdPose = TrackedDevicePose_t() // pose used by app to render this frame

    constructor()

    constructor(m_nSize: Int, m_nFrameIndex: Int, m_nNumFramePresents: Int, m_nNumDroppedFrames: Int, m_nReprojectionFlags: Int, m_flSystemTimeInSeconds: Double,
                m_flPreSubmitGpuMs: Float, m_flPostSubmitGpuMs: Float, m_flTotalRenderGpuMs: Float, m_flCompositorRenderGpuMs: Float,
                m_flCompositorRenderCpuMs: Float, m_flCompositorIdleCpuMs: Float, m_flClientFrameIntervalMs: Float, m_flPresentCallCpuMs: Float,
                m_flWaitForPresentCpuMs: Float, m_flSubmitFrameMs: Float, m_flWaitGetPosesCalledMs: Float, m_flNewPosesReadyMs: Float, m_flNewFrameReadyMs: Float,
                m_flCompositorUpdateStartMs: Float, m_flCompositorUpdateEndMs: Float, m_flCompositorRenderStartMs: Float, m_HmdPose: TrackedDevicePose_t) {
        this.m_nSize = m_nSize
        this.m_nFrameIndex = m_nFrameIndex
        this.m_nNumFramePresents = m_nNumFramePresents
        this.m_nNumDroppedFrames = m_nNumDroppedFrames
        this.m_nReprojectionFlags = m_nReprojectionFlags
        this.m_flSystemTimeInSeconds = m_flSystemTimeInSeconds
        this.m_flPreSubmitGpuMs = m_flPreSubmitGpuMs
        this.m_flPostSubmitGpuMs = m_flPostSubmitGpuMs
        this.m_flTotalRenderGpuMs = m_flTotalRenderGpuMs
        this.m_flCompositorRenderGpuMs = m_flCompositorRenderGpuMs
        this.m_flCompositorRenderCpuMs = m_flCompositorRenderCpuMs
        this.m_flCompositorIdleCpuMs = m_flCompositorIdleCpuMs
        this.m_flClientFrameIntervalMs = m_flClientFrameIntervalMs
        this.m_flPresentCallCpuMs = m_flPresentCallCpuMs
        this.m_flWaitForPresentCpuMs = m_flWaitForPresentCpuMs
        this.m_flSubmitFrameMs = m_flSubmitFrameMs
        this.m_flWaitGetPosesCalledMs = m_flWaitGetPosesCalledMs
        this.m_flNewPosesReadyMs = m_flNewPosesReadyMs
        this.m_flNewFrameReadyMs = m_flNewFrameReadyMs
        this.m_flCompositorUpdateStartMs = m_flCompositorUpdateStartMs
        this.m_flCompositorUpdateEndMs = m_flCompositorUpdateEndMs
        this.m_flCompositorRenderStartMs = m_flCompositorRenderStartMs
        this.m_HmdPose = m_HmdPose
    }

    override fun getFieldOrder(): List<*> = Arrays.asList("m_nSize", "m_nFrameIndex", "m_nNumFramePresents", "m_nNumDroppedFrames", "m_nReprojectionFlags",
            "m_flSystemTimeInSeconds", "m_flPreSubmitGpuMs", "m_flPostSubmitGpuMs", "m_flTotalRenderGpuMs", "m_flCompositorRenderGpuMs",
            "m_flCompositorRenderCpuMs", "m_flCompositorIdleCpuMs", "m_flClientFrameIntervalMs", "m_flPresentCallCpuMs", "m_flWaitForPresentCpuMs",
            "m_flSubmitFrameMs", "m_flWaitGetPosesCalledMs", "m_flNewPosesReadyMs", "m_flNewFrameReadyMs", "m_flCompositorUpdateStartMs",
            "m_flCompositorUpdateEndMs", "m_flCompositorRenderStartMs", "m_HmdPose")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : Compositor_FrameTiming(), Structure.ByReference
    class ByValue : Compositor_FrameTiming(), Structure.ByValue
}

/** Cumulative stats for current application.  These are not cleared until a new app connects, but they do stop accumulating once the associated app disconnects. */
open class Compositor_CumulativeStats : Structure {

    var m_nPid = 0 // Process id associated with these stats (may no longer be running).
    var m_nNumFramePresents = 0 // total number of times we called present (includes reprojected frames)
    var m_nNumDroppedFrames = 0 // total number of times an old frame was re-scanned out (without reprojection)
    var m_nNumReprojectedFrames = 0 // total number of times a frame was scanned out a second time (with reprojection)

    /** Values recorded at startup before application has fully faded in the first time. */
    var m_nNumFramePresentsOnStartup = 0
    var m_nNumDroppedFramesOnStartup = 0
    var m_nNumReprojectedFramesOnStartup = 0

    /** Applications may explicitly fade to the compositor.  This is usually to handle level transitions, and loading often causes
     * system wide hitches.  The following stats are collected during this period.  Does not include values recorded during startup. */
    var m_nNumLoading = 0
    var m_nNumFramePresentsLoading = 0
    var m_nNumDroppedFramesLoading = 0
    var m_nNumReprojectedFramesLoading = 0

    /** If we don't get a new frame from the app in less than 2.5 frames, then we assume the app has hung and start fading back to the compositor.
     *  The following stats are a result of this, and are a subset of those recorded above.
     *  Does not include values recorded during start up or loading. */
    var m_nNumTimedOut = 0
    var m_nNumFramePresentsTimedOut = 0
    var m_nNumDroppedFramesTimedOut = 0
    var m_nNumReprojectedFramesTimedOut = 0

    constructor()

    constructor(m_nPid: Int, m_nNumFramePresents: Int, m_nNumDroppedFrames: Int, m_nNumReprojectedFrames: Int, m_nNumFramePresentsOnStartup: Int,
                m_nNumDroppedFramesOnStartup: Int, m_nNumReprojectedFramesOnStartup: Int, m_nNumLoading: Int, m_nNumFramePresentsLoading: Int,
                m_nNumDroppedFramesLoading: Int, m_nNumReprojectedFramesLoading: Int, m_nNumTimedOut: Int, m_nNumFramePresentsTimedOut: Int,
                m_nNumDroppedFramesTimedOut: Int, m_nNumReprojectedFramesTimedOut: Int) {
        this.m_nPid = m_nPid
        this.m_nNumFramePresents = m_nNumFramePresents
        this.m_nNumDroppedFrames = m_nNumDroppedFrames
        this.m_nNumReprojectedFrames = m_nNumReprojectedFrames
        this.m_nNumFramePresentsOnStartup = m_nNumFramePresentsOnStartup
        this.m_nNumDroppedFramesOnStartup = m_nNumDroppedFramesOnStartup
        this.m_nNumReprojectedFramesOnStartup = m_nNumReprojectedFramesOnStartup
        this.m_nNumLoading = m_nNumLoading
        this.m_nNumFramePresentsLoading = m_nNumFramePresentsLoading
        this.m_nNumDroppedFramesLoading = m_nNumDroppedFramesLoading
        this.m_nNumReprojectedFramesLoading = m_nNumReprojectedFramesLoading
        this.m_nNumTimedOut = m_nNumTimedOut
        this.m_nNumFramePresentsTimedOut = m_nNumFramePresentsTimedOut
        this.m_nNumDroppedFramesTimedOut = m_nNumDroppedFramesTimedOut
        this.m_nNumReprojectedFramesTimedOut = m_nNumReprojectedFramesTimedOut
    }

    override fun getFieldOrder(): List<*> = Arrays.asList("m_nPid", "m_nNumFramePresents", "m_nNumDroppedFrames", "m_nNumReprojectedFrames",
            "m_nNumFramePresentsOnStartup", "m_nNumDroppedFramesOnStartup", "m_nNumReprojectedFramesOnStartup", "m_nNumLoading", "m_nNumFramePresentsLoading",
            "m_nNumDroppedFramesLoading", "m_nNumReprojectedFramesLoading", "m_nNumTimedOut", "m_nNumFramePresentsTimedOut", "m_nNumDroppedFramesTimedOut",
            "m_nNumReprojectedFramesTimedOut")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : Compositor_CumulativeStats(), Structure.ByReference
    class ByValue : Compositor_CumulativeStats(), Structure.ByValue
}

/** Allows the application to interact with the compositor */
open class IVRCompositor : Structure {

    /** Sets tracking space returned by WaitGetPoses */
    @JvmField var SetTrackingSpace: SetTrackingSpace_callback? = null

    interface SetTrackingSpace_callback : Callback {
        fun apply(eOrigin: Int)
    }

    /** Gets current tracking space returned by WaitGetPoses */
    @JvmField var GetTrackingSpace: GetTrackingSpace_callback? = null

    interface GetTrackingSpace_callback : Callback {
        fun apply(): Int
    }

    /** Returns pose(s) to use to render scene (and optionally poses predicted two frames out for gameplay). */
    @JvmField var WaitGetPoses: WaitGetPoses_callback? = null

    interface WaitGetPoses_callback : Callback {
        fun apply(pRenderPoseArray: IntByReference, unRenderPoseArrayCount: Int, pGamePoseArray: TrackedDevicePose_t.ByReference, unGamePoseArrayCount: Int): Int
    }

    /** Get the last set of poses returned by WaitGetPoses. */
    @JvmField var GetLastPoses: GetLastPoses_callback? = null

    interface GetLastPoses_callback : Callback {
        fun apply(pRenderPoseArray: TrackedDevicePose_t.ByReference, unRenderPoseArrayCount: Int, pGamePoseArray: TrackedDevicePose_t.ByReference,
                  unGamePoseArrayCount: Int): Int
    }

    /** Interface for accessing last set of poses returned by WaitGetPoses one at a time.
     *  Returns VRCompositorError_IndexOutOfRange if unDeviceIndex not less than main.k_unMaxTrackedDeviceCount otherwise VRCompositorError_None.
     *  It is okay to pass NULL for either pose if you only want one of the values. */
    @JvmField var GetLastPoseForTrackedDeviceIndex: GetLastPoseForTrackedDeviceIndex_callback? = null

    interface GetLastPoseForTrackedDeviceIndex_callback : Callback {
        fun apply(unDeviceIndex: TrackedDeviceIndex_t, pOutputPose: TrackedDevicePose_t.ByReference, pOutputGamePose: TrackedDevicePose_t.ByReference): Int
    }

    /** Updated scene texture to display. If pBounds is NULL the entire texture will be used.  If called from an OpenGL app, consider adding a glFlush after
     *  Submitting both frames to signal the driver to start processing, otherwise it may wait until the command buffer fills up, causing the app to miss frames.
     *
     *  OpenGL dirty state:
     *	glBindTexture     */
    @JvmField var Submit: Submit_callback? = null

    interface Submit_callback : Callback {
        fun apply(eEye: Int, pTexture: Texture_t.ByReference, pBounds: VRTextureBounds_t.ByReference? = null,
                  nSubmitFlags: Int = EVRSubmitFlags.Submit_Default.i): Int
    }

    /** Clears the frame that was sent with the last call to Submit. This will cause the compositor to show the grid until Submit is called again. */
    @JvmField var ClearLastSubmittedFrame: ClearLastSubmittedFrame_callback? = null

    interface ClearLastSubmittedFrame_callback : Callback {
        fun apply()
    }

    /** Call immediately after presenting your app's window (i.e. companion window) to unblock the compositor.
     *  This is an optional call, which only needs to be used if you can't instead call WaitGetPoses immediately after Present.
     *  For example, if your engine's render and game loop are not on separate threads, or blocking the render thread until 3ms before the next vsync would
     *  introduce a deadlock of some sort.  This function tells the compositor that you have finished all rendering after having Submitted buffers for both
     *  eyes, and it is free to start its rendering work.  This should only be called from the same thread you are rendering on. */
    @JvmField var PostPresentHandoff: PostPresentHandoff_callback? = null

    interface PostPresentHandoff_callback : Callback {
        fun apply()
    }

    /** Returns true if timing data is filled it.  Sets oldest timing info if nFramesAgo is larger than the stored history.
     *  Be sure to set timing.size = sizeof(main.Compositor_FrameTiming) on struct passed in before calling this function. */
    @JvmField var GetFrameTiming: GetFrameTiming_callback? = null

    interface GetFrameTiming_callback : Callback {
        fun apply(pTiming: Compositor_FrameTiming.ByReference, unFramesAgo: Int = 0): Byte
    }

    /** Returns the time in seconds left in the current (as identified by FrameTiming's frameIndex) frame.
     *  Due to "running start", this value may roll over to the next frame before ever reaching 0.0. */
    @JvmField var GetFrameTimeRemaining: GetFrameTimeRemaining_callback? = null

    interface GetFrameTimeRemaining_callback : Callback {
        fun apply(): Float
    }

    /** Fills out stats accumulated for the last connected application.  Pass in sizeof( main.Compositor_CumulativeStats ) as second parameter. */
    @JvmField var GetCumulativeStats: GetCumulativeStats_callback? = null

    interface GetCumulativeStats_callback : Callback {
        fun apply(pStats: Compositor_CumulativeStats.ByReference, nStatsSizeInBytes: Int)
    }

    /** Fades the view on the HMD to the specified color. The fade will take fSeconds, and the color values are between 0.0 and 1.0. This color is faded on top
     *  of the scene based on the alpha parameter. Removing the fade color instantly would be FadeToColor( 0.0, 0.0, 0.0, 0.0, 0.0 ).
     *  Values are in un-premultiplied alpha space. */
    @JvmField var FadeToColor: FadeToColor_callback? = null

    interface FadeToColor_callback : Callback {
        fun apply(fSeconds: Float, fRed: Float, fGreen: Float, fBlue: Float, fAlpha: Float, bBackground: Byte = 0)
    }

    /** Fading the Grid in or out in fSeconds */
    @JvmField var FadeGrid: FadeGrid_callback? = null

    interface FadeGrid_callback : Callback {
        fun apply(fSeconds: Float, bFadeIn: Byte)
    }

    /** Override the skybox used in the compositor (e.g. for during level loads when the app can't feed scene images fast enough)
     *  Order is Front, Back, Left, Right, Top, Bottom.  If only a single texture is passed, it is assumed in lat-long format.
     *  If two are passed, it is assumed a lat-long stereo pair. */
    @JvmField var SetSkyboxOverride: SetSkyboxOverride_callback? = null

    interface SetSkyboxOverride_callback : Callback {
        fun apply(pTextures: Texture_t.ByReference, unTextureCount: Int): Int
    }

    /** Resets compositor skybox back to defaults. */
    @JvmField var ClearSkyboxOverride: ClearSkyboxOverride_callback? = null

    interface ClearSkyboxOverride_callback : Callback {
        fun apply()
    }

    /** Brings the compositor window to the front. This is useful for covering any other window that may be on the HMD and is obscuring the compositor window. */
    @JvmField var CompositorBringToFront: CompositorBringToFront_callback? = null

    interface CompositorBringToFront_callback : Callback {
        fun apply()
    }

    /** Pushes the compositor window to the back. This is useful for allowing other applications to draw directly to the HMD. */
    @JvmField var CompositorGoToBack: CompositorGoToBack_callback? = null

    interface CompositorGoToBack_callback : Callback {
        fun apply()
    }

    /** Tells the compositor process to clean up and exit. You do not need to call this function at shutdown. Under normal circumstances the compositor will
     *  manage its own life cycle based on what applications are running. */
    @JvmField var CompositorQuit: CompositorQuit_callback? = null

    interface CompositorQuit_callback : Callback {
        fun apply()
    }

    /** Return whether the compositor is fullscreen */
    @JvmField var IsFullscreen: IsFullscreen_callback? = null

    interface IsFullscreen_callback : Callback {
        fun apply(): Byte
    }

    /** Returns the process ID of the process that is currently rendering the scene */
    @JvmField var GetCurrentSceneFocusProcess: GetCurrentSceneFocusProcess_callback? = null

    interface GetCurrentSceneFocusProcess_callback : Callback {
        fun apply(): Int
    }

    /** Returns the process ID of the process that rendered the last frame (or 0 if the compositor itself rendered the frame.)
     *  Returns 0 when fading out from an app and the app's process Id when fading into an app. */
    @JvmField var GetLastFrameRenderer: GetLastFrameRenderer_callback? = null

    interface GetLastFrameRenderer_callback : Callback {
        fun apply(): Int
    }

    /** Returns true if the current process has the scene focus */
    @JvmField var CanRenderScene: CanRenderScene_callback? = null

    interface CanRenderScene_callback : Callback {
        fun apply(): Byte
    }

    /** Creates a window on the primary monitor to display what is being shown in the headset. */
    @JvmField var ShowMirrorWindow: ShowMirrorWindow_callback? = null

    interface ShowMirrorWindow_callback : Callback {
        fun apply()
    }

    /** Closes the mirror window. */
    @JvmField var HideMirrorWindow: HideMirrorWindow_callback? = null

    interface HideMirrorWindow_callback : Callback {
        fun apply()
    }

    /** Returns true if the mirror window is shown. */
    @JvmField var IsMirrorWindowVisible: IsMirrorWindowVisible_callback? = null

    interface IsMirrorWindowVisible_callback : Callback {
        fun apply(): Byte
    }

    /** Writes all images that the compositor knows about (including overlays) to a 'screenshots' folder in the SteamVR runtime root. */
    @JvmField var CompositorDumpImages: CompositorDumpImages_callback? = null

    interface CompositorDumpImages_callback : Callback {
        fun apply()
    }

    /** Let an app know it should be rendering with low resources. */
    @JvmField var ShouldAppRenderWithLowResources: ShouldAppRenderWithLowResources_callback? = null

    interface ShouldAppRenderWithLowResources_callback : Callback {
        fun apply(): Byte
    }

    /** Override interleaved reprojection logic to force on. */
    @JvmField var ForceInterleavedReprojectionOn: ForceInterleavedReprojectionOn_callback? = null

    interface ForceInterleavedReprojectionOn_callback : Callback {
        fun apply(bOverride: Byte)
    }

    /** Force reconnecting to the compositor process. */
    @JvmField var ForceReconnectProcess: ForceReconnectProcess_callback? = null

    interface ForceReconnectProcess_callback : Callback {
        fun apply()
    }

    /** Temporarily suspends rendering (useful for finer control over scene transitions). */
    @JvmField var SuspendRendering: SuspendRendering_callback? = null

    interface SuspendRendering_callback : Callback {
        fun apply(bSuspend: Byte)
    }

    /** Opens a shared D3D11 texture with the undistorted composited image for each eye. */
    @JvmField var GetMirrorTextureD3D11: GetMirrorTextureD3D11_callback? = null

    interface GetMirrorTextureD3D11_callback : Callback {
        fun apply(eEye: Int, pD3D11DeviceOrResource: Pointer, ppD3D11ShaderResourceView: PointerByReference): Int
    }

    /** Access to mirror textures from OpenGL. */
    @JvmField var GetMirrorTextureGL: GetMirrorTextureGL_callback? = null

    interface GetMirrorTextureGL_callback : Callback {
        fun apply(eEye: Int, pglTextureId: glUInt_t_ByReference, pglSharedTextureHandle: glSharedTextureHandle_t_ByReference): Int
    }

    @JvmField var ReleaseSharedGLTexture: ReleaseSharedGLTexture_callback? = null

    interface ReleaseSharedGLTexture_callback : Callback {
        fun apply(glTextureId: glUInt_t, glSharedTextureHandle: glSharedTextureHandle_t): Byte
    }

    @JvmField var LockGLSharedTextureForAccess: LockGLSharedTextureForAccess_callback? = null

    interface LockGLSharedTextureForAccess_callback : Callback {
        fun apply(glSharedTextureHandle: glSharedTextureHandle_t)
    }

    @JvmField var UnlockGLSharedTextureForAccess: UnlockGLSharedTextureForAccess_callback? = null

    interface UnlockGLSharedTextureForAccess_callback : Callback {
        fun apply(glSharedTextureHandle: glSharedTextureHandle_t)
    }

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("SetTrackingSpace", "GetTrackingSpace", "WaitGetPoses", "GetLastPoses",
            "GetLastPoseForTrackedDeviceIndex", "Submit", "ClearLastSubmittedFrame", "PostPresentHandoff", "GetFrameTiming", "GetFrameTimeRemaining",
            "GetCumulativeStats", "FadeToColor", "FadeGrid", "SetSkyboxOverride", "ClearSkyboxOverride", "CompositorBringToFront", "CompositorGoToBack",
            "CompositorQuit", "IsFullscreen", "GetCurrentSceneFocusProcess", "GetLastFrameRenderer", "CanRenderScene", "ShowMirrorWindow", "HideMirrorWindow",
            "IsMirrorWindowVisible", "CompositorDumpImages", "ShouldAppRenderWithLowResources", "ForceInterleavedReprojectionOn", "ForceReconnectProcess",
            "SuspendRendering", "GetMirrorTextureD3D1", "GetMirrorTextureGL", "ReleaseSharedGLTexture", "LockGLSharedTextureForAccess",
            "UnlockGLSharedTextureForAccess")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRCompositor(), Structure.ByReference
    class ByValue : IVRCompositor(), Structure.ByValue
}

const val IVRCompositor_Version = "FnTable:IVRCompositor_016"

// ivrnotifications.h ==================================================================================================

// Used for passing graphic data
open class NotificationBitmap_t : Structure {

    var m_pImageData: Pointer? = null
    var m_nWidth = 0
    var m_nHeight = 0
    var m_nBytesPerPixel = 0

    constructor()

    constructor(m_pImageData: Pointer?, m_nWidth: Int, m_nHeight: Int, m_nBytesPerPixel: Int) {
        this.m_pImageData = m_pImageData
        this.m_nWidth = m_nWidth
        this.m_nHeight = m_nHeight
        this.m_nBytesPerPixel = m_nBytesPerPixel
    }

    override fun getFieldOrder(): List<*> = Arrays.asList("m_pImageData", "m_nWidth", "m_nHeight", "m_nBytesPerPixel")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : NotificationBitmap_t(), Structure.ByReference
    class ByValue : NotificationBitmap_t(), Structure.ByValue
}

/** Be aware that the notification type is used as 'priority' to pick the next notification */
enum class EVRNotificationType(val i: Int) {

    /** Transient notifications are automatically hidden after a period of time set by the user.
     *  They are used for things like information and chat messages that do not require user interaction. */
    EVRNotificationType_Transient(0),

    /** Persistent notifications are shown to the user until they are hidden by calling RemoveNotification().
     *  They are used for things like phone calls and alarms that require user interaction. */
    EVRNotificationType_Persistent(1),

    /** System notifications are shown no matter what. It is expected), that the ulUserValue is used as ID.
     *  If there is already a system notification in the queue with that ID it is not accepted into the queue to prevent spamming with system notification */
    EVRNotificationType_Transient_SystemWithUserValue(2)
}

enum class EVRNotificationStyle(val i: Int) {

    /** Creates a notification with minimal external styling. */
    EVRNotificationStyle_None(0),

    /** Used for notifications about overlay-level status. In Steam this is used for events like downloads completing. */
    EVRNotificationStyle_Application(100),

    /** Used for notifications about contacts that are unknown or not available. In Steam this is used for friend invitations and offline friends. */
    EVRNotificationStyle_Contact_Disabled(200),

    /** Used for notifications about contacts that are available but inactive. In Steam this is used for friends that are online but not playing a game. */
    EVRNotificationStyle_Contact_Enabled(201),

    /** Used for notifications about contacts that are available and active. In Steam this is used for friends that are online and currently running a game. */
    EVRNotificationStyle_Contact_Active(202),
}

const val k_unNotificationTextMaxSize = 256

typealias VRNotificationId = Int
typealias VRNotificationId_ByReference = IntByReference

/** Allows notification sources to interact with the VR system
This current interface is not yet implemented. Do not use yet. */
open class IVRNotifications : Structure {

    /** Create a notification and enqueue it to be shown to the user.
     *  An overlay handle is required to create a notification, as otherwise it would be impossible for a user to act on it.
     *  To create a two-line notification, use a line break ('\n') to split the text into two lines.
     *  The pImage argument may be NULL, in which case the specified overlay's icon will be used instead. */
    @JvmField var CreateNotification: CreateNotification_callback? = null

    interface CreateNotification_callback : Callback {
        fun apply(ulOverlayHandle: VROverlayHandle_t, ulUserValue: Long, type: Int, pchText: String, style: Int, pImage: NotificationBitmap_t.ByReference,
                /* out */ pNotificationId: VRNotificationId_ByReference): Int
    }

    /** Destroy a notification, hiding it first if it currently shown to the user. */
    @JvmField var RemoveNotification: RemoveNotification_callback? = null

    interface RemoveNotification_callback : Callback {
        fun apply(notificationId: VRNotificationId): Int
    }

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("CreateNotification", "RemoveNotification")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRNotifications(), Structure.ByReference
    class ByValue : IVRNotifications(), Structure.ByValue
}

const val IVRNotifications_Version = "FnTable:IVRNotifications_002"

// ivroverlay.h ========================================================================================================

/** The maximum length of an overlay key in bytes, counting the terminating null character. */
const val k_unVROverlayMaxKeyLength = 128

/** The maximum length of an overlay name in bytes, counting the terminating null character. */
const val k_unVROverlayMaxNameLength = 128

/** The maximum number of overlays that can exist in the system at one time. */
const val k_unMaxOverlayCount = 64

/** Types of input supported by VR Overlays */
enum class VROverlayInputMethod(val i: Int) {

    VROverlayInputMethod_None(0), //    No input events will be generated automatically for this overlay
    VROverlayInputMethod_Mouse(1), //   Tracked controllers will get mouse events automatically
}

/** Allows the caller to figure out which overlay transform getter to call. */
enum class VROverlayTransformType(val i: Int) {

    VROverlayTransform_Absolute(0),
    VROverlayTransform_TrackedDeviceRelative(1),
    VROverlayTransform_SystemOverlay(2),
    VROverlayTransform_TrackedComponent(3),
}

/** Overlay control settings */
enum class VROverlayFlags(val i: Int) {

    VROverlayFlags_None(0),

    // The following only take effect when rendered using the high quality render path (see SetHighQualityOverlay).
    VROverlayFlags_Curved(1),
    VROverlayFlags_RGSS4X(2),

    // Set this flag on a dashboard overlay to prevent a tab from showing up for that overlay
    VROverlayFlags_NoDashboardTab(3),

    // Set this flag on a dashboard that is able to deal with gamepad focus events
    VROverlayFlags_AcceptsGamepadEvents(4),

    // Indicates that the overlay should dim/brighten to show gamepad focus
    VROverlayFlags_ShowGamepadFocus(5),

    // When in VROverlayInputMethod_Mouse you can optionally enable sending VRScroll_t 
    VROverlayFlags_SendVRScrollEvents(6),
    VROverlayFlags_SendVRTouchpadEvents(7),

    // If set this will render a vertical scroll wheel on the primary controller), only needed if not using VROverlayFlags_SendVRScrollEvents but you still want
    // to represent a scroll wheel
    VROverlayFlags_ShowTouchPadScrollWheel(8),

    // If this is set ownership and render access to the overlay are transferred to the new scene process on a call to main.IVRApplications::LaunchInternalProcess
    VROverlayFlags_TransferOwnershipToInternalProcess(9),

    // If set), renders 50% of the texture in each eye), side by side
    VROverlayFlags_SideBySide_Parallel(10), // Texture is left/right
    VROverlayFlags_SideBySide_Crossed(11), // Texture is crossed and right/left

    VROverlayFlags_Panorama(12), // Texture is a panorama
    VROverlayFlags_StereoPanorama(13), // Texture is a stereo panorama

    // If this is set on an overlay owned by the scene application that overlay will be sorted with the "Other" overlays on top of all other scene overlays
    VROverlayFlags_SortWithNonSceneOverlays(14),
}

open class VROverlayIntersectionParams_t : Structure {

    var vSource = HmdVector3_t()
    var vDirection = HmdVector3_t()
    var eOrigin = ETrackingUniverseOrigin.TrackingUniverseSeated.i

    constructor()

    constructor(vSource: HmdVector3_t, vDirection: HmdVector3_t, eOrigin: Int) {
        this.vSource = vSource
        this.vDirection = vDirection
        this.eOrigin = eOrigin
    }

    override fun getFieldOrder(): List<*> = Arrays.asList("vPoint", "vNormal", "eOrigin")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VROverlayIntersectionParams_t(), Structure.ByReference
    class ByValue : VROverlayIntersectionParams_t(), Structure.ByValue
}

open class VROverlayIntersectionResults_t : Structure {

    var vPoint = HmdVector3_t()
    var vNormal = HmdVector3_t()
    var vUVs = HmdVector2_t()
    var fDistance = 0f

    constructor()

    constructor(vPoint: HmdVector3_t, vNormal: HmdVector3_t, vUVs: HmdVector2_t, fDistance: Float) {
        this.vPoint = vPoint
        this.vNormal = vNormal
        this.vUVs = vUVs
        this.fDistance = fDistance
    }

    override fun getFieldOrder(): List<*> = Arrays.asList("vPoint", "vNormal", "vUVs", "fDistance")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VROverlayIntersectionResults_t(), Structure.ByReference
    class ByValue : VROverlayIntersectionResults_t(), Structure.ByValue
}

// Input modes for the Big Picture gamepad text entry
enum class EGamepadTextInputMode(val i: Int) {

    k_EGamepadTextInputModeNormal(0),
    k_EGamepadTextInputModePassword(1),
    k_EGamepadTextInputModeSubmit(2)
}

// Controls number of allowed lines for the Big Picture gamepad text entry
enum class EGamepadTextInputLineMode(val i: Int) {

    k_EGamepadTextInputLineModeSingleLine(0),
    k_EGamepadTextInputLineModeMultipleLines(1)
}

/** Directions for changing focus between overlays with the gamepad */
enum class EOverlayDirection(val i: Int) {

    OverlayDirection_Up(0),
    OverlayDirection_Down(1),
    OverlayDirection_Left(2),
    OverlayDirection_Right(3),

    OverlayDirection_Count(4)
}

//open class IVROverlay : Structure {
//
//    // ---------------------------------------------
//    // Overlay management methods
//    // ---------------------------------------------
//
//    /** Finds an existing overlay with the specified key. */
//    @JvmField var FindOverlay: IVROverlay.FindOverlay_callback? = null
//
//    interface FindOverlay_callback : Callback {
//        fun apply(pchOverlayKey: String, pOverlayHandle: VROverlayHandle_t_ByReference): Int
//    }
//
//    /** Creates a new named overlay. All overlays start hidden and with default settings. */
//    @JvmField var CreateOverlay: IVROverlay.CreateOverlay_callback? = null
//
//    interface CreateOverlay_callback : Callback {
//        fun apply(pchOverlayKey: String, pchOverlayFriendlyName: String, pOverlayHandle: VROverlayHandle_t_ByReference): Int
//    }
//
//    /** Destroys the specified overlay. When an application calls VR_Shutdown all overlays created by that app are automatically destroyed. */
//    @JvmField var DestroyOverlay: IVROverlay.DestroyOverlay_callback? = null
//
//    interface DestroyOverlay_callback : Callback {
//        fun apply(ulOverlayHandle: VROverlayHandle_t): Int
//    }
//
//    /** Specify which overlay to use the high quality render path.  This overlay will be composited in during the distortion pass which results in it drawing on
//     *  top of everything else, but also at a higher quality as it samples the source texture directly rather than rasterizing into each eye's render texture
//     *  first.  Because if this, only one of these is supported at any given time.  It is most useful for overlays that are expected to take up most of the
//     *  user's view (e.g. streaming video).
//     *  This mode does not support mouse input to your overlay. */
//    @JvmField var SetHighQualityOverlay: IVROverlay.SetHighQualityOverlay_callback? = null
//
//    interface SetHighQualityOverlay_callback : Callback {
//        fun apply(ulOverlayHandle: VROverlayHandle_t): Int
//    }
//
//    /** Returns the overlay handle of the current overlay being rendered using the single high quality overlay render path.
//     *  Otherwise it will return main.k_ulOverlayHandleInvalid. */
//    @JvmField var GetHighQualityOverlay: IVROverlay.GetHighQualityOverlay_callback? = null
//
//    interface GetHighQualityOverlay_callback : Callback {
//        fun apply(): VROverlayHandle_t
//    }
//
//    /** Fills the provided buffer with the string key of the overlay. Returns the size of buffer required to store the key, including the terminating null
//     *  character. main.k_unVROverlayMaxKeyLength will be enough bytes to fit the string. */
//    @JvmField var GetOverlayKey: IVROverlay.GetOverlayKey_callback? = null
//
//    interface GetOverlayKey_callback : Callback {
//        fun apply(ulOverlayHandle: VROverlayHandle_t, pchValue: String, unBufferSize: Int, pError: IntByReference = IntByReference(0)): Int
//    }
//
//    /** Fills the provided buffer with the friendly name of the overlay. Returns the size of buffer required to store the key, including the terminating null
//     *  character. main.k_unVROverlayMaxNameLength will be enough bytes to fit the string. */
//    @JvmField var GetOverlayName: IVROverlay.GetOverlayName_callback? = null
//
//    interface GetOverlayName_callback : Callback {
//        fun apply(ulOverlayHandle: VROverlayHandle_t, pchValue: String, unBufferSize: Int, pError: IntByReference = IntByReference(0)): Int
//    }
//
//    /** Gets the raw image data from an overlay. Overlay image data is always returned as RGBA data, 4 bytes per pixel. If the buffer is not large enough,
//     *  width and height will be set and VROverlayError_ArrayTooSmall is returned. */
//    @JvmField var GetOverlayImageData: IVROverlay.GetOverlayImageData_callback? = null
//
//    interface GetOverlayImageData_callback : Callback {
//        fun apply(ulOverlayHandle: VROverlayHandle_t, pvBuffer: Pointer, unBufferSize: Int, punWidth: IntByReference, punHeight: IntByReference): Int
//    }
//
//    /** returns a string that corresponds with the specified overlay error. The string will be the name of the error enum value for all valid error codes */
//    @JvmField var GetOverlayErrorNameFromEnum: IVROverlay.GetOverlayErrorNameFromEnum_callback? = null
//
//    interface GetOverlayErrorNameFromEnum_callback : Callback {
//        fun apply(error: Int): String
//    }
//
//
//    // ---------------------------------------------
//    // Overlay rendering methods
//    // ---------------------------------------------
//
//    /** Sets the pid that is allowed to render to this overlay (the creator pid is always allow to render), by default this is the pid of the process that made
//     *  the overlay */
//    @JvmField var SetOverlayRenderingPid: IVROverlay.SetOverlayRenderingPid_callback? = null
//
//    interface SetOverlayRenderingPid_callback : Callback {
//        fun apply(ulOverlayHandle: VROverlayHandle_t, unPID: Int): Int
//    }
//
//    /** Gets the pid that is allowed to render to this overlay */
//    @JvmField var GetOverlayRenderingPid: IVROverlay.GetOverlayRenderingPid_callback? = null
//
//    interface GetOverlayRenderingPid_callback : Callback {
//        fun apply(ulOverlayHandle: VROverlayHandle_t): Int
//    }
//
//    /** Specify flag setting for a given overlay */
//    @JvmField var SetOverlayFlag: IVROverlay.SetOverlayFlag_callback? = null
//
//    interface SetOverlayFlag_callback : Callback {
//        fun apply(ulOverlayHandle: VROverlayHandle_t, eOverlayFlag: main.VROverlayFlags, bEnabled: Byte): Int
//    }
//
//    /** Sets flag setting for a given overlay */
//    @JvmField var GetOverlayFlag: IVROverlay.GetOverlayFlag_callback? = null
//
//    interface GetOverlayFlag_callback : Callback {
//        fun apply(ulOverlayHandle: VROverlayHandle_t, eOverlayFlag: main.VROverlayFlags, pbEnabled: ByteByReference): Int
//    }
//
//    /** Sets the color tint of the overlay quad. Use 0.0 to 1.0 per channel. */
//    @JvmField var SetOverlayColor: IVROverlay.SetOverlayColor_callback? = null
//
//    interface SetOverlayColor_callback : Callback {
//        fun apply(ulOverlayHandle: VROverlayHandle_t, fRed: Float, fGreen: Float, fBlue: Float): Int
//    }
//
//    /** Gets the color tint of the overlay quad. */
//    @JvmField var GetOverlayColor: IVROverlay.GetOverlayColor_callback? = null
//
//    interface GetOverlayColor_callback : Callback {
//        fun apply(ulOverlayHandle: VROverlayHandle_t, pfRed: FloatByReference, pfGreen: FloatByReference, pfBlue: FloatByReference): Int
//    }
//
//    /** Sets the alpha of the overlay quad. Use 1.0 for 100 percent opacity to 0.0 for 0 percent opacity. */
//    @JvmField var SetOverlayAlpha: IVROverlay.SetOverlayAlpha_callback? = null
//
//    interface SetOverlayAlpha_callback : Callback {
//        fun apply(ulOverlayHandle: VROverlayHandle_t, fAlpha: Float): Int
//    }
//
//    /** Gets the alpha of the overlay quad. By default overlays are rendering at 100 percent alpha (1.0). */
//    @JvmField var VROverlayHandle_t: IVROverlay.GetOverlayAlpha_callback? = null
//
//    interface GetOverlayAlpha_callback : Callback {
//        fun apply(ulOverlayHandle: VROverlayHandle_t, pfAlpha: FloatByReference): Int
//    }
//
//    /** Sets the aspect ratio of the texels in the overlay. 1.0 means the texels are square. 2.0 means the texels are twice as wide as they are tall.
//     *  Defaults to 1.0. */
//    interface SetOverlayTexelAspect_callback:Callback {
//        fun apply( ulOverlayHandle:VROverlayHandle_t, fTexelAspect :Float):Int
//    }
//
//    /** Gets the aspect ratio of the texels in the overlay. Defaults to 1.0 */
//    virtual main.EVROverlayError GetOverlayTexelAspect( VROverlayHandle_t ulOverlayHandle, float *pfTexelAspect ) = 0;
//
//    /** Sets the rendering sort order for the overlay. Overlays are rendered this order:
//     *      Overlays owned by the scene application
//     *      Overlays owned by some other application
//     *
//     *	Within a category overlays are rendered lowest sort order to highest sort order. Overlays with the same
//     *	sort order are rendered back to front base on distance from the HMD.
//     *
//     *	Sort order defaults to 0. */
//    virtual main.EVROverlayError SetOverlaySortOrder( VROverlayHandle_t ulOverlayHandle, uint32_t unSortOrder ) = 0;
//
//    /** Gets the sort order of the overlay. See SetOverlaySortOrder for how this works. */
//    virtual main.EVROverlayError GetOverlaySortOrder( VROverlayHandle_t ulOverlayHandle, uint32_t *punSortOrder ) = 0;
//
//    /** Sets the width of the overlay quad in meters. By default overlays are rendered on a quad that is 1 meter across */
//    virtual main.EVROverlayError SetOverlayWidthInMeters( VROverlayHandle_t ulOverlayHandle, float fWidthInMeters ) = 0;
//
//    /** Returns the width of the overlay quad in meters. By default overlays are rendered on a quad that is 1 meter across */
//    virtual main.EVROverlayError GetOverlayWidthInMeters( VROverlayHandle_t ulOverlayHandle, float *pfWidthInMeters ) = 0;
//
//    /** For high-quality curved overlays only, sets the distance range in meters from the overlay used to automatically curve
//     * the surface around the viewer.  Min is distance is when the surface will be most curved.  Max is when least curved. */
//    virtual main.EVROverlayError SetOverlayAutoCurveDistanceRangeInMeters( VROverlayHandle_t ulOverlayHandle, float fMinDistanceInMeters, float fMaxDistanceInMeters ) = 0;
//
//    /** For high-quality curved overlays only, gets the distance range in meters from the overlay used to automatically curve
//     * the surface around the viewer.  Min is distance is when the surface will be most curved.  Max is when least curved. */
//    virtual main.EVROverlayError GetOverlayAutoCurveDistanceRangeInMeters( VROverlayHandle_t ulOverlayHandle, float *pfMinDistanceInMeters, float *pfMaxDistanceInMeters ) = 0;
//
//    /** Sets the colorspace the overlay texture's data is in.  Defaults to 'auto'.
//     * If the texture needs to be resolved, you should call SetOverlayTexture with the appropriate colorspace instead. */
//    virtual main.EVROverlayError SetOverlayTextureColorSpace( VROverlayHandle_t ulOverlayHandle, main.EColorSpace eTextureColorSpace ) = 0;
//
//    /** Gets the overlay's current colorspace setting. */
//    virtual main.EVROverlayError GetOverlayTextureColorSpace( VROverlayHandle_t ulOverlayHandle, main.EColorSpace *peTextureColorSpace ) = 0;
//
//    /** Sets the part of the texture to use for the overlay. UV Min is the upper left corner and UV Max is the lower right corner. */
//    virtual main.EVROverlayError SetOverlayTextureBounds( VROverlayHandle_t ulOverlayHandle, const main.VRTextureBounds_t *pOverlayTextureBounds ) = 0;
//
//    /** Gets the part of the texture to use for the overlay. UV Min is the upper left corner and UV Max is the lower right corner. */
//    virtual main.EVROverlayError GetOverlayTextureBounds( VROverlayHandle_t ulOverlayHandle, main.VRTextureBounds_t *pOverlayTextureBounds ) = 0;
//
//    /** Returns the transform type of this overlay. */
//    virtual main.EVROverlayError GetOverlayTransformType( VROverlayHandle_t ulOverlayHandle, main.VROverlayTransformType *peTransformType ) = 0;
//
//    /** Sets the transform to absolute tracking origin. */
//    virtual main.EVROverlayError SetOverlayTransformAbsolute( VROverlayHandle_t ulOverlayHandle, main.ETrackingUniverseOrigin eTrackingOrigin, const main.HmdMatrix34_t *pmatTrackingOriginToOverlayTransform ) = 0;
//
//    /** Gets the transform if it is absolute. Returns an error if the transform is some other type. */
//    virtual main.EVROverlayError GetOverlayTransformAbsolute( VROverlayHandle_t ulOverlayHandle, main.ETrackingUniverseOrigin *peTrackingOrigin, main.HmdMatrix34_t *pmatTrackingOriginToOverlayTransform ) = 0;
//
//    /** Sets the transform to relative to the transform of the specified tracked device. */
//    virtual main.EVROverlayError SetOverlayTransformTrackedDeviceRelative( VROverlayHandle_t ulOverlayHandle, TrackedDeviceIndex_t unTrackedDevice, const main.HmdMatrix34_t *pmatTrackedDeviceToOverlayTransform ) = 0;
//
//    /** Gets the transform if it is relative to a tracked device. Returns an error if the transform is some other type. */
//    virtual main.EVROverlayError GetOverlayTransformTrackedDeviceRelative( VROverlayHandle_t ulOverlayHandle, TrackedDeviceIndex_t *punTrackedDevice, main.HmdMatrix34_t *pmatTrackedDeviceToOverlayTransform ) = 0;
//
//    /** Sets the transform to draw the overlay on a rendermodel component mesh instead of a quad. This will only draw when the system is
//     * drawing the device. Overlays with this transform type cannot receive mouse events. */
//    virtual main.EVROverlayError SetOverlayTransformTrackedDeviceComponent( VROverlayHandle_t ulOverlayHandle, TrackedDeviceIndex_t unDeviceIndex, const char *pchComponentName ) = 0;
//
//    /** Gets the transform information when the overlay is rendering on a component. */
//    virtual main.EVROverlayError GetOverlayTransformTrackedDeviceComponent( VROverlayHandle_t ulOverlayHandle, TrackedDeviceIndex_t *punDeviceIndex, char *pchComponentName, uint32_t unComponentNameSize ) = 0;
//
//    /** Shows the VR overlay.  For dashboard overlays, only the Dashboard Manager is allowed to call this. */
//    virtual main.EVROverlayError ShowOverlay( VROverlayHandle_t ulOverlayHandle ) = 0;
//
//    /** Hides the VR overlay.  For dashboard overlays, only the Dashboard Manager is allowed to call this. */
//    virtual main.EVROverlayError HideOverlay( VROverlayHandle_t ulOverlayHandle ) = 0;
//
//    /** Returns true if the overlay is visible. */
//    virtual bool IsOverlayVisible( VROverlayHandle_t ulOverlayHandle ) = 0;
//
//    /** Get the transform in 3d space associated with a specific 2d point in the overlay's coordinate space (where 0,0 is the lower left). -Z points out of the overlay */
//    virtual main.EVROverlayError GetTransformForOverlayCoordinates( VROverlayHandle_t ulOverlayHandle, main.ETrackingUniverseOrigin eTrackingOrigin, main.HmdVector2_t coordinatesInOverlay, main.HmdMatrix34_t *pmatTransform ) = 0;
//
//    // ---------------------------------------------
//    // Overlay input methods
//    // ---------------------------------------------
//
//    /** Returns true and fills the event with the next event on the overlay's event queue, if there is one.
//     * If there are no events this method returns false. uncbVREvent should be the size in bytes of the main.VREvent_t struct */
//    virtual bool PollNextOverlayEvent( VROverlayHandle_t ulOverlayHandle, main.VREvent_t *pEvent, uint32_t uncbVREvent ) = 0;
//
//    /** Returns the current input settings for the specified overlay. */
//    virtual main.EVROverlayError GetOverlayInputMethod( VROverlayHandle_t ulOverlayHandle, main.VROverlayInputMethod *peInputMethod ) = 0;
//
//    /** Sets the input settings for the specified overlay. */
//    virtual main.EVROverlayError SetOverlayInputMethod( VROverlayHandle_t ulOverlayHandle, main.VROverlayInputMethod eInputMethod ) = 0;
//
//    /** Gets the mouse scaling factor that is used for mouse events. The actual texture may be a different size, but this is
//     * typically the size of the underlying UI in pixels. */
//    virtual main.EVROverlayError GetOverlayMouseScale( VROverlayHandle_t ulOverlayHandle, main.HmdVector2_t *pvecMouseScale ) = 0;
//
//    /** Sets the mouse scaling factor that is used for mouse events. The actual texture may be a different size, but this is
//     * typically the size of the underlying UI in pixels (not in world space). */
//    virtual main.EVROverlayError SetOverlayMouseScale( VROverlayHandle_t ulOverlayHandle, const main.HmdVector2_t *pvecMouseScale ) = 0;
//
//    /** Computes the overlay-space pixel coordinates of where the ray intersects the overlay with the
//     * specified settings. Returns false if there is no intersection. */
//    virtual bool ComputeOverlayIntersection( VROverlayHandle_t ulOverlayHandle, const main.VROverlayIntersectionParams_t *pParams, main.VROverlayIntersectionResults_t *pResults ) = 0;
//
//    /** Processes mouse input from the specified controller as though it were a mouse pointed at a compositor overlay with the
//     * specified settings. The controller is treated like a laser pointer on the -z axis. The point where the laser pointer would
//     * intersect with the overlay is the mouse position, the trigger is left mouse, and the track pad is right mouse.
//     *
//     * Return true if the controller is pointed at the overlay and an event was generated. */
//    virtual bool HandleControllerOverlayInteractionAsMouse( VROverlayHandle_t ulOverlayHandle, TrackedDeviceIndex_t unControllerDeviceIndex ) = 0;
//
//    /** Returns true if the specified overlay is the hover target. An overlay is the hover target when it is the last overlay "moused over"
//     * by the virtual mouse pointer */
//    virtual bool IsHoverTargetOverlay( VROverlayHandle_t ulOverlayHandle ) = 0;
//
//    /** Returns the current Gamepad focus overlay */
//    virtual vr::VROverlayHandle_t GetGamepadFocusOverlay() = 0;
//
//    /** Sets the current Gamepad focus overlay */
//    virtual main.EVROverlayError SetGamepadFocusOverlay( VROverlayHandle_t ulNewFocusOverlay ) = 0;
//
//    /** Sets an overlay's neighbor. This will also set the neighbor of the "to" overlay
//     * to point back to the "from" overlay. If an overlay's neighbor is set to invalid both
//     * ends will be cleared */
//    virtual main.EVROverlayError SetOverlayNeighbor( main.EOverlayDirection eDirection, VROverlayHandle_t ulFrom, VROverlayHandle_t ulTo ) = 0;
//
//    /** Changes the Gamepad focus from one overlay to one of its neighbors. Returns VROverlayError_NoNeighbor if there is no
//     * neighbor in that direction */
//    virtual main.EVROverlayError MoveGamepadFocusToNeighbor( main.EOverlayDirection eDirection, VROverlayHandle_t ulFrom ) = 0;
//
//    // ---------------------------------------------
//    // Overlay texture methods
//    // ---------------------------------------------
//
//    /** Texture to draw for the overlay. This function can only be called by the overlay's creator or renderer process (see SetOverlayRenderingPid) .
//     *
//     * OpenGL dirty state:
//     *	glBindTexture
//     */
//    virtual main.EVROverlayError SetOverlayTexture( VROverlayHandle_t ulOverlayHandle, const main.Texture_t *pTexture ) = 0;
//
//    /** Use this to tell the overlay system to release the texture set for this overlay. */
//    virtual main.EVROverlayError ClearOverlayTexture( VROverlayHandle_t ulOverlayHandle ) = 0;
//
//    /** Separate interface for providing the data as a stream of bytes, but there is an upper bound on data
//     * that can be sent. This function can only be called by the overlay's renderer process. */
//    virtual main.EVROverlayError SetOverlayRaw( VROverlayHandle_t ulOverlayHandle, void *pvBuffer, uint32_t unWidth, uint32_t unHeight, uint32_t unDepth ) = 0;
//
//    /** Separate interface for providing the image through a filename: can be png or jpg, and should not be bigger than 1920x1080.
//     * This function can only be called by the overlay's renderer process */
//    virtual main.EVROverlayError SetOverlayFromFile( VROverlayHandle_t ulOverlayHandle, const char *pchFilePath ) = 0;
//
//    /** Get the native texture handle/device for an overlay you have created.
//     * On windows this handle will be a ID3D11ShaderResourceView with a ID3D11Texture2D bound.
//     *
//     * The texture will always be sized to match the backing texture you supplied in SetOverlayTexture above.
//     *
//     * You MUST call ReleaseNativeOverlayHandle() with pNativeTextureHandle once you are done with this texture.
//     *
//     * pNativeTextureHandle is an OUTPUT, it will be a pointer to a ID3D11ShaderResourceView *.
//     * pNativeTextureRef is an INPUT and should be a ID3D11Resource *. The device used by pNativeTextureRef will be used to bind pNativeTextureHandle.
//     */
//    virtual main.EVROverlayError GetOverlayTexture( VROverlayHandle_t ulOverlayHandle, void **pNativeTextureHandle, void *pNativeTextureRef, uint32_t *pWidth, uint32_t *pHeight, uint32_t *pNativeFormat, main.EGraphicsAPIConvention *pAPI, main.EColorSpace *pColorSpace ) = 0;
//
//    /** Release the pNativeTextureHandle provided from the GetOverlayTexture call, this allows the system to free the underlying GPU resources for this object,
//     * so only do it once you stop rendering this texture.
//     */
//    virtual main.EVROverlayError ReleaseNativeOverlayHandle( VROverlayHandle_t ulOverlayHandle, void *pNativeTextureHandle ) = 0;
//
//    /** Get the size of the overlay texture */
//    virtual main.EVROverlayError GetOverlayTextureSize( VROverlayHandle_t ulOverlayHandle, uint32_t *pWidth, uint32_t *pHeight ) = 0;
//
//    // ----------------------------------------------
//    // Dashboard Overlay Methods
//    // ----------------------------------------------
//
//    /** Creates a dashboard overlay and returns its handle */
//    virtual main.EVROverlayError CreateDashboardOverlay( const char *pchOverlayKey, const char *pchOverlayFriendlyName, VROverlayHandle_t * pMainHandle, VROverlayHandle_t *pThumbnailHandle ) = 0;
//
//    /** Returns true if the dashboard is visible */
//    virtual bool IsDashboardVisible() = 0;
//
//    /** returns true if the dashboard is visible and the specified overlay is the active system Overlay */
//    virtual bool IsActiveDashboardOverlay( VROverlayHandle_t ulOverlayHandle ) = 0;
//
//    /** Sets the dashboard overlay to only appear when the specified process ID has scene focus */
//    virtual main.EVROverlayError SetDashboardOverlaySceneProcess( VROverlayHandle_t ulOverlayHandle, uint32_t unProcessId ) = 0;
//
//    /** Gets the process ID that this dashboard overlay requires to have scene focus */
//    virtual main.EVROverlayError GetDashboardOverlaySceneProcess( VROverlayHandle_t ulOverlayHandle, uint32_t *punProcessId ) = 0;
//
//    /** Shows the dashboard. */
//    virtual void ShowDashboard( const char *pchOverlayToShow ) = 0;
//
//    /** Returns the tracked device that has the laser pointer in the dashboard */
//    virtual vr::TrackedDeviceIndex_t GetPrimaryDashboardDevice() = 0;
//
//    // ---------------------------------------------
//    // Keyboard methods
//    // ---------------------------------------------
//
//    /** Show the virtual keyboard to accept input **/
//    virtual main.EVROverlayError ShowKeyboard( main.EGamepadTextInputMode eInputMode, main.EGamepadTextInputLineMode eLineInputMode, const char *pchDescription, uint32_t unCharMax, const char *pchExistingText, bool bUseMinimalMode, uint64_t uUserValue ) = 0;
//
//    virtual main.EVROverlayError ShowKeyboardForOverlay( VROverlayHandle_t ulOverlayHandle, main.EGamepadTextInputMode eInputMode, main.EGamepadTextInputLineMode eLineInputMode, const char *pchDescription, uint32_t unCharMax, const char *pchExistingText, bool bUseMinimalMode, uint64_t uUserValue ) = 0;
//
//    /** Get the text that was entered into the text input **/
//    virtual uint32_t GetKeyboardText( VR_OUT_STRING() char *pchText, uint32_t cchText ) = 0;
//
//    /** Hide the virtual keyboard **/
//    virtual void HideKeyboard() = 0;
//
//    /** Set the position of the keyboard in world space **/
//    virtual void SetKeyboardTransformAbsolute( main.ETrackingUniverseOrigin eTrackingOrigin, const main.HmdMatrix34_t *pmatTrackingOriginToKeyboardTransform ) = 0;
//
//    /** Set the position of the keyboard in overlay space by telling it to avoid a rectangle in the overlay. Rectangle coords have (0,0) in the bottom left **/
//    virtual void SetKeyboardPositionForOverlay( VROverlayHandle_t ulOverlayHandle, main.HmdRect2_t avoidRect ) = 0;
//
//    constructor()
//
//    constructor(vPoint: main.HmdVector3_t, vNormal: main.HmdVector3_t, vUVs: main.HmdVector2_t, fDistance: Float) {
//        this.vPoint = vPoint
//        this.vNormal = vNormal
//        this.vUVs = vUVs
//        this.fDistance = fDistance
//    }
//
//    override fun getFieldOrder(): List<*> = Arrays.asList("vPoint", "vNormal", "vUVs", "fDistance")
//
//    constructor(peer: Pointer) : super(peer) {
//        read()
//    }
//
//    class ByReference : IVROverlay(), Structure.ByReference
//    class ByValue : IVROverlay(), Structure.ByValue
//}

//------------------------------------------------------------------------------------------------------------

external fun VR_GetGenericInterface(pchInterfaceVersion: String, peError: IntByReference): Pointer
external fun VR_IsInterfaceVersionValid(pchInterfaceVersion: String): Byte

external fun VR_InitInternal(peError: IntByReference, eType: Int): Pointer
external fun VR_ShutdownInternal()

fun VR_Init(error: IntByReference, applicationType: Int): IVRSystem {

    var vrSystem: IVRSystem? = null

    VR_InitInternal(error, applicationType)
//    val ctx = COpenVRContext()
//    ctx.clear()

    if (error.value == EVRInitError.VRInitError_None.i) {

        if (VR_IsInterfaceVersionValid(IVRSystem_Version) !== 0.toByte()) {

            vrSystem = IVRSystem(VR_GetGenericInterface(IVRSystem_Version, error))

        } else {

            VR_ShutdownInternal()
            error.value = EVRInitError.VRInitError_Init_InterfaceNotFound.i
        }
    }
    return vrSystem!!
}


class OpenVR {
    companion object {
        init {
            println("in")
//            Native.register(NativeLibrary.getInstance("openvr_api"))
            Native.register(OpenVR::class.java, NativeLibrary.getInstance("openvr_api"))
        }
    }
}

fun main(args: Array<String>) {

//    Native.register(NativeLibrary.getInstance("openvr_api"))
    val OpenVR = OpenVR()
    val error = IntByReference()
    val a = VR_Init(error, EVRApplicationType.VRApplication_Scene.i)
    println(error.value)
    val w = IntByReference(0)
    val h = IntByReference(0)
    a.GetRecommendedRenderTargetSize!!.apply(w, h)
    println("w: ${w.value}, h: ${h.value}")
}