//package openvr.lib
//
//import com.sun.jna.Callback
//import com.sun.jna.Pointer
//import com.sun.jna.Structure
//import glm_.b
//import glm_.bool
//
//open class DriverPoseQuaternion : Structure {
//
//    @JvmField
//    var w = 0.0
//    @JvmField
//    var x = 0.0
//    @JvmField
//    var y = 0.0
//    @JvmField
//    var z = 0.0
//
//    constructor()
//
//    override fun getFieldOrder() = listOf("w", "x", "y", "z")
//
//    constructor(w: Double, x: Double, y: Double, z: Double) {
//        this.w = w
//        this.x = x
//        this.y = y
//        this.z = z
//    }
//
//    constructor(peer: Pointer) : super(peer) {
//        read()
//    }
//
//    class ByReference : VREvent_Process(), Structure.ByReference
//    class ByValue : VREvent_Process(), Structure.ByValue
//}
//
//open class DriverPose : Structure {
//
//    /** Time offset of this pose, in seconds from the actual time of the pose, relative to the time of the PoseUpdated()
//     *  call made by the driver. 	 */
//    @JvmField
//    var poseTimeOffset = 0.0
//    /** Generally, the pose maintained by a driver is in an inertial coordinate system different from the world system
//     *  of x+ right, y+ up, z+ back.
//     *  Also, the driver is not usually tracking the "head" position, but instead an internal IMU or another reference
//     *  point in the HMD.
//     *  The following two transforms transform positions and orientations to app world space from driver world space,
//     *  and to HMD head space from driver local body space.
//     *
//     *  We maintain the driver pose state in its internal coordinate system, so we can do the pose prediction math
//     *  without having to use angular acceleration.  A driver's angular acceleration is generally not measured,
//     *  and is instead calculated from successive samples of angular velocity.
//     *  This leads to a noisy angular acceleration values, which are also lagged due to the filtering required to reduce
//     *  noise to an acceptable level. 	 */
//    @JvmField
//    var worldFromDriverRotation = HmdQuat()
//
//    @JvmField
//    var vecWorldFromDriverTranslation = DoubleArray(3)
//
//    @JvmField
//    var driverFromHeadRotation = HmdQuat()
//
//    @JvmField
//    var vecDriverFromHeadTranslation = DoubleArray(3)
//
//    /** State of driver pose, in meters and radians.
//     *  Position of the driver tracking reference in driver world space
//     *  +[0] (x) is right
//     *  +[1] (y) is up
//     *  -[2] (z) is forward */
//    @JvmField
//    var vecPosition = DoubleArray(3)
//
//    /** Velocity of the pose in meters/second */
//    @JvmField
//    var vecVelocity = DoubleArray(3)
//
//    /** Acceleration of the pose in meters/second */
//    @JvmField
//    var vecAcceleration = DoubleArray(3)
//
//    /** Orientation of the tracker, represented as a quaternion */
//    @JvmField
//    var rotation = HmdQuat()
//
//    /** Angular velocity of the pose in axis-angle representation. The direction is the angle of rotation and
//     *  the magnitude is the angle around that axis in radians/second. */
//    @JvmField
//    var vecAngularVelocity = DoubleArray(3)
//
//    /** Angular acceleration of the pose in axis-angle representation. The direction is the angle of rotation and
//     *  the magnitude is the angle around that axis in radians/second^2. */
//    @JvmField
//    var vecAngularAcceleration = DoubleArray(3)
//
//    @JvmField
//    var _result = 0
//    var result: ETrackingResult
//        get() = ETrackingResult of _result
//        set(value) {
//            _result = value.i
//        }
//
//    @JvmField
//    var _poseIsValid: Byte = 0
//    var poseIsValid: Boolean
//        get() = _poseIsValid.bool
//        set(value) {
//            _poseIsValid = value.b
//        }
//
//    @JvmField
//    var _willDriftInYaw: Byte = 0
//    var willDriftInYaw: Boolean
//        get() = _willDriftInYaw.bool
//        set(value) {
//            _willDriftInYaw = value.b
//        }
//
//    @JvmField
//    var _shouldApplyHeadModel: Byte = 0
//    var shouldApplyHeadModel: Boolean
//        get() = _shouldApplyHeadModel.bool
//        set(value) {
//            _shouldApplyHeadModel = value.b
//        }
//
//    @JvmField
//    var _deviceIsConnected: Byte = 0
//    var deviceIsConnected: Boolean
//        get() = _deviceIsConnected.bool
//        set(value) {
//            _deviceIsConnected = value.b
//        }
//
//    constructor()
//
//    override fun getFieldOrder() = listOf("poseTimeOffset", "worldFromDriverRotation", "vecWorldFromDriverTranslation",
//            "driverFromHeadRotation", "vecDriverFromHeadTranslation", "vecPosition", "vecVelocity", "vecAcceleration", "rotation",
//            "vecAngularVelocity", "vecAngularAcceleration", "_result", "_poseIsValid", "_willDriftInYaw", "_shouldApplyHeadModel",
//            "_deviceIsConnected")
//
//    constructor(poseTimeOffset: Double, worldFromDriverRotation: HmdQuat, vecWorldFromDriverTranslation: DoubleArray,
//                driverFromHeadRotation: HmdQuat, vecDriverFromHeadTranslation: DoubleArray, vecPosition: DoubleArray,
//                vecVelocity: DoubleArray, vecAcceleration: DoubleArray, rotation: HmdQuat, vecAngularVelocity: DoubleArray,
//                vecAngularAcceleration: DoubleArray, result: ETrackingResult, poseIsValid: Boolean, willDriftInYaw: Boolean,
//                shouldApplyHeadModel: Boolean, deviceIsConnected: Boolean) :
//            this(poseTimeOffset, worldFromDriverRotation, vecWorldFromDriverTranslation, driverFromHeadRotation,
//                    vecDriverFromHeadTranslation, vecPosition, vecVelocity, vecAcceleration, rotation, vecAngularVelocity,
//                    vecAngularAcceleration, result.i, poseIsValid.b, willDriftInYaw.b, shouldApplyHeadModel.b, deviceIsConnected.b)
//
//    constructor(poseTimeOffset: Double, worldFromDriverRotation: HmdQuat, vecWorldFromDriverTranslation: DoubleArray,
//                driverFromHeadRotation: HmdQuat, vecDriverFromHeadTranslation: DoubleArray, vecPosition: DoubleArray,
//                vecVelocity: DoubleArray, vecAcceleration: DoubleArray, rotation: HmdQuat, vecAngularVelocity: DoubleArray,
//                vecAngularAcceleration: DoubleArray, result: Int, poseIsValid: Byte, willDriftInYaw: Byte,
//                shouldApplyHeadModel: Byte, deviceIsConnected: Byte) {
//        this.poseTimeOffset = poseTimeOffset
//        this.worldFromDriverRotation = worldFromDriverRotation
//        this.vecWorldFromDriverTranslation = vecWorldFromDriverTranslation
//        this.driverFromHeadRotation = driverFromHeadRotation
//        this.vecDriverFromHeadTranslation = vecDriverFromHeadTranslation
//        this.vecPosition = vecPosition
//        this.vecVelocity = vecVelocity
//        this.vecAcceleration = vecAcceleration
//        this.rotation = rotation
//        this.vecAngularVelocity = vecAngularVelocity
//        this.vecAngularAcceleration = vecAngularAcceleration
//        this._result = result
//        this._poseIsValid = poseIsValid
//        this._willDriftInYaw = willDriftInYaw
//        this._shouldApplyHeadModel = shouldApplyHeadModel
//        this._deviceIsConnected = deviceIsConnected
//    }
//
//    constructor(peer: Pointer) : super(peer) {
//        read()
//    }
//
//    class ByReference : VREvent_Process(), Structure.ByReference
//    class ByValue : VREvent_Process(), Structure.ByValue
//}
//
///** Purpose: Represents a single tracked device in a driver */
//open class ITrackedDeviceServerDriver : Structure {
//
//// ------------------------------------
//// Management Methods
//// ------------------------------------
//
//    /** This is called before an HMD is returned to the application. It will always be called before any display or
//     *  tracking methods. Memory and processor use by the ITrackedDeviceServerDriver object should be kept to a minimum
//     *  until it is activated.
//     *  The pose listener is guaranteed to be valid until Deactivate is called, but should not be used after that point. */
//    fun activate(objectId: Int) = EVRInitError of Activate!!(objectId)
//
//    @JvmField
//    var Activate: Activate_callback? = null
//
//    interface Activate_callback : Callback {
//        operator fun invoke(unObjectId: Int): Int
//    }
//
//    /** This is called when The VR system is switching from this Hmd being the active display to another Hmd being
//     *  the active display. The driver should clean whatever memory and thread use it can when it is deactivated */
//    fun deactivate() = Deactivate!!()
//
//    @JvmField
//    var Deactivate: Deactivate_callback? = null
//
//    interface Deactivate_callback : Callback {
//        operator fun invoke()
//    }
//
//
//    /** Handles a request from the system to put this device into standby mode. What that means is defined per-device. */
//    fun enterStandby() = EnterStandby!!()
//
//    @JvmField
//    var EnterStandby: EnterStandby_callback? = null
//
//    interface EnterStandby_callback : Callback {
//        operator fun invoke()
//    }
//
//
//    /** Requests a component interface of the driver for device-specific functionality. The driver should return NULL
//     *  if the requested interface or version is not supported. */
//    fun getComponent(componentNameAndVersion: String) = GetComponent!!(componentNameAndVersion)
//
//    @JvmField
//    var GetComponent: GetComponent_callback? = null
//
//    interface GetComponent_callback : Callback {
//        operator fun invoke(pchComponentNameAndVersion: String): Any?
//    }
//
//
//    /** A VR Client has made this debug request of the driver. The set of valid requests is entirely up to the driver
//     *  and the client to figure out, as is the format of the response. Responses that exceed the length
//     *  of the supplied buffer should be truncated and null terminated  */
//    fun debugRequest(componentNameAndVersion: String): String {
//        val bytes = ByteArray(32)
//        DebugRequest!!(componentNameAndVersion, bytes, bytes.size)
//        return String(bytes)
//    }
//
//    @JvmField
//    var DebugRequest: DebugRequest_callback? = null
//
//    interface DebugRequest_callback : Callback {
//        operator fun invoke(pchRequest: String, pchResponseBuffer: ByteArray, unResponseBufferSize: Int): Any?
//    }
//
//// ------------------------------------
//// Tracking Methods
//// ------------------------------------
//
//    fun getPose() = GetPose!!()
//
//    @JvmField
//    var GetPose: GetPose_callback? = null
//
//    interface GetPose_callback : Callback {
//        operator fun invoke(): DriverPose
//    }
//
//
//    constructor()
//
//    override fun getFieldOrder() = listOf("GetDriverCount", "GetDriverName", "GetDriverHandle")
//
//    constructor(peer: Pointer) : super(peer) {
//        read()
//    }
//
//    class ByReference : IVRDriverManager(), Structure.ByReference
//    class ByValue : IVRDriverManager(), Structure.ByValue
//}
//
//val ITrackedDeviceServerDriver_Version  = "ITrackedDeviceServerDriver_005"