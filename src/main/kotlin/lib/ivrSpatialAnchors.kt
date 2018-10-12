package lib

import glm_.set
import org.lwjgl.openvr.SpatialAnchorPose
import org.lwjgl.openvr.VRSpatialAnchors.*
import org.lwjgl.system.MemoryStack.stackGet
import org.lwjgl.system.MemoryUtil.memASCII
import org.lwjgl.system.MemoryUtil.memCallocInt

object vrSpatialAnchors : vrInterface {

    const val invalidHandle = 0

    const val maxDescriptorSize = 32

    enum class Error {
        Success,
        Internal,
        UnknownHandle,
        ArrayTooSmall,
        InvalidDescriptorChar,
        NotYetAvailable,
        NotAvailableInThisUniverse,
        PermanentlyUnavailable,
        WrongDriver,
        DescriptorTooLong,
        Unknown,
        NoRoomCalibration,
        InvalidArgument,
        UnknownDriver;

        @JvmField
        val i = ordinal

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    val pError = memCallocInt(1)

    var error: Error
        get() = Error of pError[0]
        set(value) {
            pError[0] = value.i
        }

    /**
     * JVM custom
     *
     * Returns a handle for an spatial anchor described by "descriptor".  On success, {@code pHandle} will contain a handle valid for this session. Caller can
     * wait for an event or occasionally poll {@link #VRSpatialAnchors_GetSpatialAnchorPose GetSpatialAnchorPose} to find the virtual coordinate associated with this anchor.
     *
     * Note: Multi-thread unsafe if not passing a pError and reading it from the class property.
     */
    @JvmOverloads
    fun createSpatialAnchorFromDescriptor(descriptor: CharSequence, pErr: VRSpatialAnchorErrorBuffer = pError): SpatialAnchorHandle =
            intAddress { pHandleOut ->
                pErr[0] = nVRSpatialAnchors_CreateSpatialAnchorFromDescriptor(addressOfAscii(descriptor), pHandleOut)
            }

    /**
     * JVM custom
     *
     * Returns a handle for an new spatial anchor at {@code pose}.
     *
     * <p>On success, {@code pHandle} will contain a handle valid for this session. Caller can wait for an event or occasionally poll
     * {@link #VRSpatialAnchors_GetSpatialAnchorDescriptor GetSpatialAnchorDescriptor} to find the permanent descriptor for this pose. The result of {@link #VRSpatialAnchors_GetSpatialAnchorPose GetSpatialAnchorPose} may evolve from this initial
     * position if the driver chooses to update it. The anchor will be associated with the driver that provides {@code deviceIndex}, and the driver may use
     * that specific device as a hint for how to best create the anchor. The {@code origin} must match whatever tracking origin you are working in
     * (seated/standing/raw).</p>
     *
     * <p>This should be called when the user is close to (and ideally looking at/interacting with) the target physical location. At that moment, the driver will
     * have the most information about how to recover that physical point in the future, and the quality of the anchor (when the descriptor is re-used) will
     * be highest. The caller may decide to apply offsets from this initial pose, but is advised to stay relatively close to the original pose location for
     * highest fidelity.</p>
     *
     * Note: Multi-thread unsafe if not passing a pError and reading it from the class property.
     */
    @JvmOverloads
    fun createSpatialAnchorFromPose(deviceIndex: TrackedDeviceIndex, origin: TrackingUniverseOrigin, pose: SpatialAnchorPose, pErr: VRSpatialAnchorErrorBuffer = pError): SpatialAnchorHandle =
            intAddress { pHandleOut ->
                pErr[0] = nVRSpatialAnchors_CreateSpatialAnchorFromPose(deviceIndex, origin.i, pose.adr, pHandleOut)
            }

    /**
     * Get the pose for a given handle. TODO more convenient?
     *
     * <p>This is intended to be cheap enough to call every frame (or fairly often) so that the driver can refine this position when it has more information
     * available.</p>
     */
    fun getSpatialAnchorPose(handle: SpatialAnchorHandle, origin: TrackingUniverseOrigin, poseOut: SpatialAnchorPose): Error =
            Error of nVRSpatialAnchors_GetSpatialAnchorPose(handle, origin.i, poseOut.adr)

    /**
     * JVM custom
     *
     * Get the descriptor for a given handle.
     *
     * <p>This will be empty for handles where the driver has not yet built a descriptor. It will be the application-supplied descriptor for previously saved
     * anchors that the application is requesting poses for.  If the driver has called {@code UpdateSpatialAnchorDescriptor()} already in this session, it
     * will be the descriptor provided by the driver.</p>
     *
     * Note: Multi-thread unsafe if not passing a pError and reading it from the class property.
     */
    @JvmOverloads
    fun getSpatialAnchorDescriptor(handle: SpatialAnchorHandle, pErr: VRSpatialAnchorErrorBuffer = pError): String {
        val pDescr = stackGet().nmalloc(1, maxDescriptorSize)
        val pDescrSize = pIntOf(maxDescriptorSize)
        pErr[0] = nVRSpatialAnchors_GetSpatialAnchorDescriptor(handle, pDescr, pDescrSize)
        return memASCII(pDescr)
    }

    override val version: String
        get() = "IVRSpatialAnchors_001"
}