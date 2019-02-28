package openvr.lib

import glm_.BYTES
import glm_.vec2.Vec2i
import kool.stak
import org.lwjgl.openvr.HmdColor
import org.lwjgl.openvr.HmdQuad
import org.lwjgl.openvr.VRChaperone
import org.lwjgl.openvr.VRChaperone.*
import org.lwjgl.system.MemoryUtil.memGetInt


object vrChaperone : vrInterface {

    enum class CalibrationState(@JvmField val i: Int) {

        // OK!
        /**  Chaperone is fully calibrated and working correctly     */
        OK(1),

        // Warnings
        Warning(100),
        /** A base station thinks that it might have moved  */
        Warning_BaseStationMayHaveMoved(101),
        /** There are less base stations than when calibrated   */
        Warning_BaseStationRemoved(102),
        /** Seated bounds haven't been calibrated for the current tracking center   */
        Warning_SeatedBoundsInvalid(103),

        // Errors
        /** The UniverseID is invalid   */
        Error(200),
        /** Tracking center hasn't be calibrated for at least one of the base stations  */
        Error_BaseStationUninitialized(201),
        /** Tracking center is calibrated), but base stations disagree on the tracking space    */
        Error_BaseStationConflict(202),
        /** Play Area hasn't been calibrated for the current tracking center    */
        Error_PlayAreaInvalid(203),
        /** Collision Bounds haven't been calibrated for the current tracking center    */
        Error_CollisionBoundsInvalid(204);

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    /** Get the current state of Chaperone calibration. This state can change at any time during a session due to physical base station changes. */
    val calibrationState: CalibrationState
        get() = CalibrationState of VRChaperone.VRChaperone_GetCalibrationState()

    /**
     * Returns the width and depth of the Play Area (formerly named Soft Bounds) in X and Z. Tracking space center(0, 0, 0) is the center of the Play Area.
     *
     * @param size
     */
    infix fun getPlayAreaSize(size: Vec2i): Boolean =
            stak {
                val x = it.nmalloc(1, Vec2i.size)
                val y = x + Int.BYTES
                nVRChaperone_GetPlayAreaSize(x, y).also {
                    size.put(memGetInt(x), memGetInt(y))
                }
            }

    /**
     * Returns the 4 corner positions of the Play Area (formerly named Soft Bounds).
     *
     * <p>Corners are in counter-clockwise order. Standing center (0,0,0) is the center of the Play Area. It's a rectangle. 2 sides are parallel to the X axis
     * and 2 sides are parallel to the Z axis. Height of every corner is 0Y (on the floor).</p>
     *
     * @param rect
     */
    infix fun getPlayAreaRect(rect: HmdQuad): Boolean =
            nVRChaperone_GetPlayAreaRect(rect.adr)

    /** Reload Chaperone data from the .vrchap file on disk. */
    fun reloadInfo() = VRChaperone_ReloadInfo()

    /**
     * Optionally give the chaperone system a hit about the color and brightness in the scene.
     *
     * @param color
     */
    infix fun setSceneColor(color: HmdColor) = nVRChaperone_SetSceneColor(color.adr) // TODO vec3/4?

    /**
     * Get the current chaperone bounds draw color and brightness.
     *
     * @param outputColorArray
     * @param collisionBoundsFadeDistance
     * @param outputCameraColor
     */
    fun getBoundsColor(outputColorArray: HmdColor.Buffer, collisionBoundsFadeDistance: Float, outputCameraColor: HmdColor) =
            nVRChaperone_GetBoundsColor(outputColorArray.adr, outputColorArray.rem, collisionBoundsFadeDistance, outputCameraColor.adr)

    /** Determine whether the bounds are showing right now. */
    val areBoundsVisible: Boolean
        get() = VRChaperone_AreBoundsVisible()

    /**
     * Force the bounds to show, mostly for utilities.
     *
     * @param force
     */
    infix fun forceBoundsVisible(force: Boolean) = VRChaperone_ForceBoundsVisible(force)

    override val version: String
        get() = "IVRChaperone_003"
}