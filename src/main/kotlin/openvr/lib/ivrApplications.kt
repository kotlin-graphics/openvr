package openvr.lib

import kool.*
import kool.lib.toByteArray
import org.lwjgl.openvr.AppOverrideKeys
import org.lwjgl.openvr.OpenVR.IVRApplications
import org.lwjgl.openvr.VRApplications.*
import org.lwjgl.system.MemoryUtil.*
import java.nio.ByteBuffer


/**
 * Application management
 */
object vrApplications : vrInterface {

    /** The maximum length of an application key */
    const val maxKeyLength = 128

    /** Used for all errors reported by the openvr.lib.IVRApplications interface */
    enum class Error(@JvmField val i: Int) {

        None(0),
        /** Only one application can use any given key  */
        AppKeyAlreadyExists(100),
        /** the running application does not have a manifest    */
        NoManifest(101),
        /** No application is running   */
        NoApplication(102),
        InvalidIndex(103),
        /** the application could not be found  */
        UnknownApplication(104),
        /** An IPC failure caused the request to fail   */
        IPCFailed(105),
        ApplicationAlreadyRunning(106),
        InvalidManifest(107),
        InvalidApplication(108),
        /** the process didn't start    */
        LaunchFailed(109),
        /** the system was already starting the same application    */
        ApplicationAlreadyStarting(110),
        /** The system was already starting a different application */
        LaunchInProgress(111),
        OldApplicationQuitting(112),
        TransitionAborted(113),
        /** error when you try to call LaunchApplication() on a template value app (use LaunchTemplateApplication)  */
        IsTemplate(114),
        SteamVRIsExiting(115),
        /** The provided buffer was too small to fit the requested data */
        BufferTooSmall(200),
        /** The requested property was not set  */
        PropertyNotSet(201),
        UnknownProperty(202),
        InvalidParameter(203);

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }

        /**
         * ~ VRApplications_GetApplicationsErrorNameFromEnum
         *
         * Returns a string for an applications error.
         */
        override fun toString(): String = stak { memASCII(nVRApplications_GetApplicationsErrorNameFromEnum(i)) }
    }

    /** these are the properties available on applications. */
    enum class Property(@JvmField val i: Int) {

        Name_String(0),

        LaunchType_String(11),
        WorkingDirectory_String(12),
        BinaryPath_String(13),
        Arguments_String(14),
        URL_String(15),

        Description_String(50),
        NewsURL_String(51),
        ImagePath_String(52),
        Source_String(53),
        ActionManifestURL_String(54),

        IsDashboardOverlay_Bool(60),
        IsTemplate_Bool(61),
        IsInstanced_Bool(62),
        IsInternal_Bool(63),
        WantsCompositorPauseInStandby_Bool(64),

        LastLaunchTime_Uint64(70);

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }

        /** Returns a string for an application transition state. */
        override fun toString(): String = stak { memASCII(nVRApplications_GetApplicationsTransitionStateNameFromEnum(i)) }
    }

    val pError = memCallocInt(1)

    var error: Error
        get() = Error of pError[0]
        set(value) {
            pError[0] = value.i
        }

    /** These are states the scene application startup process will go through. */
    enum class TransitionState(@JvmField val i: Int) {

        None(0),

        OldAppQuitSent(10),
        WaitingForExternalLaunch(11),

        NewAppLaunched(20);

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    /**
     * Adds an application manifest to the list to load when building the list of installed applications.
     *
     * <p>Temporary manifests are not automatically loaded.</p>
     *
     * @param applicationManifestFullPath
     * @param temporary
     */
    fun addApplicationManifest(applicationManifestFullPath: String, temporary: Boolean = false): Error =
            stak.asciiAdr(applicationManifestFullPath) { Error of nVRApplications_AddApplicationManifest(it, temporary) }

    /**
     * Removes an application manifest from the list to load when building the list of installed applications.
     *
     * @param applicationManifestFullPath
     */
    infix fun removeApplicationManifest(applicationManifestFullPath: String): Error =
            stak { Error of nVRApplications_RemoveApplicationManifest(it.asciiAdr(applicationManifestFullPath)) }

    /**
     * Returns true if an application is installed.
     *
     * @param appKey
     */
    infix fun isApplicationInstalled(appKey: String): Boolean =
            stak { nVRApplications_IsApplicationInstalled(it.asciiAdr(appKey)) }

    /** Returns the number of applications available in the list. */
    val applicationCount: Int
        get() = VRApplications_GetApplicationCount()

    /**
     * JVM custom
     * Returns the key of the specified application. The index is at least 0 and is less than the return value of {@link #VRApplications_GetApplicationCount GetApplicationCount}. The buffer should be
     * at least {@link VR#k_unMaxApplicationKeyLength} in order to fit the key.
     *
     * Note: Multi-thread unsafe if reading the error from the class property.
     *
     * @param applicationIndex
     * @param appKeyBuffer
     */
    @JvmOverloads
    fun getApplicationKeyByIndex(applicationIndex: Int, pErr: VRApplicationErrorBuffer = pError): ByteArray =
            stak {
                val buf = it.malloc(maxKeyLength)
                pErr[0] = nVRApplications_GetApplicationKeyByIndex(applicationIndex, buf.adr, maxKeyLength)
                buf.toByteArray()
            }

    /**
     * JVM custom
     *
     * Returns the key of the application for the specified Process Id. The buffer should be at least {@link VR#k_unMaxApplicationKeyLength} in order to fit the key.
     *
     * Note: Multi-thread unsafe if reading the error from the class property.
     *
     * @param processId
     * @param appKeyBuffer
     */
    @JvmOverloads
    fun getApplicationKeyByProcessId(processId: Int, pErr: VRApplicationErrorBuffer = pError): ByteArray =
            stak {
                val buf = it.malloc(maxKeyLength)
                pErr[0] = nVRApplications_GetApplicationKeyByProcessId(processId, buf.adr, maxKeyLength)
                buf.toByteArray()
            }

    /**
     * Launches the application. The existing scene application will exit and then the new application will start.
     *
     * <p>This call is not valid for dashboard overlay applications.</p>
     *
     * @param appKey
     */
    infix fun launchApplication(appKey: String): Error =
            stak { Error of nVRApplications_LaunchApplication(it.asciiAdr(appKey)) }

    /**
     * Launches an instance of an application of type template, with its app key being {@code newAppKey} (which must be unique) and optionally override
     * sections from the manifest file via {@link AppOverrideKeys}.
     *
     * @param templateAppKey
     * @param newAppKey
     * @param keys
     */
    fun launchTemplateApplication(templateAppKey: String, newAppKey: String, keys: AppOverrideKeys.Buffer): Error =
            stak { Error of nVRApplications_LaunchTemplateApplication(it.asciiAdr(templateAppKey), it.asciiAdr(newAppKey), keys.adr, keys.rem) }

    /**
     * Launches the application currently associated with this mime type and passes it the option args, typically the filename or object name of the item
     * being launched.
     *
     * @param mimeType
     * @param args
     */
    fun launchApplicationFromMimeType(mimeType: String, args: String): Error =
            stak { Error of nVRApplications_LaunchApplicationFromMimeType(it.asciiAdr(mimeType), it.asciiAdr(args)) }

    /**
     * Launches the dashboard overlay application if it is not already running. This call is only valid for dashboard overlay applications.
     *
     * @param appKey
     */
    infix fun launchDashboardOverlay(appKey: String): Error =
            stak { Error of nVRApplications_LaunchDashboardOverlay(it.asciiAdr(appKey)) }

    /**
     * Cancel a pending launch for an application.
     *
     * @param appKey
     */
    infix fun cancelApplicationLaunch(appKey: String): Boolean =
            stak.asciiAdr(appKey) { nVRApplications_CancelApplicationLaunch(it) }

    /**
     * Identifies a running application. OpenVR can't always tell which process started in response to a URL. This function allows a URL handler (or the
     * process itself) to identify the app key for the now running application. Passing a process ID of 0 identifies the calling process. The application must
     * be one that's known to the system via a call to {@link #VRApplications_AddApplicationManifest AddApplicationManifest}.
     *
     * @param processId
     * @param appKey
     */
    fun identifyApplication(processId: Int, appKey: String): Error =
            stak { Error of nVRApplications_IdentifyApplication(processId, it.asciiAdr(appKey)) }

    /**
     * Returns the process ID for an application. Return 0 if the application was not found or is not running.
     *
     * @param appKey
     */
    infix fun getApplicationProcessId(appKey: String): Int =
            stak.asciiAdr(appKey) { nVRApplications_GetApplicationProcessId(it) }

    /**
     * Kind of useless on JVM, but it will be offered anyway on the enum itself
     * Returns a string for an applications error.
     *
     * @param error one of:<br><table><tr><td>{@link VR#EVRApplicationError_VRApplicationError_None}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_AppKeyAlreadyExists}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_NoManifest}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_NoApplication}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_InvalidIndex}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_UnknownApplication}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_IPCFailed}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_ApplicationAlreadyRunning}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_InvalidManifest}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_InvalidApplication}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_LaunchFailed}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_ApplicationAlreadyStarting}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_LaunchInProgress}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_OldApplicationQuitting}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_TransitionAborted}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_IsTemplate}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_SteamVRIsExiting}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_BufferTooSmall}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_PropertyNotSet}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_UnknownProperty}</td></tr><tr><td>{@link VR#EVRApplicationError_VRApplicationError_InvalidParameter}</td></tr></table>
     */
    //@Nullable
    //@NativeType("char const *")
    //public static String VRApplications_GetApplicationsErrorNameFromEnum(@NativeType("Error") int error) {
    //    long __result = nVRApplications_GetApplicationsErrorNameFromEnum (error)
    //    return memASCIISafe(__result)
    //}


    // ---------------  Application properties  --------------- //


    /**
     * JVM custom
     *
     * Returns a value for an application property. The required buffer size to fit this value will be returned.
     *
     * Note: Multi-thread unsafe if not passing a pError and reading it from the class property.
     *
     * @param appKey
     * @param property                one of:<br><table><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_Name_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_LaunchType_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_WorkingDirectory_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_BinaryPath_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_Arguments_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_URL_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_Description_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_NewsURL_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_ImagePath_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_Source_String}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_ActionManifestURL_String}</td></tr></table>
     * @param propertyValueBufferLen
     * @param error
     */
    @JvmOverloads
    fun getApplicationPropertyString(appKey: String, property: Property, propertyValueBufferLen: Int = vr.maxPropertyStringSize, error: VRApplicationErrorBuffer = pError): String =
            stak { s ->
                val appKeyEncoded = s.asciiAdr(appKey)
                var pPropertyValue = s.nmalloc(1, propertyValueBufferLen)
                val result = nVRApplications_GetApplicationPropertyString(appKeyEncoded, property.i, pPropertyValue, propertyValueBufferLen, error.adr)
                if (result > propertyValueBufferLen) {
                    pPropertyValue = s.nmalloc(1, result)
                    nVRApplications_GetApplicationPropertyString(appKeyEncoded, property.i, pPropertyValue, propertyValueBufferLen, error.adr)
                }
                memASCII(pPropertyValue, result - 1)
            }

    /**
     * Returns a bool value for an application property. Returns false in all error cases.
     *
     * Note: Multi-thread unsafe if not passing a pError and reading it from the class property.
     *
     * @param appKey
     * @param property one of:<br><table><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_IsDashboardOverlay_Bool}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_IsTemplate_Bool}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_IsInstanced_Bool}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_IsInternal_Bool}</td></tr><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_WantsCompositorPauseInStandby_Bool}</td></tr></table>
     * @param error
     */
    @JvmOverloads
    fun getApplicationPropertyBool(appKey: String, property: Property, error: VRApplicationErrorBuffer = pError): Boolean =
            stak.asciiAdr(appKey) { nVRApplications_GetApplicationPropertyBool(it, property.i, error.adr) }

    /**
     * Returns a uint64 value for an application property. Returns 0 in all error cases.
     *
     * Note: Multi-thread unsafe if not passing a pError and reading it from the class property.
     *
     * @param appKey
     * @param property must be:<br><table><tr><td>{@link VR#EVRApplicationProperty_VRApplicationProperty_LastLaunchTime_Uint64}</td></tr></table>
     * @param error
     */
    @JvmOverloads
    fun getApplicationPropertyLong(appKey: String, property: Property, error: VRApplicationErrorBuffer = pError): Long =
            stak.asciiAdr(appKey) { nVRApplications_GetApplicationPropertyUint64(it, property.i, error.adr) }

    /**
     * Sets the application auto-launch flag. This is only valid for applications which return true for
     * {@link VR#EVRApplicationProperty_VRApplicationProperty_IsDashboardOverlay_Bool}.
     *
     * @param appKey
     * @param autoLaunch
     */
    fun setApplicationAutoLaunch(appKey: String, autoLaunch: Boolean): Error =
            stak { Error of nVRApplications_SetApplicationAutoLaunch(it.asciiAdr(appKey), autoLaunch) }

    /**
     * Gets the application auto-launch flag. This is only valid for applications which return true for
     * {@link VR#EVRApplicationProperty_VRApplicationProperty_IsDashboardOverlay_Bool}.
     *
     * @param appKey
     */
    infix fun getApplicationAutoLaunch(appKey: String): Boolean =
            stak.asciiAdr(appKey) { nVRApplications_GetApplicationAutoLaunch(it) }

    /**
     * Adds this mime-type to the list of supported mime types for this application.
     *
     * @param appKey
     * @param mimeType
     */
    fun setDefaultApplicationForMimeType(appKey: String, mimeType: String): Error =
            stak { Error of nVRApplications_SetDefaultApplicationForMimeType(it.asciiAdr(appKey), it.asciiAdr(mimeType)) }

    /**
     * Return the app key that will open this mime type. TODO offer a more convenient one?
     *
     * @param mimeType
     * @param appKeyBuffer
     */
    fun getDefaultApplicationForMimeType(mimeType: String, appKeyBuffer: ByteBuffer?): Boolean =
            stak {
                nVRApplications_GetDefaultApplicationForMimeType(it.asciiAdr(mimeType), appKeyBuffer?.adr
                        ?: NULL, appKeyBuffer?.cap ?: 0)
            }

    /**
     * Get the list of supported mime types for this application, comma-delimited. TODO offer a more convenient one?
     *
     * @param appKey
     * @param mimeTypesBuffer
     */
    fun getApplicationSupportedMimeTypes(appKey: String, mimeTypesBuffer: ByteBuffer?): Boolean =
            stak.asciiAdr(appKey) {
                nVRApplications_GetApplicationSupportedMimeTypes(it, mimeTypesBuffer?.adr ?: NULL, mimeTypesBuffer?.rem
                        ?: 0)
            }

    /**
     * Get the list of app-keys that support this mime type, comma-delimited, the return value is number of bytes you need to return the full string.
     *
     * @param mimeType
     */
    infix fun getApplicationsThatSupportMimeType(mimeType: String): List<String> =
            stak {
                val pMimeType = it.asciiAdr(mimeType)
                val size = nVRApplications_GetApplicationsThatSupportMimeType(pMimeType, NULL, 0)
                val appKeys = it.nmalloc(1, size)
                nVRApplications_GetApplicationsThatSupportMimeType(pMimeType, appKeys, size)
                memASCII(appKeys, size - 1).split(',')
            }

    /**
     * Get the args list from an app launch that had the process already running, you call this when you get a {@link VR#EVREventType_VREvent_ApplicationMimeTypeLoad}.
     *
     * @param handle
     * @param argsSize
     */
    infix fun getApplicationLaunchArguments(handle: Int): String =
            stak {
                val size = nVRApplications_GetApplicationLaunchArguments(handle, NULL, 0)
                val args = it.nmalloc(1, size)
                nVRApplications_GetApplicationLaunchArguments(handle, args, size)
                memASCII(args, size - 1)
            }

    /**
     * Returns the app key for the application that is starting up.
     *
     * @param appKeyBuffer
     */
    val startingApplication: String =
            stak {
                val appKey = it.nmalloc(1, maxKeyLength)
                pError[0] = nVRApplications_GetStartingApplication(appKey, maxKeyLength)
                memASCII(appKey, maxKeyLength - 1)
            }

    /** Returns the application transition state. */
    val transitionState: TransitionState
        get() = TransitionState of VRApplications_GetTransitionState()

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
    infix fun performApplicationPrelaunchCheck(appKey: String): Error =
            stak.asciiAdr(appKey) { Error of nVRApplications_PerformApplicationPrelaunchCheck(it) }

    /**
     * Kind of useless on JVM, but it will be offered anyway on the enum itself
     * Returns a string for an application transition state.
     *
     * @param state one of:<br><table><tr><td>{@link VR#EVRApplicationTransitionState_VRApplicationTransition_None}</td></tr><tr><td>{@link VR#EVRApplicationTransitionState_VRApplicationTransition_OldAppQuitSent}</td></tr><tr><td>{@link VR#EVRApplicationTransitionState_VRApplicationTransition_WaitingForExternalLaunch}</td></tr><tr><td>{@link VR#EVRApplicationTransitionState_VRApplicationTransition_NewAppLaunched}</td></tr></table>
     */
//    infix fun getApplicationsTransitionStateNameFromEnum(state: TransitionState): String? {
//        val result = nVRApplications_GetApplicationsTransitionStateNameFromEnum(state.i)
//        return memASCIISafe(result)
//    }

    /** Returns true if the outgoing scene app has requested a save prompt before exiting. */
    val isQuitUserPromptRequested: Boolean
        get() = VRApplications_IsQuitUserPromptRequested()

    /**
     * Starts a subprocess within the calling application. This suppresses all application transition UI and automatically identifies the new executable as
     * part of the same application. On success the calling process should exit immediately. If working directory is {@code NULL} or "" the directory portion of the
     * binary path will be the working directory.
     *
     * @param binaryPath
     * @param arguments
     * @param workingDirectory
     */
    fun launchInternalProcess(binaryPath: String, arguments: String, workingDirectory: String): Error =
            stak { Error of nVRApplications_LaunchInternalProcess(it.asciiAdr(binaryPath), it.asciiAdr(arguments), it.asciiAdr(workingDirectory)) }

    /**
     * Returns the current scene process ID according to the application system. A scene process will get scene focus once it starts rendering, but it will
     * appear here once it calls {@code VR_Init} with the Scene application.
     * type.
     */
    val currentSceneProcessId: Int
        get() = VRApplications_GetCurrentSceneProcessId()

    override val version: String
        get() = "IVRApplications_006"
}