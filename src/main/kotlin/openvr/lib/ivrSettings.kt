package openvr.lib

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import java.util.*

// ivrsettings.h ==================================================================================================================================================

open class IVRSettings : Structure {

    fun getSettingsErrorNameFromEnum(error: EVRSettingsError) = GetSettingsErrorNameFromEnum!!(error.i)
    @JvmField var GetSettingsErrorNameFromEnum: GetSettingsErrorNameFromEnum_callback? = null

    interface GetSettingsErrorNameFromEnum_callback : Callback {
        operator fun invoke(eError: Int): String
    }


    // Returns true if file sync occurred (force or settings dirty)
    @JvmOverloads fun sync(force: Boolean = false, error: EVRSettingsError_ByReference? = null) = Sync!!(force, error)

    @JvmField var Sync: Sync_callback? = null

    interface Sync_callback : Callback {
        operator fun invoke(bForce: Boolean, peError: EVRSettingsError_ByReference?): Boolean
    }


    @JvmOverloads fun setBool(section: String, settingsKey: String, value: Boolean, error: EVRSettingsError_ByReference? = null) = SetBool!!(section, settingsKey, value, error)

    @JvmField var SetBool: SetBool_callback? = null

    interface SetBool_callback : Callback {
        operator fun invoke(pchSection: String, pchSettingsKey: String, bValue: Boolean, peError: EVRSettingsError_ByReference?)
    }


    @JvmOverloads fun setInt32(section: String, settingsKey: String, value: Int, error: EVRSettingsError_ByReference? = null) = SetInt32!!(section, settingsKey, value, error)

    @JvmField var SetInt32: SetInt32_callback? = null

    interface SetInt32_callback : Callback {
        operator fun invoke(pchSection: String, pchSettingsKey: String, nValue: Int, peError: EVRSettingsError_ByReference?)
    }


    @JvmOverloads fun setFloat(section: String, settingsKey: String, value: Float, error: EVRSettingsError_ByReference? = null)
            = SetFloat!!(section, settingsKey, value, error)

    @JvmField var SetFloat: SetFloat_callback? = null

    interface SetFloat_callback : Callback {
        operator fun invoke(pchSection: String, pchSettingsKey: String, flValue: Float, peError: EVRSettingsError_ByReference?)
    }


    @JvmOverloads fun setString(section: String, settingsKey: String, value: String, error: EVRSettingsError_ByReference? = null) = SetString!!(section, settingsKey, value, error)

    @JvmField var SetString: SetString_callback? = null

    interface SetString_callback : Callback {
        operator fun invoke(pchSection: String, pchSettingsKey: String, pchValue: String, peError: EVRSettingsError_ByReference?)
    }


    // Users of the system need to provide a proper default in default.vrsettings in the resources/settings/ directory of either the runtime or the driver_xxx
    // directory. Otherwise the default will be false, 0, 0.0 or ""
    @JvmOverloads fun getBool(section: String, settingsKey: String, error: EVRSettingsError_ByReference? = null) = GetBool!!(section, settingsKey, error)

    @JvmField var GetBool: GetBool_callback? = null

    interface GetBool_callback : Callback {
        operator fun invoke(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference?): Boolean
    }


    @JvmOverloads fun getInt32(section: String, settingsKey: String, error: EVRSettingsError_ByReference? = null) = GetInt32!!(section, settingsKey, error)

    @JvmField var GetInt32: GetInt32_callback? = null

    interface GetInt32_callback : Callback {
        operator fun invoke(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference?): Int
    }


    @JvmOverloads fun getFloat(section: String, settingsKey: String, error: EVRSettingsError_ByReference? = null) = GetFloat!!(section, settingsKey, error)

    @JvmField var GetFloat: GetFloat_callback? = null

    interface GetFloat_callback : Callback {
        operator fun invoke(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference?): Float
    }


    fun getString(section: String, settingsKey: String, value: String, valueLen: Int, error: EVRSettingsError_ByReference? = null) = GetString!!(section, settingsKey, value, valueLen, error)

    @JvmField var GetString: GetString_callback? = null

    interface GetString_callback : Callback {
        operator fun invoke(pchSection: String, pchSettingsKey: String, pchValue: String, unValueLen: Int, peError: EVRSettingsError_ByReference?)
    }


    @JvmOverloads fun removeSection(section: String, error: EVRSettingsError_ByReference? = null) = RemoveSection!!(section, error)

    @JvmField var RemoveSection: RemoveSection_callback? = null

    interface RemoveSection_callback : Callback {
        operator fun invoke(pchSection: String, peError: EVRSettingsError_ByReference?)
    }


    @JvmOverloads fun removeKeyInSection(section: String, settingsKey: String, error: EVRSettingsError_ByReference? = null) = RemoveKeyInSection!!(section, settingsKey, error)

    @JvmField var RemoveKeyInSection: RemoveKeyInSection_callback? = null

    interface RemoveKeyInSection_callback : Callback {
        operator fun invoke(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference?)
    }


    constructor()

    override fun getFieldOrder()= listOf("GetSettingsErrorNameFromEnum", "Sync", "SetBool", "SetInt32",
            "SetFloat", "SetString", "GetBool", "GetInt32", "GetFloat", "GetString", "RemoveSection", "RemoveKeyInSection")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRSettings(), Structure.ByReference
    class ByValue : IVRSettings(), Structure.ByValue
}

//-----------------------------------------------------------------------------
val IVRSettings_Version = "IVRSettings_002"

//-----------------------------------------------------------------------------
// steamvr keys

val SteamVR_Section = "steamvr"
val SteamVR_RequireHmd_String = "requireHmd"
val SteamVR_ForcedDriverKey_String = "forcedDriver"
val SteamVR_ForcedHmdKey_String = "forcedHmd"
val SteamVR_DisplayDebug_Bool = "displayDebug"
val SteamVR_DebugProcessPipe_String = "debugProcessPipe"
val SteamVR_EnableDistortion_Bool = "enableDistortion"
val SteamVR_DisplayDebugX_Int32 = "displayDebugX"
val SteamVR_DisplayDebugY_Int32 = "displayDebugY"
val SteamVR_SendSystemButtonToAllApps_Bool = "sendSystemButtonToAllApps"
val SteamVR_LogLevel_Int32 = "loglevel"
val SteamVR_IPD_Float = "ipd"
val SteamVR_Background_String = "background"
val SteamVR_BackgroundUseDomeProjection_Bool = "backgroundUseDomeProjection"
val SteamVR_BackgroundCameraHeight_Float = "backgroundCameraHeight"
val SteamVR_BackgroundDomeRadius_Float = "backgroundDomeRadius"
val SteamVR_GridColor_String = "gridColor"
val SteamVR_PlayAreaColor_String = "playAreaColor"
val SteamVR_ShowStage_Bool = "showStage"
val SteamVR_ActivateMultipleDrivers_Bool = "activateMultipleDrivers"
val SteamVR_DirectMode_Bool = "directMode"
val SteamVR_DirectModeEdidVid_Int32 = "directModeEdidVid"
val SteamVR_DirectModeEdidPid_Int32 = "directModeEdidPid"
val SteamVR_UsingSpeakers_Bool = "usingSpeakers"
val SteamVR_SpeakersForwardYawOffsetDegrees_Float = "speakersForwardYawOffsetDegrees"
val SteamVR_BaseStationPowerManagement_Bool = "basestationPowerManagement"
val SteamVR_NeverKillProcesses_Bool = "neverKillProcesses"
val SteamVR_SupersampleScale_Float = "supersampleScale"
val SteamVR_AllowAsyncReprojection_Bool = "allowAsyncReprojection"
val SteamVR_AllowReprojection_Bool = "allowInterleavedReprojection"
val SteamVR_ForceReprojection_Bool = "forceReprojection"
val SteamVR_ForceFadeOnBadTracking_Bool = "forceFadeOnBadTracking"
val SteamVR_DefaultMirrorView_Int32 = "defaultMirrorView"
val SteamVR_ShowMirrorView_Bool = "showMirrorView"
val SteamVR_MirrorViewGeometry_String = "mirrorViewGeometry"
val SteamVR_StartMonitorFromAppLaunch = "startMonitorFromAppLaunch"
val SteamVR_StartCompositorFromAppLaunch_Bool = "startCompositorFromAppLaunch"
val SteamVR_StartDashboardFromAppLaunch_Bool = "startDashboardFromAppLaunch"
val SteamVR_StartOverlayAppsFromDashboard_Bool = "startOverlayAppsFromDashboard"
val SteamVR_EnableHomeApp = "enableHomeApp"
val SteamVR_CycleBackgroundImageTimeSec_Int32 = "CycleBackgroundImageTimeSec"
val SteamVR_RetailDemo_Bool = "retailDemo"
val SteamVR_IpdOffset_Float = "ipdOffset"
val SteamVR_AllowSupersampleFiltering_Bool = "allowSupersampleFiltering"
val SteamVR_SupersampleManualOverride_Bool = "supersampleManualOverride"
val SteamVR_EnableLinuxVulkanAsync_Bool = "enableLinuxVulkanAsync"
val SteamVR_AllowDisplayLockedMode_Bool = "allowDisplayLockedMode"
val SteamVR_HaveStartedTutorialForNativeChaperoneDriver_Bool = "haveStartedTutorialForNativeChaperoneDriver"
val SteamVR_ForceWindows32bitVRMonitor = "forceWindows32BitVRMonitor"
val SteamVR_DebugInput = "debugInput"
val SteamVR_LegacyInputRebinding = "legacyInputRebinding"

//-----------------------------------------------------------------------------
// lighthouse keys

val Lighthouse_Section = "driver_lighthouse"
val Lighthouse_DisableIMU_Bool = "disableimu"
val DisableIMUExceptHMD_Bool = "disableimuexcepthmd"
val Lighthouse_UseDisambiguation_String = "usedisambiguation"
val Lighthouse_DisambiguationDebug_Int32 = "disambiguationdebug"
val Lighthouse_PrimaryBasestation_Int32 = "primarybasestation"
val Lighthouse_DBHistory_Bool = "dbhistory"
val Lighthouse_EnableBluetooth_Bool = "enableBluetooth"
val Lighthouse_PowerManagedBaseStations_String = "PowerManagedBaseStations"

//-----------------------------------------------------------------------------
// null keys

val Null_Section = "driver_null"
val Null_SerialNumber_String = "serialNumber"
val Null_ModelNumber_String = "modelNumber"
val Null_WindowX_Int32 = "windowX"
val Null_WindowY_Int32 = "windowY"
val Null_WindowWidth_Int32 = "windowWidth"
val Null_WindowHeight_Int32 = "windowHeight"
val Null_RenderWidth_Int32 = "renderWidth"
val Null_RenderHeight_Int32 = "renderHeight"
val Null_SecondsFromVsyncToPhotons_Float = "secondsFromVsyncToPhotons"
val Null_DisplayFrequency_Float = "displayFrequency"

//-----------------------------------------------------------------------------
// user interface keys
val UserInterface_Section = "userinterface"
val UserInterface_StatusAlwaysOnTop_Bool = "StatusAlwaysOnTop"
val UserInterface_MinimizeToTray_Bool = "MinimizeToTray"
val UserInterface_Screenshots_Bool = "screenshots"
val UserInterface_ScreenshotType_Int = "screenshotType"

//-----------------------------------------------------------------------------
// notification keys
val Notifications_Section = "notifications"
val Notifications_DoNotDisturb_Bool = "DoNotDisturb"

//-----------------------------------------------------------------------------
// keyboard keys
val Keyboard_Section = "keyboard"
val Keyboard_TutorialCompletions = "TutorialCompletions"
val Keyboard_ScaleX = "ScaleX"
val Keyboard_ScaleY = "ScaleY"
val Keyboard_OffsetLeftX = "OffsetLeftX"
val Keyboard_OffsetRightX = "OffsetRightX"
val Keyboard_OffsetY = "OffsetY"
val Keyboard_Smoothing = "Smoothing"

//-----------------------------------------------------------------------------
// perf keys
val Perf_Section = "perfcheck"
val Perf_HeuristicActive_Bool = "heuristicActive"
val Perf_NotifyInHMD_Bool = "warnInHMD"
val Perf_NotifyOnlyOnce_Bool = "warnOnlyOnce"
val Perf_AllowTimingStore_Bool = "allowTimingStore"
val Perf_SaveTimingsOnExit_Bool = "saveTimingsOnExit"
val Perf_TestData_Float = "perfTestData"
val Perf_LinuxGPUProfiling_Bool = "linuxGPUProfiling"

//-----------------------------------------------------------------------------
// collision bounds keys
val CollisionBounds_Section = "collisionBounds"
val CollisionBounds_Style_Int32 = "CollisionBoundsStyle"
val CollisionBounds_GroundPerimeterOn_Bool = "CollisionBoundsGroundPerimeterOn"
val CollisionBounds_CenterMarkerOn_Bool = "CollisionBoundsCenterMarkerOn"
val CollisionBounds_PlaySpaceOn_Bool = "CollisionBoundsPlaySpaceOn"
val CollisionBounds_FadeDistance_Float = "CollisionBoundsFadeDistance"
val CollisionBounds_ColorGammaR_Int32 = "CollisionBoundsColorGammaR"
val CollisionBounds_ColorGammaG_Int32 = "CollisionBoundsColorGammaG"
val CollisionBounds_ColorGammaB_Int32 = "CollisionBoundsColorGammaB"
val CollisionBounds_ColorGammaA_Int32 = "CollisionBoundsColorGammaA"

//-----------------------------------------------------------------------------
// camera keys
val Camera_Section = "camera"
val Camera_EnableCamera_Bool = "enableCamera"
val Camera_EnableCameraInDashboard_Bool = "enableCameraInDashboard"
val Camera_EnableCameraForCollisionBounds_Bool = "enableCameraForCollisionBounds"
val Camera_EnableCameraForRoomView_Bool = "enableCameraForRoomView"
val Camera_BoundsColorGammaR_Int32 = "cameraBoundsColorGammaR"
val Camera_BoundsColorGammaG_Int32 = "cameraBoundsColorGammaG"
val Camera_BoundsColorGammaB_Int32 = "cameraBoundsColorGammaB"
val Camera_BoundsColorGammaA_Int32 = "cameraBoundsColorGammaA"
val Camera_BoundsStrength_Int32 = "cameraBoundsStrength"
val Camera_RoomViewMode_Int32 = "cameraRoomViewMode"

//-----------------------------------------------------------------------------
// audio keys
val audio_Section = "audio"
val audio_OnPlaybackDevice_String = "onPlaybackDevice"
val audio_OnRecordDevice_String = "onRecordDevice"
val audio_OnPlaybackMirrorDevice_String = "onPlaybackMirrorDevice"
val audio_OffPlaybackDevice_String = "offPlaybackDevice"
val audio_OffRecordDevice_String = "offRecordDevice"
val audio_VIVEHDMIGain = "viveHDMIGain"

//-----------------------------------------------------------------------------
// power management keys
val Power_Section = "power"
val Power_PowerOffOnExit_Bool = "powerOffOnExit"
val Power_TurnOffScreensTimeout_Float = "turnOffScreensTimeout"
val Power_TurnOffControllersTimeout_Float = "turnOffControllersTimeout"
val Power_ReturnToWatchdogTimeout_Float = "returnToWatchdogTimeout"
val Power_AutoLaunchSteamVROnButtonPress = "autoLaunchSteamVROnButtonPress"
val Power_PauseCompositorOnStandby_Bool = "pauseCompositorOnStandby"

//-----------------------------------------------------------------------------
// dashboard keys
val Dashboard_Section = "dashboard"
val Dashboard_EnableDashboard_Bool = "enableDashboard"
val Dashboard_ArcadeMode_Bool = "arcadeMode"
val Dashboard_EnableWebUI = "webUI"
val Dashboard_EnableWebUIDevTools = "webUIDevTools"

//-----------------------------------------------------------------------------
// model skin keys
val modelskin_Section = "modelskins"

//-----------------------------------------------------------------------------
// driver keys - These could be checked in any driver_<name> section
val Driver_Enable_Bool = "enable"

//-----------------------------------------------------------------------------
// web interface keys
val WebInterface_Section = "WebInterface"
val WebInterface_WebPort_String = "WebPort"