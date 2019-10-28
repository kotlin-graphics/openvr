package openvr.unity

import glm_.f
import glm_.min
import glm_.quat.Quat
import openvr.assets.steamVR.script.SteamVR_Settings
import kotlin.math.abs
import kotlin.math.acos


object Time {

    private val start = System.currentTimeMillis()

    /** The real time in seconds since the game started (Read Only). */
    val realtimeSinceStartup: Float
        get() = (System.currentTimeMillis() - start) / 1_000f

    var renderedFrameCount = 0

    /** The total number of frames that have passed (Read Only). */
    var frameCount = 0

    /** The scale at which the time is passing. This can be used for slow motion effects. */
    var timeScale = 1f

    /** The maximum time a frame can spend on particle updates. If the frame takes longer than this,
     *  then updates are split into multiple smaller updates. */
    var maximumParticleDeltaTime = 0.03f

    /** A smoothed out Time.deltaTime (Read Only). */
    var smoothDeltaTime = 0f

    /** The maximum time a frame can take. Physics and other fixed frame rate updates
     *  (like MonoBehaviour's MonoBehaviour.FixedUpdate). */
    var maximumDeltaTime = 1f /3

    /** Slows game playback time to allow screenshots to be saved between frames. */
    var captureFramerate = false

    /** The interval in seconds at which physics and other fixed frame rate updates
     *  (like MonoBehaviour's MonoBehaviour.FixedUpdate) are performed. */
    var fixedDeltaTime = 0.02f

    /** The timeScale-independent interval in seconds from the last frame to the current one (Read Only). */
    var unscaledDeltaTime = 0.02f

    /** The TimeScale-independant time the latest MonoBehaviour.FixedUpdate has started (Read Only).
     *  This is the time in seconds since the start of the game. */
//    public static float fixedUnscaledTime { get; }

    /** The timeScale-independant time for this frame (Read Only). This is the time in seconds since the start of the game. */
//    public static float unscaledTime { get; }

    /** The time the latest MonoBehaviour.FixedUpdate has started (Read Only). This is the time in seconds since the start of the game. */
//    public static float fixedTime { get; }

    /** The time in seconds it took to complete the last frame (Read Only). */
    var deltaTime = 0f

    /** The time this frame has started (Read Only). This is the time in seconds since the last level has been loaded. */
//    public static float timeSinceLevelLoad { get; }

    /** The time at the beginning of this frame (Read Only). This is the time in seconds since the start of the game. */
//    [NativeProperty("CurTime")]
//    public static float time { get; }

    /** The timeScale-independent interval in seconds from the last fixed frame to the current one (Read Only). */
//    public static float fixedUnscaledDeltaTime { get; }

    /** Returns true if called inside a fixed time step callback (like MonoBehaviour's MonoBehaviour.FixedUpdate),
     *  otherwise returns false. */
//    public static bool inFixedTimeStep { get; }
}


object Application {
    var isEditor = false

    var dataPath = SteamVR_Settings.actionsFilePath
}


/** @Returns the angle in degrees between two rotations a and b.
 *
 *  https://github.com/jamesjlinden/unity-decompiled/blob/master/UnityEngine/UnityEngine/Quaternion.cs#L401 */
fun angle(a: Quat, b: Quat): Float = (acos(abs(a dot b) min 1f) * 2.0 * 57.2957801818848).f