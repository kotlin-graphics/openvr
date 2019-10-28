package openvr.assets.steamVR.input

class EventClass<T>(val list: ArrayList<T>) {

    operator fun plusAssign(event: T) {
        list += event
    }

    operator fun minusAssign(event: T) {
        list -= event
    }
}