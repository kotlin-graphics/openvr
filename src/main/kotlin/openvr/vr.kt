//package openvr
//
//object vr {
//
//    @JvmField val maxTrackedDeviceCount = openvr.lib.getK_unMaxTrackedDeviceCount
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