package openvr.lib

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import java.util.*

// ivrdrivermanager.h =================================================================================================================================================

open class IVRDriverManager : Structure {

    val driverCount get() = GetDriverCount!!()

    @JvmField var GetDriverCount: GetDriverCount_callback? = null

    interface GetDriverCount_callback : Callback {
        operator fun invoke(): Int
    }

    /** Returns the length of the number of bytes necessary to hold this string including the trailing null.    */
    fun getDriverName(driver: DriverId, value: String, bufferSize: Int) = GetDriverName!!(driver, value, bufferSize)

    @JvmField var GetDriverName: GetDriverName_callback? = null

    interface GetDriverName_callback : Callback {
        operator fun invoke(nDriver: DriverId, pchValue: String, unBufferSize: Int): Int
    }

    constructor()

    override fun getFieldOrder(): List<String> = Arrays.asList("GetDriverCount", "GetDriverName")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRDriverManager(), Structure.ByReference
    class ByValue : IVRDriverManager(), Structure.ByValue
}

val IVRDriverManager_Version = "IVRDriverManager_001"