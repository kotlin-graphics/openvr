package openvr

import com.sun.jna.ptr.ByReference
import glm_.L

//package openvr
//
//object vr {
//
//    @JvmField val maxTrackedDeviceCount = openvr.lib.maxTrackedDeviceCount
//
//    fun init(error: EVRInitError_ByReference, applicationType: EVRApplicationType) = openvr.lib.vrInit(error, applicationType)
//
//    fun getVRInitErrorAsEnglishDescription(error: EVRInitError) = openvr.lib.vrGetVRInitErrorAsEnglishDescription(error)
//
//    fun getGenericInterface(pchInterfaceVersion: String, peError: EVRInitError_ByReference) = openvr.lib.vrGetGenericInterface(pchInterfaceVersion, peError)
//
//    @JvmField val IVRRenderModels_Version = openvr.lib.getIVRRenderModels_Version
//
//    val compositor get() = openvr.vrCompositor()
//
//    @JvmField val trackedDeviceIndex_Hmd = openvr.lib.getK_unTrackedDeviceIndex_Hmd
//
//    val renderModels get() = openvr.vrRenderModels()
//
//    fun shutdown() = openvr.lib.vrShutdown()
//}


class StringByReference : ByReference {

    var value: String
        get() = pointer.getString(0)
        private set(str) {
            pointer.setString(0, str)
        }

    @JvmOverloads constructor(size: Int = 0) : super(if (size < 4) 4 else size) {
        pointer.clear(if (size < 4) 4 else size.L)
    }

    constructor(str: String) : super(if (str.length < 4) 4 else str.length + 1) {
        value = str
    }
}