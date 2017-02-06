package openvr

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import java.util.*

// ivrsettings.h ==================================================================================================================================================

open class IVRSettings : Structure {

    fun getSettingsErrorNameFromEnum(eError: EVRSettingsError) = GetSettingsErrorNameFromEnum!!.invoke(eError.i)
    internal @JvmField var GetSettingsErrorNameFromEnum: GetSettingsErrorNameFromEnum_callback? = null

    interface GetSettingsErrorNameFromEnum_callback : Callback {
        fun invoke(eError: Int): String
    }


    // Returns true if file sync occurred (force or settings dirty)
    @JvmOverloads fun sync(bForce: Boolean = false, peError: EVRSettingsError_ByReference? = null) = Sync!!.invoke(bForce, peError)

    internal @JvmField var Sync: Sync_callback? = null

    interface Sync_callback : Callback {
        fun invoke(bForce: Boolean, peError: EVRSettingsError_ByReference?): Boolean
    }


    @JvmOverloads fun setBool(pchSection: String, pchSettingsKey: String, bValue: Boolean,
                              peError: EVRSettingsError_ByReference? = null)
            = SetBool!!.invoke(pchSection, pchSettingsKey, bValue, peError)

    internal @JvmField var SetBool: SetBool_callback? = null

    interface SetBool_callback : Callback {
        fun invoke(pchSection: String, pchSettingsKey: String, bValue: Boolean, peError: EVRSettingsError_ByReference?)
    }


    @JvmOverloads fun setInt32(pchSection: String, pchSettingsKey: String, nValue: Int, peError: EVRSettingsError_ByReference? = null)
            = SetInt32!!.invoke(pchSection, pchSettingsKey, nValue, peError)

    internal @JvmField var SetInt32: SetInt32_callback? = null

    interface SetInt32_callback : Callback {
        fun invoke(pchSection: String, pchSettingsKey: String, nValue: Int, peError: EVRSettingsError_ByReference?)
    }


    @JvmOverloads fun setFloat(pchSection: String, pchSettingsKey: String, flValue: Float,
                               peError: EVRSettingsError_ByReference? = null)
            = SetFloat!!.invoke(pchSection, pchSettingsKey, flValue, peError)

    internal @JvmField var SetFloat: SetFloat_callback? = null

    interface SetFloat_callback : Callback {
        fun invoke(pchSection: String, pchSettingsKey: String, flValue: Float, peError: EVRSettingsError_ByReference?)
    }


    @JvmOverloads fun setString(pchSection: String, pchSettingsKey: String, pchValue: String,
                                peError: EVRSettingsError_ByReference? = null)
            = SetString!!.invoke(pchSection, pchSettingsKey, pchValue, peError)

    internal @JvmField var SetString: SetString_callback? = null

    interface SetString_callback : Callback {
        fun invoke(pchSection: String, pchSettingsKey: String, pchValue: String, peError: EVRSettingsError_ByReference?)
    }


    // Users of the system need to provide a proper default in default.vrsettings in the resources/settings/ directory of either the runtime or the driver_xxx
    // directory. Otherwise the default will be false, 0, 0.0 or ""
    @JvmOverloads fun getBool(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference? = null)
            = GetBool!!.invoke(pchSection, pchSettingsKey, peError)

    internal @JvmField var GetBool: GetBool_callback? = null

    interface GetBool_callback : Callback {
        fun invoke(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference?): Boolean
    }


    @JvmOverloads fun getInt32(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference? = null)
            = GetInt32!!.invoke(pchSection, pchSettingsKey, peError)

    internal @JvmField var GetInt32: GetInt32_callback? = null

    interface GetInt32_callback : Callback {
        fun invoke(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference?): Int
    }


    @JvmOverloads fun getFloat(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference? = null)
            = GetFloat!!.invoke(pchSection, pchSettingsKey, peError)

    internal @JvmField var GetFloat: GetFloat_callback? = null

    interface GetFloat_callback : Callback {
        fun invoke(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference?): Float
    }


    fun getString(pchSection: String, pchSettingsKey: String, pchValue: String, unValueLen: Int,
                  peError: EVRSettingsError_ByReference? = null)
            = GetString!!.invoke(pchSection, pchSettingsKey, pchValue, unValueLen, peError)

    internal @JvmField var GetString: GetString_callback? = null

    interface GetString_callback : Callback {
        fun invoke(pchSection: String, pchSettingsKey: String, pchValue: String, unValueLen: Int,
                   peError: EVRSettingsError_ByReference?)
    }


    @JvmOverloads fun removeSection(pchSection: String, peError: EVRSettingsError_ByReference? = null)
            = RemoveSection!!.invoke(pchSection, peError)

    internal @JvmField var RemoveSection: RemoveSection_callback? = null

    interface RemoveSection_callback : Callback {
        fun invoke(pchSection: String, peError: EVRSettingsError_ByReference?)
    }


    @JvmOverloads fun removeKeyInSection(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference? = null)
            = RemoveKeyInSection!!.invoke(pchSection, pchSettingsKey, peError)

    internal @JvmField var RemoveKeyInSection: RemoveKeyInSection_callback? = null

    interface RemoveKeyInSection_callback : Callback {
        fun invoke(pchSection: String, pchSettingsKey: String, peError: EVRSettingsError_ByReference?)
    }


    constructor()

    override fun getFieldOrder(): List<String> = Arrays.asList("GetSettingsErrorNameFromEnum", "Sync", "SetBool", "SetInt32",
            "SetFloat", "SetString", "GetBool", "GetInt32", "GetFloat", "GetString", "RemoveSection", "RemoveKeyInSection")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRSettings(), Structure.ByReference
    class ByValue : IVRSettings(), Structure.ByValue
}

//-----------------------------------------------------------------------------
val IVRSettings_Version = "FnTable:IVRSettings_002";

//-----------------------------------------------------------------------------
// steamvr keys

val k_pch_SteamVR_Section = "steamvr"
val k_pch_SteamVR_RequireHmd_String = "requireHmd"
val k_pch_SteamVR_ForcedDriverKey_String = "forcedDriver"
val k_pch_SteamVR_ForcedHmdKey_String = "forcedHmd"
val k_pch_SteamVR_DisplayDebug_Bool = "displayDebug"
val k_pch_SteamVR_DebugProcessPipe_String = "debugProcessPipe"
val k_pch_SteamVR_EnableDistortion_Bool = "enableDistortion"
val k_pch_SteamVR_DisplayDebugX_Int32 = "displayDebugX"
val k_pch_SteamVR_DisplayDebugY_Int32 = "displayDebugY"
val k_pch_SteamVR_SendSystemButtonToAllApps_Bool = "sendSystemButtonToAllApps"
val k_pch_SteamVR_LogLevel_Int32 = "loglevel"
val k_pch_SteamVR_IPD_Float = "ipd"
val k_pch_SteamVR_Background_String = "background"
val k_pch_SteamVR_BackgroundUseDomeProjection_Bool = "backgroundUseDomeProjection"
val k_pch_SteamVR_BackgroundCameraHeight_Float = "backgroundCameraHeight"
val k_pch_SteamVR_BackgroundDomeRadius_Float = "backgroundDomeRadius"
val k_pch_SteamVR_GridColor_String = "gridColor"
val k_pch_SteamVR_PlayAreaColor_String = "playAreaColor"
val k_pch_SteamVR_ShowStage_Bool = "showStage"
val k_pch_SteamVR_ActivateMultipleDrivers_Bool = "activateMultipleDrivers"
val k_pch_SteamVR_DirectMode_Bool = "directMode"
val k_pch_SteamVR_DirectModeEdidVid_Int32 = "directModeEdidVid"
val k_pch_SteamVR_DirectModeEdidPid_Int32 = "directModeEdidPid"
val k_pch_SteamVR_UsingSpeakers_Bool = "usingSpeakers"
val k_pch_SteamVR_SpeakersForwardYawOffsetDegrees_Float = "speakersForwardYawOffsetDegrees"
val k_pch_SteamVR_BaseStationPowerManagement_Bool = "basestationPowerManagement"
val k_pch_SteamVR_NeverKillProcesses_Bool = "neverKillProcesses"
val k_pch_SteamVR_RenderTargetMultiplier_Float = "renderTargetMultiplier"
val k_pch_SteamVR_AllowAsyncReprojection_Bool = "allowAsyncReprojection";
val k_pch_SteamVR_AllowReprojection_Bool = "allowInterleavedReprojection";
val k_pch_SteamVR_ForceReprojection_Bool = "forceReprojection"
val k_pch_SteamVR_ForceFadeOnBadTracking_Bool = "forceFadeOnBadTracking"
val k_pch_SteamVR_DefaultMirrorView_Int32 = "defaultMirrorView"
val k_pch_SteamVR_ShowMirrorView_Bool = "showMirrorView"
val k_pch_SteamVR_MirrorViewGeometry_String = "mirrorViewGeometry"
val k_pch_SteamVR_StartMonitorFromAppLaunch = "startMonitorFromAppLaunch"
val k_pch_SteamVR_StartCompositorFromAppLaunch_Bool = "startCompositorFromAppLaunch"
val k_pch_SteamVR_StartDashboardFromAppLaunch_Bool = "startDashboardFromAppLaunch"
val k_pch_SteamVR_StartOverlayAppsFromDashboard_Bool = "startOverlayAppsFromDashboard"
val k_pch_SteamVR_EnableHomeApp = "enableHomeApp"
val k_pch_SteamVR_SetInitialDefaultHomeApp = "setInitialDefaultHomeApp"
val k_pch_SteamVR_CycleBackgroundImageTimeSec_Int32 = "CycleBackgroundImageTimeSec"
val k_pch_SteamVR_RetailDemo_Bool = "retailDemo"
val k_pch_SteamVR_IpdOffset_Float = "ipdOffset"


//-----------------------------------------------------------------------------
// lighthouse keys

val k_pch_Lighthouse_Section = "driver_lighthouse"
val k_pch_Lighthouse_DisableIMU_Bool = "disableimu"
val k_pch_Lighthouse_UseDisambiguation_String = "usedisambiguation"
val k_pch_Lighthouse_DisambiguationDebug_Int32 = "disambiguationdebug"
val k_pch_Lighthouse_PrimaryBasestation_Int32 = "primarybasestation"
val k_pch_Lighthouse_DBHistory_Bool = "dbhistory"

//-----------------------------------------------------------------------------
// null keys

val k_pch_Null_Section = "driver_null"
val k_pch_Null_SerialNumber_String = "serialNumber"
val k_pch_Null_ModelNumber_String = "modelNumber"
val k_pch_Null_WindowX_Int32 = "windowX"
val k_pch_Null_WindowY_Int32 = "windowY"
val k_pch_Null_WindowWidth_Int32 = "windowWidth"
val k_pch_Null_WindowHeight_Int32 = "windowHeight"
val k_pch_Null_RenderWidth_Int32 = "renderWidth"
val k_pch_Null_RenderHeight_Int32 = "renderHeight"
val k_pch_Null_SecondsFromVsyncToPhotons_Float = "secondsFromVsyncToPhotons"
val k_pch_Null_DisplayFrequency_Float = "displayFrequency"

//-----------------------------------------------------------------------------
// user interface keys
val k_pch_UserInterface_Section = "userinterface"
val k_pch_UserInterface_StatusAlwaysOnTop_Bool = "StatusAlwaysOnTop"
val k_pch_UserInterface_MinimizeToTray_Bool = "MinimizeToTray"
val k_pch_UserInterface_Screenshots_Bool = "screenshots"
val k_pch_UserInterface_ScreenshotType_Int = "screenshotType"

//-----------------------------------------------------------------------------
// notification keys
val k_pch_Notifications_Section = "notifications"
val k_pch_Notifications_DoNotDisturb_Bool = "DoNotDisturb"

//-----------------------------------------------------------------------------
// keyboard keys
val k_pch_Keyboard_Section = "keyboard"
val k_pch_Keyboard_TutorialCompletions = "TutorialCompletions"
val k_pch_Keyboard_ScaleX = "ScaleX"
val k_pch_Keyboard_ScaleY = "ScaleY"
val k_pch_Keyboard_OffsetLeftX = "OffsetLeftX"
val k_pch_Keyboard_OffsetRightX = "OffsetRightX"
val k_pch_Keyboard_OffsetY = "OffsetY"
val k_pch_Keyboard_Smoothing = "Smoothing"

//-----------------------------------------------------------------------------
// perf keys
val k_pch_Perf_Section = "perfcheck"
val k_pch_Perf_HeuristicActive_Bool = "heuristicActive"
val k_pch_Perf_NotifyInHMD_Bool = "warnInHMD"
val k_pch_Perf_NotifyOnlyOnce_Bool = "warnOnlyOnce"
val k_pch_Perf_AllowTimingStore_Bool = "allowTimingStore"
val k_pch_Perf_SaveTimingsOnExit_Bool = "saveTimingsOnExit"
val k_pch_Perf_TestData_Float = "perfTestData"

//-----------------------------------------------------------------------------
// collision bounds keys
val k_pch_CollisionBounds_Section = "collisionBounds"
val k_pch_CollisionBounds_Style_Int32 = "CollisionBoundsStyle"
val k_pch_CollisionBounds_GroundPerimeterOn_Bool = "CollisionBoundsGroundPerimeterOn"
val k_pch_CollisionBounds_CenterMarkerOn_Bool = "CollisionBoundsCenterMarkerOn"
val k_pch_CollisionBounds_PlaySpaceOn_Bool = "CollisionBoundsPlaySpaceOn"
val k_pch_CollisionBounds_FadeDistance_Float = "CollisionBoundsFadeDistance"
val k_pch_CollisionBounds_ColorGammaR_Int32 = "CollisionBoundsColorGammaR"
val k_pch_CollisionBounds_ColorGammaG_Int32 = "CollisionBoundsColorGammaG"
val k_pch_CollisionBounds_ColorGammaB_Int32 = "CollisionBoundsColorGammaB"
val k_pch_CollisionBounds_ColorGammaA_Int32 = "CollisionBoundsColorGammaA"

//-----------------------------------------------------------------------------
// camera keys
val k_pch_Camera_Section = "camera"
val k_pch_Camera_EnableCamera_Bool = "enableCamera"
val k_pch_Camera_EnableCameraInDashboard_Bool = "enableCameraInDashboard"
val k_pch_Camera_EnableCameraForCollisionBounds_Bool = "enableCameraForCollisionBounds"
val k_pch_Camera_EnableCameraForRoomView_Bool = "enableCameraForRoomView"
val k_pch_Camera_BoundsColorGammaR_Int32 = "cameraBoundsColorGammaR"
val k_pch_Camera_BoundsColorGammaG_Int32 = "cameraBoundsColorGammaG"
val k_pch_Camera_BoundsColorGammaB_Int32 = "cameraBoundsColorGammaB"
val k_pch_Camera_BoundsColorGammaA_Int32 = "cameraBoundsColorGammaA"
val k_pch_Camera_BoundsStrength_Int32 = "cameraBoundsStrength"

//-----------------------------------------------------------------------------
// audio keys
val k_pch_audio_Section = "audio"
val k_pch_audio_OnPlaybackDevice_String = "onPlaybackDevice"
val k_pch_audio_OnRecordDevice_String = "onRecordDevice"
val k_pch_audio_OnPlaybackMirrorDevice_String = "onPlaybackMirrorDevice"
val k_pch_audio_OffPlaybackDevice_String = "offPlaybackDevice"
val k_pch_audio_OffRecordDevice_String = "offRecordDevice"
val k_pch_audio_VIVEHDMIGain = "viveHDMIGain"

//-----------------------------------------------------------------------------
// power management keys
val k_pch_Power_Section = "power"
val k_pch_Power_PowerOffOnExit_Bool = "powerOffOnExit"
val k_pch_Power_TurnOffScreensTimeout_Float = "turnOffScreensTimeout"
val k_pch_Power_TurnOffControllersTimeout_Float = "turnOffControllersTimeout"
val k_pch_Power_ReturnToWatchdogTimeout_Float = "returnToWatchdogTimeout"
val k_pch_Power_AutoLaunchSteamVROnButtonPress = "autoLaunchSteamVROnButtonPress"

//-----------------------------------------------------------------------------
// dashboard keys
val k_pch_Dashboard_Section = "dashboard"
val k_pch_Dashboard_EnableDashboard_Bool = "enableDashboard"
val k_pch_Dashboard_ArcadeMode_Bool = "arcadeMode"

//-----------------------------------------------------------------------------
// model skin keys
val k_pch_modelskin_Section = "modelskins"

//-----------------------------------------------------------------------------
// driver keys - These could be checked in any driver_<name> section
val k_pch_Driver_Enable_Bool = "enable";