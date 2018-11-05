package lib

import glm_.set
import kool.adr
import kool.stak
import org.lwjgl.openvr.VRSettings.*
import org.lwjgl.system.MemoryStack.stackGet
import org.lwjgl.system.MemoryUtil.memASCII
import org.lwjgl.system.MemoryUtil.memCallocInt

object vrSettings : vrInterface {

    /** The maximum length of a settings key */
    const val maxKeyLength = 128

    enum class Error(@JvmField val i: Int) {

        None(0),
        IPCFailed(1),
        WriteFailed(2),
        ReadFailed(3),
        JsonParseFailed(4),
        /** This will be returned if the setting does not appear in the appropriate default file and has not been set   */
        UnsetSettingHasNoDefault(5);

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }

        override fun toString(): String = stak { memASCII(nVRSettings_GetSettingsErrorNameFromEnum(i)) }
    }

    val pError = memCallocInt(1)

    var error: Error
        get() = Error of pError[0]
        set(value) {
            pError[0] = value.i
        }

    // Kind of useless on JVM, but it will be offered anyway on the enum itself
    //@Nullable
    //@NativeType("char const *")
    //public static String VRSettings_GetSettingsErrorNameFromEnum(@NativeType("Error") int eError) {
    //    long __result = nVRSettings_GetSettingsErrorNameFromEnum(eError);
    //    return memASCIISafe(__result);
    //}

    object steamVr {

        val section = "steamvr"

        var requireHmd: String
            get() = getString(section, "requireHmd")
            set(value) = setString(section, "requireHmd", value)

        var forcedDriver: String
            get() = getString(section, "forcedDriver")
            set(value) = setString(section, "forcedDriver", value)

        var forcedHmd: String
            get() = getString(section, "forcedHmd")
            set(value) = setString(section, "forcedHmd", value)

        var displayDebug: Boolean
            get() = getBool(section, "displayDebug")
            set(value) = setBool(section, "displayDebug", value)

        var debugProcessPipe: String
            get() = getString(section, "debugProcessPipe")
            set(value) = setString(section, "debugProcessPipe", value)

        var displayDebugX: Int
            get() = getInt(section, "displayDebugX")
            set(value) = setInt(section, "displayDebugX", value)

        var displayDebugY: Int
            get() = getInt(section, "displayDebugY")
            set(value) = setInt(section, "displayDebugY", value)

        var sendSystemButtonToAllApps: Boolean
            get() = getBool(section, "sendSystemButtonToAllApps")
            set(value) = setBool(section, "sendSystemButtonToAllApps", value)

        var loglevel: Int
            get() = getInt(section, "loglevel")
            set(value) = setInt(section, "loglevel", value)

        var ipd: Float
            get() = getFloat(section, "ipd")
            set(value) = setFloat(section, "ipd", value)

        var background: String
            get() = getString(section, "background")
            set(value) = setString(section, "background", value)

        var backgroundUseDomeProjection: Boolean
            get() = getBool(section, "backgroundUseDomeProjection")
            set(value) = setBool(section, "backgroundUseDomeProjection", value)

        var backgroundCameraHeight: Float
            get() = getFloat(section, "backgroundCameraHeight")
            set(value) = setFloat(section, "backgroundCameraHeight", value)

        var backgroundDomeRadius: Float
            get() = getFloat(section, "backgroundDomeRadius")
            set(value) = setFloat(section, "backgroundDomeRadius", value)

        var gridColor: String
            get() = getString(section, "gridColor")
            set(value) = setString(section, "gridColor", value)

        var playAreaColor: String
            get() = getString(section, "playAreaColor")
            set(value) = setString(section, "playAreaColor", value)

        var showStage: Boolean
            get() = getBool(section, "showStage")
            set(value) = setBool(section, "showStage", value)

        var activateMultipleDrivers: Boolean
            get() = getBool(section, "activateMultipleDrivers")
            set(value) = setBool(section, "activateMultipleDrivers", value)

        var directMode: Boolean
            get() = getBool(section, "directMode")
            set(value) = setBool(section, "directMode", value)

        var directModeEdidVid: Int
            get() = getInt(section, "directModeEdidVid")
            set(value) = setInt(section, "directModeEdidVid", value)

        var directModeEdidPid: Int
            get() = getInt(section, "directModeEdidPid")
            set(value) = setInt(section, "directModeEdidPid", value)

        var usingSpeakers: Boolean
            get() = getBool(section, "usingSpeakers")
            set(value) = setBool(section, "usingSpeakers", value)

        var speakersForwardYawOffsetDegrees: Float
            get() = getFloat(section, "speakersForwardYawOffsetDegrees")
            set(value) = setFloat(section, "speakersForwardYawOffsetDegrees", value)

        var basestationPowerManagement: Boolean
            get() = getBool(section, "basestationPowerManagement")
            set(value) = setBool(section, "basestationPowerManagement", value)

        var neverKillProcesses: Boolean
            get() = getBool(section, "neverKillProcesses")
            set(value) = setBool(section, "neverKillProcesses", value)

        var supersampleScale: Float
            get() = getFloat(section, "supersampleScale")
            set(value) = setFloat(section, "supersampleScale", value)

        var maxRecommendedResolution: Int
            get() = getInt(section, "maxRecommendedResolution")
            set(value) = setInt(section, "maxRecommendedResolution", value)

        var allowAsyncReprojection: Boolean
            get() = getBool(section, "allowAsyncReprojection")
            set(value) = setBool(section, "allowAsyncReprojection", value)

        var allowInterleavedReprojection: Boolean
            get() = getBool(section, "allowInterleavedReprojection")
            set(value) = setBool(section, "allowInterleavedReprojection", value)

        var forceReprojection: Boolean
            get() = getBool(section, "forceReprojection")
            set(value) = setBool(section, "forceReprojection", value)

        var forceFadeOnBadTracking: Boolean
            get() = getBool(section, "forceFadeOnBadTracking")
            set(value) = setBool(section, "forceFadeOnBadTracking", value)

        var defaultMirrorView: Int
            get() = getInt(section, "mirrorView")
            set(value) = setInt(section, "mirrorView", value)

        var showMirrorView: Boolean
            get() = getBool(section, "showMirrorView")
            set(value) = setBool(section, "showMirrorView", value)

        var mirrorViewGeometry: String
            get() = getString(section, "mirrorViewGeometry")
            set(value) = setString(section, "mirrorViewGeometry", value)

        var mirrorViewGeometryMaximized: String
            get() = getString(section, "mirrorViewGeometryMaximized")
            set(value) = setString(section, "mirrorViewGeometryMaximized", value)

        var startMonitorFromAppLaunch: Boolean
            get() = getBool(section, "startMonitorFromAppLaunch")
            set(value) = setBool(section, "startMonitorFromAppLaunch", value)

        var startCompositorFromAppLaunch: Boolean
            get() = getBool(section, "startCompositorFromAppLaunch")
            set(value) = setBool(section, "startCompositorFromAppLaunch", value)

        var startDashboardFromAppLaunch: Boolean
            get() = getBool(section, "startDashboardFromAppLaunch")
            set(value) = setBool(section, "startDashboardFromAppLaunch", value)

        var startOverlayAppsFromDashboard: Boolean
            get() = getBool(section, "startOverlayAppsFromDashboard")
            set(value) = setBool(section, "startOverlayAppsFromDashboard", value)

        var enableHomeApp: Boolean
            get() = getBool(section, "enableHomeApp")
            set(value) = setBool(section, "enableHomeApp", value)

        var retailDemo: Boolean
            get() = getBool(section, "retailDemo")
            set(value) = setBool(section, "retailDemo", value)

        var cycleBackgroundImageTimeSec: Int
            get() = getInt(section, "CycleBackgroundImageTimeSec")
            set(value) = setInt(section, "CycleBackgroundImageTimeSec", value)

        var ipdOffset: Float
            get() = getFloat(section, "ipdOffset")
            set(value) = setFloat(section, "ipdOffset", value)

        var allowSupersampleFiltering: Boolean
            get() = getBool(section, "allowSupersampleFiltering")
            set(value) = setBool(section, "allowSupersampleFiltering", value)

        var supersampleManualOverride: Boolean
            get() = getBool(section, "supersampleManualOverride")
            set(value) = setBool(section, "supersampleManualOverride", value)

        var enableLinuxVulkanAsync: Boolean
            get() = getBool(section, "enableLinuxVulkanAsync")
            set(value) = setBool(section, "enableLinuxVulkanAsync", value)

        var allowDisplayLockedMode: Boolean
            get() = getBool(section, "allowDisplayLockedMode")
            set(value) = setBool(section, "allowDisplayLockedMode", value)

        var haveStartedTutorialForNativeChaperoneDriver: Boolean
            get() = getBool(section, "haveStartedTutorialForNativeChaperoneDriver")
            set(value) = setBool(section, "haveStartedTutorialForNativeChaperoneDriver", value)

        var forceWindows32BitVRMonitor: Boolean // TODO unsure
            get() = getBool(section, "forceWindows32BitVRMonitor")
            set(value) = setBool(section, "forceWindows32BitVRMonitor", value)

        var debugInput: Boolean
            get() = getBool(section, "debugInput")
            set(value) = setBool(section, "debugInput", value)

        var legacyInputRebinding: Boolean
            get() = getBool(section, "legacyInputRebinding")
            set(value) = setBool(section, "legacyInputRebinding", value)

        var debugInputBinding: Boolean
            get() = getBool(section, "debugInputBinding")
            set(value) = setBool(section, "debugInputBinding", value)

        var inputBindingUI: Boolean
            get() = getBool(section, "inputBindingUI")
            set(value) = setBool(section, "inputBindingUI", value)

        var renderCameraMode: String
            get() = getString(section, "renderCameraMode")
            set(value) = setString(section, "renderCameraMode", value)
    }

    object lighthouse {

        val section = "driver_lighthouse"

        var disableimu: Boolean
            get() = getBool(section, "disableimu")
            set(value) = setBool(section, "disableimu", value)

        var disableimuexcepthmd: Boolean
            get() = getBool(section, "disableimuexcepthmd")
            set(value) = setBool(section, "disableimuexcepthmd", value)

        var usedisambiguation: String
            get() = getString(section, "usedisambiguation")
            set(value) = setString(section, "usedisambiguation", value)

        var disambiguationdebug: Int
            get() = getInt(section, "disambiguationdebug")
            set(value) = setInt(section, "disambiguationdebug", value)

        var primarybasestation: Int
            get() = getInt(section, "primarybasestation")
            set(value) = setInt(section, "primarybasestation", value)

        var dbhistory: Boolean
            get() = getBool(section, "dbhistory")
            set(value) = setBool(section, "dbhistory", value)

        var enableBluetooth: Boolean
            get() = getBool(section, "enableBluetooth")
            set(value) = setBool(section, "enableBluetooth", value)

        var powerManagedBaseStations: String
            get() = getString(section, "PowerManagedBaseStations")
            set(value) = setString(section, "PowerManagedBaseStations", value)

        var enableImuFallback: Boolean
            get() = getBool(section, "enableImuFallback")
            set(value) = setBool(section, "enableImuFallback", value)

        var enable: Boolean
            get() = getBool(section, "enable")
            set(value) = setBool(section, "enable", value)
    }

    object `null` {

        val section = "driver_null"


        var serialNumber: String
            get() = getString(section, "serialNumber")
            set(value) = setString(section, "serialNumber", value)

        var modelNumber: String
            get() = getString(section, "modelNumber")
            set(value) = setString(section, "modelNumber", value)

        var windowX: Int
            get() = getInt(section, "windowX")
            set(value) = setInt(section, "windowX", value)

        var windowY: Int
            get() = getInt(section, "windowY")
            set(value) = setInt(section, "windowY", value)

        var windowWidth: Int
            get() = getInt(section, "windowWidth")
            set(value) = setInt(section, "windowWidth", value)

        var windowHeight: Int
            get() = getInt(section, "windowHeight")
            set(value) = setInt(section, "windowHeight", value)

        var renderWidth: Int
            get() = getInt(section, "renderWidth")
            set(value) = setInt(section, "renderWidth", value)

        var renderHeight: Int
            get() = getInt(section, "renderHeight")
            set(value) = setInt(section, "renderHeight", value)

        var secondsFromVsyncToPhotons: Float
            get() = getFloat(section, "secondsFromVsyncToPhotons")
            set(value) = setFloat(section, "secondsFromVsyncToPhotons", value)

        var displayFrequency: Float
            get() = getFloat(section, "displayFrequency")
            set(value) = setFloat(section, "displayFrequency", value)

        var enable: Boolean
            get() = getBool(section, "enable")
            set(value) = setBool(section, "enable", value)
    }

    object userInterface {

        val section = "userinterface"


        var statusAlwaysOnTop: Boolean
            get() = getBool(section, "StatusAlwaysOnTop")
            set(value) = setBool(section, "StatusAlwaysOnTop", value)

        var minimizeToTray: Boolean
            get() = getBool(section, "MinimizeToTray")
            set(value) = setBool(section, "MinimizeToTray", value)

        var hidePopupsWhenStatusMinimized: Boolean
            get() = getBool(section, "HidePopupsWhenStatusMinimized")
            set(value) = setBool(section, "HidePopupsWhenStatusMinimized", value)

        var screenshots: Boolean
            get() = getBool(section, "screenshots")
            set(value) = setBool(section, "screenshots", value)

        var screenshotType: Int
            get() = getInt(section, "screenshotType")
            set(value) = setInt(section, "screenshotType", value)
    }

    object notifications {

        val section = "notifications"


        var doNotDisturb: Boolean
            get() = getBool(section, "DoNotDisturb")
            set(value) = setBool(section, "DoNotDisturb", value)
    }

    object keyboard {

        val section = "keyboard"

        var tutorialCompletions: Int
            get() = getInt(section, "TutorialCompletions")
            set(value) = setInt(section, "TutorialCompletions", value)

        var scaleX: Float
            get() = getFloat(section, "ScaleX")
            set(value) = setFloat(section, "ScaleX", value)

        var scaleY: Float
            get() = getFloat(section, "ScaleY")
            set(value) = setFloat(section, "ScaleY", value)

        var offsetLeftX: Float
            get() = getFloat(section, "OffsetLeftX")
            set(value) = setFloat(section, "OffsetLeftX", value)

        var offsetRightX: Float
            get() = getFloat(section, "OffsetRightX")
            set(value) = setFloat(section, "OffsetRightX", value)

        var offsetY: Float
            get() = getFloat(section, "OffsetY")
            set(value) = setFloat(section, "OffsetY", value)

        var smoothing: Boolean
            get() = getBool(section, "Smoothing")
            set(value) = setBool(section, "Smoothing", value)
    }

    object perf {

        val section = "perfcheck"


        var perfGraphInHMD: Boolean
            get() = getBool(section, "perfGraphInHMD")
            set(value) = setBool(section, "perfGraphInHMD", value)

        var allowTimingStore: Boolean
            get() = getBool(section, "allowTimingStore")
            set(value) = setBool(section, "allowTimingStore", value)

        var saveTimingsOnExit: Boolean
            get() = getBool(section, "saveTimingsOnExit")
            set(value) = setBool(section, "saveTimingsOnExit", value)

        var perfTestData: Float
            get() = getFloat(section, "perfTestData")
            set(value) = setFloat(section, "perfTestData", value)

        var linuxGPUProfiling: Boolean
            get() = getBool(section, "linuxGPUProfiling")
            set(value) = setBool(section, "linuxGPUProfiling", value)
    }

    object vrWebHelper {

        val section = "VRWebHelper"


        var debuggerEnabled: Boolean
            get() = getBool(section, "DebuggerEnabled")
            set(value) = setBool(section, "DebuggerEnabled", value)

        var debuggerPort: Int
            get() = getInt(section, "DebuggerPort")
            set(value) = setInt(section, "DebuggerPort", value)
    }

    object collisionBounds {

        val section = "collisionBounds"


        var collisionBoundsStyle: Int
            get() = getInt(section, "CollisionBoundsStyle")
            set(value) = setInt(section, "CollisionBoundsStyle", value)

        var collisionBoundsGroundPerimeterOn: Boolean
            get() = getBool(section, "CollisionBoundsGroundPerimeterOn")
            set(value) = setBool(section, "CollisionBoundsGroundPerimeterOn", value)

        var collisionBoundsCenterMarkerOn: Boolean
            get() = getBool(section, "CollisionBoundsCenterMarkerOn")
            set(value) = setBool(section, "CollisionBoundsCenterMarkerOn", value)

        var collisionBoundsPlaySpaceOn: Boolean
            get() = getBool(section, "CollisionBoundsPlaySpaceOn")
            set(value) = setBool(section, "CollisionBoundsPlaySpaceOn", value)

        var collisionBoundsFadeDistance: Float
            get() = getFloat(section, "CollisionBoundsFadeDistance")
            set(value) = setFloat(section, "CollisionBoundsFadeDistance", value)

        var collisionBoundsColorGammaR: Int
            get() = getInt(section, "CollisionBoundsColorGammaR")
            set(value) = setInt(section, "CollisionBoundsColorGammaR", value)

        var collisionBoundsColorGammaG: Int
            get() = getInt(section, "CollisionBoundsColorGammaG")
            set(value) = setInt(section, "CollisionBoundsColorGammaG", value)

        var collisionBoundsColorGammaB: Int
            get() = getInt(section, "CollisionBoundsColorGammaB")
            set(value) = setInt(section, "CollisionBoundsColorGammaB", value)

        var collisionBoundsColorGammaA: Int
            get() = getInt(section, "CollisionBoundsColorGammaA")
            set(value) = setInt(section, "CollisionBoundsColorGammaA", value)
    }

    object camera {

        val section = "camera"


        var enableCamera: Boolean
            get() = getBool(section, "enableCamera")
            set(value) = setBool(section, "enableCamera", value)

        var enableCameraInDashboard: Boolean
            get() = getBool(section, "enableCameraInDashboard")
            set(value) = setBool(section, "enableCameraInDashboard", value)

        var enableCameraForCollisionBounds: Boolean
            get() = getBool(section, "enableCameraForCollisionBounds")
            set(value) = setBool(section, "enableCameraForCollisionBounds", value)

        var enableCameraForRoomView: Boolean
            get() = getBool(section, "enableCameraForRoomView")
            set(value) = setBool(section, "enableCameraForRoomView", value)

        var cameraBoundsColorGammaR: Int
            get() = getInt(section, "cameraBoundsColorGammaR")
            set(value) = setInt(section, "cameraBoundsColorGammaR", value)

        var cameraBoundsColorGammaG: Int
            get() = getInt(section, "cameraBoundsColorGammaG")
            set(value) = setInt(section, "cameraBoundsColorGammaG", value)

        var cameraBoundsColorGammaB: Int
            get() = getInt(section, "cameraBoundsColorGammaB")
            set(value) = setInt(section, "cameraBoundsColorGammaB", value)

        var cameraBoundsColorGammaA: Int
            get() = getInt(section, "cameraBoundsColorGammaA")
            set(value) = setInt(section, "cameraBoundsColorGammaA", value)

        var cameraBoundsStrength: Int
            get() = getInt(section, "cameraBoundsStrength")
            set(value) = setInt(section, "cameraBoundsStrength", value)

        var cameraRoomViewMode: Int
            get() = getInt(section, "cameraRoomViewMode")
            set(value) = setInt(section, "cameraRoomViewMode", value)
    }

    object audio {

        val section = "audio"


        var onPlaybackDevice: String
            get() = getString(section, "onPlaybackDevice")
            set(value) = setString(section, "onPlaybackDevice", value)

        var onRecordDevice: String
            get() = getString(section, "onRecordDevice")
            set(value) = setString(section, "onRecordDevice", value)

        var onPlaybackMirrorDevice: String
            get() = getString(section, "onPlaybackMirrorDevice")
            set(value) = setString(section, "onPlaybackMirrorDevice", value)

        var offPlaybackDevice: String
            get() = getString(section, "offPlaybackDevice")
            set(value) = setString(section, "offPlaybackDevice", value)

        var offRecordDevice: String
            get() = getString(section, "offRecordDevice")
            set(value) = setString(section, "offRecordDevice", value)

        var viveHDMIGain: Boolean
            get() = getBool(section, "viveHDMIGain")
            set(value) = setBool(section, "viveHDMIGain", value)
    }

    object power {

        val section = "power"


        var powerOffOnExit: Boolean
            get() = getBool(section, "powerOffOnExit")
            set(value) = setBool(section, "powerOffOnExit", value)

        var turnOffScreensTimeout: Float
            get() = getFloat(section, "turnOffScreensTimeout")
            set(value) = setFloat(section, "turnOffScreensTimeout", value)

        var turnOffControllersTimeout: Float
            get() = getFloat(section, "turnOffControllersTimeout")
            set(value) = setFloat(section, "turnOffControllersTimeout", value)

        var returnToWatchdogTimeout: Float
            get() = getFloat(section, "returnToWatchdogTimeout")
            set(value) = setFloat(section, "returnToWatchdogTimeout", value)

        var autoLaunchSteamVROnButtonPress: Boolean
            get() = getBool(section, "autoLaunchSteamVROnButtonPress")
            set(value) = setBool(section, "autoLaunchSteamVROnButtonPress", value)

        var pauseCompositorOnStandby: Boolean
            get() = getBool(section, "pauseCompositorOnStandby")
            set(value) = setBool(section, "pauseCompositorOnStandby", value)
    }

    object dashboard {

        val section = "dashboard"


        var enableDashboard: Boolean
            get() = getBool(section, "enableDashboard")
            set(value) = setBool(section, "enableDashboard", value)

        var arcadeMode: Boolean
            get() = getBool(section, "arcadeMode")
            set(value) = setBool(section, "arcadeMode", value)

        var webUI: Boolean
            get() = getBool(section, "webUI")
            set(value) = setBool(section, "webUI", value)

        var webUIDevTools: Boolean
            get() = getBool(section, "webUIDevTools")
            set(value) = setBool(section, "webUIDevTools", value)

        var webUIDashboard: Boolean
            get() = getBool(section, "webUIDashboard")
            set(value) = setBool(section, "webUIDashboard", value)
    }

    object modelskins {

        val section = "modelskins"
    }

    object webInterface {

        val section = "WebInterface"


        var webEnable: Boolean
            get() = getBool(section, "WebEnable")
            set(value) = setBool(section, "WebEnable", value)

        var webPort: String
            get() = getString(section, "WebPort")
            set(value) = setString(section, "WebPort", value)
    }

    object trackingOverrides // TODO

    object perApp // TODO

    object trackers

    /**
     * Returns true if file sync occurred (force or settings dirty).
     *
     * Note: Multi-thread unsafe if not passing a pError and reading it from the class property.
     */
    fun sync(force: Boolean = false, pErr: VRSettingsErrorBuffer = pError): Boolean =
            nVRSettings_Sync(force, pErr.adr)

    /** Note: Multi-thread unsafe if not passing a pError and reading it from the class property. */
    @JvmOverloads
    fun setBool(section: String, settingsKey: String, value: Boolean, pErr: VRSettingsErrorBuffer = pError) =
            nVRSettings_SetBool(addressOfAscii(section), addressOfAscii(settingsKey), value, pErr.adr)

    /** Note: Multi-thread unsafe if not passing a pError and reading it from the class property. */
    @JvmOverloads
    fun setInt(section: String, settingsKey: String, value: Int, pErr: VRSettingsErrorBuffer = pError) =
            nVRSettings_SetInt32(addressOfAscii(section), addressOfAscii(settingsKey), value, pErr.adr)

    /** Note: Multi-thread unsafe if not passing a pError and reading it from the class property. */
    @JvmOverloads
    fun setFloat(section: String, settingsKey: String, value: Float, pErr: VRSettingsErrorBuffer = pError) =
            nVRSettings_SetFloat(addressOfAscii(section), addressOfAscii(settingsKey), value, pErr.adr)

    /** Note: Multi-thread unsafe if not passing a pError and reading it from the class property. */
    @JvmOverloads
    fun setString(section: String, settingsKey: String, value: String, pErr: VRSettingsErrorBuffer = pError) =
            nVRSettings_SetString(addressOfAscii(section), addressOfAscii(settingsKey), addressOfAscii(value), pErr.adr)

    /** Note: Multi-thread unsafe if not passing a pError and reading it from the class property. */
    @JvmOverloads
    fun getBool(section: String, settingsKey: String, pErr: VRSettingsErrorBuffer = pError): Boolean =
            nVRSettings_GetBool(addressOfAscii(section), addressOfAscii(settingsKey), pErr.adr)

    /** Note: Multi-thread unsafe if not passing a pError and reading it from the class property. */
    @JvmOverloads
    fun getInt(section: String, settingsKey: String, pErr: VRSettingsErrorBuffer = pError): Int =
            nVRSettings_GetInt32(addressOfAscii(section), addressOfAscii(settingsKey), pErr.adr)

    /** Note: Multi-thread unsafe if not passing a pError and reading it from the class property. */
    fun getFloat(section: String, settingsKey: String, pErr: VRSettingsErrorBuffer = pError): Float =
            nVRSettings_GetFloat(addressOfAscii(section), addressOfAscii(settingsKey), pErr.adr)

    /** Note: Multi-thread unsafe if not passing a pError and reading it from the class property. */
    fun getString(section: String, settingsKey: String, pErr: VRSettingsErrorBuffer = pError): String {
        val s = stackGet().nmalloc(1, maxKeyLength)
        nVRSettings_GetString(addressOfAscii(section), addressOfAscii(settingsKey), s, maxKeyLength, pErr.adr)
        return memASCII(s)
    }

    /** Note: Multi-thread unsafe if not passing a pError and reading it from the class property. */
    fun removeSection(section: String, pErr: VRSettingsErrorBuffer = pError) =
            nVRSettings_RemoveSection(addressOfAscii(section), pErr.adr)

    /** Note: Multi-thread unsafe if not passing a pError and reading it from the class property. */
    fun removeKeyInSection(section: String, settingsKey: String, pErr: VRSettingsErrorBuffer = pError) =
            nVRSettings_RemoveKeyInSection(addressOfAscii(section), addressOfAscii(settingsKey), pErr.adr)

    override val version: String
        get() = "IVRSettings_002"
}