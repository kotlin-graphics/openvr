package main;

import com.sun.jna.ptr.IntByReference;
import openvr.*;

import static openvr.vr.vrInit;
import static openvr.IvrcompositorKt.getIVRCompositor_Version;

/**
 * Created by GBarbieri on 06.02.2017.
 */
public class Test {

    public static void main(String[] argvs) {

        EVRInitError_ByReference error = new EVRInitError_ByReference();

        IVRSystem hmd = vrInit(error, EVRApplicationType.Scene);

        System.out.println(error.value.toString());

        IntByReference w = new IntByReference();
        IntByReference h = new IntByReference();
        hmd.getRecommendedRenderTargetSize(w, h);
        System.out.println("resolution: "+w.getValue()+" x "+h.getValue());

        IVRCompositor compositor = new IVRCompositor(vr.VR_GetGenericInterface(getIVRCompositor_Version(), error));

        TrackedDevicePose_t a = new TrackedDevicePose_t();
    }
}
