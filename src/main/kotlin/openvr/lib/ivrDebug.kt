package openvr.lib

import kool.adr
import kool.asciiAdr
import kool.set
import org.lwjgl.openvr.VRDebug
import org.lwjgl.openvr.VRDebug.*
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.system.MemoryUtil.memASCII

object vrDebug : vrInterface {

    enum class Error {
        Success, BadParameter;

        val i = ordinal

        companion object {
            infix fun of(i: Int) = Error.values().first { it.i == i }
        }
    }

    val pError = MemoryUtil.memCallocInt(1)

    var error: Error
        get() = Error of pError[0]
        set(value) {
            pError[0] = value.i
        }


    /** Create a vr profiler discrete event (point)
     * The event will be associated with the message provided in pchMessage, and the current
     * time will be used as the event timestamp. */
    fun emitVrProfilerEvent(message: String): Error =
            Error of stak.asciiAdr(message) { nVRDebug_EmitVrProfilerEvent(it) }

    /** Create an vr profiler duration event (line)
     * The current time will be used as the timestamp for the start of the line.
     * On success, pHandleOut will contain a handle valid for terminating this event. */
    fun beginVrProfilerEvent(): VrProfilerEventHandle =
            stak.longAdr { pError[0] = nVRDebug_BeginVrProfilerEvent(it) }

    /** Terminate a vr profiler event
     * The event associated with hHandle will be considered completed when this method is called.
     * The current time will be used assocaited to the termination time of the event, and
     * pchMessage will be used as the event title. */
    fun finishVrProfilerEvent(handle: VrProfilerEventHandle, message: String): Error =
            Error of stak.asciiAdr(message) { nVRDebug_FinishVrProfilerEvent(handle, it) }

    /** Sends a request to the driver for the specified device and returns the response. The maximum response size is 32k,
     * but this method can be called with a smaller buffer. If the response exceeds the size of the buffer, it is truncated.
     * The size of the response including its terminating null is returned. */
    fun driverDebugRequest(deviceIndex: TrackedDeviceIndex, request: String): String =
            stak { s ->
                val pRequest = s.asciiAdr(request)
                val responseSize = nVRDebug_DriverDebugRequest(deviceIndex, pRequest, NULL, 0)
                val pResponse = s.nmalloc(1, responseSize)
                nVRDebug_DriverDebugRequest(deviceIndex, pRequest, pResponse, responseSize)
                memASCII(pRequest, responseSize - 1)
            }

    override val version: String
        get() = "IVRDebug_001"
}

/** Handle for vr profiler events */
typealias VrProfilerEventHandle = Long