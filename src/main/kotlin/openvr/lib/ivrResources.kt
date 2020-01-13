package openvr.lib

import kool.asciiAdr
import org.lwjgl.openvr.VRResources.nVRResources_GetResourceFullPath
import org.lwjgl.openvr.VRResources.nVRResources_LoadSharedResource
import org.lwjgl.system.MemoryUtil.NULL


object vrResources : vrInterface {

    /**
     * Loads the specified resource into the provided buffer if large enough.
     *
     * @return the size in bytes of the buffer required to hold the specified resource
     */
    infix fun loadSharedResource(resourceName: String): String =
            stak { s ->
                val resourceNameEncoded = s.asciiAdr(resourceName)
                val bufferLen = nVRResources_LoadSharedResource(resourceNameEncoded, NULL, 0)
                s.asciiAdr(bufferLen) {
                    nVRResources_LoadSharedResource(resourceNameEncoded, it, bufferLen)
                }
            }

    /**
     * Provides the full path to the specified resource. Resource names can include named directories for drivers and other things, and this resolves all of
     * those and returns the actual physical path. {@code resourceTypeDirectory} is the subdirectory of resources to look in.
     */
    fun getResourceFullPath(resourceName: String, resourceTypeDirectory: String): String =
            stak { s ->
                val resourceNameEncoded = s.asciiAdr(resourceName)
                val resourceTypeDirectoryEncoded = s.asciiAdr(resourceTypeDirectory)
                val bufferLen = nVRResources_GetResourceFullPath(resourceNameEncoded, resourceTypeDirectoryEncoded, NULL, 0)
                s.asciiAdr(bufferLen) {
                    nVRResources_GetResourceFullPath(resourceNameEncoded, resourceTypeDirectoryEncoded, it, bufferLen)
                }
            }

    override val version: String
        get() = "IVRResources_001"
}