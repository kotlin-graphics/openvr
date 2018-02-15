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
import java.util.*

// ivrrendermodels.h ==============================================================================================================================================

val k_pch_Controller_Component_GDC2015 = "gdc2015"   // Canonical coordinate system of the gdc 2015 wired controller, provided for backwards compatibility
val k_pch_Controller_Component_Base = "base"         // For controllers with an unambiguous 'base'.
val k_pch_Controller_Component_Tip = "tip"           // For controllers with an unambiguous 'tip' (used for 'laser-pointing')
val k_pch_Controller_Component_HandGrip = "handgrip" // Neutral, ambidextrous hand-pose when holding controller. On plane between neutrally posed index finger and thumb
val k_pch_Controller_Component_Status = "status"     // 1:1 aspect ratio status area, with canonical [0,1] uv mapping

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
open class RenderModel_ComponentState_t : Structure {

    @JvmField var mTrackingToComponentRenderModel = HmdMat34()  // Transform required when drawing the component render model
    @JvmField var mTrackingToComponentLocal = HmdMat34()        // Transform available for attaching to a local component coordinate system (-Z out from surface )
    @JvmField var uProperties: VRComponentProperties = 0

    constructor()

    constructor(mTrackingToComponentRenderModel: HmdMat34, mTrackingToComponentLocal: HmdMat34, uProperties: Int) {
        this.mTrackingToComponentRenderModel = mTrackingToComponentRenderModel
        this.mTrackingToComponentLocal = mTrackingToComponentLocal
        this.uProperties = uProperties
    }

    override fun getFieldOrder(): List<String> = Arrays.asList("mTrackingToComponentRenderModel", "mTrackingToComponentLocal", "uProperties")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : RenderModel_ComponentState_t(), Structure.ByReference
    class ByValue : RenderModel_ComponentState_t(), Structure.ByValue
}

/** A single vertex in a render model */
open class RenderModel_Vertex_t : Structure {

    @JvmField var vPosition = HmdVec3()  // position in meters in device space
    @JvmField var vNormal = HmdVec3()
    @JvmField var rfTextureCoord = floatArrayOf(0f, 0f)

    operator fun get(i: Int) = when (i) {
        in 0 until 3 -> vPosition[i]
        in 3 until 6 -> vNormal[i]
        in 6 until 8 -> rfTextureCoord[i]
        else -> throw IndexOutOfBoundsException()
    }

    constructor()

    constructor(vPosition: HmdVec3, vNormal: HmdVec3, rfTextureCoord: FloatArray) {
        this.vPosition = vPosition
        this.vNormal = vNormal
        // TODO check all size checks, necessary/dangerous?
        if (rfTextureCoord.size != this.rfTextureCoord.size) throw IllegalArgumentException("Wrong array size!")
        this.rfTextureCoord = rfTextureCoord
    }

    override fun getFieldOrder(): List<String> = Arrays.asList("vPosition", "vNormal", "rfTextureCoord")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : RenderModel_Vertex_t(), Structure.ByReference
    class ByValue : RenderModel_Vertex_t(), Structure.ByValue

    companion object {
        @JvmStatic val SIZE = 2 * HmdVec3.SIZE + 2 * Float.BYTES
        @JvmStatic val POSITION_OFFSET = 0
        @JvmStatic val NORMAL_OFFSET = HmdVec3.SIZE
        @JvmStatic val TEX_COORD_OFFSET = HmdVec3.SIZE * 2
    }
}

/** A texture map for use on a render model */
open class RenderModel_TextureMap_t : Structure {

    // width and height of the texture map in pixels
    @JvmField var unWidth = 0.toShort()
    @JvmField var unHeight = 0.toShort()
    // Map texture data. All textures are RGBA with 8 bits per channel per pixel. Data size is width * height * 4ub
    @JvmField var rubTextureMapData_internal: Pointer? = null
    val textureMapData
        get() = rubTextureMapData_internal?.getByteArray(0, SIZE.i)

    val SIZE
        get() = unWidth * unHeight * 4 * Byte.BYTES

    constructor()

    constructor(unWidth: Short, unHeight: Short, rubTextureMapData_internal: Pointer?) {
        this.unWidth = unWidth
        this.unHeight = unHeight
        this.rubTextureMapData_internal = rubTextureMapData_internal
    }

    override fun getFieldOrder(): List<String> = Arrays.asList("unWidth", "unHeight", "rubTextureMapData_internal")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : RenderModel_TextureMap_t, Structure.ByReference {
        constructor() : super()
        constructor(peer: Pointer) : super(peer)
    }

    class ByValue : RenderModel_TextureMap_t(), Structure.ByValue
}

/**  Session unique texture identifier. Rendermodels which share the same texture will have the same id.
IDs <0 denote the texture is not present */

typealias TextureID_t = Int

val INVALID_TEXTURE_ID = -1

/** A texture map for use on a render model */
open class RenderModel_t : Structure {

    @JvmField var rVertexData_internal: RenderModel_Vertex_t.ByReference? = null//   Vertex data for the mesh
    @JvmField var unVertexCount = 0                                      // Number of vertices in the vertex data
    @JvmField var rIndexData_internal: ShortByReference? = null                   // Indices into the vertex data for each triangle
    @JvmField var unTriangleCount = 0                    //                 Number of triangles in the mesh. Index count is 3 * TriangleCount
    // Session unique texture identifier. Rendermodels which share the same texture will have the same id. <0 == texture not present
    @JvmField var diffuseTextureId = INVALID_TEXTURE_ID

    val vertices
        get() = rVertexData_internal?.pointer?.getByteArray(0, unVertexCount * RenderModel_Vertex_t.SIZE)

    val indices
        get() = rIndexData_internal?.pointer?.getByteArray(0, unTriangleCount * 3 * Short.BYTES)

    constructor()

    constructor(rVertexData: RenderModel_Vertex_t.ByReference, unVertexCount: Int, rIndexData: ShortByReference, unTriangleCount: Int,
                diffuseTextureId: Int) {
        this.rVertexData_internal = rVertexData
        this.unVertexCount = unVertexCount
        this.rIndexData_internal = rIndexData
        this.unTriangleCount = unTriangleCount
        this.diffuseTextureId = diffuseTextureId
    }

    override fun getFieldOrder(): List<String> = Arrays.asList("rVertexData_internal", "unVertexCount", "rIndexData_internal",
            "unTriangleCount", "diffuseTextureId")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : RenderModel_t, Structure.ByReference {
        constructor() : super()
        constructor(peer: Pointer) : super(peer)
    }

    class ByValue : RenderModel_t(), Structure.ByValue
}

/** A texture map for use on a render model */
open class RenderModel_ControllerMode_State_t : Structure {

    @JvmField var bScrollWheelVisible_internal = 0.b   // is this controller currently set to be in a scroll wheel mode
    var bScrollWheelVisible
    set(value) {
        bScrollWheelVisible_internal = if(value) 1.b else 0.b
    }
    get() = bScrollWheelVisible_internal == 1.b

    constructor()

    constructor(bScrollWheelVisible: Boolean) {
        this.bScrollWheelVisible_internal = if(bScrollWheelVisible) 1.b else 0.b
    }

    override fun getFieldOrder(): List<String> = Arrays.asList("bScrollWheelVisible_internal")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : RenderModel_ControllerMode_State_t(), Structure.ByReference
    class ByValue : RenderModel_ControllerMode_State_t(), Structure.ByValue
}

open class IVRRenderModels : Structure {

    /** Loads and returns a render model for use in the application. pchRenderModelName should be a render model name from the Prop_RenderModelName_String
     *  property or an absolute path name to a render model on disk.
     *
     *  The resulting render model is valid until VR_Shutdown() is called or until FreeRenderModel() is called. When the application is finished with the render
     *  model it should call FreeRenderModel() to free the memory associated with the model.
     *
     *  The method returns VRRenderModelError_Loading while the render model is still being loaded.
     *  The method returns VRRenderModelError_None once loaded successfully, otherwise will return an error. */
    fun loadRenderModel_Async(pchRenderModelName: String, ppRenderModel: PointerByReference)
            = EVRRenderModelError.of(LoadRenderModel_Async!!.invoke(pchRenderModelName, ppRenderModel))

    @JvmField var LoadRenderModel_Async: LoadRenderModel_Async_callback? = null

    interface LoadRenderModel_Async_callback : Callback {
        fun invoke(pchRenderModelName: String, ppRenderModel: PointerByReference): Int
    }

    /** Frees a previously returned render model
     *   It is safe to call this on a null ptr. */
    fun freeRenderModel(pRenderModel: RenderModel_t.ByReference) = FreeRenderModel!!.invoke(pRenderModel)

    @JvmField var FreeRenderModel: FreeRenderModel_callback? = null

    interface FreeRenderModel_callback : Callback {
        fun invoke(pRenderModel: RenderModel_t.ByReference)
    }

    /** Loads and returns a texture for use in the application. */
    fun loadTexture_Async(textureId: TextureID_t, ppTexture: PointerByReference) = EVRRenderModelError.of(LoadTexture_Async!!.invoke(textureId, ppTexture))

    @JvmField var LoadTexture_Async: LoadTexture_Async_callback? = null

    interface LoadTexture_Async_callback : Callback {
        fun invoke(textureId: TextureID_t, ppTexture: PointerByReference): Int
    }

    /** Frees a previously returned texture
     *  It is safe to call this on a null ptr. */
    fun freeTexture(pTexture: RenderModel_TextureMap_t.ByReference) = FreeTexture!!.invoke(pTexture)

    @JvmField var FreeTexture: FreeTexture_callback? = null

    interface FreeTexture_callback : Callback {
        fun invoke(pTexture: RenderModel_TextureMap_t.ByReference)
    }

    /** Creates a D3D11 texture and loads data into it. */
    fun loadTextureD3D11_Async(textureId: TextureID_t, pD3D11Device: Pointer, ppD3D11Texture2D: PointerByReference)
            = EVRRenderModelError.of(LoadTextureD3D11_Async!!.invoke(textureId, pD3D11Device, ppD3D11Texture2D))

    @JvmField var LoadTextureD3D11_Async: LoadTextureD3D11_Async_callback? = null

    interface LoadTextureD3D11_Async_callback : Callback {
        fun invoke(textureId: TextureID_t, pD3D11Device: Pointer, ppD3D11Texture2D: PointerByReference): Int
    }

    /** Helper function to copy the bits into an existing texture. */
    fun loadIntoTextureD3D11_Async(textureId: TextureID_t, pDstTexture: Pointer)
            = EVRRenderModelError.of(LoadIntoTextureD3D11_Async!!.invoke(textureId, pDstTexture))

    @JvmField var LoadIntoTextureD3D11_Async: LoadIntoTextureD3D11_Async_callback? = null

    interface LoadIntoTextureD3D11_Async_callback : Callback {
        fun invoke(textureId: TextureID_t, pDstTexture: Pointer): Int
    }

    /** Use this to free textures created with LoadTextureD3D11_Async instead of calling Release on them. */
    fun freeTextureD3D11(pD3D11Texture2D: Pointer) = FreeTextureD3D11!!.invoke(pD3D11Texture2D)

    @JvmField var FreeTextureD3D11: FreeTextureD3D11_callback? = null

    interface FreeTextureD3D11_callback : Callback {
        fun invoke(pD3D11Texture2D: Pointer)
    }

    /** Use this to get the names of available render models.  Index does not correlate to a tracked device index, but is only used for iterating over all
     *  available render models.  If the index is out of range, this function will return 0.
     *  Otherwise, it will return the size of the buffer required for the name. */
    fun getRenderModelName(unRenderModelIndex: Int, pchRenderModelName: String, unRenderModelNameLen: Int)
            = GetRenderModelName!!.invoke(unRenderModelIndex, pchRenderModelName, unRenderModelNameLen)

    @JvmField var GetRenderModelName: GetRenderModelName_callback? = null

    interface GetRenderModelName_callback : Callback {
        fun invoke(unRenderModelIndex: Int, pchRenderModelName: String, unRenderModelNameLen: Int): Int
    }

    /** Returns the number of available render models. */
    fun getRenderModelCount() = GetRenderModelCount!!.invoke()

    @JvmField var GetRenderModelCount: GetRenderModelCount_callback? = null

    interface GetRenderModelCount_callback : Callback {
        fun invoke(): Int
    }


    /** Returns the number of components of the specified render model.
     *  Components are useful when client application wish to draw, label, or otherwise interact with components of tracked objects.
     *  Examples controller components:
     *      renderable things such as triggers, buttons
     *      non-renderable things which include coordinate systems such as 'tip', 'base', a neutral controller agnostic hand-pose
     *      If all controller components are enumerated and rendered, it will be equivalent to drawing the traditional render model
     *      Returns 0 if components not supported, >0 otherwise */
    fun getComponentCount(pchRenderModelName: String) = GetComponentCount!!.invoke(pchRenderModelName)

    @JvmField var GetComponentCount: GetComponentCount_callback? = null

    interface GetComponentCount_callback : Callback {
        fun invoke(pchRenderModelName: String): Int
    }

    /** Wrapper: returns a string property. If the device index is not valid or the property is not a string value this function will
     *  return an empty String. */
    fun getComponentName(pchRenderModelName: String, unComponentIndex: Int): String {

        var ret = ""
        val bytes = ByteArray(maxPropertyStringSize) // TODO optimize?

        val propLen = GetComponentName!!.invoke(pchRenderModelName, unComponentIndex, bytes, bytes.size)

        if(propLen > 0){
            ret = String(bytes).filter { it.isLetterOrDigit() || it == '_' }
        }

        return ret
    }

    @JvmField
    var GetComponentName: GetComponentName_callback? = null

    /** Use this to get the names of available components.  Index does not correlate to a tracked device index, but is only used for iterating over all available
     *  components.  If the index is out of range, this function will return 0.
     *  Otherwise, it will return the size of the buffer required for the name. */
    interface GetComponentName_callback : Callback {
        fun invoke(pchRenderModelName: String, unComponentIndex: Int, pchComponentName: ByteArray?, unComponentNameLen: Int): Int
    }

    /** Get the button mask for all buttons associated with this component
     *  If no buttons (or axes) are associated with this component, return 0
     *      Note: multiple components may be associated with the same button. Ex: two grip buttons on a single controller.
     *      Note: A single component may be associated with multiple buttons. Ex: A trackpad which also provides "D-pad" functionality */
    fun getComponentButtonMask(pchRenderModelName: String, pchComponentName: String) = GetComponentButtonMask!!.invoke(pchRenderModelName, pchComponentName)

    @JvmField var GetComponentButtonMask: GetComponentButtonMask_callback? = null

    interface GetComponentButtonMask_callback : Callback {
        fun invoke(pchRenderModelName: String, pchComponentName: String): Long
    }

    /** Wrapper: returns a string property. If the component name is out of range or the property is not a string value this function will
     *  return an empty String. */
    fun getComponentRenderModelName(pchRenderModelName: String, pchComponentName: String) : String {

        var ret = ""
        val bytes = ByteArray(maxPropertyStringSize) // TODO optimize?

        val propLen = GetComponentRenderModelName!!.invoke(pchRenderModelName, pchComponentName, bytes, bytes.size)

        if(propLen > 0){

            ret = String(bytes).trim()
        }

        return ret
    }

    @JvmField var GetComponentRenderModelName: GetComponentRenderModelName_callback? = null

    /** Use this to get the render model name for the specified rendermode/component combination, to be passed to LoadRenderModel.
     *  If the component name is out of range, this function will return 0.
     *  Otherwise, it will return the size of the buffer required for the name. */
    interface GetComponentRenderModelName_callback : Callback {
        fun invoke(pchRenderModelName: String, pchComponentName: String, pchComponentRenderModelName: ByteArray?, unComponentRenderModelNameLen: Int): Int
    }

    /** Use this to query information about the component, as a function of the controller state.
     *
     *  For dynamic controller components (ex: trigger) values will reflect component motions
     *  For static components this will return a consistent value independent of the VRControllerState
     *
     *  If the pchRenderModelName or pchComponentName is invalid, this will return false (and transforms will be set to identity).
     *  Otherwise, return true
     *  Note: For dynamic objects, visibility may be dynamic. (I.e., true/false will be returned based on controller state and controller mode state ) */
    fun getComponentState(pchRenderModelName: String, pchComponentName: String, pControllerState: VRControllerState.ByReference,
                          pState: RenderModel_ControllerMode_State_t.ByReference, pComponentState: RenderModel_ComponentState_t.ByReference)
            = GetComponentState!!.invoke(pchRenderModelName, pchComponentName, pControllerState, pState, pComponentState)

    @JvmField var GetComponentState: GetComponentState_callback? = null

    interface GetComponentState_callback : Callback {
        fun invoke(pchRenderModelName: String, pchComponentName: String, pControllerState: VRControllerState.ByReference,
                   pState: RenderModel_ControllerMode_State_t.ByReference, pComponentState: RenderModel_ComponentState_t.ByReference): Boolean
    }

    /** Returns true if the render model has a component with the specified name */
    fun renderModelHasComponent(pchRenderModelName: String, pchComponentName: String) = RenderModelHasComponent!!.invoke(pchRenderModelName, pchComponentName)

    @JvmField var RenderModelHasComponent: RenderModelHasComponent_callback? = null

    interface RenderModelHasComponent_callback : Callback {
        fun invoke(pchRenderModelName: String, pchComponentName: String): Boolean
    }

    /** Returns the URL of the thumbnail image for this rendermodel */
    fun getRenderModelThumbnailURL(pchRenderModelName: String, pchThumbnailURL: String, unThumbnailURLLen: Int, peError: EVRRenderModelError_ByReference)
            = GetRenderModelThumbnailURL!!.invoke(pchRenderModelName, pchThumbnailURL, unThumbnailURLLen, peError)

    @JvmField var GetRenderModelThumbnailURL: GetRenderModelThumbnailURL_callback? = null

    interface GetRenderModelThumbnailURL_callback : Callback {
        fun invoke(pchRenderModelName: String, pchThumbnailURL: String, unThumbnailURLLen: Int, peError: EVRRenderModelError_ByReference): Int
    }

    /** Provides a render model path that will load the unskinned model if the model name provided has been replace by the user. If the model hasn't been
     *  replaced the path value will still be a valid path to load the model. Pass this to LoadRenderModel_Async, etc. to load the model. */
    fun getRenderModelOriginalPath(pchRenderModelName: String, pchOriginalPath: String, unOriginalPathLen: Int, peError: EVRRenderModelError_ByReference)
            = GetRenderModelOriginalPath!!.invoke(pchRenderModelName, pchOriginalPath, unOriginalPathLen, peError)

    @JvmField var GetRenderModelOriginalPath: GetRenderModelOriginalPath_callback? = null

    interface GetRenderModelOriginalPath_callback : Callback {
        fun invoke(pchRenderModelName: String, pchOriginalPath: String, unOriginalPathLen: Int, peError: EVRRenderModelError_ByReference): Int
    }

    /** Returns a string for a render model error
     *  You can also use EVRRenderModelError.getName()  */
    fun getRenderModelErrorNameFromEnum(error: EVRRenderModelError) = GetRenderModelErrorNameFromEnum!!.invoke(error.i)

    @JvmField var GetRenderModelErrorNameFromEnum: GetRenderModelErrorNameFromEnum_callback? = null

    interface GetRenderModelErrorNameFromEnum_callback : Callback {
        fun invoke(error: Int): String
    }

    constructor()

    override fun getFieldOrder(): List<String> = Arrays.asList("LoadRenderModel_Async", "FreeRenderModel", "LoadTexture_Async", "FreeTexture",
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