package main

import glm_.vec4.Vec4
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWKeyCallbackI
import org.lwjgl.glfw.GLFWVidMode
import org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT24
import org.lwjgl.opengl.GL30.*
import org.lwjgl.openvr.*
import org.lwjgl.openvr.VR.*
import org.lwjgl.openvr.VRCompositor.VRCompositor_PostPresentHandoff
import org.lwjgl.openvr.VRCompositor.VRCompositor_Submit
import org.lwjgl.openvr.VRSystem.VRSystem_GetRecommendedRenderTargetSize
import org.lwjgl.openvr.VRSystem.VRSystem_GetStringTrackedDeviceProperty
import org.lwjgl.system.MemoryStack.stackPush
import java.lang.System.exit
import java.nio.ByteBuffer
import java.nio.IntBuffer
import org.lwjgl.opengl.EXTFramebufferObject
import org.lwjgl.opengl.EXTFramebufferObject.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL42.glTexStorage2D
import org.lwjgl.opengl.GL45.glCreateTextures
import org.lwjgl.system.MemoryUtil
import java.nio.ByteOrder

val EXIT_FAILURE = 1
val EXIT_SUCCESS = 0

fun checkFramebufferStatus(name: String) {
    if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
        when (glCheckFramebufferStatus(GL_FRAMEBUFFER)) {
            GL_FRAMEBUFFER_UNDEFINED                     -> println("UNDEFINED")
            GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT         -> println("INCOMPLETE_ATTACHMENT")
            GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT -> println("INCOMPLETE_MISSING_ATTACHMENT")
            GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER        -> println("INCOMPLETE_DRAW_BUFFER")
            GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER        -> println("INCOMPLETE_READ_BUFFER")
            GL_FRAMEBUFFER_UNSUPPORTED                   -> println("UNSUPPORTED")
            GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE        -> println("INCOMPLETE_MULTISAMPLE")
        }
        throw RuntimeException("$name is incomplete")
    } else {
        println("$name is complete")
    }
}

fun getTrackedDeviceString(unDevice: Int, prop: Int): String {
    stackPush().let {
        val peError: IntBuffer = it.mallocInt(1)

        if (OpenVR.VRSystem != null) {
            println("GetStringTrackedDeviceProperty: " + OpenVR.VRSystem!!.GetStringTrackedDeviceProperty)
            println("GetRecommendedRenderTargetSize: " + OpenVR.VRSystem!!.GetRecommendedRenderTargetSize)
        } else {
            throw RuntimeException("Error: OpenVR.VRSystem was null.")
        }

        // val unRequiredBufferLen = k_unMaxPropertyStringSize
        val unRequiredBufferLen = VRSystem_GetStringTrackedDeviceProperty(unDevice, prop, null, null)
        if (unRequiredBufferLen == 0)
            return ""

        val stringBuffer: ByteBuffer = it.malloc(unRequiredBufferLen)
        VRSystem.VRSystem_GetStringTrackedDeviceProperty(unDevice, prop, stringBuffer, peError)

        return MemoryUtil.memUTF8(stringBuffer, unRequiredBufferLen - 1)
    }
}

class MyErrorCallback : GLFWErrorCallback() {
    override fun invoke(error: Int, description: Long) {
        val desc = getDescription(description)
        println("GLFW Error code: $error description: $desc")
    }
}

class MyKeyCallback : GLFWKeyCallbackI {
    override fun invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
            glfwSetWindowShouldClose(window, true)
    }
}

class Eye(vrApp: HelloVr.OpenVRApplication, color: Vec4) {
    val bounds: VRTextureBounds
    val texture: Int

    init {
        bounds = VRTextureBounds.create()
        bounds.set(0f, 0f, 1.0f, 1.0f)

        // FBO
        val buffer = ByteBuffer.allocateDirect(1 * 4).order(ByteOrder.nativeOrder()).asIntBuffer()
        EXTFramebufferObject.glGenFramebuffersEXT(buffer)
        val fbo = buffer.get()

        // Bind FBO
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fbo)

        // Texture
        val texBuffer = BufferUtils.createIntBuffer(1)
        glCreateTextures(GL_TEXTURE_2D, texBuffer)
        texture = texBuffer[0]
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture)
        glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, vrApp.recWidth, vrApp.recHeight)
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, texture, 0)

        // Depth Buffer
        val depthFormat = GL_DEPTH_COMPONENT24
        val depthRenderBufferID = glGenRenderbuffersEXT()
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, depthFormat, 512, 512);
        // Attach Depth
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, depthRenderBufferID);

        glViewport(0, 0, vrApp.recWidth, vrApp.recHeight)

        glClearColor(color[0], color[1], color[2], color[3])

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        checkFramebufferStatus("left eye")
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            when (glCheckFramebufferStatus(GL_FRAMEBUFFER)) {
                GL_FRAMEBUFFER_UNDEFINED                     -> println("UNDEFINED")
                GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT         -> println("INCOMPLETE_ATTACHMENT")
                GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT -> println("INCOMPLETE_MISSING_ATTACHMENT")
                GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER        -> println("INCOMPLETE_DRAW_BUFFER")
                GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER        -> println("INCOMPLETE_READ_BUFFER")
                GL_FRAMEBUFFER_UNSUPPORTED                   -> println("UNSUPPORTED")
                GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE        -> println("INCOMPLETE_MULTISAMPLE")
            }
            throw RuntimeException("left rt incomplete")
        }
    }
}

class HelloVr {

    // Override for vidmode printing
//  std::ostream& operator<<(std::ostream& str, const GLFWvidmode& vidmode) {
//    str << "\n{ // GLFWvidmode \n"
//    str << "\twidth = " << vidmode.width <<'\n'
//    str << "\theight = " << vidmode.height <<'\n'
//    str << "\tredBits = " << vidmode.redBits<<'\n'
//    str << "\tgreenBits = "<< vidmode.greenBits<<'\n'
//    str << "\tblueBits = "<< vidmode.blueBits<<'\n'
//    str << "\trefreshRate = "<<vidmode.refreshRate<<'\n'
//    str << "\n}\n" << std::endl
//
//    return str
//  }

    class OpenVRApplication {
        var hmd: Int = 0
        val recWidth: Int
            get() {
                return widthBuffer[0]
            }
        val recHeight: Int
            get() {
                return heightBuffer[0]
            }

        private var widthBuffer = BufferUtils.createIntBuffer(1)
        private var heightBuffer = BufferUtils.createIntBuffer(1)

        init {
            if (!hmdIsPresent()) { throw RuntimeException("Error : HMD not detected on the system") }

            if (!VR_IsRuntimeInstalled()) {
                throw RuntimeException("Error : OpenVR Runtime not detected on the system")
            }

            stackPush().let {
                val err = it.mallocInt(1)
                println("initializing VR")
                hmd = VR_InitInternal(err, EVRApplicationType_VRApplication_Scene)

                if (err[0] != EVRInitError_VRInitError_None) {
                    handleVRError(err[0])
                } else {
                    println("initialized VR")
                }

                val token = VR_GetInitToken()
                OpenVR.create(token)

                println(getTrackedDeviceString(VR.k_unTrackedDeviceIndex_Hmd, ETrackedDeviceProperty_Prop_TrackingSystemName_String))
                println(getTrackedDeviceString(VR.k_unTrackedDeviceIndex_Hmd, ETrackedDeviceProperty_Prop_SerialNumber_String))

//      if (!vr::VRCompositor()) { throw RuntimeException("Unable to initialize VR compositor!\n ") }

                println("getting recommended render size")
                VRSystem_GetRecommendedRenderTargetSize(widthBuffer, heightBuffer)

                println("Initialized HMD with suggested render target size : $recWidth x $recHeight")
            }
        }

        /// returns if the system believes there is an HMD present without initializing all of OpenVR
        fun hmdIsPresent(): Boolean {
            return VR_IsHmdPresent()
        }

        fun cleanup() {
            if (hmd != 0) {
                VR_ShutdownInternal()
            }
        }

        fun makeTexture(handle: Long, textureType: Int, colorSpace: Int): Texture {
            val buffer = BufferUtils.createByteBuffer(Texture.SIZEOF)

            val t = org.lwjgl.openvr.Texture(buffer)

            t.set(handle, textureType, colorSpace)
            t.eType(ETextureType_TextureType_OpenGL)
            t.eColorSpace(colorSpace)

            return t
        }

        fun submitFramesOpenGL(leftEye: Eye, rightEye: Eye, linear: Boolean = false) {
            if (hmd == 0) {
                throw RuntimeException("Error : presenting frames when VR system handle is null")
            }

            stackPush().let { stack ->
                val trackedDevicePose: TrackedDevicePose.Buffer = TrackedDevicePose.Buffer(stack.malloc(VR.k_unMaxTrackedDeviceCount * TrackedDevicePose.SIZEOF))
                val renderDevicePose: TrackedDevicePose.Buffer = TrackedDevicePose.Buffer(stack.malloc(VR.k_unMaxTrackedDeviceCount * TrackedDevicePose.SIZEOF))
                VRCompositor.VRCompositor_WaitGetPoses(trackedDevicePose, renderDevicePose)

                ///\todo the documentation on this is completely unclear.  I have no idea which one is correct...
                /// seems to imply that we always want Gamma in opengl because linear is for framebuffers that have been
                /// processed by DXGI...
                val colorSpace: Int = if (linear) {
                    EColorSpace_ColorSpace_Linear
                } else {
                    EColorSpace_ColorSpace_Gamma
                }

                val leftEyeTexture = makeTexture(leftEye.texture.toLong(), ETextureType_TextureType_OpenGL, colorSpace)
                val rightEyeTexture = makeTexture(rightEye.texture.toLong(), ETextureType_TextureType_OpenGL, colorSpace)

                VRCompositor_Submit(EVREye_Eye_Left, leftEyeTexture, leftEye.bounds, EVRSubmitFlags_Submit_Default)
                VRCompositor_Submit(EVREye_Eye_Right, rightEyeTexture, rightEye.bounds, EVRSubmitFlags_Submit_Default)
            }

            VRCompositor_PostPresentHandoff()
        }

        fun handleVRError(err: Int) {
            throw RuntimeException(VR_GetVRInitErrorAsEnglishDescription(err))
        }
    }

    fun printModes(modes: GLFWVidMode.Buffer?) {
        if (modes != null) {
            println("Got Video modes!")
            for (i in 0..modes.count() - 1) {
                println(modes[i])
            }
        } else {
            println("Failed to get video modes")
        }
    }

    fun main() {
        try {
            glfwSetErrorCallback(MyErrorCallback())

            if (!glfwInit()) {
                exit(EXIT_FAILURE)
            }

            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5)

            val vrApp = OpenVRApplication()

            val window = glfwCreateWindow(vrApp.recWidth, vrApp.recHeight, "Hello GLFW", 0, 0)

            if (window == 0L) {
                glfwTerminate()
                exit(EXIT_FAILURE)
            }

            stackPush().let { stack ->
                val modes = glfwGetVideoModes(glfwGetPrimaryMonitor())
//        printModes(modes)

                glfwSetKeyCallback(window, MyKeyCallback())
                glfwMakeContextCurrent(window)
                glfwSwapInterval(0)

                GL.createCapabilities()
                glClearColor(0.0f, 0.0f, 1.0f, 1.0f)

                val leftEye  = Eye(vrApp, Vec4(1,0,0,0))
                val rightEye = Eye(vrApp,Vec4(0,1,0,0))

                glBindFramebuffer(GL_FRAMEBUFFER, 0)

                glClearColor(0.0f, 0.0f, 1.0f, 1.0f)

                val width: IntBuffer = stack.mallocInt(1)
                val height: IntBuffer = stack.mallocInt(1)
                while (!glfwWindowShouldClose(window)) {
                    glfwGetFramebufferSize(window, width, height)

                    glViewport(0, 0, width[0], height[0])
                    glClear(GL_COLOR_BUFFER_BIT)

                    vrApp.submitFramesOpenGL(leftEye, rightEye)

                    glfwSwapBuffers(window)
                    glfwPollEvents()
                }
            }

            VR_ShutdownInternal() ///\todo if I don't include this here, and just let the destructor handle shutting down VR, the process never terminates correctly, and breaks VR until I reboot.

            glfwDestroyWindow(window)
            glfwTerminate()
        } catch (err: Exception) {
            System.err.println(err)
            exit(EXIT_FAILURE)
        }

        println("End")
        exit(EXIT_SUCCESS)
    }


    companion object {
        // Entry point for the application
        @JvmStatic
        fun main(args: Array<String>) {
            HelloVr().main()
        }
    }
}