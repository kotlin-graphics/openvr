package openvr.plugin

import glm_.glm
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import openvr.lib.*
import openvr.lib.TrackedDevicePose_t
import openvr.lib.VRControllerState_t


object Controller {

    enum class ButtonMask(val i: Long) {
        /** reserved    */
        System(1L shl EVRButtonId.System.i),
        ApplicationMenu(1L shl EVRButtonId.ApplicationMenu.i),
        Grip(1L shl EVRButtonId.Grip.i),
        Axis0(1L shl EVRButtonId.Axis0.i),
        Axis1(1L shl EVRButtonId.Axis1.i),
        Axis2(1L shl EVRButtonId.Axis2.i),
        Axis3(1L shl EVRButtonId.Axis3.i),
        Axis4(1L shl EVRButtonId.Axis4.i),
        SteamVR_Touchpad(1L shl EVRButtonId.SteamVR_Touchpad.i),
        SteamVR_Trigger(1L shl EVRButtonId.SteamVR_Trigger.i);
    }

    private val _transform = Utils.RigidTransform()

    var touchpadMoveCallback: ((Boolean, Vec2) -> Unit)? = null
    var hairTriggerMoveCallback: ((Boolean, Float) -> Unit)? = null

    class Device(val index: Int) {

        var _frameCount = -1

        var valid = false
            private set

        var left = true

        val connected get() = update().run { _pose.bDeviceIsConnected }
        val hasTracking get() = update().run { _pose.bPoseIsValid }
        val outOfRange get() = update().run { _pose.eTrackingResult == ETrackingResult.Running_OutOfRange || _pose.eTrackingResult == ETrackingResult.Calibrating_OutOfRange }
        val calibrating get() = update().run { _pose.eTrackingResult == ETrackingResult.Calibrating_InProgress || _pose.eTrackingResult == ETrackingResult.Calibrating_OutOfRange }
        val uninitialized get() = update().run { _pose.eTrackingResult == ETrackingResult.Uninitialized }

        /*  These values are only accurate for the last controller state change (e.g. trigger release),
            and by definition, will always lag behind the predicted visual poses that drive SteamVR_TrackedObjects
            since they are sync'd to the input timestamp that caused them to update.    */
        val transform get() = update().run { _transform put _pose.mDeviceToAbsoluteTracking }
        val velocity get() = update().run { _velocity.put(_pose.vVelocity.x, _pose.vVelocity.y, -_pose.vVelocity.z) }
        val angularVelocity get() = update().run { _angularVelocity.put(-_pose.vAngularVelocity.x, -_pose.vAngularVelocity.y, _pose.vAngularVelocity.z) }
        val state get() = update().run { _state }
        val prevState get() = update().run { _prevState }
        val pose get() = update().run { _pose }

        private val _velocity = Vec3()
        private val _angularVelocity = Vec3()
        private val _state = VRControllerState_t.ByReference()
        private val _prevState = VRControllerState_t.ByReference()
        private val _pose = TrackedDevicePose_t.ByReference()
        private var prevFrameCount = -1
        private val prevTouchpadPos = Vec2()
        /** amount touchpad position must be increased or released on a single axes to change state   */
        var touchpadDelta = 0.1f

        fun update(frameCount: Int = _frameCount) {
            if (frameCount != prevFrameCount) {
                prevFrameCount = frameCount
                vrSystem?.let {
                    valid = it.getControllerStateWithPose(Render.trackingSpace, index, _state, _state.size(), _pose)
                    if (ButtonMask.SteamVR_Touchpad.touch)
                        touchpadMoveCallback?.let {
                            if (prevTouchpadPos.x - _state.axis0.pos.x >= touchpadDelta || prevTouchpadPos.y - _state.axis0.pos.y >= touchpadDelta) {
                                it.invoke(left, _state.axis0.pos)
                                prevTouchpadPos put _state.axis0.pos
                            }
                        }
                }
                updateHairTrigger()
            }
        }

        val ButtonMask.press get() = update().let { _state.ulButtonPressed has this }
        val ButtonMask.pressDown get() = update().let { _state.ulButtonPressed has this && _prevState.ulButtonPressed hasnt this }
        val ButtonMask.pressUp get() = update().let { _state.ulButtonPressed hasnt this && _prevState.ulButtonPressed has this }

        val EVRButtonId.press get() = update().let { _state.ulButtonPressed has this }
        val EVRButtonId.pressDown get() = update().let { _state.ulButtonPressed has this && _prevState.ulButtonPressed hasnt this }
        val EVRButtonId.pressUp get() = update().let { _state.ulButtonPressed hasnt this && _prevState.ulButtonPressed has this }

        val ButtonMask.touch get() = update().let { _state.ulButtonTouched has this }
        val ButtonMask.touchDown get() = update().let { _state.ulButtonTouched has this && _prevState.ulButtonTouched hasnt this }
        val ButtonMask.touchUp get() = update().let { _state.ulButtonTouched hasnt this && _prevState.ulButtonTouched has this }

        val EVRButtonId.touch get() = update().let { _state.ulButtonTouched has this }
        val EVRButtonId.touchDown get() = update().let { _state.ulButtonTouched has this && _prevState.ulButtonTouched hasnt this }
        val EVRButtonId.touchUp get() = update().let { _state.ulButtonTouched hasnt this && _prevState.ulButtonTouched has this }

        val _axis = Vec2()
        fun axis(buttonId: EVRButtonId = EVRButtonId.SteamVR_Touchpad): Vec2 {
            update()
            return _axis.apply {
                when (buttonId.i - EVRButtonId.Axis0.i) {
                    0 -> put(_state.axis0.x, _state.axis0.y)
                    1 -> put(_state.axis1.x, _state.axis1.y)
                    2 -> put(_state.axis2.x, _state.axis2.y)
                    3 -> put(_state.axis3.x, _state.axis3.y)
                    4 -> put(_state.axis4.x, _state.axis4.y)
                    else -> put(0)
                }
            }
        }

        fun triggerHapticPulse(durationMicroSec: Short = 500, buttonId: EVRButtonId = EVRButtonId.SteamVR_Touchpad) =
                vrSystem?.triggerHapticPulse(index, buttonId.i - EVRButtonId.Axis0.i, durationMicroSec)


        /** amount trigger must be pulled or released to change state   */
        var hairTriggerDelta = 0.1f
        private var hairTriggerLimit = 0f
        private var hairTriggerState = false
        private var hairTriggerPrevState = false

        fun updateHairTrigger() {
            hairTriggerPrevState = hairTriggerState
            val value = _state.axis1.x  // trigger
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
        private infix fun Long.has(buttonId: EVRButtonId) = (this and buttonId.mask) != 0L
        private infix fun Long.hasnt(buttonId: EVRButtonId) = (this and buttonId.mask) == 0L
    }

    val devices = Array(k_unMaxTrackedDeviceCount, { Device(it) })

    /** updates the controllers scanning [0, k_unMaxTrackedDeviceCount) */
    fun update(frameCount: Int) {
        for (i in 0 until k_unMaxTrackedDeviceCount) devices[i].update(frameCount)
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
    fun deviceIndex(relation: DeviceRelation, deviceClass: ETrackedDeviceClass = ETrackedDeviceClass.Controller,
                    relativeTo: Int = k_unTrackedDeviceIndex_Hmd): Int {

        var result = -1

        val invXform =
                if (relativeTo < k_unMaxTrackedDeviceCount)
                    devices[relativeTo].transform.inverse_()
                else
                    _transform.apply { pos put 0; rot.put(1f, 0f, 0f, 0f) }

        val system = vrSystem ?: return result

        var best = -Float.MAX_VALUE
        for (i in 0 until k_unMaxTrackedDeviceCount) {
            if (i == relativeTo || system.getTrackedDeviceClass(i) != deviceClass) continue
            if (!devices[i].connected) continue
            if (relation == DeviceRelation.First) return i
            var score = 0f
            val pos = invXform * devices[i].transform.pos
            score = when (relation) {
                DeviceRelation.FarthestRight -> pos.x
                DeviceRelation.FarthestLeft -> -pos.x
                else -> {
                    val dir = Vec3(pos.x, 0f, pos.z).normalize_()
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