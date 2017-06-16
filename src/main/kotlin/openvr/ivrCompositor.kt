package openvr

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.PointerByReference
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

val VRCompositor_ReprojectionReason_Cpu = 0x01
val VRCompositor_ReprojectionReason_Gpu = 0x02
/**
 *  This flag indicates the async reprojection mode is active, but does not indicate if reprojection actually happened or not.
 *  Use the ReprojectionReason flags above to check if reprojection was actually applied (i.e. scene texture was reused).
 *  NumFramePresents > 1 also indicates the scene texture was reused, and also the number of times that it was presented in total.  */
val VRCompositor_ReprojectionAsync = 0x04

/** Provides a single frame's timing information to the app */
open class Compositor_FrameTiming : Structure {

    var m_nSize = 0 // Set to sizeof( openvr.Compositor_FrameTiming )
    var m_nFrameIndex = 0
    var m_nNumFramePresents = 0 // number of times this frame was presented
    var m_nNumMisPresented = 0 // number of times this frame was presented on a vsync other than it was originally predicted to
    var m_nNumDroppedFrames = 0 // number of additional times previous frame was scanned out
    var m_nReprojectionFlags = 0

    /** Absolute time reference for comparing frames.  This aligns with the vsync that running start is relative to. */
    var m_flSystemTimeInSeconds = 0.0

    /** These times may include work from other processes due to OS scheduling.
     *  The fewer packets of work these are broken up into, the less likely this will happen.
     *  GPU work can be broken up by calling Flush.  This can sometimes be useful to get the GPU started processing that work earlier in the frame. */
    var m_flPreSubmitGpuMs = 0f // time spent rendering the scene (gpu work submitted between WaitGetPoses and second Submit)
    var m_flPostSubmitGpuMs = 0f // additional time spent rendering by application (e.g. companion window)
    var m_flTotalRenderGpuMs = 0f // time between work submitted immediately after present (ideally vsync) until the end of compositor submitted work
    var m_flCompositorRenderGpuMs = 0f // time spend performing distortion correction, rendering chaperone, overlays, etc.
    var m_flCompositorRenderCpuMs = 0f // time spent on cpu submitting the above work for this frame
    var m_flCompositorIdleCpuMs = 0f // time spent waiting for running start (application could have used this much more time)

    /** Miscellaneous measured intervals. */
    var m_flClientFrameIntervalMs = 0f // time between calls to WaitGetPoses
    var m_flPresentCallCpuMs = 0f // time blocked on call to present (usually 0.0, but can go long)
    var m_flWaitForPresentCpuMs = 0f // time spent spin-waiting for frame index to change (not near-zero indicates wait object failure)
    var m_flSubmitFrameMs = 0f // time spent in openvr.IVRCompositor::Submit (not near-zero indicates driver issue)

    /** The following are all relative to this frame's SystemTimeInSeconds */
    var m_flWaitGetPosesCalledMs = 0f
    var m_flNewPosesReadyMs = 0f
    var m_flNewFrameReadyMs = 0f // second call to openvr.IVRCompositor::Submit
    var m_flCompositorUpdateStartMs = 0f
    var m_flCompositorUpdateEndMs = 0f
    var m_flCompositorRenderStartMs = 0f

    var m_HmdPose = TrackedDevicePose_t() // pose used by app to render this frame

    constructor()

    constructor(m_nSize: Int, m_nFrameIndex: Int, m_nNumFramePresents: Int, m_nNumMisPresented: Int, m_nNumDroppedFrames: Int,
                m_nReprojectionFlags: Int, m_flSystemTimeInSeconds: Double, m_flPreSubmitGpuMs: Float, m_flPostSubmitGpuMs: Float,
                m_flTotalRenderGpuMs: Float, m_flCompositorRenderGpuMs: Float, m_flCompositorRenderCpuMs: Float,
                m_flCompositorIdleCpuMs: Float, m_flClientFrameIntervalMs: Float, m_flPresentCallCpuMs: Float,
                m_flWaitForPresentCpuMs: Float, m_flSubmitFrameMs: Float, m_flWaitGetPosesCalledMs: Float, m_flNewPosesReadyMs: Float,
                m_flNewFrameReadyMs: Float, m_flCompositorUpdateStartMs: Float, m_flCompositorUpdateEndMs: Float,
                m_flCompositorRenderStartMs: Float, m_HmdPose: TrackedDevicePose_t) {
        this.m_nSize = m_nSize
        this.m_nFrameIndex = m_nFrameIndex
        this.m_nNumFramePresents = m_nNumFramePresents
        this.m_nNumMisPresented = m_nNumMisPresented
        this.m_nNumDroppedFrames = m_nNumDroppedFrames
        this.m_nReprojectionFlags = m_nReprojectionFlags
        this.m_flSystemTimeInSeconds = m_flSystemTimeInSeconds
        this.m_flPreSubmitGpuMs = m_flPreSubmitGpuMs
        this.m_flPostSubmitGpuMs = m_flPostSubmitGpuMs
        this.m_flTotalRenderGpuMs = m_flTotalRenderGpuMs
        this.m_flCompositorRenderGpuMs = m_flCompositorRenderGpuMs
        this.m_flCompositorRenderCpuMs = m_flCompositorRenderCpuMs
        this.m_flCompositorIdleCpuMs = m_flCompositorIdleCpuMs
        this.m_flClientFrameIntervalMs = m_flClientFrameIntervalMs
        this.m_flPresentCallCpuMs = m_flPresentCallCpuMs
        this.m_flWaitForPresentCpuMs = m_flWaitForPresentCpuMs
        this.m_flSubmitFrameMs = m_flSubmitFrameMs
        this.m_flWaitGetPosesCalledMs = m_flWaitGetPosesCalledMs
        this.m_flNewPosesReadyMs = m_flNewPosesReadyMs
        this.m_flNewFrameReadyMs = m_flNewFrameReadyMs
        this.m_flCompositorUpdateStartMs = m_flCompositorUpdateStartMs
        this.m_flCompositorUpdateEndMs = m_flCompositorUpdateEndMs
        this.m_flCompositorRenderStartMs = m_flCompositorRenderStartMs
        this.m_HmdPose = m_HmdPose
    }

    override fun getFieldOrder(): List<String> = Arrays.asList("m_nSize", "m_nFrameIndex", "m_nNumFramePresents", "m_nNumMisPresented",
            "m_nNumDroppedFrames", "m_nReprojectionFlags", "m_flSystemTimeInSeconds", "m_flPreSubmitGpuMs", "m_flPostSubmitGpuMs",
            "m_flTotalRenderGpuMs", "m_flCompositorRenderGpuMs", "m_flCompositorRenderCpuMs", "m_flCompositorIdleCpuMs",
            "m_flClientFrameIntervalMs", "m_flPresentCallCpuMs", "m_flWaitForPresentCpuMs", "m_flSubmitFrameMs",
            "m_flWaitGetPosesCalledMs", "m_flNewPosesReadyMs", "m_flNewFrameReadyMs", "m_flCompositorUpdateStartMs",
            "m_flCompositorUpdateEndMs", "m_flCompositorRenderStartMs", "m_HmdPose")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : Compositor_FrameTiming(), Structure.ByReference
    class ByValue : Compositor_FrameTiming(), Structure.ByValue
}

/** Cumulative stats for current application.  These are not cleared until a new app connects, but they do stop accumulating once the associated app disconnects. */
open class Compositor_CumulativeStats : Structure {

    var m_nPid = 0 // Process id associated with these stats (may no longer be running).
    var m_nNumFramePresents = 0 // total number of times we called present (includes reprojected frames)
    var m_nNumDroppedFrames = 0 // total number of times an old frame was re-scanned out (without reprojection)
    var m_nNumReprojectedFrames = 0 // total number of times a frame was scanned out a second time (with reprojection)

    /** Values recorded at startup before application has fully faded in the first time. */
    var m_nNumFramePresentsOnStartup = 0
    var m_nNumDroppedFramesOnStartup = 0
    var m_nNumReprojectedFramesOnStartup = 0

    /** Applications may explicitly fade to the compositor.  This is usually to handle level transitions, and loading often causes
     * system wide hitches.  The following stats are collected during this period.  Does not include values recorded during startup. */
    var m_nNumLoading = 0
    var m_nNumFramePresentsLoading = 0
    var m_nNumDroppedFramesLoading = 0
    var m_nNumReprojectedFramesLoading = 0

    /** If we don't get a new frame from the app in less than 2.5 frames, then we assume the app has hung and start fading back to the compositor.
     *  The following stats are a result of this, and are a subset of those recorded above.
     *  Does not include values recorded during start up or loading. */
    var m_nNumTimedOut = 0
    var m_nNumFramePresentsTimedOut = 0
    var m_nNumDroppedFramesTimedOut = 0
    var m_nNumReprojectedFramesTimedOut = 0

    constructor()

    constructor(m_nPid: Int, m_nNumFramePresents: Int, m_nNumDroppedFrames: Int, m_nNumReprojectedFrames: Int, m_nNumFramePresentsOnStartup: Int,
                m_nNumDroppedFramesOnStartup: Int, m_nNumReprojectedFramesOnStartup: Int, m_nNumLoading: Int, m_nNumFramePresentsLoading: Int,
                m_nNumDroppedFramesLoading: Int, m_nNumReprojectedFramesLoading: Int, m_nNumTimedOut: Int, m_nNumFramePresentsTimedOut: Int,
                m_nNumDroppedFramesTimedOut: Int, m_nNumReprojectedFramesTimedOut: Int) {
        this.m_nPid = m_nPid
        this.m_nNumFramePresents = m_nNumFramePresents
        this.m_nNumDroppedFrames = m_nNumDroppedFrames
        this.m_nNumReprojectedFrames = m_nNumReprojectedFrames
        this.m_nNumFramePresentsOnStartup = m_nNumFramePresentsOnStartup
        this.m_nNumDroppedFramesOnStartup = m_nNumDroppedFramesOnStartup
        this.m_nNumReprojectedFramesOnStartup = m_nNumReprojectedFramesOnStartup
        this.m_nNumLoading = m_nNumLoading
        this.m_nNumFramePresentsLoading = m_nNumFramePresentsLoading
        this.m_nNumDroppedFramesLoading = m_nNumDroppedFramesLoading
        this.m_nNumReprojectedFramesLoading = m_nNumReprojectedFramesLoading
        this.m_nNumTimedOut = m_nNumTimedOut
        this.m_nNumFramePresentsTimedOut = m_nNumFramePresentsTimedOut
        this.m_nNumDroppedFramesTimedOut = m_nNumDroppedFramesTimedOut
        this.m_nNumReprojectedFramesTimedOut = m_nNumReprojectedFramesTimedOut
    }

    override fun getFieldOrder(): List<String> = Arrays.asList("m_nPid", "m_nNumFramePresents", "m_nNumDroppedFrames", "m_nNumReprojectedFrames",
            "m_nNumFramePresentsOnStartup", "m_nNumDroppedFramesOnStartup", "m_nNumReprojectedFramesOnStartup", "m_nNumLoading", "m_nNumFramePresentsLoading",
            "m_nNumDroppedFramesLoading", "m_nNumReprojectedFramesLoading", "m_nNumTimedOut", "m_nNumFramePresentsTimedOut", "m_nNumDroppedFramesTimedOut",
            "m_nNumReprojectedFramesTimedOut")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : Compositor_CumulativeStats(), Structure.ByReference
    class ByValue : Compositor_CumulativeStats(), Structure.ByValue
}

/** Allows the application to interact with the compositor */
open class IVRCompositor : Structure {

    /** Sets tracking space returned by WaitGetPoses */
    fun setTrackingSpace(eOrigin: ETrackingUniverseOrigin) = SetTrackingSpace!!.invoke(eOrigin.i)

    @JvmField var SetTrackingSpace: SetTrackingSpace_callback? = null

    interface SetTrackingSpace_callback : Callback {
        fun invoke(eOrigin: Int)
    }

    /** Gets current tracking space returned by WaitGetPoses */
    fun getTrackingSpace() = ETrackingUniverseOrigin.of(GetTrackingSpace!!.invoke())

    @JvmField var GetTrackingSpace: GetTrackingSpace_callback? = null

    interface GetTrackingSpace_callback : Callback {
        fun invoke(): Int
    }

    /** Scene applications should call this function to get poses to render with (and optionally poses predicted an additional frame
     *  out to use for gameplay).
     *  This function will block until "running start" milliseconds before the start of the frame, and should be called at the last
     *  moment before needing to start rendering.
     *
     *  Return codes:
     *      - IsNotSceneApplication (make sure to call VR_Init with VRApplicaiton_Scene)
     *      - DoNotHaveFocus (some other app has taken focus - this will throttle the call to 10hz to reduce the impact on that app)    */
    fun waitGetPoses(pRenderPoseArray: TrackedDevicePose_t.ByReference, unRenderPoseArrayCount: Int, pGamePoseArray: TrackedDevicePose_t.ByReference?,
                     unGamePoseArrayCount: Int) = WaitGetPoses!!.invoke(pRenderPoseArray, unRenderPoseArrayCount, pGamePoseArray, unGamePoseArrayCount)

    @JvmField var WaitGetPoses: WaitGetPoses_callback? = null

    interface WaitGetPoses_callback : Callback {
        fun invoke(pRenderPoseArray: TrackedDevicePose_t.ByReference, unRenderPoseArrayCount: Int, pGamePoseArray: TrackedDevicePose_t.ByReference?,
                   unGamePoseArrayCount: Int): Int
    }

    /** Get the last set of poses returned by WaitGetPoses. */
    fun getLastPoses(pRenderPoseArray: TrackedDevicePose_t.ByReference, unRenderPoseArrayCount: Int, pGamePoseArray: TrackedDevicePose_t.ByReference,
                     unGamePoseArrayCount: Int) = GetLastPoses!!.invoke(pRenderPoseArray, unRenderPoseArrayCount, pGamePoseArray, unGamePoseArrayCount)

    @JvmField var GetLastPoses: GetLastPoses_callback? = null

    interface GetLastPoses_callback : Callback {
        fun invoke(pRenderPoseArray: TrackedDevicePose_t.ByReference, unRenderPoseArrayCount: Int, pGamePoseArray: TrackedDevicePose_t.ByReference,
                   unGamePoseArrayCount: Int): Int
    }

    /** Interface for accessing last set of poses returned by WaitGetPoses one at a time.
     *  Returns VRCompositorError_IndexOutOfRange if unDeviceIndex not less than openvr.k_unMaxTrackedDeviceCount otherwise VRCompositorError_None.
     *  It is okay to pass NULL for either pose if you only want one of the values. */
    fun getLastPoseForTrackedDeviceIndex(unDeviceIndex: TrackedDeviceIndex_t, pOutputPose: TrackedDevicePose_t.ByReference,
                                         pOutputGamePose: TrackedDevicePose_t.ByReference)
            = EVRCompositorError.of(GetLastPoseForTrackedDeviceIndex!!.invoke(unDeviceIndex, pOutputPose, pOutputGamePose))

    @JvmField var GetLastPoseForTrackedDeviceIndex: GetLastPoseForTrackedDeviceIndex_callback? = null

    interface GetLastPoseForTrackedDeviceIndex_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t, pOutputPose: TrackedDevicePose_t.ByReference, pOutputGamePose: TrackedDevicePose_t.ByReference): Int
    }

    /** Updated scene texture to display. If pBounds is NULL the entire texture will be used.  If called from an OpenGL app, consider adding a glFlush after
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
    @JvmOverloads fun submit(eEye: EVREye, pTexture: Texture_t.ByReference, pBounds: VRTextureBounds_t.ByReference? = null,
                             nSubmitFlags: EVRSubmitFlags = EVRSubmitFlags.Default)
            = EVRCompositorError.of(Submit!!.invoke(eEye.i, pTexture, pBounds, nSubmitFlags.i))

    @JvmField var Submit: Submit_callback? = null

    interface Submit_callback : Callback {
        fun invoke(eEye: Int, pTexture: Texture_t.ByReference, pBounds: VRTextureBounds_t.ByReference?, nSubmitFlags: Int): Int
    }

    /** Clears the frame that was sent with the last call to Submit. This will cause the compositor to show the grid until Submit is called again. */
    fun clearLastSubmittedFrame() = ClearLastSubmittedFrame!!.invoke()

    @JvmField var ClearLastSubmittedFrame: ClearLastSubmittedFrame_callback? = null

    interface ClearLastSubmittedFrame_callback : Callback {
        fun invoke()
    }

    /** Call immediately after presenting your app's window (i.e. companion window) to unblock the compositor.
     *  This is an optional call, which only needs to be used if you can't instead call WaitGetPoses immediately after Present.
     *  For example, if your engine's render and game loop are not on separate threads, or blocking the render thread until 3ms before the next vsync would
     *  introduce a deadlock of some sort.  This function tells the compositor that you have finished all rendering after having Submitted buffers for both
     *  eyes, and it is free to start its rendering work.  This should only be called from the same thread you are rendering on. */
    fun postPresentHandoff() = PostPresentHandoff!!.invoke()

    @JvmField var PostPresentHandoff: PostPresentHandoff_callback? = null

    interface PostPresentHandoff_callback : Callback {
        fun invoke()
    }

    /** Returns true if timing data is filled it.  Sets oldest timing info if nFramesAgo is larger than the stored history.
     *  Be sure to set timing.size = sizeof(openvr.Compositor_FrameTiming) on struct passed in before calling this function. */
    @JvmOverloads fun getFrameTiming(pTiming: Compositor_FrameTiming.ByReference, unFramesAgo: Int = 0)
            = GetFrameTiming!!.invoke(pTiming, unFramesAgo)

    @JvmField var GetFrameTiming: GetFrameTiming_callback? = null

    interface GetFrameTiming_callback : Callback {
        fun invoke(pTiming: Compositor_FrameTiming.ByReference, unFramesAgo: Int): Boolean
    }

    /** Interface for copying a range of timing data.  Frames are returned in ascending order (oldest to newest) with the last being
     *  the most recent frame.
     *  Only the first entry's m_nSize needs to be set, as the rest will be inferred from that.  Returns total number of entries
     *  filled out. */
    fun getFrameTimings(pTiming: Compositor_FrameTiming.ByReference, nFrames: Int) = GetFrameTimings!!.invoke(pTiming, nFrames)

    @JvmField var GetFrameTimings: GetFrameTimings_callback? = null

    interface GetFrameTimings_callback : Callback {
        fun invoke(pTiming: Compositor_FrameTiming.ByReference, nFrames: Int): Int
    }

    /** Returns the time in seconds left in the current (as identified by FrameTiming's frameIndex) frame.
     *  Due to "running start", this value may roll over to the next frame before ever reaching 0.0. */
    fun getFrameTimeRemaining() = GetFrameTimeRemaining!!.invoke()

    @JvmField var GetFrameTimeRemaining: GetFrameTimeRemaining_callback? = null

    interface GetFrameTimeRemaining_callback : Callback {
        fun invoke(): Float
    }

    /** Fills out stats accumulated for the last connected application.  Pass in sizeof( openvr.Compositor_CumulativeStats ) as second parameter. */
    fun getCumulativeStats(pStats: Compositor_CumulativeStats.ByReference, nStatsSizeInBytes: Int) = GetCumulativeStats!!.invoke(pStats, nStatsSizeInBytes)

    @JvmField var GetCumulativeStats: GetCumulativeStats_callback? = null

    interface GetCumulativeStats_callback : Callback {
        fun invoke(pStats: Compositor_CumulativeStats.ByReference, nStatsSizeInBytes: Int)
    }

    /** Fades the view on the HMD to the specified color. The fade will take fSeconds, and the color values are between 0.0 and 1.0. This color is faded on top
     *  of the scene based on the alpha parameter. Removing the fade color instantly would be FadeToColor( 0.0, 0.0, 0.0, 0.0, 0.0 ).
     *  Values are in un-premultiplied alpha space. */
    @JvmOverloads fun fadeToColor(fSeconds: Float, fRed: Float, fGreen: Float, fBlue: Float, fAlpha: Float,
                                  bBackground: Boolean = false)
            = FadeToColor!!.invoke(fSeconds, fRed, fGreen, fBlue, fAlpha, bBackground)

    @JvmField var FadeToColor: FadeToColor_callback? = null

    interface FadeToColor_callback : Callback {
        fun invoke(fSeconds: Float, fRed: Float, fGreen: Float, fBlue: Float, fAlpha: Float, bBackground: Boolean)
    }

    /** Get current fade color value. */
    @JvmOverloads fun getCurrentFadeColor(bBackground: Boolean = false) = GetCurrentFadeColor!!.invoke(bBackground)

    @JvmField var GetCurrentFadeColor: GetCurrentFadeColor_callback? = null

    interface GetCurrentFadeColor_callback : Callback {
        fun invoke(bBackground: Boolean): HmdColor_t
    }

    /** Fading the Grid in or out in fSeconds */
    fun fadeGrid(fSeconds: Float, bFadeIn: Boolean) = FadeGrid!!.invoke(fSeconds, bFadeIn)

    @JvmField var FadeGrid: FadeGrid_callback? = null

    interface FadeGrid_callback : Callback {
        fun invoke(fSeconds: Float, bFadeIn: Boolean)
    }

    /** Get current alpha value of grid. */
    fun getCurrentGridAlpha() = GetCurrentGridAlpha!!.invoke()

    @JvmField var GetCurrentGridAlpha: GetCurrentGridAlpha_callback? = null

    interface GetCurrentGridAlpha_callback : Callback {
        fun invoke(): Float
    }

    /** Override the skybox used in the compositor (e.g. for during level loads when the app can't feed scene images fast enough)
     *  Order is Front, Back, Left, Right, Top, Bottom.  If only a single texture is passed, it is assumed in lat-long format.
     *  If two are passed, it is assumed a lat-long stereo pair. */
    fun setSkyboxOverride(pTextures: Texture_t.ByReference, unTextureCount: Int)
            = EVRCompositorError.of(SetSkyboxOverride!!.invoke(pTextures, unTextureCount))

    @JvmField var SetSkyboxOverride: SetSkyboxOverride_callback? = null

    interface SetSkyboxOverride_callback : Callback {
        fun invoke(pTextures: Texture_t.ByReference, unTextureCount: Int): Int
    }

    /** Resets compositor skybox back to defaults. */
    fun clearSkyboxOverride() = ClearSkyboxOverride!!.invoke()

    @JvmField var ClearSkyboxOverride: ClearSkyboxOverride_callback? = null

    interface ClearSkyboxOverride_callback : Callback {
        fun invoke()
    }

    /** Brings the compositor window to the front. This is useful for covering any other window that may be on the HMD and is obscuring the compositor window. */
    fun compositorBringToFront() = CompositorBringToFront!!.invoke()

    @JvmField var CompositorBringToFront: CompositorBringToFront_callback? = null

    interface CompositorBringToFront_callback : Callback {
        fun invoke()
    }

    /** Pushes the compositor window to the back. This is useful for allowing other applications to draw directly to the HMD. */
    fun compositorGoToBack() = CompositorGoToBack!!.invoke()

    @JvmField var CompositorGoToBack: CompositorGoToBack_callback? = null

    interface CompositorGoToBack_callback : Callback {
        fun invoke()
    }

    /** Tells the compositor process to clean up and exit. You do not need to call this function at shutdown. Under normal circumstances the compositor will
     *  manage its own life cycle based on what applications are running. */
    fun compositorQuit() = CompositorQuit!!.invoke()

    @JvmField var CompositorQuit: CompositorQuit_callback? = null

    interface CompositorQuit_callback : Callback {
        fun invoke()
    }

    /** Return whether the compositor is fullscreen */
    fun isFullscreen() = IsFullscreen!!.invoke()

    @JvmField var IsFullscreen: IsFullscreen_callback? = null

    interface IsFullscreen_callback : Callback {
        fun invoke(): Boolean
    }

    /** Returns the process ID of the process that is currently rendering the scene */
    fun getCurrentSceneFocusProcess() = GetCurrentSceneFocusProcess!!.invoke()

    @JvmField var GetCurrentSceneFocusProcess: GetCurrentSceneFocusProcess_callback? = null

    interface GetCurrentSceneFocusProcess_callback : Callback {
        fun invoke(): Int
    }

    /** Returns the process ID of the process that rendered the last frame (or 0 if the compositor itself rendered the frame.)
     *  Returns 0 when fading out from an app and the app's process Id when fading into an app. */
    fun getLastFrameRenderer() = GetLastFrameRenderer!!.invoke()

    @JvmField var GetLastFrameRenderer: GetLastFrameRenderer_callback? = null

    interface GetLastFrameRenderer_callback : Callback {
        fun invoke(): Int
    }

    /** Returns true if the current process has the scene focus */
    fun canRenderScene() = CanRenderScene!!.invoke()

    @JvmField var CanRenderScene: CanRenderScene_callback? = null

    interface CanRenderScene_callback : Callback {
        fun invoke(): Boolean
    }

    /** Creates a window on the primary monitor to display what is being shown in the headset. */
    fun showMirrorWindow() = ShowMirrorWindow!!.invoke()

    @JvmField var ShowMirrorWindow: ShowMirrorWindow_callback? = null

    interface ShowMirrorWindow_callback : Callback {
        fun invoke()
    }

    /** Closes the mirror window. */
    fun hideMirrorWindow() = HideMirrorWindow!!.invoke()

    @JvmField var HideMirrorWindow: HideMirrorWindow_callback? = null

    interface HideMirrorWindow_callback : Callback {
        fun invoke()
    }

    /** Returns true if the mirror window is shown. */
    fun isMirrorWindowVisible() = IsMirrorWindowVisible!!.invoke()

    @JvmField var IsMirrorWindowVisible: IsMirrorWindowVisible_callback? = null

    interface IsMirrorWindowVisible_callback : Callback {
        fun invoke(): Boolean
    }

    /** Writes all images that the compositor knows about (including overlays) to a 'screenshots' folder in the SteamVR runtime root. */
    fun compositorDumpImages() = CompositorDumpImages!!.invoke()

    @JvmField var CompositorDumpImages: CompositorDumpImages_callback? = null

    interface CompositorDumpImages_callback : Callback {
        fun invoke()
    }

    /** Let an app know it should be rendering with low resources. */
    fun shouldAppRenderWithLowResources() = ShouldAppRenderWithLowResources!!.invoke()

    @JvmField var ShouldAppRenderWithLowResources: ShouldAppRenderWithLowResources_callback? = null

    interface ShouldAppRenderWithLowResources_callback : Callback {
        fun invoke(): Boolean
    }

    /** Override interleaved reprojection logic to force on. */
    fun forceInterleavedReprojectionOn(bOverride: Boolean) = ForceInterleavedReprojectionOn!!.invoke(bOverride)

    @JvmField var ForceInterleavedReprojectionOn: ForceInterleavedReprojectionOn_callback? = null

    interface ForceInterleavedReprojectionOn_callback : Callback {
        fun invoke(bOverride: Boolean)
    }

    /** Force reconnecting to the compositor process. */
    fun forceReconnectProcess() = ForceReconnectProcess!!.invoke()

    @JvmField var ForceReconnectProcess: ForceReconnectProcess_callback? = null

    interface ForceReconnectProcess_callback : Callback {
        fun invoke()
    }

    /** Temporarily suspends rendering (useful for finer control over scene transitions). */
    fun suspendRendering(bSuspend: Boolean) = SuspendRendering!!.invoke(bSuspend)

    @JvmField var SuspendRendering: SuspendRendering_callback? = null

    interface SuspendRendering_callback : Callback {
        fun invoke(bSuspend: Boolean)
    }

    /** Opens a shared D3D11 texture with the undistorted composited image for each eye. Use ReleaseMirrorTextureD3D11 when finished
     * instead of calling Release on the resource itself. */
    fun getMirrorTextureD3D11(eEye: EVREye, pD3D11DeviceOrResource: Pointer, ppD3D11ShaderResourceView: PointerByReference)
            = EVRCompositorError.of(GetMirrorTextureD3D11!!.invoke(eEye.i, pD3D11DeviceOrResource, ppD3D11ShaderResourceView))

    @JvmField var GetMirrorTextureD3D11: GetMirrorTextureD3D11_callback? = null

    interface GetMirrorTextureD3D11_callback : Callback {
        fun invoke(eEye: Int, pD3D11DeviceOrResource: Pointer, ppD3D11ShaderResourceView: PointerByReference): Int
    }

    fun releaseMirrorTextureD3D11(ppD3D11ShaderResourceView: PointerByReference) =
            ReleaseMirrorTextureD3D11!!.invoke(ppD3D11ShaderResourceView)

    @JvmField var ReleaseMirrorTextureD3D11: ReleaseMirrorTextureD3D11_callback? = null

    interface ReleaseMirrorTextureD3D11_callback : Callback {
        fun invoke(pD3D11ShaderResourceView: PointerByReference)
    }

    /** Access to mirror textures from OpenGL. */
    fun getMirrorTextureGL(eEye: EVREye, pglTextureId: glUInt_t_ByReference, pglSharedTextureHandle: glSharedTextureHandle_t_ByReference)
            = GetMirrorTextureGL!!.invoke(eEye.i, pglTextureId, pglSharedTextureHandle)

    @JvmField var GetMirrorTextureGL: GetMirrorTextureGL_callback? = null

    interface GetMirrorTextureGL_callback : Callback {
        fun invoke(eEye: Int, pglTextureId: glUInt_t_ByReference, pglSharedTextureHandle: glSharedTextureHandle_t_ByReference): Int
    }


    fun releaseSharedGLTexture(glTextureId: glUInt_t, glSharedTextureHandle: glSharedTextureHandle_t) =
            ReleaseSharedGLTexture!!.invoke(glTextureId, glSharedTextureHandle)

    @JvmField var ReleaseSharedGLTexture: ReleaseSharedGLTexture_callback? = null

    interface ReleaseSharedGLTexture_callback : Callback {
        fun invoke(glTextureId: glUInt_t, glSharedTextureHandle: glSharedTextureHandle_t): Boolean
    }


    fun lockGLSharedTextureForAccess(glSharedTextureHandle: glSharedTextureHandle_t) = LockGLSharedTextureForAccess!!.invoke(glSharedTextureHandle)
    @JvmField var LockGLSharedTextureForAccess: LockGLSharedTextureForAccess_callback? = null

    interface LockGLSharedTextureForAccess_callback : Callback {
        fun invoke(glSharedTextureHandle: glSharedTextureHandle_t)
    }


    fun unlockGLSharedTextureForAccess(glSharedTextureHandle: glSharedTextureHandle_t) = UnlockGLSharedTextureForAccess!!.invoke(glSharedTextureHandle)
    @JvmField var UnlockGLSharedTextureForAccess: UnlockGLSharedTextureForAccess_callback? = null

    interface UnlockGLSharedTextureForAccess_callback : Callback {
        fun invoke(glSharedTextureHandle: glSharedTextureHandle_t)
    }

    /** [Vulkan Only]
     *  return 0. Otherwise it returns the length of the number of bytes necessary to hold this string including the trailing
     *  null.  The string will be a space separated list of-required instance extensions to enable in VkCreateInstance */
    fun getVulkanInstanceExtensionsRequired(pchValue: String, unBufferSize: Int) =
            GetVulkanInstanceExtensionsRequired!!.invoke(pchValue, unBufferSize)

    @JvmField var GetVulkanInstanceExtensionsRequired: GetVulkanInstanceExtensionsRequired_callback? = null

    interface GetVulkanInstanceExtensionsRequired_callback : Callback {
        fun invoke(pchValue: String, unBufferSize: Int): Int
    }

    /** [Vulkan only]
     *   return 0. Otherwise it returns the length of the number of bytes necessary to hold this string including the trailing
     *   null.  The string will be a space separated list of required device extensions to enable in VkCreateDevice */
    fun getVulkanDeviceExtensionsRequired(pPhysicalDevice: VkPhysicalDevice_T.ByReference, pchValue: String, unBufferSize: Int) =
            GetVulkanDeviceExtensionsRequired!!.invoke(pPhysicalDevice, pchValue, unBufferSize)

    @JvmField var GetVulkanDeviceExtensionsRequired: GetVulkanDeviceExtensionsRequired_callback? = null

    interface GetVulkanDeviceExtensionsRequired_callback : Callback {
        fun invoke(pPhysicalDevice: VkPhysicalDevice_T.ByReference, pchValue: String, unBufferSize: Int): Int
    }

    constructor()

    override fun getFieldOrder(): List<String> = listOf("SetTrackingSpace", "GetTrackingSpace", "WaitGetPoses", "GetLastPoses",
            "GetLastPoseForTrackedDeviceIndex", "Submit", "ClearLastSubmittedFrame", "PostPresentHandoff", "GetFrameTiming",
            "GetFrameTimings", "GetFrameTimeRemaining", "GetCumulativeStats", "FadeToColor", "GetCurrentFadeColor", "FadeGrid",
            "GetCurrentGridAlpha", "SetSkyboxOverride", "ClearSkyboxOverride", "CompositorBringToFront", "CompositorGoToBack",
            "CompositorQuit", "IsFullscreen", "GetCurrentSceneFocusProcess", "GetLastFrameRenderer", "CanRenderScene",
            "ShowMirrorWindow", "HideMirrorWindow", "IsMirrorWindowVisible", "CompositorDumpImages", "ShouldAppRenderWithLowResources",
            "ForceInterleavedReprojectionOn", "ForceReconnectProcess", "SuspendRendering", "GetMirrorTextureD3D11",
            "ReleaseMirrorTextureD3D11", "GetMirrorTextureGL", "ReleaseSharedGLTexture", "LockGLSharedTextureForAccess",
            "UnlockGLSharedTextureForAccess", "GetVulkanInstanceExtensionsRequired", "GetVulkanDeviceExtensionsRequired")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRCompositor(), Structure.ByReference
    class ByValue : IVRCompositor(), Structure.ByValue
}

val IVRCompositor_Version = "FnTable:IVRCompositor_020"