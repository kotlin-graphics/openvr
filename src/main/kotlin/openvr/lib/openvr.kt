/**
 * Created by GBarbieri on 07.10.2016.
 */

//@file:JvmName("vr")
@file:Suppress("LeakingThis")

package openvr.lib

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.Union
import com.sun.jna.ptr.ByteByReference
import com.sun.jna.ptr.IntByReference
import com.sun.jna.ptr.LongByReference
import com.sun.jna.ptr.PointerByReference
import glm_.BYTES
import glm_.b
import glm_.i
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import java.nio.ByteBuffer

val version = "1.0.10"

class BooleanByReference(@JvmField var value: Boolean = false) : ByteByReference(if (value) 1 else 0)

// Forward declarations to avoid requiring vulkan.h
//struct VkDevice_T;
open class VkPhysicalDevice : Structure {

    constructor()

    override fun getFieldOrder(): List<String> = listOf("")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VkPhysicalDevice(), Structure.ByReference
    class ByValue : VkPhysicalDevice(), Structure.ByValue
}

open class VkInstance : Structure {

    constructor()

    override fun getFieldOrder(): List<String> = listOf("")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VkPhysicalDevice(), Structure.ByReference
    class ByValue : VkPhysicalDevice(), Structure.ByValue
}
//struct VkQueue_T;
// Forward declarations to avoid requiring d3d12.h
//struct ID3D12Resource;
//struct ID3D12CommandQueue;

typealias glSharedTextureHandle = Pointer
typealias glSharedTextureHandle_ByReference = PointerByReference
typealias glInt = Int
typealias glUInt = Int
typealias glUInt_ByReference = IntByReference

/** right-handed system
 *  +y is up
 *  +x is to the right
 *  -z is forward
 *  Distance unit is meters     */
open class HmdMat34 : Structure {

    @JvmField
    var m = FloatArray(3 * 4)

    constructor()

    override fun getFieldOrder(): List<String> = listOf("m")

    constructor(m: FloatArray) {
        if (m.size != this.m.size) throw IllegalArgumentException("Wrong array size!")
        this.m = m
        write()
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdMat34(), Structure.ByReference
    class ByValue : HmdMat34(), Structure.ByValue

    operator fun get(i: Int) = m[i]

    infix fun to(mat4: Mat4) = mat4.put(
            m[0], m[4], m[8], 0f,
            m[1], m[5], m[9], 0f,
            m[2], m[6], m[10], 0f,
            m[3], m[7], m[11], 1f)
}

open class HmdMat44 : Structure {

    @JvmField
    var m = FloatArray(4 * 4)

    constructor()

    override fun getFieldOrder(): List<String> = listOf("m")

    constructor(m: FloatArray) {
        if (m.size != this.m.size) throw IllegalArgumentException("Wrong array size!")
        this.m = m
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdMat44(), Structure.ByReference
    class ByValue : HmdMat44(), Structure.ByValue

    operator fun get(i: Int) = m[i]

    infix fun to(mat4: Mat4) = mat4.put(
            m[0], m[4], m[8], m[12],
            m[1], m[5], m[9], m[13],
            m[2], m[6], m[10], m[14],
            m[3], m[7], m[11], m[15])
}

open class HmdVec3 : Structure {

    @JvmField
    var v = FloatArray(3)

    var x
        get() = v[0]
        set(value) = v.set(0, value)
    var y
        get() = v[1]
        set(value) = v.set(1, value)
    var z
        get() = v[2]
        set(value) = v.set(2, value)

    constructor()

    override fun getFieldOrder(): List<String> = listOf("v")

    constructor(v: FloatArray) {
        if (v.size != this.v.size) throw IllegalArgumentException("Wrong array size!")
        this.v = v
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdVec3(), Structure.ByReference
    class ByValue : HmdVec3(), Structure.ByValue

    fun toDbb(bb: ByteBuffer, offset: Int) {
        for (i in 0..2) bb.putFloat(offset + i * Float.BYTES, v[i])
    }

    companion object {
        val SIZE = Vec3.size
    }

    operator fun get(i: Int) = v[i]
}

open class HmdVec4 : Structure {

    @JvmField
    var v = FloatArray(4)

    var x
        get() = v[0]
        set(value) = v.set(0, value)
    var y
        get() = v[1]
        set(value) = v.set(1, value)
    var z
        get() = v[2]
        set(value) = v.set(2, value)
    var w
        get() = v[3]
        set(value) = v.set(3, value)

    constructor()

    override fun getFieldOrder(): List<String> = listOf("v")

    constructor(v: FloatArray) {
        if (v.size != this.v.size) throw IllegalArgumentException("Wrong array size!")
        this.v = v
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdVec4(), Structure.ByReference
    class ByValue : HmdVec4(), Structure.ByValue

    operator fun get(i: Int) = v[i]
}

open class HmdVec3d : Structure {

    @JvmField
    var v = DoubleArray(3)

    var x
        get() = v[0]
        set(value) = v.set(0, value)
    var y
        get() = v[1]
        set(value) = v.set(1, value)
    var z
        get() = v[2]
        set(value) = v.set(2, value)

    constructor()

    override fun getFieldOrder(): List<String> = listOf("v")

    constructor(v: DoubleArray) {
        if (v.size != this.v.size) throw IllegalArgumentException("Wrong array size!")
        this.v = v
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdVec3d(), Structure.ByReference
    class ByValue : HmdVec3d(), Structure.ByValue

    operator fun get(i: Int) = v[i]
}

open class HmdVec2 : Structure {

    @JvmField
    var v = FloatArray(2)

    var x
        get() = v[0]
        set(value) = v.set(0, value)
    var y
        get() = v[1]
        set(value) = v.set(1, value)

    constructor()

    override fun getFieldOrder(): List<String> = listOf("v")

    constructor(v: FloatArray) {
        if (v.size != this.v.size) throw IllegalArgumentException("Wrong array size!")
        this.v = v
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdVec2(), Structure.ByReference
    class ByValue : HmdVec2(), Structure.ByValue

    operator fun get(i: Int) = v[i]
}

open class HmdQuat : Structure {

    @JvmField
    var w = 0.0
    @JvmField
    var x = 0.0
    @JvmField
    var y = 0.0
    @JvmField
    var z = 0.0

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

    class ByReference : HmdQuat(), Structure.ByReference
    class ByValue : HmdQuat(), Structure.ByValue

    operator fun get(i: Int) = when (i) {
        0 -> x
        1 -> y
        2 -> z
        3 -> w
        else -> throw IndexOutOfBoundsException()
    }
}

open class HmdColor : Structure {

    @JvmField
    var r: Float = 0f
    @JvmField
    var g: Float = 0f
    @JvmField
    var b: Float = 0f
    @JvmField
    var a: Float = 0f

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

    class ByReference : HmdColor(), Structure.ByReference
    class ByValue : HmdColor(), Structure.ByValue

    operator fun get(i: Int) = when (i) {
        0 -> r
        1 -> g
        2 -> b
        3 -> a
        else -> throw IndexOutOfBoundsException()
    }
}

open class HmdQuad : Structure {

    @JvmField
    var vCorners = arrayOf(HmdVec3(), HmdVec3(), HmdVec3(), HmdVec3())

    constructor()

    override fun getFieldOrder(): List<String> = listOf("vCorners")

    constructor(vCorners: Array<HmdVec3>) {
        if (vCorners.size != this.vCorners.size) throw IllegalArgumentException("Wrong array size!")
        this.vCorners = vCorners
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdQuad(), Structure.ByReference
    class ByValue : HmdQuad(), Structure.ByValue
}

open class HmdRect2 : Structure {

    @JvmField
    var vTopLeft = HmdVec2()
    @JvmField
    var vBottomRight = HmdVec2()

    constructor()

    override fun getFieldOrder(): List<String> = listOf("vTopLeft", "vBottomRight")

    constructor(vTopLeft: HmdVec2, vBottomRight: HmdVec2) {
        this.vTopLeft = vTopLeft
        this.vBottomRight = vBottomRight
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HmdRect2(), Structure.ByReference
    class ByValue : HmdRect2(), Structure.ByValue
}

/** Used to return the post-distortion UVs for each color channel.
 *  UVs range from 0 to 1 with 0,0 in the upper left corner of the source render target. The 0,0 to 1,1 range covers a single eye.  */
open class DistortionCoordinates : Structure {

    @JvmField
    var rfRed = FloatArray(2)
    @JvmField
    var rfGreen = FloatArray(2)
    @JvmField
    var rfBlue = FloatArray(2)

    constructor()

    override fun getFieldOrder(): List<String> = listOf("rfRed", "rfGreen", "rfBlue")

    constructor(rfRed: FloatArray, rfGreen: FloatArray, rfBlue: FloatArray) {
        if (rfRed.size != this.rfRed.size) throw IllegalArgumentException("Wrong rfRed array size!")
        this.rfRed = rfRed
        if (rfGreen.size != this.rfGreen.size) throw IllegalArgumentException("Wrong rfGreen array size!")
        this.rfGreen = rfGreen
        if (rfBlue.size != this.rfBlue.size) throw IllegalArgumentException("Wrong rfBlue array size!")
        this.rfBlue = rfBlue
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : DistortionCoordinates(), Structure.ByReference
    class ByValue : DistortionCoordinates(), Structure.ByValue
}

enum class EVREye(@JvmField val i: Int) {
    Left(0),
    Right(1);

    companion object {
        @JvmStatic
        val MAX = 2

        fun of(i: Int) = values().first { it.i == i }
    }
}

enum class ETextureType(@JvmField val i: Int) {
    /** Handle is an ID3D11Texture  */
    DirectX(0),
    /** Handle is an OpenGL texture name or an OpenGL render buffer name, depending on submit flags */
    OpenGL(1),
    /** Handle is a pointer to a VRVulkanTextureData_t structure    */
    Vulkan(2),
    /** Handle is a macOS cross-process-sharable IOSurfaceRef   */
    IOSurface(3),
    /** Handle is a pointer to a D3D12TextureData_t structure   */
    DirectX12(4);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class ETextureType_ByReference(@JvmField var value: ETextureType = ETextureType.OpenGL) : IntByReference(value.i)

enum class EColorSpace(@JvmField val i: Int) {
    /** Assumes 'gamma' for 8-bit per component formats, otherwise 'linear'.  This mirrors the DXGI formats which have
     *  _SRGB variants. */
    Auto(0),
    /** Texture data can be displayed directly on the display without any conversion (a.k.a. display native format).    */
    Gamma(1),
    /** Same as gamma but has been converted to a linear representation using DXGI's sRGB conversion algorithm. */
    Linear(2);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class EColorSpace_ByReference(@JvmField var value: EColorSpace = EColorSpace.Auto) : IntByReference(value.i)

open class Texture : Structure {

    @JvmField
    var handle = 0L  //See ETextureType definition above
    @JvmField
    var eType = 0
    val type
        get() = ETextureType.of(eType)
    @JvmField
    var eColorSpac = 0
    val colorSpace
        get() = EColorSpace.of(eColorSpac)

    constructor()

    override fun getFieldOrder(): List<String> = listOf("handle", "eType", "eColorSpac")

    constructor(handle: Long, eType: ETextureType, eColorSpace: EColorSpace) {
        this.handle = handle
        this.eType = eType.i
        this.eColorSpac = eColorSpace.i
        write()
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : Texture, Structure.ByReference {

        constructor() : super()
        constructor(handle: Long, type: ETextureType, eColorSpace: EColorSpace) : super(handle, type, eColorSpace)

        fun put(handle: Long, type: ETextureType, colorSpace: EColorSpace) {
            this.handle = handle
            this.eType = type.i
            this.eColorSpac = colorSpace.i
        }
    }

    class ByValue : Texture(), Structure.ByValue
}

// Handle to a shared texture (HANDLE on Windows obtained using OpenSharedResource).
typealias SharedTextureHandle = Long

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

typealias DriverId = Int

val driverNone = 0xFFFFFFFF.i

val maxDriverDebugResponseSize = 32768

        /** Used to pass device IDs to API calls */
typealias TrackedDeviceIndex = Int

typealias TrackedDeviceIndex_ByReference = IntByReference

val trackedDeviceIndex_Hmd = 0
val maxTrackedDeviceCount = 64
val trackedDeviceIndexOther = 0xFFFFFFFE.i
val trackedDeviceIndexInvalid = 0xFFFFFFFF.i

/** Describes what kind of object is being tracked at a given ID */
enum class ETrackedDeviceClass(@JvmField val i: Int) {
    /** the ID was not valid.   */
    Invalid(0),
    /** Head-Mounted Displays   */
    HMD(1),
    /** Tracked controllers */
    Controller(2),
    /** Generic trackers, similar to controllers    */
    GenericTracker(3),
    /** Camera and base stations that serve as tracking reference points;   */
    TrackingReference(4),
    /** Accessories that aren't necessarily tracked themselves, but may redirect video output from other tracked devices    */
    DisplayRedirect(5);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Describes what specific role associated with a tracked device */
enum class ETrackedControllerRole(@JvmField val i: Int) {
    /** Invalid value for controller value  */
    Invalid(0),
    /** Tracked device associated with the left hand    */
    LeftHand(1),
    /** Tracked device associated with the right hand   */
    RightHand(2);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** describes a single pose for a tracked object */
open class TrackedDevicePose : Structure {

    @JvmField
    var mDeviceToAbsoluteTracking = HmdMat34()
    @JvmField
    var vVelocity = HmdVec3()          // velocity in tracker space in m/s
    @JvmField
    var vAngularVelocity = HmdVec3()   // angular velocity in radians/s (?)
    @JvmField
    var eTrackingResult_internal = 0
    var eTrackingResult
        set(value) {
            eTrackingResult_internal = value.i
        }
        get() = ETrackingResult.of(eTrackingResult_internal)
    @JvmField
    var bPoseIsValid_internal = 0.b
    var bPoseIsValid
        set(value) {
            bPoseIsValid_internal = if (value) 1.b else 0.b
        }
        get() = bPoseIsValid_internal == 1.b
    /** This indicates that there is a device connected for this spot in the pose array.
     * It could go from true to false if the user unplugs the device.     */
    @JvmField
    var bDeviceIsConnected_internal = 0.b
    var bDeviceIsConnected
        set(value) {
            bDeviceIsConnected_internal = if (value) 1.b else 0.b
        }
        get() = bDeviceIsConnected_internal == 1.b

    constructor()

    override fun getFieldOrder(): List<String> = listOf("mDeviceToAbsoluteTracking", "vVelocity", "vAngularVelocity",
            "eTrackingResult_internal", "bPoseIsValid_internal", "bDeviceIsConnected_internal")

    constructor(mDeviceToAbsoluteTracking: HmdMat34, vVelocity: HmdVec3, vAngularVelocity: HmdVec3,
                eTrackingResult: ETrackingResult, bPoseIsValid: Boolean, bDeviceIsConnected: Boolean) {
        this.mDeviceToAbsoluteTracking = mDeviceToAbsoluteTracking
        this.vVelocity = vVelocity
        this.vAngularVelocity = vAngularVelocity
        this.eTrackingResult_internal = eTrackingResult.i
        this.bPoseIsValid_internal = if (bPoseIsValid) 1.b else 0.b
        this.bDeviceIsConnected_internal = if (bDeviceIsConnected) 1.b else 0.b
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : TrackedDevicePose(), Structure.ByReference
    class ByValue : TrackedDevicePose(), Structure.ByValue
}

/** Identifies which style of tracking origin the application wants to use for the poses it is requesting */
enum class ETrackingUniverseOrigin(@JvmField val i: Int) {
    /** Poses are provided relative to the seated zero pose */
    Seated(0),
    /** Poses are provided relative to the safe bounds configured by the user   */
    Standing(1),
    /** Poses are provided in the coordinate system defined by the driver. It has Y up and is unified for devices of the same driver.
     * You usually don't want this one. */
    RawAndUncalibrated(2);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class ETrackingUniverseOrigin_ByReference(@JvmField var value: ETrackingUniverseOrigin = ETrackingUniverseOrigin.Seated) : IntByReference(value.i)

// Refers to a single container of properties
typealias PropertyContainerHandle = Long

typealias PropertyTypeTag = Int

val invalidPropertyContainer: PropertyContainerHandle = 0
val invalidPropertyTag: PropertyTypeTag = 0

// Use these tags to set/get common types as struct properties
val floatPropertyTag: PropertyTypeTag = 1
val int32PropertyTag: PropertyTypeTag = 2
val uint64PropertyTag: PropertyTypeTag = 3
val boolPropertyTag: PropertyTypeTag = 4
val stringPropertyTag: PropertyTypeTag = 5

val hmdMatrix34PropertyTag: PropertyTypeTag = 20
val hmdMatrix44PropertyTag: PropertyTypeTag = 21
val hmdVector3PropertyTag: PropertyTypeTag = 22
val hmdVector4PropertyTag: PropertyTypeTag = 23

val hiddenAreaPropertyTag: PropertyTypeTag = 30
val pathHandleInfoTag = 31
val actionPropertyTag = 32
val inputValuePropertyTag = 33
val wildcardPropertyTag = 34
val hapticVibrationPropertyTag = 35

val openVRInternalReserved_Start: PropertyTypeTag = 1000
val openVRInternalReserved_End: PropertyTypeTag = 10000


/** Each entry in this value represents a property that can be retrieved about a tracked device.
 *  Many fields are only valid for one openvr.lib.ETrackedDeviceClass. */
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
    /** 0 is empty), 1 is full  */
    DeviceBatteryPercentage_Float(1012),
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
    ResourceRoot_String(1035),
    RegisteredDeviceType_String(1036),
    /** input profile to use for this device in the input system. Will default to tracking system name if this isn't provided */
    InputProfilePath_String(1037),

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
    SecondsFromPhotonsToVblank_Float(2042),
    DriverDirectModeSendsVsyncEvents_Bool(2043),
    DisplayDebugMode_Bool(2044),
    GraphicsAdapterLuid_Uint64(2045),
    DriverProvidedChaperonePath_String(2048),
    /** expected number of sensors or basestations to reserve UI space for */
    ExpectedTrackingReferenceCount_Int32(2049),
    /** expected number of tracked controllers to reserve UI space for */
    ExpectedControllerCount_Int32(2050),
    /** placeholder icon for "left" controller if not yet detected/loaded */
    NamedIconPathControllerLeftDeviceOff_String(2051),
    /** placeholder icon for "right" controller if not yet detected/loaded */
    NamedIconPathControllerRightDeviceOff_String(2052),
    /** placeholder icon for sensor/base if not yet detected/loaded */
    NamedIconPathTrackingReferenceDeviceOff_String(2053),
    DoNotApplyPrediction_Bool(2054),
    CameraToHeadTransforms_Matrix34_Array(2055),
    DriverIsDrawingControllers_Bool(2057),
    DriverRequestsApplicationPause_Bool(2058),
    DriverRequestsReducedRendering_Bool(2059),

    // Properties that are unique to TrackedDeviceClass_Controller
    AttachedDeviceId_String(3000),
    SupportedButtons_Uint64(3001),
    /** Return value is of value openvr.lib.EVRControllerAxisType   */
    Axis0Type_Int32(3002),
    /** Return value is of value openvr.lib.EVRControllerAxisType   */
    Axis1Type_Int32(3003),
    /** Return value is of value openvr.lib.EVRControllerAxisType   */
    Axis2Type_Int32(3004),
    /** Return value is of value openvr.lib.EVRControllerAxisType   */
    Axis3Type_Int32(3005),
    /** Return value is of value openvr.lib.EVRControllerAxisType   */
    Axis4Type_Int32(3006),
    /** Return value is of value openvr.lib.ETrackedControllerRole  */
    ControllerRoleHint_Int32(3007),

    // Properties that are unique to TrackedDeviceClass_TrackingReference
    FieldOfViewLeftDegrees_Float(4000),
    FieldOfViewRightDegrees_Float(4001),
    FieldOfViewTopDegrees_Float(4002),
    FieldOfViewBottomDegrees_Float(4003),
    TrackingRangeMinimumMeters_Float(4004),
    TrackingRangeMaximumMeters_Float(4005),
    ModeLabel_String(4006),


    // Properties that are used for user interface like icons names

    /** DEPRECATED. Value not referenced. Now expected to be part of icon path properties.  */
    IconPathName_String(5000),
    /** {driver}/icons/icon_filename - PNG for static icon, or GIF for animation, 50x32 for headsets and 32x32 for others   */
    NamedIconPathDeviceOff_String(5001),
    /** {driver}/icons/icon_filename - PNG for static icon, or GIF for animation, 50x32 for headsets and 32x32 for others   */
    NamedIconPathDeviceSearching_String(5002),
    /** {driver}/icons/icon_filename - PNG for static icon, or GIF for animation, 50x32 for headsets and 32x32 for others   */
    NamedIconPathDeviceSearchingAlert_String(5003),
    /** {driver}/icons/icon_filename - PNG for static icon, or GIF for animation, 50x32 for headsets and 32x32 for others   */
    NamedIconPathDeviceReady_String(5004),
    /** {driver}/icons/icon_filename - PNG for static icon, or GIF for animation, 50x32 for headsets and 32x32 for others    */
    NamedIconPathDeviceReadyAlert_String(5005),
    /** {driver}/icons/icon_filename - PNG for static icon, or GIF for animation, 50x32 for headsets and 32x32 for others    */
    NamedIconPathDeviceNotReady_String(5006),
    /** {driver}/icons/icon_filename - PNG for static icon, or GIF for animation, 50x32 for headsets and 32x32 for others    */
    NamedIconPathDeviceStandby_String(5007),
    /** {driver}/icons/icon_filename - PNG for static icon, or GIF for animation, 50x32 for headsets and 32x32 for others    */
    NamedIconPathDeviceAlertLow_String(5008),

    // Properties that are used by helpers, but are opaque to applications
    DisplayHiddenArea_Binary_Start(5100),
    DisplayHiddenArea_Binary_End(5150),
    ParentContainer(5151),

    // Properties that are unique to drivers
    UserConfigPath_String(6000),
    InstallPath_String(6001),
    HasDisplayComponent_Bool(6002),
    HasControllerComponent_Bool(6003),
    HasCameraComponent_Bool(6004),
    HasDriverDirectModeComponent_Bool(6005),
    HasVirtualDisplayComponent_Bool(6006),

    // Properties that are set internally based on other information provided by drivers
    ControllerType_String(7000),
    LegacyInputProfile_String(7001),

    // Vendors are free to expose private debug data in this reserved region
    VendorSpecific_Reserved_Start(10000),
    VendorSpecific_Reserved_End(10999),

    TrackedDeviceProperty_Max(1000000);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** No string property will ever be longer than this length */
val maxPropertyStringSize = 32 * 1024

/** Used to return errors that occur when reading properties. */
enum class ETrackedPropertyError(@JvmField val i: Int) {

    Success(0),
    WrongDataType(1),
    WrongDeviceClass(2),
    BufferTooSmall(3),
    /** Driver has not set the property (and may not ever). */
    UnknownProperty(4),
    InvalidDevice(5),
    CouldNotContactServer(6),
    ValueNotProvidedByDevice(7),
    StringExceedsMaximumLength(8),
    /** The property value isn't known yet, but is expected soon. Call again later. */
    NotYetAvailable(9),
    PermissionDenied(10),
    InvalidOperation(11),
    CannotWriteToWildcards(12);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class ETrackedPropertyError_ByReference(@JvmField var value: ETrackedPropertyError = ETrackedPropertyError.Success) : IntByReference(value.i)

/** Allows the application to control what part of the provided texture will be used in the frame buffer. */
open class VRTextureBounds : Structure {

    @JvmField
    var uMin = 0f
    @JvmField
    var vMin = 0f
    @JvmField
    var uMax = 0f
    @JvmField
    var vMax = 0f

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

    class ByReference : VRTextureBounds(), Structure.ByReference
    class ByValue : VRTextureBounds(), Structure.ByValue
}

/** Allows specifying pose used to render provided scene texture (if different from value returned by WaitGetPoses).    */
open class VRTextureWithPose : Texture {

    /** Actual pose used to render scene textures.  */
    @JvmField
    var deviceToAbsoluteTracking = HmdMat34()

    constructor()

    override fun getFieldOrder(): List<String> = listOf("deviceToAbsoluteTracking")

    constructor(deviceToAbsoluteTracking: HmdMat34) {
        this.deviceToAbsoluteTracking = deviceToAbsoluteTracking
    }

    constructor(peer: Pointer) : super(peer)    // TODO clean other constructor from read() when extending an openvr class

    class ByReference : VRTextureWithPose(), Structure.ByReference
    class ByValue : VRTextureWithPose(), Structure.ByValue
}

open class VRTextureDepthInfo : Structure {

    @JvmField
    var handle = Pointer(0) // See ETextureType definition above
    @JvmField
    var projection = HmdMat44()
    @JvmField
    var range = HmdVec2() // 0..1

    constructor()

    override fun getFieldOrder(): List<String> = listOf("handle, projection, range")

    constructor(handle: Pointer, projection: HmdMat44, range: HmdVec2) {
        this.handle = handle
        this.projection = projection
        this.range = range
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VRTextureDepthInfo(), Structure.ByReference
    class ByValue : VRTextureDepthInfo(), Structure.ByValue
}

open class VRTextureWithDepth : Texture {
    @JvmField
    var depth = VRTextureDepthInfo()

    constructor() : super()

    override fun getFieldOrder(): List<String> = listOf("depth")

    constructor(depth: VRTextureDepthInfo) {
        this.depth = depth
    }

    constructor(peer: Pointer) : super(peer)

    class ByReference : VRTextureWithDepth(), Structure.ByReference
    class ByValue : VRTextureWithDepth(), Structure.ByValue
}

open class VRTextureWithPoseAndDepth : VRTextureWithPose {
    @JvmField
    var depth = VRTextureDepthInfo()

    constructor() : super()

    override fun getFieldOrder(): List<String> = listOf("depth")

    constructor(depth: VRTextureDepthInfo) {
        this.depth = depth
    }

    constructor(peer: Pointer) : super(peer)

    class ByReference : VRTextureWithPoseAndDepth(), Structure.ByReference
    class ByValue : VRTextureWithPoseAndDepth(), Structure.ByValue
}

/** Allows the application to control how scene textures are used by the compositor when calling Submit. */
enum class EVRSubmitFlags(@JvmField val i: Int) {

    /** Simple render path. App submits rendered left and right eye images with no lens distortion correction applied.  */
    Default(0x00),
    /** App submits final left and right eye images with lens distortion already applied (lens distortion makes the
     *  images appear barrel distorted with chromatic aberration correction applied). The app would have used the data
     *  returned by vr::openvr.lib.IVRSystem::ComputeDistortion() to apply the correct distortion to the rendered images
     *  before calling Submit().    */
    LensDistortionAlreadyApplied(0x01),
    /** If the texture pointer passed in is actually a renderbuffer (e.g. for MSAA in OpenGL) then set this flag.   */
    GlRenderBuffer(0x02),
    /** Do not use  */
    Reserved(0x04),
    /** Set to indicate that pTexture is a pointer to a VRTextureWithPose.
     *  This flag can be combined with Submit_TextureWithDepth to pass a VRTextureWithPoseAndDepth. */
    TextureWithPose(0x08),
    /** Set to indicate that pTexture is a pointer to a VRTextureWithDepth.
     *  This flag can be combined with Submit_TextureWithPose to pass a VRTextureWithPoseAndDepth.  */
    TextureWithDepth(0x10);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Data required for passing Vulkan textures to openvr.lib.IVRCompositor::Submit.
 * Be sure to call OpenVR_Shutdown before destroying these resources. */
/*struct VRVulkanTextureData_t
{
    uint64_t m_nImage; // VkImage
    VkDevice_T *m_pDevice;
    VkPhysicalDevice *m_pPhysicalDevice;
    VkInstance *m_pInstance;
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
    WirelessDisconnect(112),
    WirelessReconnect(113),

    /** data is controller  */
    ButtonPress(200),
    /** data is controller  */
    ButtonUnpress(201),
    /** data is controller  */
    ButtonTouch(202),
    /** data is controller  */
    ButtonUntouch(203),
    /** data is dualAnalog */
    DualAnalog_Press(250),
    /** data is dualAnalog */
    DualAnalog_Unpress(251),
    /** data is dualAnalog */
    DualAnalog_Touch(252),
    /** data is dualAnalog */
    DualAnalog_Untouch(253),
    /** data is dualAnalog */
    DualAnalog_Move(254),
    /** data is dualAnalog */
    DualAnalog_ModeSwitch1(255),
    /** data is dualAnalog */
    DualAnalog_ModeSwitch2(256),
    /** data is dualAnalog */
    DualAnalog_Cancel(257),

    /** data is mouse   */
    MouseMove(300),
    /** data is mouse   */
    MouseButtonDown(301),
    /** data is mouse   */
    MouseButtonUp(302),
    /** data is overlay */
    FocusEnter(303),
    /** data is overlay */
    FocusLeave(304),
    /** data is mouse   */
    Scroll(305),
    /** data is mouse   */
    TouchPadMove(306),
    /** data is overlay, global event  */
    OverlayFocusChanged(307),
    /** JVM openvr custom   */
    HairTriggerMove(308),

    /** data is process DEPRECATED  */
    InputFocusCaptured(400),
    /** data is process DEPRECATED  */
    InputFocusReleased(401),
    /** data is process */
    SceneFocusLost(402),
    /** data is process */
    SceneFocusGained(403),
    /** data is process - The App actually drawing the scene changed (usually to or from the compositor)    */
    SceneApplicationChanged(404),
    /** data is process - New app got access to draw the scene  */
    SceneFocusChanged(405),
    /** data is process */
    InputFocusChanged(406),
    /** data is process */
    SceneApplicationSecondaryRenderingStarted(407),

    /** Sent to the scene application to request hiding render models temporarily   */
    HideRenderModels(410),
    /** Sent to the scene application to request restoring render model visibility  */
    ShowRenderModels(411),

    ConsoleOpened(420),
    ConsoleClosed(421),

    OverlayShown(500),
    OverlayHidden(501),
    DashboardActivated(502),
    DashboardDeactivated(503),
    /** Sent to the overlay manager - data is overlay   */
    DashboardThumbSelected(504),
    /** Sent to the overlay manager - data is overlay   */
    DashboardRequested(505),
    /** Send to the overlay manager */
    ResetDashboard(506),
    /** Send to the dashboard to render a toast - data is the notification ID   */
    RenderToast(507),
    /** Sent to overlays when a SetOverlayRaw or SetOverlayFromFile call finishes loading   */
    ImageLoaded(508),
    /** Sent to keyboard renderer in the dashboard to invoke it */
    ShowKeyboard(509),
    /** Sent to keyboard renderer in the dashboard to hide it   */
    HideKeyboard(510),
    /** Sent to an overlay when IVROverlay::SetFocusOverlay is called on it */
    OverlayGamepadFocusGained(511),
    /** Send to an overlay when it previously had focus and IVROverlay::SetFocusOverlay is called on something else */
    OverlayGamepadFocusLost(512),
    OverlaySharedTextureChanged(513),
    DashboardGuideButtonDown(514),
    DashboardGuideButtonUp(515),
    /** Screenshot button combo was pressed), Dashboard should request a screenshot */
    ScreenshotTriggered(516),
    /** Sent to overlays when a SetOverlayRaw or SetOverlayfromFail fails to load   */
    ImageFailed(517),
    DashboardOverlayCreated(518),

    // Screenshot API
    /** Sent by vrclient application to compositor to take a screenshot */
    RequestScreenshot(520),
    /** Sent by compositor to the application that the screenshot has been taken    */
    ScreenshotTaken(521),
    /** Sent by compositor to the application that the screenshot failed to be taken    */
    ScreenshotFailed(522),
    /** Sent by compositor to the dashboard that a completed screenshot was submitted   */
    SubmitScreenshotToDashboard(523),
    /** Sent by compositor to the dashboard that a completed screenshot was submitted   */
    ScreenshotProgressToDashboard(524),

    PrimaryDashboardDeviceChanged(525),

    Notification_Shown(600),
    Notification_Hidden(601),
    Notification_BeginInteraction(602),
    Notification_Destroyed(603),

    /** data is process */
    Quit(700),
    /** data is process */
    ProcessQuit(701),
    /** data is process */
    QuitAborted_UserPrompt(702),
    /** data is process */
    QuitAcknowledged(703),
    /** The driver has requested that SteamVR shut down */
    DriverRequestedQuit(704),

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
    EnableHomeAppSettingsHaveChanged(856),
    SteamVRSectionSettingChanged(857),
    LighthouseSectionSettingChanged(858),
    NullSectionSettingChanged(859),
    UserInterfaceSectionSettingChanged(860),
    NotificationsSectionSettingChanged(861),
    KeyboardSectionSettingChanged(862),
    PerfSectionSettingChanged(863),
    DashboardSectionSettingChanged(864),
    WebInterfaceSectionSettingChanged(865),

    StatusUpdate(900),

    WebInterface_InstallDriverCompleted(950),

    MCImageUpdated(1000),

    FirmwareUpdateStarted(1100),
    FirmwareUpdateFinished(1101),

    KeyboardClosed(1200),
    KeyboardCharInput(1201),
    /** Sent when DONE button clicked on keyboard   */
    KeyboardDone(1202),

    ApplicationTransitionStarted(1300),
    ApplicationTransitionAborted(1301),
    ApplicationTransitionNewAppStarted(1302),
    ApplicationListUpdated(1303),
    ApplicationMimeTypeLoad(1304),
    ApplicationTransitionNewAppLaunchComplete(1305),
    ProcessConnected(1306),
    ProcessDisconnected(1307),

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
    MessageOverlayCloseRequested(1651),
    /** data is hapticVibration */
    Input_HapticVibration(1700),

    // Vendors are free to expose private events in this reserved region
    VendorSpecific_Reserved_Start(10000),
    VendorSpecific_Reserved_End(19999);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Level of Hmd activity
 *  UserInteraction_Timeout means the device is in the process of timing out.
 *  InUse = ( k_EDeviceActivityLevel_UserInteraction || k_EDeviceActivityLevel_UserInteraction_Timeout )
 *  VREvent_TrackedDeviceUserInteractionStarted fires when the devices transitions from Standby -> UserInteraction or
 *  Idle -> UserInteraction.
 *  VREvent_TrackedDeviceUserInteractionEnded fires when the devices transitions from UserInteraction_Timeout -> Idle   */
enum class EDeviceActivityLevel(@JvmField val i: Int) {

    Unknown(-1),
    /** No activity for the last 10 seconds */
    Idle(0),
    /** Activity (movement or prox sensor) is happening now */
    UserInteraction(1),
    /** No activity for the last 0.5 seconds    */
    UserInteraction_Timeout(2),
    /** Idle for at least 5 seconds (configurable in Settings -> Power Management)  */
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

    val mask get() = 1L shl i
}

fun buttonMaskFromId(id: EVRButtonId) = 1L shl id.i


/** used for controller button events */
open class VREvent_Controller : Structure {

    @JvmField
    var button_internal = 0  // openvr.lib.EVRButtonId value
    var button
        set(value) {
            button_internal = value.i
        }
        get() = EVRButtonId.of(button_internal)

    constructor()

    override fun getFieldOrder(): List<String> = listOf("button_internal")

    constructor(button: EVRButtonId) {
        this.button_internal = button.i
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Controller(), Structure.ByReference
    class ByValue : VREvent_Controller(), Structure.ByValue
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
open class VREvent_Mouse : Structure {

    // coords are in GL space, bottom left of the texture is 0,0
    @JvmField
    var x = 0f
    @JvmField
    var y = 0f
    @JvmField
    var button_internal = 0  // openvr.lib.EVRMouseButton value
    var button
        set(value) {
            button_internal = value.i
        }
        get() = EVRMouseButton.of(button_internal)

    constructor()

    override fun getFieldOrder(): List<String> = listOf("x", "y", "button_internal")

    constructor(x: Float, y: Float, button: EVRMouseButton) {
        this.x = x
        this.y = y
        this.button_internal = button.i
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Mouse(), Structure.ByReference
    class ByValue : VREvent_Mouse(), Structure.ByValue
}

/** used for simulated mouse wheel scroll in overlay space */
open class VREvent_Scroll : Structure {

    // movement in fraction of the pad traversed since last delta, 1.0 for a full swipe
    @JvmField
    var xdelta = 0f
    @JvmField
    var ydelta = 0f
    @JvmField
    var repeatCount = 0

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

    class ByReference : VREvent_Scroll(), Structure.ByReference
    class ByValue : VREvent_Scroll(), Structure.ByValue
}

/** when in mouse input mode you can receive data from the touchpad, these events are only sent if the users finger
 *  is on the touchpad (or just released from it). These events are sent to overlays with the
 *  VROverlayFlags_SendVRTouchpadEvents flag set. */
open class VREvent_TouchPadMove : Structure {

    /** true if the users finger is detected on the touch pad */
    @JvmField
    var bFingerDown_internal = 0.b
    var bFingerDown
        set(value) {
            bFingerDown_internal = if (value) 1.b else 0.b
        }
        get() = bFingerDown_internal == 1.b

    /** How long the finger has been down in seconds */
    @JvmField
    var flSecondsFingerDown = 0f

    /** These values indicate the starting finger position (so you can do some basic swipe stuff) */
    @JvmField
    var fValueXFirst = 0f
    @JvmField
    var fValueYFirst = 0f

    /** This is the raw sampled coordinate without deadzoning */
    @JvmField
    var fValueXRaw = 0f
    @JvmField
    var fValueYRaw = 0f

    constructor()

    override fun getFieldOrder(): List<String> = listOf("bFingerDown_internal", "flSecondsFingerDown", "fValueXFirst", "fValueYFirst",
            "fValueXRaw", "fValueYRaw")

    constructor(bFingerDown: Boolean, flSecondsFingerDown: Float, fValueXFirst: Float, fValueYFirst: Float, fValueXRaw: Float, fValueYRaw: Float) {
        this.bFingerDown_internal = if (bFingerDown) 1.b else 0.b
        this.flSecondsFingerDown = flSecondsFingerDown
        this.fValueXFirst = fValueXFirst
        this.fValueYFirst = fValueYFirst
        this.fValueXRaw = fValueXRaw
        this.fValueYRaw = fValueYRaw
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_TouchPadMove(), Structure.ByReference
    class ByValue : VREvent_TouchPadMove(), Structure.ByValue
}

enum class EDualAnalogWhich {
    Left, Right;

    val i = ordinal

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

open class VREvent_DualAnalog : Structure {

    /** coordinates are -1..1 analog values */
    @JvmField
    var x = 0f
    @JvmField
    var y = 0f

    /** transformed by the center and radius numbers provided by the overlay */
    @JvmField
    var transformedX = 0f
    @JvmField
    var transformedY = 0f

    @JvmField
    var which_internal = 0
    var which
        get() = EDualAnalogWhich.of(which_internal)
        set(value) {
            which_internal = value.i
        }

    constructor()

    override fun getFieldOrder(): List<String> = listOf("x", "y", "transformedX", "transformedY", "which_internal")

    constructor(x: Float, y: Float, transformedX: Float, transformedY: Float, which: EDualAnalogWhich) {
        this.x = x
        this.y = y
        this.transformedX = transformedX
        this.transformedY = transformedY
        this.which = which
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_DualAnalog(), Structure.ByReference
    class ByValue : VREvent_DualAnalog(), Structure.ByValue
}

open class VREvent_HapticVibration : Structure {

    /** property container handle of the device with the haptic component */
    @JvmField
    var containerHandle = 0L
    /** Which haptic component needs to vibrate */
    @JvmField
    var componentHandle = 0L
    @JvmField
    var durationSeconds = 0f
    @JvmField
    var frequency = 0f
    @JvmField
    var amplitude = 0f

    constructor()

    override fun getFieldOrder(): List<String> = listOf("containerHandle", "componentHandle", "durationSeconds", "frequency", "amplitude")

    constructor(containerHandle: Long, componentHandle: Long, durationSeconds: Float, frequency: Float, amplitude: Float) {
        this.containerHandle = containerHandle
        this.componentHandle = componentHandle
        this.durationSeconds = durationSeconds
        this.frequency = frequency
        this.amplitude = amplitude
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_HapticVibration(), Structure.ByReference
    class ByValue : VREvent_HapticVibration(), Structure.ByValue
}

/** notification related events. Details will still change at this point */
open class VREvent_Notification : Structure {

    @JvmField
    var ulUserValue = 0L
    @JvmField
    var notificationId = 0

    constructor()

    override fun getFieldOrder(): List<String> = listOf("ulUserValue", "notificationId")

    constructor(ulUserValue: Long, notificationId: Int) {
        this.ulUserValue = ulUserValue
        this.notificationId = notificationId
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Notification(), Structure.ByReference
    class ByValue : VREvent_Notification(), Structure.ByValue
}

/** Used for events about processes */
open class VREvent_Process : Structure {

    @JvmField
    var pid = 0
    @JvmField
    var oldPid = 0
    @JvmField
    var bForced_internal = 0.b
    var bForced
        set(value) {
            bForced_internal = if (value) 1.b else 0.b
        }
        get() = bForced_internal == 1.b

    constructor()

    override fun getFieldOrder(): List<String> = listOf("pid", "oldPid", "bForced_internal")

    constructor(pid: Int, oldPid: Int, bForced: Boolean) {
        this.pid = pid
        this.oldPid = oldPid
        this.bForced_internal = if (bForced) 1.b else 0.b
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Process(), Structure.ByReference
    class ByValue : VREvent_Process(), Structure.ByValue
}

/** Used for a few events about overlays */
open class VREvent_Overlay : Structure {

    @JvmField
    var overlayHandle = 0L

    constructor()

    override fun getFieldOrder(): List<String> = listOf("overlayHandle")

    constructor(overlayHandle: Long) {
        this.overlayHandle = overlayHandle
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Overlay(), Structure.ByReference
    class ByValue : VREvent_Overlay(), Structure.ByValue
}

/** Used for a few events about overlays */
open class VREvent_Status : Structure {

    @JvmField
    var statusState_internal = 0 // openvr.lib.EVRState value
    var statusState
        set(value) {
            statusState_internal = value.i
        }
        get() = EVRState.of(statusState_internal)

    constructor()

    override fun getFieldOrder(): List<String> = listOf("statusState_internal")

    constructor(statusState: EVRState) {
        this.statusState_internal = statusState.i
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Status(), Structure.ByReference
    class ByValue : VREvent_Status(), Structure.ByValue
}

/** Used for keyboard events **/
open class VREvent_Keyboard : Structure {

    @JvmField
    var cNewInput_internal = ByteArray(8)    // Up to 11 bytes of new input
    var cNewInput
        set(value) {
            cNewInput_internal = value.take(8).toByteArray()
        }
        get() = String(cNewInput_internal).take(8) // TODO check \0
    @JvmField
    var uUserValue = 0L // Possible flags about the new input

    constructor()

    override fun getFieldOrder(): List<String> = listOf("cNewInput_internal", "uUserValue")

    constructor(cNewInput: String, uUserValue: Long) {
        this.cNewInput_internal = cNewInput.take(8).toByteArray()
        this.uUserValue = uUserValue
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Keyboard(), Structure.ByReference
    class ByValue : VREvent_Keyboard(), Structure.ByValue
}

open class VREvent_Ipd : Structure {

    @JvmField
    var ipdMeters = 0f

    constructor()

    override fun getFieldOrder(): List<String> = listOf("ipdMeters")

    constructor(ipdMeters: Float) {
        this.ipdMeters = ipdMeters
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Ipd(), Structure.ByReference
    class ByValue : VREvent_Ipd(), Structure.ByValue
}

open class VREvent_Chaperone : Structure {

    @JvmField
    var m_nPreviousUniverse = 0L
    @JvmField
    var m_nCurrentUniverse = 0L

    constructor()

    override fun getFieldOrder(): List<String> = listOf("m_nPreviousUniverse", "m_nCurrentUniverse")

    constructor(m_nPreviousUniverse: Long, m_nCurrentUniverse: Long) {
        this.m_nPreviousUniverse = m_nPreviousUniverse
        this.m_nCurrentUniverse = m_nCurrentUniverse
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Chaperone(), Structure.ByReference
    class ByValue : VREvent_Chaperone(), Structure.ByValue
}

/** Not actually used for any events */
open class VREvent_Reserved : Structure {

    @JvmField
    var reserved0 = 0L
    @JvmField
    var reserved1 = 0L

    constructor()

    override fun getFieldOrder(): List<String> = listOf("reserved0", "reserved1")

    constructor(reserved0: Long, reserved1: Long) {
        this.reserved0 = reserved0
        this.reserved1 = reserved1
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Reserved(), Structure.ByReference
    class ByValue : VREvent_Reserved(), Structure.ByValue
}

open class VREvent_PerformanceTest : Structure {

    @JvmField
    var m_nFidelityLevel = 0

    constructor()

    override fun getFieldOrder(): List<String> = listOf("m_nFidelityLevel")

    constructor(m_nFidelityLevel: Int) {
        this.m_nFidelityLevel = m_nFidelityLevel
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_PerformanceTest(), Structure.ByReference
    class ByValue : VREvent_PerformanceTest(), Structure.ByValue
}

open class VREvent_SeatedZeroPoseReset : Structure {

    @JvmField
    var bResetBySystemMenu_internal = 0.b
    var bResetBySystemMenu
        set(value) {
            bResetBySystemMenu_internal = if (value) 1.b else 0.b
        }
        get() = bResetBySystemMenu_internal == 1.b

    constructor()

    override fun getFieldOrder(): List<String> = listOf("bResetBySystemMenu_internal")

    constructor(bResetBySystemMenu: Boolean) {
        this.bResetBySystemMenu_internal = if (bResetBySystemMenu) 1.b else 0.b
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_SeatedZeroPoseReset(), Structure.ByReference
    class ByValue : VREvent_SeatedZeroPoseReset(), Structure.ByValue
}

open class VREvent_Screenshot : Structure {

    @JvmField
    var handle = 0
    @JvmField
    var type = 0

    constructor()

    constructor(handle: Int, type: Int) {
        this.handle = handle
        this.type = type
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<String> = listOf("handle", "type")

    class ByReference : VREvent_Screenshot(), Structure.ByReference
    class ByValue : VREvent_Screenshot(), Structure.ByValue
}

open class VREvent_ScreenshotProgress : Structure {

    @JvmField
    var progress = 0f

    constructor()

    constructor(progress: Float) {
        this.progress = progress
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<String> = listOf("progress")

    class ByReference : VREvent_ScreenshotProgress(), Structure.ByReference
    class ByValue : VREvent_ScreenshotProgress(), Structure.ByValue
}

open class VREvent_ApplicationLaunch : Structure {

    @JvmField
    var pid = 0
    @JvmField
    var unArgsHandle = 0

    constructor()

    constructor(pid: Int, unArgsHandle: Int) {
        this.pid = pid
        this.unArgsHandle = unArgsHandle
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<String> = listOf("pid", "unArgsHandle")

    class ByReference : VREvent_ApplicationLaunch(), Structure.ByReference
    class ByValue : VREvent_ApplicationLaunch(), Structure.ByValue
}

open class VREvent_EditingCameraSurface : Structure {

    @JvmField
    var overlayHandle = 0L
    @JvmField
    var nVisualMode = 0

    constructor()

    constructor(overlayHandle: Long, nVisualMode: Int) {
        this.overlayHandle = overlayHandle
        this.nVisualMode = nVisualMode
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<String> = listOf("overlayHandle", "nVisualMode")

    class ByReference : VREvent_EditingCameraSurface(), Structure.ByReference
    class ByValue : VREvent_EditingCameraSurface(), Structure.ByValue
}

open class VREvent_MessageOverlay : Structure {

    @JvmField
    var unVRMessageOverlayResponse_internal = 0 // vr::VRMessageOverlayResponse enum
    var unVRMessageOverlayResponse
        set(value) {
            unVRMessageOverlayResponse_internal = value.i
        }
        get() = VRMessageOverlayResponse.of(unVRMessageOverlayResponse_internal)

    constructor()

    constructor(unVRMessageOverlayResponse: VRMessageOverlayResponse) {
        this.unVRMessageOverlayResponse_internal = unVRMessageOverlayResponse.i
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<String> = listOf("unVRMessageOverlayResponse_internal")

    class ByReference : VREvent_MessageOverlay(), Structure.ByReference
    class ByValue : VREvent_MessageOverlay(), Structure.ByValue
}

open class VREvent_Property : Structure {

    @JvmField
    var container: PropertyContainerHandle = 0
    @JvmField
    var prop_internal = 0
    var prop
        set(value) {
            prop_internal = value.i
        }
        get() = ETrackedDeviceProperty.of(prop_internal)

    constructor()

    constructor(container: PropertyContainerHandle, prop: ETrackedDeviceProperty) {
        this.container = container
        this.prop_internal = prop.i
        write() // TODO?
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<String> = listOf("container", "prop_internal")

    class ByReference : VREvent_Property(), Structure.ByReference
    class ByValue : VREvent_Property(), Structure.ByValue
}

/** If you change this you must manually update openvr_interop.cs.py */
open class VREvent_Data : Union {

    @JvmField
    var controller = VREvent_Controller()
    @JvmField
    var mouse = VREvent_Mouse()
    @JvmField
    var scroll = VREvent_Scroll()
    @JvmField
    var process = VREvent_Process()
    @JvmField
    var notification = VREvent_Notification()
    @JvmField
    var overlay = VREvent_Overlay()
    @JvmField
    var status = VREvent_Status()
    @JvmField
    var keyboard = VREvent_Keyboard()
    @JvmField
    var ipd = VREvent_Ipd()
    @JvmField
    var chaperone = VREvent_Chaperone()
    @JvmField
    var performanceTest = VREvent_PerformanceTest()
    @JvmField
    var touchPadMove = VREvent_TouchPadMove()
    @JvmField
    var seatedZeroPoseReset = VREvent_SeatedZeroPoseReset()
    @JvmField
    var screenshot = VREvent_Screenshot()
    @JvmField
    var screenshotProgress = VREvent_ScreenshotProgress()
    @JvmField
    var applicationLaunch = VREvent_ApplicationLaunch()
    @JvmField
    var cameraSurface = VREvent_EditingCameraSurface()
    @JvmField
    var messageOverlay = VREvent_MessageOverlay()
    @JvmField
    var property = VREvent_Property()
    @JvmField
    var dualAnalog = VREvent_DualAnalog()
    @JvmField
    var hapticVibration = VREvent_HapticVibration()

    constructor() : super()
    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent_Data(), Structure.ByReference
    class ByValue : VREvent_Data(), Structure.ByValue

    override fun getFieldOrder(): List<String> = listOf("controller", "mouse", "scroll", "process", "notification", "overlay",
            "status", "keyboard", "ipd", "chaperone", "performanceTest", "touchPadMove", "seatedZeroPoseReset", "screenshot",
            "screenshotProgress", "applicationLaunch", "cameraSurface", "messageOverlay", "property", "dualAnalog", "hapticVibration")
}

/** An event posted by the server to all running applications */
open class VREvent : Structure {

    @JvmField
    var eventType_internal = 0   // openvr.lib.EVREventType value
    var eventType
        set(value) {
            eventType_internal = value.i
        }
        get() = EVREventType.of(eventType_internal)
    @JvmField
    var trackedDeviceIndex = 0
    @JvmField
    var eventAgeSeconds = 0f
    // event data must be the end of the struct as its size is variable
    @JvmField
    var data = VREvent_Data()

    constructor()

    override fun getFieldOrder(): List<String> = listOf("eventType_internal", "trackedDeviceIndex", "eventAgeSeconds", "data")

    constructor(eventType: EVREventType, trackedDeviceIndex: Int, eventAgeSeconds: Float, data: VREvent_Data) {
        this.eventType_internal = eventType.i
        this.trackedDeviceIndex = trackedDeviceIndex
        this.eventAgeSeconds = eventAgeSeconds
        this.data = data
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VREvent(), Structure.ByReference

    class ByValue : VREvent(), Structure.ByValue
}

enum class EVRInputError {
    None,
    NameNotFound,
    WrongType,
    InvalidHandle,
    InvalidParam,
    NoSteam,
    MaxCapacityReached,
    IPCError,
    NoActiveActionSet,
    InvalidDevice;

    val i = ordinal
}

/** The mesh to draw into the stencil (or depth) buffer to perform early stencil (or depth) kills of pixels that will never appear on the HMD.
 *  This mesh draws on all the pixels that will be hidden after distortion. *
 *  If the HMD does not provide a visible area mesh pVertexData will be NULL and unTriangleCount will be 0. */
open class HiddenAreaMesh : Structure {

    @JvmField
    var pVertexData: HmdVec2.ByReference? = null
    @JvmField
    var unTriangleCount = 0

    constructor()

    override fun getFieldOrder(): List<String> = listOf("pVertexData", "unTriangleCount")

    constructor(pVertexData: HmdVec2.ByReference, unTriangleCount: Int) {
        this.pVertexData = pVertexData
        this.unTriangleCount = unTriangleCount
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : HiddenAreaMesh(), Structure.ByReference
    class ByValue : HiddenAreaMesh(), Structure.ByValue
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
    /** Analog trigger data is in the X axis    */
    Trigger(3);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** contains information about one axis on the controller */
open class VRControllerAxis : Structure {

    @JvmField
    var x = 0f  // Ranges from -1.0 to 1.0 for joysticks and track pads. Ranges from 0.0 to 1.0 for triggers were 0 is fully released.
    @JvmField
    var y = 0f  // Ranges from -1.0 to 1.0 for joysticks and track pads. Is always 0.0 for triggers.

    val pos = Vec2()
        get() = field.put(x, y)

    constructor()

    override fun getFieldOrder(): List<String> = listOf("x", "y")

    constructor(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VRControllerAxis(), Structure.ByReference
    class ByValue : VRControllerAxis(), Structure.ByValue
}

/** the number of axes in the controller state */
val controllerStateAxisCount = 5

/** Holds all the state of a controller at one moment in time. */
open class VRControllerState : Structure {

    /** If packet num matches that on your prior call, then the controller state hasn't been changed since your last
     *  call and there is no need to process it.    */
    @JvmField
    var unPacketNum = 0

    // bit flags for each of the buttons. Use ButtonMaskFromId to turn an ID into a mask
    @JvmField
    var ulButtonPressed = 0L
    @JvmField
    var ulButtonTouched = 0L

    // Axis data for the controller's analog inputs
    @JvmField
    var rAxis = Array(controllerStateAxisCount, { VRControllerAxis() })

    val axis0 get() = rAxis[0]
    val axis1 get() = rAxis[1]
    val axis2 get() = rAxis[2]
    val axis3 get() = rAxis[3]
    val axis4 get() = rAxis[4]

    constructor()

    override fun getFieldOrder(): List<String> = listOf("unPacketNum", "ulButtonPressed", "ulButtonTouched", "rAxis")

    constructor(unPacketNum: Int, ulButtonPressed: Long, ulButtonTouched: Long, rAxis: Array<VRControllerAxis>) {
        this.unPacketNum = unPacketNum
        this.ulButtonPressed = ulButtonPressed
        this.ulButtonTouched = ulButtonTouched
        if (rAxis.size != this.rAxis.size) throw IllegalArgumentException("Wrong array size!")
        this.rAxis = rAxis
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VRControllerState(), Structure.ByReference
    class ByValue : VRControllerState(), Structure.ByValue
}

typealias VRControllerState001 = VRControllerState

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

    @JvmField
    var size = 0    // sizeof(openvr.lib.Compositor_OverlaySettings)
    @JvmField
    var curved_internal = 0.b
    var curved
        set(value) {
            curved_internal = if (value) 1.b else 0.b
        }
        get() = curved_internal == 1.b
    @JvmField
    var antialias_internal = 0.b
    var antialias
        set(value) {
            antialias_internal = if (value) 1.b else 0.b
        }
        get() = antialias_internal == 1.b
    @JvmField
    var scale = 0f
    @JvmField
    var distance = 0f
    @JvmField
    var alpha = 0f
    @JvmField
    var uOffset = 0f
    @JvmField
    var vOffset = 0f
    @JvmField
    var uScale = 0f
    @JvmField
    var vScale = 0f
    @JvmField
    var gridDivs = 0f
    @JvmField
    var gridWidth = 0f
    @JvmField
    var gridScale = 0f
    @JvmField
    var transform = HmdMat44()

    constructor()

    constructor(size: Int, curved: Boolean, antialias: Boolean, scale: Float, distance: Float, alpha: Float, uOffset: Float,
                vOffset: Float, uScale: Float, vScale: Float, gridDivs: Float, gridWidth: Float, gridScale: Float,
                transform: HmdMat44) {
        this.size = size
        this.curved_internal = if (curved) 1.b else 0.b
        this.antialias_internal = if (antialias) 1.b else 0.b
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

    override fun getFieldOrder(): List<String> = listOf("size", "curved_internal", "antialias_internal", "scale", "distance", "alpha",
            "uOffset", "vOffset", "uScale", "vScale", "gridDivs", "gridWidth", "gridScale", "transform")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : Compositor_OverlaySettings(), Structure.ByReference
    class ByValue : Compositor_OverlaySettings(), Structure.ByValue
}

        /** used to refer to a single VR overlay */
typealias VROverlayHandle = Long

typealias VROverlayHandle_ByReference = LongByReference

val overlayHandleInvalid = 0L

/** Errors that can occur around VR overlays */
enum class EVROverlayError(@JvmField val i: Int) {

    None(0),

    UnknownOverlay(10),
    InvalidHandle(11),
    PermissionDenied(12),
    /** No more overlays could be created because the maximum number already exist  */
    OverlayLimitExceeded(13),
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
    BadMaskPrimitive(30),
    TextureAlreadyLocked(31),
    TextureLockCapacityReached(32),
    TextureNotLocked(33);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class EVROverlayError_ByReference(@JvmField var value: EVROverlayError = EVROverlayError.None) : IntByReference(value.i)

/** value values to pass in to openvr.VR_Init to identify whether the application will draw a 3D scene.     */
enum class EVRApplicationType(@JvmField val i: Int) {
    /** Some other kind of application that isn't covered by the other entries  */
    Other(0),
    /** Application will submit 3D frames   */
    Scene(1),
    /** Application only interacts with overlays    */
    Overlay(2),
    /** Application should not start SteamVR if it's not already running), and should not keep it running if everything
     *  else quits. */
    Background(3),
    /** Init should not try to load any drivers. The application needs access to utility interfaces
     *  (like openvr.lib.IVRSettings and openvr.lib.IVRApplications) but not hardware.  */
    Utility(4),
    /** Reserved for vrmonitor  */
    VRMonitor(5),
    /** Reserved for Steam  */
    SteamWatchdog(6),
    /** Start up SteamVR    */
    Bootstrapper(7),

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
    /** Used internally to cause retries to vrserver    */
    Init_Retry(115),
    /** The calling application should silently exit. The user canceled app startup */
    Init_InitCanceledByUser(116),
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
    Init_VRHomeNotFound(135),
    Init_VRHomeStartupFailed(136),
    Init_RebootingBusy(137),
    Init_FirmwareUpdateBusy(138),
    Init_FirmwareRecoveryBusy(139),

    Init_USBServiceBusy(140),

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
    Compositor_UnableToCreateDevice(405),

    VendorSpecific_UnableToConnectToOculusRuntime(1000),
    VendorSpecific_WindowsNotInDevMode(1001),

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
    /** left eye only   */
    Mono(1),
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
    /** This is the camera video frame size in pixels), still distorted.    */
    Distorted(0),
    /** In pixels), an undistorted inscribed rectangle region without invalid regions. This size is subject to changes
     *  shortly. */
    Undistorted(1),
    /** In pixels), maximum undistorted with invalid regions. Non zero alpha component identifies valid regions.    */
    MaximumUndistorted(2),
    MAX(3);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

typealias TrackedCameraHandle = Long

val INVALID_TRACKED_CAMERA_HANDLE = 0L

open class CameraVideoStreamFrameHeader : Structure {

    @JvmField
    var eFrameType_internal = 0
    var eFrameType
        set(value) {
            eFrameType_internal = value.i
        }
        get() = EVRTrackedCameraFrameType.of(eFrameType_internal)

    @JvmField
    var nWidth = 0
    @JvmField
    var nHeight = 0
    @JvmField
    var nBytesPerPixel = 0

    @JvmField
    var nFrameSequence = 0

    @JvmField
    var standingTrackedDevicePose = TrackedDevicePose()

    constructor()

    constructor(eFrameType: EVRTrackedCameraFrameType, nWidth: Int, nHeight: Int, nBytesPerPixel: Int, nFrameSequence: Int,
                standingTrackedDevicePose: TrackedDevicePose) {

        this.eFrameType_internal = eFrameType.i
        this.nWidth = nWidth
        this.nHeight = nHeight
        this.nBytesPerPixel = nBytesPerPixel
        this.nFrameSequence = nFrameSequence
        this.standingTrackedDevicePose = standingTrackedDevicePose
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<String> = listOf("eFrameType_internal", "nWidth", "nHeight", "nBytesPerPixel", "nFrameSequence",
            "standingTrackedDevicePose")

    class ByReference : CameraVideoStreamFrameHeader(), Structure.ByReference
    class ByValue : CameraVideoStreamFrameHeader(), Structure.ByValue
}

// Screenshot types
typealias ScreenshotHandle = Int

typealias ScreenshotHandle_ByReference = IntByReference

val screenshotHandleInvalid = 0

/** Frame timing data provided by direct mode drivers. */
open class DriverDirectMode_FrameTiming : Structure {

    /** Set to sizeof( DriverDirectMode_FrameTiming ) */
    @JvmField
    var size = 0
    /** number of times frame was presented */
    @JvmField
    var numFramePresents = 0
    /** number of times frame was presented on a vsync other than it was originally predicted to */
    @JvmField
    var numMisPresented = 0
    /** number of additional times previous frame was scanned out (i.e. compositor missed vsync) */
    @JvmField
    var numDroppedFrames = 0
    @JvmField
    var reprojectionFlags = 0

    constructor()

    constructor(size: Int, numFramePresents: Int, numMisPresented: Int, numDroppedFrames: Int, reprojectionFlags: Int) {

        this.size = size
        this.numFramePresents = numFramePresents
        this.numMisPresented = numMisPresented
        this.numDroppedFrames = numDroppedFrames
        this.reprojectionFlags = reprojectionFlags
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<String> = listOf("size", "numFramePresents", "numMisPresented", "numDroppedFrames", "reprojectionFlags")

    class ByReference : DriverDirectMode_FrameTiming(), Structure.ByReference
    class ByValue : DriverDirectMode_FrameTiming(), Structure.ByValue
}

// ================================================================================================================================================================

/** Finds the active installation of the VR API and initializes it. The provided path must be absolute or relative to
 *  the current working directory. These are the local install versions of the equivalent functions in steamvr.h and
 *  will work without a local Steam install.
 *
 *  This path is to the "root" of the VR API install. That's the directory with the "drivers" directory and a platform
 *  (i.e. "win32") directory in it, not the directory with the DLL itself.
 *
 *  startupInfo is reserved for future use.    */
fun vrInit(error: EVRInitError_ByReference?, applicationType: EVRApplicationType, startupInfo: String? = null): IVRSystem? {

    try{
        Native.register("openvr_api")
    } catch (exc: Exception) {
        println(exc)
        println(exc.message)
    }

    var pVRSystem: IVRSystem? = null

    val eError = EVRInitError_ByReference()
    vrToken = VR_InitInternal2(eError, applicationType.i, startupInfo)
    COpenVRContext.clear()

    if (eError.value == EVRInitError.None)
        if (VR_IsInterfaceVersionValid(IVRSystem_Version))
            pVRSystem = vrSystem
        else {
            VR_ShutdownInternal()
            eError.value = EVRInitError.Init_InterfaceNotFound
        }
    error?.value = eError.value
    return pVRSystem
}

/** unloads vrclient.dll. Any interface pointers from the interface are invalid after this point */
fun vrShutdown() = VR_ShutdownInternal()

/** Returns true if there is an HMD attached. This check is as lightweight as possible and can be called outside of VR_Init/VR_Shutdown. It should be used when
 *  an application wants to know if initializing VR is a possibility but isn't ready to take that step yet.  */
fun vrIsHmdPresent() = VR_IsHmdPresent()

internal external fun VR_IsHmdPresent(): Boolean

/** Returns true if the OpenVR runtime is installed. */
fun vrIsRuntimeInstalled() = VR_IsRuntimeInstalled()

internal external fun VR_IsRuntimeInstalled(): Boolean

/** Returns where the OpenVR runtime is installed. */
fun vrRuntimePath() = VR_RuntimePath()

internal external fun VR_RuntimePath(): String

/** Returns the name of the value value for an EVRInitError. This function may be called outside of VR_Init()/VR_Shutdown(). */
fun vrGetVRInitErrorAsSymbol(error: EVRInitError) = VR_GetVRInitErrorAsSymbol(error.i)

internal external fun VR_GetVRInitErrorAsSymbol(error: Int): String

/** Returns an English string for an EVRInitError. Applications should call VR_GetVRInitErrorAsSymbol instead and use that
 *  as a key to look up their own localized error message. This function may be called outside of VR_Init()/VR_Shutdown(). */
fun vrGetVRInitErrorAsEnglishDescription(error: EVRInitError) = VR_GetVRInitErrorAsEnglishDescription(error.i)

internal external fun VR_GetVRInitErrorAsEnglishDescription(error: Int): String

/** Returns the interface of the specified version. This method must be called after VR_Init. The pointer returned is
 * valid until VR_Shutdown is called.   */
fun vrGetGenericInterface(pchInterfaceVersion: String, peError: EVRInitError_ByReference) =
        VR_GetGenericInterface(pchInterfaceVersion, peError)

internal external fun VR_GetGenericInterface(pchInterfaceVersion: String, peError: EVRInitError_ByReference): Pointer?

/** Returns whether the interface of the specified version exists.   */
fun vrIsInterfaceVersionValid(pchInterfaceVersion: String) = VR_IsInterfaceVersionValid(pchInterfaceVersion)

internal external fun VR_IsInterfaceVersionValid(pchInterfaceVersion: String): Boolean

/** Returns a token that represents whether the VR interface handles need to be reloaded */
fun vrGetInitToken() = VR_GetInitToken()

internal external fun VR_GetInitToken(): Int

val FnTable = "FnTable:"

object COpenVRContext {

    private var m_pVRSystem: IVRSystem? = null
    private var m_pVRChaperone: IVRChaperone? = null
    private var m_pVRChaperoneSetup: IVRChaperoneSetup? = null
    private var m_pVRCompositor: IVRCompositor? = null
    private var m_pVROverlay: IVROverlay? = null
    private var m_pVRResources: IVRResources? = null
    private var m_pVRRenderModels: IVRRenderModels? = null
    private var m_pVRExtendedDisplay: IVRExtendedDisplay? = null
    private var m_pVRSettings: IVRSettings? = null
    private var m_pVRApplications: IVRApplications? = null
    private var m_pVRTrackedCamera: IVRTrackedCamera? = null
    private var m_pVRScreenshots: IVRScreenshots? = null
    private var m_pVRDriverManager: IVRDriverManager? = null

    private val error = EVRInitError_ByReference(EVRInitError.None)

    private fun checkClear() {
        if (vrToken != VR_GetInitToken()) {
            clear()
            vrToken = VR_GetInitToken()
        }
    }

    fun vrSystem(): IVRSystem? {
        checkClear()
        if (m_pVRSystem == null)
            m_pVRSystem = vrGetGenericInterface(FnTable + IVRSystem_Version, error)?.let(::IVRSystem)
        return m_pVRSystem
    }

    fun vrChaperone(): IVRChaperone? {
        checkClear()
        if (m_pVRChaperone == null)
            m_pVRChaperone = vrGetGenericInterface(FnTable + IVRChaperone_Version, error)?.let(::IVRChaperone)
        return m_pVRChaperone
    }

    fun vrChaperoneSetup(): IVRChaperoneSetup? {
        checkClear()
        if (m_pVRChaperoneSetup == null)
            m_pVRChaperoneSetup = vrGetGenericInterface(FnTable + IVRChaperoneSetup_Version, error)?.let(::IVRChaperoneSetup)
        return m_pVRChaperoneSetup
    }

    fun vrCompositor(): IVRCompositor? {
        checkClear()
        if (m_pVRCompositor == null)
            m_pVRCompositor = vrGetGenericInterface(FnTable + IVRCompositor_Version, error)?.let(::IVRCompositor)
        return m_pVRCompositor
    }

    fun vrOverlay(): IVROverlay? {
        checkClear()
        if (m_pVROverlay == null)
            m_pVROverlay = vrGetGenericInterface(FnTable + IVROverlay_Version, error)?.let(::IVROverlay)
        return m_pVROverlay
    }

    fun vrResources(): IVRResources? {
        checkClear()
        if (m_pVRResources == null)
            m_pVRResources = vrGetGenericInterface(FnTable + IVRResources_Version, error)?.let(::IVRResources)
        return m_pVRResources
    }

    fun vrRenderModels(): IVRRenderModels? {
        checkClear()
        if (m_pVRRenderModels == null)
            m_pVRRenderModels = vrGetGenericInterface(FnTable + IVRRenderModels_Version, error)?.let(::IVRRenderModels)
        return m_pVRRenderModels
    }

    fun vrExtendedDisplay(): IVRExtendedDisplay? {
        checkClear()
        if (m_pVRExtendedDisplay == null)
            m_pVRExtendedDisplay = vrGetGenericInterface(FnTable + IVRExtendedDisplay_Version, error)?.let(::IVRExtendedDisplay)
        return m_pVRExtendedDisplay
    }

    fun vrSettings(): IVRSettings? {
        checkClear()
        if (m_pVRSettings == null)
            m_pVRSettings = vrGetGenericInterface(FnTable + IVRSettings_Version, error)?.let(::IVRSettings)
        return m_pVRSettings
    }

    fun vrApplications(): IVRApplications? {
        checkClear()
        if (m_pVRApplications == null)
            m_pVRApplications = vrGetGenericInterface(FnTable + IVRApplications_Version, error)?.let(::IVRApplications)
        return m_pVRApplications
    }

    fun vrTrackedCamera(): IVRTrackedCamera? {
        checkClear()
        if (m_pVRTrackedCamera == null)
            m_pVRTrackedCamera = vrGetGenericInterface(FnTable + IVRTrackedCamera_Version, error)?.let(::IVRTrackedCamera)
        return m_pVRTrackedCamera
    }

    fun vrScreenshots(): IVRScreenshots? {
        checkClear()
        if (m_pVRScreenshots == null)
            m_pVRScreenshots = vrGetGenericInterface(FnTable + IVRScreenshots_Version, error)?.let(::IVRScreenshots)
        return m_pVRScreenshots
    }

    fun vrDriverManager(): IVRDriverManager? {
        checkClear()
        if (m_pVRDriverManager == null)
            m_pVRDriverManager = vrGetGenericInterface(FnTable + IVRDriverManager_Version, error)?.let(::IVRDriverManager)
        return m_pVRDriverManager
    }

    fun clear() {
        m_pVRSystem = null
        m_pVRChaperone = null
        m_pVRChaperoneSetup = null
        m_pVRCompositor = null
        m_pVROverlay = null
        m_pVRResources = null
        m_pVRRenderModels = null
        m_pVRExtendedDisplay = null
        m_pVRSettings = null
        m_pVRApplications = null
        m_pVRTrackedCamera = null
        m_pVRScreenshots = null
        m_pVRDriverManager = null
    }
}

var vrToken = 0

val vrSystem get() = COpenVRContext.vrSystem()
val vrChaperone get() = COpenVRContext.vrChaperone()
val vrChaperoneSetup get() = COpenVRContext.vrChaperoneSetup()
val vrCompositor get() = COpenVRContext.vrCompositor()
val vrOverlay get() = COpenVRContext.vrOverlay()
val vrResources get() = COpenVRContext.vrResources()
val vrRenderModels get() = COpenVRContext.vrRenderModels()
val vrExtendedDisplay get() = COpenVRContext.vrExtendedDisplay()
val vrSettings get() = COpenVRContext.vrSettings()
val vrApplications get() = COpenVRContext.vrApplications()
val vrTrackedCamera get() = COpenVRContext.vrTrackedCamera()
val vrScreenshots get() = COpenVRContext.vrScreenshots()
val vrDriverManager get() = COpenVRContext.vrDriverManager()

internal external fun VR_InitInternal2(peError: EVRInitError_ByReference, eType: Int, pStartupInfo: String? = null): Int
internal external fun VR_ShutdownInternal()