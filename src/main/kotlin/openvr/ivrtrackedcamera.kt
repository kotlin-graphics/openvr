package openvr

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
    fun getCameraErrorNameFromEnum(eCameraError: EVRTrackedCameraError) = GetCameraErrorNameFromEnum!!.invoke(eCameraError.i)

    @JvmField var GetCameraErrorNameFromEnum: GetCameraErrorNameFromEnum_callback? = null

    interface GetCameraErrorNameFromEnum_callback : Callback {
        fun invoke(eCameraError: Int): String
    }

    /** For convenience, same as tracked property request Prop_HasCamera_Bool */
    // TODO check automatic conversion *Boolean -> *Byte
    fun hasCamera(nDeviceIndex: TrackedDeviceIndex_t, pHasCamera: BooleanByReference) = EVRTrackedCameraError.of(HasCamera!!.invoke(nDeviceIndex, pHasCamera))

    @JvmField var HasCamera: HasCamera_callback? = null

    interface HasCamera_callback : Callback {
        fun invoke(nDeviceIndex: TrackedDeviceIndex_t, pHasCamera: ByteByReference): Int
    }

    /** Gets size of the image frame. */
    fun getCameraFrameSize(nDeviceIndex: TrackedDeviceIndex_t, eFrameType: EVRTrackedCameraFrameType, pnWidth: IntByReference, pnHeight: IntByReference,
                           pnFrameBufferSize: IntByReference)
            = EVRTrackedCameraError.of(GetCameraFrameSize!!.invoke(nDeviceIndex, eFrameType.i, pnWidth, pnHeight, pnFrameBufferSize))

    @JvmField var GetCameraFrameSize: GetCameraFrameSize_callback? = null

    interface GetCameraFrameSize_callback : Callback {
        fun invoke(nDeviceIndex: TrackedDeviceIndex_t, eFrameType: Int, pnWidth: IntByReference, pnHeight: IntByReference, pnFrameBufferSize: IntByReference): Int
    }


    fun GetCameraIntrinsics(nDeviceIndex: TrackedDeviceIndex_t, eFrameType: EVRTrackedCameraFrameType, pFocalLength: HmdVector2_t.ByReference,
                            pCenter: HmdVector2_t.ByReference)
            = EVRTrackedCameraError.of(GetCameraIntrinsics!!.invoke(nDeviceIndex, eFrameType.i, pFocalLength, pCenter))

    @JvmField var GetCameraIntrinsics: GetCameraIntrinsics_callback? = null

    interface GetCameraIntrinsics_callback : Callback {
        fun invoke(nDeviceIndex: TrackedDeviceIndex_t, eFrameType: Int, pFocalLength: HmdVector2_t.ByReference, pCenter: HmdVector2_t.ByReference): Int
    }


    fun getCameraProjection(nDeviceIndex: TrackedDeviceIndex_t, eFrameType: EVRTrackedCameraFrameType, flZNear: Float, flZFar: Float,
                            pProjection: HmdMatrix44_t.ByReference)
            = EVRTrackedCameraError.of(GetCameraProjection!!.invoke(nDeviceIndex, eFrameType.i, flZNear, flZFar, pProjection))

    @JvmField var GetCameraProjection: GetCameraProjection_callback? = null

    interface GetCameraProjection_callback : Callback {
        fun invoke(nDeviceIndex: TrackedDeviceIndex_t, eFrameType: Int, flZNear: Float, flZFar: Float, pProjection: HmdMatrix44_t.ByReference): Int
    }

    /** Acquiring streaming service permits video streaming for the caller. Releasing hints the system that video services do not need to be maintained for this
     *  client.
     *  If the camera has not already been activated, a one time spin up may incur some auto exposure as well as initial streaming frame delays.
     *  The camera should be considered a global resource accessible for shared consumption but not exclusive to any caller.
     *  The camera may go inactive due to lack of active consumers or headset idleness. */
    fun acquireVideoStreamingService(nDeviceIndex: TrackedDeviceIndex_t, pHandle: TrackedCameraHandle_t)
            = EVRTrackedCameraError.of(AcquireVideoStreamingService!!.invoke(nDeviceIndex, pHandle))

    @JvmField var AcquireVideoStreamingService: AcquireVideoStreamingService_callback? = null

    interface AcquireVideoStreamingService_callback : Callback {
        fun invoke(nDeviceIndex: TrackedDeviceIndex_t, pHandle: TrackedCameraHandle_t): Int
    }

    fun releaseVideoStreamingService(hTrackedCamera: TrackedCameraHandle_t) = EVRTrackedCameraError.of(ReleaseVideoStreamingService!!.invoke(hTrackedCamera))
    @JvmField var ReleaseVideoStreamingService: ReleaseVideoStreamingService_callback? = null

    interface ReleaseVideoStreamingService_callback : Callback {
        fun invoke(hTrackedCamera: TrackedCameraHandle_t): Int
    }

    /** Copies the image frame into a caller's provided buffer. The image data is currently provided as RGBA data, 4 bytes per pixel.
     *  A caller can provide null for the framebuffer or frameheader if not desired. Requesting the frame header first, followed by the frame buffer allows
     *  the caller to determine if the frame as advanced per the frame header sequence.
     *  If there is no frame available yet, due to initial camera spinup or re-activation, the error will be VRTrackedCameraError_NoFrameAvailable.
     *  Ideally a caller should be polling at ~16ms intervals */
    fun getVideoStreamFrameBuffer(hTrackedCamera: TrackedCameraHandle_t, eFrameType: EVRTrackedCameraFrameType, pFrameBuffer: Pointer, nFrameBufferSize: Int,
                                  pFrameHeader: CameraVideoStreamFrameHeader_t.ByReference, nFrameHeaderSize: Int)
            = EVRTrackedCameraError.of(GetVideoStreamFrameBuffer!!.invoke(hTrackedCamera, eFrameType.i, pFrameBuffer, nFrameBufferSize, pFrameHeader,
            nFrameHeaderSize))

    @JvmField var GetVideoStreamFrameBuffer: GetVideoStreamFrameBuffer_callback? = null

    interface GetVideoStreamFrameBuffer_callback : Callback {
        fun invoke(hTrackedCamera: TrackedCameraHandle_t, eFrameType: Int, pFrameBuffer: Pointer, nFrameBufferSize: Int,
                   pFrameHeader: CameraVideoStreamFrameHeader_t.ByReference, nFrameHeaderSize: Int): Int
    }

    /** Gets size of the image frame. */
    fun getVideoStreamTextureSize(nDeviceIndex: TrackedDeviceIndex_t, eFrameType: EVRTrackedCameraFrameType, pTextureBounds: VRTextureBounds_t.ByReference,
                                  pnWidth: IntByReference, pnHeight: IntByReference)
            = EVRTrackedCameraError.of(GetVideoStreamTextureSize!!.invoke(nDeviceIndex, eFrameType.i, pTextureBounds, pnWidth, pnHeight))

    @JvmField var GetVideoStreamTextureSize: GetVideoStreamTextureSize_callback? = null

    interface GetVideoStreamTextureSize_callback : Callback {
        fun invoke(nDeviceIndex: TrackedDeviceIndex_t, eFrameType: Int, pTextureBounds: VRTextureBounds_t.ByReference, pnWidth: IntByReference,
                   pnHeight: IntByReference): Int
    }

    /** Access a shared D3D11 texture for the specified tracked camera stream.
     *  The camera frame type VRTrackedCameraFrameType_Undistorted is not supported directly as a shared texture. It is an interior
     *  subregion of the shared texture VRTrackedCameraFrameType_MaximumUndistorted.
     *  Instead, use GetVideoStreamTextureSize() with VRTrackedCameraFrameType_Undistorted to determine the proper interior subregion
     *  bounds along with GetVideoStreamTextureD3D11() with VRTrackedCameraFrameType_MaximumUndistorted to provide the texture.
     *  The VRTrackedCameraFrameType_MaximumUndistorted will yield an image where the invalid regions are decoded by the alpha channel
     *  having a zero component. The valid regions all have a non-zero alpha component. The subregion as described by
     *  VRTrackedCameraFrameType_Undistorted guarantees a rectangle where all pixels are valid. */
    fun getVideoStreamTextureD3D11(hTrackedCamera: TrackedCameraHandle_t, eFrameType: EVRTrackedCameraFrameType, pD3D11DeviceOrResource: Pointer,
                                   ppD3D11ShaderResourceView: PointerByReference, pFrameHeader: CameraVideoStreamFrameHeader_t.ByReference, nFrameHeaderSize: Int)
            = EVRTrackedCameraError.of(GetVideoStreamTextureD3D11!!.invoke(hTrackedCamera, eFrameType.i, pD3D11DeviceOrResource, ppD3D11ShaderResourceView,
            pFrameHeader, nFrameHeaderSize))

    @JvmField var GetVideoStreamTextureD3D11: GetVideoStreamTextureD3D11_callback? = null

    interface GetVideoStreamTextureD3D11_callback : Callback {
        fun invoke(hTrackedCamera: TrackedCameraHandle_t, eFrameType: Int, pD3D11DeviceOrResource: Pointer, ppD3D11ShaderResourceView: PointerByReference,
                   pFrameHeader: CameraVideoStreamFrameHeader_t.ByReference, nFrameHeaderSize: Int): Int
    }

    /** Access a shared GL texture for the specified tracked camera stream */
    fun getVideoStreamTextureGL(hTrackedCamera: TrackedCameraHandle_t, eFrameType: EVRTrackedCameraFrameType, pglTextureId: glUInt_t_ByReference,
                                pFrameHeader: CameraVideoStreamFrameHeader_t.ByReference, nFrameHeaderSize: Int)
            = EVRTrackedCameraError.of(GetVideoStreamTextureGL!!.invoke(hTrackedCamera, eFrameType.i, pglTextureId, pFrameHeader, nFrameHeaderSize))

    @JvmField var GetVideoStreamTextureGL: GetVideoStreamTextureGL_callback? = null

    interface GetVideoStreamTextureGL_callback : Callback {
        fun invoke(hTrackedCamera: TrackedCameraHandle_t, eFrameType: Int, pglTextureId: IntByReference, pFrameHeader: CameraVideoStreamFrameHeader_t.ByReference,
                   nFrameHeaderSize: Int): Int
    }


    fun releaseVideoStreamTextureGL(hTrackedCamera: TrackedCameraHandle_t, glTextureId: glUInt_t)
            = EVRTrackedCameraError.of(ReleaseVideoStreamTextureGL!!.invoke(hTrackedCamera, glTextureId))

    @JvmField var ReleaseVideoStreamTextureGL: ReleaseVideoStreamTextureGL_callback? = null

    interface ReleaseVideoStreamTextureGL_callback : Callback {
        fun invoke(hTrackedCamera: TrackedCameraHandle_t, glTextureId: Int): Int
    }

    constructor()

    override fun getFieldOrder(): List<String> = Arrays.asList("GetCameraErrorNameFromEnum", "HasCamera", "GetCameraFrameSize",
            "GetCameraIntrinsics", "GetCameraProjection", "AcquireVideoStreamingService", "ReleaseVideoStreamingService",
            "GetVideoStreamFrameBuffer", "GetVideoStreamTextureSize", "GetVideoStreamTextureD3D11", "GetVideoStreamTextureGL",
            "ReleaseVideoStreamTextureGL")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRTrackedCamera(), Structure.ByReference
    class ByValue : IVRTrackedCamera(), Structure.ByValue
}

val IVRTrackedCamera_Version = "FnTable:IVRTrackedCamera_003"