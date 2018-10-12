package openvr.plugin

import glm_.glm
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import lib.*
import org.lwjgl.openvr.TrackedDevicePose
import org.lwjgl.openvr.VRControllerState


object Controller {

    enum class ButtonMask(val i: Long) {
        /** reserved    */
        System(1L shl VRButtonId.System.i),
        ApplicationMenu(1L shl VRButtonId.ApplicationMenu.i),
        Grip(1L shl VRButtonId.Grip.i),
        Axis0(1L shl VRButtonId.Axis0.i),
        Axis1(1L shl VRButtonId.Axis1.i),
        Axis2(1L shl VRButtonId.Axis2.i),
        Axis3(1L shl VRButtonId.Axis3.i),
        Axis4(1L shl VRButtonId.Axis4.i),
        SteamVR_Touchpad(1L shl VRButtonId.SteamVR_Touchpad.i),
        SteamVR_Trigger(1L shl VRButtonId.SteamVR_Trigger.i);
    }

    private val _transform = Utils.RigidTransform()

    var touchpadMoveCallback: ((Boolean, Vec2) -> Unit)? = null
    var hairTriggerMoveCallback: ((Boolean, Float) -> Unit)? = null

    class Device(val index: Int) {

        var _frameCount = -1

        var valid = false
            private set

        var left = true

        val connected get() = update().run { _pose.deviceIsConnected }
        val hasTracking get() = update().run { _pose.poseIsValid }

        val outOfRange get() = update().run { _pose.trackingResult == TrackingResult.Running_OutOfRange || _pose.trackingResult == TrackingResult.Calibrating_OutOfRange }
        val calibrating get() = update().run { _pose.trackingResult == TrackingResult.Calibrating_InProgress || _pose.trackingResult == TrackingResult.Calibrating_OutOfRange }
        val uninitialized get() = update().run { _pose.trackingResult == TrackingResult.Uninitialized }

        /*  These values are only accurate for the last controller state change (e.g. trigger release),
            and by definition, will always lag behind the predicted visual poses that drive SteamVR_TrackedObjects
            since they are sync'd to the input timestamp that caused them to update.    */
        val transform get() = update().run { _transform put _pose.deviceToAbsoluteTracking }
        val velocity get() = update().run { _velocity.put(_pose.velocity.x, _pose.velocity.y, -_pose.velocity.z) }
        val angularVelocity get() = update().run { _angularVelocity.put(-_pose.angularVelocity.x, -_pose.angularVelocity.y, _pose.angularVelocity.z) }
        val state get() = update().run { _state }
        val prevState get() = update().run { _prevState }
        val pose get() = update().run { _pose }

        private val _velocity = Vec3()
        private val _angularVelocity = Vec3()
        private val _state = VRControllerState.calloc()
        private val _prevState = VRControllerState.calloc()
        private val _pose = TrackedDevicePose.calloc()
        private var prevFrameCount = -1
        private val prevTouchpadPos = Vec2()
        /** amount touchpad position must be increased or released on a single axes to change state   */
        var touchpadDelta = 0.1f

        fun update(frameCount: Int = _frameCount) {
            if (frameCount != prevFrameCount) {
                prevFrameCount = frameCount
                valid = vrSystem.getControllerStateWithPose(Render.trackingSpace, index, _state, _pose)
                if (ButtonMask.SteamVR_Touchpad.touch)
                    touchpadMoveCallback?.let {
                        if (prevTouchpadPos.x - _state.axis[0].x >= touchpadDelta || prevTouchpadPos.y - _state.axis[0].y >= touchpadDelta) {
                            it(left, _state.axis[0].pos)
                            prevTouchpadPos put _state.axis[0].pos
                        }
                    }
                updateHairTrigger()
            }
        }

        val ButtonMask.press get() = update().let { _state.buttonPressed has this }
        val ButtonMask.pressDown get() = update().let { _state.buttonPressed has this && _prevState.buttonPressed hasnt this }
        val ButtonMask.pressUp get() = update().let { _state.buttonPressed hasnt this && _prevState.buttonPressed has this }

        val VRButtonId.press get() = update().let { _state.buttonPressed has this }
        val VRButtonId.pressDown get() = update().let { _state.buttonPressed has this && _prevState.buttonPressed hasnt this }
        val VRButtonId.pressUp get() = update().let { _state.buttonPressed hasnt this && _prevState.buttonPressed has this }

        val ButtonMask.touch get() = update().let { _state.buttonTouched has this }
        val ButtonMask.touchDown get() = update().let { _state.buttonTouched has this && _prevState.buttonTouched hasnt this }
        val ButtonMask.touchUp get() = update().let { _state.buttonTouched hasnt this && _prevState.buttonTouched has this }

        val VRButtonId.touch get() = update().let { _state.buttonTouched has this }
        val VRButtonId.touchDown get() = update().let { _state.buttonTouched has this && _prevState.buttonTouched hasnt this }
        val VRButtonId.touchUp get() = update().let { _state.buttonTouched hasnt this && _prevState.buttonTouched has this }

        val _axis = Vec2()
        fun axis(buttonId: VRButtonId = VRButtonId.SteamVR_Touchpad): Vec2 {
            update()
            return _axis.apply {
                when (buttonId.i - VRButtonId.Axis0.i) {
                    0 -> put(_state.axis[0].x, _state.axis[0].y)
                    1 -> put(_state.axis[1].x, _state.axis[1].y)
                    2 -> put(_state.axis[2].x, _state.axis[2].y)
                    3 -> put(_state.axis[3].x, _state.axis[3].y)
                    4 -> put(_state.axis[4].x, _state.axis[4].y)
                    else -> put(0)
                }
            }
        }

        fun triggerHapticPulse(durationMicroSec: Int = 500, buttonId: VRButtonId = VRButtonId.SteamVR_Touchpad) =
                vrSystem.triggerHapticPulse(index, buttonId.i - VRButtonId.Axis0.i, durationMicroSec)


        /** amount trigger must be pulled or released to change state   */
        var hairTriggerDelta = 0.1f
        private var hairTriggerLimit = 0f
        private var hairTriggerState = false
        private var hairTriggerPrevState = false

        fun updateHairTrigger() {
            hairTriggerPrevState = hairTriggerState
            val value = _state.axis[1].x  // trigger
            if (hairTriggerState) {
                if (value < hairTriggerLimit - hairTriggerDelta || value <= 0f)
                    hairTriggerState = false
            } else if (value > hairTriggerLimit + hairTriggerDelta || value >= 1f)
                hairTriggerState = true
            hairTriggerLimit = if (hairTriggerState) glm.max(hairTriggerLimit, value) else glm.min(hairTriggerLimit, value)
        }

        val hairTrigger get() = update().let { hairTriggerState }
        val hairTriggerDown get() = update().let { hairTriggerState && !hairTriggerPrevState }
        val hairTriggerUp get() = update().let { !hairTriggerState && hairTriggerPrevState }

        private infix fun Long.has(buttonMask: ButtonMask) = (this and buttonMask.i) != 0L
        private infix fun Long.hasnt(buttonMask: ButtonMask) = (this and buttonMask.i) == 0L
        private infix fun Long.has(buttonId: VRButtonId) = (this and buttonId.mask) != 0L
        private infix fun Long.hasnt(buttonId: VRButtonId) = (this and buttonId.mask) == 0L
    }

    val devices = Array(vr.maxTrackedDeviceCount) { Device(it) }

    /** updates the controllers scanning [0, maxTrackedDeviceCount) */
    fun update(frameCount: Int) {
        for (i in 0 until vr.maxTrackedDeviceCount)
            devices[i].update(frameCount)
    }

    /** This helper can be used in a variety of ways.  Beware that indices may change as new devices are dynamically
     *  added or removed, controllers are physically swapped between hands, arms crossed, etc.  */
    enum class DeviceRelation {
        First,
        // radially
        Leftmost,
        Rightmost,
        // distance - also see vr.hmd.GetSortedTrackedDeviceIndicesOfClass
        FarthestLeft,
        FarthestRight
    }

    /** @return use -1 for absolute tracking space  */
    fun deviceIndex(relation: DeviceRelation, deviceClass: TrackedDeviceClass = TrackedDeviceClass.Controller,
                    relativeTo: Int = vr.trackedDeviceIndex_Hmd): Int {

        var result = -1

        val invXform =
                if (relativeTo < vr.maxTrackedDeviceCount)
                    devices[relativeTo].transform.inverse_()
                else
                    _transform.apply { pos put 0; rot.put(1f, 0f, 0f, 0f) }

        val system = vrSystem ?: return result

        var best = -Float.MAX_VALUE
        for (i in 0 until vr.maxTrackedDeviceCount) {
            if (i == relativeTo || system.getTrackedDeviceClass(i) != deviceClass) continue
            if (!devices[i].connected) continue
            if (relation == DeviceRelation.First) return i
            var score: Float
            val pos = invXform * devices[i].transform.pos
            score = when (relation) {
                DeviceRelation.FarthestRight -> pos.x
                DeviceRelation.FarthestLeft -> -pos.x
                else -> {
                    val dir = Vec3(pos.x, 0f, pos.z).normalizeAssign()
                    val dot = dir dot Vec3(0, 0, -1)// TODO check and maybe implement in glm Vector3.forward)
                    val cross = dir cross Vec3(0, 0, -1) // Vector3.forward)
                    if (relation == DeviceRelation.Leftmost)
                        if (cross.y > 0f) 2f - dot else dot
                    else
                        if (cross.y < 0f) 2f - dot else dot
                }
            }
            if (score > best) {
                result = i
                best = score
            }
        }
        return result
    }
}