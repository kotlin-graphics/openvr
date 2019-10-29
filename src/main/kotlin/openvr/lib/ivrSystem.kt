package openvr.lib

import glm_.BYTES
import glm_.mat4x4.Mat4
import glm_.s
import glm_.vec2.Vec2i
import glm_.vec4.Vec4
import kool.adr
import kool.rem
import kool.set
import org.lwjgl.openvr.*
import org.lwjgl.openvr.VRSystem.*
import org.lwjgl.system.MemoryUtil.*
import org.lwjgl.vulkan.VkInstance
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.LongBuffer


object vrSystem : vrInterface {

    val pError = memCallocInt(1)

    var error: TrackedPropertyError
        get() = TrackedPropertyError of pError[0]
        set(value) {
            pError[0] = value.i
        }

    // ------------------------------------
    // Display Methods
    // ------------------------------------

    /**
     * Returns the suggested size for the intermediate render target that the distortion pulls from.
     *
     * @param pnWidth  recommended width for the offscreen render target
     * @param pnHeight recommended height for the offscreen render target
     */
    val recommendedRenderTargetSize: Vec2i
        get() = stak {
            val width = it.nmalloc(1, Vec2i.size)
            val height = width + Int.BYTES
            nVRSystem_GetRecommendedRenderTargetSize(width, height)
            Vec2i(memGetInt(width), memGetInt(height))
        }

    /**
     * Returns the projection matrix for the specified eye.
     *
     * @param eye   determines which eye the function should return the projection for. One of:<br><table><tr><td>{@link VR#EVREye_Eye_Left}</td><td>{@link VR#EVREye_Eye_Right}</td></tr></table>
     * @param nearZ distance to the near clip plane in meters
     * @param farZ  distance to the far clip plane in meters
     */
    fun getProjectionMatrix(eye: VREye, nearZ: Float, farZ: Float, res: Mat4 = Mat4()): Mat4 {
        val hmdMat44 = vr.HmdMatrix44()
        nVRSystem_GetProjectionMatrix(eye.i, nearZ, farZ, hmdMat44.adr)
        return hmdMat44 to res
    }

    /**
     * JVM custom
     * Returns the components necessary to build your own projection matrix in case your application is doing something fancy like infinite Z.
     *
     * @param eye    determines which eye the function should return the projection for. One of:<br><table><tr><td>{@link VR#EVREye_Eye_Left}</td><td>{@link VR#EVREye_Eye_Right}</td></tr></table>
     * @return [left, right, top, bottom] coordinate for the bottom clipping plane
     */
    infix fun getProjectionRaw(eye: VREye): FloatArray =
            stak {
                val left = it.nmalloc(1, Vec4.size)
                val right = left + Float.BYTES
                val top = right + Float.BYTES
                val bottom = top + Float.BYTES
                nVRSystem_GetProjectionRaw(eye.i, left, right, top, bottom)
                floatArrayOf(memGetFloat(left), memGetFloat(right), memGetFloat(top), memGetFloat(bottom))
            }

    /**
     * Gets the result of the distortion function for the specified eye and input UVs. UVs go from 0,0 in the upper left of that eye's viewport and 1,1 in the
     * lower right of that eye's viewport.
     *
     * @param eye                   determines which eye the function should return the distortion value for. One of:<br><table><tr><td>{@link VR#EVREye_Eye_Left}</td><td>{@link VR#EVREye_Eye_Right}</td></tr></table>
     * @param u                     horizontal texture coordinate for the output pixel within the viewport
     * @param v                     vertical texture coordinate for the output pixel within the viewport
     * @param distortionCoordinates a struct in which to return the distortion coordinates
     *
     * @return true for success. Otherwise, returns false, and distortion coordinates are not suitable.
     */
    fun computeDistortion(eye: VREye, u: Float, v: Float, distortionCoordinates: DistortionCoordinates): Boolean =
            nVRSystem_ComputeDistortion(eye.i, u, v, distortionCoordinates.adr)

    /**
     * Returns the transform from eye space to the head space. Eye space is the per-eye flavor of head space that provides stereo disparity. Instead of
     * {@code Model * View * Projection} the sequence is {@code Model * View * Eye^-1 * Projection}. Normally {@code View} and {@code Eye^-1} will be
     * multiplied together and treated as {@code View} in your application.
     *
     * @param eye determines which eye the function should return the eye matrix for. One of:<br><table><tr><td>{@link VR#EVREye_Eye_Left}</td><td>{@link VR#EVREye_Eye_Right}</td></tr></table>
     */
    fun getEyeToHeadTransform(eye: VREye, res: Mat4 = Mat4()): Mat4 {
        val hmdMat34 = vr.HmdMatrix34()
        nVRSystem_GetEyeToHeadTransform(eye.i, hmdMat34.adr)
        return hmdMat34 to res
    }

    /**
     * Returns the number of elapsed seconds since the last recorded vsync event. This will come from a vsync timer event in the timer if possible or from the
     * application-reported time if that is not available.
     *
     * @param secondsSinceLastVsync fractional number of seconds since the last vsync event. This will never exceed the length of a single frame.
     * @param frameCounter         the number of frames since vrserver.exe started
     *
     * @return if no vsync times are available the function will return zero for vsync time and frame counter and return false from the method
     */
    fun getTimeSinceLastVsync(secondsSinceLastVsync: FloatBuffer, frameCounter: LongBuffer): Boolean =
            nVRSystem_GetTimeSinceLastVsync(secondsSinceLastVsync.adr, frameCounter.adr)

    /**
     * <h3>D3D9 Only</h3>
     *
     * <p>Returns the adapter index that the user should pass into {@code CreateDevice} to set up D3D9 in such a way that it can go full screen exclusive on the
     * HMD.</p>
     *
     * @return -1 if there was an error
     */
    fun getD3D9AdapterIndex(): Int = VRSystem_GetD3D9AdapterIndex()

    /**
     * <h3>D3D10/11 Only</h3>
     *
     * <p>Returns the adapter index that the user should pass into {@code EnumAdapters} to create the device and swap chain in DX10 and DX11. If an error occurs
     * the index will be set to -1.</p>
     *
     * @param adapterIndex the index of the adapter to use for this display
     */
    infix fun getDXGIOutputInfo(adapterIndex: IntBuffer) = nVRSystem_GetDXGIOutputInfo(adapterIndex.adr)

    /**
     * Returns platform- and texture-type specific adapter identification so that applications and the compositor are creating textures and swap chains on the
     * same GPU. If an error occurs the device will be set to 0.
     *
     * <h3>D3D10/11/12 Only (D3D9 Not Supported)</h3>
     *
     * <p>Returns the adapter LUID that identifies the GPU attached to the HMD. The user should enumerate all adapters using {@code IDXGIFactory::EnumAdapters}
     * and {@code IDXGIAdapter::GetDesc} to find the adapter with the matching LUID, or use {@code IDXGIFactory4::EnumAdapterByLuid}. The discovered
     * {@code IDXGIAdapter} should be used to create the device and swap chain.</p>
     *
     * <h3>Vulkan Only</h3>
     *
     * <p>Returns the {@code VkPhysicalDevice} that should be used by the application. {@code instance} must be the instance the application will use to query
     * for the {@code VkPhysicalDevice}. The application must create the {@code VkInstance} with extensions returned by {@link VRCompositor#VRCompositor_GetVulkanInstanceExtensionsRequired GetVulkanInstanceExtensionsRequired}
     * enabled.</p>
     *
     * <h3>macOS Only</h3>
     *
     * <p>For {@link VR#ETextureType_TextureType_IOSurface} returns the {@code id<MTLDevice>} that should be used by the application.</p>
     *
     * <p>On 10.13+ for {@link VR#ETextureType_TextureType_OpenGL} returns the {@code registryId} of the renderer which should be used by the application. See Apple
     * Technical Q&A QA1168 for information on enumerating GL Renderers, and the new {@code kCGLRPRegistryIDLow} and {@code kCGLRPRegistryIDHigh}
     * {@code CGLRendererProperty} values in the 10.13 SDK.</p>
     *
     * <p>Pre 10.13 for {@link VR#ETextureType_TextureType_OpenGL} returns 0, as there is no dependable way to correlate the HMDs {@code MTLDevice} with a GL Renderer.</p>
     *
     * @param device
     * @param textureType one of:<br><table><tr><td>{@link VR#ETextureType_TextureType_DirectX}</td><td>{@link VR#ETextureType_TextureType_OpenGL}</td></tr><tr><td>{@link VR#ETextureType_TextureType_Vulkan}</td><td>{@link VR#ETextureType_TextureType_IOSurface}</td></tr><tr><td>{@link VR#ETextureType_TextureType_DirectX12}</td><td>{@link VR#ETextureType_TextureType_DXGISharedHandle}</td></tr></table>
     * @param instance   an optional parameter that is required only when {@code textureType} is {@link VR#ETextureType_TextureType_Vulkan}
     */
    fun getOutputDevice(device: LongBuffer, textureType: TextureType, instance: VkInstance? = null) =
            nVRSystem_GetOutputDevice(device.adr, textureType.i, instance?.adr ?: NULL)


    // ------------------------------------
    // Display Mode methods
    // ------------------------------------


    /** Use to determine if the headset display is part of the desktop (i.e. extended) or hidden (i.e. direct mode). */
    val isDisplayOnDesktop: Boolean
        get() = VRSystem_IsDisplayOnDesktop()

    /**
     * Sets the display visibility (true = extended, false = direct mode).
     *
     * @param isVisibleOnDesktop the display visibility
     *
     * @return true indicates that the change was successful
     */
    infix fun setDisplayVisibility(isVisibleOnDesktop: Boolean): Boolean =
            VRSystem_SetDisplayVisibility(isVisibleOnDesktop)


    // ------------------------------------
    // Tracking Methods
    // ------------------------------------


    /**
     * The pose that the tracker thinks that the HMD will be in at the specified number of seconds into the future. Pass 0 to get the state at the instant the
     * method is called. Most of the time the application should calculate the time until the photons will be emitted from the display and pass that time into
     * the method.
     *
     * <p>This is roughly analogous to the inverse of the view matrix in most applications, though many games will need to do some additional rotation or
     * translation on top of the rotation and translation provided by the head pose.</p>
     *
     * <p>For devices where {@code bPoseIsValid} is true the application can use the pose to position the device in question. The provided array can be any size
     * up to {@link VR#k_unMaxTrackedDeviceCount}.</p>
     *
     * <p>Seated experiences should call this method with {@link VR#ETrackingUniverseOrigin_TrackingUniverseSeated} and receive poses relative to the seated zero pose.
     * Standing experiences should call this method with {@link VR#ETrackingUniverseOrigin_TrackingUniverseStanding} and receive poses relative to the Chaperone Play
     * Area. {@link VR#ETrackingUniverseOrigin_TrackingUniverseRawAndUncalibrated} should probably not be used unless the application is the Chaperone calibration tool
     * itself, but will provide poses relative to the hardware-specific coordinate system in the driver.</p>
     *
     * @param origin                           tracking universe that returned poses should be relative to. One of:<br><table><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseSeated}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseStanding}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseRawAndUncalibrated}</td></tr></table>
     * @param predictedSecondsToPhotonsFromNow number of seconds from now to predict poses for. Positive numbers are in the future. Pass 0 to get the state at the instant the function is called.
     * @param trackedDevicePoseArray
     */
    fun getDeviceToAbsoluteTrackingPose(origin: TrackingUniverseOrigin, predictedSecondsToPhotonsFromNow: Float, trackedDevicePoseArray: TrackedDevicePose.Buffer) =
            nVRSystem_GetDeviceToAbsoluteTrackingPose(origin.i, predictedSecondsToPhotonsFromNow, trackedDevicePoseArray.adr, trackedDevicePoseArray.rem)

    /**
     * Sets the zero pose for the seated tracker coordinate system to the current position and yaw of the HMD. After {@code ResetSeatedZeroPose} all
     * {@link #VRSystem_GetDeviceToAbsoluteTrackingPose GetDeviceToAbsoluteTrackingPose} calls that pass {@link VR#ETrackingUniverseOrigin_TrackingUniverseSeated} as the origin will be relative to this new zero
     * pose. The new zero coordinate system will not change the fact that the Y axis is up in the real world, so the next pose returned from
     * {@code GetDeviceToAbsoluteTrackingPose} after a call to {@code ResetSeatedZeroPose} may not be exactly an identity matrix.
     *
     * <p>NOTE: This function overrides the user's previously saved seated zero pose and should only be called as the result of a user action. Users are also
     * able to set their seated zero pose via the OpenVR Dashboard.</p>
     */
    fun resetSeatedZeroPose() = VRSystem_ResetSeatedZeroPose()

    /**
     * Returns the transform from the seated zero pose to the standing absolute tracking system. This allows  applications to represent the seated origin to
     * used or transform object positions from one coordinate system to the other.
     *
     * <p>The seated origin may or may not be inside the Play Area or Collision Bounds returned by {@code IVRChaperone}. Its position depends on what the user
     * has set from the Dashboard settings and previous calls to {@link #VRSystem_ResetSeatedZeroPose ResetSeatedZeroPose}.</p>
     */
    fun getSeatedZeroPoseToStandingAbsoluteTrackingPose(res: Mat4 = Mat4()): Mat4 {
        val hmdMat34 = vr.HmdMatrix34()
        nVRSystem_GetSeatedZeroPoseToStandingAbsoluteTrackingPose(hmdMat34.adr)
        return hmdMat34 to res
    }

    /**
     * Returns the transform from the tracking origin to the standing absolute tracking system. This allows applications to convert from raw tracking space to
     * the calibrated standing coordinate system.
     */
    fun getRawZeroPoseToStandingAbsoluteTrackingPose(res: Mat4 = Mat4()): Mat4 {
        val hmdMat34 = vr.HmdMatrix34()
        nVRSystem_GetRawZeroPoseToStandingAbsoluteTrackingPose(hmdMat34.adr)
        return hmdMat34 to res
    }

    /**
     * Get a sorted array of device indices of a given class of tracked devices (e.g. controllers).  Devices are sorted right to left relative to the
     * specified tracked device (default: hmd -- pass in -1 for absolute tracking space).
     *
     * @param trackedDeviceClass            one of:<br><table><tr><td>{@link VR#ETrackedDeviceClass_TrackedDeviceClass_Invalid}</td></tr><tr><td>{@link VR#ETrackedDeviceClass_TrackedDeviceClass_HMD}</td></tr><tr><td>{@link VR#ETrackedDeviceClass_TrackedDeviceClass_Controller}</td></tr><tr><td>{@link VR#ETrackedDeviceClass_TrackedDeviceClass_GenericTracker}</td></tr><tr><td>{@link VR#ETrackedDeviceClass_TrackedDeviceClass_TrackingReference}</td></tr><tr><td>{@link VR#ETrackedDeviceClass_TrackedDeviceClass_DisplayRedirect}</td></tr></table>
     * @param trackedDeviceIndexArray
     * @param relativeToTrackedDeviceIndex
     *
     * @return the number of devices in the list, or the size of the array needed if not large enough
     */
    fun getSortedTrackedDeviceIndicesOfClass(trackedDeviceClass: TrackedDeviceClass, trackedDeviceIndexArray: IntBuffer?,
                                             relativeToTrackedDeviceIndex: TrackedDeviceIndex = vr.trackedDeviceIndex_Hmd): Int =
            nVRSystem_GetSortedTrackedDeviceIndicesOfClass(trackedDeviceClass.i, trackedDeviceIndexArray?.adr
                    ?: NULL, trackedDeviceIndexArray?.rem ?: 0, relativeToTrackedDeviceIndex)

    /**
     * Returns the level of activity on the device.
     *
     * @param deviceId
     */
    infix fun getTrackedDeviceActivityLevel(deviceId: TrackedDeviceIndex): DeviceActivityLevel =
            DeviceActivityLevel of VRSystem.VRSystem_GetTrackedDeviceActivityLevel(deviceId)

    /**
     * Convenience utility to apply the specified transform to the specified pose. This properly transforms all pose components, including velocity and
     * angular velocity.
     *
     * @param outputPose
     * @param trackedDevicePose
     * @param transform
     */
    fun applyTransform(outputPose: TrackedDevicePose, trackedDevicePose: TrackedDevicePose, transform: Mat4) {
        nVRSystem_ApplyTransform(outputPose.adr, trackedDevicePose.adr, vr.HmdMatrix34(transform).adr)
    }

    /**
     * Returns the device index associated with a specific role, for example the left hand or the right hand.
     *
     * @param deviceType one of:<br><table><tr><td>{@link VR#ETrackedControllerRole_TrackedControllerRole_Invalid}</td></tr><tr><td>{@link VR#ETrackedControllerRole_TrackedControllerRole_LeftHand}</td></tr><tr><td>{@link VR#ETrackedControllerRole_TrackedControllerRole_RightHand}</td></tr><tr><td>{@link VR#ETrackedControllerRole_TrackedControllerRole_OptOut}</td></tr><tr><td>{@link VR#ETrackedControllerRole_TrackedControllerRole_Max}</td></tr></table>
     */
    @Deprecated("This function is deprecated in favor of the new IVRInput system.")
    infix fun getTrackedDeviceIndexForControllerRole(deviceType: TrackedControllerRole): TrackedDeviceIndex =
            VRSystem_GetTrackedDeviceIndexForControllerRole(deviceType.i)

    /**
     * Returns the controller type associated with a device index.
     *
     * @param deviceIndex
     */
    @Deprecated("This function is deprecated in favor of the new IVRInput system.")
    infix fun getControllerRoleForTrackedDeviceIndex(deviceIndex: TrackedDeviceIndex): TrackedControllerRole =
            TrackedControllerRole of VRSystem.VRSystem_GetControllerRoleForTrackedDeviceIndex(deviceIndex)


    // ------------------------------------
    // Property methods
    // ------------------------------------


    /**
     * Returns the device class of a tracked device. If there has not been a device connected in this slot since the application started this function will
     * return {@link VR#ETrackedDeviceClass_TrackedDeviceClass_Invalid}. For previous detected devices the function will return the previously observed device class.
     *
     * <p>To determine which devices exist on the system, just loop from 0 to {@link VR#k_unMaxTrackedDeviceCount} and check the device class. Every device with something
     * other than {@link VR#ETrackedDeviceClass_TrackedDeviceClass_Invalid} is associated with an actual tracked device.</p>
     *
     * @param deviceIndex index of the device to get the device class for.
     */
    infix fun getTrackedDeviceClass(deviceIndex: TrackedDeviceIndex): TrackedDeviceClass =
            TrackedDeviceClass of VRSystem.VRSystem_GetTrackedDeviceClass(deviceIndex)

    /**
     * Returns true if there is a device connected in this slot.
     *
     * @param deviceIndex index of the device to test connected state for
     */
    infix fun isTrackedDeviceConnected(deviceIndex: TrackedDeviceIndex): Boolean =
            VRSystem_IsTrackedDeviceConnected(deviceIndex)

    inline fun <reified T> getTrackedDeviceProperty(deviceIndex: TrackedDeviceIndex, prop: TrackedDeviceProperty, pErr: TrackedPropertyErrorBuffer = pError): T {
        return when (T::class.java) {
            Boolean::class.java -> getBoolTrackedDeviceProperty(deviceIndex, prop, pErr) as T
            Float::class.java -> getFloatTrackedDeviceProperty(deviceIndex, prop, pErr) as T
            Int::class.java -> getIntTrackedDeviceProperty(deviceIndex, prop, pErr) as T
            Long::class.java -> getLongTrackedDeviceProperty(deviceIndex, prop, pErr) as T
            Mat4::class.java -> getMat4TrackedDeviceProperty(deviceIndex, prop, pErr) as T
            // TODO getArray
            else -> throw Error()
        }
    }

    /**
     * Returns a bool property. If the device index is not valid or the property is not a bool type this function will return false.
     *
     * Note: Multi-thread unsafe if not passing a pError and reading it from the class property.
     *
     * @param deviceIndex index of the device to get the property for
     * @param prop          which property to get
     * @param pErr        the pErr returned when attempting to fetch this property. This can be {@code NULL} if the caller doesn't care about the source of a property pErr.
     */
    fun getBoolTrackedDeviceProperty(deviceIndex: TrackedDeviceIndex, prop: TrackedDeviceProperty, pErr: TrackedPropertyErrorBuffer = pError): Boolean =
            nVRSystem_GetBoolTrackedDeviceProperty(deviceIndex, prop.i, pErr.adr)

    /**
     * Returns a float property. If the device index is not valid or the property is not a float type this function will return 0.
     *
     * Note: Multi-thread unsafe if not passing a pError and reading it from the class property.
     *
     * @param deviceIndex index of the device to get the property for
     * @param prop          which property to get
     * @param pErr        the pErr returned when attempting to fetch this property. This can be {@code NULL} if the caller doesn't care about the source of a property pErr.
     */
    fun getFloatTrackedDeviceProperty(deviceIndex: TrackedDeviceIndex, prop: TrackedDeviceProperty, pErr: TrackedPropertyErrorBuffer = pError): Float =
            nVRSystem_GetFloatTrackedDeviceProperty(deviceIndex, prop.i, pErr.adr)

    /**
     * Returns an int property. If the device index is not valid or the property is not a int type this function will return 0.
     *
     * Note: Multi-thread unsafe if not passing a pError and reading it from the class property.
     *
     * @param deviceIndex index of the device to get the property for
     * @param prop          which property to get
     * @param pErr        the pErr returned when attempting to fetch this property. This can be {@code NULL} if the caller doesn't care about the source of a property pErr.
     */
    fun getIntTrackedDeviceProperty(deviceIndex: TrackedDeviceIndex, prop: TrackedDeviceProperty, pErr: TrackedPropertyErrorBuffer = pError): Int =
            nVRSystem_GetInt32TrackedDeviceProperty(deviceIndex, prop.i, pErr.adr)

    /**
     * Returns a uint64 property. If the device index is not valid or the property is not a uint64 type this function will return 0.
     *
     * Note: Multi-thread unsafe if not passing a pError and reading it from the class property.
     *
     * @param deviceIndex index of the device to get the property for
     * @param prop          which property to get
     * @param pErr        the pErr returned when attempting to fetch this property. This can be {@code NULL} if the caller doesn't care about the source of a property pErr.
     */
    fun getLongTrackedDeviceProperty(deviceIndex: TrackedDeviceIndex, prop: TrackedDeviceProperty, pErr: TrackedPropertyErrorBuffer = pError): Long =
            nVRSystem_GetUint64TrackedDeviceProperty(deviceIndex, prop.i, pErr.adr)

    /**
     * Returns a matrix property. If the device index is not valid or the property is not a matrix type, this function will return identity.
     *
     * Note: Multi-thread unsafe if not passing a pError and reading it from the class property.
     *
     * @param deviceIndex index of the device to get the property for
     * @param prop          which property to get
     * @param pErr        the pErr returned when attempting to fetch this property. This can be {@code NULL} if the caller doesn't care about the source of a property pErr.
     */
    fun getMat4TrackedDeviceProperty(deviceIndex: TrackedDeviceIndex, prop: TrackedDeviceProperty, pErr: TrackedPropertyErrorBuffer = pError): Mat4 {
        val hmdMat34 = vr.HmdMatrix34()
        nVRSystem_GetMatrix34TrackedDeviceProperty(deviceIndex, prop.i, pErr.adr, hmdMat34.adr)
        return hmdMat34.toMat4()
    }

    /**
     * Returns an array of one type of property.
     *
     * <p>If the device index is not valid or the property is not a single value or an array of the specified type, this function will return 0. Otherwise it
     * returns the number of bytes necessary to hold the array of properties. If {@code unBufferSize} is greater than the returned size and {@code buffer} is
     * non-{@code NULL}, {@code buffer} is filled with the contents of array of properties.</p>
     *
     * Note: Multi-thread unsafe if not passing a pError and reading it from the class property.
     *
     * @param deviceIndex index of the device to get the property for
     * @param prop          which property to get
     * @param propType
     * @param buffer
     * @param pErr        the pErr returned when attempting to fetch this property. This can be {@code NULL} if the caller doesn't care about the source of a property pErr.
     */
    fun getArrayTrackedDeviceProperty(deviceIndex: TrackedDeviceIndex, prop: TrackedDeviceProperty, propType: PropertyTypeTag, buffer: ByteBuffer?, pErr: TrackedPropertyErrorBuffer = pError): Int =
            nVRSystem_GetArrayTrackedDeviceProperty(deviceIndex, prop.i, propType, buffer?.adr
                    ?: NULL, buffer?.rem ?: 0, pErr.adr)

    /**
     * JVM custom
     *
     * Returns a string property. If the device index is not valid or the property is not a string type this function will return 0. Otherwise it returns the
     * length of the number of bytes necessary to hold this string including the trailing null. Strings will always fit in buffers of
     * {@link VR#k_unMaxPropertyStringSize} characters.
     *
     * Note: Multi-thread unsafe if not passing a pError and reading it from the class property.
     *
     * @param deviceIndex index of the device to get the property for
     * @param prop          which property to get
     * @param pchValue      the buffer to store string properties in. {@code unBufferSize} should be the size of this buffer.
     * @param pError        the error returned when attempting to fetch this property. This can be {@code NULL} if the caller doesn't care about the source of a property error.
     */
    @JvmOverloads
    fun getStringTrackedDeviceProperty(deviceIndex: TrackedDeviceIndex, prop: TrackedDeviceProperty, pErr: TrackedPropertyErrorBuffer = pError): String =
            stak {
                val initialSize = 32
                val bytes = it.malloc(initialSize)
                val propLen = nVRSystem_GetStringTrackedDeviceProperty(deviceIndex, prop.i, bytes.adr, initialSize, pErr.adr)

                when (pErr[0]) {
                    TrackedPropertyError.Success.i -> memASCII(bytes, propLen - 1)
                    TrackedPropertyError.BufferTooSmall.i -> {
                        val newBytes = it.malloc(propLen)
                        nVRSystem_GetStringTrackedDeviceProperty(deviceIndex, prop.i, newBytes.adr, propLen, pErr.adr)
                        memASCII(newBytes, propLen - 1)
                    }
                    else -> throw Error(error.toString())
                }
            }

    /**
     * Kind of useless on JVM, but it will be offered anyway on the enum itself
     * Returns a string that corresponds with the specified property error. The string will be the name of the error enum value for all valid error codes.
     *
     * @param error the error code to return a string for. One of:<br><table><tr><td>{@link VR#ETrackedPropertyError_TrackedProp_Success}</td></tr><tr><td>{@link VR#ETrackedPropertyError_TrackedProp_WrongDataType}</td></tr><tr><td>{@link VR#ETrackedPropertyError_TrackedProp_WrongDeviceClass}</td></tr><tr><td>{@link VR#ETrackedPropertyError_TrackedProp_BufferTooSmall}</td></tr><tr><td>{@link VR#ETrackedPropertyError_TrackedProp_UnknownProperty}</td></tr><tr><td>{@link VR#ETrackedPropertyError_TrackedProp_InvalidDevice}</td></tr><tr><td>{@link VR#ETrackedPropertyError_TrackedProp_CouldNotContactServer}</td></tr><tr><td>{@link VR#ETrackedPropertyError_TrackedProp_ValueNotProvidedByDevice}</td></tr><tr><td>{@link VR#ETrackedPropertyError_TrackedProp_StringExceedsMaximumLength}</td></tr><tr><td>{@link VR#ETrackedPropertyError_TrackedProp_NotYetAvailable}</td></tr><tr><td>{@link VR#ETrackedPropertyError_TrackedProp_PermissionDenied}</td></tr><tr><td>{@link VR#ETrackedPropertyError_TrackedProp_InvalidOperation}</td></tr><tr><td>{@link VR#ETrackedPropertyError_TrackedProp_CannotWriteToWildcards}</td></tr></table>
     */
    //@Nullable
    //@NativeType("char *")
    //public static String VRSystem_GetPropErrorNameFromEnum(@NativeType("TrackedPropertyError") int error) {
    //    long __result = nVRSystem_GetPropErrorNameFromEnum(error);
    //    return memASCIISafe(__result);
    //}


    // ------------------------------------
    // Event methods
    // ------------------------------------


    /** JVM custom */
    inline fun <R> pollingEvents(block: (VREvent) -> R) {
        val event = vr.VREvent()
        while (pollNextEvent(event))
            block(event)
    }

    /** Returns true and fills the event with the next event on the queue if there is one. If there are no events this method returns false. */
    fun pollNextEvent(event: VREvent, size: Int = VREvent.SIZEOF): Boolean = nVRSystem_PollNextEvent(event.adr, size)

    /**
     * Returns true and fills the event with the next event on the queue if there is one. If there are no events this method returns false. Fills in the pose
     * of the associated tracked device in the provided pose struct. This pose will always be older than the call to this function and should not be used to
     * render the device.
     *
     * @param origin            the tracking system to return the event's pose in. One of:<br><table><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseSeated}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseStanding}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseRawAndUncalibrated}</td></tr></table>
     * @param event             an event structure to fill with the next event
     * @param trackedDevicePose a pose struct to fill with the returned event's pose. Must not be {@code NULL}.
     */
    fun pollNextEventWithPose(origin: TrackingUniverseOrigin, event: VREvent, trackedDevicePose: TrackedDevicePose): Boolean =
            nVRSystem_PollNextEventWithPose(origin.i, event.adr, VREvent.SIZEOF, trackedDevicePose.adr)

    /**
     * Kind of useless on JVM, but it will be offered anyway on the enum itself
     * Returns the name of an {@code EVREvent} enum value.
     *
     * @param eType the event type to return a string for. One of:<br><table><tr><td>{@link VR#EVREventType_VREvent_None}</td></tr><tr><td>{@link VR#EVREventType_VREvent_TrackedDeviceActivated}</td></tr><tr><td>{@link VR#EVREventType_VREvent_TrackedDeviceDeactivated}</td></tr><tr><td>{@link VR#EVREventType_VREvent_TrackedDeviceUpdated}</td></tr><tr><td>{@link VR#EVREventType_VREvent_TrackedDeviceUserInteractionStarted}</td></tr><tr><td>{@link VR#EVREventType_VREvent_TrackedDeviceUserInteractionEnded}</td></tr><tr><td>{@link VR#EVREventType_VREvent_IpdChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_EnterStandbyMode}</td></tr><tr><td>{@link VR#EVREventType_VREvent_LeaveStandbyMode}</td></tr><tr><td>{@link VR#EVREventType_VREvent_TrackedDeviceRoleChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_WatchdogWakeUpRequested}</td></tr><tr><td>{@link VR#EVREventType_VREvent_LensDistortionChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_PropertyChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_WirelessDisconnect}</td></tr><tr><td>{@link VR#EVREventType_VREvent_WirelessReconnect}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ButtonPress}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ButtonUnpress}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ButtonTouch}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ButtonUntouch}</td></tr><tr><td>{@link VR#EVREventType_VREvent_DualAnalog_Press}</td></tr><tr><td>{@link VR#EVREventType_VREvent_DualAnalog_Unpress}</td></tr><tr><td>{@link VR#EVREventType_VREvent_DualAnalog_Touch}</td></tr><tr><td>{@link VR#EVREventType_VREvent_DualAnalog_Untouch}</td></tr><tr><td>{@link VR#EVREventType_VREvent_DualAnalog_Move}</td></tr><tr><td>{@link VR#EVREventType_VREvent_DualAnalog_ModeSwitch1}</td></tr><tr><td>{@link VR#EVREventType_VREvent_DualAnalog_ModeSwitch2}</td></tr><tr><td>{@link VR#EVREventType_VREvent_DualAnalog_Cancel}</td></tr><tr><td>{@link VR#EVREventType_VREvent_MouseMove}</td></tr><tr><td>{@link VR#EVREventType_VREvent_MouseButtonDown}</td></tr><tr><td>{@link VR#EVREventType_VREvent_MouseButtonUp}</td></tr><tr><td>{@link VR#EVREventType_VREvent_FocusEnter}</td></tr><tr><td>{@link VR#EVREventType_VREvent_FocusLeave}</td></tr><tr><td>{@link VR#EVREventType_VREvent_Scroll}</td></tr><tr><td>{@link VR#EVREventType_VREvent_TouchPadMove}</td></tr><tr><td>{@link VR#EVREventType_VREvent_OverlayFocusChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_InputFocusCaptured}</td></tr><tr><td>{@link VR#EVREventType_VREvent_InputFocusReleased}</td></tr><tr><td>{@link VR#EVREventType_VREvent_SceneFocusLost}</td></tr><tr><td>{@link VR#EVREventType_VREvent_SceneFocusGained}</td></tr><tr><td>{@link VR#EVREventType_VREvent_SceneApplicationChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_SceneFocusChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_InputFocusChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_SceneApplicationSecondaryRenderingStarted}</td></tr><tr><td>{@link VR#EVREventType_VREvent_SceneApplicationUsingWrongGraphicsAdapter}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ActionBindingReloaded}</td></tr><tr><td>{@link VR#EVREventType_VREvent_HideRenderModels}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ShowRenderModels}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ConsoleOpened}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ConsoleClosed}</td></tr><tr><td>{@link VR#EVREventType_VREvent_OverlayShown}</td></tr><tr><td>{@link VR#EVREventType_VREvent_OverlayHidden}</td></tr><tr><td>{@link VR#EVREventType_VREvent_DashboardActivated}</td></tr><tr><td>{@link VR#EVREventType_VREvent_DashboardDeactivated}</td></tr><tr><td>{@link VR#EVREventType_VREvent_DashboardThumbSelected}</td></tr><tr><td>{@link VR#EVREventType_VREvent_DashboardRequested}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ResetDashboard}</td></tr><tr><td>{@link VR#EVREventType_VREvent_RenderToast}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ImageLoaded}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ShowKeyboard}</td></tr><tr><td>{@link VR#EVREventType_VREvent_HideKeyboard}</td></tr><tr><td>{@link VR#EVREventType_VREvent_OverlayGamepadFocusGained}</td></tr><tr><td>{@link VR#EVREventType_VREvent_OverlayGamepadFocusLost}</td></tr><tr><td>{@link VR#EVREventType_VREvent_OverlaySharedTextureChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ScreenshotTriggered}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ImageFailed}</td></tr><tr><td>{@link VR#EVREventType_VREvent_DashboardOverlayCreated}</td></tr><tr><td>{@link VR#EVREventType_VREvent_SwitchGamepadFocus}</td></tr><tr><td>{@link VR#EVREventType_VREvent_RequestScreenshot}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ScreenshotTaken}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ScreenshotFailed}</td></tr><tr><td>{@link VR#EVREventType_VREvent_SubmitScreenshotToDashboard}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ScreenshotProgressToDashboard}</td></tr><tr><td>{@link VR#EVREventType_VREvent_PrimaryDashboardDeviceChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_RoomViewShown}</td></tr><tr><td>{@link VR#EVREventType_VREvent_RoomViewHidden}</td></tr><tr><td>{@link VR#EVREventType_VREvent_Notification_Shown}</td></tr><tr><td>{@link VR#EVREventType_VREvent_Notification_Hidden}</td></tr><tr><td>{@link VR#EVREventType_VREvent_Notification_BeginInteraction}</td></tr><tr><td>{@link VR#EVREventType_VREvent_Notification_Destroyed}</td></tr><tr><td>{@link VR#EVREventType_VREvent_Quit}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ProcessQuit}</td></tr><tr><td>{@link VR#EVREventType_VREvent_QuitAborted_UserPrompt}</td></tr><tr><td>{@link VR#EVREventType_VREvent_QuitAcknowledged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_DriverRequestedQuit}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ChaperoneDataHasChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ChaperoneUniverseHasChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ChaperoneTempDataHasChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ChaperoneSettingsHaveChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_SeatedZeroPoseReset}</td></tr><tr><td>{@link VR#EVREventType_VREvent_AudioSettingsHaveChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_BackgroundSettingHasChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_CameraSettingsHaveChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ReprojectionSettingHasChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ModelSkinSettingsHaveChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_EnvironmentSettingsHaveChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_PowerSettingsHaveChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_EnableHomeAppSettingsHaveChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_SteamVRSectionSettingChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_LighthouseSectionSettingChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_NullSectionSettingChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_UserInterfaceSectionSettingChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_NotificationsSectionSettingChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_KeyboardSectionSettingChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_PerfSectionSettingChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_DashboardSectionSettingChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_WebInterfaceSectionSettingChanged}</td></tr><tr><td>{@link VR#EVREventType_VREvent_StatusUpdate}</td></tr><tr><td>{@link VR#EVREventType_VREvent_WebInterface_InstallDriverCompleted}</td></tr><tr><td>{@link VR#EVREventType_VREvent_MCImageUpdated}</td></tr><tr><td>{@link VR#EVREventType_VREvent_FirmwareUpdateStarted}</td></tr><tr><td>{@link VR#EVREventType_VREvent_FirmwareUpdateFinished}</td></tr><tr><td>{@link VR#EVREventType_VREvent_KeyboardClosed}</td></tr><tr><td>{@link VR#EVREventType_VREvent_KeyboardCharInput}</td></tr><tr><td>{@link VR#EVREventType_VREvent_KeyboardDone}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ApplicationTransitionStarted}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ApplicationTransitionAborted}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ApplicationTransitionNewAppStarted}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ApplicationListUpdated}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ApplicationMimeTypeLoad}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ApplicationTransitionNewAppLaunchComplete}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ProcessConnected}</td></tr><tr><td>{@link VR#EVREventType_VREvent_ProcessDisconnected}</td></tr><tr><td>{@link VR#EVREventType_VREvent_Compositor_MirrorWindowShown}</td></tr><tr><td>{@link VR#EVREventType_VREvent_Compositor_MirrorWindowHidden}</td></tr><tr><td>{@link VR#EVREventType_VREvent_Compositor_ChaperoneBoundsShown}</td></tr><tr><td>{@link VR#EVREventType_VREvent_Compositor_ChaperoneBoundsHidden}</td></tr><tr><td>{@link VR#EVREventType_VREvent_TrackedCamera_StartVideoStream}</td></tr><tr><td>{@link VR#EVREventType_VREvent_TrackedCamera_StopVideoStream}</td></tr><tr><td>{@link VR#EVREventType_VREvent_TrackedCamera_PauseVideoStream}</td></tr><tr><td>{@link VR#EVREventType_VREvent_TrackedCamera_ResumeVideoStream}</td></tr><tr><td>{@link VR#EVREventType_VREvent_TrackedCamera_EditingSurface}</td></tr><tr><td>{@link VR#EVREventType_VREvent_PerformanceTest_EnableCapture}</td></tr><tr><td>{@link VR#EVREventType_VREvent_PerformanceTest_DisableCapture}</td></tr><tr><td>{@link VR#EVREventType_VREvent_PerformanceTest_FidelityLevel}</td></tr><tr><td>{@link VR#EVREventType_VREvent_MessageOverlay_Closed}</td></tr><tr><td>{@link VR#EVREventType_VREvent_MessageOverlayCloseRequested}</td></tr><tr><td>{@link VR#EVREventType_VREvent_Input_HapticVibration}</td></tr><tr><td>{@link VR#EVREventType_VREvent_Input_BindingLoadFailed}</td></tr><tr><td>{@link VR#EVREventType_VREvent_Input_BindingLoadSuccessful}</td></tr><tr><td>{@link VR#EVREventType_VREvent_VendorSpecific_Reserved_Start}</td></tr><tr><td>{@link VR#EVREventType_VREvent_VendorSpecific_Reserved_End}</td></tr></table>
     */
    //@Nullable
    //@NativeType("char *")
    //public static String VRSystem_GetEventTypeNameFromEnum(@NativeType("VREventType") int eType) {
    //    long __result = nVRSystem_GetEventTypeNameFromEnum(eType);
    //    return memASCIISafe(__result);
    //}


    // ------------------------------------
    // Rendering helper methods
    // ------------------------------------


    /**
     * Returns the hidden area mesh for the current HMD. The pixels covered by this mesh will never be seen by the user after the lens distortion is applied
     * based on visibility to the panels. If this HMD does not have a hidden area mesh, the vertex data and count will be {@code NULL} and 0 respectively.
     *
     * <p>This mesh is meant to be rendered into the stencil buffer (or into the depth buffer setting nearz) before rendering each eye's view. This will improve
     * performance by letting the GPU early-reject pixels the user will never see before running the pixel shader.</p>
     *
     * <div style="margin-left: 26px; border-left: 1px solid gray; padding-left: 14px;"><h5>Note</h5>
     *
     * <p>Render this mesh with backface culling disabled since the winding order of the vertices can be different per-HMD or per-eye.</p></div>
     *
     * @param eye the eye to get the hidden area mesh for. One of:<br><table><tr><td>{@link VR#EVREye_Eye_Left}</td><td>{@link VR#EVREye_Eye_Right}</td></tr></table>
     * @param type one of:<br><table><tr><td>{@link VR#EHiddenAreaMeshType_k_eHiddenAreaMesh_Standard}</td></tr><tr><td>{@link VR#EHiddenAreaMeshType_k_eHiddenAreaMesh_Inverse}</td></tr><tr><td>{@link VR#EHiddenAreaMeshType_k_eHiddenAreaMesh_LineLoop}</td></tr><tr><td>{@link VR#EHiddenAreaMeshType_k_eHiddenAreaMesh_Max}</td></tr></table>
     */
    fun getHiddenAreaMesh(eye: VREye, type: HiddenAreaMeshType = HiddenAreaMeshType.Standard): HiddenAreaMesh =
            vr.HiddenAreaMesh().apply { VRSystem.nVRSystem_GetHiddenAreaMesh(eye.i, type.i, adr) }

    /**
     * Fills the supplied struct with the current state of the controller.
     *
     * @param controllerDeviceIndex the tracked device index of the controller to get the state of
     * @param controllerState        a struct to fill with the controller state
     *
     * @return false if the controller index is invalid
     */
    @Deprecated("This function is deprecated in favor of the new IVRInput system.")
    fun getControllerState(controllerDeviceIndex: TrackedDeviceIndex, controllerState: VRControllerState): Boolean =
            nVRSystem_GetControllerState(controllerDeviceIndex, controllerState.adr, VRControllerState.SIZEOF)

    /**
     * Fills the supplied struct with the current state of the controller and the provided pose with the pose of the controller when the controller state was
     * updated most recently. Use this form if you need a precise controller pose as input to your application when the user presses or releases a button.
     *
     * @param origin                 the tracking coordinate system to return the pose in. One of:<br><table><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseSeated}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseStanding}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseRawAndUncalibrated}</td></tr></table>
     * @param controllerDeviceIndex the tracked device index of the controller to get the state of
     * @param controllerState        a struct to fill with the controller state
     * @param unControllerStateSize   the size in bytes of the {@link VRControllerState} struct
     * @param trackedDevicePose      a pose struct to fill with the pose of the controller when the last button event occurred
     */
    @Deprecated("This function is deprecated in favor of the new IVRInput system.")
    fun getControllerStateWithPose(origin: TrackingUniverseOrigin, controllerDeviceIndex: TrackedDeviceIndex, controllerState: VRControllerState, trackedDevicePose: TrackedDevicePose): Boolean =
            nVRSystem_GetControllerStateWithPose(origin.i, controllerDeviceIndex, controllerState.adr, VRControllerState.SIZEOF, trackedDevicePose.adr)

    /**
     * Trigger a single haptic pulse on a controller. After this call the application may not trigger another haptic pulse on this controller and axis
     * combination for 5ms.
     *
     * @param controllerDeviceIndex the tracked device index of the controller to trigger a haptic pulse on
     * @param axisId                the ID of the axis to trigger a haptic pulse on
     * @param durationMicroSec      the duration of the desired haptic pulse in microseconds
     */
    @Deprecated("This function is deprecated in favor of the new IVRInput system.")
    fun triggerHapticPulse(controllerDeviceIndex: TrackedDeviceIndex, axisId: Int, durationMicroSec: Int) =
            VRSystem_TriggerHapticPulse(controllerDeviceIndex, axisId, durationMicroSec.s)

    /**
     * Kind of useless on JVM, but it will be offered anyway on the enum itself
     * Returns the name of an {@code VRButtonId} enum value.
     *
     * @param eButtonId the button ID to return the name of. One of:<br><table><tr><td>{@link VR#EVRButtonId_k_EButton_System}</td><td>{@link VR#EVRButtonId_k_EButton_ApplicationMenu}</td></tr><tr><td>{@link VR#EVRButtonId_k_EButton_Grip}</td><td>{@link VR#EVRButtonId_k_EButton_DPad_Left}</td></tr><tr><td>{@link VR#EVRButtonId_k_EButton_DPad_Up}</td><td>{@link VR#EVRButtonId_k_EButton_DPad_Right}</td></tr><tr><td>{@link VR#EVRButtonId_k_EButton_DPad_Down}</td><td>{@link VR#EVRButtonId_k_EButton_A}</td></tr><tr><td>{@link VR#EVRButtonId_k_EButton_ProximitySensor}</td><td>{@link VR#EVRButtonId_k_EButton_Axis0}</td></tr><tr><td>{@link VR#EVRButtonId_k_EButton_Axis1}</td><td>{@link VR#EVRButtonId_k_EButton_Axis2}</td></tr><tr><td>{@link VR#EVRButtonId_k_EButton_Axis3}</td><td>{@link VR#EVRButtonId_k_EButton_Axis4}</td></tr><tr><td>{@link VR#EVRButtonId_k_EButton_SteamVR_Touchpad}</td><td>{@link VR#EVRButtonId_k_EButton_SteamVR_Trigger}</td></tr><tr><td>{@link VR#EVRButtonId_k_EButton_Dashboard_Back}</td><td>{@link VR#EVRButtonId_k_EButton_Max}</td></tr></table>
     */
    //@Nullable
    //@NativeType("char *")
    //public static String VRSystem_GetButtonIdNameFromEnum(@NativeType("VRButtonId") int eButtonId) {
    //    long __result = nVRSystem_GetButtonIdNameFromEnum(eButtonId);
    //    return memASCIISafe(__result);
    //}

    /**
     * Kind of useless on JVM, but it will be offered anyway on the enum itself
     * Returns the name of an {@code VRControllerAxisType} enum value.
     *
     * @param eAxisType the controller axis type to get a name for. One of:<br><table><tr><td>{@link VR#EVRControllerAxisType_k_eControllerAxis_None}</td></tr><tr><td>{@link VR#EVRControllerAxisType_k_eControllerAxis_TrackPad}</td></tr><tr><td>{@link VR#EVRControllerAxisType_k_eControllerAxis_Joystick}</td></tr><tr><td>{@link VR#EVRControllerAxisType_k_eControllerAxis_Trigger}</td></tr></table>
     */
    //@Nullable
    //@NativeType("char *")
    //public static String VRSystem_GetControllerAxisTypeNameFromEnum(@NativeType("VRControllerAxisType") int eAxisType) {
    //    long __result = nVRSystem_GetControllerAxisTypeNameFromEnum(eAxisType);
    //    return memASCIISafe(__result);
    //}


    /**
     * Returns true if this application is receiving input from the system. This would return false if system-related functionality
     * is consuming the input stream.
     */
    val isInputAvailable: Boolean
        get() = VRSystem_IsInputAvailable()

    /**
     * Returns true if SteamVR is drawing controllers on top of the application. Applications should consider not drawing anything attached to the user's
     * hands in this case.
     */
    val isSteamVRDrawingControllers: Boolean
        get() = VRSystem_IsSteamVRDrawingControllers()

    /**
     * Returns true if the user has put SteamVR into a mode that is distracting them from the application. For applications where this is appropriate, the
     * application should pause ongoing activity.
     */
    val shouldApplicationPause: Boolean
        get() = VRSystem_ShouldApplicationPause()

    /**
     * Returns true if SteamVR is doing significant rendering work and the game should do what it can to reduce its own workload. One common way to do this is
     * to reduce the size of the render target provided for each eye.
     */
    val shouldApplicationReduceRenderingWork: Boolean
        get() = VRSystem_ShouldApplicationReduceRenderingWork()


    // ------------------------------------
    // Firmware methods
    // ------------------------------------


    /**
     * Performs the actual firmware update if applicable.
     *
     * <p>The following events will be sent, if {@link VR#EVRFirmwareError_VRFirmwareError_None} was returned: {@link VR#EVREventType_VREvent_FirmwareUpdateStarted},
     * {@link VR#EVREventType_VREvent_FirmwareUpdateFinished}</p>
     *
     * <p>Use the properties {@link VR#ETrackedDeviceProperty_Prop_Firmware_UpdateAvailable_Bool}, {@link VR#ETrackedDeviceProperty_Prop_Firmware_ManualUpdate_Bool}, and
     * {@link VR#ETrackedDeviceProperty_Prop_Firmware_ManualUpdateURL_String} to figure our whether a firmware update is available, and to figure out whether its a
     * manual update. {@link VR#ETrackedDeviceProperty_Prop_Firmware_ManualUpdateURL_String} should point to an URL describing the manual update process.</p>
     *
     * @param deviceIndex
     */
    infix fun performFirmwareUpdate(deviceIndex: TrackedDeviceIndex): VRFirmwareError =
            VRFirmwareError of VRSystem_PerformFirmwareUpdate(deviceIndex)


    // ------------------------------------
    // Application life cycle methods
    // ------------------------------------


    /**
     * Call this to acknowledge to the system that {@link VR#EVREventType_VREvent_Quit} has been received and that the process is exiting. This extends the timeout
     * until the process is killed.
     */
    fun acknowledgeQuit_Exiting() = VRSystem_AcknowledgeQuit_Exiting()

    /**
     * Call this to tell the system that the user is being prompted to save data. This halts the timeout and dismisses the dashboard (if it was up).
     * Applications should be sure to actually prompt the user to save and then exit afterward, otherwise the user will be left in a confusing state.
     */
    fun acknowledgeQuit_UserPrompt() = VRSystem_AcknowledgeQuit_UserPrompt()

    // -------------------------------------
    // App container sandbox methods
    // -------------------------------------

    /** Retrieves a null-terminated, semicolon-delimited list of UTF8 file paths that an application
     * must have read access to when running inside of an app container. Returns the number of bytes
     * needed to hold the list. */
    val appContainerFilePaths: List<String>
        get() = stak { s ->
            val bytes = nVRSystem_GetAppContainerFilePaths(NULL, 0)
            val buffer = s.malloc(bytes).adr
            nVRSystem_GetAppContainerFilePaths(buffer, bytes)
            memASCII(buffer).split(';')
        }

    // -------------------------------------
    // System methods
    // -------------------------------------

    /** Returns the current version of the SteamVR runtime. The returned string will remain valid until VR_Shutdown is called.
     *
     * NOTE: Is it not appropriate to use this version to test for the presence of any SteamVR feature. Only use this version
     * number for logging or showing to a user, and not to try to detect anything at runtime. When appropriate, feature-specific
     * presence information is provided by other APIs. */
    val runtimeVersion: String
        get() = TODO()//memASCII(VRSGRun)

    override val version: String
        get() = "IVRSystem_020"
}