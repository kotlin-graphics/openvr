package lib

import ab.appBuffer
import glm_.buffer.adr
import openvr.lib.ETrackingUniverseOrigin
import org.lwjgl.openvr.*
import org.lwjgl.openvr.OpenVR.IVRInput
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.system.MemoryUtil.memGetLong
import vkk.adr
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.nio.LongBuffer


/**
 * Sets the path to the action manifest JSON file that is used by this application. If this information was set on the Steam partner site, calls to this
 * function are ignored. If the Steam partner site setting and the path provided by this call are different,
 * {@link VR#EVRInputError_VRInputError_MismatchedActionManifest} is returned.
 *
 * <p>This call must be made before the first call to {@link #VRInput_UpdateActionState UpdateActionState} or {@link VRSystem#VRSystem_PollNextEvent PollNextEvent}.</p>
 */
fun IVRInput.setActionManifestPath(actionManifestPath: String): EVRInputError {
    val actionManifestPathEncoded = appBuffer.bufferOfAscii(actionManifestPath)
    return EVRInputError of VRInput.nVRInput_SetActionManifestPath(actionManifestPathEncoded.adr)
}

/** Returns a handle for an action set. This handle is used for all performance-sensitive calls. */
fun IVRInput.getActionSetHandle(actionSetName: String, handle: VRActionSetHandleBuffer): EVRInputError {
    val actionSetNameEncoded = appBuffer.bufferOfAscii(actionSetName)
    return EVRInputError of VRInput.nVRInput_GetActionSetHandle(actionSetNameEncoded.adr, handle.adr)
}

/** Returns a handle for an action set. This handle is used for all performance-sensitive calls. */
infix fun IVRInput.getActionSetHandle(actionSetName: String): VRActionSetHandle {
    val actionSetNameEncoded = appBuffer.bufferOfAscii(actionSetName)
    val handle = appBuffer.long
    VRInput.nVRInput_GetActionSetHandle(actionSetNameEncoded.adr, handle)
    return memGetLong(handle)
}

/** Returns a handle for an action. This handle is used for all performance-sensitive calls. */
fun IVRInput.getActionHandle(actionName: String, handle: VRActionHandleBuffer): EVRInputError {
    val actionNameEncoded = appBuffer.bufferOfAscii(actionName)
    return EVRInputError of VRInput.nVRInput_GetActionHandle(actionNameEncoded.adr, handle.adr)
}

/** Returns a handle for an action. This handle is used for all performance-sensitive calls. */
infix fun IVRInput.getActionHandle(actionName: String): VRActionHandle {
    val actionNameEncoded = appBuffer.bufferOfAscii(actionName)
    val handle = appBuffer.long
    VRInput.nVRInput_GetActionHandle(actionNameEncoded.adr, handle)
    return memGetLong(handle)
}

/** Returns a handle for any path in the input system. E.g. {@code /user/hand/right}. */
fun IVRInput.getInputSourceHandle(inputSourcePath: String, handle: VRInputValueHandleBuffer): EVRInputError {
    val inputSourcePathEncoded = appBuffer.bufferOfAscii(inputSourcePath)
    return EVRInputError of VRInput.nVRInput_GetInputSourceHandle(inputSourcePathEncoded.adr, handle.adr)
}

/** Returns a handle for any path in the input system. E.g. {@code /user/hand/right}. */
infix fun IVRInput.getInputSourceHandle(inputSourcePath: String): VRInputValueHandle {
    val inputSourcePathEncoded = appBuffer.bufferOfAscii(inputSourcePath)
    val handle = appBuffer.long
    VRInput.nVRInput_GetInputSourceHandle(inputSourcePathEncoded.adr, handle)
    return memGetLong(handle)
}

/**
 * Reads the current state into all actions. After this call, the results of {@code Get*Action} calls will be the same until the next call to
 * {@code UpdateActionState}.
 */
infix fun IVRInput.updateActionState(set: VRActiveActionSet): EVRInputError {
    return EVRInputError of VRInput.nVRInput_UpdateActionState(set.adr, VRActiveActionSet.SIZEOF, 1)
}

/**
 * Reads the current state into all actions. After this call, the results of {@code Get*Action} calls will be the same until the next call to
 * {@code UpdateActionState}.
 */
infix fun IVRInput.updateActionState(sets: VRActiveActionSet.Buffer): EVRInputError {
    return EVRInputError of VRInput.nVRInput_UpdateActionState(sets.adr, VRActiveActionSet.SIZEOF, sets.rem)
}

/**
 * Reads the state of a digital action given its handle. This will return {@link VR#EVRInputError_VRInputError_WrongType} if the type of action is something other
 * than digital.
 */
fun IVRInput.getDigitalActionData(action: VRActionHandle, actionData: InputDigitalActionData): EVRInputError {
    return EVRInputError of VRInput.nVRInput_GetDigitalActionData(action, actionData.adr, 1)
}

/**
 * Reads the state of a digital action given its handle. This will return {@link VR#EVRInputError_VRInputError_WrongType} if the type of action is something other
 * than digital.
 */
fun IVRInput.getDigitalActionData(action: VRActionHandle, actionData: InputDigitalActionData.Buffer): EVRInputError {
    return EVRInputError of VRInput.nVRInput_GetDigitalActionData(action, actionData.adr, actionData.rem)
}

/**
 * Reads the state of an analog action given its handle. This will return {@link VR#EVRInputError_VRInputError_WrongType} if the type of action is something other
 * than analog.
 */
fun IVRInput.getAnalogActionData(action: VRActionHandle, actionData: InputAnalogActionData): EVRInputError {
    return EVRInputError of VRInput.nVRInput_GetAnalogActionData(action, actionData.adr, 1)
}

/**
 * Reads the state of an analog action given its handle. This will return {@link VR#EVRInputError_VRInputError_WrongType} if the type of action is something other
 * than analog.
 */
fun IVRInput.getAnalogActionData(action: VRActionHandle, actionData: InputAnalogActionData.Buffer): EVRInputError {
    return EVRInputError of VRInput.nVRInput_GetAnalogActionData(action, actionData.adr, actionData.rem)
}

/**
 * Reads the state of a pose action given its handle.
 *
 * @param origin one of:<br><table><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseSeated}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseStanding}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseRawAndUncalibrated}</td></tr></table>
 */
fun IVRInput.getPoseActionData(action: VRActionHandle, origin: ETrackingUniverseOrigin, predictedSecondsFromNow: Float, actionData: InputPoseActionData): EVRInputError {
    return EVRInputError of VRInput.nVRInput_GetPoseActionData(action, origin.i, predictedSecondsFromNow, actionData.adr, 1)
}

/**
 * Reads the state of a pose action given its handle.
 *
 * @param origin one of:<br><table><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseSeated}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseStanding}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseRawAndUncalibrated}</td></tr></table>
 */
fun IVRInput.getPoseActionData(action: VRActionHandle, origin: ETrackingUniverseOrigin, predictedSecondsFromNow: Float, actionData: InputPoseActionData.Buffer): EVRInputError {
    return EVRInputError of VRInput.nVRInput_GetPoseActionData(action, origin.i, predictedSecondsFromNow, actionData.adr, actionData.rem)
}

/**
 * Reads the state of a skeletal action given its handle.
 *
 * @param boneParent one of:<br><table><tr><td>{@link VR#EVRSkeletalTransformSpace_VRSkeletalTransformSpace_Action}</td></tr><tr><td>{@link VR#EVRSkeletalTransformSpace_VRSkeletalTransformSpace_Parent}</td></tr><tr><td>{@link VR#EVRSkeletalTransformSpace_VRSkeletalTransformSpace_Additive}</td></tr></table>
 */
fun IVRInput.getSkeletalActionData(action: VRActionHandle, boneParent: EVRSkeletalTransformSpace, predictedSecondsFromNow: Float, actionData: InputSkeletonActionData.Buffer, transformArray: VRBoneTransform.Buffer): EVRInputError {
    return EVRInputError of VRInput.nVRInput_GetSkeletalActionData(action, boneParent.i, predictedSecondsFromNow, actionData.adr, actionData.rem, transformArray.adr, transformArray.rem)
}

/**
 * Reads the state of a skeletal action given its handle in a compressed form that is suitable for sending over the network. The required buffer size will
 * never exceed ({@code sizeof(VR_BoneTransform_t)*boneCount + 2}). Usually the size will be much smaller.
 *
 * @param boneParent one of:<br><table><tr><td>{@link VR#EVRSkeletalTransformSpace_VRSkeletalTransformSpace_Action}</td></tr><tr><td>{@link VR#EVRSkeletalTransformSpace_VRSkeletalTransformSpace_Parent}</td></tr><tr><td>{@link VR#EVRSkeletalTransformSpace_VRSkeletalTransformSpace_Additive}</td></tr></table>
 * @param requiredCompressedSize ~ uint32 *
 */
fun IVRInput.getSkeletalActionDataCompressed(action: VRActionHandle, boneParent: EVRSkeletalTransformSpace, predictedSecondsFromNow: Float, compressedData: ByteBuffer?, requiredCompressedSize: IntBuffer?): EVRInputError {
    return EVRInputError of VRInput.nVRInput_GetSkeletalActionDataCompressed(action, boneParent.i, predictedSecondsFromNow, compressedData?.adr ?: NULL, compressedData?.rem ?: 0, requiredCompressedSize?.adr ?: NULL)
}

/** Turns a compressed buffer from {@link #VRInput_GetSkeletalActionDataCompressed GetSkeletalActionDataCompressed} and turns it back into a bone transform array.
 *  @param boneParent ~ EVRSkeletalTransformSpace *
 *  */
fun IVRInput.uncompressSkeletalActionData(compressedBuffer: ByteBuffer, boneParent: IntBuffer, transformArray: VRBoneTransform.Buffer): EVRInputError {
    return EVRInputError of VRInput.nVRInput_UncompressSkeletalActionData(compressedBuffer.adr, compressedBuffer.rem, boneParent.adr, transformArray.adr, transformArray.rem)
}

/** Triggers a haptic event as described by the specified action. */
fun IVRInput.triggerHapticVibrationAction(action: VRActionHandle, startSecondsFromNow: Float, durationSeconds: Float, frequency: Float, amplitude: Float): EVRInputError {
    return EVRInputError of VRInput.VRInput_TriggerHapticVibrationAction(action, startSecondsFromNow, durationSeconds, frequency, amplitude)
}

/** Retrieve origin handles for an action.
 *  @param originsOut ~ VRInputValueHandle *
 *  */
fun IVRInput.getActionOrigins(actionSetHandle: VRActionSetHandle, digitalActionHandle: VRActionHandle, originsOut: LongBuffer): EVRInputError {
    return EVRInputError of VRInput.nVRInput_GetActionOrigins(actionSetHandle, digitalActionHandle, originsOut.adr, originsOut.rem)
}

/** Retrieves the name of the origin in the current language. */
fun IVRInput.getOriginLocalizedName(origin: VRInputValueHandle, nameArray: ByteBuffer): EVRInputError {
    return EVRInputError of VRInput.nVRInput_GetOriginLocalizedName(origin, nameArray.adr, nameArray.rem)
}

/** Retrieves useful information for the origin of this action. */
fun IVRInput.getOriginTrackedDeviceInfo(origin: VRInputValueHandle, originInfo: InputOriginInfo): EVRInputError {
    return EVRInputError of VRInput.nVRInput_GetOriginTrackedDeviceInfo(origin, originInfo.adr, 1)
}

/** Retrieves useful information for the origin of this action. */
fun IVRInput.getOriginTrackedDeviceInfo(origin: VRInputValueHandle, originInfo: InputOriginInfo.Buffer): EVRInputError {
    return EVRInputError of VRInput.nVRInput_GetOriginTrackedDeviceInfo(origin, originInfo.adr, originInfo.rem)
}

/** Shows the current binding for the action in-headset. */
fun IVRInput.showActionOrigins(actionSetHandle: VRActionSetHandle, actionHandle: VRActionHandle): EVRInputError {
    return EVRInputError of VRInput.VRInput_ShowActionOrigins(actionSetHandle, actionHandle)
}

/** Shows the current binding all the actions in the specified action sets. */
fun IVRInput.showBindingsForActionSet(sets: VRActiveActionSet.Buffer, originToHighlight: VRInputValueHandle): EVRInputError {
    return EVRInputError of VRInput.nVRInput_ShowBindingsForActionSet(sets.adr, VRActiveActionSet.SIZEOF, sets.rem, originToHighlight)
}