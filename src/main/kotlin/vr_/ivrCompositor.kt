package vr_

import ab.appBuffer
import glm_.buffer.adr
import glm_.buffer.cap
import glm_.vec4.Vec4
import openvr.lib.EVRCompositorError
import openvr.lib.EVREye
import openvr.lib.EVRSubmitFlags
import org.lwjgl.PointerBuffer
import org.lwjgl.openvr.*
import org.lwjgl.openvr.OpenVR.IVRCompositor
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.system.MemoryUtil.memASCII
import org.lwjgl.vulkan.VkPhysicalDevice
import vkk.adr
import java.nio.IntBuffer


/**
 * Sets tracking space returned by {@link #VRCompositor_WaitGetPoses WaitGetPoses}.
 *
 * @param origin one of:<br><table><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseSeated}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseStanding}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseRawAndUncalibrated}</td></tr></table>
 */
infix fun IVRCompositor.setTrackingSpace(origin: TrackingUniverseOrigin) {
    VRCompositor.VRCompositor_SetTrackingSpace(origin.i)
}

/** Gets current tracking space returned by {@link #VRCompositor_WaitGetPoses WaitGetPoses}. */
val IVRCompositor.trackingSpace: TrackingUniverseOrigin
    get() = TrackingUniverseOrigin of VRCompositor.VRCompositor_GetTrackingSpace()

/**
 *  Scene applications should call this function to get poses to render with (and optionally poses predicted an additional
 *  frame out to use for gameplay).
 *  This function will block until "running start" milliseconds before the start of the frame, and should be called
 *  at the last moment before needing to start rendering.
 *
 * @param renderPoseArray
 * @param gamePoseArray
 */
fun IVRCompositor.waitGetPoses(renderPoseArray: TrackedDevicePose.Buffer, gamePoseArray: TrackedDevicePose.Buffer? = null): EVRCompositorError {
    return EVRCompositorError of VRCompositor.nVRCompositor_WaitGetPoses(renderPoseArray.adr, renderPoseArray.remaining(), gamePoseArray?.adr
            ?: NULL, gamePoseArray?.remaining() ?: 0)
}

/**
 * Get the last set of poses returned by {@link #VRCompositor_WaitGetPoses WaitGetPoses}.
 *
 * @param renderPoseArray
 * @param gamePoseArray
 */
fun IVRCompositor.getLastPoses(renderPoseArray: TrackedDevicePose.Buffer, gamePoseArray: TrackedDevicePose.Buffer): EVRCompositorError {
    return EVRCompositorError of VRCompositor.nVRCompositor_GetLastPoses(renderPoseArray.adr, renderPoseArray.remaining(), gamePoseArray.adr, gamePoseArray.remaining())
}

/**
 * Interface for accessing last set of poses returned by {@link #VRCompositor_WaitGetPoses WaitGetPoses} one at a time.
 *
 * <p>It is okay to pass {@code NULL} for either pose if you only want one of the values.</p>
 *
 * @param deviceIndex
 * @param outputPose
 * @param outputGamePose
 *
 * @return {@link VR#EVRCompositorError_VRCompositorError_IndexOutOfRange} if {@code deviceIndex} not less than {@link VR#k_unMaxTrackedDeviceCount} otherwise
 *         {@link VR#EVRCompositorError_VRCompositorError_None}
 */
fun IVRCompositor.gGetLastPoseForTrackedDeviceIndex(deviceIndex: TrackedDeviceIndex, outputPose: TrackedDevicePose?, outputGamePose: TrackedDevicePose?): EVRCompositorError {
    return EVRCompositorError of VRCompositor.nVRCompositor_GetLastPoseForTrackedDeviceIndex(deviceIndex, outputPose?.adr
            ?: NULL, outputGamePose?.adr ?: NULL)
}

/**
 * Updated scene texture to display.
 *
 * <p>If {@code bounds} is {@code NULL} the entire texture will be used. If called from an OpenGL app, consider adding a
 * {@code glFlush} after submitting both frames to signal the driver to start processing, otherwise it may wait until the command buffer fills up, causing
 * the app to miss frames.</p>
 *
 * <p>OpenGL dirty state: glBindTexture</p>
 *
 * @param eye         one of:<br><table><tr><td>{@link VR#EVREye_Eye_Left}</td><td>{@link VR#EVREye_Eye_Right}</td></tr></table>
 * @param texture
 * @param bounds
 * @param submitFlags one of:<br><table><tr><td>{@link VR#EVRSubmitFlags_Submit_Default}</td><td>{@link VR#EVRSubmitFlags_Submit_LensDistortionAlreadyApplied}</td></tr><tr><td>{@link VR#EVRSubmitFlags_Submit_GlRenderBuffer}</td><td>{@link VR#EVRSubmitFlags_Submit_Reserved}</td></tr><tr><td>{@link VR#EVRSubmitFlags_Submit_TextureWithDepth}</td></tr></table>
 *
 * @return return codes:
 *
 *         <ul>
 *         <li>IsNotSceneApplication (make sure to call VR_Init with VRApplication_Scene)</li>
 *         <li>DoNotHaveFocus (some other app has taken focus)</li>
 *         <li>TextureIsOnWrongDevice (application did not use proper AdapterIndex - see IVRSystem.GetDXGIOutputInfo)</li>
 *         <li>SharedTexturesNotSupported (application needs to call CreateDXGIFactory1 or later before creating DX device)</li>
 *         <li>TextureUsesUnsupportedFormat (scene textures must be compatible with DXGI sharing rules - e.g. uncompressed, no mips, etc.)</li>
 *         <li>InvalidTexture (usually means bad arguments passed in)</li>
 *         <li>AlreadySubmitted (app has submitted two left textures or two right textures in a single frame - i.e. before calling WaitGetPoses again)</li>
 *         </ul>
 */
fun IVRCompositor.submit(eye: EVREye, texture: Texture, bounds: VRTextureBounds? = null, submitFlags: EVRSubmitFlags = EVRSubmitFlags.Default): EVRCompositorError {
    return EVRCompositorError of VRCompositor.nVRCompositor_Submit(eye.i, texture.adr, bounds?.adr
            ?: NULL, submitFlags.i)
}

/** Clears the frame that was sent with the last call to Submit. This will cause the compositor to show the grid until {@link #VRCompositor_Submit Submit} is called again. */
fun IVRCompositor.clearLastSubmittedFrame() = VRCompositor.VRCompositor_ClearLastSubmittedFrame()

/**
 * Call immediately after presenting your app's window (i.e. companion window) to unblock the compositor.
 *
 * <p>This is an optional call, which only needs to be used if you can't instead call {@link #VRCompositor_WaitGetPoses WaitGetPoses} immediately after {@code Present()}. For example, if
 * your engine's render and game loop are not on separate threads, or blocking the render thread until 3ms before the next vsync would introduce a
 * deadlock of some sort. This function tells the compositor that you have finished all rendering after having Submitted buffers for both eyes, and it is
 * free to start its rendering work. This should only be called from the same thread you are rendering on.</p>
 */
fun IVRCompositor.postPresentHandoff() = VRCompositor.VRCompositor_PostPresentHandoff()

/**
 * Returns true if timing data is filled it. Sets oldest timing info if {@code nFramesAgo} is larger than the stored history.
 *
 * <p>Be sure to set {@code timing.size = sizeof(Compositor_FrameTiming)} on struct passed in before calling this function.</p>
 *
 * @param timing
 */
infix fun IVRCompositor.getFrameTiming(timing: CompositorFrameTiming.Buffer): Boolean {
    return VRCompositor.nVRCompositor_GetFrameTiming(timing.adr, timing.remaining())
}

/**
 * Interface for copying a range of timing data. Frames are returned in ascending order (oldest to newest) with the last being the most recent frame. Only
 * the first entry's {@code m_nSize} needs to be set, as the rest will be inferred from that. Returns total number of entries filled out.
 *
 * @param timing
 */
infix fun IVRCompositor.getFrameTimings(timing: CompositorFrameTiming.Buffer): Int {
    return VRCompositor.nVRCompositor_GetFrameTimings(timing.adr, timing.remaining())
}

/**
 * Returns the time in seconds left in the current (as identified by FrameTiming's frameIndex) frame.
 *
 * <p>Due to "running start", this value may roll over to the next frame before ever reaching 0.0.</p>
 */
val IVRCompositor.frameTimeRemaining: Float
    get() = VRCompositor.VRCompositor_GetFrameTimeRemaining()

/**
 * Fills out stats accumulated for the last connected application.
 *
 * @param stats
 */
infix fun IVRCompositor.getCumulativeStats(stats: CompositorCumulativeStats) {
    VRCompositor.nVRCompositor_GetCumulativeStats(stats.adr, CompositorCumulativeStats.SIZEOF)
}

/**
 * Fades the view on the HMD to the specified color.
 *
 * <p>The fade will take {@code seconds}, and the color values are between 0.0 and 1.0. This color is faded on top of the scene based on the alpha
 * parameter. Removing the fade color instantly would be {@code FadeToColor( 0.0, 0.0, 0.0, 0.0, 0.0 )}. Values are in un-premultiplied alpha space.</p>
 *
 * @param seconds
 * @param red
 * @param green
 * @param blue
 * @param alpha
 * @param background
 */
fun IVRCompositor.fadeToColor(seconds: Float, red: Float, green: Float, blue: Float, alpha: Float, background: Boolean = false) {
    VRCompositor.VRCompositor_FadeToColor(seconds, red, green, blue, alpha, background)
}

/**
 * Get current fade color value.
 *
 * @param background
 */
infix fun IVRCompositor.getCurrentFadeColor(background: Boolean): Vec4 {
    val color = vr.HmdColor()
    VRCompositor.nVRCompositor_GetCurrentFadeColor(background, color.adr)
    return Vec4(color.r, color.g, color.b, color.a)
}

/**
 * Fading the Grid in or out in {@code seconds}.
 *
 * @param seconds
 * @param fadeIn
 */
fun IVRCompositor.fadeGrid(seconds: Float, fadeIn: Boolean) {
    VRCompositor.VRCompositor_FadeGrid(seconds, fadeIn)
}

/** Get current alpha value of grid. */
val IVRCompositor.currentGridAlpha: Float
    get() = VRCompositor.VRCompositor_GetCurrentGridAlpha()

/**
 * Override the skybox used in the compositor (e.g. for during level loads when the app can't feed scene images fast enough)
 *
 * <p>Order is Front, Back, Left, Right, Top, Bottom. If only a single texture is passed, it is assumed in lat-long format. If two are passed, it is assumed
 * a lat-long stereo pair.</p>
 *
 * @param textures
 */
infix fun IVRCompositor.setSkyboxOverride(textures: Texture.Buffer): EVRCompositorError {
    return EVRCompositorError of VRCompositor.nVRCompositor_SetSkyboxOverride(textures.adr, textures.remaining())
}

/** Resets compositor skybox back to defaults. */
fun IVRCompositor.clearSkyboxOverride() = VRCompositor.VRCompositor_ClearSkyboxOverride()

/** Brings the compositor window to the front. This is useful for covering any other window that may be on the HMD and is obscuring the compositor window. */
fun IVRCompositor.bringToFront() = VRCompositor.VRCompositor_CompositorBringToFront()

/** Pushes the compositor window to the back. This is useful for allowing other applications to draw directly to the HMD. */
fun IVRCompositor.goToBack() = VRCompositor.VRCompositor_CompositorGoToBack()

/**
 * Tells the compositor process to clean up and exit. You do not need to call this function at shutdown. Under normal circumstances the compositor will
 * manage its own life cycle based on what applications are running.
 */
fun IVRCompositor.quit() = VRCompositor.VRCompositor_CompositorQuit()

/** Return whether the compositor is fullscreen. */
val IVRCompositor.isFullscreen: Boolean
    get() = VRCompositor.VRCompositor_IsFullscreen()

/** Returns the process ID of the process that is currently rendering the scene. */
val IVRCompositor.currentSceneFocusProcess: Int
    get() = VRCompositor.VRCompositor_GetCurrentSceneFocusProcess()

/**
 * Returns the process ID of the process that rendered the last frame (or 0 if the compositor itself rendered the frame).
 *
 * @return 0 when fading out from an app and the app's process Id when fading into an app
 */
val IVRCompositor.lastFrameRenderer: Int
    get() = VRCompositor.VRCompositor_GetLastFrameRenderer()

/** Returns true if the current process has the scene focus. */
val IVRCompositor.canRenderScene: Boolean
    get() = VRCompositor.VRCompositor_CanRenderScene()

/** Creates a window on the primary monitor to display what is being shown in the headset. */
fun IVRCompositor.showMirrorWindow() = VRCompositor.VRCompositor_ShowMirrorWindow()

/** Closes the mirror window. */
fun IVRCompositor.hideMirrorWindow() = VRCompositor.VRCompositor_HideMirrorWindow()

/** Returns true if the mirror window is shown. */
val IVRCompositor.isMirrorWindowVisible: Boolean
    get() = VRCompositor.VRCompositor_IsMirrorWindowVisible()

/** Writes all images that the compositor knows about (including overlays) to a 'screenshots' folder in the SteamVR runtime root. */
fun IVRCompositor.dumpImages() = VRCompositor.VRCompositor_CompositorDumpImages()

/** Let an app know it should be rendering with low resources. */
val IVRCompositor.shouldAppRenderWithLowResources: Boolean
    get() = VRCompositor.VRCompositor_ShouldAppRenderWithLowResources()

/**
 * Override interleaved reprojection logic to force on.
 *
 * @param override
 */
infix fun IVRCompositor.forceInterleavedReprojectionOn(override: Boolean) = VRCompositor.VRCompositor_ForceInterleavedReprojectionOn(override)

/** Force reconnecting to the compositor process. */
fun IVRCompositor.forceReconnectProcess() = VRCompositor.VRCompositor_ForceReconnectProcess()

/**
 * Temporarily suspends rendering (useful for finer control over scene transitions).
 *
 * @param suspend
 */
infix fun IVRCompositor.suspendRendering(suspend: Boolean) = VRCompositor.VRCompositor_SuspendRendering(suspend)

/**
 * Opens a shared D3D11 texture with the undistorted composited image for each eye.
 *
 * <p>Use {@link #VRCompositor_ReleaseMirrorTextureD3D11 ReleaseMirrorTextureD3D11} when finished instead of calling Release on the resource itself.</p>
 *
 * @param eye
 * @param d3D11DeviceOrResource
 * @param d3D11ShaderResourceView
 */
fun IVRCompositor.getMirrorTextureD3D11(eye: EVREye, d3D11DeviceOrResource: Long, d3D11ShaderResourceView: PointerBuffer): EVRCompositorError {
    return EVRCompositorError of VRCompositor.nVRCompositor_GetMirrorTextureD3D11(eye.i, d3D11DeviceOrResource, d3D11ShaderResourceView.adr)
}

/**
 * Releases a shared D3D11 texture.
 *
 * @param d3D11ShaderResourceView
 */
infix fun IVRCompositor.releaseMirrorTextureD3D11(d3D11ShaderResourceView: Long) = VRCompositor.VRCompositor_ReleaseMirrorTextureD3D11(d3D11ShaderResourceView)

/**
 * Access to mirror textures from OpenGL.
 *
 * @param eye
 * @param textureId
 * @param sharedTextureHandle ~ glSharedTextureHandle
 */
fun IVRCompositor.getMirrorTextureGL(eye: EVREye, textureId: IntBuffer, sharedTextureHandle: PointerBuffer): EVRCompositorError {
    return EVRCompositorError of VRCompositor.nVRCompositor_GetMirrorTextureGL(eye.i, textureId.adr, sharedTextureHandle.adr)
}

fun IVRCompositor.releaseSharedGLTexture(glTextureId: glUInt, glSharedTextureHandle: glSharedTextureHandle): Boolean {
    return VRCompositor.VRCompositor_ReleaseSharedGLTexture(glTextureId, glSharedTextureHandle)
}

infix fun IVRCompositor.lockGLSharedTextureForAccess(glSharedTextureHandle: glSharedTextureHandle) {
    VRCompositor.VRCompositor_LockGLSharedTextureForAccess(glSharedTextureHandle)
}

infix fun IVRCompositor.unlockGLSharedTextureForAccess(glSharedTextureHandle: glSharedTextureHandle) {
    VRCompositor.VRCompositor_UnlockGLSharedTextureForAccess(glSharedTextureHandle)
}

/**
 * Returns 0. Otherwise it returns the length of the number of bytes necessary to hold this string including the trailing null.
 * The string will be a space separated list of-required instance extensions to enable in {@code VkCreateInstance}.
 */
val IVRCompositor.vulkanInstanceExtensionsRequired: List<String>
    get() {
        val size = VRCompositor.nVRCompositor_GetVulkanInstanceExtensionsRequired(NULL, 0)
        val buffer = appBuffer.buffer(size)
        return memASCII(buffer, buffer.cap - 1).split(' ')
    }

/**
 * Returns 0. Otherwise it returns the length of the number of bytes necessary to hold this string including the trailing null.
 * The string will be a space separated list of required device extensions to enable in {@code VkCreateDevice}.
 *
 * @param physicalDevice
 * @param unBufferSize
 */
infix fun IVRCompositor.getVulkanDeviceExtensionsRequired(physicalDevice: VkPhysicalDevice): List<String> {
    val size = VRCompositor.nVRCompositor_GetVulkanDeviceExtensionsRequired(physicalDevice.adr, NULL, 0)
    val buffer = appBuffer.buffer(size)
    return memASCII(buffer, size - 1).split(' ')
}

/**
 * <h3>Vulkan/D3D12 Only</h3>
 *
 * <p>There are two purposes for {@code SetExplicitTimingMode}:</p>
 *
 * <ol>
 * <li>To get a more accurate GPU timestamp for when the frame begins in Vulkan/D3D12 applications.</li>
 * <li>(Optional) To avoid having {@link #VRCompositor_WaitGetPoses WaitGetPoses} access the Vulkan queue so that the queue can be accessed from another thread while {@code WaitGetPoses}
 * is executing.</li>
 * </ol>
 *
 * <p>More accurate GPU timestamp for the start of the frame is achieved by the application calling {@link #VRCompositor_SubmitExplicitTimingData SubmitExplicitTimingData} immediately before its first
 * submission to the Vulkan/D3D12 queue. This is more accurate because normally this GPU timestamp is recorded during {@link #VRCompositor_WaitGetPoses WaitGetPoses}. In D3D11,
 * {@code WaitGetPoses} queues a GPU timestamp write, but it does not actually get submitted to the GPU until the application flushes. By using
 * {@code SubmitExplicitTimingData}, the timestamp is recorded at the same place for Vulkan/D3D12 as it is for D3D11, resulting in a more accurate GPU
 * time measurement for the frame.</p>
 *
 * <p>Avoiding {@link #VRCompositor_WaitGetPoses WaitGetPoses} accessing the Vulkan queue can be achieved using {@code SetExplicitTimingMode} as well. If this is desired, the application
 * should set the timing mode to {@link VR#EVRCompositorTimingMode_VRCompositorTimingMode_Explicit_ApplicationPerformsPostPresentHandoff} and <b>MUST</b> call
 * {@link #VRCompositor_PostPresentHandoff PostPresentHandoff} itself. If these conditions are met, then {@code WaitGetPoses} is guaranteed not to access the queue. Note that
 * {@code PostPresentHandoff} and {@code SubmitExplicitTimingData} will access the queue, so only {@code WaitGetPoses} becomes safe for accessing the
 * queue from another thread.</p>
 *
 * @param timingMode
 */
fun IVRCompositor.setExplicitTimingMode(timingMode: EVRCompositorTimingMode)  = VRCompositor.VRCompositor_SetExplicitTimingMode(timingMode.i)

/**
 * <h3>Vulkan/D3D12 Only</h3>
 *
 * <p>Submit explicit timing data. When {@code SetExplicitTimingMode} is true, this must be called immediately before the application's first
 * {@code vkQueueSubmit} (Vulkan) or {@code ID3D12CommandQueue::ExecuteCommandLists} (D3D12) of each frame. This function will insert a GPU timestamp
 * write just before the application starts its rendering. This function will perform a {@code vkQueueSubmit} on Vulkan so must not be done simultaneously
 * with {@code VkQueue} operations on another thread.</p>
 *
 * @return {@link VR#EVRCompositorError_VRCompositorError_RequestFailed} if {@code SetExplicitTimingMode} is not enabled
 */
fun IVRCompositor.submitExplicitTimingData(): EVRCompositorError {
    return EVRCompositorError of VRCompositor.VRCompositor_SubmitExplicitTimingData()
}