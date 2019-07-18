package openvr.steamVR.input

import glm_.vec3.Vec3
import openvr.lib.*
import openvr.plugin2.SteamVR_Input_Source
import openvr.plugin2.SteamVR_Input_Sources
import openvr.unity.Time
import org.lwjgl.openvr.InputAnalogActionData

//======= Copyright (c) Valve Corporation, All rights reserved. ===============

typealias SteamVR_Action_Vector3_AxisHandler = (fromAction: SteamVR_Action_Vector3, fromSource: SteamVR_Input_Sources, axis: Vec3, delta: Vec3) -> Unit
typealias SteamVR_Action_Vector3_ActiveChangeHandler = (fromAction: SteamVR_Action_Vector3, fromSource: SteamVR_Input_Sources, active: Boolean) -> Unit
typealias SteamVR_Action_Vector3_ChangeHandler = (fromAction: SteamVR_Action_Vector3, fromSource: SteamVR_Input_Sources, axis: Vec3, delta: Vec3) -> Unit
typealias SteamVR_Action_Vector3_UpdateHandler = (fromAction: SteamVR_Action_Vector3, fromSource: SteamVR_Input_Sources, axis: Vec3, delta: Vec3) -> Unit

/** An analog action with three values generally from -1 to 1. Also provides a delta since the last update. */
class SteamVR_Action_Vector3 : SteamVR_Action_In<SteamVR_Action_Vector3_Source_Map, SteamVR_Action_Vector3_Source>(),
        ISteamVR_Action_Vector3/*, ISerializationCallbackReceiver*/ {

//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> This event fires whenever the axis changes by more than the specified changeTolerance</summary>
//    public event ChangeHandler onChange
//    { add { sourceMap[SteamVR_Input_Sources.Any].onChange += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onChange -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> This event fires whenever the action is updated</summary>
//    public event UpdateHandler onUpdate
//    { add { sourceMap[SteamVR_Input_Sources.Any].onUpdate += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onUpdate -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> This event will fire whenever the Vector3 value of the action is non-zero</summary>
//    public event AxisHandler onAxis
//    { add { sourceMap[SteamVR_Input_Sources.Any].onAxis += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onAxis -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> Event fires when the active state (ActionSet active and binding active) changes</summary>
//    public event ActiveChangeHandler onActiveChange
//    { add { sourceMap[SteamVR_Input_Sources.Any].onActiveChange += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onActiveChange -= value; } }
//
//    /// <summary><strong>[Shortcut to: SteamVR_Input_Sources.Any]</strong> Event fires when the active state of the binding changes</summary>
//    public event ActiveChangeHandler onActiveBindingChange
//    { add { sourceMap[SteamVR_Input_Sources.Any].onActiveBindingChange += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onActiveBindingChange -= value; } }


    /** [Shortcut to: SteamVR_Input_Sources.Any]</strong> The current Vector3 value of the action.
     *  Note: Will only return non-zero if the action is also active. */
    override val axis: Vec3
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.axis

    /** [Shortcut to: SteamVR_Input_Sources.Any] The Vector3 value of the action from the previous update.
     *  Note: Will only return non-zero if the action is also active. */
    override val lastAxis: Vec3
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastAxis

    /** [Shortcut to: SteamVR_Input_Sources.Any] The Vector3 value difference between this update and the previous update.
     *  Note: Will only return non-zero if the action is also active. */
    override val delta: Vec3
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.delta

    /** [Shortcut to: SteamVR_Input_Sources.Any] The Vector3 value difference between the previous update and update before that.
     *  Note: Will only return non-zero if the action is also active. */
    override val lastDelta: Vec3
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastDelta


    /** The current Vector3 value of the action
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getAxis(inputSource: SteamVR_Input_Sources): Vec3 =
            sourceMap!![inputSource]!!.axis

    /** The Vector3 value difference between this update and the previous update.
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getAxisDelta(inputSource: SteamVR_Input_Sources): Vec3 =
            sourceMap!![inputSource]!!.delta

    /** The Vector3 value of the action from the previous update.
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getLastAxis(inputSource: SteamVR_Input_Sources): Vec3 =
            sourceMap!![inputSource]!!.lastAxis

    /** The Vector3 value difference between the previous update and update before that.
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getLastAxisDelta(inputSource: SteamVR_Input_Sources): Vec3 =
            sourceMap!![inputSource]!!.lastDelta

    /** Executes a function when the *functional* active state of this action (with the specified inputSource) changes.
     *  This happens when the action is bound or unbound, or when the ActionSet changes state.
     *  @param functionToCall: A local function that receives the boolean action who's active state changes and the corresponding input source
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnActiveChangeListener(functionToCall: SteamVR_Action_Vector3_ActiveChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onActiveChange += functionToCall
    }

    /** Stops executing a function when the *functional* active state of this action (with the specified inputSource) changes.
     *  This happens when the action is bound or unbound, or when the ActionSet changes state.
     *  @param functionToStopCalling: The local function that you've setup to receive update events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnActiveChangeListener(functionToStopCalling: SteamVR_Action_Vector3_ActiveChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onActiveChange -= functionToStopCalling
    }

    /** Executes a function when the active state of this action (with the specified inputSource) changes. This happens when the action is bound or unbound
     *  @param functionToCall: A local function that receives the boolean action who's active state changes and the corresponding input source
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnActiveBindingChangeListener(functionToCall: SteamVR_Action_Vector3_ActiveChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onActiveBindingChange += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive update events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnActiveBindingChangeListener(functionToStopCalling: SteamVR_Action_Vector3_ActiveChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onActiveBindingChange -= functionToStopCalling
    }

    /** Executes a function when the axis changes by more than the specified changeTolerance
     *  @param functionToCall: A local function that receives the boolean action who's state has changed, the corresponding input source, and the new value
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnChangeListener(functionToCall: SteamVR_Action_Vector3_ChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onChange += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive on change events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnChangeListener(functionToStopCalling: SteamVR_Action_Vector3_ChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onChange -= functionToStopCalling
    }

    /** Executes a function when the state of this action (with the specified inputSource) is updated.
     *  @param functionToCall: A local function that receives the boolean action who's state has changed, the corresponding input source, and the new value
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnUpdateListener(functionToCall: SteamVR_Action_Vector3_UpdateHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onUpdate += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive update events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnUpdateListener(functionToStopCalling: SteamVR_Action_Vector3_UpdateHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onUpdate -= functionToStopCalling
    }

    /** Executes a function when the Vector3 value of the action is non-zero.
     *  @param functionToCall: A local function that receives the boolean action who's state has changed, the corresponding input source, and the new value
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnAxisListener(functionToCall: SteamVR_Action_Vector3_AxisHandler, inputSource: SteamVR_Input_Sources )    {
        sourceMap!![inputSource]!!.onAxis += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive update events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnAxisListener(functionToStopCalling: SteamVR_Action_Vector3_AxisHandler, inputSource:  SteamVR_Input_Sources)    {
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

class SteamVR_Action_Vector3_Source_Map : SteamVR_Action_In_Source_Map<SteamVR_Action_Vector3_Source>()

class SteamVR_Action_Vector3_Source : SteamVR_Action_In_Source(), ISteamVR_Action_Vector3 {

    /** The amount the axis needs to change before a change is detected */
    var changeTolerance = Float.MIN_VALUE

    /** Event fires when the value of the action is non-zero */
    val onAxis = ArrayList<SteamVR_Action_Vector3_AxisHandler>()

    /** Event fires when the active state (ActionSet active and binding active) changes */
    val onActiveChange = ArrayList<SteamVR_Action_Vector3_ActiveChangeHandler>()

    /** Event fires when the active state of the binding changes */
    val onActiveBindingChange = ArrayList<SteamVR_Action_Vector3_ActiveChangeHandler>()

    /** This event fires whenever the axis changes by more than the specified changeTolerance */
    val onChange = ArrayList<SteamVR_Action_Vector3_ChangeHandler>()

    /** Event fires when the action is updated */
    val onUpdate = ArrayList<SteamVR_Action_Vector3_UpdateHandler>()


    /** The current Vector3 value of the action.
     *  Note: Will only return non-zero if the action is also active. */
    override var axis = Vec3()
        protected set

    /** The Vector3 value of the action from the previous update.
     *  Note: Will only return non-zero if the action is also active. */
    override var lastAxis = Vec3()
        protected set

    /** The Vector3 value difference between this update and the previous update.
     *  Note: Will only return non-zero if the action is also active. */
    override var delta = Vec3()
        protected set

    /** The Vector3 value difference between the previous update and update before that.
     *  Note: Will only return non-zero if the action is also active. */
    override var lastDelta = Vec3()
        protected set

    /** If the Vector3 value of this action has changed more than the changeTolerance since the last update */
    override var changed = false

    /** If the Vector3 value of this action has changed more than the changeTolerance between the previous update and the update before that */
    override var lastChanged = false

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
    override var lastActive = false

    /** @Returns true if the action was bound during the previous update */
    override val lastActiveBinding: Boolean
        get() = lastActionData.active


    protected val actionData = InputAnalogActionData.calloc()
    protected var lastActionData = InputAnalogActionData.calloc()

    protected lateinit var vector3Action: SteamVR_Action_Vector3

    /** [Should not be called by user code] Sets up the internals of the action source before SteamVR has been initialized. */
    override fun preinitialize(wrappingAction: SteamVR_Action, forInputSource: SteamVR_Input_Sources) {
        super.preinitialize(wrappingAction, forInputSource)
        vector3Action = wrappingAction as SteamVR_Action_Vector3
    }

    /** [Should not be called by user code]
     *  Updates the data for this action and this input source. Sends related events. */
    override fun updateValue() {
        lastActionData = actionData
        lastActive = active
        lastAxis = axis
        lastDelta = delta

        val err = vrInput.getAnalogActionData(handle, actionData, SteamVR_Input_Source.getHandle(inputSource))
        if (err != vrInput.Error.None)
            System.err.println("[SteamVR] GetAnalogActionData error ($fullPath): $err handle: $handle")

        updateTime = Time.realtimeSinceStartup
        axis = Vec3(actionData.x, actionData.y, actionData.z)
        delta = Vec3(actionData.deltaX, actionData.deltaY, actionData.deltaZ)

        changed = false

        if (active) {
            if (delta.length() > changeTolerance) {
                changed = true
                changedTime = Time.realtimeSinceStartup + actionData.updateTime //fUpdateTime is the time from the time the action was called that the action changed

                onChange.forEach { it(vector3Action, inputSource, axis, delta) }
            }

            if (axis anyNotEqual 0f)
                onAxis.forEach { it(vector3Action, inputSource, axis, delta) }

            onUpdate.forEach { it(vector3Action, inputSource, axis, delta) }
        }


        if (lastActiveBinding != activeBinding)
            onActiveBindingChange.forEach { it(vector3Action, inputSource, activeBinding) }

        if (lastActive != active)
            onActiveChange.forEach { it(vector3Action, inputSource, activeBinding) }
    }
}

/** Boolean actions are either true or false. There is an onStateUp and onStateDown event for the rising and falling edge. */
interface ISteamVR_Action_Vector3 : ISteamVR_Action_In_Source {

    /** The current float value of the action.
     *  Note: Will only return non-zero if the action is also active. */
    val axis: Vec3

    /** The float value of the action from the previous update.
     *  Note: Will only return non-zero if the action is also active. */
    val lastAxis: Vec3

    /** The float value difference between this update and the previous update.
     *  Note: Will only return non-zero if the action is also active. */
    val delta: Vec3

    /** The float value difference between the previous update and update before that.
     *  Note: Will only return non-zero if the action is also active. */
    val lastDelta: Vec3
}