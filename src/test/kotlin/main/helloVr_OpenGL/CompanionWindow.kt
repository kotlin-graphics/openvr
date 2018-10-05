package main.helloVr_OpenGL

import ab.appBuffer
import glm_.BYTES
import glm_.L
import glm_.buffer.intBufferBig
import glm_.i
import glm_.vec2.Vec2
import glm_.vec2.Vec2i
import gln.buffer.glArrayBufferData
import gln.buffer.glBindArrayBuffer
import gln.buffer.glBindElementBuffer
import gln.buffer.glElementBufferData
import gln.glViewport
import gln.glf.glf
import gln.glf.semantic
import gln.program.glUseProgram
import gln.program.usingProgram
import gln.vertexArray.glBindVertexArray
import gln.vertexArray.glEnableVertexAttribArray
import gln.vertexArray.glVertexAttribPointer
import openvr.lib.trackedDeviceIndex_Hmd
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import org.lwjgl.opengl.GL15.GL_STATIC_DRAW
import org.lwjgl.opengl.GL15.glGenBuffers
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30.glGenVertexArrays
import uno.glfw.GlfwWindow
import uno.glfw.glfw
import lib.TrackedDeviceProperty
import lib.getStringTrackedDeviceProperty

class CompanionWindow {

    val resolution = Vec2i(640, 320)
    val position = Vec2i(700, 100)

    val strDriver = hmd.getStringTrackedDeviceProperty(trackedDeviceIndex_Hmd, TrackedDeviceProperty.TrackingSystemName_String)
    val strDisplay = hmd.getStringTrackedDeviceProperty(trackedDeviceIndex_Hmd, TrackedDeviceProperty.SerialNumber_String)

    val window = GlfwWindow(resolution, "helloVr - $strDriver $strDisplay").also {
        it.pos = position
        it.makeContextCurrent()
        glfw.swapInterval = vBlank.i
        it.show()
        GL.createCapabilities()
        it.autoSwap = false
    }

    object Buffer {
        val VERTEX = 0
        val INDEX = 1
        val MAX = 2
    }

    var bufferName = intBufferBig(Buffer.MAX)
    val vertexArrayName = intBufferBig(1)
    val indexSize: Int
    val program = ProgramWindow()

    init {

        val vertices = appBuffer.floatBufferOf(
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

        val indices = appBuffer.shortBufferOf(
                0, 1, 3,
                0, 3, 2,
                4, 5, 7,
                4, 7, 6)

        indexSize = indices.size / Short.BYTES

        glGenVertexArrays(vertexArrayName)
        glBindVertexArray(vertexArrayName)

        glGenBuffers(bufferName)

        glBindArrayBuffer(bufferName[Buffer.VERTEX])
        glArrayBufferData(vertices, GL_STATIC_DRAW)

        glBindElementBuffer(bufferName[Buffer.INDEX])
        glElementBufferData(indices, GL_STATIC_DRAW)

        glEnableVertexAttribArray(glf.pos2_tc2)
        glVertexAttribPointer(glf.pos2_tc2)

        glBindVertexArray()
    }

    fun render() {

        glDisable(GL_DEPTH_TEST)
        glViewport(resolution)

        glBindVertexArray(vertexArrayName)
        GL20.glUseProgram(program.name)

        // render left eye with first half of index array and right eye with the second half.
        for (eye in openvr.lib.EVREye.values()) {
            glBindTexture(GL_TEXTURE_2D, eyeDesc[eye.i].textureName[FrameBufferDesc.Target.RESOLVE])
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            // offset is in bytes, so indexSize points to half of indices, because short -> int
            glDrawElements(GL_TRIANGLES, indexSize / 2, GL_UNSIGNED_SHORT, if (eye == openvr.lib.EVREye.Left) 0 else indexSize.L)
        }

        glBindVertexArray()
        glUseProgram()
    }

    object VertexData {
        val SIZE = Vec2.size * 2
        val OFFSET_POSITION = 0
        val OFFSET_TEXCOORD = Vec2.size
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
            usingProgram(name) { "myTexture".unit = semantic.sampler.DIFFUSE }
        }
    }
}