package openvr

import openvr.lib.*
import org.lwjgl.openvr.VREvent

/**
 * Created by GBarbieri on 14.03.2017.
 */

abstract class ControllersListener {

    var leftIndex = vr.trackedDeviceIndexInvalid
    var rightIndex = vr.trackedDeviceIndexInvalid

    val leftState = VRControllerState()
    val rightState = VRControllerState()

    fun init() {

        // Process SteamVR controller state
        var firstFound = false
        repeat(vr.maxTrackedDeviceCount) {
            if (vrSystem.getTrackedDeviceClass(it) == TrackedDeviceClass.Controller) {
                val state =
                        if (firstFound) {
                            leftIndex = it
                            leftState
                        } else {    // right first
                            rightIndex = it
                            rightState
                        }
                firstFound = true
            }
        }
        updateStates()
    }

    fun update(event: VREvent) = when (event.eventType) {

        VREventType.TrackedDeviceActivated -> {

            if (vrSystem.getControllerRoleForTrackedDeviceIndex(event.trackedDeviceIndex) == TrackedControllerRole.LeftHand)
                leftIndex = event.trackedDeviceIndex
            else
                rightIndex = event.trackedDeviceIndex   // right first

            trackedDeviceActivated(event)
        }

        VREventType.TrackedDeviceDeactivated -> {

            if (event.trackedDeviceIndex == leftIndex)
                leftIndex = vr.trackedDeviceIndexInvalid
            else if (event.trackedDeviceIndex == rightIndex)
                rightIndex = vr.trackedDeviceIndexInvalid

            trackedDeviceDeactivated(event)
        }

        VREventType.TrackedDeviceRoleChanged -> {

            if (event.trackedDeviceIndex == leftIndex || event.trackedDeviceIndex == rightIndex)

                if (vrSystem.getControllerRoleForTrackedDeviceIndex(leftIndex) == TrackedControllerRole.RightHand
                        || vrSystem.getControllerRoleForTrackedDeviceIndex(rightIndex) == TrackedControllerRole.LeftHand) {

                    val temp = leftIndex
                    leftIndex = rightIndex
                    rightIndex = temp

                    updateStates()
                }

            trackedDeviceRoleChanged(event)
        }

        VREventType.PropertyChanged -> propertyChanged(event)

        else -> {
        }
    }

    abstract fun trackedDeviceActivated(event: VREvent)
    abstract fun trackedDeviceDeactivated(event: VREvent)

    abstract fun trackedDeviceRoleChanged(event: VREvent)
    abstract fun propertyChanged(event: VREvent)

    fun updateStates() {
        if (leftIndex != vr.trackedDeviceIndexInvalid)
            vrSystem.getControllerState(leftIndex, leftState)
        if (rightIndex != vr.trackedDeviceIndexInvalid)
            vrSystem.getControllerState(rightIndex, rightState)
    }
}