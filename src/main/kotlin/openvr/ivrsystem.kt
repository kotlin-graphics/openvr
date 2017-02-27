package openvr

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.FloatByReference
import com.sun.jna.ptr.IntByReference
import com.sun.jna.ptr.LongByReference
import java.util.*

// ivrsystem.h ====================================================================================================================================================

open class IVRSystem : Structure {

    // ------------------------------------
    // Display Methods
    // ------------------------------------

    /** Suggested size for the intermediate render target that the distortion pulls from. */
    fun getRecommendedRenderTargetSize(pnWidth: IntByReference, pnHeight: IntByReference)
            = GetRecommendedRenderTargetSize!!.invoke(pnWidth, pnHeight)

    @JvmField var GetRecommendedRenderTargetSize: GetRecommendedRenderTargetSize_callback? = null

    interface GetRecommendedRenderTargetSize_callback : Callback {
        fun invoke(pnWidth: IntByReference, pnHeight: IntByReference)
    }

    /** The projection matrix for the specified eye */
    fun getProjectionMatrix(eEye: EVREye, fNearZ: Float, fFarZ: Float) = GetProjectionMatrix!!.invoke(eEye.i, fNearZ, fFarZ)

    @JvmField var GetProjectionMatrix: GetProjectionMatrix_callback? = null

    interface GetProjectionMatrix_callback : Callback {
        fun invoke(eEye: Int, fNearZ: Float, fFarZ: Float): HmdMatrix44_t.ByValue
    }

    /** The components necessary to build your own projection matrix in case your application is doing something fancy like infinite Z  */
    fun getProjectionRaw(eEye: EVREye, pfLeft: FloatByReference, pfRight: FloatByReference, pfTop: FloatByReference, pfBottom: FloatByReference)
            = GetProjectionRaw!!.invoke(eEye.i, pfLeft, pfRight, pfTop, pfBottom)

    @JvmField var GetProjectionRaw: GetProjectionRaw_callback? = null

    interface GetProjectionRaw_callback : Callback {
        fun invoke(eEye: Int, pfLeft: FloatByReference, pfRight: FloatByReference, pfTop: FloatByReference, pfBottom: FloatByReference)
    }

    /** Gets the result of the distortion function for the specified eye and input UVs.
     *  UVs go from 0,0 in the upper left of that eye's viewport and 1,1 in the lower right of that eye's viewport.
     *  Returns true for success. Otherwise, returns false, and distortion coordinates are not suitable.    */
    fun computeDistortion(eEye: EVREye, fU: Float, fV: Float, pDistortionCoordinates_t: DistortionCoordinates_t.ByReference)
            = ComputeDistortion!!.invoke(eEye.i, fU, fV, pDistortionCoordinates_t)

    @JvmField var ComputeDistortion: ComputeDistortion_callback? = null

    interface ComputeDistortion_callback : Callback {
        fun invoke(eEye: Int, fU: Float, fV: Float, pDistortionCoordinates_t: DistortionCoordinates_t.ByReference): Boolean
    }

    /** Returns the transform from eye space to the head space. Eye space is the per-eye flavor of head space that provides stereo disparity.
     *  Instead of Model * View * Projection the sequence is Model * View * Eye^-1 * Projection.
     *  Normally View and Eye^-1 will be multiplied together and treated as View in your application.   */
    fun getEyeToHeadTransform(eEye: EVREye) = GetEyeToHeadTransform!!.invoke(eEye.i)

    @JvmField var GetEyeToHeadTransform: GetEyeToHeadTransform_callback? = null

    interface GetEyeToHeadTransform_callback : Callback {
        fun invoke(eEye: Int): HmdMatrix34_t.ByValue
    }

    /** Returns the number of elapsed seconds since the last recorded vsync event. This will come from a vsync timer event in the timer if possible or from the
     *  application-reported time if that is not available.
     *  If no vsync times are available the function will return zero for vsync time and frame counter and return false from the method.    */
    fun getTimeSinceLastVsync(pfSecondsSinceLastVsync: FloatByReference, pulFrameCounter: LongByReference)
            = GetTimeSinceLastVsync!!.invoke(pfSecondsSinceLastVsync, pulFrameCounter)

    @JvmField var GetTimeSinceLastVsync: GetTimeSinceLastVsync_callback? = null

    interface GetTimeSinceLastVsync_callback : Callback {
        fun invoke(pfSecondsSinceLastVsync: FloatByReference, pulFrameCounter: LongByReference): Boolean
    }

    /** [D3D9 Only]
     * Returns the adapter index that the user should pass into CreateDevice to set up D3D9 in such a way that it can go full screen exclusive on the HMD.
     * Returns -1 if there was an error.     */
    fun getD3D9AdapterIndex() = GetD3D9AdapterIndex!!.invoke()

    @JvmField var GetD3D9AdapterIndex: GetD3D9AdapterIndex_callback? = null

    interface GetD3D9AdapterIndex_callback : Callback {
        fun invoke(): Int
    }

    /** [D3D10/11 Only]
     * Returns the adapter index that the user should pass into EnumAdapters to create the device and swap chain in DX10 and DX11.
     * If an error occurs the index will be set to -1.  */
    fun getDXGIOutputInfo(pnAdapterIndex: IntByReference) = GetDXGIOutputInfo!!.invoke(pnAdapterIndex)

    @JvmField var GetDXGIOutputInfo: GetDXGIOutputInfo_callback? = null

    interface GetDXGIOutputInfo_callback : Callback {
        fun invoke(pnAdapterIndex: IntByReference)
    }


    // ------------------------------------
    // Display Mode methods
    // ------------------------------------

    /** Use to determine if the headset display is part of the desktop (i.e. extended) or hidden (i.e. direct mode). */
    fun isDisplayOnDesktop() = IsDisplayOnDesktop!!.invoke()

    @JvmField var IsDisplayOnDesktop: IsDisplayOnDesktop_callback? = null

    interface IsDisplayOnDesktop_callback : Callback {
        fun invoke(): Boolean
    }

    /** Set the display visibility (true = extended, false = direct mode).  Return value of true indicates that the change was successful. */
    fun setDisplayVisibility(bIsVisibleOnDesktop: Boolean): Boolean = SetDisplayVisibility!!.invoke(bIsVisibleOnDesktop)

    @JvmField var SetDisplayVisibility: SetDisplayVisibility_callback? = null

    interface SetDisplayVisibility_callback : Callback {
        fun invoke(bIsVisibleOnDesktop: Boolean): Boolean
    }


    // ------------------------------------
    // Tracking Methods
    // ------------------------------------

    /** The pose that the tracker thinks that the HMD will be in at the specified number of seconds into the future. Pass 0 to get the state at the instant the
     *  method is called. Most of the time the application should calculate the time until the photons will be emitted from the display and pass that time into
     *  the method.
     *
     *  This is roughly analogous to the inverse of the view matrix in most applications, though many games will need to do some additional rotation or
     *  translation on top of the rotation and translation provided by the head pose.
     *
     *  For devices where bPoseIsValid is true the application can use the pose to position the device in question. The provided array can be any size up to
     *  openvr.k_unMaxTrackedDeviceCount.
     *
     *  Seated experiences should call this method with TrackingUniverseSeated and receive poses relative to the seated zero pose. Standing experiences should
     *  call this method with TrackingUniverseStanding and receive poses relative to the Chaperone Play Area. TrackingUniverseRawAndUncalibrated should probably
     *  not be used unless the application is the Chaperone calibration tool itself, but will provide poses relative to the hardware-specific coordinate system
     *  in the driver.     */
    fun GetDeviceToAbsoluteTrackingPose(eOrigin: ETrackingUniverseOrigin, fPredictedSecondsToPhotonsFromNow: Float,
                                        pTrackedDevicePoseArray: TrackedDevicePose_t.ByReference, unTrackedDevicePoseArrayCount: Int)
            = GetDeviceToAbsoluteTrackingPose!!.invoke(eOrigin.i, fPredictedSecondsToPhotonsFromNow, pTrackedDevicePoseArray, unTrackedDevicePoseArrayCount)

    @JvmField var GetDeviceToAbsoluteTrackingPose: GetDeviceToAbsoluteTrackingPose_callback? = null

    interface GetDeviceToAbsoluteTrackingPose_callback : Callback {
        fun invoke(eOrigin: Int, fPredictedSecondsToPhotonsFromNow: Float, pTrackedDevicePoseArray: TrackedDevicePose_t.ByReference,
                   unTrackedDevicePoseArrayCount: Int)
    }

    /** Sets the zero pose for the seated tracker coordinate system to the current position and yaw of the HMD. After ResetSeatedZeroPose all
     *  GetDeviceToAbsoluteTrackingPose calls that pass TrackingUniverseSeated as the origin will be relative to this new zero pose. The new zero coordinate
     *  system will not change the fact that the Y axis is up in the real world, so the next pose returned from GetDeviceToAbsoluteTrackingPose after a call to
     *  ResetSeatedZeroPose may not be exactly an identity matrix.
     *
     *  NOTE: This function overrides the user's previously saved seated zero pose and should only be called as the result of a user action.
     *  Users are also able to set their seated zero pose via the openvr.OpenVR Dashboard.     **/
    fun resetSeatedZeroPose() = ResetSeatedZeroPose!!.invoke()

    @JvmField var ResetSeatedZeroPose: ResetSeatedZeroPose_callback? = null

    interface ResetSeatedZeroPose_callback : Callback {
        fun invoke()
    }

    /** Returns the transform from the seated zero pose to the standing absolute tracking system. This allows applications to represent the seated origin to
     *  used or transform object positions from one coordinate system to the other.
     *
     *  The seated origin may or may not be inside the Play Area or Collision Bounds returned by openvr.IVRChaperone. Its position depends on what the user has set
     *  from the Dashboard settings and previous calls to ResetSeatedZeroPose. */
    fun getSeatedZeroPoseToStandingAbsoluteTrackingPose() = GetSeatedZeroPoseToStandingAbsoluteTrackingPose!!.invoke()

    @JvmField var GetSeatedZeroPoseToStandingAbsoluteTrackingPose: GetSeatedZeroPoseToStandingAbsoluteTrackingPose_callback? = null

    interface GetSeatedZeroPoseToStandingAbsoluteTrackingPose_callback : Callback {
        fun invoke(): HmdMatrix34_t.ByValue
    }

    /** Returns the transform from the tracking origin to the standing absolute tracking system. This allows applications to convert from raw tracking space to
     *  the calibrated standing coordinate system. */
    fun getRawZeroPoseToStandingAbsoluteTrackingPose() = GetRawZeroPoseToStandingAbsoluteTrackingPose!!.invoke()

    @JvmField var GetRawZeroPoseToStandingAbsoluteTrackingPose: GetRawZeroPoseToStandingAbsoluteTrackingPose_callback? = null

    interface GetRawZeroPoseToStandingAbsoluteTrackingPose_callback : Callback {
        fun invoke(): HmdMatrix34_t.ByValue
    }

    /** Get a sorted array of device indices of a given class of tracked devices (e.g. controllers).  Devices are sorted right to left relative to the specified
     *  tracked device (default: hmd -- pass in -1 for absolute tracking space).  Returns the number of devices in the list, or the size of the array needed if
     *  not large enough. */
    @JvmOverloads fun getSortedTrackedDeviceIndicesOfClass(eTrackedDeviceClass: ETrackedDeviceClass,
                                                           punTrackedDeviceIndexArray: TrackedDeviceIndex_t_ByReference,
                                                           unTrackedDeviceIndexArrayCount: Int,
                                                           unRelativeToTrackedDeviceIndex: TrackedDeviceIndex_t = k_unTrackedDeviceIndex_Hmd)
            = GetSortedTrackedDeviceIndicesOfClass!!.invoke(eTrackedDeviceClass.i, punTrackedDeviceIndexArray, unTrackedDeviceIndexArrayCount,
            unRelativeToTrackedDeviceIndex)

    @JvmField var GetSortedTrackedDeviceIndicesOfClass: GetSortedTrackedDeviceIndicesOfClass_callback? = null

    interface GetSortedTrackedDeviceIndicesOfClass_callback : Callback {
        fun invoke(eTrackedDeviceClass: Int, punTrackedDeviceIndexArray: TrackedDeviceIndex_t_ByReference, unTrackedDeviceIndexArrayCount: Int,
                   unRelativeToTrackedDeviceIndex: TrackedDeviceIndex_t): Int
    }

    /** Returns the level of activity on the device. */
    fun getTrackedDeviceActivityLevel(unDeviceId: TrackedDeviceIndex_t) = EDeviceActivityLevel.of(GetTrackedDeviceActivityLevel!!.invoke(unDeviceId))

    @JvmField var GetTrackedDeviceActivityLevel: GetTrackedDeviceActivityLevel_callback? = null

    interface GetTrackedDeviceActivityLevel_callback : Callback {
        fun invoke(unDeviceId: TrackedDeviceIndex_t): Int
    }

    /** Convenience utility to apply the specified transform to the specified pose.
     *  This properly transforms all pose components, including velocity and angular velocity     */
    fun invokeTransform(pOutputPose: TrackedDevicePose_t.ByReference, pTrackedDevicePose: TrackedDevicePose_t.ByReference, pTransform: HmdMatrix34_t.ByReference)
            = ApplyTransform!!.invoke(pOutputPose, pTrackedDevicePose, pTransform)

    @JvmField var ApplyTransform: ApplyTransform_callback? = null

    interface ApplyTransform_callback : Callback {
        fun invoke(pOutputPose: TrackedDevicePose_t.ByReference, pTrackedDevicePose: TrackedDevicePose_t.ByReference, pTransform: HmdMatrix34_t.ByReference)
    }

    /** Returns the device index associated with a specific role, for example the left hand or the right hand. */
    fun getTrackedDeviceIndexForControllerRole(unDeviceType: ETrackedControllerRole) = GetTrackedDeviceIndexForControllerRole!!.invoke(unDeviceType.i)

    @JvmField var GetTrackedDeviceIndexForControllerRole: GetTrackedDeviceIndexForControllerRole_callback? = null

    interface GetTrackedDeviceIndexForControllerRole_callback : Callback {
        fun invoke(unDeviceType: Int): TrackedDeviceIndex_t
    }

    /** Returns the controller value associated with a device index. */
    fun getControllerRoleForTrackedDeviceIndex(unDeviceIndex: TrackedDeviceIndex_t)
            = ETrackedControllerRole.of(GetControllerRoleForTrackedDeviceIndex!!.invoke(unDeviceIndex))

    @JvmField var GetControllerRoleForTrackedDeviceIndex: GetControllerRoleForTrackedDeviceIndex_callback? = null

    interface GetControllerRoleForTrackedDeviceIndex_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t): Int
    }


    // ------------------------------------
    // Property methods
    // ------------------------------------

    /** Returns the device class of a tracked device. If there has not been a device connected in this slot since the application started this function will
     *  return TrackedDevice_Invalid. For previous detected devices the function will return the previously observed device class.
     *
     * To determine which devices exist on the system, just loop from 0 to openvr.k_unMaxTrackedDeviceCount and check the device class. Every device with something
     * other than TrackedDevice_Invalid is associated with an actual tracked device. */
    fun getTrackedDeviceClass(unDeviceIndex: TrackedDeviceIndex_t) = ETrackedDeviceClass.of(GetTrackedDeviceClass!!.invoke(unDeviceIndex))

    @JvmField var GetTrackedDeviceClass: GetTrackedDeviceClass_callback? = null

    interface GetTrackedDeviceClass_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t): Int
    }

    /** Returns true if there is a device connected in this slot. */
    fun isTrackedDeviceConnected(unDeviceIndex: TrackedDeviceIndex_t) = IsTrackedDeviceConnected!!.invoke(unDeviceIndex)

    @JvmField var IsTrackedDeviceConnected: IsTrackedDeviceConnected_callback? = null

    interface IsTrackedDeviceConnected_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t): Boolean
    }

    /** Returns a bool property. If the device index is not valid or the property is not a bool value this function will return false. */
    @JvmOverloads fun getBoolTrackedDeviceProperty(unDeviceIndex: TrackedDeviceIndex_t, prop: ETrackedDeviceProperty,
                                                   pError: ETrackedPropertyError_ByReference? = null)
            = GetBoolTrackedDeviceProperty!!.invoke(unDeviceIndex, prop.i, pError)

    @JvmField var GetBoolTrackedDeviceProperty: GetBoolTrackedDeviceProperty_callback? = null

    interface GetBoolTrackedDeviceProperty_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: ETrackedPropertyError_ByReference?): Boolean
    }

    /** Returns a float property. If the device index is not valid or the property is not a float value this function will return 0. */
    @JvmOverloads fun getFloatTrackedDeviceProperty(unDeviceIndex: TrackedDeviceIndex_t, prop: ETrackedDeviceProperty,
                                                    pError: ETrackedPropertyError_ByReference? = null): Float
            = GetFloatTrackedDeviceProperty!!.invoke(unDeviceIndex, prop.i, pError)

    @JvmField var GetFloatTrackedDeviceProperty: GetFloatTrackedDeviceProperty_callback? = null

    interface GetFloatTrackedDeviceProperty_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: ETrackedPropertyError_ByReference?): Float
    }

    /** Returns an int property. If the device index is not valid or the property is not a int value this function will return 0. */
    @JvmOverloads fun getInt32TrackedDeviceProperty(unDeviceIndex: TrackedDeviceIndex_t, prop: ETrackedDeviceProperty,
                                                    pError: ETrackedPropertyError_ByReference? = null)
            = GetInt32TrackedDeviceProperty!!.invoke(unDeviceIndex, prop.i, pError)

    @JvmField var GetInt32TrackedDeviceProperty: GetInt32TrackedDeviceProperty_callback? = null

    interface GetInt32TrackedDeviceProperty_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: ETrackedPropertyError_ByReference?): Int
    }

    /** Returns a uint64 property. If the device index is not valid or the property is not a uint64 value this function will return 0. */
    @JvmOverloads fun getUint64TrackedDeviceProperty(unDeviceIndex: TrackedDeviceIndex_t, prop: ETrackedDeviceProperty,
                                                     pError: ETrackedPropertyError_ByReference? = null)
            = GetUint64TrackedDeviceProperty!!.invoke(unDeviceIndex, prop.i, pError)

    @JvmField var GetUint64TrackedDeviceProperty: GetUint64TrackedDeviceProperty_callback? = null

    interface GetUint64TrackedDeviceProperty_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: ETrackedPropertyError_ByReference?): Long
    }

    /** Returns a matrix property. If the device index is not valid or the property is not a matrix value, this function will return identity. */
    @JvmOverloads fun getMatrix34TrackedDeviceProperty(unDeviceIndex: TrackedDeviceIndex_t, prop: ETrackedDeviceProperty,
                                                       pError: ETrackedPropertyError_ByReference? = null)
            = GetMatrix34TrackedDeviceProperty!!.invoke(unDeviceIndex, prop.i, pError)

    @JvmField var GetMatrix34TrackedDeviceProperty: GetMatrix34TrackedDeviceProperty_callback? = null

    interface GetMatrix34TrackedDeviceProperty_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pError: ETrackedPropertyError_ByReference?): HmdMatrix34_t.ByValue
    }

    /** Wrapper: returns a string property. If the device index is not valid or the property is not a string value this function will
     *  return an empty String. */
    @JvmOverloads fun getStringTrackedDeviceProperty(unDeviceIndex: TrackedDeviceIndex_t, prop: ETrackedDeviceProperty,
                                                     pError: ETrackedPropertyError_ByReference? = null): String {

        val err = ETrackedPropertyError_ByReference(ETrackedPropertyError.Success)
        var ret = ""

        val bytes = ByteArray(32)
        val propLen = GetStringTrackedDeviceProperty!!.invoke(unDeviceIndex, prop.i, bytes, bytes.size, err)

        if (err.value == ETrackedPropertyError.Success)
            ret = String(bytes).filter { it.isLetterOrDigit() || it == '_' }
        else if (err.value == ETrackedPropertyError.BufferTooSmall) {
            val newBytes = ByteArray(propLen)
            GetStringTrackedDeviceProperty!!.invoke(unDeviceIndex, prop.i, newBytes, propLen, err)
            if (err.value == ETrackedPropertyError.Success)
                ret = String(newBytes).drop(1)
        }
        pError?.let {
            it.value = err.value
        }
        return ret
    }

    @JvmField var GetStringTrackedDeviceProperty: GetStringTrackedDeviceProperty_callback? = null

    /** Returns a string property. If the device index is not valid or the property is not a string value this function will return 0.
     *  Otherwise it returns the length of the number of bytes necessary to hold this string including the trailing null.
     *  Strings will always fit in buffers of k_unMaxPropertyStringSize characters. */
    interface GetStringTrackedDeviceProperty_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t, prop: Int, pchValue: ByteArray?, unBufferSize: Int,
                   pError: ETrackedPropertyError_ByReference?): Int
    }

    /** returns a string that corresponds with the specified property error. The string will be the name of the error value value for all valid error codes */
    fun getPropErrorNameFromEnum(error: ETrackedPropertyError) = GetPropErrorNameFromEnum!!.invoke(error.i)

    @JvmField var GetPropErrorNameFromEnum: GetPropErrorNameFromEnum_callback? = null

    interface GetPropErrorNameFromEnum_callback : Callback {
        fun invoke(error: Int): String
    }


    // ------------------------------------
    // Event methods
    // ------------------------------------

    /** Returns true and fills the event with the next event on the queue if there is one. If there are no events this method returns
     *  false. uncbVREvent should be the size in bytes of the openvr.VREvent_t struct */
    fun pollNextEvent(pEvent: VREvent_t.ByReference, uncbVREvent: Int) = PollNextEvent!!.invoke(pEvent, uncbVREvent)

    @JvmField var PollNextEvent: PollNextEvent_callback? = null

    interface PollNextEvent_callback : Callback {
        fun invoke(pEvent: VREvent_t.ByReference, uncbVREvent: Int): Boolean
    }

    /** Returns true and fills the event with the next event on the queue if there is one. If there are no events this method returns false. Fills in the pose
     *  of the associated tracked device in the provided pose struct.
     *  This pose will always be older than the call to this function and should not be used to render the device.
     *  uncbVREvent should be the size in bytes of the openvr.VREvent_t struct */
    fun pollNextEventWithPose(eOrigin: ETrackingUniverseOrigin, pEvent: VREvent_t.ByReference, uncbVREvent: Int,
                              pTrackedDevicePose: TrackedDevicePose_t.ByReference)
            = PollNextEventWithPose!!.invoke(eOrigin.i, pEvent, uncbVREvent, pTrackedDevicePose)

    @JvmField var PollNextEventWithPose: PollNextEventWithPose_callback? = null

    interface PollNextEventWithPose_callback : Callback {
        fun invoke(eOrigin: Int, pEvent: VREvent_t.ByReference, uncbVREvent: Int, pTrackedDevicePose: TrackedDevicePose_t.ByReference): Boolean
    }

    /** returns the name of an EVREvent value value */
    fun getEventTypeNameFromEnum(eType: EVREventType) = GetEventTypeNameFromEnum!!.invoke(eType.i)

    @JvmField var GetEventTypeNameFromEnum: GetEventTypeNameFromEnum_callback? = null

    interface GetEventTypeNameFromEnum_callback : Callback {
        fun invoke(eType: Int): String
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
     *  Setting the bLineLoop argument will return a line loop of vertices in HiddenAreaMesh_t->pVertexData with
     *  HiddenAreaMesh_t->unTriangleCount set to the number of vertices.
     */
    @JvmOverloads fun GetHiddenAreaMesh(eEye: EVREye, type: EHiddenAreaMeshType = EHiddenAreaMeshType.Standard)
            = GetHiddenAreaMesh!!.invoke(eEye.i, type)

    @JvmField var GetHiddenAreaMesh: GetHiddenAreaMesh_callback? = null

    interface GetHiddenAreaMesh_callback : Callback {
        fun invoke(eEye: Int, type: EHiddenAreaMeshType): HiddenAreaMesh_t.ByValue
    }


    // ------------------------------------
    // Controller methods
    // ------------------------------------

    /** Fills the supplied struct with the current state of the controller. Returns false if the controller index is invalid. */
    fun getControllerState(unControllerDeviceIndex: TrackedDeviceIndex_t, pControllerState: VRControllerState_t.ByReference,
                           unControllerStateSize: Int)
            = GetControllerState!!.invoke(unControllerDeviceIndex, pControllerState, unControllerStateSize)

    @JvmField var GetControllerState: GetControllerState_callback? = null

    interface GetControllerState_callback : Callback {
        fun invoke(unControllerDeviceIndex: TrackedDeviceIndex_t, pControllerState: VRControllerState_t.ByReference,
                   unControllerStateSize: Int): Boolean
    }

    /** Fills the supplied struct with the current state of the controller and the provided pose with the pose of the controller when the controller state was
     *  updated most recently. Use this form if you need a precise controller pose as input to your application when the user presses or releases a button. */
    fun getControllerStateWithPose(eOrigin: ETrackingUniverseOrigin, unControllerDeviceIndex: TrackedDeviceIndex_t,
                                   pControllerState: VRControllerState_t.ByReference,
                                   pTrackedDevicePose: TrackedDevicePose_t.ByReference, unControllerStateSize: Int)
            = GetControllerStateWithPose!!.invoke(eOrigin.i, unControllerDeviceIndex, pControllerState, pTrackedDevicePose,
            unControllerStateSize)

    @JvmField var GetControllerStateWithPose: GetControllerStateWithPose_callback? = null

    interface GetControllerStateWithPose_callback : Callback {
        fun invoke(eOrigin: Int, unControllerDeviceIndex: TrackedDeviceIndex_t, pControllerState: VRControllerState_t.ByReference,
                   pTrackedDevicePose: TrackedDevicePose_t.ByReference, unControllerStateSize: Int): Boolean
    }

    /** Trigger a single haptic pulse on a controller. After this call the application may not trigger another haptic pulse on this controller and axis
     *  combination for 5ms. */
    fun triggerHapticPulse(unControllerDeviceIndex: TrackedDeviceIndex_t, unAxisId: Int, usDurationMicroSec: Short)
            = TriggerHapticPulse!!.invoke(unControllerDeviceIndex, unAxisId, usDurationMicroSec)

    @JvmField var TriggerHapticPulse: TriggerHapticPulse_callback? = null

    interface TriggerHapticPulse_callback : Callback {
        fun invoke(unControllerDeviceIndex: TrackedDeviceIndex_t, unAxisId: Int, usDurationMicroSec: Short)
    }

    /** returns the name of an openvr.EVRButtonId value value */
    fun getButtonIdNameFromEnum(eButtonId: EVRButtonId) = GetButtonIdNameFromEnum!!.invoke(eButtonId.i)

    @JvmField var GetButtonIdNameFromEnum: GetButtonIdNameFromEnum_callback? = null

    interface GetButtonIdNameFromEnum_callback : Callback {
        fun invoke(eButtonId: Int): String
    }

    /** returns the game of an openvr.EVRControllerAxisType value value */
    fun getControllerAxisTypeNameFromEnum(eAxisType: EVRControllerAxisType) = GetControllerAxisTypeNameFromEnum!!.invoke(eAxisType.i)

    @JvmField var GetControllerAxisTypeNameFromEnum: GetControllerAxisTypeNameFromEnum_callback? = null

    interface GetControllerAxisTypeNameFromEnum_callback : Callback {
        fun invoke(eAxisType: Int): String
    }

    /** Tells openvr.OpenVR that this process wants exclusive access to controller button states and button events. Other apps will be notified that they have lost
     *  input focus with a VREvent_InputFocusCaptured event. Returns false if input focus could not be captured for some reason. */
    fun captureInputFocus() = CaptureInputFocus!!.invoke()

    @JvmField var CaptureInputFocus: CaptureInputFocus_callback? = null

    interface CaptureInputFocus_callback : Callback {
        fun invoke(): Boolean
    }

    /** Tells openvr.OpenVR that this process no longer wants exclusive access to button states and button events. Other apps will be notified that input focus has
     *  been released with a VREvent_InputFocusReleased event. */
    fun releaseInputFocus() = ReleaseInputFocus!!.invoke()

    @JvmField var ReleaseInputFocus: ReleaseInputFocus_callback? = null

    interface ReleaseInputFocus_callback : Callback {
        fun invoke()
    }

    /** Returns true if input focus is captured by another process. */
    fun isInputFocusCapturedByAnotherProcess() = IsInputFocusCapturedByAnotherProcess!!.invoke()

    @JvmField var IsInputFocusCapturedByAnotherProcess: IsInputFocusCapturedByAnotherProcess_callback? = null

    interface IsInputFocusCapturedByAnotherProcess_callback : Callback {
        fun invoke(): Boolean
    }


    // ------------------------------------
    // Debug Methods
    // ------------------------------------

    /** Sends a request to the driver for the specified device and returns the response. The maximum response size is 32k, but this method can be called with
     *  a smaller buffer. If the response exceeds the size of the buffer, it is truncated.
     *  The size of the response including its terminating null is returned. */
    fun driverDebugRequest(unDeviceIndex: TrackedDeviceIndex_t, pchRequest: String, pchResponseBuffer: String, unResponseBufferSize: Int)
            = DriverDebugRequest!!.invoke(unDeviceIndex, pchRequest, pchResponseBuffer, unResponseBufferSize)

    @JvmField var DriverDebugRequest: DriverDebugRequest_callback? = null

    interface DriverDebugRequest_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t, pchRequest: String, pchResponseBuffer: String, unResponseBufferSize: Int): Int
    }


    // ------------------------------------
    // Firmware methods
    // ------------------------------------

    /** Performs the actual firmware update if applicable.
     *  The following events will be sent, if VRFirmwareError_None was returned: VREvent_FirmwareUpdateStarted, VREvent_FirmwareUpdateFinished
     *  Use the properties Prop_Firmware_UpdateAvailable_Bool, Prop_Firmware_ManualUpdate_Bool, and Prop_Firmware_ManualUpdateURL_String to figure our whether
     *  a firmware update is available, and to figure out whether its a manual update
     *  Prop_Firmware_ManualUpdateURL_String should point to an URL describing the manual update process */
    fun performFirmwareUpdate(unDeviceIndex: TrackedDeviceIndex_t) = EVRFirmwareError.of(PerformFirmwareUpdate!!.invoke(unDeviceIndex))

    @JvmField var PerformFirmwareUpdate: PerformFirmwareUpdate_callback? = null

    interface PerformFirmwareUpdate_callback : Callback {
        fun invoke(unDeviceIndex: TrackedDeviceIndex_t): Int
    }


    // ------------------------------------
    // Application life cycle methods
    // ------------------------------------

    /** Call this to acknowledge to the system that VREvent_Quit has been received and that the process is exiting.
     *  This extends the timeout until the process is killed. */
    fun acknowledgeQuit_Exiting() = AcknowledgeQuit_Exiting!!.invoke()

    @JvmField var AcknowledgeQuit_Exiting: AcknowledgeQuit_Exiting_callback? = null

    interface AcknowledgeQuit_Exiting_callback : Callback {
        fun invoke()
    }

    /** Call this to tell the system that the user is being prompted to save data. This halts the timeout and dismisses the dashboard (if it was up).
     *  Applications should be sure to actually prompt the user to save and then exit afterward, otherwise the user will be left in a confusing state. */
    fun acknowledgeQuit_UserPrompt() = AcknowledgeQuit_UserPrompt!!.invoke()

    @JvmField var AcknowledgeQuit_UserPrompt: AcknowledgeQuit_UserPrompt_callback? = null

    interface AcknowledgeQuit_UserPrompt_callback : Callback {
        fun invoke()
    }

    constructor()

    override fun getFieldOrder(): List<String> = Arrays.asList("GetRecommendedRenderTargetSize", "GetProjectionMatrix",
            "GetProjectionRaw", "ComputeDistortion", "GetEyeToHeadTransform", "GetTimeSinceLastVsync", "GetD3D9AdapterIndex",
            "GetDXGIOutputInfo", "IsDisplayOnDesktop", "SetDisplayVisibility", "GetDeviceToAbsoluteTrackingPose", "ResetSeatedZeroPose",
            "GetSeatedZeroPoseToStandingAbsoluteTrackingPose", "GetRawZeroPoseToStandingAbsoluteTrackingPose",
            "GetSortedTrackedDeviceIndicesOfClass", "GetTrackedDeviceActivityLevel", "ApplyTransform",
            "GetTrackedDeviceIndexForControllerRole", "GetControllerRoleForTrackedDeviceIndex", "GetTrackedDeviceClass",
            "IsTrackedDeviceConnected", "GetBoolTrackedDeviceProperty", "GetFloatTrackedDeviceProperty", "GetInt32TrackedDeviceProperty",
            "GetUint64TrackedDeviceProperty", "GetMatrix34TrackedDeviceProperty", "GetStringTrackedDeviceProperty",
            "GetPropErrorNameFromEnum", "PollNextEvent", "PollNextEventWithPose", "GetEventTypeNameFromEnum", "GetHiddenAreaMesh",
            "GetControllerState", "GetControllerStateWithPose", "TriggerHapticPulse", "GetButtonIdNameFromEnum",
            "GetControllerAxisTypeNameFromEnum", "CaptureInputFocus", "ReleaseInputFocus", "IsInputFocusCapturedByAnotherProcess",
            "DriverDebugRequest", "PerformFirmwareUpdate", "AcknowledgeQuit_Exiting", "AcknowledgeQuit_UserPrompt")

    constructor (peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRSystem(), Structure.ByReference
    class ByValue : IVRSystem(), Structure.ByValue
}

val IVRSystem_Version = "FnTable:IVRSystem_015"

/** Used for all errors reported by the openvr.IVRApplications interface */
enum class EVRApplicationError(@JvmField val i: Int) {

    None(0),

    AppKeyAlreadyExists(100), // Only one application can use any given key
    NoManifest(101), //          the running application does not have a manifest
    NoApplication(102), //       No application is running
    InvalidIndex(103),
    UnknownApplication(104), //  the application could not be found
    IPCFailed(105), //           An IPC failure caused the request to fail
    ApplicationAlreadyRunning(106),
    InvalidManifest(107),
    InvalidApplication(108),
    LaunchFailed(109), //        the process didn't start
    ApplicationAlreadyStarting(110), // the system was already starting the same application
    LaunchInProgress(111), //    The system was already starting a different application
    OldApplicationQuitting(112),
    TransitionAborted(113),
    IsTemplate(114), // error when you try to call LaunchApplication() on a template value app (use LaunchTemplateApplication)

    BufferTooSmall(200), //      The provided buffer was too small to fit the requested data
    PropertyNotSet(201), //      The requested property was not set
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

    IsDashboardOverlay_Bool(60),
    IsTemplate_Bool(61),
    IsInstanced_Bool(62),
    IsInternal_Bool(63),

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

    @JvmField var pchKey = "" // TODO check
    @JvmField var pchValue = ""

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