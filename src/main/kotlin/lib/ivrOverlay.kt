package lib

import ab.appBuffer
import glm_.buffer.adr
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec2.Vec2i
import glm_.vec3.Vec3
import openvr.lib.EColorSpace
import openvr.lib.ETrackingUniverseOrigin
import org.lwjgl.PointerBuffer
import org.lwjgl.openvr.*
import org.lwjgl.openvr.OpenVR.IVROverlay
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.NULL
import vkk.adr
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.LongBuffer


// ---------------------------------------------
// Overlay management methods
// ---------------------------------------------


/**
 * Finds an existing overlay with the specified key.
 *
 * @param overlayKey
 * @param overlayHandle ~VROverlayHandle
 */
fun IVROverlay.findOverlay(overlayKey: String, overlayHandle: LongBuffer): EVROverlayError {
    val overlayKeyEncoded = appBuffer.bufferOfAscii(overlayKey)
    return EVROverlayError of VROverlay.nVROverlay_FindOverlay(overlayKeyEncoded.adr, overlayHandle.adr)
}

/**
 * Creates a new named overlay. All overlays start hidden and with default settings.
 *
 * @param overlayKey
 * @param overlayName
 * @param overlayHandle ~VROverlayHandle
 */
fun IVROverlay.createOverlay(overlayKey: String, overlayName: String, overlayHandle: LongBuffer): EVROverlayError {
    val overlayKeyEncoded = appBuffer.bufferOfAscii(overlayKey)
    val overlayNameEncoded = appBuffer.bufferOfAscii(overlayName)
    return EVROverlayError of VROverlay.nVROverlay_CreateOverlay(overlayKeyEncoded.adr, overlayNameEncoded.adr, overlayHandle.adr)
}

/**
 * Destroys the specified overlay. When an application calls {@link VR#VR_ShutdownInternal ShutdownInternal} all overlays created by that app are automatically destroyed.
 *
 * @param overlayHandle
 */
infix fun IVROverlay.destroyOverlay(overlayHandle: VROverlayHandle): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_DestroyOverlay(overlayHandle)
}

/**
 * Specify which overlay to use the high quality render path.
 *
 * <p>This overlay will be composited in during the distortion pass which results in it drawing on top of everything else, but also at a higher quality as it
 * samples the source texture directly rather than rasterizing into each eye's render texture first. Because if this, only one of these is supported at
 * any given time. It is most useful for overlays that are expected to take up most of the user's view (e.g. streaming video). This mode does not support
 * mouse input to your overlay.</p>
 *
 * @param overlayHandle
 */
infix fun IVROverlay.setHighQualityOverlay(overlayHandle: VROverlayHandle): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_SetHighQualityOverlay(overlayHandle)
}

/**
 * Returns the overlay handle of the current overlay being rendered using the single high quality overlay render path. Otherwise it will return
 * {@link VR#k_ulOverlayHandleInvalid}.
 */
val IVROverlay.highQualityOverlay: VROverlayHandle
    get() = VROverlay.VROverlay_GetHighQualityOverlay()

/**
 * Fills the provided buffer with the string key of the overlay. Returns the size of buffer required to store the key, including the terminating null
 * character. {@link VR#k_unVROverlayMaxKeyLength} will be enough bytes to fit the string.
 *
 * @param overlayHandle
 * @param error ~EVROverlayError
 */
fun IVROverlay.getOverlayKey(overlayHandle: VROverlayHandle, error: IntBuffer? = null): String {
    val value = appBuffer.buffer(vr.vrOverlayMaxKeyLength)
    val err = error ?: appBuffer.intBuffer
    val size = VROverlay.nVROverlay_GetOverlayKey(overlayHandle, value.adr, vr.vrOverlayMaxKeyLength, err.adr)
    return MemoryUtil.memASCII(value, size - 1)
}

/**
 * Fills the provided buffer with the friendly name of the overlay. Returns the size of buffer required to store the key, including the terminating null
 * character. {@link VR#k_unVROverlayMaxNameLength} will be enough bytes to fit the string.
 *
 * @param overlayHandle
 * @param error ~EVROverlayError
 */
fun IVROverlay.getOverlayName(overlayHandle: VROverlayHandle, error: IntBuffer? = null): String {
    val value = appBuffer.buffer(vr.vrOverlayMaxNameLength)
    val err = error ?: appBuffer.intBuffer
    val size = VROverlay.nVROverlay_GetOverlayName(overlayHandle, value.adr, vr.vrOverlayMaxKeyLength, err.adr)
    return MemoryUtil.memASCII(value, size - 1)
}

/**
 * Sets the name to use for this overlay.
 *
 * @param overlayHandle
 * @param name
 */
fun IVROverlay.setOverlayName(overlayHandle: VROverlayHandle, name: String): EVROverlayError {
    val nameEncoded = appBuffer.bufferOfAscii(name)
    return EVROverlayError of VROverlay.nVROverlay_SetOverlayName(overlayHandle, nameEncoded.adr)
}

/**
 * Gets the raw image data from an overlay. Overlay image data is always returned as RGBA data, 4 bytes per pixel. If the buffer is not large enough,
 * width and height will be set and {@link VR#EVROverlayError_VROverlayError_ArrayTooSmall} is returned.
 *
 * @param overlayHandle
 * @param buffer
 * @param punWidth
 * @param punHeight
 */
fun IVROverlay.getOverlayImageData(overlayHandle: VROverlayHandle, buffer: ByteBuffer, size: Vec2i): EVROverlayError {
    val pWidth = appBuffer.int
    val pHeight = appBuffer.int
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayImageData(overlayHandle, buffer.adr, buffer.rem, pWidth, pHeight).also {
        size(MemoryUtil.memGetInt(pWidth), MemoryUtil.memGetInt(pHeight))
    }
}

/**
 * Useless for JVM
 * Returns a string that corresponds with the specified overlay error. The string will be the name of the error enum value for all valid error codes.
 *
 * @param error one of:<br><table><tr><td>{@link VR#EVROverlayError_VROverlayError_None}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_UnknownOverlay}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_InvalidHandle}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_PermissionDenied}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_OverlayLimitExceeded}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_WrongVisibilityType}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_KeyTooLong}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_NameTooLong}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_KeyInUse}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_WrongTransformType}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_InvalidTrackedDevice}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_InvalidParameter}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_ThumbnailCantBeDestroyed}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_ArrayTooSmall}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_RequestFailed}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_InvalidTexture}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_UnableToLoadFile}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_KeyboardAlreadyInUse}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_NoNeighbor}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_TooManyMaskPrimitives}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_BadMaskPrimitive}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_TextureAlreadyLocked}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_TextureLockCapacityReached}</td></tr><tr><td>{@link VR#EVROverlayError_VROverlayError_TextureNotLocked}</td></tr></table>
 */
//@Nullable
//@NativeType("char const *")
//public static String VROverlay_GetOverlayErrorNameFromEnum(@NativeType("EVROverlayError") int error) {
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
fun IVROverlay.setOverlayRenderingPid(overlayHandle: VROverlayHandle, pid: Int): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_SetOverlayRenderingPid(overlayHandle, pid)
}

/**
 * Gets the pid that is allowed to render to this overlay.
 *
 * @param overlayHandle
 */
infix fun IVROverlay.getOverlayRenderingPid(overlayHandle: VROverlayHandle): Int {
    return VROverlay.VROverlay_GetOverlayRenderingPid(overlayHandle)
}

/**
 * Specify flag setting for a given overlay.
 *
 * @param overlayHandle
 * @param overlayFlag    one of:<br><table><tr><td>{@link VR#VROverlayFlags_None}</td><td>{@link VR#VROverlayFlags_Curved}</td></tr><tr><td>{@link VR#VROverlayFlags_RGSS4X}</td><td>{@link VR#VROverlayFlags_NoDashboardTab}</td></tr><tr><td>{@link VR#VROverlayFlags_AcceptsGamepadEvents}</td><td>{@link VR#VROverlayFlags_ShowGamepadFocus}</td></tr><tr><td>{@link VR#VROverlayFlags_SendVRScrollEvents}</td><td>{@link VR#VROverlayFlags_SendVRTouchpadEvents}</td></tr><tr><td>{@link VR#VROverlayFlags_ShowTouchPadScrollWheel}</td><td>{@link VR#VROverlayFlags_TransferOwnershipToInternalProcess}</td></tr><tr><td>{@link VR#VROverlayFlags_SideBySide_Parallel}</td><td>{@link VR#VROverlayFlags_SideBySide_Crossed}</td></tr><tr><td>{@link VR#VROverlayFlags_Panorama}</td><td>{@link VR#VROverlayFlags_StereoPanorama}</td></tr><tr><td>{@link VR#VROverlayFlags_SortWithNonSceneOverlays}</td><td>{@link VR#VROverlayFlags_VisibleInDashboard}</td></tr></table>
 * @param enabled
 */
fun IVROverlay.setOverlayFlag(overlayHandle: VROverlayHandle, overlayFlag: VROverlayFlags, enabled: Boolean): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_SetOverlayFlag(overlayHandle, overlayFlag.i, enabled)
}

/**
 * Sets flag setting for a given overlay.
 *
 * @param overlayHandle
 * @param overlayFlag    one of:<br><table><tr><td>{@link VR#VROverlayFlags_None}</td><td>{@link VR#VROverlayFlags_Curved}</td></tr><tr><td>{@link VR#VROverlayFlags_RGSS4X}</td><td>{@link VR#VROverlayFlags_NoDashboardTab}</td></tr><tr><td>{@link VR#VROverlayFlags_AcceptsGamepadEvents}</td><td>{@link VR#VROverlayFlags_ShowGamepadFocus}</td></tr><tr><td>{@link VR#VROverlayFlags_SendVRScrollEvents}</td><td>{@link VR#VROverlayFlags_SendVRTouchpadEvents}</td></tr><tr><td>{@link VR#VROverlayFlags_ShowTouchPadScrollWheel}</td><td>{@link VR#VROverlayFlags_TransferOwnershipToInternalProcess}</td></tr><tr><td>{@link VR#VROverlayFlags_SideBySide_Parallel}</td><td>{@link VR#VROverlayFlags_SideBySide_Crossed}</td></tr><tr><td>{@link VR#VROverlayFlags_Panorama}</td><td>{@link VR#VROverlayFlags_StereoPanorama}</td></tr><tr><td>{@link VR#VROverlayFlags_SortWithNonSceneOverlays}</td><td>{@link VR#VROverlayFlags_VisibleInDashboard}</td></tr></table>
 * @param enabled ~bool*
 */
fun IVROverlay.getOverlayFlag(overlayHandle: VROverlayHandle, overlayFlag: VROverlayFlags, enabled: ByteBuffer): EVROverlayError {
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayFlag(overlayHandle, overlayFlag.i, enabled.adr)
}

/**
 * Sets the color tint of the overlay quad. Use 0.0 to 1.0 per channel.
 *
 * @param overlayHandle
 * @param red
 * @param green
 * @param blue
 */
fun IVROverlay.setOverlayColor(overlayHandle: VROverlayHandle, red: Float, green: Float, blue: Float): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_SetOverlayColor(overlayHandle, red, green, blue)
}

/**
 * Gets the color tint of the overlay quad.
 *
 * @param overlayHandle
 * @param pfRed
 * @param pfGreen
 * @param pfBlue
 */
fun IVROverlay.getOverlayColor(overlayHandle: VROverlayHandle, color: Vec3): EVROverlayError {
    val red = appBuffer.float
    val green = appBuffer.float
    val blue = appBuffer.float
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayColor(overlayHandle, red, green, blue).also {
        color(MemoryUtil.memGetFloat(red), MemoryUtil.memGetFloat(green), MemoryUtil.memGetFloat(blue))
    }
}

/**
 * Sets the alpha of the overlay quad. Use 1.0 for 100 percent opacity to 0.0 for 0 percent opacity.
 *
 * @param overlayHandle
 * @param alpha
 */
fun IVROverlay.setOverlayAlpha(overlayHandle: VROverlayHandle, alpha: Float): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_SetOverlayAlpha(overlayHandle, alpha)
}

/**
 * Gets the alpha of the overlay quad. By default overlays are rendering at 100 percent alpha (1.0).
 *
 * @param overlayHandle
 * @param alpha
 */
fun IVROverlay.getOverlayAlpha(overlayHandle: VROverlayHandle, alpha: FloatBuffer): EVROverlayError {
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayAlpha(overlayHandle, alpha.adr)
}

/**
 * Sets the aspect ratio of the texels in the overlay. 1.0 means the texels are square. 2.0 means the texels are twice as wide as they are tall.
 *
 * <p>Defaults to 1.0.</p>
 *
 * @param overlayHandle
 * @param texelAspect
 */
fun IVROverlay.setOverlayTexelAspect(overlayHandle: VROverlayHandle, texelAspect: Float): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_SetOverlayTexelAspect(overlayHandle, texelAspect)
}

/**
 * Gets the aspect ratio of the texels in the overlay. Defaults to 1.0.
 *
 * @param overlayHandle
 * @param texelAspect
 */
fun IVROverlay.getOverlayTexelAspect(overlayHandle: VROverlayHandle, texelAspect: FloatBuffer): EVROverlayError {
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayTexelAspect(overlayHandle, texelAspect.adr)
}

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
fun IVROverlay.setOverlaySortOrder(overlayHandle: VROverlayHandle, sortOrder: Int): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_SetOverlaySortOrder(overlayHandle, sortOrder)
}

/**
 * Gets the sort order of the overlay. See {@link #VROverlay_SetOverlaySortOrder SetOverlaySortOrder} for how this works.
 *
 * @param overlayHandle
 * @param sortOrder
 */
fun IVROverlay.getOverlaySortOrder(overlayHandle: VROverlayHandle, sortOrder: IntBuffer): EVROverlayError {
    return EVROverlayError of VROverlay.nVROverlay_GetOverlaySortOrder(overlayHandle, sortOrder.adr)
}

/**
 * Sets the width of the overlay quad in meters. By default overlays are rendered on a quad that is 1 meter across.
 *
 * @param overlayHandle
 * @param widthInMeters
 */
fun IVROverlay.setOverlayWidthInMeters(overlayHandle: VROverlayHandle, widthInMeters: Float): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_SetOverlayWidthInMeters(overlayHandle, widthInMeters)
}

/**
 * Returns the width of the overlay quad in meters. By default overlays are rendered on a quad that is 1 meter across.
 *
 * @param overlayHandle
 * @param widthInMeters
 */
fun IVROverlay.getOverlayWidthInMeters(overlayHandle: VROverlayHandle, widthInMeters: FloatBuffer): EVROverlayError {
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayWidthInMeters(overlayHandle, widthInMeters.adr)
}

/**
 * For high-quality curved overlays only, sets the distance range in meters from the overlay used to automatically curve the surface around the viewer.
 * Min is distance is when the surface will be most curved. Max is when least curved.
 *
 * @param overlayHandle
 * @param minDistanceInMeters
 * @param maxDistanceInMeters
 */
fun IVROverlay.setOverlayAutoCurveDistanceRangeInMeters(overlayHandle: VROverlayHandle, minDistanceInMeters: Float, maxDistanceInMeters: Float): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_SetOverlayAutoCurveDistanceRangeInMeters(overlayHandle, minDistanceInMeters, maxDistanceInMeters)
}

/**
 * For high-quality curved overlays only, gets the distance range in meters from the overlay used to automatically curve the surface around the viewer.
 * Min is distance is when the surface will be most curved. Max is when least curved.
 *
 * @param overlayHandle
 * @param minDistanceInMeters
 * @param maxDistanceInMeters
 */
fun IVROverlay.getOverlayAutoCurveDistanceRangeInMeters(overlayHandle: VROverlayHandle, minDistanceInMeters: FloatBuffer, maxDistanceInMeters: FloatBuffer): EVROverlayError {
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayAutoCurveDistanceRangeInMeters(overlayHandle, minDistanceInMeters.adr, maxDistanceInMeters.adr)
}

/**
 * Sets the colorspace the overlay texture's data is in. Defaults to 'auto'. If the texture needs to be resolved, you should call {@link #VROverlay_SetOverlayTexture SetOverlayTexture}
 * with the appropriate colorspace instead.
 *
 * @param overlayHandle
 * @param textureColorSpace one of:<br><table><tr><td>{@link VR#EColorSpace_ColorSpace_Auto}</td><td>{@link VR#EColorSpace_ColorSpace_Gamma}</td></tr><tr><td>{@link VR#EColorSpace_ColorSpace_Linear}</td></tr></table>
 */
fun IVROverlay.setOverlayTextureColorSpace(overlayHandle: VROverlayHandle, textureColorSpace: EColorSpace): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_SetOverlayTextureColorSpace(overlayHandle, textureColorSpace.i)
}

/**
 * Gets the overlay's current colorspace setting.
 *
 * @param overlayHandle
 * @param textureColorSpace ~ EColorSpace *
 */
fun IVROverlay.getOverlayTextureColorSpace(overlayHandle: VROverlayHandle, textureColorSpace: IntBuffer): EVROverlayError {
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayTextureColorSpace(overlayHandle, textureColorSpace.adr)
}

/**
 * Sets the part of the texture to use for the overlay. UV Min is the upper left corner and UV Max is the lower right corner.
 *
 * @param overlayHandle
 * @param overlayTextureBounds
 */
fun IVROverlay.setOverlayTextureBounds(overlayHandle: VROverlayHandle, overlayTextureBounds: VRTextureBounds): EVROverlayError {
    return EVROverlayError of VROverlay.nVROverlay_SetOverlayTextureBounds(overlayHandle, overlayTextureBounds.adr)
}

/**
 * Gets the part of the texture to use for the overlay. UV Min is the upper left corner and UV Max is the lower right corner.
 *
 * @param overlayHandle
 * @param overlayTextureBounds
 */
fun IVROverlay.getOverlayTextureBounds(overlayHandle: VROverlayHandle, overlayTextureBounds: VRTextureBounds): EVROverlayError {
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayTextureBounds(overlayHandle, overlayTextureBounds.address())
}

/**
 * Gets render model to draw behind this overlay.
 *
 * @param overlayHandle
 * @param value TODO check if bytebuffer is right
 * @param color
 * @param error ~EVROverlayError*
 */
fun IVROverlay.getOverlayRenderModel(overlayHandle: VROverlayHandle, value: ByteBuffer, color: HmdColor, error: IntBuffer): Int {
    return VROverlay.nVROverlay_GetOverlayRenderModel(overlayHandle, value.adr, value.rem, color.adr, error.adr)
}

fun IVROverlay.setOverlayRenderModel(overlayHandle: VROverlayHandle, renderModel: String, color: HmdColor): EVROverlayError {
    val renderModelEncoded = appBuffer.bufferOfAscii(renderModel)
    return EVROverlayError of VROverlay.nVROverlay_SetOverlayRenderModel(overlayHandle, renderModelEncoded.adr, color.adr)
}

/**
 * Returns the transform type of this overlay.
 *
 * @param overlayHandle
 * @param transformType ~ VROverlayTransformType *
 */
fun IVROverlay.getOverlayTransformType(overlayHandle: VROverlayHandle, transformType: IntBuffer): EVROverlayError {
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayTransformType(overlayHandle, transformType.adr)
}

/**
 * Sets the transform to absolute tracking origin.
 *
 * @param overlayHandle
 * @param trackingOrigin                      one of:<br><table><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseSeated}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseStanding}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseRawAndUncalibrated}</td></tr></table>
 * @param matTrackingOriginToOverlayTransform
 */
fun IVROverlay.setOverlayTransformAbsolute(overlayHandle: VROverlayHandle, trackingOrigin: ETrackingUniverseOrigin, matTrackingOriginToOverlayTransform: Mat4): EVROverlayError {
    val hmdMat34 = vr.HmdMatrix34(matTrackingOriginToOverlayTransform)
    return EVROverlayError of VROverlay.nVROverlay_SetOverlayTransformAbsolute(overlayHandle, trackingOrigin.i, hmdMat34.adr)
}

/**
 * Gets the transform if it is absolute. Returns an error if the transform is some other type.
 *
 * @param overlayHandle
 * @param trackingOrigin ~ETrackingUniverseOrigin *
 * @param matTrackingOriginToOverlayTransform
 */
fun IVROverlay.getOverlayTransformAbsolute(overlayHandle: VROverlayHandle, trackingOrigin: IntBuffer, matTrackingOriginToOverlayTransform: Mat4): EVROverlayError {
    val hmdMat34 = vr.HmdMatrix34()
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayTransformAbsolute(overlayHandle, trackingOrigin.adr, hmdMat34.adr).also {
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
fun IVROverlay.setOverlayTransformTrackedDeviceRelative(overlayHandle: VROverlayHandle, trackedDevice: TrackedDeviceIndex, matTrackedDeviceToOverlayTransform: Mat4): EVROverlayError {
    val hmdMat34 = vr.HmdMatrix34(matTrackedDeviceToOverlayTransform)
    return EVROverlayError of VROverlay.nVROverlay_SetOverlayTransformTrackedDeviceRelative(overlayHandle, trackedDevice, hmdMat34.adr)
}

/**
 * Gets the transform if it is relative to a tracked device. Returns an error if the transform is some other type.
 *
 * @param overlayHandle
 * @param trackedDevice ~ TrackedDeviceIndex *
 * @param matTrackedDeviceToOverlayTransform
 */
fun IVROverlay.getOverlayTransformTrackedDeviceRelative(overlayHandle: VROverlayHandle, trackedDevice: IntBuffer, matTrackedDeviceToOverlayTransform: Mat4): EVROverlayError {
    val hmdMat34 = vr.HmdMatrix34()
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayTransformTrackedDeviceRelative(overlayHandle, trackedDevice.adr, hmdMat34.adr).also {
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
fun IVROverlay.setOverlayTransformTrackedDeviceComponent(overlayHandle: VROverlayHandle, deviceIndex: TrackedDeviceIndex, componentName: String): EVROverlayError {
    val componentNameEncoded = appBuffer.bufferOfAscii(componentName)
    return EVROverlayError of VROverlay.nVROverlay_SetOverlayTransformTrackedDeviceComponent(overlayHandle, deviceIndex, componentNameEncoded.adr)
}

/**
 * Gets the transform information when the overlay is rendering on a component.
 *
 * @param overlayHandle
 * @param deviceIndex ~ TrackedDeviceIndex *
 * @param componentName ~ char *
 */
fun IVROverlay.getOverlayTransformTrackedDeviceComponent(overlayHandle: VROverlayHandle, deviceIndex: IntBuffer, componentName: ByteBuffer): EVROverlayError {
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayTransformTrackedDeviceComponent(overlayHandle, deviceIndex.adr, componentName.adr, componentName.rem)
}

fun IVROverlay.getOverlayTransformOverlayRelative(overlayHandle: VROverlayHandle, overlayHandleParent: LongBuffer, matParentOverlayToOverlayTransform: Mat4): EVROverlayError {
    val hmdMat34 = vr.HmdMatrix34()
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayTransformOverlayRelative(overlayHandle, overlayHandleParent.adr, hmdMat34.adr).also {
        hmdMat34 to matParentOverlayToOverlayTransform
    }
}

fun IVROverlay.setOverlayTransformOverlayRelative(overlayHandle: VROverlayHandle, overlayHandleParent: VROverlayHandle, matParentOverlayToOverlayTransform: Mat4): EVROverlayError {
    val hmdMat34 = vr.HmdMatrix34(matParentOverlayToOverlayTransform)
    return EVROverlayError of VROverlay.nVROverlay_SetOverlayTransformOverlayRelative(overlayHandle, overlayHandleParent, hmdMat34.adr)
}

/**
 * Shows the VR overlay. For dashboard overlays, only the Dashboard Manager is allowed to call this.
 *
 * @param overlayHandle
 */
infix fun IVROverlay.showOverlay(overlayHandle: VROverlayHandle): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_ShowOverlay(overlayHandle)
}

/**
 * Hides the VR overlay. For dashboard overlays, only the Dashboard Manager is allowed to call this.
 *
 * @param overlayHandle
 */
infix fun IVROverlay.hideOverlay(overlayHandle: VROverlayHandle): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_HideOverlay(overlayHandle)
}

/**
 * Returns true if the overlay is visible.
 *
 * @param overlayHandle
 */
infix fun IVROverlay.isOverlayVisible(overlayHandle: VROverlayHandle): Boolean {
    return VROverlay.VROverlay_IsOverlayVisible(overlayHandle)
}

/**
 * Get the transform in 3d space associated with a specific 2d point in the overlay's coordinate space (where 0,0 is the lower left). -Z points out of the
 * overlay.
 *
 * @param overlayHandle
 * @param trackingOrigin      one of:<br><table><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseSeated}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseStanding}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseRawAndUncalibrated}</td></tr></table>
 * @param coordinatesInOverlay
 * @param matTransform
 */
fun IVROverlay.getTransformForOverlayCoordinates(overlayHandle: VROverlayHandle, trackingOrigin: ETrackingUniverseOrigin, coordinatesInOverlay: Vec2, matTransform: Mat4): EVROverlayError {
    val hmdVector2 = vr.HmdVector2(coordinatesInOverlay)
    val hmdMat34 = vr.HmdMatrix34()
    return EVROverlayError of VROverlay.nVROverlay_GetTransformForOverlayCoordinates(overlayHandle, trackingOrigin.i, hmdVector2.adr, hmdMat34.adr).also {
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
fun IVROverlay.pollNextOverlayEvent(overlayHandle: VROverlayHandle, event: VREvent): Boolean {
    return VROverlay.nVROverlay_PollNextOverlayEvent(overlayHandle, event.adr, VREvent.SIZEOF)
}

/**
 * Returns the current input settings for the specified overlay.
 *
 * @param overlayHandle
 * @param inputMethod ~VROverlayInputMethod*
 */
fun IVROverlay.getOverlayInputMethod(overlayHandle: VROverlayHandle, inputMethod: IntBuffer): EVROverlayError {
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayInputMethod(overlayHandle, inputMethod.adr)
}

/**
 * Sets the input settings for the specified overlay.
 *
 * @param overlayHandle
 * @param inputMethod    one of:<br><table><tr><td>{@link VR#VROverlayInputMethod_None}</td><td>{@link VR#VROverlayInputMethod_Mouse}</td></tr><tr><td>{@link VR#VROverlayInputMethod_DualAnalog}</td></tr></table>
 */
fun IVROverlay.setOverlayInputMethod(overlayHandle: VROverlayHandle, inputMethod: VROverlayInputMethod): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_SetOverlayInputMethod(overlayHandle, inputMethod.i)
}

/**
 * Gets the mouse scaling factor that is used for mouse events. The actual texture may be a different size, but this is typically the size of the
 * underlying UI in pixels.
 *
 * @param overlayHandle
 * @param vecMouseScale
 */
fun IVROverlay.getOverlayMouseScale(overlayHandle: VROverlayHandle, vecMouseScale: Vec2): EVROverlayError {
    val hmdVec2 = vr.HmdVector2()
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayMouseScale(overlayHandle, hmdVec2.adr).also {
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
fun IVROverlay.setOverlayMouseScale(overlayHandle: VROverlayHandle, vecMouseScale: Vec2): EVROverlayError {
    val hmdVec2 = vr.HmdVector2()
    return EVROverlayError of VROverlay.nVROverlay_SetOverlayMouseScale(overlayHandle, hmdVec2.adr)
}

/**
 * Computes the overlay-space pixel coordinates of where the ray intersects the overlay with the specified settings. Returns false if there is no
 * intersection.
 *
 * @param overlayHandle
 * @param params
 * @param results
 */
fun IVROverlay.computeOverlayIntersection(overlayHandle: VROverlayHandle, params: VROverlayIntersectionParams, results: VROverlayIntersectionResults): Boolean {
    return VROverlay.nVROverlay_ComputeOverlayIntersection(overlayHandle, params.adr, results.adr)
}

/**
 * Returns true if the specified overlay is the hover target. An overlay is the hover target when it is the last overlay "moused over" by the virtual
 * mouse pointer.
 *
 * @param overlayHandle
 */
infix fun IVROverlay.isHoverTargetOverlay(overlayHandle: VROverlayHandle): Boolean {
    return VROverlay.VROverlay_IsHoverTargetOverlay(overlayHandle)
}

/** Returns the current Gamepad focus overlay. */
val IVROverlay.gamepadFocusOverlay: VROverlayHandle
    get() = VROverlay.VROverlay_GetGamepadFocusOverlay()

/**
 * Sets the current Gamepad focus overlay.
 *
 * @param newFocusOverlay
 */
infix fun IVROverlay.setGamepadFocusOverlay(newFocusOverlay: VROverlayHandle): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_SetGamepadFocusOverlay(newFocusOverlay)
}

/**
 * Sets an overlay's neighbor. This will also set the neighbor of the "to" overlay to point back to the "from" overlay. If an overlay's neighbor is set to
 * invalid both ends will be cleared.
 * "
 *
 * @param direction one of:<br><table><tr><td>{@link VR#EOverlayDirection_OverlayDirection_Up}</td><td>{@link VR#EOverlayDirection_OverlayDirection_Down}</td></tr><tr><td>{@link VR#EOverlayDirection_OverlayDirection_Left}</td><td>{@link VR#EOverlayDirection_OverlayDirection_Right}</td></tr><tr><td>{@link VR#EOverlayDirection_OverlayDirection_Count}</td></tr></table>
 * @param from
 * @param to
 */
fun IVROverlay.setOverlayNeighbor(direction: EOverlayDirection, from: VROverlayHandle, to: VROverlayHandle): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_SetOverlayNeighbor(direction.i, from, to)
}

/**
 * Changes the Gamepad focus from one overlay to one of its neighbors.
 *
 * @param direction one of:<br><table><tr><td>{@link VR#EOverlayDirection_OverlayDirection_Up}</td><td>{@link VR#EOverlayDirection_OverlayDirection_Down}</td></tr><tr><td>{@link VR#EOverlayDirection_OverlayDirection_Left}</td><td>{@link VR#EOverlayDirection_OverlayDirection_Right}</td></tr><tr><td>{@link VR#EOverlayDirection_OverlayDirection_Count}</td></tr></table>
 * @param from
 *
 * @return {@link VR#EVROverlayError_VROverlayError_NoNeighbor} if there is no neighbor in that direction
 */
fun IVROverlay.moveGamepadFocusToNeighbor(direction: EOverlayDirection, from: VROverlayHandle): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_MoveGamepadFocusToNeighbor(direction.i, from)
}

/**
 * Sets the analog input to Dual Analog coordinate scale for the specified overlay.
 *
 * @param overlay
 * @param which    one of:<br><table><tr><td>{@link VR#EDualAnalogWhich_k_EDualAnalog_Left}</td><td>{@link VR#EDualAnalogWhich_k_EDualAnalog_Right}</td></tr></table>
 * @param center
 * @param radius
 */
fun IVROverlay.setOverlayDualAnalogTransform(overlay: VROverlayHandle, which: EDualAnalogWhich, center: Vec2, radius: Float): EVROverlayError {
    val hmdVec2 = vr.HmdVector2(center)
    return EVROverlayError of VROverlay.nVROverlay_SetOverlayDualAnalogTransform(overlay, which.i, hmdVec2.adr, radius)
}

/**
 * Gets the analog input to Dual Analog coordinate scale for the specified overlay.
 *
 * @param overlay
 * @param which    one of:<br><table><tr><td>{@link VR#EDualAnalogWhich_k_EDualAnalog_Left}</td><td>{@link VR#EDualAnalogWhich_k_EDualAnalog_Right}</td></tr></table>
 * @param center
 * @param radius
 */
fun IVROverlay.getOverlayDualAnalogTransform(overlay: VROverlayHandle, which: EDualAnalogWhich, center: Vec2, radius: FloatBuffer): EVROverlayError {
    val hmdVec2 = vr.HmdVector2()
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayDualAnalogTransform(overlay, which.i, hmdVec2.adr, radius.adr).also {
        hmdVec2 to center
    }
}

/**
 * Texture to draw for the overlay. This function can only be called by the overlay's creator or renderer process (see {@link #VROverlay_SetOverlayRenderingPid SetOverlayRenderingPid}).
 *
 * @param overlayHandle
 * @param texture
 */
fun IVROverlay.setOverlayTexture(overlayHandle: VROverlayHandle, texture: Texture): EVROverlayError {
    return EVROverlayError of VROverlay.nVROverlay_SetOverlayTexture(overlayHandle, texture.adr)
}

/**
 * Use this to tell the overlay system to release the texture set for this overlay.
 *
 * @param overlayHandle
 */
fun IVROverlay.clearOverlayTexture(overlayHandle: VROverlayHandle): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_ClearOverlayTexture(overlayHandle)
}

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
fun IVROverlay.setOverlayRaw(overlayHandle: VROverlayHandle, buffer: ByteBuffer, width: Int, height: Int, depth: Int): EVROverlayError {
    return EVROverlayError of VROverlay.nVROverlay_SetOverlayRaw(overlayHandle, buffer.adr, width, height, depth)
}

/**
 * Separate interface for providing the image through a filename: can be png or jpg, and should not be bigger than 1920x1080. This function can only be
 * called by the overlay's renderer process
 *
 * @param overlayHandle
 * @param filePath
 */
fun IVROverlay.setOverlayFromFile(overlayHandle: VROverlayHandle, filePath: String): EVROverlayError {
    val filePathEncoded = appBuffer.bufferOfAscii(filePath)
    return EVROverlayError of VROverlay.nVROverlay_SetOverlayFromFile(overlayHandle, filePathEncoded.adr)
}

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
 * @param apiType  ~ETextureType *
 * @param colorSpace    ~EColorSpace *
 * @param textureBounds
 */
fun IVROverlay.getOverlayTexture(overlayHandle: VROverlayHandle, nativeTextureHandle: PointerBuffer, nativeTextureRef: Long, width: IntBuffer, height: IntBuffer, nativeFormat: IntBuffer, apiType: IntBuffer, colorSpace: IntBuffer, textureBounds: VRTextureBounds): EVROverlayError {
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayTexture(overlayHandle, nativeTextureHandle.adr, nativeTextureRef, width.adr, height.adr, nativeFormat.adr, apiType.adr, colorSpace.adr, textureBounds.adr)
}

/**
 * Release the {@code nativeTextureHandle} provided from the {@link #VROverlay_GetOverlayTexture GetOverlayTexture} call, this allows the system to free the underlying GPU resources for
 * this object, so only do it once you stop rendering this texture.
 *
 * @param overlayHandle
 * @param nativeTextureHandle
 */
fun IVROverlay.releaseNativeOverlayHandle(overlayHandle: VROverlayHandle, nativeTextureHandle: Long): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_ReleaseNativeOverlayHandle(overlayHandle, nativeTextureHandle)
}

/**
 * Get the size of the overlay texture.
 *
 * @param overlayHandle
 * @param width
 * @param height
 */
fun IVROverlay.getOverlayTextureSize(overlayHandle: VROverlayHandle, width: IntBuffer, height: IntBuffer): EVROverlayError {
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayTextureSize(overlayHandle, width.adr, height.adr)
}

/**
 * Creates a dashboard overlay and returns its handle.
 *
 * @param overlayKey
 * @param overlayFriendlyName
 * @param mainHandle   ~ VROverlayHandle *
 * @param thumbnailHandle  ~ VROverlayHandle *
 */
fun IVROverlay.createDashboardOverlay(overlayKey: String, overlayFriendlyName: String, mainHandle: LongBuffer, thumbnailHandle: LongBuffer): EVROverlayError {
    val overlayKeyEncoded = appBuffer.bufferOfAscii(overlayKey)
    val overlayFriendlyNameEncoded = appBuffer.bufferOfAscii(overlayFriendlyName)
    return EVROverlayError of VROverlay.nVROverlay_CreateDashboardOverlay(overlayKeyEncoded.adr, overlayFriendlyNameEncoded.adr, mainHandle.adr, thumbnailHandle.adr)
}

/** Returns true if the dashboard is visible. */
val IVROverlay.isDashboardVisible: Boolean
    get() = VROverlay.VROverlay_IsDashboardVisible()

/**
 * Returns true if the dashboard is visible and the specified overlay is the active system Overlay.
 *
 * @param overlayHandle
 */
infix fun IVROverlay.isActiveDashboardOverlay(overlayHandle: VROverlayHandle): Boolean {
    return VROverlay.VROverlay_IsActiveDashboardOverlay(overlayHandle)
}

/**
 * Sets the dashboard overlay to only appear when the specified process ID has scene focus.
 *
 * @param overlayHandle
 * @param processId
 */
fun IVROverlay.setDashboardOverlaySceneProcess(overlayHandle: VROverlayHandle, processId: Int): EVROverlayError {
    return EVROverlayError of VROverlay.VROverlay_SetDashboardOverlaySceneProcess(overlayHandle, processId)
}

/**
 * Gets the process ID that this dashboard overlay requires to have scene focus.
 *
 * @param overlayHandle
 * @param processId
 */
fun IVROverlay.getDashboardOverlaySceneProcess(overlayHandle: VROverlayHandle, processId: IntBuffer): EVROverlayError {
    return EVROverlayError of VROverlay.nVROverlay_GetDashboardOverlaySceneProcess(overlayHandle, processId.adr)
}

/**
 * Shows the dashboard.
 *
 * @param overlayToShow
 */
fun IVROverlay.showDashboard(overlayToShow: String) {
    val overlayToShowEncoded = appBuffer.bufferOfAscii(overlayToShow)
    VROverlay.nVROverlay_ShowDashboard(overlayToShowEncoded.adr)
}

/** Returns the tracked device that has the laser pointer in the dashboard. */
val IVROverlay.primaryDashboardDevice: TrackedDeviceIndex
    get() = VROverlay.VROverlay_GetPrimaryDashboardDevice()

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
fun IVROverlay.showKeyboard(inputMode: EGamepadTextInputMode, lineInputMode: EGamepadTextInputLineMode, description: String, charMax: Int, existingText: String, useMinimalMode: Boolean, userValue: Long): EVROverlayError {
    val descriptionEncoded = appBuffer.bufferOfAscii(description)
    val existingTextEncoded = appBuffer.bufferOfAscii(existingText)
    return EVROverlayError of VROverlay.nVROverlay_ShowKeyboard(inputMode.i, lineInputMode.i, descriptionEncoded.adr, charMax, existingTextEncoded.adr, useMinimalMode, userValue)
}

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
fun IVROverlay.showKeyboardForOverlay(overlayHandle: VROverlayHandle, inputMode: EGamepadTextInputMode, lineInputMode: EGamepadTextInputLineMode, description: String, charMax: Int, existingText: String, useMinimalMode: Boolean, userValue: Long): EVROverlayError {
    val pchDescriptionEncoded = appBuffer.bufferOfAscii(description)
    val pchExistingTextEncoded = appBuffer.bufferOfAscii(existingText)
    return EVROverlayError of VROverlay.nVROverlay_ShowKeyboardForOverlay(overlayHandle, inputMode.i, lineInputMode.i, pchDescriptionEncoded.adr, charMax, pchExistingTextEncoded.adr, useMinimalMode, userValue)
}

/**
 * Get the textSize that was entered into the textSize input.
 *
 * @param textSize
 */
fun IVROverlay.getKeyboardText(textSize: Int): String {
    val text = appBuffer.buffer(textSize)
    val size = VROverlay.nVROverlay_GetKeyboardText(text.adr, textSize)
    return MemoryUtil.memASCII(text, size - 1)
}

/** Hide the virtual keyboard. */
fun IVROverlay.hideKeyboard() = VROverlay. VROverlay_HideKeyboard()

/**
 * Set the position of the keyboard in world space.
 *
 * @param trackingOrigin                       one of:<br><table><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseSeated}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseStanding}</td></tr><tr><td>{@link VR#ETrackingUniverseOrigin_TrackingUniverseRawAndUncalibrated}</td></tr></table>
 * @param matTrackingOriginToKeyboardTransform
 */
fun IVROverlay.setKeyboardTransformAbsolute(trackingOrigin: ETrackingUniverseOrigin, matTrackingOriginToKeyboardTransform: Mat4) {
    val hmdMat34 = vr.HmdMatrix34(matTrackingOriginToKeyboardTransform)
    VROverlay.nVROverlay_SetKeyboardTransformAbsolute(trackingOrigin.i, hmdMat34.adr)
}

/**
 * Set the position of the keyboard in overlay space by telling it to avoid a rectangle in the overlay. Rectangle coords have (0,0) in the bottom left.
 *
 * @param overlayHandle
 * @param avoidRect
 */
fun IVROverlay.setKeyboardPositionForOverlay(overlayHandle: VROverlayHandle, avoidRect: HmdRect2) {
    VROverlay.nVROverlay_SetKeyboardPositionForOverlay(overlayHandle, avoidRect.adr)
}

/**
 * Sets a list of primitives to be used for controller ray intersection typically the size of the underlying UI in pixels(not in world space).
 *
 * @param overlayHandle
 * @param maskPrimitives
 */
fun IVROverlay.setOverlayIntersectionMask(overlayHandle: VROverlayHandle, maskPrimitives: VROverlayIntersectionMaskPrimitive.Buffer): EVROverlayError {
    return EVROverlayError of VROverlay.nVROverlay_SetOverlayIntersectionMask(overlayHandle, maskPrimitives.adr, maskPrimitives.rem, VROverlayIntersectionMaskPrimitive.SIZEOF)
}

fun IVROverlay.getOverlayFlags(overlayHandle: VROverlayHandle, flags: IntBuffer): EVROverlayError {
    return EVROverlayError of VROverlay.nVROverlay_GetOverlayFlags(overlayHandle, flags.adr)
}

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
fun IVROverlay.showMessageOverlay(text: String, caption: String, button0Text: String, button1Text: String?, button2Text: String?, button3Text: String?): VRMessageOverlayResponse {
    val textEncoded = appBuffer.bufferOfAscii(text)
    val captionEncoded = appBuffer.bufferOfAscii(caption)
    val button0TextEncoded = appBuffer.bufferOfAscii(button0Text)
    val button1TextEncoded = button1Text?.let { appBuffer.bufferOfAscii(it) }
    val button2TextEncoded = button2Text?.let { appBuffer.bufferOfAscii(it) }
    val button3TextEncoded = button3Text?.let { appBuffer.bufferOfAscii(it) }
    return VRMessageOverlayResponse of VROverlay.nVROverlay_ShowMessageOverlay(textEncoded.adr, captionEncoded.adr, button0TextEncoded.adr, button1TextEncoded?.adr ?: NULL, button2TextEncoded?.adr ?: NULL, button3TextEncoded?.adr ?: NULL)
}

/** If the calling process owns the overlay and it's open, this will close it. */
fun IVROverlay.closeMessageOverlay() = VROverlay.VROverlay_CloseMessageOverlay()