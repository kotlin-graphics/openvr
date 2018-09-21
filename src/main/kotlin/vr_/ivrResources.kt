package vr_

import ab.appBuffer
import glm_.buffer.adr
import org.lwjgl.openvr.OpenVR.IVRResources
import org.lwjgl.openvr.VRResources
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.system.MemoryUtil.memASCII


/**
 * Loads the specified resource into the provided buffer if large enough.
 *
 * @return the size in bytes of the buffer required to hold the specified resource
 */
infix fun IVRResources.loadSharedResource(resourceName: String): String {
    val resourceNameEncoded = appBuffer.bufferOfAscii(resourceName)
    val bufferLen = VRResources.nVRResources_LoadSharedResource(resourceNameEncoded.adr, NULL, 0)
    val buffer = appBuffer.buffer(bufferLen)
    val result = VRResources.nVRResources_LoadSharedResource(resourceNameEncoded.adr, buffer.adr, bufferLen)
    return memASCII(buffer, result - 1)
}

/**
 * Provides the full path to the specified resource. Resource names can include named directories for drivers and other things, and this resolves all of
 * those and returns the actual physical path. {@code resourceTypeDirectory} is the subdirectory of resources to look in.
 */
fun IVRResources.getResourceFullPath(resourceName: String, resourceTypeDirectory: String): String {
    val resourceNameEncoded = appBuffer.bufferOfAscii(resourceName)
    val resourceTypeDirectoryEncoded = appBuffer.bufferOfAscii(resourceTypeDirectory)
    val bufferLen = VRResources.nVRResources_GetResourceFullPath(resourceNameEncoded.adr, resourceTypeDirectoryEncoded.adr, NULL, 0)
    val pathBuffer = appBuffer.buffer(bufferLen)
    val result = VRResources.nVRResources_GetResourceFullPath(resourceNameEncoded.adr, resourceTypeDirectoryEncoded.adr, pathBuffer.adr, bufferLen)
    return memASCII(pathBuffer, result - 1)
}