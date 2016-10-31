@file:JvmName("vr")

package openvr

import com.sun.jna.*
import com.sun.jna.ptr.*
import java.nio.ByteBuffer
import java.util.*

/**
 * Created by GBarbieri on 07.10.2016.
 */

/** Should be your first call   */
fun loadNatives() = Native.register(NativeLibrary.getInstance("openvr_api"))

class BooleanByReference(@JvmField var value: Boolean = false) : ByteByReference(if (value) 1 else 0)


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

    @JvmField var m = FloatArray(3 * 4)

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

    operator fun get(i: Int) = m[i]
}

open class HmdMatrix44_t : Structure {

    @JvmField var m = FloatArray(4 * 4)

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

    operator fun get(i: Int) = m[i]
}

open class HmdVector3_t : Structure {

    @JvmField var v = FloatArray(3)

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("v")

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

    @JvmField var v = FloatArray(4)

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("v")

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

    @JvmField var v = DoubleArray(3)

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("v")

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

    @JvmField var v = FloatArray(2)

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("v")

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

    @JvmField var w: Double = 0.0
    @JvmField var x: Double = 0.0
    @JvmField var y: Double = 0.0
    @JvmField var z: Double = 0.0

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

    @JvmField var r: Float = 0f
    @JvmField var g: Float = 0f
    @JvmField var b: Float = 0f
    @JvmField var a: Float = 0f

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

    @JvmField var vCorners = arrayOf(HmdVector3_t(), HmdVector3_t(), HmdVector3_t(), HmdVector3_t())

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

    @JvmField var vTopLeft = HmdVector2_t()
    @JvmField var vBottomRight = HmdVector2_t()

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

    @JvmField var rfRed = FloatArray(2)
    @JvmField var rfGreen = FloatArray(2)
    @JvmField var rfBlue = FloatArray(2)

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

enum class EVREye(@JvmField val i: Int) {
    Eye_Left(0),
    Eye_Right(1),
    MAX(2);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EGraphicsAPIConvention(@JvmField val i: Int) {

    API_DirectX(0), // Normalized Z goes from 0 at the viewer to 1 at the far clip plane
    API_OpenGL(1);  // Normalized Z goes from 1 at the viewer to -1 at the far clip plane

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class EGraphicsAPIConvention_ByReference(@JvmField var value: EGraphicsAPIConvention = EGraphicsAPIConvention.API_OpenGL) : IntByReference(value.i)

enum class EColorSpace(@JvmField val i: Int) {

    ColorSpace_Auto(0), //  Assumes 'gamma' for 8-bit per component formats, otherwise 'linear'.  This mirrors the DXGI formats which have _SRGB variants.
    ColorSpace_Gamma(1), // Texture data can be displayed directly on the display without any conversion (a.k.a. display native format).
    ColorSpace_Linear(2); // Same as gamma but has been converted to a linear representation using DXGI's sRGB conversion algorithm.

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class EColorSpace_ByReference(@JvmField var value: EColorSpace = EColorSpace.ColorSpace_Auto) : IntByReference(value.i)

open class Texture_t : Structure {

    @JvmField var handle = 0  // Native d3d texture pointer or GL texture id.
    @JvmField var eType = 0
    fun eType() = EGraphicsAPIConvention.of(eType)
    @JvmField var eColorSpace = 0
    fun eColorSpace() = EColorSpace.of(eColorSpace)

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("handle", "eType", "eColorSpace")

    constructor(handle: Int, eType: Int, eColorSpace: Int) {
        set(handle, eType, eColorSpace)
    }

    constructor(handle: Int, eType: EGraphicsAPIConvention, eColorSpace: EColorSpace) : this(handle, eType.i, eColorSpace.i)

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

enum class ETrackingResult(@JvmField val i: Int) {

    TrackingResult_Uninitialized(1),

    TrackingResult_Calibrating_InProgress(100),
    TrackingResult_Calibrating_OutOfRange(101),

    TrackingResult_Running_OK(200),
    TrackingResult_Running_OutOfRange(201);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

const val k_unTrackingStringSize = 32
const val k_unMaxDriverDebugResponseSize = 32768

/** Used to pass device IDs to API calls */
typealias TrackedDeviceIndex_t = Int
typealias TrackedDeviceIndex_t_ByReference = IntByReference
const val k_unTrackedDeviceIndex_Hmd = 0
const val k_unMaxTrackedDeviceCount = 16
const val k_unTrackedDeviceIndexOther = 0xFFFFFFFE.toInt();
const val k_unTrackedDeviceIndexInvalid = 0xFFFFFFFF.toInt();

/** Describes what kind of object is being tracked at a given ID */
enum class ETrackedDeviceClass(@JvmField val i: Int) {

    TrackedDeviceClass_Invalid(0), //           the ID was not valid.
    TrackedDeviceClass_HMD(1), //               Head-Mounted Displays
    TrackedDeviceClass_Controller(2), //        Tracked controllers
    TrackedDeviceClass_TrackingReference(4), // Camera and base stations that serve as tracking reference points

    TrackedDeviceClass_Count(5), //             This isn't a class that will ever be returned. It is used for allocating arrays and such

    TrackedDeviceClass_Other(1000);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Describes what specific role associated with a tracked device */
enum class ETrackedControllerRole(@JvmField val i: Int) {

    TrackedControllerRole_Invalid(0), //    Invalid value for controller value
    TrackedControllerRole_LeftHand(1), //   Tracked device associated with the left hand
    TrackedControllerRole_RightHand(2);//   Tracked device associated with the right hand

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** describes a single pose for a tracked object */
open class TrackedDevicePose_t : Structure {

    @JvmField var mDeviceToAbsoluteTracking = HmdMatrix34_t()
    @JvmField var vVelocity = HmdVector3_t()          // velocity in tracker space in m/s
    @JvmField var vAngularVelocity = HmdVector3_t()   // angular velocity in radians/s (?)
    @JvmField var eTrackingResult = 0
    fun eTrackingResult() = ETrackingResult.of(eTrackingResult)
    @JvmField var bPoseIsValid = false
    /**
     * This indicates that there is a device connected for this spot in the pose array.
     * It could go from true to false if the user unplugs the device.
     */
    @JvmField var bDeviceIsConnected = false

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("mDeviceToAbsoluteTracking", "vVelocity", "vAngularVelocity",
            "eTrackingResult", "bPoseIsValid", "bDeviceIsConnected")

    constructor(mDeviceToAbsoluteTracking: HmdMatrix34_t, vVelocity: HmdVector3_t, vAngularVelocity: HmdVector3_t, eTrackingResult: Int, bPoseIsValid: Boolean,
                bDeviceIsConnected: Boolean) {
        this.mDeviceToAbsoluteTracking = mDeviceToAbsoluteTracking
        this.vVelocity = vVelocity
        this.vAngularVelocity = vAngularVelocity
        this.eTrackingResult = eTrackingResult
        this.bPoseIsValid = bPoseIsValid
        this.bDeviceIsConnected = bDeviceIsConnected
    }

    constructor(mDeviceToAbsoluteTracking: HmdMatrix34_t, vVelocity: HmdVector3_t, vAngularVelocity: HmdVector3_t, eTrackingResult: ETrackingResult,
                bPoseIsValid: Boolean, bDeviceIsConnected: Boolean)
    : this(mDeviceToAbsoluteTracking, vVelocity, vAngularVelocity, eTrackingResult.i, bPoseIsValid, bDeviceIsConnected)

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : TrackedDevicePose_t(), Structure.ByReference
    class ByValue : TrackedDevicePose_t(), Structure.ByValue
}

/** Identifies which style of tracking origin the application wants to use for the poses it is requesting */
enum class ETrackingUniverseOrigin(@JvmField val i: Int) {

    TrackingUniverseSeated(0), //               Poses are provided relative to the seated zero pose
    TrackingUniverseStanding(1), //             Poses are provided relative to the safe bounds configured by the user
    TrackingUniverseRawAndUncalibrated(2);    // Poses are provided in the coordinate system defined by the driver. You probably don't want this one.

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class ETrackingUniverseOrigin_ByReference(@JvmField var value: ETrackingUniverseOrigin = ETrackingUniverseOrigin.TrackingUniverseSeated) : IntByReference(value.i)

/** Each entry in this value represents a property that can be retrieved about a tracked device.
 *  Many fields are only valid for one openvr.ETrackedDeviceClass. */
enum class ETrackedDeviceProperty(@JvmField val i: Int) {

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
    Prop_Axis0Type_Int32(3002), //          Return value is of value openvr.EVRControllerAxisType
    Prop_Axis1Type_Int32(3003), //          Return value is of value openvr.EVRControllerAxisType
    Prop_Axis2Type_Int32(3004), //          Return value is of value openvr.EVRControllerAxisType
    Prop_Axis3Type_Int32(3005), //          Return value is of value openvr.EVRControllerAxisType
    Prop_Axis4Type_Int32(3006), //          Return value is of value openvr.EVRControllerAxisType
    Prop_ControllerRoleHint_Int32(3007), // Return value is of value openvr.ETrackedControllerRole

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
    Prop_VendorSpecific_Reserved_End(10999);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** No string property will ever be longer than this length */
const val k_unMaxPropertyStringSize = 32 * 1024

/** Used to return errors that occur when reading properties. */
enum class ETrackedPropertyError(@JvmField val i: Int) {

    TrackedProp_Success(0),
    TrackedProp_WrongDataType(1),
    TrackedProp_WrongDeviceClass(2),
    TrackedProp_BufferTooSmall(3),
    TrackedProp_UnknownProperty(4),
    TrackedProp_InvalidDevice(5),
    TrackedProp_CouldNotContactServer(6),
    TrackedProp_ValueNotProvidedByDevice(7),
    TrackedProp_StringExceedsMaximumLength(8),
    TrackedProp_NotYetAvailable(9); // The property value isn't known yet, but is expected soon. Call again later.

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class ETrackedPropertyError_ByReference(@JvmField var value: ETrackedPropertyError = ETrackedPropertyError.TrackedProp_Success) : IntByReference(value.i)

/** Allows the application to control what part of the provided texture will be used in the frame buffer. */
open class VRTextureBounds_t : Structure {

    @JvmField var uMin = 0f
    @JvmField var vMin = 0f
    @JvmField var uMax = 0f
    @JvmField var vMax = 0f

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
enum class EVRSubmitFlags(@JvmField val i: Int) {

    // Simple render path. App submits rendered left and right eye images with no lens distortion correction applied.
    Submit_Default(0x00),

    // App submits final left and right eye images with lens distortion already applied (lens distortion makes the images appear
    // barrel distorted with chromatic aberration correction applied). The app would have used the data returned by
    // vr::openvr.IVRSystem::ComputeDistortion() to apply the correct distortion to the rendered images before calling Submit().
    Submit_LensDistortionAlreadyApplied(0x01),

    // If the texture pointer passed in is actually a renderbuffer (e.g. for MSAA in OpenGL) then set this flag.
    Submit_GlRenderBuffer(0x02),

    // Handle is pointer to VulkanData_t
    Submit_VulkanTexture(0x04);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Data required for passing Vulkan textures to openvr.IVRCompositor::Submit.
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
enum class EVRState(@JvmField val i: Int) {

    VRState_Undefined(-1),
    VRState_Off(0),
    VRState_Searching(1),
    VRState_Searching_Alert(2),
    VRState_Ready(3),
    VRState_Ready_Alert(4),
    VRState_NotReady(5),
    VRState_Standby(6),
    VRState_Ready_Alert_Low(7);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** The types of events that could be posted (and what the parameters mean for each event value) */
enum class EVREventType(@JvmField val i: Int) {

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
    VREvent_VendorSpecific_Reserved_End(19999);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Level of Hmd activity */
enum class EDeviceActivityLevel(@JvmField val i: Int) {

    k_EDeviceActivityLevel_Unknown(-1),
    k_EDeviceActivityLevel_Idle(0),
    k_EDeviceActivityLevel_UserInteraction(1),
    k_EDeviceActivityLevel_UserInteraction_Timeout(2),
    k_EDeviceActivityLevel_Standby(3);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** VR controller button and axis IDs */
enum class EVRButtonId(@JvmField val i: Int) {

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

    k_EButton_Max(64);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

fun buttonMaskFromId(id: EVRButtonId) = (1 shl id.i).toLong()

/** used for controller button events */
open class VREvent_Controller_t : VREvent_Data_t {

    @JvmField var button = 0  // openvr.EVRButtonId value
    fun button() = EVRButtonId.of(button)

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("button")

    constructor(button: Int) {
        this.button = button
    }

    constructor(button: EVRButtonId) : this(button.i)

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Controller_t(), Structure.ByReference
    class ByValue : VREvent_Controller_t(), Structure.ByValue
}

/** used for simulated mouse events in overlay space */
enum class EVRMouseButton(@JvmField val i: Int) {

    VRMouseButton_Left(0x0001),
    VRMouseButton_Right(0x0002),
    VRMouseButton_Middle(0x0004);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** used for simulated mouse events in overlay space */
open class VREvent_Mouse_t : VREvent_Data_t {

    // co-ords are in GL space, bottom left of the texture is 0,0
    @JvmField var x = 0f
    @JvmField var y = 0f
    @JvmField var button = 0  // openvr.EVRMouseButton value
    fun button() = EVRMouseButton.of(button)

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("x", "y", "button")

    constructor(x: Float, y: Float, button: Int) {
        this.x = x
        this.y = y
        this.button = button
    }

    constructor(x: Float, y: Float, button: EVRMouseButton) : this(x, y, button.i)

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Mouse_t(), Structure.ByReference
    class ByValue : VREvent_Mouse_t(), Structure.ByValue
}

/** used for simulated mouse wheel scroll in overlay space */
open class VREvent_Scroll_t : VREvent_Data_t {

    // movement in fraction of the pad traversed since last delta, 1.0 for a full swipe
    @JvmField var xdelta = 0f
    @JvmField var ydelta = 0f
    @JvmField var repeatCount = 0

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

    // true if the users finger is detected on the touch pad
    @JvmField var bFingerDown = false

    // How long the finger has been down in seconds
    @JvmField var flSecondsFingerDown = 0f

    // These values indicate the starting finger position (so you can do some basic swipe stuff)
    @JvmField var fValueXFirst = 0f
    @JvmField var fValueYFirst = 0f

    // This is the raw sampled coordinate without deadzoning
    @JvmField var fValueXRaw = 0f
    @JvmField var fValueYRaw = 0f

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("bFingerDown", "flSecondsFingerDown", "fValueXFirst", "fValueYFirst",
            "fValueXRaw", "fValueYRaw")

    constructor(bFingerDown: Boolean, flSecondsFingerDown: Float, fValueXFirst: Float, fValueYFirst: Float, fValueXRaw: Float, fValueYRaw: Float) {
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

    @JvmField var ulUserValue = 0L
    @JvmField var notificationId = 0

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

    @JvmField var pid = 0
    @JvmField var oldPid = 0
    @JvmField var bForced = false

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("pid", "oldPid", "bForced")

    constructor(pid: Int, oldPid: Int, bForced: Boolean) {
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

    @JvmField var overlayHandle = 0L

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

    @JvmField var statusState = 0 // openvr.EVRState value

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

    @JvmField var cNewInput = ""    // Up to 11 bytes of new input
    @JvmField var uUserValue = 0L // Possible flags about the new input

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

    @JvmField var ipdMeters = 0f

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

    @JvmField var m_nPreviousUniverse = 0L
    @JvmField var m_nCurrentUniverse = 0L

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

    @JvmField var reserved0 = 0L
    @JvmField var reserved1 = 0L

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

    @JvmField var m_nFidelityLevel = 0

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

    @JvmField var bResetBySystemMenu = false

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("bResetBySystemMenu")

    constructor(bResetBySystemMenu: Boolean) {
        this.bResetBySystemMenu = bResetBySystemMenu
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_SeatedZeroPoseReset_t(), Structure.ByReference
    class ByValue : VREvent_SeatedZeroPoseReset_t(), Structure.ByValue
}

open class VREvent_Screenshot_t : VREvent_Data_t {

    @JvmField var handle = 0
    @JvmField var type = 0

    constructor()

    constructor(handle: Int, type: Int) {
        this.handle = handle
        this.type = type
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<*> = Arrays.asList("handle", "value")

    class ByReference : VREvent_Screenshot_t(), Structure.ByReference
    class ByValue : VREvent_Screenshot_t(), Structure.ByValue
}

open class VREvent_ScreenshotProgress_t : VREvent_Data_t {

    @JvmField var progress = 0f

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

    @JvmField var pid = 0
    @JvmField var unArgsHandle = 0

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

    @JvmField var overlayHandle = 0L
    @JvmField var nVisualMode = 0

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

    @JvmField var eventType = 0   // openvr.EVREventType value
    fun eventType() = EVREventType.of(eventType)
    @JvmField var TrackedDeviceIndex_t = 0
    @JvmField var eventAgeSeconds = 0f
    // event data must be the end of the struct as its size is variable
    @JvmField var data: VREvent_Data_t? = null

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("eventType", "trackedDeviceIndex", "eventAgeSeconds", "data")

    constructor(eventType: Int, trackedDeviceIndex: Int, eventAgeSeconds: Float, data: VREvent_Data_t) {
        this.eventType = eventType
        this.TrackedDeviceIndex_t = trackedDeviceIndex
        this.eventAgeSeconds = eventAgeSeconds
        this.data = data
    }

    constructor(eventType: EVREventType, trackedDeviceIndex: Int, eventAgeSeconds: Float, data: VREvent_Data_t)
    : this(eventType.i, trackedDeviceIndex, eventAgeSeconds, data)

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

    @JvmField var pVertexData: HmdVector2_t.ByReference? = null
    @JvmField var unTriangleCount = 0

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

/** Identifies what kind of axis is on the controller at index n. Read this value with pVRSystem->Get( nControllerDeviceIndex, Prop_Axis0Type_Int32 + n );   */
enum class EVRControllerAxisType(@JvmField val i: Int) {

    k_eControllerAxis_None(0),
    k_eControllerAxis_TrackPad(1),
    k_eControllerAxis_Joystick(2),
    k_eControllerAxis_Trigger(3); // Analog trigger data is in the X axis

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** contains information about one axis on the controller */
open class VRControllerAxis_t : Structure {

    @JvmField var x = 0f  // Ranges from -1.0 to 1.0 for joysticks and track pads. Ranges from 0.0 to 1.0 for triggers were 0 is fully released.
    @JvmField var y = 0f  // Ranges from -1.0 to 1.0 for joysticks and track pads. Is always 0.0 for triggers.

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
    @JvmField var unPacketNum = 0

    // bit flags for each of the buttons. Use ButtonMaskFromId to turn an ID into a mask
    @JvmField var ulButtonPressed = 0L
    @JvmField var ulButtonTouched = 0L
    // Axis data for the controller's analog inputs
    @JvmField var rAxis = arrayOf(VRControllerAxis_t(), VRControllerAxis_t(), VRControllerAxis_t(), VRControllerAxis_t(), VRControllerAxis_t())

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
enum class EVRControllerEventOutputType(@JvmField val i: Int) {

    ControllerEventOutput_OSEvents(0),
    ControllerEventOutput_VREvents(1);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Collision Bounds Style */
enum class ECollisionBoundsStyle(@JvmField val i: Int) {

    COLLISION_BOUNDS_STYLE_BEGINNER(0),
    COLLISION_BOUNDS_STYLE_INTERMEDIATE(1),
    COLLISION_BOUNDS_STYLE_SQUARES(2),
    COLLISION_BOUNDS_STYLE_ADVANCED(3),
    COLLISION_BOUNDS_STYLE_NONE(4),

    COLLISION_BOUNDS_STYLE_COUNT(5);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Allows the application to customize how the overlay appears in the compositor */
open class Compositor_OverlaySettings : Structure {

    @JvmField var size = 0    // sizeof(openvr.Compositor_OverlaySettings)
    @JvmField var curved = false
    @JvmField var antialias = false
    @JvmField var scale = 0f
    @JvmField var distance = 0f
    @JvmField var alpha = 0f
    @JvmField var uOffset = 0f
    @JvmField var vOffset = 0f
    @JvmField var uScale = 0f
    @JvmField var vScale = 0f
    @JvmField var gridDivs = 0f
    @JvmField var gridWidth = 0f
    @JvmField var gridScale = 0f
    @JvmField var transform = HmdMatrix44_t()

    constructor()

    constructor(size: Int, curved: Boolean, antialias: Boolean, scale: Float, distance: Float, alpha: Float, uOffset: Float, vOffset: Float, uScale: Float,
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
enum class EVROverlayError(@JvmField val i: Int) {

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
    VROverlayError_NoNeighbor(27);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class EVROverlayError_ByReference(@JvmField var value: EVROverlayError = EVROverlayError.VROverlayError_None) : IntByReference(value.i)

/** value values to pass in to openvr.VR_Init to identify whether the application will draw a 3D scene.     */
enum class EVRApplicationType(@JvmField val i: Int) {

    VRApplication_Other(0), //          Some other kind of application that isn't covered by the other entries
    VRApplication_Scene(1), //          Application will submit 3D frames
    VRApplication_Overlay(2), //        Application only interacts with overlays
    VRApplication_Background(3), //     Application should not start SteamVR if it's not already running), and should not
    //                                  keep it running if everything else quits.
    VRApplication_Utility(4), //        Init should not try to load any drivers. The application needs access to utility
    //                                  interfaces (like openvr.IVRSettings and openvr.IVRApplications) but not hardware.
    VRApplication_VRMonitor(5), //      Reserved for vrmonitor
    VRApplication_SteamWatchdog(6), //  Reserved for Steam

    VRApplication_Max(7);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** error codes for firmware */
enum class EVRFirmwareError(@JvmField val i: Int) {

    VRFirmwareError_None(0),
    VRFirmwareError_Success(1),
    VRFirmwareError_Fail(2);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** error codes for notifications */
enum class EVRNotificationError(@JvmField val i: Int) {

    VRNotificationError_OK(0),
    VRNotificationError_InvalidNotificationId(100),
    VRNotificationError_NotificationQueueFull(101),
    VRNotificationError_InvalidOverlayHandle(102),
    VRNotificationError_SystemWithUserValueAlreadyExists(103);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** error codes returned by Vr_Init */
// Please add adequate error description to https://developer.valvesoftware.com/w/index.php?title=Category:SteamVRHelp
enum class EVRInitError(@JvmField val i: Int) {

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

    VRInitError_Steam_SteamInstallationNotFound(2000);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class EVRInitError_ByReference(@JvmField var value: EVRInitError = EVRInitError.VRInitError_None) : IntByReference(value.i)

enum class EVRScreenshotType(@JvmField val i: Int) {

    VRScreenshotType_None(0),
    VRScreenshotType_Mono(1), // left eye only
    VRScreenshotType_Stereo(2),
    VRScreenshotType_Cubemap(3),
    VRScreenshotType_MonoPanorama(4),
    VRScreenshotType_StereoPanorama(5);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EVRScreenshotPropertyFilenames(@JvmField val i: Int) {

    VRScreenshotPropertyFilenames_Preview(0),
    VRScreenshotPropertyFilenames_VR(1);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EVRTrackedCameraError(@JvmField val i: Int) {

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
    VRTrackedCameraError_InvalidFrameBufferSize(115);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EVRTrackedCameraFrameType(@JvmField val i: Int) {

    VRTrackedCameraFrameType_Distorted(0), //           This is the camera video frame size in pixels), still distorted.
    VRTrackedCameraFrameType_Undistorted(1), //         In pixels), an undistorted inscribed rectangle region without invalid regions. This size is subject to changes shortly.
    VRTrackedCameraFrameType_MaximumUndistorted(2), //  In pixels), maximum undistorted with invalid regions. Non zero alpha component identifies valid regions.
    MAX_CAMERA_FRAME_TYPES(3);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

typealias TrackedCameraHandle_t = Long
const val INVALID_TRACKED_CAMERA_HANDLE = 0L

open class CameraVideoStreamFrameHeader_t : Structure {

    @JvmField var eFrameType = 0
    fun eFrameType() = EVRTrackedCameraFrameType.of(eFrameType)

    @JvmField var nWidth = 0
    @JvmField var nHeight = 0
    @JvmField var nBytesPerPixel = 0

    @JvmField var nFrameSequence = 0

    @JvmField var standingTrackedDevicePose = TrackedDevicePose_t()

    constructor()

    constructor(eFrameType: Int, nWidth: Int, nHeight: Int, nBytesPerPixel: Int, nFrameSequence: Int, standingTrackedDevicePose: TrackedDevicePose_t) {

        this.eFrameType = eFrameType
        this.nWidth = nWidth
        this.nHeight = nHeight
        this.nBytesPerPixel = nBytesPerPixel
        this.nFrameSequence = nFrameSequence
        this.standingTrackedDevicePose = standingTrackedDevicePose
    }

    constructor(eFrameType: EVRTrackedCameraFrameType, nWidth: Int, nHeight: Int, nBytesPerPixel: Int, nFrameSequence: Int,
                standingTrackedDevicePose: TrackedDevicePose_t) : this(eFrameType.i, nWidth, nHeight, nBytesPerPixel, nFrameSequence, standingTrackedDevicePose)

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<*> = Arrays.asList("eFrameType", "nWidth", "nHeight", "nBytesPerPixel", "nFrameSequence", "standingTrackedDevicePose")

    class ByReference : CameraVideoStreamFrameHeader_t(), Structure.ByReference
    class ByValue : CameraVideoStreamFrameHeader_t(), Structure.ByValue
}

// Screenshot types
typealias ScreenshotHandle_t = Int
typealias ScreenshotHandle_t_ByReference = IntByReference
const val k_unScreenshotHandleInvalid = 0

// ivrsystem.h ====================================================================================================================================================

open class IVRSystem : Structure {

    // ------------------------------------
    // Display Methods
    // ------------------------------------

    /** Suggested size for the intermediate render target that the distortion pulls from. */
    fun getRecommendedRenderTargetSize(pnWidth: IntByReference, pnHeight: IntByReference)
            = GetRecommendedRenderTargetSize!!.invoke(pnWidth, pnHeight)

    @JvmField var GetRecommendedRenderTargetSize: GetRecommendedRenderTargetSize_callback? = null

    interface GetRecommendedRenderTargetSize_callback : Callback {
        fun invoke(pnWidth: IntByReference, pnHeight: IntByReference)
    }

    /** The projection matrix for the specified eye */
    fun getProjectionMatrix(eEye: EVREye, fNearZ: Float, fFarZ: Float, eProjType: EGraphicsAPIConvention)
            = GetProjectionMatrix!!.invoke(eEye.i, fNearZ, fFarZ, eProjType.i)

    @JvmField var GetProjectionMatrix: GetProjectionMatrix_callback? = null

    interface GetProjectionMatrix_callback : Callback {
        fun invoke(eEye: Int, fNearZ: Float, fFarZ: Float, eProjType: Int): HmdMatrix44_t.ByValue
    }

    /** The components necessary to build your own projection matrix in case your application is doing something fancy like infinite Z  */
    fun getProjectionRaw(eEye: EVREye, pfLeft: FloatByReference, pfRight: FloatByReference, pfTop: FloatByReference, pfBottom: FloatByReference)
            = GetProjectionRaw!!.invoke(eEye.i, pfLeft, pfRight, pfTop, pfBottom)

    @JvmField var GetProjectionRaw: GetProjectionRaw_callback? = null

    interface GetProjectionRaw_callback : Callback {
        fun invoke(eEye: Int, pfLeft: FloatByReference, pfRight: FloatByReference, pfTop: FloatByReference, pfBottom: FloatByReference)
    }

    /** Returns the result of the distortion function for the specified eye and input UVs.
     *  UVs go from 0,0 in the upper left of that eye's viewport and 1,1 in the lower right of that eye's viewport.
     *  Values may be NAN to indicate an error has occurred.     */
    fun computeDistortion(eEye: EVREye, fU: Float, fV: Float) = ComputeDistortion!!.invoke(eEye.i, fU, fV)

    @JvmField var ComputeDistortion: ComputeDistortion_callback? = null

    interface ComputeDistortion_callback : Callback {
        fun invoke(eEye: Int, fU: Float, fV: Float): DistortionCoordinates_t.ByValue
    }

    /** Returns the transform from eye space to the head space. Eye space is the per-eye flavor of head space that provides stereo disparity.
     *  Instead of Model * View * Projection the sequence is Model * View * Eye^-1 * Projection.
     *  Normally View and Eye^-1 will be multiplied together and treated as View in your application.   */
    fun getEyeToHeadTransform(eEye: EVREye) = GetEyeToHeadTransform!!.invoke(eEye.i)

    @JvmField var GetEyeToHeadTransform: GetEyeToHeadTransform_callback? = null

    interface GetEyeToHeadTransform_callback : Callback {
        fun invoke(eEye: Int): HmdMatrix34_t.ByValue
    }

    /** Returns the number of elapsed seconds since the last recorded vsync event. This will come from a vsync timer event in the timer if possible or from the
     *  application-reported time if that is not available.
     *  If no vsync times are available the function will return zero for vsync time and frame counter and return false from the method.    */
    fun getTimeSinceLastVsync(pfSecondsSinceLastVsync: FloatByReference, pulFrameCounter: LongByReference)
            = GetTimeSinceLastVsync!!.invoke(pfSecondsSinceLastVsync, pulFrameCounter)

    @JvmField var GetTimeSinceLastVsync: GetTimeSinceLastVsync_callback? = null

    interface GetTimeSinceLastVsync_callback : Callback {
        fun invoke(pfSecondsSinceLastVsync: FloatByReference, pulFrameCounter: LongByReference): Boolean
    }

    /** [D3D9 Only]
     * Returns the adapter index that the user should pass into CreateDevice to set up D3D9 in such a way that it can go full screen exclusive on the HMD.
     * Returns -1 if there was an error.     */
    fun getD3D9AdapterIndex() = GetD3D9AdapterIndex!!.invoke()

    @JvmField var GetD3D9AdapterIndex: GetD3D9AdapterIndex_callback? = null

    interface GetD3D9AdapterIndex_callback : Callback {
        fun invoke(): Int
    }

    /** [D3D10/11 Only]
     * Returns the adapter index that the user should pass into EnumAdapters to create the device and swap chain in DX10 and DX11. If an error occurs the index
     * will be set to -1. The index will also be -1 if the headset is in direct mode on the driver side instead of using the compositor's builtin direct mode
     * support.      */
    fun getDXGIOutputInfo(pnAdapterIndex: IntByReference) = GetDXGIOutputInfo!!.invoke(pnAdapterIndex)

    @JvmField var GetDXGIOutputInfo: GetDXGIOutputInfo_callback? = null

    interface GetDXGIOutputInfo_callback : Callback {
        fun invoke(pnAdapterIndex: IntByReference)
    }


    // ------------------------------------
    // Display Mode methods
    // ------------------------------------

    /** Use to determine if the headset display is part of the desktop (i.e. extended) or hidden (i.e. direct mode). */
    fun isDisplayOnDesktop() = IsDisplayOnDesktop!!.invoke()

    @JvmField var IsDisplayOnDesktop: IsDisplayOnDesktop_callback? = null

    interface IsDisplayOnDesktop_callback : Callback {
        fun invoke(): Boolean
    }

    /** Set the display visibility (true = extended, false = direct mode).  Return value of true indicates that the change was successful. */
    fun setDisplayVisibility(bIsVisibleOnDesktop: Boolean): Boolean = SetDisplayVisibility!!.invoke(bIsVisibleOnDesktop)

    @JvmField var SetDisplayVisibility: SetDisplayVisibility_callback? = null

    interface SetDisplayVisibility_callback : Callback {
        fun invoke(bIsVisibleOnDesktop: Boolean): Boolean
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
     *  openvr.k_unMaxTrackedDeviceCount.
     *
     *  Seated experiences should call this method with TrackingUniverseSeated and receive poses relative to the seated zero pose. Standing experiences should
     *  call this method with TrackingUniverseStanding and receive poses relative to the Chaperone Play Area. TrackingUniverseRawAndUncalibrated should probably
     *  not be used unless the application is the Chaperone calibration tool itself, but will provide poses relative to the hardware-specific coordinate system
     *  in the driver.     */
    fun GetDeviceToAbsoluteTrackingPose(eOrigin: ETrackingUniverseOrigin, fPredictedSecondsToPhotonsFromNow: Float,
                                        pTrackedDevicePoseArray: TrackedDevicePose_t.ByReference, unTrackedDevicePoseArrayCount: Int)
            = GetDeviceToAbsoluteTrackingPose!!.invoke(eOrigin.i, fPredictedSecondsToPhotonsFromNow, pTrackedDevicePoseArray, unTrackedDevicePoseArrayCount)

    @JvmField var GetDeviceToAbsoluteTrackingPose: GetDeviceToAbsoluteTrackingPose_callback? = null

    interface GetDeviceToAbsoluteTrackingPose_callback : Callback {
        fun invoke(eOrigin: Int, fPredictedSecondsToPhotonsFromNow: Float, pTrackedDevicePoseArray: TrackedDevicePose_t.ByReference,
                   unTrackedDevicePoseArrayCount: Int)
    }

    /** Sets the zero pose for the seated tracker coordinate system to the current position and yaw of the HMD. After ResetSeatedZeroPose all
     *  GetDeviceToAbsoluteTrackingPose calls that pass TrackingUniverseSeated as the origin will be relative to this new zero pose. The new zero coordinate
     *  system will not change the fact that the Y axis is up in the real world, so the next pose returned from GetDeviceToAbsoluteTrackingPose after a call to
     *  ResetSeatedZeroPose may not be exactly an identity matrix.
     *
     *  NOTE: This function overrides the user's previously saved seated zero pose and should only be called as the result of a user action.
     *  Users are also able to set their seated zero pose via the openvr.OpenVR Dashboard.     **/
    fun resetSeatedZeroPose() = ResetSeatedZeroPose!!.invoke()

    @JvmField var ResetSeatedZeroPose: ResetSeatedZeroPose_callback? = null

    interface ResetSeatedZeroPose_callback : Callback {
        fun invoke()
    }

    /** Returns the transform from the seated zero pose to the standing absolute tracking system. This allows applications to represent the seated origin to
     *  used or transform object positions from one coordinate system to the other.
     *
     *  The seated origin may or may not be inside the Play Area or Collision Bounds returned by openvr.IVRChaperone. Its position depends on what the user has set
     *  from the Dashboard settings and previous calls to ResetSeatedZeroPose. */
    fun getSeatedZeroPoseToStandingAbsoluteTrackingPose() = GetSeatedZeroPoseToStandingAbsoluteTrackingPose!!.invoke()

    @JvmField var GetSeatedZeroPoseToStandingAbsoluteTrackingPose: GetSeatedZeroPoseToStandingAbsoluteTrackingPose_callback? = null

    interface GetSeatedZeroPoseToStandingAbsoluteTrackingPose_callback : Callback {
        fun invoke(): HmdMatrix34_t.ByValue
    }

    /** Returns the transform from the tracking origin to the standing absolute tracking system. This allows applications to convert from raw tracking space to
     *  the calibrated standing coordinate system. */
    fun getRawZeroPoseToStandingAbsoluteTrackingPose() = GetRawZeroPoseToStandingAbsoluteTrackingPose!!.invoke()

    @JvmField var GetRawZeroPoseToStandingAbsoluteTrackingPose: GetRawZeroPoseToStandingAbsoluteTrackingPose_callback? = null

    interface GetRawZeroPoseToStandingAbsoluteTrackingPose_callback : Callback {
        fun invoke(): HmdMatrix34_t.ByValue
    }

    /** Get a sorted array of device indices of a given class of tracked devices (e.g. controllers).  Devices are sorted right to left relative to the specified
     *  tracked device (default: hmd -- pass in -1 for absolute tracking space).  Returns the number of devices in the list, or the size of the array needed if
     *  not large enough. */
    fun getSortedTrackedDeviceIndicesOfClass(eTrackedDeviceClass: ETrackedDeviceClass,
                                             punTrackedDeviceIndexArray: TrackedDeviceIndex_t_ByReference,
                                             unTrackedDeviceIndexArrayCount: Int,
                                             unRelativeToTrackedDeviceIndex: TrackedDeviceIndex_t = k_unTrackedDeviceIndex_Hmd)
            = GetSortedTrackedDeviceIndicesOfClass!!.invoke(eTrackedDeviceClass.i, punTrackedDeviceIndexArray, unTrackedDeviceIndexArrayCount,
            unRelativeToTrackedDeviceIndex)

    @JvmField var GetSortedTrackedDeviceIndicesOfClass: GetSortedTrackedDeviceIndicesOfClass_callback? = null

    interface GetSortedTrackedDeviceIndicesOfClass_callback : Callback {
        fun invoke(eTrackedDeviceClass: Int, punTrackedDeviceIndexArray: TrackedDeviceIndex_t_ByReference, unTrackedDeviceIndexArrayCount: Int,
                   unRelativeToTrackedDeviceIndex: TrackedDeviceIndex_t = k_unTrackedDeviceIndex_Hmd): Int
    }

    /** Returns the level of activity on the device. */
    fun getTrackedDeviceActivityLevel(unDeviceId: TrackedDeviceIndex_t) = EDeviceActivityLevel.of(GetTrackedDeviceActivityLevel!!.invoke(unDeviceId))

    @JvmField var GetTrackedDeviceActivityLevel: GetTrackedDeviceActivityLevel_callback? = null

    interface GetTrackedDeviceActivityLevel_callback : Callback {
        fun invoke(unDeviceId: TrackedDeviceIndex_t): Int
    }

    /** Convenience utility to apply the specified transform to the specified pose.
     *  This properly transforms all pose components, including velocity and angular velocity     */
    fun invokeTransform(pOutputPose: TrackedDevicePose_t.ByReference, pTrackedDevicePose: TrackedDevicePose_t.ByReference, pTransform: HmdMatrix34_t.ByReference)
            = ApplyTransform!!.invoke(pOutputPose, pTrackedDevicePose, pTransform)

    @JvmField var ApplyTransform: ApplyTransform_callback? = null

    interface ApplyTransform_callback : Callback {
        fun invoke(pOutputPose: TrackedDevicePose_t.ByReference, pTrackedDevicePose: TrackedDevicePose_t.ByReference, pTransform: HmdMatrix34_t.ByReference)
    }

    /** Returns the device index associated with a specific role, for example the left hand or the right hand. */
    fun getTrackedDeviceIndexForControllerRole(unDeviceType: ETrackedControllerRole) = GetTrackedDeviceIndexForControllerRole!!.invoke(unDeviceType.i)

    @JvmField var GetTrackedDeviceIndexForControllerRole: GetTrackedDeviceIndexForControllerRole_callback? = null

    interface GetTrackedDeviceIndexForControllerRole_callback : Callback {
        fun invoke(unDeviceType: Int): TrackedDeviceIndex_t
    }

    /** Returns the controller value associated with a device index. */
    fun getControllerRoleForTrackedDeviceIndex(unDeviceIndex: TrackedDeviceIndex_t)
            = ETrackedControllerRole.of(GetControllerRoleForTrackedDeviceIndex!!.invoke(unDeviceIndex))

    @JvmField var GetControllerRoleForTrackedDeviceIndex: GetControllerRoleForTrackedDeviceIndex_callback? = null

    interface GetControllerRoleForTrackedDeviceIndex_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t): Int
    }


    // ------------------------------------
    // Property methods
    // ------------------------------------

    /** Returns the device class of a tracked device. If there has not been a device connected in this slot since the application started this function will
     *  return TrackedDevice_Invalid. For previous detected devices the function will return the previously observed device class.
     *
     * To determine which devices exist on the system, just loop from 0 to openvr.k_unMaxTrackedDeviceCount and check the device class. Every device with something
     * other than TrackedDevice_Invalid is associated with an actual tracked device. */
    fun getTrackedDeviceClass(unDeviceIndex: TrackedDeviceIndex_t) = ETrackedDeviceClass.of(GetTrackedDeviceClass!!.invoke(unDeviceIndex))

    @JvmField var GetTrackedDeviceClass: GetTrackedDeviceClass_callback? = null

    interface GetTrackedDeviceClass_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t): Int
    }

    /** Returns true if there is a device connected in this slot. */
    fun isTrackedDeviceConnected(unDeviceIndex: TrackedDeviceIndex_t) = IsTrackedDeviceConnected!!.invoke(unDeviceIndex)

    @JvmField var IsTrackedDeviceConnected: IsTrackedDeviceConnected_callback? = null

    interface IsTrackedDeviceConnected_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t): Boolean
    }

    /** Returns a bool property. If the device index is not valid or the property is not a bool value this function will return false. */
    fun getBoolTrackedDeviceProperty(unDeviceIndex: TrackedDeviceIndex_t, prop: ETrackedDeviceProperty, pError: ETrackedPropertyError_ByReference? = null)
            = GetBoolTrackedDeviceProperty!!.invoke(unDeviceIndex, prop.i, pError)

    @JvmField var GetBoolTrackedDeviceProperty: GetBoolTrackedDeviceProperty_callback? = null

    interface GetBoolTrackedDeviceProperty_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: ETrackedPropertyError_ByReference? = null): Boolean
    }

    /** Returns a float property. If the device index is not valid or the property is not a float value this function will return 0. */
    fun getFloatTrackedDeviceProperty(unDeviceIndex: TrackedDeviceIndex_t, prop: ETrackedDeviceProperty, pError: ETrackedPropertyError_ByReference? = null): Float
            = GetFloatTrackedDeviceProperty!!.invoke(unDeviceIndex, prop.i, pError)

    @JvmField var GetFloatTrackedDeviceProperty: GetFloatTrackedDeviceProperty_callback? = null

    interface GetFloatTrackedDeviceProperty_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: ETrackedPropertyError_ByReference? = null): Float
    }

    /** Returns an int property. If the device index is not valid or the property is not a int value this function will return 0. */
    fun getInt32TrackedDeviceProperty(unDeviceIndex: TrackedDeviceIndex_t, prop: ETrackedDeviceProperty, pError: ETrackedPropertyError_ByReference? = null)
            = GetInt32TrackedDeviceProperty!!.invoke(unDeviceIndex, prop.i, pError)

    @JvmField var GetInt32TrackedDeviceProperty: GetInt32TrackedDeviceProperty_callback? = null

    interface GetInt32TrackedDeviceProperty_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: ETrackedPropertyError_ByReference? = null): Int
    }

    /** Returns a uint64 property. If the device index is not valid or the property is not a uint64 value this function will return 0. */
    fun getUint64TrackedDeviceProperty(unDeviceIndex: TrackedDeviceIndex_t, prop: ETrackedDeviceProperty, pError: ETrackedPropertyError_ByReference? = null)
            = GetUint64TrackedDeviceProperty!!.invoke(unDeviceIndex, prop.i, pError)

    @JvmField var GetUint64TrackedDeviceProperty: GetUint64TrackedDeviceProperty_callback? = null

    interface GetUint64TrackedDeviceProperty_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: ETrackedPropertyError_ByReference? = null): Long
    }

    /** Returns a matrix property. If the device index is not valid or the property is not a matrix value, this function will return identity. */
    fun getMatrix34TrackedDeviceProperty(unDeviceIndex: TrackedDeviceIndex_t, prop: ETrackedDeviceProperty, pError: ETrackedPropertyError_ByReference? = null)
            = GetMatrix34TrackedDeviceProperty!!.invoke(unDeviceIndex, prop.i, pError)

    @JvmField var GetMatrix34TrackedDeviceProperty: GetMatrix34TrackedDeviceProperty_callback? = null

    interface GetMatrix34TrackedDeviceProperty_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: ETrackedPropertyError_ByReference? = null): HmdMatrix34_t.ByValue
    }

    /** Returns a string property. If the device index is not valid or the property is not a string value this function will return 0. Otherwise it returns the
     *  length of the number of bytes necessary to hold this string including the trailing null. Strings will generally fit in buffers of openvr.k_unTrackingStringSize
     *  characters. */
    fun getStringTrackedDeviceProperty(unDeviceIndex: TrackedDeviceIndex_t, prop: ETrackedDeviceProperty, pchValue: String, unBufferSize: Int,
                                       pError: ETrackedPropertyError_ByReference? = null)
            = GetStringTrackedDeviceProperty!!.invoke(unDeviceIndex, prop.i, pchValue, unBufferSize, pError)

    @JvmField var GetStringTrackedDeviceProperty: GetStringTrackedDeviceProperty_callback? = null

    interface GetStringTrackedDeviceProperty_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pchValue: String, unBufferSize: Int, pError: ETrackedPropertyError_ByReference? = null): Int
    }

    /** returns a string that corresponds with the specified property error. The string will be the name of the error value value for all valid error codes */
    fun getPropErrorNameFromEnum(error: ETrackedPropertyError) = GetPropErrorNameFromEnum!!.invoke(error.i)

    @JvmField var GetPropErrorNameFromEnum: GetPropErrorNameFromEnum_callback? = null

    interface GetPropErrorNameFromEnum_callback : Callback {
        fun invoke(error: Int): String
    }


    // ------------------------------------
    // Event methods
    // ------------------------------------

    /** Returns true and fills the event with the next event on the queue if there is one. If there are no events this method returns false. uncbVREvent should
     *  be the size in bytes of the openvr.VREvent_t struct */
    fun pollNextEvent(pEvent: VREvent_t.ByReference, uncbVREvent: Int) = PollNextEvent!!.invoke(pEvent, uncbVREvent)

    @JvmField var PollNextEvent: PollNextEvent_callback? = null

    interface PollNextEvent_callback : Callback {
        fun invoke(pEvent: VREvent_t.ByReference, uncbVREvent: Int): Boolean
    }

    /** Returns true and fills the event with the next event on the queue if there is one. If there are no events this method returns false. Fills in the pose
     *  of the associated tracked device in the provided pose struct.
     *  This pose will always be older than the call to this function and should not be used to render the device.
     *  uncbVREvent should be the size in bytes of the openvr.VREvent_t struct */
    fun pollNextEventWithPose(eOrigin: ETrackingUniverseOrigin, pEvent: VREvent_t.ByReference, uncbVREvent: Int,
                              pTrackedDevicePose: TrackedDevicePose_t.ByReference)
            = PollNextEventWithPose!!.invoke(eOrigin.i, pEvent, uncbVREvent, pTrackedDevicePose)

    @JvmField var PollNextEventWithPose: PollNextEventWithPose_callback? = null

    interface PollNextEventWithPose_callback : Callback {
        fun invoke(eOrigin: Int, pEvent: VREvent_t.ByReference, uncbVREvent: Int, pTrackedDevicePose: TrackedDevicePose_t.ByReference): Boolean
    }

    /** returns the name of an EVREvent value value */
    fun getEventTypeNameFromEnum(eType: EVREventType) = GetEventTypeNameFromEnum!!.invoke(eType.i)

    @JvmField var GetEventTypeNameFromEnum: GetEventTypeNameFromEnum_callback? = null

    interface GetEventTypeNameFromEnum_callback : Callback {
        fun invoke(eType: Int): String
    }


    // ------------------------------------
    // Rendering helper methods
    // ------------------------------------

    /** Returns the stencil mesh information for the current HMD. If this HMD does not have a stencil mesh the vertex data and count will be NULL and 0
     *  respectively. This mesh is meant to be rendered into the stencil buffer (or into the depth buffer setting nearz) before rendering each eye's view.
     *  The pixels covered by this mesh will never be seen by the user after the lens distortion is applied and based on visibility to the panels.
     *  This will improve perf by letting the GPU early-reject pixels the user will never see before running the pixel shader.
     *  NOTE: Render this mesh with backface culling disabled since the winding order of the vertices can be different per-HMD or per-eye.     */
    fun GetHiddenAreaMesh(eEye: EVREye) = GetHiddenAreaMesh!!.invoke(eEye.i)

    @JvmField var GetHiddenAreaMesh: GetHiddenAreaMesh_callback? = null

    interface GetHiddenAreaMesh_callback : Callback {
        fun invoke(eEye: Int): HiddenAreaMesh_t.ByValue
    }


    // ------------------------------------
    // Controller methods
    // ------------------------------------

    /** Fills the supplied struct with the current state of the controller. Returns false if the controller index is invalid. */
    fun getControllerState(unControllerDeviceIndex: TrackedDeviceIndex_t, pControllerState: VRControllerState_t.ByReference)
            = GetControllerState!!.invoke(unControllerDeviceIndex, pControllerState)

    @JvmField var GetControllerState: GetControllerState_callback? = null

    interface GetControllerState_callback : Callback {
        fun invoke(unControllerDeviceIndex: TrackedDeviceIndex_t, pControllerState: VRControllerState_t.ByReference): Boolean
    }

    /** Fills the supplied struct with the current state of the controller and the provided pose with the pose of the controller when the controller state was
     *  updated most recently. Use this form if you need a precise controller pose as input to your application when the user presses or releases a button. */
    fun GetControllerStateWithPose(eOrigin: ETrackingUniverseOrigin, unControllerDeviceIndex: TrackedDeviceIndex_t,
                                   pControllerState: VRControllerState_t.ByReference, pTrackedDevicePose: TrackedDevicePose_t.ByReference)
            = GetControllerStateWithPose!!.invoke(eOrigin.i, unControllerDeviceIndex, pControllerState, pTrackedDevicePose)

    @JvmField var GetControllerStateWithPose: GetControllerStateWithPose_callback? = null

    interface GetControllerStateWithPose_callback : Callback {
        fun invoke(eOrigin: Int, unControllerDeviceIndex: TrackedDeviceIndex_t, pControllerState: VRControllerState_t.ByReference,
                   pTrackedDevicePose: TrackedDevicePose_t.ByReference): Boolean
    }

    /** Trigger a single haptic pulse on a controller. After this call the application may not trigger another haptic pulse on this controller and axis
     *  combination for 5ms. */
    fun triggerHapticPulse(unControllerDeviceIndex: TrackedDeviceIndex_t, unAxisId: Int, usDurationMicroSec: Short)
            = TriggerHapticPulse!!.invoke(unControllerDeviceIndex, unAxisId, usDurationMicroSec)

    @JvmField var TriggerHapticPulse: TriggerHapticPulse_callback? = null

    interface TriggerHapticPulse_callback : Callback {
        fun invoke(unControllerDeviceIndex: TrackedDeviceIndex_t, unAxisId: Int, usDurationMicroSec: Short)
    }

    /** returns the name of an openvr.EVRButtonId value value */
    fun getButtonIdNameFromEnum(eButtonId: EVRButtonId) = GetButtonIdNameFromEnum!!.invoke(eButtonId.i)

    @JvmField var GetButtonIdNameFromEnum: GetButtonIdNameFromEnum_callback? = null

    interface GetButtonIdNameFromEnum_callback : Callback {
        fun invoke(eButtonId: Int): String
    }

    /** returns the game of an openvr.EVRControllerAxisType value value */
    fun getControllerAxisTypeNameFromEnum(eAxisType: EVRControllerAxisType) = GetControllerAxisTypeNameFromEnum!!.invoke(eAxisType.i)

    @JvmField var GetControllerAxisTypeNameFromEnum: GetControllerAxisTypeNameFromEnum_callback? = null

    interface GetControllerAxisTypeNameFromEnum_callback : Callback {
        fun invoke(eAxisType: Int): String
    }

    /** Tells openvr.OpenVR that this process wants exclusive access to controller button states and button events. Other apps will be notified that they have lost
     *  input focus with a VREvent_InputFocusCaptured event. Returns false if input focus could not be captured for some reason. */
    fun captureInputFocus() = CaptureInputFocus!!.invoke()

    @JvmField var CaptureInputFocus: CaptureInputFocus_callback? = null

    interface CaptureInputFocus_callback : Callback {
        fun invoke(): Boolean
    }

    /** Tells openvr.OpenVR that this process no longer wants exclusive access to button states and button events. Other apps will be notified that input focus has
     *  been released with a VREvent_InputFocusReleased event. */
    fun releaseInputFocus() = ReleaseInputFocus!!.invoke()

    @JvmField var ReleaseInputFocus: ReleaseInputFocus_callback? = null

    interface ReleaseInputFocus_callback : Callback {
        fun invoke()
    }

    /** Returns true if input focus is captured by another process. */
    fun isInputFocusCapturedByAnotherProcess() = IsInputFocusCapturedByAnotherProcess!!.invoke()

    @JvmField var IsInputFocusCapturedByAnotherProcess: IsInputFocusCapturedByAnotherProcess_callback? = null

    interface IsInputFocusCapturedByAnotherProcess_callback : Callback {
        fun invoke(): Boolean
    }


    // ------------------------------------
    // Debug Methods
    // ------------------------------------

    /** Sends a request to the driver for the specified device and returns the response. The maximum response size is 32k, but this method can be called with
     *  a smaller buffer. If the response exceeds the size of the buffer, it is truncated.
     *  The size of the response including its terminating null is returned. */
    fun driverDebugRequest(unDeviceIndex: TrackedDeviceIndex_t, pchRequest: String, pchResponseBuffer: String, unResponseBufferSize: Int)
            = DriverDebugRequest!!.invoke(unDeviceIndex, pchRequest, pchResponseBuffer, unResponseBufferSize)

    @JvmField var DriverDebugRequest: DriverDebugRequest_callback? = null

    interface DriverDebugRequest_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t, pchRequest: String, pchResponseBuffer: String, unResponseBufferSize: Int): Int
    }


    // ------------------------------------
    // Firmware methods
    // ------------------------------------

    /** Performs the actual firmware update if applicable.
     *  The following events will be sent, if VRFirmwareError_None was returned: VREvent_FirmwareUpdateStarted, VREvent_FirmwareUpdateFinished
     *  Use the properties Prop_Firmware_UpdateAvailable_Bool, Prop_Firmware_ManualUpdate_Bool, and Prop_Firmware_ManualUpdateURL_String to figure our whether
     *  a firmware update is available, and to figure out whether its a manual update
     *  Prop_Firmware_ManualUpdateURL_String should point to an URL describing the manual update process */
    fun performFirmwareUpdate(unDeviceIndex: TrackedDeviceIndex_t) = EVRFirmwareError.of(PerformFirmwareUpdate!!.invoke(unDeviceIndex))

    @JvmField var PerformFirmwareUpdate: PerformFirmwareUpdate_callback? = null

    interface PerformFirmwareUpdate_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t): Int
    }


    // ------------------------------------
    // Application life cycle methods
    // ------------------------------------

    /** Call this to acknowledge to the system that VREvent_Quit has been received and that the process is exiting.
     *  This extends the timeout until the process is killed. */
    fun acknowledgeQuit_Exiting() = AcknowledgeQuit_Exiting!!.invoke()

    @JvmField var AcknowledgeQuit_Exiting: AcknowledgeQuit_Exiting_callback? = null

    interface AcknowledgeQuit_Exiting_callback : Callback {
        fun invoke()
    }

    /** Call this to tell the system that the user is being prompted to save data. This halts the timeout and dismisses the dashboard (if it was up).
     *  Applications should be sure to actually prompt the user to save and then exit afterward, otherwise the user will be left in a confusing state. */
    fun acknowledgeQuit_UserPrompt() = AcknowledgeQuit_UserPrompt!!.invoke()

    @JvmField var AcknowledgeQuit_UserPrompt: AcknowledgeQuit_UserPrompt_callback? = null

    interface AcknowledgeQuit_UserPrompt_callback : Callback {
        fun invoke()
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

    class ByReference : IVRSystem(), Structure.ByReference
    class ByValue : IVRSystem(), Structure.ByValue
}

const val IVRSystem_Version = "FnTable:IVRSystem_012"

/** Used for all errors reported by the openvr.IVRApplications interface */
enum class EVRApplicationError(@JvmField val i: Int) {

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
    VRApplicationError_IsTemplate(114), // error when you try to call LaunchApplication() on a template value app (use LaunchTemplateApplication)

    VRApplicationError_BufferTooSmall(200), //      The provided buffer was too small to fit the requested data
    VRApplicationError_PropertyNotSet(201), //      The requested property was not set
    VRApplicationError_UnknownProperty(202),
    VRApplicationError_InvalidParameter(203);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class EVRApplicationError_ByReference(val value: EVRApplicationError = EVRApplicationError.VRApplicationError_None) : IntByReference(value.i)

/** The maximum length of an application key */
const val k_unMaxApplicationKeyLength = 128

/** these are the properties available on applications. */
enum class EVRApplicationProperty(@JvmField val i: Int) {

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

    VRApplicationProperty_LastLaunchTime_Uint64(70);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** These are states the scene application startup process will go through. */
enum class EVRApplicationTransitionState(@JvmField val i: Int) {

    VRApplicationTransition_None(0),

    VRApplicationTransition_OldAppQuitSent(10),
    VRApplicationTransition_WaitingForExternalLaunch(11),

    VRApplicationTransition_NewAppLaunched(20);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

fun Int.toEVRApplicationTransitionState() = EVRApplicationTransitionState.values().first { it.i == this }

open class AppOverrideKeys_t : Structure {

    @JvmField var pchKey = ""
    @JvmField var pchValue = ""

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

// ivrapplications.h ==============================================================================================================================================

open class IVRApplications : Structure {

    // ---------------  Application management  --------------- //

    /** Adds an application manifest to the list to load when building the list of installed applications.
     *  Temporary manifests are not automatically loaded */
    fun addApplicationManifest(pchApplicationManifestFullPath: String, bTemporary: Boolean = false)
            = AddApplicationManifest!!.invoke(pchApplicationManifestFullPath, bTemporary)

    @JvmField var AddApplicationManifest: AddApplicationManifest_callback? = null

    interface AddApplicationManifest_callback : Callback {
        fun invoke(pchApplicationManifestFullPath: String, bTemporary: Boolean = false): Int
    }

    /** Removes an application manifest from the list to load when building the list of installed applications. */
    fun removeApplicationManifest(pchApplicationManifestFullPath: String)
            = EVRApplicationError.of(RemoveApplicationManifest!!.invoke(pchApplicationManifestFullPath))

    @JvmField var RemoveApplicationManifest: RemoveApplicationManifest_callback? = null

    interface RemoveApplicationManifest_callback : Callback {
        fun invoke(pchApplicationManifestFullPath: String): Int
    }

    /** Returns true if an application is installed */
    fun isApplicationInstalled(pchAppKey: String) = IsApplicationInstalled!!.invoke(pchAppKey)

    @JvmField var IsApplicationInstalled: IsApplicationInstalled_callback? = null

    interface IsApplicationInstalled_callback : Callback {
        fun invoke(pchAppKey: String): Boolean
    }

    /** Returns the number of applications available in the list */
    fun getApplicationCount() = GetApplicationCount!!.invoke()

    @JvmField var GetApplicationCount: GetApplicationCount_callback? = null

    interface GetApplicationCount_callback : Callback {
        fun invoke(): Int
    }

    /** Returns the key of the specified application. The index is at least 0 and is less than the return value of GetApplicationCount(). The buffer should be
     *  at least openvr.k_unMaxApplicationKeyLength in order to fit the key. */
    fun getApplicationKeyByIndex(unApplicationIndex: Int, pchAppKeyBuffer: String, unAppKeyBufferLen: Int)
            = EVRApplicationError.of(GetApplicationKeyByIndex!!.invoke(unApplicationIndex, pchAppKeyBuffer, unAppKeyBufferLen))

    @JvmField var GetApplicationKeyByIndex: GetApplicationKeyByIndex_callback? = null

    interface GetApplicationKeyByIndex_callback : Callback {
        fun invoke(unApplicationIndex: Int, pchAppKeyBuffer: String, unAppKeyBufferLen: Int): Int
    }

    /** Returns the key of the application for the specified Process Id. The buffer should be at least openvr.k_unMaxApplicationKeyLength in order to fit the key. */
    fun getApplicationKeyByProcessId(unProcessId: Int, pchAppKeyBuffer: String, unAppKeyBufferLen: Int)
            = EVRApplicationError.of(GetApplicationKeyByProcessId!!.invoke(unProcessId, pchAppKeyBuffer, unAppKeyBufferLen))

    @JvmField var GetApplicationKeyByProcessId: GetApplicationKeyByProcessId_callback? = null

    interface GetApplicationKeyByProcessId_callback : Callback {
        fun invoke(unProcessId: Int, pchAppKeyBuffer: String, unAppKeyBufferLen: Int): Int
    }

    /** Launches the application. The existing scene application will exit and then the new application will start.
     *  This call is not valid for dashboard overlay applications. */
    fun launchApplication(pchAppKey: String) = EVRApplicationError.of(LaunchApplication!!.invoke(pchAppKey))

    @JvmField var LaunchApplication: LaunchApplication_callback? = null

    interface LaunchApplication_callback : Callback {
        fun invoke(pchAppKey: String): Int
    }

    /** Launches an instance of an application of value template, with its app key being pchNewAppKey (which must be unique) and optionally override sections
     *  from the manifest file via openvr.AppOverrideKeys_t     */
    fun launchTemplateApplication(pchTemplateAppKey: String, pchNewAppKey: String, pKeys: AppOverrideKeys_t.ByReference, unKeys: Int)
            = EVRApplicationError.of(LaunchTemplateApplication!!.invoke(pchTemplateAppKey, pchNewAppKey, pKeys, unKeys))

    @JvmField var LaunchTemplateApplication: LaunchTemplateApplication_callback? = null

    interface LaunchTemplateApplication_callback : Callback {
        fun invoke(pchTemplateAppKey: String, pchNewAppKey: String, pKeys: AppOverrideKeys_t.ByReference, unKeys: Int): Int
    }

    /** launches the application currently associated with this mime value and passes it the option args, typically the filename or object name of the item being
     *  launched     */
    fun launchApplicationFromMimeType(pchMimeType: String, pchArgs: String) = EVRApplicationError.of(LaunchApplicationFromMimeType!!.invoke(pchMimeType, pchArgs))

    @JvmField var LaunchApplicationFromMimeType: LaunchApplicationFromMimeType_callback? = null

    interface LaunchApplicationFromMimeType_callback : Callback {
        fun invoke(pchMimeType: String, pchArgs: String): Int
    }

    /** Launches the dashboard overlay application if it is not already running. This call is only valid for dashboard overlay applications. */
    fun launchDashboardOverlay(pchAppKey: String) = EVRApplicationError.of(LaunchDashboardOverlay!!.invoke(pchAppKey))

    @JvmField var LaunchDashboardOverlay: LaunchDashboardOverlay_callback? = null

    interface LaunchDashboardOverlay_callback : Callback {
        fun invoke(pchAppKey: String): Int
    }

    /** Cancel a pending launch for an application */
    fun cancelApplicationLaunch(pchAppKey: String) = CancelApplicationLaunch!!.invoke(pchAppKey)

    @JvmField var CancelApplicationLaunch: CancelApplicationLaunch_callback? = null

    interface CancelApplicationLaunch_callback : Callback {
        fun invoke(pchAppKey: String): Boolean
    }

    /** Identifies a running application. openvr.OpenVR can't always tell which process started in response to a URL. This function allows a URL handler (or the process
     *  itself) to identify the app key for the now running application. Passing a process ID of 0 identifies the calling process.
     *  The application must be one that's known to the system via a call to AddApplicationManifest. */
    fun identifyApplication(unProcessId: Int, pchAppKey: String) = EVRApplicationError.of(IdentifyApplication!!.invoke(unProcessId, pchAppKey))

    @JvmField var IdentifyApplication: IdentifyApplication_callback? = null

    interface IdentifyApplication_callback : Callback {
        fun invoke(unProcessId: Int, pchAppKey: String): Int
    }

    /** Returns the process ID for an application. Return 0 if the application was not found or is not running. */
    fun getApplicationProcessId(pchAppKey: String) = GetApplicationProcessId!!.invoke(pchAppKey)

    @JvmField var GetApplicationProcessId: GetApplicationProcessId_callback? = null

    interface GetApplicationProcessId_callback : Callback {
        fun invoke(pchAppKey: String): Int
    }

    /** Returns a string for an applications error */
    fun getApplicationsErrorNameFromEnum(error: EVRApplicationError) = GetApplicationsErrorNameFromEnum!!.invoke(error.i)

    @JvmField var GetApplicationsErrorNameFromEnum: GetApplicationsErrorNameFromEnum_callback? = null

    interface GetApplicationsErrorNameFromEnum_callback : Callback {
        fun invoke(error: Int): String
    }

    // ---------------  Application properties  --------------- //

    /** Returns a value for an application property. The required buffer size to fit this value will be returned. */
    fun getApplicationPropertyString(pchAppKey: String, eProperty: EVRApplicationProperty, pchPropertyValueBuffer: String,
                                     unPropertyValueBufferLen: Int, peError: EVRApplicationError_ByReference? = null)
            = GetApplicationPropertyString!!.invoke(pchAppKey, eProperty.i, pchPropertyValueBuffer, unPropertyValueBufferLen, peError)

    @JvmField var GetApplicationPropertyString: GetApplicationPropertyString_callback? = null

    interface GetApplicationPropertyString_callback : Callback {
        fun invoke(pchAppKey: String, eProperty: Int, pchPropertyValueBuffer: String, unPropertyValueBufferLen: Int,
                   peError: EVRApplicationError_ByReference? = null)
    }

    /** Returns a bool value for an application property. Returns false in all error cases. */
    fun getApplicationPropertyBool(pchAppKey: String, eProperty: EVRApplicationProperty, peError: EVRApplicationError_ByReference? = null)
            = GetApplicationPropertyBool!!.invoke(pchAppKey, eProperty.i, peError)

    @JvmField var GetApplicationPropertyBool: GetApplicationPropertyBool_callback? = null

    interface GetApplicationPropertyBool_callback : Callback {
        fun invoke(pchAppKey: String, eProperty: Int, peError: EVRApplicationError_ByReference? = null): Boolean
    }

    /** Returns a uint64 value for an application property. Returns 0 in all error cases. */
    fun getApplicationPropertyUint64(pchAppKey: String, eProperty: EVRApplicationProperty, peError: EVRApplicationError_ByReference? = null)
            = GetApplicationPropertyUint64!!.invoke(pchAppKey, eProperty.i, peError)

    @JvmField var GetApplicationPropertyUint64: GetApplicationPropertyUint64_callback? = null

    interface GetApplicationPropertyUint64_callback : Callback {
        fun invoke(pchAppKey: String, eProperty: Int, peError: EVRApplicationError_ByReference? = null): Long
    }

    /** Sets the application auto-launch flag. This is only valid for applications which return true for VRApplicationProperty_IsDashboardOverlay_Bool. */
    fun setApplicationAutoLaunch(pchAppKey: String, bAutoLaunch: Boolean) = EVRApplicationError.of(SetApplicationAutoLaunch!!.invoke(pchAppKey, bAutoLaunch))

    @JvmField var SetApplicationAutoLaunch: SetApplicationAutoLaunch_callback? = null

    interface SetApplicationAutoLaunch_callback : Callback {
        fun invoke(pchAppKey: String, bAutoLaunch: Boolean): Int
    }

    /** Gets the application auto-launch flag. This is only valid for applications which return true for VRApplicationProperty_IsDashboardOverlay_Bool. */
    fun getApplicationAutoLaunch(pchAppKey: String) = GetApplicationAutoLaunch!!.invoke(pchAppKey)

    @JvmField var GetApplicationAutoLaunch: GetApplicationAutoLaunch_callback? = null

    interface GetApplicationAutoLaunch_callback : Callback {
        fun invoke(pchAppKey: String): Boolean
    }

    /** Adds this mime-value to the list of supported mime types for this application*/
    fun setDefaultApplicationForMimeType(pchAppKey: String, pchMimeType: String)
            = EVRApplicationError.of(SetDefaultApplicationForMimeType!!.invoke(pchAppKey, pchMimeType))

    @JvmField var SetDefaultApplicationForMimeType: SetDefaultApplicationForMimeType_callback? = null

    interface SetDefaultApplicationForMimeType_callback : Callback {
        fun invoke(pchAppKey: String, pchMimeType: String): Int
    }

    /** return the app key that will open this mime value */
    fun getDefaultApplicationForMimeType(pchMimeType: String, pchAppKeyBuffer: String, unAppKeyBufferLen: Int)
            = GetDefaultApplicationForMimeType!!.invoke(pchMimeType, pchAppKeyBuffer, unAppKeyBufferLen)

    @JvmField var GetDefaultApplicationForMimeType: GetDefaultApplicationForMimeType_callback? = null

    interface GetDefaultApplicationForMimeType_callback : Callback {
        fun invoke(pchMimeType: String, pchAppKeyBuffer: String, unAppKeyBufferLen: Int): Boolean
    }

    /** Get the list of supported mime types for this application, comma-delimited */
    fun getApplicationSupportedMimeTypes(pchAppKey: String, pchMimeTypesBuffer: String, unMimeTypesBuffer: Int)
            = GetApplicationSupportedMimeTypes!!.invoke(pchAppKey, pchMimeTypesBuffer, unMimeTypesBuffer)

    @JvmField var GetApplicationSupportedMimeTypes: GetApplicationSupportedMimeTypes_callback? = null

    interface GetApplicationSupportedMimeTypes_callback : Callback {
        fun invoke(pchAppKey: String, pchMimeTypesBuffer: String, unMimeTypesBuffer: Int): Boolean
    }

    /** Get the list of app-keys that support this mime value, comma-delimited, the return value is number of bytes you need to return the full string */
    fun getApplicationsThatSupportMimeType(pchMimeType: String, pchAppKeysThatSupportBuffer: String, unAppKeysThatSupportBuffer: Int)
            = GetApplicationsThatSupportMimeType!!.invoke(pchMimeType, pchAppKeysThatSupportBuffer, unAppKeysThatSupportBuffer)

    @JvmField var GetApplicationsThatSupportMimeType: GetApplicationsThatSupportMimeType_callback? = null

    interface GetApplicationsThatSupportMimeType_callback : Callback {
        fun invoke(pchMimeType: String, pchAppKeysThatSupportBuffer: String, unAppKeysThatSupportBuffer: Int): Int
    }

    /** Get the args list from an app launch that had the process already running, you call this when you get a VREvent_ApplicationMimeTypeLoad */
    fun getApplicationLaunchArguments(unHandle: Int, pchArgs: String, unArgs: Int) = GetApplicationLaunchArguments!!.invoke(unHandle, pchArgs, unArgs)

    @JvmField var GetApplicationLaunchArguments: GetApplicationLaunchArguments_callback? = null

    interface GetApplicationLaunchArguments_callback : Callback {
        fun invoke(unHandle: Int, pchArgs: String, unArgs: Int): Int
    }

    // ---------------  Transition methods --------------- //

    /** Returns the app key for the application that is starting up */
    fun getStartingApplication(pchAppKeyBuffer: String, unAppKeyBufferLen: Int)
            = EVRApplicationError.of(GetStartingApplication!!.invoke(pchAppKeyBuffer, unAppKeyBufferLen))

    @JvmField var GetStartingApplication: GetStartingApplication_callback? = null

    interface GetStartingApplication_callback : Callback {
        fun invoke(pchAppKeyBuffer: String, unAppKeyBufferLen: Int): Int
    }

    /** Returns the application transition state */
    fun getTransitionState() = GetTransitionState!!.invoke().toEVRApplicationTransitionState()

    @JvmField var GetTransitionState: GetTransitionState_callback? = null

    interface GetTransitionState_callback : Callback {
        fun invoke(): Int
    }

    /** Returns errors that would prevent the specified application from launching immediately. Calling this function will cause the current scene application to
     *  quit, so only call it when you are actually about to launch something else.
     *  What the caller should do about these failures depends on the failure:
     *      VRApplicationError_OldApplicationQuitting       - An existing application has been told to quit. Wait for a VREvent_ProcessQuit and try again.
     *      VRApplicationError_ApplicationAlreadyStarting   - This application is already starting. This is a permanent failure.
     *      VRApplicationError_LaunchInProgress	            - A different application is already starting. This is a permanent failure.
     *      VRApplicationError_None                         - Go ahead and launch. Everything is clear.     */
    fun performApplicationPrelaunchCheck(pchAppKey: String) = EVRApplicationError.of(PerformApplicationPrelaunchCheck!!.invoke(pchAppKey))

    @JvmField var PerformApplicationPrelaunchCheck: PerformApplicationPrelaunchCheck_callback? = null

    interface PerformApplicationPrelaunchCheck_callback : Callback {
        fun invoke(pchAppKey: String): Int
    }

    /** Returns a string for an application transition state */
    fun getApplicationsTransitionStateNameFromEnum(state: EVRApplicationTransitionState) = GetApplicationsTransitionStateNameFromEnum!!.invoke(state.i)

    @JvmField var GetApplicationsTransitionStateNameFromEnum: GetApplicationsTransitionStateNameFromEnum_callback? = null

    interface GetApplicationsTransitionStateNameFromEnum_callback : Callback {
        fun invoke(state: Int): String
    }

    /** Returns true if the outgoing scene app has requested a save prompt before exiting */
    fun isQuitUserPromptRequested() = IsQuitUserPromptRequested!!.invoke()

    @JvmField var IsQuitUserPromptRequested: IsQuitUserPromptRequested_callback? = null

    interface IsQuitUserPromptRequested_callback : Callback {
        fun invoke(): Boolean
    }

    /** Starts a subprocess within the calling application. This suppresses all application transition UI and automatically identifies the new executable as part
     *  of the same application. On success the calling process should exit immediately.
     *  If working directory is NULL or "" the directory portion of the binary path will be the working directory. */
    fun launchInternalProcess(pchBinaryPath: String, pchArguments: String, pchWorkingDirectory: String)
            = EVRApplicationError.of(LaunchInternalProcess!!.invoke(pchBinaryPath, pchArguments, pchWorkingDirectory))

    @JvmField var LaunchInternalProcess: LaunchInternalProcess_callback? = null

    interface LaunchInternalProcess_callback : Callback {
        fun invoke(pchBinaryPath: String, pchArguments: String, pchWorkingDirectory: String): Int
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

    class ByReference : IVRApplications(), Structure.ByReference
    class ByValue : IVRApplications(), Structure.ByValue
}

const val IVRApplications_Version = "FnTable:IVRApplications_006"

enum class EVRSettingsError(@JvmField val i: Int) {

    VRSettingsError_None(0),
    VRSettingsError_IPCFailed(1),
    VRSettingsError_WriteFailed(2),
    VRSettingsError_ReadFailed(3),
    VRSettingsError_JsonParseFailed(4),
    VRSettingsError_UnsetSettingHasNoDefault(5); // This will be returned if the setting does not appear in the appropriate default file and has not been set

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class EVRSettingsError_ByReference(@JvmField var value: EVRSettingsError = EVRSettingsError.VRSettingsError_None) : IntByReference(value.i)

// The maximum length of a settings key
const val k_unMaxSettingsKeyLength = 128

// ivrsettings.h ==================================================================================================================================================

open class IVRSettings : Structure {

    fun getSettingsErrorNameFromEnum(eError: EVRSettingsError) = GetSettingsErrorNameFromEnum!!.invoke(eError.i)
    @JvmField var GetSettingsErrorNameFromEnum: GetSettingsErrorNameFromEnum_callback? = null

    interface GetSettingsErrorNameFromEnum_callback : Callback {
        fun invoke(eError: Int): String
    }


    // Returns true if file sync occurred (force or settings dirty)
    fun sync(bForce: Boolean = false, peError: EVRSettingsError_ByReference? = null) = Sync!!.invoke(bForce, peError)

    @JvmField var Sync: Sync_callback? = null

    interface Sync_callback : Callback {
        fun invoke(bForce: Boolean = false, peError: EVRSettingsError_ByReference? = null): Boolean
    }


    fun setBool(pchSection: String, pchSettingsKey: String, bValue: Boolean, peError: EVRSettingsError_ByReference? = null)
            = SetBool!!.invoke(pchSection, pchSettingsKey, bValue, peError)

    @JvmField var SetBool: SetBool_callback? = null

    interface SetBool_callback : Callback {
        fun invoke(pchSection: String, pchSettingsKey: String, bValue: Boolean, peError: EVRSettingsError_ByReference? = null)
    }


    fun setInt32(pchSection: String, pchSettingsKey: String, nValue: Int, peError: EVRSettingsError_ByReference? = null)
            = SetInt32!!.invoke(pchSection, pchSettingsKey, nValue, peError)

    @JvmField var SetInt32: SetInt32_callback? = null

    interface SetInt32_callback : Callback {
        fun invoke(pchSection: String, pchSettingsKey: String, nValue: Int, peError: EVRSettingsError_ByReference? = null)
    }


    fun setFloat(pchSection: String, pchSettingsKey: String, flValue: Float, peError: EVRSettingsError_ByReference? = null)
            = SetFloat!!.invoke(pchSection, pchSettingsKey, flValue, peError)

    @JvmField var SetFloat: SetFloat_callback? = null

    interface SetFloat_callback : Callback {
        fun invoke(pchSection: String, pchSettingsKey: String, flValue: Float, peError: EVRSettingsError_ByReference? = null)
    }


    fun setString(pchSection: String, pchSettingsKey: String, pchValue: String, peError: EVRSettingsError_ByReference? = null)
            = SetString!!.invoke(pchSection, pchSettingsKey, pchValue, peError)

    @JvmField var SetString: SetString_callback? = null

    interface SetString_callback : Callback {
        fun invoke(pchSection: String, pchSettingsKey: String, pchValue: String, peError: EVRSettingsError_ByReference? = null)
    }


    // Users of the system need to provide a proper default in default.vrsettings in the resources/settings/ directory of either the runtime or the driver_xxx
    // directory. Otherwise the default will be false, 0, 0.0 or ""
    fun getBool(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference? = null)
            = GetBool!!.invoke(pchSection, pchSettingsKey, peError)

    @JvmField var GetBool: GetBool_callback? = null

    interface GetBool_callback : Callback {
        fun invoke(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference? = null): Boolean
    }


    fun getInt32(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference? = null)
            = GetInt32!!.invoke(pchSection, pchSettingsKey, peError)

    @JvmField var GetInt32: GetInt32_callback? = null

    interface GetInt32_callback : Callback {
        fun invoke(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference? = null): Int
    }


    fun getFloat(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference? = null)
            = GetFloat!!.invoke(pchSection, pchSettingsKey, peError)

    @JvmField var GetFloat: GetFloat_callback? = null

    interface GetFloat_callback : Callback {
        fun invoke(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference? = null): Float
    }


    fun getString(pchSection: String, pchSettingsKey: String, pchValue: String, unValueLen: Int, peError: EVRSettingsError_ByReference? = null)
            = GetString!!.invoke(pchSection, pchSettingsKey, pchValue, unValueLen, peError)

    @JvmField var GetString: GetString_callback? = null

    interface GetString_callback : Callback {
        fun invoke(pchSection: String, pchSettingsKey: String, pchValue: String, unValueLen: Int, peError: EVRSettingsError_ByReference? = null)
    }


    fun removeSection(pchSection: String, peError: EVRSettingsError_ByReference? = null) = RemoveSection!!.invoke(pchSection, peError)
    @JvmField var RemoveSection: RemoveSection_callback? = null

    interface RemoveSection_callback : Callback {
        fun invoke(pchSection: String, peError: EVRSettingsError_ByReference? = null)
    }


    fun removeKeyInSection(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference? = null)
            = RemoveKeyInSection!!.invoke(pchSection, pchSettingsKey, peError)

    @JvmField var RemoveKeyInSection: RemoveKeyInSection_callback? = null

    interface RemoveKeyInSection_callback : Callback {
        fun invoke(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference? = null)
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

// ivrchaperone.h =================================================================================================================================================

enum class ChaperoneCalibrationState(@JvmField val i: Int) {

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
    ChaperoneCalibrationState_Error_CollisionBoundsInvalid(204);     // Collision Bounds haven't been calibrated for the current tracking center

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** HIGH LEVEL TRACKING SPACE ASSUMPTIONS:
 * 0,0,0 is the preferred standing area center.
 * 0Y is the floor height.
 * -Z is the preferred forward facing direction. */

open class IVRChaperone : Structure {

    /** Get the current state of Chaperone calibration. This state can change at any time during a session due to physical base station changes. **/
    fun getCalibrationState() = ChaperoneCalibrationState.of(GetCalibrationState!!.invoke())

    @JvmField var GetCalibrationState: GetCalibrationState_callback? = null

    interface GetCalibrationState_callback : Callback {
        fun invoke(): Int
    }

    /** Returns the width and depth of the Play Area (formerly named Soft Bounds) in X and Z.
     * Tracking space center (0,0,0) is the center of the Play Area. **/
    fun getPlayAreaSize(pSizeX: FloatByReference, pSizeZ: FloatByReference) = GetPlayAreaSize!!.invoke(pSizeX, pSizeZ)

    @JvmField var GetPlayAreaSize: GetPlayAreaSize_callback? = null

    interface GetPlayAreaSize_callback : Callback {
        fun invoke(pSizeX: FloatByReference, pSizeZ: FloatByReference): Boolean
    }

    /** Returns the 4 corner positions of the Play Area (formerly named Soft Bounds).
     * Corners are in counter-clockwise order.
     * Standing center (0,0,0) is the center of the Play Area.
     * It's a rectangle.
     * 2 sides are parallel to the X axis and 2 sides are parallel to the Z axis.
     * Height of every corner is 0Y (on the floor). **/
    fun getPlayAreaRect(rect: HmdQuad_t.ByReference) = GetPlayAreaRect!!.invoke(rect)

    @JvmField var GetPlayAreaRect: GetPlayAreaRect_callback? = null

    interface GetPlayAreaRect_callback : Callback {
        fun invoke(rect: HmdQuad_t.ByReference): Boolean
    }

    /** Reload Chaperone data from the .vrchap file on disk. */
    fun reloadInfo() = ReloadInfo!!.invoke()

    @JvmField var ReloadInfo: ReloadInfo_callback? = null

    interface ReloadInfo_callback : Callback {
        fun invoke()
    }

    /** Optionally give the chaperone system a hit about the color and brightness in the scene **/
    fun setSceneColor(color: HmdColor_t) = SetSceneColor!!.invoke(color)

    @JvmField var SetSceneColor: SetSceneColor_callback? = null

    interface SetSceneColor_callback : Callback {
        fun invoke(color: HmdColor_t)
    }

    /** Get the current chaperone bounds draw color and brightness **/
    fun getBoundsColor(pOutputColorArray: HmdColor_t.ByReference, nNumOutputColors: Int, flCollisionBoundsFadeDistance: Float,
                       pOutputCameraColor: HmdColor_t.ByReference)
            = GetBoundsColor!!.invoke(pOutputColorArray, nNumOutputColors, flCollisionBoundsFadeDistance, pOutputCameraColor)

    @JvmField var GetBoundsColor: GetBoundsColor_callback? = null

    interface GetBoundsColor_callback : Callback {
        fun invoke(pOutputColorArray: HmdColor_t.ByReference, nNumOutputColors: Int, flCollisionBoundsFadeDistance: Float,
                   pOutputCameraColor: HmdColor_t.ByReference)
    }

    /** Determine whether the bounds are showing right now **/
    fun areBoundsVisible() = AreBoundsVisible!!.invoke()

    @JvmField var AreBoundsVisible: AreBoundsVisible_callback? = null

    interface AreBoundsVisible_callback : Callback {
        fun invoke(): Boolean
    }

    /** Force the bounds to show, mostly for utilities **/
    fun forceBoundsVisible(bForce: Boolean) = ForceBoundsVisible!!.invoke(bForce)

    @JvmField var ForceBoundsVisible: ForceBoundsVisible_callback? = null

    interface ForceBoundsVisible_callback : Callback {
        fun invoke(bForce: Boolean)
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

// ivrchaperonesetup.h ============================================================================================================================================

enum class EChaperoneConfigFile(@JvmField val i: Int) {

    EChaperoneConfigFile_Live(1), //    The live chaperone config, used by most applications and games
    EChaperoneConfigFile_Temp(2);    //  The temporary chaperone config, used to live-preview collision bounds in room setup

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EChaperoneImportFlags(@JvmField val i: Int) {

    EChaperoneImport_BoundsOnly(0x0001);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Manages the working copy of the chaperone info. By default this will be the same as the live copy. Any changes made with this interface will stay in the
 *  working copy until CommitWorkingCopy() is called, at which point the working copy and the live copy will be the same again. */
open class IVRChaperoneSetup : Structure {

    /** Saves the current working copy to disk */
    fun commitWorkingCopy(configFile: EChaperoneConfigFile) = CommitWorkingCopy!!.invoke(configFile.i)

    @JvmField var CommitWorkingCopy: CommitWorkingCopy_callback? = null

    interface CommitWorkingCopy_callback : Callback {
        fun invoke(configFile: Int): Boolean
    }

    /** Reverts the working copy to match the live chaperone calibration.
     *  To modify existing data this MUST be do WHILE getting a non-error ChaperoneCalibrationStatus.
     *  Only after this should you do gets and sets on the existing data. */
    fun revertWorkingCopy() = RevertWorkingCopy!!.invoke()

    @JvmField var RevertWorkingCopy: RevertWorkingCopy_callback? = null

    interface RevertWorkingCopy_callback : Callback {
        fun invoke()
    }

    /** Returns the width and depth of the Play Area (formerly named Soft Bounds) in X and Z from the working copy.
     *  Tracking space center (0,0,0) is the center of the Play Area. */
    fun getWorkingPlayAreaSize(pSizeX: FloatByReference, pSizeZ: FloatByReference) = GetWorkingPlayAreaSize!!.invoke(pSizeX, pSizeZ)

    @JvmField var GetWorkingPlayAreaSize: GetWorkingPlayAreaSize_callback? = null

    interface GetWorkingPlayAreaSize_callback : Callback {
        fun invoke(pSizeX: FloatByReference, pSizeZ: FloatByReference): Boolean
    }

    /** Returns the 4 corner positions of the Play Area (formerly named Soft Bounds) from the working copy.
     *  Corners are in clockwise order.
     *  Tracking space center (0,0,0) is the center of the Play Area.
     *  It's a rectangle.
     *  2 sides are parallel to the X axis and 2 sides are parallel to the Z axis.
     *  Height of every corner is 0Y (on the floor). **/
    fun getWorkingPlayAreaRect(rect: HmdQuad_t.ByReference) = GetWorkingPlayAreaRect!!.invoke(rect)

    @JvmField var GetWorkingPlayAreaRect: GetWorkingPlayAreaRect_callback? = null

    interface GetWorkingPlayAreaRect_callback : Callback {
        fun invoke(rect: HmdQuad_t.ByReference): Boolean
    }

    /** Returns the number of Quads if the buffer points to null. Otherwise it returns Quads
     * into the buffer up to the max specified from the working copy. */
    fun getWorkingCollisionBoundsInfo(pQuadsBuffer: HmdQuad_t.ByReference, punQuadsCount: IntByReference)
            = GetWorkingCollisionBoundsInfo!!.invoke(pQuadsBuffer, punQuadsCount)

    @JvmField var GetWorkingCollisionBoundsInfo: GetWorkingCollisionBoundsInfo_callback? = null

    interface GetWorkingCollisionBoundsInfo_callback : Callback {
        fun invoke(pQuadsBuffer: HmdQuad_t.ByReference, punQuadsCount: IntByReference): Boolean
    }

    /** Returns the number of Quads if the buffer points to null. Otherwise it returns Quads into the buffer up to the max specified. */
    fun getLiveCollisionBoundsInfo(pQuadsBuffer: HmdQuad_t.ByReference, punQuadsCount: IntByReference)
            = GetLiveCollisionBoundsInfo!!.invoke(pQuadsBuffer, punQuadsCount)

    @JvmField var GetLiveCollisionBoundsInfo: GetLiveCollisionBoundsInfo_callback? = null

    interface GetLiveCollisionBoundsInfo_callback : Callback {
        fun invoke(pQuadsBuffer: HmdQuad_t.ByReference, punQuadsCount: IntByReference): Boolean
    }

    /** Returns the preferred seated position from the working copy. */
    fun getWorkingSeatedZeroPoseToRawTrackingPose(pmatSeatedZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference)
            = GetWorkingSeatedZeroPoseToRawTrackingPose!!.invoke(pmatSeatedZeroPoseToRawTrackingPose)

    @JvmField var GetWorkingSeatedZeroPoseToRawTrackingPose: GetWorkingSeatedZeroPoseToRawTrackingPose_callback? = null

    interface GetWorkingSeatedZeroPoseToRawTrackingPose_callback : Callback {
        fun invoke(pmatSeatedZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference): Boolean
    }

    /** Returns the standing origin from the working copy. */
    fun getWorkingStandingZeroPoseToRawTrackingPose(pmatStandingZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference)
            = GetWorkingStandingZeroPoseToRawTrackingPose!!.invoke(pmatStandingZeroPoseToRawTrackingPose)

    @JvmField var GetWorkingStandingZeroPoseToRawTrackingPose: GetWorkingStandingZeroPoseToRawTrackingPose_callback? = null

    interface GetWorkingStandingZeroPoseToRawTrackingPose_callback : Callback {
        fun invoke(pmatStandingZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference): Boolean
    }

    /** Sets the Play Area in the working copy. */
    fun setWorkingPlayAreaSize(sizeX: Float, sizeZ: Float) = SetWorkingPlayAreaSize!!.invoke(sizeX, sizeZ)

    @JvmField var SetWorkingPlayAreaSize: SetWorkingPlayAreaSize_callback? = null

    interface SetWorkingPlayAreaSize_callback : Callback {
        fun invoke(sizeX: Float, sizeZ: Float)
    }

    /** Sets the Collision Bounds in the working copy. */
    fun setWorkingCollisionBoundsInfo(pQuadsBuffer: HmdQuad_t.ByReference, unQuadsCount: Int) = SetWorkingCollisionBoundsInfo!!.invoke(pQuadsBuffer, unQuadsCount)

    @JvmField var SetWorkingCollisionBoundsInfo: SetWorkingCollisionBoundsInfo_callback? = null

    interface SetWorkingCollisionBoundsInfo_callback : Callback {
        fun invoke(pQuadsBuffer: HmdQuad_t.ByReference, unQuadsCount: Int)
    }

    /** Sets the preferred seated position in the working copy. */
    fun setWorkingSeatedZeroPoseToRawTrackingPose(pmatSeatedZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference)
            = SetWorkingSeatedZeroPoseToRawTrackingPose!!.invoke(pmatSeatedZeroPoseToRawTrackingPose)

    @JvmField var SetWorkingSeatedZeroPoseToRawTrackingPose: SetWorkingSeatedZeroPoseToRawTrackingPose_callback? = null

    interface SetWorkingSeatedZeroPoseToRawTrackingPose_callback : Callback {
        fun invoke(pMatSeatedZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference)
    }

    /** Sets the preferred standing position in the working copy. */
    fun setWorkingStandingZeroPoseToRawTrackingPose(pmatStandingZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference)
            = SetWorkingStandingZeroPoseToRawTrackingPose!!.invoke(pmatStandingZeroPoseToRawTrackingPose)

    @JvmField var SetWorkingStandingZeroPoseToRawTrackingPose: SetWorkingStandingZeroPoseToRawTrackingPose_callback? = null

    interface SetWorkingStandingZeroPoseToRawTrackingPose_callback : Callback {
        fun invoke(pMatStandingZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference)
    }

    /** Tear everything down and reload it from the file on disk */
    fun reloadFromDisk(configFile: EChaperoneConfigFile) = ReloadFromDisk!!.invoke(configFile.i)

    @JvmField var ReloadFromDisk: ReloadFromDisk_callback? = null

    interface ReloadFromDisk_callback : Callback {
        fun invoke(configFile: Int)
    }

    /** Returns the preferred seated position. */
    fun getLiveSeatedZeroPoseToRawTrackingPose(pmatSeatedZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference)
            = GetLiveSeatedZeroPoseToRawTrackingPose!!.invoke(pmatSeatedZeroPoseToRawTrackingPose)

    @JvmField var GetLiveSeatedZeroPoseToRawTrackingPose: GetLiveSeatedZeroPoseToRawTrackingPose_callback? = null

    interface GetLiveSeatedZeroPoseToRawTrackingPose_callback : Callback {
        fun invoke(pmatSeatedZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference): Boolean
    }


    fun setWorkingCollisionBoundsTagsInfo(pTagsBuffer: ByteByReference, unTagCount: Int) =
            SetWorkingCollisionBoundsTagsInfo!!.invoke(pTagsBuffer, unTagCount)

    @JvmField var SetWorkingCollisionBoundsTagsInfo: SetWorkingCollisionBoundsTagsInfo_callback? = null

    interface SetWorkingCollisionBoundsTagsInfo_callback : Callback {
        fun invoke(pTagsBuffer: ByteByReference, unTagCount: Int)
    }


    fun getLiveCollisionBoundsTagsInfo(pTagsBuffer: ByteByReference, punTagCount: IntByReference)
            = GetLiveCollisionBoundsTagsInfo!!.invoke(pTagsBuffer, punTagCount)

    @JvmField var GetLiveCollisionBoundsTagsInfo: GetLiveCollisionBoundsTagsInfo_callback? = null

    interface GetLiveCollisionBoundsTagsInfo_callback : Callback {
        fun invoke(pTagsBuffer: ByteByReference, punTagCount: IntByReference): Boolean
    }


    fun setWorkingPhysicalBoundsInfo(pQuadsBuffer: HmdQuad_t.ByReference, unQuadsCount: Int) = SetWorkingPhysicalBoundsInfo!!.invoke(pQuadsBuffer, unQuadsCount)
    @JvmField var SetWorkingPhysicalBoundsInfo: SetWorkingPhysicalBoundsInfo_callback? = null

    interface SetWorkingPhysicalBoundsInfo_callback : Callback {
        fun invoke(pQuadsBuffer: HmdQuad_t.ByReference, unQuadsCount: Int): Boolean
    }


    fun getLivePhysicalBoundsInfo(pQuadsBuffer: HmdQuad_t.ByReference, punQuadsCount: IntByReference)
            = GetLivePhysicalBoundsInfo!!.invoke(pQuadsBuffer, punQuadsCount)

    @JvmField var GetLivePhysicalBoundsInfo: GetLivePhysicalBoundsInfo_callback? = null

    interface GetLivePhysicalBoundsInfo_callback : Callback {
        fun invoke(pQuadsBuffer: HmdQuad_t.ByReference, punQuadsCount: IntByReference): Boolean
    }


    fun exportLiveToBuffer(pBuffer: String, pnBufferLength: IntByReference) = ExportLiveToBuffer!!.invoke(pBuffer, pnBufferLength)
    @JvmField var ExportLiveToBuffer: ExportLiveToBuffer_callback? = null

    interface ExportLiveToBuffer_callback : Callback {
        fun invoke(pBuffer: String, pnBufferLength: IntByReference): Boolean
    }


    fun importFromBufferToWorking(pBuffer: String, nImportFlags: Int) = ImportFromBufferToWorking!!.invoke(pBuffer, nImportFlags)
    @JvmField var ImportFromBufferToWorking: ImportFromBufferToWorking_callback? = null

    interface ImportFromBufferToWorking_callback : Callback {
        fun invoke(pBuffer: String, nImportFlags: Int): Boolean
    }


    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("CommitWorkingCopy", "RevertWorkingCopy", "GetWorkingPlayAreaSize", "GetWorkingPlayAreaRect",
            "GetWorkingCollisionBoundsInfo", "GetLiveCollisionBoundsInfo", "GetWorkingSeatedZeroPoseToRawTrackingPose",
            "GetWorkingStandingZeroPoseToRawTrackingPose", "SetWorkingPlayAreaSize", "SetWorkingCollisionBoundsInfo", "SetWorkingSeatedZeroPoseToRawTrackingPose",
            "SetWorkingStandingZeroPoseToRawTrackingPose", "ReloadFromDisk", "GetLiveSeatedZeroPoseToRawTrackingPose", "SetWorkingCollisionBoundsTagsInfo",
            "GetLiveCollisionBoundsTagsInfo", "SetWorkingPhysicalBoundsInfo", "GetLivePhysicalBoundsInfo", "ExportLiveToBuffer", "ImportFromBufferToWorking")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRChaperoneSetup(), Structure.ByReference
    class ByValue : IVRChaperoneSetup(), Structure.ByValue
}

const val IVRChaperoneSetup_Version = "FnTable:IVRChaperoneSetup_005"

// ivrcompositor.h ================================================================================================================================================

/** Errors that can occur with the VR compositor */
enum class EVRCompositorError(@JvmField val i: Int) {

    VRCompositorError_None(0),
    VRCompositorError_RequestFailed(1),
    VRCompositorError_IncompatibleVersion(100),
    VRCompositorError_DoNotHaveFocus(101),
    VRCompositorError_InvalidTexture(102),
    VRCompositorError_IsNotSceneApplication(103),
    VRCompositorError_TextureIsOnWrongDevice(104),
    VRCompositorError_TextureUsesUnsupportedFormat(105),
    VRCompositorError_SharedTexturesNotSupported(106),
    VRCompositorError_IndexOutOfRange(107);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

const val VRCompositor_ReprojectionReason_Cpu = 0x01
const val VRCompositor_ReprojectionReason_Gpu = 0x02

/** Provides a single frame's timing information to the app */
open class Compositor_FrameTiming : Structure {

    var m_nSize = 0 // Set to sizeof( openvr.Compositor_FrameTiming )
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
    var m_flSubmitFrameMs = 0f // time spent in openvr.IVRCompositor::Submit (not near-zero indicates driver issue)

    /** The following are all relative to this frame's SystemTimeInSeconds */
    var m_flWaitGetPosesCalledMs = 0f
    var m_flNewPosesReadyMs = 0f
    var m_flNewFrameReadyMs = 0f // second call to openvr.IVRCompositor::Submit
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
    fun setTrackingSpace(eOrigin: ETrackingUniverseOrigin) = SetTrackingSpace!!.invoke(eOrigin.i)

    @JvmField var SetTrackingSpace: SetTrackingSpace_callback? = null

    interface SetTrackingSpace_callback : Callback {
        fun invoke(eOrigin: Int)
    }

    /** Gets current tracking space returned by WaitGetPoses */
    fun getTrackingSpace() = ETrackingUniverseOrigin.of(GetTrackingSpace!!.invoke())

    @JvmField var GetTrackingSpace: GetTrackingSpace_callback? = null

    interface GetTrackingSpace_callback : Callback {
        fun invoke(): Int
    }

    /** Returns pose(s) to use to render scene (and optionally poses predicted two frames out for gameplay). */
    fun waitGetPoses(pRenderPoseArray: TrackedDevicePose_t.ByReference, unRenderPoseArrayCount: Int, pGamePoseArray: TrackedDevicePose_t.ByReference,
                     unGamePoseArrayCount: Int) = WaitGetPoses!!.invoke(pRenderPoseArray, unRenderPoseArrayCount, pGamePoseArray, unGamePoseArrayCount)

    @JvmField var WaitGetPoses: WaitGetPoses_callback? = null

    interface WaitGetPoses_callback : Callback {
        fun invoke(pRenderPoseArray: TrackedDevicePose_t.ByReference, unRenderPoseArrayCount: Int, pGamePoseArray: TrackedDevicePose_t.ByReference, unGamePoseArrayCount: Int): Int
    }

    /** Get the last set of poses returned by WaitGetPoses. */
    fun getLastPoses(pRenderPoseArray: TrackedDevicePose_t.ByReference, unRenderPoseArrayCount: Int, pGamePoseArray: TrackedDevicePose_t.ByReference,
                     unGamePoseArrayCount: Int) = GetLastPoses!!.invoke(pRenderPoseArray, unRenderPoseArrayCount, pGamePoseArray, unGamePoseArrayCount)

    @JvmField var GetLastPoses: GetLastPoses_callback? = null

    interface GetLastPoses_callback : Callback {
        fun invoke(pRenderPoseArray: TrackedDevicePose_t.ByReference, unRenderPoseArrayCount: Int, pGamePoseArray: TrackedDevicePose_t.ByReference,
                   unGamePoseArrayCount: Int): Int
    }

    /** Interface for accessing last set of poses returned by WaitGetPoses one at a time.
     *  Returns VRCompositorError_IndexOutOfRange if unDeviceIndex not less than openvr.k_unMaxTrackedDeviceCount otherwise VRCompositorError_None.
     *  It is okay to pass NULL for either pose if you only want one of the values. */
    fun getLastPoseForTrackedDeviceIndex(unDeviceIndex: TrackedDeviceIndex_t, pOutputPose: TrackedDevicePose_t.ByReference,
                                         pOutputGamePose: TrackedDevicePose_t.ByReference)
            = EVRCompositorError.of(GetLastPoseForTrackedDeviceIndex!!.invoke(unDeviceIndex, pOutputPose, pOutputGamePose))

    @JvmField var GetLastPoseForTrackedDeviceIndex: GetLastPoseForTrackedDeviceIndex_callback? = null

    interface GetLastPoseForTrackedDeviceIndex_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t, pOutputPose: TrackedDevicePose_t.ByReference, pOutputGamePose: TrackedDevicePose_t.ByReference): Int
    }

    /** Updated scene texture to display. If pBounds is NULL the entire texture will be used.  If called from an OpenGL app, consider adding a glFlush after
     *  Submitting both frames to signal the driver to start processing, otherwise it may wait until the command buffer fills up, causing the app to miss frames.
     *
     *  OpenGL dirty state:
     *	    glBindTexture     */
    fun submit(eEye: EVREye, pTexture: Texture_t.ByReference, pBounds: VRTextureBounds_t.ByReference? = null,
               nSubmitFlags: EVRSubmitFlags = EVRSubmitFlags.Submit_Default) = EVRCompositorError.of(Submit!!.invoke(eEye.i, pTexture, pBounds, nSubmitFlags.i))

    @JvmField var Submit: Submit_callback? = null

    interface Submit_callback : Callback {
        fun invoke(eEye: Int, pTexture: Texture_t.ByReference, pBounds: VRTextureBounds_t.ByReference? = null,
                   nSubmitFlags: Int = EVRSubmitFlags.Submit_Default.i): Int
    }

    /** Clears the frame that was sent with the last call to Submit. This will cause the compositor to show the grid until Submit is called again. */
    fun clearLastSubmittedFrame() = ClearLastSubmittedFrame!!.invoke()

    @JvmField var ClearLastSubmittedFrame: ClearLastSubmittedFrame_callback? = null

    interface ClearLastSubmittedFrame_callback : Callback {
        fun invoke()
    }

    /** Call immediately after presenting your app's window (i.e. companion window) to unblock the compositor.
     *  This is an optional call, which only needs to be used if you can't instead call WaitGetPoses immediately after Present.
     *  For example, if your engine's render and game loop are not on separate threads, or blocking the render thread until 3ms before the next vsync would
     *  introduce a deadlock of some sort.  This function tells the compositor that you have finished all rendering after having Submitted buffers for both
     *  eyes, and it is free to start its rendering work.  This should only be called from the same thread you are rendering on. */
    fun postPresentHandoff() = PostPresentHandoff!!.invoke()

    @JvmField var PostPresentHandoff: PostPresentHandoff_callback? = null

    interface PostPresentHandoff_callback : Callback {
        fun invoke()
    }

    /** Returns true if timing data is filled it.  Sets oldest timing info if nFramesAgo is larger than the stored history.
     *  Be sure to set timing.size = sizeof(openvr.Compositor_FrameTiming) on struct passed in before calling this function. */
    fun getFrameTiming(pTiming: Compositor_FrameTiming.ByReference, unFramesAgo: Int = 0) = GetFrameTiming!!.invoke(pTiming, unFramesAgo)

    @JvmField var GetFrameTiming: GetFrameTiming_callback? = null

    interface GetFrameTiming_callback : Callback {
        fun invoke(pTiming: Compositor_FrameTiming.ByReference, unFramesAgo: Int = 0): Boolean
    }

    /** Returns the time in seconds left in the current (as identified by FrameTiming's frameIndex) frame.
     *  Due to "running start", this value may roll over to the next frame before ever reaching 0.0. */
    fun getFrameTimeRemaining() = GetFrameTimeRemaining!!.invoke()

    @JvmField var GetFrameTimeRemaining: GetFrameTimeRemaining_callback? = null

    interface GetFrameTimeRemaining_callback : Callback {
        fun invoke(): Float
    }

    /** Fills out stats accumulated for the last connected application.  Pass in sizeof( openvr.Compositor_CumulativeStats ) as second parameter. */
    fun getCumulativeStats(pStats: Compositor_CumulativeStats.ByReference, nStatsSizeInBytes: Int) = GetCumulativeStats!!.invoke(pStats, nStatsSizeInBytes)

    @JvmField var GetCumulativeStats: GetCumulativeStats_callback? = null

    interface GetCumulativeStats_callback : Callback {
        fun invoke(pStats: Compositor_CumulativeStats.ByReference, nStatsSizeInBytes: Int)
    }

    /** Fades the view on the HMD to the specified color. The fade will take fSeconds, and the color values are between 0.0 and 1.0. This color is faded on top
     *  of the scene based on the alpha parameter. Removing the fade color instantly would be FadeToColor( 0.0, 0.0, 0.0, 0.0, 0.0 ).
     *  Values are in un-premultiplied alpha space. */
    fun fadeToColor(fSeconds: Float, fRed: Float, fGreen: Float, fBlue: Float, fAlpha: Float, bBackground: Boolean = false)
            = FadeToColor!!.invoke(fSeconds, fRed, fGreen, fBlue, fAlpha, bBackground)

    @JvmField var FadeToColor: FadeToColor_callback? = null

    interface FadeToColor_callback : Callback {
        fun invoke(fSeconds: Float, fRed: Float, fGreen: Float, fBlue: Float, fAlpha: Float, bBackground: Boolean = false)
    }

    /** Fading the Grid in or out in fSeconds */
    fun fadeGrid(fSeconds: Float, bFadeIn: Boolean) = FadeGrid!!.invoke(fSeconds, bFadeIn)

    @JvmField var FadeGrid: FadeGrid_callback? = null

    interface FadeGrid_callback : Callback {
        fun invoke(fSeconds: Float, bFadeIn: Boolean)
    }

    /** Override the skybox used in the compositor (e.g. for during level loads when the app can't feed scene images fast enough)
     *  Order is Front, Back, Left, Right, Top, Bottom.  If only a single texture is passed, it is assumed in lat-long format.
     *  If two are passed, it is assumed a lat-long stereo pair. */
    fun setSkyboxOverride(pTextures: Texture_t.ByReference, unTextureCount: Int) = EVRCompositorError.of(SetSkyboxOverride!!.invoke(pTextures, unTextureCount))

    @JvmField var SetSkyboxOverride: SetSkyboxOverride_callback? = null

    interface SetSkyboxOverride_callback : Callback {
        fun invoke(pTextures: Texture_t.ByReference, unTextureCount: Int): Int
    }

    /** Resets compositor skybox back to defaults. */
    fun clearSkyboxOverride() = ClearSkyboxOverride!!.invoke()

    @JvmField var ClearSkyboxOverride: ClearSkyboxOverride_callback? = null

    interface ClearSkyboxOverride_callback : Callback {
        fun invoke()
    }

    /** Brings the compositor window to the front. This is useful for covering any other window that may be on the HMD and is obscuring the compositor window. */
    fun compositorBringToFront() = CompositorBringToFront!!.invoke()

    @JvmField var CompositorBringToFront: CompositorBringToFront_callback? = null

    interface CompositorBringToFront_callback : Callback {
        fun invoke()
    }

    /** Pushes the compositor window to the back. This is useful for allowing other applications to draw directly to the HMD. */
    fun compositorGoToBack() = CompositorGoToBack!!.invoke()

    @JvmField var CompositorGoToBack: CompositorGoToBack_callback? = null

    interface CompositorGoToBack_callback : Callback {
        fun invoke()
    }

    /** Tells the compositor process to clean up and exit. You do not need to call this function at shutdown. Under normal circumstances the compositor will
     *  manage its own life cycle based on what applications are running. */
    fun compositorQuit() = CompositorQuit!!.invoke()

    @JvmField var CompositorQuit: CompositorQuit_callback? = null

    interface CompositorQuit_callback : Callback {
        fun invoke()
    }

    /** Return whether the compositor is fullscreen */
    fun isFullscreen() = IsFullscreen!!.invoke()

    @JvmField var IsFullscreen: IsFullscreen_callback? = null

    interface IsFullscreen_callback : Callback {
        fun invoke(): Boolean
    }

    /** Returns the process ID of the process that is currently rendering the scene */
    fun getCurrentSceneFocusProcess() = GetCurrentSceneFocusProcess!!.invoke()

    @JvmField var GetCurrentSceneFocusProcess: GetCurrentSceneFocusProcess_callback? = null

    interface GetCurrentSceneFocusProcess_callback : Callback {
        fun invoke(): Int
    }

    /** Returns the process ID of the process that rendered the last frame (or 0 if the compositor itself rendered the frame.)
     *  Returns 0 when fading out from an app and the app's process Id when fading into an app. */
    fun getLastFrameRenderer() = GetLastFrameRenderer!!.invoke()

    @JvmField var GetLastFrameRenderer: GetLastFrameRenderer_callback? = null

    interface GetLastFrameRenderer_callback : Callback {
        fun invoke(): Int
    }

    /** Returns true if the current process has the scene focus */
    fun canRenderScene() = CanRenderScene!!.invoke()

    @JvmField var CanRenderScene: CanRenderScene_callback? = null

    interface CanRenderScene_callback : Callback {
        fun invoke(): Boolean
    }

    /** Creates a window on the primary monitor to display what is being shown in the headset. */
    fun showMirrorWindow() = ShowMirrorWindow!!.invoke()

    @JvmField var ShowMirrorWindow: ShowMirrorWindow_callback? = null

    interface ShowMirrorWindow_callback : Callback {
        fun invoke()
    }

    /** Closes the mirror window. */
    fun hideMirrorWindow() = HideMirrorWindow!!.invoke()

    @JvmField var HideMirrorWindow: HideMirrorWindow_callback? = null

    interface HideMirrorWindow_callback : Callback {
        fun invoke()
    }

    /** Returns true if the mirror window is shown. */
    fun isMirrorWindowVisible() = IsMirrorWindowVisible!!.invoke()

    @JvmField var IsMirrorWindowVisible: IsMirrorWindowVisible_callback? = null

    interface IsMirrorWindowVisible_callback : Callback {
        fun invoke(): Boolean
    }

    /** Writes all images that the compositor knows about (including overlays) to a 'screenshots' folder in the SteamVR runtime root. */
    fun compositorDumpImages() = CompositorDumpImages!!.invoke()

    @JvmField var CompositorDumpImages: CompositorDumpImages_callback? = null

    interface CompositorDumpImages_callback : Callback {
        fun invoke()
    }

    /** Let an app know it should be rendering with low resources. */
    fun shouldAppRenderWithLowResources() = ShouldAppRenderWithLowResources!!.invoke()

    @JvmField var ShouldAppRenderWithLowResources: ShouldAppRenderWithLowResources_callback? = null

    interface ShouldAppRenderWithLowResources_callback : Callback {
        fun invoke(): Boolean
    }

    /** Override interleaved reprojection logic to force on. */
    fun forceInterleavedReprojectionOn(bOverride: Boolean) = ForceInterleavedReprojectionOn!!.invoke(bOverride)

    @JvmField var ForceInterleavedReprojectionOn: ForceInterleavedReprojectionOn_callback? = null

    interface ForceInterleavedReprojectionOn_callback : Callback {
        fun invoke(bOverride: Boolean)
    }

    /** Force reconnecting to the compositor process. */
    fun forceReconnectProcess() = ForceReconnectProcess!!.invoke()

    @JvmField var ForceReconnectProcess: ForceReconnectProcess_callback? = null

    interface ForceReconnectProcess_callback : Callback {
        fun invoke()
    }

    /** Temporarily suspends rendering (useful for finer control over scene transitions). */
    fun suspendRendering(bSuspend: Boolean) = SuspendRendering!!.invoke(bSuspend)

    @JvmField var SuspendRendering: SuspendRendering_callback? = null

    interface SuspendRendering_callback : Callback {
        fun invoke(bSuspend: Boolean)
    }

    /** Opens a shared D3D11 texture with the undistorted composited image for each eye. */
    fun getMirrorTextureD3D11(eEye: EVREye, pD3D11DeviceOrResource: Pointer, ppD3D11ShaderResourceView: PointerByReference)
            = EVRCompositorError.of(GetMirrorTextureD3D11!!.invoke(eEye.i, pD3D11DeviceOrResource, ppD3D11ShaderResourceView))

    @JvmField var GetMirrorTextureD3D11: GetMirrorTextureD3D11_callback? = null

    interface GetMirrorTextureD3D11_callback : Callback {
        fun invoke(eEye: Int, pD3D11DeviceOrResource: Pointer, ppD3D11ShaderResourceView: PointerByReference): Int
    }

    /** Access to mirror textures from OpenGL. */
    fun getMirrorTextureGL(eEye: EVREye, pglTextureId: glUInt_t_ByReference, pglSharedTextureHandle: glSharedTextureHandle_t_ByReference)
            = GetMirrorTextureGL!!.invoke(eEye.i, pglTextureId, pglSharedTextureHandle)

    @JvmField var GetMirrorTextureGL: GetMirrorTextureGL_callback? = null

    interface GetMirrorTextureGL_callback : Callback {
        fun invoke(eEye: Int, pglTextureId: glUInt_t_ByReference, pglSharedTextureHandle: glSharedTextureHandle_t_ByReference): Int
    }


    fun releaseSharedGLTexture(glTextureId: glUInt_t, glSharedTextureHandle: glSharedTextureHandle_t) =
            ReleaseSharedGLTexture!!.invoke(glTextureId, glSharedTextureHandle)

    @JvmField var ReleaseSharedGLTexture: ReleaseSharedGLTexture_callback? = null

    interface ReleaseSharedGLTexture_callback : Callback {
        fun invoke(glTextureId: glUInt_t, glSharedTextureHandle: glSharedTextureHandle_t): Boolean
    }


    fun lockGLSharedTextureForAccess(glSharedTextureHandle: glSharedTextureHandle_t) = LockGLSharedTextureForAccess!!.invoke(glSharedTextureHandle)
    @JvmField var LockGLSharedTextureForAccess: LockGLSharedTextureForAccess_callback? = null

    interface LockGLSharedTextureForAccess_callback : Callback {
        fun invoke(glSharedTextureHandle: glSharedTextureHandle_t)
    }


    fun unlockGLSharedTextureForAccess(glSharedTextureHandle: glSharedTextureHandle_t) = UnlockGLSharedTextureForAccess!!.invoke(glSharedTextureHandle)
    @JvmField var UnlockGLSharedTextureForAccess: UnlockGLSharedTextureForAccess_callback? = null

    interface UnlockGLSharedTextureForAccess_callback : Callback {
        fun invoke(glSharedTextureHandle: glSharedTextureHandle_t)
    }

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("SetTrackingSpace", "GetTrackingSpace", "WaitGetPoses", "GetLastPoses",
            "GetLastPoseForTrackedDeviceIndex", "Submit", "ClearLastSubmittedFrame", "PostPresentHandoff", "GetFrameTiming", "GetFrameTimeRemaining",
            "GetCumulativeStats", "FadeToColor", "FadeGrid", "SetSkyboxOverride", "ClearSkyboxOverride", "CompositorBringToFront", "CompositorGoToBack",
            "CompositorQuit", "IsFullscreen", "GetCurrentSceneFocusProcess", "GetLastFrameRenderer", "CanRenderScene", "ShowMirrorWindow", "HideMirrorWindow",
            "IsMirrorWindowVisible", "CompositorDumpImages", "ShouldAppRenderWithLowResources", "ForceInterleavedReprojectionOn", "ForceReconnectProcess",
            "SuspendRendering", "GetMirrorTextureD3D11", "GetMirrorTextureGL", "ReleaseSharedGLTexture", "LockGLSharedTextureForAccess",
            "UnlockGLSharedTextureForAccess")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRCompositor(), Structure.ByReference
    class ByValue : IVRCompositor(), Structure.ByValue
}

const val IVRCompositor_Version = "FnTable:IVRCompositor_016"

// ivrnotifications.h =============================================================================================================================================

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

/** Be aware that the notification value is used as 'priority' to pick the next notification */
enum class EVRNotificationType(@JvmField val i: Int) {

    /** Transient notifications are automatically hidden after a period of time set by the user.
     *  They are used for things like information and chat messages that do not require user interaction. */
    EVRNotificationType_Transient(0),

    /** Persistent notifications are shown to the user until they are hidden by calling RemoveNotification().
     *  They are used for things like phone calls and alarms that require user interaction. */
    EVRNotificationType_Persistent(1),

    /** System notifications are shown no matter what. It is expected), that the ulUserValue is used as ID.
     *  If there is already a system notification in the queue with that ID it is not accepted into the queue to prevent spamming with system notification */
    EVRNotificationType_Transient_SystemWithUserValue(2);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EVRNotificationStyle(@JvmField val i: Int) {

    /** Creates a notification with minimal external styling. */
    EVRNotificationStyle_None(0),

    /** Used for notifications about overlay-level status. In Steam this is used for events like downloads completing. */
    EVRNotificationStyle_Application(100),

    /** Used for notifications about contacts that are unknown or not available. In Steam this is used for friend invitations and offline friends. */
    EVRNotificationStyle_Contact_Disabled(200),

    /** Used for notifications about contacts that are available but inactive. In Steam this is used for friends that are online but not playing a game. */
    EVRNotificationStyle_Contact_Enabled(201),

    /** Used for notifications about contacts that are available and active. In Steam this is used for friends that are online and currently running a game. */
    EVRNotificationStyle_Contact_Active(202);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
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
    fun createNotification(ulOverlayHandle: VROverlayHandle_t, ulUserValue: Long, type: EVRNotificationType, pchText: String, style: EVRNotificationStyle,
                           pImage: NotificationBitmap_t.ByReference, /* out */ pNotificationId: VRNotificationId_ByReference)
            = EVRNotificationError.of(CreateNotification!!.invoke(ulOverlayHandle, ulUserValue, type.i, pchText, style.i, pImage, pNotificationId))

    @JvmField var CreateNotification: CreateNotification_callback? = null

    interface CreateNotification_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, ulUserValue: Long, type: Int, pchText: String, style: Int, pImage: NotificationBitmap_t.ByReference,
                /* out */ pNotificationId: VRNotificationId_ByReference): Int
    }

    /** Destroy a notification, hiding it first if it currently shown to the user. */
    fun removeNotification(notificationId: VRNotificationId) = EVRNotificationError.of(RemoveNotification!!.invoke(notificationId))

    @JvmField var RemoveNotification: RemoveNotification_callback? = null

    interface RemoveNotification_callback : Callback {
        fun invoke(notificationId: VRNotificationId): Int
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

// ivroverlay.h ===================================================================================================================================================

/** The maximum length of an overlay key in bytes, counting the terminating null character. */
const val k_unVROverlayMaxKeyLength = 128

/** The maximum length of an overlay name in bytes, counting the terminating null character. */
const val k_unVROverlayMaxNameLength = 128

/** The maximum number of overlays that can exist in the system at one time. */
const val k_unMaxOverlayCount = 64

/** Types of input supported by VR Overlays */
enum class VROverlayInputMethod(@JvmField val i: Int) {

    VROverlayInputMethod_None(0), //    No input events will be generated automatically for this overlay
    VROverlayInputMethod_Mouse(1); //   Tracked controllers will get mouse events automatically

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class VROverlayInputMethod_ByReference(var value: VROverlayInputMethod = VROverlayInputMethod.VROverlayInputMethod_None) : IntByReference(value.i)

/** Allows the caller to figure out which overlay transform getter to call. */
enum class VROverlayTransformType(@JvmField val i: Int) {

    VROverlayTransform_Absolute(0),
    VROverlayTransform_TrackedDeviceRelative(1),
    VROverlayTransform_SystemOverlay(2),
    VROverlayTransform_TrackedComponent(3);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class VROverlayTransformType_ByReference(@JvmField var value: VROverlayTransformType = VROverlayTransformType.VROverlayTransform_Absolute) : IntByReference(value.i)

/** Overlay control settings */
enum class VROverlayFlags(@JvmField val i: Int) {

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

    // If this is set ownership and render access to the overlay are transferred to the new scene process on a call to openvr.IVRApplications::LaunchInternalProcess
    VROverlayFlags_TransferOwnershipToInternalProcess(9),

    // If set), renders 50% of the texture in each eye), side by side
    VROverlayFlags_SideBySide_Parallel(10), // Texture is left/right
    VROverlayFlags_SideBySide_Crossed(11), // Texture is crossed and right/left

    VROverlayFlags_Panorama(12), // Texture is a panorama
    VROverlayFlags_StereoPanorama(13), // Texture is a stereo panorama

    // If this is set on an overlay owned by the scene application that overlay will be sorted with the "Other" overlays on top of all other scene overlays
    VROverlayFlags_SortWithNonSceneOverlays(14);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

open class VROverlayIntersectionParams_t : Structure {

    @JvmField var vSource = HmdVector3_t()
    @JvmField var vDirection = HmdVector3_t()
    @JvmField var eOrigin = ETrackingUniverseOrigin.TrackingUniverseSeated.i
    fun eOrigin() = ETrackingUniverseOrigin.of(eOrigin)

    constructor()

    constructor(vSource: HmdVector3_t, vDirection: HmdVector3_t, eOrigin: Int) {
        this.vSource = vSource
        this.vDirection = vDirection
        this.eOrigin = eOrigin
    }

    constructor(vSource: HmdVector3_t, vDirection: HmdVector3_t, eOrigin: ETrackingUniverseOrigin) : this(vSource, vDirection, eOrigin.i)

    override fun getFieldOrder(): List<*> = Arrays.asList("vPoint", "vNormal", "eOrigin")

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

    override fun getFieldOrder(): List<*> = Arrays.asList("vPoint", "vNormal", "vUVs", "fDistance")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VROverlayIntersectionResults_t(), Structure.ByReference
    class ByValue : VROverlayIntersectionResults_t(), Structure.ByValue
}

// Input modes for the Big Picture gamepad text entry
enum class EGamepadTextInputMode(@JvmField val i: Int) {

    k_EGamepadTextInputModeNormal(0),
    k_EGamepadTextInputModePassword(1),
    k_EGamepadTextInputModeSubmit(2);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

// Controls number of allowed lines for the Big Picture gamepad text entry
enum class EGamepadTextInputLineMode(@JvmField val i: Int) {

    k_EGamepadTextInputLineModeSingleLine(0),
    k_EGamepadTextInputLineModeMultipleLines(1);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Directions for changing focus between overlays with the gamepad */
enum class EOverlayDirection(@JvmField val i: Int) {

    OverlayDirection_Up(0),
    OverlayDirection_Down(1),
    OverlayDirection_Left(2),
    OverlayDirection_Right(3),

    OverlayDirection_Count(4);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
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

    /** Fills the provided buffer with the string key of the overlay. Returns the size of buffer required to store the key, including the terminating null
     *  character. openvr.k_unVROverlayMaxKeyLength will be enough bytes to fit the string. */
    fun getOverlayKey(ulOverlayHandle: VROverlayHandle_t, pchValue: String, unBufferSize: Int, pError: EVROverlayError_ByReference? = null)
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
    fun getOverlayTexture(ulOverlayHandle: VROverlayHandle_t, pNativeTextureHandle: PointerByReference, pNativeTextureRef: Pointer, pnWidth: IntByReference,
                          pnHeight: IntByReference, pNativeFormat: IntByReference, pAPI: EGraphicsAPIConvention_ByReference, pColorSpace: EColorSpace_ByReference)
            = EVROverlayError.of(GetOverlayTexture!!.invoke(ulOverlayHandle, pNativeTextureHandle, pNativeTextureRef, pnWidth, pnHeight, pNativeFormat, pAPI,
            pColorSpace))

    @JvmField var GetOverlayTexture: GetOverlayTexture_callback? = null

    interface GetOverlayTexture_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle_t, pNativeTextureHandle: PointerByReference, pNativeTextureRef: Pointer, pWidth: IntByReference,
                   pHeight: IntByReference, pNativeFormat: IntByReference, pAPI: EGraphicsAPIConvention_ByReference, pColorSpace: EColorSpace_ByReference): Int
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

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("FindOverlay", "CreateOverlay", "DestroyOverlay", "SetHighQualityOverlay", "GetHighQualityOverlay",
            "GetOverlayKey", "GetOverlayName", "GetOverlayImageData", "GetOverlayErrorNameFromEnum", "SetOverlayRenderingPid", "GetOverlayRenderingPid",
            "SetOverlayFlag", "GetOverlayFlag", "SetOverlayColor", "GetOverlayColor", "SetOverlayAlpha", "GetOverlayAlpha", "SetOverlayTexelAspect",
            "GetOverlayTexelAspect", "SetOverlaySortOrder", "GetOverlaySortOrder", "SetOverlayWidthInMeters", "GetOverlayWidthInMeters",
            "SetOverlayAutoCurveDistanceRangeInMeters", "GetOverlayAutoCurveDistanceRangeInMeters", "SetOverlayTextureColorSpace", "GetOverlayTextureColorSpace",
            "SetOverlayTextureBounds", "GetOverlayTextureBounds", "GetOverlayTransformType", "SetOverlayTransformAbsolute", "GetOverlayTransformAbsolute",
            "SetOverlayTransformTrackedDeviceRelative", "GetOverlayTransformTrackedDeviceRelative", "SetOverlayTransformTrackedDeviceComponent",
            "GetOverlayTransformTrackedDeviceComponent", "ShowOverlay", "HideOverlay", "IsOverlayVisible", "GetTransformForOverlayCoordinates", "PollNextOverlayEvent",
            "GetOverlayInputMethod", "GetOverlayMouseScale", "SetOverlayMouseScale", "ComputeOverlayIntersection", "HandleControllerOverlayInteractionAsMouse",
            "IsHoverTargetOverlay", "GetGamepadFocusOverlay", "SetGamepadFocusOverlay", "SetOverlayNeighbor", "MoveGamepadFocusToNeighbor", "SetOverlayTexture",
            "ClearOverlayTexture", "SetOverlayRaw", "SetOverlayFromFile", "GetOverlayTexture", "ReleaseNativeOverlayHandle", "GetOverlayTextureSize",
            "CreateDashboardOverlay", "IsDashboardVisible", "IsActiveDashboardOverlay", "SetDashboardOverlaySceneProcess", "GetDashboardOverlaySceneProcess",
            "ShowDashboard", "GetPrimaryDashboardDevice", "ShowKeyboard", "ShowKeyboardForOverlay", "GetKeyboardText", "HideKeyboard",
            "SetKeyboardTransformAbsolute", "SetKeyboardPositionForOverlay")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVROverlay(), Structure.ByReference
    class ByValue : IVROverlay(), Structure.ByValue
}

const val IVROverlay_Version = "FnTable:IVROverlay_013";

// ivrrendermodels.h ==============================================================================================================================================

const val k_pch_Controller_Component_GDC2015 = "gdc2015"   // Canonical coordinate system of the gdc 2015 wired controller, provided for backwards compatibility
const val k_pch_Controller_Component_Base = "base"         // For controllers with an unambiguous 'base'.
const val k_pch_Controller_Component_Tip = "tip"           // For controllers with an unambiguous 'tip' (used for 'laser-pointing')
const val k_pch_Controller_Component_HandGrip = "handgrip" // Neutral, ambidextrous hand-pose when holding controller. On plane between neutrally posed index finger and thumb
const val k_pch_Controller_Component_Status = "status"     // 1:1 aspect ratio status area, with canonical [0,1] uv mapping

/** Errors that can occur with the VR compositor */
enum class EVRRenderModelError(@JvmField val i: Int) {

    VRRenderModelError_None(0),
    VRRenderModelError_Loading(100),
    VRRenderModelError_NotSupported(200),
    VRRenderModelError_InvalidArg(300),
    VRRenderModelError_InvalidModel(301),
    VRRenderModelError_NoShapes(302),
    VRRenderModelError_MultipleShapes(303),
    VRRenderModelError_TooManyVertices(304),
    VRRenderModelError_MultipleTextures(305),
    VRRenderModelError_BufferTooSmall(306),
    VRRenderModelError_NotEnoughNormals(307),
    VRRenderModelError_NotEnoughTexCoords(308),

    VRRenderModelError_InvalidTexture(400);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class EVRRenderModelError_ByReference(val value: EVRRenderModelError = EVRRenderModelError.VRRenderModelError_None) : IntByReference(value.i)

typealias VRComponentProperties = Int

enum class EVRComponentProperty(@JvmField val i: Int) {

    VRComponentProperty_IsStatic(1 shl 0),
    VRComponentProperty_IsVisible(1 shl 1),
    VRComponentProperty_IsTouched(1 shl 2),
    VRComponentProperty_IsPressed(1 shl 3),
    VRComponentProperty_IsScrolled(1 shl 4);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Describes state information about a render-model component, including transforms and other dynamic properties */
open class RenderModel_ComponentState_t : Structure {

    @JvmField var mTrackingToComponentRenderModel = HmdMatrix34_t()  // Transform required when drawing the component render model
    @JvmField var mTrackingToComponentLocal = HmdMatrix34_t()        // Transform available for attaching to a local component coordinate system (-Z out from surface )
    @JvmField var uProperties: VRComponentProperties = 0

    constructor()

    constructor(mTrackingToComponentRenderModel: HmdMatrix34_t, mTrackingToComponentLocal: HmdMatrix34_t, uProperties: Int) {
        this.mTrackingToComponentRenderModel = mTrackingToComponentRenderModel
        this.mTrackingToComponentLocal = mTrackingToComponentLocal
        this.uProperties = uProperties
    }

    override fun getFieldOrder(): List<*> = Arrays.asList("mTrackingToComponentRenderModel", "mTrackingToComponentLocal", "uProperties")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : RenderModel_ComponentState_t(), Structure.ByReference
    class ByValue : RenderModel_ComponentState_t(), Structure.ByValue
}

/** A single vertex in a render model */
open class RenderModel_Vertex_t : Structure {

    @JvmField var vPosition = HmdVector3_t()  // position in meters in device space
    @JvmField var vNormal = HmdVector3_t()
    @JvmField var rfTextureCoord = floatArrayOf(0f, 0f)

    constructor()

    constructor(vPosition: HmdVector3_t, vNormal: HmdVector3_t, rfTextureCoord: FloatArray) {
        this.vPosition = vPosition
        this.vNormal = vNormal
        // TODO check all size checks, necessary/dangerous?
        if (rfTextureCoord.size != this.rfTextureCoord.size) throw IllegalArgumentException("Wrong array size !")
        this.rfTextureCoord = rfTextureCoord
    }

    override fun getFieldOrder(): List<*> = Arrays.asList("vPosition", "vNormal", "rfTextureCoord")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : RenderModel_Vertex_t(), Structure.ByReference
    class ByValue : RenderModel_Vertex_t(), Structure.ByValue
}

/** A texture map for use on a render model */
open class RenderModel_TextureMap_t : Structure {

    // width and height of the texture map in pixels
    @JvmField var unWidth = 0.toShort()
    @JvmField var unHeight = 0.toShort()
    // Map texture data. All textures are RGBA with 8 bits per channel per pixel. Data size is width * height * 4ub
    @JvmField var rubTextureMapData: Pointer? = null

    constructor()

    constructor(unWidth: Short, unHeight: Short, rubTextureMapData: Pointer) {
        this.unWidth = unWidth
        this.unHeight = unHeight
        this.rubTextureMapData = rubTextureMapData
    }

    override fun getFieldOrder(): List<*> = Arrays.asList("unWidth", "unHeight", "rubTextureMapData")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : RenderModel_TextureMap_t(), Structure.ByReference
    class ByValue : RenderModel_TextureMap_t(), Structure.ByValue
}

/**  Session unique texture identifier. Rendermodels which share the same texture will have the same id.
IDs <0 denote the texture is not present */

typealias TextureID_t = Int

const val INVALID_TEXTURE_ID = -1

/** A texture map for use on a render model */
open class RenderModel_t : Structure {

    @JvmField var rVertexData: RenderModel_Vertex_t.ByReference? = null//   Vertex data for the mesh
    @JvmField var unVertexCount = 0                                      // Number of vertices in the vertex data
    @JvmField var rIndexData: ShortByReference? = null                   // Indices into the vertex data for each triangle
    @JvmField var unTriangleCount = 0                    //                 Number of triangles in the mesh. Index count is 3 * TriangleCount
    // Session unique texture identifier. Rendermodels which share the same texture will have the same id. <0 == texture not present
    @JvmField var diffuseTextureId = INVALID_TEXTURE_ID

    constructor()

    constructor(rVertexData: RenderModel_Vertex_t.ByReference, unVertexCount: Int, rIndexData: ShortByReference, unTriangleCount: Int, diffuseTextureId: Int) {
        this.rVertexData = rVertexData
        this.unVertexCount = unVertexCount
        this.rIndexData = rIndexData
        this.unTriangleCount = unTriangleCount
        this.diffuseTextureId = diffuseTextureId
    }

    override fun getFieldOrder(): List<*> = Arrays.asList("rVertexData", "unVertexCount", "rIndexData", "unTriangleCount", "diffuseTextureId")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : RenderModel_t(), Structure.ByReference
    class ByValue : RenderModel_t(), Structure.ByValue
}

/** A texture map for use on a render model */
open class RenderModel_ControllerMode_State_t : Structure {

    @JvmField var bScrollWheelVisible = false   // is this controller currently set to be in a scroll wheel mode

    constructor()

    constructor(bScrollWheelVisible: Boolean) {
        this.bScrollWheelVisible = bScrollWheelVisible
    }

    override fun getFieldOrder(): List<*> = Arrays.asList("bScrollWheelVisible")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : RenderModel_ControllerMode_State_t(), Structure.ByReference
    class ByValue : RenderModel_ControllerMode_State_t(), Structure.ByValue
}

open class IVRRenderModels : Structure {

    /** Loads and returns a render model for use in the application. pchRenderModelName should be a render model name from the Prop_RenderModelName_String
     *  property or an absolute path name to a render model on disk.
     *
     *  The resulting render model is valid until VR_Shutdown() is called or until FreeRenderModel() is called. When the application is finished with the render
     *  model it should call FreeRenderModel() to free the memory associated with the model.
     *
     *  The method returns VRRenderModelError_Loading while the render model is still being loaded.
     *  The method returns VRRenderModelError_None once loaded successfully, otherwise will return an error. */
    fun loadRenderModel_Async(pchRenderModelName: String, ppRenderModel: PointerByReference)
            = EVRRenderModelError.of(LoadRenderModel_Async!!.invoke(pchRenderModelName, ppRenderModel))

    @JvmField var LoadRenderModel_Async: LoadRenderModel_Async_callback? = null

    interface LoadRenderModel_Async_callback : Callback {
        fun invoke(pchRenderModelName: String, ppRenderModel: PointerByReference): Int
    }

    /** Frees a previously returned render model
     *   It is safe to call this on a null ptr. */
    fun freeRenderModel(pRenderModel: RenderModel_t.ByReference) = FreeRenderModel!!.invoke(pRenderModel)

    @JvmField var FreeRenderModel: FreeRenderModel_callback? = null

    interface FreeRenderModel_callback : Callback {
        fun invoke(pRenderModel: RenderModel_t.ByReference)
    }

    /** Loads and returns a texture for use in the application. */
    fun loadTexture_Async(textureId: TextureID_t, ppTexture: PointerByReference) = EVRRenderModelError.of(LoadTexture_Async!!.invoke(textureId, ppTexture))

    @JvmField var LoadTexture_Async: LoadTexture_Async_callback? = null

    interface LoadTexture_Async_callback : Callback {
        fun invoke(textureId: TextureID_t, ppTexture: PointerByReference): Int
    }

    /** Frees a previously returned texture
     *  It is safe to call this on a null ptr. */
    fun freeTexture(pTexture: RenderModel_TextureMap_t.ByReference) = FreeTexture!!.invoke(pTexture)

    @JvmField var FreeTexture: FreeTexture_callback? = null

    interface FreeTexture_callback : Callback {
        fun invoke(pTexture: RenderModel_TextureMap_t.ByReference)
    }

    /** Creates a D3D11 texture and loads data into it. */
    fun loadTextureD3D11_Async(textureId: TextureID_t, pD3D11Device: Pointer, ppD3D11Texture2D: PointerByReference)
            = EVRRenderModelError.of(LoadTextureD3D11_Async!!.invoke(textureId, pD3D11Device, ppD3D11Texture2D))

    @JvmField var LoadTextureD3D11_Async: LoadTextureD3D11_Async_callback? = null

    interface LoadTextureD3D11_Async_callback : Callback {
        fun invoke(textureId: TextureID_t, pD3D11Device: Pointer, ppD3D11Texture2D: PointerByReference): Int
    }

    /** Helper function to copy the bits into an existing texture. */
    fun loadIntoTextureD3D11_Async(textureId: TextureID_t, pDstTexture: Pointer)
            = EVRRenderModelError.of(LoadIntoTextureD3D11_Async!!.invoke(textureId, pDstTexture))

    @JvmField var LoadIntoTextureD3D11_Async: LoadIntoTextureD3D11_Async_callback? = null

    interface LoadIntoTextureD3D11_Async_callback : Callback {
        fun invoke(textureId: TextureID_t, pDstTexture: Pointer): Int
    }

    /** Use this to free textures created with LoadTextureD3D11_Async instead of calling Release on them. */
    fun freeTextureD3D11(pD3D11Texture2D: Pointer) = FreeTextureD3D11!!.invoke(pD3D11Texture2D)

    @JvmField var FreeTextureD3D11: FreeTextureD3D11_callback? = null

    interface FreeTextureD3D11_callback : Callback {
        fun invoke(pD3D11Texture2D: Pointer)
    }

    /** Use this to get the names of available render models.  Index does not correlate to a tracked device index, but is only used for iterating over all
     *  available render models.  If the index is out of range, this function will return 0.
     *  Otherwise, it will return the size of the buffer required for the name. */
    fun getRenderModelName(unRenderModelIndex: Int, pchRenderModelName: String, unRenderModelNameLen: Int)
            = GetRenderModelName!!.invoke(unRenderModelIndex, pchRenderModelName, unRenderModelNameLen)

    @JvmField var GetRenderModelName: GetRenderModelName_callback? = null

    interface GetRenderModelName_callback : Callback {
        fun invoke(unRenderModelIndex: Int, pchRenderModelName: String, unRenderModelNameLen: Int): Int
    }

    /** Returns the number of available render models. */
    fun getRenderModelCount() = GetRenderModelCount!!.invoke()

    @JvmField var GetRenderModelCount: GetRenderModelCount_callback? = null

    interface GetRenderModelCount_callback : Callback {
        fun invoke(): Int
    }


    /** Returns the number of components of the specified render model.
     *  Components are useful when client application wish to draw, label, or otherwise interact with components of tracked objects.
     *  Examples controller components:
     *      renderable things such as triggers, buttons
     *      non-renderable things which include coordinate systems such as 'tip', 'base', a neutral controller agnostic hand-pose
     *      If all controller components are enumerated and rendered, it will be equivalent to drawing the traditional render model
     *      Returns 0 if components not supported, >0 otherwise */
    fun getComponentCount(pchRenderModelName: String) = GetComponentCount!!.invoke(pchRenderModelName)

    @JvmField var GetComponentCount: GetComponentCount_callback? = null

    interface GetComponentCount_callback : Callback {
        fun invoke(pchRenderModelName: String): Int
    }

    /** Use this to get the names of available components.  Index does not correlate to a tracked device index, but is only used for iterating over all available
     *  components.  If the index is out of range, this function will return 0.
     *  Otherwise, it will return the size of the buffer required for the name. */
    fun getComponentName(pchRenderModelName: String, unComponentIndex: Int, pchComponentName: String, unComponentNameLen: Int)
            = GetComponentName!!.invoke(pchRenderModelName, unComponentIndex, pchComponentName, unComponentNameLen)

    @JvmField var GetComponentName: GetComponentName_callback? = null

    interface GetComponentName_callback : Callback {
        fun invoke(pchRenderModelName: String, unComponentIndex: Int, pchComponentName: String, unComponentNameLen: Int): Int
    }

    /** Get the button mask for all buttons associated with this component
     *  If no buttons (or axes) are associated with this component, return 0
     *      Note: multiple components may be associated with the same button. Ex: two grip buttons on a single controller.
     *      Note: A single component may be associated with multiple buttons. Ex: A trackpad which also provides "D-pad" functionality */
    fun getComponentButtonMask(pchRenderModelName: String, pchComponentName: String) = GetComponentButtonMask!!.invoke(pchRenderModelName, pchComponentName)

    @JvmField var GetComponentButtonMask: GetComponentButtonMask_callback? = null

    interface GetComponentButtonMask_callback : Callback {
        fun invoke(pchRenderModelName: String, pchComponentName: String): Long
    }

    /** Use this to get the render model name for the specified rendermode/component combination, to be passed to LoadRenderModel.
     *  If the component name is out of range, this function will return 0.
     *  Otherwise, it will return the size of the buffer required for the name. */
    fun getComponentRenderModelName(pchRenderModelName: String, pchComponentName: String, pchComponentRenderModelName: String, unComponentRenderModelNameLen: Int)
            = GetComponentRenderModelName!!.invoke(pchRenderModelName, pchComponentName, pchComponentRenderModelName, unComponentRenderModelNameLen)

    @JvmField var GetComponentRenderModelName: GetComponentRenderModelName_callback? = null

    interface GetComponentRenderModelName_callback : Callback {
        fun invoke(pchRenderModelName: String, pchComponentName: String, pchComponentRenderModelName: String, unComponentRenderModelNameLen: Int): Int
    }

    /** Use this to query information about the component, as a function of the controller state.
     *
     *  For dynamic controller components (ex: trigger) values will reflect component motions
     *  For static components this will return a consistent value independent of the VRControllerState_t
     *
     *  If the pchRenderModelName or pchComponentName is invalid, this will return false (and transforms will be set to identity).
     *  Otherwise, return true
     *  Note: For dynamic objects, visibility may be dynamic. (I.e., true/false will be returned based on controller state and controller mode state ) */
    fun getComponentState(pchRenderModelName: String, pchComponentName: String, pControllerState: VRControllerState_t.ByReference,
                          pState: RenderModel_ControllerMode_State_t.ByReference, pComponentState: RenderModel_ComponentState_t.ByReference)
            = GetComponentState!!.invoke(pchRenderModelName, pchComponentName, pControllerState, pState, pComponentState)

    @JvmField var GetComponentState: GetComponentState_callback? = null

    interface GetComponentState_callback : Callback {
        fun invoke(pchRenderModelName: String, pchComponentName: String, pControllerState: VRControllerState_t.ByReference,
                   pState: RenderModel_ControllerMode_State_t.ByReference, pComponentState: RenderModel_ComponentState_t.ByReference): Boolean
    }

    /** Returns true if the render model has a component with the specified name */
    fun renderModelHasComponent(pchRenderModelName: String, pchComponentName: String) = RenderModelHasComponent!!.invoke(pchRenderModelName, pchComponentName)

    @JvmField var RenderModelHasComponent: RenderModelHasComponent_callback? = null

    interface RenderModelHasComponent_callback : Callback {
        fun invoke(pchRenderModelName: String, pchComponentName: String): Boolean
    }

    /** Returns the URL of the thumbnail image for this rendermodel */
    fun getRenderModelThumbnailURL(pchRenderModelName: String, pchThumbnailURL: String, unThumbnailURLLen: Int, peError: EVRRenderModelError_ByReference)
            = GetRenderModelThumbnailURL!!.invoke(pchRenderModelName, pchThumbnailURL, unThumbnailURLLen, peError)

    @JvmField var GetRenderModelThumbnailURL: GetRenderModelThumbnailURL_callback? = null

    interface GetRenderModelThumbnailURL_callback : Callback {
        fun invoke(pchRenderModelName: String, pchThumbnailURL: String, unThumbnailURLLen: Int, peError: EVRRenderModelError_ByReference): Int
    }

    /** Provides a render model path that will load the unskinned model if the model name provided has been replace by the user. If the model hasn't been
     *  replaced the path value will still be a valid path to load the model. Pass this to LoadRenderModel_Async, etc. to load the model. */
    fun getRenderModelOriginalPath(pchRenderModelName: String, pchOriginalPath: String, unOriginalPathLen: Int, peError: EVRRenderModelError_ByReference)
            = GetRenderModelOriginalPath!!.invoke(pchRenderModelName, pchOriginalPath, unOriginalPathLen, peError)

    @JvmField var GetRenderModelOriginalPath: GetRenderModelOriginalPath_callback? = null

    interface GetRenderModelOriginalPath_callback : Callback {
        fun invoke(pchRenderModelName: String, pchOriginalPath: String, unOriginalPathLen: Int, peError: EVRRenderModelError_ByReference): Int
    }

    /** Returns a string for a render model error */
    fun getRenderModelErrorNameFromEnum(error: EVRRenderModelError) = GetRenderModelErrorNameFromEnum!!.invoke(error.i)

    @JvmField var GetRenderModelErrorNameFromEnum: GetRenderModelErrorNameFromEnum_callback? = null

    interface GetRenderModelErrorNameFromEnum_callback : Callback {
        fun invoke(error: Int): String
    }

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("LoadRenderModel_Async", "FreeRenderModel", "LoadTexture_Async", "FreeTexture",
            "LoadTextureD3D11_Async", "LoadIntoTextureD3D11_Async", "FreeTextureD3D11", "GetRenderModelName", "GetRenderModelCount", "GetComponentCount",
            "GetComponentName", "GetComponentButtonMask", "GetComponentRenderModelName", "GetComponentState", "RenderModelHasComponent",
            "GetRenderModelThumbnailURL", "GetRenderModelOriginalPath", "GetRenderModelErrorNameFromEnum")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRRenderModels(), Structure.ByReference
    class ByValue : IVRRenderModels(), Structure.ByValue
}

const val IVRRenderModels_Version = "FnTable:IVRRenderModels_005"

// ivrextendeddisplay.h ===========================================================================================================================================

/** NOTE: Use of this interface is not recommended in production applications. It will not work for displays which use
 *  direct-to-display mode. Creating our own window is also incompatible with the VR compositor and is not available when the compositor is running. */
open class IVRExtendedDisplay : Structure {

    /** Size and position that the window needs to be on the VR display. */
    fun getWindowBounds(pnX: IntByReference, pnY: IntByReference, pnWidth: IntByReference, pnHeight: IntByReference)
            = GetWindowBounds!!.invoke(pnX, pnY, pnWidth, pnHeight)

    @JvmField var GetWindowBounds: GetWindowBounds_callback? = null

    interface GetWindowBounds_callback : Callback {
        fun invoke(pnX: IntByReference, pnY: IntByReference, pnWidth: IntByReference, pnHeight: IntByReference)
    }

    /** Gets the viewport in the frame buffer to draw the output of the distortion into */
    fun getEyeOutputViewport(eEye: EVREye, pnX: IntByReference, pnY: IntByReference, pnWidth: IntByReference, pnHeight: IntByReference)
            = GetEyeOutputViewport!!.invoke(eEye.i, pnX, pnY, pnWidth, pnHeight)

    @JvmField var GetEyeOutputViewport: GetEyeOutputViewport_callback? = null

    interface GetEyeOutputViewport_callback : Callback {
        fun invoke(eEye: Int, pnX: IntByReference, pnY: IntByReference, pnWidth: IntByReference, pnHeight: IntByReference)
    }

    /** [D3D10/11 Only]
     *  Returns the adapter index and output index that the user should pass into EnumAdapters and EnumOutputs to create the device and swap chain in DX10 and
     *  DX11. If an error occurs both indices will be set to -1.     */
    fun getDXGIOutputInfo(pnAdapterIndex: IntByReference, pnAdapterOutputIndex: IntByReference) = GetDXGIOutputInfo!!.invoke(pnAdapterIndex, pnAdapterOutputIndex)

    @JvmField var GetDXGIOutputInfo: GetDXGIOutputInfo_callback? = null

    interface GetDXGIOutputInfo_callback : Callback {
        fun invoke(pnAdapterIndex: IntByReference, pnAdapterOutputIndex: IntByReference)
    }

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("GetWindowBounds", "GetEyeOutputViewport", "GetDXGIOutputInfo")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRExtendedDisplay(), Structure.ByReference
    class ByValue : IVRExtendedDisplay(), Structure.ByValue
}

const val IVRExtendedDisplay_Version = "FnTable:IVRExtendedDisplay_001"

// ivrtrackedcamera.h =============================================================================================================================================

open class IVRTrackedCamera : Structure {

    /** Returns a string for an error */
    fun getCameraErrorNameFromEnum(eCameraError: EVRTrackedCameraError) = GetCameraErrorNameFromEnum!!.invoke(eCameraError.i)

    @JvmField var GetCameraErrorNameFromEnum: GetCameraErrorNameFromEnum_callback? = null

    interface GetCameraErrorNameFromEnum_callback : Callback {
        fun invoke(eCameraError: Int): String
    }

    /** For convenience, same as tracked property request Prop_HasCamera_Bool */
    // TODO check automatic conversion *Boolean -> *Byte
    fun hasCamera(nDeviceIndex: TrackedDeviceIndex_t, pHasCamera: BooleanByReference) = EVRTrackedCameraError.of(HasCamera!!.invoke(nDeviceIndex, pHasCamera))

    @JvmField var HasCamera: HasCamera_callback? = null

    interface HasCamera_callback : Callback {
        fun invoke(nDeviceIndex: TrackedDeviceIndex_t, pHasCamera: ByteByReference): Int
    }

    /** Gets size of the image frame. */
    fun getCameraFrameSize(nDeviceIndex: TrackedDeviceIndex_t, eFrameType: EVRTrackedCameraFrameType, pnWidth: IntByReference, pnHeight: IntByReference,
                           pnFrameBufferSize: IntByReference)
            = EVRTrackedCameraError.of(GetCameraFrameSize!!.invoke(nDeviceIndex, eFrameType.i, pnWidth, pnHeight, pnFrameBufferSize))

    @JvmField var GetCameraFrameSize: GetCameraFrameSize_callback? = null

    interface GetCameraFrameSize_callback : Callback {
        fun invoke(nDeviceIndex: TrackedDeviceIndex_t, eFrameType: Int, pnWidth: IntByReference, pnHeight: IntByReference, pnFrameBufferSize: IntByReference): Int
    }


    fun getCameraIntrinisics(nDeviceIndex: TrackedDeviceIndex_t, eFrameType: EVRTrackedCameraFrameType, pFocalLength: HmdVector2_t.ByReference,
                             pCenter: HmdVector2_t.ByReference)
            = EVRTrackedCameraError.of(GetCameraIntrinisics!!.invoke(nDeviceIndex, eFrameType.i, pFocalLength, pCenter))

    @JvmField var GetCameraIntrinisics: GetCameraIntrinisics_callback? = null

    interface GetCameraIntrinisics_callback : Callback {
        fun invoke(nDeviceIndex: TrackedDeviceIndex_t, eFrameType: Int, pFocalLength: HmdVector2_t.ByReference, pCenter: HmdVector2_t.ByReference): Int
    }


    fun getCameraProjection(nDeviceIndex: TrackedDeviceIndex_t, eFrameType: EVRTrackedCameraFrameType, flZNear: Float, flZFar: Float,
                            pProjection: HmdMatrix44_t.ByReference)
            = EVRTrackedCameraError.of(GetCameraProjection!!.invoke(nDeviceIndex, eFrameType.i, flZNear, flZFar, pProjection))

    @JvmField var GetCameraProjection: GetCameraProjection_callback? = null

    interface GetCameraProjection_callback : Callback {
        fun invoke(nDeviceIndex: TrackedDeviceIndex_t, eFrameType: Int, flZNear: Float, flZFar: Float, pProjection: HmdMatrix44_t.ByReference): Int
    }

    /** Acquiring streaming service permits video streaming for the caller. Releasing hints the system that video services do not need to be maintained for this
     *  client.
     *  If the camera has not already been activated, a one time spin up may incur some auto exposure as well as initial streaming frame delays.
     *  The camera should be considered a global resource accessible for shared consumption but not exclusive to any caller.
     *  The camera may go inactive due to lack of active consumers or headset idleness. */
    fun acquireVideoStreamingService(nDeviceIndex: TrackedDeviceIndex_t, pHandle: TrackedCameraHandle_t)
            = EVRTrackedCameraError.of(AcquireVideoStreamingService!!.invoke(nDeviceIndex, pHandle))

    @JvmField var AcquireVideoStreamingService: AcquireVideoStreamingService_callback? = null

    interface AcquireVideoStreamingService_callback : Callback {
        fun invoke(nDeviceIndex: TrackedDeviceIndex_t, pHandle: TrackedCameraHandle_t): Int
    }

    fun releaseVideoStreamingService(hTrackedCamera: TrackedCameraHandle_t) = EVRTrackedCameraError.of(ReleaseVideoStreamingService!!.invoke(hTrackedCamera))
    @JvmField var ReleaseVideoStreamingService: ReleaseVideoStreamingService_callback? = null

    interface ReleaseVideoStreamingService_callback : Callback {
        fun invoke(hTrackedCamera: TrackedCameraHandle_t): Int
    }

    /** Copies the image frame into a caller's provided buffer. The image data is currently provided as RGBA data, 4 bytes per pixel.
     *  A caller can provide null for the framebuffer or frameheader if not desired. Requesting the frame header first, followed by the frame buffer allows
     *  the caller to determine if the frame as advanced per the frame header sequence.
     *  If there is no frame available yet, due to initial camera spinup or re-activation, the error will be VRTrackedCameraError_NoFrameAvailable.
     *  Ideally a caller should be polling at ~16ms intervals */
    fun getVideoStreamFrameBuffer(hTrackedCamera: TrackedCameraHandle_t, eFrameType: EVRTrackedCameraFrameType, pFrameBuffer: Pointer, nFrameBufferSize: Int,
                                  pFrameHeader: CameraVideoStreamFrameHeader_t.ByReference, nFrameHeaderSize: Int)
            = EVRTrackedCameraError.of(GetVideoStreamFrameBuffer!!.invoke(hTrackedCamera, eFrameType.i, pFrameBuffer, nFrameBufferSize, pFrameHeader,
            nFrameHeaderSize))

    @JvmField var GetVideoStreamFrameBuffer: GetVideoStreamFrameBuffer_callback? = null

    interface GetVideoStreamFrameBuffer_callback : Callback {
        fun invoke(hTrackedCamera: TrackedCameraHandle_t, eFrameType: Int, pFrameBuffer: Pointer, nFrameBufferSize: Int,
                   pFrameHeader: CameraVideoStreamFrameHeader_t.ByReference, nFrameHeaderSize: Int): Int
    }

    /** Gets size of the image frame. */
    fun getVideoStreamTextureSize(nDeviceIndex: TrackedDeviceIndex_t, eFrameType: EVRTrackedCameraFrameType, pTextureBounds: VRTextureBounds_t.ByReference,
                                  pnWidth: IntByReference, pnHeight: IntByReference)
            = EVRTrackedCameraError.of(GetVideoStreamTextureSize!!.invoke(nDeviceIndex, eFrameType.i, pTextureBounds, pnWidth, pnHeight))

    @JvmField var GetVideoStreamTextureSize: GetVideoStreamTextureSize_callback? = null

    interface GetVideoStreamTextureSize_callback : Callback {
        fun invoke(nDeviceIndex: TrackedDeviceIndex_t, eFrameType: Int, pTextureBounds: VRTextureBounds_t.ByReference, pnWidth: IntByReference,
                   pnHeight: IntByReference): Int
    }

    /** Access a shared D3D11 texture for the specified tracked camera stream */
    fun getVideoStreamTextureD3D11(hTrackedCamera: TrackedCameraHandle_t, eFrameType: EVRTrackedCameraFrameType, pD3D11DeviceOrResource: Pointer,
                                   ppD3D11ShaderResourceView: PointerByReference, pFrameHeader: CameraVideoStreamFrameHeader_t.ByReference, nFrameHeaderSize: Int)
            = EVRTrackedCameraError.of(GetVideoStreamTextureD3D11!!.invoke(hTrackedCamera, eFrameType.i, pD3D11DeviceOrResource, ppD3D11ShaderResourceView,
            pFrameHeader, nFrameHeaderSize))

    @JvmField var GetVideoStreamTextureD3D11: GetVideoStreamTextureD3D11_callback? = null

    interface GetVideoStreamTextureD3D11_callback : Callback {
        fun invoke(hTrackedCamera: TrackedCameraHandle_t, eFrameType: Int, pD3D11DeviceOrResource: Pointer, ppD3D11ShaderResourceView: PointerByReference,
                   pFrameHeader: CameraVideoStreamFrameHeader_t.ByReference, nFrameHeaderSize: Int): Int
    }

    /** Access a shared GL texture for the specified tracked camera stream */
    fun getVideoStreamTextureGL(hTrackedCamera: TrackedCameraHandle_t, eFrameType: EVRTrackedCameraFrameType, pglTextureId: glUInt_t_ByReference,
                                pFrameHeader: CameraVideoStreamFrameHeader_t.ByReference, nFrameHeaderSize: Int)
            = EVRTrackedCameraError.of(GetVideoStreamTextureGL!!.invoke(hTrackedCamera, eFrameType.i, pglTextureId, pFrameHeader, nFrameHeaderSize))

    @JvmField var GetVideoStreamTextureGL: GetVideoStreamTextureGL_callback? = null

    interface GetVideoStreamTextureGL_callback : Callback {
        fun invoke(hTrackedCamera: TrackedCameraHandle_t, eFrameType: Int, pglTextureId: IntByReference, pFrameHeader: CameraVideoStreamFrameHeader_t.ByReference,
                   nFrameHeaderSize: Int): Int
    }


    fun releaseVideoStreamTextureGL(hTrackedCamera: TrackedCameraHandle_t, glTextureId: glUInt_t)
            = EVRTrackedCameraError.of(ReleaseVideoStreamTextureGL!!.invoke(hTrackedCamera, glTextureId))

    @JvmField var ReleaseVideoStreamTextureGL: ReleaseVideoStreamTextureGL_callback? = null

    interface ReleaseVideoStreamTextureGL_callback : Callback {
        fun invoke(hTrackedCamera: TrackedCameraHandle_t, glTextureId: Int): Int
    }

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("GetCameraErrorNameFromEnum", "HasCamera", "GetCameraFrameSize", "GetCameraIntrinisics",
            "GetCameraProjection", "AcquireVideoStreamingService", "ReleaseVideoStreamingService", "GetVideoStreamFrameBuffer", "GetVideoStreamTextureSize",
            "GetVideoStreamTextureD3D11", "GetVideoStreamTextureGL", "ReleaseVideoStreamTextureGL")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRTrackedCamera(), Structure.ByReference
    class ByValue : IVRTrackedCamera(), Structure.ByValue
}

const val IVRTrackedCamera_Version = "FnTable:IVRTrackedCamera_003"

// ivrscreenshots.h ===============================================================================================================================================

/** Errors that can occur with the VR compositor */
enum class EVRScreenshotError(@JvmField val i: Int) {

    VRScreenshotError_None(0),
    VRScreenshotError_RequestFailed(1),
    VRScreenshotError_IncompatibleVersion(100),
    VRScreenshotError_NotFound(101),
    VRScreenshotError_BufferTooSmall(102),
    VRScreenshotError_ScreenshotAlreadyInProgress(108);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class EVRScreenshotError_ByReference(val value: EVRScreenshotError = EVRScreenshotError.VRScreenshotError_None) : IntByReference(value.i)

/** Allows the application to generate screenshots */
open class IVRScreenshots : Structure {

    /** Request a screenshot of the requested type.
     *  A request of the VRScreenshotType_Stereo type will always work. Other types will depend on the underlying application support.
     *  The first file name is for the preview image and should be a regular screenshot (ideally from the left eye). The second is the VR screenshot in the correct
     *  format. They should be in the same aspect ratio.
     *  Formats per type:
     *      VRScreenshotType_Mono: the VR filename is ignored (can be nullptr), this is a normal flat single shot.
     *      VRScreenshotType_Stereo:  The VR image should be a side-by-side with the left eye image on the left.
     *      VRScreenshotType_Cubemap: The VR image should be six square images composited horizontally.
     *      VRScreenshotType_StereoPanorama: above/below with left eye panorama being the above image.  Image is typically square with the panorama being 2x
     *      horizontal.
     *
     *  Note that the VR dashboard will call this function when the user presses the screenshot binding (currently System Button + Trigger).  If Steam is running,
     *  the destination file names will be in %TEMP% and will be copied into Steam's screenshot library for the running application once SubmitScreenshot() is
     *  called.
     *  If Steam is not running, the paths will be in the user's documents folder under Documents\SteamVR\Screenshots.
     *  Other VR applications can call this to initate a screenshot outside of user control.
     *  The destination file names do not need an extension, will be replaced with the correct one for the format which is currently .png. */
    fun requestScreenshot(pOutScreenshotHandle: ScreenshotHandle_t_ByReference, type: EVRScreenshotType, pchPreviewFilename: String, pchVRFilename: String)
            = EVRScreenshotError.of(RequestScreenshot!!.invoke(pOutScreenshotHandle, type.i, pchPreviewFilename, pchVRFilename))

    @JvmField var RequestScreenshot: RequestScreenshot_callback? = null

    interface RequestScreenshot_callback : Callback {
        fun invoke(pOutScreenshotHandle: ScreenshotHandle_t_ByReference, type: Int, pchPreviewFilename: String, pchVRFilename: String): Int
    }

    /** Called by the running VR application to indicate that it wishes to be in charge of screenshots.  If the application does not call this, the Compositor
     *  will only support VRScreenshotType_Stereo screenshots that will be captured without notification to the running app.
     *  Once hooked your application will receive a VREvent_RequestScreenshot event when the user presses the buttons to take a screenshot. */
    fun hookScreenshot(pSupportedTypes: Array<EVRScreenshotType>, numTypes: Int): EVRScreenshotError {

        val pointer = Memory((numTypes * Native.getNativeSize(java.lang.Double.TYPE)).toLong())
        pointer.read(0, pSupportedTypes.map { it.i }.toIntArray(), 0, numTypes) // TODO probably also the other arrays needs Pointer type

        return EVRScreenshotError.of(HookScreenshot!!.invoke(pointer, numTypes))
    }

    @JvmField var HookScreenshot: HookScreenshot_callback? = null

    interface HookScreenshot_callback : Callback {
        fun invoke(pSupportedTypes: Pointer, numTypes: Int): Int
    }

    /** When your application receives a VREvent_RequestScreenshot event, call these functions to get the details of the screenshot request. */
    fun getScreenshotPropertyType(screenshotHandle: ScreenshotHandle_t, pError: EVRScreenshotError_ByReference)
            = EVRScreenshotError.of(GetScreenshotPropertyType!!.invoke(screenshotHandle, pError))

    @JvmField var GetScreenshotPropertyType: GetScreenshotPropertyType_callback? = null

    interface GetScreenshotPropertyType_callback : Callback {
        fun invoke(screenshotHandle: ScreenshotHandle_t, pError: IntByReference): Int
    }

    /** Get the filename for the preview or vr image (see vr::EScreenshotPropertyFilenames).  The return value is the size of the string.   */
    fun getScreenshotPropertyFilename(screenshotHandle: ScreenshotHandle_t, filenameType: EVRScreenshotPropertyFilenames, pchFilename: String,
                                      cchFilename: Int, pError: EVRScreenshotError_ByReference)
            = EVRScreenshotError.of(GetScreenshotPropertyFilename!!.invoke(screenshotHandle, filenameType.i, pchFilename, cchFilename, pError))

    @JvmField var GetScreenshotPropertyFilename: GetScreenshotPropertyFilename_callback? = null

    interface GetScreenshotPropertyFilename_callback : Callback {
        fun invoke(screenshotHandle: ScreenshotHandle_t, filenameType: Int, pchFilename: String, cchFilename: Int, pError: EVRScreenshotError_ByReference): Int
    }

    /** Call this if the application is taking the screen shot will take more than a few ms processing. This will result in an overlay being presented that shows
     *  a completion bar. */
    fun updateScreenshotProgress(screenshotHandle: ScreenshotHandle_t, flProgress: Float)
            = EVRScreenshotError.of(UpdateScreenshotProgress!!.invoke(screenshotHandle, flProgress))

    @JvmField var UpdateScreenshotProgress: UpdateScreenshotProgress_callback? = null

    interface UpdateScreenshotProgress_callback : Callback {
        fun invoke(screenshotHandle: ScreenshotHandle_t, flProgress: Float): Int
    }

    /** Tells the compositor to take an internal screenshot of type VRScreenshotType_Stereo. It will take the current submitted scene textures of the running
     *  application and write them into the preview image and a side-by-side file for the VR image.
     *  This is similiar to request screenshot, but doesn't ever talk to the application, just takes the shot and submits. */
    fun takeStereoScreenshot(pOutScreenshotHandle: ScreenshotHandle_t_ByReference, pchPreviewFilename: String, pchVRFilename: String)
            = EVRScreenshotError.of(TakeStereoScreenshot!!.invoke(pOutScreenshotHandle, pchPreviewFilename, pchVRFilename))

    @JvmField var TakeStereoScreenshot: TakeStereoScreenshot_callback? = null

    interface TakeStereoScreenshot_callback : Callback {
        fun invoke(pOutScreenshotHandle: ScreenshotHandle_t_ByReference, pchPreviewFilename: String, pchVRFilename: String): Int
    }

    /** Submit the completed screenshot.  If Steam is running this will call into the Steam client and upload the screenshot to the screenshots section of the
     *  library for the running application.  If Steam is not running, this function will display a notification to the user that the screenshot was taken.
     *  The paths should be full paths with extensions.
     *  File paths should be absolute including exntensions.
     *  screenshotHandle can be k_unScreenshotHandleInvalid if this was a new shot taking by the app to be saved and not initiated by a user (achievement earned
     *  or something) */
    fun submitScreenshot(screenshotHandle: ScreenshotHandle_t, type: EVRScreenshotType, pchSourcePreviewFilename: String, pchSourceVRFilename: String)
            = EVRScreenshotError.of(SubmitScreenshot!!.invoke(screenshotHandle, type.i, pchSourcePreviewFilename, pchSourceVRFilename))

    @JvmField var SubmitScreenshot: SubmitScreenshot_callback? = null

    interface SubmitScreenshot_callback : Callback {
        fun invoke(screenshotHandle: ScreenshotHandle_t, type: Int, pchSourcePreviewFilename: String, pchSourceVRFilename: String): Int
    }

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("RequestScreenshot", "HookScreenshot", "GetScreenshotPropertyType", "GetScreenshotPropertyFilename",
            "UpdateScreenshotProgress", "TakeStereoScreenshot", "SubmitScreenshot")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRScreenshots(), Structure.ByReference
    class ByValue : IVRScreenshots(), Structure.ByValue
}

const val IVRScreenshots_Version = "FnTable:IVRScreenshots_001"

// ivrresources.h =================================================================================================================================================

open class IVRResources : Structure {

    // ------------------------------------
    // Shared Resource Methods
    // ------------------------------------

    /** Loads the specified resource into the provided buffer if large enough.
     *  Returns the size in bytes of the buffer required to hold the specified resource. */
    fun loadSharedResource(pchResourceName: String, pchBuffer: String, unBufferSize: Int) = LoadSharedResource!!.invoke(pchResourceName, pchBuffer, unBufferSize)

    @JvmField var LoadSharedResource: LoadSharedResource_callback? = null

    interface LoadSharedResource_callback : Callback {
        fun invoke(pchResourceName: String, pchBuffer: String, unBufferLen: Int): Int
    }

    /** Provides the full path to the specified resource. Resource names can include named directories for drivers and other things, and this resolves all of
     *  those and returns the actual physical path.
     *  pchResourceTypeDirectory is the subdirectory of resources to look in. */
    fun getResourceFullPath(pchResourceName: String, pchResourceTypeDirectory: String, pchPathBuffer: String, unBufferLen: Int)
            = GetResourceFullPath!!.invoke(pchResourceName, pchResourceTypeDirectory, pchPathBuffer, unBufferLen)

    @JvmField var GetResourceFullPath: GetResourceFullPath_callback? = null

    interface GetResourceFullPath_callback : Callback {
        fun invoke(pchResourceName: String, pchResourceTypeDirectory: String, pchPathBuffer: String, unBufferLen: Int): Int
    }

    constructor()

    override fun getFieldOrder(): List<*> = Arrays.asList("LoadSharedResource", "GetResourceFullPath")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRScreenshots(), Structure.ByReference
    class ByValue : IVRScreenshots(), Structure.ByValue
}

const val IVRResources_Version = "FnTable:IVRResources_001"

// ================================================================================================================================================================

/** Finds the active installation of the VR API and initializes it. The provided path must be absolute or relative to the current working directory. These are
 *  the local install versions of the equivalent functions in steamvr.h and will work without a local Steam install.
 *
 *  This path is to the "root" of the VR API install. That's the directory with the "drivers" directory and a platform (i.e. "win32") directory in it,
 *  not the directory with the DLL itself.   */
//fun vrInit(peError: EVRInitError_ByReference, eApplicationType: EVRApplicationError) = VR_Init(peError, eApplicationType.i)
//external fun VR_Init(peError: IntByReference, eApplicationType: Int): IVRSystem.ByReference // TODO check

/** unloads vrclient.dll. Any interface pointers from the interface are invalid after this point */
//fun vrShutdown() = VR_Shutdown()
//external fun VR_Shutdown()

/** Returns true if there is an HMD attached. This check is as lightweight as possible and can be called outside of VR_Init/VR_Shutdown. It should be used when
 *  an application wants to know if initializing VR is a possibility but isn't ready to take that step yet.  */
fun vrIsHmdPresent() = VR_IsHmdPresent()

external fun VR_IsHmdPresent(): Boolean

/** Returns true if the OpenVR runtime is installed. */
fun vrIsRuntimeInstalled() = VR_IsRuntimeInstalled()

external fun VR_IsRuntimeInstalled(): Boolean

/** Returns where the OpenVR runtime is installed. */
fun vrRuntimePath() = VR_RuntimePath()

external fun VR_RuntimePath(): String

/** Returns the name of the value value for an EVRInitError. This function may be called outside of VR_Init()/VR_Shutdown(). */
fun vrGetVRInitErrorAsSymbol(error: EVRInitError) = VR_GetVRInitErrorAsSymbol(error.i)

external fun VR_GetVRInitErrorAsSymbol(error: Int): String

/** Returns an english string for an EVRInitError. Applications should call VR_GetVRInitErrorAsSymbol instead and use that as a key to look up their own localized
 *  error message. This function may be called outside of VR_Init()/VR_Shutdown(). */
fun VR_GetVRInitErrorAsEnglishDescription(error: EVRInitError) = VR_GetVRInitErrorAsEnglishDescription(error.i)

external fun VR_GetVRInitErrorAsEnglishDescription(error: Int): String

/** Returns the interface of the specified version. This method must be called after VR_Init. The pointer returned is valid until VR_Shutdown is called.     */
fun vrGetGenericInterface(pchInterfaceVersion: String, peError: EVRInitError_ByReference) = VR_GetGenericInterface(pchInterfaceVersion, peError)

external fun VR_GetGenericInterface(pchInterfaceVersion: String, peError: EVRInitError_ByReference): Pointer

/** Returns whether the interface of the specified version exists.   */
fun vrIsInterfaceVersionValid(pchInterfaceVersion: String) = VR_IsInterfaceVersionValid(pchInterfaceVersion)

external fun VR_IsInterfaceVersionValid(pchInterfaceVersion: String): Boolean

/** Returns a token that represents whether the VR interface handles need to be reloaded */
fun vrGetInitToken() = VR_GetInitToken()

external fun VR_GetInitToken()

object COpenVRContext {

    private var m_pVRSystem: IVRSystem? = null
    private val m_pVRChaperone: IVRChaperone? = null
    private val m_pVRChaperoneSetup: IVRChaperoneSetup? = null
    private val m_pVRCompositor: IVRCompositor? = null
    private val m_pVROverlay: IVROverlay? = null
    private val m_pVRResources: IVRResources? = null
    private val m_pVRRenderModels: IVRRenderModels? = null
    private val m_pVRExtendedDisplay: IVRExtendedDisplay? = null
    private val m_pVRSettings: IVRSettings? = null
    private val m_pVRApplications: IVRApplications? = null
    private val m_pVRTrackedCamera: IVRTrackedCamera? = null
    private val m_pVRScreenshots: IVRScreenshots? = null

    private val error = EVRInitError_ByReference(EVRInitError.VRInitError_None)

    fun VRSystem() = m_pVRSystem ?: IVRSystem(vrGetGenericInterface(IVRSystem_Version, error))
    fun VRChaperone() = m_pVRChaperone ?: IVRChaperone(vrGetGenericInterface(IVRChaperone_Version, error))
    fun VRChaperoneSetup() = m_pVRChaperoneSetup ?: IVRChaperoneSetup(vrGetGenericInterface(IVRChaperoneSetup_Version, error))
    fun VRCompositor() = m_pVRCompositor ?: IVRCompositor(vrGetGenericInterface(IVRCompositor_Version, error))
    fun VROverlay() = m_pVROverlay ?: IVROverlay(vrGetGenericInterface(IVROverlay_Version, error))
    fun VRResources() = m_pVRResources ?: IVRResources(vrGetGenericInterface(IVRResources_Version, error))
    fun VRRenderModels() = m_pVRRenderModels ?: IVRRenderModels(vrGetGenericInterface(IVRRenderModels_Version, error))
    fun VRExtendedDisplay() = m_pVRExtendedDisplay ?: IVRExtendedDisplay(vrGetGenericInterface(IVRExtendedDisplay_Version, error))
    fun VRSettings() = m_pVRSettings ?: IVRSettings(vrGetGenericInterface(IVRSettings_Version, error))
    fun VRApplications() = m_pVRApplications ?: IVRApplications(vrGetGenericInterface(IVRApplications_Version, error))
    fun VRTrackedCamera() = m_pVRTrackedCamera ?: IVRTrackedCamera(vrGetGenericInterface(IVRTrackedCamera_Version, error))
    fun VRScreenshots() = m_pVRScreenshots ?: IVRScreenshots(vrGetGenericInterface(IVRScreenshots_Version, error))
}

fun VRSystem() = COpenVRContext.VRSystem()
fun VRChaperone() = COpenVRContext.VRChaperone()
fun VRChaperoneSetup() = COpenVRContext.VRChaperoneSetup()
fun VRCompositor() = COpenVRContext.VRCompositor()
fun VROverlay() = COpenVRContext.VROverlay()
fun VRResources() = COpenVRContext.VRResources()
fun VRRenderModels() = COpenVRContext.VRRenderModels()
fun VRExtendedDisplay() = COpenVRContext.VRExtendedDisplay()
fun VRSettings() = COpenVRContext.VRSettings()
fun VRApplications() = COpenVRContext.VRApplications()
fun VRTrackedCamera() = COpenVRContext.VRTrackedCamera()
fun VRScreenshots() = COpenVRContext.VRScreenshots()

fun vrInitInternal(peError: EVRInitError_ByReference, eType: EVRApplicationType) = VR_InitInternal(peError, eType.i)
external fun VR_InitInternal(peError: EVRInitError_ByReference, eType: Int): Pointer

fun vrShutdownInternal() = VR_ShutdownInternal()
external fun VR_ShutdownInternal()

fun VR_Init(error: EVRInitError_ByReference, applicationType: EVRApplicationType): IVRSystem? {

    var pVRSystem: IVRSystem? = null

    vrInitInternal(error, applicationType)

    if (error.value == EVRInitError.VRInitError_None)

        if (VR_IsInterfaceVersionValid(IVRSystem_Version))
            pVRSystem = VRSystem()
        else {
            VR_ShutdownInternal()
            error.value = EVRInitError.VRInitError_Init_InterfaceNotFound
        }

    return pVRSystem
}