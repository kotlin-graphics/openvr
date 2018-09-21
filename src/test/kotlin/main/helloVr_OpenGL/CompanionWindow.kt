package main.helloVr_OpenGL

import glm_.BYTES
import glm_.L
import kool.intBufferBig
import glm_.i
import glm_.size
import glm_.vec2.Vec2
import glm_.vec2.Vec2i
import gln.glf.semantic
import openvr.lib.ETrackedDeviceProperty
import openvr.lib.trackedDeviceIndex_Hmd
import org.lwjgl.opengl.GL
import uno.buffer.floatBufferOf
import uno.buffer.shortBufferOf
import uno.glfw.GlfwWindow
import uno.glfw.glfw

class CompanionWindow {

    val resolution = Vec2i(640, 320)
    val position = Vec2i(700, 100)

    val strDriver = hmd.getStringTrackedDeviceProperty(trackedDeviceIndex_Hmd, ETrackedDeviceProperty.TrackingSystemName_String)
    val strDisplay = hmd.getStringTrackedDeviceProperty(trackedDeviceIndex_Hmd, ETrackedDeviceProperty.SerialNumber_String)

    val window = GlfwWindow(resolution, "helloVr - $strDriver $strDisplay").also {
        it.pos = position
        it.makeContextCurrent()
        glfw.swapInterval = vBlank.i
        it.show()
        GL.createCapabilities()
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

        val vertices = floatBufferOf(
                /* left eye verts
                | Pos | TexCoord    */
                -1, -1, 0, 0,
                +0, -1, 1, 0,
                -1, +1, 0, 1,
                +0, +1, 1, 1,
                /*  right eye verts
                | Pos | TexCoord    */
                +0, -1, 0, 0,
                +1, -1, 1, 0,
                +0, +1, 0, 1,
                +1, +1, 1, 1)

        val indices = shortBufferOf(
                0, 1, 3,
                0, 3, 2,
                4, 5, 7,
                4, 7, 6)

        indexSize = indices.size / Short.BYTES

//        with(gl) {
//
//            glGenVertexArrays(1, vertexArrayName)
//            glBindVertexArray(vertexArrayName[0])
//
//            glGenBuffers(Buffer.MAX, bufferName)
//
//            glBindBuffer(GL_ARRAY_BUFFER, bufferName[Buffer.VERTEX])
//            glBufferData(GL_ARRAY_BUFFER, vertices.size.L, vertices, GL_STATIC_DRAW)
//
//            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferName[Buffer.INDEX])
//            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.size.L, indices, GL_STATIC_DRAW)
//
//            glEnableVertexAttribArray(Semantic.Attr.POSITION)
//            glVertexAttribPointer(Semantic.Attr.POSITION, glm_.vec2.Vec2.length, GL_FLOAT, false, VertexData.SIZE, VertexData.OFFSET_POSITION)
//
//            glEnableVertexAttribArray(Semantic.Attr.TEX_COORD)
//            glVertexAttribPointer(Semantic.Attr.TEX_COORD, glm_.vec2.Vec2.length, GL_FLOAT, false, VertexData.SIZE, VertexData.OFFSET_TEXCOORD)
//
//            glBindVertexArray(0)
//
//            glDisableVertexAttribArray(Semantic.Attr.POSITION)
//            glDisableVertexAttribArray(Semantic.Attr.TEX_COORD)
//
//            glBindBuffer(GL_ARRAY_BUFFER, 0)
//            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
//        }
    }

//    fun render() = with(gl) {
//
//        glDisable(GL_DEPTH_TEST)
//        glViewport(0, 0, companionWindowSize.x, companionWindowSize.y)
//
//        glBindVertexArray(vertexArrayName[0])
//        glUseProgram(program.name)
//
//        // render left eye with first half of index array and right eye with the second half.
//        for (eye in openvr.lib.EVREye.values()) {
//            glBindTexture(GL_TEXTURE_2D, eyeDesc[eye.i].textureName[FrameBufferDesc.Target.RESOLVE])
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
//            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
//            // offset is in bytes, so indexSize points to half of indices, because short -> int
//            glDrawElements(GL_TRIANGLES, indexSize / 2, GL_UNSIGNED_SHORT, if (eye == openvr.lib.EVREye.Left) 0 else indexSize.L)
//        }
//
//        glBindVertexArray(0)
//        glUseProgram(0)
//    }

    object VertexData {
        val SIZE = Vec2.size * 2
        val OFFSET_POSITION = 0.L
        val OFFSET_TEXCOORD = Vec2.size.L
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
//            with(gl) {
//                glUseProgram(name)
//                glUniform1i(glGetUniformLocation(name, "myTexture"), Semantic.Sampler.DIFFUSE)
//                glUseProgram(0)
//            }
        }
    }
}