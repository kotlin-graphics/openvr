package vr_

import ab.advance
import ab.appBuffer
import ab.appBuffer.ptr
import glm_.i
import glm_.mat4x4.Mat4
import glm_.set
import glm_.vec2.Vec2
import openvr.lib.VRApplication
import openvr.lib.VRInitError
import org.lwjgl.openvr.*
import org.lwjgl.openvr.OpenVR.IVRSystem
import org.lwjgl.system.MemoryUtil.NULL
import java.nio.IntBuffer
import java.nio.file.Paths


object vr {

    inline fun HmdMatrix34(): HmdMatrix34 = HmdMatrix34.create(ptr.advance(HmdMatrix34.SIZEOF))
    inline fun HmdMatrix34(capacity: Int): HmdMatrix34.Buffer = HmdMatrix34.create(ptr.advance(HmdMatrix34.SIZEOF * capacity), capacity)
    inline fun HmdMatrix34(block: HmdMatrix34.() -> Unit): HmdMatrix34 = HmdMatrix34().also(block)
    inline fun HmdMatrix34(mat: Mat4): HmdMatrix34 = HmdMatrix34().apply {
        m
                .put(0, mat[0, 0]).put(1, mat[0, 1]).put(2, mat[0, 2]).put(3, mat[0, 3])
                .put(4, mat[1, 0]).put(5, mat[1, 1]).put(6, mat[1, 2]).put(7, mat[1, 3])
                .put(8, mat[2, 0]).put(9, mat[2, 1]).put(10, mat[2, 2]).put(11, mat[2, 3])
    }

    inline fun HmdMatrix44(): HmdMatrix44 = HmdMatrix44.create(ptr.advance(HmdMatrix44.SIZEOF))
    inline fun HmdMatrix44(capacity: Int): HmdMatrix44.Buffer = HmdMatrix44.create(ptr.advance(HmdMatrix44.SIZEOF * capacity), capacity)
    inline fun HmdMatrix44(block: HmdMatrix44.() -> Unit): HmdMatrix44 = HmdMatrix44().also(block)
    inline fun HmdMatrix44(mat: Mat4): HmdMatrix44 = HmdMatrix44().apply {
        m
                .put(0, mat[0, 0]).put(1, mat[0, 1]).put(2, mat[0, 2]).put(3, mat[0, 3])
                .put(4, mat[1, 0]).put(5, mat[1, 1]).put(6, mat[1, 2]).put(7, mat[1, 3])
                .put(8, mat[2, 0]).put(9, mat[2, 1]).put(10, mat[2, 2]).put(11, mat[2, 3])
                .put(12, mat[3, 0]).put(13, mat[3, 1]).put(14, mat[3, 2]).put(15, mat[3, 3])
    }

    inline fun HmdVector3(): HmdVector3 = HmdVector3.create(ptr.advance(HmdVector3.SIZEOF))
    inline fun HmdVector3(capacity: Int): HmdVector3.Buffer = HmdVector3.create(ptr.advance(HmdVector3.SIZEOF * capacity), capacity)
    inline fun HmdVector3(block: HmdVector3.() -> Unit): HmdVector3 = HmdVector3().also(block)

    inline fun HmdVector4(): HmdVector4 = HmdVector4.create(ptr.advance(HmdVector4.SIZEOF))
    inline fun HmdVector4(capacity: Int): HmdVector4.Buffer = HmdVector4.create(ptr.advance(HmdVector4.SIZEOF * capacity), capacity)
    inline fun HmdVector4(block: HmdVector4.() -> Unit): HmdVector4 = HmdVector4().also(block)

    inline fun HmdVector3d(): HmdVector3d = HmdVector3d.create(ptr.advance(HmdVector3d.SIZEOF))
    inline fun HmdVector3d(capacity: Int): HmdVector3d.Buffer = HmdVector3d.create(ptr.advance(HmdVector3d.SIZEOF * capacity), capacity)
    inline fun HmdVector3d(block: HmdVector3d.() -> Unit): HmdVector3d = HmdVector3d().also(block)

    inline fun HmdVector2(): HmdVector2 = HmdVector2.create(ptr.advance(HmdVector2.SIZEOF))
    inline fun HmdVector2(capacity: Int): HmdVector2.Buffer = HmdVector2.create(ptr.advance(HmdVector2.SIZEOF * capacity), capacity)
    inline fun HmdVector2(block: HmdVector2.() -> Unit): HmdVector2 = HmdVector2().also(block)
    inline fun HmdVector2(vec2: Vec2): HmdVector2 = HmdVector2().apply { x = vec2.x; y = vec2.y }

    inline fun HmdQuaternion(): HmdQuaternion = HmdQuaternion.create(ptr.advance(HmdQuaternion.SIZEOF))
    inline fun HmdQuaternion(capacity: Int): HmdQuaternion.Buffer = HmdQuaternion.create(ptr.advance(HmdQuaternion.SIZEOF * capacity), capacity)
    inline fun HmdQuaternion(block: HmdQuaternion.() -> Unit): HmdQuaternion = HmdQuaternion().also(block)

    inline fun HmdQuaternionf(): HmdQuaternionf = HmdQuaternionf.create(ptr.advance(HmdQuaternionf.SIZEOF))
    inline fun HmdQuaternionf(capacity: Int): HmdQuaternionf.Buffer = HmdQuaternionf.create(ptr.advance(HmdQuaternionf.SIZEOF * capacity), capacity)
    inline fun HmdQuaternionf(block: HmdQuaternionf.() -> Unit): HmdQuaternionf = HmdQuaternionf().also(block)

    inline fun HmdColor(): HmdColor = HmdColor.create(ptr.advance(HmdColor.SIZEOF))
    inline fun HmdColor(capacity: Int): HmdColor.Buffer = HmdColor.create(ptr.advance(HmdColor.SIZEOF * capacity), capacity)
    inline fun HmdColor(block: HmdColor.() -> Unit): HmdColor = HmdColor().also(block)

    inline fun HmdQuad(): HmdQuad = HmdQuad.create(ptr.advance(HmdQuad.SIZEOF))
    inline fun HmdQuad(capacity: Int): HmdQuad.Buffer = HmdQuad.create(ptr.advance(HmdQuad.SIZEOF * capacity), capacity)
    inline fun HmdQuad(block: HmdQuad.() -> Unit): HmdQuad = HmdQuad().also(block)

    inline fun HmdRect2(): HmdRect2 = HmdRect2.create(ptr.advance(HmdRect2.SIZEOF))
    inline fun HmdRect2(capacity: Int): HmdRect2.Buffer = HmdRect2.create(ptr.advance(HmdRect2.SIZEOF * capacity), capacity)
    inline fun HmdRect2(block: HmdRect2.() -> Unit): HmdRect2 = HmdRect2().also(block)

    inline fun DistortionCoordinates(): DistortionCoordinates = DistortionCoordinates.create(ptr.advance(DistortionCoordinates.SIZEOF))
    inline fun DistortionCoordinates(capacity: Int): DistortionCoordinates.Buffer = DistortionCoordinates.create(ptr.advance(DistortionCoordinates.SIZEOF * capacity), capacity)
    inline fun DistortionCoordinates(block: DistortionCoordinates.() -> Unit): DistortionCoordinates = DistortionCoordinates().also(block)

    inline fun Texture(): Texture = Texture.create(ptr.advance(Texture.SIZEOF))
    inline fun Texture(capacity: Int): Texture.Buffer = Texture.create(ptr.advance(Texture.SIZEOF * capacity), capacity)
    inline fun Texture(block: Texture.() -> Unit): Texture = Texture().also(block)

    val driverNone = 0xFFFFFFFF.i

    val maxDriverDebugResponseSize = 32768

    val trackedDeviceIndex_Hmd = 0
    val maxTrackedDeviceCount = 64
    val trackedDeviceIndexOther = 0xFFFFFFFE.i
    val trackedDeviceIndexInvalid = 0xFFFFFFFF.i


    inline fun TrackedDevicePose(): TrackedDevicePose = TrackedDevicePose.create(ptr.advance(TrackedDevicePose.SIZEOF))
    inline fun TrackedDevicePose(capacity: Int): TrackedDevicePose.Buffer = TrackedDevicePose.create(ptr.advance(TrackedDevicePose.SIZEOF * capacity), capacity)
    inline fun TrackedDevicePose(block: TrackedDevicePose.() -> Unit): TrackedDevicePose = TrackedDevicePose().also(block)


    val invalidPropertyContainer: PropertyContainerHandle = 0
    val invalidPropertyTag: PropertyTypeTag = 0

    val invalidDriverHandle: PropertyContainerHandle = 0

    // Use these tags to set/get common types as struct properties
    val floatPropertyTag: PropertyTypeTag = 1
    val int32PropertyTag: PropertyTypeTag = 2
    val uint64PropertyTag: PropertyTypeTag = 3
    val boolPropertyTag: PropertyTypeTag = 4
    val stringPropertyTag: PropertyTypeTag = 5

    val hmdMatrix34PropertyTag: PropertyTypeTag = 20
    val hmdMatrix44PropertyTag: PropertyTypeTag = 21
    val hmdVector3PropertyTag: PropertyTypeTag = 22
    val hmdVector4PropertyTag: PropertyTypeTag = 23

    val hiddenAreaPropertyTag: PropertyTypeTag = 30
    val pathHandleInfoTag: PropertyTypeTag = 31
    val actionPropertyTag: PropertyTypeTag = 32
    val inputValuePropertyTag: PropertyTypeTag = 33
    val wildcardPropertyTag: PropertyTypeTag = 34
    val hapticVibrationPropertyTag: PropertyTypeTag = 35
    val skeletonPropertyTag: PropertyTypeTag = 36

    val openVRInternalReserved_Start: PropertyTypeTag = 1000
    val openVRInternalReserved_End: PropertyTypeTag = 10000


    /** No string property will ever be longer than this length */
    val maxPropertyStringSize = 32 * 1024


    inline fun TextureBounds(): VRTextureBounds = VRTextureBounds.create(ptr.advance(VRTextureBounds.SIZEOF))
    inline fun TextureBounds(capacity: Int): VRTextureBounds.Buffer = VRTextureBounds.create(ptr.advance(VRTextureBounds.SIZEOF * capacity), capacity)
    inline fun TextureBounds(block: VRTextureBounds.() -> Unit): VRTextureBounds = TextureBounds().also(block)

    inline fun TextureWithPose(): VRTextureWithPose = VRTextureWithPose.create(ptr.advance(VRTextureWithPose.SIZEOF))
    inline fun TextureWithPose(capacity: Int): VRTextureWithPose.Buffer = VRTextureWithPose.create(ptr.advance(VRTextureWithPose.SIZEOF * capacity), capacity)
    inline fun TextureWithPose(block: VRTextureWithPose.() -> Unit): VRTextureWithPose = TextureWithPose().also(block)

    inline fun TextureDepthInfo(): VRTextureDepthInfo = VRTextureDepthInfo.create(ptr.advance(VRTextureDepthInfo.SIZEOF))
    inline fun TextureDepthInfo(capacity: Int): VRTextureDepthInfo.Buffer = VRTextureDepthInfo.create(ptr.advance(VRTextureDepthInfo.SIZEOF * capacity), capacity)
    inline fun TextureDepthInfo(block: VRTextureDepthInfo.() -> Unit): VRTextureDepthInfo = TextureDepthInfo().also(block)

    inline fun TextureWithDepth(): VRTextureWithDepth = VRTextureWithDepth.create(ptr.advance(VRTextureWithDepth.SIZEOF))
    inline fun TextureWithDepth(capacity: Int): VRTextureWithDepth.Buffer = VRTextureWithDepth.create(ptr.advance(VRTextureWithDepth.SIZEOF * capacity), capacity)
    inline fun TextureWithDepth(block: VRTextureWithDepth.() -> Unit): VRTextureWithDepth = TextureWithDepth().also(block)

    inline fun TextureWithPoseAndDepth(): VRTextureWithPoseAndDepth = VRTextureWithPoseAndDepth.create(ptr.advance(VRTextureWithPoseAndDepth.SIZEOF))
    inline fun TextureWithPoseAndDepth(capacity: Int): VRTextureWithPoseAndDepth.Buffer = VRTextureWithPoseAndDepth.create(ptr.advance(VRTextureWithPoseAndDepth.SIZEOF * capacity), capacity)
    inline fun TextureWithPoseAndDepth(block: VRTextureWithPoseAndDepth.() -> Unit): VRTextureWithPoseAndDepth = TextureWithPoseAndDepth().also(block)

    inline fun VRVulkanTextureData(): VRVulkanTextureData = VRVulkanTextureData.create(ptr.advance(VRVulkanTextureData.SIZEOF))
    inline fun VRVulkanTextureData(capacity: Int): VRVulkanTextureData.Buffer = VRVulkanTextureData.create(ptr.advance(VRVulkanTextureData.SIZEOF * capacity), capacity)
    inline fun VRVulkanTextureData(block: VRVulkanTextureData.() -> Unit): VRVulkanTextureData = VRVulkanTextureData().also(block)

    inline fun VREventController(): VREventController = VREventController.create(ptr.advance(VREventController.SIZEOF))
    inline fun VREventController(capacity: Int): VREventController.Buffer = VREventController.create(ptr.advance(VREventController.SIZEOF * capacity), capacity)
    inline fun VREventController(block: VREventController.() -> Unit): VREventController = VREventController().also(block)

    inline fun VREventMouse(): VREventMouse = VREventMouse.create(ptr.advance(VREventMouse.SIZEOF))
    inline fun VREventMouse(capacity: Int): VREventMouse.Buffer = VREventMouse.create(ptr.advance(VREventMouse.SIZEOF * capacity), capacity)
    inline fun VREventMouse(block: VREventMouse.() -> Unit): VREventMouse = VREventMouse().also(block)

    inline fun VREventScroll(): VREventScroll = VREventScroll.create(ptr.advance(VREventScroll.SIZEOF))
    inline fun VREventScroll(capacity: Int): VREventScroll.Buffer = VREventScroll.create(ptr.advance(VREventScroll.SIZEOF * capacity), capacity)
    inline fun VREventScroll(block: VREventScroll.() -> Unit): VREventScroll = VREventScroll().also(block)

    inline fun VREventTouchPadMove(): VREventTouchPadMove = VREventTouchPadMove.create(ptr.advance(VREventTouchPadMove.SIZEOF))
    inline fun VREventTouchPadMove(capacity: Int): VREventTouchPadMove.Buffer = VREventTouchPadMove.create(ptr.advance(VREventTouchPadMove.SIZEOF * capacity), capacity)
    inline fun VREventTouchPadMove(block: VREventTouchPadMove.() -> Unit): VREventTouchPadMove = VREventTouchPadMove().also(block)

    inline fun VREventNotification(): VREventNotification = VREventNotification.create(ptr.advance(VREventNotification.SIZEOF))
    inline fun VREventNotification(capacity: Int): VREventNotification.Buffer = VREventNotification.create(ptr.advance(VREventNotification.SIZEOF * capacity), capacity)
    inline fun VREventNotification(block: VREventNotification.() -> Unit): VREventNotification = VREventNotification().also(block)

    inline fun VREventProcess(): VREventProcess = VREventProcess.create(ptr.advance(VREventProcess.SIZEOF))
    inline fun VREventProcess(capacity: Int): VREventProcess.Buffer = VREventProcess.create(ptr.advance(VREventProcess.SIZEOF * capacity), capacity)
    inline fun VREventProcess(block: VREventProcess.() -> Unit): VREventProcess = VREventProcess().also(block)

    inline fun VREventOverlay(): VREventOverlay = VREventOverlay.create(ptr.advance(VREventOverlay.SIZEOF))
    inline fun VREventOverlay(capacity: Int): VREventOverlay.Buffer = VREventOverlay.create(ptr.advance(VREventOverlay.SIZEOF * capacity), capacity)
    inline fun VREventOverlay(block: VREventOverlay.() -> Unit): VREventOverlay = VREventOverlay().also(block)

    inline fun VREventStatus(): VREventStatus = VREventStatus.create(ptr.advance(VREventStatus.SIZEOF))
    inline fun VREventStatus(capacity: Int): VREventStatus.Buffer = VREventStatus.create(ptr.advance(VREventStatus.SIZEOF * capacity), capacity)
    inline fun VREventStatus(block: VREventStatus.() -> Unit): VREventStatus = VREventStatus().also(block)

    inline fun VREventKeyboard(): VREventKeyboard = VREventKeyboard.create(ptr.advance(VREventKeyboard.SIZEOF))
    inline fun VREventKeyboard(capacity: Int): VREventKeyboard.Buffer = VREventKeyboard.create(ptr.advance(VREventKeyboard.SIZEOF * capacity), capacity)
    inline fun VREventKeyboard(block: VREventKeyboard.() -> Unit): VREventKeyboard = VREventKeyboard().also(block)

    inline fun VREventIpd(): VREventIpd = VREventIpd.create(ptr.advance(VREventIpd.SIZEOF))
    inline fun VREventIpd(capacity: Int): VREventIpd.Buffer = VREventIpd.create(ptr.advance(VREventIpd.SIZEOF * capacity), capacity)
    inline fun VREventIpd(block: VREventIpd.() -> Unit): VREventIpd = VREventIpd().also(block)

    inline fun VREventChaperone(): VREventChaperone = VREventChaperone.create(ptr.advance(VREventChaperone.SIZEOF))
    inline fun VREventChaperone(capacity: Int): VREventChaperone.Buffer = VREventChaperone.create(ptr.advance(VREventChaperone.SIZEOF * capacity), capacity)
    inline fun VREventChaperone(block: VREventChaperone.() -> Unit): VREventChaperone = VREventChaperone().also(block)

    inline fun VREventReserved(): VREventReserved = VREventReserved.create(ptr.advance(VREventReserved.SIZEOF))
    inline fun VREventReserved(capacity: Int): VREventReserved.Buffer = VREventReserved.create(ptr.advance(VREventReserved.SIZEOF * capacity), capacity)
    inline fun VREventReserved(block: VREventReserved.() -> Unit): VREventReserved = VREventReserved().also(block)

    inline fun VREventPerformanceTest(): VREventPerformanceTest = VREventPerformanceTest.create(ptr.advance(VREventPerformanceTest.SIZEOF))
    inline fun VREventPerformanceTest(capacity: Int): VREventPerformanceTest.Buffer = VREventPerformanceTest.create(ptr.advance(VREventPerformanceTest.SIZEOF * capacity), capacity)
    inline fun VREventPerformanceTest(block: VREventPerformanceTest.() -> Unit): VREventPerformanceTest = VREventPerformanceTest().also(block)

    inline fun VREventSeatedZeroPoseReset(): VREventSeatedZeroPoseReset = VREventSeatedZeroPoseReset.create(ptr.advance(VREventSeatedZeroPoseReset.SIZEOF))
    inline fun VREventSeatedZeroPoseReset(capacity: Int): VREventSeatedZeroPoseReset.Buffer = VREventSeatedZeroPoseReset.create(ptr.advance(VREventSeatedZeroPoseReset.SIZEOF * capacity), capacity)
    inline fun VREventSeatedZeroPoseReset(block: VREventSeatedZeroPoseReset.() -> Unit): VREventSeatedZeroPoseReset = VREventSeatedZeroPoseReset().also(block)

    inline fun VREventScreenshot(): VREventScreenshot = VREventScreenshot.create(ptr.advance(VREventScreenshot.SIZEOF))
    inline fun VREventScreenshot(capacity: Int): VREventScreenshot.Buffer = VREventScreenshot.create(ptr.advance(VREventScreenshot.SIZEOF * capacity), capacity)
    inline fun VREventScreenshot(block: VREventScreenshot.() -> Unit): VREventScreenshot = VREventScreenshot().also(block)

    inline fun VREventScreenshotProgress(): VREventScreenshotProgress = VREventScreenshotProgress.create(ptr.advance(VREventScreenshotProgress.SIZEOF))
    inline fun VREventScreenshotProgress(capacity: Int): VREventScreenshotProgress.Buffer = VREventScreenshotProgress.create(ptr.advance(VREventScreenshotProgress.SIZEOF * capacity), capacity)
    inline fun VREventScreenshotProgress(block: VREventScreenshotProgress.() -> Unit): VREventScreenshotProgress = VREventScreenshotProgress().also(block)

    inline fun VREventApplicationLaunch(): VREventApplicationLaunch = VREventApplicationLaunch.create(ptr.advance(VREventApplicationLaunch.SIZEOF))
    inline fun VREventApplicationLaunch(capacity: Int): VREventApplicationLaunch.Buffer = VREventApplicationLaunch.create(ptr.advance(VREventApplicationLaunch.SIZEOF * capacity), capacity)
    inline fun VREventApplicationLaunch(block: VREventApplicationLaunch.() -> Unit): VREventApplicationLaunch = VREventApplicationLaunch().also(block)

    inline fun VREventEditingCameraSurface(): VREventEditingCameraSurface = VREventEditingCameraSurface.create(ptr.advance(VREventEditingCameraSurface.SIZEOF))
    inline fun VREventEditingCameraSurface(capacity: Int): VREventEditingCameraSurface.Buffer = VREventEditingCameraSurface.create(ptr.advance(VREventEditingCameraSurface.SIZEOF * capacity), capacity)
    inline fun VREventEditingCameraSurface(block: VREventEditingCameraSurface.() -> Unit): VREventEditingCameraSurface = VREventEditingCameraSurface().also(block)

    inline fun VREventMessageOverlay(): VREventMessageOverlay = VREventMessageOverlay.create(ptr.advance(VREventMessageOverlay.SIZEOF))
    inline fun VREventMessageOverlay(capacity: Int): VREventMessageOverlay.Buffer = VREventMessageOverlay.create(ptr.advance(VREventMessageOverlay.SIZEOF * capacity), capacity)
    inline fun VREventMessageOverlay(block: VREventMessageOverlay.() -> Unit): VREventMessageOverlay = VREventMessageOverlay().also(block)

    inline fun VREventProperty(): VREventProperty = VREventProperty.create(ptr.advance(VREventProperty.SIZEOF))
    inline fun VREventProperty(capacity: Int): VREventProperty.Buffer = VREventProperty.create(ptr.advance(VREventProperty.SIZEOF * capacity), capacity)
    inline fun VREventProperty(block: VREventProperty.() -> Unit): VREventProperty = VREventProperty().also(block)

    inline fun VREventDualAnalog(): VREventDualAnalog = VREventDualAnalog.create(ptr.advance(VREventDualAnalog.SIZEOF))
    inline fun VREventDualAnalog(capacity: Int): VREventDualAnalog.Buffer = VREventDualAnalog.create(ptr.advance(VREventDualAnalog.SIZEOF * capacity), capacity)
    inline fun VREventDualAnalog(block: VREventDualAnalog.() -> Unit): VREventDualAnalog = VREventDualAnalog().also(block)

    inline fun VREventHapticVibration(): VREventHapticVibration = VREventHapticVibration.create(ptr.advance(VREventHapticVibration.SIZEOF))
    inline fun VREventHapticVibration(capacity: Int): VREventHapticVibration.Buffer = VREventHapticVibration.create(ptr.advance(VREventHapticVibration.SIZEOF * capacity), capacity)
    inline fun VREventHapticVibration(block: VREventHapticVibration.() -> Unit): VREventHapticVibration = VREventHapticVibration().also(block)

    inline fun VREventWebConsole(): VREventWebConsole = VREventWebConsole.create(ptr.advance(VREventWebConsole.SIZEOF))
    inline fun VREventWebConsole(capacity: Int): VREventWebConsole.Buffer = VREventWebConsole.create(ptr.advance(VREventWebConsole.SIZEOF * capacity), capacity)
    inline fun VREventWebConsole(block: VREventWebConsole.() -> Unit): VREventWebConsole = VREventWebConsole().also(block)

    inline fun VREventInputBindingLoad(): VREventInputBindingLoad = VREventInputBindingLoad.create(ptr.advance(VREventInputBindingLoad.SIZEOF))
    inline fun VREventInputBindingLoad(capacity: Int): VREventInputBindingLoad.Buffer = VREventInputBindingLoad.create(ptr.advance(VREventInputBindingLoad.SIZEOF * capacity), capacity)
    inline fun VREventInputBindingLoad(block: VREventInputBindingLoad.() -> Unit): VREventInputBindingLoad = VREventInputBindingLoad().also(block)

    inline fun VREvent(): VREvent = VREvent.create(ptr.advance(VREvent.SIZEOF))
    inline fun VREvent(capacity: Int): VREvent.Buffer = VREvent.create(ptr.advance(VREvent.SIZEOF * capacity), capacity)
    inline fun VREvent(block: VREvent.() -> Unit): VREvent = VREvent().also(block)

    inline fun HiddenAreaMesh(): HiddenAreaMesh = HiddenAreaMesh.create(ptr.advance(HiddenAreaMesh.SIZEOF))
    inline fun HiddenAreaMesh(capacity: Int): HiddenAreaMesh.Buffer = HiddenAreaMesh.create(ptr.advance(HiddenAreaMesh.SIZEOF * capacity), capacity)
    inline fun HiddenAreaMesh(block: HiddenAreaMesh.() -> Unit): HiddenAreaMesh = HiddenAreaMesh().also(block)

    inline fun VRControllerAxis(): VRControllerAxis = VRControllerAxis.create(ptr.advance(VRControllerAxis.SIZEOF))
    inline fun VRControllerAxis(capacity: Int): VRControllerAxis.Buffer = VRControllerAxis.create(ptr.advance(VRControllerAxis.SIZEOF * capacity), capacity)
    inline fun VRControllerAxis(block: VRControllerAxis.() -> Unit): VRControllerAxis = VRControllerAxis().also(block)

    /** the number of axes in the controller state */
    val controllerStateAxisCount = 5

    inline fun VRControllerState(): VRControllerState = VRControllerState.create(ptr.advance(VRControllerState.SIZEOF))
    inline fun VRControllerState(capacity: Int): VRControllerState.Buffer = VRControllerState.create(ptr.advance(VRControllerState.SIZEOF * capacity), capacity)
    inline fun VRControllerState(block: VRControllerState.() -> Unit): VRControllerState = VRControllerState().also(block)

    inline fun CompositorOverlaySettings(): CompositorOverlaySettings = CompositorOverlaySettings.create(ptr.advance(CompositorOverlaySettings.SIZEOF))
    inline fun CompositorOverlaySettings(capacity: Int): CompositorOverlaySettings.Buffer = CompositorOverlaySettings.create(ptr.advance(CompositorOverlaySettings.SIZEOF * capacity), capacity)
    inline fun CompositorOverlaySettings(block: CompositorOverlaySettings.() -> Unit): CompositorOverlaySettings = CompositorOverlaySettings().also(block)

    val overlayHandleInvalid: VROverlayHandle = NULL

//    inline fun VRBoneTransform(): VRBoneTransform_ = VRBoneTransform_.create(ptr.advance(VRBoneTransform_.SIZEOF)) FIXME TYPO
//    inline fun VRBoneTransform(capacity: Int): VRBoneTransform_.Buffer = VRBoneTransform_.create(ptr.advance(VRBoneTransform_.SIZEOF * capacity), capacity)
//    inline fun VRBoneTransform(block: VRBoneTransform_.() -> Unit): VRBoneTransform_ = VRBoneTransform().also(block)

    val INVALID_TRACKED_CAMERA_HANDLE: TrackedCameraHandle = NULL

    inline fun CameraVideoStreamFrameHeader(): CameraVideoStreamFrameHeader = CameraVideoStreamFrameHeader.create(ptr.advance(CameraVideoStreamFrameHeader.SIZEOF))
    inline fun CameraVideoStreamFrameHeader(capacity: Int): CameraVideoStreamFrameHeader.Buffer = CameraVideoStreamFrameHeader.create(ptr.advance(CameraVideoStreamFrameHeader.SIZEOF * capacity), capacity)
    inline fun CameraVideoStreamFrameHeader(block: CameraVideoStreamFrameHeader.() -> Unit): CameraVideoStreamFrameHeader = CameraVideoStreamFrameHeader().also(block)

    val screenshotHandleInvalid: ScreenshotHandle = 0

    inline fun DriverDirectModeFrameTiming(): DriverDirectModeFrameTiming = DriverDirectModeFrameTiming.create(ptr.advance(DriverDirectModeFrameTiming.SIZEOF))
    inline fun DriverDirectModeFrameTiming(capacity: Int): DriverDirectModeFrameTiming.Buffer = DriverDirectModeFrameTiming.create(ptr.advance(DriverDirectModeFrameTiming.SIZEOF * capacity), capacity)
    inline fun DriverDirectModeFrameTiming(block: DriverDirectModeFrameTiming.() -> Unit): DriverDirectModeFrameTiming = DriverDirectModeFrameTiming().also(block)

    inline fun ImuSample(): ImuSample = ImuSample.create(ptr.advance(ImuSample.SIZEOF))
    inline fun ImuSample(capacity: Int): ImuSample.Buffer = ImuSample.create(ptr.advance(ImuSample.SIZEOF * capacity), capacity)
    inline fun ImuSample(block: ImuSample.() -> Unit): ImuSample = ImuSample().also(block)

    // ivrsystem.h

    const val IVRSystem_Version = "IVRSystem_019"

    // ivrapplications.h

    /** The maximum length of an application key */
    const val maxApplicationKeyLength = 128

    inline fun AppOverrideKeys(): AppOverrideKeys = AppOverrideKeys.create(ptr.advance(AppOverrideKeys.SIZEOF))
    inline fun AppOverrideKeys(capacity: Int): AppOverrideKeys.Buffer = AppOverrideKeys.create(ptr.advance(AppOverrideKeys.SIZEOF * capacity), capacity)
    inline fun AppOverrideKeys(block: AppOverrideKeys.() -> Unit): AppOverrideKeys = AppOverrideKeys().also(block)

    /** Currently recognized mime types */
    const val mimeType_HomeApp = "vr/home"
    const val mimeType_GameTheater = "vr/game_theater"

    const val IVRApplications_Version = "IVRApplications_006"

    // ivrsettings.h

    /** The maximum length of a settings key */
    const val maxSettingsKeyLength = 128

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
    val SteamVR_DebugInputBinding = "debugInputBinding"

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

    //-----------------------------------------------------------------------------
// tracking overrides - keys are device paths, values are the device paths their tracking/pose information overrides
    val TrackingOverride_Section = "TrackingOverrides"

    //-----------------------------------------------------------------------------
// per-app keys - the section name for these is the app key itself. Some of these are prefixed by the controller type
    val App_BindingAutosaveURLSuffix_String = "AutosaveURL"
    val App_BindingCurrentURLSuffix_String = "CurrentURL"
    val App_NeedToUpdateAutosaveSuffix_Bool = "NeedToUpdateAutosave"
    val App_ActionManifestURL_String = "ActionManifestURL"

    // ivrchaperone.h =================================================================================================================================================

    const val IVRChaperone_Version = "IVRChaperone_003"

    // ivrchaperonesetup.h

    const val IVRChaperoneSetup_Version = "IVRChaperoneSetup_005"

    // ivrcompositor.h ================================================================================================================================================

    val VRCompositor_ReprojectionReason_Cpu = 0x01
    val VRCompositor_ReprojectionReason_Gpu = 0x02
    /**
     *  This flag indicates the async reprojection mode is active, but does not indicate if reprojection actually happened or not.
     *  Use the ReprojectionReason flags above to check if reprojection was actually applied (i.e. scene texture was reused).
     *  NumFramePresents > 1 also indicates the scene texture was reused, and also the number of times that it was presented in total.  */
    val VRCompositor_ReprojectionAsync = 0x04

    inline fun CompositorFrameTiming(): CompositorFrameTiming = CompositorFrameTiming.create(ptr.advance(CompositorFrameTiming.SIZEOF))
    inline fun CompositorFrameTiming(capacity: Int): CompositorFrameTiming.Buffer = CompositorFrameTiming.create(ptr.advance(CompositorFrameTiming.SIZEOF * capacity), capacity)
    inline fun CompositorFrameTiming(block: CompositorFrameTiming.() -> Unit): CompositorFrameTiming = CompositorFrameTiming().also(block)

    inline fun CompositorCumulativeStats(): CompositorCumulativeStats = CompositorCumulativeStats.create(ptr.advance(CompositorCumulativeStats.SIZEOF))
    inline fun CompositorCumulativeStats(capacity: Int): CompositorCumulativeStats.Buffer = CompositorCumulativeStats.create(ptr.advance(CompositorCumulativeStats.SIZEOF * capacity), capacity)
    inline fun CompositorCumulativeStats(block: CompositorCumulativeStats.() -> Unit): CompositorCumulativeStats = CompositorCumulativeStats().also(block)

    const val IVRCompositor_Version = "IVRCompositor_022"


    // ivrnotifications.h

    const val notificationTextMaxSize = 256

    const val IVRNotifications_Version = "IVRNotifications_002"


    // ivroverlay.h

    /** The maximum length of an overlay key in bytes, counting the terminating null character. */
    const val vrOverlayMaxKeyLength = 128

    /** The maximum length of an overlay name in bytes, counting the terminating null character. */
    const val vrOverlayMaxNameLength = 128

    /** The maximum number of overlays that can exist in the system at one time. */
    const val maxOverlayCount = 64

    /** The maximum number of overlay intersection mask primitives per overlay */
    const val maxOverlayIntersectionMaskPrimitivesCount = 32

    inline fun VROverlayIntersectionParams(): VROverlayIntersectionParams = VROverlayIntersectionParams.create(ptr.advance(VROverlayIntersectionParams.SIZEOF))
    inline fun VROverlayIntersectionParams(capacity: Int): VROverlayIntersectionParams.Buffer = VROverlayIntersectionParams.create(ptr.advance(VROverlayIntersectionParams.SIZEOF * capacity), capacity)
    inline fun VROverlayIntersectionParams(block: VROverlayIntersectionParams.() -> Unit): VROverlayIntersectionParams = VROverlayIntersectionParams().also(block)

    inline fun VROverlayIntersectionResults(): VROverlayIntersectionResults = VROverlayIntersectionResults.create(ptr.advance(VROverlayIntersectionResults.SIZEOF))
    inline fun VROverlayIntersectionResults(capacity: Int): VROverlayIntersectionResults.Buffer = VROverlayIntersectionResults.create(ptr.advance(VROverlayIntersectionResults.SIZEOF * capacity), capacity)
    inline fun VROverlayIntersectionResults(block: VROverlayIntersectionResults.() -> Unit): VROverlayIntersectionResults = VROverlayIntersectionResults().also(block)

    inline fun IntersectionMaskRectangle(): IntersectionMaskRectangle = IntersectionMaskRectangle.create(ptr.advance(IntersectionMaskRectangle.SIZEOF))
    inline fun IntersectionMaskRectangle(capacity: Int): IntersectionMaskRectangle.Buffer = IntersectionMaskRectangle.create(ptr.advance(IntersectionMaskRectangle.SIZEOF * capacity), capacity)
    inline fun IntersectionMaskRectangle(block: IntersectionMaskRectangle.() -> Unit): IntersectionMaskRectangle = IntersectionMaskRectangle().also(block)

    inline fun IntersectionMaskCircle(): IntersectionMaskCircle = IntersectionMaskCircle.create(ptr.advance(IntersectionMaskCircle.SIZEOF))
    inline fun IntersectionMaskCircle(capacity: Int): IntersectionMaskCircle.Buffer = IntersectionMaskCircle.create(ptr.advance(IntersectionMaskCircle.SIZEOF * capacity), capacity)
    inline fun IntersectionMaskCircle(block: IntersectionMaskCircle.() -> Unit): IntersectionMaskCircle = IntersectionMaskCircle().also(block)

    inline fun VROverlayIntersectionMaskPrimitiveData(): VROverlayIntersectionMaskPrimitiveData = VROverlayIntersectionMaskPrimitiveData.create(ptr.advance(VROverlayIntersectionMaskPrimitiveData.SIZEOF))
    inline fun VROverlayIntersectionMaskPrimitiveData(capacity: Int): VROverlayIntersectionMaskPrimitiveData.Buffer = VROverlayIntersectionMaskPrimitiveData.create(ptr.advance(VROverlayIntersectionMaskPrimitiveData.SIZEOF * capacity), capacity)
    inline fun VROverlayIntersectionMaskPrimitiveData(block: VROverlayIntersectionMaskPrimitiveData.() -> Unit): VROverlayIntersectionMaskPrimitiveData = VROverlayIntersectionMaskPrimitiveData().also(block)

    inline fun VROverlayIntersectionMaskPrimitive(): VROverlayIntersectionMaskPrimitive = VROverlayIntersectionMaskPrimitive.create(ptr.advance(VROverlayIntersectionMaskPrimitive.SIZEOF))
    inline fun VROverlayIntersectionMaskPrimitive(capacity: Int): VROverlayIntersectionMaskPrimitive.Buffer = VROverlayIntersectionMaskPrimitive.create(ptr.advance(VROverlayIntersectionMaskPrimitive.SIZEOF * capacity), capacity)
    inline fun VROverlayIntersectionMaskPrimitive(block: VROverlayIntersectionMaskPrimitive.() -> Unit): VROverlayIntersectionMaskPrimitive = VROverlayIntersectionMaskPrimitive().also(block)

    const val IVROverlay_Version = "IVROverlay_018"

    // ivrrendermodels.h

    /** Canonical coordinate system of the gdc 2015 wired controller, provided for backwards compatibility */
    val Controller_Component_GDC2015 = "gdc2015"
    /** For controllers with an unambiguous 'base'. */
    val Controller_Component_Base = "base"
    /** For controllers with an unambiguous 'tip' (used for 'laser-pointing') */
    val Controller_Component_Tip = "tip"
    /** Neutral, ambidextrous hand-pose when holding controller. On plane between neutrally posed index finger and thumb */
    val Controller_Component_HandGrip = "handgrip"
    /** 1:1 aspect ratio status area, with canonical [0,1] uv mapping */
    val Controller_Component_Status = "status"


    inline fun RenderModelComponentState(): RenderModelComponentState = RenderModelComponentState.create(ptr.advance(RenderModelComponentState.SIZEOF))
    inline fun RenderModelComponentState(capacity: Int): RenderModelComponentState.Buffer = RenderModelComponentState.create(ptr.advance(RenderModelComponentState.SIZEOF * capacity), capacity)
    inline fun RenderModelComponentState(block: RenderModelComponentState.() -> Unit): RenderModelComponentState = RenderModelComponentState().also(block)

    inline fun RenderModel(): RenderModel = RenderModel.create(ptr.advance(RenderModel.SIZEOF))
    inline fun RenderModel(capacity: Int): RenderModel.Buffer = RenderModel.create(ptr.advance(RenderModel.SIZEOF * capacity), capacity)
    inline fun RenderModel(block: RenderModel.() -> Unit): RenderModel = RenderModel().also(block)

    inline fun RenderModelControllerModeState(): RenderModelControllerModeState = RenderModelControllerModeState.create(ptr.advance(RenderModelControllerModeState.SIZEOF))
    inline fun RenderModelControllerModeState(capacity: Int): RenderModelControllerModeState.Buffer = RenderModelControllerModeState.create(ptr.advance(RenderModelControllerModeState.SIZEOF * capacity), capacity)
    inline fun RenderModelControllerModeState(block: RenderModelControllerModeState.() -> Unit): RenderModelControllerModeState = RenderModelControllerModeState().also(block)

    const val IVRRenderModels_Version = "IVRRenderModels_005"

    // ivrextendeddisplay.h

    const val IVRExtendedDisplay_Version = "IVRExtendedDisplay_001"

    // ivrtrackedcamera.h

    const val IVRTrackedCamera_Version = "IVRTrackedCamera_003"

    // ivrscreenshots.h

    const val IVRScreenshots_Version = "IVRScreenshots_001"

    // ivrresources.h

    const val IVRResources_Version = "IVRResources_001"

    // ivrdrivermanager.h

    const val IVRDriverManager_Version = "IVRDriverManager_001"

    // ivrinput.h

    val invalidActionHandle: VRActionHandle = NULL
    val invalidActionSetHandle: VRActionSetHandle = NULL
    val invalidInputValueHandle: VRInputValueHandle = NULL

    val maxActionNameLength = 64
    val maxActionSetNameLength = 64
    val maxActionOriginCount = 16

    inline fun InputAnalogActionData(): InputAnalogActionData = InputAnalogActionData.create(ptr.advance(InputAnalogActionData.SIZEOF))
    inline fun InputAnalogActionData(capacity: Int): InputAnalogActionData.Buffer = InputAnalogActionData.create(ptr.advance(InputAnalogActionData.SIZEOF * capacity), capacity)
    inline fun InputAnalogActionData(block: InputAnalogActionData.() -> Unit): InputAnalogActionData = InputAnalogActionData().also(block)

    inline fun InputDigitalActionData(): InputDigitalActionData = InputDigitalActionData.create(ptr.advance(InputDigitalActionData.SIZEOF))
    inline fun InputDigitalActionData(capacity: Int): InputDigitalActionData.Buffer = InputDigitalActionData.create(ptr.advance(InputDigitalActionData.SIZEOF * capacity), capacity)
    inline fun InputDigitalActionData(block: InputDigitalActionData.() -> Unit): InputDigitalActionData = InputDigitalActionData().also(block)

    inline fun InputPoseActionData(): InputPoseActionData = InputPoseActionData.create(ptr.advance(InputPoseActionData.SIZEOF))
    inline fun InputPoseActionData(capacity: Int): InputPoseActionData.Buffer = InputPoseActionData.create(ptr.advance(InputPoseActionData.SIZEOF * capacity), capacity)
    inline fun InputPoseActionData(block: InputPoseActionData.() -> Unit): InputPoseActionData = InputPoseActionData().also(block)


    inline fun InputSkeletonActionData(): InputSkeletonActionData = InputSkeletonActionData.create(ptr.advance(InputSkeletonActionData.SIZEOF))
    inline fun InputSkeletonActionData(capacity: Int): InputSkeletonActionData.Buffer = InputSkeletonActionData.create(ptr.advance(InputSkeletonActionData.SIZEOF * capacity), capacity)
    inline fun InputSkeletonActionData(block: InputSkeletonActionData.() -> Unit): InputSkeletonActionData = InputSkeletonActionData().also(block)


    inline fun InputOriginInfo(): InputOriginInfo = InputOriginInfo.create(ptr.advance(InputOriginInfo.SIZEOF))
    inline fun InputOriginInfo(capacity: Int): InputOriginInfo.Buffer = InputOriginInfo.create(ptr.advance(InputOriginInfo.SIZEOF * capacity), capacity)
    inline fun InputOriginInfo(block: InputOriginInfo.() -> Unit): InputOriginInfo = InputOriginInfo().also(block)


    inline fun VRActiveActionSet(): VRActiveActionSet = VRActiveActionSet.create(ptr.advance(VRActiveActionSet.SIZEOF))
    inline fun VRActiveActionSet(capacity: Int): VRActiveActionSet.Buffer = VRActiveActionSet.create(ptr.advance(VRActiveActionSet.SIZEOF * capacity), capacity)
    inline fun VRActiveActionSet(block: VRActiveActionSet.() -> Unit): VRActiveActionSet = VRActiveActionSet().also(block)


    const val IVRInput_Version = "IVRInput_003"

    // ivriobuffer.h

    const val invalidIOBufferHandle = NULL

    const val IVRIOBuffer_Version = "IVRIOBuffer_001"

    /** Finds the active installation of the VR API and initializes it. The provided path must be absolute
     * or relative to the current working directory. These are the local install versions of the equivalent
     * functions in steamvr.h and will work without a local Steam install.
     *
     * This path is to the "root" of the VR API install. That's the directory with
     * the "drivers" directory and a platform (i.e. "win32") directory in it, not the directory with the DLL itself.
     *
     * startupInfo is reserved for future use.
     */
    infix fun init(applicationType: VRApplication): IVRSystem? = init(appBuffer.intBuffer, applicationType)

    fun init(error: IntBuffer, applicationType: VRApplication): IVRSystem? {

        var vrSystem: IVRSystem? = null

        val token = VR.VR_InitInternal(error, applicationType.i)
        OpenVR.create(token)

        if (error[0] == VRInitError.None.i)
            if (VR.VR_IsInterfaceVersionValid(IVRSystem_Version))
                vrSystem = OpenVR.VRSystem
            else {
                VR.VR_ShutdownInternal()
                error[0] = vr_.VRInitError.Init_InterfaceNotFound.i
            }

        return vrSystem
    }

    /** unloads vrclient.dll. Any interface pointers from the interface are invalid after this point */
    fun shutdown() = VR.VR_ShutdownInternal()

    /** Returns true if there is an HMD attached. This check is as lightweight as possible and
     *  can be called outside of VR_Init/VR_Shutdown. It should be used when an application wants
     *  to know if initializing VR is a possibility but isn't ready to take that step yet.     */
    val isHmdPresent: Boolean
        get() = VR.VR_IsHmdPresent()

    /** Returns true if the OpenVR runtime is installed. */
    val isRuntimeInstalled: Boolean
        get() = VR.VR_IsRuntimeInstalled()

    /** Returns where the OpenVR runtime is installed. */
    val runtimePath: String?
        get() = VR.VR_RuntimePath()

    /** Returns the name of the enum value for an EVRInitError. This function may be called outside of VR_Init()/VR_Shutdown().
     *  Also VRInitError::asSymbol */
    fun getVRInitErrorAsSymbol(error: VRInitError): String? = VR.VR_GetVRInitErrorAsSymbol(error.i)

    /** Returns an English string for an EVRInitError. Applications should call VR_GetVRInitErrorAsSymbol instead and
     * use that as a key to look up their own localized error message. This function may be called outside of VR_Init()/VR_Shutdown(). */
    fun getVRInitErrorAsEnglishDescription(error: VRInitError): String? = VR.VR_GetVRInitErrorAsEnglishDescription(error.i)

    /** Returns the interface of the specified version. This method must be called after VR_Init. The
     * pointer returned is valid until VR_Shutdown is called.     */
    fun getGenericInterface(interfaceVersion: String, error: IntBuffer): Long = VR.VR_GetGenericInterface(interfaceVersion, error)

    /** Returns whether the interface of the specified version exists.     */
    fun isInterfaceVersionValid(interfaceVersion: String): Boolean = VR.VR_IsInterfaceVersionValid(interfaceVersion)

    /** Returns a token that represents whether the VR interface handles need to be reloaded */
    val initToken: Int
        get () = VR.VR_GetInitToken()
}

var DEBUG = java.lang.management.ManagementFactory.getRuntimeMXBean().inputArguments.toString().indexOf("jdwp") >= 0

var OPENVR_NO_EXCEPTIONS = false

fun checkOpenVR(value: Int) {
    if (DEBUG && value != 0)
        if (OPENVR_NO_EXCEPTIONS)
            System.err.println("Error $value")
        else
            throw Error("Error $value")
}

val NUL = '\u0000'

val vrSystem = OpenVR.VRSystem!!
val vrChaperone = OpenVR.VRChaperone!!
val vrChaperoneSetup = OpenVR.VRChaperoneSetup!!
val vrCompositor = OpenVR.VRCompositor!!
val vrOverlay = OpenVR.VROverlay!!
val vrResources = OpenVR.VRResources!!
val vrRenderModels= OpenVR.VRRenderModels!!
val vrExtendedDisplay= OpenVR.VRExtendedDisplay!!
val vrSettings= OpenVR.VRSettings!!
val vrApplications= OpenVR.VRApplications!!
val vrTrackedCamera= OpenVR.VRTrackedCamera!!
val vrScreenshots= OpenVR.VRScreenshots!!
val vrDriverManager= OpenVR.VRDriverManager!!
val vrInput= OpenVR.VRInput!!
val vrioBuffer= OpenVR.VRIOBuffer!!

val assetPath = Paths.get("").toAbsolutePath().toString() + "/src/main/resources"