package openvr.helloVr_OpenGL

import glm_.L
import glm_.f
import glm_.mat4x4.Mat4
import glm_.vec3.Vec3
import glm_.vec3.Vec3i
import glm_.vec4.Vec4
import gln.buffer.Usage
import gln.buffer.initArrayBuffer
import gln.clear.glClearColorBuffer
import gln.clear.glClearDepthBuffer
import gln.glf.glf
import gln.glf.semantic
import gln.program.usingProgram
import gln.texture.glBindTexture2d
import gln.uniform.glUniform
import gln.vertexArray.glBindVertexArray
import gln.vertexArray.initVertexArray
import kool.*
import kool.lib.isNotEmpty
import openvr.lib.VREye
import org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT
import org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture
import org.lwjgl.opengl.GL30.*
import uno.kotlin.url
import java.awt.image.DataBufferByte
import javax.imageio.ImageIO

class Scene {

    val volumeInit = 20
    /** cube array */
    val sceneVolume = Vec3i(volumeInit)

    val scale = .3f
    val scaleSpacing = 4f

    var vertexCount = 0
    val vertexArrayName = IntBuffer(1)
    val bufferName = IntBuffer(1)
    val textureName = IntBuffer(1)

    init {
        setupTextureMaps()
        setup()
    }

    fun setupTextureMaps() {

//        val texture = gli.load(ClassLoader.getSystemResource("cube_texture.png").toURI())
//
//        initTexture2d(textureName) {
//            image(texture)
//
//            wrap(clampToEdge, clampToEdge)
//            filter(linear, linear_mmLinear)
//        }
        val image = ImageIO.read("cube_texture.png".url)
        (image.raster.dataBuffer as DataBufferByte).data.toBuffer().use {

            glGenTextures(textureName)
            glActiveTexture(GL_TEXTURE0)
            glBindTexture(GL_TEXTURE_2D, textureName[0])

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, image.width, image.height, 0, GL_RGB, GL_UNSIGNED_BYTE, it)

            glGenerateMipmap(GL_TEXTURE_2D)

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)

            val largest = glGetFloat(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT)
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, largest)

            glBindTexture(GL_TEXTURE_2D, 0)
        }
    }

    /** Purpose: create a sea of cubes  */
    fun setup() {

        val vertDataArray = ArrayList<Float>()

        val matScale = Mat4() scale scale
        val matTranlate = Mat4() translate Vec3(-sceneVolume * scaleSpacing / 2f)

        val mat: Mat4 = matScale * matTranlate

        repeat(sceneVolume.x) {

            repeat(sceneVolume.y) {

                repeat(sceneVolume.z) {

                    addCube(mat, vertDataArray)
                    mat *= Mat4().translate(scaleSpacing, 0f, 0f)
                }
                mat *= Mat4().translate(-sceneVolume.x * scaleSpacing, scaleSpacing, 0f)
            }
            mat *= Mat4().translate(0f, -sceneVolume.y * scaleSpacing, scaleSpacing)
        }
        vertexCount = vertDataArray.size / 5

        initArrayBuffer(bufferName) {

            vertDataArray.toFloatArray().toBuffer().use { vertices ->

                data(vertices, Usage.StaticDraw)
            }
        }

        initVertexArray(vertexArrayName) {

            array(bufferName, glf.pos3_tc2)
        }
    }

    fun addCube(mat: Mat4, vertData: ArrayList<Float>) {

        val a = mat * Vec4(0, 0, 0, 1)
        val b = mat * Vec4(1, 0, 0, 1)
        val c = mat * Vec4(1, 1, 0, 1)
        val d = mat * Vec4(0, 1, 0, 1)
        val e = mat * Vec4(0, 0, 1, 1)
        val f = mat * Vec4(1, 0, 1, 1)
        val g = mat * Vec4(1, 1, 1, 1)
        val h = mat * Vec4(0, 1, 1, 1)

        // triangles instead of quads
        addCubeVertex(e.x, e.y, e.z, 0, 1, vertData) //Front
        addCubeVertex(f.x, f.y, f.z, 1, 1, vertData)
        addCubeVertex(g.x, g.y, g.z, 1, 0, vertData)
        addCubeVertex(g.x, g.y, g.z, 1, 0, vertData)
        addCubeVertex(h.x, h.y, h.z, 0, 0, vertData)
        addCubeVertex(e.x, e.y, e.z, 0, 1, vertData)

        addCubeVertex(b.x, b.y, b.z, 0, 1, vertData) //Back
        addCubeVertex(a.x, a.y, a.z, 1, 1, vertData)
        addCubeVertex(d.x, d.y, d.z, 1, 0, vertData)
        addCubeVertex(d.x, d.y, d.z, 1, 0, vertData)
        addCubeVertex(c.x, c.y, c.z, 0, 0, vertData)
        addCubeVertex(b.x, b.y, b.z, 0, 1, vertData)

        addCubeVertex(h.x, h.y, h.z, 0, 1, vertData) //Top
        addCubeVertex(g.x, g.y, g.z, 1, 1, vertData)
        addCubeVertex(c.x, c.y, c.z, 1, 0, vertData)
        addCubeVertex(c.x, c.y, c.z, 1, 0, vertData)
        addCubeVertex(d.x, d.y, d.z, 0, 0, vertData)
        addCubeVertex(h.x, h.y, h.z, 0, 1, vertData)

        addCubeVertex(a.x, a.y, a.z, 0, 1, vertData) //Bottom
        addCubeVertex(b.x, b.y, b.z, 1, 1, vertData)
        addCubeVertex(f.x, f.y, f.z, 1, 0, vertData)
        addCubeVertex(f.x, f.y, f.z, 1, 0, vertData)
        addCubeVertex(e.x, e.y, e.z, 0, 0, vertData)
        addCubeVertex(a.x, a.y, a.z, 0, 1, vertData)

        addCubeVertex(a.x, a.y, a.z, 0, 1, vertData) //Left
        addCubeVertex(e.x, e.y, e.z, 1, 1, vertData)
        addCubeVertex(h.x, h.y, h.z, 1, 0, vertData)
        addCubeVertex(h.x, h.y, h.z, 1, 0, vertData)
        addCubeVertex(d.x, d.y, d.z, 0, 0, vertData)
        addCubeVertex(a.x, a.y, a.z, 0, 1, vertData)

        addCubeVertex(f.x, f.y, f.z, 0, 1, vertData) //Right
        addCubeVertex(b.x, b.y, b.z, 1, 1, vertData)
        addCubeVertex(c.x, c.y, c.z, 1, 0, vertData)
        addCubeVertex(c.x, c.y, c.z, 1, 0, vertData)
        addCubeVertex(g.x, g.y, g.z, 0, 0, vertData)
        addCubeVertex(f.x, f.y, f.z, 0, 1, vertData)
    }

    fun addCubeVertex(fl0: Float, fl1: Float, fl2: Float, fl3: Int, fl4: Int, vertData: ArrayList<Float>) {
        vertData += fl0
        vertData += fl1
        vertData += fl2
        vertData += fl3.f
        vertData += fl4.f
    }

    val program = ProgramScene()
    val controllerAxes = ControllerAxes()
    val modelProgram = ProgramModel()

    /** Purpose: Renders a scene with respect to nEye.  */
    fun render(eye: VREye) {

        glClearColorBuffer(clearColor)
        glClearDepthBuffer()
        glEnable(GL_DEPTH_TEST)

        if (showCubes) {
            glUseProgram(program.name)
            glUniform(program.matrix, getCurrentViewProjectionMatrix(eye))
            glBindVertexArray(vertexArrayName)
            glActiveTexture(GL_TEXTURE0 + semantic.sampler.DIFFUSE)
            glBindTexture2d(textureName)
            glDrawArrays(GL_TRIANGLES, 0, vertexCount)
            glBindVertexArray()
        }

        val isInputAvailable = hmd.isInputAvailable

        // draw the controller axis lines
        if (isInputAvailable)
            controllerAxes.render(eye)

        // ----- Render Model rendering -----
        glUseProgram(modelProgram.name)

        for (hand in Hand.values()) {

            if (!rHand[hand].showController)
                continue

            rHand[hand].renderModel?.let {
                val matDeviceToTracking = rHand[hand].rmat4Pose
                val matMVP = getCurrentViewProjectionMatrix(eye) * matDeviceToTracking
                glUniformMatrix4fv(modelProgram.matrix, false, matMVP.toFloatBufferStack())

                it.draw()
            }
        }
        glUseProgram(0)
    }

    /** Purpose: Gets a Current View Projection Matrix with respect to nEye, which may be an Eye_Left or an Eye_Right.  */
    fun getCurrentViewProjectionMatrix(eye: VREye) = projection[eye.i] * eyePos[eye.i] * hmdPose

    fun dispose() {

        glDeleteVertexArrays(vertexArrayName)
        glDeleteBuffers(bufferName)
        glDeleteTextures(textureName)
        program.delete()
        controllerAxes.dispose()
        modelProgram.delete()

        vertexArrayName.free()
        bufferName.free()
        textureName.free()
    }

    class ProgramScene : ProgramBase(
            vertSrc = """
                #version 410
                uniform mat4 matrix;
                layout(location = ${semantic.attr.POSITION}) in vec3 position;
                layout(location = ${semantic.attr.TEX_COORD}) in vec2 uvCoords;
                out vec2 uv;
                void main() {
                    uv = uvCoords;
                    gl_Position = matrix * vec4(position, 1);
                }""",
            fragSrc = """
                #version 410 core
                uniform sampler2D myTexture;
                in vec2 uv;
                layout(location = ${semantic.frag.COLOR}) out vec4 outColor;
                void main() {
                    outColor = texture(myTexture, uv);
                }""") {

        init {
            usingProgram(name) { "myTexture".unit = semantic.sampler.DIFFUSE }
        }
    }

    class ProgramModel : ProgramBase(
            vertSrc = """
                #version 410
                uniform mat4 matrix;
                layout(location = ${semantic.attr.POSITION}) in vec3 position;
                layout(location = ${semantic.attr.TEX_COORD}) in vec2 uvCoords;
                out vec2 uv;
                void main() {
                    uv = uvCoords;
                    gl_Position = matrix * vec4(position, 1);
                }""",
            fragSrc = """
                #version 410 core
                uniform sampler2D diffuse;
                in vec2 uv;
                layout(location = ${semantic.frag.COLOR}) out vec4 outputColor;
                void main() {
                    outputColor = texture(diffuse, uv);
                }""") {

        init {
            usingProgram(name) { "diffuse".unit = semantic.sampler.DIFFUSE }
        }
    }

    open class ProgramBase(vertSrc: String, fragSrc: String) : Program(vertSrc, fragSrc) {
        val matrix = glGetUniformLocation(name, "matrix")
    }

    inner class ControllerAxes {

        val program = Program()
        val vertexArrayName = IntBuffer(1)
        val bufferName = IntBuffer(1)
        val vertexData = FloatBuffer(3 * 4 * 3 + 3 * 4)
        var vertCount = 0

        init {
            // Setup the VAO the first time through.

            glGenVertexArrays(vertexArrayName)
            glBindVertexArray(vertexArrayName[0])

            glGenBuffers(bufferName)
            glBindBuffer(GL_ARRAY_BUFFER, bufferName[0])

            glBufferData(GL_ARRAY_BUFFER, vertexData.remSize.L, GL_STREAM_DRAW)

            val stride = 2 * Vec3.size
            var offset = 0L

            glEnableVertexAttribArray(semantic.attr.POSITION)
            glVertexAttribPointer(semantic.attr.POSITION, Vec3.length, GL_FLOAT, false, stride, offset)

            offset += Vec3.size
            glEnableVertexAttribArray(semantic.attr.COLOR)
            glVertexAttribPointer(semantic.attr.COLOR, Vec3.length, GL_FLOAT, false, stride, offset)

            glBindVertexArray(0)
        }

        /** Purpose: Draw all of the controllers as X/Y/Z lines */
        fun update() {

            // Don't attempt to update controllers if input is not available
            if (!hmd.isInputAvailable) return

            vertCount = 0
            trackedControllerCount = 0

            for (hand in Hand.values()) {

                if (!rHand[hand].showController) continue

                val mat = rHand[hand].rmat4Pose

                val center = mat * Vec4(0, 0, 0, 1)

                val stride = 4 * Vec3.length

                for (i in 0..2) {

                    val color = Vec3(0)
                    val point = Vec4(0, 0, 0, 1)
                    point[i] += .05f  // offset in X, Y, Z
                    color[i] = 1  // R, G, B
                    point put (mat * point)

                    center.to(vertexData, stride * i)
                    color.to(vertexData, stride * i + 3)
                    point.to(vertexData, stride * i + 6)
                    color.to(vertexData, stride * i + 9)

                    vertCount += 2
                }

                val start = mat * Vec4(0, 0, -.02f, 1)
                val end = mat * Vec4(0, 0, -39f, 1)
                val color = Vec3(.92f, .92f, .71f)

                start.to(vertexData, stride * 3)
                color.to(vertexData, stride * 3 + 3)

                end.to(vertexData, stride * 3 + 6)
                color.to(vertexData, stride * 3 + 9)

                vertCount += 2
            }

            glBindBuffer(GL_ARRAY_BUFFER, bufferName[0])
            // set vertex data if we have some
            if (vertexData.isNotEmpty())
                glBufferSubData(GL_ARRAY_BUFFER, 0, vertexData)
        }

        fun render(eye: VREye) {

            update()

            glUseProgram(program.name)
            glUniformMatrix4fv(program.matrix, false, getCurrentViewProjectionMatrix(eye).toFloatBufferStack())
            glBindVertexArray(vertexArrayName[0])
            glDrawArrays(GL_LINES, 0, vertCount)
            glBindVertexArray(0)
        }

        fun dispose() {

            glDeleteVertexArrays(vertexArrayName)
            glDeleteBuffers(bufferName)
            program.delete()

            vertexArrayName.free()
            bufferName.free()
            vertexData.free()
        }

        inner class Program : ProgramBase(
                vertSrc = """
                #version 410
                uniform mat4 matrix;
                layout(location = ${semantic.attr.POSITION}) in vec4 position;
                layout(location = ${semantic.attr.COLOR}) in vec3 color;
                out vec4 Color;
                void main() {
                    Color = vec4(color, 1);
                    gl_Position = matrix * position;
                }""",
                fragSrc = """
                #version 410
                in vec4 Color;
                layout(location = ${semantic.frag.COLOR}) out vec4 outputColor;
                void main() {
                    outputColor = Color;
                }""")
    }
}