package openvr.steamVR.input

import glm_.glm
import glm_.quat.Quat
import glm_.vec3.Vec3
import openvr.lib.*
import openvr.plugin2.SteamVR_Input_Sources
import openvr.plugin2.Transform
import openvr.steamVR.script.SteamVR_Utils.position
import openvr.steamVR.script.SteamVR_Utils.rotation
import openvr.unity.Time
import openvr.unity.angle
import org.lwjgl.openvr.InputPoseActionData
import kotlin.math.abs

//======= Copyright (c) Valve Corporation, All rights reserved. ===============

typealias SteamVR_Action_Pose_ActiveChangeHandler = (fromAction: SteamVR_Action_Pose, fromSource: SteamVR_Input_Sources, active: Boolean) -> Unit
typealias SteamVR_Action_Pose_ChangeHandler = (fromAction: SteamVR_Action_Pose, fromSource: SteamVR_Input_Sources) -> Unit
typealias SteamVR_Action_Pose_UpdateHandler = (fromAction: SteamVR_Action_Pose, fromSource: SteamVR_Input_Sources) -> Unit
typealias SteamVR_Action_Pose_TrackingChangeHandler = (fromAction: SteamVR_Action_Pose, fromSource: SteamVR_Input_Sources, trackingState: TrackingResult) -> Unit
typealias SteamVR_Action_Pose_ValidPoseChangeHandler = (fromAction: SteamVR_Action_Pose, fromSource: SteamVR_Input_Sources, validPose: Boolean) -> Unit
typealias SteamVR_Action_Pose_DeviceConnectedChangeHandler = (fromAction: SteamVR_Action_Pose, fromSource: SteamVR_Input_Sources, deviceConnected: Boolean) -> Unit

/** Pose actions represent a position, rotation, and velocities inside the tracked space.
 *  SteamVR keeps a log of past poses so you can retrieve old poses with GetPoseAtTimeOffset or GetVelocitiesAtTimeOffset.
 *  You can also pass in times in the future to these methods for SteamVR's best prediction of where the pose will be at that time. */
class SteamVR_Action_Pose : SteamVR_Action_Pose_Base<SteamVR_Action_Pose_Source_Map<SteamVR_Action_Pose_Source>,
        SteamVR_Action_Pose_Source>()/*, ISerializationCallbackReceiver*/ {


    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> Event fires when the active state (ActionSet active and binding active) changes</summary>
//    public event ActiveChangeHandler onActiveChange
//    { add { sourceMap[SteamVR_Input_Sources.Any].onActiveChange += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onActiveChange -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> Event fires when the active state of the binding changes</summary>
//    public event ActiveChangeHandler onActiveBindingChange
//    { add { sourceMap[SteamVR_Input_Sources.Any].onActiveBindingChange += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onActiveBindingChange -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> Event fires when the orientation of the pose changes more than the changeTolerance</summary>
//    public event ChangeHandler onChange
//    { add { sourceMap[SteamVR_Input_Sources.Any].onChange += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onChange -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> Event fires when the action is updated</summary>
//    public event UpdateHandler onUpdate
//    { add { sourceMap[SteamVR_Input_Sources.Any].onUpdate += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onUpdate -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> Event fires when the state of the tracking has changed</summary>
//    public event TrackingChangeHandler onTrackingChanged
//    { add { sourceMap[SteamVR_Input_Sources.Any].onTrackingChanged += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onTrackingChanged -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> Event fires when the validity of the pose has changed</summary>
//    public event ValidPoseChangeHandler onValidPoseChanged
//    { add { sourceMap[SteamVR_Input_Sources.Any].onValidPoseChanged += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onValidPoseChanged -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> Event fires when the device bound to this pose is connected or disconnected</summary>
//    public event DeviceConnectedChangeHandler onDeviceConnectedChanged
//    { add { sourceMap[SteamVR_Input_Sources.Any].onDeviceConnectedChanged += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onDeviceConnectedChanged -= value; } }

    /** Fires an event when a device is connected or disconnected.
     *  @param inputSource: The device you would like to add an event to. Any if the action is not device specific.
     *  @param functionToCall: The method you would like to be called when a device is connected. Should take a SteamVR_Action_Pose as a param */
    fun addOnDeviceConnectedChanged(inputSource: SteamVR_Input_Sources, functionToCall: SteamVR_Action_Pose_DeviceConnectedChangeHandler) {
        sourceMap!![inputSource]!!.onDeviceConnectedChanged += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param inputSource: The device you would like to remove an event from. Any if the action is not device specific.
     *  @param functionToStopCalling: The method you would like to stop calling when a device is connected. Should take a SteamVR_Action_Pose as a param */
    fun removeOnDeviceConnectedChanged(inputSource: SteamVR_Input_Sources, functionToStopCalling: SteamVR_Action_Pose_DeviceConnectedChangeHandler) {
        sourceMap!![inputSource]!!.onDeviceConnectedChanged -= functionToStopCalling
    }


    /** Fires an event when the tracking of the device has changed
     *  @param inputSource: The device you would like to add an event to. Any if the action is not device specific.
     *  @param functionToCall: The method you would like to be called when tracking has changed. Should take a SteamVR_Action_Pose as a param */
    fun addOnTrackingChanged(inputSource: SteamVR_Input_Sources, functionToCall: SteamVR_Action_Pose_TrackingChangeHandler) {
        sourceMap!![inputSource]!!.onTrackingChanged += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param inputSource: The device you would like to remove an event from. Any if the action is not device specific.
     *  @param functionToStopCalling: The method you would like to stop calling when tracking has changed. Should take a SteamVR_Action_Pose as a param */
    fun removeOnTrackingChanged(inputSource: SteamVR_Input_Sources, functionToStopCalling: SteamVR_Action_Pose_TrackingChangeHandler) {
        sourceMap!![inputSource]!!.onTrackingChanged -= functionToStopCalling
    }


    /** Fires an event when the device now has a valid pose or no longer has a valid pose
     *  @param inputSource: The device you would like to add an event to. Any if the action is not device specific.
     *  @param functionToCall: The method you would like to be called when the pose has become valid or invalid. Should take a SteamVR_Action_Pose as a param */
    fun addOnValidPoseChanged(inputSource: SteamVR_Input_Sources, functionToCall: SteamVR_Action_Pose_ValidPoseChangeHandler) {
        sourceMap!![inputSource]!!.onValidPoseChanged += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param inputSource: The device you would like to remove an event from. Any if the action is not device specific.
     *  @param functionToStopCalling: The method you would like to stop calling when the pose has become valid or invalid. Should take a SteamVR_Action_Pose as a param */
    fun removeOnValidPoseChanged(inputSource: SteamVR_Input_Sources, functionToStopCalling: SteamVR_Action_Pose_ValidPoseChangeHandler) {
        sourceMap!![inputSource]!!.onValidPoseChanged -= functionToStopCalling
    }


    /** Executes a function when this action's bound state changes
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnActiveChangeListener(inputSource: SteamVR_Input_Sources, functionToCall: SteamVR_Action_Pose_ActiveChangeHandler) {
        sourceMap!![inputSource]!!.onActiveChange += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive update events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnActiveChangeListener(inputSource: SteamVR_Input_Sources, functionToStopCalling: SteamVR_Action_Pose_ActiveChangeHandler) {
        sourceMap!![inputSource]!!.onActiveChange -= functionToStopCalling
    }

    /** Executes a function when the state of this action (with the specified inputSource) changes
     *  @param functionToCall: A local function that receives the boolean action who's state has changed, the corresponding input source, and the new value
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnChangeListener(inputSource: SteamVR_Input_Sources, functionToCall: SteamVR_Action_Pose_ChangeHandler) {
        sourceMap!![inputSource]!!.onChange += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive on change events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnChangeListener(inputSource: SteamVR_Input_Sources, functionToStopCalling: SteamVR_Action_Pose_ChangeHandler) {
        sourceMap!![inputSource]!!.onChange -= functionToStopCalling
    }

    /** Executes a function when the state of this action (with the specified inputSource) is updated.
     *  @param functionToCall: A local function that receives the boolean action who's state has changed, the corresponding input source, and the new value
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnUpdateListener(inputSource: SteamVR_Input_Sources, functionToCall: SteamVR_Action_Pose_UpdateHandler) {
        sourceMap!![inputSource]!!.onUpdate += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive update events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnUpdateListener(inputSource: SteamVR_Input_Sources, functionToStopCalling: SteamVR_Action_Pose_UpdateHandler) {
        sourceMap!![inputSource]!!.onUpdate -= functionToStopCalling
    }

//    void ISerializationCallbackReceiver.OnBeforeSerialize()
//    {}
//
//    void ISerializationCallbackReceiver.OnAfterDeserialize()
//    {
//        InitAfterDeserialize()
//    }

    companion object {

        /** Sets all pose and skeleton actions to use the specified universe origin. */
        fun setTrackingUniverseOrigin(newOrigin: TrackingUniverseOrigin) {
            setUniverseOrigin(newOrigin)
            vrCompositor.trackingSpace = newOrigin
        }
    }
}

/** The base pose action (pose and skeleton inherit from this) */
abstract class SteamVR_Action_Pose_Base<SourceMap, SourceElement> : SteamVR_Action_In<SourceMap, SourceElement>(),
        ISteamVR_Action_Pose where SourceMap : SteamVR_Action_Pose_Source_Map<SourceElement>,
                                   SourceElement : SteamVR_Action_Pose_Source {

    companion object {
        @JvmStatic
        /**  Sets all pose (and skeleton) actions to use the specified universe origin. */
        protected fun setUniverseOrigin(newOrigin: TrackingUniverseOrigin) {
            SteamVR_Input.actionsPose.forEach { it.sourceMap!!.setTrackingUniverseOrigin(newOrigin) }
            SteamVR_Input.actionsSkeleton.forEach { it.sourceMap!!.setTrackingUniverseOrigin(newOrigin) }
        }
    }

    /** [Shortcut to: SteamVR_Input_Sources.Any] The local position of this action relative to the universe origin */
    override val localPosition: Vec3
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.localPosition

    /** [Shortcut to: SteamVR_Input_Sources.Any] The local rotation of this action relative to the universe origin */
    override val localRotation: Quat
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.localRotation

    /** [Shortcut to: SteamVR_Input_Sources.Any] The state of the tracking system that is used to create pose data (position, rotation, etc) */
    override val trackingState: TrackingResult
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.trackingState

    /** [Shortcut to: SteamVR_Input_Sources.Any] The local velocity of this pose relative to the universe origin */
    override val velocity: Vec3
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.velocity

    /** [Shortcut to: SteamVR_Input_Sources.Any] The local angular velocity of this pose relative to the universe origin */
    override val angularVelocity: Vec3
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.angularVelocity

    /** [Shortcut to: SteamVR_Input_Sources.Any] True if the pose retrieved for this action and input source is valid (good data from the tracking source) */
    override val poseIsValid: Boolean
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.poseIsValid

    /** [Shortcut to: SteamVR_Input_Sources.Any] True if the device bound to this action and input source is connected */
    override val deviceIsConnected: Boolean
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.deviceIsConnected

    /** [Shortcut to: SteamVR_Input_Sources.Any] The local position for this pose during the previous update */
    override val lastLocalPosition: Vec3
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastLocalPosition

    /** [Shortcut to: SteamVR_Input_Sources.Any] The local rotation for this pose during the previous update */
    override val lastLocalRotation: Quat
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastLocalRotation

    /** [Shortcut to: SteamVR_Input_Sources.Any] The tracking state for this pose during the previous update */
    override val lastTrackingState: TrackingResult
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastTrackingState

    /** [Shortcut to: SteamVR_Input_Sources.Any] The velocity for this pose during the previous update */
    override val lastVelocity: Vec3
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastVelocity

    /** [Shortcut to: SteamVR_Input_Sources.Any] The angular velocity for this pose during the previous update */
    override val lastAngularVelocity: Vec3
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastAngularVelocity

    /** [Shortcut to: SteamVR_Input_Sources.Any] True if the pose was valid during the previous update */
    override val lastPoseIsValid: Boolean
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastPoseIsValid

    /** [Shortcut to: SteamVR_Input_Sources.Any] True if the device bound to this action was connected during the previous update */
    override val lastDeviceIsConnected: Boolean
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastDeviceIsConnected


    /** [Should not be called by user code]
     *  Updates the data for all the input sources the system has detected need to be updated. */
    fun updateValues(skipStateAndEventUpdates: Boolean) = sourceMap!!.updateValues(skipStateAndEventUpdates)

    /** SteamVR keeps a log of past poses so you can retrieve old poses or estimated poses in the future by passing in a secondsFromNow value that is negative or positive.
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific.
     *  @param secondsFromNow: The time offset in the future (estimated) or in the past (previously recorded) you want to get data from
     *  @returns true if the call succeeded */
    fun getVelocitiesAtTimeOffset(inputSource: SteamVR_Input_Sources, secondsFromNow: Float, velocity: Vec3,
                                  angularVelocity: Vec3): Boolean =
            sourceMap!![inputSource]!!.getVelocitiesAtTimeOffset(secondsFromNow, velocity, angularVelocity)

    /** SteamVR keeps a log of past poses so you can retrieve old poses or estimated poses in the future by passing in a secondsFromNow value that is negative or positive.
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific.
     *  @param secondsFromNow: The time offset in the future (estimated) or in the past (previously recorded) you want to get data from
     *  @returns true if the call succeeded */
    fun getPoseAtTimeOffset(inputSource: SteamVR_Input_Sources, secondsFromNow: Float, localPosition: Vec3,
                            localRotation: Quat, velocity: Vec3, angularVelocity: Vec3): Boolean =
            sourceMap!![inputSource]!!.getPoseAtTimeOffset(secondsFromNow, localPosition, localRotation, velocity, angularVelocity)

    /** Update a transform's local position and local roation to match the pose from the most recent update
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific.
     *  @param transformToUpdate: The transform of the object to be updated */
    fun updateTransform(inputSource: SteamVR_Input_Sources, transformToUpdate: Transform) =
            sourceMap!![inputSource]!!.updateTransform(transformToUpdate)

    /** The local position of this action relative to the universe origin
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getLocalPosition(inputSource: SteamVR_Input_Sources): Vec3 =
            sourceMap!![inputSource]!!.localPosition

    /** The local rotation of this action relative to the universe origin
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getLocalRotation(inputSource: SteamVR_Input_Sources): Quat =
            sourceMap!![inputSource]!!.localRotation

    /** The local velocity of this pose relative to the universe origin
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getVelocity(inputSource: SteamVR_Input_Sources): Vec3 =
            sourceMap!![inputSource]!!.velocity

    /** The local angular velocity of this pose relative to the universe origin
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getAngularVelocity(inputSource: SteamVR_Input_Sources): Vec3 =
            sourceMap!![inputSource]!!.angularVelocity

    /** True if the device bound to this action and input source is connected
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getDeviceIsConnected(inputSource: SteamVR_Input_Sources): Boolean =
            sourceMap!![inputSource]!!.deviceIsConnected

    /** True if the pose retrieved for this action and input source is valid (good data from the tracking source)
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getPoseIsValid(inputSource: SteamVR_Input_Sources): Boolean =
            sourceMap!![inputSource]!!.poseIsValid

    /** The state of the tracking system that is used to create pose data (position, rotation, etc)
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getTrackingResult(inputSource: SteamVR_Input_Sources): TrackingResult =
            sourceMap!![inputSource]!!.trackingState


    /** The local position for this pose during the previous update
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getLastLocalPosition(inputSource: SteamVR_Input_Sources): Vec3 =
            sourceMap!![inputSource]!!.lastLocalPosition

    /** The local rotation for this pose during the previous update
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getLastLocalRotation(inputSource: SteamVR_Input_Sources): Quat =
            sourceMap!![inputSource]!!.lastLocalRotation

    /** The velocity for this pose during the previous update
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getLastVelocity(inputSource: SteamVR_Input_Sources): Vec3 =
            sourceMap!![inputSource]!!.lastVelocity

    /** The angular velocity for this pose during the previous update
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getLastAngularVelocity(inputSource: SteamVR_Input_Sources): Vec3 =
            sourceMap!![inputSource]!!.lastAngularVelocity

    /** True if the device bound to this action was connected during the previous update
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getLastDeviceIsConnected(inputSource: SteamVR_Input_Sources): Boolean =
            sourceMap!![inputSource]!!.lastDeviceIsConnected

    /** True if the pose was valid during the previous update
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getLastPoseIsValid(inputSource: SteamVR_Input_Sources): Boolean =
            sourceMap!![inputSource]!!.lastPoseIsValid

    /** The tracking state for this pose during the previous update
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getLastTrackingResult(inputSource: SteamVR_Input_Sources): TrackingResult =
            sourceMap!![inputSource]!!.lastTrackingState
}

/** Boolean actions are either true or false. There is an onStateUp and onStateDown event for the rising and falling edge. */
open class SteamVR_Action_Pose_Source_Map<Source> : SteamVR_Action_In_Source_Map<Source>()
        where Source : SteamVR_Action_Pose_Source {

    /** Sets all pose (and skeleton) actions to use the specified universe origin without going through the sourcemap indexer */
    fun setTrackingUniverseOrigin(newOrigin: TrackingUniverseOrigin) =
            sources.values.forEach { it.universeOrigin = newOrigin }

    fun updateValues(skipStateAndEventUpdates: Boolean) =
            updatingSources.forEach { sources[it]!!.updateValue(skipStateAndEventUpdates) }
}

open class SteamVR_Action_Pose_Source : SteamVR_Action_In_Source(), ISteamVR_Action_Pose {

    var universeOrigin = TrackingUniverseOrigin.RawAndUncalibrated

    /** The distance the pose needs to move/rotate before a change is detected */
    var changeTolerance = Float.MIN_VALUE

    /** Event fires when the active state (ActionSet active and binding active) changes */
    val onActiveChange = ArrayList<SteamVR_Action_Pose_ActiveChangeHandler>()

    /** Event fires when the active state of the binding changes */
    val onActiveBindingChange = ArrayList<SteamVR_Action_Pose_ActiveChangeHandler>()

    /** Event fires when the orientation of the pose changes more than the changeTolerance */
    val onChange = ArrayList<SteamVR_Action_Pose_ChangeHandler>()

    /** Event fires when the action is updated */
    val onUpdate = ArrayList<SteamVR_Action_Pose_UpdateHandler>()

    /** Event fires when the state of the tracking system that is used to create pose data (position, rotation, etc) changes */
    val onTrackingChanged = ArrayList<SteamVR_Action_Pose_TrackingChangeHandler>()

    /** Event fires when the state of the pose data retrieved for this action changes validity (good/bad data from the tracking source) */
    val onValidPoseChanged = ArrayList<SteamVR_Action_Pose_ValidPoseChangeHandler>()

    /** Event fires when the device bound to this action is connected or disconnected */
    val onDeviceConnectedChanged = ArrayList<SteamVR_Action_Pose_DeviceConnectedChangeHandler>()


    /** True when the orientation of the pose has changhd more than changeTolerance in the last update. Note: Will only return true if the action is also active. */
    override var changed: Boolean = false

    /** The value of the action's 'changed' during the previous update */
    override var lastChanged: Boolean = false

    /** The handle to the origin of the component that was used to update this pose */
    override val activeOrigin: VRInputValueHandle
        get() = if (active) poseActionData.activeOrigin else 0

    /** The handle to the origin of the component that was used to update the value for this action (for the previous update) */
    override val lastActiveOrigin: VRInputValueHandle
        get() = lastPoseActionData.activeOrigin

    /** True if this action is bound and the ActionSet is active */
    override val active: Boolean
        get() = activeBinding && action.actionSet!!.isActive(inputSource)

    /** True if the action is bound */
    override val activeBinding: Boolean
        get() = poseActionData.active


    /** If the action was active (ActionSet active and binding active) during the last update */
    override var lastActive: Boolean = false

    /** If the action's binding was active during the previous update */
    override val lastActiveBinding: Boolean
        get() = lastPoseActionData.active

    /** The state of the tracking system that is used to create pose data (position, rotation, etc) */
    override val trackingState: TrackingResult
        get() = poseActionData.pose.trackingResult

    /** The tracking state for this pose during the previous update */
    override val lastTrackingState: TrackingResult
        get() = lastPoseActionData.pose.trackingResult

    /** True if the pose retrieved for this action and input source is valid (good data from the tracking source) */
    override val poseIsValid: Boolean
        get() = poseActionData.pose.poseIsValid

    /** True if the pose was valid during the previous update */
    override val lastPoseIsValid: Boolean
        get() = lastPoseActionData.pose.poseIsValid

    /** True if the device bound to this action and input source is connected */
    override val deviceIsConnected: Boolean
        get() = poseActionData.pose.deviceIsConnected

    /** True if the device bound to this action was connected during the previous update */
    override val lastDeviceIsConnected: Boolean
        get() = lastPoseActionData.pose.deviceIsConnected


    /** The local position of this action relative to the universe origin */
    override val localPosition = Vec3()

    /** The local rotation of this action relative to the universe origin */
    override val localRotation = Quat()

    /** The local position for this pose during the previous update */
    override val lastLocalPosition = Vec3()

    /** The local rotation for this pose during the previous update */
    override val lastLocalRotation = Quat()

    /** The local velocity of this pose relative to the universe origin */
    override val velocity = Vec3()

    /** The velocity for this pose during the previous update */
    override val lastVelocity = Vec3()

    /** The local angular velocity of this pose relative to the universe origin */
    override val angularVelocity = Vec3()

    /** The angular velocity for this pose during the previous update */
    override val lastAngularVelocity = Vec3()


    protected val poseActionData = InputPoseActionData.calloc()

    protected var lastPoseActionData = InputPoseActionData.calloc()

    protected val tempPoseActionData = InputPoseActionData.calloc()


    protected lateinit var poseAction: SteamVR_Action_Pose

    /** [Should not be called by user code] Sets up the internals of the action source before SteamVR has been initialized. */
    override fun preinitialize(wrappingAction: SteamVR_Action, forInputSource: SteamVR_Input_Sources) {
        super.preinitialize(wrappingAction, forInputSource)
        poseAction = wrappingAction as SteamVR_Action_Pose
    }

    /** [Should not be called by user code]
     *  Updates the data for this action and this input source. Sends related events. */
    override fun updateValue() = updateValue(false)

    /** [Should not be called by user code]
     *  Updates the data for this action and this input source. Sends related events. */
    open fun updateValue(skipStateAndEventUpdates: Boolean) {
        lastChanged = changed
        lastPoseActionData.free()
        lastPoseActionData = poseActionData
        lastLocalPosition put localPosition
        lastLocalRotation put localRotation
        lastVelocity put velocity
        lastAngularVelocity put angularVelocity

        val err = vrInput.getPoseActionDataForNextFrame(handle, universeOrigin, poseActionData, inputSourceHandle)
        if (err != vrInput.Error.None)
            System.err.println("[SteamVR] GetPoseActionData error ($fullPath): $err Handle: $handle. SteamVR_Input source: $inputSource")

        setCacheVariables()
        changed = isChanged()

        if (changed)
            changedTime = updateTime

        if (!skipStateAndEventUpdates)
            checkAndSendEvents()
    }

    protected fun setCacheVariables() {
        localPosition put poseActionData.pose.mDeviceToAbsoluteTracking().position
        localRotation put poseActionData.pose.mDeviceToAbsoluteTracking().rotation
        velocity put getUnityCoordinateVelocity(poseActionData.pose.velocity)
        angularVelocity put getUnityCoordinateAngularVelocity(poseActionData.pose.angularVelocity)
        updateTime = Time.realtimeSinceStartup
    }

    /** JVM ~getChanged to avoid clash with ::changed */
    protected fun isChanged(): Boolean = when {
        glm.distance(localPosition, lastLocalPosition) > changeTolerance -> true
        abs(angle(localRotation, lastLocalRotation)) > changeTolerance -> true
        glm.distance(velocity, lastVelocity) > changeTolerance -> true
        glm.distance(angularVelocity, lastAngularVelocity) > changeTolerance -> true
        else -> false
    }

    /** SteamVR keeps a log of past poses so you can retrieve old poses or estimated poses in the future by passing in a secondsFromNow value that is negative or positive.
     *
     *  @param secondsFromNow: The time offset in the future (estimated) or in the past (previously recorded) you want to get data from
     *  @returns true if we successfully returned a pose */
    fun getVelocitiesAtTimeOffset(secondsFromNow: Float, velocityAtTime: Vec3, angularVelocityAtTime: Vec3): Boolean {
        val err = vrInput.getPoseActionDataRelativeToNow(handle, universeOrigin, secondsFromNow, tempPoseActionData, inputSourceHandle)
        if (err != vrInput.Error.None) {
            System.err.println("[SteamVR] GetPoseActionData error ($fullPath): $err handle: $handle") //todo: this should be an error

            velocityAtTime put 0f
            angularVelocityAtTime put 0f
            return false
        }

        velocityAtTime put getUnityCoordinateVelocity(tempPoseActionData.pose.velocity)
        angularVelocityAtTime put getUnityCoordinateAngularVelocity(tempPoseActionData.pose.angularVelocity)

        return true
    }

    /** SteamVR keeps a log of past poses so you can retrieve old poses or estimated poses in the future by passing in a secondsFromNow value that is negative or positive.
     *
     *  @param secondsFromNow: The time offset in the future (estimated) or in the past (previously recorded) you want to get data from
     *  @returns true if we successfully returned a pose */
    fun getPoseAtTimeOffset(secondsFromNow: Float, positionAtTime: Vec3, rotationAtTime: Quat, velocityAtTime: Vec3, angularVelocityAtTime: Vec3): Boolean {
        val err = vrInput.getPoseActionDataRelativeToNow(handle, universeOrigin, secondsFromNow, tempPoseActionData, inputSourceHandle)
        if (err != vrInput.Error.None) {
            System.err.println("[SteamVR] GetPoseActionData error ($fullPath): $err handle: $handle") //todo: this should be an error

            velocityAtTime put 0f
            angularVelocityAtTime put 0f
            positionAtTime put 0f
            rotationAtTime.put(1f, 0f, 0f, 0f)
            return false
        }

        velocityAtTime put getUnityCoordinateVelocity(tempPoseActionData.pose.velocity)
        angularVelocityAtTime put getUnityCoordinateAngularVelocity(tempPoseActionData.pose.angularVelocity)
        positionAtTime put tempPoseActionData.pose.mDeviceToAbsoluteTracking().position
        rotationAtTime put tempPoseActionData.pose.mDeviceToAbsoluteTracking().rotation

        return true
    }

    /** Update a transform's local position and local roation to match the pose.
     *
     *  @param transformToUpdate: The transform of the object to be updated */
    fun updateTransform(transformToUpdate: Transform) {
        transformToUpdate.localPosition put localPosition
        transformToUpdate.localRotation put localRotation
    }

    protected open fun checkAndSendEvents() {
        if (trackingState != lastTrackingState)
            onTrackingChanged.forEach { it(poseAction, inputSource, trackingState) }

        if (poseIsValid != lastPoseIsValid)
            onValidPoseChanged.forEach { it(poseAction, inputSource, poseIsValid) }

        if (deviceIsConnected != lastDeviceIsConnected)
            onDeviceConnectedChanged.forEach { it(poseAction, inputSource, deviceIsConnected) }

        if (changed)
            onChange.forEach { it(poseAction, inputSource) }

        if (active != lastActive)
            onActiveChange.forEach { it(poseAction, inputSource, active) }

        if (activeBinding != lastActiveBinding)
            onActiveBindingChange.forEach { it(poseAction, inputSource, activeBinding) }

        onUpdate.forEach { it(poseAction, inputSource) }
    }

    protected fun getUnityCoordinateVelocity(vector: Vec3) = getUnityCoordinateVelocity(vector.x, vector.y, vector.z)

    protected fun getUnityCoordinateAngularVelocity(vector: Vec3) = getUnityCoordinateAngularVelocity(vector.x, vector.y, vector.z)

    protected fun getUnityCoordinateVelocity(x: Float, y: Float, z: Float) = Vec3(x, y, -z)

    protected fun getUnityCoordinateAngularVelocity(x: Float, y: Float, z: Float) = Vec3(-x, -y, z)
}

/** Boolean actions are either true or false. There is an onStateUp and onStateDown event for the rising and falling edge. */
interface ISteamVR_Action_Pose : ISteamVR_Action_In_Source {
    /** The local position of this action relative to the universe origin */
    val localPosition: Vec3

    /** The local rotation of this action relative to the universe origin */
    val localRotation: Quat

    /** The state of the tracking system that is used to create pose data (position, rotation, etc) */
    val trackingState: TrackingResult

    /** The local velocity of this pose relative to the universe origin */
    val velocity: Vec3

    /** The local angular velocity of this pose relative to the universe origin */
    val angularVelocity: Vec3

    /** True if the pose retrieved for this action and input source is valid (good data from the tracking source) */
    val poseIsValid: Boolean

    /** True if the device bound to this action and input source is connected */
    val deviceIsConnected: Boolean


    /** The local position for this pose during the previous update */
    val lastLocalPosition: Vec3

    /** The local rotation for this pose during the previous update */
    val lastLocalRotation: Quat

    /** The tracking state for this pose during the previous update */
    val lastTrackingState: TrackingResult

    /** The velocity for this pose during the previous update */
    val lastVelocity: Vec3

    /** The angular velocity for this pose during the previous update */
    val lastAngularVelocity: Vec3

    /** True if the pose was valid during the previous update */
    val lastPoseIsValid: Boolean

    /** True if the device bound to this action was connected during the previous update */
    val lastDeviceIsConnected: Boolean
}