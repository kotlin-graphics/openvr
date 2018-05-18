package openvr.lib

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.FloatByReference
import glm_.vec2.Vec2
import java.util.*

// ivrchaperone.h =================================================================================================================================================

enum class ChaperoneCalibrationState(@JvmField val i: Int) {

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

/** HIGH LEVEL TRACKING SPACE ASSUMPTIONS:
 * 0,0,0 is the preferred standing area center.
 * 0Y is the floor height.
 * -Z is the preferred forward facing direction. */

open class IVRChaperone : Structure {

    /** Get the current state of Chaperone calibration. This state can change at any time during a session due to physical base station changes. **/
    val calibrationState get() = ChaperoneCalibrationState.of(GetCalibrationState!!())

    @JvmField
    var GetCalibrationState: GetCalibrationState_callback? = null

    interface GetCalibrationState_callback : Callback {
        operator fun invoke(): Int
    }

    /** Returns the width and depth of the Play Area (formerly named Soft Bounds) in X and Z.
     * Tracking space center (0,0,0) is the center of the Play Area. **/
    infix fun getPlayAreaSize(area: Vec2): Boolean {
        val sizeX = FloatByReference()
        val sizeZ = FloatByReference()
        return GetPlayAreaSize!!(sizeX, sizeZ).also { area.put(sizeX.value, sizeZ.value) }
    }

    @JvmField
    var GetPlayAreaSize: GetPlayAreaSize_callback? = null

    interface GetPlayAreaSize_callback : Callback {
        operator fun invoke(pSizeX: FloatByReference, pSizeZ: FloatByReference): Boolean
    }

    /** Returns the 4 corner positions of the Play Area (formerly named Soft Bounds).
     * Corners are in counter-clockwise order.
     * Standing center (0,0,0) is the center of the Play Area.
     * It's a rectangle.
     * 2 sides are parallel to the X axis and 2 sides are parallel to the Z axis.
     * Height of every corner is 0Y (on the floor). **/
    infix fun getPlayAreaRect(rect: HmdQuad.ByReference) = GetPlayAreaRect!!(rect)

    @JvmField
    var GetPlayAreaRect: GetPlayAreaRect_callback? = null

    interface GetPlayAreaRect_callback : Callback {
        operator fun invoke(rect: HmdQuad.ByReference): Boolean
    }

    /** Reload Chaperone data from the .vrchap file on disk. */
    fun reloadInfo() = ReloadInfo!!()

    @JvmField
    var ReloadInfo: ReloadInfo_callback? = null

    interface ReloadInfo_callback : Callback {
        operator fun invoke()
    }

    /** Optionally give the chaperone system a hit about the color and brightness in the scene **/
    infix fun setSceneColor(color: HmdColor) = SetSceneColor!!(color)

    @JvmField
    var SetSceneColor: SetSceneColor_callback? = null

    interface SetSceneColor_callback : Callback {
        operator fun invoke(color: HmdColor)
    }

    /** Get the current chaperone bounds draw color and brightness **/
    fun getBoundsColor(outputColorArray: HmdColor.ByReference, numOutputColors: Int, collisionBoundsFadeDistance: Float, outputCameraColor: HmdColor.ByReference) = GetBoundsColor!!(outputColorArray, numOutputColors, collisionBoundsFadeDistance, outputCameraColor)

    @JvmField
    var GetBoundsColor: GetBoundsColor_callback? = null

    interface GetBoundsColor_callback : Callback {
        operator fun invoke(pOutputColorArray: HmdColor.ByReference, nNumOutputColors: Int, flCollisionBoundsFadeDistance: Float,
                   pOutputCameraColor: HmdColor.ByReference)
    }

    /** Determine whether the bounds are showing right now **/
    val areBoundsVisible get() = AreBoundsVisible!!()

    @JvmField
    var AreBoundsVisible: AreBoundsVisible_callback? = null

    interface AreBoundsVisible_callback : Callback {
        operator fun invoke(): Boolean
    }

    /** Force the bounds to show, mostly for utilities **/
    infix fun forceBoundsVisible(force: Boolean) = ForceBoundsVisible!!(force)

    @JvmField
    var ForceBoundsVisible: ForceBoundsVisible_callback? = null

    interface ForceBoundsVisible_callback : Callback {
        operator fun invoke(bForce: Boolean)
    }


    constructor()

    override fun getFieldOrder() = listOf("GetCalibrationState", "GetPlayAreaSize", "GetPlayAreaRect", "ReloadInfo", "SetSceneColor",
            "GetBoundsColor", "AreBoundsVisible_callback", "ForceBoundsVisible")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRChaperone(), Structure.ByReference
    class ByValue : IVRChaperone(), Structure.ByValue
}

val IVRChaperone_Version = "IVRChaperone_003"

fun main(args: Array<String>) {
    var a: FloatArray? = null
    a!![0]
}