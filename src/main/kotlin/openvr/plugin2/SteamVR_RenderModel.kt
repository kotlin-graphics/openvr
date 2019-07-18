package openvr.plugin2

import gli_.Format
import gli_.Texture2d
import gli_.memCopy
import glm_.i
import glm_.quat.Quat
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import kool.adr
import kool.remSize
import openvr.lib.*
import org.lwjgl.openvr.*
import java.util.*
import org.lwjgl.system.MemoryStack.stackPush


class SteamVR_RenderModel : Component() {

    var index = TrackedObject.Index.None
    protected var inputSource = SteamVR_Input_Sources.Any

    val modelOverrideWarning = "Model override is really only meant to be used in " +
            "the scene view for lining things up; using it at runtime is discouraged.  Use tracked device " +
            "index instead to ensure the correct model is displayed for all users."

    /** Model override is really only meant to be used in the scene view for lining things up; using it at runtime is discouraged.
     *  Use tracked device index instead to ensure the correct model is displayed for all users. */
    var modelOverride = ""

    /** Shader to apply to model. */
    var shader: Shader? = null

    /** Enable to print out when render models are loaded.  */
    var verbose = false

    /** If available, break down into separate components instead of loading as a single mesh. */
    var createComponents = true

    /** Update transforms of components at runtime to reflect user action. */
    var updateDynamically = true

    /** Additional controller settings for showing scrollwheel, etc. */
    lateinit var controllerModeState: RenderModelControllerModeState

    /** Name of the sub-object which represents the "local" coordinate space for each component. */
    val localTransformName = "attach"

    /** Cached name of this render model for updating component transforms at runtime. */
    var renderModelName = ""
        private set

    var initializedAttachPoints = false

    val componentAttachPoints = mutableMapOf<String, Transform?>()

//    private List<MeshRenderer> meshRenderers = new List<MeshRenderer>();

    // If someone knows how to keep these from getting cleaned up every time
    // you exit play mode, let me know.  I've tried marking the RenderModel
    // class below as [System.Serializable] and switching to normal public
    // variables for mesh and material to get them to serialize properly,
    // as well as tried marking the mesh and material objects as
    // DontUnloadUnusedAsset, but Unity was still unloading them.
    // The hashtable is preserving its entries, but the mesh and material
    // variables are going null.

    class RenderModel(var mesh: Mesh?, val material: Material)

    val models = Hashtable<String, RenderModel>()
    val materials = Hashtable<TextureId, Material>()

    // Helper class to load render models interface on demand and clean up when done.
    /*public sealed class RenderModelInterfaceHolder : System.IDisposable
    {
        private bool needsShutdown, failedLoadInterface;
        private CVRRenderModels _instance;
        public CVRRenderModels instance
        {
            get
            {
                if (_instance == null && !failedLoadInterface)
                {
                    if (Application.isEditor && Application.isPlaying == false)
                        needsShutdown = SteamVR.InitializeTemporarySession();

                    _instance = OpenVR.RenderModels;
                    if (_instance == null)
                    {
                        Debug.LogError("<b>[SteamVR]</b> Failed to load IVRRenderModels interface version " + OpenVR.IVRRenderModels_Version);
                        failedLoadInterface = true;
                    }
                }
                return _instance;
            }
        }
        public void Dispose()
        {
            if (needsShutdown)
                SteamVR.ExitTemporarySession();
        }
    }*/

//    private void OnModelSkinSettingsHaveChanged(VREvent_t vrEvent)
//    {
//        if (!string.IsNullOrEmpty(renderModelName))
//        {
//            renderModelName = "";
//            UpdateModel();
//        }
//    }
//
//    public void SetMeshRendererState(bool state)
//    {
//        for (int rendererIndex = 0; rendererIndex < meshRenderers.Count; rendererIndex++)
//        {
//            MeshRenderer renderer = meshRenderers[rendererIndex];
//
//            if (renderer != null)
//                renderer.enabled = state;
//        }
//    }

//    fun onHideRenderModels(hidden: Boolean) = setMeshRendererState(!hidden)

    fun onDeviceConnected(i: Int, connected: Boolean) {
        if (i != index.i)
            return

        if (connected)
            updateModel()
    }

    fun updateModel() {

        if (index == TrackedObject.Index.None)
            return

        val s = vrSystem.getStringTrackedDeviceProperty(index.i, TrackedDeviceProperty.RenderModelName_String)
        if (renderModelName != s)
            setModelAsync(s)
    }

    fun setModelAsync(newRenderModelName: String) {

//        meshRenderers.Clear();

        if (newRenderModelName.isEmpty())
            return

        // Preload all render models before asking for the data to create meshes.

        // Gather names of render models to preload.
        lateinit var renderModelNames: Array<String?>

        val count = vrRenderModels.getComponentCount(newRenderModelName)
        if (count > 0) {

            renderModelNames = Array(count) { null }

            repeat(count) { componentIndex ->

                val componentName = vrRenderModels.getComponentName(newRenderModelName, componentIndex)!!

                val s = vrRenderModels.getComponentRenderModelName(newRenderModelName, componentName)

                // Only need to preload if not already cached.
                if (models[s]?.mesh == null)
                    renderModelNames[componentIndex] = s
            }
        } else
            renderModelNames = when { // Only need to preload if not already cached.
                models[newRenderModelName]?.mesh == null -> arrayOf(newRenderModelName)
                else -> emptyArray()
            }

        // Keep trying every 100ms until all components finish loading.
        while (true) {
            var loading = false
            renderModelNames.filterNot { it.isNullOrEmpty() }.forEach { modelName ->

                val renderModel = vrRenderModels.loadRenderModel_Async(modelName!!)

                if (vrRenderModels.error == vrRenderModels.Error.Loading)
                    loading = true
                else if (vrRenderModels.error == vrRenderModels.Error.None) {
                    // Preload textures as well.
                    // Check the cache first.
                    val material = materials[renderModel!!.diffuseTextureId]
                    if (material?.mainTexture == null) {
                        vrRenderModels.loadTexture_Async(renderModel.diffuseTextureId)
                        if (vrRenderModels.error == vrRenderModels.Error.Loading)
                            loading = true
                    }
                }
            }
            if (loading)
                Thread.sleep(100)
            else
                break
        }

        val success = setModel(newRenderModelName)
        this.renderModelName = newRenderModelName
//        SteamVR_Events.RenderModelLoaded.Send(this, success)
    }

    fun setModel(renderModelName: String): Boolean {

//        StripMesh(gameObject)

        if (createComponents) {

            componentAttachPoints.clear()

            if (loadComponents(renderModelName)) {
                updateComponents()
                return true
            }

            println("[SteamVR] Render model does not support components, falling back to single mesh.")
        }

        if (renderModelName.isNotEmpty()) {
            var model = models[renderModelName]
            if (model?.mesh == null) {
                if (verbose)
                    println("[SteamVR] Loading render model $renderModelName")

                model = loadRenderModel(renderModelName, renderModelName)
                if (model == null)
                    return false

                models[renderModelName] = model
            }

//                gameObject.AddComponent<MeshFilter>().mesh = model.mesh
//                MeshRenderer newRenderer = gameObject . AddComponent < MeshRenderer >()
//                newRenderer.sharedMaterial = model.material
//                meshRenderers.Add(newRenderer)
            return true
        }

        return false
    }

    fun loadRenderModel(renderModelName: String, baseName: String): RenderModel? {

        val renderModel: org.lwjgl.openvr.RenderModel? = vrRenderModels.loadRenderModel_Async(renderModelName)

        if (vrRenderModels.error != vrRenderModels.Error.None) {
            System.err.println("[SteamVR] Failed to load render model $renderModelName - ${vrRenderModels.error}")
            return null
        }

        val v = renderModel!!.vertices
        val vertexSize = Vec3.length * 2 + Vec2.length
        val vertices = Array(renderModel.vertexCount) { val p = it * vertexSize; Vec3(v[p], v[p + 1], -v[p + 2]) }
        val normals = Array(renderModel.vertexCount) { val p = it * vertexSize + Vec3.length; Vec3(v[p], v[p + 1], -v[p + 2]) }
        val uv = Array(renderModel.vertexCount) { val p = it * vertexSize + Vec3.length * 2; Vec2(v[p], v[p + 1]) }

        val indexCount = renderModel.triangleCount * 3
        val indices = renderModel.indices

        val triangles = IntArray(indexCount)
        for (iTri in 0 until renderModel.triangleCount) {
            triangles[iTri * 3 + 0] = indices[iTri * 3 + 2].i
            triangles[iTri * 3 + 1] = indices[iTri * 3 + 1].i
            triangles[iTri * 3 + 2] = indices[iTri * 3 + 0].i
        }

        val mesh = Mesh()
        mesh.vertices = vertices
        mesh.normals = normals
        mesh.uv = uv
        mesh.triangles = triangles

//        #if (UNITY_5_4 || UNITY_5_3 || UNITY_5_2 || UNITY_5_1 || UNITY_5_0)
//            mesh.Optimize()
//        #endif
        //mesh.hideFlags = HideFlags.DontUnloadUnusedAsset;

        // Check cache before loading texture.
        var material = materials[renderModel.diffuseTextureId]
        if (material?.mainTexture == null) {

            val diffuseTexture: RenderModelTextureMap? = vrRenderModels.loadTexture_Async(renderModel.diffuseTextureId)

            if (vrRenderModels.error == vrRenderModels.Error.None) {

                val texture = Texture2d(Format.RGBA8_UNORM_PACK8, diffuseTexture!!.size)

                val textureMapData = diffuseTexture.textureMapData

                memCopy(textureMapData.adr, texture.data().adr, textureMapData.remSize)

                material = Material (/*shader ?: Shader.Find("Standard")*/)
                material.mainTexture = texture
                //material.hideFlags = HideFlags.DontUnloadUnusedAsset;

                materials[renderModel.diffuseTextureId] = material

                diffuseTexture.free()
            } else
                error("[SteamVR] Failed to load render model texture for render model $renderModelName. Error: ${vrRenderModels.error}")
        }

        // Delay freeing when we can since we'll often get multiple requests for the same model right
        // after another (e.g. two controllers or two basestations).
//        #if UNITY_EDITOR
//        if (!Application.isPlaying)
            renderModel.free()
//        else
//        #endif
//        StartCoroutine(FreeRenderModel(pRenderModel))

        return RenderModel (mesh, material)
    }

    //        IEnumerator FreeRenderModel (System.IntPtr pRenderModel)
//        {
//            `yield` return new WaitForSeconds (1.0f)
//
//            using(var holder = new RenderModelInterfaceHolder())
//            {
//                var renderModels = holder.instance
//                renderModels.FreeRenderModel(pRenderModel)
//            }
//        }
//
    fun findTransformByName(componentName: String, inTransform_: Transform? = null): Transform? {
        val inTransform = inTransform_ ?: this.transform
        return inTransform.children.find { it.name == componentName }
    }

    //
//        public Transform GetComponentTransform(string componentName)
//        {
//            if (componentName == null)
//                return this.transform
//
//            if (componentAttachPoints.ContainsKey(componentName))
//                return componentAttachPoints[componentName]
//
//            return null
//        }
//
//        private void StripMesh(GameObject go)
//        {
//            var meshRenderer = go.GetComponent<MeshRenderer>()
//            if (meshRenderer != null)
//                DestroyImmediate(meshRenderer)
//
//            var meshFilter = go.GetComponent<MeshFilter>()
//            if (meshFilter != null)
//                DestroyImmediate(meshFilter)
//        }
//
    fun loadComponents(renderModelName: String): Boolean {

        // Disable existing components (we will re-enable them if referenced by this new model).
        // Also strip mesh filter and renderer since these will get re-added if the new component needs them.
        lateinit var t: Transform
//        for (int childIndex = 0; childIndex < t.childCount; childIndex++)
//        {
//            var child = t.GetChild(childIndex)
//            child.gameObject.SetActive(false)
//            StripMesh(child.gameObject)
//        }

        // If no model specified, we're done; return success.
        if (renderModelName.isEmpty())
            return true

        val count = vrRenderModels.getComponentCount(renderModelName)
        if (count == 0)
            return false

        for (i in 0 until count) {

            val componentName = vrRenderModels.getComponentName(renderModelName, i) ?: continue

            // Create (or reuse) a child object for this component (some components are dynamic and don't have meshes).
            val t_ = findTransformByName(componentName)
            if (t_ != null) {
                t = t_
//                t.gameObject.SetActive(true)
                componentAttachPoints[componentName] = findTransformByName(localTransformName, t)
            } else {
                t = GameObject(componentName).transform.apply {
                    parent = transform
                }
//                t.gameObject.layer = gameObject.layer

                // Also create a child 'attach' object for attaching things.
                val attach = GameObject(localTransformName).transform
                attach.parent = t
                attach.localPosition = Vec3()
                attach.localRotation = Quat()
                attach.localScale = Vec3(1)
//                attach.gameObject.layer = gameObject.layer

                componentAttachPoints[componentName] = attach
            }

            // Reset transform.
            t.localPosition = Vec3()
            t.localRotation = Quat()
            t.localScale = Vec3(1)

            val componentRenderModelName = vrRenderModels.getComponentRenderModelName(renderModelName, componentName)
                    ?: continue

            // Check the cache or load into memory.
            var model = models[componentRenderModelName]
            if (model?.mesh == null) {
                if (verbose)
                    println("[SteamVR] Loading render model $componentRenderModelName")

                model = loadRenderModel(componentRenderModelName, renderModelName)
                if (model == null)
                    continue

                models[componentRenderModelName] = model
            }

//            t.gameObject.meshes += model.mesh!!
//            MeshRenderer newRenderer = t . gameObject . AddComponent < MeshRenderer >()
//            newRenderer.sharedMaterial = model.material
//            meshRenderers.Add(newRenderer)
            t.gameObject.models += model
        }

        return true
    }
//
//        SteamVR_Events.Action deviceConnectedAction, hideRenderModelsAction, modelSkinSettingsHaveChangedAction
//
//        RenderModels()
//        {
//            deviceConnectedAction = SteamVR_Events.DeviceConnectedAction(OnDeviceConnected)
//            hideRenderModelsAction = SteamVR_Events.HideRenderModelsAction(OnHideRenderModels)
//            modelSkinSettingsHaveChangedAction = SteamVR_Events.SystemAction(EVREventType.VREvent_ModelSkinSettingsHaveChanged, OnModelSkinSettingsHaveChanged)
//        }
//
//        void OnEnable ()
//        {
//            #if UNITY_EDITOR
//            if (!Application.isPlaying)
//                return
//            #endif
//            if (!string.IsNullOrEmpty(modelOverride)) {
//                Debug.Log("<b>[SteamVR]</b> " + modelOverrideWarning)
//                enabled = false
//                return
//            }
//
//            var system = OpenVR.System
//            if (system != null && system.IsTrackedDeviceConnected((uint) index)) {
//                UpdateModel()
//            }
//
//            deviceConnectedAction.enabled = true
//            hideRenderModelsAction.enabled = true
//            modelSkinSettingsHaveChangedAction.enabled = true
//        }
//
//        void OnDisable ()
//        {
//            #if UNITY_EDITOR
//            if (!Application.isPlaying)
//                return
//            #endif
//            deviceConnectedAction.enabled = false
//            hideRenderModelsAction.enabled = false
//            modelSkinSettingsHaveChangedAction.enabled = false
//        }
//
//        #if UNITY_EDITOR
//        Hashtable values
//        #endif
        fun update ()        {
//            #if UNITY_EDITOR
//            if (!Application.isPlaying) {
//                // See if anything has changed since this gets called whenever anything gets touched.
//                var fields = GetType().GetFields(System.Reflection.BindingFlags.Instance | System . Reflection . BindingFlags . Public)
//
//                bool modified = false
//
//                if (values == null) {
//                    modified = true
//                } else {
//                    foreach(var f in fields)
//                    {
//                        if (!values.Contains(f)) {
//                            modified = true
//                            break
//                        }
//
//                        var v0 = values[f]
//                        var v1 = f.GetValue(this)
//                        if (v1 != null) {
//                            if (!v1.Equals(v0)) {
//                                modified = true
//                                break
//                            }
//                        } else if (v0 != null) {
//                            modified = true
//                            break
//                        }
//                    }
//                }
//
//                if (modified) {
//                    if (renderModelName != modelOverride) {
//                        renderModelName = modelOverride
//                        SetModel(modelOverride)
//                    }
//
//                    values = new Hashtable ()
//                    foreach(var f in fields)
//                    values[f] = f.GetValue(this)
//                }
//
//                return // Do not update transforms (below) when not playing in Editor (to avoid keeping OpenVR running all the time).
//            }
//            #endif
            // Update component transforms dynamically.
            if (updateDynamically)
                updateComponents()
        }

    val nameCache = mutableMapOf<UUID, String>()


    fun updateComponents() {

        if (transform.childCount == 0)
            return

        val stack = stackPush()
        for (child in transform.children) {

            // Cache names since accessing an object's name allocate memory.
            val componentName = nameCache.getOrPut(child.instanceId) { child.name }

            val componentState = RenderModelComponentState.callocStack(stack)
            if (!vrRenderModels.getComponentStateForDevicePath(renderModelName, componentName, SteamVR_Input_Source.getHandle(inputSource), controllerModeState, componentState))
                continue

            child.localPosition = componentState.mTrackingToComponentRenderModel().position
            child.localRotation = componentState.mTrackingToComponentRenderModel().rotation

            var attach: Transform? = null
            for (childChild in child.children) {
                val childInstanceID = childChild.instanceId
                val childName = nameCache.getOrPut(childInstanceID) { childChild.name }

                if (childName == localTransformName)
                    attach = childChild
            }

            if (attach != null) {
                attach.position = transform.transformPoint(componentState.mTrackingToComponentLocal().position)
                attach.rotation = transform.rotation * componentState.mTrackingToComponentLocal().rotation

                initializedAttachPoints = true
            }

            val visible = componentState.properties has VRComponentProperty.IsVisible
//            if (visible != child.gameObject.activeSelf) {
//                child.gameObject.SetActive(visible)
//            }
        }
        stack.pop()
    }

    //
//        public void SetDeviceIndex(int newIndex)
//        {
//            this.index = (SteamVR_TrackedObject.EIndex) newIndex
//
//                    modelOverride = ""
//
//            if (enabled) {
//                UpdateModel()
//            }
//        }
//
//        public void SetInputSource(SteamVR_Input_Sources newInputSource)
//        {
//            inputSource = newInputSource
//        }
//
    fun sleep() {
//            #if !UNITY_METRO
//            //System.Threading.Thread.SpinWait(1); //faster napping
        Thread.sleep(1)
//            #endif
    }
//
//        /// <summary>
//        /// Helper function to handle the inconvenient fact that the packing for RenderModel_t is
//        /// different on Linux/OSX (4) than it is on Windows (8)
//        /// </summary>
//        /// <param name="pRenderModel">native pointer to the RenderModel_t</param>
//        /// <returns></returns>
//        private RenderModel_t MarshalRenderModel(System.IntPtr pRenderModel)
//        {
//            if ((System.Environment.OSVersion.Platform == System.PlatformID.MacOSX) ||
//                    (System.Environment.OSVersion.Platform == System.PlatformID.Unix)) {
//                var packedModel = (RenderModel_t_Packed) Marshal . PtrToStructure (pRenderModel, typeof(RenderModel_t_Packed))
//                RenderModel_t model = new RenderModel_t()
//                packedModel.Unpack(ref model)
//                return model
//            } else {
//                return (RenderModel_t) Marshal . PtrToStructure (pRenderModel, typeof(RenderModel_t))
//            }
//        }
//
//        /// <summary>
//        /// Helper function to handle the inconvenient fact that the packing for RenderModel_TextureMap_t is
//        /// different on Linux/OSX (4) than it is on Windows (8)
//        /// </summary>
//        /// <param name="pRenderModel">native pointer to the RenderModel_TextureMap_t</param>
//        /// <returns></returns>
//        private RenderModel_TextureMap_t MarshalRenderModel_TextureMap(System.IntPtr pRenderModel)
//        {
//            if ((System.Environment.OSVersion.Platform == System.PlatformID.MacOSX) ||
//                    (System.Environment.OSVersion.Platform == System.PlatformID.Unix)) {
//                var packedModel = (RenderModel_TextureMap_t_Packed) Marshal . PtrToStructure (pRenderModel, typeof(RenderModel_TextureMap_t_Packed))
//                RenderModel_TextureMap_t model = new RenderModel_TextureMap_t()
//                packedModel.Unpack(ref model)
//                return model
//            } else {
//                return (RenderModel_TextureMap_t) Marshal . PtrToStructure (pRenderModel, typeof(RenderModel_TextureMap_t))
//            }
//        }
}