package openvr

import com.sun.jna.*
import com.sun.jna.ptr.IntByReference
import java.lang.Double
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
     *  Other VR applications can call this to initate a screenshot outside of user control.
     *  The destination file names do not need an extension, will be replaced with the correct one for the format which is currently .png. */
    fun requestScreenshot(pOutScreenshotHandle: ScreenshotHandle_t_ByReference, type: EVRScreenshotType, pchPreviewFilename: String, pchVRFilename: String)
            = EVRScreenshotError.of(RequestScreenshot!!.invoke(pOutScreenshotHandle, type.i, pchPreviewFilename, pchVRFilename))

    @JvmField var RequestScreenshot: RequestScreenshot_callback? = null

    interface RequestScreenshot_callback : Callback {
        fun invoke(pOutScreenshotHandle: ScreenshotHandle_t_ByReference, type: Int, pchPreviewFilename: String, pchVRFilename: String): Int
    }

    /** Called by the running VR application to indicate that it wishes to be in charge of screenshots.  If the application does not call this, the Compositor
     *  will only support VRScreenshotType_Stereo screenshots that will be captured without notification to the running app.
     *  Once hooked your application will receive a VREvent_RequestScreenshot event when the user presses the buttons to take a screenshot. */
    fun hookScreenshot(pSupportedTypes: Array<EVRScreenshotType>, numTypes: Int): EVRScreenshotError {

        val pointer = Memory((numTypes * Native.getNativeSize(Double.TYPE)).toLong())
        pointer.read(0, pSupportedTypes.map { it.i }.toIntArray(), 0, numTypes) // TODO probably also the other arrays needs Pointer type

        return EVRScreenshotError.of(HookScreenshot!!.invoke(pointer, numTypes))
    }

    @JvmField var HookScreenshot: HookScreenshot_callback? = null

    interface HookScreenshot_callback : Callback {
        fun invoke(pSupportedTypes: Pointer, numTypes: Int): Int
    }

    /** When your application receives a VREvent_RequestScreenshot event, call these functions to get the details of the screenshot request. */
    fun getScreenshotPropertyType(screenshotHandle: ScreenshotHandle_t, pError: EVRScreenshotError_ByReference)
            = EVRScreenshotError.of(GetScreenshotPropertyType!!.invoke(screenshotHandle, pError))

    @JvmField var GetScreenshotPropertyType: GetScreenshotPropertyType_callback? = null

    interface GetScreenshotPropertyType_callback : Callback {
        fun invoke(screenshotHandle: ScreenshotHandle_t, pError: IntByReference): Int
    }

    /** Get the filename for the preview or vr image (see vr::EScreenshotPropertyFilenames).  The return value is the size of the string.   */
    fun getScreenshotPropertyFilename(screenshotHandle: ScreenshotHandle_t, filenameType: EVRScreenshotPropertyFilenames, pchFilename: String,
                                      cchFilename: Int, pError: EVRScreenshotError_ByReference)
            = EVRScreenshotError.of(GetScreenshotPropertyFilename!!.invoke(screenshotHandle, filenameType.i, pchFilename, cchFilename, pError))

    @JvmField var GetScreenshotPropertyFilename: GetScreenshotPropertyFilename_callback? = null

    interface GetScreenshotPropertyFilename_callback : Callback {
        fun invoke(screenshotHandle: ScreenshotHandle_t, filenameType: Int, pchFilename: String, cchFilename: Int, pError: EVRScreenshotError_ByReference): Int
    }

    /** Call this if the application is taking the screen shot will take more than a few ms processing. This will result in an overlay being presented that shows
     *  a completion bar. */
    fun updateScreenshotProgress(screenshotHandle: ScreenshotHandle_t, flProgress: Float)
            = EVRScreenshotError.of(UpdateScreenshotProgress!!.invoke(screenshotHandle, flProgress))

    @JvmField var UpdateScreenshotProgress: UpdateScreenshotProgress_callback? = null

    interface UpdateScreenshotProgress_callback : Callback {
        fun invoke(screenshotHandle: ScreenshotHandle_t, flProgress: Float): Int
    }

    /** Tells the compositor to take an internal screenshot of type VRScreenshotType_Stereo. It will take the current submitted scene textures of the running
     *  application and write them into the preview image and a side-by-side file for the VR image.
     *  This is similiar to request screenshot, but doesn't ever talk to the application, just takes the shot and submits. */
    fun takeStereoScreenshot(pOutScreenshotHandle: ScreenshotHandle_t_ByReference, pchPreviewFilename: String, pchVRFilename: String)
            = EVRScreenshotError.of(TakeStereoScreenshot!!.invoke(pOutScreenshotHandle, pchPreviewFilename, pchVRFilename))

    @JvmField var TakeStereoScreenshot: TakeStereoScreenshot_callback? = null

    interface TakeStereoScreenshot_callback : Callback {
        fun invoke(pOutScreenshotHandle: ScreenshotHandle_t_ByReference, pchPreviewFilename: String, pchVRFilename: String): Int
    }

    /** Submit the completed screenshot.  If Steam is running this will call into the Steam client and upload the screenshot to the screenshots section of the
     *  library for the running application.  If Steam is not running, this function will display a notification to the user that the screenshot was taken.
     *  The paths should be full paths with extensions.
     *  File paths should be absolute including exntensions.
     *  screenshotHandle can be k_unScreenshotHandleInvalid if this was a new shot taking by the app to be saved and not initiated by a user (achievement earned
     *  or something) */
    fun submitScreenshot(screenshotHandle: ScreenshotHandle_t, type: EVRScreenshotType, pchSourcePreviewFilename: String, pchSourceVRFilename: String)
            = EVRScreenshotError.of(SubmitScreenshot!!.invoke(screenshotHandle, type.i, pchSourcePreviewFilename, pchSourceVRFilename))

    @JvmField var SubmitScreenshot: SubmitScreenshot_callback? = null

    interface SubmitScreenshot_callback : Callback {
        fun invoke(screenshotHandle: ScreenshotHandle_t, type: Int, pchSourcePreviewFilename: String, pchSourceVRFilename: String): Int
    }

    constructor()

    override fun getFieldOrder(): List<String> = Arrays.asList("RequestScreenshot", "HookScreenshot", "GetScreenshotPropertyType", "GetScreenshotPropertyFilename",
            "UpdateScreenshotProgress", "TakeStereoScreenshot", "SubmitScreenshot")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRScreenshots(), Structure.ByReference
    class ByValue : IVRScreenshots(), Structure.ByValue
}

val IVRScreenshots_Version = "FnTable:IVRScreenshots_001"