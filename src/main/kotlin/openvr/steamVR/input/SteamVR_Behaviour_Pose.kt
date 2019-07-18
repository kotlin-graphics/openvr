package openvr.steamVR.input

import glm_.vec3.Vec3
import openvr.lib.TrackingResult
import openvr.lib.vrInput
import openvr.plugin2.SteamVR_Input_Sources
import openvr.plugin2.Transform
import openvr.steamVR.script.SteamVR_HistoryBuffer
import openvr.unity.Time

//======= Copyright (c) Valve Corporation, All rights reserved. ===============

typealias SteamVR_Behaviour_PoseEvent = (SteamVR_Behaviour_Pose, SteamVR_Input_Sources) -> Unit
typealias SteamVR_Behaviour_Pose_ConnectedChangedEvent = (SteamVR_Behaviour_Pose, SteamVR_Input_Sources, Boolean) -> Unit
typealias SteamVR_Behaviour_Pose_TrackingChangedEvent = (SteamVR_Behaviour_Pose, SteamVR_Input_Sources, TrackingResult) -> Unit
typealias SteamVR_Behaviour_Pose_DeviceIndexChangedEvent = (SteamVR_Behaviour_Pose, SteamVR_Input_Sources, Int) -> Unit

/** This component simplifies the use of Pose actions. Adding it to a gameobject will auto set that transform's position and rotation every update to match the pose.
 *  Advanced velocity estimation is handled through a buffer of the last 30 updates. */
class SteamVR_Behaviour_Pose {

    val poseAction = SteamVR_Input.getAction<SteamVR_Action_Pose>(ActionType.Pose, "Pose")

    /** The device this action should apply to. Any if the action is not device specific. */
    lateinit var inputSource: SteamVR_Input_Sources

    /** If not set, relative to parent */
    var origin: Transform? = null

    /** @Returns whether or not the current pose is in a valid state */
    val isValid: Boolean
        get() = poseAction!![inputSource]!!.poseIsValid

    /** @Returns whether or not the pose action is bound and able to be updated */
    val isActive: Boolean
        get() = poseAction!![inputSource]!!.active


    /** This Unity event will fire whenever the position or rotation of this transform is updated. */
    var onTransformUpdated: SteamVR_Behaviour_PoseEvent? = null

    /** This Unity event will fire whenever the position or rotation of this transform is changed. */
    var onTransformChanged: SteamVR_Behaviour_PoseEvent? = null

    /** This Unity event will fire whenever the device is connected or disconnected */
    var onConnectedChanged: SteamVR_Behaviour_Pose_ConnectedChangedEvent? = null

    /** This Unity event will fire whenever the device's tracking state changes */
    var onTrackingChanged: SteamVR_Behaviour_Pose_TrackingChangedEvent? = null

    /** This Unity event will fire whenever the device's deviceIndex changes */
    var onDeviceIndexChanged: SteamVR_Behaviour_Pose_DeviceIndexChangedEvent? = null


    /** This C# event will fire whenever the position or rotation of this transform is updated. */
    var onTransformUpdatedEvent: SteamVR_Behaviour_Pose_UpdateHandler? = null

    /** This C# event will fire whenever the position or rotation of this transform is changed. */
    var onTransformChangedEvent: SteamVR_Behaviour_Pose_ChangeHandler? = null

    /** This C# event will fire whenever the device is connected or disconnected */
    var onConnectedChangedEvent: SteamVR_Behaviour_Pose_DeviceConnectedChangeHandler? = null

    /** This C# event will fire whenever the device's tracking state changes */
    var onTrackingChangedEvent: SteamVR_Behaviour_Pose_TrackingChangeHandler? = null

    /** This C# event will fire whenever the device's deviceIndex changes */
    var onDeviceIndexChangedEvent: SteamVR_Behaviour_Pose_DeviceIndexChangedHandler? = null


    /** Can be disabled to stop broadcasting bound device status changes */
    var broadcastDeviceChanges = true

    protected var deviceIndex = -1

    protected val historyBuffer = SteamVR_HistoryBuffer(30)


    protected fun start() {
        if (poseAction == null) {
            System.err.println("[SteamVR] No pose action set for this component")
            return
        }

        checkDeviceIndex()

//        if (origin == null)
//            origin = transform.parent TODO
    }

    protected fun onEnable() {
//        SteamVR.Initialize()

        if (poseAction != null) {
            poseAction[inputSource]!!.onUpdate += ::onUpdate
            poseAction[inputSource]!!.onDeviceConnectedChanged += ::onDeviceConnectedChanged
            poseAction[inputSource]!!.onTrackingChanged += ::onTrackingChanged
            poseAction[inputSource]!!.onChange += ::onChange
        }
    }

    protected fun onDisable() {
        poseAction?.get(inputSource)!!.let {
            it.onUpdate -= ::onUpdate
            it.onDeviceConnectedChanged -= ::onDeviceConnectedChanged
            it.onTrackingChanged -= ::onTrackingChanged
            it.onChange -= ::onChange
        }

        historyBuffer.clear()
    }

    private fun onUpdate(fromAction: SteamVR_Action_Pose, fromSource: SteamVR_Input_Sources) {

        updateHistoryBuffer()

        updateTransform()

        onTransformUpdated?.invoke(this, inputSource)
        onTransformUpdatedEvent?.invoke(this, inputSource)
    }

    protected fun updateTransform() {

        checkDeviceIndex()
        TODO()
//        if (origin != null) {
//            transform.position = origin.transform.TransformPoint(poseAction[inputSource].localPosition)
//            transform.rotation = origin.rotation * poseAction[inputSource].localRotation
//        } else {
//            transform.localPosition = poseAction[inputSource].localPosition
//            transform.localRotation = poseAction[inputSource].localRotation
//        }
    }

    private fun onChange(fromAction: SteamVR_Action_Pose, fromSource: SteamVR_Input_Sources) {
        onTransformChanged?.invoke(this, fromSource)
        onTransformChangedEvent?.invoke(this, fromSource)
    }

    protected fun onDeviceConnectedChanged(changedAction: SteamVR_Action_Pose, changedSource: SteamVR_Input_Sources, connected: Boolean) {

        checkDeviceIndex()

        onConnectedChanged?.invoke(this, inputSource, connected)
        onConnectedChangedEvent?.invoke(this, inputSource, connected)
    }

    protected fun onTrackingChanged(changedAction: SteamVR_Action_Pose, changedSource: SteamVR_Input_Sources, trackingChanged: TrackingResult) {
        onTrackingChanged?.invoke(this, inputSource, trackingChanged)
        onTrackingChangedEvent?.invoke(this, inputSource, trackingChanged)
    }

    protected fun checkDeviceIndex() {
        val action = poseAction!![inputSource]!!
        if (action.active && action.deviceIsConnected) {
            val currentDeviceIndex = action.trackedDeviceIndex

            if (deviceIndex != currentDeviceIndex) {
                deviceIndex = currentDeviceIndex

                if (broadcastDeviceChanges) {
                    println("SetInputSource $inputSource")
                    println("SetDeviceIndex $deviceIndex")
                }

                onDeviceIndexChanged?.invoke(this, inputSource, deviceIndex)
                onDeviceIndexChangedEvent?.invoke(this, inputSource, deviceIndex)
            }
        }
    }

    /** @Returns the device index for the device bound to the pose. */
    fun GetDeviceIndex(): Int {
        if (deviceIndex == -1)
            checkDeviceIndex()
        return deviceIndex
    }

    /** @Returns the current velocity of the pose (as of the last update) */
    val velocity: Vec3
        get() = poseAction!![inputSource]!!.velocity

    /** @Returns the current angular velocity of the pose (as of the last update) */
    val angularVelocity: Vec3
        get() = poseAction!![inputSource]!!.angularVelocity

    /** @Returns the velocities of the pose at the time specified. Can predict in the future or return past values. */
    fun getVelocitiesAtTimeOffset(secondsFromNow: Float, velocity: Vec3, angularVelocity: Vec3): Boolean =
            poseAction!![inputSource]!!.getVelocitiesAtTimeOffset(secondsFromNow, velocity, angularVelocity)

    /** Uses previously recorded values to find the peak speed of the pose and returns the corresponding velocity and angular velocity */
    fun getEstimatedPeakVelocities(velocity: Vec3, angularVelocity: Vec3) {
        val top = historyBuffer.getTopVelocity(10, 1)
        historyBuffer.getAverageVelocities(velocity, angularVelocity, 2, top)
    }

    protected var lastFrameUpdated = 0
    protected fun updateHistoryBuffer() {
        val currentFrame = Time.frameCount
        if (lastFrameUpdated != currentFrame) {
            val action = poseAction!![inputSource]!!
            historyBuffer.update(action.localPosition, action.localRotation, action.velocity, action.angularVelocity)
            lastFrameUpdated = currentFrame
        }
    }

    /** Gets the localized name of the device that the action corresponds to.
     *
     *  @param localizedParts:
     *      VRInputString.Hand - Which hand the origin is in. E.g. "Left Hand"
     *      VRInputString.ControllerType - What kind of controller the user has in that hand.E.g. "Vive Controller"
     *      VRInputString.InputSource - What part of that controller is the origin. E.g. "Trackpad"
     *      VRInputString.All - All of the above. E.g. "Left Hand Vive Controller Trackpad" */
    fun getLocalizedName(localizedParts: Array<vrInput.VRInputString>): String? =
            poseAction?.getLocalizedOriginPart(inputSource, localizedParts)
}

typealias SteamVR_Behaviour_Pose_ActiveChangeHandler = (fromAction: SteamVR_Behaviour_Pose, fromSource: SteamVR_Input_Sources, active: Boolean) -> Unit
typealias SteamVR_Behaviour_Pose_ChangeHandler = (fromAction: SteamVR_Behaviour_Pose, fromSource: SteamVR_Input_Sources) -> Unit
typealias SteamVR_Behaviour_Pose_UpdateHandler = (fromAction: SteamVR_Behaviour_Pose, fromSource: SteamVR_Input_Sources) -> Unit
typealias SteamVR_Behaviour_Pose_TrackingChangeHandler = (fromAction: SteamVR_Behaviour_Pose, fromSource: SteamVR_Input_Sources, trackingState: TrackingResult) -> Unit
typealias SteamVR_Behaviour_Pose_ValidPoseChangeHandler = (fromAction: SteamVR_Behaviour_Pose, fromSource: SteamVR_Input_Sources, validPose: Boolean) -> Unit
typealias SteamVR_Behaviour_Pose_DeviceConnectedChangeHandler = (fromAction: SteamVR_Behaviour_Pose, fromSource: SteamVR_Input_Sources, deviceConnected: Boolean) -> Unit
typealias SteamVR_Behaviour_Pose_DeviceIndexChangedHandler = (fromAction: SteamVR_Behaviour_Pose, fromSource: SteamVR_Input_Sources, newDeviceIndex: Int) -> Unit