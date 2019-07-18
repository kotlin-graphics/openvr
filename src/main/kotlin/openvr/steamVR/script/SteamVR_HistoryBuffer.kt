package openvr.steamVR.script

import glm_.quat.Quat
import glm_.vec3.Vec3

//======= Copyright (c) Valve Corporation, All rights reserved. ===============

open class SteamVR_RingBuffer<T : Any>(val clazz: Class<T>, size: Int) {

    val default: T
        get() = clazz.getDeclaredConstructor().newInstance()

    protected val buffer = Array<Any?>(size) { default }
    protected var currentIndex = 0
    protected var lastElement: T? = null

    operator fun plus(newElement: T) {
        buffer[currentIndex] = newElement
        stepForward()
    }

    fun stepForward() {
        lastElement = buffer[currentIndex] as T?

        currentIndex++
        if (currentIndex >= buffer.size)
            currentIndex = 0

        cleared = false
    }

    operator fun get(index: Int): T =
            buffer[if (index < 0) index + buffer.size else index] as T

//    public virtual T GetLast()
//    {
//        return lastElement
//    }

    val lastIndex: Int
        get() {
            var lastIndex = currentIndex - 1
            if (lastIndex < 0)
                lastIndex += buffer.size

            return lastIndex
        }

    private var cleared = false

    fun clear() {
        if (cleared)
            return

        if (buffer == null)
            return

        for (index in buffer.indices)
            buffer[index] = default

        lastElement = default

        currentIndex = 0

        cleared = true
    }

    companion object {
        inline operator fun <reified T : Any> invoke(size: Int): SteamVR_RingBuffer<T> = SteamVR_RingBuffer(T::class.java, size)
    }
}

class SteamVR_HistoryBuffer(size: Int) : SteamVR_RingBuffer<SteamVR_HistoryStep>(SteamVR_HistoryStep::class.java, size) {

    fun update(position: Vec3, rotation: Quat, velocity: Vec3, angularVelocity: Vec3) {

        if (buffer[currentIndex] == null)
            buffer[currentIndex] = SteamVR_HistoryStep()

        (buffer[currentIndex] as SteamVR_HistoryStep).also {
            it.position put position
            it.rotation put rotation
            it.velocity put velocity
            it.angularVelocity put angularVelocity
            it.timeInTicks = System.currentTimeMillis()
        }
        stepForward()
    }

    fun getVelocityMagnitudeTrend(toIndex_: Int = -1, fromIndex_: Int = -1): Float {

        var toIndex = toIndex_
        if (toIndex == -1)
            toIndex = currentIndex - 1

        if (toIndex < 0)
            toIndex += buffer.size

        var fromIndex = fromIndex_
        if (fromIndex == -1)
            fromIndex = toIndex - 1

        if (fromIndex < 0)
            fromIndex += buffer.size

        val toStep = buffer[toIndex] as SteamVR_HistoryStep?
        val fromStep = buffer[fromIndex] as SteamVR_HistoryStep?

        return when {
            toStep?.isValid == true && fromStep?.isValid == true -> toStep.velocity.length() - fromStep.velocity.length()
            else -> 0f
        }
    }

    val SteamVR_HistoryStep.isValid: Boolean
        get() = timeInTicks != -1L

    fun getTopVelocity(forFrames_: Int, addFrames: Int = 0): Int {

        var forFrames = forFrames_
        var topFrame = currentIndex
        var topVelocitySqr = 0f

        var currentFrame = currentIndex

        while (forFrames > 0) {
            forFrames--
            currentFrame--

            if (currentFrame < 0)
                currentFrame = buffer.lastIndex

            val currentStep = buffer[currentFrame] as SteamVR_HistoryStep?

            if (currentStep?.isValid == false)
                break

            val currentSqr = currentStep!!.velocity.length()
            if (currentSqr > topVelocitySqr) {
                topFrame = currentFrame
                topVelocitySqr = currentSqr
            }
        }

        topFrame += addFrames

        if (topFrame >= buffer.size)
            topFrame -= buffer.size

        return topFrame
    }

    fun getAverageVelocities(velocity: Vec3, angularVelocity: Vec3, forFrames_: Int, startFrame_: Int = -1) {

        velocity put 0f
        angularVelocity put 0f

        var startFrame = startFrame_
        if (startFrame == -1)
            startFrame = currentIndex - 1

        if (startFrame < 0)
            startFrame = buffer.lastIndex

        var forFrames = forFrames_
        var endFrame = startFrame - forFrames

        if (endFrame < 0)
            endFrame += buffer.size

        val totalVelocity = Vec3()
        val totalAngularVelocity = Vec3()
        var totalFrames = 0f
        var currentFrame = startFrame
        while (forFrames > 0) {
            forFrames--
            currentFrame--

            if (currentFrame < 0)
                currentFrame = buffer.lastIndex

            val currentStep = buffer[currentFrame] as SteamVR_HistoryStep?

            if (currentStep?.isValid == false)
                break

            totalFrames++

            totalVelocity += currentStep!!.velocity
            totalAngularVelocity += currentStep!!.angularVelocity
        }

        velocity put (totalVelocity / totalFrames)
        angularVelocity put (totalAngularVelocity / totalFrames)
    }
}

class SteamVR_HistoryStep {
    val position = Vec3()
    val rotation = Quat()

    val velocity = Vec3()

    val angularVelocity = Vec3()

    var timeInTicks = -1L
}