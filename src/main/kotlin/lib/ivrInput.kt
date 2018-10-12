package lib

import glm_.set
import kool.adr
import kool.rem
import kool.stak
import org.lwjgl.openvr.*
import org.lwjgl.openvr.VRInput.*
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.system.MemoryUtil.memCallocInt
import java.io.File
import java.net.URL
import java.nio.ByteBuffer
import java.nio.IntBuffer

object vrInput : vrInterface {

    val maxActionNameLength = 64
    val maxActionSetNameLength = 64
    val maxActionOriginCount = 16

    enum class Error {
        None,
        NameNotFound,
        WrongType,
        InvalidHandle,
        InvalidParam,
        NoSteam,
        MaxCapacityReached,
        IPCError,
        NoActiveActionSet,
        InvalidDevice,
        InvalidSkeleton,
        InvalidBoneCount,
        InvalidCompressedData,
        NoData,
        BufferTooSmall,
        MismatchedActionManifest,
        MissingSkeletonData;

        val i = ordinal

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }

        fun check() {
            if (this != None)
                throw kotlin.Error(toString())
        }
    }

    val pError = memCallocInt(1)

    var error: Error
        get() = Error of pError[0]
        set(value) {
            pError[0] = value.i
        }

    enum class FilterCancelType { Timers, Momentum;

        val i = ordinal

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    /**
     * Sets the path to the action manifest JSON file that is used by this application. If this information was set on the Steam partner site, calls to this
     * function are ignored. If the Steam partner site setting and the path provided by this call are different,
     * {@link VR#EVRInputError_VRInputError_MismatchedActionManifest} is returned.
     *
     * <p>This call must be made before the first call to {@link #VRInput_UpdateActionState UpdateActionState} or {@link VRSystem#VRSystem_PollNextEvent PollNextEvent}.</p>
     */
    fun setActionManifestPath(actionManifestPath: URL): Error {
        val file = File(actionManifestPath.file)
        val pPath = addressOfAscii(file.absolutePath)
        return Error of nVRInput_SetActionManifestPath(pPath)
    }

    /**
     * JVM custom
     *
     * Returns a handle for an action set. This handle is used for all performance-sensitive calls.
     *
     * Note: Multi-thread unsafe if reading the error from the class property. */
    fun getActionSetHandle(actionSetName: String, pErr: VRInputErrorBuffer = pError): VRActionSetHandle =
            stak.longAddress {
                pErr[0] = nVRInput_GetActionSetHandle(addressOfAscii(actionSetName), it)
            }

    /** JVM custom
     *
     *  Returns a handle for an action. This handle is used for all performance-sensitive calls.
     *
     *  Note: Multi-thread unsafe if reading the error from the class property.     */
    fun getActionHandle(actionName: String, pErr: VRInputErrorBuffer = pError): VRActionHandle =
            stak.longAddress {
                pErr[0] = nVRInput_GetActionHandle(addressOfAscii(actionName), it)
            }

    /** JVM custom
     *
     * Returns a handle for any path in the input system. E.g. {@code /user/hand/right}.
     *
     * Note: Multi-thread unsafe if reading the error from the class property.     */
    fun getInputSourceHandle(inputSourcePath: String, pErr: VRInputErrorBuffer = pError): VRInputValueHandle =
            stak.longAddress {
                pErr[0] = nVRInput_GetInputSourceHandle(addressOfAscii(inputSourcePath), it)
            }

    /**
     * Reads the current state into all actions. After this call, the results of {@code Get*Action} calls will be the same until the next call to
     * {@code UpdateActionState}.
     */
    infix fun updateActionState(set: VRActiveActionSet): Error =
            Error of nVRInput_UpdateActionState(set.adr, VRActiveActionSet.SIZEOF, 1)

    /**
     * Reads the current state into all actions. After this call, the results of {@code Get*Action} calls will be the same until the next call to
     * {@code UpdateActionState}.
     */
    infix fun updateActionState(sets: VRActiveActionSet.Buffer): Error =
            Error of nVRInput_UpdateActionState(sets.adr, VRActiveActionSet.SIZEOF, sets.rem)

    /**
     * Reads the state of a digital action given its handle. This will return {@link VR#EVRInputError_VRInputError_WrongType} if the type of action is something other
     * than digital. TODO more convenient?
     */
    fun getDigitalActionData(action: VRActionHandle, actionData: InputDigitalActionData, restrictToDevice: VRInputValueHandle): Error =
            Error of nVRInput_GetDigitalActionData(action, actionData.adr, actionData.sizeof(), restrictToDevice)

    /**
     * Reads the state of a digital action given its handle. This will return {@link VR#EVRInputError_VRInputError_WrongType} if the type of action is something other
     * than digital. TODO more convenient?
     */
    fun getDigitalActionData(action: VRActionHandle, actionData: InputDigitalActionData.Buffer, restrictToDevice: VRInputValueHandle): Error =
            Error of nVRInput_GetDigitalActionData(action, actionData.adr, actionData.rem, restrictToDevice)

    /**
     * Reads the state of an analog action given its handle. This will return {@link VR#EVRInputError_VRInputError_WrongType} if the type of action is something other
     * than analog. TODO more convenient?
     */
    fun getAnalogActionData(action: VRActionHandle, actionData: InputAnalogActionData, restrictToDevice: VRInputValueHandle): Error =
            Error of nVRInput_GetAnalogActionData(action, actionData.adr, InputAnalogActionData.SIZEOF, restrictToDevice)

    /**
     * Reads the state of an analog action given its handle. This will return {@link VR#EVRInputError_VRInputError_WrongType} if the type of action is something other
     * than analog. TODO more convenient?
     */
    fun getAnalogActionData(action: VRActionHandle, actionData: InputAnalogActionData.Buffer, restrictToDevice: VRInputValueHandle): Error =
            Error of nVRInput_GetAnalogActionData(action, actionData.adr, actionData.rem, restrictToDevice)

    /**
     * Reads the state of a pose action given its handle. TODO more convenient?
     *
     * @param origin one of:<br><table><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseSeated}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseStanding}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseRawAndUncalibrated}</td></tr></table>
     */
    fun getPoseActionData(action: VRActionHandle, origin: TrackingUniverseOrigin, predictedSecondsFromNow: Float,
                          actionData: InputPoseActionData, restrictToDevice: VRInputValueHandle): Error =
            Error of nVRInput_GetPoseActionData(action, origin.i, predictedSecondsFromNow, actionData.adr, InputPoseActionData.SIZEOF, restrictToDevice)

    /**
     * Reads the state of a pose action given its handle. TODO more convenient?
     *
     * @param origin one of:<br><table><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseSeated}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseStanding}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseRawAndUncalibrated}</td></tr></table>
     */
    fun getPoseActionData(action: VRActionHandle, origin: TrackingUniverseOrigin, predictedSecondsFromNow: Float,
                          actionData: InputPoseActionData.Buffer, restrictToDevice: VRInputValueHandle): Error =
            Error of VRInput.nVRInput_GetPoseActionData(action, origin.i, predictedSecondsFromNow, actionData.adr, actionData.rem, restrictToDevice)

    /**
     * Reads the state of a skeletal action given its handle. TODO more convenient?
     */
    fun getSkeletalActionData(action: VRActionHandle, actionData: InputSkeletalActionData.Buffer, restrictToDevice: VRInputValueHandle): Error =
            Error of nVRInput_GetSkeletalActionData(action, actionData.adr, actionData.rem, restrictToDevice)

// --------------- Skeletal Bone Data ------------------- //

    /** Reads the state of the skeletal bone data associated with this action and copies it into the given buffer. */
    fun getSkeletalBoneData(action: VRActionHandle, transformSpace: VRSkeletalTransformSpace, motionRange: VRSkeletalMotionRange, transformArray: VRBoneTransform.Buffer, restrictToDevice: VRInputValueHandle): Error =
            Error of nVRInput_GetSkeletalBoneData(action, transformSpace.i, motionRange.i, transformArray.adr, transformArray.rem, restrictToDevice)

    /**
     * Reads the state of the skeletal bone data in a compressed form that is suitable for sending over the network. The required buffer size will never
     * exceed ({@code sizeof(VR_BoneTransform_t)*boneCount + 2}). Usually the size will be much smaller.
     */
    fun getSkeletalBoneDataCompressed(action: VRActionHandle, transformSpace: VRSkeletalTransformSpace, motionRange: VRSkeletalMotionRange, compressedData: ByteBuffer?, requiredCompressedSize: IntBuffer?, restrictToDevice: VRInputValueHandle): Error =
            Error of nVRInput_GetSkeletalBoneDataCompressed(action, transformSpace.i, motionRange.i, compressedData?.adr
                    ?: NULL, compressedData?.rem ?: 0, requiredCompressedSize?.adr ?: NULL, restrictToDevice)

    /** Turns a compressed buffer from GetSkeletalBoneDataCompressed and turns it back into a bone transform array. */
//    virtual EVRInputError DecompressSkeletalBoneData( void *pvCompressedBuffer, uint32_t unCompressedBufferSize, EVRSkeletalTransformSpace *peTransformSpace, VR_ARRAY_COUNT( unTransformArrayCount ) VRBoneTransform_t *pTransformArray, uint32_t unTransformArrayCount ) = 0

    /** Triggers a haptic event as described by the specified action. */
    fun triggerHapticVibrationAction(action: VRActionHandle, startSecondsFromNow: Float, durationSeconds: Float, frequency: Float,
                                     amplitude: Float, restrictToDevice: VRInputValueHandle): Error =
            Error of VRInput_TriggerHapticVibrationAction(action, startSecondsFromNow, durationSeconds, frequency, amplitude, restrictToDevice)

    /** Retrieve origin handles for an action. TODO more convenient? */
    fun getActionOrigins(actionSetHandle: VRActionSetHandle, digitalActionHandle: VRActionHandle, originsOut: VRInputValueHandleBuffer): Error =
            Error of nVRInput_GetActionOrigins(actionSetHandle, digitalActionHandle, originsOut.adr, originsOut.rem)

    /** Retrieves the name of the origin in the current language. TODO more convenient? */
    fun getOriginLocalizedName(origin: VRInputValueHandle, nameArray: ByteBuffer): Error =
            Error of nVRInput_GetOriginLocalizedName(origin, nameArray.adr, nameArray.rem)

    /** Retrieves useful information for the origin of this action. TODO more convenient? */
    fun getOriginTrackedDeviceInfo(origin: VRInputValueHandle, originInfo: InputOriginInfo): Error =
            Error of nVRInput_GetOriginTrackedDeviceInfo(origin, originInfo.adr, InputOriginInfo.SIZEOF)

    /** Retrieves useful information for the origin of this action. TODO more convenient? */
    fun getOriginTrackedDeviceInfo(origin: VRInputValueHandle, originInfo: InputOriginInfo.Buffer): Error =
            Error of nVRInput_GetOriginTrackedDeviceInfo(origin, originInfo.adr, originInfo.rem)

    /** Shows the current binding for the action in-headset. */
    fun showActionOrigins(actionSetHandle: VRActionSetHandle, actionHandle: VRActionHandle): Error =
            Error of VRInput_ShowActionOrigins(actionSetHandle, actionHandle)

    /** Shows the current binding all the actions in the specified action sets. */
    fun showBindingsForActionSet(sets: VRActiveActionSet.Buffer, originToHighlight: VRInputValueHandle): Error =
            Error of nVRInput_ShowBindingsForActionSet(sets.adr, VRActiveActionSet.SIZEOF, sets.rem, originToHighlight)

    override val version: String
        get() = "IVRInput_004"
}