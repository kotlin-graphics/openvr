package openvr.lib

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.FloatByReference
import com.sun.jna.ptr.IntByReference
import com.sun.jna.ptr.LongByReference
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2i
import java.util.*

// ivrsystem.h ====================================================================================================================================================

open class IVRSystem : Structure {

    // ------------------------------------
    // Display Methods
    // ------------------------------------

    /** Suggested size for the intermediate render target that the distortion pulls from. */
    val recommendedRenderTargetSize: Vec2i
        get() {
            val w = IntByReference()
            val h = IntByReference()
            GetRecommendedRenderTargetSize!!(w, h)
            return Vec2i(w.value, h.value)
        }

    @JvmField
    var GetRecommendedRenderTargetSize: GetRecommendedRenderTargetSize_callback? = null

    interface GetRecommendedRenderTargetSize_callback : Callback {
        operator fun invoke(pnWidth: IntByReference, pnHeight: IntByReference)
    }

    /** The projection matrix for the specified eye */
    fun getProjectionMatrix(eye: EVREye, nearZ: Float, farZ: Float, res: Mat4 = Mat4()): Mat4 {
        val m = GetProjectionMatrix!!(eye.i, nearZ, farZ)
        return res.put(
                m[0], m[4], m[8], m[12],
                m[1], m[5], m[9], m[13],
                m[2], m[6], m[10], m[14],
                m[3], m[7], m[11], m[15])
    }

    @JvmField
    var GetProjectionMatrix: GetProjectionMatrix_callback? = null

    interface GetProjectionMatrix_callback : Callback {
        operator fun invoke(eEye: Int, fNearZ: Float, fFarZ: Float): HmdMat44.ByValue
    }

    /** The components necessary to build your own projection matrix in case your application is doing something fancy like infinite Z  */
    fun getProjectionRaw(eye: EVREye, left: FloatByReference, right: FloatByReference, top: FloatByReference, bottom: FloatByReference) = GetProjectionRaw!!(eye.i, left, right, top, bottom)

    @JvmField
    var GetProjectionRaw: GetProjectionRaw_callback? = null

    interface GetProjectionRaw_callback : Callback {
        operator fun invoke(eEye: Int, pfLeft: FloatByReference, pfRight: FloatByReference, pfTop: FloatByReference, pfBottom: FloatByReference)
    }

    /** Gets the result of the distortion function for the specified eye and input UVs.
     *  UVs go from 0,0 in the upper left of that eye's viewport and 1,1 in the lower right of that eye's viewport.
     *  Returns true for success. Otherwise, returns false, and distortion coordinates are not suitable.    */
    fun computeDistortion(eye: EVREye, u: Float, v: Float, distortionCoordinates: DistortionCoordinates.ByReference) = ComputeDistortion!!(eye.i, u, v, distortionCoordinates)

    @JvmField
    var ComputeDistortion: ComputeDistortion_callback? = null

    interface ComputeDistortion_callback : Callback {
        operator fun invoke(eEye: Int, fU: Float, fV: Float, pDistortionCoordinates_t: DistortionCoordinates.ByReference): Boolean
    }

    /** Returns the transform from eye space to the head space. Eye space is the per-eye flavor of head space that provides stereo
     *  disparity.
     *  Instead of Model * View * Projection the sequence is Model * View * Eye^-1 * Projection.
     *  Normally View and Eye^-1 will be multiplied together and treated as View in your application.   */
    fun getEyeToHeadTransform(eye: EVREye, res: Mat4 = Mat4()): Mat4 {
        val m = GetEyeToHeadTransform!!(eye.i)
        return res.put(
                m[0], m[4], m[8], 0f,
                m[1], m[5], m[9], 0f,
                m[2], m[6], m[10], 0f,
                m[3], m[7], m[11], 1f)
    }

    @JvmField
    var GetEyeToHeadTransform: GetEyeToHeadTransform_callback? = null

    interface GetEyeToHeadTransform_callback : Callback {
        operator fun invoke(eEye: Int): HmdMat34.ByValue
    }

    /** Returns the number of elapsed seconds since the last recorded vsync event. This will come from a vsync timer event in the timer
     *  if possible or from the application-reported time if that is not available.
     *  If no vsync times are available the function will return zero for vsync time and frame counter and return false from the method.    */
    fun getTimeSinceLastVsync(secondsSinceLastVsync: FloatByReference, frameCounter: LongByReference) = GetTimeSinceLastVsync!!(secondsSinceLastVsync, frameCounter)

    @JvmField
    var GetTimeSinceLastVsync: GetTimeSinceLastVsync_callback? = null

    interface GetTimeSinceLastVsync_callback : Callback {
        operator fun invoke(pfSecondsSinceLastVsync: FloatByReference, pulFrameCounter: LongByReference): Boolean
    }

    /** [D3D9 Only]
     * Returns the adapter index that the user should pass into CreateDevice to set up D3D9 in such a way that it can go full screen
     * exclusive on the HMD.
     * Returns -1 if there was an error.     */
    val d3d9AdapterIndex get() = GetD3D9AdapterIndex!!()

    @JvmField
    var GetD3D9AdapterIndex: GetD3D9AdapterIndex_callback? = null

    interface GetD3D9AdapterIndex_callback : Callback {
        operator fun invoke(): Int
    }

    /** [D3D10/11 Only]
     * Returns the adapter index that the user should pass into EnumAdapters to create the device and swap chain in DX10 and DX11.
     * If an error occurs the index will be set to -1.  */
    fun getDXGIOutputInfo(adapterIndex: IntByReference) = GetDXGIOutputInfo!!(adapterIndex)

    @JvmField
    var GetDXGIOutputInfo: GetDXGIOutputInfo_callback? = null

    interface GetDXGIOutputInfo_callback : Callback {
        operator fun invoke(pnAdapterIndex: IntByReference)
    }

    /** Returns platform- and texture-type specific adapter identification so that applications and the compositor are
     *  creating textures and swap chains on the same GPU. If an error occurs the device will be set to 0.
     *  instance is an optional parameter that is required only when textureType is TextureType_Vulkan.
     *  [D3D10/11/12 Only (D3D9 Not Supported)]
     *  Returns the adapter LUID that identifies the GPU attached to the HMD. The user should enumerate all adapters
     *  using IDXGIFactory::EnumAdapters and IDXGIAdapter::GetDesc to find the adapter with the matching LUID, or use
     *  IDXGIFactory4::EnumAdapterByLuid.
     *  The discovered IDXGIAdapter should be used to create the device and swap chain.
     *  [Vulkan Only]
     *  Returns the VkPhysicalDevice that should be used by the application.
     *  instance must be the instance the application will use to query for the VkPhysicalDevice. The application must
     *  create the VkInstance with extensions returned by IVRCompositor::GetVulkanInstanceExtensionsRequired enabled.
     *  [macOS Only]
     *  For TextureType_IOSurface returns the id<MTLDevice> that should be used by the application.
     *  On 10.13+ for TextureType_OpenGL returns the 'registryId' of the renderer which should be used
     *  by the application. See Apple Technical Q&A QA1168 for information on enumerating GL Renderers, and the
     *  new kCGLRPRegistryIDLow and kCGLRPRegistryIDHigh CGLRendererProperty values in the 10.13 SDK.
     *  Pre 10.13 for TextureType_OpenGL returns 0, as there is no dependable way to correlate the HMDs MTLDevice
     *  with a GL Renderer.    */
    fun getOutputDevice_callback(device: LongByReference, textureType: ETextureType, instance: VkInstance? = null) = GetOutputDevice!!(device, textureType.i, instance)

    @JvmField
    var GetOutputDevice: GetOutputDevice_callback? = null

    interface GetOutputDevice_callback : Callback {
        operator fun invoke(pnDevice: LongByReference, textureType: Int, pInstance: VkInstance? = null)
    }

    // ------------------------------------
    // Display Mode methods
    // ------------------------------------

    /** Use to determine if the headset display is part of the desktop (i.e. extended) or hidden (i.e. direct mode). */
    val isDisplayOnDesktop get() = IsDisplayOnDesktop!!()

    @JvmField
    var IsDisplayOnDesktop: IsDisplayOnDesktop_callback? = null

    interface IsDisplayOnDesktop_callback : Callback {
        operator fun invoke(): Boolean
    }

    /** Set the display visibility (true = extended, false = direct mode).  Return value of true indicates that the change was successful. */
    infix fun setDisplayVisibility(isVisibleOnDesktop: Boolean): Boolean = SetDisplayVisibility!!(isVisibleOnDesktop)

    @JvmField
    var SetDisplayVisibility: SetDisplayVisibility_callback? = null

    interface SetDisplayVisibility_callback : Callback {
        operator fun invoke(bIsVisibleOnDesktop: Boolean): Boolean
    }


    // ------------------------------------
    // Tracking Methods
    // ------------------------------------

    /** The pose that the tracker thinks that the HMD will be in at the specified number of seconds into the future. Pass 0 to get the
     *  state at the instant the method is called. Most of the time the application should calculate the time until the photons will be
     *  emitted from the display and pass that time into the method.
     *
     *  This is roughly analogous to the inverse of the view matrix in most applications, though many games will need to do some
     *  additional rotation or translation on top of the rotation and translation provided by the head pose.
     *
     *  For devices where poseIsValid is true the application can use the pose to position the device in question. The provided array
     *  can be any size up to openvr.lib.getK_unMaxTrackedDeviceCount.
     *
     *  Seated experiences should call this method with TrackingUniverseSeated and receive poses relative to the seated zero pose.
     *  Standing experiences should call this method with TrackingUniverseStanding and receive poses relative to the Chaperone Play Area.
     *  TrackingUniverseRawAndUncalibrated should probably not be used unless the application is the Chaperone calibration tool itself,
     *  but will provide poses relative to the hardware-specific coordinate system in the driver.     */
    fun getDeviceToAbsoluteTrackingPose(origin: ETrackingUniverseOrigin, predictedSecondsToPhotonsFromNow: Float, trackedDevicePoseArray: TrackedDevicePose.ByReference, trackedDevicePoseArrayCount: Int) = GetDeviceToAbsoluteTrackingPose!!(origin.i, predictedSecondsToPhotonsFromNow, trackedDevicePoseArray, trackedDevicePoseArrayCount)

    @JvmField
    var GetDeviceToAbsoluteTrackingPose: GetDeviceToAbsoluteTrackingPose_callback? = null

    interface GetDeviceToAbsoluteTrackingPose_callback : Callback {
        operator fun invoke(eOrigin: Int, fPredictedSecondsToPhotonsFromNow: Float, pTrackedDevicePoseArray: TrackedDevicePose.ByReference, unTrackedDevicePoseArrayCount: Int)
    }

    /** Sets the zero pose for the seated tracker coordinate system to the current position and yaw of the HMD.
     *  After ResetSeatedZeroPose all GetDeviceToAbsoluteTrackingPose calls that pass TrackingUniverseSeated as the origin will be
     *  relative to this new zero pose. The new zero coordinate system will not change the fact that the Y axis is up in the real world,
     *  so the next pose returned from GetDeviceToAbsoluteTrackingPose after a call to ResetSeatedZeroPose may not be exactly an
     *  identity matrix.
     *
     *  NOTE: This function overrides the user's previously saved seated zero pose and should only be called as the result of a user
     *  action.
     *  Users are also able to set their seated zero pose via the openvr.OpenVR Dashboard.     **/
    fun resetSeatedZeroPose() = ResetSeatedZeroPose!!()

    @JvmField
    var ResetSeatedZeroPose: ResetSeatedZeroPose_callback? = null

    interface ResetSeatedZeroPose_callback : Callback {
        operator fun invoke()
    }

    /** Returns the transform from the seated zero pose to the standing absolute tracking system. This allows applications to
     *  represent the seated origin to used or transform object positions from one coordinate system to the other.
     *
     *  The seated origin may or may not be inside the Play Area or Collision Bounds returned by openvr.lib.IVRChaperone.
     *  Its position depends on what the user has set from the Dashboard settings and previous calls to ResetSeatedZeroPose. */
    val seatedZeroPoseToStandingAbsoluteTrackingPose get() = GetSeatedZeroPoseToStandingAbsoluteTrackingPose!!()

    @JvmField
    var GetSeatedZeroPoseToStandingAbsoluteTrackingPose: GetSeatedZeroPoseToStandingAbsoluteTrackingPose_callback? = null

    interface GetSeatedZeroPoseToStandingAbsoluteTrackingPose_callback : Callback {
        operator fun invoke(): HmdMat34.ByValue
    }

    /** Returns the transform from the tracking origin to the standing absolute tracking system. This allows applications to convert
     *  from raw tracking space to the calibrated standing coordinate system. */
    fun getRawZeroPoseToStandingAbsoluteTrackingPose(res: Mat4 = Mat4()) = GetRawZeroPoseToStandingAbsoluteTrackingPose!!().to(res)

    @JvmField
    var GetRawZeroPoseToStandingAbsoluteTrackingPose: GetRawZeroPoseToStandingAbsoluteTrackingPose_callback? = null

    interface GetRawZeroPoseToStandingAbsoluteTrackingPose_callback : Callback {
        operator fun invoke(): HmdMat34.ByValue
    }

    /** Get a sorted array of device indices of a given class of tracked devices (e.g. controllers).  Devices are sorted right to left
     *  relative to the specified tracked device (default: hmd -- pass in -1 for absolute tracking space).
     *  Returns the number of devices in the list, or the size of the array needed if not large enough. */
    @JvmOverloads
    fun getSortedTrackedDeviceIndicesOfClass(trackedDeviceClass: ETrackedDeviceClass, trackedDeviceIndexArray: TrackedDeviceIndex_ByReference, trackedDeviceIndexArrayCount: Int, relativeToTrackedDeviceIndex: TrackedDeviceIndex = trackedDeviceIndex_Hmd) = GetSortedTrackedDeviceIndicesOfClass!!(trackedDeviceClass.i, trackedDeviceIndexArray, trackedDeviceIndexArrayCount, relativeToTrackedDeviceIndex)

    @JvmField
    var GetSortedTrackedDeviceIndicesOfClass: GetSortedTrackedDeviceIndicesOfClass_callback? = null

    interface GetSortedTrackedDeviceIndicesOfClass_callback : Callback {
        operator fun invoke(eTrackedDeviceClass: Int, punTrackedDeviceIndexArray: TrackedDeviceIndex_ByReference, unTrackedDeviceIndexArrayCount: Int, unRelativeToTrackedDeviceIndex: TrackedDeviceIndex): Int
    }

    /** Returns the level of activity on the device. */
    infix fun getTrackedDeviceActivityLevel(deviceId: TrackedDeviceIndex) = EDeviceActivityLevel.of(GetTrackedDeviceActivityLevel!!(deviceId))

    @JvmField
    var GetTrackedDeviceActivityLevel: GetTrackedDeviceActivityLevel_callback? = null

    interface GetTrackedDeviceActivityLevel_callback : Callback {
        operator fun invoke(unDeviceId: TrackedDeviceIndex): Int
    }

    /** Convenience utility to apply the specified transform to the specified pose.
     *  This properly transforms all pose components, including velocity and angular velocity     */
    fun applyTransform(outputPose: TrackedDevicePose.ByReference, trackedDevicePose: TrackedDevicePose.ByReference, transform: HmdMat34.ByReference) = ApplyTransform!!(outputPose, trackedDevicePose, transform)

    @JvmField
    var ApplyTransform: ApplyTransform_callback? = null

    interface ApplyTransform_callback : Callback {
        operator fun invoke(pOutputPose: TrackedDevicePose.ByReference, pTrackedDevicePose: TrackedDevicePose.ByReference, pTransform: HmdMat34.ByReference)
    }

    /** Returns the device index associated with a specific role, for example the left hand or the right hand. */
    infix fun getTrackedDeviceIndexForControllerRole(deviceType: ETrackedControllerRole) = GetTrackedDeviceIndexForControllerRole!!(deviceType.i)

    @JvmField
    var GetTrackedDeviceIndexForControllerRole: GetTrackedDeviceIndexForControllerRole_callback? = null

    interface GetTrackedDeviceIndexForControllerRole_callback : Callback {
        operator fun invoke(unDeviceType: Int): TrackedDeviceIndex
    }

    /** Returns the controller value associated with a device index. */
    infix fun getControllerRoleForTrackedDeviceIndex(deviceIndex: TrackedDeviceIndex) = ETrackedControllerRole.of(GetControllerRoleForTrackedDeviceIndex!!(deviceIndex))

    @JvmField
    var GetControllerRoleForTrackedDeviceIndex: GetControllerRoleForTrackedDeviceIndex_callback? = null

    interface GetControllerRoleForTrackedDeviceIndex_callback : Callback {
        operator fun invoke(unDeviceIndex: TrackedDeviceIndex): Int
    }


    // ------------------------------------
    // Property methods
    // ------------------------------------

    /** Returns the device class of a tracked device. If there has not been a device connected in this slot since the application
     *  started this function will return TrackedDevice_Invalid. For previous detected devices the function will return the previously
     *  observed device class.
     *
     *  To determine which devices exist on the system, just loop from 0 to openvr.lib.getK_unMaxTrackedDeviceCount and check
     *  the device class. Every device with something other than TrackedDevice_Invalid is associated with an actual tracked device. */
    infix fun getTrackedDeviceClass(deviceIndex: TrackedDeviceIndex) = ETrackedDeviceClass.of(GetTrackedDeviceClass!!(deviceIndex))

    @JvmField
    var GetTrackedDeviceClass: GetTrackedDeviceClass_callback? = null

    interface GetTrackedDeviceClass_callback : Callback {
        operator fun invoke(unDeviceIndex: TrackedDeviceIndex): Int
    }

    /** Returns true if there is a device connected in this slot. */
    infix fun isTrackedDeviceConnected(deviceIndex: TrackedDeviceIndex) = IsTrackedDeviceConnected!!(deviceIndex)

    @JvmField
    var IsTrackedDeviceConnected: IsTrackedDeviceConnected_callback? = null

    interface IsTrackedDeviceConnected_callback : Callback {
        operator fun invoke(unDeviceIndex: TrackedDeviceIndex): Boolean
    }

    /** Returns a bool property. If the device index is not valid or the property is not a bool value this function will return false. */
    @JvmOverloads
    fun getBoolTrackedDeviceProperty(deviceIndex: TrackedDeviceIndex, prop: ETrackedDeviceProperty, error: ETrackedPropertyError_ByReference? = null) = GetBoolTrackedDeviceProperty!!(deviceIndex, prop.i, error)

    @JvmField
    var GetBoolTrackedDeviceProperty: GetBoolTrackedDeviceProperty_callback? = null

    interface GetBoolTrackedDeviceProperty_callback : Callback {
        operator fun invoke(unDeviceIndex: TrackedDeviceIndex, prop: Int, pError: ETrackedPropertyError_ByReference?): Boolean
    }

    /** Returns a float property. If the device index is not valid or the property is not a float value this function will return 0. */
    @JvmOverloads
    fun getFloatTrackedDeviceProperty(deviceIndex: TrackedDeviceIndex, prop: ETrackedDeviceProperty, error: ETrackedPropertyError_ByReference? = null): Float = GetFloatTrackedDeviceProperty!!(deviceIndex, prop.i, error)

    @JvmField
    var GetFloatTrackedDeviceProperty: GetFloatTrackedDeviceProperty_callback? = null

    interface GetFloatTrackedDeviceProperty_callback : Callback {
        operator fun invoke(unDeviceIndex: TrackedDeviceIndex, prop: Int, pError: ETrackedPropertyError_ByReference?): Float
    }

    /** Returns an int property. If the device index is not valid or the property is not a int value this function will return 0. */
    @JvmOverloads
    fun getInt32TrackedDeviceProperty(deviceIndex: TrackedDeviceIndex, prop: ETrackedDeviceProperty, error: ETrackedPropertyError_ByReference? = null) = GetInt32TrackedDeviceProperty!!(deviceIndex, prop.i, error)

    @JvmField
    var GetInt32TrackedDeviceProperty: GetInt32TrackedDeviceProperty_callback? = null

    interface GetInt32TrackedDeviceProperty_callback : Callback {
        operator fun invoke(unDeviceIndex: TrackedDeviceIndex, prop: Int, pError: ETrackedPropertyError_ByReference?): Int
    }

    /** Returns a uint64 property. If the device index is not valid or the property is not a uint64 value this function will return 0. */
    @JvmOverloads
    fun getUint64TrackedDeviceProperty(deviceIndex: TrackedDeviceIndex, prop: ETrackedDeviceProperty, error: ETrackedPropertyError_ByReference? = null) = GetUint64TrackedDeviceProperty!!(deviceIndex, prop.i, error)

    @JvmField
    var GetUint64TrackedDeviceProperty: GetUint64TrackedDeviceProperty_callback? = null

    interface GetUint64TrackedDeviceProperty_callback : Callback {
        operator fun invoke(unDeviceIndex: TrackedDeviceIndex, prop: Int, pError: ETrackedPropertyError_ByReference?): Long
    }

    /** Returns a matrix property. If the device index is not valid or the property is not a matrix value, this function will return identity. */
    @JvmOverloads
    fun getMatrix34TrackedDeviceProperty(deviceIndex: TrackedDeviceIndex, prop: ETrackedDeviceProperty, error: ETrackedPropertyError_ByReference? = null) = GetMatrix34TrackedDeviceProperty!!(deviceIndex, prop.i, error)

    @JvmField
    var GetMatrix34TrackedDeviceProperty: GetMatrix34TrackedDeviceProperty_callback? = null

    interface GetMatrix34TrackedDeviceProperty_callback : Callback {
        operator fun invoke(unDeviceIndex: TrackedDeviceIndex, prop: Int, pError: ETrackedPropertyError_ByReference?): HmdMat34.ByValue
    }

    /** Returns an array of one type of property. If the device index is not valid or the property is not a single value or
     *  an array of the specified type,
     *  this function will return 0. Otherwise it returns the number of bytes necessary to hold the array of properties.
     *  If bufferSize is greater than the returned size and buffer is non-NULL, buffer is filled with the contents of array of properties. */
    fun getArrayTrackedDeviceProperty(deviceIndex: TrackedDeviceIndex, prop: ETrackedDeviceProperty, propType: PropertyTypeTag, buffer: Pointer, bufferSize: Int, error: ETrackedPropertyError_ByReference = ETrackedPropertyError_ByReference(ETrackedPropertyError.Success)) = GetArrayTrackedDeviceProperty!!(deviceIndex, prop.i, propType, buffer, bufferSize, error)

    @JvmField
    var GetArrayTrackedDeviceProperty: GetArrayTrackedDeviceProperty_callback? = null

    interface GetArrayTrackedDeviceProperty_callback : Callback {
        operator fun invoke(deviceIndex: TrackedDeviceIndex, prop: Int, propType: PropertyTypeTag, buffer: Pointer, bufferSize: Int, error: ETrackedPropertyError_ByReference = ETrackedPropertyError_ByReference(ETrackedPropertyError.Success)): Int
    }

    /** Wrapper: returns a string property. If the device index is not valid or the property is not a string value this function will
     *  return an empty String. */
    @JvmOverloads
    fun getStringTrackedDeviceProperty(deviceIndex: TrackedDeviceIndex, prop: ETrackedDeviceProperty, error: ETrackedPropertyError_ByReference? = null): String {

        val err = ETrackedPropertyError_ByReference(ETrackedPropertyError.Success)
        var ret = ""

        val bytes = ByteArray(32)
        val propLen = GetStringTrackedDeviceProperty!!(deviceIndex, prop.i, bytes, bytes.size, err)

        if (err.value == ETrackedPropertyError.Success)
            ret = String(bytes).filter { it.isLetterOrDigit() || it == '_' }
        else if (err.value == ETrackedPropertyError.BufferTooSmall) {
            val newBytes = ByteArray(propLen)
            GetStringTrackedDeviceProperty!!(deviceIndex, prop.i, newBytes, propLen, err)
            if (err.value == ETrackedPropertyError.Success)
                ret = String(newBytes).drop(1)
        }
        error?.let { it.value = err.value }
        return ret
    }

    @JvmField
    var GetStringTrackedDeviceProperty: GetStringTrackedDeviceProperty_callback? = null

    /** Returns a string property. If the device index is not valid or the property is not a string value this function will return 0.
     *  Otherwise it returns the length of the number of bytes necessary to hold this string including the trailing null.
     *  Strings will always fit in buffers of maxPropertyStringSize characters. */
    interface GetStringTrackedDeviceProperty_callback : Callback {
        operator fun invoke(unDeviceIndex: TrackedDeviceIndex, prop: Int, pchValue: ByteArray?, unBufferSize: Int,
                            pError: ETrackedPropertyError_ByReference?): Int
    }

    /** returns a string that corresponds with the specified property error. The string will be the name of the error value value for all valid error codes */
    infix fun getPropErrorNameFromEnum(error: ETrackedPropertyError) = GetPropErrorNameFromEnum!!(error.i)

    @JvmField
    var GetPropErrorNameFromEnum: GetPropErrorNameFromEnum_callback? = null

    interface GetPropErrorNameFromEnum_callback : Callback {
        operator fun invoke(error: Int): String
    }


    // ------------------------------------
    // Event methods
    // ------------------------------------

    /** Returns true and fills the event with the next event on the queue if there is one. If there are no events this method returns
     *  false. vrEvent should be the size in bytes of the openvr.lib.VREvent struct */
    fun pollNextEvent(event: VREvent.ByReference, vrEvent: Int) = PollNextEvent!!(event, vrEvent)

    @JvmField
    var PollNextEvent: PollNextEvent_callback? = null

    interface PollNextEvent_callback : Callback {
        operator fun invoke(pEvent: VREvent.ByReference, uncbVREvent: Int): Boolean
    }

    /** Returns true and fills the event with the next event on the queue if there is one. If there are no events this method returns false. Fills in the pose
     *  of the associated tracked device in the provided pose struct.
     *  This pose will always be older than the call to this function and should not be used to render the device.
     *  vrEvent should be the size in bytes of the openvr.lib.VREvent struct */
    fun pollNextEventWithPose(origin: ETrackingUniverseOrigin, event: VREvent.ByReference, vrEvent: Int, trackedDevicePose: TrackedDevicePose.ByReference) = PollNextEventWithPose!!(origin.i, event, vrEvent, trackedDevicePose)

    @JvmField
    var PollNextEventWithPose: PollNextEventWithPose_callback? = null

    interface PollNextEventWithPose_callback : Callback {
        operator fun invoke(eOrigin: Int, pEvent: VREvent.ByReference, uncbVREvent: Int, pTrackedDevicePose: TrackedDevicePose.ByReference): Boolean
    }

    /** returns the name of an EVREvent value value */
    infix fun getEventTypeNameFromEnum(type: EVREventType) = GetEventTypeNameFromEnum!!(type.i)

    @JvmField
    var GetEventTypeNameFromEnum: GetEventTypeNameFromEnum_callback? = null

    interface GetEventTypeNameFromEnum_callback : Callback {
        operator fun invoke(eType: Int): String
    }


    // ------------------------------------
    // Rendering helper methods
    // ------------------------------------

    /** Returns the hidden area mesh for the current HMD. The pixels covered by this mesh will never be seen by the user after the lens
     *  distortion is applied based on visibility to the panels. If this HMD does not have a hidden area mesh, the vertex data and
     *  count will be NULL and 0 respectively.
     *  This mesh is meant to be rendered into the stencil buffer (or into the depth buffer setting nearz) before rendering each eye's
     *  view.
     *  This will improve performance by letting the GPU early-reject pixels the user will never see before running the pixel shader.
     *  NOTE: Render this mesh with backface culling disabled since the winding order of the vertices can be different per-HMD or
     *  per-eye.
     *  Setting the bInverse argument to true will produce the visible area mesh that is commonly used in place of full-screen quads.
     *  The visible area mesh covers all of the pixels the hidden area mesh does not cover.
     *  Setting the bLineLoop argument will return a line loop of vertices in HiddenAreaMesh->vertexData with
     *  HiddenAreaMesh->triangleCount set to the number of vertices.
     */
    @JvmOverloads
    fun getHiddenAreaMesh(eye: EVREye, type: EHiddenAreaMeshType = EHiddenAreaMeshType.Standard) = GetHiddenAreaMesh!!(eye.i, type)

    @JvmField
    var GetHiddenAreaMesh: GetHiddenAreaMesh_callback? = null

    interface GetHiddenAreaMesh_callback : Callback {
        operator fun invoke(eEye: Int, type: EHiddenAreaMeshType): HiddenAreaMesh.ByValue
    }


    // ------------------------------------
    // Controller methods
    // ------------------------------------

    /** Fills the supplied struct with the current state of the controller. Returns false if the controller index is invalid. */
    fun getControllerState(controllerDeviceIndex: TrackedDeviceIndex, controllerState: VRControllerState.ByReference, controllerStateSize: Int) = GetControllerState!!(controllerDeviceIndex, controllerState, controllerStateSize)

    @JvmField
    var GetControllerState: GetControllerState_callback? = null

    interface GetControllerState_callback : Callback {
        operator fun invoke(unControllerDeviceIndex: TrackedDeviceIndex, pControllerState: VRControllerState.ByReference, unControllerStateSize: Int): Boolean
    }

    /** Fills the supplied struct with the current state of the controller and the provided pose with the pose of the controller when the controller state was
     *  updated most recently. Use this form if you need a precise controller pose as input to your application when the user presses or releases a button. */
    fun getControllerStateWithPose(origin: ETrackingUniverseOrigin, controllerDeviceIndex: TrackedDeviceIndex, controllerState: VRControllerState.ByReference, controllerStateSize: Int, trackedDevicePose: TrackedDevicePose.ByReference) = GetControllerStateWithPose!!(origin.i, controllerDeviceIndex, controllerState, controllerStateSize, trackedDevicePose)

    @JvmField
    var GetControllerStateWithPose: GetControllerStateWithPose_callback? = null

    interface GetControllerStateWithPose_callback : Callback {
        operator fun invoke(eOrigin: Int, unControllerDeviceIndex: TrackedDeviceIndex, pControllerState: VRControllerState.ByReference, unControllerStateSize: Int, pTrackedDevicePose: TrackedDevicePose.ByReference): Boolean
    }

    /** Trigger a single haptic pulse on a controller. After this call the application may not trigger another haptic pulse on this controller and axis
     *  combination for 5ms. */
    fun triggerHapticPulse(controllerDeviceIndex: TrackedDeviceIndex, axisId: Int, durationMicroSec: Short) = TriggerHapticPulse!!(controllerDeviceIndex, axisId, durationMicroSec)

    @JvmField
    var TriggerHapticPulse: TriggerHapticPulse_callback? = null

    interface TriggerHapticPulse_callback : Callback {
        operator fun invoke(unControllerDeviceIndex: TrackedDeviceIndex, unAxisId: Int, usDurationMicroSec: Short)
    }

    /** returns the name of an openvr.lib.EVRButtonId value value */
    infix fun getButtonIdNameFromEnum(buttonId: EVRButtonId) = GetButtonIdNameFromEnum!!(buttonId.i)

    @JvmField
    var GetButtonIdNameFromEnum: GetButtonIdNameFromEnum_callback? = null

    interface GetButtonIdNameFromEnum_callback : Callback {
        operator fun invoke(eButtonId: Int): String
    }

    /** returns the game of an openvr.lib.EVRControllerAxisType value value */
    infix fun getControllerAxisTypeNameFromEnum(axisType: EVRControllerAxisType) = GetControllerAxisTypeNameFromEnum!!(axisType.i)

    @JvmField
    var GetControllerAxisTypeNameFromEnum: GetControllerAxisTypeNameFromEnum_callback? = null

    interface GetControllerAxisTypeNameFromEnum_callback : Callback {
        operator fun invoke(eAxisType: Int): String
    }

    /** Returns true if this application is receiving input from the system. This would return false if system-related
     *  functionality is consuming the input stream. */
    val isInputAvailable get() = IsInputAvailable!!()

    @JvmField
    var IsInputAvailable: IsInputAvailable_callback? = null

    interface IsInputAvailable_callback : Callback {
        operator fun invoke(): Boolean
    }


    /** Returns true SteamVR is drawing controllers on top of the application. Applications should consider not drawing
     *  anything attached to the user's hands in this case. */
    val isSteamVRDrawingControllers get() = IsSteamVRDrawingControllers!!()

    @JvmField
    var IsSteamVRDrawingControllers: IsSteamVRDrawingControllers_callback? = null

    interface IsSteamVRDrawingControllers_callback : Callback {
        operator fun invoke(): Boolean
    }

    /** Tells openvr.OpenVR that this process no longer wants exclusive access to button states and button events. Other apps will be notified that input focus has
     *  been released with a VREvent_InputFocusReleased event. */
    fun releaseInputFocus() = ReleaseInputFocus!!()

    @JvmField
    var ReleaseInputFocus: ReleaseInputFocus_callback? = null

    interface ReleaseInputFocus_callback : Callback {
        operator fun invoke()
    }

    /** Returns true if the user has put SteamVR into a mode that is distracting them from the application.
     *  For applications where this is appropriate, the application should pause ongoing activity. */
    val shouldApplicationPause get() = ShouldApplicationPause!!()

    @JvmField
    var ShouldApplicationPause: ShouldApplicationPause_callback? = null

    interface ShouldApplicationPause_callback : Callback {
        operator fun invoke(): Boolean
    }

    /** Returns true if SteamVR is doing significant rendering work and the game should do what it can to reduce its
     *  own workload. One common way to do this is to reduce the size of the render target provided for each eye. */
    val shouldApplicationReduceRenderingWork get() = ShouldApplicationReduceRenderingWork!!()

    @JvmField
    var ShouldApplicationReduceRenderingWork: ShouldApplicationReduceRenderingWork_callback? = null

    interface ShouldApplicationReduceRenderingWork_callback : Callback {
        operator fun invoke(): Boolean
    }


    // ------------------------------------
    // Debug Methods
    // ------------------------------------

    /** Sends a request to the driver for the specified device and returns the response. The maximum response size is 32k, but this method can be called with
     *  a smaller buffer. If the response exceeds the size of the buffer, it is truncated.
     *  The size of the response including its terminating null is returned. */
    fun driverDebugRequest(deviceIndex: TrackedDeviceIndex, request: String, responseBuffer: String, responseBufferSize: Int) = DriverDebugRequest!!(deviceIndex, request, responseBuffer, responseBufferSize)

    @JvmField
    var DriverDebugRequest: DriverDebugRequest_callback? = null

    interface DriverDebugRequest_callback : Callback {
        operator fun invoke(unDeviceIndex: TrackedDeviceIndex, pchRequest: String, pchResponseBuffer: String, unResponseBufferSize: Int): Int
    }


    // ------------------------------------
    // Firmware methods
    // ------------------------------------

    /** Performs the actual firmware update if applicable.
     *  The following events will be sent, if VRFirmwareError_None was returned: VREvent_FirmwareUpdateStarted, VREvent_FirmwareUpdateFinished
     *  Use the properties Prop_Firmware_UpdateAvailable_Bool, Prop_Firmware_ManualUpdate_Bool, and Prop_Firmware_ManualUpdateURL_String to figure our whether
     *  a firmware update is available, and to figure out whether its a manual update
     *  Prop_Firmware_ManualUpdateURL_String should point to an URL describing the manual update process */
    fun performFirmwareUpdate(deviceIndex: TrackedDeviceIndex) = EVRFirmwareError.of(PerformFirmwareUpdate!!(deviceIndex))

    @JvmField
    var PerformFirmwareUpdate: PerformFirmwareUpdate_callback? = null

    interface PerformFirmwareUpdate_callback : Callback {
        operator fun invoke(unDeviceIndex: TrackedDeviceIndex): Int
    }


    // ------------------------------------
    // Application life cycle methods
    // ------------------------------------

    /** Call this to acknowledge to the system that VREvent_Quit has been received and that the process is exiting.
     *  This extends the timeout until the process is killed. */
    fun acknowledgeQuit_Exiting() = AcknowledgeQuit_Exiting!!()

    @JvmField
    var AcknowledgeQuit_Exiting: AcknowledgeQuit_Exiting_callback? = null

    interface AcknowledgeQuit_Exiting_callback : Callback {
        operator fun invoke()
    }

    /** Call this to tell the system that the user is being prompted to save data. This halts the timeout and dismisses the dashboard (if it was up).
     *  Applications should be sure to actually prompt the user to save and then exit afterward, otherwise the user will be left in a confusing state. */
    fun acknowledgeQuit_UserPrompt() = AcknowledgeQuit_UserPrompt!!()

    @JvmField
    var AcknowledgeQuit_UserPrompt: AcknowledgeQuit_UserPrompt_callback? = null

    interface AcknowledgeQuit_UserPrompt_callback : Callback {
        operator fun invoke()
    }

    constructor()

    override fun getFieldOrder(): List<String> = Arrays.asList("GetRecommendedRenderTargetSize", "GetProjectionMatrix",
            "GetProjectionRaw", "ComputeDistortion", "GetEyeToHeadTransform", "GetTimeSinceLastVsync", "GetD3D9AdapterIndex",
            "GetDXGIOutputInfo", "GetOutputDevice", "IsDisplayOnDesktop", "SetDisplayVisibility", "GetDeviceToAbsoluteTrackingPose",
            "ResetSeatedZeroPose", "GetSeatedZeroPoseToStandingAbsoluteTrackingPose", "GetRawZeroPoseToStandingAbsoluteTrackingPose",
            "GetSortedTrackedDeviceIndicesOfClass", "GetTrackedDeviceActivityLevel", "ApplyTransform",
            "GetTrackedDeviceIndexForControllerRole", "GetControllerRoleForTrackedDeviceIndex", "GetTrackedDeviceClass",
            "IsTrackedDeviceConnected", "GetBoolTrackedDeviceProperty", "GetFloatTrackedDeviceProperty", "GetInt32TrackedDeviceProperty",
            "GetUint64TrackedDeviceProperty", "GetMatrix34TrackedDeviceProperty", "GetArrayTrackedDeviceProperty",
            "GetStringTrackedDeviceProperty", "GetPropErrorNameFromEnum", "PollNextEvent", "PollNextEventWithPose",
            "GetEventTypeNameFromEnum", "GetHiddenAreaMesh", "GetControllerState", "GetControllerStateWithPose", "TriggerHapticPulse",
            "GetButtonIdNameFromEnum", "GetControllerAxisTypeNameFromEnum", "IsInputAvailable", "IsSteamVRDrawingControllers",
            "ReleaseInputFocus", "ShouldApplicationPause", "ShouldApplicationReduceRenderingWork", "DriverDebugRequest",
            "PerformFirmwareUpdate", "AcknowledgeQuit_Exiting", "AcknowledgeQuit_UserPrompt")

    constructor (peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRSystem(), Structure.ByReference
    class ByValue : IVRSystem(), Structure.ByValue
}

val IVRSystem_Version = "IVRSystem_019"

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
        fun of(i: Int) = values().first { it.i == i }
    }
}

class EVRApplicationError_ByReference(val value: EVRApplicationError = EVRApplicationError.None) : IntByReference(value.i)

/** The maximum length of an application key */
val k_unMaxApplicationKeyLength = 128

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
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** These are states the scene application startup process will go through. */
enum class EVRApplicationTransitionState(@JvmField val i: Int) {

    None(0),

    OldAppQuitSent(10),
    WaitingForExternalLaunch(11),

    NewAppLaunched(20);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

fun Int.toEVRApplicationTransitionState() = EVRApplicationTransitionState.values().first { it.i == this }

open class AppOverrideKeys_t : Structure {

    @JvmField
    var pchKey = "" // TODO check
    @JvmField
    var pchValue = ""

    constructor()

    constructor(pchKey: String, pchValue: String) {
        this.pchKey = pchKey
        this.pchValue = pchValue
    }

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    override fun getFieldOrder(): List<String> = Arrays.asList("pchKey", "pchValue")

    class ByReference : AppOverrideKeys_t(), Structure.ByReference
    class ByValue : AppOverrideKeys_t(), Structure.ByValue
}

/** Currently recognized mime types */
val k_pch_MimeType_HomeApp = "vr/home"
val k_pch_MimeType_GameTheater = "vr/game_theater"