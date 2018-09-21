package vr_

import ab.appBuffer
import glm_.buffer.adr
import glm_.vec2.Vec2i
import openvr.lib.EVREye
import org.lwjgl.openvr.OpenVR.IVRExtendedDisplay
import org.lwjgl.openvr.VRExtendedDisplay
import org.lwjgl.system.MemoryUtil
import java.nio.IntBuffer

/** Size and position that the window needs to be on the VR display. */
fun IVRExtendedDisplay.getWindowBounds(pos: Vec2i, size: Vec2i) {
    val x = appBuffer.int
    val y = appBuffer.int
    val width = appBuffer.int
    val height = appBuffer.int
    VRExtendedDisplay.nVRExtendedDisplay_GetWindowBounds(x, y, width, height)
    pos.put(MemoryUtil.memGetInt(x), MemoryUtil.memGetInt(y))
    size.put(MemoryUtil.memGetInt(width), MemoryUtil.memGetInt(height))
}

/** Gets the viewport in the frame buffer to draw the output of the distortion into. */
fun IVRExtendedDisplay.getEyeOutputViewport(eye: EVREye, pos: Vec2i, size: Vec2i) {
    val x = appBuffer.int
    val y = appBuffer.int
    val width = appBuffer.int
    val height = appBuffer.int
    VRExtendedDisplay.nVRExtendedDisplay_GetEyeOutputViewport(eye.i, x, y, width, height)
    pos.put(MemoryUtil.memGetInt(x), MemoryUtil.memGetInt(y))
    size.put(MemoryUtil.memGetInt(width), MemoryUtil.memGetInt(height))
}

/**
 * <h3>D3D10/11 Only</h3>
 *
 * <p>Returns the adapter index and output index that the user should pass into {@code EnumAdapters} and {@code EnumOutputs} to create the device and swap
 * chain in DX10 and DX11. If an error occurs both indices will be set to -1.</p>
 */
fun IVRExtendedDisplay.getDXGIOutputInfo(adapterIndex: IntBuffer, adapterOutputIndex: IntBuffer) {
    VRExtendedDisplay.nVRExtendedDisplay_GetDXGIOutputInfo(adapterIndex.adr, adapterOutputIndex.adr)
}