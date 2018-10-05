package lib

import kool.adr
import kool.rem
import kool.stak
import org.lwjgl.openvr.AppOverrideKeys
import org.lwjgl.openvr.OpenVR.IVRApplications
import org.lwjgl.openvr.VRApplications.*
import org.lwjgl.system.MemoryUtil.*
import uno.kotlin.buffers.remaining
import java.nio.ByteBuffer
import java.nio.IntBuffer


// ---------------  Application management  --------------- //

var applicationError = EVRApplicationError.None

private fun setApplicationError_(error: EVRApplicationError) {
    if (applicationError != EVRApplicationError.None)
        applicationError = error
}

/**
 * Adds an application manifest to the list to load when building the list of installed applications.
 *
 * <p>Temporary manifests are not automatically loaded.</p>
 *
 * @param applicationManifestFullPath
 * @param temporary
 */
fun IVRApplications.addApplicationManifest(applicationManifestFullPath: String, temporary: Boolean = false): EVRApplicationError = stak {
    val applicationManifestFullPathEncoded = it.bufferOfAscii(applicationManifestFullPath)
    return EVRApplicationError of nVRApplications_AddApplicationManifest(applicationManifestFullPathEncoded.adr, temporary)
}

/**
 * Removes an application manifest from the list to load when building the list of installed applications.
 *
 * @param applicationManifestFullPath
 */
infix fun IVRApplications.removeApplicationManifest(applicationManifestFullPath: String): EVRApplicationError = stak {
    val applicationManifestFullPathEncoded = it.bufferOfAscii(applicationManifestFullPath)
    return EVRApplicationError of nVRApplications_RemoveApplicationManifest(applicationManifestFullPathEncoded.adr)
}

/**
 * Returns true if an application is installed.
 *
 * @param appKey
 */
infix fun IVRApplications.isApplicationInstalled(appKey: String): Boolean = stak {
    val appKeyEncoded = it.bufferOfAscii(appKey)
    return nVRApplications_IsApplicationInstalled(appKeyEncoded.adr)
}

/** Returns the number of applications available in the list. */
val IVRApplications.applicationCount: Int
    get() = VRApplications_GetApplicationCount()

/**
 * Returns the key of the specified application. The index is at least 0 and is less than the return value of {@link #VRApplications_GetApplicationCount GetApplicationCount}. The buffer should be
 * at least {@link VR#k_unMaxApplicationKeyLength} in order to fit the key.
 *
 * @param applicationIndex
 * @param appKeyBuffer
 */
fun IVRApplications.getApplicationKeyByIndex(applicationIndex: Int, appKeyBuffer: ByteBuffer?): EVRApplicationError {
    return EVRApplicationError of nVRApplications_GetApplicationKeyByIndex(applicationIndex, appKeyBuffer?.adr
            ?: NULL, appKeyBuffer?.rem ?: 0)
}

/**
 * Returns the key of the application for the specified Process Id. The buffer should be at least {@link VR#k_unMaxApplicationKeyLength} in order to fit the key.
 *
 * @param processId
 * @param appKeyBuffer
 */
fun IVRApplications.getApplicationKeyByProcessId(processId: Int, appKeyBuffer: ByteBuffer?): EVRApplicationError {
    return EVRApplicationError of nVRApplications_GetApplicationKeyByProcessId(processId, appKeyBuffer?.adr
            ?: NULL, appKeyBuffer?.rem ?: 0)
}

/**
 * Launches the application. The existing scene application will exit and then the new application will start.
 *
 * <p>This call is not valid for dashboard overlay applications.</p>
 *
 * @param appKey
 */
infix fun IVRApplications.launchApplication(appKey: String): EVRApplicationError = stak {
    val appKeyEncoded = it.bufferOfAscii(appKey)
    return EVRApplicationError of nVRApplications_LaunchApplication(appKeyEncoded.adr)
}

/**
 * Launches an instance of an application of type template, with its app key being {@code newAppKey} (which must be unique) and optionally override
 * sections from the manifest file via {@link AppOverrideKeys}.
 *
 * @param templateAppKey
 * @param newAppKey
 * @param keys
 */
fun IVRApplications.launchTemplateApplication(templateAppKey: String, newAppKey: String, keys: AppOverrideKeys.Buffer): EVRApplicationError = stak {
    val templateAppKeyEncoded = it.bufferOfAscii(templateAppKey)
    val newAppKeyEncoded = it.bufferOfAscii(newAppKey)
    return EVRApplicationError of nVRApplications_LaunchTemplateApplication(templateAppKeyEncoded.adr, newAppKeyEncoded.adr, keys.adr, keys.rem)
}

/**
 * Launches the application currently associated with this mime type and passes it the option args, typically the filename or object name of the item
 * being launched.
 *
 * @param mimeType
 * @param args
 */
fun IVRApplications.launchApplicationFromMimeType(mimeType: String, args: String): EVRApplicationError = stak {
    val mimeTypeEncoded = it.bufferOfAscii(mimeType)
    val argsEncoded = it.bufferOfAscii(args)
    return EVRApplicationError of nVRApplications_LaunchApplicationFromMimeType(mimeTypeEncoded.adr, argsEncoded.adr)
}

/**
 * Launches the dashboard overlay application if it is not already running. This call is only valid for dashboard overlay applications.
 *
 * @param appKey
 */
infix fun IVRApplications.launchDashboardOverlay(appKey: String): EVRApplicationError = stak {
    val appKeyEncoded = it.bufferOfAscii(appKey)
    return EVRApplicationError of nVRApplications_LaunchDashboardOverlay(appKeyEncoded.adr)
}

/**
 * Cancel a pending launch for an application.
 *
 * @param appKey
 */
infix fun IVRApplications.cancelApplicationLaunch(appKey: String): Boolean = stak {
    val appKeyEncoded = it.bufferOfAscii(appKey)
    return nVRApplications_CancelApplicationLaunch(appKeyEncoded.adr)
}

/**
 * Identifies a running application. OpenVR can't always tell which process started in response to a URL. This function allows a URL handler (or the
 * process itself) to identify the app key for the now running application. Passing a process ID of 0 identifies the calling process. The application must
 * be one that's known to the system via a call to {@link #VRApplications_AddApplicationManifest AddApplicationManifest}.
 *
 * @param processId
 * @param appKey
 */
fun IVRApplications.identifyApplication(processId: Int, appKey: String): EVRApplicationError = stak {
    val appKeyEncoded = it.bufferOfAscii(appKey)
    return EVRApplicationError of nVRApplications_IdentifyApplication(processId, appKeyEncoded.adr)
}

/**
 * Returns the process ID for an application. Return 0 if the application was not found or is not running.
 *
 * @param appKey
 */
infix fun IVRApplications.getApplicationProcessId(appKey: String): Int = stak {
    val appKeyEncoded = it.bufferOfAscii(appKey)
    return nVRApplications_GetApplicationProcessId(appKeyEncoded.adr)
}

/**
 * Useless on JVM TODO check
 * Returns a string for an applications error.
 *
 * @param error one of:<br><table><tr><td>{@link VR#EVRApplicationError_VRApplicationError_None}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_AppKeyAlreadyExists}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_NoManifest}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_NoApplication}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_InvalidIndex}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_UnknownApplication}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_IPCFailed}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_ApplicationAlreadyRunning}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_InvalidManifest}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_InvalidApplication}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_LaunchFailed}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_ApplicationAlreadyStarting}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_LaunchInProgress}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_OldApplicationQuitting}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_TransitionAborted}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_IsTemplate}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_SteamVRIsExiting}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_BufferTooSmall}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_PropertyNotSet}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_UnknownProperty}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_InvalidParameter}</td></tr></table>
 */
//@Nullable
//@NativeType("char const *")
//public static String VRApplications_GetApplicationsErrorNameFromEnum(@NativeType("EVRApplicationError") int error) {
//    long __result = nVRApplications_GetApplicationsErrorNameFromEnum (error)
//    return memASCIISafe(__result)
//}


// ---------------  Application properties  --------------- //


/**
 * Returns a value for an application property. The required buffer size to fit this value will be returned.
 *
 * @param appKey
 * @param property                one of:<br><table><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_Name_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_LaunchType_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_WorkingDirectory_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_BinaryPath_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_Arguments_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_URL_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_Description_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_NewsURL_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_ImagePath_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_Source_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_ActionManifestURL_String}</td></tr></table>
 * @param propertyValueBufferLen
 * @param error ~ EVRApplicationError
 */
fun IVRApplications.getApplicationPropertyString(appKey: String, property: EVRApplicationProperty, propertyValueBufferLen: Int = vr.maxPropertyStringSize, error: IntBuffer? = null): String = stak {
    val appKeyEncoded = it.bufferOfAscii(appKey)
    val propertyValueBuffer = it.malloc(propertyValueBufferLen)
    val result = nVRApplications_GetApplicationPropertyString(appKeyEncoded.adr, property.i, propertyValueBuffer.adr, propertyValueBufferLen, error?.adr
            ?: NULL)
    return memASCII(propertyValueBuffer, result - 1)
}

/**
 * Returns a bool value for an application property. Returns false in all error cases.
 *
 * @param appKey
 * @param property one of:<br><table><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_IsDashboardOverlay_Bool}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_IsTemplate_Bool}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_IsInstanced_Bool}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_IsInternal_Bool}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_WantsCompositorPauseInStandby_Bool}</td></tr></table>
 * @param error  ~ EVRApplicationError
 */
fun IVRApplications.getApplicationPropertyBool(appKey: String, property: EVRApplicationProperty, error: IntBuffer? = null): Boolean = stak {
    val appKeyEncoded = it.bufferOfAscii(appKey)
    return nVRApplications_GetApplicationPropertyBool(appKeyEncoded.adr, property.i, error?.adr ?: NULL)
}

/**
 * Returns a uint64 value for an application property. Returns 0 in all error cases.
 *
 * @param appKey
 * @param property must be:<br><table><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_LastLaunchTime_Uint64}</td></tr></table>
 * @param error ~ EVRApplicationError
 */
fun IVRApplications.getApplicationPropertyLong(appKey: String, property: EVRApplicationProperty, error: IntBuffer? = null): Long = stak {
    val appKeyEncoded = it.bufferOfAscii(appKey)
    return nVRApplications_GetApplicationPropertyUint64(appKeyEncoded.adr, property.i, error?.adr ?: NULL)
}

/**
 * Sets the application auto-launch flag. This is only valid for applications which return true for
 * {@link VR#EVRApplicationProperty_VRApplicationProperty_IsDashboardOverlay_Bool}.
 *
 * @param appKey
 * @param autoLaunch
 */
fun IVRApplications.setApplicationAutoLaunch(appKey: String, autoLaunch: Boolean): EVRApplicationError = stak {
    val appKeyEncoded = it.bufferOfAscii(appKey)
    return EVRApplicationError of nVRApplications_SetApplicationAutoLaunch(appKeyEncoded.adr, autoLaunch)
}

/**
 * Gets the application auto-launch flag. This is only valid for applications which return true for
 * {@link VR#EVRApplicationProperty_VRApplicationProperty_IsDashboardOverlay_Bool}.
 *
 * @param appKey
 */
infix fun IVRApplications.getApplicationAutoLaunch(appKey: String): Boolean = stak {
    val appKeyEncoded = it.bufferOfAscii(appKey)
    return VRApplications.nVRApplications_GetApplicationAutoLaunch(appKeyEncoded.adr)
}

/**
 * Adds this mime-type to the list of supported mime types for this application.
 *
 * @param appKey
 * @param mimeType
 */
fun IVRApplications.setDefaultApplicationForMimeType(appKey: String, mimeType: String): EVRApplicationError {
    val appKeyEncoded = appBuffer.bufferOfAscii(appKey)
    val mimeTypeEncoded = appBuffer.bufferOfAscii(mimeType)
    return EVRApplicationError of VRApplications.nVRApplications_SetDefaultApplicationForMimeType(appKeyEncoded.adr, mimeTypeEncoded.adr)
}

/**
 * Return the app key that will open this mime type.
 *
 * @param mimeType
 * @param appKeyBuffer
 */
fun IVRApplications.getDefaultApplicationForMimeType(mimeType: String, appKeyBuffer: ByteBuffer?): Boolean {
    val mimeTypeEncoded = appBuffer.bufferOfAscii(mimeType)
    return VRApplications.nVRApplications_GetDefaultApplicationForMimeType(mimeTypeEncoded.adr, appKeyBuffer?.adr
            ?: NULL, appKeyBuffer?.cap ?: 0)
}

/**
 * Get the list of supported mime types for this application, comma-delimited.
 *
 * @param appKey
 * @param mimeTypesBuffer
 */
fun IVRApplications.getApplicationSupportedMimeTypes(appKey: String, mimeTypesBuffer: ByteBuffer?): Boolean {
    val appKeyEncoded = appBuffer.bufferOfAscii(appKey)
    return VRApplications.nVRApplications_GetApplicationSupportedMimeTypes(appKeyEncoded.adr, mimeTypesBuffer?.adr
            ?: NULL, mimeTypesBuffer?.remaining ?: 0)
}

/**
 * Get the list of app-keys that support this mime type, comma-delimited, the return value is number of bytes you need to return the full string.
 *
 * @param mimeType
 * @param pchAppKeysThatSupportBuffer
 */
fun IVRApplications.getApplicationsThatSupportMimeType(mimeType: String, appKeysThatSupportBufferSize: Int): String {
    val mimeTypeEncoded = appBuffer.bufferOfAscii(mimeType)
    val appKeysThatSupportBuffer = appBuffer.buffer(appKeysThatSupportBufferSize)
    val result = VRApplications.nVRApplications_GetApplicationsThatSupportMimeType(mimeTypeEncoded.adr, appKeysThatSupportBuffer.adr, appKeysThatSupportBufferSize)
    return memASCII(appKeysThatSupportBuffer, result - 1)
}

/**
 * Get the args list from an app launch that had the process already running, you call this when you get a {@link VR#EVREventType_VREvent_ApplicationMimeTypeLoad}.
 *
 * @param handle
 * @param argsSize
 */
fun IVRApplications.getApplicationLaunchArguments(handle: Int, argsSize: Int): String {
    val args = appBuffer.buffer(argsSize)
    val result = VRApplications.nVRApplications_GetApplicationLaunchArguments(handle, args.adr, argsSize)
    return memASCII(args, result - 1)
}

/**
 * Returns the app key for the application that is starting up.
 *
 * @param appKeyBuffer
 */
infix fun IVRApplications.getStartingApplication(appKeyBuffer: ByteBuffer): EVRApplicationError {
    return EVRApplicationError of VRApplications.nVRApplications_GetStartingApplication(appKeyBuffer.adr, appKeyBuffer.rem)
}

/** Returns the application transition state. */
val IVRApplications.transitionState: EVRApplicationTransitionState
    get() = EVRApplicationTransitionState of VRApplications.VRApplications_GetTransitionState()

/**
 * Returns errors that would prevent the specified application from launching immediately. Calling this function will cause the current scene application
 * to quit, so only call it when you are actually about to launch something else.
 *
 * <p>What the caller should do about these failures depends on the failure:</p>
 *
 * <ul>
 * <li>{@link VR#EVRApplicationError_VRApplicationError_OldApplicationQuitting} - An existing application has been told to quit. Wait for a
 * {@link VR#EVREventType_VREvent_ProcessQuit} and try again.</li>
 * <li>{@link VR#EVRApplicationError_VRApplicationError_ApplicationAlreadyStarting} - This application is already starting. This is a permanent failure.</li>
 * <li>{@link VR#EVRApplicationError_VRApplicationError_LaunchInProgress} - A different application is already starting. This is a permanent failure.</li>
 * <li>{@link VR#EVRApplicationError_VRApplicationError_None} - Go ahead and launch. Everything is clear.</li>
 * </ul>
 *
 * @param appKey
 */
infix fun IVRApplications.performApplicationPrelaunchCheck(appKey: String): EVRApplicationError {
    val appKeyEncoded = appBuffer.bufferOfAscii(appKey)
    return EVRApplicationError of VRApplications.nVRApplications_PerformApplicationPrelaunchCheck(appKeyEncoded.adr)
}

/**
 * Returns a string for an application transition state.
 *
 * @param state one of:<br><table><tr><td>{@link VR#EVRApplicationTransitionState_VRApplicationTransition_None}</td></tr><tr><td>{@link VR#EVRApplicationTransitionState_VRApplicationTransition_OldAppQuitSent}</td></tr><tr><td>{@link VR#EVRApplicationTransitionState_VRApplicationTransition_WaitingForExternalLaunch}</td></tr><tr><td>{@link VR#EVRApplicationTransitionState_VRApplicationTransition_NewAppLaunched}</td></tr></table>
 */
infix fun IVRApplications.getApplicationsTransitionStateNameFromEnum(state: EVRApplicationTransitionState): String? {
    val result = VRApplications.nVRApplications_GetApplicationsTransitionStateNameFromEnum(state.i)
    return memASCIISafe(result)
}

/** Returns true if the outgoing scene app has requested a save prompt before exiting. */
val IVRApplications.isQuitUserPromptRequested: Boolean
    get() = VRApplications.VRApplications_IsQuitUserPromptRequested()

/**
 * Starts a subprocess within the calling application. This suppresses all application transition UI and automatically identifies the new executable as
 * part of the same application. On success the calling process should exit immediately. If working directory is {@code NULL} or "" the directory portion of the
 * binary path will be the working directory.
 *
 * @param binaryPath
 * @param arguments
 * @param workingDirectory
 */
fun IVRApplications.launchInternalProcess(binaryPath: String, arguments: String, workingDirectory: String): EVRApplicationError {
    val binaryPathEncoded = appBuffer.bufferOfAscii(binaryPath)
    val argumentsEncoded = appBuffer.bufferOfAscii(arguments)
    val workingDirectoryEncoded = appBuffer.bufferOfAscii(workingDirectory)
    return EVRApplicationError of VRApplications.nVRApplications_LaunchInternalProcess(binaryPathEncoded.adr, argumentsEncoded.adr, workingDirectoryEncoded.adr)
}

/**
 * Returns the current scene process ID according to the application system. A scene process will get scene focus once it starts rendering, but it will
 * appear here once it calls {@code VR_Init} with the Scene application.
 * type.
 */
val IVRApplications.currentSceneProcessId: Int
    get() = VRApplications.VRApplications_GetCurrentSceneProcessId()