package openvr.lib

import glm_.BYTES
import glm_.mat4x4.Mat4
import kool.set
import glm_.vec2.Vec2i
import kool.adr
import kool.rem
import kool.stak
import org.lwjgl.PointerBuffer
import org.lwjgl.openvr.CameraVideoStreamFrameHeader
import org.lwjgl.openvr.HmdVector2
import org.lwjgl.openvr.VRTextureBounds
import org.lwjgl.openvr.VRTrackedCamera.*
import org.lwjgl.system.MemoryStack.stackGet
import org.lwjgl.system.MemoryUtil.*
import java.nio.ByteBuffer
import java.nio.IntBuffer


object vrTrackedCamera : vrInterface {

    enum class Error(@JvmField val i: Int) {

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

        /** Returns a string for an error. */
        override fun toString(): String = stak { memASCII(nVRTrackedCamera_GetCameraErrorNameFromEnum(i)) }
    }

    val pError = memCallocInt(1)

    var error: Error
        get() = Error of pError[0]
        set(value) {
            pError[0] = value.i
        }

    enum class FrameLayout(@JvmField val i: Int) {
        Mono(0x0001),
        Stereo(0x0002),
        /** Stereo frames are Top/Bottom (left/right) */
        VerticalLayout(0x0010),
        /** Stereo frames are Left/Right */
        HorizontalLayout(0x0020), ;
    }

    enum class FrameType(@JvmField val i: Int) {
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

    enum class VRDistortionFunctionType{None, FTheta, Extended_FTheta, MAX_DISTORTION_FUNCTION_TYPES }

    const val maxDistortionFunctionParameters: Int = 8

    /**
     * Kind of useless on JVM, but it will be offered anyway on the enum itself
     * Returns a string for an error.
     *
     * @param cameraError one of:<br><table><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_None}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_OperationFailed}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_InvalidHandle}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_InvalidFrameHeaderVersion}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_OutOfHandles}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_IPCFailure}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_NotSupportedForThisDevice}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_SharedMemoryFailure}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_FrameBufferingFailure}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_StreamSetupFailure}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_InvalidGLTextureId}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_InvalidSharedTextureHandle}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_FailedToGetGLTextureId}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_SharedTextureFailure}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_NoFrameAvailable}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_InvalidArgument}</td></tr><tr><td>{@link VR#EVRTrackedCameraError_VRTrackedCameraError_InvalidFrameBufferSize}</td></tr></table>
     */
//    fun IVRTrackedCamera.getCameraErrorNameFromEnum(cameraError: Error): String? {
//        val result = VRTrackedCamera.nVRTrackedCamera_GetCameraErrorNameFromEnum(cameraError.i)
//        return MemoryUtil.memASCIISafe(result)
//    }

    /** For convenience, same as tracked property request {@link VR#ETrackedDeviceProperty_Prop_HasCamera_Bool}. */
    fun hasCamera(deviceIndex: TrackedDeviceIndex, hasCamera: ByteBuffer): Error =
            Error of nVRTrackedCamera_HasCamera(deviceIndex, hasCamera.adr)

    /**
     * Gets size of the image frame.
     *
     * @param frameType one of:<br><table><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Distorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Undistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_MaximumUndistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_MAX_CAMERA_FRAME_TYPES}</td></tr></table>
     */
    fun getCameraFrameSize(deviceIndex: TrackedDeviceIndex, frameType: FrameType, size: Vec2i, frameBufferSize: IntBuffer): Error {
        val width = stackGet().nmalloc(1, Vec2i.size)
        val height = width + Int.BYTES
        return Error of nVRTrackedCamera_GetCameraFrameSize(deviceIndex, frameType.i, width, height, frameBufferSize.adr).also {
            size.put(memGetInt(width), memGetInt(height))
        }
    }

    /** @param frameType one of:<br><table><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Distorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Undistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_MaximumUndistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_MAX_CAMERA_FRAME_TYPES}</td></tr></table> */
    fun getCameraIntrinsics(deviceIndex: TrackedDeviceIndex, cameraIndex: Int, frameType: FrameType,
                            focalLength: HmdVector2.Buffer, center: HmdVector2.Buffer): Error =
            Error of nVRTrackedCamera_GetCameraIntrinsics(deviceIndex, cameraIndex, frameType.i, focalLength.adr, center.adr)

    /** @param frameType one of:<br><table><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Distorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Undistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_MaximumUndistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_MAX_CAMERA_FRAME_TYPES}</td></tr></table> */
    fun getCameraProjection(deviceIndex: TrackedDeviceIndex, cameraIndex: Int, frameType: FrameType, zNear: Float, zFar: Float,
                            projection: Mat4): Error {
        val hmdMatrix34 = vr.HmdMatrix34()
        return Error of nVRTrackedCamera_GetCameraProjection(deviceIndex, cameraIndex, frameType.i, zNear, zFar, hmdMatrix34.adr).also {
            hmdMatrix34 to projection
        }
    }

    /**
     * JVM custom
     *
     * Acquiring streaming service permits video streaming for the caller. Releasing hints the system that video services do not need to be maintained for
     * this client. If the camera has not already been activated, a one time spin up may incur some auto exposure as well as initial streaming frame delays.
     * The camera should be considered a global resource accessible for shared consumption but not exclusive to any caller. The camera may go inactive due to
     * lack of active consumers or headset idleness.
     */
    @JvmOverloads
    fun acquireVideoStreamingService(deviceIndex: TrackedDeviceIndex, pErr: TrackedCameraErrorBuffer = pError): TrackedCameraHandle =
            stak.longAddress {
                pErr[0] = nVRTrackedCamera_AcquireVideoStreamingService(deviceIndex, it)
            }

    fun releaseVideoStreamingService(trackedCamera: TrackedCameraHandle): Error =
            Error of VRTrackedCamera_ReleaseVideoStreamingService(trackedCamera)

    /**
     * Copies the image frame into a caller's provided buffer. The image data is currently provided as RGBA data, 4 bytes per pixel. A caller can provide null
     * for the framebuffer or frameheader if not desired. Requesting the frame header first, followed by the frame buffer allows the caller to determine if
     * the frame as advanced per the frame header sequence. If there is no frame available yet, due to initial camera spinup or re-activation, the error will
     * be {@link VR#EVRTrackedCameraError_VRTrackedCameraError_NoFrameAvailable}. Ideally a caller should be polling at ~16ms intervals.
     *
     * @param frameType one of:<br><table><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Distorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Undistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_MaximumUndistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_MAX_CAMERA_FRAME_TYPES}</td></tr></table>
     */
    fun getVideoStreamFrameBuffer(trackedCamera: TrackedCameraHandle, frameType: FrameType, frameBuffer: ByteBuffer, frameHeader: CameraVideoStreamFrameHeader): Error =
            Error of nVRTrackedCamera_GetVideoStreamFrameBuffer(trackedCamera, frameType.i, frameBuffer.adr, frameBuffer.rem, frameHeader.adr, CameraVideoStreamFrameHeader.SIZEOF)

    /**
     * Gets size of the image frame.
     *
     * @param frameType one of:<br><table><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Distorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_Undistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_VRTrackedCameraFrameType_MaximumUndistorted}</td></tr><tr><td>{@link VR#EVRTrackedCameraFrameType_MAX_CAMERA_FRAME_TYPES}</td></tr></table>
     */
    fun getVideoStreamTextureSize(deviceIndex: TrackedDeviceIndex, frameType: FrameType, textureBounds: VRTextureBounds, size: Vec2i): Error {
        val width = stackGet().nmalloc(1, Int.BYTES * 2)
        val height = width + Int.BYTES
        return Error of nVRTrackedCamera_GetVideoStreamTextureSize(deviceIndex, frameType.i, textureBounds.adr, width, height).also {
            size.put(memGetInt(width), memGetInt(height))
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
    fun getVideoStreamTextureD3D11(trackedCamera: TrackedCameraHandle, frameType: FrameType, d3D11DeviceOrResource: Long,
                                   d3D11ShaderResourceView: PointerBuffer, frameHeader: CameraVideoStreamFrameHeader): Error =
            Error of nVRTrackedCamera_GetVideoStreamTextureD3D11(trackedCamera, frameType.i, d3D11DeviceOrResource, d3D11ShaderResourceView.adr, frameHeader.adr, CameraVideoStreamFrameHeader.SIZEOF)

    /** Access a shared GL texture for the specified tracked camera stream.
     *  @param textureId ~glUInt *
     *  */
    fun getVideoStreamTextureGL(trackedCamera: TrackedCameraHandle, frameType: FrameType, textureId: IntBuffer, frameHeader: CameraVideoStreamFrameHeader): Error =
            Error of nVRTrackedCamera_GetVideoStreamTextureGL(trackedCamera, frameType.i, textureId.adr, frameHeader.adr, CameraVideoStreamFrameHeader.SIZEOF)

    fun releaseVideoStreamTextureGL(trackedCamera: TrackedCameraHandle, glTextureId: glUInt): Error =
            Error of VRTrackedCamera_ReleaseVideoStreamTextureGL(trackedCamera, glTextureId)

    override val version: String
        get() = "IVRTrackedCamera_005"
}