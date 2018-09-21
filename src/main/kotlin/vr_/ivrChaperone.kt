package vr_

import ab.appBuffer
import glm_.vec2.Vec2i
import org.lwjgl.openvr.HmdColor
import org.lwjgl.openvr.HmdQuad
import org.lwjgl.openvr.OpenVR.IVRChaperone
import org.lwjgl.openvr.VRChaperone
import org.lwjgl.system.MemoryUtil
import vkk.adr

/** Get the current state of Chaperone calibration. This state can change at any time during a session due to physical base station changes. */
val IVRChaperone.calibrationState: ChaperoneCalibrationState
    get() = ChaperoneCalibrationState of VRChaperone.VRChaperone_GetCalibrationState()

/**
 * Returns the width and depth of the Play Area (formerly named Soft Bounds) in X and Z. Tracking space center(0, 0, 0) is the center of the Play Area.
 *
 * @param pSizeX
 * @param pSizeZ
 */
infix fun IVRChaperone.playAreaSize(size: Vec2i): Boolean {
    val w = appBuffer.int
    val h = appBuffer.int
    return VRChaperone.nVRChaperone_GetPlayAreaSize(w, h).also { size(MemoryUtil.memGetInt(w), MemoryUtil.memGetInt(h)) }
}

/**
 * Returns the 4 corner positions of the Play Area (formerly named Soft Bounds).
 *
 * <p>Corners are in counter-clockwise order. Standing center (0,0,0) is the center of the Play Area. It's a rectangle. 2 sides are parallel to the X axis
 * and 2 sides are parallel to the Z axis. Height of every corner is 0Y (on the floor).</p>
 *
 * @param rect
 */
infix fun IVRChaperone.getPlayAreaRect(rect: HmdQuad): Boolean {
    return VRChaperone.nVRChaperone_GetPlayAreaRect(rect.adr)
}

/** Reload Chaperone data from the .vrchap file on disk. */
fun IVRChaperone.reloadInfo() = VRChaperone.VRChaperone_ReloadInfo()

/**
 * Optionally give the chaperone system a hit about the color and brightness in the scene.
 *
 * @param color
 */
infix fun IVRChaperone.setSceneColor(color: HmdColor) = VRChaperone.nVRChaperone_SetSceneColor(color.adr)

/**
 * Get the current chaperone bounds draw color and brightness.
 *
 * @param outputColorArray
 * @param collisionBoundsFadeDistance
 * @param outputCameraColor
 */
fun IVRChaperone.getBoundsColor(outputColorArray: HmdColor.Buffer, collisionBoundsFadeDistance: Float, outputCameraColor: HmdColor) {
    VRChaperone.nVRChaperone_GetBoundsColor(outputColorArray.adr, outputColorArray.remaining(), collisionBoundsFadeDistance, outputCameraColor.adr)
}

/** Determine whether the bounds are showing right now. */
val IVRChaperone.areBoundsVisible: Boolean
    get() = VRChaperone.VRChaperone_AreBoundsVisible()

/**
 * Force the bounds to show, mostly for utilities.
 *
 * @param force
 */
infix fun IVRChaperone.forceBoundsVisible(force: Boolean) = VRChaperone.VRChaperone_ForceBoundsVisible(force)