package vr_

import ab.appBuffer
import glm_.BYTES
import glm_.L
import glm_.buffer.cap
import glm_.i
import glm_.mat4x4.Mat4
import glm_.quat.Quat
import glm_.set
import glm_.vec2.Vec2
import glm_.vec2.Vec2i
import glm_.vec3.Vec3
import glm_.vec4.Vec4
import glm_.vec4.Vec4ub
import openvr.lib.ETrackingUniverseOrigin
import openvr.lib.EVREventType
import openvr.lib.VRMessageOverlayResponse
import org.lwjgl.openvr.*
import org.lwjgl.system.MemoryUtil.memPutFloat
import org.lwjgl.vulkan.*
import vkk.VkFormat
import vkk.VkImage
import vkk.VkSampleCount
import vkk.adr
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.ShortBuffer


var HmdMatrix34.m: FloatBuffer
    get() = HmdMatrix34.nm(adr)
    set(value) = HmdMatrix34.nm(adr, value)
val HmdMatrix34.asMat4: Mat4
    get() = Mat4(
            m[0], m[4], m[8], 0f,
            m[1], m[5], m[9], 0f,
            m[2], m[6], m[10], 0f,
            m[3], m[7], m[11], 1f)

infix fun HmdMatrix34.to(mat: Mat4) {
    mat.put(m[0], m[4], m[8], 0f,
            m[1], m[5], m[9], 0f,
            m[2], m[6], m[10], 0f,
            m[3], m[7], m[11], 1f)
}


var HmdMatrix44.m: FloatBuffer
    get() = HmdMatrix44.nm(adr)
    set(value) = HmdMatrix44.nm(adr, value)
val HmdMatrix44.asMat4: Mat4
    get() = Mat4(
            m[0], m[4], m[8], m[12],
            m[1], m[5], m[9], m[13],
            m[2], m[6], m[10], m[14],
            m[3], m[7], m[11], m[15])

infix fun HmdMatrix44.to(mat: Mat4) {
    mat.put(m[0], m[4], m[8], m[12],
            m[1], m[5], m[9], m[13],
            m[2], m[6], m[10], m[14],
            m[3], m[7], m[11], m[15])
}


var HmdVector3.x: Float
    get() = HmdVector3.nv(adr, 0)
    set(value) = HmdVector3.nv(adr, 0, value)
var HmdVector3.y: Float
    get() = HmdVector3.nv(adr, 1)
    set(value) = HmdVector3.nv(adr, 1, value)
var HmdVector3.z: Float
    get() = HmdVector3.nv(adr, 2)
    set(value) = HmdVector3.nv(adr, 2, value)


var HmdVector4.x: Float
    get() = HmdVector4.nv(adr, 0)
    set(value) = HmdVector4.nv(adr, 0, value)
var HmdVector4.y: Float
    get() = HmdVector4.nv(adr, 1)
    set(value) = HmdVector4.nv(adr, 1, value)
var HmdVector4.z: Float
    get() = HmdVector4.nv(adr, 2)
    set(value) = HmdVector4.nv(adr, 2, value)
var HmdVector4.w: Float
    get() = HmdVector4.nv(adr, 3)
    set(value) = HmdVector4.nv(adr, 3, value)


var HmdVector3d.x: Double
    get() = HmdVector3d.nv(adr, 0)
    set(value) = HmdVector3d.nv(adr, 0, value)
var HmdVector3d.y: Double
    get() = HmdVector3d.nv(adr, 1)
    set(value) = HmdVector3d.nv(adr, 1, value)
var HmdVector3d.z: Double
    get() = HmdVector3d.nv(adr, 2)
    set(value) = HmdVector3d.nv(adr, 2, value)


var HmdVector2.x: Float
    get() = HmdVector2.nv(adr, 0)
    set(value) = HmdVector2.nv(adr, 0, value)
var HmdVector2.y: Float
    get() = HmdVector2.nv(adr, 1)
    set(value) = HmdVector2.nv(adr, 1, value)
infix fun HmdVector2.to(vec2: Vec2) = vec2.put(x, y)


var HmdQuaternion.w: Double
    get() = HmdQuaternion.nw(adr)
    set(value) = HmdQuaternion.nw(adr, value)
var HmdQuaternion.x: Double
    get() = HmdQuaternion.nx(adr)
    set(value) = HmdQuaternion.nx(adr, value)
var HmdQuaternion.y: Double
    get() = HmdQuaternion.ny(adr)
    set(value) = HmdQuaternion.ny(adr, value)
var HmdQuaternion.z: Double
    get() = HmdQuaternion.nz(adr)
    set(value) = HmdQuaternion.nz(adr, value)


var HmdQuaternionf.w: Float
    get() = HmdQuaternionf.nw(adr)
    set(value) = HmdQuaternionf.nw(adr, value)
var HmdQuaternionf.x: Float
    get() = HmdQuaternionf.nx(adr)
    set(value) = HmdQuaternionf.nx(adr, value)
var HmdQuaternionf.y: Float
    get() = HmdQuaternionf.ny(adr)
    set(value) = HmdQuaternionf.ny(adr, value)
var HmdQuaternionf.z: Float
    get() = HmdQuaternionf.nz(adr)
    set(value) = HmdQuaternionf.nz(adr, value)


var HmdColor.r: Float
    get() = HmdColor.nr(adr)
    set(value) = HmdColor.nr(adr, value)
var HmdColor.g: Float
    get() = HmdColor.ng(adr)
    set(value) = HmdColor.ng(adr, value)
var HmdColor.b: Float
    get() = HmdColor.nb(adr)
    set(value) = HmdColor.nb(adr, value)
var HmdColor.a: Float
    get() = HmdColor.na(adr)
    set(value) = HmdColor.na(adr, value)


var HmdQuad.corners: Array<Vec3>
    get() {
        val vecs = HmdQuad.nvCorners(adr)
        return Array(vecs.capacity()) {
            Vec3(vecs[it].x, vecs[it].y, vecs[it].z)
        }
    }
    set(value) {
        val ofs = adr + HmdQuad.VCORNERS
        for(v in value) {
            memPutFloat(ofs, v.x)
            memPutFloat(ofs + Float.BYTES, v.y)
            memPutFloat(ofs + Float.BYTES * 2, v.z)
        }
    }


var HmdRect2.topLeft: HmdVector2
    get() = HmdRect2.nvTopLeft(adr)
    set(value) = HmdRect2.nvTopLeft(adr, value)
var HmdRect2.bottomRight: HmdVector2
    get() = HmdRect2.nvBottomRight(adr)
    set(value) = HmdRect2.nvBottomRight(adr, value)


val DistortionCoordinates.red: FloatBuffer
    get() = DistortionCoordinates.nrfRed(adr)
val DistortionCoordinates.green: FloatBuffer
    get() = DistortionCoordinates.nrfGreen(adr)
val DistortionCoordinates.blue: FloatBuffer
    get() = DistortionCoordinates.nrfBlue(adr)


var Texture.handle: TextureId
    get() = Texture.nhandle(adr).i
    set(value) = Texture.nhandle(adr, value.L)
var Texture.type: TextureType
    get() = TextureType of Texture.neType(adr)
    set(value) = Texture.neType(adr, value.i)
var Texture.colorSpace: ColorSpace
    get() = ColorSpace of Texture.neColorSpace(adr)
    set(value) = Texture.neColorSpace(adr, value.i)


var TrackedDevicePose.deviceToAbsoluteTracking: Mat4
    get() = TrackedDevicePose.nmDeviceToAbsoluteTracking(adr).asMat4
    set(value) {
        val ofs = adr + TrackedDevicePose.MDEVICETOABSOLUTETRACKING
        memPutFloat(ofs + Float.BYTES * 0, value[0, 0])
        memPutFloat(ofs + Float.BYTES * 1, value[1, 0])
        memPutFloat(ofs + Float.BYTES * 2, value[2, 0])
        memPutFloat(ofs + Float.BYTES * 3, value[3, 0])
        memPutFloat(ofs + Float.BYTES * 4, value[0, 1])
        memPutFloat(ofs + Float.BYTES * 5, value[1, 1])
        memPutFloat(ofs + Float.BYTES * 6, value[2, 1])
        memPutFloat(ofs + Float.BYTES * 7, value[3, 1])
        memPutFloat(ofs + Float.BYTES * 8, value[0, 2])
        memPutFloat(ofs + Float.BYTES * 9, value[1, 2])
        memPutFloat(ofs + Float.BYTES * 10, value[2, 2])
        memPutFloat(ofs + Float.BYTES * 11, value[3, 2])
    }
var TrackedDevicePose.velocity: HmdVector3
    get() = TrackedDevicePose.nvVelocity(adr)
    set(value) = TrackedDevicePose.nvVelocity(adr, value)
var TrackedDevicePose.angularVelocity: HmdVector3
    get() = TrackedDevicePose.nvAngularVelocity(adr)
    set(value) = TrackedDevicePose.nvAngularVelocity(adr, value)
var TrackedDevicePose.trackingResult: TrackingResult
    get() = TrackingResult of TrackedDevicePose.neTrackingResult(adr)
    set(value) = TrackedDevicePose.neTrackingResult(adr, value.i)
var TrackedDevicePose.poseIsValid: Boolean
    get() = TrackedDevicePose.nbPoseIsValid(adr)
    set(value) = TrackedDevicePose.nbPoseIsValid(adr, value)
var TrackedDevicePose.deviceIsConnected: Boolean
    get() = TrackedDevicePose.nbDeviceIsConnected(adr)
    set(value) = TrackedDevicePose.nbDeviceIsConnected(adr, value)


var VRTextureBounds.uMin: Float
    get() = VRTextureBounds.nuMin(adr)
    set(value) = VRTextureBounds.nuMin(adr, value)
var VRTextureBounds.vMin: Float
    get() = VRTextureBounds.nvMin(adr)
    set(value) = VRTextureBounds.nvMin(adr, value)
var VRTextureBounds.uMax: Float
    get() = VRTextureBounds.nuMax(adr)
    set(value) = VRTextureBounds.nuMax(adr, value)
var VRTextureBounds.vMax: Float
    get() = VRTextureBounds.nvMax(adr)
    set(value) = VRTextureBounds.nvMax(adr, value)


var VRTextureWithPose.deviceToAbsoluteTracking: HmdMatrix34
    get() = VRTextureWithPose.nmDeviceToAbsoluteTracking(adr)
    set(value) = VRTextureWithPose.nmDeviceToAbsoluteTracking(adr, value)


var VRTextureDepthInfo.handle: Long
    get() = VRTextureDepthInfo.nhandle(adr)
    set(value) = VRTextureDepthInfo.nhandle(adr, value)
var VRTextureDepthInfo.projection: HmdMatrix44
    get() = VRTextureDepthInfo.nmProjection(adr)
    set(value) = VRTextureDepthInfo.nmProjection(adr, value)
var VRTextureDepthInfo.range: HmdVector2
    get() = VRTextureDepthInfo.nvRange(adr)
    set(value) = VRTextureDepthInfo.nvRange(adr, value)


var VRTextureWithDepth.depth: VRTextureDepthInfo
    get() = VRTextureWithDepth.ndepth(adr)
    set(value) = VRTextureWithDepth.ndepth(adr, value)


var VRTextureWithPoseAndDepth.depth: VRTextureDepthInfo
    get() = VRTextureWithPoseAndDepth.ndepth(adr)
    set(value) = VRTextureWithPoseAndDepth.ndepth(adr, value)


var VRVulkanTextureData.image: VkImage
    get() = VRVulkanTextureData.nm_nImage(adr)
    set(value) = VRVulkanTextureData.nm_nImage(adr, value)

fun VRVulkanTextureData.device(physicalDevice: VkPhysicalDevice, ci: VkDeviceCreateInfo): VkDevice {
    return VkDevice(VRVulkanTextureData.nm_pDevice(adr), physicalDevice, ci)
}

fun VRVulkanTextureData.setDevice(device: VkDevice) = VRVulkanTextureData.nm_pDevice(adr, device.adr)

fun VRVulkanTextureData.physicalDevice(instance: VkInstance): VkPhysicalDevice {
    return VkPhysicalDevice(VRVulkanTextureData.nm_pPhysicalDevice(adr), instance)
}

fun VRVulkanTextureData.setPhysicalDevice(physicalDevice: VkPhysicalDevice) = VRVulkanTextureData.nm_pPhysicalDevice(adr)

fun VRVulkanTextureData.instance(ci: VkInstanceCreateInfo): VkInstance {
    return VkInstance(VRVulkanTextureData.nm_pInstance(adr), ci)
}

fun VRVulkanTextureData.setInstance(instance: VkInstance) = VRVulkanTextureData.nm_pInstance(adr)

fun VRVulkanTextureData.queue(device: VkDevice): VkQueue {
    return VkQueue(VRVulkanTextureData.nm_pQueue(adr), device)
}

fun VRVulkanTextureData.setQueue(queue: VkQueue) = VRVulkanTextureData.nm_pQueue(adr)

var VRVulkanTextureData.queueFamilyIndex: Int
    get() = VRVulkanTextureData.nm_nQueueFamilyIndex(adr)
    set(value) = VRVulkanTextureData.nm_nQueueFamilyIndex(adr, value)
/** JVM custom */
var VRVulkanTextureData.size: Vec2i
    get() = Vec2i(VRVulkanTextureData.nm_nWidth(adr), VRVulkanTextureData.nm_nHeight(adr))
    set(value) {
        VRVulkanTextureData.nm_nWidth(adr, value.x)
        VRVulkanTextureData.nm_nHeight(adr, value.y)
    }
var VRVulkanTextureData.format: VkFormat
    get() = VkFormat of VRVulkanTextureData.nm_nFormat(adr)
    set(value) = VRVulkanTextureData.nm_nFormat(adr, value.i)
var VRVulkanTextureData.sampleCount: VkSampleCount
    get() = VkSampleCount of VRVulkanTextureData.nm_nSampleCount(adr)
    set(value) = VRVulkanTextureData.nm_nSampleCount(adr, value.i)


//var D3D12TextureData.sampleCount
//    get() = VRVulkanTextureData.nm_nSampleCount(adr)
//    set(value) = VRVulkanTextureData.nm_nSampleCount(adr, value)


val VREventController.button: EVRButtonId
    get() = EVRButtonId of VREventController.nbutton(adr)

/** JVM custom */
val VREventMouse.pos: Vec2
    get() = Vec2(VREventMouse.nx(adr), VREventMouse.ny(adr))
val VREventMouse.button: EVRMouseButton
    get() = EVRMouseButton of VREventMouse.nbutton(adr)


val VREventScroll.delta: Vec2
    get() = Vec2(VREventScroll.nxdelta(adr), VREventScroll.nydelta(adr))
val VREventScroll.repeatCount: Int
    get() = VREventScroll.nrepeatCount(adr)


val VREventTouchPadMove.fingerDown: Boolean
    get() = VREventTouchPadMove.nbFingerDown(adr)
val VREventTouchPadMove.secondsFingerDown: Float
    get() = VREventTouchPadMove.nflSecondsFingerDown(adr)
val VREventTouchPadMove.valueFirst: Vec2
    get() = Vec2(VREventTouchPadMove.nfValueXFirst(adr), VREventTouchPadMove.nfValueYFirst(adr))
val VREventTouchPadMove.valueRaw: Vec2
    get() = Vec2(VREventTouchPadMove.nfValueXRaw(adr), VREventTouchPadMove.nfValueYRaw(adr))


val VREventNotification.userValue: Long
    get() = VREventNotification.nulUserValue(adr)
val VREventNotification.notificationId: Int
    get() = VREventNotification.nnotificationId(adr)


val VREventProcess.pid: Int
    get() = VREventProcess.npid(adr)
val VREventProcess.oldPid: Int
    get() = VREventProcess.noldPid(adr)
val VREventProcess.forced: Boolean
    get() = VREventProcess.nbForced(adr)


val VREventOverlay.overlayHandle: Long
    get() = VREventOverlay.noverlayHandle(adr)
val VREventOverlay.devicePath: Long
    get() = VREventOverlay.ndevicePath(adr)


val VREventStatus.statusState: EVRState
    get() = EVRState of VREventStatus.nstatusState(adr)


val VREventKeyboard.newInput: String
    get() {
        val ni = VREventKeyboard.ncNewInput(adr)
        val chars = ByteArray(ni.cap) { ni[it] }
        return String(chars)
    }
val VREventKeyboard.userValue: Long
    get() = VREventKeyboard.nuUserValue(adr)


val VREventIpd.ipdMeters: Float
    get() = VREventIpd.nipdMeters(adr)


val VREventChaperone.previousUniverse: Long
    get() = VREventChaperone.nm_nPreviousUniverse(adr)
val VREventChaperone.currentUniverse: Long
    get() = VREventChaperone.nm_nCurrentUniverse(adr)


val VREventReserved.reserved0: Long
    get() = VREventReserved.nreserved0(adr)
val VREventReserved.reserved1: Long
    get() = VREventReserved.nreserved1(adr)
val VREventReserved.reserved2: Long
    get() = VREventReserved.nreserved2(adr)
val VREventReserved.reserved3: Long
    get() = VREventReserved.nreserved3(adr)


val VREventPerformanceTest.fidelityLevel: Int
    get() = VREventPerformanceTest.nm_nFidelityLevel(adr)


val VREventSeatedZeroPoseReset.resetBySystemMenu: Boolean
    get() = VREventSeatedZeroPoseReset.nbResetBySystemMenu(adr)


val VREventScreenshot.handle: Int
    get() = VREventScreenshot.nhandle(adr)
val VREventScreenshot.type: Int
    get() = VREventScreenshot.ntype(adr)


val VREventScreenshotProgress.progress: Float
    get() = VREventScreenshotProgress.nprogress(adr)


val VREventApplicationLaunch.pid: Int
    get() = VREventApplicationLaunch.npid(adr)
val VREventApplicationLaunch.argsHandle: Int
    get() = VREventApplicationLaunch.nunArgsHandle(adr)


val VREventEditingCameraSurface.overlayHandle: Long
    get() = VREventEditingCameraSurface.noverlayHandle(adr)
val VREventEditingCameraSurface.visualMode: Int
    get() = VREventEditingCameraSurface.nnVisualMode(adr)


val VREventMessageOverlay.response: VRMessageOverlayResponse
    get() = VRMessageOverlayResponse of VREventMessageOverlay.nunVRMessageOverlayResponse(adr)


val VREventProperty.container: PropertyContainerHandle
    get() = VREventProperty.ncontainer(adr)
val VREventProperty.prop: TrackedDeviceProperty
    get() = TrackedDeviceProperty of VREventProperty.nprop(adr)


val VREventHapticVibration.containerHandle: Long
    get() = VREventHapticVibration.ncontainerHandle(adr)
val VREventHapticVibration.componentHandle: Long
    get() = VREventHapticVibration.ncomponentHandle(adr)
val VREventHapticVibration.durationSeconds: Float
    get() = VREventHapticVibration.nfDurationSeconds(adr)
val VREventHapticVibration.frequency: Float
    get() = VREventHapticVibration.nfFrequency(adr)
val VREventHapticVibration.amplitude: Float
    get() = VREventHapticVibration.nfAmplitude(adr)


val VREventWebConsole.handle: WebConsoleHandle
    get() = VREventWebConsole.nwebConsoleHandle(adr)


val VREventInputBindingLoad.appContainer: PropertyContainerHandle
    get() = VREventInputBindingLoad.nulAppContainer(adr)
val VREventInputBindingLoad.pathMessage: Long
    get() = VREventInputBindingLoad.npathMessage(adr)
val VREventInputBindingLoad.pathUrl: Long
    get() = VREventInputBindingLoad.npathUrl(adr)


val VREvent.eventType: EVREventType
    get() = EVREventType of VREvent.neventType(adr)
val VREvent.trackedDeviceIndex: TrackedDeviceIndex
    get() = VREvent.ntrackedDeviceIndex(adr)
val VREvent.eventAgeSeconds: Float
    get() = VREvent.neventAgeSeconds(adr)
/** event data must be the end of the struct as its size is variable */
val VREvent.data: VREventData
    get() = VREvent.ndata(adr)


val HiddenAreaMesh.data: HmdVector2.Buffer?
    get() = HiddenAreaMesh.npVertexData(adr)
val HiddenAreaMesh.triangleCount: Int
    get() = HiddenAreaMesh.nunTriangleCount(adr)


var VRControllerAxis.pos: Vec2
    get() = Vec2(VRControllerAxis.nx(adr), VRControllerAxis.ny(adr))
    set(value) {
        VRControllerAxis.nx(adr, value.x)
        VRControllerAxis.ny(adr, value.y)
    }


var VRControllerState.packetNum: Int
    get() = VRControllerState.nunPacketNum(adr)
    set(value) = VRControllerState.nunPacketNum(adr, value)
var VRControllerState.buttonPressed: Long
    get() = VRControllerState.nulButtonPressed(adr)
    set(value) = VRControllerState.nulButtonPressed(adr, value)
var VRControllerState.buttonTouched: Long
    get() = VRControllerState.nulButtonTouched(adr)
    set(value) = VRControllerState.nulButtonTouched(adr, value)
var VRControllerState.axis: VRControllerAxis.Buffer
    get() = VRControllerState.nrAxis(adr)
    set(value) = VRControllerState.nrAxis(adr, value)


var CompositorOverlaySettings.size: Int
    get() = CompositorOverlaySettings.nsize(adr)
    set(value) = CompositorOverlaySettings.nsize(adr, value)
var CompositorOverlaySettings.curved: Boolean
    get() = CompositorOverlaySettings.ncurved(adr)
    set(value) = CompositorOverlaySettings.ncurved(adr, value)
var CompositorOverlaySettings.antialias: Boolean
    get() = CompositorOverlaySettings.nantialias(adr)
    set(value) = CompositorOverlaySettings.nantialias(adr, value)
var CompositorOverlaySettings.scale: Float
    get() = CompositorOverlaySettings.nuScale(adr)
    set(value) = CompositorOverlaySettings.nuScale(adr, value)
var CompositorOverlaySettings.distance: Float
    get() = CompositorOverlaySettings.ndistance(adr)
    set(value) = CompositorOverlaySettings.ndistance(adr, value)
var CompositorOverlaySettings.alpha: Float
    get() = CompositorOverlaySettings.nalpha(adr)
    set(value) = CompositorOverlaySettings.nalpha(adr, value)
var CompositorOverlaySettings.uvOffset: Vec2
    get() = Vec2(CompositorOverlaySettings.nuOffset(adr), CompositorOverlaySettings.nvOffset(adr))
    set(value) {
        CompositorOverlaySettings.nuOffset(adr, value.x)
        CompositorOverlaySettings.nvOffset(adr, value.y)
    }
var CompositorOverlaySettings.uvScale: Vec2
    get() = Vec2(CompositorOverlaySettings.nuScale(adr), CompositorOverlaySettings.nvScale(adr))
    set(value) {
        CompositorOverlaySettings.nuScale(adr, value.x)
        CompositorOverlaySettings.nvScale(adr, value.y)
    }
var CompositorOverlaySettings.gridDivs: Float
    get() = CompositorOverlaySettings.ngridDivs(adr)
    set(value) = CompositorOverlaySettings.ngridDivs(adr, value)
var CompositorOverlaySettings.gridWidth: Float
    get() = CompositorOverlaySettings.ngridWidth(adr)
    set(value) = CompositorOverlaySettings.ngridWidth(adr, value)
var CompositorOverlaySettings.gridScale: Float
    get() = CompositorOverlaySettings.ngridScale(adr)
    set(value) = CompositorOverlaySettings.ngridScale(adr, value)
var CompositorOverlaySettings.transform: Mat4
    get() = CompositorOverlaySettings.ntransform(adr).asMat4
    set(value) {
        val ofs = adr + CompositorOverlaySettings.TRANSFORM
        memPutFloat(ofs + Float.BYTES * 0, value[0, 0])
        memPutFloat(ofs + Float.BYTES * 1, value[1, 0])
        memPutFloat(ofs + Float.BYTES * 2, value[2, 0])
        memPutFloat(ofs + Float.BYTES * 3, value[3, 0])
        memPutFloat(ofs + Float.BYTES * 4, value[0, 1])
        memPutFloat(ofs + Float.BYTES * 5, value[1, 1])
        memPutFloat(ofs + Float.BYTES * 6, value[2, 1])
        memPutFloat(ofs + Float.BYTES * 7, value[3, 1])
        memPutFloat(ofs + Float.BYTES * 8, value[0, 2])
        memPutFloat(ofs + Float.BYTES * 9, value[1, 2])
        memPutFloat(ofs + Float.BYTES * 10, value[2, 2])
        memPutFloat(ofs + Float.BYTES * 11, value[3, 2])
        memPutFloat(ofs + Float.BYTES * 12, value[0, 3])
        memPutFloat(ofs + Float.BYTES * 13, value[1, 3])
        memPutFloat(ofs + Float.BYTES * 14, value[2, 3])
        memPutFloat(ofs + Float.BYTES * 15, value[3, 3])
    }


val VRBoneTransform.position: Vec4
    get() = VRBoneTransform.`nposition$`(adr).let { Vec4(it.x, it.y, it.z, it.w) }
val VRBoneTransform.orientation: Quat
    get() = VRBoneTransform.norientation(adr).let { Quat(it.w, it.x, it.y, it.z) }


val CameraVideoStreamFrameHeader.frameType: EVRTrackedCameraFrameType
    get() = EVRTrackedCameraFrameType of CameraVideoStreamFrameHeader.neFrameType(adr)
val CameraVideoStreamFrameHeader.resolution: Vec2i
    get() = Vec2i(CameraVideoStreamFrameHeader.nnWidth(adr), CameraVideoStreamFrameHeader.nnHeight(adr))
val CameraVideoStreamFrameHeader.bytePerPixel: Int
    get() = CameraVideoStreamFrameHeader.nnBytesPerPixel(adr)
val CameraVideoStreamFrameHeader.frameSequence: Int
    get() = CameraVideoStreamFrameHeader.nnFrameSequence(adr)
val CameraVideoStreamFrameHeader.standingTrackedDevicePos: TrackedDevicePose
    get() = CameraVideoStreamFrameHeader.nstandingTrackedDevicePose(adr)


val DriverDirectModeFrameTiming.size: Int
    get() = DriverDirectModeFrameTiming.nm_nSize(adr)
val DriverDirectModeFrameTiming.numFramePresents: Int
    get() = DriverDirectModeFrameTiming.nm_nNumFramePresents(adr)
val DriverDirectModeFrameTiming.numMisPresented: Int
    get() = DriverDirectModeFrameTiming.nm_nNumMisPresented(adr)
val DriverDirectModeFrameTiming.numDroppedFrames: Int
    get() = DriverDirectModeFrameTiming.nm_nNumDroppedFrames(adr)
val DriverDirectModeFrameTiming.reprojectionFlags: Int
    get() = DriverDirectModeFrameTiming.nm_nReprojectionFlags(adr)


val ImuSample.sampleTime: Double
    get() = ImuSample.nfSampleTime(adr)
val ImuSample.accel: Vec3
    get() = ImuSample.nvAccel(adr).let { Vec3(it.x, it.y, it.z) }
val ImuSample.gyro: Vec3
    get() = ImuSample.nvGyro(adr).let { Vec3(it.x, it.y, it.z) }
val ImuSample.offScaleFlags: Int
    get() = ImuSample.nunOffScaleFlags(adr)

// ivrsystem.h


// ivrapplications.h

var AppOverrideKeys.key: String
    get() = pchKeyString()
    set(value) {
        val encoded = appBuffer.bufferOfAscii(value)
        AppOverrideKeys.npchKey(adr, encoded)
    }
var AppOverrideKeys.value: String
    get() = TODO()//pchString()
    set(value) = TODO()

// ivrsettings.h

// ivrchaperone.h

// ivrchaperonesetup.h

// ivrcompositor.h ================================================================================================================================================

val CompositorFrameTiming.size: Int
    get() = CompositorFrameTiming.nm_nSize(adr)
val CompositorFrameTiming.frameIndex: Int
    get() = CompositorFrameTiming.nm_nFrameIndex(adr)
val CompositorFrameTiming.numFramePresents: Int
    get() = CompositorFrameTiming.nm_nNumFramePresents(adr)
val CompositorFrameTiming.numMisPresented: Int
    get() = CompositorFrameTiming.nm_nNumMisPresented(adr)
val CompositorFrameTiming.numDroppedFrames: Int
    get() = CompositorFrameTiming.nm_nNumDroppedFrames(adr)
val CompositorFrameTiming.reprojectionFlags: Int
    get() = CompositorFrameTiming.nm_nReprojectionFlags(adr)
val CompositorFrameTiming.systemTimeInSeconds: Double
    get() = CompositorFrameTiming.nm_flSystemTimeInSeconds(adr)
val CompositorFrameTiming.preSubmitGpuMs: Float
    get() = CompositorFrameTiming.nm_flPreSubmitGpuMs(adr)
val CompositorFrameTiming.postSubmitGpuMs: Float
    get() = CompositorFrameTiming.nm_flPostSubmitGpuMs(adr)
val CompositorFrameTiming.totalRenderGpuMs: Float
    get() = CompositorFrameTiming.nm_flTotalRenderGpuMs(adr)
val CompositorFrameTiming.compositorRenderGpuMs: Float
    get() = CompositorFrameTiming.nm_flCompositorRenderGpuMs(adr)
val CompositorFrameTiming.compositorRenderCpuMs: Float
    get() = CompositorFrameTiming.nm_flCompositorRenderCpuMs(adr)
val CompositorFrameTiming.compositorIdleCpuMs: Float
    get() = CompositorFrameTiming.nm_flCompositorIdleCpuMs(adr)
val CompositorFrameTiming.clientFrameIntervalMs: Float
    get() = CompositorFrameTiming.nm_flClientFrameIntervalMs(adr)
val CompositorFrameTiming.presentCallCpuMs: Float
    get() = CompositorFrameTiming.nm_flPresentCallCpuMs(adr)
val CompositorFrameTiming.waitForPresentCpuMs: Float
    get() = CompositorFrameTiming.nm_flWaitForPresentCpuMs(adr)
val CompositorFrameTiming.submitFrameMs: Float
    get() = CompositorFrameTiming.nm_flSubmitFrameMs(adr)
val CompositorFrameTiming.waitGetPosesCalledMs: Float
    get() = CompositorFrameTiming.nm_flWaitGetPosesCalledMs(adr)
val CompositorFrameTiming.newPosesReadyMs: Float
    get() = CompositorFrameTiming.nm_flNewPosesReadyMs(adr)
val CompositorFrameTiming.newFrameReadyMs: Float
    get() = CompositorFrameTiming.nm_flNewFrameReadyMs(adr)
val CompositorFrameTiming.compositorUpdateStartMs: Float
    get() = CompositorFrameTiming.nm_flCompositorUpdateStartMs(adr)
val CompositorFrameTiming.compositorUpdateEndMs: Float
    get() = CompositorFrameTiming.nm_flCompositorUpdateEndMs(adr)
val CompositorFrameTiming.compositorRenderStartMs: Float
    get() = CompositorFrameTiming.nm_flCompositorRenderStartMs(adr)
val CompositorFrameTiming.hmdPose: TrackedDevicePose
    get() = CompositorFrameTiming.nm_HmdPose(adr)


val CompositorCumulativeStats.pid: Int
    get() = CompositorCumulativeStats.nm_nPid(adr)
val CompositorCumulativeStats.numFramePresents: Int
    get() = CompositorCumulativeStats.nm_nNumFramePresents(adr)
val CompositorCumulativeStats.numDroppedFrames: Int
    get() = CompositorCumulativeStats.nm_nNumDroppedFrames(adr)
val CompositorCumulativeStats.numReprojectedFrames: Int
    get() = CompositorCumulativeStats.nm_nNumReprojectedFrames(adr)
val CompositorCumulativeStats.numFramePresentsOnStartup: Int
    get() = CompositorCumulativeStats.nm_nNumFramePresentsOnStartup(adr)
val CompositorCumulativeStats.numDroppedFramesOnStartup: Int
    get() = CompositorCumulativeStats.nm_nNumDroppedFramesOnStartup(adr)
val CompositorCumulativeStats.numReprojectedFramesOnStartup: Int
    get() = CompositorCumulativeStats.nm_nNumReprojectedFramesOnStartup(adr)
val CompositorCumulativeStats.numLoading: Int
    get() = CompositorCumulativeStats.nm_nNumLoading(adr)
val CompositorCumulativeStats.numFramePresentsLoading: Int
    get() = CompositorCumulativeStats.nm_nNumFramePresentsLoading(adr)
val CompositorCumulativeStats.numDroppedFramesLoading: Int
    get() = CompositorCumulativeStats.nm_nNumDroppedFramesLoading(adr)
val CompositorCumulativeStats.numReprojectedFramesLoading: Int
    get() = CompositorCumulativeStats.nm_nNumReprojectedFramesLoading(adr)
val CompositorCumulativeStats.numTimedOut: Int
    get() = CompositorCumulativeStats.nm_nNumTimedOut(adr)
val CompositorCumulativeStats.numFramePresentsTimedOut: Int
    get() = CompositorCumulativeStats.nm_nNumFramePresentsTimedOut(adr)
val CompositorCumulativeStats.numDroppedFramesTimedOut: Int
    get() = CompositorCumulativeStats.nm_nNumDroppedFramesTimedOut(adr)
val CompositorCumulativeStats.numReprojectedFramesTimedOut: Int
    get() = CompositorCumulativeStats.nm_nNumReprojectedFramesTimedOut(adr)


// ivrnotifications.h

var NotificationBitmap.imageData: ByteBuffer
    get() = NotificationBitmap.nm_pImageData(adr, width * height * bytesPerPixel)
    set(value) = NotificationBitmap.nm_pImageData(adr, value)
/** JVM custom */
var NotificationBitmap.size: Vec2i
    get() = Vec2i(NotificationBitmap.nm_nWidth(adr), NotificationBitmap.nm_nHeight(adr))
    set(value) {
        NotificationBitmap.nm_nWidth(adr, value.x)
        NotificationBitmap.nm_nHeight(adr, value.y)
    }
var NotificationBitmap.width: Int
    get() = NotificationBitmap.nm_nWidth(adr)
    set(value) = NotificationBitmap.nm_nWidth(adr, value)
var NotificationBitmap.height: Int
    get() = NotificationBitmap.nm_nHeight(adr)
    set(value) = NotificationBitmap.nm_nWidth(adr, value)
var NotificationBitmap.bytesPerPixel: Int
    get() = NotificationBitmap.nm_nBytesPerPixel(adr)
    set(value) = NotificationBitmap.nm_nBytesPerPixel(adr, value)


// ivroverlay.h

var VROverlayIntersectionParams.source: Vec3
    get() = VROverlayIntersectionParams.nvSource(adr).let { Vec3(it.x, it.y, it.z) }
    set(value) {
        memPutFloat(adr + VROverlayIntersectionParams.VSOURCE, value.x)
        memPutFloat(adr + VROverlayIntersectionParams.VSOURCE + Float.BYTES, value.y)
        memPutFloat(adr + VROverlayIntersectionParams.VSOURCE + Float.BYTES * 2, value.z)
    }
var VROverlayIntersectionParams.direction: Vec3
    get() = VROverlayIntersectionParams.nvDirection(adr).let { Vec3(it.x, it.y, it.z) }
    set(value) {
        memPutFloat(adr + VROverlayIntersectionParams.VDIRECTION, value.x)
        memPutFloat(adr + VROverlayIntersectionParams.VDIRECTION + Float.BYTES, value.y)
        memPutFloat(adr + VROverlayIntersectionParams.VDIRECTION + Float.BYTES * 2, value.z)
    }
var VROverlayIntersectionParams.origin: ETrackingUniverseOrigin
    get() = ETrackingUniverseOrigin of VROverlayIntersectionParams.neOrigin(adr)
    set(value) = VROverlayIntersectionParams.neOrigin(adr, value.i)


val VROverlayIntersectionResults.point: Vec3
    get() = VROverlayIntersectionResults.nvPoint(adr).let { Vec3(it.x, it.y, it.z) }
val VROverlayIntersectionResults.normal: Vec3
    get() = VROverlayIntersectionResults.nvNormal(adr).let { Vec3(it.x, it.y, it.z) }
val VROverlayIntersectionResults.uv: Vec2
    get() = VROverlayIntersectionResults.nvNormal(adr).let { Vec2(it.x, it.y) }
val VROverlayIntersectionResults.distance: Float
    get() = VROverlayIntersectionResults.nfDistance(adr)


var IntersectionMaskRectangle.topLeftX: Float
    get() = IntersectionMaskRectangle.nm_flTopLeftX(adr)
    set(value) = IntersectionMaskRectangle.nm_flTopLeftX(adr, value)
var IntersectionMaskRectangle.topLeftY: Float
    get() = IntersectionMaskRectangle.nm_flTopLeftY(adr)
    set(value) = IntersectionMaskRectangle.nm_flTopLeftY(adr, value)
/** JVM custom */
var IntersectionMaskRectangle.topLeft: Vec2
    get() = Vec2(IntersectionMaskRectangle.nm_flTopLeftX(adr), IntersectionMaskRectangle.nm_flTopLeftY(adr))
    set(value) {
        IntersectionMaskRectangle.nm_flTopLeftX(adr, value.x)
        IntersectionMaskRectangle.nm_flTopLeftY(adr, value.y)
    }
var IntersectionMaskRectangle.width: Float
    get() = IntersectionMaskRectangle.nm_flWidth(adr)
    set(value) = IntersectionMaskRectangle.nm_flWidth(adr, value)
var IntersectionMaskRectangle.height: Float
    get() = IntersectionMaskRectangle.nm_flHeight(adr)
    set(value) = IntersectionMaskRectangle.nm_flHeight(adr, value)
/** JVM custom */
var IntersectionMaskRectangle.size: Vec2
    get() = Vec2(IntersectionMaskRectangle.nm_flWidth(adr), IntersectionMaskRectangle.nm_flHeight(adr))
    set(value) {
        IntersectionMaskRectangle.nm_flWidth(adr, value.x)
        IntersectionMaskRectangle.nm_flHeight(adr, value.y)
    }



var IntersectionMaskCircle.centerX: Float
    get() = IntersectionMaskCircle.nm_flCenterX(adr)
    set(value) = IntersectionMaskCircle.nm_flCenterX(adr, value)
var IntersectionMaskCircle.centerY: Float
    get() = IntersectionMaskCircle.nm_flCenterY(adr)
    set(value) = IntersectionMaskCircle.nm_flCenterY(adr, value)
/** JVM custom */
var IntersectionMaskCircle.center: Vec2
    get() = Vec2(IntersectionMaskCircle.nm_flCenterX(adr), IntersectionMaskCircle.nm_flCenterY(adr))
    set(value) {
        IntersectionMaskCircle.nm_flCenterX(adr, value.x)
        IntersectionMaskCircle.nm_flCenterY(adr, value.y)
    }
var IntersectionMaskCircle.radius: Float
    get() = IntersectionMaskCircle.nm_flRadius(adr)
    set(value) = IntersectionMaskCircle.nm_flRadius(adr, value)


var VROverlayIntersectionMaskPrimitiveData.rectangle : IntersectionMaskRectangle
    get() = VROverlayIntersectionMaskPrimitiveData.nm_Rectangle(adr)
    set(value) = VROverlayIntersectionMaskPrimitiveData.nm_Rectangle(adr, value)
var VROverlayIntersectionMaskPrimitiveData.circle : IntersectionMaskCircle
    get() = VROverlayIntersectionMaskPrimitiveData.nm_Circle(adr)
    set(value) = VROverlayIntersectionMaskPrimitiveData.nm_Circle(adr, value)


var VROverlayIntersectionMaskPrimitive.primitiveType : EVROverlayIntersectionMaskPrimitiveType
    get() = EVROverlayIntersectionMaskPrimitiveType of VROverlayIntersectionMaskPrimitive.nm_nPrimitiveType(adr)
    set(value) = VROverlayIntersectionMaskPrimitive.nm_nPrimitiveType(adr, value.i)
var VROverlayIntersectionMaskPrimitive.primitive : VROverlayIntersectionMaskPrimitiveData
    get() = VROverlayIntersectionMaskPrimitive.nm_Primitive(adr)
    set(value) = VROverlayIntersectionMaskPrimitive.nm_Primitive(adr, value)


// ivrrendermodels.h


var RenderModelComponentState.trackingToComponentRenderModel : Mat4
    get() = RenderModelComponentState.nmTrackingToComponentRenderModel(adr).asMat4
    set(value) {
        val ofs = adr + RenderModelComponentState.MTRACKINGTOCOMPONENTRENDERMODEL
        memPutFloat(ofs + Float.BYTES * 0, value[0, 0])
        memPutFloat(ofs + Float.BYTES * 1, value[1, 0])
        memPutFloat(ofs + Float.BYTES * 2, value[2, 0])
        memPutFloat(ofs + Float.BYTES * 3, value[3, 0])
        memPutFloat(ofs + Float.BYTES * 4, value[0, 1])
        memPutFloat(ofs + Float.BYTES * 5, value[1, 1])
        memPutFloat(ofs + Float.BYTES * 6, value[2, 1])
        memPutFloat(ofs + Float.BYTES * 7, value[3, 1])
        memPutFloat(ofs + Float.BYTES * 8, value[0, 2])
        memPutFloat(ofs + Float.BYTES * 9, value[1, 2])
        memPutFloat(ofs + Float.BYTES * 10, value[2, 2])
        memPutFloat(ofs + Float.BYTES * 11, value[3, 2])
    }
var RenderModelComponentState.mTrackingToComponentLocal : Mat4
    get() = RenderModelComponentState.nmTrackingToComponentLocal(adr).asMat4
    set(value) {
        val ofs = adr + RenderModelComponentState.MTRACKINGTOCOMPONENTLOCAL
        memPutFloat(ofs + Float.BYTES * 0, value[0, 0])
        memPutFloat(ofs + Float.BYTES * 1, value[1, 0])
        memPutFloat(ofs + Float.BYTES * 2, value[2, 0])
        memPutFloat(ofs + Float.BYTES * 3, value[3, 0])
        memPutFloat(ofs + Float.BYTES * 4, value[0, 1])
        memPutFloat(ofs + Float.BYTES * 5, value[1, 1])
        memPutFloat(ofs + Float.BYTES * 6, value[2, 1])
        memPutFloat(ofs + Float.BYTES * 7, value[3, 1])
        memPutFloat(ofs + Float.BYTES * 8, value[0, 2])
        memPutFloat(ofs + Float.BYTES * 9, value[1, 2])
        memPutFloat(ofs + Float.BYTES * 10, value[2, 2])
        memPutFloat(ofs + Float.BYTES * 11, value[3, 2])
    }
val RenderModelComponentState.properties : VRComponentProperties
    get() = RenderModelComponentState.nuProperties(adr)


/** position in meters in device space */
var RenderModelVertex.position: Vec3
    get() = RenderModelVertex.nvPosition(adr).let { Vec3(it.x, it.y, it.z) }
    set(value) {
        RenderModelVertex.nvPosition(adr).apply {
            x = value.x
            y = value.y
            z = value.z
        }
    }
var RenderModelVertex.normal: Vec3
    get() = RenderModelVertex.nvNormal(adr).let { Vec3(it.x, it.y, it.z) }
    set(value) {
        RenderModelVertex.nvNormal(adr).apply {
            x = value.x
            y = value.y
            z = value.z
        }
    }
var RenderModelVertex.textureCoord: Vec2
    get() = RenderModelVertex.nrfTextureCoord(adr).let { Vec2(it) }
    set(value) {
        RenderModelVertex.nrfTextureCoord(adr).apply {
            set(0, value.x)
            set(1, value.y)
        }
    }

/** texture map size in pixels */
val RenderModelTextureMap.width: Int
    get() = RenderModelTextureMap.nunWidth(adr).i
val RenderModelTextureMap.height: Int
    get() = RenderModelTextureMap.nunHeight(adr).i
val RenderModelTextureMap.size: Vec2i
    get() = Vec2i(width, height)
/** Map texture data. All textures are RGBA with 8 bits per channel per pixel. Data size is width * height * 4ub */
val RenderModelTextureMap.textureMapData: ByteBuffer
    get() = RenderModelTextureMap.nrubTextureMapData(adr, size.x * size.y * Vec4ub.size)


/** Vertex data for the mesh */
val RenderModel.vertices: FloatArray
    get() {
        val vertices = RenderModel.nrVertexData(adr)
        val res = FloatArray(vertexCount * RenderModelVertex.SIZEOF)
        var i = 0
        for (v in vertices) {
            res[i++] = v.position.x
            res[i++] = v.position.y
            res[i++] = v.position.z
            res[i++] = v.normal.x
            res[i++] = v.normal.y
            res[i++] = v.normal.z
            res[i++] = v.textureCoord.x
            res[i++] = v.textureCoord.y
        }
        return res
    }

/** Number of vertices in the vertex data */
val RenderModel.vertexCount: Int
    get() = RenderModel.nunVertexCount(adr)

/** Indices into the vertex data for each triangle */
val RenderModel.indices: ShortBuffer
    get() = RenderModel.nIndexData(adr)

/** Number of triangles in the mesh. Index count is 3 * TriangleCount */
val RenderModel.triangleCount: Int
    get() = RenderModel.nunTriangleCount(adr)

/** Session unique texture identifier. Rendermodels which share the same texture will have the same id. <0 == texture not present */
val RenderModel.diffuseTextureId: TextureId
    get() = RenderModel.ndiffuseTextureId(adr)


/** is this controller currently set to be in a scroll wheel mode */
var RenderModelControllerModeState.scrollWheelVisible: Boolean
    get() = RenderModelControllerModeState.nbScrollWheelVisible(adr)
    set(value) = RenderModelControllerModeState.nbScrollWheelVisible(adr, value)


// ivrextendeddisplay.h

// ivrtrackedcamera.h

// ivrscreenshots.h

// ivrdrivermanager.h

// ivrinput.h

/** Whether or not this action is currently available to be bound in the active action set */
val InputAnalogActionData.active: Boolean
    get() = InputAnalogActionData.nbActive(adr)

/** The origin that caused this action's current state */
val InputAnalogActionData.activeOrigin: VRInputValueHandle
    get() = InputAnalogActionData.nactiveOrigin(adr)

/** The current state of this action; will be delta updates for mouse actions */
val InputAnalogActionData.x: Float
    get() = InputAnalogActionData.nx(adr)

/** The current state of this action; will be delta updates for mouse actions */
val InputAnalogActionData.y: Float
    get() = InputAnalogActionData.ny(adr)

/** The current state of this action; will be delta updates for mouse actions */
val InputAnalogActionData.z: Float
    get() = InputAnalogActionData.nz(adr)

/** Deltas since the previous call to UpdateActionState() */
val InputAnalogActionData.deltaX: Float
    get() = InputAnalogActionData.ndeltaX(adr)

/** Deltas since the previous call to UpdateActionState() */
val InputAnalogActionData.deltaY: Float
    get() = InputAnalogActionData.ndeltaY(adr)

/** Deltas since the previous call to UpdateActionState() */
val InputAnalogActionData.deltaZ: Float
    get() = InputAnalogActionData.ndeltaZ(adr)

/** Time relative to now when this event happened. Will be negative to indicate a past time. */
val InputAnalogActionData.updateTime: Float
    get() = InputAnalogActionData.nfUpdateTime(adr)


/** Whether or not this action is currently available to be bound in the active action set */
val InputDigitalActionData.active: Boolean
    get() = InputDigitalActionData.nbActive(adr)

/** The origin that caused this action's current state */
val InputDigitalActionData.activeOrigin: VRInputValueHandle
    get() = InputDigitalActionData.nactiveOrigin(adr)

/** The current state of this action; will be true if currently pressed */
val InputDigitalActionData.state: Boolean
    get() = InputDigitalActionData.nbState(adr)

/** This is true if the state has changed since the last frame */
val InputDigitalActionData.changed: Boolean
    get() = InputDigitalActionData.nbChanged(adr)

/** Time relative to now when this event happened. Will be negative to indicate a past time. */
val InputDigitalActionData.updateTime: Float
    get() = InputDigitalActionData.nfUpdateTime(adr)


/** Whether or not this action is currently available to be bound in the active action set */
val InputPoseActionData.active: Boolean
    get() = InputPoseActionData.nbActive(adr)

/** The origin that caused this action's current state */
val InputPoseActionData.activeOrigin: VRInputValueHandle
    get() = InputPoseActionData.nactiveOrigin(adr)

/** The current state of this action */
val InputPoseActionData.pose: TrackedDevicePose
    get() = InputPoseActionData.npose(adr)


/** Whether or not this action is currently available to be bound in the active action set */
val InputSkeletonActionData.active: Boolean
    get() = InputSkeletonActionData.nbActive(adr)

/** Whether or not this action is currently available to be bound in the active action set */
val InputSkeletonActionData.activeOrigin: VRInputValueHandle
    get() = InputSkeletonActionData.nactiveOrigin(adr)



val InputOriginInfo.devicePath: VRInputValueHandle
    get() = InputOriginInfo.ndevicePath(adr)

val InputOriginInfo.trackedDeviceIndex: TrackedDeviceIndex
    get() = InputOriginInfo.ntrackedDeviceIndex(adr)

val InputOriginInfo.renderModelComponentName: String
    get() = InputOriginInfo.nrchRenderModelComponentNameString(adr)


/** This is the handle of the action set to activate for this frame. */
var VRActiveActionSet.actionSet: VRActionSetHandle
    get() = VRActiveActionSet.nulActionSet(adr)
    set(value) = VRActiveActionSet.nulActionSet(adr, value)

/** This is the handle of a device path that this action set should be active for.
 *  To activate for all devices, set this to ::invalidInputValueHandle. */
var VRActiveActionSet.restrictedToDevice: VRInputValueHandle
    get() = VRActiveActionSet.nulRestrictedToDevice(adr)
    set(value) = VRActiveActionSet.nulRestrictedToDevice(adr, value)

/** The action set to activate for all devices other than ulRestrictedDevice.
 * If restrictedToDevice is set to ::invalidInputValueHandle, this parameter is ignored. */
var VRActiveActionSet.secondaryActionSet: VRActionSetHandle
    get() = VRActiveActionSet.nulSecondaryActionSet(adr)
    set(value) = VRActiveActionSet.nulSecondaryActionSet(adr, value)


// ivriobuffer.h