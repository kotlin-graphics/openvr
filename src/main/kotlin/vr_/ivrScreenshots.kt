package vr_

import ab.appBuffer
import glm_.buffer.adr
import org.lwjgl.openvr.OpenVR.IVRScreenshots
import org.lwjgl.openvr.VRScreenshots
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.system.MemoryUtil.memASCII
import java.nio.IntBuffer

/**
 * Request a screenshot of the requested type.
 *
 * <p>A request of the {@link VR#EVRScreenshotType_VRScreenshotType_Stereo} type will always work. Other types will depend on the underlying application support.</p>
 *
 * <p>The first file name is for the preview image and should be a regular screenshot (ideally from the left eye). The second is the VR screenshot in the
 * correct format. They should be in the same aspect ratio.</p>
 *
 * <p>Note that the VR dashboard will call this function when the user presses the screenshot binding (currently System Button + Trigger). If Steam is
 * running, the destination file names will be in %TEMP% and will be copied into Steam's screenshot library for the running application once
 * {@link #VRScreenshots_SubmitScreenshot SubmitScreenshot} is called. If Steam is not running, the paths will be in the user's documents folder under Documents\SteamVR\Screenshots. Other VR
 * applications can call this to initate a screenshot outside of user control. The destination file names do not need an extension, will be replaced with
 * the correct one for the format which is currently .png.</p>
 *
 * @param type one of:<br><table><tr><td>{@link VR#EVRScreenshotType_VRScreenshotType_None}</td></tr><tr><td>{@link VR#EVRScreenshotType_VRScreenshotType_Mono}</td></tr><tr><td>{@link VR#EVRScreenshotType_VRScreenshotType_Stereo}</td></tr><tr><td>{@link VR#EVRScreenshotType_VRScreenshotType_Cubemap}</td></tr><tr><td>{@link VR#EVRScreenshotType_VRScreenshotType_MonoPanorama}</td></tr><tr><td>{@link VR#EVRScreenshotType_VRScreenshotType_StereoPanorama}</td></tr></table>
 * @param outScreenshotHandle ~ScreenshotHandle *
 */
fun IVRScreenshots.requestScreenshot(outScreenshotHandle: IntBuffer, type: EVRScreenshotType, previewFilename: String, vrFilename: String): EVRScreenshotError {
    val previewFilenameEncoded = appBuffer.bufferOfAscii(previewFilename)
    val vrFilenameEncoded = appBuffer.bufferOfAscii(vrFilename)
    return EVRScreenshotError of VRScreenshots.nVRScreenshots_RequestScreenshot(outScreenshotHandle.adr, type.i, previewFilenameEncoded.adr, vrFilenameEncoded.adr)
}

/**
 * Called by the running VR application to indicate that it wishes to be in charge of screenshots. If the application does not call this, the Compositor
 * will only support {@link VR#EVRScreenshotType_VRScreenshotType_Stereo} screenshots that will be captured without notification to the running app.
 *
 * <p>Once hooked your application will receive a {@link VR#EVREventType_VREvent_RequestScreenshot} event when the user presses the buttons to take a screenshot.</p>
 * @param supportedTypes ~EVRScreenshotType *
 */
infix fun IVRScreenshots.hookScreenshot(supportedTypes: IntBuffer): EVRScreenshotError {
    return EVRScreenshotError of VRScreenshots.nVRScreenshots_HookScreenshot(supportedTypes.adr, supportedTypes.remaining())
}

/** When your application receives a {@link VR#EVREventType_VREvent_RequestScreenshot} event, call these functions to get the details of the screenshot request.
 *  @param error ~ EVRScreenshotError *
 *  */
fun IVRScreenshots.getScreenshotPropertyType(screenshotHandle: ScreenshotHandle, error: IntBuffer): EVRScreenshotType {
    return EVRScreenshotType of VRScreenshots.nVRScreenshots_GetScreenshotPropertyType(screenshotHandle, error.adr)
}

/**
 * Get the filename for the preview or vr image (see {@code EScreenshotPropertyFilenames}).
 *
 * @param filenameType one of:<br><table><tr><td>{@link VR#EVRScreenshotPropertyFilenames_VRScreenshotPropertyFilenames_Preview}</td></tr><tr><td>{@link VR#EVRScreenshotPropertyFilenames_VRScreenshotPropertyFilenames_VR}</td></tr></table>
 * @param error ~EVRScreenshotError *
 *
 * @return the size of the string
 */
fun IVRScreenshots.getScreenshotPropertyFilename(screenshotHandle: ScreenshotHandle, filenameType: EVRScreenshotPropertyFilenames, error: IntBuffer): String {
    val filenameLen = VRScreenshots.nVRScreenshots_GetScreenshotPropertyFilename(screenshotHandle, filenameType.i, NULL, 0, error.adr)
    val filename = appBuffer.buffer(filenameLen)
    val result = VRScreenshots.nVRScreenshots_GetScreenshotPropertyFilename(screenshotHandle, filenameType.i, filename.adr, filenameLen, error.adr)
    return memASCII(filename, result - 1)
}

/**
 * Call this if the application is taking the screen shot will take more than a few ms processing. This will result in an overlay being presented that
 * shows a completion bar.
 */
fun IVRScreenshots.updateScreenshotProgress(screenshotHandle: ScreenshotHandle, progress: Float): EVRScreenshotError {
    return EVRScreenshotError of VRScreenshots.VRScreenshots_UpdateScreenshotProgress(screenshotHandle, progress)
}

/**
 * Tells the compositor to take an internal screenshot of type {@link VR#EVRScreenshotType_VRScreenshotType_Stereo}. It will take the current submitted scene
 * textures of the running application and write them into the preview image and a side-by-side file for the VR image.
 *
 * <p>This is similiar to request screenshot, but doesn't ever talk to the application, just takes the shot and submits.</p>
 *
 * @param outScreenshotHandle ~ScreenshotHandle *
 */
fun IVRScreenshots.takeStereoScreenshot(outScreenshotHandle: IntBuffer, previewFilename: String, vrFilename: String): EVRScreenshotError {
    val previewFilenameEncoded = appBuffer.bufferOfAscii(previewFilename)
    val vrFilenameEncoded = appBuffer.bufferOfAscii(vrFilename)
    return EVRScreenshotError of VRScreenshots.nVRScreenshots_TakeStereoScreenshot(outScreenshotHandle.adr, previewFilenameEncoded.adr, vrFilenameEncoded.adr)
}

/**
 * Submit the completed screenshot.
 *
 * <p>If Steam is running this will call into the Steam client and upload the screenshot to the screenshots section of the library for the running
 * application. If Steam is not running, this function will display a notification to the user that the screenshot was taken. The paths should be full
 * paths with extensions.</p>
 *
 * <p>File paths should be absolute including extensions.</p>
 *
 * <p>{@code screenshotHandle} can be {@link VR#k_unScreenshotHandleInvalid} if this was a new shot taking by the app to be saved and not initiated by a user
 * (achievement earned or something).</p>
 *
 * @param type one of:<br><table><tr><td>{@link VR#EVRScreenshotType_VRScreenshotType_None}</td></tr><tr><td>{@link VR#EVRScreenshotType_VRScreenshotType_Mono}</td></tr><tr><td>{@link VR#EVRScreenshotType_VRScreenshotType_Stereo}</td></tr><tr><td>{@link VR#EVRScreenshotType_VRScreenshotType_Cubemap}</td></tr><tr><td>{@link VR#EVRScreenshotType_VRScreenshotType_MonoPanorama}</td></tr><tr><td>{@link VR#EVRScreenshotType_VRScreenshotType_StereoPanorama}</td></tr></table>
 */
fun IVRScreenshots.submitScreenshot(screenshotHandle: ScreenshotHandle, type: EVRScreenshotType, sourcePreviewFilename: String, sourceVRFilename: String): EVRScreenshotError {
    val sourcePreviewFilenameEncoded = appBuffer.bufferOfAscii(sourcePreviewFilename)
    val sourceVRFilenameEncoded = appBuffer.bufferOfAscii(sourceVRFilename)
    return EVRScreenshotError of VRScreenshots.nVRScreenshots_SubmitScreenshot(screenshotHandle, type.i, sourcePreviewFilenameEncoded.adr, sourceVRFilenameEncoded.adr)
}