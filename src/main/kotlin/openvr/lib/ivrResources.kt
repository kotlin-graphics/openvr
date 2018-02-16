package openvr.lib

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import java.util.*

// ivrresources.h =================================================================================================================================================

open class IVRResources : Structure {

    // ------------------------------------
    // Shared Resource Methods
    // ------------------------------------

    /** Loads the specified resource into the provided buffer if large enough.
     *  Returns the size in bytes of the buffer required to hold the specified resource. */
    fun loadSharedResource(resourceName: String, buffer: String, bufferSize: Int) = LoadSharedResource!!(resourceName, buffer, bufferSize)

    @JvmField var LoadSharedResource: LoadSharedResource_callback? = null

    interface LoadSharedResource_callback : Callback {
        operator fun invoke(pchResourceName: String, pchBuffer: String, unBufferLen: Int): Int
    }

    /** Provides the full path to the specified resource. Resource names can include named directories for drivers and other things, and this resolves all of
     *  those and returns the actual physical path.
     *  resourceTypeDirectory is the subdirectory of resources to look in. */
    fun getResourceFullPath(resourceName: String, resourceTypeDirectory: String, pathBuffer: String, bufferLen: Int) = GetResourceFullPath!!(resourceName, resourceTypeDirectory, pathBuffer, bufferLen)

    @JvmField var GetResourceFullPath: GetResourceFullPath_callback? = null

    interface GetResourceFullPath_callback : Callback {
        operator fun invoke(pchResourceName: String, pchResourceTypeDirectory: String, pchPathBuffer: String, unBufferLen: Int): Int
    }

    constructor()

    override fun getFieldOrder(): List<String> = Arrays.asList("LoadSharedResource", "GetResourceFullPath")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRScreenshots(), Structure.ByReference
    class ByValue : IVRScreenshots(), Structure.ByValue
}

val IVRResources_Version = "IVRResources_001"