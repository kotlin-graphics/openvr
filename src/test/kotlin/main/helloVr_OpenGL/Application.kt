package main.helloVr_OpenGL

import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec4.Vec4
import gln.clear.glClearColorBuffer
import gln.clear.glClearDepthBuffer
import gln.debug.glDebugMessageCallback
import lib.*
import lib.VREventType
import lib.vrRenderModels.freeNative
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.KHRDebug.GL_DEBUG_OUTPUT_SYNCHRONOUS
import org.lwjgl.opengl.KHRDebug.glDebugMessageControl
import org.lwjgl.openvr.TrackedDevicePose
import org.lwjgl.openvr.VREvent
import org.lwjgl.system.MemoryUtil.NULL
import uno.glfw.GlfwWindow.CursorStatus
import uno.glfw.Key
import uno.glfw.glfw
import uno.glfw.windowHint.Profile
import java.nio.IntBuffer
import java.nio.file.Paths
import kotlin.reflect.KMutableProperty0
import lib.TrackedDeviceClass as TDC
import org.lwjgl.opengl.KHRDebug.glDebugMessageCallback as _

fun main(args: Array<String>) {

    if (System.getenv("TRAVIS") == "true") // for travis
        return

    Application().apply {
        runMainLoop()
        shutdown()
    }
}

const val debugOpenGL = true
const val vBlank = true
const val glFinishHack = true

// Loading the SteamVR Runtime
val hmd = vrSystem
lateinit var scene: Scene

lateinit var eyeDesc: Array<FrameBufferDesc>

enum class Hand { Left, Right }

val clearColor = Vec4(0, 0, 0, 1)

var showCubes = true

lateinit var projection: Array<Mat4>
lateinit var eyePos: Array<Mat4>

var hmdPose = Mat4()

var trackedControllerCount = 0
var trackedControllerCount_Last = -1

class ControllerInfo {
    var source = vr.invalidInputValueHandle
    var actionPose = vr.invalidActionHandle
    var actionHaptic = vr.invalidActionHandle
    val rmat4Pose = Mat4()
    var renderModel: CGLRenderModel? = null
    var renderModelName = ""
    var showController = false
}

val rHand = Array(2) { ControllerInfo() }
operator fun Array<ControllerInfo>.get(hand: Hand) = get(hand.ordinal)


class Application {

    val trackedDevicePose = TrackedDevicePose.calloc(vr.maxTrackedDeviceCount)

    val rmat4DevicePose = Array(vr.maxTrackedDeviceCount) { Mat4() }

    var actionHideCubes = vr.invalidActionHandle
    var actionHideThisController = vr.invalidActionHandle
    var actionTriggerHaptic = vr.invalidActionHandle
    var actionAnalongInput = vr.invalidActionHandle

    var actionsetDemo = vr.invalidActionSetHandle

    init {
        with(glfw) {
            init()
            windowHint {
                context.version = "4.1"
                profile = Profile.core
                debug = debugOpenGL
            }
        }

        // Loading the SteamVR Runtime
        val err = vr.init()

        if (err != VRInitError.None)
            throw Error("Unable to init VR runtime: ${err.asEnglishDescription}")

        if (!vrCompositor.isInterfaceVersionValid)
            throw Error("Application::init - Failed to initialize VR Compositor!")

        vrInput.apply {

            setActionManifestPath(javaClass.classLoader.getResource("hellovr_actions.json"))

            actionHideCubes = getActionHandle("/actions/demo/in/HideCubes")
            actionHideThisController = getActionHandle("/actions/demo/in/HideThisController")
            actionTriggerHaptic = getActionHandle("/actions/demo/in/TriggerHaptic")
            actionAnalongInput = getActionHandle("/actions/demo/in/AnalogInput")

            actionsetDemo = getActionSetHandle("/actions/demo")

            rHand[Hand.Left].apply {
                actionHaptic = getActionHandle("/actions/demo/out/Haptic_Left")
                source = getInputSourceHandle("/user/hand/left")
                actionPose = getActionHandle("/actions/demo/in/Hand_Left")
            }
            rHand[Hand.Right].apply {
                actionHaptic = getActionHandle("/actions/demo/out/Haptic_Right")
                source = getInputSourceHandle("/user/hand/right")
                actionPose = getActionHandle("/actions/demo/in/Hand_Right")
            }
        }

        val nearClip = 0.1f
        val farClip = 30f

        // Gets a Matrix Projection Eye with respect to nEye.
        projection = Array(2) { hmd.getProjectionMatrix(VREye of it, nearClip, farClip) }

        // Purpose: Gets an HMDMatrixPoseEye with respect to nEye.
        eyePos = Array(2) { hmd.getEyeToHeadTransform(VREye of it).inverse() }
    }

    // GLFW bookkeeping
    val companionWindow = CompanionWindow() // gl resources

    val window
        get() = companionWindow.window

    init {
        if (debugOpenGL) {
            glDebugMessageCallback { source, type, id, severity, message ->
                println("$id: $type of $severity severity, raised from $source: $message")
            }
            glDebugMessageControl(GL_DONT_CARE, GL_DONT_CARE, GL_DONT_CARE, null as IntBuffer?, true)
            glEnable(GL_DEBUG_OUTPUT_SYNCHRONOUS)
        }
        scene = Scene() // gl resources
        eyeDesc = Array(2) { FrameBufferDesc(hmd.recommendedRenderTargetSize) }
    }

    var validPoseCount = 0
    var validPoseCount_Last = 0
    val analogValue = Vec2()

    /** what classes we saw poses for this frame */
    var strPoseClasses = ""
    /** for each device, a character representing its class */
    val devClassChar = CharArray(vr.maxTrackedDeviceCount)

    val renderModels = ArrayList<CGLRenderModel>()

    fun runMainLoop() {

        window.cursorStatus = CursorStatus.Hidden

        window.loop {
            handleInput()
            renderFrame()
        }
    }

    var hapticDevice: VRInputValueHandle = NULL
    var hideDevice: VRInputValueHandle = NULL

    fun handleInput() {

        window.apply {
            if (isPressed(Key.ESCAPE) || isPressed(Key.Q))
                shouldClose = true
            else if (isPressed(Key.C))
                showCubes = !showCubes
        }

        // Process SteamVR events
        hmd.pollingEvents { event -> event.process() }

        /*  Process SteamVR action state
            UpdateActionState is called each frame to update the state of the actions themselves. The application
            controls which action sets are active with the provided array of VRActiveActionSet_t structs. */
        val actionSet = vr.VRActiveActionSet().apply { actionSet = actionsetDemo }
        vrInput updateActionState actionSet

        showCubes = !getDigitalActionState(actionHideCubes)

        if (getDigitalActionRisingEdge(actionTriggerHaptic, ::hapticDevice)) {
            if (hapticDevice == rHand[Hand.Left].source)
                vrInput.triggerHapticVibrationAction(rHand[Hand.Left].actionHaptic, 0f, 1f, 4f, 1f, vr.invalidInputValueHandle)
            if (hapticDevice == rHand[Hand.Right].source)
                vrInput.triggerHapticVibrationAction(rHand[Hand.Right].actionHaptic, 0f, 1f, 4f, 1f, vr.invalidInputValueHandle)
        }

        val analogData = vr.InputAnalogActionData()
        if (vrInput.getAnalogActionData(actionAnalongInput, analogData, vr.invalidInputValueHandle) == vrInput.Error.None && analogData.active)
            analogValue.put(analogData.x, analogData.y)

        rHand[Hand.Left].showController = true
        rHand[Hand.Right].showController = true

        if (getDigitalActionState(actionHideThisController, ::hideDevice)) {
            if (hideDevice == rHand[Hand.Left].source)
                rHand[Hand.Left].showController = false
            if (hideDevice == rHand[Hand.Right].source)
                rHand[Hand.Right].showController = false
        }

        for (hand in Hand.values()) {
            val poseData = vr.InputPoseActionData()
            val err = vrInput.getPoseActionData(rHand[hand].actionPose, TrackingUniverseOrigin.Standing, 0f, poseData, vr.invalidInputValueHandle)
            if (err != vrInput.Error.None || !poseData.active || !poseData.pose.poseIsValid)
                rHand[hand].showController = false
            else {
                rHand[hand].rmat4Pose put poseData.pose.deviceToAbsoluteTracking

                val originInfo = vr.InputOriginInfo()
                if (vrInput.getOriginTrackedDeviceInfo(poseData.activeOrigin, originInfo) == vrInput.Error.None
                        && originInfo.trackedDeviceIndex != vr.trackedDeviceIndexInvalid) {
                    val renderModelName = hmd.getStringTrackedDeviceProperty(originInfo.trackedDeviceIndex, TrackedDeviceProperty.RenderModelName_String)
                    if (renderModelName != rHand[hand].renderModelName) {
                        rHand[hand].renderModel = findOrLoadRenderModel(renderModelName)
                    }
                }
            }
        }
    }

    /** Purpose: Processes a single VR event */
    fun VREvent.process() = when (eventType) {
        VREventType.TrackedDeviceDeactivated -> println("Device $trackedDeviceIndex detached.")
        VREventType.TrackedDeviceUpdated -> println("Device $trackedDeviceIndex updated.")
        VREventType.TrackedDeviceActivated -> println("Device $trackedDeviceIndex activated.")
        else -> Unit
    }

    /** Purpose: Returns true if the action is active and had a rising edge */
    fun getDigitalActionRisingEdge(action: VRActionHandle, pDevicePath: KMutableProperty0<VRInputValueHandle>? = null): Boolean {
        val actionData = vr.InputDigitalActionData()
        vrInput.getDigitalActionData(action, actionData, vr.invalidInputValueHandle)
        pDevicePath?.let {
            var devicePath by it
            devicePath = vr.invalidInputValueHandle
            if (actionData.active) {
                val originInfo = vr.InputOriginInfo()
                if (vrInput.Error.None == vrInput.getOriginTrackedDeviceInfo(actionData.activeOrigin, originInfo))
                    devicePath = originInfo.devicePath
            }
        }
        return actionData.active && actionData.changed && actionData.state
    }

    /** Purpose: Returns true if the action is active and had a falling edge */
    fun getDigitalActionFallingEdge(action: VRActionHandle, pDevicePath: KMutableProperty0<VRInputValueHandle>? = null): Boolean {
        val actionData = vr.InputDigitalActionData()
        vrInput.getDigitalActionData(action, actionData, vr.invalidInputValueHandle)
        pDevicePath?.let {
            var devicePath by it
            devicePath = vr.invalidInputValueHandle
            if (actionData.active) {
                val originInfo = vr.InputOriginInfo()
                if (vrInput.Error.None == vrInput.getOriginTrackedDeviceInfo(actionData.activeOrigin, originInfo))
                    devicePath = originInfo.devicePath
            }
        }
        return actionData.active && actionData.changed && !actionData.state
    }

    /** Purpose: Returns true if the action is active and its state is true */
    fun getDigitalActionState(action: VRActionHandle, pDevicePath: KMutableProperty0<VRInputValueHandle>? = null): Boolean {
        val actionData = vr.InputDigitalActionData()
        val err = vrInput.getDigitalActionData(action, actionData, vr.invalidInputValueHandle)
        pDevicePath?.let {
            var devicePath by it
            devicePath = vr.invalidInputValueHandle
            if (actionData.active) {
                val originInfo = vr.InputOriginInfo()
                if (vrInput.Error.None == vrInput.getOriginTrackedDeviceInfo(actionData.activeOrigin, originInfo))
                    devicePath = originInfo.devicePath
            }
        }
        return actionData.active && actionData.state
    }

    /** Purpose: Finds a render model we've already loaded or loads a new one */
    fun findOrLoadRenderModel(renderModelName: String): CGLRenderModel? =
            renderModels.find { it.modelName == renderModelName } ?: run {

                // load the model if we didn't find one

                val model = vrRenderModels.loadRenderModel(renderModelName) ?: run {
                    println("Unable to load render model $renderModelName - ${vrRenderModels.error}")
                    return null // move on to the next tracked device
                }

                val texture = vrRenderModels.loadTexture(model.diffuseTextureId) ?: run {
                    println("Unable to load render texture id:${model.diffuseTextureId} for render model $renderModelName")
                    model.freeNative()
                    return null // move on to the next tracked device
                }

                return CGLRenderModel(renderModelName, model, texture).also {
                    renderModels += it
                    model.freeNative()
                    texture.freeNative()
                }
            }

    fun renderFrame() {

        // for now as fast as possible
        renderStereoTargets()
        companionWindow.render()

        val leftEyeTexture = vr.Texture(eyeDesc[0].textureName[FrameBufferDesc.Target.RESOLVE], TextureType.OpenGL, ColorSpace.Gamma)
        vrCompositor.submit(VREye.Left, leftEyeTexture)
        val rightEyeTexture = vr.Texture(eyeDesc[1].textureName[FrameBufferDesc.Target.RESOLVE], TextureType.OpenGL, ColorSpace.Gamma)
        vrCompositor.submit(VREye.Right, rightEyeTexture)

        if (vBlank && glFinishHack)
        /*  HACKHACK. From gpuview profiling, it looks like there is a bug where two renders and a present happen
            right before and after the vsync causing all kinds of jittering issues. This glFinish() appears to clear
            that up. Temporary fix while I try to get nvidia to investigate this problem.
            1/29/2014 mikesart */
            glFinish()

        // SwapWindow
        window.present()

        /*  Clear
            We want to make sure the glFinish waits for the entire present to complete, not just the submission
            of the command. So, we do a clear here right here so the glFinish will wait fully for the swap.             */
        glClearColorBuffer(clearColor)
        glClearDepthBuffer()

        // Flush and wait for swap.
        if (vBlank) {
            glFlush()
            glFinish()
        }

        // Spew out the controller and pose count whenever they change.
        if (trackedControllerCount != trackedControllerCount_Last || validPoseCount != validPoseCount_Last) {
            validPoseCount_Last = validPoseCount
            trackedControllerCount_Last = trackedControllerCount

            println("PoseCount: $validPoseCount($strPoseClasses) Controllers: $trackedControllerCount")
        }

        updateHMDMatrixPose()
    }

    fun renderStereoTargets() {

        glClearColorBuffer()

        for (eye in VREye.values())
            eyeDesc[eye.i].render(eye)
    }

    fun updateHMDMatrixPose() {

        vrCompositor.waitGetPoses(trackedDevicePose)

        validPoseCount = 0
        strPoseClasses = ""
        for (device in 0 until vr.maxTrackedDeviceCount) {
            if (trackedDevicePose[device].poseIsValid) {
                validPoseCount++
                rmat4DevicePose[device] = trackedDevicePose[device].deviceToAbsoluteTracking
                if (devClassChar[device] == NUL)
                    devClassChar[device] = when (hmd getTrackedDeviceClass device) {
                        TDC.Controller -> 'C'
                        TDC.HMD -> 'H'
                        TDC.Invalid -> 'I'
                        TDC.GenericTracker -> 'G'
                        TDC.TrackingReference -> 'T'
                        else -> '?'
                    }
                strPoseClasses += devClassChar[device]
            }
        }

        if (trackedDevicePose[vr.trackedDeviceIndex_Hmd].poseIsValid)
            rmat4DevicePose[vr.trackedDeviceIndex_Hmd] inverse hmdPose
    }

    fun shutdown() {

        vr.shutdown()

        renderModels.forEach { it.dispose() }

        scene.dispose()

        eyeDesc.forEach { it.dispose() }

        companionWindow.dispose()

        glfw.terminate()
    }
}

val assetPath = Paths.get("").toAbsolutePath().toString() + "/src/main/resources"