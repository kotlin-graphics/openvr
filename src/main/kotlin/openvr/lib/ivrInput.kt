package openvr.lib

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.IntByReference
import glm_.size
import org.lwjgl.system.MemoryUtil.NULL
import java.nio.ByteBuffer

// ivrinput.h =============================================================================================================================================

typealias VRActionHandle = Long
typealias VRActionSetHandle = Long
typealias VRInputValueHandle = Long

val invalidActionHandle: VRActionHandle = NULL
val invalidActionSetHandle: VRActionSetHandle = NULL
val invalidInputValueHandle: VRInputValueHandle = NULL

val maxActionNameLength = 64
val maxActionSetNameLength = 64
val maxActionOriginCount = 16

open class InputAnalogActionData : Structure {

    /** Whether or not this action is currently available to be bound in the active action set */
    @JvmField
    var active = false
    /** The origin that caused this action's current state */
    @JvmField
    var activeOrigin: VRInputValueHandle = NULL
    // The current state of this action; will be delta updates for mouse actions
    @JvmField
    var x = 0f
    @JvmField
    var y = 0f
    @JvmField
    var z = 0f
    // Deltas since the previous call to UpdateActionState()
    @JvmField
    var deltaX = 0f
    @JvmField
    var deltaY = 0f
    @JvmField
    var deltaZ = 0f
    /** Time relative to now when this event happened. Will be negative to indicate a past time. */
    @JvmField
    var updateTime = 0f

    constructor()

    constructor(active: Boolean, activeOrigin: VRInputValueHandle, x: Float, y: Float, z: Float, deltaX: Float, deltaY: Float,
                deltaZ: Float, updateTime: Float) {
        this.active = active
        this.activeOrigin = activeOrigin
        this.x = x
        this.y = y
        this.z = z
        this.deltaX = deltaX
        this.deltaY = deltaY
        this.deltaZ = deltaZ
        this.updateTime = updateTime
    }

    override fun getFieldOrder() = listOf("active", "activeOrigin", "x", "y", "z", "deltaX", "deltaY", "deltaZ", "updateTime")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : InputAnalogActionData(), Structure.ByReference
    class ByValue : InputAnalogActionData(), Structure.ByValue
}

open class InputDigitalActionData : Structure {

    /** Whether or not this action is currently available to be bound in the active action set */
    @JvmField
    var active = false
    /** The origin that caused this action's current state */
    @JvmField
    var activeOrigin: VRInputValueHandle = NULL
    /** The current state of this action; will be true if currently pressed */
    @JvmField
    var state = false
    /** This is true if the state has changed since the last frame */
    @JvmField
    var changed = false
    /** Time relative to now when this event happened. Will be negative to indicate a past time. */
    @JvmField
    var updateTime = 0f

    constructor()

    constructor(active: Boolean, activeOrigin: VRInputValueHandle, state: Boolean, changed: Boolean, updateTime: Float) {
        this.active = active
        this.activeOrigin = activeOrigin
        this.state = state
        this.changed = changed
        this.updateTime = updateTime
    }

    override fun getFieldOrder() = listOf("active", "activeOrigin", "state", "changed", "updateTime")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : InputDigitalActionData(), Structure.ByReference
    class ByValue : InputDigitalActionData(), Structure.ByValue
}

open class InputPoseActionData : Structure {

    /** Whether or not this action is currently available to be bound in the active action set */
    @JvmField
    var active = false
    /** The origin that caused this action's current state */
    @JvmField
    var activeOrigin: VRInputValueHandle = NULL
    /** The current state of this action */
    @JvmField
    var pose = TrackedDevicePose()

    constructor()

    constructor(active: Boolean, activeOrigin: VRInputValueHandle, pose: TrackedDevicePose) {
        this.active = active
        this.activeOrigin = activeOrigin
        this.pose = pose
    }

    override fun getFieldOrder() = listOf("active", "activeOrigin", "pose")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : InputPoseActionData(), Structure.ByReference
    class ByValue : InputPoseActionData(), Structure.ByValue
}

enum class EVRSkeletalTransformSpace { Action, Parent, Additive;

    val i = ordinal
}

open class InputSkeletonActionData : Structure {

    /** Whether or not this action is currently available to be bound in the active action set */
    @JvmField
    var active = false
    /** The origin that caused this action's current state */
    @JvmField
    var activeOrigin: VRInputValueHandle = NULL

    constructor()

    constructor(active: Boolean, activeOrigin: VRInputValueHandle) {
        this.active = active
        this.activeOrigin = activeOrigin
    }

    override fun getFieldOrder() = listOf("active", "activeOrigin")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : InputSkeletonActionData(), Structure.ByReference
    class ByValue : InputSkeletonActionData(), Structure.ByValue
}

enum class EVRInputFilterCancelType { Timers, Momentum;

    val i = ordinal
}

open class InputOriginInfo : Structure {

    @JvmField
    var devicePath: VRInputValueHandle = NULL
    @JvmField
    var trackedDeviceIndex: TrackedDeviceIndex = 0
    @JvmField
    var _renderModelComponentName = CharArray(128)
    var renderModelComponentName
        get() = String(_renderModelComponentName)
        set(value) {
            value.toCharArray(_renderModelComponentName)
        }

    constructor()

    constructor(devicePath: VRInputValueHandle, trackedDeviceIndex: TrackedDeviceIndex, renderModelComponentName: String) {
        this.devicePath = devicePath
        this.trackedDeviceIndex = trackedDeviceIndex
        this.renderModelComponentName = renderModelComponentName
    }

    override fun getFieldOrder() = listOf("devicePath", "trackedDeviceIndex", "renderModelComponentName")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : InputOriginInfo(), Structure.ByReference
    class ByValue : InputOriginInfo(), Structure.ByValue
}

open class VRActiveActionSet : Structure {

    /** This is the handle of the action set to activate for this frame. */
    @JvmField
    var actionSet: VRActionSetHandle = NULL
    /** This is the handle of a device path that this action set should be active for.
     *  To activate for all devices, set this to k_ulInvalidInputValueHandle. */
    @JvmField
    var restrictedToDevice: VRInputValueHandle = NULL
    /** The action set to activate for all devices other than ulRestrictedDevice. If restrictedToDevice is set to
     *  InvalidInputValueHandle, this parameter is ignored. */
    @JvmField
    var secondaryActionSet: VRActionSetHandle = NULL

    constructor()

    constructor(actionSet: VRActionSetHandle, restrictedToDevice: VRInputValueHandle, secondaryActionSet: VRActionSetHandle) {
        this.actionSet = actionSet
        this.restrictedToDevice = restrictedToDevice
        this.secondaryActionSet = secondaryActionSet
    }

    override fun getFieldOrder() = listOf("actionSet", "restrictedToDevice", "secondaryActionSet")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : VRActiveActionSet(), Structure.ByReference
    class ByValue : VRActiveActionSet(), Structure.ByValue
}

/** Allows notification sources to interact with the VR system
This current interface is not yet implemented. Do not use yet. */
open class IVRInput : Structure {

    // ---------------  Handle management   --------------- //

    /** Sets the path to the action manifest JSON file that is used by this application. If this information was set
     *  on the Steam partner site, calls to this function are ignored. If the Steam partner site setting and the path
     *  provided by this call are different, VRInputError_MismatchedActionManifest is returned.
     *  This call must be made before the first call to UpdateActionState or IVRSystem::PollNextEvent. */
    fun setActionManifestPath(actionManifestPath: String) = EVRInputError of SetActionManifestPath!!(actionManifestPath)

    @JvmField
    var SetActionManifestPath: SetActionManifestPath_callback? = null

    interface SetActionManifestPath_callback : Callback {
        operator fun invoke(pchActionManifestPath: String): Int
    }

    /** Returns a handle for an action set. This handle is used for all performance-sensitive calls. */
    fun getActionSetHandle(actionSetName: String, handle: VRActionSetHandle) = EVRInputError of GetActionSetHandle!!(actionSetName, handle)

    @JvmField
    var GetActionSetHandle: GetActionSetHandle_callback? = null

    interface GetActionSetHandle_callback : Callback {
        operator fun invoke(pchActionSetName: String, pHandle: VRActionSetHandle): Int
    }

    /** Returns a handle for an action. This handle is used for all performance-sensitive calls. */
    fun getActionHandle(actionName: String, handle: VRActionHandle) = EVRInputError of GetActionHandle!!(actionName, handle)

    @JvmField
    var GetActionHandle: GetActionHandle_callback? = null

    interface GetActionHandle_callback : Callback {
        operator fun invoke(pchActionName: String, pHandle: VRActionHandle): Int
    }

    /** Returns a handle for any path in the input system. E.g. /user/hand/right */
    fun getInputSourceHandle(inputSourcePath: String, handle: VRInputValueHandle) = EVRInputError of GetInputSourceHandle!!(inputSourcePath, handle)

    @JvmField
    var GetInputSourceHandle: GetInputSourceHandle_callback? = null

    interface GetInputSourceHandle_callback : Callback {
        operator fun invoke(pchInputSourcePath: String, pHandle: VRInputValueHandle): Int
    }

    // --------------- Reading action state ------------------- //

    /** Reads the current state into all actions. After this call, the results of Get*Action calls will be the same
     *  until the next call to UpdateActionState. */
    fun updateActionState(sets: VRActiveActionSet.ByReference, sizeOfVRSelectedActionSet: Int, setCount: Int): EVRInputError {
        return EVRInputError of UpdateActionState!!(sets, sizeOfVRSelectedActionSet, setCount)
    }

    @JvmField
    var UpdateActionState: UpdateActionState_callback? = null

    interface UpdateActionState_callback : Callback {
        operator fun invoke(pSets: VRActiveActionSet.ByReference, unSizeOfVRSelectedActionSet: Int, unSetCount: Int): Int
    }

    /** Reads the state of a digital action given its handle. This will return VRInputError_WrongType if the type of
     *  action is something other than digital */
    fun getDigitalActionData(action: VRActionHandle, actionData: InputDigitalActionData.ByReference, actionDataSize: Int): EVRInputError {
        return EVRInputError of GetDigitalActionData!!(action, actionData, actionDataSize)
    }

    @JvmField
    var GetDigitalActionData: GetDigitalActionData_callback? = null

    interface GetDigitalActionData_callback : Callback {
        operator fun invoke(action: VRActionHandle, pActionData: InputDigitalActionData.ByReference, unActionDataSize: Int): Int
    }


    /** Reads the state of an analog action given its handle. This will return VRInputError_WrongType if the type of
     *  action is something other than analog */
    fun getAnalogActionData(action: VRActionHandle, actionData: InputAnalogActionData.ByReference, actionDataSize: Int): EVRInputError {
        return EVRInputError of GetAnalogActionData!!(action, actionData, actionDataSize)
    }

    @JvmField
    var GetAnalogActionData: GetAnalogActionData_callback? = null

    interface GetAnalogActionData_callback : Callback {
        operator fun invoke(action: VRActionHandle, pActionData: InputAnalogActionData.ByReference, unActionDataSize: Int): Int
    }


    /** Reads the state of a pose action given its handle. */
    fun getPoseActionData(action: VRActionHandle, origin: ETrackingUniverseOrigin, predictedSecondsFromNow: Float, actionData: InputPoseActionData.ByReference, actionDataSize: Int): EVRInputError {
        return EVRInputError of GetPoseActionData!!(action, origin.i, predictedSecondsFromNow, actionData, actionDataSize)
    }

    @JvmField
    var GetPoseActionData: GetPoseActionData_callback? = null

    interface GetPoseActionData_callback : Callback {
        operator fun invoke(action: VRActionHandle, eOrigin: Int, fPredictedSecondsFromNow: Float, pActionData: InputPoseActionData.ByReference, unActionDataSize: Int): Int
    }


    /** Reads the state of a skeletal action given its handle. */
    fun getSkeletalActionData(action: VRActionHandle, boneParent: EVRSkeletalTransformSpace, predictedSecondsFromNow: Float, actionData: InputSkeletonActionData.ByReference, transformArray: VRBoneTransform.ByReference): EVRInputError {
        return EVRInputError of GetSkeletalActionData!!(action, boneParent.i, predictedSecondsFromNow, actionData, actionData.size(), transformArray, transformArray.size())
    }

    @JvmField
    var GetSkeletalActionData: GetSkeletalActionData_callback? = null

    interface GetSkeletalActionData_callback : Callback {
        operator fun invoke(action: VRActionHandle, eBoneParent: Int, fPredictedSecondsFromNow: Float, pActionData: InputSkeletonActionData.ByReference, unActionDataSize: Int, pTransformArray: VRBoneTransform.ByReference, unTransformArrayCount: Int): Int
    }

    /** Reads the state of a skeletal action given its handle in a compressed form that is suitable for sending over
     *  the network. The required buffer size will never exceed ( sizeof(VR_BoneTransform_t)*boneCount + 2).
     *  Usually the size will be much smaller. */
    fun getSkeletalActionDataCompressed(action: VRActionHandle, boneParent: EVRSkeletalTransformSpace, predictedSecondsFromNow: Float, compressedData: ByteBuffer, requiredCompressedSize: IntByReference): EVRInputError {
        return EVRInputError of GetSkeletalActionDataCompressed!!(action, boneParent.i, predictedSecondsFromNow, compressedData, compressedData.size, requiredCompressedSize)
    }

    @JvmField
    var GetSkeletalActionDataCompressed: GetSkeletalActionDataCompressed_callback? = null

    interface GetSkeletalActionDataCompressed_callback : Callback {
        operator fun invoke(action: VRActionHandle, eBoneParent: Int, fPredictedSecondsFromNow: Float, pvCompressedData: ByteBuffer, unCompressedSize: Int, punRequiredCompressedSize: IntByReference): Int
    }

    /** Turns a compressed buffer from GetSkeletalActionDataCompressed and turns it back into a bone transform array. */
    fun uncompressSkeletalActionData(compressedBuffer: ByteBuffer, compressedBufferSize: Int, boneParent: Array<EVRSkeletalTransformSpace>, transformArray: VRBoneTransform.ByReference): EVRInputError {
        val peBoneParent = IntArray(boneParent.size) { boneParent[it].i }
        return EVRInputError of UncompressSkeletalActionData!!(compressedBuffer, compressedBufferSize, peBoneParent, transformArray, transformArray.size())
    }

    @JvmField
    var UncompressSkeletalActionData: UncompressSkeletalActionData_callback? = null

    interface UncompressSkeletalActionData_callback : Callback {
        operator fun invoke(pvCompressedBuffer: ByteBuffer, unCompressedBufferSize: Int, peBoneParent: IntArray, pTransformArray: VRBoneTransform.ByReference, unTransformArrayCount: Int): Int
    }

    // --------------- Haptics ------------------- //

    /** Triggers a haptic event as described by the specified action */
    fun triggerHapticVibrationAction(action: VRActionHandle, startSecondsFromNow: Float, durationSeconds: Float, frequency: Float, amplitude: Float): EVRInputError {
        return EVRInputError of TriggerHapticVibrationAction!!(action, startSecondsFromNow, durationSeconds, frequency, amplitude)
    }

    @JvmField
    var TriggerHapticVibrationAction: TriggerHapticVibrationAction_callback? = null

    interface TriggerHapticVibrationAction_callback : Callback {
        operator fun invoke(action: VRActionHandle, fStartSecondsFromNow: Float, fDurationSeconds: Float, fFrequency: Float, fAmplitude: Float): Int
    }

    // --------------- Action Origins ---------------- //

    /** Retrieve origin handles for an action */
    fun getActionOrigins(actionSetHandle: VRActionSetHandle, digitalActionHandle: VRActionHandle, originsOut: Array<VRInputValueHandle>): EVRInputError {
        return EVRInputError of GetActionOrigins!!(actionSetHandle, digitalActionHandle, LongArray(originsOut.size) { originsOut[it] }, originsOut.size)
    }

    @JvmField
    var GetActionOrigins: GetActionOrigins_callback? = null

    interface GetActionOrigins_callback : Callback {
        operator fun invoke(actionSetHandle: VRActionSetHandle, digitalActionHandle: VRActionHandle, originsOut: LongArray, originOutCount: Int): Int
    }

    /** Retrieves the name of the origin in the current language */
    fun getOriginLocalizedName(origin: VRInputValueHandle, nameArray: String): EVRInputError {
        return EVRInputError of GetOriginLocalizedName!!(origin, nameArray, nameArray.length)
    }

    @JvmField
    var GetOriginLocalizedName: GetOriginLocalizedName_callback? = null

    interface GetOriginLocalizedName_callback : Callback {
        operator fun invoke(origin: VRInputValueHandle, pchNameArray: String, unNameArraySize: Int): Int
    }

    /** Retrieves useful information for the origin of this action */
    fun getOriginTrackedDeviceInfo(origin: VRInputValueHandle, originInfo: InputOriginInfo.ByReference): EVRInputError {
        return EVRInputError of GetOriginTrackedDeviceInfo!!(origin, originInfo, originInfo.size())
    }

    @JvmField
    var GetOriginTrackedDeviceInfo: GetOriginTrackedDeviceInfo_callback? = null

    interface GetOriginTrackedDeviceInfo_callback : Callback {
        operator fun invoke(origin: VRInputValueHandle, pOriginInfo: InputOriginInfo.ByReference, unOriginInfoSize: Int): Int
    }

    /** Shows the current binding for the action in-headset */
    fun showActionOrigins(actionSetHandle: VRActionSetHandle, actionHandle: VRActionHandle): EVRInputError {
        return EVRInputError of ShowActionOrigins!!(actionSetHandle, actionHandle)
    }

    @JvmField
    var ShowActionOrigins: ShowActionOrigins_callback? = null

    interface ShowActionOrigins_callback : Callback {
        operator fun invoke(actionSetHandle: VRActionSetHandle, ulActionHandle: VRActionHandle): Int
    }

    /** Shows the current binding all the actions in the specified action sets */
    fun showBindingsForActionSet(sets: VRActiveActionSet.ByReference, sizeOfVRSelectedActionSet: Int, setCount: Int, originToHighlight: VRInputValueHandle): EVRInputError {
        return EVRInputError of ShowBindingsForActionSet!!(sets, sizeOfVRSelectedActionSet, setCount, originToHighlight)
    }

    @JvmField
    var ShowBindingsForActionSet: ShowBindingsForActionSet_callback? = null

    interface ShowBindingsForActionSet_callback : Callback {
        operator fun invoke(pSets: VRActiveActionSet.ByReference, unSizeOfVRSelectedActionSet: Int, unSetCount: Int, originToHighlight: VRInputValueHandle): Int
    }

    constructor()

    override fun getFieldOrder() = listOf("SetActionManifestPath", "GetActionSetHandle", "GetActionHandle",
            "GetInputSourceHandle", "UpdateActionState", "GetDigitalActionData", "GetAnalogActionData",
            "GetPoseActionData", "GetSkeletalActionData", "GetSkeletalActionDataCompressed",
            "UncompressSkeletalActionData", "TriggerHapticVibrationAction", "GetActionOrigins",
            "GetOriginLocalizedName", "GetOriginTrackedDeviceInfo", "ShowActionOrigins", "ShowBindingsForActionSet")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRNotifications(), Structure.ByReference
    class ByValue : IVRNotifications(), Structure.ByValue
}

val IVRInput_Version = "FnTable:IVRInput_003"