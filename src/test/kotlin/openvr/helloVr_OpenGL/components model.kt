package openvr.helloVr_OpenGL

import glm_.vec2.Vec2
import glm_.vec3.Vec3
import gln.glf.semantic
import gln.texture.glBindTexture2d
import gln.texture.glGenerateMipmap2D
import gln.texture.glTex2dParameter
import gln.texture.glTexImage2D
import gln.vertexArray.glBindVertexArray
import gln.vertexArray.glVertexAttribPointer
import kool.IntBuffer
import kool.free
import kool.get
import openvr.lib.*
import openvr.lib.vrRenderModels.freeNative
import org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT
import org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL30.*
import org.lwjgl.openvr.RenderModel
import org.lwjgl.openvr.RenderModelComponentState
import org.lwjgl.openvr.RenderModelTextureMap
import org.lwjgl.openvr.RenderModelVertex

class Model(val name: String) {

    val components = ArrayList<ModelComponent>()
    val componentState = RenderModelComponentState.calloc()

    fun draw(matrixUL: Int) {

        components.forEach {
            //            vrRenderModels.getComponentStateForDevicePath(name, it.name, )
        }
    }

    companion object {

        fun load(name: String): Model? {

            val count = vrRenderModels.getComponentCount(name)
            if (count == 0) return null

            val model = Model(name)

            for (i in 0 until count) {

                val componentName = vrRenderModels.getComponentName(name, i) ?: continue

                val componentPath = vrRenderModels.getComponentRenderModelName(name, componentName) ?: continue

                val renderModel = vrRenderModels.loadRenderModel(componentPath) ?: continue
                val texture = vrRenderModels.loadTexture(renderModel.diffuseTextureId)

                if (texture == null) {
                    renderModel.freeNative()
                    continue
                }

                model.components += ModelComponent(componentName, renderModel, texture)
            }

            return model
        }
    }
}

class ModelComponent(val name: String, vrModel: RenderModel, vrDiffuseTexture: RenderModelTextureMap) {

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