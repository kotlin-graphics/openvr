package openvr.plugin2

import java.util.*

open class Component : Object() {

    lateinit var gameObject: GameObject
    val transform = Transform()
}

open class Object {

    open var name = ""

    val instanceId = UUID.randomUUID()
}