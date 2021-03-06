package openvr.helloVr_OpenGL

import glm_.vec2.Vec2
import glm_.vec2.Vec2i
import glm_.vec3.Vec3
import gln.buffer.*
import kool.get
import gln.glf.semantic
import gln.texture.glBindTexture2d
import gln.texture.glGenerateMipmap2D
import gln.texture.glTex2dParameter
import gln.texture.glTexImage2D
import gln.vertexArray.glBindVertexArray
import gln.vertexArray.glVertexAttribPointer
import kool.free
import kool.Buffer
import kool.IntBuffer
import openvr.lib.*
import org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT
import org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE
import org.lwjgl.opengl.GL32.glTexImage2DMultisample
import org.lwjgl.openvr.RenderModel
import org.lwjgl.openvr.RenderModelTextureMap
import org.lwjgl.openvr.RenderModelVertex
import java.nio.ByteBuffer

class FrameBufferDesc(val size: Vec2i) {

    object Target {
        val RENDER = 0
        val RESOLVE = 1
        val MAX = 2
    }

    val depthRenderbufferName = IntBuffer(1)
    val textureName = IntBuffer(Target.MAX)
    val frameBufferName = IntBuffer(Target.MAX)

    /** Purpose: Creates a frame buffer. Returns true if the buffer was set up. Throw error if the setup failed.    */
    init {

        glGenFramebuffers(frameBufferName)
        glGenRenderbuffers(depthRenderbufferName)
        glGenTextures(textureName)

        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferName[Target.RENDER])

        glBindRenderbuffer(GL_RENDERBUFFER, depthRenderbufferName[0])
        glRenderbufferStorageMultisample(GL_RENDERBUFFER, 4, GL_DEPTH_COMPONENT, size.x, size.y)
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthRenderbufferName[0])

        glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, textureName[Target.RENDER])
        glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, 4, GL_RGBA8, size.x, size.y, true)
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D_MULTISAMPLE, textureName[Target.RENDER], 0)


        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferName[Target.RESOLVE])

        glBindTexture(GL_TEXTURE_2D, textureName[Target.RESOLVE])
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 0)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, size.x, size.y, 0, GL_RGBA, GL_UNSIGNED_BYTE, null as ByteBuffer?)
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureName[Target.RESOLVE], 0)

        // check FBO status
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw Error("framebuffer incomplete!")

        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    fun render(eye: VREye) {

        glEnable(GL_MULTISAMPLE)

        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferName[Target.RENDER])
        glViewport(0, 0, size.x, size.y)
        scene.render(eye)
        glBindFramebuffer(GL_FRAMEBUFFER, 0)

        glDisable(GL_MULTISAMPLE)

        glBindFramebuffer(GL_READ_FRAMEBUFFER, frameBufferName[Target.RENDER])
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBufferName[Target.RESOLVE])

        glBlitFramebuffer(0, 0, size.x, size.y, 0, 0, size.x, size.y, GL_COLOR_BUFFER_BIT, GL_LINEAR)

        glBindFramebuffer(GL_READ_FRAMEBUFFER, 0)
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0)
    }

    fun dispose() {

        glDeleteRenderbuffers(depthRenderbufferName)
        glDeleteTextures(textureName)
        glDeleteFramebuffers(frameBufferName)

        depthRenderbufferName.free()
        textureName.free()
        frameBufferName.free()
    }
}

class CGLRenderModel(val modelName: String, vrModel: RenderModel, vrDiffuseTexture: RenderModelTextureMap) {

    enum class Buffer { VERTEX, INDEX }

    var bufferName = IntBuffer<Buffer>()
    val vertexArrayName = IntBuffer(1)
    val textureName = IntBuffer(1)
    var vertexCount = 0

    /** Purpose: Allocates and populates the GL resources for a render model */
    init {

        // create and bind a VAO to hold state for this model
        glGenVertexArrays(vertexArrayName)
        glBindVertexArray(vertexArrayName)

        // Populate a vertex buffer
        glGenBuffers(bufferName)
        glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferName[Buffer.VERTEX])
        glBufferData(GL_ARRAY_BUFFER, vrModel.vertices, GL15.GL_STATIC_DRAW)

        // Identify the components in the vertex buffer
        glEnableVertexAttribArray(semantic.attr.POSITION)
        glVertexAttribPointer(semantic.attr.POSITION, Vec3.length, GL_FLOAT, false, RenderModelVertex.SIZEOF, RenderModelVertex.VPOSITION)
        glEnableVertexAttribArray(semantic.attr.TEX_COORD)
        glVertexAttribPointer(semantic.attr.TEX_COORD, Vec2.length, GL_FLOAT, false, RenderModelVertex.SIZEOF, RenderModelVertex.RFTEXTURECOORD)

        // Create and populate the index buffer
        glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, bufferName[Buffer.INDEX])
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, vrModel.indices, GL15.GL_STATIC_DRAW)

        glBindVertexArray()

        // create and populate the texture
        glGenTextures(textureName)
        glActiveTexture(GL_TEXTURE0 + semantic.sampler.DIFFUSE)
        glBindTexture2d(textureName)

        glTexImage2D(GL_RGBA, vrDiffuseTexture.size, GL_RGBA, GL_UNSIGNED_BYTE, vrDiffuseTexture.textureMapData)

        // If this renders black ask McJohn what's wrong.
        glGenerateMipmap2D()

        glTex2dParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTex2dParameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTex2dParameter(GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTex2dParameter(GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)

        val largest = glGetFloat(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT)
        glTex2dParameter(GL_TEXTURE_MAX_ANISOTROPY_EXT, largest)

        glBindTexture2d()

        vertexCount = vrModel.triangleCount * 3
    }

    /** Purpose: Draws the render model */
    fun draw() {

        glBindVertexArray(vertexArrayName)

        glActiveTexture(GL_TEXTURE0 + semantic.sampler.DIFFUSE)
        glBindTexture2d(textureName)

        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_SHORT, 0)

        glBindVertexArray(0)
    }

    /** Purpose: Frees the GL resources for a render model     */
    fun dispose() {

        glDeleteBuffers(bufferName)
        glDeleteVertexArrays(vertexArrayName)
        GL11.glDeleteTextures(textureName)

        bufferName.free()
        vertexArrayName.free()
        textureName.free()
    }
}