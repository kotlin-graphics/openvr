package vr_

import ab.appBuffer
import glm_.buffer.adr
import org.lwjgl.PointerBuffer
import org.lwjgl.openvr.*
import org.lwjgl.openvr.OpenVR.IVRRenderModels
import org.lwjgl.system.MemoryUtil.*
import vkk.adr
import java.nio.IntBuffer


/**
 * Loads and returns a render model for use in the application. {@code renderModelName} should be a render model name from the
 * {@link VR#ETrackedDeviceProperty_Prop_RenderModelName_String} property or an absolute path name to a render model on disk.
 *
 * <p>The resulting render model is valid until {@link VR#VR_ShutdownInternal ShutdownInternal} is called or until {@link #VRRenderModels_FreeRenderModel FreeRenderModel} is called. When the application is finished with
 * the render model it should call {@link #VRRenderModels_FreeRenderModel FreeRenderModel} to free the memory associated with the model.</p>
 */
fun IVRRenderModels.loadRenderModel_Async(renderModelName: String, renderModel: PointerBuffer): EVRRenderModelError {
    val renderModelNameEncoded = appBuffer.bufferOfAscii(renderModelName)
    return EVRRenderModelError of VRRenderModels.nVRRenderModels_LoadRenderModel_Async(renderModelNameEncoded.adr, renderModel.adr)
}

/** Frees a previously returned render model It is safe to call this on a null ptr. */
fun RenderModel?.free() {
    if (this != null)
        VRRenderModels.nVRRenderModels_FreeRenderModel(adr)
}

/** Loads and returns a texture for use in the application. */
fun IVRRenderModels.loadTexture_Async(textureId: TextureId, texture: PointerBuffer): EVRRenderModelError {
    return EVRRenderModelError of VRRenderModels.nVRRenderModels_LoadTexture_Async(textureId, texture.adr)
}

/** Frees a previously returned texture. It is safe to call this on a null ptr. */
fun RenderModelTextureMap?.free() {
    if (this != null)
        VRRenderModels.nVRRenderModels_FreeTexture(adr)
}

/** Creates a D3D11 texture and loads data into it. */
fun IVRRenderModels.loadTextureD3D11_Async(textureId: TextureId, d3D11Device: Long, d3D11Texture2D: PointerBuffer): EVRRenderModelError {
    return EVRRenderModelError of VRRenderModels.nVRRenderModels_LoadTextureD3D11_Async(textureId, d3D11Device, d3D11Texture2D.adr)
}

/** Helper function to copy the bits into an existing texture. */
fun IVRRenderModels.loadIntoTextureD3D11_Async(textureId: TextureId, dstTexture: Long): EVRRenderModelError {
    return EVRRenderModelError of VRRenderModels.VRRenderModels_LoadIntoTextureD3D11_Async(textureId, dstTexture)
}

/** Use this to free textures created with LoadTextureD3D11_Async instead of calling Release on them. */
fun IVRRenderModels.freeTextureD3D11(d3D11Texture2D: Long) = VRRenderModels.VRRenderModels_FreeTextureD3D11(d3D11Texture2D)

/**
 * Use this to get the names of available render models. Index does not correlate to a tracked device index, but is only used for iterating over all
 * available render models. If the index is out of range, this function will return 0. Otherwise, it will return the size of the buffer required for the
 * name.
 */
infix fun IVRRenderModels.getRenderModelName(renderModelIndex: Int): String {
    val renderModelNameLen = VRRenderModels.nVRRenderModels_GetRenderModelName(renderModelIndex, NULL, 0)
    val renderModelName = appBuffer.buffer(renderModelNameLen)
    val result = VRRenderModels.nVRRenderModels_GetRenderModelName(renderModelIndex, renderModelName.adr, renderModelNameLen)
    return memASCII(renderModelName, result - 1)
}

/** Returns the number of available render models. */
val IVRRenderModels.renderModelCount: Int
    get() = VRRenderModels.VRRenderModels_GetRenderModelCount()

/**
 * Returns the number of components of the specified render model.
 *
 * <p>Components are useful when client application wish to draw, label, or otherwise interact with components of tracked objects.</p>
 */
fun IVRRenderModels.getComponentCount(renderModelName: String): Int {
    val renderModelNameEncoded = appBuffer.bufferOfAscii(renderModelName)
    return VRRenderModels.nVRRenderModels_GetComponentCount(renderModelNameEncoded.adr)
}

/**
 * Use this to get the names of available components. Index does not correlate to a tracked device index, but is only used for iterating over all
 * available components. If the index is out of range, this function will return 0. Otherwise, it will return the size of the buffer required for the
 * name.
 */
fun IVRRenderModels.getComponentName(renderModelName: String, componentIndex: Int): String {
    val renderModelNameEncoded = appBuffer.bufferOfAscii(renderModelName)
    val componentNameLen = VRRenderModels.nVRRenderModels_GetComponentName(renderModelNameEncoded.adr, componentIndex, NULL, 0)
    val componentName = appBuffer.buffer(componentNameLen)
    val result = VRRenderModels.nVRRenderModels_GetComponentName(renderModelNameEncoded.adr, componentIndex, componentName.adr, componentNameLen)
    return memASCII(componentName, result - 1)
}

/**
 * Get the button mask for all buttons associated with this component.
 *
 * <p>If no buttons (or axes) are associated with this component, return 0</p>
 *
 * <div style="margin-left: 26px; border-left: 1px solid gray; padding-left: 14px;"><h5>Note</h5>
 *
 * <p>multiple components may be associated with the same button. Ex: two grip buttons on a single controller.</p></div>
 *
 * <div style="margin-left: 26px; border-left: 1px solid gray; padding-left: 14px;"><h5>Note</h5>
 *
 * <p>A single component may be associated with multiple buttons. Ex: A trackpad which also provides "D-pad" functionality</p></div>
 */
fun IVRRenderModels.getComponentButtonMask(renderModelName: String, componentName: String): Long {
    val renderModelNameEncoded = appBuffer.bufferOfAscii(renderModelName)
    val componentNameEncoded = appBuffer.bufferOfAscii(componentName)
    return VRRenderModels.nVRRenderModels_GetComponentButtonMask(renderModelNameEncoded.adr, componentNameEncoded.adr)
}

/**
 * Use this to get the render model name for the specified rendermode/component combination, to be passed to {@link #VRRenderModels_LoadRenderModel_Async LoadRenderModel_Async}. If the component
 * name is out of range, this function will return 0. Otherwise, it will return the size of the buffer required for the name.
 */
fun IVRRenderModels.getComponentRenderModelName(renderModelName: String, componentName: String): String {
    val renderModelNameEncoded = appBuffer.bufferOfAscii(renderModelName)
    val componentNameEncoded = appBuffer.bufferOfAscii(componentName)
    val componentRenderModelNameLen = VRRenderModels.nVRRenderModels_GetComponentRenderModelName(renderModelNameEncoded.adr, componentNameEncoded.adr, NULL, 0)
    val componentRenderModelName = appBuffer.buffer(componentRenderModelNameLen)
    val result = VRRenderModels.nVRRenderModels_GetComponentRenderModelName(renderModelNameEncoded.adr, componentNameEncoded.adr, componentRenderModelName.adr, componentRenderModelNameLen)
    return memASCII(componentRenderModelName, result - 1)
}

/**
 * Use this to query information about the component, as a function of the controller state.
 *
 * <p>For dynamic controller components (ex: trigger) values will reflect component motions. For static components this will return a consistent value
 * independent of the {@link VRControllerState}.</p>
 *
 * <p>If the {@code renderModelName} or {@code componentName} is invalid, this will return false (and transforms will be set to identity). Otherwise,
 * return true.</p>
 *
 * <div style="margin-left: 26px; border-left: 1px solid gray; padding-left: 14px;"><h5>Note</h5>
 *
 * <p>For dynamic objects, visibility may be dynamic. (I.e., true/false will be returned based on controller state and controller mode state )</p></div>
 */
fun IVRRenderModels.getComponentState(renderModelName: String, componentName: String, controllerState: VRControllerState, state: RenderModelControllerModeState, componentState: RenderModelComponentState): Boolean {
    val renderModelNameEncoded = appBuffer.bufferOfAscii(renderModelName)
    val componentNameEncoded = appBuffer.bufferOfAscii(componentName)
    return VRRenderModels.nVRRenderModels_GetComponentState(renderModelNameEncoded.adr, componentNameEncoded.adr, controllerState.adr, state.adr, componentState.adr)
}

/** Returns true if the render model has a component with the specified name. */
fun IVRRenderModels.renderModelHasComponent(renderModelName: String, componentName: String): Boolean {
    val renderModelNameEncoded = appBuffer.bufferOfAscii(renderModelName)
    val componentNameEncoded = appBuffer.bufferOfAscii(componentName)
    return VRRenderModels.nVRRenderModels_RenderModelHasComponent(renderModelNameEncoded.adr, componentNameEncoded.adr)
}

/** Returns the URL of the thumbnail image for this rendermodel. */
fun IVRRenderModels.getRenderModelThumbnailURL(renderModelName: String, error: IntBuffer): String {
    val renderModelNameEncoded = appBuffer.bufferOfAscii(renderModelName)
    val thumbnailURLLen = VRRenderModels.nVRRenderModels_GetRenderModelThumbnailURL(renderModelNameEncoded.adr, NULL, 0, error.adr)
    val thumbnailURL = appBuffer.buffer(thumbnailURLLen)
    val result = VRRenderModels.nVRRenderModels_GetRenderModelThumbnailURL(renderModelNameEncoded.adr, thumbnailURL.adr, thumbnailURLLen, error.adr)
    return memASCII(thumbnailURL, result - 1)
}

/**
 * Provides a render model path that will load the unskinned model if the model name provided has been replace by the user. If the model hasn't been
 * replaced the path value will still be a valid path to load the model. Pass this to LoadRenderModel_Async, etc. to load the model.
 * @param error ~ EVRRenderModelError *
 */
fun IVRRenderModels.getRenderModelOriginalPath(renderModelName: String, error: IntBuffer): String {
    val renderModelNameEncoded = appBuffer.bufferOfAscii(renderModelName)
    val originalPathLen = VRRenderModels.nVRRenderModels_GetRenderModelOriginalPath(renderModelNameEncoded.adr, NULL, 0, error.adr)
    val originalPath = appBuffer.buffer(originalPathLen)
    val result = VRRenderModels.nVRRenderModels_GetRenderModelOriginalPath(renderModelNameEncoded.adr, originalPath.adr, originalPathLen, error.adr)
    return memASCII(originalPath, result - 1)
}

/** Returns a string for a render model error. */
fun IVRRenderModels.getRenderModelErrorNameFromEnum(error: EVRRenderModelError): String? {
    val result = VRRenderModels.nVRRenderModels_GetRenderModelErrorNameFromEnum(error.i)
    return memASCIISafe(result)
}