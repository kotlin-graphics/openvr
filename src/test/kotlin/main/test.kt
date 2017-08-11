import com.sun.jna.ptr.FloatByReference
import com.sun.jna.ptr.IntByReference
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import openvr.*
import openvr.lib.*


/**
 * Created by GBarbieri on 25.10.2016.
 */


class Test : StringSpec() {

    init {

        "simple test" {

            val error = EVRInitError_ByReference()

            val hmd = vrInit(error, EVRApplicationType.Scene)
//            vr.compositor shouldNotBe null

            error.value shouldBe EVRInitError.None

            if (hmd == null) throw Error()

            val w = IntByReference(0)
            val h = IntByReference(0)
            hmd.getRecommendedRenderTargetSize(w, h)
            (w.value > 0 && h.value > 0) shouldBe true


            val m = hmd.getProjectionMatrix(EVREye.Left, .1f, 10f)
            /** 0.75787073  0           -0.05657852   0
             *  0           0.6820195   -0.0013340205 0
             *  0           0           -1.0101011    -0.10101011
             *  0           0           -1            0             */
            (m[0, 0] != 0f && m[1, 0] == 0f && m[2, 0] != 0f && m[3, 0] == 0f &&
                    m[0, 1] == 0f && m[1, 1] != 0f && m[2, 1] != 0f && m[3, 1] == 0f &&
                    m[0, 2] == 0f && m[1, 2] == 0f && m[2, 2] != 0f && m[3, 2] != 0f &&
                    m[0, 3] == 0f && m[1, 3] == 0f && m[2, 3] != 0f && m[3, 3] == 0f) shouldBe true


            val left_ = FloatByReference()
            val right = FloatByReference()
            val top = FloatByReference()
            val bottom = FloatByReference()
            hmd.getProjectionRaw(EVREye.Left, left_, right, top, bottom)
//      -1.3941408, 1.2448317, -1.4681898, 1.4642779
            (left_.value < 0 && right.value > 0 && top.value < 0 && bottom.value >= 0) shouldBe true

            val overlayHandle = VROverlayHandle_t_ByReference()
            val overlayThumbnailHandle = VROverlayHandle_t_ByReference()

            vrOverlay?.let {
                val name = "systemOverlay"
                val key = "sample.$name"
                val overlayError = it.createDashboardOverlay(key, name, overlayHandle, overlayThumbnailHandle)
                overlayError shouldBe EVROverlayError.None
            }

            println(vrOverlay!!.setOverlayInputMethod(overlayHandle.value, VROverlayInputMethod.Mouse))

            val listener = Listener(hmd)

            val start = System.nanoTime()
            while (System.nanoTime() - start < 15e9)
                listener.poll()

//            if(vr.compositor == null) throw Error()

//            val dc = hmd.computeDistortion(EVREye.Left, .5f, .5f)
//            //
//            assert(dc.rfRed[0] in 0..1 && dc.rfRed[1] in 0..1 && dc.rfGreen[0] in 0..1 && dc.rfGreen[1] in 0..1 && dc.rfBlue[0] in 0..1 && dc.rfBlue[1] in 0..1)
//
//
//            val m43 = hmd.getEyeToHeadTransform(EVREye.Left)
//            /** 1   0   0   -0.03045
//             *  0   1   0   0
//             *  0   0   1   0.015         */
//            assert(m43[0] == 1f && m43[1] == 0f && m43[2] == 0f && m43[3] < 0
//                    && m43[4] == 0f && m43[5] == 1f && m43[6] == 0f && m43[7] == 0f
//                    && m43[8] == 0f && m43[9] == 0f && m43[10] == 1f && m43[11] > 0)
//            println()
//    println("IsDisplayOnDesktop " + IVRSystem.IsDisplayOnDesktop())
//    040 78880

            vrShutdown()
        }
    }
}

class Listener(hmd: IVRSystem) : SteamVRListener(hmd) {
    override fun trackedDeviceActivated(left: Boolean) = println("activated $left")
    override fun trackedDeviceDeactivated(left: Boolean) = println("deactivated $left")
    override fun trackedDeviceRoleChanged(left: Boolean) = println("role changed $left")
    override fun trackedDeviceUpdated(left: Boolean) = println("updated $left")
    override fun buttonPress(left: Boolean, button: EVRButtonId) {
        println("pressed $button")
    }

    override fun touchPadMove() {
        println("touchPadMove")
    }

    override fun mouseMove() {
        println("mouseMove")
    }

    override fun scroll() {
        println("scroll")
    }
}