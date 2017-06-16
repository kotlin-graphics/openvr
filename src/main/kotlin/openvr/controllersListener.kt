package openvr

/**
 * Created by GBarbieri on 14.03.2017.
 */

abstract class ControllersListener {

    var leftIndex = k_unTrackedDeviceIndexInvalid
    var rightIndex = k_unTrackedDeviceIndexInvalid

    val leftState = VRControllerState_t.ByReference()
    val rightState = VRControllerState_t.ByReference()

    fun init() {

        // Process SteamVR controller state
        var firstFound = false
        repeat(k_unMaxTrackedDeviceCount) {
            if (vrSystem()!!.getTrackedDeviceClass(it) == ETrackedDeviceClass.Controller) {
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

    fun update(event: VREvent_t) = when (event.eventType) {

        EVREventType.TrackedDeviceActivated -> {

            if (vrSystem()!!.getControllerRoleForTrackedDeviceIndex(event.trackedDeviceIndex) == ETrackedControllerRole.LeftHand)
                leftIndex = event.trackedDeviceIndex
            else
                rightIndex = event.trackedDeviceIndex   // right first

            trackedDeviceActivated(event)
        }

        EVREventType.TrackedDeviceDeactivated -> {

            if (event.trackedDeviceIndex == leftIndex)
                leftIndex = k_unTrackedDeviceIndexInvalid
            else if (event.trackedDeviceIndex == rightIndex)
                rightIndex = k_unTrackedDeviceIndexInvalid

            trackedDeviceDeactivated(event)
        }

        EVREventType.TrackedDeviceRoleChanged -> {

            if (event.trackedDeviceIndex == leftIndex || event.trackedDeviceIndex == rightIndex)

                if (vrSystem()!!.getControllerRoleForTrackedDeviceIndex(leftIndex) == ETrackedControllerRole.RightHand
                        || vrSystem()!!.getControllerRoleForTrackedDeviceIndex(rightIndex) == ETrackedControllerRole.LeftHand) {

                    val temp = leftIndex
                    leftIndex = rightIndex
                    rightIndex = temp

                    updateStates()
                }

            trackedDeviceRoleChanged(event)
        }

        EVREventType.PropertyChanged -> propertyChanged(event)

        else -> {
        }
    }

    abstract fun trackedDeviceActivated(event: VREvent_t)
    abstract fun trackedDeviceDeactivated(event: VREvent_t)

    abstract fun trackedDeviceRoleChanged(event: VREvent_t)
    abstract fun propertyChanged(event: VREvent_t)

    fun updateStates() {
        if (leftIndex != k_unTrackedDeviceIndexInvalid)
            vrSystem()!!.getControllerState(leftIndex, leftState, leftState.size())
        if (rightIndex != k_unTrackedDeviceIndexInvalid)
            vrSystem()!!.getControllerState(rightIndex, rightState, rightState.size())
    }
}