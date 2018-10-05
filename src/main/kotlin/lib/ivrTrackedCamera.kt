package lib

import ab.appBuffer
import glm_.buffer.adr
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2i
import org.lwjgl.PointerBuffer
import org.lwjgl.openvr.CameraVideoStreamFrameHeader
import org.lwjgl.openvr.HmdVector2
import org.lwjgl.openvr.OpenVR.IVRTrackedCamera
import org.lwjgl.openvr.VRTextureBounds
import org.lwjgl.openvr.VRTrackedCamera
import org.lwjgl.system.MemoryUtil
import vkk.adr
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.nio.LongBuffer


/**
 * Returns a string for an error.
 *
 * @param cameraError one of:<br><table><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_None}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_OperationFailed}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_InvalidHandle}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_InvalidFrameHeaderVersion}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_OutOfHandles}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_IPCFailure}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_NotSupportedForThisDevice}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_SharedMemoryFailure}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_FrameBufferingFailure}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_StreamSetupFailure}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_InvalidGLTextureId}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_InvalidSharedTextureHandle}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_FailedToGetGLTextureId}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_SharedTextureFailure}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_NoFrameAvailable}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_InvalidArgument}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_InvalidFrameBufferSize}</td></tr></table>
 */
fun IVRTrackedCamera.getCameraErrorNameFromEnum(cameraError: EVRTrackedCameraError): String? {
    val result = VRTrackedCamera.nVRTrackedCamera_GetCameraErrorNameFromEnum(cameraError.i)
    return MemoryUtil.memASCIISafe(result)
}

/** For convenience, same as tracked property request {@link VR#ETrackedDeviceProperty_Prop_HasCamera_Bool}. */
fun IVRTrackedCamera.hasCamera(deviceIndex: TrackedDeviceIndex, hasCamera: ByteBuffer): EVRTrackedCameraError {
    return EVRTrackedCameraError of VRTrackedCamera.nVRTrackedCamera_HasCamera(deviceIndex, hasCamera.adr)
}

/**
 * Gets size of the image frame.
 *
 * @param frameType one of:<br><table><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Distorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Undistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_MaximumUndistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_MAX_CAMERA_FRAME_TYPES}</td></tr></table>
 */
fun IVRTrackedCamera.getCameraFrameSize(deviceIndex: TrackedDeviceIndex, frameType: EVRTrackedCameraFrameType, size: Vec2i, frameBufferSize: IntBuffer): EVRTrackedCameraError {
    val width = appBuffer.int
    val height = appBuffer.int
    return EVRTrackedCameraError of VRTrackedCamera.nVRTrackedCamera_GetCameraFrameSize(deviceIndex, frameType.i, width, height, frameBufferSize.adr).also {
        size.put(MemoryUtil.memGetInt(width), MemoryUtil.memGetInt(height))
    }
}

/** @param frameType one of:<br><table><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Distorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Undistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_MaximumUndistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_MAX_CAMERA_FRAME_TYPES}</td></tr></table> */
fun IVRTrackedCamera.getCameraIntrinsics(deviceIndex: TrackedDeviceIndex, frameType: EVRTrackedCameraFrameType, focalLength: HmdVector2.Buffer, center: HmdVector2.Buffer): EVRTrackedCameraError {
    return EVRTrackedCameraError of VRTrackedCamera.nVRTrackedCamera_GetCameraIntrinsics(deviceIndex, frameType.i, focalLength.adr, center.adr)
}

/** @param frameType one of:<br><table><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Distorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Undistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_MaximumUndistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_MAX_CAMERA_FRAME_TYPES}</td></tr></table> */
fun IVRTrackedCamera.getCameraProjection(deviceIndex: TrackedDeviceIndex, frameType: EVRTrackedCameraFrameType, zNear: Float, zFar: Float, projection: Mat4): EVRTrackedCameraError {
    val hmdMatrix34 = vr.HmdMatrix34()
    return EVRTrackedCameraError of VRTrackedCamera.nVRTrackedCamera_GetCameraProjection(deviceIndex, frameType.i, zNear, zFar, hmdMatrix34.adr).also {
        hmdMatrix34 to projection
    }
}

/**
 * Acquiring streaming service permits video streaming for the caller. Releasing hints the system that video services do not need to be maintained for
 * this client. If the camera has not already been activated, a one time spin up may incur some auto exposure as well as initial streaming frame delays.
 * The camera should be considered a global resource accessible for shared consumption but not exclusive to any caller. The camera may go inactive due to
 * lack of active consumers or headset idleness.
 *
 * @param handle ~ TrackedCameraHandle *
 */
fun IVRTrackedCamera.acquireVideoStreamingService(deviceIndex: TrackedDeviceIndex, handle: LongBuffer): EVRTrackedCameraError {
    return EVRTrackedCameraError of VRTrackedCamera.nVRTrackedCamera_AcquireVideoStreamingService(deviceIndex, handle.adr)
}

fun IVRTrackedCamera.releaseVideoStreamingService(trackedCamera: TrackedCameraHandle): EVRTrackedCameraError {
    return EVRTrackedCameraError of VRTrackedCamera.VRTrackedCamera_ReleaseVideoStreamingService(trackedCamera)
}

/**
 * Copies the image frame into a caller's provided buffer. The image data is currently provided as RGBA data, 4 bytes per pixel. A caller can provide null
 * for the framebuffer or frameheader if not desired. Requesting the frame header first, followed by the frame buffer allows the caller to determine if
 * the frame as advanced per the frame header sequence. If there is no frame available yet, due to initial camera spinup or re-activation, the error will
 * be {@link VR#EVRTrackedCameraError_VRTrackedCameraError_NoFrameAvailable}. Ideally a caller should be polling at ~16ms intervals.
 *
 * @param frameType one of:<br><table><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Distorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Undistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_MaximumUndistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_MAX_CAMERA_FRAME_TYPES}</td></tr></table>
 */
fun IVRTrackedCamera.getVideoStreamFrameBuffer(trackedCamera: TrackedCameraHandle, frameType: EVRTrackedCameraFrameType, frameBuffer: ByteBuffer, frameHeader: CameraVideoStreamFrameHeader): EVRTrackedCameraError {
    return EVRTrackedCameraError of VRTrackedCamera.nVRTrackedCamera_GetVideoStreamFrameBuffer(trackedCamera, frameType.i, frameBuffer.adr, frameBuffer.rem, frameHeader.adr, CameraVideoStreamFrameHeader.SIZEOF)
}

/**
 * Gets size of the image frame.
 *
 * @param frameType one of:<br><table><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Distorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Undistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_MaximumUndistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_MAX_CAMERA_FRAME_TYPES}</td></tr></table>
 */
fun IVRTrackedCamera.getVideoStreamTextureSize(deviceIndex: TrackedDeviceIndex, frameType: EVRTrackedCameraFrameType, textureBounds: VRTextureBounds, size: Vec2i): EVRTrackedCameraError {
    val width = appBuffer.int
    val height = appBuffer.int
    return EVRTrackedCameraError of VRTrackedCamera.nVRTrackedCamera_GetVideoStreamTextureSize(deviceIndex, frameType.i, textureBounds.adr, width, height).also {
        size.put(MemoryUtil.memGetInt(width), MemoryUtil.memGetInt(height))
    }
}

/**
 * Access a shared D3D11 texture for the specified tracked camera stream.
 *
 * <p>The camera frame type {@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Undistorted} is not supported directly as a shared texture. It is an interior
 * subregion of the shared texture {@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_MaximumUndistorted}. Instead, use {@link #VRTrackedCamera_GetVideoStreamTextureSize GetVideoStreamTextureSize} with
 * {@code VRTrackedCameraFrameType_Undistorted} to determine the proper interior subregion bounds along with
 * {@code GetVideoStreamTextureD3D11()} with {@code VRTrackedCameraFrameType_MaximumUndistorted} to provide the texture. The
 * {@code VRTrackedCameraFrameType_MaximumUndistorted} will yield an image where the invalid regions are decoded by the alpha channel having a zero
 * component. The valid regions all have a non-zero alpha component. The subregion as described by {@code VRTrackedCameraFrameType_Undistorted} guarantees
 * a rectangle where all pixels are valid.</p>
 *
 * @param frameType one of:<br><table><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Distorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Undistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_MaximumUndistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_MAX_CAMERA_FRAME_TYPES}</td></tr></table>
 */
fun IVRTrackedCamera.getVideoStreamTextureD3D11(trackedCamera: TrackedCameraHandle, frameType: EVRTrackedCameraFrameType, d3D11DeviceOrResource: Long, d3D11ShaderResourceView: PointerBuffer, frameHeader: CameraVideoStreamFrameHeader): EVRTrackedCameraError {
    return EVRTrackedCameraError of VRTrackedCamera.nVRTrackedCamera_GetVideoStreamTextureD3D11(trackedCamera, frameType.i, d3D11DeviceOrResource, d3D11ShaderResourceView.adr, frameHeader.adr, CameraVideoStreamFrameHeader.SIZEOF)
}

/** Access a shared GL texture for the specified tracked camera stream.
 *  @param textureId ~glUInt *
 *  */
fun IVRTrackedCamera.getVideoStreamTextureGL(trackedCamera: TrackedCameraHandle, frameType: EVRTrackedCameraFrameType, textureId: IntBuffer, frameHeader: CameraVideoStreamFrameHeader): EVRTrackedCameraError {
    return EVRTrackedCameraError of VRTrackedCamera.nVRTrackedCamera_GetVideoStreamTextureGL(trackedCamera, frameType.i, textureId.adr, frameHeader.adr, CameraVideoStreamFrameHeader.SIZEOF)
}

fun IVRTrackedCamera.releaseVideoStreamTextureGL(trackedCamera: TrackedCameraHandle, glTextureId: glUInt): EVRTrackedCameraError {
    return EVRTrackedCameraError of VRTrackedCamera.VRTrackedCamera_ReleaseVideoStreamTextureGL(trackedCamera, glTextureId)
}