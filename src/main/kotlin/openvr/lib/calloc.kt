package openvr.lib

import org.lwjgl.openvr.VRControllerState
import org.lwjgl.openvr.TrackedDevicePose

fun VRControllerState() = VRControllerState.calloc()
fun TrackedDevicePose() = TrackedDevicePose.calloc()