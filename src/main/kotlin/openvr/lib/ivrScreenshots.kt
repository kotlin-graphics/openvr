package openvr.lib

import com.sun.jna.*
import com.sun.jna.ptr.IntByReference
import java.util.*

// ivrscreenshots.h ===============================================================================================================================================

/** Errors that can occur with the VR compositor */
enum class EVRScreenshotError(@JvmField val i: Int) {

    None(0),
    RequestFailed(1),
    IncompatibleVersion(100),
    NotFound(101),
    BufferTooSmall(102),
    ScreenshotAlreadyInProgress(108);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class EVRScreenshotError_ByReference(val value: EVRScreenshotError = EVRScreenshotError.None) : IntByReference(value.i)

/** Allows the application to generate screenshots */
open class IVRScreenshots : Structure {

    /** Request a screenshot of the requested type.
     *  A request of the VRScreenshotType_Stereo type will always work. Other types will depend on the underlying application support.
     *  The first file name is for the preview image and should be a regular screenshot (ideally from the left eye). The second is the VR screenshot in the correct
     *  format. They should be in the same aspect ratio.
     *  Formats per type:
     *      VRScreenshotType_Mono: the VR filename is ignored (can be nullptr), this is a normal flat single shot.
     *      VRScreenshotType_Stereo:  The VR image should be a side-by-side with the left eye image on the left.
     *      VRScreenshotType_Cubemap: The VR image should be six square images composited horizontally.
     *      VRScreenshotType_StereoPanorama: above/below with left eye panorama being the above image.  Image is typically square with the panorama being 2x
     *      horizontal.
     *
     *  Note that the VR dashboard will call this function when the user presses the screenshot binding (currently System Button + Trigger).  If Steam is running,
     *  the destination file names will be in %TEMP% and will be copied into Steam's screenshot library for the running application once SubmitScreenshot() is
     *  called.
     *  If Steam is not running, the paths will be in the user's documents folder under Documents\SteamVR\Screenshots.
     *  Other VR applications can call this to initiate a screenshot outside of user control.
     *  The destination file names do not need an extension, will be replaced with the correct one for the format which is currently .png. */
    fun requestScreenshot(outScreenshotHandle: ScreenshotHandle_ByReference, type: EVRScreenshotType, previewFilename: String, vrFilename: String) = EVRScreenshotError.of(RequestScreenshot!!(outScreenshotHandle, type.i, previewFilename, vrFilename))

    @JvmField var RequestScreenshot: RequestScreenshot_callback? = null

    interface RequestScreenshot_callback : Callback {
        operator fun invoke(pOutScreenshotHandle: ScreenshotHandle_ByReference, type: Int, pchPreviewFilename: String, pchVRFilename: String): Int
    }

    /** Called by the running VR application to indicate that it wishes to be in charge of screenshots.  If the application does not call this, the Compositor
     *  will only support VRScreenshotType_Stereo screenshots that will be captured without notification to the running app.
     *  Once hooked your application will receive a VREvent_RequestScreenshot event when the user presses the buttons to take a screenshot. */
    fun hookScreenshot(supportedTypes: Array<EVRScreenshotType>, numTypes: Int): EVRScreenshotError {
        val pointer = Memory((numTypes * Native.getNativeSize(java.lang.Double.TYPE)).toLong())
        pointer.read(0, supportedTypes.map { it.i }.toIntArray(), 0, numTypes) // TODO probably also the other arrays needs Pointer type
        return EVRScreenshotError.of(HookScreenshot!!(pointer, numTypes))
    }

    @JvmField var HookScreenshot: HookScreenshot_callback? = null

    interface HookScreenshot_callback : Callback {
        operator fun invoke(pSupportedTypes: Pointer, numTypes: Int): Int
    }

    /** When your application receives a VREvent_RequestScreenshot event, call these functions to get the details of the screenshot request. */
    fun getScreenshotPropertyType(screenshotHandle: ScreenshotHandle, error: EVRScreenshotError_ByReference) = EVRScreenshotError.of(GetScreenshotPropertyType!!(screenshotHandle, error))

    @JvmField var GetScreenshotPropertyType: GetScreenshotPropertyType_callback? = null

    interface GetScreenshotPropertyType_callback : Callback {
        operator fun invoke(screenshotHandle: ScreenshotHandle, pError: IntByReference): Int
    }

    /** Get the filename for the preview or vr image (see vr::EScreenshotPropertyFilenames).  The return value is the size of the string.   */
    fun getScreenshotPropertyFilename(screenshotHandle: ScreenshotHandle, filenameType: EVRScreenshotPropertyFilenames, filename: String, filenameLen: Int, error: EVRScreenshotError_ByReference) = EVRScreenshotError.of(GetScreenshotPropertyFilename!!(screenshotHandle, filenameType.i, filename, filenameLen, error))

    @JvmField var GetScreenshotPropertyFilename: GetScreenshotPropertyFilename_callback? = null

    interface GetScreenshotPropertyFilename_callback : Callback {
        operator fun invoke(screenshotHandle: ScreenshotHandle, filenameType: Int, pchFilename: String, cchFilename: Int, pError: EVRScreenshotError_ByReference): Int
    }

    /** Call this if the application is taking the screen shot will take more than a few ms processing. This will result in an overlay being presented that shows
     *  a completion bar. */
    fun updateScreenshotProgress(screenshotHandle: ScreenshotHandle, progress: Float) = EVRScreenshotError.of(UpdateScreenshotProgress!!(screenshotHandle, progress))

    @JvmField var UpdateScreenshotProgress: UpdateScreenshotProgress_callback? = null

    interface UpdateScreenshotProgress_callback : Callback {
        operator fun invoke(screenshotHandle: ScreenshotHandle, flProgress: Float): Int
    }

    /** Tells the compositor to take an internal screenshot of type VRScreenshotType_Stereo. It will take the current submitted scene textures of the running
     *  application and write them into the preview image and a side-by-side file for the VR image.
     *  This is similar to request screenshot, but doesn't ever talk to the application, just takes the shot and submits. */
    fun takeStereoScreenshot(outScreenshotHandle: ScreenshotHandle_ByReference, previewFilename: String, vrFilename: String) = EVRScreenshotError.of(TakeStereoScreenshot!!(outScreenshotHandle, previewFilename, vrFilename))

    @JvmField var TakeStereoScreenshot: TakeStereoScreenshot_callback? = null

    interface TakeStereoScreenshot_callback : Callback {
        operator fun invoke(pOutScreenshotHandle: ScreenshotHandle_ByReference, pchPreviewFilename: String, pchVRFilename: String): Int
    }

    /** Submit the completed screenshot.  If Steam is running this will call into the Steam client and upload the screenshot to the screenshots section of the
     *  library for the running application.  If Steam is not running, this function will display a notification to the user that the screenshot was taken.
     *  The paths should be full paths with extensions.
     *  File paths should be absolute including extensions.
     *  screenshotHandle can be screenshotHandleInvalid if this was a new shot taking by the app to be saved and not initiated by a user (achievement earned
     *  or something) */
    fun submitScreenshot(screenshotHandle: ScreenshotHandle, type: EVRScreenshotType, sourcePreviewFilename: String, sourceVRFilename: String) = EVRScreenshotError.of(SubmitScreenshot!!(screenshotHandle, type.i, sourcePreviewFilename, sourceVRFilename))

    @JvmField var SubmitScreenshot: SubmitScreenshot_callback? = null

    interface SubmitScreenshot_callback : Callback {
        operator fun invoke(screenshotHandle: ScreenshotHandle, type: Int, pchSourcePreviewFilename: String, pchSourceVRFilename: String): Int
    }

    constructor()

    override fun getFieldOrder()= listOf("RequestScreenshot", "HookScreenshot", "GetScreenshotPropertyType", "GetScreenshotPropertyFilename",
            "UpdateScreenshotProgress", "TakeStereoScreenshot", "SubmitScreenshot")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRScreenshots(), Structure.ByReference
    class ByValue : IVRScreenshots(), Structure.ByValue
}

val IVRScreenshots_Version = "IVRScreenshots_001"