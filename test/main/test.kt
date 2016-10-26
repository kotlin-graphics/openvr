package main

import com.sun.jna.Native
import com.sun.jna.NativeLibrary
import com.sun.jna.ptr.IntByReference
import java.nio.ByteBuffer
import main.VR_Init
import main.OpenVR

/**
 * Created by GBarbieri on 25.10.2016.
 */


fun main(args: Array<String>) {

    val OpenVR = OpenVR()
    val error = IntByReference()
    val a = VR_Init(error, EVRApplicationType.VRApplication_Scene.i)
    println(error.value)
    val w = IntByReference(0)
    val h = IntByReference(0)
    a.GetRecommendedRenderTargetSize!!.apply(w, h)
    println("w: ${w.value}, h: ${h.value}")
//    040 78880
}