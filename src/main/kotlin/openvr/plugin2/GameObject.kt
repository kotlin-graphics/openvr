package openvr.plugin2


class GameObject(override var name: String) : Component() {

    init {
        transform.gameObject = this
    }

//    val meshes = ArrayList<Mesh>()
    val models = ArrayList<SteamVR_RenderModel.RenderModel>()
}