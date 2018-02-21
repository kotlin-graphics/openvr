package openvr.lib

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.PointerByReference
import glm_.vec3.Vec3
import glm_.vec4.Vec4
import java.util.*

// ivrcompositor.h ================================================================================================================================================

/** Errors that can occur with the VR compositor */
enum class EVRCompositorError(@JvmField val i: Int) {

    None(0),
    RequestFailed(1),
    IncompatibleVersion(100),
    DoNotHaveFocus(101),
    InvalidTexture(102),
    IsNotSceneApplication(103),
    TextureIsOnWrongDevice(104),
    TextureUsesUnsupportedFormat(105),
    SharedTexturesNotSupported(106),
    IndexOutOfRange(107),
    AlreadySubmitted(108),
    InvalidBounds(109);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Timing mode passed to SetExplicitTimingMode(); see that function for documentation */
enum class EVRCompositorTimingMode {
    Implicit, RuntimePerformsPostPresentHandoff, ApplicationPerformsPostPresentHandoff;

    val i = ordinal

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

val VRCompositor_ReprojectionReason_Cpu = 0x01
val VRCompositor_ReprojectionReason_Gpu = 0x02
/**
 *  This flag indicates the async reprojection mode is active, but does not indicate if reprojection actually happened or not.
 *  Use the ReprojectionReason flags above to check if reprojection was actually applied (i.e. scene texture was reused).
 *  NumFramePresents > 1 also indicates the scene texture was reused, and also the number of times that it was presented in total.  */
val VRCompositor_ReprojectionAsync = 0x04

/** Provides a single frame's timing information to the app */
open class Compositor_FrameTiming : Structure {

    /** Set to sizeOf( Compositor_FrameTiming )  */
    @JvmField
    var size = 0

    @JvmField
    var frameIndex = 0
    /** number of times this frame was presented    */
    @JvmField
    var numFramePresents = 0
    /** number of times this frame was presented on a vsync other than it was originally predicted to   */
    @JvmField
    var numMisPresented = 0
    /** number of additional times previous frame was scanned out   */
    @JvmField
    var numDroppedFrames = 0

    @JvmField
    var reprojectionFlags = 0


    /** Absolute time reference for comparing frames.  This aligns with the vsync that running start is relative to. */
    @JvmField
    var systemTimeInSeconds = 0.0


    /*  These times may include work from other processes due to OS scheduling.
        The fewer packets of work these are broken up into, the less likely this will happen.
        GPU work can be broken up by calling Flush.  This can sometimes be useful to get the GPU started processing
        that work earlier in the frame.     */

    /** time spent rendering the scene (gpu work submitted between WaitGetPoses and second Submit)  */
    @JvmField
    var preSubmitGpuMs = 0f
    /** additional time spent rendering by application (e.g. companion window)  */
    @JvmField
    var postSubmitGpuMs = 0f
    /** time between work submitted immediately after present (ideally vsync) until the end of compositor submitted work    */
    @JvmField
    var totalRenderGpuMs = 0f
    /** time spend performing distortion correction, rendering chaperone, overlays, etc.    */
    @JvmField
    var compositorRenderGpuMs = 0f
    /** time spent on cpu submitting the above work for this frame  */
    @JvmField
    var compositorRenderCpuMs = 0f
    /** time spent waiting for running start (application could have used this much more time)  */
    @JvmField
    var compositorIdleCpuMs = 0f


    // Miscellaneous measured intervals.

    /** time between calls to WaitGetPoses  */
    @JvmField
    var clientFrameIntervalMs = 0f
    /** time blocked on call to present (usually 0.0, but can go long)  */
    @JvmField
    var presentCallCpuMs = 0f
    /** time spent spin-waiting for frame index to change (not near-zero indicates wait object failure) */
    @JvmField
    var waitForPresentCpuMs = 0f
    /** time spent in openvr.lib.IVRCompositor::Submit (not near-zero indicates driver issue)   */
    @JvmField
    var submitFrameMs = 0f


    // The following are all relative to this frame's SystemTimeInSeconds 

    @JvmField
    var waitGetPosesCalledMs = 0f

    @JvmField
    var newPosesReadyMs = 0f
    /** second call to IVRCompositor::submit */
    @JvmField
    var newFrameReadyMs = 0f

    @JvmField
    var compositorUpdateStartMs = 0f

    @JvmField
    var compositorUpdateEndMs = 0f

    @JvmField
    var compositorRenderStartMs = 0f


    @JvmField
    var hmdPose = TrackedDevicePose() // pose used by app to render this frame

    constructor()

    constructor(size: Int, frameIndex: Int, numFramePresents: Int, numMisPresented: Int, numDroppedFrames: Int,
                reprojectionFlags: Int, systemTimeInSeconds: Double, preSubmitGpuMs: Float, postSubmitGpuMs: Float,
                totalRenderGpuMs: Float, compositorRenderGpuMs: Float, compositorRenderCpuMs: Float,
                compositorIdleCpuMs: Float, clientFrameIntervalMs: Float, presentCallCpuMs: Float,
                waitForPresentCpuMs: Float, submitFrameMs: Float, waitGetPosesCalledMs: Float, newPosesReadyMs: Float,
                newFrameReadyMs: Float, compositorUpdateStartMs: Float, compositorUpdateEndMs: Float,
                compositorRenderStartMs: Float, hmdPose: TrackedDevicePose) {
        this.size = size
        this.frameIndex = frameIndex
        this.numFramePresents = numFramePresents
        this.numMisPresented = numMisPresented
        this.numDroppedFrames = numDroppedFrames
        this.reprojectionFlags = reprojectionFlags
        this.systemTimeInSeconds = systemTimeInSeconds
        this.preSubmitGpuMs = preSubmitGpuMs
        this.postSubmitGpuMs = postSubmitGpuMs
        this.totalRenderGpuMs = totalRenderGpuMs
        this.compositorRenderGpuMs = compositorRenderGpuMs
        this.compositorRenderCpuMs = compositorRenderCpuMs
        this.compositorIdleCpuMs = compositorIdleCpuMs
        this.clientFrameIntervalMs = clientFrameIntervalMs
        this.presentCallCpuMs = presentCallCpuMs
        this.waitForPresentCpuMs = waitForPresentCpuMs
        this.submitFrameMs = submitFrameMs
        this.waitGetPosesCalledMs = waitGetPosesCalledMs
        this.newPosesReadyMs = newPosesReadyMs
        this.newFrameReadyMs = newFrameReadyMs
        this.compositorUpdateStartMs = compositorUpdateStartMs
        this.compositorUpdateEndMs = compositorUpdateEndMs
        this.compositorRenderStartMs = compositorRenderStartMs
        this.hmdPose = hmdPose
    }

    override fun getFieldOrder() = listOf("size", "frameIndex", "numFramePresents", "numMisPresented",
            "numDroppedFrames", "reprojectionFlags", "systemTimeInSeconds", "preSubmitGpuMs", "postSubmitGpuMs",
            "totalRenderGpuMs", "compositorRenderGpuMs", "compositorRenderCpuMs", "compositorIdleCpuMs",
            "clientFrameIntervalMs", "presentCallCpuMs", "waitForPresentCpuMs", "submitFrameMs",
            "waitGetPosesCalledMs", "newPosesReadyMs", "newFrameReadyMs", "compositorUpdateStartMs",
            "compositorUpdateEndMs", "compositorRenderStartMs", "hmdPose")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : Compositor_FrameTiming(), Structure.ByReference
    class ByValue : Compositor_FrameTiming(), Structure.ByValue
}

/** Cumulative stats for current application.  These are not cleared until a new app connects, but they do stop accumulating once the associated app disconnects. */
open class Compositor_CumulativeStats : Structure {

    /** Process id associated with these stats (may no longer be running).  */
    @JvmField
    var pid = 0
    /** total number of times we called present (includes reprojected frames)   */
    @JvmField
    var numFramePresents = 0
    /** total number of times an old frame was re-scanned out (without reprojection)    */
    @JvmField
    var numDroppedFrames = 0
    /** total number of times a frame was scanned out a second time (with reprojection) */
    @JvmField
    var numReprojectedFrames = 0


    // Values recorded at startup before application has fully faded in the first time.

    @JvmField
    var numFramePresentsOnStartup = 0
    @JvmField
    var numDroppedFramesOnStartup = 0
    @JvmField
    var numReprojectedFramesOnStartup = 0


    /*  Applications may explicitly fade to the compositor.  This is usually to handle level transitions, and loading 
        often causes system wide hitches.  The following stats are collected during this period.  
        Does not include values recorded during startup.    */

    @JvmField
    var numLoading = 0
    @JvmField
    var numFramePresentsLoading = 0
    @JvmField
    var numDroppedFramesLoading = 0
    @JvmField
    var numReprojectedFramesLoading = 0


    /*  If we don't get a new frame from the app in less than 2.5 frames, then we assume the app has hung and 
        start fading back to the compositor.
        The following stats are a result of this, and are a subset of those recorded above.
        Does not include values recorded during start up or loading.    */

    @JvmField
    var numTimedOut = 0
    @JvmField
    var numFramePresentsTimedOut = 0
    @JvmField
    var numDroppedFramesTimedOut = 0
    @JvmField
    var numReprojectedFramesTimedOut = 0

    constructor()

    constructor(pid: Int, numFramePresents: Int, numDroppedFrames: Int, numReprojectedFrames: Int,
                numFramePresentsOnStartup: Int, numDroppedFramesOnStartup: Int, numReprojectedFramesOnStartup: Int,
                numLoading: Int, numFramePresentsLoading: Int, numDroppedFramesLoading: Int,
                numReprojectedFramesLoading: Int, numTimedOut: Int, numFramePresentsTimedOut: Int,
                numDroppedFramesTimedOut: Int, numReprojectedFramesTimedOut: Int) {
        this.pid = pid
        this.numFramePresents = numFramePresents
        this.numDroppedFrames = numDroppedFrames
        this.numReprojectedFrames = numReprojectedFrames
        this.numFramePresentsOnStartup = numFramePresentsOnStartup
        this.numDroppedFramesOnStartup = numDroppedFramesOnStartup
        this.numReprojectedFramesOnStartup = numReprojectedFramesOnStartup
        this.numLoading = numLoading
        this.numFramePresentsLoading = numFramePresentsLoading
        this.numDroppedFramesLoading = numDroppedFramesLoading
        this.numReprojectedFramesLoading = numReprojectedFramesLoading
        this.numTimedOut = numTimedOut
        this.numFramePresentsTimedOut = numFramePresentsTimedOut
        this.numDroppedFramesTimedOut = numDroppedFramesTimedOut
        this.numReprojectedFramesTimedOut = numReprojectedFramesTimedOut
    }

    override fun getFieldOrder()= listOf("pid", "numFramePresents", "numDroppedFrames",
            "numReprojectedFrames", "numFramePresentsOnStartup", "numDroppedFramesOnStartup",
            "numReprojectedFramesOnStartup", "numLoading", "numFramePresentsLoading", "numDroppedFramesLoading",
            "numReprojectedFramesLoading", "numTimedOut", "numFramePresentsTimedOut", "numDroppedFramesTimedOut",
            "numReprojectedFramesTimedOut")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : Compositor_CumulativeStats(), Structure.ByReference
    class ByValue : Compositor_CumulativeStats(), Structure.ByValue
}

/** Allows the application to interact with the compositor */
open class IVRCompositor : Structure {

    /** Setter: sets tracking space returned by WaitGetPoses
     *  Getter: gets current tracking space returned by WaitGetPoses    */
    var trackingSpace
        get() = ETrackingUniverseOrigin.of(GetTrackingSpace!!())
        set(value) = SetTrackingSpace!!(value.i)

    @JvmField
    var SetTrackingSpace: SetTrackingSpace_callback? = null

    interface SetTrackingSpace_callback : Callback {
        operator fun invoke(eOrigin: Int)
    }

    @JvmField
    var GetTrackingSpace: GetTrackingSpace_callback? = null

    interface GetTrackingSpace_callback : Callback {
        operator fun invoke(): Int
    }

    /** Scene applications should call this function to get poses to render with (and optionally poses predicted an additional frame
     *  out to use for gameplay).
     *  This function will block until "running start" milliseconds before the start of the frame, and should be called at the last
     *  moment before needing to start rendering.
     *
     *  Return codes:
     *      - IsNotSceneApplication (make sure to call VR_Init with VRApplicaiton_Scene)
     *      - DoNotHaveFocus (some other app has taken focus - this will throttle the call to 10hz to reduce the impact on that app)    */
    fun waitGetPoses(renderPoseArray: TrackedDevicePose.ByReference, renderPoseArrayCount: Int, gamePoseArray: TrackedDevicePose.ByReference?, gamePoseArrayCount: Int) = WaitGetPoses!!(renderPoseArray, renderPoseArrayCount, gamePoseArray, gamePoseArrayCount)

    @JvmField
    var WaitGetPoses: WaitGetPoses_callback? = null

    interface WaitGetPoses_callback : Callback {
        operator fun invoke(pRenderPoseArray: TrackedDevicePose.ByReference, unRenderPoseArrayCount: Int, pGamePoseArray: TrackedDevicePose.ByReference?, unGamePoseArrayCount: Int): Int
    }

    /** Get the last set of poses returned by WaitGetPoses. */
    fun getLastPoses(renderPoseArray: TrackedDevicePose.ByReference, renderPoseArrayCount: Int, gamePoseArray: TrackedDevicePose.ByReference, gamePoseArrayCount: Int) = GetLastPoses!!(renderPoseArray, renderPoseArrayCount, gamePoseArray, gamePoseArrayCount)

    @JvmField
    var GetLastPoses: GetLastPoses_callback? = null

    interface GetLastPoses_callback : Callback {
        operator fun invoke(pRenderPoseArray: TrackedDevicePose.ByReference, unRenderPoseArrayCount: Int, pGamePoseArray: TrackedDevicePose.ByReference, unGamePoseArrayCount: Int): Int
    }

    /** Interface for accessing last set of poses returned by WaitGetPoses one at a time.
     *  Returns VRCompositorError_IndexOutOfRange if deviceIndex not less than openvr.lib.getK_unMaxTrackedDeviceCount otherwise VRCompositorError_None.
     *  It is okay to pass NULL for either pose if you only want one of the values. */
    fun getLastPoseForTrackedDeviceIndex(deviceIndex: TrackedDeviceIndex, outputPose: TrackedDevicePose.ByReference, outputGamePose: TrackedDevicePose.ByReference) = EVRCompositorError.of(GetLastPoseForTrackedDeviceIndex!!(deviceIndex, outputPose, outputGamePose))

    @JvmField
    var GetLastPoseForTrackedDeviceIndex: GetLastPoseForTrackedDeviceIndex_callback? = null

    interface GetLastPoseForTrackedDeviceIndex_callback : Callback {
        operator fun invoke(unDeviceIndex: TrackedDeviceIndex, pOutputPose: TrackedDevicePose.ByReference, pOutputGamePose: TrackedDevicePose.ByReference): Int
    }

    /** Updated scene texture to display. If bounds is NULL the entire texture will be used.  If called from an OpenGL app, consider adding a glFlush after
     *  Submitting both frames to signal the driver to start processing, otherwise it may wait until the command buffer fills up, causing the app to miss frames.
     *
     *  OpenGL dirty state:
     *	    glBindTexture
     *
     *	Return codes:
     *      - IsNotSceneApplication (make sure to call VR_Init with VRApplicaiton_Scene)
     *      - DoNotHaveFocus (some other app has taken focus)
     *      - TextureIsOnWrongDevice (application did not use proper AdapterIndex - see IVRSystem.GetDXGIOutputInfo)
     *      - SharedTexturesNotSupported (application needs to call CreateDXGIFactory1 or later before creating DX device)
     *      - TextureUsesUnsupportedFormat (scene textures must be compatible with DXGI sharing rules - e.g. uncompressed, no mips, etc.)
     *      - InvalidTexture (usually means bad arguments passed in)
     *      - AlreadySubmitted (app has submitted two left textures or two right textures in a single frame - i.e. before calling
     *          WaitGetPoses again) */
    @JvmOverloads
    fun submit(eye: EVREye, texture: Texture.ByReference, bounds: VRTextureBounds.ByReference? = null, submitFlags: EVRSubmitFlags = EVRSubmitFlags.Default) = EVRCompositorError.of(Submit!!(eye.i, texture, bounds, submitFlags.i))

    @JvmField
    var Submit: Submit_callback? = null

    interface Submit_callback : Callback {
        operator fun invoke(eEye: Int, pTexture: Texture.ByReference, pBounds: VRTextureBounds.ByReference?, nSubmitFlags: Int): Int
    }

    /** Clears the frame that was sent with the last call to Submit. This will cause the compositor to show the grid until Submit is called again. */
    fun clearLastSubmittedFrame() = ClearLastSubmittedFrame!!()

    @JvmField
    var ClearLastSubmittedFrame: ClearLastSubmittedFrame_callback? = null

    interface ClearLastSubmittedFrame_callback : Callback {
        operator fun invoke()
    }

    /** Call immediately after presenting your app's window (i.e. companion window) to unblock the compositor.
     *  This is an optional call, which only needs to be used if you can't instead call WaitGetPoses immediately after Present.
     *  For example, if your engine's render and game loop are not on separate threads, or blocking the render thread until 3ms before the next vsync would
     *  introduce a deadlock of some sort.  This function tells the compositor that you have finished all rendering after having Submitted buffers for both
     *  eyes, and it is free to start its rendering work.  This should only be called from the same thread you are rendering on. */
    fun postPresentHandoff() = PostPresentHandoff!!()

    @JvmField
    var PostPresentHandoff: PostPresentHandoff_callback? = null

    interface PostPresentHandoff_callback : Callback {
        operator fun invoke()
    }

    /** Returns true if timing data is filled it.  Sets oldest timing info if nFramesAgo is larger than the stored history.
     *  Be sure to set timing.size = sizeof(openvr.lib.Compositor_FrameTiming) on struct passed in before calling this function. */
    @JvmOverloads
    fun getFrameTiming(timing: Compositor_FrameTiming.ByReference, framesAgo: Int = 0) = GetFrameTiming!!(timing, framesAgo)

    @JvmField
    var GetFrameTiming: GetFrameTiming_callback? = null

    interface GetFrameTiming_callback : Callback {
        operator fun invoke(pTiming: Compositor_FrameTiming.ByReference, unFramesAgo: Int): Boolean
    }

    /** Interface for copying a range of timing data.  Frames are returned in ascending order (oldest to newest) with the last being
     *  the most recent frame.
     *  Only the first entry's size needs to be set, as the rest will be inferred from that.  Returns total number of entries
     *  filled out. */
    fun getFrameTimings(timing: Compositor_FrameTiming.ByReference, frames: Int) = GetFrameTimings!!(timing, frames)

    @JvmField
    var GetFrameTimings: GetFrameTimings_callback? = null

    interface GetFrameTimings_callback : Callback {
        operator fun invoke(pTiming: Compositor_FrameTiming.ByReference, nFrames: Int): Int
    }

    /** Returns the time in seconds left in the current (as identified by FrameTiming's frameIndex) frame.
     *  Due to "running start", this value may roll over to the next frame before ever reaching 0.0. */
    val frameTimeRemaining get() = GetFrameTimeRemaining!!()

    @JvmField
    var GetFrameTimeRemaining: GetFrameTimeRemaining_callback? = null

    interface GetFrameTimeRemaining_callback : Callback {
        operator fun invoke(): Float
    }

    /** Fills out stats accumulated for the last connected application.  Pass in sizeof( openvr.lib.Compositor_CumulativeStats ) as second parameter. */
    fun getCumulativeStats(stats: Compositor_CumulativeStats.ByReference, statsSizeInBytes: Int) = GetCumulativeStats!!(stats, statsSizeInBytes)

    @JvmField
    var GetCumulativeStats: GetCumulativeStats_callback? = null

    interface GetCumulativeStats_callback : Callback {
        operator fun invoke(pStats: Compositor_CumulativeStats.ByReference, nStatsSizeInBytes: Int)
    }

    /** Fades the view on the HMD to the specified color. The fade will take seconds, and the color values are between 0.0 and 1.0. This color is faded on top
     *  of the scene based on the alpha parameter. Removing the fade color instantly would be FadeToColor( 0.0, 0.0, 0.0, 0.0, 0.0 ).
     *  Values are in un-premultiplied alpha space. */
    @JvmOverloads
    fun fadeToColor(seconds: Float, color: Vec3, alpha: Float, background: Boolean = false) = FadeToColor!!(seconds, color.r, color.g, color.b, alpha, background)

    fun fadeToColor(seconds: Float, color: Vec4, background: Boolean = false) = FadeToColor!!(seconds, color.r, color.g, color.b, color.a, background)
    fun fadeToColor(seconds: Float, red: Float, green: Float, blue: Float, alpha: Float, background: Boolean = false) = FadeToColor!!(seconds, red, green, blue, alpha, background)

    @JvmField
    var FadeToColor: FadeToColor_callback? = null

    interface FadeToColor_callback : Callback {
        operator fun invoke(fSeconds: Float, fRed: Float, fGreen: Float, fBlue: Float, fAlpha: Float, bBackground: Boolean)
    }

    /** Get current fade color value. */
    @JvmOverloads
    fun getCurrentFadeColor(background: Boolean = false) = GetCurrentFadeColor!!(background)

    @JvmField
    var GetCurrentFadeColor: GetCurrentFadeColor_callback? = null

    interface GetCurrentFadeColor_callback : Callback {
        operator fun invoke(bBackground: Boolean): HmdColor
    }

    /** Fading the Grid in or out in seconds */
    fun fadeGrid(seconds: Float, fadeIn: Boolean) = FadeGrid!!(seconds, fadeIn)

    @JvmField
    var FadeGrid: FadeGrid_callback? = null

    interface FadeGrid_callback : Callback {
        operator fun invoke(fSeconds: Float, bFadeIn: Boolean)
    }

    /** Get current alpha value of grid. */
    val currentGridAlpha get() = GetCurrentGridAlpha!!()

    @JvmField
    var GetCurrentGridAlpha: GetCurrentGridAlpha_callback? = null

    interface GetCurrentGridAlpha_callback : Callback {
        operator fun invoke(): Float
    }

    /** Override the skybox used in the compositor (e.g. for during level loads when the app can't feed scene images fast enough)
     *  Order is Front, Back, Left, Right, Top, Bottom.  If only a single texture is passed, it is assumed in lat-long format.
     *  If two are passed, it is assumed a lat-long stereo pair. */
    fun setSkyboxOverride(textures: Texture.ByReference, textureCount: Int) = EVRCompositorError.of(SetSkyboxOverride!!(textures, textureCount))

    @JvmField
    var SetSkyboxOverride: SetSkyboxOverride_callback? = null

    interface SetSkyboxOverride_callback : Callback {
        operator fun invoke(pTextures: Texture.ByReference, unTextureCount: Int): Int
    }

    /** Resets compositor skybox back to defaults. */
    fun clearSkyboxOverride() = ClearSkyboxOverride!!()

    @JvmField
    var ClearSkyboxOverride: ClearSkyboxOverride_callback? = null

    interface ClearSkyboxOverride_callback : Callback {
        operator fun invoke()
    }

    /** Brings the compositor window to the front. This is useful for covering any other window that may be on the HMD and is obscuring the compositor window. */
    fun compositorBringToFront() = CompositorBringToFront!!()

    @JvmField
    var CompositorBringToFront: CompositorBringToFront_callback? = null

    interface CompositorBringToFront_callback : Callback {
        operator fun invoke()
    }

    /** Pushes the compositor window to the back. This is useful for allowing other applications to draw directly to the HMD. */
    fun compositorGoToBack() = CompositorGoToBack!!()

    @JvmField
    var CompositorGoToBack: CompositorGoToBack_callback? = null

    interface CompositorGoToBack_callback : Callback {
        operator fun invoke()
    }

    /** Tells the compositor process to clean up and exit. You do not need to call this function at shutdown. Under normal circumstances the compositor will
     *  manage its own life cycle based on what applications are running. */
    fun compositorQuit() = CompositorQuit!!()

    @JvmField
    var CompositorQuit: CompositorQuit_callback? = null

    interface CompositorQuit_callback : Callback {
        operator fun invoke()
    }

    /** Return whether the compositor is fullscreen */
    val isFullscreen get() = IsFullscreen!!()

    @JvmField
    var IsFullscreen: IsFullscreen_callback? = null

    interface IsFullscreen_callback : Callback {
        operator fun invoke(): Boolean
    }

    /** Returns the process ID of the process that is currently rendering the scene */
    val currentSceneFocusProcess get() = GetCurrentSceneFocusProcess!!()

    @JvmField
    var GetCurrentSceneFocusProcess: GetCurrentSceneFocusProcess_callback? = null

    interface GetCurrentSceneFocusProcess_callback : Callback {
        operator fun invoke(): Int
    }

    /** Returns the process ID of the process that rendered the last frame (or 0 if the compositor itself rendered the frame.)
     *  Returns 0 when fading out from an app and the app's process Id when fading into an app. */
    val lastFrameRenderer get() = GetLastFrameRenderer!!()

    @JvmField
    var GetLastFrameRenderer: GetLastFrameRenderer_callback? = null

    interface GetLastFrameRenderer_callback : Callback {
        operator fun invoke(): Int
    }

    /** Returns true if the current process has the scene focus */
    val canRenderScene get() = CanRenderScene!!()

    @JvmField
    var CanRenderScene: CanRenderScene_callback? = null

    interface CanRenderScene_callback : Callback {
        operator fun invoke(): Boolean
    }

    /** Creates a window on the primary monitor to display what is being shown in the headset. */
    fun showMirrorWindow() = ShowMirrorWindow!!()

    @JvmField
    var ShowMirrorWindow: ShowMirrorWindow_callback? = null

    interface ShowMirrorWindow_callback : Callback {
        operator fun invoke()
    }

    /** Closes the mirror window. */
    fun hideMirrorWindow() = HideMirrorWindow!!()

    @JvmField
    var HideMirrorWindow: HideMirrorWindow_callback? = null

    interface HideMirrorWindow_callback : Callback {
        operator fun invoke()
    }

    /** Returns true if the mirror window is shown. */
    val isMirrorWindowVisible get() = IsMirrorWindowVisible!!()

    @JvmField
    var IsMirrorWindowVisible: IsMirrorWindowVisible_callback? = null

    interface IsMirrorWindowVisible_callback : Callback {
        operator fun invoke(): Boolean
    }

    /** Writes all images that the compositor knows about (including overlays) to a 'screenshots' folder in the SteamVR runtime root. */
    fun compositorDumpImages() = CompositorDumpImages!!()

    @JvmField
    var CompositorDumpImages: CompositorDumpImages_callback? = null

    interface CompositorDumpImages_callback : Callback {
        operator fun invoke()
    }

    /** Let an app know it should be rendering with low resources. */
    val shouldAppRenderWithLowResources get() = ShouldAppRenderWithLowResources!!()

    @JvmField
    var ShouldAppRenderWithLowResources: ShouldAppRenderWithLowResources_callback? = null

    interface ShouldAppRenderWithLowResources_callback : Callback {
        operator fun invoke(): Boolean
    }

    /** Override interleaved reprojection logic to force on. */
    infix fun forceInterleavedReprojectionOn(override: Boolean) = ForceInterleavedReprojectionOn!!(override)

    @JvmField
    var ForceInterleavedReprojectionOn: ForceInterleavedReprojectionOn_callback? = null

    interface ForceInterleavedReprojectionOn_callback : Callback {
        operator fun invoke(bOverride: Boolean)
    }

    /** Force reconnecting to the compositor process. */
    fun forceReconnectProcess() = ForceReconnectProcess!!()

    @JvmField
    var ForceReconnectProcess: ForceReconnectProcess_callback? = null

    interface ForceReconnectProcess_callback : Callback {
        operator fun invoke()
    }

    /** Temporarily suspends rendering (useful for finer control over scene transitions). */
    infix fun suspendRendering(suspend: Boolean) = SuspendRendering!!(suspend)

    @JvmField
    var SuspendRendering: SuspendRendering_callback? = null

    interface SuspendRendering_callback : Callback {
        operator fun invoke(bSuspend: Boolean)
    }

    /** Opens a shared D3D11 texture with the undistorted composited image for each eye. Use ReleaseMirrorTextureD3D11 when finished
     * instead of calling Release on the resource itself. */
    fun getMirrorTextureD3D11(eye: EVREye, d3d11DeviceOrResource: Pointer, d3d11ShaderResourceView: PointerByReference) = EVRCompositorError.of(GetMirrorTextureD3D11!!(eye.i, d3d11DeviceOrResource, d3d11ShaderResourceView))

    @JvmField
    var GetMirrorTextureD3D11: GetMirrorTextureD3D11_callback? = null

    interface GetMirrorTextureD3D11_callback : Callback {
        operator fun invoke(eEye: Int, pD3D11DeviceOrResource: Pointer, ppD3D11ShaderResourceView: PointerByReference): Int
    }


    infix fun releaseMirrorTextureD3D11(d3d11ShaderResourceView: PointerByReference) = ReleaseMirrorTextureD3D11!!(d3d11ShaderResourceView)

    @JvmField
    var ReleaseMirrorTextureD3D11: ReleaseMirrorTextureD3D11_callback? = null

    interface ReleaseMirrorTextureD3D11_callback : Callback {
        operator fun invoke(pD3D11ShaderResourceView: PointerByReference)
    }

    /** Access to mirror textures from OpenGL. */
    fun getMirrorTextureGl(eye: EVREye, textureId: glUInt_ByReference, sharedTextureHandle: glSharedTextureHandle_ByReference) = GetMirrorTextureGL!!(eye.i, textureId, sharedTextureHandle)

    @JvmField
    var GetMirrorTextureGL: GetMirrorTextureGL_callback? = null

    interface GetMirrorTextureGL_callback : Callback {
        operator fun invoke(eEye: Int, pglTextureId: glUInt_ByReference, pglSharedTextureHandle: glSharedTextureHandle_ByReference): Int
    }


    fun releaseSharedGLTexture(glTextureId: glUInt, glSharedTextureHandle: glSharedTextureHandle) = ReleaseSharedGLTexture!!(glTextureId, glSharedTextureHandle)

    @JvmField
    var ReleaseSharedGLTexture: ReleaseSharedGLTexture_callback? = null

    interface ReleaseSharedGLTexture_callback : Callback {
        operator fun invoke(glTextureId: glUInt, glSharedTextureHandle: glSharedTextureHandle): Boolean
    }


    infix fun lockGLSharedTextureForAccess(glSharedTextureHandle: glSharedTextureHandle) = LockGLSharedTextureForAccess!!(glSharedTextureHandle)

    @JvmField
    var LockGLSharedTextureForAccess: LockGLSharedTextureForAccess_callback? = null

    interface LockGLSharedTextureForAccess_callback : Callback {
        operator fun invoke(glSharedTextureHandle: glSharedTextureHandle)
    }


    infix fun unlockGLSharedTextureForAccess(glSharedTextureHandle: glSharedTextureHandle) = UnlockGLSharedTextureForAccess!!(glSharedTextureHandle)

    @JvmField
    var UnlockGLSharedTextureForAccess: UnlockGLSharedTextureForAccess_callback? = null

    interface UnlockGLSharedTextureForAccess_callback : Callback {
        operator fun invoke(glSharedTextureHandle: glSharedTextureHandle)
    }

    /** [Vulkan Only]
     *  return 0. Otherwise it returns the length of the number of bytes necessary to hold this string including the trailing
     *  null.  The string will be a space separated list of-required instance extensions to enable in VkCreateInstance */
    fun getVulkanInstanceExtensionsRequired(value: String, bufferSize: Int) = GetVulkanInstanceExtensionsRequired!!(value, bufferSize)

    @JvmField
    var GetVulkanInstanceExtensionsRequired: GetVulkanInstanceExtensionsRequired_callback? = null

    interface GetVulkanInstanceExtensionsRequired_callback : Callback {
        operator fun invoke(pchValue: String, unBufferSize: Int): Int
    }

    /** [Vulkan only]
     *   return 0. Otherwise it returns the length of the number of bytes necessary to hold this string including the trailing
     *   null.  The string will be a space separated list of required device extensions to enable in VkCreateDevice */
    fun getVulkanDeviceExtensionsRequired(physicalDevice: VkPhysicalDevice.ByReference, value: String, bufferSize: Int) = GetVulkanDeviceExtensionsRequired!!(physicalDevice, value, bufferSize)

    @JvmField
    var GetVulkanDeviceExtensionsRequired: GetVulkanDeviceExtensionsRequired_callback? = null

    interface GetVulkanDeviceExtensionsRequired_callback : Callback {
        operator fun invoke(pPhysicalDevice: VkPhysicalDevice.ByReference, pchValue: String, unBufferSize: Int): Int
    }

    /** [ Vulkan/D3D12 Only ]
     *  There are two purposes for SetExplicitTimingMode:
     *	1. To get a more accurate GPU timestamp for when the frame begins in Vulkan/D3D12 applications.
     *	2. (Optional) To avoid having WaitGetPoses access the Vulkan queue so that the queue can be accessed from
     *	another thread while WaitGetPoses is executing.
     *
     *  More accurate GPU timestamp for the start of the frame is achieved by the application calling
     *  SubmitExplicitTimingData immediately before its first submission to the Vulkan/D3D12 queue.
     *  This is more accurate because normally this GPU timestamp is recorded during WaitGetPoses.  In D3D11,
     *  WaitGetPoses queues a GPU timestamp write, but it does not actually get submitted to the GPU until the
     *  application flushes.  By using SubmitExplicitTimingData, the timestamp is recorded at the same place for
     *  Vulkan/D3D12 as it is for D3D11, resulting in a more accurate GPU time measurement for the frame.
     *
     *  Avoiding WaitGetPoses accessing the Vulkan queue can be achieved using SetExplicitTimingMode as well.
     *  If this is desired, the application should set the timing mode to Explicit_ApplicationPerformsPostPresentHandoff
     *  and *MUST* call PostPresentHandoff itself. If these conditions are met, then WaitGetPoses is guaranteed
     *  not to access the queue.  Note that PostPresentHandoff and SubmitExplicitTimingData will access the queue, so
     *  only WaitGetPoses becomes safe for accessing the queue from another thread. */
    infix fun setExplicitTimingMode(timingMode: EVRCompositorTimingMode) = SetExplicitTimingMode!!(timingMode.i)

    @JvmField
    var SetExplicitTimingMode: SetExplicitTimingMode_callback? = null

    interface SetExplicitTimingMode_callback : Callback {
        operator fun invoke(bExplicitTimingMode: Int)
    }

    /** [ Vulkan/D3D12 Only ]
     *  Submit explicit timing data.  When SetExplicitTimingMode is true, this must be called immediately before
     *  the application's first vkQueueSubmit (Vulkan) or ID3D12CommandQueue::ExecuteCommandLists (D3D12) of each frame.
     *  This function will insert a GPU timestamp write just before the application starts its rendering.  This function
     *  will perform a vkQueueSubmit on Vulkan so must not be done simultaneously with VkQueue operations on another
     *  thread.
     *  Returns VRCompositorError_RequestFailed if SetExplicitTimingMode is not enabled.    */
    fun submitExplicitTimingData() = EVRCompositorError.of(SubmitExplicitTimingData!!())

    @JvmField
    var SubmitExplicitTimingData: SubmitExplicitTimingData_callback? = null

    interface SubmitExplicitTimingData_callback : Callback {
        operator fun invoke(): Int
    }

    constructor()

    override fun getFieldOrder()= listOf("SetTrackingSpace", "GetTrackingSpace", "WaitGetPoses", "GetLastPoses",
            "GetLastPoseForTrackedDeviceIndex", "Submit", "ClearLastSubmittedFrame", "PostPresentHandoff", "GetFrameTiming",
            "GetFrameTimings", "GetFrameTimeRemaining", "GetCumulativeStats", "FadeToColor", "GetCurrentFadeColor", "FadeGrid",
            "GetCurrentGridAlpha", "SetSkyboxOverride", "ClearSkyboxOverride", "CompositorBringToFront", "CompositorGoToBack",
            "CompositorQuit", "IsFullscreen", "GetCurrentSceneFocusProcess", "GetLastFrameRenderer", "CanRenderScene",
            "ShowMirrorWindow", "HideMirrorWindow", "IsMirrorWindowVisible", "CompositorDumpImages", "ShouldAppRenderWithLowResources",
            "ForceInterleavedReprojectionOn", "ForceReconnectProcess", "SuspendRendering", "GetMirrorTextureD3D11",
            "ReleaseMirrorTextureD3D11", "GetMirrorTextureGL", "ReleaseSharedGLTexture", "LockGLSharedTextureForAccess",
            "UnlockGLSharedTextureForAccess", "GetVulkanInstanceExtensionsRequired", "GetVulkanDeviceExtensionsRequired",
            "SetExplicitTimingMode", "SubmitExplicitTimingData")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRCompositor(), Structure.ByReference
    class ByValue : IVRCompositor(), Structure.ByValue
}

val IVRCompositor_Version = "IVRCompositor_022"