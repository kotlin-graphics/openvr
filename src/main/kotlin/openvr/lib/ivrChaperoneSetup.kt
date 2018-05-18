package openvr.lib

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.ByteByReference
import com.sun.jna.ptr.FloatByReference
import com.sun.jna.ptr.IntByReference
import glm_.vec2.Vec2
import java.util.*

// ivrchaperonesetup.h ============================================================================================================================================

enum class EChaperoneConfigFile(@JvmField val i: Int) {
    /** The live chaperone config, used by most applications and games  */
    Live(1),
    /** The temporary chaperone config, used to live-preview collision bounds in room setup */
    Temp(2);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EChaperoneImportFlags(@JvmField val i: Int) {

    BoundsOnly(0x0001);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Manages the working copy of the chaperone info. By default this will be the same as the live copy. Any changes made with this interface will stay in the
 *  working copy until CommitWorkingCopy() is called, at which point the working copy and the live copy will be the same again. */
open class IVRChaperoneSetup : Structure {

    /** Saves the current working copy to disk */
    infix fun commitWorkingCopy(configFile: EChaperoneConfigFile) = CommitWorkingCopy!!(configFile.i)

    @JvmField var CommitWorkingCopy: CommitWorkingCopy_callback? = null

    interface CommitWorkingCopy_callback : Callback {
        operator fun invoke(configFile: Int): Boolean
    }

    /** Reverts the working copy to match the live chaperone calibration.
     *  To modify existing data this MUST be do WHILE getting a non-error ChaperoneCalibrationStatus.
     *  Only after this should you do gets and sets on the existing data. */
    fun revertWorkingCopy() = RevertWorkingCopy!!()

    @JvmField var RevertWorkingCopy: RevertWorkingCopy_callback? = null

    interface RevertWorkingCopy_callback : Callback {
        operator fun invoke()
    }

    /** Returns the width and depth of the Play Area (formerly named Soft Bounds) in X and Z from the working copy.
     *  Tracking space center (0,0,0) is the center of the Play Area. */
    infix fun getWorkingPlayAreaSize(size: Vec2): Boolean {
        val sizeX = FloatByReference()
        val sizeZ = FloatByReference ()
        return GetWorkingPlayAreaSize!!(sizeX, sizeZ).also { size.put(sizeX.value, sizeZ.value) }
    }

    @JvmField var GetWorkingPlayAreaSize: GetWorkingPlayAreaSize_callback? = null

    interface GetWorkingPlayAreaSize_callback : Callback {
        operator fun invoke(pSizeX: FloatByReference, pSizeZ: FloatByReference): Boolean
    }
    /** Returns the 4 corner positions of the Play Area (formerly named Soft Bounds) from the working copy.
     *  Corners are in clockwise order.
     *  Tracking space center (0,0,0) is the center of the Play Area.
     *  It's a rectangle.
     *  2 sides are parallel to the X axis and 2 sides are parallel to the Z axis.
     *  Height of every corner is 0Y (on the floor). **/
    infix fun getWorkingPlayAreaRect(rect: HmdQuad.ByReference) = GetWorkingPlayAreaRect!!(rect)

    @JvmField var GetWorkingPlayAreaRect: GetWorkingPlayAreaRect_callback? = null

    interface GetWorkingPlayAreaRect_callback : Callback {
        operator fun invoke(rect: HmdQuad.ByReference): Boolean
    }

    /** Returns the number of Quads if the buffer points to null. Otherwise it returns Quads
     * into the buffer up to the max specified from the working copy. */
    fun getWorkingCollisionBoundsInfo(quadsBuffer: HmdQuad.ByReference, quadsCount: IntByReference) = GetWorkingCollisionBoundsInfo!!(quadsBuffer, quadsCount)

    @JvmField var GetWorkingCollisionBoundsInfo: GetWorkingCollisionBoundsInfo_callback? = null

    interface GetWorkingCollisionBoundsInfo_callback : Callback {
        operator fun invoke(pQuadsBuffer: HmdQuad.ByReference, punQuadsCount: IntByReference): Boolean
    }

    /** Returns the number of Quads if the buffer points to null. Otherwise it returns Quads into the buffer up to the max specified. */
    fun getLiveCollisionBoundsInfo(quadsBuffer: HmdQuad.ByReference, quadsCount: IntByReference) = GetLiveCollisionBoundsInfo!!(quadsBuffer, quadsCount)

    @JvmField var GetLiveCollisionBoundsInfo: GetLiveCollisionBoundsInfo_callback? = null

    interface GetLiveCollisionBoundsInfo_callback : Callback {
        operator fun invoke(pQuadsBuffer: HmdQuad.ByReference, punQuadsCount: IntByReference): Boolean
    }

    /** Returns the preferred seated position from the working copy. */
    infix fun getWorkingSeatedZeroPoseToRawTrackingPose(matSeatedZeroPoseToRawTrackingPose: HmdMat34.ByReference) = GetWorkingSeatedZeroPoseToRawTrackingPose!!(matSeatedZeroPoseToRawTrackingPose)

    @JvmField var GetWorkingSeatedZeroPoseToRawTrackingPose: GetWorkingSeatedZeroPoseToRawTrackingPose_callback? = null

    interface GetWorkingSeatedZeroPoseToRawTrackingPose_callback : Callback {
        operator fun invoke(pmatSeatedZeroPoseToRawTrackingPose: HmdMat34.ByReference): Boolean
    }

    /** Returns the standing origin from the working copy. */
    infix fun getWorkingStandingZeroPoseToRawTrackingPose(matStandingZeroPoseToRawTrackingPose: HmdMat34.ByReference) = GetWorkingStandingZeroPoseToRawTrackingPose!!(matStandingZeroPoseToRawTrackingPose)

    @JvmField var GetWorkingStandingZeroPoseToRawTrackingPose: GetWorkingStandingZeroPoseToRawTrackingPose_callback? = null

    interface GetWorkingStandingZeroPoseToRawTrackingPose_callback : Callback {
        operator fun invoke(pmatStandingZeroPoseToRawTrackingPose: HmdMat34.ByReference): Boolean
    }

    /** Sets the Play Area in the working copy. */
    infix fun setWorkingPlayAreaSize(size: Vec2) = SetWorkingPlayAreaSize!!(size.x, size.y)

    @JvmField var SetWorkingPlayAreaSize: SetWorkingPlayAreaSize_callback? = null

    interface SetWorkingPlayAreaSize_callback : Callback {
        operator fun invoke(sizeX: Float, sizeZ: Float)
    }

    /** Sets the Collision Bounds in the working copy. */
    fun setWorkingCollisionBoundsInfo(quadsBuffer: HmdQuad.ByReference, quadsCount: Int) = SetWorkingCollisionBoundsInfo!!(quadsBuffer, quadsCount)

    @JvmField var SetWorkingCollisionBoundsInfo: SetWorkingCollisionBoundsInfo_callback? = null

    interface SetWorkingCollisionBoundsInfo_callback : Callback {
        operator fun invoke(pQuadsBuffer: HmdQuad.ByReference, unQuadsCount: Int)
    }

    /** Sets the preferred seated position in the working copy. */
    infix fun setWorkingSeatedZeroPoseToRawTrackingPose(matSeatedZeroPoseToRawTrackingPose: HmdMat34.ByReference) = SetWorkingSeatedZeroPoseToRawTrackingPose!!(matSeatedZeroPoseToRawTrackingPose)

    @JvmField var SetWorkingSeatedZeroPoseToRawTrackingPose: SetWorkingSeatedZeroPoseToRawTrackingPose_callback? = null

    interface SetWorkingSeatedZeroPoseToRawTrackingPose_callback : Callback {
        operator fun invoke(pMatSeatedZeroPoseToRawTrackingPose: HmdMat34.ByReference)
    }

    /** Sets the preferred standing position in the working copy. */
    infix fun setWorkingStandingZeroPoseToRawTrackingPose(matStandingZeroPoseToRawTrackingPose: HmdMat34.ByReference) = SetWorkingStandingZeroPoseToRawTrackingPose!!(matStandingZeroPoseToRawTrackingPose)

    @JvmField var SetWorkingStandingZeroPoseToRawTrackingPose: SetWorkingStandingZeroPoseToRawTrackingPose_callback? = null

    interface SetWorkingStandingZeroPoseToRawTrackingPose_callback : Callback {
        operator fun invoke(pMatStandingZeroPoseToRawTrackingPose: HmdMat34.ByReference)
    }

    /** Tear everything down and reload it from the file on disk */
    infix fun reloadFromDisk(configFile: EChaperoneConfigFile) = ReloadFromDisk!!(configFile.i)

    @JvmField var ReloadFromDisk: ReloadFromDisk_callback? = null

    interface ReloadFromDisk_callback : Callback {
        operator fun invoke(configFile: Int)
    }

    /** Returns the preferred seated position. */
    infix fun getLiveSeatedZeroPoseToRawTrackingPose(matSeatedZeroPoseToRawTrackingPose: HmdMat34.ByReference) = GetLiveSeatedZeroPoseToRawTrackingPose!!(matSeatedZeroPoseToRawTrackingPose)

    @JvmField var GetLiveSeatedZeroPoseToRawTrackingPose: GetLiveSeatedZeroPoseToRawTrackingPose_callback? = null

    interface GetLiveSeatedZeroPoseToRawTrackingPose_callback : Callback {
        operator fun invoke(pmatSeatedZeroPoseToRawTrackingPose: HmdMat34.ByReference): Boolean
    }


    fun setWorkingCollisionBoundsTagsInfo(tagsBuffer: ByteByReference, tagCount: Int) = SetWorkingCollisionBoundsTagsInfo!!(tagsBuffer, tagCount)

    @JvmField var SetWorkingCollisionBoundsTagsInfo: SetWorkingCollisionBoundsTagsInfo_callback? = null

    interface SetWorkingCollisionBoundsTagsInfo_callback : Callback {
        operator fun invoke(pTagsBuffer: ByteByReference, unTagCount: Int)
    }


    fun getLiveCollisionBoundsTagsInfo(tagsBuffer: ByteByReference, tagCount: IntByReference) = GetLiveCollisionBoundsTagsInfo!!(tagsBuffer, tagCount)

    @JvmField var GetLiveCollisionBoundsTagsInfo: GetLiveCollisionBoundsTagsInfo_callback? = null

    interface GetLiveCollisionBoundsTagsInfo_callback : Callback {
        operator fun invoke(pTagsBuffer: ByteByReference, punTagCount: IntByReference): Boolean
    }


    fun setWorkingPhysicalBoundsInfo(quadsBuffer: HmdQuad.ByReference, quadsCount: Int) = SetWorkingPhysicalBoundsInfo!!(quadsBuffer, quadsCount)
    @JvmField var SetWorkingPhysicalBoundsInfo: SetWorkingPhysicalBoundsInfo_callback? = null

    interface SetWorkingPhysicalBoundsInfo_callback : Callback {
        operator fun invoke(pQuadsBuffer: HmdQuad.ByReference, unQuadsCount: Int): Boolean
    }


    fun getLivePhysicalBoundsInfo(quadsBuffer: HmdQuad.ByReference, quadsCount: IntByReference) = GetLivePhysicalBoundsInfo!!(quadsBuffer, quadsCount)

    @JvmField var GetLivePhysicalBoundsInfo: GetLivePhysicalBoundsInfo_callback? = null

    interface GetLivePhysicalBoundsInfo_callback : Callback {
        operator fun invoke(pQuadsBuffer: HmdQuad.ByReference, punQuadsCount: IntByReference): Boolean
    }


    fun exportLiveToBuffer(buffer: String, bufferLength: IntByReference) = ExportLiveToBuffer!!(buffer, bufferLength)
    @JvmField var ExportLiveToBuffer: ExportLiveToBuffer_callback? = null

    interface ExportLiveToBuffer_callback : Callback {
        operator fun invoke(pBuffer: String, pnBufferLength: IntByReference): Boolean
    }


    fun importFromBufferToWorking(buffer: String, importFlags: Int) = ImportFromBufferToWorking!!(buffer, importFlags)
    @JvmField var ImportFromBufferToWorking: ImportFromBufferToWorking_callback? = null

    interface ImportFromBufferToWorking_callback : Callback {
        operator fun invoke(pBuffer: String, nImportFlags: Int): Boolean
    }


    constructor()

    override fun getFieldOrder() = listOf("CommitWorkingCopy", "RevertWorkingCopy", "GetWorkingPlayAreaSize", "GetWorkingPlayAreaRect",
            "GetWorkingCollisionBoundsInfo", "GetLiveCollisionBoundsInfo", "GetWorkingSeatedZeroPoseToRawTrackingPose",
            "GetWorkingStandingZeroPoseToRawTrackingPose", "SetWorkingPlayAreaSize", "SetWorkingCollisionBoundsInfo", "SetWorkingSeatedZeroPoseToRawTrackingPose",
            "SetWorkingStandingZeroPoseToRawTrackingPose", "ReloadFromDisk", "GetLiveSeatedZeroPoseToRawTrackingPose", "SetWorkingCollisionBoundsTagsInfo",
            "GetLiveCollisionBoundsTagsInfo", "SetWorkingPhysicalBoundsInfo", "GetLivePhysicalBoundsInfo", "ExportLiveToBuffer", "ImportFromBufferToWorking")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRChaperoneSetup(), Structure.ByReference
    class ByValue : IVRChaperoneSetup(), Structure.ByValue
}

val IVRChaperoneSetup_Version = "IVRChaperoneSetup_005"