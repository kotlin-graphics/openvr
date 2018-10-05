package lib

import ab.appBuffer
import glm_.buffer.adr
import org.lwjgl.openvr.OpenVR.IVRDriverManager
import org.lwjgl.openvr.VRDriverManager
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.system.MemoryUtil.memASCII

val IVRDriverManager.driverCount: Int
    get() = VRDriverManager.VRDriverManager_GetDriverCount()

/** @return the length of the number of bytes necessary to hold this string including the trailing null */
infix fun IVRDriverManager.getDriverName(driver: DriverId): String {
    val bufferSize = VRDriverManager.nVRDriverManager_GetDriverName(driver, NULL, 0)
    val value = appBuffer.buffer(bufferSize)
    val result = VRDriverManager.nVRDriverManager_GetDriverName(driver, value.adr, bufferSize)
    return memASCII(value, result - 1)
}

/**
 * Returns the property container handle for the specified driver.
 *
 * @param driverName the driver name
 */
infix fun IVRDriverManager.getDriverHandle(driverName: String): DriverHandle {
    val driverNameEncoded = appBuffer.bufferOfAscii(driverName)
    return VRDriverManager.nVRDriverManager_GetDriverHandle(driverNameEncoded.adr)
}