package main.helloVr_OpenGL

import glm_.vec2.Vec2i
import kool.intBufferBig
import openvr.lib.EVREye

class FrameBufferDesc(val size: Vec2i) {

    object Target {
        val RENDER = 0
        val RESOLVE = 1
        val MAX = 2
    }

    val depthRenderbufferName = intBufferBig(1)
    val textureName = intBufferBig(Target.MAX)
    val frameBufferName = intBufferBig(Target.MAX)

    /** Purpose: Creates a frame buffer. Returns true if the buffer was set up. Throw error if the setup failed.    */
    init {

//        glGenFramebuffers(Target.MAX, frameBufferName)
//        glGenRenderbuffers(1, depthRenderbufferName)
//        glGenTextures(Target.MAX, textureName)
//
//        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferName[Target.RENDER])
//
//        glBindRenderbuffer(GL_RENDERBUFFER, depthRenderbufferName[0])
//        glRenderbufferStorageMultisample(GL_RENDERBUFFER, 4, GL_DEPTH_COMPONENT, size.x, size.y)
//        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthRenderbufferName[0])
//
//        glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, textureName[Target.RENDER])
//        glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, 4, GL_RGBA8, size.x, size.y, true)
//        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D_MULTISAMPLE, textureName[Target.RENDER], 0)
//
//
//        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferName[Target.RESOLVE])
//
//        glBindTexture(GL_TEXTURE_2D, textureName[Target.RESOLVE])
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 0)
//        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, size.x, size.y, 0, GL_RGBA, GL_UNSIGNED_BYTE, null)
//        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureName[Target.RESOLVE], 0)
//
//        // check FBO status
//        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
//            throw Error("framebuffer incomplete!")
//
//        glBindFramebuffer(GL_FRAMEBUFFER, 0)
    }

    fun render(eye: EVREye) {

//        glEnable(GL_MULTISAMPLE)
//
//        glBindFramebuffer(GL_FRAMEBUFFER, frameBufferName[Target.RENDER])
//        glViewport(0, 0, size.x, size.y)
//        scene.render(eye)
//        glBindFramebuffer(GL_FRAMEBUFFER, 0)
//
//        glDisable(GL_MULTISAMPLE)
//
//        glBindFramebuffer(GL_READ_FRAMEBUFFER, frameBufferName[Target.RENDER])
//        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBufferName[Target.RESOLVE])
//
//        glBlitFramebuffer(0, 0, size.x, size.y, 0, 0, size.x, size.y, GL_COLOR_BUFFER_BIT, GL_LINEAR)
//
//        glBindFramebuffer(GL_READ_FRAMEBUFFER, 0)
//        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0)
    }
}