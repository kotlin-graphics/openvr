package openvr.assets.steamVR.script

typealias UnityAction0 = () -> Unit
typealias UnityAction1<T> = (T) -> Unit
typealias UnityAction2<T0, T1> = (T0, T1) -> Unit
typealias UnityAction3<T0, T1, T2> = (T0, T1, T2) -> Unit

abstract class UnityEvent0 {

    val calls = ArrayList<() -> Unit>()
}

abstract class UnityEvent1<T> {

    val calls = ArrayList<(T) -> Unit>()
}

abstract class UnityEvent2<T0, T1> {

    val calls = ArrayList<(T0, T1) -> Unit>()
}

abstract class UnityEvent3<T0, T1, T2> {

    val calls = ArrayList<(T0, T1, T2) -> Unit>()
}