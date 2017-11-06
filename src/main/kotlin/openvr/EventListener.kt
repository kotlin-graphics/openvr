package openvr

import glm_.glm
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import openvr.lib.*
import openvr.plugin.Render
import openvr.plugin.Utils

open class EventListener(val hmd: IVRSystem) {

    val states = Array(k_unMaxTrackedDeviceCount, { VRControllerState_t.ByReference() })

    var left = -1
    var right = -1

    val devices = Array(k_unMaxTrackedDeviceCount, { Device(it) })

    init {
        updateRoles()
    }

    private fun updateRoles() {
        left = -1
        right = -1
        for (i in 0 until k_unMaxTrackedDeviceCount) {
            if (hmd.getTrackedDeviceClass(i) == ETrackedDeviceClass.Controller && hmd.getControllerState(i, states[i], states[i].size()))
                if (left == -1) left = i
                else right = i
        }
        devices.getOrNull(left)?.left = true
        devices.getOrNull(right)?.left = false
        println("new roles, left: $left, right: $right")
    }

    val event = VREvent_t.ByReference()
    var frameCount = 0

    fun poll() {

        while (hmd.pollNextEvent(event, event.size())) process()

        devices.getOrNull(left)?.update(frameCount)
        devices.getOrNull(right)?.update(frameCount)

        frameCount++
    }

    private fun process() {
        if(event.eventType_internal == 10005) return // bug https://github.com/ValveSoftware/openvr/issues/420
        when (event.eventType) {
            EVREventType.TrackedDeviceActivated -> updateRoles().also { trackedDeviceActivated(event.trackedDeviceIndex == left) }
            EVREventType.TrackedDeviceDeactivated -> updateRoles().also { trackedDeviceDeactivated(event.trackedDeviceIndex == left) }
            EVREventType.TrackedDeviceUpdated -> updateRoles().also { trackedDeviceUpdated(event.trackedDeviceIndex == left) }
            EVREventType.TrackedDeviceUserInteractionStarted -> updateRoles().also { trackedDeviceUserInteractionStarted(event.trackedDeviceIndex == left) }
            EVREventType.TrackedDeviceUserInteractionEnded -> updateRoles().also { trackedDeviceUserInteractionEnded(event.trackedDeviceIndex == left) }
            EVREventType.IpdChanged -> ipdChanged()
            EVREventType.EnterStandbyMode -> enterStandbyMode()
            EVREventType.LeaveStandbyMode -> leaveStandbyMode()
            EVREventType.TrackedDeviceRoleChanged -> updateRoles().also { trackedDeviceRoleChanged(event.trackedDeviceIndex == left) }
            EVREventType.WatchdogWakeUpRequested -> watchdogWakeUpRequested()
            EVREventType.LensDistortionChanged -> lensDistortionChanged()
            EVREventType.PropertyChanged -> propertyChanged()
            EVREventType.WirelessDisconnect -> wirelessDisconnect()
            EVREventType.WirelessReconnect -> wirelessReconnect()
            EVREventType.ButtonPress -> buttonPress(event.trackedDeviceIndex == left, VREvent_Controller_t(event.data.pointer).button)
            EVREventType.ButtonUnpress -> buttonUnpress(event.trackedDeviceIndex == left, VREvent_Controller_t(event.data.pointer).button)
            EVREventType.ButtonTouch -> buttonTouch(event.trackedDeviceIndex == left, VREvent_Controller_t(event.data.pointer).button)
            EVREventType.ButtonUntouch -> buttonUntouch(event.trackedDeviceIndex == left, VREvent_Controller_t(event.data.pointer).button)
            EVREventType.MouseMove -> mouseMove()
            EVREventType.MouseButtonDown -> mouseButtonDown()
            EVREventType.MouseButtonUp -> mouseButtonUp()
            EVREventType.FocusEnter -> focusEnter()
            EVREventType.FocusLeave -> focusLeave()
            EVREventType.Scroll -> scroll()
            EVREventType.TouchPadMove -> {
                val state = VRControllerState_t.ByReference()
                touchpadMove(event.trackedDeviceIndex == left,
                        if (hmd.getControllerState(event.trackedDeviceIndex, state, state.size())) Vec2()
                        else state.axis0.pos)
            }
            EVREventType.OverlayFocusChanged -> overlayFocusChanged()
        // EVREventType.TriggerMove JVM specific
            EVREventType.InputFocusCaptured -> inputFocusCaptured()
            EVREventType.InputFocusReleased -> inputFocusReleased()
            EVREventType.SceneFocusLost -> sceneFocusLost()
            EVREventType.SceneFocusGained -> sceneFocusGained()
            EVREventType.SceneApplicationChanged -> sceneApplicationChanged()
            EVREventType.SceneFocusChanged -> sceneFocusChanged()
            EVREventType.InputFocusChanged -> inputFocusChanged()
            EVREventType.SceneApplicationSecondaryRenderingStarted -> sceneApplicationSecondaryRenderingStarted()
            EVREventType.HideRenderModels -> hideRenderModels()
            EVREventType.ShowRenderModels -> showRenderModels()
            EVREventType.OverlayShown -> overlayShown()
            EVREventType.OverlayHidden -> overlayHidden()
            EVREventType.DashboardActivated -> dashboardActivated()
            EVREventType.DashboardDeactivated -> dashboardDeactivated()
            EVREventType.DashboardThumbSelected -> dashboardThumbSelected()
            EVREventType.DashboardRequested -> dashboardRequested()
            EVREventType.ResetDashboard -> resetDashboard()
            EVREventType.RenderToast -> renderToast()
            EVREventType.ImageLoaded -> imageLoaded()
            EVREventType.ShowKeyboard -> showKeyboard()
            EVREventType.HideKeyboard -> hideKeyboard()
            EVREventType.OverlayGamepadFocusGained -> overlayGamepadFocusGained()
            EVREventType.OverlayGamepadFocusLost -> overlayGamepadFocusLost()
            EVREventType.OverlaySharedTextureChanged -> overlaySharedTextureChanged()
            EVREventType.DashboardGuideButtonDown -> dashboardGuideButtonDown()
            EVREventType.DashboardGuideButtonUp -> dashboardGuideButtonUp()
            EVREventType.ScreenshotTriggered -> screenshotTriggered()
            EVREventType.ImageFailed -> imageFailed()
            EVREventType.DashboardOverlayCreated -> dashboardOverlayCreated()
            EVREventType.RequestScreenshot -> requestScreenshot()
            EVREventType.ScreenshotTaken -> screenshotTaken()
            EVREventType.ScreenshotFailed -> screenshotFailed()
            EVREventType.SubmitScreenshotToDashboard -> submitScreenshotToDashboard()
            EVREventType.ScreenshotProgressToDashboard -> screenshotProgressToDashboard()
            EVREventType.PrimaryDashboardDeviceChanged -> primaryDashboardDeviceChanged()
            EVREventType.Notification_Shown -> notification_Shown()
            EVREventType.Notification_Hidden -> notification_Hidden()
            EVREventType.Notification_BeginInteraction -> notification_BeginInteraction()
            EVREventType.Notification_Destroyed -> notification_Destroyed()
            EVREventType.Quit -> quit()
            EVREventType.ProcessQuit -> processQuit()
            EVREventType.QuitAborted_UserPrompt -> quitAborted_UserPrompt()
            EVREventType.QuitAcknowledged -> quitAcknowledged()
            EVREventType.DriverRequestedQuit -> driverRequestedQuit()
            EVREventType.ChaperoneDataHasChanged -> chaperoneDataHasChanged()
            EVREventType.ChaperoneUniverseHasChanged -> chaperoneUniverseHasChanged()
            EVREventType.ChaperoneTempDataHasChanged -> chaperoneTempDataHasChanged()
            EVREventType.ChaperoneSettingsHaveChanged -> chaperoneSettingsHaveChanged()
            EVREventType.SeatedZeroPoseReset -> seatedZeroPoseReset()
            EVREventType.AudioSettingsHaveChanged -> audioSettingsHaveChanged()
            EVREventType.BackgroundSettingHasChanged -> backgroundSettingHasChanged()
            EVREventType.CameraSettingsHaveChanged -> cameraSettingsHaveChanged()
            EVREventType.ReprojectionSettingHasChanged -> reprojectionSettingHasChanged()
            EVREventType.ModelSkinSettingsHaveChanged -> modelSkinSettingsHaveChanged()
            EVREventType.EnvironmentSettingsHaveChanged -> environmentSettingsHaveChanged()
            EVREventType.PowerSettingsHaveChanged -> powerSettingsHaveChanged()
            EVREventType.EnableHomeAppSettingsHaveChanged -> enableHomeAppSettingsHaveChanged()
            EVREventType.StatusUpdate -> statusUpdate()
            EVREventType.MCImageUpdated -> mcImageUpdated()
            EVREventType.FirmwareUpdateStarted -> firmwareUpdateStarted()
            EVREventType.FirmwareUpdateFinished -> firmwareUpdateFinished()
            EVREventType.KeyboardClosed -> keyboardClosed()
            EVREventType.KeyboardCharInput -> keyboardCharInput()
            EVREventType.KeyboardDone -> keyboardDone()
            EVREventType.ApplicationTransitionStarted -> applicationTransitionStarted()
            EVREventType.ApplicationTransitionAborted -> applicationTransitionAborted()
            EVREventType.ApplicationTransitionNewAppStarted -> applicationTransitionNewAppStarted()
            EVREventType.ApplicationListUpdated -> applicationListUpdated()
            EVREventType.ApplicationMimeTypeLoad -> applicationMimeTypeLoad()
            EVREventType.ApplicationTransitionNewAppLaunchComplete -> applicationTransitionNewAppLaunchComplete()
            EVREventType.ProcessConnected -> processConnected()
            EVREventType.ProcessDisconnected -> processDisconnected()
            EVREventType.Compositor_MirrorWindowShown -> compositor_MirrorWindowShown()
            EVREventType.Compositor_MirrorWindowHidden -> compositor_MirrorWindowHidden()
            EVREventType.Compositor_ChaperoneBoundsShown -> compositor_ChaperoneBoundsShown()
            EVREventType.Compositor_ChaperoneBoundsHidden -> compositor_ChaperoneBoundsHidden()
            EVREventType.TrackedCamera_StartVideoStream -> trackedCamera_StartVideoStream()
            EVREventType.TrackedCamera_StopVideoStream -> trackedCamera_StopVideoStream()
            EVREventType.TrackedCamera_PauseVideoStream -> trackedCamera_PauseVideoStream()
            EVREventType.TrackedCamera_ResumeVideoStream -> trackedCamera_ResumeVideoStream()
            EVREventType.TrackedCamera_EditingSurface -> trackedCamera_EditingSurface()
            EVREventType.PerformanceTest_EnableCapture -> performanceTest_EnableCapture()
            EVREventType.PerformanceTest_DisableCapture -> performanceTest_DisableCapture()
            EVREventType.PerformanceTest_FidelityLevel -> performanceTest_FidelityLevel()
            EVREventType.MessageOverlay_Closed -> messageOverlay_Closed()
            else -> Unit   // None, VendorSpecific_Reserved_Start / End
        }
    }

    open fun trackedDeviceActivated(left: Boolean) {}
    open fun trackedDeviceDeactivated(left: Boolean) {}
    open fun trackedDeviceUpdated(left: Boolean) {}
    open fun trackedDeviceUserInteractionStarted(left: Boolean) {}
    open fun trackedDeviceUserInteractionEnded(left: Boolean) {}
    open fun ipdChanged() {}
    open fun enterStandbyMode() {}
    open fun leaveStandbyMode() {}
    open fun trackedDeviceRoleChanged(left: Boolean) {}
    open fun watchdogWakeUpRequested() {}
    open fun lensDistortionChanged() {}
    open fun propertyChanged() {}
    open fun wirelessDisconnect() {}
    open fun wirelessReconnect() {}
    open fun buttonPress(left: Boolean, button: EVRButtonId) {}
    open fun buttonUnpress(left: Boolean, button: EVRButtonId) {}
    open fun buttonTouch(left: Boolean, button: EVRButtonId) {}
    open fun buttonUntouch(left: Boolean, button: EVRButtonId) {}
    open fun mouseMove() {}
    open fun mouseButtonDown() {}
    open fun mouseButtonUp() {}
    open fun focusEnter() {}
    open fun focusLeave() {}
    open fun scroll() {}
    open fun touchpadMove(left: Boolean, pos: Vec2) {}
    open fun overlayFocusChanged() {}
    open fun triggerMove(left: Boolean, state: Boolean, limit: Float, value: Float) {}
    open fun inputFocusCaptured() {}
    open fun inputFocusReleased() {}
    open fun sceneFocusLost() {}
    open fun sceneFocusGained() {}
    open fun sceneApplicationChanged() {}
    open fun sceneFocusChanged() {}
    open fun inputFocusChanged() {}
    open fun sceneApplicationSecondaryRenderingStarted() {}
    open fun hideRenderModels() {}
    open fun showRenderModels() {}
    open fun overlayShown() {}
    open fun overlayHidden() {}
    open fun dashboardActivated() {}
    open fun dashboardDeactivated() {}
    open fun dashboardThumbSelected() {}
    open fun dashboardRequested() {}
    open fun resetDashboard() {}
    open fun renderToast() {}
    open fun imageLoaded() {}
    open fun showKeyboard() {}
    open fun hideKeyboard() {}
    open fun overlayGamepadFocusGained() {}
    open fun overlayGamepadFocusLost() {}
    open fun overlaySharedTextureChanged() {}
    open fun dashboardGuideButtonDown() {}
    open fun dashboardGuideButtonUp() {}
    open fun screenshotTriggered() {}
    open fun imageFailed() {}
    open fun dashboardOverlayCreated() {}
    open fun requestScreenshot() {}
    open fun screenshotTaken() {}
    open fun screenshotFailed() {}
    open fun submitScreenshotToDashboard() {}
    open fun screenshotProgressToDashboard() {}
    open fun primaryDashboardDeviceChanged() {}
    open fun notification_Shown() {}
    open fun notification_Hidden() {}
    open fun notification_BeginInteraction() {}
    open fun notification_Destroyed() {}
    open fun quit() {}
    open fun processQuit() {}
    open fun quitAborted_UserPrompt() {}
    open fun quitAcknowledged() {}
    open fun driverRequestedQuit() {}
    open fun chaperoneDataHasChanged() {}
    open fun chaperoneUniverseHasChanged() {}
    open fun chaperoneTempDataHasChanged() {}
    open fun chaperoneSettingsHaveChanged() {}
    open fun seatedZeroPoseReset() {}
    open fun audioSettingsHaveChanged() {}
    open fun backgroundSettingHasChanged() {}
    open fun cameraSettingsHaveChanged() {}
    open fun reprojectionSettingHasChanged() {}
    open fun modelSkinSettingsHaveChanged() {}
    open fun environmentSettingsHaveChanged() {}
    open fun powerSettingsHaveChanged() {}
    open fun enableHomeAppSettingsHaveChanged() {}
    open fun statusUpdate() {}
    open fun mcImageUpdated() {}
    open fun firmwareUpdateStarted() {}
    open fun firmwareUpdateFinished() {}
    open fun keyboardClosed() {}
    open fun keyboardCharInput() {}
    open fun keyboardDone() {}
    open fun applicationTransitionStarted() {}
    open fun applicationTransitionAborted() {}
    open fun applicationTransitionNewAppStarted() {}
    open fun applicationListUpdated() {}
    open fun applicationMimeTypeLoad() {}
    open fun applicationTransitionNewAppLaunchComplete() {}
    open fun processConnected() {}
    open fun processDisconnected() {}
    open fun compositor_MirrorWindowShown() {}
    open fun compositor_MirrorWindowHidden() {}
    open fun compositor_ChaperoneBoundsShown() {}
    open fun compositor_ChaperoneBoundsHidden() {}
    open fun trackedCamera_StartVideoStream() {}
    open fun trackedCamera_StopVideoStream() {}
    open fun trackedCamera_PauseVideoStream() {}
    open fun trackedCamera_ResumeVideoStream() {}
    open fun trackedCamera_EditingSurface() {}
    open fun performanceTest_EnableCapture() {}
    open fun performanceTest_DisableCapture() {}
    open fun performanceTest_FidelityLevel() {}
    open fun messageOverlay_Closed() {}


    val leftDevice get() = devices.getOrNull(left)
    val rightDevice get() = devices.getOrNull(right)


    // Controllers Settings
    var leftTouchpadMode = TouchpadMode.Off
    var leftTouchpadDelta = .1f
    var leftTriggerMode = TriggerMode.OnState
    var leftTriggerDelta = .1f

    var rightTouchpadMode = TouchpadMode.Off
    var rightTouchpadDelta = .1f
    var rightTriggerMode = TriggerMode.Off
    var rightTriggerDelta = .1f

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

    enum class TouchpadMode { Off, OnDelta, Always }
    enum class TriggerMode { Off, OnState, OnLimit, Always }

    inner class Device(val index: Int) {

        var _frameCount = -1

        var valid = false
            private set

        var left = true

        val connected get() = update().run { _pose.bDeviceIsConnected }
        val hasTracking get() = update().run { _pose.bPoseIsValid }
        val outOfRange get() = update().run { _pose.eTrackingResult == ETrackingResult.Running_OutOfRange || _pose.eTrackingResult == ETrackingResult.Calibrating_OutOfRange }
        val calibrating get() = update().run { _pose.eTrackingResult == ETrackingResult.Calibrating_InProgress || _pose.eTrackingResult == ETrackingResult.Calibrating_OutOfRange }
        val uninitialized get() = update().run { _pose.eTrackingResult == ETrackingResult.Uninitialized }

        private val _transform = Utils.RigidTransform()

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
        val touchpadMode get() = if (left) leftTouchpadMode else rightTouchpadMode
        /** amount touchpad position must be increased or released on a single axes to change state   */
        val touchpadDelta get() = if (left) leftTouchpadDelta else rightTouchpadDelta

        fun update(frameCount: Int = _frameCount) {
            if (frameCount != prevFrameCount) {
                prevFrameCount = frameCount
                valid = hmd.getControllerStateWithPose(Render.trackingSpace, index, _state, _state.size(), _pose)
                if (valid) {
                    updateTouchpad()
                    updateTrigger()
                }
            }
        }

        fun updateTouchpad() {
            if (ButtonMask.SteamVR_Touchpad.touch) when (touchpadMode) {
                TouchpadMode.Off -> Unit
                TouchpadMode.OnDelta -> {
                    if (prevTouchpadPos.x - _state.axis0.pos.x >= touchpadDelta || prevTouchpadPos.y - _state.axis0.pos.y >= touchpadDelta) {
                        touchpadMove(left, _state.axis0.pos)
                        prevTouchpadPos put _state.axis0.pos
                    }
                }
                TouchpadMode.Always -> {
                    touchpadMove(left, _state.axis0.pos)
                    prevTouchpadPos put _state.axis0.pos
                }
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
                hmd.triggerHapticPulse(index, buttonId.i - EVRButtonId.Axis0.i, durationMicroSec)


        val triggerMode get() = if (left) leftTriggerMode else rightTriggerMode
        /** amount trigger must be pulled or released to change state   */
        val triggerDelta get() = if (left) leftTriggerDelta else rightTriggerDelta
        private var triggerLimit = 0f
        private var triggerPrevLimit = triggerLimit
        private var triggerState = false
        private var triggerPrevState = triggerState

        fun updateTrigger() {
            if (ButtonMask.SteamVR_Trigger.touch) when (triggerMode) {
                TriggerMode.Off -> Unit
                else -> {
                    triggerPrevState = triggerState
                    val value = _state.axis1.x  // trigger
                    if (triggerState) {
                        if (value < triggerLimit - triggerDelta || value <= 0f)
                            triggerState = false
                    } else if (value > triggerLimit + triggerDelta || value >= 1f)
                        triggerState = true
                    triggerLimit = if (triggerState) glm.max(triggerLimit, value) else glm.min(triggerLimit, value)
                    if (when (triggerMode) {
                        TriggerMode.OnState -> triggerPrevState != triggerState
                        TriggerMode.OnLimit ->
                            if (triggerPrevLimit != triggerLimit) {
                                triggerPrevLimit = triggerLimit
                                true
                            } else false
                        else -> true    // remains only Always at this point, Off has been excluded by the previous `when`
                    })
                        triggerMove(left, triggerState, triggerLimit, value)
                }
            }
        }

        val trigger get() = update().let { triggerState }
        val triggerDown get() = update().let { triggerState && !triggerPrevState }
        val triggerUp get() = update().let { !triggerState && triggerPrevState }

        private infix fun Long.has(buttonMask: ButtonMask) = (this and buttonMask.i) != 0L
        private infix fun Long.hasnt(buttonMask: ButtonMask) = (this and buttonMask.i) == 0L
        private infix fun Long.has(buttonId: EVRButtonId) = (this and buttonId.mask) != 0L
        private infix fun Long.hasnt(buttonId: EVRButtonId) = (this and buttonId.mask) == 0L
    }
}