package openvr.steamVR.input

import glm_.func.deg
import glm_.glm
import glm_.glm.PIf
import glm_.glm.abs
import glm_.quat.Quat
import glm_.vec3.Vec3
import kool.indices
import openvr.lib.*
import openvr.lib.vrInput.VRSummaryType
import openvr.plugin.Utils
import openvr.plugin2.SteamVR_Input_Sources
import openvr.plugin2.Transform
import openvr.unity.Time
import openvr.unity.angle
import org.lwjgl.openvr.InputSkeletalActionData
import org.lwjgl.openvr.VRBoneTransform
import org.lwjgl.openvr.VRSkeletalSummaryData

//======= Copyright (c) Valve Corporation, All rights reserved. ===============

typealias SteamVR_Action_Skeleton_ActiveChangeHandler = (fromAction: SteamVR_Action_Skeleton, active: Boolean) -> Unit
typealias SteamVR_Action_Skeleton_ChangeHandler = (fromAction: SteamVR_Action_Skeleton) -> Unit
typealias SteamVR_Action_Skeleton_UpdateHandler = (fromAction: SteamVR_Action_Skeleton) -> Unit
typealias SteamVR_Action_Skeleton_TrackingChangeHandler = (fromAction: SteamVR_Action_Skeleton, trackingState: TrackingResult) -> Unit
typealias SteamVR_Action_Skeleton_ValidPoseChangeHandler = (fromAction: SteamVR_Action_Skeleton, validPose: Boolean) -> Unit
typealias SteamVR_Action_Skeleton_DeviceConnectedChangeHandler = (fromAction: SteamVR_Action_Skeleton, deviceConnected: Boolean) -> Unit

/** Skeleton Actions are our best approximation of where your hands are while holding vr controllers and pressing buttons. We give you 31 bones to help you animate hand models.
 *  For more information check out this blog post: https://steamcommunity.com/games/250820/announcements/detail/1690421280625220068 */
class SteamVR_Action_Skeleton : SteamVR_Action_Pose_Base<SteamVR_Action_Skeleton_Source_Map,
        SteamVR_Action_Skeleton_Source>(), ISteamVR_Action_Skeleton_Source/*, ISerializationCallbackReceiver */ {

    /** Event fires when the active state (ActionSet active and binding active) changes */
    val onActiveChange = EventClass(sourceMap!![SteamVR_Input_Sources.Any]!!.onActiveChange_)

    /** Event fires when the active state of the binding changes */
    val onActiveBindingChange = EventClass(sourceMap!![SteamVR_Input_Sources.Any]!!.onActiveBindingChange_)

    /** Event fires when the state of the pose or bones moves more than the changeTolerance */
    val onChange = EventClass(sourceMap!![SteamVR_Input_Sources.Any]!!.onChange_)

    /** Event fires when the action is updated */
    val onUpdate = EventClass(sourceMap!![SteamVR_Input_Sources.Any]!!.onUpdate_)

    /** Event fires when the state of the tracking system that is used to create pose data (position, rotation, etc) changes */
    val onTrackingChanged = EventClass(sourceMap!![SteamVR_Input_Sources.Any]!!.onTrackingChanged_)

    /** Event fires when the state of the pose data retrieved for this action changes validity (good/bad data from the tracking source) */
    val onValidPoseChanged = EventClass(sourceMap!![SteamVR_Input_Sources.Any]!!.onValidPoseChanged_)

    /** Event fires when the device bound to this action is connected or disconnected */
    val onDeviceConnectedChanged = EventClass(sourceMap!![SteamVR_Input_Sources.Any]!!.onDeviceConnectedChanged_)

    /** [Should not be called by user code]
     *  Updates the skeleton action data */
    fun updateValue(skipStateAndEventUpdates: Boolean) =
            sourceMap!![SteamVR_Input_Sources.Any]!!.updateValue(skipStateAndEventUpdates)

    /** [Should not be called by user code]
     *  Updates the skeleton action data without firing events */
    fun updateValueWithoutEvents() = sourceMap!![SteamVR_Input_Sources.Any]!!.updateValue(true)

    /** Update a transform's local position and local roation to match the pose from the most recent update
     *
     *  @param transformToUpdate: The transform of the object to be updated */
    fun updateTransform(transformToUpdate: Transform) =
            super.updateTransform(SteamVR_Input_Sources.Any, transformToUpdate)

    /** An array of the positions of the bones from the most recent update. Relative to skeletalTransformSpace. See SteamVR_Skeleton_JointIndexes for bone indexes. */
    override val bonePositions: Array<Vec3>
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.bonePositions

    /** An array of the rotations of the bones from the most recent update. Relative to skeletalTransformSpace. See SteamVR_Skeleton_JointIndexes for bone indexes. */
    override val boneRotations: Array<Quat>
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.boneRotations

    /** From the previous update: An array of the positions of the bones from the most recent update. Relative to skeletalTransformSpace. See SteamVR_Skeleton_JointIndexes for bone indexes. */
    override val lastBonePositions: Array<Vec3>
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastBonePositions

    /** From the previous update: An array of the rotations of the bones from the most recent update. Relative to skeletalTransformSpace. See SteamVR_Skeleton_JointIndexes for bone indexes. */
    override val lastBoneRotations: Array<Quat>
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastBoneRotations

    /** The range of motion the we're using to get bone data from. With Controller being your hand while holding the controller. */
    override var rangeOfMotion: VRSkeletalMotionRange
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.rangeOfMotion
        set(value) {
            sourceMap!![SteamVR_Input_Sources.Any]!!.rangeOfMotion = value
        }

    /** The space to get bone data in. Parent space by default */
    override var skeletalTransformSpace: vrInput.VRSkeletalTransformSpace
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.skeletalTransformSpace
        set(value) {
            sourceMap!![SteamVR_Input_Sources.Any]!!.skeletalTransformSpace = value
        }

    /** The type of summary data that will be retrieved by default. FromAnimation is smoothed data to based on the skeletal animation system. FromDevice is as recent from the device as we can get - may be different data from smoothed. */
    var summaryDataType: VRSummaryType
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.summaryDataType
        set(newValue) {
            sourceMap!![SteamVR_Input_Sources.Any]!!.summaryDataType = newValue
        }

    /** Get the accuracy level of the skeletal tracking data.
     *      Estimated: Body part location can’t be directly determined by the device. Any skeletal pose provided
     *          by the device is estimated based on the active buttons, triggers, joysticks, or other input sensors.
     *          Examples include the Vive Controller and gamepads.
     *      Partial: Body part location can be measured directly but with fewer degrees of freedom than the actual body
     *          part.Certain body part positions may be unmeasured by the device and estimated from other input data.
     *          Examples include Knuckles or gloves that only measure finger curl
     *      Full: Body part location can be measured directly throughout the entire range of motion of the body part.
     *          Examples include hi-end mocap systems, or gloves that measure the rotation of each finger segment. */
    override val skeletalTrackingLevel: vrInput.VRSkeletalTrackingLevel
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.skeletalTrackingLevel

    /** A 0-1 value representing how curled the thumb is. 0 being straight, 1 being fully curled. */
    override val thumbCurl: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.thumbCurl

    /** A 0-1 value representing how curled the index finger is. 0 being straight, 1 being fully curled. */
    override val indexCurl: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.indexCurl

    /** A 0-1 value representing how curled the middle finger is. 0 being straight, 1 being fully curled. */
    override val middleCurl: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.middleCurl

    /** A 0-1 value representing how curled the ring finger is. 0 being straight, 1 being fully curled. */
    override val ringCurl: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.ringCurl

    /** A 0-1 value representing how curled the pinky finger is. 0 being straight, 1 being fully curled. */
    override val pinkyCurl: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.pinkyCurl

    /** A 0-1 value representing the size of the gap between the thumb and index fingers */
    override val thumbIndexSplay: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.thumbIndexSplay

    /** A 0-1 value representing the size of the gap between the index and middle fingers */
    override val indexMiddleSplay: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.indexMiddleSplay

    /** A 0-1 value representing the size of the gap between the middle and ring fingers */
    override val middleRingSplay: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.middleRingSplay

    /** A 0-1 value representing the size of the gap between the ring and pinky fingers */
    override val ringPinkySplay: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.ringPinkySplay


    /** [Previous Update] A 0-1 value representing how curled the thumb is. 0 being straight, 1 being fully curled. */
    override val lastThumbCurl: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastThumbCurl

    /** [Previous Update] A 0-1 value representing how curled the index finger is. 0 being straight, 1 being fully curled. */
    override val lastIndexCurl: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastIndexCurl

    /** [Previous Update] A 0-1 value representing how curled the middle finger is. 0 being straight, 1 being fully curled. */
    override val lastMiddleCurl: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastMiddleCurl

    /** [Previous Update] A 0-1 value representing how curled the ring finger is. 0 being straight, 1 being fully curled. */
    override val lastRingCurl: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastRingCurl

    /** [Previous Update] A 0-1 value representing how curled the pinky finger is. 0 being straight, 1 being fully curled. */
    override val lastPinkyCurl: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastPinkyCurl

    /** [Previous Update] A 0-1 value representing the size of the gap between the thumb and index fingers */
    override val lastThumbIndexSplay: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastThumbIndexSplay

    /** [Previous Update] A 0-1 value representing the size of the gap between the index and middle fingers */
    override val lastIndexMiddleSplay: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastIndexMiddleSplay

    /** [Previous Update] A 0-1 value representing the size of the gap between the middle and ring fingers */
    override val lastMiddleRingSplay: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastMiddleRingSplay

    /** [Previous Update] A 0-1 value representing the size of the gap between the ring and pinky fingers */
    override val lastRingPinkySplay: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastRingPinkySplay

    /** 0-1 values representing how curled the specified finger is. 0 being straight, 1 being fully curled. For indexes see: SteamVR_Skeleton_FingerIndexes */
    override val fingerCurls: FloatArray
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.fingerCurls

    /** 0-1 values representing how splayed the specified finger and it's next index'd finger is. For indexes see: SteamVR_Skeleton_FingerIndexes */
    override val fingerSplays: FloatArray
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.fingerSplays

    /** [Previous Update] 0-1 values representing how curled the specified finger is. 0 being straight, 1 being fully curled. For indexes see: SteamVR_Skeleton_FingerIndexes */
    override val lastFingerCurls: FloatArray
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastFingerCurls

    /** [Previous Update] 0-1 values representing how splayed the specified finger and it's next index'd finger is. For indexes see: SteamVR_Skeleton_FingerIndexes */
    override val lastFingerSplays: FloatArray
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastFingerSplays

    /** summary>Separate from "changed". If the pose for this skeleton action has changed (root position/rotation) */
    val poseChanged: Boolean
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.poseChanged


    /** Skips processing the full per bone data and only does the summary data */
    override var onlyUpdateSummaryData: Boolean
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.onlyUpdateSummaryData
        set(newValue) {
            sourceMap!![SteamVR_Input_Sources.Any]!!.onlyUpdateSummaryData = newValue
        }

    /** True if this action is bound and the ActionSet is active
     *  JVM ~getActive to avoid clashing */
    fun GetActive(): Boolean = sourceMap!![SteamVR_Input_Sources.Any]!!.active

    /** True if the ActionSet that contains this action is active */
    fun getSetActive(): Boolean = actionSet!!.isActive(SteamVR_Input_Sources.Any)

    /** SteamVR keeps a log of past poses so you can retrieve old poses or estimated poses in the future by passing in a secondsFromNow value that is negative or positive.
     *
     *  @param secondsFromNow: The time offset in the future (estimated) or in the past (previously recorded) you want to get data from
     *  @returns true if we successfully returned a pose */
    fun getVelocitiesAtTimeOffset(secondsFromNow: Float, velocity: Vec3, angularVelocity: Vec3): Boolean =
            sourceMap!![SteamVR_Input_Sources.Any]!!.getVelocitiesAtTimeOffset(secondsFromNow, velocity, angularVelocity)

    /** SteamVR keeps a log of past poses so you can retrieve old poses or estimated poses in the future by passing in a secondsFromNow value that is negative or positive.
     *
     *  @param secondsFromNow: The time offset in the future (estimated) or in the past (previously recorded) you want to get data from
     *  @returns true if we successfully returned a pose */
    fun getPoseAtTimeOffset(secondsFromNow: Float, position: Vec3, rotation: Quat, velocity: Vec3, angularVelocity: Vec3): Boolean =
            sourceMap!![SteamVR_Input_Sources.Any]!!.getPoseAtTimeOffset(secondsFromNow, position, rotation, velocity, angularVelocity)

    /** The local position of the pose relative to the universe origin */
    override val localPosition: Vec3
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.localPosition

    /** The local rotation of the pose relative to the universe origin */
    override val localRotation: Quat
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.localRotation

    /** The local velocity of the pose relative to the universe origin */
    override val velocity: Vec3
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.velocity

    /** The local angular velocity of the pose relative to the universe origin */
    override val angularVelocity: Vec3 =
            sourceMap!![SteamVR_Input_Sources.Any]!!.angularVelocity

    /** True if the device bound to this action is connected */
    override val deviceIsConnected: Boolean =
            sourceMap!![SteamVR_Input_Sources.Any]!!.deviceIsConnected

    /** True if the pose retrieved for this action is valid (good data from the tracking source) */
    override val poseIsValid: Boolean =
            sourceMap!![SteamVR_Input_Sources.Any]!!.poseIsValid

    /** The state of the tracking system that is used to create pose data (position, rotation, etc) */
    val trackingResult: TrackingResult =
            sourceMap!![SteamVR_Input_Sources.Any]!!.trackingState


    /** The last local position of the pose relative to the universe origin */
    override val lastLocalPosition: Vec3 =
            sourceMap!![SteamVR_Input_Sources.Any]!!.lastLocalPosition

    /** The last local rotation of the pose relative to the universe origin */
    override val lastLocalRotation: Quat =
            sourceMap!![SteamVR_Input_Sources.Any]!!.lastLocalRotation

    /** The last local velocity of the pose relative to the universe origin */
    override val lastVelocity: Vec3 =
            sourceMap!![SteamVR_Input_Sources.Any]!!.lastVelocity

    /** The last local angular velocity of the pose relative to the universe origin */
    override val lastAngularVelocity: Vec3 =
            sourceMap!![SteamVR_Input_Sources.Any]!!.lastAngularVelocity

    /** True if the device bound to this action was connected during the previous update */
    override val lastDeviceIsConnected: Boolean =
            sourceMap!![SteamVR_Input_Sources.Any]!!.lastDeviceIsConnected

    /** True if the pose was valid during the previous update */
    override val lastPoseIsValid: Boolean =
            sourceMap!![SteamVR_Input_Sources.Any]!!.lastPoseIsValid

    /** The tracking state for this pose during the previous update */
    val lastTrackingResult: TrackingResult =
            sourceMap!![SteamVR_Input_Sources.Any]!!.lastTrackingState


    /** Gets the bone positions in local space. This array may be modified later so if you want to hold this data then pass true to get a copy of the data instead of the actual array
     *
     *  @param copy: This array may be modified later so if you want to hold this data then pass true to get a copy of the data instead of the actual array */
    fun getBonePositions(copy: Boolean = false): Array<Vec3> = when {
        copy -> sourceMap!![SteamVR_Input_Sources.Any]!!.bonePositions.clone()
        else -> sourceMap!![SteamVR_Input_Sources.Any]!!.bonePositions
    }

    /** Gets the bone rotations in local space. This array may be modified later so if you want to hold this data then pass true to get a copy of the data instead of the actual array
     *
     *  @param copy: This array may be modified later so if you want to hold this data then pass true to get a copy of the data instead of the actual array */
    fun getBoneRotations(copy: Boolean = false): Array<Quat> = when {
        copy -> sourceMap!![SteamVR_Input_Sources.Any]!!.boneRotations.clone()
        else -> sourceMap!![SteamVR_Input_Sources.Any]!!.boneRotations
    }

    /** Gets the bone positions in local space from the previous update. This array may be modified later so if you want to hold this data then pass true to get a copy of the data instead of the actual array
     *
     *  @param copy: This array may be modified later so if you want to hold this data then pass true to get a copy of the data instead of the actual array */
    fun getLastBonePositions(copy: Boolean = false): Array<Vec3> = when {
        copy -> sourceMap!![SteamVR_Input_Sources.Any]!!.lastBonePositions.clone()
        else -> sourceMap!![SteamVR_Input_Sources.Any]!!.lastBonePositions
    }

    /** Gets the bone rotations in local space from the previous update. This array may be modified later so if you want to hold this data then pass true to get a copy of the data instead of the actual array
     *
     *  @param copy: This array may be modified later so if you want to hold this data then pass true to get a copy of the data instead of the actual array */
    fun getLastBoneRotations(copy: Boolean = false): Array<Quat> = when {
        copy -> sourceMap!![SteamVR_Input_Sources.Any]!!.lastBoneRotations.clone()
        else -> sourceMap!![SteamVR_Input_Sources.Any]!!.lastBoneRotations
    }

    /** Set the range of the motion of the bones in this skeleton. Options are "With Controller" as if your hand is holding your VR controller.
     *  Or "Without Controller" as if your hand is empty. This will set the range for the following update. */
    fun aetRangeOfMotion(range: VRSkeletalMotionRange) {
        sourceMap!![SteamVR_Input_Sources.Any]!!.rangeOfMotion = range
    }

//    /** Sets the space that you'll get bone data back in. Options are relative to the Model and relative to the Parent bone
//     *
//     *  @param space: the space that you'll get bone data back in. Options are relative to the Model and relative to the Parent bone. */
//    override var skeletalTransformSpace: vrInput.VRSkeletalTransformSpace
//        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.skeletalTransformSpace
//        set(value) {
//            sourceMap!![SteamVR_Input_Sources.Any]!!.skeletalTransformSpace = value
//        }

    /** @Returns the total number of bones in the skeleton */
    val boneCount: Int
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.boneCount

    /** @Returns the order of bones in the hierarchy */
    val boneHierarchy: IntArray
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.boneHierarchy

    /** @Returns the name of the bone */
    fun getBoneName(boneIndex: Int): String =
            sourceMap!![SteamVR_Input_Sources.Any]!!.getBoneName(boneIndex)

    /** @Returns an array of positions/rotations that represent the state of each bone in a reference pose.
     *
     *  @param transformSpace: What to get the position/rotation data relative to, the model, or the bone's parent
     *  @param referencePose: Which reference pose to return */
    fun getReferenceTransforms(transformSpace: vrInput.VRSkeletalTransformSpace,
                               referencePose: vrInput.VRSkeletalReferencePose): Array<Utils.RigidTransform> =
            sourceMap!![SteamVR_Input_Sources.Any]!!.getReferenceTransforms(transformSpace, referencePose)

//    /** Get the accuracy level of the skeletal tracking data.
//     *  @returns:
//     *      Estimated: Body part location can’t be directly determined by the device. Any skeletal pose provided
//     *          by the device is estimated based on the active buttons, triggers, joysticks, or other input sensors.
//     *          Examples include the Vive Controller and gamepads.
//     *      Partial: Body part location can be measured directly but with fewer degrees of freedom than the actual body
//     *          part.Certain body part positions may be unmeasured by the device and estimated from other input data.
//     *          Examples include Knuckles or gloves that only measure finger curl
//     *      Full: Body part location can be measured directly throughout the entire range of motion of the body part.
//     *          Examples include hi-end mocap systems, or gloves that measure the rotation of each finger segment. */
//    override val skeletalTrackingLevel: vrInput.VRSkeletalTrackingLevel
//        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.skeletalTrackingLevel

    /** @Returns the finger curl data that we calculate each update. This array may be modified later so if you want to hold this data then pass true to get a copy of the data instead of the actual array
     *
     *  @param copy: This array may be modified later so if you want to hold this data then pass true to get a copy of the data instead of the actual array */
    fun getFingerCurls(copy: Boolean = false): FloatArray = when {
        copy -> sourceMap!![SteamVR_Input_Sources.Any]!!.fingerCurls.clone()
        else -> sourceMap!![SteamVR_Input_Sources.Any]!!.fingerCurls
    }

    /** @Returns the finger curl data from the previous update. This array may be modified later so if you want to hold this data then pass true to get a copy of the data instead of the actual array
     *
     *  @param copy: This array may be modified later so if you want to hold this data then pass true to get a copy of the data instead of the actual array */
    fun getLastFingerCurls(copy: Boolean = false) = when {
        copy -> sourceMap!![SteamVR_Input_Sources.Any]!!.lastFingerCurls.clone()
        else -> sourceMap!![SteamVR_Input_Sources.Any]!!.lastFingerCurls
    }

    /** @Returns the finger splay data that we calculate each update. This array may be modified later so if you want to hold this data then pass true to get a copy of the data instead of the actual array
     *
     *  @param copy: This array may be modified later so if you want to hold this data then pass true to get a copy of the data instead of the actual array */
    fun getFingerSplays(copy: Boolean = false): FloatArray = when {
        copy -> sourceMap!![SteamVR_Input_Sources.Any]!!.fingerSplays.clone()
        else -> sourceMap!![SteamVR_Input_Sources.Any]!!.fingerSplays
    }

    /** @Returns the finger splay data from the previous update. This array may be modified later so if you want to hold this data then pass true to get a copy of the data instead of the actual array
     *
     *  @param copy: This array may be modified later so if you want to hold this data then pass true to get a copy of the data instead of the actual array */
    fun getLastFingerSplays(copy: Boolean = false): FloatArray = when {
        copy -> sourceMap!![SteamVR_Input_Sources.Any]!!.lastFingerSplays.clone()
        else -> sourceMap!![SteamVR_Input_Sources.Any]!!.lastFingerSplays
    }

    /** @Returns a value indicating how much the passed in finger is currently curled.
     *
     *  @param finger: The index of the finger to return a curl value for. 0-4. thumb, index, middle, ring, pinky
     *  @returns 0-1 value. 0 being straight, 1 being fully curled. */
    fun getFingerCurl(finger: Int): Float =
            sourceMap!![SteamVR_Input_Sources.Any]!!.fingerCurls[finger]

    /** @Returns a value indicating how the size of the gap between fingers.
     *
     *  @param fingerGapIndex: The index of the finger gap to return a splay value for. 0 being the gap between thumb and index, 1 being the gap between index and middle, 2 being the gap between middle and ring, and 3 being the gap between ring and pinky.
     *  @returns 0-1 value. 0 being no gap, 1 being "full" gap */
    fun getSplay(fingerGapIndex: Int): Float =
            sourceMap!![SteamVR_Input_Sources.Any]!!.fingerSplays[fingerGapIndex]

    /** @Returns a value indicating how much the passed in finger is currently curled.
     *
     *  @param finger: The finger to return a curl value for
     *  @returns 0-1 value. 0 being straight, 1 being fully curled. */
    fun getFingerCurl(finger: FingerIndex): Float =
            getFingerCurl(finger.i)

    /** @Returns a value indicating how the size of the gap between fingers.
     *
     *  @param fingerGapIndex: The finger gap to return a splay value for.
     *  @returns 0-1 value. 0 being no gap, 1 being "full" gap */
    fun getSplay(fingerSplay: FingerSplayIndex): Float =
            getSplay(fingerSplay.i)

    /** @Returns a value indicating how much the passed in finger was curled during the previous update
     *
     *  @param finger: The index of the finger to return a curl value for. 0-4. thumb, index, middle, ring, pinky
     *  @returns 0-1 value. 0 being straight, 1 being fully curled. */
    fun getLastFingerCurl(finger: Int): Float =
            sourceMap!![SteamVR_Input_Sources.Any]!!.lastFingerCurls[finger]

    /** @Returns a value indicating the size of the gap between fingers during the previous update
     *
     *  @param fingerGapIndex: The index of the finger gap to return a splay value for. 0 being the gap between thumb and index, 1 being the gap between index and middle, 2 being the gap between middle and ring, and 3 being the gap between ring and pinky.
     *  @returns 0-1 value. 0 being no gap, 1 being "full" gap */
    fun getLastSplay(fingerGapIndex: Int): Float =
            sourceMap!![SteamVR_Input_Sources.Any]!!.lastFingerSplays[fingerGapIndex]

    /** @Returns a value indicating how much the passed in finger was curled during the previous update
     *
     *  @param finger: The finger to return a curl value for
     *  @returns 0-1 value. 0 being straight, 1 being fully curled. */
    fun getLastFingerCurl(finger: FingerIndex): Float =
            getLastFingerCurl(finger.i)

    /** @Returns a value indicating the size of the gap between fingers during the previous update
     *
     *  @param fingerGapIndex: The finger gap to return a splay value for.
     *  @returns 0-1 value. 0 being no gap, 1 being "full" gap */
    fun getLastSplay(fingerSplay: FingerSplayIndex): Float =
            getLastSplay(fingerSplay.i)


    /** Gets the localized name of the device that the action corresponds to. Include as many EVRInputStringBits as you want to add to the localized string
     *
     *  @param localizedParts:
     *      VRInputString.Hand - Which hand the origin is in. ex: "Left Hand".
     *      VRInputString.ControllerType - What kind of controller the user has in that hand. ex: "Vive Controller".
     *      VRInputString.InputSource - What part of that controller is the origin. ex: "Trackpad".
     *      VRInputString.All - All of the above. ex: "Left Hand Vive Controller Trackpad".  */
    fun getLocalizedName(localizedParts: Array<vrInput.VRInputString>): String? =
            sourceMap!![SteamVR_Input_Sources.Any]!!.getLocalizedOriginPart(localizedParts)


    /** Fires an event when a device is connected or disconnected.
     *  @param functionToCall: The method you would like to be called when a device is connected. Should take a SteamVR_Action_Pose as a param */
    fun addOnDeviceConnectedChanged(functionToCall: SteamVR_Action_Skeleton_DeviceConnectedChangeHandler) {
        sourceMap!![SteamVR_Input_Sources.Any]!!.onDeviceConnectedChanged_ += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The method you would like to stop calling when a device is connected. Should take a SteamVR_Action_Pose as a param */
    fun removeOnDeviceConnectedChanged(functionToStopCalling: SteamVR_Action_Skeleton_DeviceConnectedChangeHandler) {
        sourceMap!![SteamVR_Input_Sources.Any]!!.onDeviceConnectedChanged_ -= functionToStopCalling
    }


    /** Fires an event when the tracking of the device has changed
     *  @param functionToCall: The method you would like to be called when tracking has changed. Should take a SteamVR_Action_Pose as a param */
    fun addOnTrackingChanged(functionToCall: SteamVR_Action_Skeleton_TrackingChangeHandler) {
        sourceMap!![SteamVR_Input_Sources.Any]!!.onTrackingChanged_ += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The method you would like to stop calling when tracking has changed. Should take a SteamVR_Action_Pose as a param */
    fun removeOnTrackingChanged(functionToStopCalling: SteamVR_Action_Skeleton_TrackingChangeHandler) {
        sourceMap!![SteamVR_Input_Sources.Any]!!.onTrackingChanged_ -= functionToStopCalling
    }


    /** Fires an event when the device now has a valid pose or no longer has a valid pose
     *  @param functionToCall: The method you would like to be called when the pose has become valid or invalid. Should take a SteamVR_Action_Pose as a param */
    fun addOnValidPoseChanged(functionToCall: SteamVR_Action_Skeleton_ValidPoseChangeHandler) {
        sourceMap!![SteamVR_Input_Sources.Any]!!.onValidPoseChanged_ += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The method you would like to stop calling when the pose has become valid or invalid. Should take a SteamVR_Action_Pose as a param */
    fun removeOnValidPoseChanged(functionToStopCalling: SteamVR_Action_Skeleton_ValidPoseChangeHandler) {
        sourceMap!![SteamVR_Input_Sources.Any]!!.onValidPoseChanged_ -= functionToStopCalling
    }


    /** Executes a function when this action's bound state changes */
    fun addOnActiveChangeListener(functionToCall: SteamVR_Action_Skeleton_ActiveChangeHandler) {
        sourceMap!![SteamVR_Input_Sources.Any]!!.onActiveChange_ += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive update events */
    fun removeOnActiveChangeListener(functionToStopCalling: SteamVR_Action_Skeleton_ActiveChangeHandler) {
        sourceMap!![SteamVR_Input_Sources.Any]!!.onActiveChange_ -= functionToStopCalling
    }

    /** Executes a function when the state of this action changes
     *  @param functionToCall: A local function that receives the boolean action who's state has changed, the corresponding input source, and the new value */
    fun addOnChangeListener(functionToCall: SteamVR_Action_Skeleton_ChangeHandler) {
        sourceMap!![SteamVR_Input_Sources.Any]!!.onChange_ += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive on change events */
    fun removeOnChangeListener(functionToStopCalling: SteamVR_Action_Skeleton_ChangeHandler) {
        sourceMap!![SteamVR_Input_Sources.Any]!!.onChange_ -= functionToStopCalling
    }

    /** Executes a function when the state of this action is updated.
     *  @param functionToCall: A local function that receives the boolean action who's state has changed, the corresponding input source, and the new value */
    fun addOnUpdateListener(functionToCall: SteamVR_Action_Skeleton_UpdateHandler) {
        sourceMap!![SteamVR_Input_Sources.Any]!!.onUpdate_ += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive update events */
    fun removeOnUpdateListener(functionToStopCalling: SteamVR_Action_Skeleton_UpdateHandler) {
        sourceMap!![SteamVR_Input_Sources.Any]!!.onUpdate_ -= functionToStopCalling
    }

//    void ISerializationCallbackReceiver.OnBeforeSerialize()
//    {
//    }
//
//    void ISerializationCallbackReceiver.OnAfterDeserialize()
//    {
//        InitAfterDeserialize()
//    }

    companion object {

        const val numBones = 31
        val steamVRFixUpRotation = glm.angleAxis(PIf.deg, Vec3(0, 1, 0))
    }
}

class SteamVR_Action_Skeleton_Source_Map : SteamVR_Action_Pose_Source_Map<SteamVR_Action_Skeleton_Source>() {
    override fun getSourceElementForIndexer(inputSource: SteamVR_Input_Sources): SteamVR_Action_Skeleton_Source? =
            sources[SteamVR_Input_Sources.Any] //just in case somebody tries to access a different element, redirect them to the correct one.
}

/** Skeleton Actions are our best approximation of where your hands are while holding vr controllers and pressing buttons. We give you 31 bones to help you animate hand models.
 *  For more information check out this blog post: https://steamcommunity.com/games/250820/announcements/detail/1690421280625220068 */
class SteamVR_Action_Skeleton_Source : SteamVR_Action_Pose_Source(), ISteamVR_Action_Skeleton_Source {

    /** Event fires when the active state (ActionSet active and binding active) changes */
    val onActiveChange_ = ArrayList<SteamVR_Action_Skeleton_ActiveChangeHandler>()

    /** Event fires when the active state of the binding changes */
    val onActiveBindingChange_ = ArrayList<SteamVR_Action_Skeleton_ActiveChangeHandler>()

    /** Event fires when the orientation of the pose or bones changes more than the changeTolerance */
    val onChange_ = ArrayList<SteamVR_Action_Skeleton_ChangeHandler>()

    /** Event fires when the action is updated */
    val onUpdate_ = ArrayList<SteamVR_Action_Skeleton_UpdateHandler>()

    /** Event fires when the state of the tracking system that is used to create pose data (position, rotation, etc) changes */
    val onTrackingChanged_ = ArrayList<SteamVR_Action_Skeleton_TrackingChangeHandler>()

    /** Event fires when the state of the pose data retrieved for this action changes validity (good/bad data from the tracking source) */
    val onValidPoseChanged_ = ArrayList<SteamVR_Action_Skeleton_ValidPoseChangeHandler>()

    /** Event fires when the device bound to this action is connected or disconnected */
    val onDeviceConnectedChanged_ = ArrayList<SteamVR_Action_Skeleton_DeviceConnectedChangeHandler>()


    /** True if the action is bound */
    override val activeBinding: Boolean
        get() = skeletonActionData.active

    /** True if the action's binding was active during the previous update */
    override val lastActiveBinding: Boolean
        get() = lastSkeletonActionData.active

    /** An array of the positions of the bones from the most recent update. Relative to skeletalTransformSpace. See SteamVR_Skeleton_JointIndexes for bone indexes. */
    override lateinit var bonePositions: Array<Vec3>
        protected set

    /** An array of the rotations of the bones from the most recent update. Relative to skeletalTransformSpace. See SteamVR_Skeleton_JointIndexes for bone indexes. */
    override lateinit var boneRotations: Array<Quat>
        protected set

    /** From the previous update: An array of the positions of the bones from the most recent update. Relative to skeletalTransformSpace. See SteamVR_Skeleton_JointIndexes for bone indexes. */
    override lateinit var lastBonePositions: Array<Vec3>
        protected set

    /** From the previous update: An array of the rotations of the bones from the most recent update. Relative to skeletalTransformSpace. See SteamVR_Skeleton_JointIndexes for bone indexes. */
    override lateinit var lastBoneRotations: Array<Quat>
        protected set


    /** The range of motion the we're using to get bone data from. With Controller being your hand while holding the controller. */
    override lateinit var rangeOfMotion: VRSkeletalMotionRange

    /** The space to get bone data in. Parent space by default */
    override lateinit var skeletalTransformSpace: vrInput.VRSkeletalTransformSpace

    /** The type of summary data that will be retrieved by default. FromAnimation is smoothed data to based on the skeletal animation system. FromDevice is as recent from the device as we can get - may be different data from smoothed. */
    lateinit var summaryDataType: VRSummaryType


    /** A 0-1 value representing how curled the thumb is. 0 being straight, 1 being fully curled. */
    override val thumbCurl: Float
        get() = fingerCurls[FingerIndex.thumb.i]

    /** A 0-1 value representing how curled the index finger is. 0 being straight, 1 being fully curled. */
    override val indexCurl: Float
        get() = fingerCurls[FingerIndex.index.i]

    /** A 0-1 value representing how curled the middle finger is. 0 being straight, 1 being fully curled. */
    override val middleCurl: Float
        get() = fingerCurls[FingerIndex.middle.i]

    /** A 0-1 value representing how curled the ring finger is. 0 being straight, 1 being fully curled. */
    override val ringCurl: Float
        get() = fingerCurls[FingerIndex.ring.i]

    /** A 0-1 value representing how curled the pinky finger is. 0 being straight, 1 being fully curled. */
    override val pinkyCurl: Float
        get() = fingerCurls[FingerIndex.pinky.i]


    /** A 0-1 value representing the size of the gap between the thumb and index fingers */
    override val thumbIndexSplay: Float
        get() = fingerSplays[FingerSplayIndex.thumbIndex.i]

    /** A 0-1 value representing the size of the gap between the index and middle fingers */
    override val indexMiddleSplay: Float
        get() = fingerSplays[FingerSplayIndex.indexMiddle.i]

    /** A 0-1 value representing the size of the gap between the middle and ring fingers */
    override val middleRingSplay: Float
        get() = fingerSplays[FingerSplayIndex.middleRing.i]

    /** A 0-1 value representing the size of the gap between the ring and pinky fingers */
    override val ringPinkySplay: Float
        get() = fingerSplays[FingerSplayIndex.ringPinky.i]


    /** [Previous Update] A 0-1 value representing how curled the thumb is. 0 being straight, 1 being fully curled. */
    override val lastThumbCurl: Float
        get() = lastFingerCurls[FingerIndex.thumb.i]

    /** [Previous Update] A 0-1 value representing how curled the index finger is. 0 being straight, 1 being fully curled. */
    override val lastIndexCurl: Float
        get() = lastFingerCurls[FingerIndex.index.i]

    /** [Previous Update] A 0-1 value representing how curled the middle finger is. 0 being straight, 1 being fully curled. */
    override val lastMiddleCurl: Float
        get() = lastFingerCurls[FingerIndex.middle.i]

    /** [Previous Update] A 0-1 value representing how curled the ring finger is. 0 being straight, 1 being fully curled. */
    override val lastRingCurl: Float
        get() = lastFingerCurls[FingerIndex.ring.i]

    /** [Previous Update] A 0-1 value representing how curled the pinky finger is. 0 being straight, 1 being fully curled. */
    override val lastPinkyCurl: Float
        get() = lastFingerCurls[FingerIndex.pinky.i]


    /** [Previous Update] A 0-1 value representing the size of the gap between the thumb and index fingers */
    override val lastThumbIndexSplay: Float
        get() = lastFingerSplays[FingerSplayIndex.thumbIndex.i]

    /** [Previous Update] A 0-1 value representing the size of the gap between the index and middle fingers */
    override val lastIndexMiddleSplay: Float
        get() = lastFingerSplays[FingerSplayIndex.indexMiddle.i]

    /** [Previous Update] A 0-1 value representing the size of the gap between the middle and ring fingers */
    override val lastMiddleRingSplay: Float
        get() = lastFingerSplays[FingerSplayIndex.middleRing.i]

    /** [Previous Update] A 0-1 value representing the size of the gap between the ring and pinky fingers */
    override val lastRingPinkySplay: Float
        get() = lastFingerSplays[FingerSplayIndex.ringPinky.i]


    /** 0-1 values representing how curled the specified finger is. 0 being straight, 1 being fully curled. For indexes see: SteamVR_Skeleton_FingerIndexes */
    override lateinit var fingerCurls: FloatArray
        protected set

    /** 0-1 values representing how splayed the specified finger and it's next index'd finger is. For indexes see: SteamVR_Skeleton_FingerIndexes */
    override lateinit var fingerSplays: FloatArray
        protected set

    /** [Previous Update] 0-1 values representing how curled the specified finger is. 0 being straight, 1 being fully curled. For indexes see: SteamVR_Skeleton_FingerIndexes */
    override lateinit var lastFingerCurls: FloatArray
        protected set

    /** [Previous Update] 0-1 values representing how splayed the specified finger and it's next index'd finger is. For indexes see: SteamVR_Skeleton_FingerIndexes */
    override lateinit var lastFingerSplays: FloatArray
        protected set

    /** Separate from "changed". If the pose for this skeleton action has changed (root position/rotation) */
    var poseChanged = false
        protected set

    /** Skips processing the full per bone data and only does the summary data */
    override var onlyUpdateSummaryData: Boolean = false


    protected val skeletalSummaryData = VRSkeletalSummaryData.calloc()
    protected var lastSkeletalSummaryData = VRSkeletalSummaryData.calloc()
    protected lateinit var skeletonAction: SteamVR_Action_Skeleton

    protected val tempBoneTransforms = VRBoneTransform.calloc(SteamVR_Action_Skeleton.numBones)

    protected val skeletonActionData = InputSkeletalActionData.calloc()

    protected var lastSkeletonActionData = InputSkeletalActionData.calloc()

    protected val tempSkeletonActionData = InputSkeletalActionData.calloc()

    override fun preinitialize(wrappingAction: SteamVR_Action, forInputSource: SteamVR_Input_Sources) {

        super.preinitialize(wrappingAction, forInputSource)
        skeletonAction = wrappingAction as SteamVR_Action_Skeleton

        bonePositions = Array(SteamVR_Action_Skeleton.numBones) { Vec3() }
        lastBonePositions = Array(SteamVR_Action_Skeleton.numBones) { Vec3() }
        boneRotations = Array(SteamVR_Action_Skeleton.numBones) { Quat() }
        lastBoneRotations = Array(SteamVR_Action_Skeleton.numBones) { Quat() }

        rangeOfMotion = VRSkeletalMotionRange.WithController
        skeletalTransformSpace = vrInput.VRSkeletalTransformSpace.Parent

        fingerCurls = FloatArray(FingerIndex.values().size)
        fingerSplays = FloatArray(FingerSplayIndex.values().size)

        lastFingerCurls = FloatArray(FingerIndex.values().size)
        lastFingerSplays = FloatArray(FingerSplayIndex.values().size)
    }

    /** [Should not be called by user code]
     *  Updates the data for this action and this input source. Sends related events. */
    override fun updateValue() = updateValue(false)

    /** [Should not be called by user code]
     *  Updates the data for this action and this input source. Sends related events. */
    override fun updateValue(skipStateAndEventUpdates: Boolean) {

        lastActive = active
        lastSkeletonActionData = skeletonActionData // TODO check assignment/copy
        lastSkeletalSummaryData = skeletalSummaryData

        if (!onlyUpdateSummaryData)
            repeat(SteamVR_Action_Skeleton.numBones) {
                lastBonePositions[it] = bonePositions[it]
                lastBoneRotations[it] = boneRotations[it]
            }

        FingerIndex.values().forEach { lastFingerCurls[it.i] = fingerCurls[it.i] } // TODO check values()

        FingerSplayIndex.values().forEach { lastFingerSplays[it.i] = fingerSplays[it.i] } // TODO check values()

        super.updateValue(true)
        poseChanged = changed

        var error = vrInput.getSkeletalActionData(handle, skeletonActionData)
        if (error != vrInput.Error.None) {
            System.err.println("[SteamVR] GetSkeletalActionData error ($fullPath): $error handle: $handle")
            return
        }

        if (active) {
            if (onlyUpdateSummaryData == false) {
                error = vrInput.getSkeletalBoneData(handle, skeletalTransformSpace, rangeOfMotion, tempBoneTransforms)
                if (error != vrInput.Error.None)
                    System.err.println("[SteamVR] GetSkeletalBoneData error ($fullPath): $error handle: $handle")

                for (boneIndex in tempBoneTransforms.indices) {
                    // SteamVR's coordinate system is right handed, and Unity's is left handed.  The FBX data has its
                    // X axis flipped when Unity imports it, so here we need to flip the X axis as well
                    val p = tempBoneTransforms[boneIndex].position
                    bonePositions[boneIndex].x = -p.x
                    bonePositions[boneIndex].y = p.y
                    bonePositions[boneIndex].z = p.z

                    val o = tempBoneTransforms[boneIndex].orientation
                    boneRotations[boneIndex].x = o.x
                    boneRotations[boneIndex].y = -o.y
                    boneRotations[boneIndex].z = -o.z
                    boneRotations[boneIndex].w = o.w
                }

                // Now that we're in the same handedness as Unity, rotate the root bone around the Y axis
                // so that forward is facing down +Z

                boneRotations[0] = SteamVR_Action_Skeleton.steamVRFixUpRotation * boneRotations[0]
            }
            updateSkeletalSummaryData(summaryDataType, true)
        }

        if (!changed)
            for (boneIndex in tempBoneTransforms.indices) {
                if (glm.distance(lastBonePositions[boneIndex], bonePositions[boneIndex]) > changeTolerance) {
                    changed = true
                    break
                }
                if (abs(angle(lastBoneRotations[boneIndex], boneRotations[boneIndex])) > changeTolerance) {
                    changed = true
                    break
                }
            }

        if (changed)
            changedTime = Time.realtimeSinceStartup

        if (!skipStateAndEventUpdates)
            checkAndSendEvents()
    }

    /** The number of bones in the skeleton for this action */
    val boneCount: Int
        get() {
            val boneCount = vrInput.getBoneCount(handle)
            if (vrInput.error != vrInput.Error.None)
                System.err.println("[SteamVR] GetBoneCount error ($fullPath): ${vrInput.error} handle: $handle")
            return boneCount
        }

    /** Gets the ordering of the bone hierarchy */
    val boneHierarchy: IntArray
        get() {
            val parentIndices = vrInput.getBoneHierarchy(handle, boneCount)
            if (vrInput.error != vrInput.Error.None)
                System.err.println("[SteamVR] GetBoneHierarchy error ($fullPath): ${vrInput.error} handle: $handle")
            return parentIndices
        }

    /** Gets the name for a bone at the specified index */
    fun getBoneName(boneIndex: Int): String {
        val name = vrInput.getBoneName(handle, boneIndex)
        if (vrInput.error != vrInput.Error.None)
            System.err.println("[SteamVR] GetBoneName error ($fullPath): ${vrInput.error} handle: $handle")
        return name
    }

    /** @Returns an array of positions/rotations that represent the state of each bone in a reference pose.
     *
     *  @param transformSpace: What to get the position/rotation data relative to, the model, or the bone's parent
     *  @param referencePose: Which reference pose to return */
    fun getReferenceTransforms(transformSpace: vrInput.VRSkeletalTransformSpace, referencePose: vrInput.VRSkeletalReferencePose): Array<Utils.RigidTransform> {

        val transforms = Array(boneCount) { Utils.RigidTransform() }

        val boneTransforms = VRBoneTransform.calloc(transforms.size)

        val error = vrInput.getSkeletalReferenceTransforms(handle, transformSpace, referencePose, boneTransforms)
        if (error != vrInput.Error.None)
            System.err.println("[SteamVR] GetSkeletalReferenceTransforms error ($fullPath): $error handle: $handle")

        for (transformIndex in boneTransforms.indices) {
            val p = boneTransforms[transformIndex].position
            val position = Vec3(-p.x, p.y, p.z)
            val o = boneTransforms[transformIndex].orientation
            val rotation = Quat(o.w, o.x, -o.y, -o.z)
            transforms[transformIndex] = Utils.RigidTransform(position, rotation)
        }

        if (transforms.isNotEmpty()) {
            // Now that we're in the same handedness as Unity, rotate the root bone around the Y axis
            // so that forward is facing down +Z
            val qFixUpRot = glm.angleAxis(PIf.deg, Vec3(0, 1, 0))

            transforms[0].rot put (qFixUpRot * transforms[0].rot)
        }

        return transforms
    }

    /** Get the accuracy level of the skeletal tracking data.
     *      Estimated: Body part location can’t be directly determined by the device. Any skeletal pose provided
     *          by the device is estimated based on the active buttons, triggers, joysticks, or other input sensors.
     *          Examples include the Vive Controller and gamepads.
     *      Partial: Body part location can be measured directly but with fewer degrees of freedom than the actual body
     *          part.Certain body part positions may be unmeasured by the device and estimated from other input data.
     *          Examples include Knuckles or gloves that only measure finger curl
     *      Full: Body part location can be measured directly throughout the entire range of motion of the body part.
     *          Examples include hi-end mocap systems, or gloves that measure the rotation of each finger segment. */
    override val skeletalTrackingLevel: vrInput.VRSkeletalTrackingLevel
        get() {
            val skeletalTrackingLevel = vrInput.getSkeletalTrackingLevel(handle)
            if (vrInput.error != vrInput.Error.None)
                System.err.println("[SteamVR] GetSkeletalTrackingLevel error ($fullPath): ${vrInput.error} handle: $handle")
            return skeletalTrackingLevel
        }

    /** Get the skeletal summary data structure from OpenVR.
     *  Contains curl and splay data in finger order: thumb, index, middlg, ring, pinky.
     *  Easier access at named members: indexCurl, ringSplay, etc. */
    protected fun getSkeletalSummaryData(summaryType: VRSummaryType = VRSummaryType.FromAnimation, force: Boolean = false): VRSkeletalSummaryData {
        updateSkeletalSummaryData(summaryType, force)
        return skeletalSummaryData
    }

    /** Updates the skeletal summary data structure from OpenVR.
     *  Contains curl and splay data in finger order: thumb, index, middlg, ring, pinky.
     *  Easier access at named members: indexCurl, ringSplay, etc. */
    protected fun updateSkeletalSummaryData(summaryType: VRSummaryType = VRSummaryType.FromAnimation, force: Boolean = false) {
        if (force || this.summaryDataType != summaryType && active) {
            vrInput.getSkeletalSummaryData(handle, summaryType, skeletalSummaryData)
            if (vrInput.error != vrInput.Error.None)
                System.err.println("[SteamVR] GetSkeletalSummaryData error ($fullPath): ${vrInput.error} handle: $handle")

            fingerCurls[0] = skeletalSummaryData.flFingerCurl(0)
            fingerCurls[1] = skeletalSummaryData.flFingerCurl(1)
            fingerCurls[2] = skeletalSummaryData.flFingerCurl(2)
            fingerCurls[3] = skeletalSummaryData.flFingerCurl(3)
            fingerCurls[4] = skeletalSummaryData.flFingerCurl(4)

            //no splay data for thumb
            fingerSplays[0] = skeletalSummaryData.flFingerSplay(0)
            fingerSplays[1] = skeletalSummaryData.flFingerSplay(1)
            fingerSplays[2] = skeletalSummaryData.flFingerSplay(2)
            fingerSplays[3] = skeletalSummaryData.flFingerSplay(3)
        }
    }

    override fun checkAndSendEvents() {

        if (trackingState != lastTrackingState)
            onTrackingChanged_.forEach { it(skeletonAction, trackingState) }

        if (poseIsValid != lastPoseIsValid)
            onValidPoseChanged_.forEach { it(skeletonAction, poseIsValid) }

        if (deviceIsConnected != lastDeviceIsConnected)
            onDeviceConnectedChanged_.forEach { it(skeletonAction, deviceIsConnected) }

        if (changed)
            onChange_.forEach { it(skeletonAction) }

        if (active != lastActive)
            onActiveChange_.forEach { it(skeletonAction, active) }

        if (activeBinding != lastActiveBinding)
            onActiveBindingChange_.forEach { it(skeletonAction, activeBinding) }

        onUpdate_.forEach { it(skeletonAction) }
    }
}

interface ISteamVR_Action_Skeleton_Source {
    /** Get the accuracy level of the skeletal tracking data.
     *  @returned
     *      Estimated: Body part location can’t be directly determined by the device. Any skeletal pose provided
     *          by the device is estimated based on the active buttons, triggers, joysticks, or other input sensors.
     *          Examples include the Vive Controller and gamepads.
     *      Partial: Body part location can be measured directly but with fewer degrees of freedom than the actual
     *          body part.Certain body part positions may be unmeasured by the device and estimated from other input
     *          data.Examples include Knuckles or gloves that only measure finger curl
     *      Full: Body part location can be measured directly throughout the entire range of motion of the body part.
     *          Examples include hi-end mocap systems, or gloves that measure the rotation of each finger segment. */
    val skeletalTrackingLevel: vrInput.VRSkeletalTrackingLevel

    /** An array of the positions of the bones from the most recent update. Relative to skeletalTransformSpace. See SteamVR_Skeleton_JointIndexes for bone indexes. */
    val bonePositions: Array<Vec3>

    /** An array of the rotations of the bones from the most recent update. Relative to skeletalTransformSpace. See SteamVR_Skeleton_JointIndexes for bone indexes. */
    val boneRotations: Array<Quat>

    /** From the previous update: An array of the positions of the bones from the most recent update. Relative to skeletalTransformSpace. See SteamVR_Skeleton_JointIndexes for bone indexes. */
    val lastBonePositions: Array<Vec3>

    /** From the previous update: An array of the rotations of the bones from the most recent update. Relative to skeletalTransformSpace. See SteamVR_Skeleton_JointIndexes for bone indexes. */
    val lastBoneRotations: Array<Quat>

    /** The range of motion the we're using to get bone data from. With Controller being your hand while holding the controller. */
    var rangeOfMotion: VRSkeletalMotionRange

    /** The space to get bone data in. Parent space by default */
    var skeletalTransformSpace: vrInput.VRSkeletalTransformSpace

    /** Skips processing the full per bone data and only does the summary data */
    var onlyUpdateSummaryData: Boolean

    /** A 0-1 value representing how curled the thumb is. 0 being straight, 1 being fully curled. */
    val thumbCurl: Float

    /** A 0-1 value representing how curled the index finger is. 0 being straight, 1 being fully curled. */
    val indexCurl: Float

    /** A 0-1 value representing how curled the middle finger is. 0 being straight, 1 being fully curled. */
    val middleCurl: Float

    /** A 0-1 value representing how curled the ring finger is. 0 being straight, 1 being fully curled. */
    val ringCurl: Float

    /** A 0-1 value representing how curled the pinky finger is. 0 being straight, 1 being fully curled. */
    val pinkyCurl: Float

    /** A 0-1 value representing the size of the gap between the thumb and index fingers */
    val thumbIndexSplay: Float

    /** A 0-1 value representing the size of the gap between the index and middle fingers */
    val indexMiddleSplay: Float

    /** A 0-1 value representing the size of the gap between the middle and ring fingers */
    val middleRingSplay: Float

    /** A 0-1 value representing the size of the gap between the ring and pinky fingers */
    val ringPinkySplay: Float


    /** [Previous Update] A 0-1 value representing how curled the thumb is. 0 being straight, 1 being fully curled. */
    val lastThumbCurl: Float

    /** [Previous Update] A 0-1 value representing how curled the index finger is. 0 being straight, 1 being fully curled. */
    val lastIndexCurl: Float

    /** [Previous Update] A 0-1 value representing how curled the middle finger is. 0 being straight, 1 being fully curled. */
    val lastMiddleCurl: Float

    /** [Previous Update] A 0-1 value representing how curled the ring finger is. 0 being straight, 1 being fully curled. */
    val lastRingCurl: Float

    /** [Previous Update] A 0-1 value representing how curled the pinky finger is. 0 being straight, 1 being fully curled. */
    val lastPinkyCurl: Float

    /** [Previous Update] A 0-1 value representing the size of the gap between the thumb and index fingers */
    val lastThumbIndexSplay: Float

    /** [Previous Update] A 0-1 value representing the size of the gap between the index and middle fingers */
    val lastIndexMiddleSplay: Float

    /** [Previous Update] A 0-1 value representing the size of the gap between the middle and ring fingers */
    val lastMiddleRingSplay: Float

    /** [Previous Update] A 0-1 value representing the size of the gap between the ring and pinky fingers */
    val lastRingPinkySplay: Float


    /** 0-1 values representing how curled the specified finger is. 0 being straight, 1 being fully curled. For indexes see: SteamVR_Skeleton_FingerIndexes */
    val fingerCurls: FloatArray

    /** 0-1 values representing how splayed the specified finger and it's next index'd finger is. For indexes see: SteamVR_Skeleton_FingerIndexes */
    val fingerSplays: FloatArray

    /** [Previous Update] 0-1 values representing how curled the specified finger is. 0 being straight, 1 being fully curled. For indexes see: SteamVR_Skeleton_FingerIndexes */
    val lastFingerCurls: FloatArray

    /** [Previous Update] 0-1 values representing how splayed the specified finger and it's next index'd finger is. For indexes see: SteamVR_Skeleton_FingerIndexes */
    val lastFingerSplays: FloatArray
}

/** The change in range of the motion of the bones in the skeleton. Options are "With Controller" as if your hand is holding your VR controller.
 *  Or "Without Controller" as if your hand is empty. */
enum class SkeletalMotionRangeChange(val i: Int) {
    None(-1),
    /** Estimation of bones in hand while holding a controller */
    WithController(0),
    /** Estimation of bones in hand while hand is empty (allowing full fist) */
    WithoutController(1)
}


/** The order of the joints that SteamVR Skeleton Input is expecting. */
enum class JointIndex(val i: Int) {
    root(0),
    wrist(1),
    thumbMetacarpal(2),
    thumbProximal(2),
    thumbMiddle(3),
    thumbDistal(4),
    thumbTip(5),
    indexMetacarpal(6),
    indexProximal(7),
    indexMiddle(8),
    indexDistal(9),
    indexTip(10),
    middleMetacarpal(11),
    middleProximal(12),
    middleMiddle(13),
    middleDistal(14),
    middleTip(15),
    ringMetacarpal(16),
    ringProximal(17),
    ringMiddle(18),
    ringDistal(19),
    ringTip(20),
    pinkyMetacarpal(21),
    pinkyProximal(22),
    pinkyMiddle(23),
    pinkyDistal(24),
    pinkyTip(25),
    thumbAux(26),
    indexAux(27),
    middleAux(28),
    ringAux(29),
    pinkyAux(30);

    val finger: Int
        get() = when (this) {
            root, wrist -> -1
            thumbMetacarpal, thumbMiddle, thumbDistal, thumbTip, thumbAux -> 0
            indexMetacarpal, indexProximal, indexMiddle, indexDistal, indexTip, indexAux -> 1
            middleMetacarpal, middleProximal, middleMiddle, middleDistal, middleTip, middleAux -> 2
            ringMetacarpal, ringProximal, ringMiddle, ringDistal, ringTip, ringAux -> 3
            pinkyMetacarpal, pinkyProximal, pinkyMiddle, pinkyDistal, pinkyTip, pinkyAux -> 4
            else -> -1
        }

    companion object {
        fun getFingerForBone(boneIndex: Int): Int = when (boneIndex) {
            root.i, wrist.i -> -1
            thumbMetacarpal.i, thumbMiddle.i, thumbDistal.i, thumbTip.i, thumbAux.i -> 0
            indexMetacarpal.i, indexProximal.i, indexMiddle.i, indexDistal.i, indexTip.i, indexAux.i -> 1
            middleMetacarpal.i, middleProximal.i, middleMiddle.i, middleDistal.i, middleTip.i, middleAux.i -> 2
            ringMetacarpal.i, ringProximal.i, ringMiddle.i, ringDistal.i, ringTip.i, ringAux.i -> 3
            pinkyMetacarpal.i, pinkyProximal.i, pinkyMiddle.i, pinkyDistal.i, pinkyTip.i, pinkyAux.i -> 4
            else -> -1
        }
    }
}


/** The order of the fingers that SteamVR Skeleton Input outputs */
enum class FingerIndex {
    thumb, index, middle, ring, pinky;

    val i = ordinal
}

/** The order of the fingerSplays that SteamVR Skeleton Input outputs */
enum class FingerSplayIndex {
    thumbIndex, indexMiddle, middleRing, ringPinky;

    val i = ordinal
}