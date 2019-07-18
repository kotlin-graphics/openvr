package openvr.steamVR.input

import openvr.lib.*
import openvr.plugin2.SteamVR_Input_Source
import openvr.plugin2.SteamVR_Input_Sources
import openvr.unity.Time
import org.lwjgl.openvr.InputAnalogActionData

//======= Copyright (c) Valve Corporation, All rights reserved. ===============

typealias SteamVR_Action_Single_AxisHandler = (fromAction: SteamVR_Action_Single, fromSource: SteamVR_Input_Sources, newAxis: Float, newDelta: Float) -> Unit
typealias SteamVR_Action_Single_ActiveChangeHandler = (fromAction: SteamVR_Action_Single, fromSource: SteamVR_Input_Sources, active: Boolean) -> Unit
typealias SteamVR_Action_Single_ChangeHandler = (fromAction: SteamVR_Action_Single, fromSource: SteamVR_Input_Sources, newAxis: Float, newDelta: Float) -> Unit
typealias SteamVR_Action_Single_UpdateHandler = (fromAction: SteamVR_Action_Single, fromSource: SteamVR_Input_Sources, newAxis: Float, newDelta: Float) -> Unit

/** An analog action with a value generally from 0 to 1. Also provides a delta since the last update. */
class SteamVR_Action_Single : SteamVR_Action_In<SteamVR_Action_Single_Source_Map, SteamVR_Action_Single_Source>(),
        ISteamVR_Action_Single/*, ISerializationCallbackReceiver*/ {

    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> This event fires whenever the axis changes by more than the specified changeTolerance</summary>
//    public event ChangeHandler onChange
//    { add { sourceMap[SteamVR_Input_Sources.Any].onChange += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onChange -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> This event fires whenever the action is updated</summary>
//    public event UpdateHandler onUpdate
//    { add { sourceMap[SteamVR_Input_Sources.Any].onUpdate += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onUpdate -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> This event will fire whenever the float value of the action is non-zero</summary>
//    public event AxisHandler onAxis
//    { add { sourceMap[SteamVR_Input_Sources.Any].onAxis += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onAxis -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> This event fires when the active state (ActionSet active and binding active) changes</summary>
//    public event ActiveChangeHandler onActiveChange
//    { add { sourceMap[SteamVR_Input_Sources.Any].onActiveChange += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onActiveChange -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> This event fires when the active state of the binding changes</summary>
//    public event ActiveChangeHandler onActiveBindingChange
//    { add { sourceMap[SteamVR_Input_Sources.Any].onActiveBindingChange += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onActiveBindingChange -= value; } }


    /** [Shortcut to: SteamVR_Input_Sources.Any] The current float value of the action.
     *  Note: Will only return non-zero if the action is also active. */
    override val axis: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.axis

    /** [Shortcut to: SteamVR_Input_Sources.Any] The float value of the action from the previous update.
     *  Note: Will only return non-zero if the action is also active. */
    override val lastAxis: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastAxis

    /** [Shortcut to: SteamVR_Input_Sources.Any] The float value difference between this update and the previous update.
     *  Note: Will only return non-zero if the action is also active. */
    override val delta: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.delta

    /** [Shortcut to: SteamVR_Input_Sources.Any] The float value difference between the previous update and update before that.
     *  Note: Will only return non-zero if the action is also active. */
    override val lastDelta: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastDelta


    /** The current float value of the action
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getAxis(inputSource: SteamVR_Input_Sources): Float = sourceMap!![inputSource]!!.axis

    /** The float value difference between this update and the previous update.
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getAxisDelta(inputSource: SteamVR_Input_Sources): Float = sourceMap!![inputSource]!!.delta

    /** The float value of the action from the previous update.
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getLastAxis(inputSource: SteamVR_Input_Sources): Float = sourceMap!![inputSource]!!.lastAxis

    /** The float value difference between the previous update and update before that.
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getLastAxisDelta(inputSource: SteamVR_Input_Sources): Float =
            sourceMap!![inputSource]!!.lastDelta

    /** Executes a function when the *functional* active state of this action (with the specified inputSource) changes.
     *  This happens when the action is bound or unbound, or when the ActionSet changes state.
     *  @param functionToCall: A local function that receives the boolean action who's active state changes and the corresponding input source
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnActiveChangeListener(functionToCall: SteamVR_Action_Single_ActiveChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onActiveChange += functionToCall
    }

    /** Stops executing a function when the *functional* active state of this action (with the specified inputSource) changes.
     *  This happens when the action is bound or unbound, or when the ActionSet changes state.
     *  @param functionToStopCalling: The local function that you've setup to receive update events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnActiveChangeListener(functionToStopCalling: SteamVR_Action_Single_ActiveChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onActiveChange -= functionToStopCalling
    }

    /** Executes a function when the active state of this action (with the specified inputSource) changes. This happens when the action is bound or unbound
     *  @param functionToCall: A local function that receives the boolean action who's active state changes and the corresponding input source
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnActiveBindingChangeListener(functionToCall: SteamVR_Action_Single_ActiveChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onActiveBindingChange += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive update events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnActiveBindingChangeListener(functionToStopCalling: SteamVR_Action_Single_ActiveChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onActiveBindingChange -= functionToStopCalling
    }

    /** Executes a function when the axis changes by more than the specified changeTolerance
     *  @param functionToCall: A local function that receives the boolean action who's state has changed, the corresponding input source, and the new value
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnChangeListener(functionToCall: SteamVR_Action_Single_ChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onChange += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive on change events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnChangeListener(functionToStopCalling: SteamVR_Action_Single_ChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onChange -= functionToStopCalling
    }

    /** Executes a function when the state of this action (with the specified inputSource) is updated.
     *  @param functionToCall: A local function that receives the boolean action who's state has changed, the corresponding input source, and the new value
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnUpdateListener(functionToCall: SteamVR_Action_Single_UpdateHandler, inputSource:  SteamVR_Input_Sources)    {
        sourceMap!![inputSource]!!.onUpdate += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive update events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnUpdateListener(functionToStopCalling: SteamVR_Action_Single_UpdateHandler, inputSource:  SteamVR_Input_Sources)    {
        sourceMap!![inputSource]!!.onUpdate -= functionToStopCalling
    }

    /** Executes a function when the float value of the action is non-zero.
     *  @param functionToCall: A local function that receives the boolean action who's state has changed, the corresponding input source, and the new value
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnAxisListener( functionToCall: SteamVR_Action_Single_AxisHandler, inputSource: SteamVR_Input_Sources )    {
        sourceMap!![inputSource]!!.onAxis += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive update events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnAxisListener( functionToStopCalling: SteamVR_Action_Single_AxisHandler, inputSource:  SteamVR_Input_Sources)    {
        sourceMap!![inputSource]!!.onAxis -= functionToStopCalling
    }

//    void ISerializationCallbackReceiver.OnBeforeSerialize()
//    {
//    }
//
//    void ISerializationCallbackReceiver.OnAfterDeserialize()
//    {
//        InitAfterDeserialize()
//    }
}

class SteamVR_Action_Single_Source_Map : SteamVR_Action_In_Source_Map<SteamVR_Action_Single_Source>()

class SteamVR_Action_Single_Source : SteamVR_Action_In_Source(), ISteamVR_Action_Single {

    /** The amount the axis needs to change before a change is detected */
    val changeTolerance = Float.MIN_VALUE

    /** Event fires when the value of the action is non-zero */
    val onAxis = ArrayList<SteamVR_Action_Single_AxisHandler>()

    /** Event fires when the active state (ActionSet active and binding active) changes */
    val onActiveChange = ArrayList<SteamVR_Action_Single_ActiveChangeHandler>()

    /** Event fires when the active state of the binding changes */
    val onActiveBindingChange = ArrayList<SteamVR_Action_Single_ActiveChangeHandler>()

    /** This event fires whenever the axis changes by more than the specified changeTolerance */
    val onChange = ArrayList<SteamVR_Action_Single_ChangeHandler>()

    /** Event fires when the action is updated */
    val onUpdate = ArrayList<SteamVR_Action_Single_UpdateHandler>()

    /** The current float value of the action.
     *  Note: Will only return non-zero if the action is also active. */
    override val axis: Float
        get() = if (active) actionData.x else 0f

    /** The float value of the action from the previous update.
     *  Note: Will only return non-zero if the action is also active. */
    override val lastAxis: Float
        get() = if (active) lastActionData.x else 0f

    /** The float value difference between this update and the previous update.
     *  Note: Will only return non-zero if the action is also active. */
    override val delta: Float
        get() = if (active) actionData.deltaX else 0f

    /** The float value difference between the previous update and update before that.
     *  Note: Will only return non-zero if the action is also active. */
    override val lastDelta: Float
        get() = if (active) lastActionData.deltaX else 0f


    /** If the float value of this action has changed more than the changeTolerance since the last update */
    override var changed: Boolean = false


    /** If the float value of this action has changed more than the changeTolerance between the previous update and the update before that */
    override var lastChanged: Boolean = false

    /** The handle to the origin of the component that was used to update the value for this action */
    override val activeOrigin: VRInputValueHandle
        get() = if (active) actionData.activeOrigin else 0

    /** The handle to the origin of the component that was used to update the value for this action (for the previous update) */
    override val lastActiveOrigin: VRInputValueHandle
        get() = lastActionData.activeOrigin

    /** @Returns true if this action is bound and the ActionSet is active */
    override val active: Boolean
        get() = activeBinding && action.actionSet!!.isActive(inputSource)

    /** @Returns true if the action is bound */
    override val activeBinding: Boolean
        get() = actionData.active


    /** @Returns true if the action was bound and the ActionSet was active during the previous update */
    override var lastActive: Boolean = false

    /** @Returns true if the action was bound during the previous update */
    override val lastActiveBinding: Boolean
        get() = lastActionData.active


    protected var actionData = InputAnalogActionData.calloc()
    protected var lastActionData = InputAnalogActionData.calloc()

    protected lateinit var singleAction: SteamVR_Action_Single


    /** [Should not be called by user code] Sets up the internals of the action source before SteamVR has been initialized. */
    override fun preinitialize(wrappingAction: SteamVR_Action, forInputSource: SteamVR_Input_Sources) {
        super.preinitialize(wrappingAction, forInputSource)
        singleAction = wrappingAction as SteamVR_Action_Single
    }

    /** [Should not be called by user code]
     *  Updates the data for this action and this input source. Sends related events. */
    override fun updateValue() {

        lastActionData.free()
        lastActionData = actionData
        lastActive = active

        val err = vrInput.getAnalogActionData(handle, actionData, SteamVR_Input_Source.getHandle(inputSource))
        if (err != vrInput.Error.None)
            System.err.println("[SteamVR] GetAnalogActionData error ($fullPath): $err handle: $handle")

        updateTime = Time.realtimeSinceStartup

        changed = false

        if (active) {
            if (delta > changeTolerance || delta < -changeTolerance) {
                changed = true
                changedTime = Time.realtimeSinceStartup + actionData.updateTime //fUpdateTime is the time from the time the action was called that the action changed

                onChange.forEach { it(singleAction, inputSource, axis, delta) }
            }

            if (axis != 0f)
                onAxis.forEach { it(singleAction, inputSource, axis, delta) }

            onUpdate.forEach { it(singleAction, inputSource, axis, delta) }
        }


        if (lastActiveBinding != activeBinding)
            onActiveBindingChange.forEach { it(singleAction, inputSource, activeBinding) }

        if (lastActive != active)
            onActiveChange.forEach { it(singleAction, inputSource, activeBinding) }
    }
}

interface ISteamVR_Action_Single : ISteamVR_Action_In_Source {

    /** The current float value of the action.
     *  Note: Will only return non-zero if the action is also active. */
    val axis: Float

    /** The float value of the action from the previous update.
     *  Note: Will only return non-zero if the action is also active. */
    val lastAxis: Float


    /** The float value difference between this update and the previous update.
     *  Note: Will only return non-zero if the action is also active. */
    val delta: Float

    /** The float value difference between the previous update and update before that.
     *  Note: Will only return non-zero if the action is also active. */
    val lastDelta: Float
}