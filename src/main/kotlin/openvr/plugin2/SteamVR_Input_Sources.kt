package openvr.plugin2

enum class SteamVR_Input_Sources(val description: String) {

    Any("/unrestricted"), //todo: check to see if this gets exported: k_ulInvalidInputHandle

    LeftHand("/user/hand/left"),

    RightHand("/user/hand/right"),

    LeftFoot("/user/foot/left"),

    RightFoot("/user/foot/right"),

    LeftShoulder("/user/shoulder/left"),

    RightShoulder("/user/shoulder/right"),

    Waist("/user/waist"),

    Chest("/user/chest"),

    Head("/user/head"),

    Gamepad("/user/gamepad"),

    Camera("/user/camera"),

    Keyboard("/user/keyboard")
}