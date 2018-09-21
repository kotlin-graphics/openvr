package vr_

import org.lwjgl.openvr.VR

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
    /** Handle is an ID3D11Texture  */
    DirectX,
    /** Handle is an OpenGL texture name or an OpenGL render buffer name, depending on submit flags */
    OpenGL,
    /** Handle is a pointer to a VRVulkanTextureData_t structure    */
    Vulkan,
    /** Handle is a macOS cross-process-sharable IOSurfaceRef   */
    IOSurface,
    /** Handle is a pointer to a D3D12TextureData_t structure   */
    DirectX12,
    /** Handle is a HANDLE DXGI share handle, only supported for Overlay render targets.    */
    DXGISharedHandle;

    @JvmField
    val i = ordinal

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
    Running_OutOfRange(201);

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
    OptOut;

    @JvmField
    val i = ordinal

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
    /** EVRTrackedCameraFrameLayout value */
    CameraFrameLayout_Int32(1040),

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

    // Properties that are unique to TrackedDeviceClass_Controller
    AttachedDeviceId_String(3000),
    SupportedButtons_Uint64(3001),
    /** Return value is of value openvr.lib.EVRControllerAxisType   */
    Axis0Type_Int32(3002),
    /** Return value is of value openvr.lib.EVRControllerAxisType   */
    Axis1Type_Int32(3003),
    /** Return value is of value openvr.lib.EVRControllerAxisType   */
    Axis2Type_Int32(3004),
    /** Return value is of value openvr.lib.EVRControllerAxisType   */
    Axis3Type_Int32(3005),
    /** Return value is of value openvr.lib.EVRControllerAxisType   */
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

    // Properties that are set internally based on other information provided by drivers
    ControllerType_String(7000),
    LegacyInputProfile_String(7001),

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
    CannotWriteToWildcards(12);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
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
enum class EVRState(@JvmField val i: Int) {

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
enum class EVREventType(@JvmField val i: Int) {

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
    /** JVM openvr custom   */
    HairTriggerMove(308),

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

    ChaperoneDataHasChanged(800),
    ChaperoneUniverseHasChanged(801),
    ChaperoneTempDataHasChanged(802),
    ChaperoneSettingsHaveChanged(803),
    SeatedZeroPoseReset(804),

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
    /** data is process */
    BindingLoadFailed(1701),
    /** data is process */
    BindingLoadSuccessful(1702),

    // Vendors are free to expose private events in this reserved region
    VendorSpecific_Reserved_Start(10000),
    VendorSpecific_Reserved_End(19999);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Level of Hmd activity
 *  UserInteraction_Timeout means the device is in the process of timing out.
 *  InUse = ( k_EDeviceActivityLevel_UserInteraction || k_EDeviceActivityLevel_UserInteraction_Timeout )
 *  VREvent_TrackedDeviceUserInteractionStarted fires when the devices transitions from Standby -> UserInteraction or
 *  Idle -> UserInteraction.
 *  VREvent_TrackedDeviceUserInteractionEnded fires when the devices transitions from UserInteraction_Timeout -> Idle   */
enum class EDeviceActivityLevel(@JvmField val i: Int) {

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
enum class EVRButtonId(@JvmField val i: Int) {

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

    Max(64);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }

    val mask get() = 1L shl i
}

/** used for simulated mouse events in overlay space */
enum class EVRMouseButton(@JvmField val i: Int) {

    Left(0x0001),
    Right(0x0002),
    Middle(0x0004);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EDualAnalogWhich {
    Left, Right;

    val i = ordinal

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EVRInputError {
    None,
    NameNotFound,
    WrongType,
    InvalidHandle,
    InvalidParam,
    NoSteam,
    MaxCapacityReached,
    IPCError,
    NoActiveActionSet,
    InvalidDevice,
    InvalidSkeleton,
    InvalidBoneCount,
    InvalidCompressedData,
    NoData,
    BufferTooSmall,
    MismatchedActionManifest;

    val i = ordinal

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EHiddenAreaMeshType(@JvmField val i: Int) {

    Standard(0),
    Inverse(1),
    LineLoop(2),
    Max(3);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Identifies what kind of axis is on the controller at index n. Read this value with pVRSystem->Get( nControllerDeviceIndex, Prop_Axis0Type_Int32 + n );   */
enum class EVRControllerAxisType(@JvmField val i: Int) {

    None(0),
    TrackPad(1),
    Joystick(2),
    /** Analog trigger data is in the X axis    */
    Trigger(3);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** determines how to provide output to the application of various event processing functions. */
enum class EVRControllerEventOutputType(@JvmField val i: Int) {

    OSEvents(0),
    VREvents(1);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Collision Bounds Style */
enum class ECollisionBoundsStyle(@JvmField val i: Int) {

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

/** Errors that can occur around VR overlays */
enum class EVROverlayError(@JvmField val i: Int) {

    None(0),

    UnknownOverlay(10),
    InvalidHandle(11),
    PermissionDenied(12),
    /** No more overlays could be created because the maximum number already exist  */
    OverlayLimitExceeded(13),
    WrongVisibilityType(14),
    KeyTooLong(15),
    NameTooLong(16),
    KeyInUse(17),
    WrongTransformType(18),
    InvalidTrackedDevice(19),
    InvalidParameter(20),
    ThumbnailCantBeDestroyed(21),
    ArrayTooSmall(22),
    RequestFailed(23),
    InvalidTexture(24),
    UnableToLoadFile(25),
    KeyboardAlreadyInUse(26),
    NoNeighbor(27),
    TooManyMaskPrimitives(29),
    BadMaskPrimitive(30),
    TextureAlreadyLocked(31),
    TextureLockCapacityReached(32),
    TextureNotLocked(33);

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
    /** Start up SteamVR    */
    Bootstrapper(7),

    Max(7);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** error codes for firmware */
enum class EVRFirmwareError(@JvmField val i: Int) {

    None(0),
    Success(1),
    Fail(2);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** error codes for notifications */
enum class EVRNotificationError(@JvmField val i: Int) {

    OK(0),
    InvalidNotificationId(100),
    NotificationQueueFull(101),
    InvalidOverlayHandle(102),
    SystemWithUserValueAlreadyExists(103);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
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
}

enum class EVRScreenshotType(@JvmField val i: Int) {

    None(0),
    /** left eye only   */
    Mono(1),
    Stereo(2),
    Cubemap(3),
    MonoPanorama(4),
    StereoPanorama(5);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EVRScreenshotPropertyFilenames(@JvmField val i: Int) {

    Preview(0),
    VR(1);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EVRTrackedCameraError(@JvmField val i: Int) {

    None(0),
    OperationFailed(100),
    InvalidHandle(101),
    InvalidFrameHeaderVersion(102),
    OutOfHandles(103),
    IPCFailure(104),
    NotSupportedForThisDevice(105),
    SharedMemoryFailure(106),
    FrameBufferingFailure(107),
    StreamSetupFailure(108),
    InvalidGLTextureId(109),
    InvalidSharedTextureHandle(110),
    FailedToGetGLTextureId(111),
    SharedTextureFailure(112),
    NoFrameAvailable(113),
    InvalidArgument(114),
    InvalidFrameBufferSize(115);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EVRTrackedCameraFrameLayout(@JvmField val i: Int) {
    Mono(0x0001),
    Stereo(0x0002),
    /** Stereo frames are Top/Bottom (left/right) */
    VerticalLayout(0x0010),
    /** Stereo frames are Left/Right */
    HorizontalLayout(0x0020), ;
}

enum class EVRTrackedCameraFrameType(@JvmField val i: Int) {
    /** This is the camera video frame size in pixels), still distorted.    */
    Distorted(0),
    /** In pixels), an undistorted inscribed rectangle region without invalid regions. This size is subject to changes
     *  shortly. */
    Undistorted(1),
    /** In pixels), maximum undistorted with invalid regions. Non zero alpha component identifies valid regions.    */
    MaximumUndistorted(2),
    MAX(3);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EVSync {
    None,
    /** block following render work until vsync */
    WaitRender,
    /** do not block following render work (allow to get started early) */
    NoWaitRender
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

// ivrapplications.h

/** Used for all errors reported by the openvr.lib.IVRApplications interface */
enum class EVRApplicationError(@JvmField val i: Int) {

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
}

/** these are the properties available on applications. */
enum class EVRApplicationProperty(@JvmField val i: Int) {

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
}

/** These are states the scene application startup process will go through. */
enum class EVRApplicationTransitionState(@JvmField val i: Int) {

    None(0),

    OldAppQuitSent(10),
    WaitingForExternalLaunch(11),

    NewAppLaunched(20);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

// ivrsettings.h

enum class EVRSettingsError(@JvmField val i: Int) {

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
}

// ivrchaperone.h =================================================================================================================================================

enum class ChaperoneCalibrationState(@JvmField val i: Int) {

    // OK!
    /**  Chaperone is fully calibrated and working correctly     */
    OK(1),

    // Warnings
    Warning(100),
    /** A base station thinks that it might have moved  */
    Warning_BaseStationMayHaveMoved(101),
    /** There are less base stations than when calibrated   */
    Warning_BaseStationRemoved(102),
    /** Seated bounds haven't been calibrated for the current tracking center   */
    Warning_SeatedBoundsInvalid(103),

    // Errors
    /** The UniverseID is invalid   */
    Error(200),
    /** Tracking center hasn't be calibrated for at least one of the base stations  */
    Error_BaseStationUninitialized(201),
    /** Tracking center is calibrated), but base stations disagree on the tracking space    */
    Error_BaseStationConflict(202),
    /** Play Area hasn't been calibrated for the current tracking center    */
    Error_PlayAreaInvalid(203),
    /** Collision Bounds haven't been calibrated for the current tracking center    */
    Error_CollisionBoundsInvalid(204);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

// ivrchaperonesetup.h

enum class EChaperoneConfigFile(@JvmField val i: Int) {
    /** The live chaperone config, used by most applications and games  */
    Live(1),
    /** The temporary chaperone config, used to live-preview collision bounds in room setup */
    Temp(2);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EChaperoneImportFlags(@JvmField val i: Int) {

    BoundsOnly(0x0001);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

// ivrcompositor.h ================================================================================================================================================

/** Errors that can occur with the VR compositor */
enum class EVRCompositorError(@JvmField val i: Int) {

    None(0),
    RequestFailed(1),
    IncompatibleVersion(100),
    DoNotHaveFocus(101),
    InvalidTexture(102),
    IsNotSceneApplication(103),
    TextureIsOnWrongDevice(104),
    TextureUsesUnsupportedFormat(105),
    SharedTexturesNotSupported(106),
    IndexOutOfRange(107),
    AlreadySubmitted(108),
    InvalidBounds(109);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Timing mode passed to SetExplicitTimingMode(); see that function for documentation */
enum class EVRCompositorTimingMode {
    Implicit, RuntimePerformsPostPresentHandoff, ApplicationPerformsPostPresentHandoff;

    val i = ordinal

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

// ivrnotifications.h

/** Be aware that the notification value is used as 'priority' to pick the next notification */
enum class EVRNotificationType(@JvmField val i: Int) {

    /** Transient notifications are automatically hidden after a period of time set by the user.
     *  They are used for things like information and chat messages that do not require user interaction. */
    Transient(0),

    /** Persistent notifications are shown to the user until they are hidden by calling RemoveNotification().
     *  They are used for things like phone calls and alarms that require user interaction. */
    Persistent(1),

    /** System notifications are shown no matter what. It is expected), that the ::userValue is used as ID.
     *  If there is already a system notification in the queue with that ID it is not accepted into the queue to
     *  prevent spamming with system notification */
    Transient_SystemWithUserValue(2);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EVRNotificationStyle(@JvmField val i: Int) {

    /** Creates a notification with minimal external styling. */
    None(0),

    /** Used for notifications about overlay-level status. In Steam this is used for events like downloads completing. */
    Application(100),

    /** Used for notifications about contacts that are unknown or not available. In Steam this is used for friend
     * invitations and offline friends. */
    Contact_Disabled(200),

    /** Used for notifications about contacts that are available but inactive. In Steam this is used for friends that
     * are online but not playing a game. */
    Contact_Enabled(201),

    /** Used for notifications about contacts that are available and active. In Steam this is used for friends that
     * are online and currently running a game. */
    Contact_Active(202);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

// ivroverlay.h

/** Types of input supported by VR Overlays */
enum class VROverlayInputMethod {
    /** No input events will be generated automatically for this overlay    */
    None,
    /** Tracked controllers will get mouse events automatically */
    Mouse,
    /** Analog inputs from tracked controllers are turned into DualAnalog events */
    DualAnalog;

    val i = ordinal

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Allows the caller to figure out which overlay transform getter to call. */
enum class VROverlayTransformType(@JvmField val i: Int) {

    Absolute(0),
    TrackedDeviceRelative(1),
    SystemOverlay(2),
    TrackedComponent(3);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Overlay control settings */
enum class VROverlayFlags(@JvmField val i: Int) {

    None(0),

    /** The following only take effect when rendered using the high quality render path (see SetHighQualityOverlay).    */
    Curved(1),
    RGSS4X(2),

    /** Set this flag on a dashboard overlay to prevent a tab from showing up for that overlay  */
    NoDashboardTab(3),

    /** Set this flag on a dashboard that is able to deal with gamepad focus events */
    AcceptsGamepadEvents(4),

    /** Indicates that the overlay should dim/brighten to show gamepad focus    */
    ShowGamepadFocus(5),

    /** When in VROverlayInputMethod_Mouse you can optionally enable sending VRScroll */
    SendVRScrollEvents(6),
    SendVRTouchpadEvents(7),

    /** If set this will render a vertical scroll wheel on the primary controller), only needed if not using
     *  SendVRScrollEvents but you still want to represent a scroll wheel   */
    ShowTouchPadScrollWheel(8),

    /** If this is set ownership and render access to the overlay are transferred to the new scene process on a call
     *  to openvr.lib.IVRApplications::LaunchInternalProcess    */
    TransferOwnershipToInternalProcess(9),

    // If set), renders 50% of the texture in each eye), side by side
    /** Texture is left/right   */
    SideBySide_Parallel(10),
    /** Texture is crossed and right/left   */
    SideBySide_Crossed(11),
    /** Texture is a panorama   */
    Panorama(12),
    /** Texture is a stereo panorama    */
    StereoPanorama(13),

    /** If this is set on an overlay owned by the scene application that overlay will be sorted with the "Other"
     *  overlays on top of all other scene overlays */
    SortWithNonSceneOverlays(14),

    /** If set, the overlay will be shown in the dashboard, otherwise it will be hidden.    */
    VisibleInDashboard(15);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class VRMessageOverlayResponse(@JvmField val i: Int) {

    ButtonPress_0(0),
    ButtonPress_1(1),
    ButtonPress_2(2),
    ButtonPress_3(3),
    CouldntFindSystemOverlay(4),
    CouldntFindOrCreateClientOverlay(5),
    ApplicationQuit(6);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Input modes for the Big Picture gamepad text entry */
enum class EGamepadTextInputMode(@JvmField val i: Int) {

    Normal(0),
    Password(1),
    Submit(2);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Controls number of allowed lines for the Big Picture gamepad text entry */
enum class EGamepadTextInputLineMode(@JvmField val i: Int) {

    SingleLine(0),
    MultipleLines(1);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

/** Directions for changing focus between overlays with the gamepad */
enum class EOverlayDirection(@JvmField val i: Int) {

    Up(0),
    Down(1),
    Left(2),
    Right(3),

    Count(4);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EVROverlayIntersectionMaskPrimitiveType(@JvmField val i: Int) {

    Rectangle(0),
    Circle(1);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

// ivrrendermodels.h

/** Errors that can occur with the VR compositor */
enum class EVRRenderModelError(@JvmField val i: Int) {

    None(0),
    Loading(100),
    NotSupported(200),
    InvalidArg(300),
    InvalidModel(301),
    NoShapes(302),
    MultipleShapes(303),
    TooManyVertices(304),
    MultipleTextures(305),
    BufferTooSmall(306),
    NotEnoughNormals(307),
    NotEnoughTexCoords(308),

    InvalidTexture(400);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }

    /** Note: can't use `val name` because you can't overwrite the Enum::name   */
    fun getName() = vrRenderModels.getRenderModelErrorNameFromEnum(this)
}

enum class EVRComponentProperty(@JvmField val i: Int) {

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

// ivrscreenshots.h

/** Errors that can occur with the VR compositor */
enum class EVRScreenshotError(@JvmField val i: Int) {

    None(0),
    RequestFailed(1),
    IncompatibleVersion(100),
    NotFound(101),
    BufferTooSmall(102),
    ScreenshotAlreadyInProgress(108);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

// ivrdrivermanager.h

// ivrinput.h

enum class EVRSkeletalTransformSpace { Action, Parent, Additive;

    val i = ordinal

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EVRInputFilterCancelType { Timers, Momentum;

    val i = ordinal

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}


// ivriobuffer.h

enum class EIOBufferError(@JvmField val i: Int) {
    Success(0),
    OperationFailed(100),
    InvalidHandle(101),
    InvalidArgument(102),
    PathExists(103),
    PathDoesNotExist(104),
    Permission(105);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EIOBufferMode(@JvmField val i: Int) {
    Read(0x0001),
    Write(0x0002),
    Create(0x0200);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}