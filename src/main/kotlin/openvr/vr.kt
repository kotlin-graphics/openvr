package openvr

object vr {

    @JvmField val maxTrackedDeviceCount = openvr.k_unMaxTrackedDeviceCount

    fun init(error: EVRInitError_ByReference, applicationType: EVRApplicationType) = openvr.vrInit(error, applicationType)

    fun getVRInitErrorAsEnglishDescription(error: EVRInitError) = openvr.vrGetVRInitErrorAsEnglishDescription(error)

    fun getGenericInterface(pchInterfaceVersion: String, peError: EVRInitError_ByReference) = openvr.vrGetGenericInterface(pchInterfaceVersion, peError)

    @JvmField val IVRRenderModels_Version = openvr.IVRRenderModels_Version

    val compositor get() = openvr.vrCompositor()

    @JvmField val trackedDeviceIndex_Hmd = openvr.k_unTrackedDeviceIndex_Hmd

    val renderModels get() = openvr.vrRenderModels()

    fun shutdown() = openvr.vrShutdown()
}