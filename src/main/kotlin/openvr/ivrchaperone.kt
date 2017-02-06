package openvr

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.FloatByReference
import java.util.*

// ivrchaperone.h =================================================================================================================================================

enum class ChaperoneCalibrationState(@JvmField val i: Int) {

    // OK!
    OK(1), //                                 Chaperone is fully calibrated and working correctly

    // Warnings
    Warning(100),
    Warning_BaseStationMayHaveMoved(101), //  A base station thinks that it might have moved
    Warning_BaseStationRemoved(102), //       There are less base stations than when calibrated
    Warning_SeatedBoundsInvalid(103), //      Seated bounds haven't been calibrated for the current tracking center

    // Errors
    Error(200), //                            The UniverseID is invalid
    Error_BaseStationUninitialized(201), //    Tracking center hasn't be calibrated for at least one of the base stations
    Error_BaseStationConflict(202), //        Tracking center is calibrated), but base stations disagree on the tracking space
    Error_PlayAreaInvalid(203), //            Play Area hasn't been calibrated for the current tracking center
    Error_CollisionBoundsInvalid(204);     // Collision Bounds haven't been calibrated for the current tracking center

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** HIGH LEVEL TRACKING SPACE ASSUMPTIONS:
 * 0,0,0 is the preferred standing area center.
 * 0Y is the floor height.
 * -Z is the preferred forward facing direction. */

open class IVRChaperone : Structure {

    /** Get the current state of Chaperone calibration. This state can change at any time during a session due to physical base station changes. **/
    fun getCalibrationState() = ChaperoneCalibrationState.of(GetCalibrationState!!.invoke())

    internal @JvmField var GetCalibrationState: GetCalibrationState_callback? = null

    interface GetCalibrationState_callback : Callback {
        fun invoke(): Int
    }

    /** Returns the width and depth of the Play Area (formerly named Soft Bounds) in X and Z.
     * Tracking space center (0,0,0) is the center of the Play Area. **/
    fun getPlayAreaSize(pSizeX: FloatByReference, pSizeZ: FloatByReference) = GetPlayAreaSize!!.invoke(pSizeX, pSizeZ)

    internal @JvmField var GetPlayAreaSize: GetPlayAreaSize_callback? = null

    interface GetPlayAreaSize_callback : Callback {
        fun invoke(pSizeX: FloatByReference, pSizeZ: FloatByReference): Boolean
    }

    /** Returns the 4 corner positions of the Play Area (formerly named Soft Bounds).
     * Corners are in counter-clockwise order.
     * Standing center (0,0,0) is the center of the Play Area.
     * It's a rectangle.
     * 2 sides are parallel to the X axis and 2 sides are parallel to the Z axis.
     * Height of every corner is 0Y (on the floor). **/
    fun getPlayAreaRect(rect: HmdQuad_t.ByReference) = GetPlayAreaRect!!.invoke(rect)

    internal @JvmField var GetPlayAreaRect: GetPlayAreaRect_callback? = null

    interface GetPlayAreaRect_callback : Callback {
        fun invoke(rect: HmdQuad_t.ByReference): Boolean
    }

    /** Reload Chaperone data from the .vrchap file on disk. */
    fun reloadInfo() = ReloadInfo!!.invoke()

    internal @JvmField var ReloadInfo: ReloadInfo_callback? = null

    interface ReloadInfo_callback : Callback {
        fun invoke()
    }

    /** Optionally give the chaperone system a hit about the color and brightness in the scene **/
    fun setSceneColor(color: HmdColor_t) = SetSceneColor!!.invoke(color)

    internal @JvmField var SetSceneColor: SetSceneColor_callback? = null

    interface SetSceneColor_callback : Callback {
        fun invoke(color: HmdColor_t)
    }

    /** Get the current chaperone bounds draw color and brightness **/
    fun getBoundsColor(pOutputColorArray: HmdColor_t.ByReference, nNumOutputColors: Int, flCollisionBoundsFadeDistance: Float,
                       pOutputCameraColor: HmdColor_t.ByReference)
            = GetBoundsColor!!.invoke(pOutputColorArray, nNumOutputColors, flCollisionBoundsFadeDistance, pOutputCameraColor)

    internal @JvmField var GetBoundsColor: GetBoundsColor_callback? = null

    interface GetBoundsColor_callback : Callback {
        fun invoke(pOutputColorArray: HmdColor_t.ByReference, nNumOutputColors: Int, flCollisionBoundsFadeDistance: Float,
                   pOutputCameraColor: HmdColor_t.ByReference)
    }

    /** Determine whether the bounds are showing right now **/
    fun areBoundsVisible() = AreBoundsVisible!!.invoke()

    internal @JvmField var AreBoundsVisible: AreBoundsVisible_callback? = null

    interface AreBoundsVisible_callback : Callback {
        fun invoke(): Boolean
    }

    /** Force the bounds to show, mostly for utilities **/
    fun forceBoundsVisible(bForce: Boolean) = ForceBoundsVisible!!.invoke(bForce)

    internal @JvmField var ForceBoundsVisible: ForceBoundsVisible_callback? = null

    interface ForceBoundsVisible_callback : Callback {
        fun invoke(bForce: Boolean)
    }


    constructor()

    override fun getFieldOrder(): List<String> = Arrays.asList("GetCalibrationState", "GetPlayAreaSize", "GetPlayAreaRect", "ReloadInfo", "SetSceneColor",
            "GetBoundsColor", "AreBoundsVisible_callback", "ForceBoundsVisible")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRChaperone(), Structure.ByReference
    class ByValue : IVRChaperone(), Structure.ByValue
}

val IVRChaperone_Version = "FnTable:IVRChaperone_003";