package openvr.lib

import org.lwjgl.openvr.VR

interface vrInterface {

    val version: String

    /** Returns whether the interface of the specified version exists.     */
    val isInterfaceVersionValid: Boolean
        get() = stak.asciiAdr("FnTable:$version") { VR.nVR_IsInterfaceVersionValid(it) }
}