package openvr

import openvr.lib.*

abstract class EventListener(val hmd: IVRSystem) {

    val states = Array(k_unMaxTrackedDeviceCount, { VRControllerState_t.ByReference() })

    init {
        detectRoles()
    }

    private fun detectRoles() {
        left = -1
        right = -1
        for (i in 0 until k_unMaxTrackedDeviceCount) {
            if (hmd.getTrackedDeviceClass(i) == ETrackedDeviceClass.Controller && hmd.getControllerState(i, states[i], states[i].size()))
                if (left == -1) left = i
                else right = i
        }
    }

    var left = -1
    var right = -1

    val event = VREvent_t.ByReference()

    fun poll() {
        while (hmd.pollNextEvent(event, event.size()))
            process()
    }

    private fun process() = when (event.eventType) {
        EVREventType.TrackedDeviceActivated -> {
            detectRoles()
            trackedDeviceActivated(event.trackedDeviceIndex == left)
        }
        EVREventType.TrackedDeviceDeactivated -> {
            detectRoles()
            trackedDeviceDeactivated(event.trackedDeviceIndex == left)
        }
        EVREventType.TrackedDeviceUpdated -> {
            detectRoles()
            trackedDeviceUpdated(event.trackedDeviceIndex == left)
        }
        EVREventType.TrackedDeviceUserInteractionStarted -> {
            detectRoles()
            trackedDeviceUserInteractionStarted(event.trackedDeviceIndex == left)
        }
        EVREventType.TrackedDeviceUserInteractionEnded -> {
            detectRoles()
            trackedDeviceUserInteractionEnded(event.trackedDeviceIndex == left)
        }
        EVREventType.IpdChanged -> ipdChanged()
        EVREventType.EnterStandbyMode -> enterStandbyMode()
        EVREventType.LeaveStandbyMode -> leaveStandbyMode()
        EVREventType.TrackedDeviceRoleChanged -> {
            detectRoles()
            trackedDeviceRoleChanged(event.trackedDeviceIndex == left)
        }
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
        EVREventType.TouchPadMove -> touchPadMove()
        EVREventType.OverlayFocusChanged -> overlayFocusChanged()
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
        else -> {
        } // None, VendorSpecific_Reserved_Start / End
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
    open fun touchPadMove() {}
    open fun overlayFocusChanged() {}
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
}