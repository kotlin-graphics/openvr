@file:JvmName("vr")

import com.sun.jna.*
import com.sun.jna.ptr.*
import main.BYTES
import java.nio.ByteBuffer
import java.util.*

/**
 * Created by GBarbieri on 07.10.2016.
 */

/** Should be your first call   */
fun loadNatives() = Native.register(NativeLibrary.getInstance("openvr_api"))

class BooleanByReference(@JvmField var value: Boolean = false) : ByteByReference(if (value) 1 else 0)

// Forward declarations to avoid requiring vulkan.h
//struct VkDevice_T;
open class VkPhysicalDevice_T : Structure {

    constructor()

    override fun getFieldOrder(): List<String> = listOf("")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VkPhysicalDevice_T(), Structure.ByReference
    class ByValue : VkPhysicalDevice_T(), Structure.ByValue
}
//struct VkInstance_T;
//struct VkQueue_T;
// Forward declarations to avoid requiring d3d12.h
//struct ID3D12Resource;
//struct ID3D12CommandQueue;

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

    override fun getFieldOrder(): List<String> = listOf("m")

    constructor(m: FloatArray) {
        if (m.size != this.m.size) throw IllegalArgumentException("Wrong array size !")
        this.m = m
        write()
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

    override fun getFieldOrder(): List<String> = listOf("m")

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

    override fun getFieldOrder(): List<String> = listOf("v")

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
        for (i in 0..2) bb.putFloat(offset + i * Float.BYTES, v[i])
    }

    companion object {
        val SIZE = 3 * Float.BYTES
    }

    operator fun get(i: Int) = v[i]
}

open class HmdVector4_t : Structure {

    @JvmField var v = FloatArray(4)

    constructor()

    override fun getFieldOrder(): List<String> = listOf("v")

    constructor(v: FloatArray) {
        if (v.size != this.v.size) throw IllegalArgumentException("Wrong array size !")
        this.v = v
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdVector4_t(), Structure.ByReference
    class ByValue : HmdVector4_t(), Structure.ByValue

    operator fun get(i: Int) = v[i]
}

open class HmdVector3d_t : Structure {

    @JvmField var v = DoubleArray(3)

    constructor()

    override fun getFieldOrder(): List<String> = listOf("v")

    constructor(v: DoubleArray) {
        if (v.size != this.v.size) throw IllegalArgumentException("Wrong array size !")
        this.v = v
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdVector3d_t(), Structure.ByReference
    class ByValue : HmdVector3d_t(), Structure.ByValue

    operator fun get(i: Int) = v[i]
}

open class HmdVector2_t : Structure {

    @JvmField var v = FloatArray(2)

    constructor()

    override fun getFieldOrder(): List<String> = listOf("v")

    constructor(v: FloatArray) {
        if (v.size != this.v.size) throw IllegalArgumentException("Wrong array size !")
        this.v = v
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdVector2_t(), Structure.ByReference
    class ByValue : HmdVector2_t(), Structure.ByValue

    operator fun get(i: Int) = v[i]
}

open class HmdQuaternion_t : Structure {

    @JvmField var w: Double = 0.0
    @JvmField var x: Double = 0.0
    @JvmField var y: Double = 0.0
    @JvmField var z: Double = 0.0

    constructor()

    override fun getFieldOrder(): List<String> = listOf("w", "x", "y", "z")

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

    operator fun get(i: Int) = when (i) {
        0 -> x
        1 -> y
        2 -> z
        3 -> w
        else -> throw IndexOutOfBoundsException()
    }
}

open class HmdColor_t : Structure {

    @JvmField var r: Float = 0f
    @JvmField var g: Float = 0f
    @JvmField var b: Float = 0f
    @JvmField var a: Float = 0f

    constructor()

    override fun getFieldOrder(): List<String> = listOf("r", "g", "b", "a")

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

    operator fun get(i: Int) = when (i) {
        0 -> r
        1 -> g
        2 -> b
        3 -> a
        else -> throw IndexOutOfBoundsException()
    }
}

open class HmdQuad_t : Structure {

    @JvmField var vCorners = arrayOf(HmdVector3_t(), HmdVector3_t(), HmdVector3_t(), HmdVector3_t())

    constructor()

    override fun getFieldOrder(): List<String> = listOf("vCorners")

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

    override fun getFieldOrder(): List<String> = listOf("vTopLeft", "vBottomRight")

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

    override fun getFieldOrder(): List<String> = listOf("rfRed", "rfGreen", "rfBlue")

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
    Left(0),
    Right(1);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

enum class ETextureType(@JvmField val i: Int) {

    DirectX(0), // Handle is an ID3D11Texture
    OpenGL(1), // Handle is an OpenGL texture name or an OpenGL render buffer name, depending on submit flags
    Vulkan(2), // Handle is a pointer to a VRVulkanTextureData_t structure
    IOSurface(3), // Handle is a macOS cross-process-sharable IOSurface
    DirectX12(4); // Handle is a pointer to a D3D12TextureData_t structure

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class ETextureType_ByReference(@JvmField var value: ETextureType = ETextureType.OpenGL) : IntByReference(value.i)

enum class EColorSpace(@JvmField val i: Int) {

    Auto(0), //  Assumes 'gamma' for 8-bit per component formats, otherwise 'linear'.  This mirrors the DXGI formats which have _SRGB variants.
    Gamma(1), // Texture data can be displayed directly on the display without any conversion (a.k.a. display native format).
    Linear(2); // Same as gamma but has been converted to a linear representation using DXGI's sRGB conversion algorithm.

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class EColorSpace_ByReference(@JvmField var value: EColorSpace = EColorSpace.Auto) : IntByReference(value.i)

open class Texture_t : Structure {

    @JvmField var handle = 0  //See ETextureType definition above
    @JvmField var eType = 0
    fun eType() = ETextureType.of(eType)
    @JvmField var eColorSpace = 0
    fun eColorSpace() = EColorSpace.of(eColorSpace)

    constructor()

    override fun getFieldOrder(): List<String> = listOf("handle", "eType", "eColorSpace")

    constructor(handle: Int, eType: Int, eColorSpace: Int) {
        set(handle, eType, eColorSpace)
    }

    constructor(handle: Int, eType: ETextureType, eColorSpace: EColorSpace) : this(handle, eType.i, eColorSpace.i)

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
val INVALID_SHARED_TEXTURE_HANDLE = 0L

enum class ETrackingResult(@JvmField val i: Int) {

    Uninitialized(1),

    Calibrating_InProgress(100),
    Calibrating_OutOfRange(101),

    Running_OK(200),
    Running_OutOfRange(201);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

val k_unMaxDriverDebugResponseSize = 32768

/** Used to pass device IDs to API calls */
typealias TrackedDeviceIndex_t = Int
typealias TrackedDeviceIndex_t_ByReference = IntByReference
val k_unTrackedDeviceIndex_Hmd = 0
val k_unMaxTrackedDeviceCount = 16
val k_unTrackedDeviceIndexOther = 0xFFFFFFFE.toInt()
val k_unTrackedDeviceIndexInvalid = 0xFFFFFFFF.toInt()

/** Describes what kind of object is being tracked at a given ID */
enum class ETrackedDeviceClass(@JvmField val i: Int) {

    Invalid(0), //           the ID was not valid.
    HMD(1), //               Head-Mounted Displays
    Controller(2), //        Tracked controllers
    GenericTracker(3), // Generic trackers, similar to controllers
    TrackingReference(4); // Camera and base stations that serve as tracking reference points;

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Describes what specific role associated with a tracked device */
enum class ETrackedControllerRole(@JvmField val i: Int) {

    Invalid(0), //    Invalid value for controller value
    LeftHand(1), //   Tracked device associated with the left hand
    RightHand(2);//   Tracked device associated with the right hand

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

    override fun getFieldOrder(): List<String> = listOf("mDeviceToAbsoluteTracking", "vVelocity", "vAngularVelocity",
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

    Seated(0), //               Poses are provided relative to the seated zero pose
    Standing(1), //             Poses are provided relative to the safe bounds configured by the user
    /** Poses are provided in the coordinate system defined by the driver. It has Y up and is unified for devices of the same driver.
     * You usually don't want this one. */
    RawAndUncalibrated(2);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class ETrackingUniverseOrigin_ByReference(@JvmField var value: ETrackingUniverseOrigin = ETrackingUniverseOrigin.Seated) : IntByReference(value.i)

// Refers to a single container of properties
typealias PropertyContainerHandle_t = Long
typealias PropertyTypeTag_t = Int

val k_ulInvalidPropertyContainer: PropertyContainerHandle_t = 0
val k_unInvalidPropertyTag: PropertyTypeTag_t = 0

// Use these tags to set/get common types as struct properties
val k_unFloatPropertyTag: PropertyTypeTag_t = 1
val k_unInt32PropertyTag: PropertyTypeTag_t = 2
val k_unUint64PropertyTag: PropertyTypeTag_t = 3
val k_unBoolPropertyTag: PropertyTypeTag_t = 4
val k_unStringPropertyTag: PropertyTypeTag_t = 5

val k_unHmdMatrix34PropertyTag: PropertyTypeTag_t = 20
val k_unHmdMatrix44PropertyTag: PropertyTypeTag_t = 21
val k_unHmdVector3PropertyTag: PropertyTypeTag_t = 22
val k_unHmdVector4PropertyTag: PropertyTypeTag_t = 23

val k_unHiddenAreaPropertyTag: PropertyTypeTag_t = 30

val k_unOpenVRInternalReserved_Start: PropertyTypeTag_t = 1000
val k_unOpenVRInternalReserved_End: PropertyTypeTag_t = 10000


/** Each entry in this value represents a property that can be retrieved about a tracked device.
 *  Many fields are only valid for one openvr.ETrackedDeviceClass. */
enum class ETrackedDeviceProperty(@JvmField val i: Int) {

    Invalid(0),

    // general properties that apply to all device classes
    TrackingSystemName_String(1000),
    ModelNumber_String(1001),
    SerialNumber_String(1002),
    RenderModelName_String(1003),
    WillDriftInYaw_Bool(1004),
    ManufacturerName_String(1005),
    TrackingFirmwareVersion_String(1006),
    HardwareRevision_String(1007),
    AllWirelessDongleDescriptions_String(1008),
    ConnectedWirelessDongle_String(1009),
    DeviceIsWireless_Bool(1010),
    DeviceIsCharging_Bool(1011),
    DeviceBatteryPercentage_Float(1012), // 0 is empty), 1 is full
    StatusDisplayTransform_Matrix34(1013),
    Firmware_UpdateAvailable_Bool(1014),
    Firmware_ManualUpdate_Bool(1015),
    Firmware_ManualUpdateURL_String(1016),
    HardwareRevision_Uint64(1017),
    FirmwareVersion_Uint64(1018),
    FPGAVersion_Uint64(1019),
    VRCVersion_Uint64(1020),
    RadioVersion_Uint64(1021),
    DongleVersion_Uint64(1022),
    BlockServerShutdown_Bool(1023),
    CanUnifyCoordinateSystemWithHmd_Bool(1024),
    ContainsProximitySensor_Bool(1025),
    DeviceProvidesBatteryStatus_Bool(1026),
    DeviceCanPowerOff_Bool(1027),
    Firmware_ProgrammingTarget_String(1028),
    DeviceClass_Int32(1029),
    HasCamera_Bool(1030),
    DriverVersion_String(1031),
    Firmware_ForceUpdateRequired_Bool(1032),
    ViveSystemButtonFixRequired_Bool(1033),
    ParentDriver_Uint64(1034),

    // Properties that are unique to TrackedDeviceClass_HMD
    ReportsTimeSinceVSync_Bool(2000),
    SecondsFromVsyncToPhotons_Float(2001),
    DisplayFrequency_Float(2002),
    UserIpdMeters_Float(2003),
    CurrentUniverseId_Uint64(2004),
    PreviousUniverseId_Uint64(2005),
    DisplayFirmwareVersion_Uint64(2006),
    IsOnDesktop_Bool(2007),
    DisplayMCType_Int32(2008),
    DisplayMCOffset_Float(2009),
    DisplayMCScale_Float(2010),
    EdidVendorID_Int32(2011),
    DisplayMCImageLeft_String(2012),
    DisplayMCImageRight_String(2013),
    DisplayGCBlackClamp_Float(2014),
    EdidProductID_Int32(2015),
    CameraToHeadTransform_Matrix34(2016),
    DisplayGCType_Int32(2017),
    DisplayGCOffset_Float(2018),
    DisplayGCScale_Float(2019),
    DisplayGCPrescale_Float(2020),
    DisplayGCImage_String(2021),
    LensCenterLeftU_Float(2022),
    LensCenterLeftV_Float(2023),
    LensCenterRightU_Float(2024),
    LensCenterRightV_Float(2025),
    UserHeadToEyeDepthMeters_Float(2026),
    CameraFirmwareVersion_Uint64(2027),
    CameraFirmwareDescription_String(2028),
    DisplayFPGAVersion_Uint64(2029),
    DisplayBootloaderVersion_Uint64(2030),
    DisplayHardwareVersion_Uint64(2031),
    AudioFirmwareVersion_Uint64(2032),
    CameraCompatibilityMode_Int32(2033),
    ScreenshotHorizontalFieldOfViewDegrees_Float(2034),
    ScreenshotVerticalFieldOfViewDegrees_Float(2035),
    DisplaySuppressed_Bool(2036),
    DisplayAllowNightMode_Bool(2037),
    DisplayMCImageWidth_Int32(2038),
    DisplayMCImageHeight_Int32(2039),
    DisplayMCImageNumChannels_Int32(2040),
    DisplayMCImageData_Binary(2041),
    UsesDriverDirectMode_Bool(2042),

    // Properties that are unique to TrackedDeviceClass_Controller
    AttachedDeviceId_String(3000),
    SupportedButtons_Uint64(3001),
    Axis0Type_Int32(3002), //          Return value is of value openvr.EVRControllerAxisType
    Axis1Type_Int32(3003), //          Return value is of value openvr.EVRControllerAxisType
    Axis2Type_Int32(3004), //          Return value is of value openvr.EVRControllerAxisType
    Axis3Type_Int32(3005), //          Return value is of value openvr.EVRControllerAxisType
    Axis4Type_Int32(3006), //          Return value is of value openvr.EVRControllerAxisType
    ControllerRoleHint_Int32(3007), // Return value is of value openvr.ETrackedControllerRole

    // Properties that are unique to TrackedDeviceClass_TrackingReference
    FieldOfViewLeftDegrees_Float(4000),
    FieldOfViewRightDegrees_Float(4001),
    FieldOfViewTopDegrees_Float(4002),
    FieldOfViewBottomDegrees_Float(4003),
    TrackingRangeMinimumMeters_Float(4004),
    TrackingRangeMaximumMeters_Float(4005),
    ModeLabel_String(4006),

    // Properties that are used for user interface like icons names
    IconPathName_String(5000), //                      usually a directory named "icons"
    NamedIconPathDeviceOff_String(5001), //            PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    NamedIconPathDeviceSearching_String(5002), //      PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    NamedIconPathDeviceSearchingAlert_String(5003), // PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    NamedIconPathDeviceReady_String(5004), //          PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    NamedIconPathDeviceReadyAlert_String(5005), //     PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    NamedIconPathDeviceNotReady_String(5006), //       PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    NamedIconPathDeviceStandby_String(5007), //        PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others
    NamedIconPathDeviceAlertLow_String(5008), //       PNG for static icon), or GIF for animation), 50x32 for headsets and 32x32 for others

    // Properties that are used by helpers, but are opaque to applications
    DisplayHiddenArea_Binary_Start(5100),
    DisplayHiddenArea_Binary_End(5150),

    // Properties that are unique to drivers
    UserConfigPath_String(6000),
    InstallPath_String(6001),

    // Vendors are free to expose private debug data in this reserved region
    VendorSpecific_Reserved_Start(10000),
    VendorSpecific_Reserved_End(10999);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** No string property will ever be longer than this length */
val k_unMaxPropertyStringSize = 32 * 1024

/** Used to return errors that occur when reading properties. */
enum class ETrackedPropertyError(@JvmField val i: Int) {

    Success(0),
    WrongDataType(1),
    WrongDeviceClass(2),
    BufferTooSmall(3),
    UnknownProperty(4), // Driver has not set the property (and may not ever).
    InvalidDevice(5),
    CouldNotContactServer(6),
    ValueNotProvidedByDevice(7),
    StringExceedsMaximumLength(8),
    NotYetAvailable(9), // The property value isn't known yet, but is expected soon. Call again later.
    PermissionDenied(10),
    InvalidOperation(11);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class ETrackedPropertyError_ByReference(@JvmField var value: ETrackedPropertyError = ETrackedPropertyError.Success) : IntByReference(value.i)

/** Allows the application to control what part of the provided texture will be used in the frame buffer. */
open class VRTextureBounds_t : Structure {

    @JvmField var uMin = 0f
    @JvmField var vMin = 0f
    @JvmField var uMax = 0f
    @JvmField var vMax = 0f

    constructor()

    override fun getFieldOrder(): List<String> = listOf("uMin", "vMin", "uMax", "vMax")

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
    Default(0x00),

    // App submits final left and right eye images with lens distortion already applied (lens distortion makes the images appear
    // barrel distorted with chromatic aberration correction applied). The app would have used the data returned by
    // vr::openvr.IVRSystem::ComputeDistortion() to apply the correct distortion to the rendered images before calling Submit().
    LensDistortionAlreadyApplied(0x01),

    // If the texture pointer passed in is actually a renderbuffer (e.g. for MSAA in OpenGL) then set this flag.
    GlRenderBuffer(0x02),

    // Do not use
    Reserved(0x04);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Data required for passing Vulkan textures to openvr.IVRCompositor::Submit.
 * Be sure to call OpenVR_Shutdown before destroying these resources. */
/*struct VRVulkanTextureData_t
{
    uint64_t m_nImage; // VkImage
    VkDevice_T *m_pDevice;
    VkPhysicalDevice_T *m_pPhysicalDevice;
    VkInstance_T *m_pInstance;
    VkQueue_T *m_pQueue;
    uint32_t m_nQueueFamilyIndex;
    uint32_t m_nWidth, m_nHeight, m_nFormat, m_nSampleCount;
};*/

/** Data required for passing D3D12 textures to IVRCompositor::Submit.
 * Be sure to call OpenVR_Shutdown before destroying these resources. */
//struct D3D12TextureData_t
//{
//    ID3D12Resource *m_pResource;
//    ID3D12CommandQueue *m_pCommandQueue;
//    uint32_t m_nNodeMask;
//};


/** Status of the overall system or tracked objects */
enum class EVRState(@JvmField val i: Int) {

    Undefined(-1),
    Off(0),
    Searching(1),
    Searching_Alert(2),
    Ready(3),
    Ready_Alert(4),
    NotReady(5),
    Standby(6),
    Ready_Alert_Low(7);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** The types of events that could be posted (and what the parameters mean for each event value) */
enum class EVREventType(@JvmField val i: Int) {

    None(0),

    TrackedDeviceActivated(100),
    TrackedDeviceDeactivated(101),
    TrackedDeviceUpdated(102),
    TrackedDeviceUserInteractionStarted(103),
    TrackedDeviceUserInteractionEnded(104),
    IpdChanged(105),
    EnterStandbyMode(106),
    LeaveStandbyMode(107),
    TrackedDeviceRoleChanged(108),
    WatchdogWakeUpRequested(109),
    LensDistortionChanged(110),
    PropertyChanged(111),

    ButtonPress(200), //    data is controller
    ButtonUnpress(201), //  data is controller
    ButtonTouch(202), //    data is controller
    ButtonUntouch(203), //  data is controller

    MouseMove(300), //              data is mouse
    MouseButtonDown(301), //        data is mouse
    MouseButtonUp(302), //          data is mouse
    FocusEnter(303), //             data is overlay
    FocusLeave(304), //             data is overlay
    Scroll(305), //                 data is mouse
    TouchPadMove(306), //           data is mouse
    OverlayFocusChanged(307), //    data is overlay), global event

    InputFocusCaptured(400), //                         data is process DEPRECATED
    InputFocusReleased(401), //                         data is process DEPRECATED
    SceneFocusLost(402), //                             data is process
    SceneFocusGained(403), //                           data is process
    SceneApplicationChanged(404), //                    data is process - The App actually drawing the scene changed (usually to or from the compositor)
    SceneFocusChanged(405), //                          data is process - New app got access to draw the scene
    InputFocusChanged(406), //                          data is process
    SceneApplicationSecondaryRenderingStarted(407), //  data is process

    HideRenderModels(410), // Sent to the scene application to request hiding render models temporarily
    ShowRenderModels(411), // Sent to the scene application to request restoring render model visibility

    OverlayShown(500),
    OverlayHidden(501),
    DashboardActivated(502),
    DashboardDeactivated(503),
    DashboardThumbSelected(504), //     Sent to the overlay manager - data is overlay
    DashboardRequested(505), //         Sent to the overlay manager - data is overlay
    ResetDashboard(506), //             Send to the overlay manager
    RenderToast(507), //                Send to the dashboard to render a toast - data is the notification ID
    ImageLoaded(508), //                Sent to overlays when a SetOverlayRaw or SetOverlayFromFile call finishes loading
    ShowKeyboard(509), //               Sent to keyboard renderer in the dashboard to invoke it
    HideKeyboard(510), //               Sent to keyboard renderer in the dashboard to hide it
    OverlayGamepadFocusGained(511), //  Sent to an overlay when IVROverlay::SetFocusOverlay is called on it
    OverlayGamepadFocusLost(512), //    Send to an overlay when it previously had focus and IVROverlay::SetFocusOverlay is called on something else
    OverlaySharedTextureChanged(513),
    DashboardGuideButtonDown(514),
    DashboardGuideButtonUp(515),
    ScreenshotTriggered(516), //        Screenshot button combo was pressed), Dashboard should request a screenshot
    ImageFailed(517), //                Sent to overlays when a SetOverlayRaw or SetOverlayfromFail fails to load
    DashboardOverlayCreated(518),

    // Screenshot API
    RequestScreenshot(520), //              Sent by vrclient application to compositor to take a screenshot
    ScreenshotTaken(521), //                Sent by compositor to the application that the screenshot has been taken
    ScreenshotFailed(522), //               Sent by compositor to the application that the screenshot failed to be taken
    SubmitScreenshotToDashboard(523), //    Sent by compositor to the dashboard that a completed screenshot was submitted
    ScreenshotProgressToDashboard(524), //  Sent by compositor to the dashboard that a completed screenshot was submitted

    PrimaryDashboardDeviceChanged(525),

    Notification_Shown(600),
    Notification_Hidden(601),
    Notification_BeginInteraction(602),
    Notification_Destroyed(603),

    Quit(700), //                   data is process
    ProcessQuit(701), //            data is process
    QuitAborted_UserPrompt(702), // data is process
    QuitAcknowledged(703), //       data is process
    DriverRequestedQuit(704), //    The driver has requested that SteamVR shut down

    ChaperoneDataHasChanged(800),
    ChaperoneUniverseHasChanged(801),
    ChaperoneTempDataHasChanged(802),
    ChaperoneSettingsHaveChanged(803),
    SeatedZeroPoseReset(804),

    AudioSettingsHaveChanged(820),

    BackgroundSettingHasChanged(850),
    CameraSettingsHaveChanged(851),
    ReprojectionSettingHasChanged(852),
    ModelSkinSettingsHaveChanged(853),
    EnvironmentSettingsHaveChanged(854),
    PowerSettingsHaveChanged(855),

    StatusUpdate(900),

    MCImageUpdated(1000),

    FirmwareUpdateStarted(1100),
    FirmwareUpdateFinished(1101),

    KeyboardClosed(1200),
    KeyboardCharInput(1201),
    KeyboardDone(1202), // Sent when DONE button clicked on keyboard

    ApplicationTransitionStarted(1300),
    ApplicationTransitionAborted(1301),
    ApplicationTransitionNewAppStarted(1302),
    ApplicationListUpdated(1303),
    ApplicationMimeTypeLoad(1304),
    ApplicationTransitionNewAppLaunchComplete(1305),

    Compositor_MirrorWindowShown(1400),
    Compositor_MirrorWindowHidden(1401),
    Compositor_ChaperoneBoundsShown(1410),
    Compositor_ChaperoneBoundsHidden(1411),

    TrackedCamera_StartVideoStream(1500),
    TrackedCamera_StopVideoStream(1501),
    TrackedCamera_PauseVideoStream(1502),
    TrackedCamera_ResumeVideoStream(1503),
    TrackedCamera_EditingSurface(1550),

    PerformanceTest_EnableCapture(1600),
    PerformanceTest_DisableCapture(1601),
    PerformanceTest_FidelityLevel(1602),

    MessageOverlay_Closed(1650),

    // Vendors are free to expose private events in this reserved region
    VendorSpecific_Reserved_Start(10000),
    VendorSpecific_Reserved_End(19999);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Level of Hmd activity */
enum class EDeviceActivityLevel(@JvmField val i: Int) {

    Unknown(-1),
    Idle(0),
    UserInteraction(1),
    UserInteraction_Timeout(2),
    Standby(3);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** VR controller button and axis IDs */
enum class EVRButtonId(@JvmField val i: Int) {

    System(0),
    ApplicationMenu(1),
    Grip(2),
    DPad_Left(3),
    DPad_Up(4),
    DPad_Right(5),
    DPad_Down(6),
    A(7),

    ProximitySensor(31),

    Axis0(32),
    Axis1(33),
    Axis2(34),
    Axis3(35),
    Axis4(36),

    // aliases for well known controllers
    SteamVR_Touchpad(Axis0.i),
    SteamVR_Trigger(Axis1.i),

    Dashboard_Back(Grip.i),

    Max(64);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

fun buttonMaskFromId(id: EVRButtonId) = (1 shl id.i).toLong()


/** used for controller button events */
open class VREvent_Controller_t : Structure {

    @JvmField var button = 0  // openvr.EVRButtonId value
    fun button() = EVRButtonId.of(button)

    constructor()

    override fun getFieldOrder(): List<String> = listOf("button")

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

    Left(0x0001),
    Right(0x0002),
    Middle(0x0004);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** used for simulated mouse events in overlay space */
open class VREvent_Mouse_t : Structure {

    // co-ords are in GL space, bottom left of the texture is 0,0
    @JvmField var x = 0f
    @JvmField var y = 0f
    @JvmField var button = 0  // openvr.EVRMouseButton value
    fun button() = EVRMouseButton.of(button)

    constructor()

    override fun getFieldOrder(): List<String> = listOf("x", "y", "button")

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
open class VREvent_Scroll_t : Structure {

    // movement in fraction of the pad traversed since last delta, 1.0 for a full swipe
    @JvmField var xdelta = 0f
    @JvmField var ydelta = 0f
    @JvmField var repeatCount = 0

    constructor()

    override fun getFieldOrder(): List<String> = listOf("xdelta", "ydelta", "repeatCount")

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
open class VREvent_TouchPadMove_t : Structure {

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

    override fun getFieldOrder(): List<String> = listOf("bFingerDown", "flSecondsFingerDown", "fValueXFirst", "fValueYFirst",
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
open class VREvent_Notification_t : Structure {

    @JvmField var ulUserValue = 0L
    @JvmField var notificationId = 0

    constructor()

    override fun getFieldOrder(): List<String> = listOf("ulUserValue", "notificationId")

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
open class VREvent_Process_t : Structure {

    @JvmField var pid = 0
    @JvmField var oldPid = 0
    @JvmField var bForced = false

    constructor()

    override fun getFieldOrder(): List<String> = listOf("pid", "oldPid", "bForced")

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
open class VREvent_Overlay_t : Structure {

    @JvmField var overlayHandle = 0L

    constructor()

    override fun getFieldOrder(): List<String> = listOf("overlayHandle")

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
open class VREvent_Status_t : Structure {

    @JvmField var statusState = 0 // openvr.EVRState value

    constructor()

    override fun getFieldOrder(): List<String> = listOf("statusState")

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
open class VREvent_Keyboard_t : Structure {

    @JvmField var cNewInput = ""    // Up to 11 bytes of new input
    @JvmField var uUserValue = 0L // Possible flags about the new input

    constructor()

    override fun getFieldOrder(): List<String> = listOf("cNewInput", "uUserValue")

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

open class VREvent_Ipd_t : Structure {

    @JvmField var ipdMeters = 0f

    constructor()

    override fun getFieldOrder(): List<String> = listOf("ipdMeters")

    constructor(ipdMeters: Float) {
        this.ipdMeters = ipdMeters
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Ipd_t(), Structure.ByReference
    class ByValue : VREvent_Ipd_t(), Structure.ByValue
}

open class VREvent_Chaperone_t : Structure {

    @JvmField var m_nPreviousUniverse = 0L
    @JvmField var m_nCurrentUniverse = 0L

    constructor()

    override fun getFieldOrder(): List<String> = listOf("m_nPreviousUniverse", "m_nCurrentUniverse")

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
open class VREvent_Reserved_t : Structure {

    @JvmField var reserved0 = 0L
    @JvmField var reserved1 = 0L

    constructor()

    override fun getFieldOrder(): List<String> = listOf("reserved0", "reserved1")

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

open class VREvent_PerformanceTest_t : Structure {

    @JvmField var m_nFidelityLevel = 0

    constructor()

    override fun getFieldOrder(): List<String> = listOf("m_nFidelityLevel")

    constructor(m_nFidelityLevel: Int) {
        this.m_nFidelityLevel = m_nFidelityLevel
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_PerformanceTest_t(), Structure.ByReference
    class ByValue : VREvent_PerformanceTest_t(), Structure.ByValue
}

open class VREvent_SeatedZeroPoseReset_t : Structure {

    @JvmField var bResetBySystemMenu = false

    constructor()

    override fun getFieldOrder(): List<String> = listOf("bResetBySystemMenu")

    constructor(bResetBySystemMenu: Boolean) {
        this.bResetBySystemMenu = bResetBySystemMenu
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_SeatedZeroPoseReset_t(), Structure.ByReference
    class ByValue : VREvent_SeatedZeroPoseReset_t(), Structure.ByValue
}

open class VREvent_Screenshot_t : Structure {

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

    override fun getFieldOrder(): List<String> = listOf("handle", "value")

    class ByReference : VREvent_Screenshot_t(), Structure.ByReference
    class ByValue : VREvent_Screenshot_t(), Structure.ByValue
}

open class VREvent_ScreenshotProgress_t : Structure {

    @JvmField var progress = 0f

    constructor()

    constructor(progress: Float) {
        this.progress = progress
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<String> = listOf("progress")

    class ByReference : VREvent_ScreenshotProgress_t(), Structure.ByReference
    class ByValue : VREvent_ScreenshotProgress_t(), Structure.ByValue
}

open class VREvent_ApplicationLaunch_t : Structure {

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

    override fun getFieldOrder(): List<String> = listOf("pid", "unArgsHandle")

    class ByReference : VREvent_ApplicationLaunch_t(), Structure.ByReference
    class ByValue : VREvent_ApplicationLaunch_t(), Structure.ByValue
}

open class VREvent_EditingCameraSurface_t : Structure {

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

    override fun getFieldOrder(): List<String> = listOf("overlayHandle", "nVisualMode")

    class ByReference : VREvent_EditingCameraSurface_t(), Structure.ByReference
    class ByValue : VREvent_EditingCameraSurface_t(), Structure.ByValue
}

open class VREvent_MessageOverlay_t : Structure {

    @JvmField var unVRMessageOverlayResponse = 0 // vr::VRMessageOverlayResponse enum
    fun unVRMessageOverlayResponse() = VRMessageOverlayResponse.of(unVRMessageOverlayResponse)

    constructor()

    constructor(unVRMessageOverlayResponse: VRMessageOverlayResponse) : this(unVRMessageOverlayResponse.i)

    constructor(unVRMessageOverlayResponse: Int) {
        this.unVRMessageOverlayResponse = unVRMessageOverlayResponse
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<String> = listOf("unVRMessageOverlayResponse")

    class ByReference : VREvent_MessageOverlay_t(), Structure.ByReference
    class ByValue : VREvent_MessageOverlay_t(), Structure.ByValue
}

open class VREvent_Property_t : Structure {

    @JvmField var container: PropertyContainerHandle_t = 0
    @JvmField var prop = 0
    fun prop() = ETrackedDeviceProperty.of(prop)

    constructor()

    constructor(container: PropertyContainerHandle_t, prop: ETrackedDeviceProperty) : this(container, prop.i)

    constructor(container: PropertyContainerHandle_t, prop: Int) {
        this.container = container
        this.prop = prop
        write() // TODO?
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<String> = listOf("container", "prop")

    class ByReference : VREvent_Property_t(), Structure.ByReference
    class ByValue : VREvent_Property_t(), Structure.ByValue
}

/** If you change this you must manually update openvr_interop.cs.py */
open class VREvent_Data_t : Union() {

    class ByValue : VREvent_Data_t(), Structure.ByValue

    var controller: VREvent_Controller_t? = null
    var mouse: VREvent_Mouse_t? = null
    var scroll: VREvent_Scroll_t? = null
    var process: VREvent_Process_t? = null
    var notification: VREvent_Notification_t? = null
    var overlay: VREvent_Overlay_t? = null
    var status: VREvent_Status_t? = null
    var keyboard: VREvent_Keyboard_t? = null
    var ipd: VREvent_Ipd_t? = null
    var chaperone: VREvent_Chaperone_t? = null
    var performanceTest: VREvent_PerformanceTest_t? = null
    var touchPadMove: VREvent_TouchPadMove_t? = null
    var seatedZeroPoseReset: VREvent_SeatedZeroPoseReset_t? = null
    var screenshot: VREvent_Screenshot_t? = null
    var screenshotProgress: VREvent_ScreenshotProgress_t? = null
    var applicationLaunch: VREvent_ApplicationLaunch_t? = null
    var cameraSurface: VREvent_EditingCameraSurface_t? = null
    var messageOverlay: VREvent_MessageOverlay_t? = null
    var property: VREvent_Property_t? = null
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

    override fun getFieldOrder(): List<String> = listOf("eventType", "trackedDeviceIndex", "eventAgeSeconds", "data")

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

    override fun getFieldOrder(): List<String> = listOf("pVertexData", "unTriangleCount")

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

enum class EHiddenAreaMeshType(@JvmField val i: Int) {

    Standard(0),
    Inverse(1),
    LineLoop(2),
    Max(3);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Identifies what kind of axis is on the controller at index n. Read this value with pVRSystem->Get( nControllerDeviceIndex, Prop_Axis0Type_Int32 + n );   */
enum class EVRControllerAxisType(@JvmField val i: Int) {

    None(0),
    TrackPad(1),
    Joystick(2),
    Trigger(3); // Analog trigger data is in the X axis

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** contains information about one axis on the controller */
open class VRControllerAxis_t : Structure {

    @JvmField var x = 0f  // Ranges from -1.0 to 1.0 for joysticks and track pads. Ranges from 0.0 to 1.0 for triggers were 0 is fully released.
    @JvmField var y = 0f  // Ranges from -1.0 to 1.0 for joysticks and track pads. Is always 0.0 for triggers.

    constructor()

    override fun getFieldOrder(): List<String> = listOf("x", "y")

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
val k_unControllerStateAxisCount = 5

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

    override fun getFieldOrder(): List<String> = listOf("unPacketNum", "ulButtonPressed", "ulButtonTouched", "rAxis")

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

    OSEvents(0),
    VREvents(1);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Collision Bounds Style */
enum class ECollisionBoundsStyle(@JvmField val i: Int) {

    BEGINNER(0),
    INTERMEDIATE(1),
    SQUARES(2),
    ADVANCED(3),
    NONE(4),

    COUNT(5);

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

    override fun getFieldOrder(): List<String> = listOf("size", "curved", "antialias", "scale", "distance", "alpha", "uOffset", "vOffset", "uScale", "vScale",
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

val k_ulOverlayHandleInvalid = 0L

/** Errors that can occur around VR overlays */
enum class EVROverlayError(@JvmField val i: Int) {

    None(0),

    UnknownOverlay(10),
    InvalidHandle(11),
    PermissionDenied(12),
    OverlayLimitExceeded(13), // No more overlays could be created because the maximum number already exist
    WrongVisibilityType(14),
    KeyTooLong(15),
    NameTooLong(16),
    KeyInUse(17),
    WrongTransformType(18),
    InvalidTrackedDevice(19),
    InvalidParameter(20),
    ThumbnailCantBeDestroyed(21),
    ArrayTooSmall(22),
    RequestFailed(23),
    InvalidTexture(24),
    UnableToLoadFile(25),
    KeyboardAlreadyInUse(26),
    NoNeighbor(27),
    TooManyMaskPrimitives(29),
    BadMaskPrimitive(30);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class EVROverlayError_ByReference(@JvmField var value: EVROverlayError = EVROverlayError.None) : IntByReference(value.i)

/** value values to pass in to openvr.VR_Init to identify whether the application will draw a 3D scene.     */
enum class EVRApplicationType(@JvmField val i: Int) {

    Other(0), //          Some other kind of application that isn't covered by the other entries
    Scene(1), //          Application will submit 3D frames
    Overlay(2), //        Application only interacts with overlays
    Background(3), //     Application should not start SteamVR if it's not already running), and should not
    //                                  keep it running if everything else quits.
    Utility(4), //        Init should not try to load any drivers. The application needs access to utility
    //                                  interfaces (like openvr.IVRSettings and openvr.IVRApplications) but not hardware.
    VRMonitor(5), //      Reserved for vrmonitor
    SteamWatchdog(6), //  Reserved for Steam

    Max(7);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** error codes for firmware */
enum class EVRFirmwareError(@JvmField val i: Int) {

    None(0),
    Success(1),
    Fail(2);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** error codes for notifications */
enum class EVRNotificationError(@JvmField val i: Int) {

    OK(0),
    InvalidNotificationId(100),
    NotificationQueueFull(101),
    InvalidOverlayHandle(102),
    SystemWithUserValueAlreadyExists(103);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** error codes returned by Vr_Init */
// Please add adequate error description to https://developer.valvesoftware.com/w/index.php?title=Category:SteamVRHelp
enum class EVRInitError(@JvmField val i: Int) {

    None(0),
    Unknown(1),

    Init_InstallationNotFound(100),
    Init_InstallationCorrupt(101),
    Init_VRClientDLLNotFound(102),
    Init_FileNotFound(103),
    Init_FactoryNotFound(104),
    Init_InterfaceNotFound(105),
    Init_InvalidInterface(106),
    Init_UserConfigDirectoryInvalid(107),
    Init_HmdNotFound(108),
    Init_NotInitialized(109),
    Init_PathRegistryNotFound(110),
    Init_NoConfigPath(111),
    Init_NoLogPath(112),
    Init_PathRegistryNotWritable(113),
    Init_AppInfoInitFailed(114),
    Init_Retry(115), //                 Used internally to cause retries to vrserver
    Init_InitCanceledByUser(116), //    The calling application should silently exit. The user canceled app startup
    Init_AnotherAppLaunching(117),
    Init_SettingsInitFailed(118),
    Init_ShuttingDown(119),
    Init_TooManyObjects(120),
    Init_NoServerForBackgroundApp(121),
    Init_NotSupportedWithCompositor(122),
    Init_NotAvailableToUtilityApps(123),
    Init_Internal(124),
    Init_HmdDriverIdIsNone(125),
    Init_HmdNotFoundPresenceFailed(126),
    Init_VRMonitorNotFound(127),
    Init_VRMonitorStartupFailed(128),
    Init_LowPowerWatchdogNotSupported(129),
    Init_InvalidApplicationType(130),
    Init_NotAvailableToWatchdogApps(131),
    Init_WatchdogDisabledInSettings(132),
    Init_VRDashboardNotFound(133),
    Init_VRDashboardStartupFailed(134),

    Driver_Failed(200),
    Driver_Unknown(201),
    Driver_HmdUnknown(202),
    Driver_NotLoaded(203),
    Driver_RuntimeOutOfDate(204),
    Driver_HmdInUse(205),
    Driver_NotCalibrated(206),
    Driver_CalibrationInvalid(207),
    Driver_HmdDisplayNotFound(208),
    Driver_TrackedDeviceInterfaceUnknown(209),
    // Driver_HmdDisplayNotFoundAfterFix(210), // not needed: here for historic reasons
    Driver_HmdDriverIdOutOfBounds(211),
    Driver_HmdDisplayMirrored(212),

    IPC_ServerInitFailed(300),
    IPC_ConnectFailed(301),
    IPC_SharedStateInitFailed(302),
    IPC_CompositorInitFailed(303),
    IPC_MutexInitFailed(304),
    IPC_Failed(305),
    IPC_CompositorConnectFailed(306),
    IPC_CompositorInvalidConnectResponse(307),
    IPC_ConnectFailedAfterMultipleAttempts(308),

    Compositor_Failed(400),
    Compositor_D3D11HardwareRequired(401),
    Compositor_FirmwareRequiresUpdate(402),
    Compositor_OverlayInitFailed(403),
    Compositor_ScreenshotsInitFailed(404),

    VendorSpecific_UnableToConnectToOculusRuntime(1000),

    VendorSpecific_HmdFound_CantOpenDevice(1101),
    VendorSpecific_HmdFound_UnableToRequestConfigStart(1102),
    VendorSpecific_HmdFound_NoStoredConfig(1103),
    VendorSpecific_HmdFound_ConfigTooBig(1104),
    VendorSpecific_HmdFound_ConfigTooSmall(1105),
    VendorSpecific_HmdFound_UnableToInitZLib(1106),
    VendorSpecific_HmdFound_CantReadFirmwareVersion(1107),
    VendorSpecific_HmdFound_UnableToSendUserDataStart(1108),
    VendorSpecific_HmdFound_UnableToGetUserDataStart(1109),
    VendorSpecific_HmdFound_UnableToGetUserDataNext(1110),
    VendorSpecific_HmdFound_UserDataAddressRange(1111),
    VendorSpecific_HmdFound_UserDataError(1112),
    VendorSpecific_HmdFound_ConfigFailedSanityCheck(1113),

    Steam_SteamInstallationNotFound(2000);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class EVRInitError_ByReference(@JvmField var value: EVRInitError = EVRInitError.None) : IntByReference(value.i)

enum class EVRScreenshotType(@JvmField val i: Int) {

    None(0),
    Mono(1), // left eye only
    Stereo(2),
    Cubemap(3),
    MonoPanorama(4),
    StereoPanorama(5);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EVRScreenshotPropertyFilenames(@JvmField val i: Int) {

    Preview(0),
    VR(1);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EVRTrackedCameraError(@JvmField val i: Int) {

    None(0),
    OperationFailed(100),
    InvalidHandle(101),
    InvalidFrameHeaderVersion(102),
    OutOfHandles(103),
    IPCFailure(104),
    NotSupportedForThisDevice(105),
    SharedMemoryFailure(106),
    FrameBufferingFailure(107),
    StreamSetupFailure(108),
    InvalidGLTextureId(109),
    InvalidSharedTextureHandle(110),
    FailedToGetGLTextureId(111),
    SharedTextureFailure(112),
    NoFrameAvailable(113),
    InvalidArgument(114),
    InvalidFrameBufferSize(115);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EVRTrackedCameraFrameType(@JvmField val i: Int) {

    Distorted(0), //           This is the camera video frame size in pixels), still distorted.
    Undistorted(1), //         In pixels), an undistorted inscribed rectangle region without invalid regions. This size is subject to changes shortly.
    MaximumUndistorted(2), //  In pixels), maximum undistorted with invalid regions. Non zero alpha component identifies valid regions.
    MAX(3);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

typealias TrackedCameraHandle_t = Long
val INVALID_TRACKED_CAMERA_HANDLE = 0L

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

    override fun getFieldOrder(): List<String> = listOf("eFrameType", "nWidth", "nHeight", "nBytesPerPixel", "nFrameSequence", "standingTrackedDevicePose")

    class ByReference : CameraVideoStreamFrameHeader_t(), Structure.ByReference
    class ByValue : CameraVideoStreamFrameHeader_t(), Structure.ByValue
}

// Screenshot types
typealias ScreenshotHandle_t = Int
typealias ScreenshotHandle_t_ByReference = IntByReference
val k_unScreenshotHandleInvalid = 0


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
fun isHmdPresent() = VR_IsHmdPresent()

internal external fun VR_IsHmdPresent(): Boolean

/** Returns true if the OpenVR runtime is installed. */
fun isRuntimeInstalled() = VR_IsRuntimeInstalled()

internal external fun VR_IsRuntimeInstalled(): Boolean

/** Returns where the OpenVR runtime is installed. */
fun runtimePath() = VR_RuntimePath()

internal external fun VR_RuntimePath(): String

/** Returns the name of the value value for an EVRInitError. This function may be called outside of VR_Init()/VR_Shutdown(). */
fun getVRInitErrorAsSymbol(error: EVRInitError) = VR_GetVRInitErrorAsSymbol(error.i)

internal external fun VR_GetVRInitErrorAsSymbol(error: Int): String

/** Returns an english string for an EVRInitError. Applications should call VR_GetVRInitErrorAsSymbol instead and use that as a key to look up their own localized
 *  error message. This function may be called outside of VR_Init()/VR_Shutdown(). */
fun getVRInitErrorAsEnglishDescription(error: EVRInitError) = VR_GetVRInitErrorAsEnglishDescription(error.i)

internal external fun VR_GetVRInitErrorAsEnglishDescription(error: Int): String

/** Returns the interface of the specified version. This method must be called after VR_Init. The pointer returned is valid until VR_Shutdown is called.     */
fun getGenericInterface(pchInterfaceVersion: String, peError: EVRInitError_ByReference) = VR_GetGenericInterface(pchInterfaceVersion, peError)

internal external fun VR_GetGenericInterface(pchInterfaceVersion: String, peError: EVRInitError_ByReference): Pointer

/** Returns whether the interface of the specified version exists.   */
fun isInterfaceVersionValid(pchInterfaceVersion: String) = VR_IsInterfaceVersionValid(pchInterfaceVersion)

internal external fun VR_IsInterfaceVersionValid(pchInterfaceVersion: String): Boolean

/** Returns a token that represents whether the VR interface handles need to be reloaded */
fun getInitToken() = VR_GetInitToken()

internal external fun VR_GetInitToken()

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

    private val error = EVRInitError_ByReference(EVRInitError.None)

    fun VRSystem() = m_pVRSystem ?: IVRSystem(getGenericInterface(IVRSystem_Version, error))
    fun VRChaperone() = m_pVRChaperone ?: IVRChaperone(getGenericInterface(IVRChaperone_Version, error))
    fun VRChaperoneSetup() = m_pVRChaperoneSetup ?: IVRChaperoneSetup(getGenericInterface(IVRChaperoneSetup_Version, error))
    fun VRCompositor() = m_pVRCompositor ?: IVRCompositor(getGenericInterface(IVRCompositor_Version, error))
    fun VROverlay() = m_pVROverlay ?: IVROverlay(getGenericInterface(IVROverlay_Version, error))
    fun VRResources() = m_pVRResources ?: IVRResources(getGenericInterface(IVRResources_Version, error))
    fun VRRenderModels() = m_pVRRenderModels ?: IVRRenderModels(getGenericInterface(IVRRenderModels_Version, error))
    fun VRExtendedDisplay() = m_pVRExtendedDisplay ?: IVRExtendedDisplay(getGenericInterface(IVRExtendedDisplay_Version, error))
    fun VRSettings() = m_pVRSettings ?: IVRSettings(getGenericInterface(IVRSettings_Version, error))
    fun VRApplications() = m_pVRApplications ?: IVRApplications(getGenericInterface(IVRApplications_Version, error))
    fun VRTrackedCamera() = m_pVRTrackedCamera ?: IVRTrackedCamera(getGenericInterface(IVRTrackedCamera_Version, error))
    fun VRScreenshots() = m_pVRScreenshots ?: IVRScreenshots(getGenericInterface(IVRScreenshots_Version, error))
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

    if (error.value == EVRInitError.None)

        if (VR_IsInterfaceVersionValid(IVRSystem_Version))
            pVRSystem = VRSystem()
        else {
            VR_ShutdownInternal()
            error.value = EVRInitError.Init_InterfaceNotFound
        }

    return pVRSystem
}