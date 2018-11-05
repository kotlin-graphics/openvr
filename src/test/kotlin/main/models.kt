package main

import io.kotlintest.specs.StringSpec
import lib.vrRenderModels

class models : StringSpec() {

    init {
        "models allocation test" {

            if (System.getenv("TRAVIS") != "true") {

//                vrInit(null, EVRApplicationType.Scene)

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
//        var error: vrRenderModels.Error
//
//        val rm = vrRenderModels!!
//
//        val pModel = PointerByReference()
//        while (true) {
//            error = rm.loadRenderModel_Async(modelName, pModel)
//            if (error != EVRRenderModelError.Loading) break
//            Thread.sleep(1)
//        }
//        val model = RenderModel.ByReference(pModel.value)
//
//        if (error != EVRRenderModelError.None) {
//            System.err.println("Unable to load render model $modelName - ${error.getName()}")
//            return // move on to the next tracked device
//        }

//        val pTexture = PointerByReference()
//        while (true) {
//            if (rm.loadTexture_Async(model.diffuseTextureId, pTexture) != Error.Loading)
//                break
//            Thread.sleep(1)
//        }
//        val texture = RenderModel_TextureMap.ByReference(pTexture.value)
//
//        if (error != Error.None) {
//            System.err.println("Unable to load render texture id:${model.diffuseTextureId} for render model $modelName")
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
//
//        try {
//            rm freeRenderModel model
////            rm freeTexture texture
//        } catch (e: Error) {
//            System.err.println(e)
//        }
        println()
    }
}