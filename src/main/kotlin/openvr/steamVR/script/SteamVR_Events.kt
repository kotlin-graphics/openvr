package openvr.steamVR.script

import openvr.lib.VREventType
import openvr.plugin2.SteamVR_RenderModel
import org.lwjgl.openvr.TrackedDevicePose
import org.lwjgl.openvr.VREvent
import java.awt.Color

//======= Copyright (c) Valve Corporation, All rights reserved. ===============
//
// Purpose: Simple event system for SteamVR.
//
// Example usage:
//
//			void OnDeviceConnected(int i, bool connected) { ... }
//			SteamVR_Events.DeviceConnected.Listen(OnDeviceConnected); // Usually in OnEnable
//			SteamVR_Events.DeviceConnected.Remove(OnDeviceConnected); // Usually in OnDisable
//
// Alternatively, if Listening/Removing often these can be cached as follows:
//
//			SteamVR_Event.Action deviceConnectedAction;
//			void OnAwake() { deviceConnectedAction = SteamVR_Event.DeviceConnectedAction(OnDeviceConnected); }
//			void OnEnable() { deviceConnectedAction.enabled = true; }
//			void OnDisable() { deviceConnectedAction.enabled = false; }
//
//=============================================================================

object SteamVR_Events {

    abstract class Action {
        abstract fun enable(enabled: Boolean)
//        var enabled: Boolean
//            set(value) = enable(value)
    }

    class Action0(val _event: Event0, val action: UnityAction0) : Action() {
        override fun enable(enabled: Boolean) = when {
            enabled -> _event.listen(action)
            else -> _event.remove(action)
        }
    }

    class Action1<T>(val _event: Event1<T>, val action: UnityAction1<T>) : Action() {
        override fun enable(enabled: Boolean) = when {
            enabled -> _event.listen(action)
            else -> _event.remove(action)
        }
    }

    class Action2<T0, T1>(val _event: Event2<T0, T1>, val action: UnityAction2<T0, T1>) : Action() {
        override fun enable(enabled: Boolean) = when {
            enabled -> _event.listen(action)
            else -> _event.remove(action)
        }
    }

    class Action3<T0, T1, T2>(val _event: Event3<T0, T1, T2>, val action: UnityAction3<T0, T1, T2>) : Action() {
        override fun enable(enabled: Boolean) = when {
            enabled -> _event.listen(action)
            else -> _event.remove(action)
        }
    }

    class Event0 : UnityEvent0() {
        fun listen(action: UnityAction0) {
            calls += action
        }

        fun remove(action: UnityAction0) {
            calls -= action
        }

        fun send() = calls.forEach { it() }
    }

    class Event1<T> : UnityEvent1<T>() {
        fun listen(action: UnityAction1<T>) {
            calls += action
        }

        fun remove(action: UnityAction1<T>) {
            calls -= action
        }

        fun send(arg0: T) = calls.forEach { it(arg0) }
    }

    class Event2<T0, T1> : UnityEvent2<T0, T1>() {
        fun listen(action: UnityAction2<T0, T1>) {
            calls += action
        }

        fun remove(action: UnityAction2<T0, T1>) {
            calls -= action
        }

        fun send(arg0: T0, arg1: T1) = calls.forEach { it(arg0, arg1) }
    }

    class Event3<T0, T1, T2> : UnityEvent3<T0, T1, T2>() {
        fun listen(action: UnityAction3<T0, T1, T2>) {
            calls += action
        }

        fun remove(action: UnityAction3<T0, T1, T2>) {
            calls -= action
        }

        fun send(arg0: T0, arg1: T1, arg2: T2) = calls.forEach { it(arg0, arg1, arg2) }
    }

    val calibrating = Event1<Boolean>()
    fun calibratingAction(action: UnityAction1<Boolean>): Action = Action1(calibrating, action)

    val deviceConnected = Event2<Int, Boolean>()
    fun deviceConnectedAction(action: UnityAction2<Int, Boolean>): Action = Action2(deviceConnected, action)

    val fade = Event3<Color, Float, Boolean>()
    fun fadeAction(action: UnityAction3<Color, Float, Boolean>): Action = Action3(fade, action)

    val fadeReady = Event0()
    fun fadeReadyAction(action: UnityAction0): Action = Action0(fadeReady, action)

    val hideRenderModels = Event1<Boolean>()
    fun hideRenderModelsAction(action: UnityAction1<Boolean>): Action = Action1(hideRenderModels, action)

    val initializing = Event1<Boolean>()
    fun initializingAction(action: UnityAction1<Boolean>): Action = Action1(initializing, action)

    val inputFocus = Event1<Boolean>()
    fun inputFocusAction(action: UnityAction1<Boolean>): Action = Action1(inputFocus, action)

    val loading = Event1<Boolean>()
    fun loadingAction(action: UnityAction1<Boolean>): Action = Action1(loading, action)

    val loadingFadeIn = Event1<Float>()
    fun loadingFadeInAction(action: UnityAction1<Float>): Action = Action1(loadingFadeIn, action)

    val loadingFadeOut = Event1<Float>()
    fun loadingFadeOutAction(action: UnityAction1<Float>): Action = Action1(loadingFadeOut, action)

    val newPoses = Event1<Array<TrackedDevicePose>>()
    fun newPosesAction(action: UnityAction1<Array<TrackedDevicePose>>): Action = Action1(newPoses, action)

    val newPosesApplied = Event0()
    fun newPosesAppliedAction(action: UnityAction0): Action = Action0(newPosesApplied, action)

    val initialized = Event1<Boolean>()
    fun initializedAction(action: UnityAction1<Boolean>): Action = Action1(initialized, action)

    val outOfRange = Event1<Boolean>()
    fun outOfRangeAction(action: UnityAction1<Boolean>): Action = Action1(outOfRange, action)

    val renderModelLoaded = Event2<SteamVR_RenderModel, Boolean>()
    fun renderModelLoadedAction(action: UnityAction2<SteamVR_RenderModel, Boolean>): Action = Action2(renderModelLoaded, action)

    val systemEvents = mutableMapOf<VREventType, Event1<VREvent>>()
    fun system(eventType: VREventType): Event1<VREvent> = systemEvents.getOrPut(eventType) { Event1() }

    fun systemAction(eventType: VREventType, action: UnityAction1<VREvent>): Action = Action1(system(eventType), action)
}