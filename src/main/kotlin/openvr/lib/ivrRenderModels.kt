package openvr.lib

import kool.adr
import kool.set
import kool.stak
import org.lwjgl.PointerBuffer
import org.lwjgl.openvr.*
import org.lwjgl.openvr.VRRenderModels.*
import org.lwjgl.system.MemoryUtil.*
import java.nio.IntBuffer


object vrRenderModels : vrInterface {

    /** Canonical coordinate system of the gdc 2015 wired controller, provided for backwards compatibility */
    const val Controller_Component_GDC2015 = "gdc2015"
    /** For controllers with an unambiguous 'base'. */
    const val Controller_Component_Base = "base"
    /** For controllers with an unambiguous 'tip' (used for 'laser-pointing') */
    const val Controller_Component_Tip = "tip"
    /** Neutral, ambidextrous hand-pose when holding controller. On plane between neutrally posed index finger and thumb */
    const val Controller_Component_HandGrip = "handgrip"
    /** 1:1 aspect ratio status area, with canonical [0,1] uv mapping */
    const val Controller_Component_Status = "status"

    /** Errors that can occur with the VR compositor */
    enum class Error(@JvmField val i: Int) {

        None(0),
        Loading(100),
        NotSupported(200),
        InvalidArg(300),
        InvalidModel(301),
        NoShapes(302),
        MultipleShapes(303),
        TooManyVertices(304),
        MultipleTextures(305),
        BufferTooSmall(306),
        NotEnoughNormals(307),
        NotEnoughTexCoords(308),

        InvalidTexture(400);

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }

        /**
         * VRRenderModels_GetRenderModelErrorNameFromEnum
         *
         * Returns a string for an render model error.
         */
        override fun toString(): String = stak { memASCII(nVRRenderModels_GetRenderModelErrorNameFromEnum(i)) }
    }

    val pError = memCallocInt(1)

    var error: Error
        get() = Error of pError[0]
        set(value) {
            pError[0] = value.i
        }

    /**
     * JVM custom
     *
     * Loads and returns a render model for use in the application. {@code renderModelName} should be a render model name from the
     * {@link VR#ETrackedDeviceProperty_Prop_RenderModelName_String} property or an absolute path name to a render model on disk.
     *
     * <p>The resulting render model is valid until {@link VR#VR_ShutdownInternal ShutdownInternal} is called or until {@link #VRRenderModels_FreeRenderModel FreeRenderModel} is called. When the application is finished with
     * the render model it should call {@link #VRRenderModels_FreeRenderModel FreeRenderModel} to free the memory associated with the model.</p>
     *
     * Note: Multi-thread unsafe if not passing a pError and reading it from the class property.
     */
    @JvmOverloads
    fun loadRenderModel(renderModelName: String, pErr: VRVRRenderModelsErrorBuffer = pError): RenderModel? =
            stak {

                val pModel = it.mallocPointer(1)
                var err: Int
                while (true) {
                    err = nVRRenderModels_LoadRenderModel_Async(it.addressOfAscii(renderModelName), pModel.adr)
                    if (err != vrRenderModels.Error.Loading.i)
                        break

                    Thread.sleep(1)
                }

                if (err != vrRenderModels.Error.None.i) {
                    pErr[0] = err
                    null // move on to the next tracked device
                } else
                    RenderModel.create(pModel[0])
            }

    /**
     * Loads and returns a render model for use in the application. {@code renderModelName} should be a render model name from the
     * {@link VR#ETrackedDeviceProperty_Prop_RenderModelName_String} property or an absolute path name to a render model on disk.
     *
     * <p>The resulting render model is valid until {@link VR#VR_ShutdownInternal ShutdownInternal} is called or until {@link #VRRenderModels_FreeRenderModel FreeRenderModel} is called. When the application is finished with
     * the render model it should call {@link #VRRenderModels_FreeRenderModel FreeRenderModel} to free the memory associated with the model.</p>
     */
    fun loadRenderModel_Async(renderModelName: String, renderModel: PointerBuffer): Error =
            stak { Error of nVRRenderModels_LoadRenderModel_Async(it.addressOfAscii(renderModelName), renderModel.adr) }

    /** Frees a previously returned render model It is safe to call this on a null ptr. */
    fun RenderModel.freeNative() = nVRRenderModels_FreeRenderModel(adr)

    /** JVM custom
     *
     *  Loads and returns a texture for use in the application.
     *
     *  Note: Multi-thread unsafe if not passing a pError and reading it from the class property. */
    @JvmOverloads
    fun loadTexture(textureId: TextureId, pErr: VRVRRenderModelsErrorBuffer = pError): RenderModelTextureMap? =

            stak {

                val pTexture = it.mallocPointer(1)
                var err: Int
                while (true) {
                    err = nVRRenderModels_LoadTexture_Async(textureId, pTexture.adr)
                    if (err != vrRenderModels.Error.Loading.i)
                        break

                    Thread.sleep(1)
                }

                if (err != vrRenderModels.Error.None.i) {
                    pErr[0] = err
                    null // move on to the next tracked device
                } else
                    RenderModelTextureMap.create(pTexture[0])
            }

    /** Loads and returns a texture for use in the application. */
    fun loadTexture_Async(textureId: TextureId, texture: PointerBuffer): Error =
            Error of nVRRenderModels_LoadTexture_Async(textureId, texture.adr)

    /** Frees a previously returned texture. It is safe to call this on a null ptr. */
    fun RenderModelTextureMap.freeNative() = nVRRenderModels_FreeTexture(adr)

    /** Creates a D3D11 texture and loads data into it. */
    fun loadTextureD3D11_Async(textureId: TextureId, d3D11Device: Long, d3D11Texture2D: PointerBuffer): Error =
            Error of nVRRenderModels_LoadTextureD3D11_Async(textureId, d3D11Device, d3D11Texture2D.adr)

    /** Helper function to copy the bits into an existing texture. */
    fun loadIntoTextureD3D11_Async(textureId: TextureId, dstTexture: Long): Error =
            Error of VRRenderModels_LoadIntoTextureD3D11_Async(textureId, dstTexture)

    /** Use this to free textures created with LoadTextureD3D11_Async instead of calling Release on them. */
    fun freeTextureD3D11(d3D11Texture2D: Long) = VRRenderModels_FreeTextureD3D11(d3D11Texture2D)

    /**
     * Use this to get the names of available render models. Index does not correlate to a tracked device index, but is only used for iterating over all
     * available render models. If the index is out of range, this function will return 0. Otherwise, it will return the size of the buffer required for the
     * name.
     */
    infix fun getRenderModelName(renderModelIndex: Int): String =
            stak {
                val renderModelNameLen = VRRenderModels.nVRRenderModels_GetRenderModelName(renderModelIndex, NULL, 0)
                val renderModelName = it.malloc(renderModelNameLen)
                val result = nVRRenderModels_GetRenderModelName(renderModelIndex, renderModelName.adr, renderModelNameLen)
                memASCII(renderModelName, result - 1)
            }

    /** Returns the number of available render models. */
    val renderModelCount: Int
        get() = VRRenderModels_GetRenderModelCount()

    /**
     * Returns the number of components of the specified render model.
     *
     * <p>Components are useful when client application wish to draw, label, or otherwise interact with components of tracked objects.</p>
     */
    fun getComponentCount(renderModelName: String): Int =
            stak { nVRRenderModels_GetComponentCount(it.addressOfAscii(renderModelName)) }

    /**
     * Use this to get the names of available components. Index does not correlate to a tracked device index, but is only used for iterating over all
     * available components. If the index is out of range, this function will return 0. Otherwise, it will return the size of the buffer required for the
     * name.
     */
    fun getComponentName(renderModelName: String, componentIndex: Int): String =
            stak {
                val renderModelNameEncoded = it.addressOfAscii(renderModelName)
                val componentNameLen = nVRRenderModels_GetComponentName(renderModelNameEncoded, componentIndex, NULL, 0)
                val componentName = it.malloc(componentNameLen)
                val result = VRRenderModels.nVRRenderModels_GetComponentName(renderModelNameEncoded, componentIndex, componentName.adr, componentNameLen)
                memASCII(componentName, result - 1)
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
    fun getComponentButtonMask(renderModelName: String, componentName: String): Long =
            stak { nVRRenderModels_GetComponentButtonMask(it.addressOfAscii(renderModelName), it.addressOfAscii(componentName)) }

    /**
     * Use this to get the render model name for the specified rendermode/component combination, to be passed to {@link #VRRenderModels_LoadRenderModel_Async LoadRenderModel_Async}. If the component
     * name is out of range, this function will return 0. Otherwise, it will return the size of the buffer required for the name.
     */
    fun getComponentRenderModelName(renderModelName: String, componentName: String): String =
            stak {
                val renderModelNameEncoded = it.addressOfAscii(renderModelName)
                val componentNameEncoded = it.addressOfAscii(componentName)
                val componentRenderModelNameLen = nVRRenderModels_GetComponentRenderModelName(renderModelNameEncoded, componentNameEncoded, NULL, 0)
                val componentRenderModelName = it.malloc(componentRenderModelNameLen)
                val result = nVRRenderModels_GetComponentRenderModelName(renderModelNameEncoded, componentNameEncoded, componentRenderModelName.adr, componentRenderModelNameLen)
                memASCII(componentRenderModelName, result - 1)
            }

    /** Use this to query information about the component, as a function of the controller state.
     *
     * For dynamic controller components (ex: trigger) values will reflect component motions
     * For static components this will return a consistent value independent of the VRControllerState_t
     *
     * If the pchRenderModelName or pchComponentName is invalid, this will return false (and transforms will be set to identity).
     * Otherwise, return true
     * Note: For dynamic objects, visibility may be dynamic. (I.e., true/false will be returned based on controller state and controller mode state ) */
    fun getComponentStateForDevicePath(renderModelName: CharSequence, componentName: CharSequence, devicePath: VRInputValueHandle, state: RenderModelControllerModeState, componentState: RenderModelComponentState): Boolean =
            stak { nVRRenderModels_GetComponentStateForDevicePath(it.addressOfAscii(renderModelName), it.addressOfAscii(componentName), devicePath, state.adr, componentState.adr) }

    /** This version of GetComponentState takes a controller state block instead of an action origin.     */
    @Deprecated("You should use the new input system and GetComponentStateForDevicePath instead.", ReplaceWith("getComponentStateForDevicePath"))
    fun getComponentState(renderModelName: String, componentName: String, controllerState: VRControllerState,
                          state: RenderModelControllerModeState, componentState: RenderModelComponentState): Boolean =
            stak { nVRRenderModels_GetComponentState(it.addressOfAscii(renderModelName), it.addressOfAscii(componentName), controllerState.adr, state.adr, componentState.adr) }

    /** Returns true if the render model has a component with the specified name. */
    fun renderModelHasComponent(renderModelName: String, componentName: String): Boolean =
            stak { nVRRenderModels_RenderModelHasComponent(it.addressOfAscii(renderModelName), it.addressOfAscii(componentName)) }

    /** Returns the URL of the thumbnail image for this rendermodel. */
    fun getRenderModelThumbnailURL(renderModelName: String, error: IntBuffer): String =
            stak {
                val renderModelNameEncoded = it.addressOfAscii(renderModelName)
                val thumbnailURLLen = nVRRenderModels_GetRenderModelThumbnailURL(renderModelNameEncoded, NULL, 0, error.adr)
                val thumbnailURL = it.malloc(thumbnailURLLen)
                val result = nVRRenderModels_GetRenderModelThumbnailURL(renderModelNameEncoded, thumbnailURL.adr, thumbnailURLLen, error.adr)
                memASCII(thumbnailURL, result - 1)
            }

    /**
     * Provides a render model path that will load the unskinned model if the model name provided has been replace by the user. If the model hasn't been
     * replaced the path value will still be a valid path to load the model. Pass this to LoadRenderModel_Async, etc. to load the model.
     * @param error ~ Error *
     */
    fun getRenderModelOriginalPath(renderModelName: String, error: IntBuffer): String =
            stak {
                val renderModelNameEncoded = it.addressOfAscii(renderModelName)
                val originalPathLen = nVRRenderModels_GetRenderModelOriginalPath(renderModelNameEncoded, NULL, 0, error.adr)
                val originalPath = it.malloc(originalPathLen)
                val result = nVRRenderModels_GetRenderModelOriginalPath(renderModelNameEncoded, originalPath.adr, originalPathLen, error.adr)
                memASCII(originalPath, result - 1)
            }

    override val version: String
        get() = "IVRRenderModels_006"
}