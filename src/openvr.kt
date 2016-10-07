import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Created by GBarbieri on 07.10.2016.
 */

const val k_unTrackingStringSize = 32
const val k_unMaxDriverDebugResponseSize = 32768
const val k_unTrackedDeviceIndex_Hmd = 0
const val k_unMaxTrackedDeviceCount = 16
const val k_unTrackedDeviceIndexOther = 4294967294
const val k_unTrackedDeviceIndexInvalid = 4294967295
const val k_unMaxPropertyStringSize = 32768
const val k_unControllerStateAxisCount = 5
const val k_ulOverlayHandleInvalid = 0L
const val k_unScreenshotHandleInvalid = 0
const val IVRSystem_Version = "IVRSystem_012"
const val IVRExtendedDisplay_Version = "IVRExtendedDisplay_001"
const val IVRTrackedCamera_Version = "IVRTrackedCamera_003"
const val k_unMaxApplicationKeyLength = 128
const val IVRApplications_Version = "IVRApplications_006"
const val IVRChaperone_Version = "IVRChaperone_003"
const val IVRChaperoneSetup_Version = "IVRChaperoneSetup_005"
const val IVRCompositor_Version = "IVRCompositor_016"
const val k_unVROverlayMaxKeyLength = 128
const val k_unVROverlayMaxNameLength = 128
const val k_unMaxOverlayCount = 64
const val IVROverlay_Version = "IVROverlay_013"
const val k_pch_Controller_Component_GDC2015 = "gdc2015"
const val k_pch_Controller_Component_Base = "base"
const val k_pch_Controller_Component_Tip = "tip"
const val k_pch_Controller_Component_HandGrip = "handgrip"
const val k_pch_Controller_Component_Status = "status"
const val IVRRenderModels_Version = "IVRRenderModels_005"
const val k_unNotificationTextMaxSize = 256
const val IVRNotifications_Version = "IVRNotifications_002"
const val k_unMaxSettingsKeyLength = 128
const val IVRSettings_Version = "IVRSettings_001"
const val k_pch_SteamVR_Section = "steamvr"
const val k_pch_SteamVR_RequireHmd_String = "requireHmd"
const val k_pch_SteamVR_ForcedDriverKey_String = "forcedDriver"
const val k_pch_SteamVR_ForcedHmdKey_String = "forcedHmd"
const val k_pch_SteamVR_DisplayDebug_Bool = "displayDebug"
const val k_pch_SteamVR_DebugProcessPipe_String = "debugProcessPipe"
const val k_pch_SteamVR_EnableDistortion_Bool = "enableDistortion"
const val k_pch_SteamVR_DisplayDebugX_Int32 = "displayDebugX"
const val k_pch_SteamVR_DisplayDebugY_Int32 = "displayDebugY"
const val k_pch_SteamVR_SendSystemButtonToAllApps_Bool = "sendSystemButtonToAllApps"
const val k_pch_SteamVR_LogLevel_Int32 = "loglevel"
const val k_pch_SteamVR_IPD_Float = "ipd"
const val k_pch_SteamVR_Background_String = "background"
const val k_pch_SteamVR_BackgroundCameraHeight_Float = "backgroundCameraHeight"
const val k_pch_SteamVR_BackgroundDomeRadius_Float = "backgroundDomeRadius"
const val k_pch_SteamVR_Environment_String = "environment"
const val k_pch_SteamVR_GridColor_String = "gridColor"
const val k_pch_SteamVR_PlayAreaColor_String = "playAreaColor"
const val k_pch_SteamVR_ShowStage_Bool = "showStage"
const val k_pch_SteamVR_ActivateMultipleDrivers_Bool = "activateMultipleDrivers"
const val k_pch_SteamVR_PowerOffOnExit_Bool = "powerOffOnExit"
const val k_pch_SteamVR_StandbyAppRunningTimeout_Float = "standbyAppRunningTimeout"
const val k_pch_SteamVR_StandbyNoAppTimeout_Float = "standbyNoAppTimeout"
const val k_pch_SteamVR_DirectMode_Bool = "directMode"
const val k_pch_SteamVR_DirectModeEdidVid_Int32 = "directModeEdidVid"
const val k_pch_SteamVR_DirectModeEdidPid_Int32 = "directModeEdidPid"
const val k_pch_SteamVR_UsingSpeakers_Bool = "usingSpeakers"
const val k_pch_SteamVR_SpeakersForwardYawOffsetDegrees_Float = "speakersForwardYawOffsetDegrees"
const val k_pch_SteamVR_BaseStationPowerManagement_Bool = "basestationPowerManagement"
const val k_pch_SteamVR_NeverKillProcesses_Bool = "neverKillProcesses"
const val k_pch_SteamVR_RenderTargetMultiplier_Float = "renderTargetMultiplier"
const val k_pch_SteamVR_AllowReprojection_Bool = "allowReprojection"
const val k_pch_SteamVR_ForceReprojection_Bool = "forceReprojection"
const val k_pch_SteamVR_ForceFadeOnBadTracking_Bool = "forceFadeOnBadTracking"
const val k_pch_SteamVR_DefaultMirrorView_Int32 = "defaultMirrorView"
const val k_pch_SteamVR_ShowMirrorView_Bool = "showMirrorView"
const val k_pch_SteamVR_StartMonitorFromAppLaunch = "startMonitorFromAppLaunch"
const val k_pch_SteamVR_AutoLaunchSteamVROnButtonPress = "autoLaunchSteamVROnButtonPress"
const val k_pch_SteamVR_UseGenericGraphcisDevice_Bool = "useGenericGraphicsDevice"
const val k_pch_Lighthouse_Section = "driver_lighthouse"
const val k_pch_Lighthouse_DisableIMU_Bool = "disableimu"
const val k_pch_Lighthouse_UseDisambiguation_String = "usedisambiguation"
const val k_pch_Lighthouse_DisambiguationDebug_Int32 = "disambiguationdebug"
const val k_pch_Lighthouse_PrimaryBasestation_Int32 = "primarybasestation"
const val k_pch_Lighthouse_LighthouseName_String = "lighthousename"
const val k_pch_Lighthouse_MaxIncidenceAngleDegrees_Float = "maxincidenceangledegrees"
const val k_pch_Lighthouse_UseLighthouseDirect_Bool = "uselighthousedirect"
const val k_pch_Lighthouse_DBHistory_Bool = "dbhistory"
const val k_pch_Null_Section = "driver_null"
const val k_pch_Null_EnableNullDriver_Bool = "enable"
const val k_pch_Null_SerialNumber_String = "serialNumber"
const val k_pch_Null_ModelNumber_String = "modelNumber"
const val k_pch_Null_WindowX_Int32 = "windowX"
const val k_pch_Null_WindowY_Int32 = "windowY"
const val k_pch_Null_WindowWidth_Int32 = "windowWidth"
const val k_pch_Null_WindowHeight_Int32 = "windowHeight"
const val k_pch_Null_RenderWidth_Int32 = "renderWidth"
const val k_pch_Null_RenderHeight_Int32 = "renderHeight"
const val k_pch_Null_SecondsFromVsyncToPhotons_Float = "secondsFromVsyncToPhotons"
const val k_pch_Null_DisplayFrequency_Float = "displayFrequency"
const val k_pch_UserInterface_Section = "userinterface"
const val k_pch_UserInterface_StatusAlwaysOnTop_Bool = "StatusAlwaysOnTop"
const val k_pch_UserInterface_Screenshots_Bool = "screenshots"
const val k_pch_UserInterface_ScreenshotType_Int = "screenshotType"
const val k_pch_Notifications_Section = "notifications"
const val k_pch_Notifications_DoNotDisturb_Bool = "DoNotDisturb"
const val k_pch_Keyboard_Section = "keyboard"
const val k_pch_Keyboard_TutorialCompletions = "TutorialCompletions"
const val k_pch_Keyboard_ScaleX = "ScaleX"
const val k_pch_Keyboard_ScaleY = "ScaleY"
const val k_pch_Keyboard_OffsetLeftX = "OffsetLeftX"
const val k_pch_Keyboard_OffsetRightX = "OffsetRightX"
const val k_pch_Keyboard_OffsetY = "OffsetY"
const val k_pch_Keyboard_Smoothing = "Smoothing"
const val k_pch_Perf_Section = "perfcheck"
const val k_pch_Perf_HeuristicActive_Bool = "heuristicActive"
const val k_pch_Perf_NotifyInHMD_Bool = "warnInHMD"
const val k_pch_Perf_NotifyOnlyOnce_Bool = "warnOnlyOnce"
const val k_pch_Perf_AllowTimingStore_Bool = "allowTimingStore"
const val k_pch_Perf_SaveTimingsOnExit_Bool = "saveTimingsOnExit"
const val k_pch_Perf_TestData_Float = "perfTestData"
const val k_pch_CollisionBounds_Section = "collisionBounds"
const val k_pch_CollisionBounds_Style_Int32 = "CollisionBoundsStyle"
const val k_pch_CollisionBounds_GroundPerimeterOn_Bool = "CollisionBoundsGroundPerimeterOn"
const val k_pch_CollisionBounds_CenterMarkerOn_Bool = "CollisionBoundsCenterMarkerOn"
const val k_pch_CollisionBounds_PlaySpaceOn_Bool = "CollisionBoundsPlaySpaceOn"
const val k_pch_CollisionBounds_FadeDistance_Float = "CollisionBoundsFadeDistance"
const val k_pch_CollisionBounds_ColorGammaR_Int32 = "CollisionBoundsColorGammaR"
const val k_pch_CollisionBounds_ColorGammaG_Int32 = "CollisionBoundsColorGammaG"
const val k_pch_CollisionBounds_ColorGammaB_Int32 = "CollisionBoundsColorGammaB"
const val k_pch_CollisionBounds_ColorGammaA_Int32 = "CollisionBoundsColorGammaA"
const val k_pch_Camera_Section = "camera"
const val k_pch_Camera_EnableCamera_Bool = "enableCamera"
const val k_pch_Camera_EnableCameraInDashboard_Bool = "enableCameraInDashboard"
const val k_pch_Camera_EnableCameraForCollisionBounds_Bool = "enableCameraForCollisionBounds"
const val k_pch_Camera_EnableCameraForRoomView_Bool = "enableCameraForRoomView"
const val k_pch_Camera_BoundsColorGammaR_Int32 = "cameraBoundsColorGammaR"
const val k_pch_Camera_BoundsColorGammaG_Int32 = "cameraBoundsColorGammaG"
const val k_pch_Camera_BoundsColorGammaB_Int32 = "cameraBoundsColorGammaB"
const val k_pch_Camera_BoundsColorGammaA_Int32 = "cameraBoundsColorGammaA"
const val k_pch_audio_Section = "audio"
const val k_pch_audio_OnPlaybackDevice_String = "onPlaybackDevice"
const val k_pch_audio_OnRecordDevice_String = "onRecordDevice"
const val k_pch_audio_OnPlaybackMirrorDevice_String = "onPlaybackMirrorDevice"
const val k_pch_audio_OffPlaybackDevice_String = "offPlaybackDevice"
const val k_pch_audio_OffRecordDevice_String = "offRecordDevice"
const val k_pch_audio_VIVEHDMIGain = "viveHDMIGain"
const val k_pch_modelskin_Section = "modelskins"
const val IVRScreenshots_Version = "IVRScreenshots_001"
const val IVRResources_Version = "IVRResources_001"

// OpenVR Enums

enum class EVREye(val v: Int) {
    EVREye_Eye_Left(0),
    EVREye_Eye_Right(1)
}

enum class EGraphicsAPIConvention(val v: Int) {
    EGraphicsAPIConvention_API_DirectX(0),
    EGraphicsAPIConvention_API_OpenGL(1)
}

enum class EColorSpace(val v: Int) {
    EColorSpace_ColorSpace_Auto(0),
    EColorSpace_ColorSpace_Gamma(1),
    EColorSpace_ColorSpace_Linear(2)
}

enum class ETrackingResult(val i: Int) {
    ETrackingResult_TrackingResult_Uninitialized(1),
    ETrackingResult_TrackingResult_Calibrating_InProgress(100),
    ETrackingResult_TrackingResult_Calibrating_OutOfRange(101),
    ETrackingResult_TrackingResult_Running_OK(200),
    ETrackingResult_TrackingResult_Running_OutOfRange(201)
}

enum class ETrackedDeviceClass(val i: Int) {
    ETrackedDeviceClass_TrackedDeviceClass_Invalid(0),
    ETrackedDeviceClass_TrackedDeviceClass_HMD(1),
    ETrackedDeviceClass_TrackedDeviceClass_Controller(2),
    ETrackedDeviceClass_TrackedDeviceClass_TrackingReference(4),
    ETrackedDeviceClass_TrackedDeviceClass_Other(1000)
}

enum class ETrackedControllerRole(val i: Int) {
    ETrackedControllerRole_TrackedControllerRole_Invalid(0),
    ETrackedControllerRole_TrackedControllerRole_LeftHand(1),
    ETrackedControllerRole_TrackedControllerRole_RightHand(2)
}

enum class ETrackingUniverseOrigin(val i: Int) {
    ETrackingUniverseOrigin_TrackingUniverseSeated(0),
    ETrackingUniverseOrigin_TrackingUniverseStanding(1),
    ETrackingUniverseOrigin_TrackingUniverseRawAndUncalibrated(2)
}

enum class ETrackedDeviceProperty(val i: Int) {
    ETrackedDeviceProperty_Prop_TrackingSystemName_String(1000),
    ETrackedDeviceProperty_Prop_ModelNumber_String(1001),
    ETrackedDeviceProperty_Prop_SerialNumber_String(1002),
    ETrackedDeviceProperty_Prop_RenderModelName_String(1003),
    ETrackedDeviceProperty_Prop_WillDriftInYaw_Bool(1004),
    ETrackedDeviceProperty_Prop_ManufacturerName_String(1005),
    ETrackedDeviceProperty_Prop_TrackingFirmwareVersion_String(1006),
    ETrackedDeviceProperty_Prop_HardwareRevision_String(1007),
    ETrackedDeviceProperty_Prop_AllWirelessDongleDescriptions_String(1008),
    ETrackedDeviceProperty_Prop_ConnectedWirelessDongle_String(1009),
    ETrackedDeviceProperty_Prop_DeviceIsWireless_Bool(1010),
    ETrackedDeviceProperty_Prop_DeviceIsCharging_Bool(1011),
    ETrackedDeviceProperty_Prop_DeviceBatteryPercentage_Float(1012),
    ETrackedDeviceProperty_Prop_StatusDisplayTransform_Matrix34(1013),
    ETrackedDeviceProperty_Prop_Firmware_UpdateAvailable_Bool(1014),
    ETrackedDeviceProperty_Prop_Firmware_ManualUpdate_Bool(1015),
    ETrackedDeviceProperty_Prop_Firmware_ManualUpdateURL_String(1016),
    ETrackedDeviceProperty_Prop_HardwareRevision_Uint64(1017),
    ETrackedDeviceProperty_Prop_FirmwareVersion_Uint64(1018),
    ETrackedDeviceProperty_Prop_FPGAVersion_Uint64(1019),
    ETrackedDeviceProperty_Prop_VRCVersion_Uint64(1020),
    ETrackedDeviceProperty_Prop_RadioVersion_Uint64(1021),
    ETrackedDeviceProperty_Prop_DongleVersion_Uint64(1022),
    ETrackedDeviceProperty_Prop_BlockServerShutdown_Bool(1023),
    ETrackedDeviceProperty_Prop_CanUnifyCoordinateSystemWithHmd_Bool(1024),
    ETrackedDeviceProperty_Prop_ContainsProximitySensor_Bool(1025),
    ETrackedDeviceProperty_Prop_DeviceProvidesBatteryStatus_Bool(1026),
    ETrackedDeviceProperty_Prop_DeviceCanPowerOff_Bool(1027),
    ETrackedDeviceProperty_Prop_Firmware_ProgrammingTarget_String(1028),
    ETrackedDeviceProperty_Prop_DeviceClass_Int32(1029),
    ETrackedDeviceProperty_Prop_HasCamera_Bool(1030),
    ETrackedDeviceProperty_Prop_DriverVersion_String(1031),
    ETrackedDeviceProperty_Prop_Firmware_ForceUpdateRequired_Bool(1032),
    ETrackedDeviceProperty_Prop_ReportsTimeSinceVSync_Bool(2000),
    ETrackedDeviceProperty_Prop_SecondsFromVsyncToPhotons_Float(2001),
    ETrackedDeviceProperty_Prop_DisplayFrequency_Float(2002),
    ETrackedDeviceProperty_Prop_UserIpdMeters_Float(2003),
    ETrackedDeviceProperty_Prop_CurrentUniverseId_Uint64(2004),
    ETrackedDeviceProperty_Prop_PreviousUniverseId_Uint64(2005),
    ETrackedDeviceProperty_Prop_DisplayFirmwareVersion_Uint64(2006),
    ETrackedDeviceProperty_Prop_IsOnDesktop_Bool(2007),
    ETrackedDeviceProperty_Prop_DisplayMCType_Int32(2008),
    ETrackedDeviceProperty_Prop_DisplayMCOffset_Float(2009),
    ETrackedDeviceProperty_Prop_DisplayMCScale_Float(2010),
    ETrackedDeviceProperty_Prop_EdidVendorID_Int32(2011),
    ETrackedDeviceProperty_Prop_DisplayMCImageLeft_String(2012),
    ETrackedDeviceProperty_Prop_DisplayMCImageRight_String(2013),
    ETrackedDeviceProperty_Prop_DisplayGCBlackClamp_Float(2014),
    ETrackedDeviceProperty_Prop_EdidProductID_Int32(2015),
    ETrackedDeviceProperty_Prop_CameraToHeadTransform_Matrix34(2016),
    ETrackedDeviceProperty_Prop_DisplayGCType_Int32(2017),
    ETrackedDeviceProperty_Prop_DisplayGCOffset_Float(2018),
    ETrackedDeviceProperty_Prop_DisplayGCScale_Float(2019),
    ETrackedDeviceProperty_Prop_DisplayGCPrescale_Float(2020),
    ETrackedDeviceProperty_Prop_DisplayGCImage_String(2021),
    ETrackedDeviceProperty_Prop_LensCenterLeftU_Float(2022),
    ETrackedDeviceProperty_Prop_LensCenterLeftV_Float(2023),
    ETrackedDeviceProperty_Prop_LensCenterRightU_Float(2024),
    ETrackedDeviceProperty_Prop_LensCenterRightV_Float(2025),
    ETrackedDeviceProperty_Prop_UserHeadToEyeDepthMeters_Float(2026),
    ETrackedDeviceProperty_Prop_CameraFirmwareVersion_Uint64(2027),
    ETrackedDeviceProperty_Prop_CameraFirmwareDescription_String(2028),
    ETrackedDeviceProperty_Prop_DisplayFPGAVersion_Uint64(2029),
    ETrackedDeviceProperty_Prop_DisplayBootloaderVersion_Uint64(2030),
    ETrackedDeviceProperty_Prop_DisplayHardwareVersion_Uint64(2031),
    ETrackedDeviceProperty_Prop_AudioFirmwareVersion_Uint64(2032),
    ETrackedDeviceProperty_Prop_CameraCompatibilityMode_Int32(2033),
    ETrackedDeviceProperty_Prop_ScreenshotHorizontalFieldOfViewDegrees_Float(2034),
    ETrackedDeviceProperty_Prop_ScreenshotVerticalFieldOfViewDegrees_Float(2035),
    ETrackedDeviceProperty_Prop_DisplaySuppressed_Bool(2036),
    ETrackedDeviceProperty_Prop_AttachedDeviceId_String(3000),
    ETrackedDeviceProperty_Prop_SupportedButtons_Uint64(3001),
    ETrackedDeviceProperty_Prop_Axis0Type_Int32(3002),
    ETrackedDeviceProperty_Prop_Axis1Type_Int32(3003),
    ETrackedDeviceProperty_Prop_Axis2Type_Int32(3004),
    ETrackedDeviceProperty_Prop_Axis3Type_Int32(3005),
    ETrackedDeviceProperty_Prop_Axis4Type_Int32(3006),
    ETrackedDeviceProperty_Prop_ControllerRoleHint_Int32(3007),
    ETrackedDeviceProperty_Prop_FieldOfViewLeftDegrees_Float(4000),
    ETrackedDeviceProperty_Prop_FieldOfViewRightDegrees_Float(4001),
    ETrackedDeviceProperty_Prop_FieldOfViewTopDegrees_Float(4002),
    ETrackedDeviceProperty_Prop_FieldOfViewBottomDegrees_Float(4003),
    ETrackedDeviceProperty_Prop_TrackingRangeMinimumMeters_Float(4004),
    ETrackedDeviceProperty_Prop_TrackingRangeMaximumMeters_Float(4005),
    ETrackedDeviceProperty_Prop_ModeLabel_String(4006),
    ETrackedDeviceProperty_Prop_VendorSpecific_Reserved_Start(10000),
    ETrackedDeviceProperty_Prop_VendorSpecific_Reserved_End(10999)
}

enum class ETrackedPropertyError(val i: Int) {
    ETrackedPropertyError_TrackedProp_Success(0),
    ETrackedPropertyError_TrackedProp_WrongDataType(1),
    ETrackedPropertyError_TrackedProp_WrongDeviceClass(2),
    ETrackedPropertyError_TrackedProp_BufferTooSmall(3),
    ETrackedPropertyError_TrackedProp_UnknownProperty(4),
    ETrackedPropertyError_TrackedProp_InvalidDevice(5),
    ETrackedPropertyError_TrackedProp_CouldNotContactServer(6),
    ETrackedPropertyError_TrackedProp_ValueNotProvidedByDevice(7),
    ETrackedPropertyError_TrackedProp_StringExceedsMaximumLength(8),
    ETrackedPropertyError_TrackedProp_NotYetAvailable(9)
}

enum class EVRSubmitFlags(val i: Int) {
    EVRSubmitFlags_Submit_Default(0),
    EVRSubmitFlags_Submit_LensDistortionAlreadyApplied(1),
    EVRSubmitFlags_Submit_GlRenderBuffer(2)
}

enum class EVRState(val i: Int) {
    EVRState_VRState_Undefined(-1),
    EVRState_VRState_Off(0),
    EVRState_VRState_Searching(1),
    EVRState_VRState_Searching_Alert(2),
    EVRState_VRState_Ready(3),
    EVRState_VRState_Ready_Alert(4),
    EVRState_VRState_NotReady(5),
    EVRState_VRState_Standby(6),
    EVRState_VRState_Ready_Alert_Low(7)
}

enum class EVREventType(val i: Int) {
    EVREventType_VREvent_None(0),
    EVREventType_VREvent_TrackedDeviceActivated(100),
    EVREventType_VREvent_TrackedDeviceDeactivated(101),
    EVREventType_VREvent_TrackedDeviceUpdated(102),
    EVREventType_VREvent_TrackedDeviceUserInteractionStarted(103),
    EVREventType_VREvent_TrackedDeviceUserInteractionEnded(104),
    EVREventType_VREvent_IpdChanged(105),
    EVREventType_VREvent_EnterStandbyMode(106),
    EVREventType_VREvent_LeaveStandbyMode(107),
    EVREventType_VREvent_TrackedDeviceRoleChanged(108),
    EVREventType_VREvent_WatchdogWakeUpRequested(109),
    EVREventType_VREvent_ButtonPress(200),
    EVREventType_VREvent_ButtonUnpress(201),
    EVREventType_VREvent_ButtonTouch(202),
    EVREventType_VREvent_ButtonUntouch(203),
    EVREventType_VREvent_MouseMove(300),
    EVREventType_VREvent_MouseButtonDown(301),
    EVREventType_VREvent_MouseButtonUp(302),
    EVREventType_VREvent_FocusEnter(303),
    EVREventType_VREvent_FocusLeave(304),
    EVREventType_VREvent_Scroll(305),
    EVREventType_VREvent_TouchPadMove(306),
    EVREventType_VREvent_OverlayFocusChanged(307),
    EVREventType_VREvent_InputFocusCaptured(400),
    EVREventType_VREvent_InputFocusReleased(401),
    EVREventType_VREvent_SceneFocusLost(402),
    EVREventType_VREvent_SceneFocusGained(403),
    EVREventType_VREvent_SceneApplicationChanged(404),
    EVREventType_VREvent_SceneFocusChanged(405),
    EVREventType_VREvent_InputFocusChanged(406),
    EVREventType_VREvent_SceneApplicationSecondaryRenderingStarted(407),
    EVREventType_VREvent_HideRenderModels(410),
    EVREventType_VREvent_ShowRenderModels(411),
    EVREventType_VREvent_OverlayShown(500),
    EVREventType_VREvent_OverlayHidden(501),
    EVREventType_VREvent_DashboardActivated(502),
    EVREventType_VREvent_DashboardDeactivated(503),
    EVREventType_VREvent_DashboardThumbSelected(504),
    EVREventType_VREvent_DashboardRequested(505),
    EVREventType_VREvent_ResetDashboard(506),
    EVREventType_VREvent_RenderToast(507),
    EVREventType_VREvent_ImageLoaded(508),
    EVREventType_VREvent_ShowKeyboard(509),
    EVREventType_VREvent_HideKeyboard(510),
    EVREventType_VREvent_OverlayGamepadFocusGained(511),
    EVREventType_VREvent_OverlayGamepadFocusLost(512),
    EVREventType_VREvent_OverlaySharedTextureChanged(513),
    EVREventType_VREvent_DashboardGuideButtonDown(514),
    EVREventType_VREvent_DashboardGuideButtonUp(515),
    EVREventType_VREvent_ScreenshotTriggered(516),
    EVREventType_VREvent_ImageFailed(517),
    EVREventType_VREvent_RequestScreenshot(520),
    EVREventType_VREvent_ScreenshotTaken(521),
    EVREventType_VREvent_ScreenshotFailed(522),
    EVREventType_VREvent_SubmitScreenshotToDashboard(523),
    EVREventType_VREvent_ScreenshotProgressToDashboard(524),
    EVREventType_VREvent_Notification_Shown(600),
    EVREventType_VREvent_Notification_Hidden(601),
    EVREventType_VREvent_Notification_BeginInteraction(602),
    EVREventType_VREvent_Notification_Destroyed(603),
    EVREventType_VREvent_Quit(700),
    EVREventType_VREvent_ProcessQuit(701),
    EVREventType_VREvent_QuitAborted_UserPrompt(702),
    EVREventType_VREvent_QuitAcknowledged(703),
    EVREventType_VREvent_DriverRequestedQuit(704),
    EVREventType_VREvent_ChaperoneDataHasChanged(800),
    EVREventType_VREvent_ChaperoneUniverseHasChanged(801),
    EVREventType_VREvent_ChaperoneTempDataHasChanged(802),
    EVREventType_VREvent_ChaperoneSettingsHaveChanged(803),
    EVREventType_VREvent_SeatedZeroPoseReset(804),
    EVREventType_VREvent_AudioSettingsHaveChanged(820),
    EVREventType_VREvent_BackgroundSettingHasChanged(850),
    EVREventType_VREvent_CameraSettingsHaveChanged(851),
    EVREventType_VREvent_ReprojectionSettingHasChanged(852),
    EVREventType_VREvent_ModelSkinSettingsHaveChanged(853),
    EVREventType_VREvent_EnvironmentSettingsHaveChanged(854),
    EVREventType_VREvent_StatusUpdate(900),
    EVREventType_VREvent_MCImageUpdated(1000),
    EVREventType_VREvent_FirmwareUpdateStarted(1100),
    EVREventType_VREvent_FirmwareUpdateFinished(1101),
    EVREventType_VREvent_KeyboardClosed(1200),
    EVREventType_VREvent_KeyboardCharInput(1201),
    EVREventType_VREvent_KeyboardDone(1202),
    EVREventType_VREvent_ApplicationTransitionStarted(1300),
    EVREventType_VREvent_ApplicationTransitionAborted(1301),
    EVREventType_VREvent_ApplicationTransitionNewAppStarted(1302),
    EVREventType_VREvent_ApplicationListUpdated(1303),
    EVREventType_VREvent_ApplicationMimeTypeLoad(1304),
    EVREventType_VREvent_Compositor_MirrorWindowShown(1400),
    EVREventType_VREvent_Compositor_MirrorWindowHidden(1401),
    EVREventType_VREvent_Compositor_ChaperoneBoundsShown(1410),
    EVREventType_VREvent_Compositor_ChaperoneBoundsHidden(1411),
    EVREventType_VREvent_TrackedCamera_StartVideoStream(1500),
    EVREventType_VREvent_TrackedCamera_StopVideoStream(1501),
    EVREventType_VREvent_TrackedCamera_PauseVideoStream(1502),
    EVREventType_VREvent_TrackedCamera_ResumeVideoStream(1503),
    EVREventType_VREvent_PerformanceTest_EnableCapture(1600),
    EVREventType_VREvent_PerformanceTest_DisableCapture(1601),
    EVREventType_VREvent_PerformanceTest_FidelityLevel(1602),
    EVREventType_VREvent_VendorSpecific_Reserved_Start(10000),
    EVREventType_VREvent_VendorSpecific_Reserved_End(19999)
}

enum class EDeviceActivityLevel(val i: Int) {
    EDeviceActivityLevel_k_EDeviceActivityLevel_Unknown(-1),
    EDeviceActivityLevel_k_EDeviceActivityLevel_Idle(0),
    EDeviceActivityLevel_k_EDeviceActivityLevel_UserInteraction(1),
    EDeviceActivityLevel_k_EDeviceActivityLevel_UserInteraction_Timeout(2),
    EDeviceActivityLevel_k_EDeviceActivityLevel_Standby(3)
}

enum class EVRButtonId(val i: Int) {
    EVRButtonId_k_EButton_System(0),
    EVRButtonId_k_EButton_ApplicationMenu(1),
    EVRButtonId_k_EButton_Grip(2),
    EVRButtonId_k_EButton_DPad_Left(3),
    EVRButtonId_k_EButton_DPad_Up(4),
    EVRButtonId_k_EButton_DPad_Right(5),
    EVRButtonId_k_EButton_DPad_Down(6),
    EVRButtonId_k_EButton_A(7),
    EVRButtonId_k_EButton_Axis0(32),
    EVRButtonId_k_EButton_Axis1(33),
    EVRButtonId_k_EButton_Axis2(34),
    EVRButtonId_k_EButton_Axis3(35),
    EVRButtonId_k_EButton_Axis4(36),
    EVRButtonId_k_EButton_SteamVR_Touchpad(32),
    EVRButtonId_k_EButton_SteamVR_Trigger(33),
    EVRButtonId_k_EButton_Dashboard_Back(2),
    EVRButtonId_k_EButton_Max(64)
}

enum class EVRMouseButton(val i: Int) {
    EVRMouseButton_VRMouseButton_Left(1),
    EVRMouseButton_VRMouseButton_Right(2),
    EVRMouseButton_VRMouseButton_Middle(4)
}

enum class EVRControllerAxisType(val i: Int) {
    EVRControllerAxisType_k_eControllerAxis_None(0),
    EVRControllerAxisType_k_eControllerAxis_TrackPad(1),
    EVRControllerAxisType_k_eControllerAxis_Joystick(2),
    EVRControllerAxisType_k_eControllerAxis_Trigger(3)
}

enum class EVRControllerEventOutputType(val i: Int) {
    EVRControllerEventOutputType_ControllerEventOutput_OSEvents(0),
    EVRControllerEventOutputType_ControllerEventOutput_VREvents(1)
}

enum class ECollisionBoundsStyle(val i: Int) {
    ECollisionBoundsStyle_COLLISION_BOUNDS_STYLE_BEGINNER(0),
    ECollisionBoundsStyle_COLLISION_BOUNDS_STYLE_INTERMEDIATE(1),
    ECollisionBoundsStyle_COLLISION_BOUNDS_STYLE_SQUARES(2),
    ECollisionBoundsStyle_COLLISION_BOUNDS_STYLE_ADVANCED(3),
    ECollisionBoundsStyle_COLLISION_BOUNDS_STYLE_NONE(4),
    ECollisionBoundsStyle_COLLISION_BOUNDS_STYLE_COUNT(5)
}

enum class EVROverlayError(val i: Int) {
    EVROverlayError_VROverlayError_None(0),
    EVROverlayError_VROverlayError_UnknownOverlay(10),
    EVROverlayError_VROverlayError_InvalidHandle(11),
    EVROverlayError_VROverlayError_PermissionDenied(12),
    EVROverlayError_VROverlayError_OverlayLimitExceeded(13),
    EVROverlayError_VROverlayError_WrongVisibilityType(14),
    EVROverlayError_VROverlayError_KeyTooLong(15),
    EVROverlayError_VROverlayError_NameTooLong(16),
    EVROverlayError_VROverlayError_KeyInUse(17),
    EVROverlayError_VROverlayError_WrongTransformType(18),
    EVROverlayError_VROverlayError_InvalidTrackedDevice(19),
    EVROverlayError_VROverlayError_InvalidParameter(20),
    EVROverlayError_VROverlayError_ThumbnailCantBeDestroyed(21),
    EVROverlayError_VROverlayError_ArrayTooSmall(22),
    EVROverlayError_VROverlayError_RequestFailed(23),
    EVROverlayError_VROverlayError_InvalidTexture(24),
    EVROverlayError_VROverlayError_UnableToLoadFile(25),
    EVROverlayError_VROVerlayError_KeyboardAlreadyInUse(26),
    EVROverlayError_VROverlayError_NoNeighbor(27)
}

enum class EVRApplicationType(val i: Int) {
    EVRApplicationType_VRApplication_Other(0),
    EVRApplicationType_VRApplication_Scene(1),
    EVRApplicationType_VRApplication_Overlay(2),
    EVRApplicationType_VRApplication_Background(3),
    EVRApplicationType_VRApplication_Utility(4),
    EVRApplicationType_VRApplication_VRMonitor(5),
    EVRApplicationType_VRApplication_SteamWatchdog(6),
    EVRApplicationType_VRApplication_Max(7)
}

enum class EVRFirmwareError(val i: Int) {
    EVRFirmwareError_VRFirmwareError_None(0),
    EVRFirmwareError_VRFirmwareError_Success(1),
    EVRFirmwareError_VRFirmwareError_Fail(2)
}

enum class EVRNotificationError(val i: Int) {
    EVRNotificationError_VRNotificationError_OK(0),
    EVRNotificationError_VRNotificationError_InvalidNotificationId(100),
    EVRNotificationError_VRNotificationError_NotificationQueueFull(101),
    EVRNotificationError_VRNotificationError_InvalidOverlayHandle(102),
    EVRNotificationError_VRNotificationError_SystemWithUserValueAlreadyExists(103)
}

enum class EVRInitError(val i: Int) {
    EVRInitError_VRInitError_None(0),
    EVRInitError_VRInitError_Unknown(1),
    EVRInitError_VRInitError_Init_InstallationNotFound(100),
    EVRInitError_VRInitError_Init_InstallationCorrupt(101),
    EVRInitError_VRInitError_Init_VRClientDLLNotFound(102),
    EVRInitError_VRInitError_Init_FileNotFound(103),
    EVRInitError_VRInitError_Init_FactoryNotFound(104),
    EVRInitError_VRInitError_Init_InterfaceNotFound(105),
    EVRInitError_VRInitError_Init_InvalidInterface(106),
    EVRInitError_VRInitError_Init_UserConfigDirectoryInvalid(107),
    EVRInitError_VRInitError_Init_HmdNotFound(108),
    EVRInitError_VRInitError_Init_NotInitialized(109),
    EVRInitError_VRInitError_Init_PathRegistryNotFound(110),
    EVRInitError_VRInitError_Init_NoConfigPath(111),
    EVRInitError_VRInitError_Init_NoLogPath(112),
    EVRInitError_VRInitError_Init_PathRegistryNotWritable(113),
    EVRInitError_VRInitError_Init_AppInfoInitFailed(114),
    EVRInitError_VRInitError_Init_Retry(115),
    EVRInitError_VRInitError_Init_InitCanceledByUser(116),
    EVRInitError_VRInitError_Init_AnotherAppLaunching(117),
    EVRInitError_VRInitError_Init_SettingsInitFailed(118),
    EVRInitError_VRInitError_Init_ShuttingDown(119),
    EVRInitError_VRInitError_Init_TooManyObjects(120),
    EVRInitError_VRInitError_Init_NoServerForBackgroundApp(121),
    EVRInitError_VRInitError_Init_NotSupportedWithCompositor(122),
    EVRInitError_VRInitError_Init_NotAvailableToUtilityApps(123),
    EVRInitError_VRInitError_Init_Internal(124),
    EVRInitError_VRInitError_Init_HmdDriverIdIsNone(125),
    EVRInitError_VRInitError_Init_HmdNotFoundPresenceFailed(126),
    EVRInitError_VRInitError_Init_VRMonitorNotFound(127),
    EVRInitError_VRInitError_Init_VRMonitorStartupFailed(128),
    EVRInitError_VRInitError_Init_LowPowerWatchdogNotSupported(129),
    EVRInitError_VRInitError_Init_InvalidApplicationType(130),
    EVRInitError_VRInitError_Init_NotAvailableToWatchdogApps(131),
    EVRInitError_VRInitError_Init_WatchdogDisabledInSettings(132),
    EVRInitError_VRInitError_Driver_Failed(200),
    EVRInitError_VRInitError_Driver_Unknown(201),
    EVRInitError_VRInitError_Driver_HmdUnknown(202),
    EVRInitError_VRInitError_Driver_NotLoaded(203),
    EVRInitError_VRInitError_Driver_RuntimeOutOfDate(204),
    EVRInitError_VRInitError_Driver_HmdInUse(205),
    EVRInitError_VRInitError_Driver_NotCalibrated(206),
    EVRInitError_VRInitError_Driver_CalibrationInvalid(207),
    EVRInitError_VRInitError_Driver_HmdDisplayNotFound(208),
    EVRInitError_VRInitError_Driver_TrackedDeviceInterfaceUnknown(209),
    EVRInitError_VRInitError_Driver_HmdDriverIdOutOfBounds(211),
    EVRInitError_VRInitError_Driver_HmdDisplayMirrored(212),
    EVRInitError_VRInitError_IPC_ServerInitFailed(300),
    EVRInitError_VRInitError_IPC_ConnectFailed(301),
    EVRInitError_VRInitError_IPC_SharedStateInitFailed(302),
    EVRInitError_VRInitError_IPC_CompositorInitFailed(303),
    EVRInitError_VRInitError_IPC_MutexInitFailed(304),
    EVRInitError_VRInitError_IPC_Failed(305),
    EVRInitError_VRInitError_IPC_CompositorConnectFailed(306),
    EVRInitError_VRInitError_IPC_CompositorInvalidConnectResponse(307),
    EVRInitError_VRInitError_IPC_ConnectFailedAfterMultipleAttempts(308),
    EVRInitError_VRInitError_Compositor_Failed(400),
    EVRInitError_VRInitError_Compositor_D3D11HardwareRequired(401),
    EVRInitError_VRInitError_Compositor_FirmwareRequiresUpdate(402),
    EVRInitError_VRInitError_Compositor_OverlayInitFailed(403),
    EVRInitError_VRInitError_Compositor_ScreenshotsInitFailed(404),
    EVRInitError_VRInitError_VendorSpecific_UnableToConnectToOculusRuntime(1000),
    EVRInitError_VRInitError_VendorSpecific_HmdFound_CantOpenDevice(1101),
    EVRInitError_VRInitError_VendorSpecific_HmdFound_UnableToRequestConfigStart(1102),
    EVRInitError_VRInitError_VendorSpecific_HmdFound_NoStoredConfig(1103),
    EVRInitError_VRInitError_VendorSpecific_HmdFound_ConfigTooBig(1104),
    EVRInitError_VRInitError_VendorSpecific_HmdFound_ConfigTooSmall(1105),
    EVRInitError_VRInitError_VendorSpecific_HmdFound_UnableToInitZLib(1106),
    EVRInitError_VRInitError_VendorSpecific_HmdFound_CantReadFirmwareVersion(1107),
    EVRInitError_VRInitError_VendorSpecific_HmdFound_UnableToSendUserDataStart(1108),
    EVRInitError_VRInitError_VendorSpecific_HmdFound_UnableToGetUserDataStart(1109),
    EVRInitError_VRInitError_VendorSpecific_HmdFound_UnableToGetUserDataNext(1110),
    EVRInitError_VRInitError_VendorSpecific_HmdFound_UserDataAddressRange(1111),
    EVRInitError_VRInitError_VendorSpecific_HmdFound_UserDataError(1112),
    EVRInitError_VRInitError_VendorSpecific_HmdFound_ConfigFailedSanityCheck(1113),
    EVRInitError_VRInitError_Steam_SteamInstallationNotFound(2000)
}

enum class EVRScreenshotType(val i: Int) {
    EVRScreenshotType_VRScreenshotType_None(0),
    EVRScreenshotType_VRScreenshotType_Mono(1),
    EVRScreenshotType_VRScreenshotType_Stereo(2),
    EVRScreenshotType_VRScreenshotType_Cubemap(3),
    EVRScreenshotType_VRScreenshotType_MonoPanorama(4),
    EVRScreenshotType_VRScreenshotType_StereoPanorama(5)
}

enum class EVRScreenshotPropertyFilenames(val i: Int) {
    EVRScreenshotPropertyFilenames_VRScreenshotPropertyFilenames_Preview(0),
    EVRScreenshotPropertyFilenames_VRScreenshotPropertyFilenames_VR(1)
}

enum class EVRTrackedCameraError(val i: Int) {
    EVRTrackedCameraError_VRTrackedCameraError_None(0),
    EVRTrackedCameraError_VRTrackedCameraError_OperationFailed(100),
    EVRTrackedCameraError_VRTrackedCameraError_InvalidHandle(101),
    EVRTrackedCameraError_VRTrackedCameraError_InvalidFrameHeaderVersion(102),
    EVRTrackedCameraError_VRTrackedCameraError_OutOfHandles(103),
    EVRTrackedCameraError_VRTrackedCameraError_IPCFailure(104),
    EVRTrackedCameraError_VRTrackedCameraError_NotSupportedForThisDevice(105),
    EVRTrackedCameraError_VRTrackedCameraError_SharedMemoryFailure(106),
    EVRTrackedCameraError_VRTrackedCameraError_FrameBufferingFailure(107),
    EVRTrackedCameraError_VRTrackedCameraError_StreamSetupFailure(108),
    EVRTrackedCameraError_VRTrackedCameraError_InvalidGLTextureId(109),
    EVRTrackedCameraError_VRTrackedCameraError_InvalidSharedTextureHandle(110),
    EVRTrackedCameraError_VRTrackedCameraError_FailedToGetGLTextureId(111),
    EVRTrackedCameraError_VRTrackedCameraError_SharedTextureFailure(112),
    EVRTrackedCameraError_VRTrackedCameraError_NoFrameAvailable(113),
    EVRTrackedCameraError_VRTrackedCameraError_InvalidArgument(114),
    EVRTrackedCameraError_VRTrackedCameraError_InvalidFrameBufferSize(115)
}

enum class EVRTrackedCameraFrameType(val i: Int) {
    EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Distorted(0),
    EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Undistorted(1),
    EVRTrackedCameraFrameType_VRTrackedCameraFrameType_MaximumUndistorted(2),
    EVRTrackedCameraFrameType_MAX_CAMERA_FRAME_TYPES(3)
}

enum class EVRApplicationError(val i: Int) {
    EVRApplicationError_VRApplicationError_None(0),
    EVRApplicationError_VRApplicationError_AppKeyAlreadyExists(100),
    EVRApplicationError_VRApplicationError_NoManifest(101),
    EVRApplicationError_VRApplicationError_NoApplication(102),
    EVRApplicationError_VRApplicationError_InvalidIndex(103),
    EVRApplicationError_VRApplicationError_UnknownApplication(104),
    EVRApplicationError_VRApplicationError_IPCFailed(105),
    EVRApplicationError_VRApplicationError_ApplicationAlreadyRunning(106),
    EVRApplicationError_VRApplicationError_InvalidManifest(107),
    EVRApplicationError_VRApplicationError_InvalidApplication(108),
    EVRApplicationError_VRApplicationError_LaunchFailed(109),
    EVRApplicationError_VRApplicationError_ApplicationAlreadyStarting(110),
    EVRApplicationError_VRApplicationError_LaunchInProgress(111),
    EVRApplicationError_VRApplicationError_OldApplicationQuitting(112),
    EVRApplicationError_VRApplicationError_TransitionAborted(113),
    EVRApplicationError_VRApplicationError_IsTemplate(114),
    EVRApplicationError_VRApplicationError_BufferTooSmall(200),
    EVRApplicationError_VRApplicationError_PropertyNotSet(201),
    EVRApplicationError_VRApplicationError_UnknownProperty(202),
    EVRApplicationError_VRApplicationError_InvalidParameter(203)
}

enum class EVRApplicationProperty(val i: Int) {
    EVRApplicationProperty_VRApplicationProperty_Name_String(0),
    EVRApplicationProperty_VRApplicationProperty_LaunchType_String(11),
    EVRApplicationProperty_VRApplicationProperty_WorkingDirectory_String(12),
    EVRApplicationProperty_VRApplicationProperty_BinaryPath_String(13),
    EVRApplicationProperty_VRApplicationProperty_Arguments_String(14),
    EVRApplicationProperty_VRApplicationProperty_URL_String(15),
    EVRApplicationProperty_VRApplicationProperty_Description_String(50),
    EVRApplicationProperty_VRApplicationProperty_NewsURL_String(51),
    EVRApplicationProperty_VRApplicationProperty_ImagePath_String(52),
    EVRApplicationProperty_VRApplicationProperty_Source_String(53),
    EVRApplicationProperty_VRApplicationProperty_IsDashboardOverlay_Bool(60),
    EVRApplicationProperty_VRApplicationProperty_IsTemplate_Bool(61),
    EVRApplicationProperty_VRApplicationProperty_IsInstanced_Bool(62),
    EVRApplicationProperty_VRApplicationProperty_LastLaunchTime_Uint64(70)
}

enum class EVRApplicationTransitionState(val i: Int) {
    EVRApplicationTransitionState_VRApplicationTransition_None(0),
    EVRApplicationTransitionState_VRApplicationTransition_OldAppQuitSent(10),
    EVRApplicationTransitionState_VRApplicationTransition_WaitingForExternalLaunch(11),
    EVRApplicationTransitionState_VRApplicationTransition_NewAppLaunched(20)
}

enum class ChaperoneCalibrationState(val i: Int) {
    ChaperoneCalibrationState_OK(1),
    ChaperoneCalibrationState_Warning(100),
    ChaperoneCalibrationState_Warning_BaseStationMayHaveMoved(101),
    ChaperoneCalibrationState_Warning_BaseStationRemoved(102),
    ChaperoneCalibrationState_Warning_SeatedBoundsInvalid(103),
    ChaperoneCalibrationState_Error(200),
    ChaperoneCalibrationState_Error_BaseStationUninitalized(201),
    ChaperoneCalibrationState_Error_BaseStationConflict(202),
    ChaperoneCalibrationState_Error_PlayAreaInvalid(203),
    ChaperoneCalibrationState_Error_CollisionBoundsInvalid(204)
}

enum class EChaperoneConfigFile(val i: Int) {
    EChaperoneConfigFile_Live(0),
    EChaperoneConfigFile_Temp(1)
}

enum class EChaperoneImportFlags(val i: Int) {
    EChaperoneImportFlags_EChaperoneImport_BoundsOnly(1)
}

enum class EVRCompositorError(val i: Int) {
    EVRCompositorError_VRCompositorError_None(0),
    EVRCompositorError_VRCompositorError_RequestFailed(1),
    EVRCompositorError_VRCompositorError_IncompatibleVersion(100),
    EVRCompositorError_VRCompositorError_DoNotHaveFocus(101),
    EVRCompositorError_VRCompositorError_InvalidTexture(102),
    EVRCompositorError_VRCompositorError_IsNotSceneApplication(103),
    EVRCompositorError_VRCompositorError_TextureIsOnWrongDevice(104),
    EVRCompositorError_VRCompositorError_TextureUsesUnsupportedFormat(105),
    EVRCompositorError_VRCompositorError_SharedTexturesNotSupported(106),
    EVRCompositorError_VRCompositorError_IndexOutOfRange(107)
}

enum class VROverlayInputMethod(val i: Int) {
    VROverlayInputMethod_None(0),
    VROverlayInputMethod_Mouse(1)
}

enum class VROverlayTransformType(val i: Int) {
    VROverlayTransformType_VROverlayTransform_Absolute(0),
    VROverlayTransformType_VROverlayTransform_TrackedDeviceRelative(1),
    VROverlayTransformType_VROverlayTransform_SystemOverlay(2),
    VROverlayTransformType_VROverlayTransform_TrackedComponent(3)
}

enum class VROverlayFlags(val i: Int) {
    VROverlayFlags_None(0),
    VROverlayFlags_Curved(1),
    VROverlayFlags_RGSS4X(2),
    VROverlayFlags_NoDashboardTab(3),
    VROverlayFlags_AcceptsGamepadEvents(4),
    VROverlayFlags_ShowGamepadFocus(5),
    VROverlayFlags_SendVRScrollEvents(6),
    VROverlayFlags_SendVRTouchpadEvents(7),
    VROverlayFlags_ShowTouchPadScrollWheel(8),
    VROverlayFlags_TransferOwnershipToInternalProcess(9),
    VROverlayFlags_SideBySide_Parallel(10),
    VROverlayFlags_SideBySide_Crossed(11),
    VROverlayFlags_Panorama(12),
    VROverlayFlags_StereoPanorama(13),
    VROverlayFlags_SortWithNonSceneOverlays(14)
}

enum class EGamepadTextInputMode(val i: Int) {
    EGamepadTextInputMode_k_EGamepadTextInputModeNormal(0),
    EGamepadTextInputMode_k_EGamepadTextInputModePassword(1),
    EGamepadTextInputMode_k_EGamepadTextInputModeSubmit(2)
}

enum class EGamepadTextInputLineMode(val i: Int) {
    EGamepadTextInputLineMode_k_EGamepadTextInputLineModeSingleLine(0),
    EGamepadTextInputLineMode_k_EGamepadTextInputLineModeMultipleLines(1)
}

enum class EOverlayDirection(val i: Int) {
    EOverlayDirection_OverlayDirection_Up(0),
    EOverlayDirection_OverlayDirection_Down(1),
    EOverlayDirection_OverlayDirection_Left(2),
    EOverlayDirection_OverlayDirection_Right(3),
    EOverlayDirection_OverlayDirection_Count(4)
}

enum class EVRRenderModelError(val i: Int) {
    EVRRenderModelError_VRRenderModelError_None(0),
    EVRRenderModelError_VRRenderModelError_Loading(100),
    EVRRenderModelError_VRRenderModelError_NotSupported(200),
    EVRRenderModelError_VRRenderModelError_InvalidArg(300),
    EVRRenderModelError_VRRenderModelError_InvalidModel(301),
    EVRRenderModelError_VRRenderModelError_NoShapes(302),
    EVRRenderModelError_VRRenderModelError_MultipleShapes(303),
    EVRRenderModelError_VRRenderModelError_TooManyVertices(304),
    EVRRenderModelError_VRRenderModelError_MultipleTextures(305),
    EVRRenderModelError_VRRenderModelError_BufferTooSmall(306),
    EVRRenderModelError_VRRenderModelError_NotEnoughNormals(307),
    EVRRenderModelError_VRRenderModelError_NotEnoughTexCoords(308),
    EVRRenderModelError_VRRenderModelError_InvalidTexture(400)
}

enum class EVRComponentProperty(val i: Int) {
    EVRComponentProperty_VRComponentProperty_IsStatic(1),
    EVRComponentProperty_VRComponentProperty_IsVisible(2),
    EVRComponentProperty_VRComponentProperty_IsTouched(4),
    EVRComponentProperty_VRComponentProperty_IsPressed(8),
    EVRComponentProperty_VRComponentProperty_IsScrolled(16)
}

enum class EVRNotificationType(val i: Int) {
    EVRNotificationType_Transient(0),
    EVRNotificationType_Persistent(1),
    EVRNotificationType_Transient_SystemWithUserValue(2)
}

enum class EVRNotificationStyle(val i: Int) {
    EVRNotificationStyle_None(0),
    EVRNotificationStyle_Application(100),
    EVRNotificationStyle_Contact_Disabled(200),
    EVRNotificationStyle_Contact_Enabled(201),
    EVRNotificationStyle_Contact_Active(202)
}

enum class EVRSettingsError(val i: Int) {
    EVRSettingsError_VRSettingsError_None(0),
    EVRSettingsError_VRSettingsError_IPCFailed(1),
    EVRSettingsError_VRSettingsError_WriteFailed(2),
    EVRSettingsError_VRSettingsError_ReadFailed(3)
}

enum class EVRScreenshotError(val i: Int) {
    EVRScreenshotError_VRScreenshotError_None(0),
    EVRScreenshotError_VRScreenshotError_RequestFailed(1),
    EVRScreenshotError_VRScreenshotError_IncompatibleVersion(100),
    EVRScreenshotError_VRScreenshotError_NotFound(101),
    EVRScreenshotError_VRScreenshotError_BufferTooSmall(102),
    EVRScreenshotError_VRScreenshotError_ScreenshotAlreadyInProgress(108)
}

typealias TrackedDeviceIndex_t = Int
typealias VRNotificationId = Int;
typealias VROverlayHandle_t = Long;
data class Pointer(val l: Long)
typealias glSharedTextureHandle_t = Pointer;
typealias glInt_t = Int;
typealias glUInt_t = Int;
typealias TrackedCameraHandle_t = Long;
typealias ScreenshotHandle_t = Int;
typealias VRComponentProperties = Int;
typealias TextureID_t = Int;
typealias HmdError = EVRInitError;
typealias Hmd_Eye = EVREye;
typealias GraphicsAPIConvention = EGraphicsAPIConvention;
typealias ColorSpace = EColorSpace;
typealias HmdTrackingResult = ETrackingResult;
typealias TrackedDeviceClass = ETrackedDeviceClass;
typealias TrackingUniverseOrigin = ETrackingUniverseOrigin;
typealias TrackedDeviceProperty = ETrackedDeviceProperty;
typealias TrackedPropertyError = ETrackedPropertyError;
typealias VRSubmitFlags_t = EVRSubmitFlags;
typealias VRState_t = EVRState;
typealias CollisionBoundsStyle_t = ECollisionBoundsStyle;
typealias VROverlayError = EVROverlayError;
typealias VRFirmwareError = EVRFirmwareError;
typealias VRCompositorError = EVRCompositorError;
typealias VRScreenshotsError = EVRScreenshotError;

class HmdMatrix34_t() {

//    fun toRawPointer(): Long{
//
//        val buf = ByteBuffer.allocateDirect(3*4).order(ByteOrder.nativeOrder())
//
//        buf.putFloat(0, m00)
//        //other mXY
//
//        return memoryAddress(buf)
//
//    }

}

class HmdMatrix44_t
class HmdVector3_t
class HmdVector4_t
class HmdVector3d_t
class HmdVector2_t
data class HmdQuaternion_t(var w: Double, var x: Double, var y: Double, var z: Double)
data class HmdColor_t(var r: Float, var g: Float, var b: Float, var a: Float)
class HmdQuad_t
data class HmdRect2_t(var vTopLeft: HmdVector2_t, var vBottomRight: HmdVector2_t)
class DistortionCoordinates_t
data class Texture_t(var handle: Pointer, var eType: EGraphicsAPIConvention, var eColorSpace: EColorSpace)

data class TrackedDevicePose_t(var mDeviceToAbsoluteTracking: HmdMatrix34_t, var vVelocity: HmdVector3_t, var vAngularVelocity: HmdVector3_t, var eTrackingResult: ETrackingResult,
                               var bPoseIsValid: Boolean, var bDeviceIsConnected: Boolean)

data class VRTextureBounds_t(var uMin: Float, var vMin: Float, var uMax: Float, var vMax: Float)
data class VREvent_Controller_t(var button: Int)
data class VREvent_Mouse_t(var x: Float, var y: Float, var button: Int)
data class VREvent_Scroll_t(var xdelta: Float, var ydelta: Float, var repeatCount: Int)
data class VREvent_TouchPadMove_t(var bFingerDown: Boolean, var flSecondsFingerDown: Float, var fValueXFirst: Float, var fValueYFirst: Float,
                                  var fValueXRaw: Float, var fValueYRaw: Float)

data class VREvent_Notification_t(var ulUserValue: Long, var notificationId: Int)
data class VREvent_Process_t(var pid: Int, var oldPid: Int, var bForced: Boolean)
data class VREvent_Overlay_t(var overlayHandle: Long)
data class VREvent_Status_t(var statusState: Int)
data class VREvent_Keyboard_t(var cNewInput: String, var uUserValue: Long)
data class VREvent_Ipd_t(var ipdMeters: Float)
data class VREvent_Chaperone_t(var m_nPreviousUniverse: Long, var m_nCurrentUniverse: Long)
data class VREvent_Reserved_t(var reserved0: Long, var reserved1: Long)
data class VREvent_PerformanceTest_t(var m_nFidelityLevel: Int)
data class VREvent_SeatedZeroPoseReset_t(var bResetBySystemMenu: Boolean)
data class VREvent_Screenshot_t(var handle: Int, var type: Int)
data class VREvent_ScreenshotProgress_t(var progress: Float)
data class VREvent_ApplicationLaunch_t(var pid: Int, var unArgsHandle: Int)
data class HiddenAreaMesh_t(var pVertexData: Array<HmdVector2_t>, var unTriangleCount: Int)
data class VRControllerAxis_t(var x: Float, var y: Float)
data class VRControllerState_t(var unPacketNum: Int, var ulButtonPressed: Long, var ulButtonTouched: Long,
                               var rAxis: Array<VRControllerState_t>)

data class Compositor_OverlaySettings(var size: Int, var curved: Boolean, var antialias: Boolean, var scale: Float, var distance: Float,
                                      var alpha: Float, var uOffset: Float, var vOffset: Float, var uScale: Float, var vScale: Float,
                                      var gridDivs: Float, var gridWidth: Float, var gridScale: Float, var transform: HmdMatrix44_t)

data class CameraVideoStreamFrameHeader_t(var eFrameType: EVRTrackedCameraFrameType, var nWidth: Int, var nHeight: Int,
                                          var nBytesPerPixel: Int, var nFrameSequence: Int, var standingTrackedDevicePose: TrackedDevicePose_t)

data class AppOverrideKeys_t(var pchKey: String, var pchValue: String)

data class Compositor_FrameTiming(var m_nSize: Int, var m_nFrameIndex: Int, var m_nNumFramePresents: Int, var m_nNumDroppedFrames: Int,
                                  var m_nReprojectionFlags: Int, var m_flSystemTimeInSeconds: Double, var m_flPreSubmitGpuMs: Float,
                                  var m_flPostSubmitGpuMs: Float, var m_flTotalRenderGpuMs: Float, var m_flCompositorRenderGpuMs: Float,
                                  var m_flCompositorRenderCpuMs: Float, var m_flCompositorIdleCpuMs: Float,
                                  var m_flClientFrameIntervalMs: Float, var m_flPresentCallCpuMs: Float, var m_flWaitForPresentCpuMs: Float,
                                  var m_flSubmitFrameMs: Float, var m_flWaitGetPosesCalledMs: Float, var m_flNewPosesReadyMs: Float,
                                  var m_flNewFrameReadyMs: Float, var m_flCompositorUpdateStartMs: Float, var m_flCompositorUpdateEndMs: Float,
                                  var m_flCompositorRenderStartMs: Float, var m_HmdPose: TrackedDevicePose_t)

data class Compositor_CumulativeStats(var m_nPid: Int, var m_nNumFramePresents: Int, var m_nNumDroppedFrames: Int,
                                      var m_nNumReprojectedFrames: Int, var m_nNumFramePresentsOnStartup: Int,
                                      var m_nNumDroppedFramesOnStartup: Int, var m_nNumReprojectedFramesOnStartup: Int, var m_nNumLoading: Int, var m_nNumFramePresentsLoading: Int,
                                      var m_nNumDroppedFramesLoading: Int, var m_nNumReprojectedFramesLoading: Int, var m_nNumTimedOut: Int,
                                      var m_nNumFramePresentsTimedOut: Int, var m_nNumDroppedFramesTimedOut: Int,
                                      var m_nNumReprojectedFramesTimedOut: Int)

data class VROverlayIntersectionParams_t(var vSource: HmdVector3_t, var vDirection: HmdVector3_t, var eOrigin: ETrackingUniverseOrigin)

data class VROverlayIntersectionResults_t(var vPoint: HmdVector3_t, var vNormal: HmdVector3_t, var vUVs: HmdVector2_t, var fDistance: Float)

data class RenderModel_ComponentState_t(var mTrackingToComponentRenderModel: HmdMatrix34_t, var mTrackingToComponentLocal: HmdMatrix34_t,
                                        var uProperties: VRComponentProperties)

data class RenderModel_Vertex_t(var vPosition: HmdVector3_t, var vNormal: HmdVector3_t, var rfTextureCoord: FloatArray)

data class RenderModel_TextureMap_t(var unWidth: Short, var unHeight: Short, var rubTextureMapData: ByteArray)

data class RenderModel_t(var rVertexData: Array<RenderModel_t>, var unVertexCount: Int, var rIndexData: ShortArray, var unTriangleCount: Int,
                         var diffuseTextureId: TextureID_t)

data class RenderModel_ControllerMode_State_t(var bScrollWheelVisible: Boolean)

data class NotificationBitmap_t(var m_pImageData: Pointer, var m_nWidth: Int, var m_nHeight: Int, var m_nBytesPerPixel: Int)

data class COpenVRContext(var m_VRSystem: Long, var m_pVRChaperone: Long, var m_pVRChaperoneSetup: Long, var m_pVRCompositor: Long,
                          var m_pVROverlay: Long, var m_pVRResources: Long, var m_pVRRenderModels: Long, var m_pVRExtendedDisplay: Long,
                          var m_pVRSettings: Long, var m_pVRApplications: Long, var m_pVRTrackedCamera: Long, var m_pVRScreenshots: Long)