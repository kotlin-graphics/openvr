package openvr.lib

import kool.adr
import kool.stak
import org.lwjgl.openvr.VRDriverManager.*
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.system.MemoryUtil.memASCII

object vrDriverManager : vrInterface {

    val driverCount: Int
        get() = VRDriverManager_GetDriverCount()

    /** @return the length of the number of bytes necessary to hold this string including the trailing null */
    infix fun getDriverName(driver: DriverId): String =
            stak {
                val bufferSize = nVRDriverManager_GetDriverName(driver, NULL, 0)
                val value = it.malloc(bufferSize)
                val result = nVRDriverManager_GetDriverName(driver, value.adr, bufferSize)
                memASCII(value, result - 1)
            }

    /**
     * Returns the property container handle for the specified driver.
     *
     * @param driverName the driver name
     */
    infix fun getDriverHandle(driverName: String): DriverHandle =
            stak { nVRDriverManager_GetDriverHandle(it.addressOfAscii(driverName)) }

    override val version: String
        get() = "IVRDriverManager_001"
}