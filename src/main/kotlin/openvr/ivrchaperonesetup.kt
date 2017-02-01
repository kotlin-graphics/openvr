import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.ByteByReference
import com.sun.jna.ptr.FloatByReference
import com.sun.jna.ptr.IntByReference
import java.util.*

// ivrchaperonesetup.h ============================================================================================================================================

enum class EChaperoneConfigFile(@JvmField val i: Int) {

    Live(1), //    The live chaperone config, used by most applications and games
    Temp(2);    //  The temporary chaperone config, used to live-preview collision bounds in room setup

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EChaperoneImportFlags(@JvmField val i: Int) {

    BoundsOnly(0x0001);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Manages the working copy of the chaperone info. By default this will be the same as the live copy. Any changes made with this interface will stay in the
 *  working copy until CommitWorkingCopy() is called, at which point the working copy and the live copy will be the same again. */
open class IVRChaperoneSetup : Structure {

    /** Saves the current working copy to disk */
    fun commitWorkingCopy(configFile: EChaperoneConfigFile) = CommitWorkingCopy!!.invoke(configFile.i)

    @JvmField var CommitWorkingCopy: CommitWorkingCopy_callback? = null

    interface CommitWorkingCopy_callback : Callback {
        fun invoke(configFile: Int): Boolean
    }

    /** Reverts the working copy to match the live chaperone calibration.
     *  To modify existing data this MUST be do WHILE getting a non-error ChaperoneCalibrationStatus.
     *  Only after this should you do gets and sets on the existing data. */
    fun revertWorkingCopy() = RevertWorkingCopy!!.invoke()

    @JvmField var RevertWorkingCopy: RevertWorkingCopy_callback? = null

    interface RevertWorkingCopy_callback : Callback {
        fun invoke()
    }

    /** Returns the width and depth of the Play Area (formerly named Soft Bounds) in X and Z from the working copy.
     *  Tracking space center (0,0,0) is the center of the Play Area. */
    fun getWorkingPlayAreaSize(pSizeX: FloatByReference, pSizeZ: FloatByReference) = GetWorkingPlayAreaSize!!.invoke(pSizeX, pSizeZ)

    @JvmField var GetWorkingPlayAreaSize: GetWorkingPlayAreaSize_callback? = null

    interface GetWorkingPlayAreaSize_callback : Callback {
        fun invoke(pSizeX: FloatByReference, pSizeZ: FloatByReference): Boolean
    }

    /** Returns the 4 corner positions of the Play Area (formerly named Soft Bounds) from the working copy.
     *  Corners are in clockwise order.
     *  Tracking space center (0,0,0) is the center of the Play Area.
     *  It's a rectangle.
     *  2 sides are parallel to the X axis and 2 sides are parallel to the Z axis.
     *  Height of every corner is 0Y (on the floor). **/
    fun getWorkingPlayAreaRect(rect: HmdQuad_t.ByReference) = GetWorkingPlayAreaRect!!.invoke(rect)

    @JvmField var GetWorkingPlayAreaRect: GetWorkingPlayAreaRect_callback? = null

    interface GetWorkingPlayAreaRect_callback : Callback {
        fun invoke(rect: HmdQuad_t.ByReference): Boolean
    }

    /** Returns the number of Quads if the buffer points to null. Otherwise it returns Quads
     * into the buffer up to the max specified from the working copy. */
    fun getWorkingCollisionBoundsInfo(pQuadsBuffer: HmdQuad_t.ByReference, punQuadsCount: IntByReference)
            = GetWorkingCollisionBoundsInfo!!.invoke(pQuadsBuffer, punQuadsCount)

    @JvmField var GetWorkingCollisionBoundsInfo: GetWorkingCollisionBoundsInfo_callback? = null

    interface GetWorkingCollisionBoundsInfo_callback : Callback {
        fun invoke(pQuadsBuffer: HmdQuad_t.ByReference, punQuadsCount: IntByReference): Boolean
    }

    /** Returns the number of Quads if the buffer points to null. Otherwise it returns Quads into the buffer up to the max specified. */
    fun getLiveCollisionBoundsInfo(pQuadsBuffer: HmdQuad_t.ByReference, punQuadsCount: IntByReference)
            = GetLiveCollisionBoundsInfo!!.invoke(pQuadsBuffer, punQuadsCount)

    @JvmField var GetLiveCollisionBoundsInfo: GetLiveCollisionBoundsInfo_callback? = null

    interface GetLiveCollisionBoundsInfo_callback : Callback {
        fun invoke(pQuadsBuffer: HmdQuad_t.ByReference, punQuadsCount: IntByReference): Boolean
    }

    /** Returns the preferred seated position from the working copy. */
    fun getWorkingSeatedZeroPoseToRawTrackingPose(pmatSeatedZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference)
            = GetWorkingSeatedZeroPoseToRawTrackingPose!!.invoke(pmatSeatedZeroPoseToRawTrackingPose)

    @JvmField var GetWorkingSeatedZeroPoseToRawTrackingPose: GetWorkingSeatedZeroPoseToRawTrackingPose_callback? = null

    interface GetWorkingSeatedZeroPoseToRawTrackingPose_callback : Callback {
        fun invoke(pmatSeatedZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference): Boolean
    }

    /** Returns the standing origin from the working copy. */
    fun getWorkingStandingZeroPoseToRawTrackingPose(pmatStandingZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference)
            = GetWorkingStandingZeroPoseToRawTrackingPose!!.invoke(pmatStandingZeroPoseToRawTrackingPose)

    @JvmField var GetWorkingStandingZeroPoseToRawTrackingPose: GetWorkingStandingZeroPoseToRawTrackingPose_callback? = null

    interface GetWorkingStandingZeroPoseToRawTrackingPose_callback : Callback {
        fun invoke(pmatStandingZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference): Boolean
    }

    /** Sets the Play Area in the working copy. */
    fun setWorkingPlayAreaSize(sizeX: Float, sizeZ: Float) = SetWorkingPlayAreaSize!!.invoke(sizeX, sizeZ)

    @JvmField var SetWorkingPlayAreaSize: SetWorkingPlayAreaSize_callback? = null

    interface SetWorkingPlayAreaSize_callback : Callback {
        fun invoke(sizeX: Float, sizeZ: Float)
    }

    /** Sets the Collision Bounds in the working copy. */
    fun setWorkingCollisionBoundsInfo(pQuadsBuffer: HmdQuad_t.ByReference, unQuadsCount: Int) = SetWorkingCollisionBoundsInfo!!.invoke(pQuadsBuffer, unQuadsCount)

    @JvmField var SetWorkingCollisionBoundsInfo: SetWorkingCollisionBoundsInfo_callback? = null

    interface SetWorkingCollisionBoundsInfo_callback : Callback {
        fun invoke(pQuadsBuffer: HmdQuad_t.ByReference, unQuadsCount: Int)
    }

    /** Sets the preferred seated position in the working copy. */
    fun setWorkingSeatedZeroPoseToRawTrackingPose(pmatSeatedZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference)
            = SetWorkingSeatedZeroPoseToRawTrackingPose!!.invoke(pmatSeatedZeroPoseToRawTrackingPose)

    @JvmField var SetWorkingSeatedZeroPoseToRawTrackingPose: SetWorkingSeatedZeroPoseToRawTrackingPose_callback? = null

    interface SetWorkingSeatedZeroPoseToRawTrackingPose_callback : Callback {
        fun invoke(pMatSeatedZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference)
    }

    /** Sets the preferred standing position in the working copy. */
    fun setWorkingStandingZeroPoseToRawTrackingPose(pmatStandingZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference)
            = SetWorkingStandingZeroPoseToRawTrackingPose!!.invoke(pmatStandingZeroPoseToRawTrackingPose)

    @JvmField var SetWorkingStandingZeroPoseToRawTrackingPose: SetWorkingStandingZeroPoseToRawTrackingPose_callback? = null

    interface SetWorkingStandingZeroPoseToRawTrackingPose_callback : Callback {
        fun invoke(pMatStandingZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference)
    }

    /** Tear everything down and reload it from the file on disk */
    fun reloadFromDisk(configFile: EChaperoneConfigFile) = ReloadFromDisk!!.invoke(configFile.i)

    @JvmField var ReloadFromDisk: ReloadFromDisk_callback? = null

    interface ReloadFromDisk_callback : Callback {
        fun invoke(configFile: Int)
    }

    /** Returns the preferred seated position. */
    fun getLiveSeatedZeroPoseToRawTrackingPose(pmatSeatedZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference)
            = GetLiveSeatedZeroPoseToRawTrackingPose!!.invoke(pmatSeatedZeroPoseToRawTrackingPose)

    @JvmField var GetLiveSeatedZeroPoseToRawTrackingPose: GetLiveSeatedZeroPoseToRawTrackingPose_callback? = null

    interface GetLiveSeatedZeroPoseToRawTrackingPose_callback : Callback {
        fun invoke(pmatSeatedZeroPoseToRawTrackingPose: HmdMatrix34_t.ByReference): Boolean
    }


    fun setWorkingCollisionBoundsTagsInfo(pTagsBuffer: ByteByReference, unTagCount: Int) =
            SetWorkingCollisionBoundsTagsInfo!!.invoke(pTagsBuffer, unTagCount)

    @JvmField var SetWorkingCollisionBoundsTagsInfo: SetWorkingCollisionBoundsTagsInfo_callback? = null

    interface SetWorkingCollisionBoundsTagsInfo_callback : Callback {
        fun invoke(pTagsBuffer: ByteByReference, unTagCount: Int)
    }


    fun getLiveCollisionBoundsTagsInfo(pTagsBuffer: ByteByReference, punTagCount: IntByReference)
            = GetLiveCollisionBoundsTagsInfo!!.invoke(pTagsBuffer, punTagCount)

    @JvmField var GetLiveCollisionBoundsTagsInfo: GetLiveCollisionBoundsTagsInfo_callback? = null

    interface GetLiveCollisionBoundsTagsInfo_callback : Callback {
        fun invoke(pTagsBuffer: ByteByReference, punTagCount: IntByReference): Boolean
    }


    fun setWorkingPhysicalBoundsInfo(pQuadsBuffer: HmdQuad_t.ByReference, unQuadsCount: Int) = SetWorkingPhysicalBoundsInfo!!.invoke(pQuadsBuffer, unQuadsCount)
    @JvmField var SetWorkingPhysicalBoundsInfo: SetWorkingPhysicalBoundsInfo_callback? = null

    interface SetWorkingPhysicalBoundsInfo_callback : Callback {
        fun invoke(pQuadsBuffer: HmdQuad_t.ByReference, unQuadsCount: Int): Boolean
    }


    fun getLivePhysicalBoundsInfo(pQuadsBuffer: HmdQuad_t.ByReference, punQuadsCount: IntByReference)
            = GetLivePhysicalBoundsInfo!!.invoke(pQuadsBuffer, punQuadsCount)

    @JvmField var GetLivePhysicalBoundsInfo: GetLivePhysicalBoundsInfo_callback? = null

    interface GetLivePhysicalBoundsInfo_callback : Callback {
        fun invoke(pQuadsBuffer: HmdQuad_t.ByReference, punQuadsCount: IntByReference): Boolean
    }


    fun exportLiveToBuffer(pBuffer: String, pnBufferLength: IntByReference) = ExportLiveToBuffer!!.invoke(pBuffer, pnBufferLength)
    @JvmField var ExportLiveToBuffer: ExportLiveToBuffer_callback? = null

    interface ExportLiveToBuffer_callback : Callback {
        fun invoke(pBuffer: String, pnBufferLength: IntByReference): Boolean
    }


    fun importFromBufferToWorking(pBuffer: String, nImportFlags: Int) = ImportFromBufferToWorking!!.invoke(pBuffer, nImportFlags)
    @JvmField var ImportFromBufferToWorking: ImportFromBufferToWorking_callback? = null

    interface ImportFromBufferToWorking_callback : Callback {
        fun invoke(pBuffer: String, nImportFlags: Int): Boolean
    }


    constructor()

    override fun getFieldOrder(): List<String> = Arrays.asList("CommitWorkingCopy", "RevertWorkingCopy", "GetWorkingPlayAreaSize", "GetWorkingPlayAreaRect",
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

val IVRChaperoneSetup_Version = "FnTable:IVRChaperoneSetup_005"