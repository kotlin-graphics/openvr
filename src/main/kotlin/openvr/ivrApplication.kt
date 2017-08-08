package openvr

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.IntByReference
import java.util.*

// ivrapplications.h ==============================================================================================================================================

open class IVRApplications : Structure {

    // ---------------  Application management  --------------- //

    /** Adds an application manifest to the list to load when building the list of installed applications.
     *  Temporary manifests are not automatically loaded */
    @JvmOverloads fun addApplicationManifest(pchApplicationManifestFullPath: String, bTemporary: Boolean = false)
            = AddApplicationManifest!!.invoke(pchApplicationManifestFullPath, bTemporary)

    @JvmField var AddApplicationManifest: AddApplicationManifest_callback? = null

    interface AddApplicationManifest_callback : Callback {
        fun invoke(pchApplicationManifestFullPath: String, bTemporary: Boolean): Int
    }

    /** Removes an application manifest from the list to load when building the list of installed applications. */
    fun removeApplicationManifest(pchApplicationManifestFullPath: String)
            = EVRApplicationError.of(RemoveApplicationManifest!!.invoke(pchApplicationManifestFullPath))

    @JvmField var RemoveApplicationManifest: RemoveApplicationManifest_callback? = null

    interface RemoveApplicationManifest_callback : Callback {
        fun invoke(pchApplicationManifestFullPath: String): Int
    }

    /** Returns true if an application is installed */
    fun isApplicationInstalled(pchAppKey: String) = IsApplicationInstalled!!.invoke(pchAppKey)

    @JvmField var IsApplicationInstalled: IsApplicationInstalled_callback? = null

    interface IsApplicationInstalled_callback : Callback {
        fun invoke(pchAppKey: String): Boolean
    }

    /** Returns the number of applications available in the list */
    fun getApplicationCount() = GetApplicationCount!!.invoke()

    @JvmField var GetApplicationCount: GetApplicationCount_callback? = null

    interface GetApplicationCount_callback : Callback {
        fun invoke(): Int
    }

    /** Returns the key of the specified application. The index is at least 0 and is less than the return value of GetApplicationCount(). The buffer should be
     *  at least openvr.k_unMaxApplicationKeyLength in order to fit the key. */
    fun getApplicationKeyByIndex(unApplicationIndex: Int, pchAppKeyBuffer: String, unAppKeyBufferLen: Int)
            = EVRApplicationError.of(GetApplicationKeyByIndex!!.invoke(unApplicationIndex, pchAppKeyBuffer, unAppKeyBufferLen))

    @JvmField var GetApplicationKeyByIndex: GetApplicationKeyByIndex_callback? = null

    interface GetApplicationKeyByIndex_callback : Callback {
        fun invoke(unApplicationIndex: Int, pchAppKeyBuffer: String, unAppKeyBufferLen: Int): Int
    }

    /** Returns the key of the application for the specified Process Id. The buffer should be at least openvr.k_unMaxApplicationKeyLength in order to fit the key. */
    fun getApplicationKeyByProcessId(unProcessId: Int, pchAppKeyBuffer: String, unAppKeyBufferLen: Int)
            = EVRApplicationError.of(GetApplicationKeyByProcessId!!.invoke(unProcessId, pchAppKeyBuffer, unAppKeyBufferLen))

    @JvmField var GetApplicationKeyByProcessId: GetApplicationKeyByProcessId_callback? = null

    interface GetApplicationKeyByProcessId_callback : Callback {
        fun invoke(unProcessId: Int, pchAppKeyBuffer: String, unAppKeyBufferLen: Int): Int
    }

    /** Launches the application. The existing scene application will exit and then the new application will start.
     *  This call is not valid for dashboard overlay applications. */
    fun launchApplication(pchAppKey: String) = EVRApplicationError.of(LaunchApplication!!.invoke(pchAppKey))

    @JvmField var LaunchApplication: LaunchApplication_callback? = null

    interface LaunchApplication_callback : Callback {
        fun invoke(pchAppKey: String): Int
    }

    /** Launches an instance of an application of value template, with its app key being pchNewAppKey (which must be unique) and optionally override sections
     *  from the manifest file via openvr.AppOverrideKeys_t     */
    fun launchTemplateApplication(pchTemplateAppKey: String, pchNewAppKey: String, pKeys: AppOverrideKeys_t.ByReference, unKeys: Int)
            = EVRApplicationError.of(LaunchTemplateApplication!!.invoke(pchTemplateAppKey, pchNewAppKey, pKeys, unKeys))

    @JvmField var LaunchTemplateApplication: LaunchTemplateApplication_callback? = null

    interface LaunchTemplateApplication_callback : Callback {
        fun invoke(pchTemplateAppKey: String, pchNewAppKey: String, pKeys: AppOverrideKeys_t.ByReference, unKeys: Int): Int
    }

    /** launches the application currently associated with this mime value and passes it the option args, typically the filename or object name of the item being
     *  launched     */
    fun launchApplicationFromMimeType(pchMimeType: String, pchArgs: String) = EVRApplicationError.of(LaunchApplicationFromMimeType!!.invoke(pchMimeType, pchArgs))

    @JvmField var LaunchApplicationFromMimeType: LaunchApplicationFromMimeType_callback? = null

    interface LaunchApplicationFromMimeType_callback : Callback {
        fun invoke(pchMimeType: String, pchArgs: String): Int
    }

    /** Launches the dashboard overlay application if it is not already running. This call is only valid for dashboard overlay applications. */
    fun launchDashboardOverlay(pchAppKey: String) = EVRApplicationError.of(LaunchDashboardOverlay!!.invoke(pchAppKey))

    @JvmField var LaunchDashboardOverlay: LaunchDashboardOverlay_callback? = null

    interface LaunchDashboardOverlay_callback : Callback {
        fun invoke(pchAppKey: String): Int
    }

    /** Cancel a pending launch for an application */
    fun cancelApplicationLaunch(pchAppKey: String) = CancelApplicationLaunch!!.invoke(pchAppKey)

    @JvmField var CancelApplicationLaunch: CancelApplicationLaunch_callback? = null

    interface CancelApplicationLaunch_callback : Callback {
        fun invoke(pchAppKey: String): Boolean
    }

    /** Identifies a running application. openvr.OpenVR can't always tell which process started in response to a URL. This function allows a URL handler (or the process
     *  itself) to identify the app key for the now running application. Passing a process ID of 0 identifies the calling process.
     *  The application must be one that's known to the system via a call to AddApplicationManifest. */
    fun identifyApplication(unProcessId: Int, pchAppKey: String) = EVRApplicationError.of(IdentifyApplication!!.invoke(unProcessId, pchAppKey))

    @JvmField var IdentifyApplication: IdentifyApplication_callback? = null

    interface IdentifyApplication_callback : Callback {
        fun invoke(unProcessId: Int, pchAppKey: String): Int
    }

    /** Returns the process ID for an application. Return 0 if the application was not found or is not running. */
    fun getApplicationProcessId(pchAppKey: String) = GetApplicationProcessId!!.invoke(pchAppKey)

    @JvmField var GetApplicationProcessId: GetApplicationProcessId_callback? = null

    interface GetApplicationProcessId_callback : Callback {
        fun invoke(pchAppKey: String): Int
    }

    /** Returns a string for an applications error */
    fun getApplicationsErrorNameFromEnum(error: EVRApplicationError) = GetApplicationsErrorNameFromEnum!!.invoke(error.i)

    @JvmField var GetApplicationsErrorNameFromEnum: GetApplicationsErrorNameFromEnum_callback? = null

    interface GetApplicationsErrorNameFromEnum_callback : Callback {
        fun invoke(error: Int): String
    }

    // ---------------  Application properties  --------------- //

    /** Returns a value for an application property. The required buffer size to fit this value will be returned. */
    @JvmOverloads fun getApplicationPropertyString(pchAppKey: String, eProperty: EVRApplicationProperty,
                                                   pchPropertyValueBuffer: String, unPropertyValueBufferLen: Int,
                                                   peError: EVRApplicationError_ByReference? = null)
            = GetApplicationPropertyString!!.invoke(pchAppKey, eProperty.i, pchPropertyValueBuffer, unPropertyValueBufferLen, peError)

    @JvmField var GetApplicationPropertyString: GetApplicationPropertyString_callback? = null

    interface GetApplicationPropertyString_callback : Callback {
        fun invoke(pchAppKey: String, eProperty: Int, pchPropertyValueBuffer: String, unPropertyValueBufferLen: Int,
                   peError: EVRApplicationError_ByReference?)
    }

    /** Returns a bool value for an application property. Returns false in all error cases. */
    @JvmOverloads fun getApplicationPropertyBool(pchAppKey: String, eProperty: EVRApplicationProperty,
                                                 peError: EVRApplicationError_ByReference? = null)
            = GetApplicationPropertyBool!!.invoke(pchAppKey, eProperty.i, peError)

    @JvmField var GetApplicationPropertyBool: GetApplicationPropertyBool_callback? = null

    interface GetApplicationPropertyBool_callback : Callback {
        fun invoke(pchAppKey: String, eProperty: Int, peError: EVRApplicationError_ByReference?): Boolean
    }

    /** Returns a uint64 value for an application property. Returns 0 in all error cases. */
    @JvmOverloads fun getApplicationPropertyUint64(pchAppKey: String, eProperty: EVRApplicationProperty,
                                                   peError: EVRApplicationError_ByReference? = null)
            = GetApplicationPropertyUint64!!.invoke(pchAppKey, eProperty.i, peError)

    @JvmField var GetApplicationPropertyUint64: GetApplicationPropertyUint64_callback? = null

    interface GetApplicationPropertyUint64_callback : Callback {
        fun invoke(pchAppKey: String, eProperty: Int, peError: EVRApplicationError_ByReference?): Long
    }

    /** Sets the application auto-launch flag. This is only valid for applications which return true for VRApplicationProperty_IsDashboardOverlay_Bool. */
    fun setApplicationAutoLaunch(pchAppKey: String, bAutoLaunch: Boolean) = EVRApplicationError.of(SetApplicationAutoLaunch!!.invoke(pchAppKey, bAutoLaunch))

    @JvmField var SetApplicationAutoLaunch: SetApplicationAutoLaunch_callback? = null

    interface SetApplicationAutoLaunch_callback : Callback {
        fun invoke(pchAppKey: String, bAutoLaunch: Boolean): Int
    }

    /** Gets the application auto-launch flag. This is only valid for applications which return true for VRApplicationProperty_IsDashboardOverlay_Bool. */
    fun getApplicationAutoLaunch(pchAppKey: String) = GetApplicationAutoLaunch!!.invoke(pchAppKey)

    @JvmField var GetApplicationAutoLaunch: GetApplicationAutoLaunch_callback? = null

    interface GetApplicationAutoLaunch_callback : Callback {
        fun invoke(pchAppKey: String): Boolean
    }

    /** Adds this mime-value to the list of supported mime types for this application*/
    fun setDefaultApplicationForMimeType(pchAppKey: String, pchMimeType: String)
            = EVRApplicationError.of(SetDefaultApplicationForMimeType!!.invoke(pchAppKey, pchMimeType))

    @JvmField var SetDefaultApplicationForMimeType: SetDefaultApplicationForMimeType_callback? = null

    interface SetDefaultApplicationForMimeType_callback : Callback {
        fun invoke(pchAppKey: String, pchMimeType: String): Int
    }

    /** return the app key that will open this mime value */
    fun getDefaultApplicationForMimeType(pchMimeType: String, pchAppKeyBuffer: String, unAppKeyBufferLen: Int)
            = GetDefaultApplicationForMimeType!!.invoke(pchMimeType, pchAppKeyBuffer, unAppKeyBufferLen)

    @JvmField var GetDefaultApplicationForMimeType: GetDefaultApplicationForMimeType_callback? = null

    interface GetDefaultApplicationForMimeType_callback : Callback {
        fun invoke(pchMimeType: String, pchAppKeyBuffer: String, unAppKeyBufferLen: Int): Boolean
    }

    /** Get the list of supported mime types for this application, comma-delimited */
    fun getApplicationSupportedMimeTypes(pchAppKey: String, pchMimeTypesBuffer: String, unMimeTypesBuffer: Int)
            = GetApplicationSupportedMimeTypes!!.invoke(pchAppKey, pchMimeTypesBuffer, unMimeTypesBuffer)

    @JvmField var GetApplicationSupportedMimeTypes: GetApplicationSupportedMimeTypes_callback? = null

    interface GetApplicationSupportedMimeTypes_callback : Callback {
        fun invoke(pchAppKey: String, pchMimeTypesBuffer: String, unMimeTypesBuffer: Int): Boolean
    }

    /** Get the list of app-keys that support this mime value, comma-delimited, the return value is number of bytes you need to return the full string */
    fun getApplicationsThatSupportMimeType(pchMimeType: String, pchAppKeysThatSupportBuffer: String, unAppKeysThatSupportBuffer: Int)
            = GetApplicationsThatSupportMimeType!!.invoke(pchMimeType, pchAppKeysThatSupportBuffer, unAppKeysThatSupportBuffer)

    @JvmField var GetApplicationsThatSupportMimeType: GetApplicationsThatSupportMimeType_callback? = null

    interface GetApplicationsThatSupportMimeType_callback : Callback {
        fun invoke(pchMimeType: String, pchAppKeysThatSupportBuffer: String, unAppKeysThatSupportBuffer: Int): Int
    }

    /** Get the args list from an app launch that had the process already running, you call this when you get a VREvent_ApplicationMimeTypeLoad */
    fun getApplicationLaunchArguments(unHandle: Int, pchArgs: String, unArgs: Int) = GetApplicationLaunchArguments!!.invoke(unHandle, pchArgs, unArgs)

    @JvmField var GetApplicationLaunchArguments: GetApplicationLaunchArguments_callback? = null

    interface GetApplicationLaunchArguments_callback : Callback {
        fun invoke(unHandle: Int, pchArgs: String, unArgs: Int): Int
    }

    // ---------------  Transition methods --------------- //

    /** Returns the app key for the application that is starting up */
    fun getStartingApplication(pchAppKeyBuffer: String, unAppKeyBufferLen: Int)
            = EVRApplicationError.of(GetStartingApplication!!.invoke(pchAppKeyBuffer, unAppKeyBufferLen))

    @JvmField var GetStartingApplication: GetStartingApplication_callback? = null

    interface GetStartingApplication_callback : Callback {
        fun invoke(pchAppKeyBuffer: String, unAppKeyBufferLen: Int): Int
    }

    /** Returns the application transition state */
    fun getTransitionState() = GetTransitionState!!.invoke().toEVRApplicationTransitionState()

    @JvmField var GetTransitionState: GetTransitionState_callback? = null

    interface GetTransitionState_callback : Callback {
        fun invoke(): Int
    }

    /** Returns errors that would prevent the specified application from launching immediately. Calling this function will cause the current scene application to
     *  quit, so only call it when you are actually about to launch something else.
     *  What the caller should do about these failures depends on the failure:
     *      VRApplicationError_OldApplicationQuitting       - An existing application has been told to quit. Wait for a VREvent_ProcessQuit and try again.
     *      VRApplicationError_ApplicationAlreadyStarting   - This application is already starting. This is a permanent failure.
     *      VRApplicationError_LaunchInProgress	            - A different application is already starting. This is a permanent failure.
     *      VRApplicationError_None                         - Go ahead and launch. Everything is clear.     */
    fun performApplicationPrelaunchCheck(pchAppKey: String) = EVRApplicationError.of(PerformApplicationPrelaunchCheck!!.invoke(pchAppKey))

    @JvmField var PerformApplicationPrelaunchCheck: PerformApplicationPrelaunchCheck_callback? = null

    interface PerformApplicationPrelaunchCheck_callback : Callback {
        fun invoke(pchAppKey: String): Int
    }

    /** Returns a string for an application transition state */
    fun getApplicationsTransitionStateNameFromEnum(state: EVRApplicationTransitionState) = GetApplicationsTransitionStateNameFromEnum!!.invoke(state.i)

    @JvmField var GetApplicationsTransitionStateNameFromEnum: GetApplicationsTransitionStateNameFromEnum_callback? = null

    interface GetApplicationsTransitionStateNameFromEnum_callback : Callback {
        fun invoke(state: Int): String
    }

    /** Returns true if the outgoing scene app has requested a save prompt before exiting */
    fun isQuitUserPromptRequested() = IsQuitUserPromptRequested!!.invoke()

    @JvmField var IsQuitUserPromptRequested: IsQuitUserPromptRequested_callback? = null

    interface IsQuitUserPromptRequested_callback : Callback {
        fun invoke(): Boolean
    }

    /** Starts a subprocess within the calling application. This suppresses all application transition UI and automatically identifies the new executable as part
     *  of the same application. On success the calling process should exit immediately.
     *  If working directory is NULL or "" the directory portion of the binary path will be the working directory. */
    fun launchInternalProcess(pchBinaryPath: String, pchArguments: String, pchWorkingDirectory: String)
            = EVRApplicationError.of(LaunchInternalProcess!!.invoke(pchBinaryPath, pchArguments, pchWorkingDirectory))

    @JvmField var LaunchInternalProcess: LaunchInternalProcess_callback? = null

    interface LaunchInternalProcess_callback : Callback {
        fun invoke(pchBinaryPath: String, pchArguments: String, pchWorkingDirectory: String): Int
    }

    /** Returns the current scene process ID according to the application system. A scene process will get scene focus once it starts
     *  rendering, but it will appear here once it calls VR_Init with the Scene application type. */
    fun getCurrentSceneProcessId() = GetCurrentSceneProcessId!!.invoke()

    @JvmField var GetCurrentSceneProcessId: GetCurrentSceneProcessId_callback?=null

    interface GetCurrentSceneProcessId_callback:Callback {
        fun invoke():Int
    }

    constructor()

    override fun getFieldOrder(): List<String> = Arrays.asList("AddApplicationManifest", "RemoveApplicationManifest", "IsApplicationInstalled", "GetApplicationCount",
            "GetApplicationKeyByIndex", "GetApplicationKeyByProcessId", "LaunchApplication", "LaunchTemplateApplication", "LaunchApplicationFromMimeType",
            "LaunchDashboardOverlay", "CancelApplicationLaunch", "IdentifyApplication", "GetApplicationProcessId", "GetApplicationsErrorNameFromEnum",
            "GetApplicationPropertyString", "GetApplicationPropertyBool", "GetApplicationPropertyUint64", "SetApplicationAutoLaunch", "GetApplicationAutoLaunch",
            "SetDefaultApplicationForMimeType", "GetDefaultApplicationForMimeType", "GetApplicationSupportedMimeTypes", "GetApplicationsThatSupportMimeType",
            "GetApplicationLaunchArguments", "GetStartingApplication", "GetTransitionState", "PerformApplicationPrelaunchCheck",
            "GetApplicationsTransitionStateNameFromEnum", "IsQuitUserPromptRequested", "LaunchInternalProcess")

    constructor (peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRApplications(), Structure.ByReference
    class ByValue : IVRApplications(), Structure.ByValue
}

val IVRApplications_Version = "IVRApplications_006"

enum class EVRSettingsError(@JvmField val i: Int) {

    None(0),
    IPCFailed(1),
    WriteFailed(2),
    ReadFailed(3),
    JsonParseFailed(4),
    /** This will be returned if the setting does not appear in the appropriate default file and has not been set   */
    UnsetSettingHasNoDefault(5);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

class EVRSettingsError_ByReference(@JvmField var value: EVRSettingsError = EVRSettingsError.None) : IntByReference(value.i)

// The maximum length of a settings key
val k_unMaxSettingsKeyLength = 128