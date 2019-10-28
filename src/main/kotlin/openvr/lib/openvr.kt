package openvr.lib

import glm_.BYTES
import glm_.L
import glm_.i
import glm_.mat3x3.Mat3
import glm_.mat4x4.Mat4
import glm_.quat.Quat
import glm_.quat.QuatD
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import glm_.vec3.Vec3d
import glm_.vec4.Vec4
import kool.Ptr
import kool.adr
import org.lwjgl.openvr.*
import org.lwjgl.openvr.VR.*
import org.lwjgl.system.MemoryUtil.*


object vr {

    const val steamVrVersionMajor = 1
    const val steamVrVersionMinor = 4
    const val steamVrVersionBuild = 18

    const val steamVrVersion = "$steamVrVersionMajor.$steamVrVersionMinor.$steamVrVersionBuild beta 00"

    fun HmdMatrix34(): HmdMatrix34 = HmdMatrix34.callocStack()
    fun HmdMatrix34(capacity: Int): HmdMatrix34.Buffer = HmdMatrix34.callocStack(capacity)
    inline fun HmdMatrix34(block: HmdMatrix34.() -> Unit): HmdMatrix34 = HmdMatrix34().also(block)
    fun HmdMatrix34(mat: Mat4): HmdMatrix34 = HmdMatrix34().apply { mat.toHmdMatrix34(adr) }

    fun HmdMatrix33(): HmdMatrix33 = HmdMatrix33.callocStack()
    fun HmdMatrix33(capacity: Int): HmdMatrix33.Buffer = HmdMatrix33.callocStack(capacity)
    inline fun HmdMatrix33(block: HmdMatrix33.() -> Unit): HmdMatrix33 = HmdMatrix33().also(block)
    fun HmdMatrix33(mat: Mat3): HmdMatrix33 = HmdMatrix33().apply { mat.to(adr, true) }

    fun HmdMatrix44(): HmdMatrix44 = HmdMatrix44.callocStack()
    fun HmdMatrix44(capacity: Int): HmdMatrix44.Buffer = HmdMatrix44.callocStack(capacity)
    inline fun HmdMatrix44(block: HmdMatrix44.() -> Unit): HmdMatrix44 = HmdMatrix44().also(block)
    fun HmdMatrix44(mat: Mat4): HmdMatrix44 = HmdMatrix44().apply { mat.to(adr, true) }

    fun HmdVector3(): HmdVector3 = HmdVector3.callocStack()
    fun HmdVector3(capacity: Int): HmdVector3.Buffer = HmdVector3.callocStack(capacity)
    inline fun HmdVector3(block: HmdVector3.() -> Unit): HmdVector3 = HmdVector3().also(block)
    fun HmdVector3(vec: Vec3): HmdVector3 = HmdVector3().apply { vec to adr }

    fun HmdVector4(): HmdVector4 = HmdVector4.callocStack()
    fun HmdVector4(capacity: Int): HmdVector4.Buffer = HmdVector4.callocStack(capacity)
    inline fun HmdVector4(block: HmdVector4.() -> Unit): HmdVector4 = HmdVector4().also(block)
    fun HmdVector4(vec: Vec4): HmdVector4 = HmdVector4().apply { vec to adr }

    fun HmdVector3d(): HmdVector3d = HmdVector3d.callocStack()
    fun HmdVector3d(capacity: Int): HmdVector3d.Buffer = HmdVector3d.callocStack(capacity)
    inline fun HmdVector3d(block: HmdVector3d.() -> Unit): HmdVector3d = HmdVector3d().also(block)
    fun HmdVector3d(vec: Vec3d): HmdVector3d = HmdVector3d().apply { vec to adr }

    fun HmdVector2(): HmdVector2 = HmdVector2.callocStack()
    fun HmdVector2(capacity: Int): HmdVector2.Buffer = HmdVector2.callocStack(capacity)
    inline fun HmdVector2(block: HmdVector2.() -> Unit): HmdVector2 = HmdVector2().also(block)
    fun HmdVector2(vec: Vec2): HmdVector2 = HmdVector2().apply { vec to adr }

    fun HmdQuaternion(): HmdQuaternion = HmdQuaternion.callocStack()
    fun HmdQuaternion(capacity: Int): HmdQuaternion.Buffer = HmdQuaternion.callocStack(capacity)
    inline fun HmdQuaternion(block: HmdQuaternion.() -> Unit): HmdQuaternion = HmdQuaternion().also(block)
    fun HmdQuaternion(quat: QuatD): HmdQuaternion = HmdQuaternion().apply { quat to adr }

    fun HmdQuaternionf(): HmdQuaternionf = HmdQuaternionf.callocStack()
    fun HmdQuaternionf(capacity: Int): HmdQuaternionf.Buffer = HmdQuaternionf.callocStack(capacity)
    inline fun HmdQuaternionf(block: HmdQuaternionf.() -> Unit): HmdQuaternionf = HmdQuaternionf().also(block)
    fun HmdQuaternionf(quat: Quat): HmdQuaternionf = HmdQuaternionf().apply { quat to adr }

    fun HmdColor(): HmdColor = HmdColor.callocStack()
    fun HmdColor(capacity: Int): HmdColor.Buffer = HmdColor.callocStack(capacity)
    inline fun HmdColor(block: HmdColor.() -> Unit): HmdColor = HmdColor().also(block)
    // TODO vec3/4?

    fun HmdQuad(): HmdQuad = HmdQuad.callocStack()
    fun HmdQuad(capacity: Int): HmdQuad.Buffer = HmdQuad.callocStack(capacity)
    inline fun HmdQuad(block: HmdQuad.() -> Unit): HmdQuad = HmdQuad().also(block)

    fun HmdRect2(): HmdRect2 = HmdRect2.callocStack()
    fun HmdRect2(capacity: Int): HmdRect2.Buffer = HmdRect2.callocStack(capacity)
    inline fun HmdRect2(block: HmdRect2.() -> Unit): HmdRect2 = HmdRect2().also(block)

    fun DistortionCoordinates(): DistortionCoordinates = DistortionCoordinates.callocStack()
    fun DistortionCoordinates(capacity: Int): DistortionCoordinates.Buffer = DistortionCoordinates.callocStack(capacity)
    inline fun DistortionCoordinates(block: DistortionCoordinates.() -> Unit): DistortionCoordinates = DistortionCoordinates().also(block)

    fun Texture(): Texture = Texture.callocStack()
    fun Texture(capacity: Int): Texture.Buffer = Texture.callocStack(capacity)
    inline fun Texture(block: Texture.() -> Unit): Texture = Texture().also(block)


    fun Texture(handle: Int, type: TextureType, colorSpace: ColorSpace): Texture = Texture().handle(handle.L).eType(type.i).eColorSpace(colorSpace.i)


    const val driverNone = -1

    const val maxDriverDebugResponseSize = 32768

    const val trackedDeviceIndex_Hmd = 0
    const val maxTrackedDeviceCount = 64
    const val trackedDeviceIndexOther = -1
    const val trackedDeviceIndexInvalid = -1


    fun TrackedDevicePose(): TrackedDevicePose = TrackedDevicePose.callocStack()
    fun TrackedDevicePose(capacity: Int): TrackedDevicePose.Buffer = TrackedDevicePose.callocStack(capacity)
    inline fun TrackedDevicePose(block: TrackedDevicePose.() -> Unit): TrackedDevicePose = TrackedDevicePose().also(block)


    const val invalidPropertyContainer: PropertyContainerHandle = 0
    const val invalidPropertyTag: PropertyTypeTag = 0

    const val invalidDriverHandle: PropertyContainerHandle = 0

    // Use these tags to set/get common types as struct properties
    const val floatPropertyTag: PropertyTypeTag = 1
    const val int32PropertyTag: PropertyTypeTag = 2
    const val uint64PropertyTag: PropertyTypeTag = 3
    const val boolPropertyTag: PropertyTypeTag = 4
    const val stringPropertyTag: PropertyTypeTag = 5

    const val hmdMatrix34PropertyTag: PropertyTypeTag = 20
    const val hmdMatrix44PropertyTag: PropertyTypeTag = 21
    const val hmdVector3PropertyTag: PropertyTypeTag = 22
    const val hmdVector4PropertyTag: PropertyTypeTag = 23
    const val hmdVector2PropertyTag: PropertyTypeTag = 24
    const val hmdQuadPropertyTag: PropertyTypeTag = 25

    const val hiddenAreaPropertyTag: PropertyTypeTag = 30
    const val pathHandleInfoTag: PropertyTypeTag = 31
    const val actionPropertyTag: PropertyTypeTag = 32
    const val inputValuePropertyTag: PropertyTypeTag = 33
    const val wildcardPropertyTag: PropertyTypeTag = 34
    const val hapticVibrationPropertyTag: PropertyTypeTag = 35
    const val skeletonPropertyTag: PropertyTypeTag = 36

    const val spatialAnchorPosePropertyTag = 40
    const val jsonPropertyTag = 41
    const val activeActionSetPropertyTag = 42

    const val openVRInternalReserved_Start: PropertyTypeTag = 1000
    const val openVRInternalReserved_End: PropertyTypeTag = 10000


    /** No string property will ever be longer than this length */
    const val maxPropertyStringSize = 32 * 1024

    const val invalidActionHandle: VRActionHandle = NULL
    const val invalidActionSetHandle: VRActionSetHandle = NULL
    const val invalidInputValueHandle: VRInputValueHandle = NULL

    fun TextureBounds(): VRTextureBounds = VRTextureBounds.callocStack()
    fun TextureBounds(capacity: Int): VRTextureBounds.Buffer = VRTextureBounds.callocStack(capacity)
    inline fun TextureBounds(block: VRTextureBounds.() -> Unit): VRTextureBounds = TextureBounds().also(block)

    fun TextureWithPose(): VRTextureWithPose = VRTextureWithPose.callocStack()
    fun TextureWithPose(capacity: Int): VRTextureWithPose.Buffer = VRTextureWithPose.callocStack(capacity)
    inline fun TextureWithPose(block: VRTextureWithPose.() -> Unit): VRTextureWithPose = TextureWithPose().also(block)

    fun TextureDepthInfo(): VRTextureDepthInfo = VRTextureDepthInfo.callocStack()
    fun TextureDepthInfo(capacity: Int): VRTextureDepthInfo.Buffer = VRTextureDepthInfo.callocStack(capacity)
    inline fun TextureDepthInfo(block: VRTextureDepthInfo.() -> Unit): VRTextureDepthInfo = TextureDepthInfo().also(block)

    fun TextureWithDepth(): VRTextureWithDepth = VRTextureWithDepth.callocStack()
    fun TextureWithDepth(capacity: Int): VRTextureWithDepth.Buffer = VRTextureWithDepth.callocStack(capacity)
    inline fun TextureWithDepth(block: VRTextureWithDepth.() -> Unit): VRTextureWithDepth = TextureWithDepth().also(block)

    fun TextureWithPoseAndDepth(): VRTextureWithPoseAndDepth = VRTextureWithPoseAndDepth.callocStack()
    fun TextureWithPoseAndDepth(capacity: Int): VRTextureWithPoseAndDepth.Buffer = VRTextureWithPoseAndDepth.callocStack(capacity)
    inline fun TextureWithPoseAndDepth(block: VRTextureWithPoseAndDepth.() -> Unit): VRTextureWithPoseAndDepth = TextureWithPoseAndDepth().also(block)

    fun VRVulkanTextureData(): VRVulkanTextureData = VRVulkanTextureData.callocStack()
    fun VRVulkanTextureData(capacity: Int): VRVulkanTextureData.Buffer = VRVulkanTextureData.callocStack(capacity)
    inline fun VRVulkanTextureData(block: VRVulkanTextureData.() -> Unit): VRVulkanTextureData = VRVulkanTextureData().also(block)

//    inline fun VREventController(): VREventController = TODO()// VREventController.
//    inline fun VREventController(capacity: Int): VREventController.Buffer = TODO()//VREventController.ca
//    inline fun VREventController(block: VREventController.() -> Unit): VREventController = VREventController().also(block)
//
//    inline fun VREventMouse(): VREventMouse = VREventMouse
//    inline fun VREventMouse(capacity: Int): VREventMouse.Buffer = VREventMouse.create(ptr.advance(VREventMouse.SIZEOF * capacity), capacity)
//    inline fun VREventMouse(block: VREventMouse.() -> Unit): VREventMouse = VREventMouse().also(block)
//
//    inline fun VREventScroll(): VREventScroll = VREventScroll
//    inline fun VREventScroll(capacity: Int): VREventScroll.Buffer = VREventScroll.create(ptr.advance(VREventScroll.SIZEOF * capacity), capacity)
//    inline fun VREventScroll(block: VREventScroll.() -> Unit): VREventScroll = VREventScroll().also(block)
//
//    inline fun VREventTouchPadMove(): VREventTouchPadMove = VREventTouchPadMove.create(ptr.advance(VREventTouchPadMove.SIZEOF))
//    inline fun VREventTouchPadMove(capacity: Int): VREventTouchPadMove.Buffer = VREventTouchPadMove.create(ptr.advance(VREventTouchPadMove.SIZEOF * capacity), capacity)
//    inline fun VREventTouchPadMove(block: VREventTouchPadMove.() -> Unit): VREventTouchPadMove = VREventTouchPadMove().also(block)
//
//    inline fun VREventNotification(): VREventNotification = VREventNotification.create(ptr.advance(VREventNotification.SIZEOF))
//    inline fun VREventNotification(capacity: Int): VREventNotification.Buffer = VREventNotification.create(ptr.advance(VREventNotification.SIZEOF * capacity), capacity)
//    inline fun VREventNotification(block: VREventNotification.() -> Unit): VREventNotification = VREventNotification().also(block)
//
//    inline fun VREventProcess(): VREventProcess = VREventProcess.create(ptr.advance(VREventProcess.SIZEOF))
//    inline fun VREventProcess(capacity: Int): VREventProcess.Buffer = VREventProcess.create(ptr.advance(VREventProcess.SIZEOF * capacity), capacity)
//    inline fun VREventProcess(block: VREventProcess.() -> Unit): VREventProcess = VREventProcess().also(block)
//
//    inline fun VREventOverlay(): VREventOverlay = VREventOverlay.create(ptr.advance(VREventOverlay.SIZEOF))
//    inline fun VREventOverlay(capacity: Int): VREventOverlay.Buffer = VREventOverlay.create(ptr.advance(VREventOverlay.SIZEOF * capacity), capacity)
//    inline fun VREventOverlay(block: VREventOverlay.() -> Unit): VREventOverlay = VREventOverlay().also(block)
//
//    inline fun VREventStatus(): VREventStatus = VREventStatus.create(ptr.advance(VREventStatus.SIZEOF))
//    inline fun VREventStatus(capacity: Int): VREventStatus.Buffer = VREventStatus.create(ptr.advance(VREventStatus.SIZEOF * capacity), capacity)
//    inline fun VREventStatus(block: VREventStatus.() -> Unit): VREventStatus = VREventStatus().also(block)
//
//    inline fun VREventKeyboard(): VREventKeyboard = VREventKeyboard.create(ptr.advance(VREventKeyboard.SIZEOF))
//    inline fun VREventKeyboard(capacity: Int): VREventKeyboard.Buffer = VREventKeyboard.create(ptr.advance(VREventKeyboard.SIZEOF * capacity), capacity)
//    inline fun VREventKeyboard(block: VREventKeyboard.() -> Unit): VREventKeyboard = VREventKeyboard().also(block)
//
//    inline fun VREventIpd(): VREventIpd = VREventIpd.create(ptr.advance(VREventIpd.SIZEOF))
//    inline fun VREventIpd(capacity: Int): VREventIpd.Buffer = VREventIpd.create(ptr.advance(VREventIpd.SIZEOF * capacity), capacity)
//    inline fun VREventIpd(block: VREventIpd.() -> Unit): VREventIpd = VREventIpd().also(block)
//
//    inline fun VREventChaperone(): VREventChaperone = VREventChaperone.create(ptr.advance(VREventChaperone.SIZEOF))
//    inline fun VREventChaperone(capacity: Int): VREventChaperone.Buffer = VREventChaperone.create(ptr.advance(VREventChaperone.SIZEOF * capacity), capacity)
//    inline fun VREventChaperone(block: VREventChaperone.() -> Unit): VREventChaperone = VREventChaperone().also(block)
//
//    inline fun VREventReserved(): VREventReserved = VREventReserved.create(ptr.advance(VREventReserved.SIZEOF))
//    inline fun VREventReserved(capacity: Int): VREventReserved.Buffer = VREventReserved.create(ptr.advance(VREventReserved.SIZEOF * capacity), capacity)
//    inline fun VREventReserved(block: VREventReserved.() -> Unit): VREventReserved = VREventReserved().also(block)
//
//    inline fun VREventPerformanceTest(): VREventPerformanceTest = VREventPerformanceTest.create(ptr.advance(VREventPerformanceTest.SIZEOF))
//    inline fun VREventPerformanceTest(capacity: Int): VREventPerformanceTest.Buffer = VREventPerformanceTest.create(ptr.advance(VREventPerformanceTest.SIZEOF * capacity), capacity)
//    inline fun VREventPerformanceTest(block: VREventPerformanceTest.() -> Unit): VREventPerformanceTest = VREventPerformanceTest().also(block)
//
//    inline fun VREventSeatedZeroPoseReset(): VREventSeatedZeroPoseReset = VREventSeatedZeroPoseReset.create(ptr.advance(VREventSeatedZeroPoseReset.SIZEOF))
//    inline fun VREventSeatedZeroPoseReset(capacity: Int): VREventSeatedZeroPoseReset.Buffer = VREventSeatedZeroPoseReset.create(ptr.advance(VREventSeatedZeroPoseReset.SIZEOF * capacity), capacity)
//    inline fun VREventSeatedZeroPoseReset(block: VREventSeatedZeroPoseReset.() -> Unit): VREventSeatedZeroPoseReset = VREventSeatedZeroPoseReset().also(block)
//
//    inline fun VREventScreenshot(): VREventScreenshot = VREventScreenshot.create(ptr.advance(VREventScreenshot.SIZEOF))
//    inline fun VREventScreenshot(capacity: Int): VREventScreenshot.Buffer = VREventScreenshot.create(ptr.advance(VREventScreenshot.SIZEOF * capacity), capacity)
//    inline fun VREventScreenshot(block: VREventScreenshot.() -> Unit): VREventScreenshot = VREventScreenshot().also(block)
//
//    inline fun VREventScreenshotProgress(): VREventScreenshotProgress = VREventScreenshotProgress.create(ptr.advance(VREventScreenshotProgress.SIZEOF))
//    inline fun VREventScreenshotProgress(capacity: Int): VREventScreenshotProgress.Buffer = VREventScreenshotProgress.create(ptr.advance(VREventScreenshotProgress.SIZEOF * capacity), capacity)
//    inline fun VREventScreenshotProgress(block: VREventScreenshotProgress.() -> Unit): VREventScreenshotProgress = VREventScreenshotProgress().also(block)
//
//    inline fun VREventApplicationLaunch(): VREventApplicationLaunch = VREventApplicationLaunch.create(ptr.advance(VREventApplicationLaunch.SIZEOF))
//    inline fun VREventApplicationLaunch(capacity: Int): VREventApplicationLaunch.Buffer = VREventApplicationLaunch.create(ptr.advance(VREventApplicationLaunch.SIZEOF * capacity), capacity)
//    inline fun VREventApplicationLaunch(block: VREventApplicationLaunch.() -> Unit): VREventApplicationLaunch = VREventApplicationLaunch().also(block)
//
//    inline fun VREventEditingCameraSurface(): VREventEditingCameraSurface = VREventEditingCameraSurface.create(ptr.advance(VREventEditingCameraSurface.SIZEOF))
//    inline fun VREventEditingCameraSurface(capacity: Int): VREventEditingCameraSurface.Buffer = VREventEditingCameraSurface.create(ptr.advance(VREventEditingCameraSurface.SIZEOF * capacity), capacity)
//    inline fun VREventEditingCameraSurface(block: VREventEditingCameraSurface.() -> Unit): VREventEditingCameraSurface = VREventEditingCameraSurface().also(block)
//
//    inline fun VREventMessageOverlay(): VREventMessageOverlay = VREventMessageOverlay.create(ptr.advance(VREventMessageOverlay.SIZEOF))
//    inline fun VREventMessageOverlay(capacity: Int): VREventMessageOverlay.Buffer = VREventMessageOverlay.create(ptr.advance(VREventMessageOverlay.SIZEOF * capacity), capacity)
//    inline fun VREventMessageOverlay(block: VREventMessageOverlay.() -> Unit): VREventMessageOverlay = VREventMessageOverlay().also(block)
//
//    inline fun VREventProperty(): VREventProperty = VREventProperty.create(ptr.advance(VREventProperty.SIZEOF))
//    inline fun VREventProperty(capacity: Int): VREventProperty.Buffer = VREventProperty.create(ptr.advance(VREventProperty.SIZEOF * capacity), capacity)
//    inline fun VREventProperty(block: VREventProperty.() -> Unit): VREventProperty = VREventProperty().also(block)
//
//    inline fun VREventDualAnalog(): VREventDualAnalog = VREventDualAnalog.create(ptr.advance(VREventDualAnalog.SIZEOF))
//    inline fun VREventDualAnalog(capacity: Int): VREventDualAnalog.Buffer = VREventDualAnalog.create(ptr.advance(VREventDualAnalog.SIZEOF * capacity), capacity)
//    inline fun VREventDualAnalog(block: VREventDualAnalog.() -> Unit): VREventDualAnalog = VREventDualAnalog().also(block)
//
//    inline fun VREventHapticVibration(): VREventHapticVibration = VREventHapticVibration.create(ptr.advance(VREventHapticVibration.SIZEOF))
//    inline fun VREventHapticVibration(capacity: Int): VREventHapticVibration.Buffer = VREventHapticVibration.create(ptr.advance(VREventHapticVibration.SIZEOF * capacity), capacity)
//    inline fun VREventHapticVibration(block: VREventHapticVibration.() -> Unit): VREventHapticVibration = VREventHapticVibration().also(block)
//
//    inline fun VREventWebConsole(): VREventWebConsole = VREventWebConsole.create(ptr.advance(VREventWebConsole.SIZEOF))
//    inline fun VREventWebConsole(capacity: Int): VREventWebConsole.Buffer = VREventWebConsole.create(ptr.advance(VREventWebConsole.SIZEOF * capacity), capacity)
//    inline fun VREventWebConsole(block: VREventWebConsole.() -> Unit): VREventWebConsole = VREventWebConsole().also(block)
//
//    inline fun VREventInputBindingLoad(): VREventInputBindingLoad = VREventInputBindingLoad.create(ptr.advance(VREventInputBindingLoad.SIZEOF))
//    inline fun VREventInputBindingLoad(capacity: Int): VREventInputBindingLoad.Buffer = VREventInputBindingLoad.create(ptr.advance(VREventInputBindingLoad.SIZEOF * capacity), capacity)
//    inline fun VREventInputBindingLoad(block: VREventInputBindingLoad.() -> Unit): VREventInputBindingLoad = VREventInputBindingLoad().also(block)

    fun VREvent(): VREvent = VREvent.callocStack()
    fun VREvent(capacity: Int): VREvent.Buffer = VREvent.callocStack(capacity)
    inline fun VREvent(block: VREvent.() -> Unit): VREvent = VREvent().also(block)

    fun HiddenAreaMesh(): HiddenAreaMesh = HiddenAreaMesh.callocStack()
    fun HiddenAreaMesh(capacity: Int): HiddenAreaMesh.Buffer = HiddenAreaMesh.callocStack(capacity)
    inline fun HiddenAreaMesh(block: HiddenAreaMesh.() -> Unit): HiddenAreaMesh = HiddenAreaMesh().also(block)

    fun VRControllerAxis(): VRControllerAxis = VRControllerAxis.callocStack()
    fun VRControllerAxis(capacity: Int): VRControllerAxis.Buffer = VRControllerAxis.callocStack(capacity)
    inline fun VRControllerAxis(block: VRControllerAxis.() -> Unit): VRControllerAxis = VRControllerAxis().also(block)

    /** the number of axes in the controller state */
    const val controllerStateAxisCount = 5

    fun VRControllerState(): VRControllerState = VRControllerState.callocStack()
    fun VRControllerState(capacity: Int): VRControllerState.Buffer = VRControllerState.callocStack(capacity)
    inline fun VRControllerState(block: VRControllerState.() -> Unit): VRControllerState = VRControllerState().also(block)

    fun CompositorOverlaySettings(): CompositorOverlaySettings = CompositorOverlaySettings.callocStack()
    fun CompositorOverlaySettings(capacity: Int): CompositorOverlaySettings.Buffer = CompositorOverlaySettings.callocStack(capacity)
    inline fun CompositorOverlaySettings(block: CompositorOverlaySettings.() -> Unit): CompositorOverlaySettings = CompositorOverlaySettings().also(block)

    const val overlayHandleInvalid: VROverlayHandle = NULL

    fun VRBoneTransform(): VRBoneTransform = VRBoneTransform.callocStack()
    fun VRBoneTransform(capacity: Int): VRBoneTransform.Buffer = VRBoneTransform.callocStack(capacity)
    inline fun VRBoneTransform(block: VRBoneTransform.() -> Unit): VRBoneTransform = VRBoneTransform().also(block)

    const val invalidBoneIndex: BoneIndex = -1

    const val INVALID_TRACKED_CAMERA_HANDLE: TrackedCameraHandle = NULL

    fun CameraVideoStreamFrameHeader(): CameraVideoStreamFrameHeader = CameraVideoStreamFrameHeader.callocStack()
    fun CameraVideoStreamFrameHeader(capacity: Int): CameraVideoStreamFrameHeader.Buffer = CameraVideoStreamFrameHeader.callocStack(capacity)
    inline fun CameraVideoStreamFrameHeader(block: CameraVideoStreamFrameHeader.() -> Unit): CameraVideoStreamFrameHeader = CameraVideoStreamFrameHeader().also(block)

    const val screenshotHandleInvalid: ScreenshotHandle = 0

//    inline fun DriverDirectModeFrameTiming(): DriverDirectModeFrameTiming = DriverDirectModeFrameTiming.ca
//    inline fun DriverDirectModeFrameTiming(capacity: Int): DriverDirectModeFrameTiming.Buffer = DriverDirectModeFrameTiming.create(ptr.advance(DriverDirectModeFrameTiming.SIZEOF * capacity), capacity)
//    inline fun DriverDirectModeFrameTiming(block: DriverDirectModeFrameTiming.() -> Unit): DriverDirectModeFrameTiming = DriverDirectModeFrameTiming().also(block)

//    inline fun ImuSample(): ImuSample = ImuSample.create(ptr.advance(ImuSample.SIZEOF))
//    inline fun ImuSample(capacity: Int): ImuSample.Buffer = ImuSample.create(ptr.advance(ImuSample.SIZEOF * capacity), capacity)
//    inline fun ImuSample(block: ImuSample.() -> Unit): ImuSample = ImuSample().also(block)

    // ivrsystem.h -> class

    // ivrapplications.h -> class

    fun AppOverrideKeys(): AppOverrideKeys = AppOverrideKeys.callocStack()
    fun AppOverrideKeys(capacity: Int): AppOverrideKeys.Buffer = AppOverrideKeys.callocStack(capacity)
    inline fun AppOverrideKeys(block: AppOverrideKeys.() -> Unit): AppOverrideKeys = AppOverrideKeys().also(block)

    /** Currently recognized mime types */
    const val mimeType_HomeApp = "vr/home"
    const val mimeType_GameTheater = "vr/game_theater"

    // ivrsettings.h -> class

    // ivrchaperone.h -> class

    // ivrchaperonesetup.h -> class

    // ivrcompositor.h -> class

    fun CompositorFrameTiming(): CompositorFrameTiming = CompositorFrameTiming.callocStack()
    fun CompositorFrameTiming(capacity: Int): CompositorFrameTiming.Buffer = CompositorFrameTiming.callocStack(capacity)
    inline fun CompositorFrameTiming(block: CompositorFrameTiming.() -> Unit): CompositorFrameTiming = CompositorFrameTiming().also(block)

    fun CompositorCumulativeStats(): CompositorCumulativeStats = CompositorCumulativeStats.callocStack()
    fun CompositorCumulativeStats(capacity: Int): CompositorCumulativeStats.Buffer = CompositorCumulativeStats.callocStack(capacity)
    inline fun CompositorCumulativeStats(block: CompositorCumulativeStats.() -> Unit): CompositorCumulativeStats = CompositorCumulativeStats().also(block)

    // ivrnotifications.h -> class

    // ivroverlay.h -> class

    fun VROverlayIntersectionParams(): VROverlayIntersectionParams = VROverlayIntersectionParams.callocStack()
    fun VROverlayIntersectionParams(capacity: Int): VROverlayIntersectionParams.Buffer = VROverlayIntersectionParams.callocStack(capacity)
    inline fun VROverlayIntersectionParams(block: VROverlayIntersectionParams.() -> Unit): VROverlayIntersectionParams = VROverlayIntersectionParams().also(block)

    fun VROverlayIntersectionResults(): VROverlayIntersectionResults = VROverlayIntersectionResults.callocStack()
    fun VROverlayIntersectionResults(capacity: Int): VROverlayIntersectionResults.Buffer = VROverlayIntersectionResults.callocStack(capacity)
    inline fun VROverlayIntersectionResults(block: VROverlayIntersectionResults.() -> Unit): VROverlayIntersectionResults = VROverlayIntersectionResults().also(block)

    fun IntersectionMaskRectangle(): IntersectionMaskRectangle = IntersectionMaskRectangle.callocStack()
    fun IntersectionMaskRectangle(capacity: Int): IntersectionMaskRectangle.Buffer = IntersectionMaskRectangle.callocStack(capacity)
    inline fun IntersectionMaskRectangle(block: IntersectionMaskRectangle.() -> Unit): IntersectionMaskRectangle = IntersectionMaskRectangle().also(block)

    fun IntersectionMaskCircle(): IntersectionMaskCircle = IntersectionMaskCircle.callocStack()
    fun IntersectionMaskCircle(capacity: Int): IntersectionMaskCircle.Buffer = IntersectionMaskCircle.callocStack(capacity)
    inline fun IntersectionMaskCircle(block: IntersectionMaskCircle.() -> Unit): IntersectionMaskCircle = IntersectionMaskCircle().also(block)

    fun VROverlayIntersectionMaskPrimitiveData(): VROverlayIntersectionMaskPrimitiveData = VROverlayIntersectionMaskPrimitiveData.callocStack()
    fun VROverlayIntersectionMaskPrimitiveData(capacity: Int): VROverlayIntersectionMaskPrimitiveData.Buffer = VROverlayIntersectionMaskPrimitiveData.callocStack(capacity)
    inline fun VROverlayIntersectionMaskPrimitiveData(block: VROverlayIntersectionMaskPrimitiveData.() -> Unit): VROverlayIntersectionMaskPrimitiveData = VROverlayIntersectionMaskPrimitiveData().also(block)

    fun VROverlayIntersectionMaskPrimitive(): VROverlayIntersectionMaskPrimitive = VROverlayIntersectionMaskPrimitive.callocStack()
    fun VROverlayIntersectionMaskPrimitive(capacity: Int): VROverlayIntersectionMaskPrimitive.Buffer = VROverlayIntersectionMaskPrimitive.callocStack(capacity)
    inline fun VROverlayIntersectionMaskPrimitive(block: VROverlayIntersectionMaskPrimitive.() -> Unit): VROverlayIntersectionMaskPrimitive = VROverlayIntersectionMaskPrimitive().also(block)

    // ivrrendermodels.h -> class

    fun RenderModelComponentState(): RenderModelComponentState = RenderModelComponentState.callocStack()
    fun RenderModelComponentState(capacity: Int): RenderModelComponentState.Buffer = RenderModelComponentState.callocStack(capacity)
    inline fun RenderModelComponentState(block: RenderModelComponentState.() -> Unit): RenderModelComponentState = RenderModelComponentState().also(block)

    fun RenderModel(): RenderModel = RenderModel.callocStack()
    fun RenderModel(capacity: Int): RenderModel.Buffer = RenderModel.callocStack(capacity)
    inline fun RenderModel(block: RenderModel.() -> Unit): RenderModel = RenderModel().also(block)

    fun RenderModelControllerModeState(): RenderModelControllerModeState = RenderModelControllerModeState.callocStack()
    fun RenderModelControllerModeState(capacity: Int): RenderModelControllerModeState.Buffer = RenderModelControllerModeState.callocStack(capacity)
    inline fun RenderModelControllerModeState(block: RenderModelControllerModeState.() -> Unit): RenderModelControllerModeState = RenderModelControllerModeState().also(block)

    // ivrextendeddisplay.h -> class

    // ivrtrackedcamera.h -> class

    // ivrscreenshots.h -> class

    // ivrresources.h -> class

    // ivrdrivermanager.h -> class

    // ivrinput.h -> class

    fun InputAnalogActionData(): InputAnalogActionData = InputAnalogActionData.callocStack()
    fun InputAnalogActionData(capacity: Int): InputAnalogActionData.Buffer = InputAnalogActionData.callocStack(capacity)
    inline fun InputAnalogActionData(block: InputAnalogActionData.() -> Unit): InputAnalogActionData = InputAnalogActionData().also(block)

    fun InputDigitalActionData(): InputDigitalActionData = InputDigitalActionData.callocStack()
    fun InputDigitalActionData(capacity: Int): InputDigitalActionData.Buffer = InputDigitalActionData.callocStack(capacity)
    inline fun InputDigitalActionData(block: InputDigitalActionData.() -> Unit): InputDigitalActionData = InputDigitalActionData().also(block)

    fun InputPoseActionData(): InputPoseActionData = InputPoseActionData.callocStack()
    fun InputPoseActionData(capacity: Int): InputPoseActionData.Buffer = InputPoseActionData.callocStack(capacity)
    inline fun InputPoseActionData(block: InputPoseActionData.() -> Unit): InputPoseActionData = InputPoseActionData().also(block)


    fun InputSkeletalActionData(): InputSkeletalActionData = InputSkeletalActionData.callocStack()
    fun InputSkeletalActionData(capacity: Int): InputSkeletalActionData.Buffer = InputSkeletalActionData.callocStack(capacity)
    inline fun InputSkeletalActionData(block: InputSkeletalActionData.() -> Unit): InputSkeletalActionData = InputSkeletalActionData().also(block)


    fun InputOriginInfo(): InputOriginInfo = InputOriginInfo.callocStack()
    fun InputOriginInfo(capacity: Int): InputOriginInfo.Buffer = InputOriginInfo.callocStack(capacity)
    inline fun InputOriginInfo(block: InputOriginInfo.() -> Unit): InputOriginInfo = InputOriginInfo().also(block)


    fun VRActiveActionSet(): VRActiveActionSet = VRActiveActionSet.callocStack()
    fun VRActiveActionSet(capacity: Int): VRActiveActionSet.Buffer = VRActiveActionSet.callocStack(capacity)
    inline fun VRActiveActionSet(block: VRActiveActionSet.() -> Unit): VRActiveActionSet = VRActiveActionSet().also(block)


    fun VRSkeletalSummaryData(): VRSkeletalSummaryData = VRSkeletalSummaryData.callocStack()
    fun VRSkeletalSummaryData(capacity: Int): VRSkeletalSummaryData.Buffer = VRSkeletalSummaryData.callocStack(capacity)
    inline fun VRSkeletalSummaryData(block: VRSkeletalSummaryData.() -> Unit): VRSkeletalSummaryData = VRSkeletalSummaryData().also(block)

    // ivriobuffer.h -> class

    // ivrspatialanchors.h
    fun SpatialAnchorPose(): SpatialAnchorPose = SpatialAnchorPose.callocStack()

    fun SpatialAnchorPose(capacity: Int): SpatialAnchorPose.Buffer = SpatialAnchorPose.callocStack(capacity)
    inline fun SpatialAnchorPose(block: SpatialAnchorPose.() -> Unit): SpatialAnchorPose = SpatialAnchorPose().also(block)

    /** Finds the active installation of the VR API and initializes it. The provided path must be absolute
     * or relative to the current working directory. These are the local install versions of the equivalent
     * functions in steamvr.h and will work without a local Steam install.
     *
     * This path is to the "root" of the VR API install. That's the directory with
     * the "drivers" directory and a platform (i.e. "win32") directory in it, not the directory with the DLL itself.
     *
     * startupInfo is reserved for future use.
     */
    fun init(applicationType: VRApplication = VRApplication.Scene): VRInitError =
            stak {

                val error = it.nmalloc(4, Int.BYTES)
                val token = nVR_InitInternal(error, applicationType.i)

                if (memGetInt(error) == VRInitError.None.i) {
                    OpenVR.create(token)
                    if (!vrSystem.isInterfaceVersionValid) {
                        VR_ShutdownInternal()
                        return VRInitError.Init_InterfaceNotFound
                    }
                }

                VRInitError of memGetInt(error)
            }

    /** unloads vrclient.dll. Any interface pointers from the interface are invalid after this point */
    fun shutdown() = VR_ShutdownInternal()

    /** Returns true if there is an HMD attached. This check is as lightweight as possible and
     *  can be called outside of VR_Init/VR_Shutdown. It should be used when an application wants
     *  to know if initializing VR is a possibility but isn't ready to take that step yet.     */
    val isHmdPresent: Boolean
        get() = VR_IsHmdPresent()

    /** Returns true if the OpenVR runtime is installed. */
    val isRuntimeInstalled: Boolean
        get() = VR_IsRuntimeInstalled()

    /** Returns where the OpenVR runtime is installed. */
    val runtimePath: String?
        get() = memASCIISafe(nVR_RuntimePath())

    /** Returns the interface of the specified version. This method must be called after VR_Init. The
     * pointer returned is valid until VR_Shutdown is called.     */
    fun getGenericInterface(interfaceVersion: String, error: VRInitErrorBuffer): Ptr =
            stak { nVR_GetGenericInterface(it.addressOfAscii(interfaceVersion), error.adr) }

    /** Returns a token that represents whether the VR interface handles need to be reloaded */
    val initToken: Int
        get () = VR_GetInitToken()
}