package openvr.lib

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import java.util.*

// ivrdrivermanager.h =================================================================================================================================================

open class IVRDriverManager : Structure {

    fun getDriverCount() = GetDriverCount!!.invoke()

    @JvmField var GetDriverCount: GetDriverCount_callback? = null

    interface GetDriverCount_callback : Callback {
        fun invoke(): Int
    }

    /** Returns the length of the number of bytes necessary to hold this string including the trailing null.    */
    fun getDriverName(nDriver: DriverId_t, pchValue: String, unBufferSize: Int) = GetDriverName!!.invoke(nDriver, pchValue, unBufferSize)

    @JvmField var GetDriverName: GetDriverName_callback? = null

    interface GetDriverName_callback : Callback {
        fun invoke(nDriver: DriverId_t, pchValue: String, unBufferSize: Int): Int
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