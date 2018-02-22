package main

import com.sun.jna.ptr.PointerByReference
import io.kotlintest.specs.StringSpec
import openvr.lib.*

class models : StringSpec() {

    init {
        "models allocation test" {

            if (System.getenv("TRAVIS") != "true") {

                vrInit(null, EVRApplicationType.Scene)

                val models = listOf(
                        "arrow",
                        "dk2_camera")

                for (i in 0..99)
                    models.forEach(::loadRenderModel)
            }
        }
    }

    fun loadRenderModel(renderModelName: String) {

        // load the model if we didn't find one
        val error = EVRRenderModelError.None

        val rm = vrRenderModels!!

        val pModel = PointerByReference()
        while (true) {
            if (rm.loadRenderModel_Async(renderModelName, pModel) != EVRRenderModelError.Loading)
                break
            Thread.sleep(1)
        }
        val model = RenderModel.ByReference(pModel.value)

        if (error != EVRRenderModelError.None) {
            System.err.println("Unable to load render model $renderModelName - ${error.getName()}")
            return // move on to the next tracked device
        }

//        val pTexture = PointerByReference()
//        while (true) {
//            if (rm.loadTexture_Async(model.diffuseTextureId, pTexture) != EVRRenderModelError.Loading)
//                break
//            Thread.sleep(1)
//        }
//        val texture = RenderModel_TextureMap.ByReference(pTexture.value)
//
//        if (error != EVRRenderModelError.None) {
//            System.err.println("Unable to load render texture id:${model.diffuseTextureId} for render model $renderModelName")
//            rm freeRenderModel model
//            return // move on to the next tracked device
//        }

//        val v = model.rVertexData!!.toArray(model.vertexCount)
//        val i = model.rIndexData!!.pointer.getShortArray(0, model.triangleCount * 3)

//        model.read()
//        model.write()
//        texture.read()
//        texture.write()
//        println(model.pointer)
//        println("   " + model.rVertexData?.pointer)
//        println("   " + model.vertexCount)
//        println("   " + model.rIndexData?.pointer)
//        println("   " + model.triangleCount)
//        println("   " + model.diffuseTextureId)
//        println(texture.pointer)

        try {
            rm freeRenderModel model
//            rm freeTexture texture
        } catch (e: Error) {
            System.err.println(e)
        }
        println()
    }
}