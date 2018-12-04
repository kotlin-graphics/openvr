package lib

import glm_.set
import kool.adr
import kool.rem
import kool.stak
import org.lwjgl.openvr.*
import org.lwjgl.openvr.VRInput.*
import org.lwjgl.system.MemoryUtil.*
import java.io.File
import java.net.URL
import java.nio.ByteBuffer
import java.nio.IntBuffer

object vrInput : vrInterface {

    const val maxActionNameLength = 64
    const val maxActionSetNameLength = 64
    const val maxActionOriginCount = 16
    const val maxBoneNameLength = 16

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
        MissingSkeletonData,
        InvalidBoneIndex;

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

    enum class VRSkeletalTransformSpace { Model, Parent;

        @JvmField
        val i = ordinal

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    enum class VRSkeletalReferencePose { BindPose, OpenHand, Fist, GripLimit;

        @JvmField
        val i = ordinal

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    enum class VRFinger { Thumb, Index, Middle, Ring, Pinky;

        @JvmField
        val i = ordinal

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    enum class VRFingerSplay { Thumb_Index, Index_Middle, Middle_Ring, Ring_Pinky;

        @JvmField
        val i = ordinal

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    enum class VRSkeletalTrackingLevel {
        /** body part location can�t be directly determined by the device. Any skeletal pose provided by
         *  the device is estimated by assuming the position required to active buttons, triggers, joysticks,
         *  or other input sensors.
         *  E.g. Vive Controller, Gamepad */
        Estimated,
        /** body part location can be measured directly but with fewer degrees of freedom than the actual body
         *  part. Certain body part positions may be unmeasured by the device and estimated from other input data.
         *  E.g. Knuckles, gloves that only measure finger curl */
        Partial,
        /** Body part location can be measured directly throughout the entire range of motion of the body part.
         *  E.g. Mocap suit for the full body, gloves that measure rotation of each finger segment */
        Full;

        @JvmField
        val i = ordinal

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    enum class VRInputFilterCancelType { Timers, Momentum;

        @JvmField
        val i = ordinal

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    enum class VRInputStringBits(@JvmField val i: Int) {
        Hand(0x01),
        ControllerType(0x02),
        InputSource(0x04),
        All(-1);

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
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
            Error of nVRInput_GetDigitalActionData(action, actionData.adr, InputDigitalActionData.SIZEOF, restrictToDevice)

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
     * Reads the state of a pose action given its handle. TODO more convenient?
     *
     * @param origin one of:<br><table><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseSeated}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseStanding}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseRawAndUncalibrated}</td></tr></table>
     */
    fun getPoseActionData(action: VRActionHandle, origin: TrackingUniverseOrigin, predictedSecondsFromNow: Float, actionData: InputPoseActionData, restrictToDevice: VRInputValueHandle): Error =
            Error of nVRInput_GetPoseActionData(action, origin.i, predictedSecondsFromNow, actionData.adr, InputPoseActionData.SIZEOF, restrictToDevice)

    /**
     * Reads the state of a skeletal action given its handle. TODO more convenient?
     */
    fun getSkeletalActionData(action: VRActionHandle, actionData: InputSkeletalActionData.Buffer): Error =
            Error of nVRInput_GetSkeletalActionData(action, actionData.adr, InputSkeletalActionData.SIZEOF)

    // ---------------  Static Skeletal Data ------------------- //

    /** JVM custom
     *
     *  Reads the number of bones in skeleton associated with the given action
     *
     *  Note: Multi-thread unsafe if reading the error from the class property.*/
    @JvmOverloads
    fun getBoneCount(action: VRActionHandle, pErr: VRInputErrorBuffer = pError): Int =
            stak.intAddress {
                pErr[0] = nVRInput_GetBoneCount(action, it)
            }

    /** Fills the given array with the index of each bone's parent in the skeleton associated with the given action */
    fun getBoneHierarchy(action: VRActionHandle, parentIndices: IntBuffer): Error =
            Error of nVRInput_GetBoneHierarchy(action, parentIndices.adr, parentIndices.rem)

    /** JVM Custom
     *
     *  Fills the given buffer with the name of the bone at the given index in the skeleton associated with the given action
     *
     *  Note: Multi-thread unsafe if reading the error from the class property.*/
    @JvmOverloads
    fun getBoneName(action: VRActionHandle, boneIndex: BoneIndex, pErr: VRInputErrorBuffer = pError): String =
            stak {
                val s = it.nmalloc(1, maxBoneNameLength)
                pErr[0] = nVRInput_GetBoneName(action, boneIndex, s, maxBoneNameLength)
                memASCII(s)
            }

    /** Fills the given buffer with the transforms for a specific static skeletal reference pose */
    fun getSkeletalReferenceTransforms(action: VRActionHandle, transformSpace: VRSkeletalTransformSpace, referencePose: VRSkeletalReferencePose, transformArray: VRBoneTransform.Buffer): Error =
            Error of nVRInput_GetSkeletalReferenceTransforms(action, transformSpace.i, referencePose.i, transformArray.adr, transformArray.rem)

    /** JVM custom
     *
     *  Reads the level of accuracy to which the controller is able to track the user to recreate a skeletal pose
     *
     *  Note: Multi-thread unsafe if reading the error from the class property. */
    @JvmOverloads
    fun getSkeletalTrackingLevel(action: VRActionHandle, pErr: VRInputErrorBuffer = pError): VRSkeletalTrackingLevel =
            VRSkeletalTrackingLevel of stak.intAddress {
                pErr[0] = nVRInput_GetSkeletalTrackingLevel(action, it)
            }

    // ---------------  Dynamic Skeletal Data ------------------- //

    /** Reads the state of the skeletal bone data associated with this action and copies it into the given buffer. */
    fun getSkeletalBoneData(action: VRActionHandle, transformSpace: VRSkeletalTransformSpace, motionRange: VRSkeletalMotionRange, transformArray: VRBoneTransform.Buffer): Error =
            Error of nVRInput_GetSkeletalBoneData(action, transformSpace.i, motionRange.i, transformArray.adr, transformArray.rem)

    /** JVM custom
     *
     *  Reads summary information about the current pose of the skeleton associated with the given action.
     *
     *  Note: Multi-thread unsafe if reading the error from the class property. */
    @JvmOverloads
    fun getSkeletalSummaryData(action: VRActionHandle, skeletalSummaryData: VRSkeletalSummaryData = vr.VRSkeletalSummaryData(), pErr: VRInputErrorBuffer = pError): VRSkeletalSummaryData {
        pErr[0] = nVRInput_GetSkeletalSummaryData(action, skeletalSummaryData.adr)
        return skeletalSummaryData
    }

    /**
     * Reads the state of the skeletal bone data in a compressed form that is suitable for sending over the network. The required buffer size will never
     * exceed ({@code sizeof(VR_BoneTransform_t)*boneCount + 2}). Usually the size will be much smaller.
     */
    fun getSkeletalBoneDataCompressed(action: VRActionHandle, motionRange: VRSkeletalMotionRange, compressedData: ByteBuffer?, requiredCompressedSize: IntBuffer?): Error =
            Error of nVRInput_GetSkeletalBoneDataCompressed(action, motionRange.i, compressedData?.adr
                    ?: NULL, compressedData?.rem ?: 0, requiredCompressedSize?.adr ?: NULL)

    /** Turns a compressed buffer from GetSkeletalBoneDataCompressed and turns it back into a bone transform array. */
    fun decompressSkeletalBoneData(compressedBuffer: ByteBuffer, transformSpace: VRSkeletalTransformSpace, transformArray: VRBoneTransform.Buffer): Error =
            Error of nVRInput_DecompressSkeletalBoneData(compressedBuffer.adr, compressedBuffer.rem, transformSpace.i, transformArray.adr, transformArray.rem)

    /** Triggers a haptic event as described by the specified action. */
    fun triggerHapticVibrationAction(action: VRActionHandle, startSecondsFromNow: Float, durationSeconds: Float, frequency: Float,
                                     amplitude: Float, restrictToDevice: VRInputValueHandle): Error =
            Error of VRInput_TriggerHapticVibrationAction(action, startSecondsFromNow, durationSeconds, frequency, amplitude, restrictToDevice)

    /** Retrieve origin handles for an action. TODO more convenient? */
    fun getActionOrigins(actionSetHandle: VRActionSetHandle, digitalActionHandle: VRActionHandle, originsOut: VRInputValueHandleBuffer): Error =
            Error of nVRInput_GetActionOrigins(actionSetHandle, digitalActionHandle, originsOut.adr, originsOut.rem)

    /** JVM custom
     *
     *  Retrieves the name of the origin in the current language. unStringSectionsToInclude is a bitfield of values in
     *  EVRInputStringBits that allows the application to specify which parts of the origin's information it wants a string for.
     *
     *  Note: Multi-thread unsafe if reading the error from the class property. */
    @JvmOverloads
    fun getOriginLocalizedName(origin: VRInputValueHandle, stringSectionsToInclude: Int, pErr: VRInputErrorBuffer = pError): String =
            stak {
                val s = it.nmalloc(1, 64)
                pErr[0] = nVRInput_GetOriginLocalizedName(origin, s, 64, stringSectionsToInclude)
                memASCII(s)
            }

    /** Retrieves useful information for the origin of this action. TODO more convenient? */
    fun getOriginTrackedDeviceInfo(origin: VRInputValueHandle, originInfo: InputOriginInfo): Error =
            Error of nVRInput_GetOriginTrackedDeviceInfo(origin, originInfo.adr, InputOriginInfo.SIZEOF)

    /** Shows the current binding for the action in-headset. */
    fun showActionOrigins(actionSetHandle: VRActionSetHandle, actionHandle: VRActionHandle): Error =
            Error of VRInput_ShowActionOrigins(actionSetHandle, actionHandle)

    /** Shows the current binding all the actions in the specified action sets. */
    fun showBindingsForActionSet(sets: VRActiveActionSet.Buffer, originToHighlight: VRInputValueHandle): Error =
            Error of nVRInput_ShowBindingsForActionSet(sets.adr, VRActiveActionSet.SIZEOF, sets.rem, originToHighlight)

    override val version: String
        get() = "IVRInput_005"
}