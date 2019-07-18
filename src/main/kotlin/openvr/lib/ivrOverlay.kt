package openvr.lib

import glm_.BYTES
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec2.Vec2i
import glm_.vec3.Vec3
import kool.adr
import kool.rem
import kool.set
import org.lwjgl.PointerBuffer
import org.lwjgl.openvr.*
import org.lwjgl.openvr.VROverlay.*
import org.lwjgl.system.MemoryUtil.*
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.LongBuffer

object vrOverlay : vrInterface {

    /** The maximum length of an overlay key in bytes, counting the terminating null character. */
    const val MaxKeyLength = 128

    /** The maximum length of an overlay name in bytes, counting the terminating null character. */
    const val MaxNameLength = 128

    /** The maximum number of overlays that can exist in the system at one time. */
    const val MaxCount = 64

    /** The maximum number of overlay intersection mask primitives per overlay */
    const val MaxIntersectionMaskPrimitivesCount = 32

    /** Errors that can occur around VR overlays */
    enum class Error(@JvmField val i: Int) {

        None(0),

        UnknownOverlay(10),
        InvalidHandle(11),
        PermissionDenied(12),
        /** No more overlays could be created because the maximum number already exist  */
        OverlayLimitExceeded(13),
        WrongVisibilityType(14),
        KeyTooLong(15),
        NameTooLong(16),
        KeyInUse(17),
        WrongTransformType(18),
        InvalidTrackedDevice(19),
        InvalidParameter(20),
        ThumbnailCantBeDestroyed(21),
        ArrayTooSmall(22),
        RequestFailed(23),
        InvalidTexture(24),
        UnableToLoadFile(25),
        KeyboardAlreadyInUse(26),
        NoNeighbor(27),
        TooManyMaskPrimitives(29),
        BadMaskPrimitive(30),
        TextureAlreadyLocked(31),
        TextureLockCapacityReached(32),
        TextureNotLocked(33);

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }

        override fun toString(): String = stak { memASCII(nVROverlay_GetOverlayErrorNameFromEnum(i)) }
    }

    val pError = memCallocInt(1)

    var error: Error
        get() = Error of pError[0]
        set(value) {
            pError[0] = value.i
        }

    enum class MessageResponse(@JvmField val i: Int) {

        ButtonPress_0(0),
        ButtonPress_1(1),
        ButtonPress_2(2),
        ButtonPress_3(3),
        CouldntFindSystemOverlay(4),
        CouldntFindOrCreateClientOverlay(5),
        ApplicationQuit(6);

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    /** Types of input supported by VR Overlays */
    enum class InputMethod {
        /** No input events will be generated automatically for this overlay    */
        None,
        /** Tracked controllers will get mouse events automatically */
        Mouse,
        /** Analog inputs from tracked controllers are turned into DualAnalog events */
        DualAnalog;

        val i = ordinal

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    /** Allows the caller to figure out which overlay transform getter to call. */
    enum class VROverlayTransformType(@JvmField val i: Int) {

        Absolute(0),
        TrackedDeviceRelative(1),
        SystemOverlay(2),
        TrackedComponent(3);

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    /** Overlay control settings */
    enum class VROverlayFlags(@JvmField val i: Int) {

        None(0),

        /** The following only take effect when rendered using the high quality render path (see SetHighQualityOverlay).    */
        Curved(1),
        RGSS4X(2),

        /** Set this flag on a dashboard overlay to prevent a tab from showing up for that overlay  */
        NoDashboardTab(3),

        /** Set this flag on a dashboard that is able to deal with gamepad focus events */
        AcceptsGamepadEvents(4),

        /** Indicates that the overlay should dim/brighten to show gamepad focus    */
        ShowGamepadFocus(5),

        /** When this is set the overlay will receive VREvent_ScrollDiscrete events like a mouse wheel.
         *  Requires mouse input mode.  */
        SendVRDiscreteScrollEvents(6),

        /** Indicates that the overlay would like to receive */
        SendVRTouchpadEvents(7),

        /** If set this will render a vertical scroll wheel on the primary controller), only needed if not using
         *  SendVRScrollEvents but you still want to represent a scroll wheel   */
        ShowTouchPadScrollWheel(8),

        /** If this is set ownership and render access to the overlay are transferred to the new scene process on a call
         *  to openvr.lib.IVRApplications::LaunchInternalProcess    */
        TransferOwnershipToInternalProcess(9),

        // If set), renders 50% of the texture in each eye), side by side
        /** Texture is left/right   */
        SideBySide_Parallel(10),
        /** Texture is crossed and right/left   */
        SideBySide_Crossed(11),
        /** Texture is a panorama   */
        Panorama(12),
        /** Texture is a stereo panorama    */
        StereoPanorama(13),

        /** If this is set on an overlay owned by the scene application that overlay will be sorted with the "Other"
         *  overlays on top of all other scene overlays */
        SortWithNonSceneOverlays(14),

        /** If set, the overlay will be shown in the dashboard, otherwise it will be hidden.    */
        VisibleInDashboard(15),

        /** If this is set and the overlay's input method is not none, the system-wide laser mouse
         *  mode will be activated whenever this overlay is visible. */
        MakeOverlaysInteractiveIfVisible(16),

        /** If this is set the overlay will receive smooth VREvent_ScrollSmooth that emulate trackpad scrolling.
         *  Requires mouse input mode.  */
        SendVRSmoothScrollEvents(17);

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    // ---------------------------------------------
    // Overlay management methods
    // ---------------------------------------------


    /**
     * Finds an existing overlay with the specified key.
     *
     * @param overlayKey
     * @param overlayHandle
     */
    fun findOverlay(overlayKey: String, overlayHandle: VROverlayHandleBuffer): Error =
            stak { Error of nVROverlay_FindOverlay(it.addressOfAscii(overlayKey), overlayHandle.adr) }

    /**
     * Creates a new named overlay. All overlays start hidden and with default settings.
     *
     * @param overlayKey
     * @param overlayName
     * @param overlayHandle ~VROverlayHandle
     */
    fun createOverlay(overlayKey: String, overlayName: String, overlayHandle: VROverlayHandleBuffer): Error =
            stak { Error of nVROverlay_CreateOverlay(it.addressOfAscii(overlayKey), it.addressOfAscii(overlayName), overlayHandle.adr) }

    /**
     * Destroys the specified overlay. When an application calls {@link VR#VR_ShutdownInternal ShutdownInternal} all overlays created by that app are automatically destroyed.
     *
     * @param overlayHandle
     */
    infix fun destroyOverlay(overlayHandle: VROverlayHandle): Error = Error of VROverlay_DestroyOverlay(overlayHandle)

    /**
     *
     *
     * @param overlayHandle
     */
    infix fun setHighQualityOverlay(overlayHandle: VROverlayHandle): Error =
            Error of VROverlay_SetHighQualityOverlay(overlayHandle)

    /**
     * JVM custom
     *
     * Getter:
     *  Returns the overlay handle of the current overlay being rendered using the single high quality overlay render path. Otherwise it will return
     *  {@link VR#k_ulOverlayHandleInvalid}.
     *
     * Setter:
     *  Specify which overlay to use the high quality render path.
     *
     *  <p>This overlay will be composited in during the distortion pass which results in it drawing on top of everything else, but also at a higher quality as it
     *  samples the source texture directly rather than rasterizing into each eye's render texture first. Because if this, only one of these is supported at
     *  any given time. It is most useful for overlays that are expected to take up most of the user's view (e.g. streaming video). This mode does not support
     *  mouse input to your overlay.</p>
     *
     *  Note: Multi-thread unsafe if reading the error from the class property.
     */
    var highQualityOverlay: VROverlayHandle
        get() = VROverlay_GetHighQualityOverlay()
        set(value) {
            error = Error of VROverlay_SetHighQualityOverlay(value)
        }

    /**
     * Fills the provided buffer with the string key of the overlay. Returns the size of buffer required to store the key, including the terminating null
     * character. {@link VR#k_unVROverlayMaxKeyLength} will be enough bytes to fit the string.
     *
     * Note: Multi-thread unsafe if not passing a pError and reading it from the class property.
     *
     * @param overlayHandle
     * @param pErr
     */
    fun getOverlayKey(overlayHandle: VROverlayHandle, pErr: VROverlayErrorBuffer = pError): String =
            stak {
                val value = it.malloc(1, MaxKeyLength)
                val size = nVROverlay_GetOverlayKey(overlayHandle, value.adr, MaxKeyLength, pErr.adr)
                memASCII(value, size - 1)
            }

    /**
     * Fills the provided buffer with the friendly name of the overlay. Returns the size of buffer required to store the key, including the terminating null
     * character. {@link VR#k_unVROverlayMaxNameLength} will be enough bytes to fit the string.
     *
     * Note: Multi-thread unsafe if not passing a pError and reading it from the class property.
     *
     * @param overlayHandle
     * @param error
     */
    fun getOverlayName(overlayHandle: VROverlayHandle, error: VROverlayErrorBuffer = pError): String =
            stak {
                val value = it.malloc(MaxNameLength)
                val size = nVROverlay_GetOverlayName(overlayHandle, value.adr, MaxKeyLength, error.adr)
                memASCII(value, size - 1)
            }

    /**
     * Sets the name to use for this overlay.
     *
     * @param overlayHandle
     * @param name
     */
    fun setOverlayName(overlayHandle: VROverlayHandle, name: String): Error =
            stak { Error of nVROverlay_SetOverlayName(overlayHandle, it.addressOfAscii(name)) }

    /**
     * Gets the raw image data from an overlay. Overlay image data is always returned as RGBA data, 4 bytes per pixel. If the buffer is not large enough,
     * width and height will be set and {@link VR#EVROverlayError_VROverlayError_ArrayTooSmall} is returned.
     *
     * @param overlayHandle
     * @param buffer
     * @param punWidth
     * @param punHeight
     */
    fun getOverlayImageData(overlayHandle: VROverlayHandle, buffer: ByteBuffer, size: Vec2i): Error =
            stak {
                val width = it.nmalloc(1, Vec2i.size)
                val height = width + Int.BYTES
                Error of nVROverlay_GetOverlayImageData(overlayHandle, buffer.adr, buffer.rem, width, height).also {
                    size(memGetInt(width), memGetInt(height))
                }
            }

    /**
     * Kind of useless on JVM, but it will be offered anyway on the enum itself
     * Returns a string that corresponds with the specified overlay error. The string will be the name of the error enum value for all valid error codes.
     *
     * @param error one of:<br><table><tr><td>{@link VR#EVROverlayError_VROverlayError_None}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_UnknownOverlay}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_InvalidHandle}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_PermissionDenied}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_OverlayLimitExceeded}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_WrongVisibilityType}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_KeyTooLong}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_NameTooLong}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_KeyInUse}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_WrongTransformType}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_InvalidTrackedDevice}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_InvalidParameter}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_ThumbnailCantBeDestroyed}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_ArrayTooSmall}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_RequestFailed}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_InvalidTexture}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_UnableToLoadFile}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_KeyboardAlreadyInUse}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_NoNeighbor}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_TooManyMaskPrimitives}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_BadMaskPrimitive}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_TextureAlreadyLocked}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_TextureLockCapacityReached}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_TextureNotLocked}</td></tr></table>
     */
    //@Nullable
    //@NativeType("char const *")
    //public static String VROverlay_GetOverlayErrorNameFromEnum(@NativeType("Error") int error) {
    //    long __result = nVROverlay_GetOverlayErrorNameFromEnum (error);
    //    return memASCIISafe(__result);
    //}


    // ---------------------------------------------
    // Overlay rendering methods
    // ---------------------------------------------


    /**
     * Sets the pid that is allowed to render to this overlay (the creator pid is always allow to render), by default this is the pid of the process that made
     * the overlay.
     *
     * @param overlayHandle
     * @param pid
     */
    fun setOverlayRenderingPid(overlayHandle: VROverlayHandle, pid: Int): Error =
            Error of VROverlay_SetOverlayRenderingPid(overlayHandle, pid)

    /**
     * Gets the pid that is allowed to render to this overlay.
     *
     * @param overlayHandle
     */
    infix fun getOverlayRenderingPid(overlayHandle: VROverlayHandle): Int =
            VROverlay_GetOverlayRenderingPid(overlayHandle)

    /**
     * Specify flag setting for a given overlay.
     *
     * @param overlayHandle
     * @param overlayFlag    one of:<br><table><tr><td>{@link VR#VROverlayFlags_None}</td><td>{@link VR#VROverlayFlags_Curved}</td></tr><tr><td>{@link VR#VROverlayFlags_RGSS4X}</td><td>{@link VR#VROverlayFlags_NoDashboardTab}</td></tr><tr><td>{@link VR#VROverlayFlags_AcceptsGamepadEvents}</td><td>{@link VR#VROverlayFlags_ShowGamepadFocus}</td></tr><tr><td>{@link VR#VROverlayFlags_SendVRScrollEvents}</td><td>{@link VR#VROverlayFlags_SendVRTouchpadEvents}</td></tr><tr><td>{@link VR#VROverlayFlags_ShowTouchPadScrollWheel}</td><td>{@link VR#VROverlayFlags_TransferOwnershipToInternalProcess}</td></tr><tr><td>{@link VR#VROverlayFlags_SideBySide_Parallel}</td><td>{@link VR#VROverlayFlags_SideBySide_Crossed}</td></tr><tr><td>{@link VR#VROverlayFlags_Panorama}</td><td>{@link VR#VROverlayFlags_StereoPanorama}</td></tr><tr><td>{@link VR#VROverlayFlags_SortWithNonSceneOverlays}</td><td>{@link VR#VROverlayFlags_VisibleInDashboard}</td></tr></table>
     * @param enabled
     */
    fun setOverlayFlag(overlayHandle: VROverlayHandle, overlayFlag: VROverlayFlags, enabled: Boolean): Error =
            Error of VROverlay_SetOverlayFlag(overlayHandle, overlayFlag.i, enabled)

    /**
     * Sets flag setting for a given overlay.
     *
     * @param overlayHandle
     * @param overlayFlag    one of:<br><table><tr><td>{@link VR#VROverlayFlags_None}</td><td>{@link VR#VROverlayFlags_Curved}</td></tr><tr><td>{@link VR#VROverlayFlags_RGSS4X}</td><td>{@link VR#VROverlayFlags_NoDashboardTab}</td></tr><tr><td>{@link VR#VROverlayFlags_AcceptsGamepadEvents}</td><td>{@link VR#VROverlayFlags_ShowGamepadFocus}</td></tr><tr><td>{@link VR#VROverlayFlags_SendVRScrollEvents}</td><td>{@link VR#VROverlayFlags_SendVRTouchpadEvents}</td></tr><tr><td>{@link VR#VROverlayFlags_ShowTouchPadScrollWheel}</td><td>{@link VR#VROverlayFlags_TransferOwnershipToInternalProcess}</td></tr><tr><td>{@link VR#VROverlayFlags_SideBySide_Parallel}</td><td>{@link VR#VROverlayFlags_SideBySide_Crossed}</td></tr><tr><td>{@link VR#VROverlayFlags_Panorama}</td><td>{@link VR#VROverlayFlags_StereoPanorama}</td></tr><tr><td>{@link VR#VROverlayFlags_SortWithNonSceneOverlays}</td><td>{@link VR#VROverlayFlags_VisibleInDashboard}</td></tr></table>
     * @param enabled ~bool*
     */
    fun getOverlayFlag(overlayHandle: VROverlayHandle, overlayFlag: VROverlayFlags, enabled: ByteBuffer): Error =
            Error of nVROverlay_GetOverlayFlag(overlayHandle, overlayFlag.i, enabled.adr)

    /**
     * Sets the color tint of the overlay quad. Use 0.0 to 1.0 per channel.
     *
     * @param overlayHandle
     * @param red
     * @param green
     * @param blue
     */
    fun setOverlayColor(overlayHandle: VROverlayHandle, red: Float, green: Float, blue: Float): Error =
            Error of VROverlay_SetOverlayColor(overlayHandle, red, green, blue)

    /**
     * Gets the color tint of the overlay quad.
     *
     * @param overlayHandle
     * @param color
     */
    fun getOverlayColor(overlayHandle: VROverlayHandle, color: Vec3): Error =
            stak {
                val red = it.nmalloc(1, Vec3.size)
                val green = red + Float.BYTES
                val blue = green + Float.BYTES
                Error of nVROverlay_GetOverlayColor(overlayHandle, red, green, blue).also {
                    color(memGetFloat(red), memGetFloat(green), memGetFloat(blue))
                }
            }

    /**
     * Sets the alpha of the overlay quad. Use 1.0 for 100 percent opacity to 0.0 for 0 percent opacity.
     *
     * @param overlayHandle
     * @param alpha
     */
    fun setOverlayAlpha(overlayHandle: VROverlayHandle, alpha: Float): Error =
            Error of VROverlay_SetOverlayAlpha(overlayHandle, alpha)

    /**
     * Gets the alpha of the overlay quad. By default overlays are rendering at 100 percent alpha (1.0).
     *
     * @param overlayHandle
     * @param alpha
     */
    fun getOverlayAlpha(overlayHandle: VROverlayHandle, alpha: FloatBuffer): Error =
            Error of nVROverlay_GetOverlayAlpha(overlayHandle, alpha.adr)

    /**
     * Sets the aspect ratio of the texels in the overlay. 1.0 means the texels are square. 2.0 means the texels are twice as wide as they are tall.
     *
     * <p>Defaults to 1.0.</p>
     *
     * @param overlayHandle
     * @param texelAspect
     */
    fun setOverlayTexelAspect(overlayHandle: VROverlayHandle, texelAspect: Float): Error =
            Error of VROverlay_SetOverlayTexelAspect(overlayHandle, texelAspect)

    /**
     * Gets the aspect ratio of the texels in the overlay. Defaults to 1.0.
     *
     * @param overlayHandle
     * @param texelAspect
     */
    fun getOverlayTexelAspect(overlayHandle: VROverlayHandle, texelAspect: FloatBuffer): Error =
            Error of nVROverlay_GetOverlayTexelAspect(overlayHandle, texelAspect.adr)

    /**
     * Sets the rendering sort order for the overlay. Overlays are rendered this order:
     *
     * <ul>
     * <li>Overlays owned by the scene application</li>
     * <li>Overlays owned by some other application</li>
     * </ul>
     *
     * <p>Within a category overlays are rendered lowest sort order to highest sort order. Overlays with the same sort order are rendered back to front base on
     * distance from the HMD.</p>
     *
     * <p>Sort order defaults to 0.</p>
     *
     * @param overlayHandle
     * @param sortOrder
     */
    fun setOverlaySortOrder(overlayHandle: VROverlayHandle, sortOrder: Int): Error =
            Error of VROverlay_SetOverlaySortOrder(overlayHandle, sortOrder)

    /**
     * Gets the sort order of the overlay. See {@link #VROverlay_SetOverlaySortOrder SetOverlaySortOrder} for how this works.
     *
     * @param overlayHandle
     * @param sortOrder
     */
    fun getOverlaySortOrder(overlayHandle: VROverlayHandle, sortOrder: IntBuffer): Error =
            Error of nVROverlay_GetOverlaySortOrder(overlayHandle, sortOrder.adr)

    /**
     * Sets the width of the overlay quad in meters. By default overlays are rendered on a quad that is 1 meter across.
     *
     * @param overlayHandle
     * @param widthInMeters
     */
    fun setOverlayWidthInMeters(overlayHandle: VROverlayHandle, widthInMeters: Float): Error =
            Error of VROverlay_SetOverlayWidthInMeters(overlayHandle, widthInMeters)

    /**
     * Returns the width of the overlay quad in meters. By default overlays are rendered on a quad that is 1 meter across.
     *
     * @param overlayHandle
     * @param widthInMeters
     */
    fun getOverlayWidthInMeters(overlayHandle: VROverlayHandle, widthInMeters: FloatBuffer): Error =
            Error of nVROverlay_GetOverlayWidthInMeters(overlayHandle, widthInMeters.adr)

    /**
     * For high-quality curved overlays only, sets the distance range in meters from the overlay used to automatically curve the surface around the viewer.
     * Min is distance is when the surface will be most curved. Max is when least curved.
     *
     * @param overlayHandle
     * @param minDistanceInMeters
     * @param maxDistanceInMeters
     */
    fun setOverlayAutoCurveDistanceRangeInMeters(overlayHandle: VROverlayHandle, minDistanceInMeters: Float, maxDistanceInMeters: Float): Error =
            Error of VROverlay_SetOverlayAutoCurveDistanceRangeInMeters(overlayHandle, minDistanceInMeters, maxDistanceInMeters)

    /**
     * For high-quality curved overlays only, gets the distance range in meters from the overlay used to automatically curve the surface around the viewer.
     * Min is distance is when the surface will be most curved. Max is when least curved.
     *
     * @param overlayHandle
     * @param minDistanceInMeters
     * @param maxDistanceInMeters
     */
    fun getOverlayAutoCurveDistanceRangeInMeters(overlayHandle: VROverlayHandle, minDistanceInMeters: FloatBuffer, maxDistanceInMeters: FloatBuffer): Error =
            Error of nVROverlay_GetOverlayAutoCurveDistanceRangeInMeters(overlayHandle, minDistanceInMeters.adr, maxDistanceInMeters.adr)

    /**
     * Sets the colorspace the overlay texture's data is in. Defaults to 'auto'. If the texture needs to be resolved, you should call {@link #VROverlay_SetOverlayTexture SetOverlayTexture}
     * with the appropriate colorspace instead.
     *
     * @param overlayHandle
     * @param textureColorSpace one of:<br><table><tr><td>{@link VR#EColorSpace_ColorSpace_Auto}</td><td>{@link VR#EColorSpace_ColorSpace_Gamma}</td></tr><tr><td>{@link VR#EColorSpace_ColorSpace_Linear}</td></tr></table>
     */
    fun setOverlayTextureColorSpace(overlayHandle: VROverlayHandle, textureColorSpace: ColorSpace): Error =
            Error of VROverlay_SetOverlayTextureColorSpace(overlayHandle, textureColorSpace.i)

    /**
     * Gets the overlay's current colorspace setting.
     *
     * @param overlayHandle
     * @param textureColorSpace ~ EColorSpace *
     */
    fun getOverlayTextureColorSpace(overlayHandle: VROverlayHandle, textureColorSpace: IntBuffer): Error =
            Error of nVROverlay_GetOverlayTextureColorSpace(overlayHandle, textureColorSpace.adr)

    /**
     * Sets the part of the texture to use for the overlay. UV Min is the upper left corner and UV Max is the lower right corner.
     *
     * @param overlayHandle
     * @param overlayTextureBounds
     */
    fun setOverlayTextureBounds(overlayHandle: VROverlayHandle, overlayTextureBounds: VRTextureBounds): Error =
            Error of nVROverlay_SetOverlayTextureBounds(overlayHandle, overlayTextureBounds.adr)

    /**
     * Gets the part of the texture to use for the overlay. UV Min is the upper left corner and UV Max is the lower right corner.
     *
     * @param overlayHandle
     * @param overlayTextureBounds
     */
    fun getOverlayTextureBounds(overlayHandle: VROverlayHandle, overlayTextureBounds: VRTextureBounds): Error =
            Error of nVROverlay_GetOverlayTextureBounds(overlayHandle, overlayTextureBounds.adr)

    /**
     * Gets render model to draw behind this overlay.
     *
     * @param overlayHandle
     * @param value TODO check if bytebuffer is right
     * @param color
     * @param error
     */
    fun getOverlayRenderModel(overlayHandle: VROverlayHandle, value: ByteBuffer, color: HmdColor, error: VROverlayErrorBuffer): Int =
            nVROverlay_GetOverlayRenderModel(overlayHandle, value.adr, value.rem, color.adr, error.adr)

    fun setOverlayRenderModel(overlayHandle: VROverlayHandle, renderModel: String, color: HmdColor): Error =
            stak { Error of nVROverlay_SetOverlayRenderModel(overlayHandle, it.addressOfAscii(renderModel), color.adr) }

    /**
     * Returns the transform type of this overlay.
     *
     * @param overlayHandle
     * @param transformType ~ VROverlayTransformType *
     */
    fun getOverlayTransformType(overlayHandle: VROverlayHandle, transformType: IntBuffer): Error =
            Error of nVROverlay_GetOverlayTransformType(overlayHandle, transformType.adr)

    /**
     * Sets the transform to absolute tracking origin.
     *
     * @param overlayHandle
     * @param trackingOrigin                      one of:<br><table><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseSeated}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseStanding}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseRawAndUncalibrated}</td></tr></table>
     * @param matTrackingOriginToOverlayTransform
     */
    fun setOverlayTransformAbsolute(overlayHandle: VROverlayHandle, trackingOrigin: TrackingUniverseOrigin, matTrackingOriginToOverlayTransform: Mat4): Error =
            Error of nVROverlay_SetOverlayTransformAbsolute(overlayHandle, trackingOrigin.i, vr.HmdMatrix34(matTrackingOriginToOverlayTransform).adr)

    /**
     * Gets the transform if it is absolute. Returns an error if the transform is some other type.
     *
     * @param overlayHandle
     * @param trackingOrigin ~ETrackingUniverseOrigin *
     * @param matTrackingOriginToOverlayTransform
     */
    fun getOverlayTransformAbsolute(overlayHandle: VROverlayHandle, trackingOrigin: IntBuffer, matTrackingOriginToOverlayTransform: Mat4): Error {
        val hmdMat34 = vr.HmdMatrix34()
        return Error of nVROverlay_GetOverlayTransformAbsolute(overlayHandle, trackingOrigin.adr, hmdMat34.adr).also {
            hmdMat34 to matTrackingOriginToOverlayTransform
        }
    }

    /**
     * Sets the transform to relative to the transform of the specified tracked device.
     *
     * @param overlayHandle
     * @param trackedDevice
     * @param matTrackedDeviceToOverlayTransform
     */
    fun setOverlayTransformTrackedDeviceRelative(overlayHandle: VROverlayHandle, trackedDevice: TrackedDeviceIndex, matTrackedDeviceToOverlayTransform: Mat4): Error =
            Error of nVROverlay_SetOverlayTransformTrackedDeviceRelative(overlayHandle, trackedDevice, vr.HmdMatrix34(matTrackedDeviceToOverlayTransform).adr)

    /**
     * Gets the transform if it is relative to a tracked device. Returns an error if the transform is some other type.
     *
     * @param overlayHandle
     * @param trackedDevice ~ TrackedDeviceIndex *
     * @param matTrackedDeviceToOverlayTransform
     */
    fun getOverlayTransformTrackedDeviceRelative(overlayHandle: VROverlayHandle, trackedDevice: IntBuffer, matTrackedDeviceToOverlayTransform: Mat4): Error {
        val hmdMat34 = vr.HmdMatrix34()
        return Error of nVROverlay_GetOverlayTransformTrackedDeviceRelative(overlayHandle, trackedDevice.adr, hmdMat34.adr).also {
            hmdMat34 to matTrackedDeviceToOverlayTransform
        }
    }

    /**
     * Sets the transform to draw the overlay on a rendermodel component mesh instead of a quad. This will only draw when the system is drawing the device.
     * Overlays with this transform type cannot receive mouse events.
     *
     * @param overlayHandle
     * @param deviceIndex
     * @param componentName
     */
    fun setOverlayTransformTrackedDeviceComponent(overlayHandle: VROverlayHandle, deviceIndex: TrackedDeviceIndex, componentName: String): Error =
            stak { Error of nVROverlay_SetOverlayTransformTrackedDeviceComponent(overlayHandle, deviceIndex, it.addressOfAscii(componentName)) }

    /**
     * Gets the transform information when the overlay is rendering on a component.
     *
     * @param overlayHandle
     * @param deviceIndex ~ TrackedDeviceIndex *
     * @param componentName ~ char *
     */
    fun getOverlayTransformTrackedDeviceComponent(overlayHandle: VROverlayHandle, deviceIndex: IntBuffer, componentName: ByteBuffer): Error =
            Error of nVROverlay_GetOverlayTransformTrackedDeviceComponent(overlayHandle, deviceIndex.adr, componentName.adr, componentName.rem)

    fun getOverlayTransformOverlayRelative(overlayHandle: VROverlayHandle, overlayHandleParent: LongBuffer, matParentOverlayToOverlayTransform: Mat4): Error {
        val hmdMat34 = vr.HmdMatrix34()
        return Error of nVROverlay_GetOverlayTransformOverlayRelative(overlayHandle, overlayHandleParent.adr, hmdMat34.adr).also {
            hmdMat34 to matParentOverlayToOverlayTransform
        }
    }

    fun setOverlayTransformOverlayRelative(overlayHandle: VROverlayHandle, overlayHandleParent: VROverlayHandle, matParentOverlayToOverlayTransform: Mat4): Error =
            Error of nVROverlay_SetOverlayTransformOverlayRelative(overlayHandle, overlayHandleParent, vr.HmdMatrix34(matParentOverlayToOverlayTransform).adr)

    /**
     * Shows the VR overlay. For dashboard overlays, only the Dashboard Manager is allowed to call this.
     *
     * @param overlayHandle
     */
    infix fun showOverlay(overlayHandle: VROverlayHandle): Error = Error of VROverlay_ShowOverlay(overlayHandle)

    /**
     * Hides the VR overlay. For dashboard overlays, only the Dashboard Manager is allowed to call this.
     *
     * @param overlayHandle
     */
    infix fun hideOverlay(overlayHandle: VROverlayHandle): Error = Error of VROverlay_HideOverlay(overlayHandle)

    /**
     * Returns true if the overlay is visible.
     *
     * @param overlayHandle
     */
    infix fun isOverlayVisible(overlayHandle: VROverlayHandle): Boolean = VROverlay_IsOverlayVisible(overlayHandle)

    /**
     * Get the transform in 3d space associated with a specific 2d point in the overlay's coordinate space (where 0,0 is the lower left). -Z points out of the
     * overlay.
     *
     * @param overlayHandle
     * @param trackingOrigin      one of:<br><table><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseSeated}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseStanding}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseRawAndUncalibrated}</td></tr></table>
     * @param coordinatesInOverlay
     * @param matTransform
     */
    fun getTransformForOverlayCoordinates(overlayHandle: VROverlayHandle, trackingOrigin: TrackingUniverseOrigin, coordinatesInOverlay: Vec2, matTransform: Mat4): Error {
        val hmdVector2 = vr.HmdVector2(coordinatesInOverlay)
        val hmdMat34 = vr.HmdMatrix34()
        return Error of nVROverlay_GetTransformForOverlayCoordinates(overlayHandle, trackingOrigin.i, hmdVector2.adr, hmdMat34.adr).also {
            hmdMat34 to matTransform
        }
    }

    /**
     * Returns true and fills the event with the next event on the overlay's event queue, if there is one.
     *
     * <p>If there are no events this method returns false. {@code uncbVREvent} should be the size in bytes of the {@link VREvent} struct.</p>
     *
     * @param overlayHandle
     * @param event
     */
    fun pollNextOverlayEvent(overlayHandle: VROverlayHandle, event: VREvent): Boolean =
            nVROverlay_PollNextOverlayEvent(overlayHandle, event.adr, VREvent.SIZEOF)

    /**
     * JVM custom
     * Returns the current input settings for the specified overlay.
     *
     * @param overlayHandle
     */
    @JvmOverloads
    fun getOverlayInputMethod(overlayHandle: VROverlayHandle, pErr: VROverlayErrorBuffer = pError): InputMethod =
            InputMethod of stak.intAddress { pInputMethod ->
                pErr[0] = nVROverlay_GetOverlayInputMethod(overlayHandle, pInputMethod)
            }

    /**
     * Sets the input settings for the specified overlay.
     *
     * @param overlayHandle
     * @param inputMethod    one of:<br><table><tr><td>{@link VR#VROverlayInputMethod_None}</td><td>{@link VR#VROverlayInputMethod_Mouse}</td></tr><tr><td>{@link VR#VROverlayInputMethod_DualAnalog}</td></tr></table>
     */
    fun setOverlayInputMethod(overlayHandle: VROverlayHandle, inputMethod: InputMethod): Error =
            Error of VROverlay_SetOverlayInputMethod(overlayHandle, inputMethod.i)

    /**
     * Gets the mouse scaling factor that is used for mouse events. The actual texture may be a different size, but this is typically the size of the
     * underlying UI in pixels.
     *
     * @param overlayHandle
     * @param vecMouseScale
     */
    fun getOverlayMouseScale(overlayHandle: VROverlayHandle, vecMouseScale: Vec2): Error {
        val hmdVec2 = vr.HmdVector2()
        return Error of nVROverlay_GetOverlayMouseScale(overlayHandle, hmdVec2.adr).also {
            hmdVec2 to vecMouseScale
        }
    }

    /**
     * Sets the mouse scaling factor that is used for mouse events. The actual texture may be a different size, but this is typically the size of the
     * underlying UI in pixels (not in world space).
     *
     * @param overlayHandle
     * @param vecMouseScale
     */
    fun setOverlayMouseScale(overlayHandle: VROverlayHandle, vecMouseScale: Vec2): Error =
            Error of nVROverlay_SetOverlayMouseScale(overlayHandle, vr.HmdVector2(vecMouseScale).adr)

    /**
     * Computes the overlay-space pixel coordinates of where the ray intersects the overlay with the specified settings. Returns false if there is no
     * intersection.
     *
     * @param overlayHandle
     * @param params
     * @param results
     */
    fun computeOverlayIntersection(overlayHandle: VROverlayHandle, params: VROverlayIntersectionParams, results: VROverlayIntersectionResults): Boolean =
            nVROverlay_ComputeOverlayIntersection(overlayHandle, params.adr, results.adr)

    /**
     * Returns true if the specified overlay is the hover target. An overlay is the hover target when it is the last overlay "moused over" by the virtual
     * mouse pointer.
     *
     * @param overlayHandle
     */
    infix fun isHoverTargetOverlay(overlayHandle: VROverlayHandle): Boolean =
            VROverlay_IsHoverTargetOverlay(overlayHandle)

    /** Returns the current Gamepad focus overlay. */
    val gamepadFocusOverlay: VROverlayHandle
        get() = VROverlay_GetGamepadFocusOverlay()

    /**
     * Sets the current Gamepad focus overlay.
     *
     * @param newFocusOverlay
     */
    infix fun setGamepadFocusOverlay(newFocusOverlay: VROverlayHandle): Error =
            Error of VROverlay_SetGamepadFocusOverlay(newFocusOverlay)

    /**
     * Sets an overlay's neighbor. This will also set the neighbor of the "to" overlay to point back to the "from" overlay. If an overlay's neighbor is set to
     * invalid both ends will be cleared.
     * "
     *
     * @param direction one of:<br><table><tr><td>{@link VR#EOverlayDirection_OverlayDirection_Up}</td><td>{@link VR#EOverlayDirection_OverlayDirection_Down}</td></tr><tr><td>{@link VR#EOverlayDirection_OverlayDirection_Left}</td><td>{@link VR#EOverlayDirection_OverlayDirection_Right}</td></tr><tr><td>{@link VR#EOverlayDirection_OverlayDirection_Count}</td></tr></table>
     * @param from
     * @param to
     */
    fun setOverlayNeighbor(direction: OverlayDirection, from: VROverlayHandle, to: VROverlayHandle): Error =
            Error of VROverlay_SetOverlayNeighbor(direction.i, from, to)

    /**
     * Changes the Gamepad focus from one overlay to one of its neighbors.
     *
     * @param direction one of:<br><table><tr><td>{@link VR#EOverlayDirection_OverlayDirection_Up}</td><td>{@link VR#EOverlayDirection_OverlayDirection_Down}</td></tr><tr><td>{@link VR#EOverlayDirection_OverlayDirection_Left}</td><td>{@link VR#EOverlayDirection_OverlayDirection_Right}</td></tr><tr><td>{@link VR#EOverlayDirection_OverlayDirection_Count}</td></tr></table>
     * @param from
     *
     * @return {@link VR#EVROverlayError_VROverlayError_NoNeighbor} if there is no neighbor in that direction
     */
    fun moveGamepadFocusToNeighbor(direction: OverlayDirection, from: VROverlayHandle): Error =
            Error of VROverlay_MoveGamepadFocusToNeighbor(direction.i, from)

    /**
     * Sets the analog input to Dual Analog coordinate scale for the specified overlay.
     *
     * @param overlay
     * @param which    one of:<br><table><tr><td>{@link VR#EDualAnalogWhich_k_EDualAnalog_Left}</td><td>{@link VR#EDualAnalogWhich_k_EDualAnalog_Right}</td></tr></table>
     * @param center
     * @param radius
     */
    fun setOverlayDualAnalogTransform(overlay: VROverlayHandle, which: DualAnalogWhich, center: Vec2, radius: Float): Error =
            Error of nVROverlay_SetOverlayDualAnalogTransform(overlay, which.i, vr.HmdVector2(center).adr, radius)

    /**
     * Gets the analog input to Dual Analog coordinate scale for the specified overlay.
     *
     * @param overlay
     * @param which    one of:<br><table><tr><td>{@link VR#EDualAnalogWhich_k_EDualAnalog_Left}</td><td>{@link VR#EDualAnalogWhich_k_EDualAnalog_Right}</td></tr></table>
     * @param center
     * @param radius
     */
    fun getOverlayDualAnalogTransform(overlay: VROverlayHandle, which: DualAnalogWhich, center: Vec2, radius: FloatBuffer): Error {
        val hmdVec2 = vr.HmdVector2()
        return Error of nVROverlay_GetOverlayDualAnalogTransform(overlay, which.i, hmdVec2.adr, radius.adr).also {
            hmdVec2 to center
        }
    }

    /**
     * Texture to draw for the overlay. This function can only be called by the overlay's creator or renderer process (see {@link #VROverlay_SetOverlayRenderingPid SetOverlayRenderingPid}).
     *
     * @param overlayHandle
     * @param texture
     */
    fun setOverlayTexture(overlayHandle: VROverlayHandle, texture: Texture): Error =
            Error of nVROverlay_SetOverlayTexture(overlayHandle, texture.adr)

    /**
     * Use this to tell the overlay system to release the texture set for this overlay.
     *
     * @param overlayHandle
     */
    fun clearOverlayTexture(overlayHandle: VROverlayHandle): Error =
            Error of VROverlay_ClearOverlayTexture(overlayHandle)

    /**
     * Separate interface for providing the data as a stream of bytes, but there is an upper bound on data that can be sent. This function can only be called
     * by the overlay's renderer process.
     *
     * @param overlayHandle
     * @param buffer
     * @param width
     * @param height
     * @param depth
     */
    fun setOverlayRaw(overlayHandle: VROverlayHandle, buffer: ByteBuffer, width: Int, height: Int, depth: Int): Error =
            Error of nVROverlay_SetOverlayRaw(overlayHandle, buffer.adr, width, height, depth)

    /**
     * Separate interface for providing the image through a filename: can be png or jpg, and should not be bigger than 1920x1080. This function can only be
     * called by the overlay's renderer process
     *
     * @param overlayHandle
     * @param filePath
     */
    fun setOverlayFromFile(overlayHandle: VROverlayHandle, filePath: String): Error =
            stak { Error of nVROverlay_SetOverlayFromFile(overlayHandle, it.addressOfAscii(filePath)) }

    /**
     * Get the native texture handle/device for an overlay you have created.
     *
     * <p>On windows this handle will be a ID3D11ShaderResourceView with a ID3D11Texture2D bound.</p>
     *
     * <p>The texture will always be sized to match the backing texture you supplied in SetOverlayTexture above.</p>
     *
     * <p>You MUST call {@link #VROverlay_ReleaseNativeOverlayHandle ReleaseNativeOverlayHandle} with {@code nativeTextureHandle} once you are done with this texture.</p>
     *
     * <p>{@code nativeTextureHandle} is an OUTPUT, it will be a pointer to a {@code ID3D11ShaderResourceView *}.
     * {@code nativeTextureRef} is an INPUT and should be a {@code ID3D11Resource *}. The device used by {@code nativeTextureRef} will be used to bind
     * {@code nativeTextureHandle}.</p>
     *
     * @param overlayHandle
     * @param nativeTextureHandle
     * @param nativeTextureRef
     * @param width
     * @param height
     * @param nativeFormat
     * @param apiType
     * @param colorSpace
     * @param textureBounds
     */
    fun getOverlayTexture(overlayHandle: VROverlayHandle, nativeTextureHandle: PointerBuffer, nativeTextureRef: Long, width: IntBuffer, height: IntBuffer,
                          nativeFormat: IntBuffer, apiType: TextureTypeBuffer, colorSpace: ColorSpaceBuffer, textureBounds: VRTextureBounds): Error =
            Error of nVROverlay_GetOverlayTexture(overlayHandle, nativeTextureHandle.adr, nativeTextureRef, width.adr, height.adr, nativeFormat.adr, apiType.adr, colorSpace.adr, textureBounds.adr)

    /**
     * Release the {@code nativeTextureHandle} provided from the {@link #VROverlay_GetOverlayTexture GetOverlayTexture} call, this allows the system to free the underlying GPU resources for
     * this object, so only do it once you stop rendering this texture.
     *
     * @param overlayHandle
     * @param nativeTextureHandle
     */
    fun releaseNativeOverlayHandle(overlayHandle: VROverlayHandle, nativeTextureHandle: Long): Error =
            Error of VROverlay_ReleaseNativeOverlayHandle(overlayHandle, nativeTextureHandle)

    /**
     * Get the size of the overlay texture.
     *
     * @param overlayHandle
     * @param width
     * @param height
     */
    fun getOverlayTextureSize(overlayHandle: VROverlayHandle, width: IntBuffer, height: IntBuffer): Error =
            Error of nVROverlay_GetOverlayTextureSize(overlayHandle, width.adr, height.adr)

    /**
     * Creates a dashboard overlay and returns its handle.
     *
     * @param overlayKey
     * @param overlayFriendlyName
     * @param mainHandle   ~ VROverlayHandle *
     * @param thumbnailHandle  ~ VROverlayHandle *
     */
    fun createDashboardOverlay(overlayKey: String, overlayFriendlyName: String, mainHandle: LongBuffer, thumbnailHandle: LongBuffer): Error =
            stak { Error of nVROverlay_CreateDashboardOverlay(it.addressOfAscii(overlayKey), it.addressOfAscii(overlayFriendlyName), mainHandle.adr, thumbnailHandle.adr) }

    /** Returns true if the dashboard is visible. */
    val isDashboardVisible: Boolean
        get() = VROverlay_IsDashboardVisible()

    /**
     * Returns true if the dashboard is visible and the specified overlay is the active system Overlay.
     *
     * @param overlayHandle
     */
    infix fun isActiveDashboardOverlay(overlayHandle: VROverlayHandle): Boolean =
            VROverlay_IsActiveDashboardOverlay(overlayHandle)

    /**
     * Sets the dashboard overlay to only appear when the specified process ID has scene focus.
     *
     * @param overlayHandle
     * @param processId
     */
    fun setDashboardOverlaySceneProcess(overlayHandle: VROverlayHandle, processId: Int): Error =
            Error of VROverlay_SetDashboardOverlaySceneProcess(overlayHandle, processId)

    /**
     * Gets the process ID that this dashboard overlay requires to have scene focus.
     *
     * @param overlayHandle
     * @param processId
     */
    fun getDashboardOverlaySceneProcess(overlayHandle: VROverlayHandle, processId: IntBuffer): Error =
            Error of nVROverlay_GetDashboardOverlaySceneProcess(overlayHandle, processId.adr)

    /**
     * Shows the dashboard.
     *
     * @param overlayToShow
     */
    fun showDashboard(overlayToShow: String) = stak { nVROverlay_ShowDashboard(it.addressOfAscii(overlayToShow)) }

    /** Returns the tracked device that has the laser pointer in the dashboard. */
    val primaryDashboardDevice: TrackedDeviceIndex
        get() = VROverlay_GetPrimaryDashboardDevice()

    /**
     * Show the virtual keyboard to accept input.
     *
     * @param inputMode      one of:<br><table><tr><td>{@link VR#EGamepadTextInputMode_k_EGamepadTextInputModeNormal}</td></tr><tr><td>{@link VR#EGamepadTextInputMode_k_EGamepadTextInputModePassword}</td></tr><tr><td>{@link VR#EGamepadTextInputMode_k_EGamepadTextInputModeSubmit}</td></tr></table>
     * @param lineInputMode  one of:<br><table><tr><td>{@link VR#EGamepadTextInputLineMode_k_EGamepadTextInputLineModeSingleLine}</td></tr><tr><td>{@link VR#EGamepadTextInputLineMode_k_EGamepadTextInputLineModeMultipleLines}</td></tr></table>
     * @param description
     * @param charMax
     * @param existingText
     * @param useMinimalMode
     * @param userValue
     */
    fun showKeyboard(inputMode: GamepadTextInputMode, lineInputMode: GamepadTextInputLineMode, description: String, charMax: Int,
                     existingText: String, useMinimalMode: Boolean, userValue: Long): Error =
            stak { Error of nVROverlay_ShowKeyboard(inputMode.i, lineInputMode.i, it.addressOfAscii(description), charMax, it.addressOfAscii(existingText), useMinimalMode, userValue) }

    /**
     * @param overlayHandle
     * @param inputMode      one of:<br><table><tr><td>{@link VR#EGamepadTextInputMode_k_EGamepadTextInputModeNormal}</td></tr><tr><td>{@link VR#EGamepadTextInputMode_k_EGamepadTextInputModePassword}</td></tr><tr><td>{@link VR#EGamepadTextInputMode_k_EGamepadTextInputModeSubmit}</td></tr></table>
     * @param lineInputMode  one of:<br><table><tr><td>{@link VR#EGamepadTextInputLineMode_k_EGamepadTextInputLineModeSingleLine}</td></tr><tr><td>{@link VR#EGamepadTextInputLineMode_k_EGamepadTextInputLineModeMultipleLines}</td></tr></table>
     * @param description
     * @param charMax
     * @param existingText
     * @param useMinimalMode
     * @param userValue
     */
    fun showKeyboardForOverlay(overlayHandle: VROverlayHandle, inputMode: GamepadTextInputMode, lineInputMode: GamepadTextInputLineMode,
                               description: String, charMax: Int, existingText: String, useMinimalMode: Boolean, userValue: Long): Error =
            stak { Error of nVROverlay_ShowKeyboardForOverlay(overlayHandle, inputMode.i, lineInputMode.i, it.addressOfAscii(description), charMax, it.addressOfAscii(existingText), useMinimalMode, userValue) }

    /**
     * Get the textSize that was entered into the textSize input.
     *
     * @param textSize
     */
    fun getKeyboardText(textSize: Int): String =
            stak {
                val text = it.malloc(textSize)
                val size = nVROverlay_GetKeyboardText(text.adr, textSize)
                memASCII(text, size - 1)
            }

    /** Hide the virtual keyboard. */
    fun hideKeyboard() = VROverlay_HideKeyboard()

    /**
     * Set the position of the keyboard in world space.
     *
     * @param trackingOrigin                       one of:<br><table><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseSeated}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseStanding}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseRawAndUncalibrated}</td></tr></table>
     * @param matTrackingOriginToKeyboardTransform
     */
    fun setKeyboardTransformAbsolute(trackingOrigin: TrackingUniverseOrigin, matTrackingOriginToKeyboardTransform: Mat4) {
        val hmdMat34 = vr.HmdMatrix34(matTrackingOriginToKeyboardTransform)
        nVROverlay_SetKeyboardTransformAbsolute(trackingOrigin.i, hmdMat34.adr)
    }

    /**
     * Set the position of the keyboard in overlay space by telling it to avoid a rectangle in the overlay. Rectangle coords have (0,0) in the bottom left.
     *
     * @param overlayHandle
     * @param avoidRect
     */
    fun setKeyboardPositionForOverlay(overlayHandle: VROverlayHandle, avoidRect: HmdRect2) =
            nVROverlay_SetKeyboardPositionForOverlay(overlayHandle, avoidRect.adr)

    /**
     * Sets a list of primitives to be used for controller ray intersection typically the size of the underlying UI in pixels(not in world space).
     *
     * @param overlayHandle
     * @param maskPrimitives
     */
    fun setOverlayIntersectionMask(overlayHandle: VROverlayHandle, maskPrimitives: VROverlayIntersectionMaskPrimitive.Buffer): Error =
            Error of nVROverlay_SetOverlayIntersectionMask(overlayHandle, maskPrimitives.adr, maskPrimitives.rem, VROverlayIntersectionMaskPrimitive.SIZEOF)

    fun getOverlayFlags(overlayHandle: VROverlayHandle, flags: IntBuffer): Error =
            Error of nVROverlay_GetOverlayFlags(overlayHandle, flags.adr)

    /**
     * Show the message overlay. This will block and return you a result.
     *
     * @param text
     * @param caption
     * @param button0Text
     * @param button1Text
     * @param button2Text
     * @param button3Text
     */
    fun showMessageOverlay(text: String, caption: String, button0Text: String, button1Text: String?, button2Text: String?, button3Text: String?): MessageResponse =
            stak {s ->
                val textEncoded = s.addressOfAscii(text)
                val captionEncoded = s.addressOfAscii(caption)
                val button0TextEncoded = s.addressOfAscii(button0Text)
                val button1TextEncoded = button1Text?.let { s.addressOfAscii(it) } ?: NULL
                val button2TextEncoded = button2Text?.let { s.addressOfAscii(it) } ?: NULL
                val button3TextEncoded = button3Text?.let { s.addressOfAscii(it) } ?: NULL
                MessageResponse of nVROverlay_ShowMessageOverlay(textEncoded, captionEncoded, button0TextEncoded, button1TextEncoded, button2TextEncoded, button3TextEncoded)
            }

    /** If the calling process owns the overlay and it's open, this will close it. */
    fun closeMessageOverlay() = VROverlay_CloseMessageOverlay()

    override val version: String
        get() = "IVROverlay_019"
}