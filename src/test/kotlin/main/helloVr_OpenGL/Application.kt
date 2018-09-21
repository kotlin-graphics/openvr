package main.helloVr_OpenGL

import ab.advance
import ab.appBuffer
import glm_.L
import glm_.mat4x4.Mat4
import glm_.vec2.Vec2
import glm_.vec4.Vec4
import gln.clear.glClearColorBuffer
import gln.clear.glClearDepthBuffer
import gln.debug.glDebugMessageCallback
import openvr.lib.*
import openvr.lib.VRApplication
import org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE
import org.lwjgl.glfw.GLFW.GLFW_KEY_Q
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.KHRDebug.GL_DEBUG_OUTPUT_SYNCHRONOUS
import org.lwjgl.opengl.KHRDebug.glDebugMessageControl
import org.lwjgl.openvr.*
import org.lwjgl.openvr.InputAnalogActionData
import org.lwjgl.openvr.InputDigitalActionData
import org.lwjgl.openvr.InputOriginInfo
import org.lwjgl.openvr.InputPoseActionData
import org.lwjgl.openvr.Texture
import org.lwjgl.openvr.TrackedDevicePose
import org.lwjgl.openvr.VRActiveActionSet
import org.lwjgl.openvr.VREvent
import org.lwjgl.system.MemoryUtil.NULL
import uno.glfw.GlfwWindow
import uno.glfw.glfw
import vr_.*
import vr_.EVREventType
import vr_.TrackedDeviceProperty
import vr_.VRActionHandle
import vr_.VRInputValueHandle
import java.nio.IntBuffer
import kotlin.properties.Delegates
import kotlin.reflect.KMutableProperty0
import org.lwjgl.opengl.KHRDebug.glDebugMessageCallback as _

fun main(args: Array<String>) {

    Application().apply {
        loop()
        shutdown()
    }
}

const val debugOpenGL = true
const val vBlank = true
const val glFinishHack = true

// Loading the SteamVR Runtime
val hmd = vr.init(VRApplication.Scene) ?: throw Error("Unable to init VR runtime")
lateinit var scene: Scene

var eyeDesc: Array<FrameBufferDesc> by Delegates.notNull()

enum class Hand { Left, Right }

val clearColor = Vec4(0, 0, 0, 1)

var showCubes = true

val nearClip = 0.1f
val farClip = 30f

val projection = Array(2) {
    // Gets a Matrix Projection Eye with respect to nEye.
    hmd.getProjectionMatrix(EVREye of it, nearClip, farClip)
}
val eyePos = Array(2) {
    // Purpose: Gets an HMDMatrixPoseEye with respect to nEye.
    hmd.getEyeToHeadTransform(EVREye of it).inverse()
}

var hmdPose = Mat4()

class Application {

    val trackedDevicePose = TrackedDevicePose.calloc(maxTrackedDeviceCount)

    val rmat4DevicePose = Array(maxTrackedDeviceCount) { Mat4() }

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

    init {
        with(glfw) {
            init()
            windowHint {
                context.version = "4.1"
                profile = "core"
                debug = debugOpenGL
            }
        }
    }

    // GLFW bookkeeping
    val companionWindow = CompanionWindow()

    init {
        if (debugOpenGL) {
            glDebugMessageCallback { source, type, id, severity, message ->
                println("$id: $type of $severity severity, raised from $source: $message")
            }
            glDebugMessageControl(GL_DONT_CARE, GL_DONT_CARE, GL_DONT_CARE, null as IntBuffer?, true)
            glEnable(GL_DEBUG_OUTPUT_SYNCHRONOUS)
        }
        scene = Scene()
        eyeDesc = Array(2) { FrameBufferDesc(hmd.recommendedRenderTargetSize) }
    }

    var trackedControllerCount = 0
    var trackedControllerCount_Last = 0
    var validPoseCount = 0
    var validPoseCount_Last = 0
    val analogValue = Vec2()

    /** what classes we saw poses for this frame */
    var strPoseClasses = ""
    /** for each device, a character representing its class */
    val devClassChar = CharArray(maxTrackedDeviceCount)

//    std::vector< CGLRenderModel * > m_vecRenderModels;

    var actionHideCubes = vr.invalidActionHandle
    var actionHideThisController = vr.invalidActionHandle
    var actionTriggerHaptic = vr.invalidActionHandle
    var actionAnalongInput = vr.invalidActionHandle

    var actionsetDemo = vr.invalidActionSetHandle

    init {

        if (!initCompositor())
            throw Error("Application::init() - Failed to initialize VR Compositor!")

        vrInput.apply {

            setActionManifestPath("$assetPath/hellovr_actions.json")

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
    }

    /** -----------------------------------------------------------------------------
     *  Purpose: Initialize Compositor. Returns true if the compositor was successfully initialized, false otherwise.
     *  ----------------------------------------------------------------------------- */
    fun initCompositor(): Boolean = when(OpenVR.VRCompositor) {
        null -> {
            System.err.println("Compositor initialization failed. See log file for details")
            false
        }
        else -> true
    }

    fun loop() {

        companionWindow.window.apply {

            cursor = GlfwWindow.Cursor.Hidden

            loop {
                handleInput()
                renderFrame()
            }
        }
    }

    var hapticDevice: VRInputValueHandle = NULL
    var hideDevice: VRInputValueHandle = NULL

    fun handleInput() {

        companionWindow.window.apply {
            if (pressed(GLFW_KEY_ESCAPE) || pressed(GLFW_KEY_Q))
                shouldClose = true
            else if (pressed(GLFW_KEY_ESCAPE))
                showCubes = !showCubes
        }

        // Process SteamVR events
        val event = VREvent.create(appBuffer.ptr.advance(VREvent.SIZEOF))
        while (hmd.pollNextEvent(event))
            event.process()

        /*  Process SteamVR action state
            UpdateActionState is called each frame to update the state of the actions themselves. The application
            controls which action sets are active with the provided array of VRActiveActionSet_t structs. */
        val actionSet = vr.VRActiveActionSet()
        actionSet.actionSet = actionsetDemo
        vrInput updateActionState actionSet

        showCubes = !getDigitalActionState(actionHideCubes)

        if (getDigitalActionRisingEdge(actionTriggerHaptic, ::hapticDevice)) {
            if (hapticDevice == rHand[Hand.Left].source)
                vrInput.triggerHapticVibrationAction(rHand[Hand.Left].actionHaptic, 0f, 1f, 4f, 1f)
            if (hapticDevice == rHand[Hand.Right].source)
                vrInput.triggerHapticVibrationAction(rHand[Hand.Right].actionHaptic, 0f, 1f, 4f, 1f)
        }

        val analogData = vr.InputAnalogActionData()
        if (vrInput.getAnalogActionData(actionAnalongInput, analogData) == EVRInputError.None && analogData.bActive())
            analogValue.put(analogData.x(), analogData.y())

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
            if (vrInput.getPoseActionData(rHand[hand].actionPose, ETrackingUniverseOrigin.Standing, 0f, poseData) != EVRInputError.None
                    || !poseData.active || !poseData.pose.poseIsValid) {
                rHand[hand].showController = false
            } else {
                rHand[hand].rmat4Pose put poseData.pose.deviceToAbsoluteTracking

                val originInfo = vr.InputOriginInfo()
                if (vrInput.getOriginTrackedDeviceInfo(poseData.activeOrigin(), originInfo) == EVRInputError.None
                        && originInfo.trackedDeviceIndex != trackedDeviceIndexInvalid) {
                    val renderModelName = hmd.getStringTrackedDeviceProperty(originInfo.trackedDeviceIndex(), TrackedDeviceProperty.RenderModelName_String)
                    if (renderModelName != rHand[hand].renderModelName) {
//                        rHand[hand].renderModel = findOrLoadRenderModel(renderModelName)
                        rHand[hand].renderModelName = renderModelName
                    }
                }
            }
        }
    }

    /** Purpose: Processes a single VR event */
    fun VREvent.process() = when (eventType) {
        EVREventType.TrackedDeviceDeactivated -> println("Device $trackedDeviceIndex detached.")
        EVREventType.TrackedDeviceUpdated -> println("Device $trackedDeviceIndex updated.")
        else -> Unit
    }

    /** Purpose: Returns true if the action is active and had a rising edge */
    fun getDigitalActionRisingEdge(action: VRActionHandle, devicePath: KMutableProperty0<VRInputValueHandle>? = null): Boolean {
        val actionData = vr.InputDigitalActionData()
        vrInput.getDigitalActionData(action, actionData)
        devicePath?.let {
            it.set(invalidInputValueHandle)
            if (actionData.active) {
                val originInfo = vr.InputOriginInfo()
                if (EVRInputError.None == vrInput.getOriginTrackedDeviceInfo(actionData.activeOrigin, originInfo))
                    it.set(originInfo.devicePath)
            }
        }
        return actionData.active && actionData.changed && actionData.state
    }


    /** Purpose: Returns true if the action is active and had a falling edge */
    fun getDigitalActionFallingEdge(action: VRActionHandle, devicePath: KMutableProperty0<VRInputValueHandle>? = null): Boolean {
        val actionData = vr.InputDigitalActionData()
        vrInput.getDigitalActionData(action, actionData)
        devicePath?.let{
            it.set(vr.invalidInputValueHandle)
            if (actionData.active) {
                val originInfo = vr.InputOriginInfo()
                if (EVRInputError.None == vrInput.getOriginTrackedDeviceInfo(actionData.activeOrigin, originInfo))
                    it.set(originInfo.devicePath)
            }
        }
        return actionData.active && actionData.changed && !actionData.state
    }


    /** Purpose: Returns true if the action is active and its state is true */
    fun getDigitalActionState(action: VRActionHandle, devicePath: KMutableProperty0<VRInputValueHandle>? = null): Boolean {
        val actionData = vr.InputDigitalActionData()
        vrInput.getDigitalActionData(action, actionData)
        devicePath?.let {
            devicePath.set(invalidInputValueHandle)
            if (actionData.active) {
                val originInfo = vr.InputOriginInfo()
                if (EVRInputError.None == vrInput.getOriginTrackedDeviceInfo(actionData.activeOrigin, originInfo))
                    devicePath.set(originInfo.devicePath)
            }
        }
        return actionData.active && actionData.state
    }

    fun renderFrame() {

        // for now as fast as possible
//        RenderControllerAxes()
        renderStereoTargets()
        companionWindow.render()

        val leftEyeTexture = vr.Texture().set(
                eyeDesc[0].textureName[FrameBufferDesc.Target.RESOLVE].L, ETextureType.OpenGL.i, EColorSpace.Gamma.i)
        vrCompositor.submit(EVREye.Left, leftEyeTexture)
        val rightEyeTexture = vr.Texture().set(
                eyeDesc[1].textureName[FrameBufferDesc.Target.RESOLVE].L, ETextureType.OpenGL.i, EColorSpace.Gamma.i)
        vrCompositor.submit(EVREye.Right, rightEyeTexture)

        if (vBlank && glFinishHack)
        /*  HACKHACK. From gpuview profiling, it looks like there is a bug where two renders and a present happen
            right before and after the vsync causing all kinds of jittering issues. This glFinish() appears to clear
            that up. Temporary fix while I try to get nvidia to investigate this problem.
            1/29/2014 mikesart */
            glFinish()

        // SwapWindow
        companionWindow.window.present()

        // Clear
        run {
            /*  We want to make sure the glFinish waits for the entire present to complete, not just the submission
                of the command. So, we do a clear here right here so the glFinish will wait fully for the swap.             */
            glClearColorBuffer(clearColor)
            glClearDepthBuffer()
        }

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

        for (eye in EVREye.values())
            eyeDesc[eye.i].render(eye)
    }

    fun updateHMDMatrixPose() {

        vrCompositor.waitGetPoses(trackedDevicePose)

        validPoseCount = 0
        strPoseClasses = ""
        for (device in 0 until maxTrackedDeviceCount) {
            if (trackedDevicePose[device].bPoseIsValid()) {
                validPoseCount++
                rmat4DevicePose[device] = trackedDevicePose[device].mDeviceToAbsoluteTracking().asMat4
                if (devClassChar[device] == NUL)
                    devClassChar[device] = when (hmd.getTrackedDeviceClass(device)) {
                        ETrackedDeviceClass.Controller -> 'C'
                        ETrackedDeviceClass.HMD -> 'H'
                        ETrackedDeviceClass.Invalid -> 'I'
                        ETrackedDeviceClass.GenericTracker -> 'G'
                        ETrackedDeviceClass.TrackingReference -> 'T'
                        else -> '?'
                    }
                strPoseClasses += devClassChar[device]
            }
        }

        if (trackedDevicePose[trackedDeviceIndex_Hmd].bPoseIsValid())
            hmdPose = rmat4DevicePose[trackedDeviceIndex_Hmd].inverse()
    }

    fun shutdown() {

        vr.shutdown()
//
//        for( std::vector< CGLRenderModel * >::iterator i = m_vecRenderModels.begin(); i != m_vecRenderModels.end(); i++ )
//        {
//            delete (*i);
//        }
//        m_vecRenderModels.clear();
//
//        if( m_pContext )
//        {
//            if( m_bDebugOpenGL )
//            {
//                glDebugMessageControl( GL_DONT_CARE, GL_DONT_CARE, GL_DONT_CARE, 0, nullptr, GL_FALSE );
//                glDebugMessageCallback(nullptr, nullptr);
//            }
//            glDeleteBuffers(1, &m_glSceneVertBuffer);
//
//            if ( m_unSceneProgramID )
//            {
//                glDeleteProgram( m_unSceneProgramID );
//            }
//            if ( m_unControllerTransformProgramID )
//            {
//                glDeleteProgram( m_unControllerTransformProgramID );
//            }
//            if ( m_unRenderModelProgramID )
//            {
//                glDeleteProgram( m_unRenderModelProgramID );
//            }
//            if ( m_unCompanionWindowProgramID )
//            {
//                glDeleteProgram( m_unCompanionWindowProgramID );
//            }
//
//            glDeleteRenderbuffers( 1, &leftEyeDesc.m_nDepthBufferId );
//            glDeleteTextures( 1, &leftEyeDesc.m_nRenderTextureId );
//            glDeleteFramebuffers( 1, &leftEyeDesc.m_nRenderFramebufferId );
//            glDeleteTextures( 1, &leftEyeDesc.m_nResolveTextureId );
//            glDeleteFramebuffers( 1, &leftEyeDesc.m_nResolveFramebufferId );
//
//            glDeleteRenderbuffers( 1, &rightEyeDesc.m_nDepthBufferId );
//            glDeleteTextures( 1, &rightEyeDesc.m_nRenderTextureId );
//            glDeleteFramebuffers( 1, &rightEyeDesc.m_nRenderFramebufferId );
//            glDeleteTextures( 1, &rightEyeDesc.m_nResolveTextureId );
//            glDeleteFramebuffers( 1, &rightEyeDesc.m_nResolveFramebufferId );
//
//            if( m_unCompanionWindowVAO != 0 )
//            {
//                glDeleteVertexArrays( 1, &m_unCompanionWindowVAO );
//            }
//            if( m_unSceneVAO != 0 )
//            {
//                glDeleteVertexArrays( 1, &m_unSceneVAO );
//            }
//            if( m_unControllerVAO != 0 )
//            {
//                glDeleteVertexArrays( 1, &m_unControllerVAO );
//            }
//        }
//
//        if( m_pCompanionWindow )
//        {
//            SDL_DestroyWindow(m_pCompanionWindow);
//            m_pCompanionWindow = NULL;
//        }
//
//        SDL_Quit();
    }
}