package openvr.lib

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.IntByReference
import com.sun.jna.ptr.PointerByReference
import com.sun.jna.ptr.ShortByReference
import glm_.BYTES
import glm_.b
import glm_.i
import glm_.s
import java.util.*

// ivrrendermodels.h ==============================================================================================================================================

val controller_Component_GDC2015 = "gdc2015"   // Canonical coordinate system of the gdc 2015 wired controller, provided for backwards compatibility
val controller_Component_Base = "base"         // For controllers with an unambiguous 'base'.
val controller_Component_Tip = "tip"           // For controllers with an unambiguous 'tip' (used for 'laser-pointing')
val controller_Component_HandGrip = "handgrip" // Neutral, ambidextrous hand-pose when holding controller. On plane between neutrally posed index finger and thumb
val controller_Component_Status = "status"     // 1:1 aspect ratio status area, with canonical [0,1] uv mapping

/** Errors that can occur with the VR compositor */
enum class EVRRenderModelError(@JvmField val i: Int) {

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
        fun of(i: Int) = values().first { it.i == i }
    }

    /** Note: can't use `val name` because you can't overwrite the Enum::name   */
    fun getName() = vrRenderModels!!.getRenderModelErrorNameFromEnum(this)
}

class EVRRenderModelError_ByReference(val value: EVRRenderModelError = EVRRenderModelError.None) : IntByReference(value.i)

typealias VRComponentProperties = Int

enum class EVRComponentProperty(@JvmField val i: Int) {

    IsStatic(1 shl 0),
    IsVisible(1 shl 1),
    IsTouched(1 shl 2),
    IsPressed(1 shl 3),
    IsScrolled(1 shl 4);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
    }
}

/** Describes state information about a render-model component, including transforms and other dynamic properties */
open class RenderModel_ComponentState : Structure {

    /** Transform required when drawing the component render model  */
    @JvmField
    var trackingToComponentRenderModel = HmdMat34()
    /** Transform available for attaching to a local component coordinate system (-Z out from surface ) */
    @JvmField
    var trackingToComponentLocal = HmdMat34()
    @JvmField
    var properties: VRComponentProperties = 0

    constructor()

    constructor(trackingToComponentRenderModel: HmdMat34, trackingToComponentLocal: HmdMat34, properties: Int) {
        this.trackingToComponentRenderModel = trackingToComponentRenderModel
        this.trackingToComponentLocal = trackingToComponentLocal
        this.properties = properties
    }

    override fun getFieldOrder()= listOf("trackingToComponentRenderModel", "trackingToComponentLocal", "properties")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : RenderModel_ComponentState(), Structure.ByReference
    class ByValue : RenderModel_ComponentState(), Structure.ByValue
}

/** A single vertex in a render model */
open class RenderModel_Vertex : Structure {

    /** position in meters in device space  */
    @JvmField
    var position = HmdVec3()
    @JvmField
    var normal = HmdVec3()
    @JvmField
    var textureCoord = floatArrayOf(0f, 0f)

    operator fun get(i: Int) = when (i) {
        in 0 until 3 -> position[i]
        in 3 until 6 -> normal[i]
        in 6 until 8 -> textureCoord[i]
        else -> throw IndexOutOfBoundsException()
    }

    constructor()

    constructor(position: HmdVec3, normal: HmdVec3, textureCoord: FloatArray) {
        this.position = position
        this.normal = normal
        // TODO check all size checks, necessary/dangerous?
        if (textureCoord.size != this.textureCoord.size) throw IllegalArgumentException("Wrong array size!")
        this.textureCoord = textureCoord
    }

    override fun getFieldOrder()= listOf("position", "normal", "textureCoord")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : RenderModel_Vertex(), Structure.ByReference
    class ByValue : RenderModel_Vertex(), Structure.ByValue

    companion object {
        @JvmStatic
        val SIZE = 2 * HmdVec3.SIZE + 2 * Float.BYTES
        @JvmStatic
        val POSITION_OFFSET = 0
        @JvmStatic
        val NORMAL_OFFSET = HmdVec3.SIZE
        @JvmStatic
        val TEX_COORD_OFFSET = HmdVec3.SIZE * 2
    }
}

/** A texture map for use on a render model */
open class RenderModel_TextureMap : Structure {

    // width and height of the texture map in pixels
    @JvmField
    var width = 0.s
    @JvmField
    var height = 0.s
    // Map texture data. All textures are RGBA with 8 bits per channel per pixel. Data size is width * height * 4ub
    @JvmField
    var rubTextureMapData: Pointer? = null
    val textureMapData
        get() = rubTextureMapData?.getByteArray(0, SIZE.i)

    val SIZE
        get() = width * height * 4 * Byte.BYTES

    constructor()

    constructor(width: Short, height: Short, rubTextureMapData: Pointer?) {
        this.width = width
        this.height = height
        this.rubTextureMapData = rubTextureMapData
    }

    override fun getFieldOrder()= listOf("width", "height", "rubTextureMapData")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : RenderModel_TextureMap, Structure.ByReference {
        constructor() : super()
        constructor(peer: Pointer) : super(peer)
    }

    class ByValue : RenderModel_TextureMap(), Structure.ByValue
}

/**  Session unique texture identifier. Rendermodels which share the same texture will have the same id.
IDs <0 denote the texture is not present */

typealias TextureId = Int

val INVALID_TEXTURE_ID = -1

/** A texture map for use on a render model */
open class RenderModel : Structure {

    /** Vertex data for the mesh    */
    @JvmField
    var rVertexData: RenderModel_Vertex.ByReference? = null
    /** Number of vertices in the vertex data   */
    @JvmField
    var vertexCount = 0
    /** Indices into the vertex data for each triangle  */
    @JvmField
    var rIndexData: ShortByReference? = null
    /** Number of triangles in the mesh. Index count is 3 * TriangleCount   */
    @JvmField
    var triangleCount = 0
    /** Session unique texture identifier. Rendermodels which share the same texture will have the same id. <0 == texture not present   */
    @JvmField
    var diffuseTextureId = INVALID_TEXTURE_ID

    val vertices
        get() = rVertexData?.pointer?.getByteArray(0, vertexCount * RenderModel_Vertex.SIZE)

    val indices
        get() = rIndexData?.pointer?.getByteArray(0, triangleCount * 3 * Short.BYTES)

    constructor()

    constructor(vertexData: RenderModel_Vertex.ByReference, vertexCount: Int, indexData: ShortByReference, triangleCount: Int,
                diffuseTextureId: Int) {
        this.rVertexData = vertexData
        this.vertexCount = vertexCount
        this.rIndexData = indexData
        this.triangleCount = triangleCount
        this.diffuseTextureId = diffuseTextureId
    }

    override fun getFieldOrder()= listOf("rVertexData", "vertexCount", "rIndexData",
            "triangleCount", "diffuseTextureId")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : RenderModel, Structure.ByReference {
        constructor() : super()
        constructor(peer: Pointer) : super(peer)
    }

    class ByValue : RenderModel(), Structure.ByValue
}

/** A texture map for use on a render model */
open class RenderModel_ControllerMode_State : Structure {

    /** is this controller currently set to be in a scroll wheel mode   */
    @JvmField
    var bScrollWheelVisible = 0.b
    var scrollWheelVisible
        set(value) {
            bScrollWheelVisible = if (value) 1.b else 0.b
        }
        get() = bScrollWheelVisible == 1.b

    constructor()

    constructor(bScrollWheelVisible: Boolean) {
        this.bScrollWheelVisible = if (bScrollWheelVisible) 1.b else 0.b
    }

    override fun getFieldOrder()= listOf("bScrollWheelVisible")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : RenderModel_ControllerMode_State(), Structure.ByReference
    class ByValue : RenderModel_ControllerMode_State(), Structure.ByValue
}

open class IVRRenderModels : Structure {

    /** Loads and returns a render model for use in the application. renderModelName should be a render model name from the Prop_RenderModelName_String
     *  property or an absolute path name to a render model on disk.
     *
     *  The resulting render model is valid until VR_Shutdown() is called or until FreeRenderModel() is called. When the application is finished with the render
     *  model it should call FreeRenderModel() to free the memory associated with the model.
     *
     *  The method returns VRRenderModelError_Loading while the render model is still being loaded.
     *  The method returns VRRenderModelError_None once loaded successfully, otherwise will return an error. */
    fun loadRenderModel_Async(renderModelName: String, renderModel: PointerByReference) = EVRRenderModelError.of(LoadRenderModel_Async!!(renderModelName, renderModel))

    @JvmField
    var LoadRenderModel_Async: LoadRenderModel_Async_callback? = null

    interface LoadRenderModel_Async_callback : Callback {
        operator fun invoke(pchRenderModelName: String, ppRenderModel: PointerByReference): Int
    }

    /** Frees a previously returned render model
     *   It is safe to call this on a null ptr. */
    infix fun freeRenderModel(renderModel: RenderModel.ByReference) = FreeRenderModel!!(renderModel)

    @JvmField
    var FreeRenderModel: FreeRenderModel_callback? = null

    interface FreeRenderModel_callback : Callback {
        operator fun invoke(pRenderModel: RenderModel.ByReference)
    }

    /** Loads and returns a texture for use in the application. */
    fun loadTexture_Async(textureId: TextureId, texture: PointerByReference) = EVRRenderModelError.of(LoadTexture_Async!!(textureId, texture))

    @JvmField
    var LoadTexture_Async: LoadTexture_Async_callback? = null

    interface LoadTexture_Async_callback : Callback {
        operator fun invoke(textureId: TextureId, ppTexture: PointerByReference): Int
    }

    /** Frees a previously returned texture
     *  It is safe to call this on a null ptr. */
    infix fun freeTexture(texture: RenderModel_TextureMap.ByReference) = FreeTexture!!(texture)

    @JvmField
    var FreeTexture: FreeTexture_callback? = null

    interface FreeTexture_callback : Callback {
        operator fun invoke(pTexture: RenderModel_TextureMap.ByReference)
    }

    /** Creates a D3D11 texture and loads data into it. */
    fun loadTextureD3D11_Async(textureId: TextureId, d3d11Device: Pointer, d3d11Texture2D: PointerByReference) = EVRRenderModelError.of(LoadTextureD3D11_Async!!(textureId, d3d11Device, d3d11Texture2D))

    @JvmField
    var LoadTextureD3D11_Async: LoadTextureD3D11_Async_callback? = null

    interface LoadTextureD3D11_Async_callback : Callback {
        operator fun invoke(textureId: TextureId, pD3D11Device: Pointer, ppD3D11Texture2D: PointerByReference): Int
    }

    /** Helper function to copy the bits into an existing texture. */
    fun loadIntoTextureD3D11_Async(textureId: TextureId, dstTexture: Pointer) = EVRRenderModelError.of(LoadIntoTextureD3D11_Async!!(textureId, dstTexture))

    @JvmField
    var LoadIntoTextureD3D11_Async: LoadIntoTextureD3D11_Async_callback? = null

    interface LoadIntoTextureD3D11_Async_callback : Callback {
        operator fun invoke(textureId: TextureId, pDstTexture: Pointer): Int
    }

    /** Use this to free textures created with LoadTextureD3D11_Async instead of calling Release on them. */
    infix fun freeTextureD3D11(d3d11Texture2D: Pointer) = FreeTextureD3D11!!(d3d11Texture2D)

    @JvmField
    var FreeTextureD3D11: FreeTextureD3D11_callback? = null

    interface FreeTextureD3D11_callback : Callback {
        operator fun invoke(pD3D11Texture2D: Pointer)
    }

    /** Use this to get the names of available render models.  Index does not correlate to a tracked device index, but is only used for iterating over all
     *  available render models.  If the index is out of range, this function will return 0.
     *  Otherwise, it will return the size of the buffer required for the name. */
    fun getRenderModelName(renderModelIndex: Int, renderModelName: String, renderModelNameLen: Int) = GetRenderModelName!!(renderModelIndex, renderModelName, renderModelNameLen)

    @JvmField
    var GetRenderModelName: GetRenderModelName_callback? = null

    interface GetRenderModelName_callback : Callback {
        operator fun invoke(unRenderModelIndex: Int, pchRenderModelName: String, unRenderModelNameLen: Int): Int
    }

    /** Returns the number of available render models. */
    val renderModelCount get() = GetRenderModelCount!!()

    @JvmField
    var GetRenderModelCount: GetRenderModelCount_callback? = null

    interface GetRenderModelCount_callback : Callback {
        operator fun invoke(): Int
    }


    /** Returns the number of components of the specified render model.
     *  Components are useful when client application wish to draw, label, or otherwise interact with components of tracked objects.
     *  Examples controller components:
     *      renderable things such as triggers, buttons
     *      non-renderable things which include coordinate systems such as 'tip', 'base', a neutral controller agnostic hand-pose
     *      If all controller components are enumerated and rendered, it will be equivalent to drawing the traditional render model
     *      Returns 0 if components not supported, >0 otherwise */
    infix fun getComponentCount(renderModelName: String) = GetComponentCount!!(renderModelName)

    @JvmField
    var GetComponentCount: GetComponentCount_callback? = null

    interface GetComponentCount_callback : Callback {
        operator fun invoke(pchRenderModelName: String): Int
    }

    /** Wrapper: returns a string property. If the device index is not valid or the property is not a string value this function will
     *  return an empty String. */
    fun getComponentName(renderModelName: String, componentIndex: Int): String {
        val bytes = ByteArray(maxPropertyStringSize)
        return when {
            GetComponentName!!(renderModelName, componentIndex, bytes, bytes.size) > 0 -> String(bytes).filter { it.isLetterOrDigit() || it == '_' }
            else -> ""
        }
    }

    @JvmField
    var GetComponentName: GetComponentName_callback? = null

    /** Use this to get the names of available components.  Index does not correlate to a tracked device index, but is only used for iterating over all available
     *  components.  If the index is out of range, this function will return 0.
     *  Otherwise, it will return the size of the buffer required for the name. */
    interface GetComponentName_callback : Callback {
        operator fun invoke(pchRenderModelName: String, unComponentIndex: Int, pchComponentName: ByteArray?, unComponentNameLen: Int): Int
    }

    /** Get the button mask for all buttons associated with this component
     *  If no buttons (or axes) are associated with this component, return 0
     *      Note: multiple components may be associated with the same button. Ex: two grip buttons on a single controller.
     *      Note: A single component may be associated with multiple buttons. Ex: A trackpad which also provides "D-pad" functionality */
    fun getComponentButtonMask(renderModelName: String, componentName: String) = GetComponentButtonMask!!(renderModelName, componentName)

    @JvmField
    var GetComponentButtonMask: GetComponentButtonMask_callback? = null

    interface GetComponentButtonMask_callback : Callback {
        operator fun invoke(pchRenderModelName: String, pchComponentName: String): Long
    }

    /** Wrapper: returns a string property. If the component name is out of range or the property is not a string value this function will
     *  return an empty String. */
    fun getComponentRenderModelName(renderModelName: String, componentName: String): String {
        val bytes = ByteArray(maxPropertyStringSize)
        return when {
            GetComponentRenderModelName!!(renderModelName, componentName, bytes, bytes.size) > 0 -> String(bytes).trim()
            else -> ""
        }
    }

    @JvmField
    var GetComponentRenderModelName: GetComponentRenderModelName_callback? = null

    /** Use this to get the render model name for the specified rendermode/component combination, to be passed to LoadRenderModel.
     *  If the component name is out of range, this function will return 0.
     *  Otherwise, it will return the size of the buffer required for the name. */
    interface GetComponentRenderModelName_callback : Callback {
        operator fun invoke(pchRenderModelName: String, pchComponentName: String, pchComponentRenderModelName: ByteArray?, unComponentRenderModelNameLen: Int): Int
    }

    /** Use this to query information about the component, as a function of the controller state.
     *
     *  For dynamic controller components (ex: trigger) values will reflect component motions
     *  For static components this will return a consistent value independent of the VRControllerState
     *
     *  If the renderModelName or componentName is invalid, this will return false (and transforms will be set to identity).
     *  Otherwise, return true
     *  Note: For dynamic objects, visibility may be dynamic. (I.e., true/false will be returned based on controller state and controller mode state ) */
    fun getComponentState(renderModelName: String, componentName: String, controllerState: VRControllerState.ByReference, state: RenderModel_ControllerMode_State.ByReference, componentState: RenderModel_ComponentState.ByReference) = GetComponentState!!(renderModelName, componentName, controllerState, state, componentState)

    @JvmField
    var GetComponentState: GetComponentState_callback? = null

    interface GetComponentState_callback : Callback {
        operator fun invoke(pchRenderModelName: String, pchComponentName: String, pControllerState: VRControllerState.ByReference, pState: RenderModel_ControllerMode_State.ByReference, pComponentState: RenderModel_ComponentState.ByReference): Boolean
    }

    /** Returns true if the render model has a component with the specified name */
    fun renderModelHasComponent(renderModelName: String, componentName: String) = RenderModelHasComponent!!(renderModelName, componentName)

    @JvmField
    var RenderModelHasComponent: RenderModelHasComponent_callback? = null

    interface RenderModelHasComponent_callback : Callback {
        operator fun invoke(pchRenderModelName: String, pchComponentName: String): Boolean
    }

    /** Returns the URL of the thumbnail image for this rendermodel */
    fun getRenderModelThumbnailURL(renderModelName: String, thumbnailURL: String, thumbnailUrlLen: Int, error: EVRRenderModelError_ByReference) = GetRenderModelThumbnailURL!!(renderModelName, thumbnailURL, thumbnailUrlLen, error)

    @JvmField
    var GetRenderModelThumbnailURL: GetRenderModelThumbnailURL_callback? = null

    interface GetRenderModelThumbnailURL_callback : Callback {
        operator fun invoke(pchRenderModelName: String, pchThumbnailURL: String, unThumbnailURLLen: Int, peError: EVRRenderModelError_ByReference): Int
    }

    /** Provides a render model path that will load the unskinned model if the model name provided has been replace by the user. If the model hasn't been
     *  replaced the path value will still be a valid path to load the model. Pass this to LoadRenderModel_Async, etc. to load the model. */
    fun getRenderModelOriginalPath(renderModelName: String, originalPath: String, originalPathLen: Int, error: EVRRenderModelError_ByReference) = GetRenderModelOriginalPath!!(renderModelName, originalPath, originalPathLen, error)

    @JvmField
    var GetRenderModelOriginalPath: GetRenderModelOriginalPath_callback? = null

    interface GetRenderModelOriginalPath_callback : Callback {
        operator fun invoke(pchRenderModelName: String, pchOriginalPath: String, unOriginalPathLen: Int, peError: EVRRenderModelError_ByReference): Int
    }

    /** Returns a string for a render model error
     *  You can also use EVRRenderModelError.getName()  */
    infix fun getRenderModelErrorNameFromEnum(error: EVRRenderModelError) = GetRenderModelErrorNameFromEnum!!(error.i)

    @JvmField
    var GetRenderModelErrorNameFromEnum: GetRenderModelErrorNameFromEnum_callback? = null

    interface GetRenderModelErrorNameFromEnum_callback : Callback {
        operator fun invoke(error: Int): String
    }

    constructor()

    override fun getFieldOrder()= listOf("LoadRenderModel_Async", "FreeRenderModel", "LoadTexture_Async", "FreeTexture",
            "LoadTextureD3D11_Async", "LoadIntoTextureD3D11_Async", "FreeTextureD3D11", "GetRenderModelName", "GetRenderModelCount", "GetComponentCount",
            "GetComponentName", "GetComponentButtonMask", "GetComponentRenderModelName", "GetComponentState", "RenderModelHasComponent",
            "GetRenderModelThumbnailURL", "GetRenderModelOriginalPath", "GetRenderModelErrorNameFromEnum")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRRenderModels(), Structure.ByReference
    class ByValue : IVRRenderModels(), Structure.ByValue
}

val IVRRenderModels_Version = "IVRRenderModels_005"