//package openvr
//
///**
// * Created by GBarbieri on 14.03.2017.
// */
//
//abstract class ControllersListener {
//
//    var leftIndex = trackedDeviceIndexInvalid
//    var rightIndex = trackedDeviceIndexInvalid
//
//    val leftState = VRControllerState.ByReference()
//    val rightState = VRControllerState.ByReference()
//
//    fun init() {
//
//        // Process SteamVR controller state
//        var firstFound = false
//        repeat(maxTrackedDeviceCount) {
//            if (vrSystem()!!.getTrackedDeviceClass(it) == ETrackedDeviceClass.Controller) {
//                val state =
//                        if (firstFound) {
//                            leftIndex = it
//                            leftState
//                        } else {    // right first
//                            rightIndex = it
//                            rightState
//                        }
//                firstFound = true
//            }
//        }
//        updateStates()
//    }
//
//    fun update(event: VREvent) = when (event.eventType) {
//
//        EVREventType.TrackedDeviceActivated -> {
//
//            if (vrSystem()!!.getControllerRoleForTrackedDeviceIndex(event.trackedDeviceIndex) == ETrackedControllerRole.LeftHand)
//                leftIndex = event.trackedDeviceIndex
//            else
//                rightIndex = event.trackedDeviceIndex   // right first
//
//            trackedDeviceActivated(event)
//        }
//
//        EVREventType.TrackedDeviceDeactivated -> {
//
//            if (event.trackedDeviceIndex == leftIndex)
//                leftIndex = trackedDeviceIndexInvalid
//            else if (event.trackedDeviceIndex == rightIndex)
//                rightIndex = trackedDeviceIndexInvalid
//
//            trackedDeviceDeactivated(event)
//        }
//
//        EVREventType.TrackedDeviceRoleChanged -> {
//
//            if (event.trackedDeviceIndex == leftIndex || event.trackedDeviceIndex == rightIndex)
//
//                if (vrSystem()!!.getControllerRoleForTrackedDeviceIndex(leftIndex) == ETrackedControllerRole.RightHand
//                        || vrSystem()!!.getControllerRoleForTrackedDeviceIndex(rightIndex) == ETrackedControllerRole.LeftHand) {
//
//                    val temp = leftIndex
//                    leftIndex = rightIndex
//                    rightIndex = temp
//
//                    updateStates()
//                }
//
//            trackedDeviceRoleChanged(event)
//        }
//
//        EVREventType.PropertyChanged -> propertyChanged(event)
//
//        else -> {
//        }
//    }
//
//    abstract fun trackedDeviceActivated(event: VREvent)
//    abstract fun trackedDeviceDeactivated(event: VREvent)
//
//    abstract fun trackedDeviceRoleChanged(event: VREvent)
//    abstract fun propertyChanged(event: VREvent)
//
//    fun updateStates() {
//        if (leftIndex != trackedDeviceIndexInvalid)
//            vrSystem()!!.getControllerState(leftIndex, leftState, leftState.size())
//        if (rightIndex != trackedDeviceIndexInvalid)
//            vrSystem()!!.getControllerState(rightIndex, rightState, rightState.size())
//    }
//}