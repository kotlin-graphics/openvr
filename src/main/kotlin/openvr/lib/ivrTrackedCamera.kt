package openvr.lib

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.ByteByReference
import com.sun.jna.ptr.IntByReference
import com.sun.jna.ptr.PointerByReference
import java.util.*

// ivrtrackedcamera.h =============================================================================================================================================

open class IVRTrackedCamera : Structure {

    /** Returns a string for an error */
    infix fun getCameraErrorNameFromEnum(cameraError: EVRTrackedCameraError) = GetCameraErrorNameFromEnum!!(cameraError.i)

    @JvmField var GetCameraErrorNameFromEnum: GetCameraErrorNameFromEnum_callback? = null

    interface GetCameraErrorNameFromEnum_callback : Callback {
        operator fun invoke(eCameraError: Int): String
    }

    /** For convenience, same as tracked property request Prop_HasCamera_Bool */
    // TODO check automatic conversion *Boolean -> *Byte
    fun hasCamera(deviceIndex: TrackedDeviceIndex, hasCamera: BooleanByReference) = EVRTrackedCameraError.of(HasCamera!!(deviceIndex, hasCamera))

    @JvmField var HasCamera: HasCamera_callback? = null

    interface HasCamera_callback : Callback {
        operator fun invoke(nDeviceIndex: TrackedDeviceIndex, pHasCamera: ByteByReference): Int
    }

    /** Gets size of the image frame. */
    fun getCameraFrameSize(deviceIndex: TrackedDeviceIndex, frameType: EVRTrackedCameraFrameType, width: IntByReference, height: IntByReference, frameBufferSize: IntByReference) = EVRTrackedCameraError.of(GetCameraFrameSize!!(deviceIndex, frameType.i, width, height, frameBufferSize))

    @JvmField var GetCameraFrameSize: GetCameraFrameSize_callback? = null

    interface GetCameraFrameSize_callback : Callback {
        operator fun invoke(nDeviceIndex: TrackedDeviceIndex, eFrameType: Int, pnWidth: IntByReference, pnHeight: IntByReference, pnFrameBufferSize: IntByReference): Int
    }


    fun GetCameraIntrinsics(deviceIndex: TrackedDeviceIndex, frameType: EVRTrackedCameraFrameType, focalLength: HmdVec2.ByReference, center: HmdVec2.ByReference) = EVRTrackedCameraError.of(GetCameraIntrinsics!!(deviceIndex, frameType.i, focalLength, center))

    @JvmField var GetCameraIntrinsics: GetCameraIntrinsics_callback? = null

    interface GetCameraIntrinsics_callback : Callback {
        operator fun invoke(nDeviceIndex: TrackedDeviceIndex, eFrameType: Int, pFocalLength: HmdVec2.ByReference, pCenter: HmdVec2.ByReference): Int
    }


    fun getCameraProjection(deviceIndex: TrackedDeviceIndex, frameType: EVRTrackedCameraFrameType, zNear: Float, zFar: Float, projection: HmdMat44.ByReference) = EVRTrackedCameraError.of(GetCameraProjection!!(deviceIndex, frameType.i, zNear, zFar, projection))

    @JvmField var GetCameraProjection: GetCameraProjection_callback? = null

    interface GetCameraProjection_callback : Callback {
        operator fun invoke(nDeviceIndex: TrackedDeviceIndex, eFrameType: Int, flZNear: Float, flZFar: Float, pProjection: HmdMat44.ByReference): Int
    }

    /** Acquiring streaming service permits video streaming for the caller. Releasing hints the system that video services do not need to be maintained for this
     *  client.
     *  If the camera has not already been activated, a one time spin up may incur some auto exposure as well as initial streaming frame delays.
     *  The camera should be considered a global resource accessible for shared consumption but not exclusive to any caller.
     *  The camera may go inactive due to lack of active consumers or headset idleness. */
    fun acquireVideoStreamingService(deviceIndex: TrackedDeviceIndex, handle: TrackedCameraHandle) = EVRTrackedCameraError.of(AcquireVideoStreamingService!!(deviceIndex, handle))

    @JvmField var AcquireVideoStreamingService: AcquireVideoStreamingService_callback? = null

    interface AcquireVideoStreamingService_callback : Callback {
        operator fun invoke(nDeviceIndex: TrackedDeviceIndex, pHandle: TrackedCameraHandle): Int
    }

    infix fun releaseVideoStreamingService(trackedCamera: TrackedCameraHandle) = EVRTrackedCameraError.of(ReleaseVideoStreamingService!!(trackedCamera))

    @JvmField var ReleaseVideoStreamingService: ReleaseVideoStreamingService_callback? = null

    interface ReleaseVideoStreamingService_callback : Callback {
        operator fun invoke(hTrackedCamera: TrackedCameraHandle): Int
    }

    /** Copies the image frame into a caller's provided buffer. The image data is currently provided as RGBA data, 4 bytes per pixel.
     *  A caller can provide null for the framebuffer or frameheader if not desired. Requesting the frame header first, followed by the frame buffer allows
     *  the caller to determine if the frame as advanced per the frame header sequence.
     *  If there is no frame available yet, due to initial camera spinup or re-activation, the error will be VRTrackedCameraError_NoFrameAvailable.
     *  Ideally a caller should be polling at ~16ms intervals */
    fun getVideoStreamFrameBuffer(trackedCamera: TrackedCameraHandle, frameType: EVRTrackedCameraFrameType, frameBuffer: Pointer, frameBufferSize: Int, frameHeader: CameraVideoStreamFrameHeader.ByReference, frameHeaderSize: Int) = EVRTrackedCameraError.of(GetVideoStreamFrameBuffer!!(trackedCamera, frameType.i, frameBuffer, frameBufferSize, frameHeader, frameHeaderSize))

    @JvmField var GetVideoStreamFrameBuffer: GetVideoStreamFrameBuffer_callback? = null

    interface GetVideoStreamFrameBuffer_callback : Callback {
        operator fun invoke(hTrackedCamera: TrackedCameraHandle, eFrameType: Int, pFrameBuffer: Pointer, nFrameBufferSize: Int, pFrameHeader: CameraVideoStreamFrameHeader.ByReference, nFrameHeaderSize: Int): Int
    }

    /** Gets size of the image frame. */
    fun getVideoStreamTextureSize(deviceIndex: TrackedDeviceIndex, frameType: EVRTrackedCameraFrameType, textureBounds: VRTextureBounds.ByReference, width: IntByReference, height: IntByReference) = EVRTrackedCameraError.of(GetVideoStreamTextureSize!!(deviceIndex, frameType.i, textureBounds, width, height))

    @JvmField var GetVideoStreamTextureSize: GetVideoStreamTextureSize_callback? = null

    interface GetVideoStreamTextureSize_callback : Callback {
        operator fun invoke(nDeviceIndex: TrackedDeviceIndex, eFrameType: Int, pTextureBounds: VRTextureBounds.ByReference, pnWidth: IntByReference, pnHeight: IntByReference): Int
    }

    /** Access a shared D3D11 texture for the specified tracked camera stream.
     *  The camera frame type VRTrackedCameraFrameType_Undistorted is not supported directly as a shared texture. It is an interior
     *  subregion of the shared texture VRTrackedCameraFrameType_MaximumUndistorted.
     *  Instead, use GetVideoStreamTextureSize() with VRTrackedCameraFrameType_Undistorted to determine the proper interior subregion
     *  bounds along with GetVideoStreamTextureD3D11() with VRTrackedCameraFrameType_MaximumUndistorted to provide the texture.
     *  The VRTrackedCameraFrameType_MaximumUndistorted will yield an image where the invalid regions are decoded by the alpha channel
     *  having a zero component. The valid regions all have a non-zero alpha component. The subregion as described by
     *  VRTrackedCameraFrameType_Undistorted guarantees a rectangle where all pixels are valid. */
    fun getVideoStreamTextureD3D11(trackedCamera: TrackedCameraHandle, frameType: EVRTrackedCameraFrameType, d3d11DeviceOrResource: Pointer, d311ShaderResourceView: PointerByReference, frameHeader: CameraVideoStreamFrameHeader.ByReference, frameHeaderSize: Int)= EVRTrackedCameraError.of(GetVideoStreamTextureD3D11!!(trackedCamera, frameType.i, d3d11DeviceOrResource, d311ShaderResourceView, frameHeader, frameHeaderSize))

    @JvmField var GetVideoStreamTextureD3D11: GetVideoStreamTextureD3D11_callback? = null

    interface GetVideoStreamTextureD3D11_callback : Callback {
        operator fun invoke(hTrackedCamera: TrackedCameraHandle, eFrameType: Int, pD3D11DeviceOrResource: Pointer, ppD3D11ShaderResourceView: PointerByReference, pFrameHeader: CameraVideoStreamFrameHeader.ByReference, nFrameHeaderSize: Int): Int
    }

    /** Access a shared GL texture for the specified tracked camera stream */
    fun getVideoStreamTextureGl(trackedCamera: TrackedCameraHandle, frameType: EVRTrackedCameraFrameType, textureId: glUInt_ByReference, frameHeader: CameraVideoStreamFrameHeader.ByReference, frameHeaderSize: Int) = EVRTrackedCameraError.of(GetVideoStreamTextureGL!!(trackedCamera, frameType.i, textureId, frameHeader, frameHeaderSize))

    @JvmField var GetVideoStreamTextureGL: GetVideoStreamTextureGL_callback? = null

    interface GetVideoStreamTextureGL_callback : Callback {
        operator fun invoke(hTrackedCamera: TrackedCameraHandle, eFrameType: Int, pglTextureId: IntByReference, pFrameHeader: CameraVideoStreamFrameHeader.ByReference, nFrameHeaderSize: Int): Int
    }


    fun releaseVideoStreamTextureGl(trackedCamera: TrackedCameraHandle, textureId: glUInt)= EVRTrackedCameraError.of(ReleaseVideoStreamTextureGL!!(trackedCamera, textureId))

    @JvmField var ReleaseVideoStreamTextureGL: ReleaseVideoStreamTextureGL_callback? = null

    interface ReleaseVideoStreamTextureGL_callback : Callback {
        operator fun invoke(hTrackedCamera: TrackedCameraHandle, glTextureId: Int): Int
    }

    constructor()

    override fun getFieldOrder()= listOf("GetCameraErrorNameFromEnum", "HasCamera", "GetCameraFrameSize",
            "GetCameraIntrinsics", "GetCameraProjection", "AcquireVideoStreamingService", "ReleaseVideoStreamingService",
            "GetVideoStreamFrameBuffer", "GetVideoStreamTextureSize", "GetVideoStreamTextureD3D11", "GetVideoStreamTextureGL",
            "ReleaseVideoStreamTextureGL")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRTrackedCamera(), Structure.ByReference
    class ByValue : IVRTrackedCamera(), Structure.ByValue
}

val IVRTrackedCamera_Version = "IVRTrackedCamera_003"