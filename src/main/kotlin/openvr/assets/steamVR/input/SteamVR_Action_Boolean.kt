package openvr.assets.steamVR.input

import openvr.lib.*
import openvr.plugin2.SteamVR_Input_Sources
import openvr.unity.Time
import org.lwjgl.openvr.InputDigitalActionData

//======= Copyright (c) Valve Corporation, All rights reserved. ===============

typealias SteamVR_Action_Boolean_StateDownHandler = (fromAction: SteamVR_Action_Boolean?, fromSource: SteamVR_Input_Sources) -> Unit
typealias SteamVR_Action_Boolean_StateUpHandler = (fromAction: SteamVR_Action_Boolean?, fromSource: SteamVR_Input_Sources) -> Unit
typealias SteamVR_Action_Boolean_StateHandler = (fromAction: SteamVR_Action_Boolean?, fromSource: SteamVR_Input_Sources) -> Unit
typealias SteamVR_Action_Boolean_ActiveChangeHandler = (fromAction: SteamVR_Action_Boolean?, fromSource: SteamVR_Input_Sources, active: Boolean) -> Unit
typealias SteamVR_Action_Boolean_ChangeHandler = (fromAction: SteamVR_Action_Boolean?, fromSource: SteamVR_Input_Sources, newState: Boolean) -> Unit
typealias SteamVR_Action_Boolean_UpdateHandler = (fromAction: SteamVR_Action_Boolean?, fromSource: SteamVR_Input_Sources, newState: Boolean) -> Unit

/** Boolean actions are either true or false. There are a variety of helper events included that will fire for the given input source. They're prefixed with "on". */
class SteamVR_Action_Boolean : SteamVR_Action_In<SteamVR_Action_Boolean_Source_Map, SteamVR_Action_Boolean_Source>(),
        ISteamVR_Action_Boolean/*, ISerializationCallbackReceiver*/ {

    /** [Shortcut to: SteamVR_Input_Sources.Any] This event fires whenever a state changes from false to true or true to false */
//    var onChange: SteamVR_Action_Boolean_ChangeHandler
//        { add { sourceMap[SteamVR_Input_Sources.Any].onChange += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onChange -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> This event fires whenever the action is updated</summary>
//    public event UpdateHandler onUpdate
//    { add { sourceMap[SteamVR_Input_Sources.Any].onUpdate += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onUpdate -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> This event fires whenever the boolean action is true and gets updated</summary>
//    public event StateHandler onState
//    { add { sourceMap[SteamVR_Input_Sources.Any].onState += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onState -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> This event fires whenever the state of the boolean action has changed from false to true in the most recent update</summary>
//    public event StateDownHandler onStateDown
//    { add { sourceMap[SteamVR_Input_Sources.Any].onStateDown += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onStateDown -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> This event fires whenever the state of the boolean action has changed from true to false in the most recent update</summary>
//    public event StateUpHandler onStateUp
//    { add { sourceMap[SteamVR_Input_Sources.Any].onStateUp += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onStateUp -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> Event fires when the active state (ActionSet active and binding active) changes</summary>
//    public event ActiveChangeHandler onActiveChange
//    { add { sourceMap[SteamVR_Input_Sources.Any].onActiveChange += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onActiveChange -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> Event fires when the bound state of the binding changes</summary>
//    public event ActiveChangeHandler onActiveBindingChange
//    { add { sourceMap[SteamVR_Input_Sources.Any].onActiveBindingChange += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onActiveBindingChange -= value; } }


    /** [Shortcut to: SteamVR_Input_Sources.Any] True when the boolean action is true */
    override val state: Boolean
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.state

    /** [Shortcut to: SteamVR_Input_Sources.Any] True when the boolean action is true and the last state was false */
    override val stateDown: Boolean
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.stateDown

    /** [Shortcut to: SteamVR_Input_Sources.Any] True when the boolean action is false and the last state was true */
    override val stateUp: Boolean
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.stateUp


    /** [Shortcut to: SteamVR_Input_Sources.Any] (previous update) True when the boolean action is true */
    override val lastState: Boolean
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastState

    /** [Shortcut to: SteamVR_Input_Sources.Any] (previous update) True when the boolean action is true and the last state was false */
    override val lastStateDown: Boolean
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastStateDown

    /** [Shortcut to: SteamVR_Input_Sources.Any] (previous update) True when the boolean action is false and the last state was true */
    override val lastStateUp: Boolean
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastStateUp


    /** @Returns true if the value of the action has been changed to true (from false) in the most recent update.
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getStateDown(inputSource: SteamVR_Input_Sources): Boolean = sourceMap!![inputSource]!!.stateDown

    /** @Returns true if the value of the action has been changed to false (from true) in the most recent update.
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getStateUp(inputSource: SteamVR_Input_Sources): Boolean = sourceMap!![inputSource]!!.stateUp

    /** @Returns true if the value of the action (state) is currently true
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getState(inputSource: SteamVR_Input_Sources): Boolean = sourceMap!![inputSource]!!.state

    /** [For the previous update] Returns true if the value of the action has been set to true (from false).
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getLastStateDown(inputSource: SteamVR_Input_Sources): Boolean =
            sourceMap!![inputSource]!!.lastStateDown

    /** [For the previous update] Returns true if the value of the action has been set to false (from true).
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getLastStateUp(inputSource: SteamVR_Input_Sources): Boolean =
            sourceMap!![inputSource]!!.lastStateUp

    /** [For the previous update] Returns true if the value of the action was true.
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getLastState(inputSource: SteamVR_Input_Sources): Boolean =
            sourceMap!![inputSource]!!.lastState

    /** Executes a function when the *functional* active state of this action (with the specified inputSource) changes.
     *  This happens when the action is bound or unbound, or when the ActionSet changes state.
     *  @param functionToCall: A local function that receives the boolean action who's active state changes and the corresponding input source
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnActiveChangeListener(functionToCall: SteamVR_Action_Boolean_ActiveChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onActiveChange += functionToCall
    }

    /** Stops executing a function when the *functional* active state of this action (with the specified inputSource) changes.
     *  This happens when the action is bound or unbound, or when the ActionSet changes state.
     *  @param functionToStopCalling: The local function that you've setup to receive update events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnActiveChangeListener(functionToStopCalling: SteamVR_Action_Boolean_ActiveChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onActiveChange -= functionToStopCalling
    }

    /** Executes a function when the active state of this action (with the specified inputSource) changes. This happens when the action is bound or unbound
     *  @param functionToCall: A local function that receives the boolean action who's active state changes and the corresponding input source
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnActiveBindingChangeListener(functionToCall: SteamVR_Action_Boolean_ActiveChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onActiveBindingChange += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive update events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnActiveBindingChangeListener(functionToStopCalling: SteamVR_Action_Boolean_ActiveChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onActiveBindingChange -= functionToStopCalling
    }

    /** Executes a function when the state of this action (with the specified inputSource) changes
     *  @param functionToCall: A local function that receives the boolean action who's state has changed, the corresponding input source, and the new value
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnChangeListener(functionToCall: SteamVR_Action_Boolean_ChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onChange += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive on change events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnChangeListener(functionToStopCalling: SteamVR_Action_Boolean_ChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onChange -= functionToStopCalling
    }

    /** Executes a function when the state of this action (with the specified inputSource) is updated.
     *  @param functionToCall: A local function that receives the boolean action who's state has changed, the corresponding input source, and the new value
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnUpdateListener(functionToCall: SteamVR_Action_Boolean_UpdateHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onUpdate += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive update events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnUpdateListener(functionToStopCalling: SteamVR_Action_Boolean_UpdateHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onUpdate -= functionToStopCalling
    }

    /** Executes a function when the state of this action (with the specified inputSource) changes to true (from false).
     *  @param functionToCall: A local function that receives the boolean action who's state has changed, the corresponding input source, and the new value
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnStateDownListener(functionToCall: SteamVR_Action_Boolean_StateDownHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onStateDown += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive update events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnStateDownListener(functionToStopCalling: SteamVR_Action_Boolean_StateDownHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onStateDown -= functionToStopCalling
    }

    /** Executes a function when the state of this action (with the specified inputSource) changes to false (from true).
     *  @param functionToCall: A local function that receives the boolean action who's state has changed, the corresponding input source, and the new value
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnStateUpListener(functionToCall: SteamVR_Action_Boolean_StateUpHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onStateUp += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnStateUpListener(functionToStopCalling: SteamVR_Action_Boolean_StateUpHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onStateUp -= functionToStopCalling
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

class SteamVR_Action_Boolean_Source_Map : SteamVR_Action_In_Source_Map<SteamVR_Action_Boolean_Source>()

class SteamVR_Action_Boolean_Source : SteamVR_Action_In_Source(), ISteamVR_Action_Boolean {

    protected var actionDataSize = 0

    /** Event fires when the state of the action changes from false to true */
    val onStateDown = ArrayList<SteamVR_Action_Boolean_StateDownHandler>()

    /** Event fires when the state of the action changes from true to false */
    val onStateUp = ArrayList<SteamVR_Action_Boolean_StateUpHandler>()

    /** Event fires when the state of the action is true and the action gets updated */
    val onState = ArrayList<SteamVR_Action_Boolean_StateHandler>()

    /** Event fires when the active state (ActionSet active and binding active) changes */
    val onActiveChange = ArrayList<SteamVR_Action_Boolean_ActiveChangeHandler>()

    /** Event fires when the active state of the binding changes */
    val onActiveBindingChange = ArrayList<SteamVR_Action_Boolean_ActiveChangeHandler>()

    /** Event fires when the state of the action changes from false to true or true to false */
    val onChange = ArrayList<SteamVR_Action_Boolean_ChangeHandler>()

    /** Event fires when the action is updated */
    val onUpdate = ArrayList<SteamVR_Action_Boolean_UpdateHandler>()

    /** The current value of the boolean action. Note: Will only return true if the action is also active. */
    override val state: Boolean
        get() = active && actionData.state

    /** True when the action's state changes from false to true. Note: Will only return true if the action is also active.
     *  Will only return true if the action is also active. */
    override val stateDown: Boolean
        get() = active && actionData.state && actionData.changed

    /** True when the action's state changes from true to false. Note: Will only return true if the action is also active.
     *  Will only return true if the action is also active. */
    override val stateUp: Boolean
        get() = active && !actionData.state && actionData.changed

    /** True when the action's state changed during the most recent update. Note: Will only return true if the action is also active.
     *  ActionSet is ignored since get is coming from the native struct. */
    override var changed: Boolean = false
        get() = active && actionData.changed


    /** The value of the action's 'state' during the previous update
     *  Always returns the previous update state */
    override val lastState: Boolean
        get() = lastActionData.state

    /** The value of the action's 'stateDown' during the previous update
     *  Always returns the previous update state */
    override val lastStateDown: Boolean
        get() = lastActionData.state && lastActionData.changed

    /** The value of the action's 'stateUp' during the previous update
     *  Always returns the previous update state */
    override val lastStateUp: Boolean
        get() = !lastActionData.state && lastActionData.changed

    /** The value of the action's 'changed' during the previous update
     *  Always returns the previous update state. Set is ignored since get is coming from the native struct. */
    override var lastChanged: Boolean = true
        get() = lastActionData.changed

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


    /** Returns true if the action was bound and the ActionSet was active during the previous update */
    override var lastActive: Boolean = true

    /** @Returns true if the action was bound during the previous update */
    override val lastActiveBinding: Boolean
        get() = lastActionData.active


    protected var actionData = InputDigitalActionData.calloc()
    protected var lastActionData = InputDigitalActionData.calloc()

    protected var booleanAction: SteamVR_Action_Boolean? = null

    /** [Should not be called by user code] Sets up the internals of the action source before SteamVR has been initialized. */
    override fun preinitialize(wrappingAction: SteamVR_Action, forInputSource: SteamVR_Input_Sources) {
        super.preinitialize(wrappingAction, forInputSource)
        booleanAction = wrappingAction as SteamVR_Action_Boolean
    }

    /** [Should not be called by user code]
     *  Initializes the handle for the inputSource, the action data size, and any other related SteamVR data. */
    override fun initialize() {
        super.initialize()

        if (actionDataSize == 0)
            actionDataSize = InputDigitalActionData.SIZEOF
    }

    /** [Should not be called by user code]
     *  Updates the data for this action and this input source. Sends related events. */
    override fun updateValue() {

        lastActionData = actionData
        lastActive = active

        val err = vrInput.getDigitalActionData(action.handle, actionData, inputSourceHandle)
        if (err != vrInput.Error.None)
            System.err.println("[SteamVR] GetDigitalActionData error (${action.fullPath}): $err handle: ${action.handle}")

        if (changed)
            changedTime = Time.realtimeSinceStartup + actionData.updateTime

        updateTime = Time.realtimeSinceStartup

        if (active) {
            if (stateDown)
                onStateDown.forEach { it(booleanAction, inputSource) }

            if (stateUp)
                onStateUp.forEach { it(booleanAction, inputSource) }

            if (state)
                onState.forEach { it(booleanAction, inputSource) }

            if (changed)
                onChange.forEach { it(booleanAction, inputSource, state) }

            onUpdate.forEach { it(booleanAction, inputSource, state) }
        }

        if (lastActiveBinding != activeBinding)
            onActiveBindingChange.forEach { it(booleanAction, inputSource, activeBinding) }

        if (lastActive != active)
            onActiveChange.forEach { it(booleanAction, inputSource, activeBinding) }
    }
}

interface ISteamVR_Action_Boolean : ISteamVR_Action_In_Source {

    /** The current value of the boolean action. Note: Will only return true if the action is also active. */
    val state: Boolean

    /** True when the action's state changes from false to true. Note: Will only return true if the action is also active. */
    val stateDown: Boolean

    /** True when the action's state changes from true to false. Note: Will only return true if the action is also active. */
    val stateUp: Boolean

    /** The value of the action's 'state' during the previous update
     *  Always returns the previous update state</remarks> */
    val lastState: Boolean

    /** The value of the action's 'stateDown' during the previous update
     *  Always returns the previous update state */
    val lastStateDown: Boolean

    /** The value of the action's 'stateUp' during the previous update
     *  Always returns the previous update state */
    val lastStateUp: Boolean
}