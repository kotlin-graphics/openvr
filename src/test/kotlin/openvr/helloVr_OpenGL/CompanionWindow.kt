package openvr.helloVr_OpenGL

import glm_.L
import glm_.vec2.Vec2i
import gln.buffer.*
import kool.get
import gln.glViewport
import gln.glf.glf
import gln.glf.semantic
import gln.program.glUseProgram
import gln.vertexArray.glBindVertexArray
import gln.vertexArray.glEnableVertexAttribArray
import gln.vertexArray.glVertexAttribPointer
import kool.cap
import kool.free
import kool.Buffer
import kool.IntBuffer
import openvr.lib.VREye
import openvr.lib.stak
import openvr.lib.vr
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER
import org.lwjgl.opengl.GL15C.glBindBuffer
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL20.glUniform1i
import org.lwjgl.opengl.GL20C
import org.lwjgl.opengl.GL20C.glGetUniformLocation
import org.lwjgl.opengl.GL20C.glUseProgram
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GLUtil
import uno.glfw.GlfwWindow
import uno.glfw.VSync
import uno.glfw.glfw
import openvr.lib.TrackedDeviceProperty as TDP

class CompanionWindow {

    val resolution = Vec2i(640, 320)
    val position = Vec2i(700, 100)

    val strDriver = hmd.getStringTrackedDeviceProperty(vr.trackedDeviceIndex_Hmd, TDP.TrackingSystemName_String)

    val strDisplay = hmd.getStringTrackedDeviceProperty(vr.trackedDeviceIndex_Hmd, TDP.SerialNumber_String)

    val window = GlfwWindow(resolution, "helloVr - $strDriver $strDisplay").also {
        it.pos = position
        it.makeContextCurrent()
        glfw.swapInterval = if (vBlank) VSync.ON else VSync.OFF
        it.show()
        GL.createCapabilities()
        it.autoSwap = false
    }

    enum class Buffer { VERTEX, INDEX }

    var bufferName = IntBuffer<Buffer>()
    val vertexArrayName = IntBuffer(1)
    var indexSize = 0
    val program = ProgramWindow()

    init {

        stak {
            val vertices = it.floats(
                    /* left eye verts
                    | Pos | TexCoord    */
                    -1f, -1f, 0f, 0f,
                    +0f, -1f, 1f, 0f,
                    -1f, +1f, 0f, 1f,
                    +0f, +1f, 1f, 1f,
                    /*  right eye verts
                    | Pos | TexCoord    */
                    +0f, -1f, 0f, 0f,
                    +1f, -1f, 1f, 0f,
                    +0f, +1f, 0f, 1f,
                    +1f, +1f, 1f, 1f)

            val indices = it.shorts(
                    0, 1, 3,
                    0, 3, 2,
                    4, 5, 7,
                    4, 7, 6)

            indexSize = indices.cap

            glGenVertexArrays(vertexArrayName)
            glBindVertexArray(vertexArrayName)

            glGenBuffers(bufferName)

            glBindBuffer(GL_ARRAY_BUFFER, bufferName[Buffer.VERTEX])
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW)

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferName[Buffer.INDEX])
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW)

            glEnableVertexAttribArray(glf.pos2_tc2)
            glVertexAttribPointer(glf.pos2_tc2)

            glBindVertexArray()
        }

        window.framebufferSizeCallback = { size -> resolution put size }
    }

    fun render() {

        glDisable(GL_DEPTH_TEST)
        glViewport(resolution)

        glBindVertexArray(vertexArrayName)
        GL20.glUseProgram(program.name)

        // render left eye with first half of index array and right eye with the second half.
        for (eye in VREye.values()) {
            glBindTexture(GL_TEXTURE_2D, eyeDesc[eye.i].textureName[FrameBufferDesc.Target.RESOLVE])
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            // offset is in bytes, so indexSize points to half of indices, because short -> int
            glDrawElements(GL_TRIANGLES, indexSize / 2, GL_UNSIGNED_SHORT, if (eye == VREye.Left) 0 else indexSize.L)
        }

        glBindVertexArray()
        glUseProgram()
    }

    fun dispose() {

        glDeleteVertexArrays(vertexArrayName)
        glDeleteBuffers(bufferName)
        program.delete()

        vertexArrayName.free()
        bufferName.free()
    }

    class ProgramWindow : Program(
            vertSrc = """
                #version 410 core
                layout(location = ${semantic.attr.POSITION}) in vec4 position;
                layout(location = ${semantic.attr.TEX_COORD}) in vec2 uvCoords;
                noperspective out vec2 uv;
                void main() {
                    uv = uvCoords;
                    gl_Position = position;
                }""",
            fragSrc = """
                #version 410 core
                uniform sampler2D myTexture;
                noperspective in vec2 uv;
                layout(location = ${semantic.frag.COLOR}) out vec4 outColor;
                void main() {
                    outColor = texture(myTexture, uv);
                }""") {
        init {
            glUseProgram(name)
            glUniform1i(glGetUniformLocation(name, "myTexture"), semantic.sampler.DIFFUSE)
        }
    }
}