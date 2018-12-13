package openvr.lib

import glm_.BYTES
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import kool.adr
import kool.rem
import org.lwjgl.openvr.HmdQuad
import org.lwjgl.openvr.HmdVector2
import org.lwjgl.openvr.VRChaperoneSetup
import org.lwjgl.openvr.VRChaperoneSetup.*
import org.lwjgl.system.MemoryStack.stackGet
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.system.MemoryUtil.memGetFloat
import java.nio.ByteBuffer
import java.nio.IntBuffer


object vrChaperoneSetup : vrInterface {

    enum class ConfigFile(@JvmField val i: Int) {
        /** The live chaperone config, used by most applications and games  */
        Live(1),
        /** The temporary chaperone config, used to live-preview collision bounds in room setup */
        Temp(2);

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    enum class ImportFlags(@JvmField val i: Int) {

        BoundsOnly(0x0001);

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    /**
     * Saves the current working copy to disk.
     *
     * @param configFile one of:<br><table><tr><td>{@link VR#EChaperoneConfigFile_Live}</td><td>{@link VR#EChaperoneConfigFile_Temp}</td></tr></table>
     */
    infix fun commitWorkingCopy(configFile: ConfigFile): Boolean = VRChaperoneSetup_CommitWorkingCopy(configFile.i)

    /**
     * Reverts the working copy to match the live chaperone calibration.
     *
     * <p>To modify existing data this MUST be do WHILE getting a non-error {@code ChaperoneCalibrationStatus}. Only after this should you do gets and sets on
     * the existing data.</p>
     */
    fun revertWorkingCopy() = VRChaperoneSetup_RevertWorkingCopy()

    /**
     * Returns the width and depth of the Play Area (formerly named Soft Bounds) in X and Z from the working copy. Tracking space center (0,0,0) is the center
     * of the Play Area.
     *
     * @param size
     */
    infix fun getWorkingPlayAreaSize(size: Vec2): Boolean {
        val x = stackGet().nmalloc(1, Vec2.size)
        val y = x + Int.BYTES
        return nVRChaperoneSetup_GetWorkingPlayAreaSize(x, x + Int.BYTES).also {
            size(memGetFloat(x), memGetFloat(y))
        }
    }

    /**
     * Returns the 4 corner positions of the Play Area (formerly named Soft Bounds) from the working copy.
     *
     * <p>Corners are in clockwise order. Tracking space center (0,0,0) is the center of the Play Area. It's a rectangle. 2 sides are parallel to the X axis and
     * 2 sides are parallel to the Z axis. Height of every corner is 0Y (on the floor).</p>
     *
     * @param rect
     */
    infix fun getWorkingPlayAreaRect(rect: HmdQuad): Boolean = nVRChaperoneSetup_GetWorkingPlayAreaRect(rect.adr)

    /**
     * Returns the number of Quads if the buffer points to null. Otherwise it returns Quads into the buffer up to the max specified from the working copy.
     *
     * @param quadsBuffer
     * @param quadsCount
     */
    fun getWorkingCollisionBoundsInfo(quadsBuffer: HmdQuad.Buffer?, quadsCount: IntBuffer): Boolean =
            nVRChaperoneSetup_GetWorkingCollisionBoundsInfo(quadsBuffer?.adr ?: NULL, quadsCount.adr)

    /**
     * Returns the number of Quads if the buffer points to null. Otherwise it returns Quads into the buffer up to the max specified.
     *
     * @param quadsBuffer
     * @param quadsCount
     */
    fun getLiveCollisionBoundsInfo(quadsBuffer: HmdQuad.Buffer?, quadsCount: IntBuffer): Boolean =
        nVRChaperoneSetup_GetLiveCollisionBoundsInfo(quadsBuffer?.adr ?: NULL, quadsCount.adr)

    /**
     * Returns the preferred seated position from the working copy.
     *
     * @param matSeatedZeroPoseToRawTrackingPose
     */
    infix fun getWorkingSeatedZeroPoseToRawTrackingPose(matSeatedZeroPoseToRawTrackingPose: Mat4): Boolean {
        val hmdMat34 = vr.HmdMatrix34()
        return VRChaperoneSetup.nVRChaperoneSetup_GetWorkingSeatedZeroPoseToRawTrackingPose(hmdMat34.adr).also {
            hmdMat34 to matSeatedZeroPoseToRawTrackingPose
        }
    }

    /**
     * Returns the standing origin from the working copy.
     *
     * @param matStandingZeroPoseToRawTrackingPose
     */
    infix fun getWorkingStandingZeroPoseToRawTrackingPose(matStandingZeroPoseToRawTrackingPose: Mat4): Boolean {
        val hmdMat34 = vr.HmdMatrix34()
        return VRChaperoneSetup.nVRChaperoneSetup_GetWorkingStandingZeroPoseToRawTrackingPose(hmdMat34.adr).also {
            hmdMat34 to matStandingZeroPoseToRawTrackingPose
        }
    }

    /**
     * Sets the Play Area in the working copy.
     *
     * @param sizeX
     * @param sizeZ
     */
    infix fun setWorkingPlayAreaSize(size: Vec2) = VRChaperoneSetup_SetWorkingPlayAreaSize(size.x, size.y)

    /**
     * Sets the Collision Bounds in the working copy.
     *
     * @param quadsBuffer
     */
    infix fun setWorkingCollisionBoundsInfo(quadsBuffer: HmdQuad.Buffer) =
            nVRChaperoneSetup_SetWorkingCollisionBoundsInfo(quadsBuffer.adr, quadsBuffer.rem)

    /** Sets the Collision Bounds in the working copy. */
    infix fun setWorkingPerimeter(pointBuffer: HmdVector2.Buffer) =
        nVRChaperoneSetup_SetWorkingPerimeter(pointBuffer.adr, pointBuffer.rem)

    /**
     * Sets the preferred seated position in the working copy.
     *
     * @param matSeatedZeroPoseToRawTrackingPose
     */
    infix fun setWorkingSeatedZeroPoseToRawTrackingPose(matSeatedZeroPoseToRawTrackingPose: Mat4) =
        nVRChaperoneSetup_SetWorkingSeatedZeroPoseToRawTrackingPose(vr.HmdMatrix34(matSeatedZeroPoseToRawTrackingPose).adr)

    /**
     * Sets the preferred standing position in the working copy.
     *
     * @param matStandingZeroPoseToRawTrackingPose
     */
    infix fun setWorkingStandingZeroPoseToRawTrackingPose(matStandingZeroPoseToRawTrackingPose: Mat4) =
        nVRChaperoneSetup_SetWorkingStandingZeroPoseToRawTrackingPose(vr.HmdMatrix34(matStandingZeroPoseToRawTrackingPose).adr)

    /**
     * Tear everything down and reload it from the file on disk.
     *
     * @param configFile one of:<br><table><tr><td>{@link VR#EChaperoneConfigFile_Live}</td><td>{@link VR#EChaperoneConfigFile_Temp}</td></tr></table>
     */
    infix fun reloadFromDisk(configFile: ConfigFile) = VRChaperoneSetup_ReloadFromDisk(configFile.i)

    /**
     * Returns the preferred seated position.
     *
     * @param matSeatedZeroPoseToRawTrackingPose
     */
    infix fun getLiveSeatedZeroPoseToRawTrackingPose(matSeatedZeroPoseToRawTrackingPose: Mat4): Boolean =
        nVRChaperoneSetup_GetLiveSeatedZeroPoseToRawTrackingPose(vr.HmdMatrix34(matSeatedZeroPoseToRawTrackingPose).adr)

    fun exportLiveToBuffer(buffer: ByteBuffer?, bufferLength: IntBuffer): Boolean =
            VRChaperoneSetup.nVRChaperoneSetup_ExportLiveToBuffer(buffer?.adr ?: NULL, bufferLength.adr)

    fun importFromBufferToWorking(buffer: ByteBuffer, importFlags: Int): Boolean =
            nVRChaperoneSetup_ImportFromBufferToWorking(buffer.adr, importFlags)

    /** Shows the chaperone data in the working set to preview in the compositor.*/
    fun showWorkingSetPreview() = VRChaperoneSetup_ShowWorkingSetPreview()

    /** Hides the chaperone data in the working set to preview in the compositor (if it was visible).*/
    fun hideWorkingSetPreview() = VRChaperoneSetup_HideWorkingSetPreview()

    override val version: String
        get() = "IVRChaperoneSetup_006"
}