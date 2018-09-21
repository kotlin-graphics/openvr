package main.helloVr_OpenGL

import openvr.lib.*
import org.lwjgl.opengl.GL11.GL_DONT_CARE
import org.lwjgl.opengl.GL11.glEnable
import org.lwjgl.opengl.KHRDebug.*
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.system.MemoryUtil.memUTF8
import uno.glfw.glfw
import uno.glfw.windowHint.Profile
import java.nio.IntBuffer
import kotlin.properties.Delegates

fun main(args: Array<String>) {

    Application().apply {
        loop()
        shutdown()
    }
}

const val debugOpenGL = true
const val vBlank = true


var hmd: IVRSystem by Delegates.notNull()

var eyeDesc: Array<FrameBufferDesc> by Delegates.notNull()

class Application {

//    bool m_bVerbose;
//    bool m_bPerf;
//    bool m_bGlFinishHack;
//
//    vr::TrackedDevicePose_t m_rTrackedDevicePose[ vr::k_unMaxTrackedDeviceCount ];
//    Matrix4 m_rmat4DevicePose[ vr::k_unMaxTrackedDeviceCount ];
//
//    struct ControllerInfo_t
//    {
//        vr::VRInputValueHandle_t m_source = vr ::k_ulInvalidInputValueHandle;
//        vr::VRActionHandle_t m_actionPose = vr ::k_ulInvalidActionHandle;
//        vr::VRActionHandle_t m_actionHaptic = vr ::k_ulInvalidActionHandle;
//        Matrix4 m_rmat4Pose;
//        CGLRenderModel * m_pRenderModel = nullptr;
//        std::string m_sRenderModelName;
//        bool m_bShowController;
//    };
//
//    enum EHand
//    {
//        Left = 0,
//        Right = 1,
//    };
//    ControllerInfo_t m_rHand[2];

    init {

        // Loading the SteamVR Runtime
        val error = EVRInitError_ByReference()
        hmd = vrInit(error, EVRApplicationType.Scene).let {
            if (error() != EVRInitError.None)
                throw Error("Unable to init VR runtime: ${vrGetVRInitErrorAsEnglishDescription(error.value)}")
            it!!
        }
        with(glfw) {
            init()
            windowHint {
                context.version = "4.1"
                profile = Profile.core
                debug = debugOpenGL
            }
        }
    }

    // GLFW bookkeeping
    val companionWindow = CompanionWindow()

    init {
        if (debugOpenGL) {
            glDebugMessageCallback({ _, _, _, _, _, message, _ -> println("GL Error: ${memUTF8(message)}") }, NULL)
            val ids: IntBuffer? = null
            glDebugMessageControl(GL_DONT_CARE, GL_DONT_CARE, GL_DONT_CARE, ids, true)
            glEnable(GL_DEBUG_OUTPUT_SYNCHRONOUS)
        }
    }

    val scene = Scene()
    //
//    private : // OpenGL bookkeeping
//    int m_iTrackedControllerCount;
//    int m_iTrackedControllerCount_Last;
//    int m_iValidPoseCount;
//    int m_iValidPoseCount_Last;
//    bool m_bShowCubes;
//    Vector2 m_vAnalogValue;
//
//    std::string m_strPoseClasses;                            // what classes we saw poses for this frame
//    char m_rDevClassChar[ vr::k_unMaxTrackedDeviceCount ];   // for each device, a character representing its class
//
//    int m_iSceneVolumeWidth;
//    int m_iSceneVolumeHeight;
//    int m_iSceneVolumeDepth;
//    float m_fScaleSpacing;
//    float m_fScale;
//
//    int m_iSceneVolumeInit;                                  // if you want something other than the default 20x20x20
//
    val nearClip = 0.1f
    val farClip = 30f
//
//    GLuint m_iTexture;
//
//    unsigned int m_uiVertcount;
//
//    GLuint m_glSceneVertBuffer;
//    GLuint m_unSceneVAO;
//    GLuint m_unCompanionWindowVAO;
//    GLuint m_glCompanionWindowIDVertBuffer;
//    GLuint m_glCompanionWindowIDIndexBuffer;
//    unsigned int m_uiCompanionWindowIndexSize;
//
//    GLuint m_glControllerVertBuffer;
//    GLuint m_unControllerVAO;
//    unsigned int m_uiControllerVertcount;
//
//    Matrix4 m_mat4HMDPose;

    //    Matrix4 m_mat4eyePosRight;
//
//    Matrix4 m_mat4ProjectionCenter;
    val projection = Array(2, ::getHmdMatrixProjectionEye)
    val eyePosLeft = Array(2, ::getHmdMatrixPoseEye)

    /** Purpose: Gets a Matrix Projection Eye with respect to nEye. */
    fun getHmdMatrixProjectionEye(eye: Int) = hmd.getProjectionMatrix(EVREye of eye, nearClip, farClip)

    /** Purpose: Gets an HMDMatrixPoseEye with respect to nEye. */
    fun getHmdMatrixPoseEye(eye: Int) = hmd.getEyeToHeadTransform(EVREye of eye).inverse()

    init {

        setupStereoRenderTargets()

        if (!initCompositor())
            throw Error("Application::init() - Failed to initialize VR Compositor!")

        vrInput!!.apply {

//            setActionManifestPath(Path_MakeAbsolute("../hellovr_actions.json", Path_StripFilename(Path_GetExecutablePath())).c_str());
//
//            getActionHandle("/actions/demo/in/HideCubes", &m_actionHideCubes);
//            getActionHandle("/actions/demo/in/HideThisController", &m_actionHideThisController);
//            getActionHandle("/actions/demo/in/TriggerHaptic", &m_actionTriggerHaptic);
//            getActionHandle("/actions/demo/in/AnalogInput", &m_actionAnalongInput);
//
//            getActionSetHandle("/actions/demo", &m_actionsetDemo);
//
//            getActionHandle("/actions/demo/out/Haptic_Left", &m_rHand[Left].m_actionHaptic);
//            getInputSourceHandle("/user/hand/left", &m_rHand[Left].m_source);
//            getActionHandle("/actions/demo/in/Hand_Left", &m_rHand[Left].m_actionPose);
//
//            getActionHandle("/actions/demo/out/Haptic_Right", &m_rHand[Right].m_actionHaptic);
//            getInputSourceHandle("/user/hand/right", &m_rHand[Right].m_source);
//            getActionHandle("/actions/demo/in/Hand_Right", &m_rHand[Right].m_actionPose);
        }
    }

    fun setupStereoRenderTargets() {
        val size = hmd.recommendedRenderTargetSize
        eyeDesc = Array(EVREye.MAX) { FrameBufferDesc(size) }
    }

    /** -----------------------------------------------------------------------------
     *  Purpose: Initialize Compositor. Returns true if the compositor was successfully initialized, false otherwise.
     *  ----------------------------------------------------------------------------- */
    fun initCompositor(): Boolean {
        if (vrCompositor == null) {
            System.err.println("Compositor initialization failed. See log file for details")
            return false
        }
        return true
    }

    fun loop() {

//        companionWindow.window.cursor = GlfwWindow.Cursor.Hidden

        companionWindow.window.loop {

            //            handleInput()

//            renderFrame()
        }
    }

    fun shutdown() {

//        if( m_pHMD )
//        {
//            vr::VR_Shutdown();
//            m_pHMD = NULL;
//        }
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