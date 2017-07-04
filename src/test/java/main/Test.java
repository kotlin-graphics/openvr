//package main;
//
//import com.sun.jna.ptr.IntByReference;
//import openvr.*;
//
//import static openvr.vr.vrCompositor;
//import static openvr.vr.vrInit;
//import static openvr.vr.vrShutdown;
////import static openvr.IvrCompositorKt.getIVRCompositor_Version;
//
///**
// * Created by GBarbieri on 06.02.2017.
// */
//public class Test {
//
//    public static void main(String[] argvs) {
//
//        EVRInitError_ByReference error = new EVRInitError_ByReference();
//
//        IVRSystem hmd = vrInit(error, EVRApplicationType.Scene);
//
//        System.out.println(error.value.toString());
//
//        IntByReference w = new IntByReference();
//        IntByReference h = new IntByReference();
//        hmd.getRecommendedRenderTargetSize(w, h);
//        System.out.println("resolution: "+w.getValue()+" x "+h.getValue());
//        assert(w.getValue() > 0 && h.getValue() > 0);
//
////        IVRCompositor compositor = new IVRCompositor(vr.VR_GetGenericInterface(getIVRCompositor_Version(), error));
////        IVRCompositor compositor = new IVRCompositor(vr.VR_GetGenericInterface("ciao", error));
//        assert (vrCompositor() != null);
//
//        TrackedDevicePose_t a = new TrackedDevicePose_t();
//
//        vrShutdown();
//    }
//}
