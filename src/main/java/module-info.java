module com.github.kotlin_graphics.openvr {

    requires kotlin.stdlib;

    requires org.lwjgl.openvr;

    requires com.github.kotlin_graphics.kool;
    requires com.github.kotlin_graphics.glm;
    requires com.github.kotlin_graphics.gli;
//    requires com.github.kotlin_graphics.gln;
    requires com.github.kotlin_graphics.uno_core;
    requires org.lwjgl.vulkan;
    requires java.desktop;
    requires klaxon;
    requires kotlinx.coroutines.core;

    exports openvr;
    exports openvr.lib;
    exports openvr.plugin2;
    exports openvr.steamVR.input;
    exports openvr.steamVR.interactionSystem.core.scripts;
    exports openvr.steamVR_Input;
    exports openvr.steamVR_Input.actionSetClasses;
    exports openvr.unity;
}