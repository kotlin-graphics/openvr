package lib

import kool.adr
import org.lwjgl.openvr.VRResources.nVRResources_GetResourceFullPath
import org.lwjgl.openvr.VRResources.nVRResources_LoadSharedResource
import org.lwjgl.system.MemoryStack.stackGet
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.system.MemoryUtil.memASCII


object vrResources : vrInterface {

    /**
     * Loads the specified resource into the provided buffer if large enough.
     *
     * @return the size in bytes of the buffer required to hold the specified resource
     */
    infix fun loadSharedResource(resourceName: String): String =
            stackGet().run {
                val resourceNameEncoded = addressOfAscii(resourceName)
                val bufferLen = nVRResources_LoadSharedResource(resourceNameEncoded, NULL, 0)
                val buffer = malloc(bufferLen)
                val result = nVRResources_LoadSharedResource(resourceNameEncoded, buffer.adr, bufferLen)
                memASCII(buffer, result - 1)
            }

    /**
     * Provides the full path to the specified resource. Resource names can include named directories for drivers and other things, and this resolves all of
     * those and returns the actual physical path. {@code resourceTypeDirectory} is the subdirectory of resources to look in.
     */
    fun getResourceFullPath(resourceName: String, resourceTypeDirectory: String): String =
            stackGet().run {
                val resourceNameEncoded = addressOfAscii(resourceName)
                val resourceTypeDirectoryEncoded = addressOfAscii(resourceTypeDirectory)
                val bufferLen = nVRResources_GetResourceFullPath(resourceNameEncoded, resourceTypeDirectoryEncoded, NULL, 0)
                val pathBuffer = malloc(bufferLen)
                val result = nVRResources_GetResourceFullPath(resourceNameEncoded, resourceTypeDirectoryEncoded, pathBuffer.adr, bufferLen)
                memASCII(pathBuffer, result - 1)
            }

    override val version: String
        get() = "IVRResources_001"
}