package lib

import kool.stak
import org.lwjgl.openvr.VR.VR_GetVRInitErrorAsEnglishDescription
import org.lwjgl.openvr.VR.VR_GetVRInitErrorAsSymbol
import org.lwjgl.openvr.VRSystem.*
import org.lwjgl.system.MemoryUtil.memASCII

enum class VREye(@JvmField val i: Int) {
    Left(0),
    Right(1);

    companion object {
        @JvmField
        val MAX = 2

        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** This texture is used directly by our renderer, so only perform atomic (copyresource or resolve) on it   */
enum class TextureType {
    /** Handle has been invalidated */
    Invalid,
    /** Handle is an ID3D11Texture  */
    DirectX,
    /** Handle is an OpenGL texture name or an OpenGL render buffer name, depending on submit flags */
    OpenGL,
    /** Handle is a pointer to a VRVulkanTextureData_t structure    */
    Vulkan,
    /** Handle is a macOS cross-process-sharable IOSurfaceRef, deprecated in favor of TextureType_Metal on supported platforms   */
    IOSurface,
    /** Handle is a pointer to a D3D12TextureData_t structure   */
    DirectX12,
    /** Handle is a HANDLE DXGI share handle, only supported for Overlay render targets.    */
    DXGISharedHandle,
    /** Handle is a MTLTexture conforming to the MTLSharedTexture protocol. Textures submitted to IVRCompositor::Submit which
     *  are of type MTLTextureType2DArray assume layer 0 is the left eye texture (vr::EVREye::Eye_left), layer 1 is the right
     *  eye texture (vr::EVREye::Eye_Right); */
    Metal;

    @JvmField
    val i = ordinal - 1

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class ColorSpace(@JvmField val i: Int) {
    /** Assumes 'gamma' for 8-bit per component formats, otherwise 'linear'.  This mirrors the DXGI formats which have
     *  _SRGB variants. */
    Auto(0),
    /** Texture data can be displayed directly on the display without any conversion (a.k.a. display native format).    */
    Gamma(1),
    /** Same as gamma but has been converted to a linear representation using DXGI's sRGB conversion algorithm. */
    Linear(2);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class TrackingResult(@JvmField val i: Int) {

    Uninitialized(1),

    Calibrating_InProgress(100),
    Calibrating_OutOfRange(101),

    Running_OK(200),
    Running_OutOfRange(201),

    Fallback_RotationOnly(300);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Describes what kind of object is being tracked at a given ID */
enum class TrackedDeviceClass(@JvmField val i: Int) {
    /** the ID was not valid.   */
    Invalid(0),
    /** Head-Mounted Displays   */
    HMD(1),
    /** Tracked controllers */
    Controller(2),
    /** Generic trackers, similar to controllers    */
    GenericTracker(3),
    /** Camera and base stations that serve as tracking reference points;   */
    TrackingReference(4),
    /** Accessories that aren't necessarily tracked themselves, but may redirect video output from other tracked devices    */
    DisplayRedirect(5);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Describes what specific role associated with a tracked device */
enum class TrackedControllerRole {
    /** Invalid value for controller value  */
    Invalid,
    /** Tracked device associated with the left hand    */
    LeftHand,
    /** Tracked device associated with the right hand   */
    RightHand,
    /** Tracked device is opting out of left/right hand selection   */
    OptOut,
    /** Tracked device is a treadmill */
    Treadmill;

    @JvmField
    val i = ordinal

    /** Returns true if the tracked controller role is allowed to be a hand
     *  ~IsRoleAllowedAsHand     */
    val isAllowedAsHand: Boolean
        get() = when (this) {
            Invalid, LeftHand, RightHand -> true
            else -> false
        }

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Identifies which style of tracking origin the application wants to use for the poses it is requesting */
enum class TrackingUniverseOrigin(@JvmField val i: Int) {
    /** Poses are provided relative to the seated zero pose */
    Seated(0),
    /** Poses are provided relative to the safe bounds configured by the user   */
    Standing(1),
    /** Poses are provided in the coordinate system defined by the driver. It has Y up and is unified for devices of the same driver.
     * You usually don't want this one. */
    RawAndUncalibrated(2);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Each entry in this value represents a property that can be retrieved about a tracked device.
 *  Many fields are only valid for one openvr.lib.TrackedDeviceClass. */
enum class TrackedDeviceProperty(@JvmField val i: Int) {

    Invalid(0),

    // general properties that apply to all device classes
    TrackingSystemName_String(1000),
    ModelNumber_String(1001),
    SerialNumber_String(1002),
    RenderModelName_String(1003),
    WillDriftInYaw_Bool(1004),
    ManufacturerName_String(1005),
    TrackingFirmwareVersion_String(1006),
    HardwareRevision_String(1007),
    AllWirelessDongleDescriptions_String(1008),
    ConnectedWirelessDongle_String(1009),
    DeviceIsWireless_Bool(1010),
    DeviceIsCharging_Bool(1011),
    /** 0 is empty), 1 is full  */
    DeviceBatteryPercentage_Float(1012),
    StatusDisplayTransform_Matrix34(1013),
    Firmware_UpdateAvailable_Bool(1014),
    Firmware_ManualUpdate_Bool(1015),
    Firmware_ManualUpdateURL_String(1016),
    HardwareRevision_Uint64(1017),
    FirmwareVersion_Uint64(1018),
    FPGAVersion_Uint64(1019),
    VRCVersion_Uint64(1020),
    RadioVersion_Uint64(1021),
    DongleVersion_Uint64(1022),
    BlockServerShutdown_Bool(1023),
    CanUnifyCoordinateSystemWithHmd_Bool(1024),
    ContainsProximitySensor_Bool(1025),
    DeviceProvidesBatteryStatus_Bool(1026),
    DeviceCanPowerOff_Bool(1027),
    Firmware_ProgrammingTarget_String(1028),
    DeviceClass_Int32(1029),
    HasCamera_Bool(1030),
    DriverVersion_String(1031),
    Firmware_ForceUpdateRequired_Bool(1032),
    ViveSystemButtonFixRequired_Bool(1033),
    ParentDriver_Uint64(1034),
    ResourceRoot_String(1035),
    RegisteredDeviceType_String(1036),
    /** input profile to use for this device in the input system. Will default to tracking system name if this isn't provided */
    InputProfilePath_String(1037),
    /** Used for devices that will never have a valid pose by design    */
    NeverTracked_Bool(1038),
    NumCameras_Int32(1039),
    /** FrameLayout value */
    CameraFrameLayout_Int32(1040),
    /** ECameraVideoStreamFormat value */
    CameraStreamFormat_Int32(1041),
    /** driver-relative path to additional device and global configuration settings */
    AdditionalDeviceSettingsPath_String(1042),
    /** Whether device supports being identified from vrmonitor (e.g. blink LED, vibrate haptics, etc) */
    Identifiable_Bool(1043),

    // Properties that are unique to TrackedDeviceClass_HMD
    ReportsTimeSinceVSync_Bool(2000),
    SecondsFromVsyncToPhotons_Float(2001),
    DisplayFrequency_Float(2002),
    UserIpdMeters_Float(2003),
    CurrentUniverseId_Uint64(2004),
    PreviousUniverseId_Uint64(2005),
    DisplayFirmwareVersion_Uint64(2006),
    IsOnDesktop_Bool(2007),
    DisplayMCType_Int32(2008),
    DisplayMCOffset_Float(2009),
    DisplayMCScale_Float(2010),
    EdidVendorID_Int32(2011),
    DisplayMCImageLeft_String(2012),
    DisplayMCImageRight_String(2013),
    DisplayGCBlackClamp_Float(2014),
    EdidProductID_Int32(2015),
    CameraToHeadTransform_Matrix34(2016),
    DisplayGCType_Int32(2017),
    DisplayGCOffset_Float(2018),
    DisplayGCScale_Float(2019),
    DisplayGCPrescale_Float(2020),
    DisplayGCImage_String(2021),
    LensCenterLeftU_Float(2022),
    LensCenterLeftV_Float(2023),
    LensCenterRightU_Float(2024),
    LensCenterRightV_Float(2025),
    UserHeadToEyeDepthMeters_Float(2026),
    CameraFirmwareVersion_Uint64(2027),
    CameraFirmwareDescription_String(2028),
    DisplayFPGAVersion_Uint64(2029),
    DisplayBootloaderVersion_Uint64(2030),
    DisplayHardwareVersion_Uint64(2031),
    AudioFirmwareVersion_Uint64(2032),
    CameraCompatibilityMode_Int32(2033),
    ScreenshotHorizontalFieldOfViewDegrees_Float(2034),
    ScreenshotVerticalFieldOfViewDegrees_Float(2035),
    DisplaySuppressed_Bool(2036),
    DisplayAllowNightMode_Bool(2037),
    DisplayMCImageWidth_Int32(2038),
    DisplayMCImageHeight_Int32(2039),
    DisplayMCImageNumChannels_Int32(2040),
    DisplayMCImageData_Binary(2041),
    SecondsFromPhotonsToVblank_Float(2042),
    DriverDirectModeSendsVsyncEvents_Bool(2043),
    DisplayDebugMode_Bool(2044),
    GraphicsAdapterLuid_Uint64(2045),
    DriverProvidedChaperonePath_String(2048),
    /** expected number of sensors or basestations to reserve UI space for */
    ExpectedTrackingReferenceCount_Int32(2049),
    /** expected number of tracked controllers to reserve UI space for */
    ExpectedControllerCount_Int32(2050),
    /** placeholder icon for "left" controller if not yet detected/loaded */
    NamedIconPathControllerLeftDeviceOff_String(2051),
    /** placeholder icon for "right" controller if not yet detected/loaded */
    NamedIconPathControllerRightDeviceOff_String(2052),
    /** placeholder icon for sensor/base if not yet detected/loaded */
    NamedIconPathTrackingReferenceDeviceOff_String(2053),
    DoNotApplyPrediction_Bool(2054),
    CameraToHeadTransforms_Matrix34_Array(2055),
    /** custom resolution of compositor calls to IVRSystem::ComputeDistortion   */
    DistortionMeshResolution_Int32(2056),
    DriverIsDrawingControllers_Bool(2057),
    DriverRequestsApplicationPause_Bool(2058),
    DriverRequestsReducedRendering_Bool(2059),
    MinimumIpdStepMeters_Float(2060),
    AudioBridgeFirmwareVersion_Uint64(2061),
    ImageBridgeFirmwareVersion_Uint64(2062),
    ImuToHeadTransform_Matrix34(2063),
    ImuFactoryGyroBias_Vector3(2064),
    ImuFactoryGyroScale_Vector3(2065),
    ImuFactoryAccelerometerBias_Vector3(2066),
    ImuFactoryAccelerometerScale_Vector3(2067),
    // reserved 2068
    ConfigurationIncludesLighthouse20Features_Bool(2069),
    // Driver requested mura correction properties
    DriverRequestedMuraCorrectionMode_Int32(2200),
    DriverRequestedMuraFeather_InnerLeft_Int32(2201),
    DriverRequestedMuraFeather_InnerRight_Int32(2202),
    DriverRequestedMuraFeather_InnerTop_Int32(2203),
    DriverRequestedMuraFeather_InnerBottom_Int32(2204),
    DriverRequestedMuraFeather_OuterLeft_Int32(2205),
    DriverRequestedMuraFeather_OuterRight_Int32(2206),
    DriverRequestedMuraFeather_OuterTop_Int32(2207),
    DriverRequestedMuraFeather_OuterBottom_Int32(2208),

    // Properties that are unique to TrackedDeviceClass_Controller
    AttachedDeviceId_String(3000),
    SupportedButtons_Uint64(3001),
    /** Return value is of value openvr.lib.VRControllerAxisType   */
    Axis0Type_Int32(3002),
    /** Return value is of value openvr.lib.VRControllerAxisType   */
    Axis1Type_Int32(3003),
    /** Return value is of value openvr.lib.VRControllerAxisType   */
    Axis2Type_Int32(3004),
    /** Return value is of value openvr.lib.VRControllerAxisType   */
    Axis3Type_Int32(3005),
    /** Return value is of value openvr.lib.VRControllerAxisType   */
    Axis4Type_Int32(3006),
    /** Return value is of value openvr.lib.TrackedControllerRole  */
    ControllerRoleHint_Int32(3007),

    // Properties that are unique to TrackedDeviceClass_TrackingReference
    FieldOfViewLeftDegrees_Float(4000),
    FieldOfViewRightDegrees_Float(4001),
    FieldOfViewTopDegrees_Float(4002),
    FieldOfViewBottomDegrees_Float(4003),
    TrackingRangeMinimumMeters_Float(4004),
    TrackingRangeMaximumMeters_Float(4005),
    ModeLabel_String(4006),


    // Properties that are used for user interface like icons names

    /** DEPRECATED. Value not referenced. Now expected to be part of icon path properties.  */
    IconPathName_String(5000),
    /** {driver}/icons/icon_filename - PNG for static icon, or GIF for animation, 50x32 for headsets and 32x32 for others   */
    NamedIconPathDeviceOff_String(5001),
    /** {driver}/icons/icon_filename - PNG for static icon, or GIF for animation, 50x32 for headsets and 32x32 for others   */
    NamedIconPathDeviceSearching_String(5002),
    /** {driver}/icons/icon_filename - PNG for static icon, or GIF for animation, 50x32 for headsets and 32x32 for others   */
    NamedIconPathDeviceSearchingAlert_String(5003),
    /** {driver}/icons/icon_filename - PNG for static icon, or GIF for animation, 50x32 for headsets and 32x32 for others   */
    NamedIconPathDeviceReady_String(5004),
    /** {driver}/icons/icon_filename - PNG for static icon, or GIF for animation, 50x32 for headsets and 32x32 for others    */
    NamedIconPathDeviceReadyAlert_String(5005),
    /** {driver}/icons/icon_filename - PNG for static icon, or GIF for animation, 50x32 for headsets and 32x32 for others    */
    NamedIconPathDeviceNotReady_String(5006),
    /** {driver}/icons/icon_filename - PNG for static icon, or GIF for animation, 50x32 for headsets and 32x32 for others    */
    NamedIconPathDeviceStandby_String(5007),
    /** {driver}/icons/icon_filename - PNG for static icon, or GIF for animation, 50x32 for headsets and 32x32 for others    */
    NamedIconPathDeviceAlertLow_String(5008),

    // Properties that are used by helpers, but are opaque to applications
    DisplayHiddenArea_Binary_Start(5100),
    DisplayHiddenArea_Binary_End(5150),
    ParentContainer(5151),

    // Properties that are unique to drivers
    UserConfigPath_String(6000),
    InstallPath_String(6001),
    HasDisplayComponent_Bool(6002),
    HasControllerComponent_Bool(6003),
    HasCameraComponent_Bool(6004),
    HasDriverDirectModeComponent_Bool(6005),
    HasVirtualDisplayComponent_Bool(6006),
    HasSpatialAnchorsSupport_Bool(6007),

    // Properties that are set internally based on other information provided by drivers
    ControllerType_String(7000),
    LegacyInputProfile_String(7001),
    /** Allows hand assignments to prefer some controllers over others. High numbers are selected over low numbers */
    ControllerHandSelectionPriority_Int32(7002),

    // Vendors are free to expose private debug data in this reserved region
    VendorSpecific_Reserved_Start(10000),
    VendorSpecific_Reserved_End(10999),

    TrackedDeviceProperty_Max(1000000);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Used to return errors that occur when reading properties. */
enum class TrackedPropertyError(@JvmField val i: Int) {

    Success(0),
    WrongDataType(1),
    WrongDeviceClass(2),
    BufferTooSmall(3),
    /** Driver has not set the property (and may not ever). */
    UnknownProperty(4),
    InvalidDevice(5),
    CouldNotContactServer(6),
    ValueNotProvidedByDevice(7),
    StringExceedsMaximumLength(8),
    /** The property value isn't known yet, but is expected soon. Call again later. */
    NotYetAvailable(9),
    PermissionDenied(10),
    InvalidOperation(11),
    CannotWriteToWildcards(12),
    IPCReadFailure(13);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }

    /** Returns a string that corresponds with the specified property error. The string will be the name of the error enum value for all valid error codes. */
    override fun toString(): String = stak { memASCII(nVRSystem_GetPropErrorNameFromEnum(i)) }
}

/** Allows the application to control how scene textures are used by the compositor when calling Submit. */
enum class SubmitFlags(@JvmField val i: Int) {

    /** Simple render path. App submits rendered left and right eye images with no lens distortion correction applied.  */
    Default(0x00),
    /** App submits final left and right eye images with lens distortion already applied (lens distortion makes the
     *  images appear barrel distorted with chromatic aberration correction applied). The app would have used the data
     *  returned by vr::openvr.lib.IVRSystem::ComputeDistortion() to apply the correct distortion to the rendered images
     *  before calling Submit().    */
    LensDistortionAlreadyApplied(0x01),
    /** If the texture pointer passed in is actually a renderbuffer (e.g. for MSAA in OpenGL) then set this flag.   */
    GlRenderBuffer(0x02),
    /** Do not use  */
    Reserved(0x04),
    /** Set to indicate that pTexture is a pointer to a VRTextureWithPose.
     *  This flag can be combined with Submit_TextureWithDepth to pass a VRTextureWithPoseAndDepth. */
    TextureWithPose(0x08),
    /** Set to indicate that pTexture is a pointer to a VRTextureWithDepth.
     *  This flag can be combined with Submit_TextureWithPose to pass a VRTextureWithPoseAndDepth.  */
    TextureWithDepth(0x10);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Status of the overall system or tracked objects */
enum class VRState(@JvmField val i: Int) {

    Undefined(-1),
    Off(0),
    Searching(1),
    Searching_Alert(2),
    Ready(3),
    Ready_Alert(4),
    NotReady(5),
    Standby(6),
    Ready_Alert_Low(7);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** The types of events that could be posted (and what the parameters mean for each event value) */
enum class VREventType(@JvmField val i: Int) {

    None(0),

    TrackedDeviceActivated(100),
    TrackedDeviceDeactivated(101),
    TrackedDeviceUpdated(102),
    TrackedDeviceUserInteractionStarted(103),
    TrackedDeviceUserInteractionEnded(104),
    IpdChanged(105),
    EnterStandbyMode(106),
    LeaveStandbyMode(107),
    TrackedDeviceRoleChanged(108),
    WatchdogWakeUpRequested(109),
    LensDistortionChanged(110),
    PropertyChanged(111),
    WirelessDisconnect(112),
    WirelessReconnect(113),

    /** data is controller  */
    ButtonPress(200),
    /** data is controller  */
    ButtonUnpress(201),
    /** data is controller  */
    ButtonTouch(202),
    /** data is controller  */
    ButtonUntouch(203),
    /** data is dualAnalog */
    DualAnalog_Press(250),
    /** data is dualAnalog */
    DualAnalog_Unpress(251),
    /** data is dualAnalog */
    DualAnalog_Touch(252),
    /** data is dualAnalog */
    DualAnalog_Untouch(253),
    /** data is dualAnalog */
    DualAnalog_Move(254),
    /** data is dualAnalog */
    DualAnalog_ModeSwitch1(255),
    /** data is dualAnalog */
    DualAnalog_ModeSwitch2(256),
    /** data is dualAnalog */
    DualAnalog_Cancel(257),

    /** data is mouse   */
    MouseMove(300),
    /** data is mouse   */
    MouseButtonDown(301),
    /** data is mouse   */
    MouseButtonUp(302),
    /** data is overlay */
    FocusEnter(303),
    /** data is overlay */
    FocusLeave(304),
    /** data is mouse   */
    Scroll(305),
    /** data is mouse   */
    TouchPadMove(306),
    /** data is overlay, global event  */
    OverlayFocusChanged(307),
    ReloadOverlays(308),
    /** JVM openvr custom   */
    HairTriggerMove(309),

    /** data is process DEPRECATED  */
    InputFocusCaptured(400),
    /** data is process DEPRECATED  */
    InputFocusReleased(401),
    /** data is process */
    SceneFocusLost(402),
    /** data is process */
    SceneFocusGained(403),
    /** data is process - The App actually drawing the scene changed (usually to or from the compositor)    */
    SceneApplicationChanged(404),
    /** data is process - New app got access to draw the scene  */
    SceneFocusChanged(405),
    /** data is process */
    InputFocusChanged(406),
    /** data is process */
    SceneApplicationSecondaryRenderingStarted(407),
    /** data is process */
    SceneApplicationUsingWrongGraphicsAdapter(408),
    /** data is process - The App that action binds reloaded for */
    ActionBindingReloaded(409),

    /** Sent to the scene application to request hiding render models temporarily   */
    HideRenderModels(410),
    /** Sent to the scene application to request restoring render model visibility  */
    ShowRenderModels(411),

    ConsoleOpened(420),
    ConsoleClosed(421),

    OverlayShown(500),
    OverlayHidden(501),
    DashboardActivated(502),
    DashboardDeactivated(503),
    /** Sent to the overlay manager - data is overlay   */
    DashboardThumbSelected(504),
    /** Sent to the overlay manager - data is overlay   */
    DashboardRequested(505),
    /** Send to the overlay manager */
    ResetDashboard(506),
    /** Send to the dashboard to render a toast - data is the notification ID   */
    RenderToast(507),
    /** Sent to overlays when a SetOverlayRaw or SetOverlayFromFile call finishes loading   */
    ImageLoaded(508),
    /** Sent to keyboard renderer in the dashboard to invoke it */
    ShowKeyboard(509),
    /** Sent to keyboard renderer in the dashboard to hide it   */
    HideKeyboard(510),
    /** Sent to an overlay when IVROverlay::SetFocusOverlay is called on it */
    OverlayGamepadFocusGained(511),
    /** Send to an overlay when it previously had focus and IVROverlay::SetFocusOverlay is called on something else */
    OverlayGamepadFocusLost(512),
    //    OverlaySharedTextureChanged(513), There are no longer sent
//    DashboardGuideButtonDown(514),
    DashboardGuideButtonUp(515),
    /** Screenshot button combo was pressed), Dashboard should request a screenshot */
    ScreenshotTriggered(516),
    /** Sent to overlays when a SetOverlayRaw or SetOverlayfromFail fails to load   */
    ImageFailed(517),
    DashboardOverlayCreated(518),
    SwitchGamepadFocus(519),

    // Screenshot API
    /** Sent by vrclient application to compositor to take a screenshot */
    RequestScreenshot(520),
    /** Sent by compositor to the application that the screenshot has been taken    */
    ScreenshotTaken(521),
    /** Sent by compositor to the application that the screenshot failed to be taken    */
    ScreenshotFailed(522),
    /** Sent by compositor to the dashboard that a completed screenshot was submitted   */
    SubmitScreenshotToDashboard(523),
    /** Sent by compositor to the dashboard that a completed screenshot was submitted   */
    ScreenshotProgressToDashboard(524),

    PrimaryDashboardDeviceChanged(525),
    /** Sent by compositor whenever room-view is enabled    */
    RoomViewShown(526),
    /** Sent by compositor whenever room-view is disabled   */
    RoomViewHidden(527),
    /** data is showUi */
    ShowUI(528),

    Notification_Shown(600),
    Notification_Hidden(601),
    Notification_BeginInteraction(602),
    Notification_Destroyed(603),

    /** data is process */
    Quit(700),
    /** data is process */
    ProcessQuit(701),
    /** data is process */
    QuitAborted_UserPrompt(702),
    /** data is process */
    QuitAcknowledged(703),
    /** The driver has requested that SteamVR shut down */
    DriverRequestedQuit(704),

    /**  Sent when the process needs to call VRChaperone()->ReloadInfo() */
    ChaperoneDataHasChanged(800),
    ChaperoneUniverseHasChanged(801),
    ChaperoneTempDataHasChanged(802),
    ChaperoneSettingsHaveChanged(803),
    SeatedZeroPoseReset(804),
    /** Sent when the process needs to reload any cached data it retrieved from VRChaperone()   */
    ChaperoneFlushCache(805),

    AudioSettingsHaveChanged(820),

    BackgroundSettingHasChanged(850),
    CameraSettingsHaveChanged(851),
    ReprojectionSettingHasChanged(852),
    ModelSkinSettingsHaveChanged(853),
    EnvironmentSettingsHaveChanged(854),
    PowerSettingsHaveChanged(855),
    EnableHomeAppSettingsHaveChanged(856),
    SteamVRSectionSettingChanged(857),
    LighthouseSectionSettingChanged(858),
    NullSectionSettingChanged(859),
    UserInterfaceSectionSettingChanged(860),
    NotificationsSectionSettingChanged(861),
    KeyboardSectionSettingChanged(862),
    PerfSectionSettingChanged(863),
    DashboardSectionSettingChanged(864),
    WebInterfaceSectionSettingChanged(865),
    TrackersSectionSettingChanged(866),
    LastKnownSectionSettingChanged(867),

    StatusUpdate(900),

    WebInterface_InstallDriverCompleted(950),

    MCImageUpdated(1000),

    FirmwareUpdateStarted(1100),
    FirmwareUpdateFinished(1101),

    KeyboardClosed(1200),
    KeyboardCharInput(1201),
    /** Sent when DONE button clicked on keyboard   */
    KeyboardDone(1202),

    ApplicationTransitionStarted(1300),
    ApplicationTransitionAborted(1301),
    ApplicationTransitionNewAppStarted(1302),
    ApplicationListUpdated(1303),
    ApplicationMimeTypeLoad(1304),
    ApplicationTransitionNewAppLaunchComplete(1305),
    ProcessConnected(1306),
    ProcessDisconnected(1307),

    Compositor_MirrorWindowShown(1400),
    Compositor_MirrorWindowHidden(1401),
    Compositor_ChaperoneBoundsShown(1410),
    Compositor_ChaperoneBoundsHidden(1411),

    TrackedCamera_StartVideoStream(1500),
    TrackedCamera_StopVideoStream(1501),
    TrackedCamera_PauseVideoStream(1502),
    TrackedCamera_ResumeVideoStream(1503),
    TrackedCamera_EditingSurface(1550),

    PerformanceTest_EnableCapture(1600),
    PerformanceTest_DisableCapture(1601),
    PerformanceTest_FidelityLevel(1602),

    MessageOverlay_Closed(1650),
    MessageOverlayCloseRequested(1651),
    /** data is hapticVibration */
    Input_HapticVibration(1700),
    /** data is inputBinding */
    BindingLoadFailed(1701),
    /** data is inputBinding */
    BindingLoadSuccessful(1702),
    /** no data */
    Input_ActionManifestReloaded(1703),
    /** data is actionManifest */
    Input_ActionManifestLoadFailed(1704),
    /** data is progressUpdate */
    ProgressUpdate(1705),
    Input_TrackerActivated(1706),
    /** data is spatialAnchor. broadcast */
    SpatialAnchors_PoseUpdated(1800),
    /** data is spatialAnchor. broadcast */
    SpatialAnchors_DescriptorUpdated(1801),
    /** data is spatialAnchor. sent to specific driver */
    SpatialAnchors_RequestPoseUpdate(1802),
    /** data is spatialAnchor. sent to specific driver */
    SpatialAnchors_RequestDescriptorUpdate(1803),

    // Vendors are free to expose private events in this reserved region
    VendorSpecific_Reserved_Start(10000),
    VendorSpecific_Reserved_End(19999);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }

    /** Returns the name of an {@code EVREvent} enum value. */
    override fun toString(): String = stak { memASCII(nVRSystem_GetEventTypeNameFromEnum(i)) }
}

/** Level of Hmd activity
 *  UserInteraction_Timeout means the device is in the process of timing out.
 *  InUse = ( k_EDeviceActivityLevel_UserInteraction || k_EDeviceActivityLevel_UserInteraction_Timeout )
 *  VREvent_TrackedDeviceUserInteractionStarted fires when the devices transitions from Standby -> UserInteraction or
 *  Idle -> UserInteraction.
 *  VREvent_TrackedDeviceUserInteractionEnded fires when the devices transitions from UserInteraction_Timeout -> Idle   */
enum class DeviceActivityLevel(@JvmField val i: Int) {

    Unknown(-1),
    /** No activity for the last 10 seconds */
    Idle(0),
    /** Activity (movement or prox sensor) is happening now */
    UserInteraction(1),
    /** No activity for the last 0.5 seconds    */
    UserInteraction_Timeout(2),
    /** Idle for at least 5 seconds (configurable in Settings -> Power Management)  */
    Standby(3);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** VR controller button and axis IDs */
enum class VRButtonId(@JvmField val i: Int) {

    System(0),
    ApplicationMenu(1),
    Grip(2),
    DPad_Left(3),
    DPad_Up(4),
    DPad_Right(5),
    DPad_Down(6),
    A(7),

    ProximitySensor(31),

    Axis0(32),
    Axis1(33),
    Axis2(34),
    Axis3(35),
    Axis4(36),

    // aliases for well known controllers
    SteamVR_Touchpad(Axis0.i),
    SteamVR_Trigger(Axis1.i),

    Dashboard_Back(Grip.i),

    Knuckles_A(Grip.i),
    Knuckles_B(ApplicationMenu.i),
    Knuckles_JoyStick(Axis3.i),

    Max(64);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }

    val mask get() = 1L shl i

    /** Returns the name of an {@code EVREvent} enum value. */
    @Deprecated("This function is deprecated in favor of the new IVRInput system.")
    override fun toString(): String = stak { memASCII(nVRSystem_GetButtonIdNameFromEnum(i)) }
}

/** used for simulated mouse events in overlay space */
enum class VRMouseButton(@JvmField val i: Int) {

    Left(0x0001),
    Right(0x0002),
    Middle(0x0004);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class DualAnalogWhich {
    Left, Right;

    val i = ordinal

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class ShowUiType { ControllerBinding, ManageTrackers, QuickStart;

    @JvmField
    val i = ordinal

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class HiddenAreaMeshType(@JvmField val i: Int) {

    Standard(0),
    Inverse(1),
    LineLoop(2),
    Max(3);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Identifies what kind of axis is on the controller at index n. Read this value with pVRSystem->Get( nControllerDeviceIndex, Prop_Axis0Type_Int32 + n );   */
enum class VRControllerAxisType(@JvmField val i: Int) {

    None(0),
    TrackPad(1),
    Joystick(2),
    /** Analog trigger data is in the X axis    */
    Trigger(3);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }

    /** Returns the name of an {@code EVREvent} enum value. */
    @Deprecated("This function is deprecated in favor of the new IVRInput system.")
    override fun toString(): String = stak { memASCII(nVRSystem_GetControllerAxisTypeNameFromEnum(i)) }
}

/** determines how to provide output to the application of various event processing functions. */
enum class VRControllerEventOutputType(@JvmField val i: Int) {

    OSEvents(0),
    VREvents(1);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Collision Bounds Style */
enum class CollisionBoundsStyle(@JvmField val i: Int) {

    BEGINNER(0),
    INTERMEDIATE(1),
    SQUARES(2),
    ADVANCED(3),
    NONE(4),

    COUNT(5);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** value values to pass in to openvr.VR_Init to identify whether the application will draw a 3D scene.     */
enum class VRApplication(@JvmField val i: Int) {
    /** Some other kind of application that isn't covered by the other entries  */
    Other(0),
    /** Application will submit 3D frames   */
    Scene(1),
    /** Application only interacts with overlays    */
    Overlay(2),
    /** Application should not start SteamVR if it's not already running), and should not keep it running if everything
     *  else quits. */
    Background(3),
    /** Init should not try to load any drivers. The application needs access to utility interfaces
     *  (like openvr.lib.IVRSettings and openvr.lib.IVRApplications) but not hardware.  */
    Utility(4),
    /** Reserved for vrmonitor  */
    VRMonitor(5),
    /** Reserved for Steam  */
    SteamWatchdog(6),
    /** reserved for vrstartup    */
    Bootstrapper(7),
    /** reserved for vrwebhelper */
    WebHelper(8),

    Max(7);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** error codes for firmware */
enum class VRFirmwareError(@JvmField val i: Int) {

    None(0),
    Success(1),
    Fail(2);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}


enum class VRSkeletalMotionRange {
    /** The range of motion of the skeleton takes into account any physical limits imposed by
     *  the controller itself.  This will tend to be the most accurate pose compared to the user's
     *  actual hand pose, but might not allow a closed fist for example */
    WithController,
    /** Retarget the range of motion provided by the input device to make the hand appear to move
     *  as if it was not holding a controller.  eg: map "hand grasping controller" to "closed fist" */
    WithoutController;

    @JvmField
    val i = ordinal
}

/** error codes returned by Vr_Init */
// Please add adequate error description to https://developer.valvesoftware.com/w/index.php?title=Category:SteamVRHelp
enum class VRInitError(@JvmField val i: Int) {

    None(0),
    Unknown(1),

    Init_InstallationNotFound(100),
    Init_InstallationCorrupt(101),
    Init_VRClientDLLNotFound(102),
    Init_FileNotFound(103),
    Init_FactoryNotFound(104),
    Init_InterfaceNotFound(105),
    Init_InvalidInterface(106),
    Init_UserConfigDirectoryInvalid(107),
    Init_HmdNotFound(108),
    Init_NotInitialized(109),
    Init_PathRegistryNotFound(110),
    Init_NoConfigPath(111),
    Init_NoLogPath(112),
    Init_PathRegistryNotWritable(113),
    Init_AppInfoInitFailed(114),
    /** Used internally to cause retries to vrserver    */
    Init_Retry(115),
    /** The calling application should silently exit. The user canceled app startup */
    Init_InitCanceledByUser(116),
    Init_AnotherAppLaunching(117),
    Init_SettingsInitFailed(118),
    Init_ShuttingDown(119),
    Init_TooManyObjects(120),
    Init_NoServerForBackgroundApp(121),
    Init_NotSupportedWithCompositor(122),
    Init_NotAvailableToUtilityApps(123),
    Init_Internal(124),
    Init_HmdDriverIdIsNone(125),
    Init_HmdNotFoundPresenceFailed(126),
    Init_VRMonitorNotFound(127),
    Init_VRMonitorStartupFailed(128),
    Init_LowPowerWatchdogNotSupported(129),
    Init_InvalidApplicationType(130),
    Init_NotAvailableToWatchdogApps(131),
    Init_WatchdogDisabledInSettings(132),
    Init_VRDashboardNotFound(133),
    Init_VRDashboardStartupFailed(134),
    Init_VRHomeNotFound(135),
    Init_VRHomeStartupFailed(136),
    Init_RebootingBusy(137),
    Init_FirmwareUpdateBusy(138),
    Init_FirmwareRecoveryBusy(139),
    Init_USBServiceBusy(140),
    VRWebHelperStartupFailed(141),
    TrackerManagerInitFailed(142),

    Driver_Failed(200),
    Driver_Unknown(201),
    Driver_HmdUnknown(202),
    Driver_NotLoaded(203),
    Driver_RuntimeOutOfDate(204),
    Driver_HmdInUse(205),
    Driver_NotCalibrated(206),
    Driver_CalibrationInvalid(207),
    Driver_HmdDisplayNotFound(208),
    Driver_TrackedDeviceInterfaceUnknown(209),
    // Driver_HmdDisplayNotFoundAfterFix(210), // not needed: here for historic reasons
    Driver_HmdDriverIdOutOfBounds(211),
    Driver_HmdDisplayMirrored(212),

    IPC_ServerInitFailed(300),
    IPC_ConnectFailed(301),
    IPC_SharedStateInitFailed(302),
    IPC_CompositorInitFailed(303),
    IPC_MutexInitFailed(304),
    IPC_Failed(305),
    IPC_CompositorConnectFailed(306),
    IPC_CompositorInvalidConnectResponse(307),
    IPC_ConnectFailedAfterMultipleAttempts(308),

    Compositor_Failed(400),
    Compositor_D3D11HardwareRequired(401),
    Compositor_FirmwareRequiresUpdate(402),
    Compositor_OverlayInitFailed(403),
    Compositor_ScreenshotsInitFailed(404),
    Compositor_UnableToCreateDevice(405),

    VendorSpecific_UnableToConnectToOculusRuntime(1000),
    VendorSpecific_WindowsNotInDevMode(1001),

    VendorSpecific_HmdFound_CantOpenDevice(1101),
    VendorSpecific_HmdFound_UnableToRequestConfigStart(1102),
    VendorSpecific_HmdFound_NoStoredConfig(1103),
    VendorSpecific_HmdFound_ConfigTooBig(1104),
    VendorSpecific_HmdFound_ConfigTooSmall(1105),
    VendorSpecific_HmdFound_UnableToInitZLib(1106),
    VendorSpecific_HmdFound_CantReadFirmwareVersion(1107),
    VendorSpecific_HmdFound_UnableToSendUserDataStart(1108),
    VendorSpecific_HmdFound_UnableToGetUserDataStart(1109),
    VendorSpecific_HmdFound_UnableToGetUserDataNext(1110),
    VendorSpecific_HmdFound_UserDataAddressRange(1111),
    VendorSpecific_HmdFound_UserDataError(1112),
    VendorSpecific_HmdFound_ConfigFailedSanityCheck(1113),

    Steam_SteamInstallationNotFound(2000);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }

    /** Returns the name of the enum value for an EVRInitError. This function may be called outside of VR_Init()/VR_Shutdown().
     *  Also VRInitError::asSymbol */
    val asSymbol: String
        get() = VR_GetVRInitErrorAsSymbol(i)!!

    /** Returns an English string for an EVRInitError. Applications should call VR_GetVRInitErrorAsSymbol instead and
     * use that as a key to look up their own localized error message. This function may be called outside of VR_Init()/VR_Shutdown(). */
    val asEnglishDescription: String
        get() = VR_GetVRInitErrorAsEnglishDescription(i)!!
}

enum class VSync {
    None,
    /** block following render work until vsync */
    WaitRender,
    /** do not block following render work (allow to get started early) */
    NoWaitRender
}

enum class VRMuraCorrectionMode { Default, NoCorrection;

    @JvmField
    val i = ordinal
}

/** raw IMU data provided by IVRIOBuffer from paths to tracked devices with IMUs */
enum class ImuOffScaleFlags(val i: Int) {
    AccelX(0x01),
    AccelY(0x02),
    AccelZ(0x04),
    GyroX(0x08),
    GyroY(0x10),
    GyroZ(0x20)
}

// ivrsystem.h

// ivrapplications.h -> class

// ivrsettings.h -> class

// ivrchaperone.h -> class

// ivrchaperonesetup.h -> class

// ivrcompositor.h -> class

// ivrnotifications.h -> class

// ivroverlay.h -> class

/** Input modes for the Big Picture gamepad text entry */
enum class GamepadTextInputMode(@JvmField val i: Int) {

    Normal(0),
    Password(1),
    Submit(2);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Controls number of allowed lines for the Big Picture gamepad text entry */
enum class GamepadTextInputLineMode(@JvmField val i: Int) {

    SingleLine(0),
    MultipleLines(1);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Directions for changing focus between overlays with the gamepad */
enum class OverlayDirection(@JvmField val i: Int) {

    Up(0),
    Down(1),
    Left(2),
    Right(3),

    Count(4);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class VROverlayIntersectionMaskPrimitiveType(@JvmField val i: Int) {

    Rectangle(0),
    Circle(1);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

// ivrrendermodels.h -> class

enum class VRComponentProperty(@JvmField val i: Int) {

    IsStatic(1 shl 0),
    IsVisible(1 shl 1),
    IsTouched(1 shl 2),
    IsPressed(1 shl 3),
    IsScrolled(1 shl 4);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

// ivrextendeddisplay.h

// ivrtrackedcamera.h

// ivrscreenshots.h -> class

// ivrdrivermanager.h

// ivrinput.h -> class

// ivriobuffer.h -> class