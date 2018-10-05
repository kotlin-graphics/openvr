package lib

import ab.appBuffer
import glm_.buffer.adr
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import org.lwjgl.openvr.HmdQuad
import org.lwjgl.openvr.OpenVR.IVRChaperoneSetup
import org.lwjgl.openvr.VRChaperoneSetup
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.system.MemoryUtil.memGetFloat
import vkk.adr
import java.nio.ByteBuffer
import java.nio.IntBuffer

/**
 * Saves the current working copy to disk.
 *
 * @param configFile one of:<br><table><tr><td>{@link VR#EChaperoneConfigFile_Live}</td><td>{@link VR#EChaperoneConfigFile_Temp}</td></tr></table>
 */
infix fun IVRChaperoneSetup.commitWorkingCopy(configFile: EChaperoneConfigFile): Boolean {
    return VRChaperoneSetup.VRChaperoneSetup_CommitWorkingCopy(configFile.i)
}

/**
 * Reverts the working copy to match the live chaperone calibration.
 *
 * <p>To modify existing data this MUST be do WHILE getting a non-error {@code ChaperoneCalibrationStatus}. Only after this should you do gets and sets on
 * the existing data.</p>
 */
fun IVRChaperoneSetup.revertWorkingCopy() = VRChaperoneSetup.VRChaperoneSetup_RevertWorkingCopy()

/**
 * Returns the width and depth of the Play Area (formerly named Soft Bounds) in X and Z from the working copy. Tracking space center (0,0,0) is the center
 * of the Play Area.
 *
 * @param pSizeX
 * @param pSizeZ
 */
infix fun IVRChaperoneSetup.getWorkingPlayAreaSize(size: Vec2): Boolean {
    val w = appBuffer.float
    val h = appBuffer.float
    return VRChaperoneSetup.nVRChaperoneSetup_GetWorkingPlayAreaSize(w, h).also { size(memGetFloat(w), memGetFloat(h)) }
}

/**
 * Returns the 4 corner positions of the Play Area (formerly named Soft Bounds) from the working copy.
 *
 * <p>Corners are in clockwise order. Tracking space center (0,0,0) is the center of the Play Area. It's a rectangle. 2 sides are parallel to the X axis and
 * 2 sides are parallel to the Z axis. Height of every corner is 0Y (on the floor).</p>
 *
 * @param rect
 */
infix fun IVRChaperoneSetup.getWorkingPlayAreaRect(rect: HmdQuad): Boolean {
    return VRChaperoneSetup.nVRChaperoneSetup_GetWorkingPlayAreaRect(rect.adr)
}

/**
 * Returns the number of Quads if the buffer points to null. Otherwise it returns Quads into the buffer up to the max specified from the working copy.
 *
 * @param quadsBuffer
 * @param quadsCount
 */
fun IVRChaperoneSetup.getWorkingCollisionBoundsInfo(quadsBuffer: HmdQuad.Buffer?, quadsCount: IntBuffer): Boolean {
    return VRChaperoneSetup.nVRChaperoneSetup_GetWorkingCollisionBoundsInfo(quadsBuffer?.adr ?: NULL, quadsCount.adr)
}

/**
 * Returns the number of Quads if the buffer points to null. Otherwise it returns Quads into the buffer up to the max specified.
 *
 * @param quadsBuffer
 * @param quadsCount
 */
fun IVRChaperoneSetup.getLiveCollisionBoundsInfo(quadsBuffer: HmdQuad.Buffer?, quadsCount: IntBuffer): Boolean {
    return VRChaperoneSetup.nVRChaperoneSetup_GetLiveCollisionBoundsInfo(quadsBuffer?.adr ?: NULL, quadsCount.adr)
}

/**
 * Returns the preferred seated position from the working copy.
 *
 * @param matSeatedZeroPoseToRawTrackingPose
 */
infix fun IVRChaperoneSetup.getWorkingSeatedZeroPoseToRawTrackingPose(matSeatedZeroPoseToRawTrackingPose: Mat4): Boolean {
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
infix fun IVRChaperoneSetup.getWorkingStandingZeroPoseToRawTrackingPose(matStandingZeroPoseToRawTrackingPose: Mat4): Boolean {
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
infix fun IVRChaperoneSetup.setWorkingPlayAreaSize(size: Vec2) = VRChaperoneSetup.VRChaperoneSetup_SetWorkingPlayAreaSize(size.x, size.y)

/**
 * Sets the Collision Bounds in the working copy.
 *
 * @param quadsBuffer
 */
infix fun IVRChaperoneSetup.setWorkingCollisionBoundsInfo(quadsBuffer: HmdQuad.Buffer) {
    VRChaperoneSetup.nVRChaperoneSetup_SetWorkingCollisionBoundsInfo(quadsBuffer.adr, quadsBuffer.rem)
}

/**
 * Sets the preferred seated position in the working copy.
 *
 * @param matSeatedZeroPoseToRawTrackingPose
 */
infix fun IVRChaperoneSetup.setWorkingSeatedZeroPoseToRawTrackingPose(matSeatedZeroPoseToRawTrackingPose: Mat4) {
    val hmdMat34 = vr.HmdMatrix34(matSeatedZeroPoseToRawTrackingPose)
    VRChaperoneSetup.nVRChaperoneSetup_SetWorkingSeatedZeroPoseToRawTrackingPose(hmdMat34.adr)
}

/**
 * Sets the preferred standing position in the working copy.
 *
 * @param matStandingZeroPoseToRawTrackingPose
 */
infix fun IVRChaperoneSetup.setWorkingStandingZeroPoseToRawTrackingPose(matStandingZeroPoseToRawTrackingPose: Mat4) {
    val hmdMat34 = vr.HmdMatrix34(matStandingZeroPoseToRawTrackingPose)
    VRChaperoneSetup.nVRChaperoneSetup_SetWorkingStandingZeroPoseToRawTrackingPose(hmdMat34.adr)
}

/**
 * Tear everything down and reload it from the file on disk.
 *
 * @param configFile one of:<br><table><tr><td>{@link VR#EChaperoneConfigFile_Live}</td><td>{@link VR#EChaperoneConfigFile_Temp}</td></tr></table>
 */
infix fun IVRChaperoneSetup.reloadFromDisk(configFile: EChaperoneConfigFile) = VRChaperoneSetup.VRChaperoneSetup_ReloadFromDisk(configFile.i)

/**
 * Returns the preferred seated position.
 *
 * @param matSeatedZeroPoseToRawTrackingPose
 */
infix fun IVRChaperoneSetup.getLiveSeatedZeroPoseToRawTrackingPose(matSeatedZeroPoseToRawTrackingPose: Mat4): Boolean {
    val hmdMat34 = vr.HmdMatrix34(matSeatedZeroPoseToRawTrackingPose)
    return VRChaperoneSetup.nVRChaperoneSetup_GetLiveSeatedZeroPoseToRawTrackingPose(hmdMat34.adr)
}

infix fun IVRChaperoneSetup.setWorkingCollisionBoundsTagsInfo(tagsBuffer: ByteBuffer) {
    VRChaperoneSetup.nVRChaperoneSetup_SetWorkingCollisionBoundsTagsInfo(tagsBuffer.adr, tagsBuffer.rem)
}

fun IVRChaperoneSetup.getLiveCollisionBoundsTagsInfo(tagsBuffer: ByteBuffer?, tagCount: IntBuffer): Boolean {
    return VRChaperoneSetup.nVRChaperoneSetup_GetLiveCollisionBoundsTagsInfo(tagsBuffer?.adr ?: NULL, tagCount.adr)
}

infix fun IVRChaperoneSetup.setWorkingPhysicalBoundsInfo(quadsBuffer: HmdQuad.Buffer): Boolean {
    return VRChaperoneSetup.nVRChaperoneSetup_SetWorkingPhysicalBoundsInfo(quadsBuffer.adr, quadsBuffer.rem)
}

fun IVRChaperoneSetup.getLivePhysicalBoundsInfo(quadsBuffer: HmdQuad.Buffer?, quadsCount: IntBuffer): Boolean {
    return VRChaperoneSetup.nVRChaperoneSetup_GetLivePhysicalBoundsInfo(quadsBuffer?.adr ?: NULL, quadsCount.adr)
}

fun IVRChaperoneSetup.exportLiveToBuffer(buffer: ByteBuffer?, bufferLength: IntBuffer): Boolean {
    return VRChaperoneSetup.nVRChaperoneSetup_ExportLiveToBuffer(buffer?.adr ?: NULL, bufferLength.adr)
}

fun IVRChaperoneSetup.importFromBufferToWorking(buffer: ByteBuffer, importFlags: Int): Boolean {
    return VRChaperoneSetup.nVRChaperoneSetup_ImportFromBufferToWorking(buffer.adr, importFlags)
}