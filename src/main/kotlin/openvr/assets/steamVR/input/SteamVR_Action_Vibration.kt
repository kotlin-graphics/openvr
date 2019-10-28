package openvr.assets.steamVR.input

import openvr.lib.vrInput
import openvr.plugin2.SteamVR_Input_Sources
import openvr.unity.Time

//======= Copyright (c) Valve Corporation, All rights reserved. ===============

typealias SteamVR_Action_Vibration_ActiveChangeHandler = (fromAction: SteamVR_Action_Vibration, fromSource: SteamVR_Input_Sources, active: Boolean) -> Unit
typealias SteamVR_Action_Vibration_ExecuteHandler = (fromAction: SteamVR_Action_Vibration, fromSource: SteamVR_Input_Sources, secondsFromNow: Float, durationSeconds: Float, frequency: Float, amplitude: Float) -> Unit

/** Vibration actions are used to trigger haptic feedback in vr controllers. */
class SteamVR_Action_Vibration :
        SteamVR_Action_OutT<SteamVR_Action_Vibration_Source_Map, SteamVR_Action_Vibration_Source>()
/*, ISerializationCallbackReceiver*/ {

    /** [SteamVR_Input_Sources.Any] This event fires whenever a change happens in the action */
//    public event ActiveChangeHandler onActiveChange
//    { add { sourceMap[SteamVR_Input_Sources.Any].onActiveChange += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onActiveChange -= value; } }
//
//    /// <summary><strong>[SteamVR_Input_Sources.Any]</strong> This event fires whenever a change happens in the action</summary>
//    public event ActiveChangeHandler onActiveBindingChange
//    { add { sourceMap[SteamVR_Input_Sources.Any].onActiveBindingChange += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onActiveBindingChange -= value; } }
//
//    /// <summary><strong>[SteamVR_Input_Sources.Any]</strong> This event fires whenever this action is executed</summary>
//    public event ExecuteHandler onExecute
//    { add { sourceMap[SteamVR_Input_Sources.Any].onExecute += value; } remove { sourceMap[SteamVR_Input_Sources.Any].onExecute -= value; } }


    /** Trigger the haptics at a certain time for a certain length
     *
     *  @param secondsFromNow: How long from the current time to execute the action (in seconds - can be 0)
     *  @param durationSeconds: How long the haptic action should last (in seconds)
     *  @param frequency: How often the haptic motor should bounce (0 - 320 in hz. The lower end being more useful)
     *  @param amplitude: How intense the haptic action should be (0 - 1)
     *  @param inputSource: The device you would like to execute the haptic action. Any if the action is not device specific. */
    fun execute(secondsFromNow: Float, durationSeconds: Float, frequency: Float, amplitude: Float, inputSource: SteamVR_Input_Sources) =
            sourceMap!![inputSource]!!.execute(secondsFromNow, durationSeconds, frequency, amplitude)


    /** Executes a function when the *functional* active state of this action (with the specified inputSource) changes.
     *  This happens when the action is bound or unbound, or when the ActionSet changes state.
     *  @param functionToCall: A local function that receives the boolean action who's active state changes and the corresponding input source
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnActiveChangeListener(functionToCall: SteamVR_Action_Vibration_ActiveChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onActiveChange += functionToCall
    }

    /** Stops executing a function when the *functional* active state of this action (with the specified inputSource) changes.
     *  This happens when the action is bound or unbound, or when the ActionSet changes state.
     *  @param functionToStopCalling: The local function that you've setup to receive update events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnActiveChangeListener(functionToStopCalling: SteamVR_Action_Vibration_ActiveChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onActiveChange -= functionToStopCalling
    }

    /** Executes a function when the active state of this action (with the specified inputSource) changes. This happens when the action is bound or unbound
     *  @param functionToCall: A local function that receives the boolean action who's active state changes and the corresponding input source
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnActiveBindingChangeListener(functionToCall: SteamVR_Action_Vibration_ActiveChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onActiveBindingChange += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive update events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnActiveBindingChangeListener(functionToStopCalling: SteamVR_Action_Vibration_ActiveChangeHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onActiveBindingChange -= functionToStopCalling
    }

    /** Executes a function when the execute method of this action (with the specified inputSource) is called. This happens when the action is bound or unbound
     *  @param functionToCall: A local function that receives the boolean action who's active state changes and the corresponding input source
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun addOnExecuteListener(functionToCall: SteamVR_Action_Vibration_ExecuteHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onExecute += functionToCall
    }

    /** Stops executing the function setup by the corresponding AddListener
     *  @param functionToStopCalling: The local function that you've setup to receive update events
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun removeOnExecuteListener(functionToStopCalling: SteamVR_Action_Vibration_ExecuteHandler, inputSource: SteamVR_Input_Sources) {
        sourceMap!![inputSource]!!.onExecute -= functionToStopCalling
    }

    /** @Returns the last time this action was executed
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    override fun getTimeLastChanged(inputSource: SteamVR_Input_Sources): Float =
            sourceMap!![inputSource]!!.timeLastExecuted

//    void ISerializationCallbackReceiver.OnBeforeSerialize()
//    {
//    }
//
//    void ISerializationCallbackReceiver.OnAfterDeserialize()
//    {
//        InitAfterDeserialize()
//    }

    override fun isUpdating(inputSource: SteamVR_Input_Sources): Boolean =
            sourceMap!!.isUpdating(inputSource)
}

class SteamVR_Action_Vibration_Source_Map : SteamVR_Action_Source_MapT<SteamVR_Action_Vibration_Source>() {
    fun isUpdating(inputSource: SteamVR_Input_Sources): Boolean =
            sources[inputSource]!!.timeLastExecuted != 0f
}

class SteamVR_Action_Vibration_Source : SteamVR_Action_Out_Source() {

    /** Event fires when the active state (ActionSet active and binding active) changes */
    val onActiveChange = ArrayList<SteamVR_Action_Vibration_ActiveChangeHandler>()

    /** Event fires when the active state of the binding changes */
    val onActiveBindingChange = ArrayList<SteamVR_Action_Vibration_ActiveChangeHandler>()

    /** Event fires whenever this action is executed */
    val onExecute = ArrayList<SteamVR_Action_Vibration_ExecuteHandler>()

    //todo: fix the active state of out actions
    /** @Returns true if this action is bound and the ActionSet is active */
    override val active: Boolean
        get() = activeBinding && setActive

    /** @Returns true if the action is bound */
    override val activeBinding: Boolean
        get() = true


    /** @Returns true if the action was bound and the ActionSet was active during the previous update */
    override var lastActive = false

    /** @Returns true if the action was bound during the previous update */
    override val lastActiveBinding: Boolean
        get() = true

    /** The last time the execute method was called on this action */
    var timeLastExecuted: Float = 0f
        protected set

    protected lateinit var vibrationAction: SteamVR_Action_Vibration

    override fun preinitialize(wrappingAction: SteamVR_Action, forInputSource: SteamVR_Input_Sources) {
        super.preinitialize(wrappingAction, forInputSource)
        vibrationAction = wrappingAction as SteamVR_Action_Vibration
    }


    /** Trigger the haptics at a certain time for a certain length
     *
     *  @param secondsFromNow: How long from the current time to execute the action (in seconds - can be 0)
     *  @param durationSeconds: How long the haptic action should last (in seconds)
     *  @param frequency: How often the haptic motor should bounce (0 - 320 in hz. The lower end being more useful)
     *  @param amplitude: How intense the haptic action should be (0 - 1)
     *  @param inputSource: The device you would like to execute the haptic action. Any if the action is not device specific. */
    fun execute(secondsFromNow: Float, durationSeconds: Float, frequency: Float, amplitude: Float) {
        if (SteamVR_Input.isStartupFrame)
            return

        timeLastExecuted = Time.realtimeSinceStartup

        val err = vrInput.triggerHapticVibrationAction(handle, secondsFromNow, durationSeconds, frequency, amplitude, inputSourceHandle)

        //Debug.Log(string.Format("[{5}: haptic] secondsFromNow({0}), durationSeconds({1}), frequency({2}), amplitude({3}), inputSource({4})", secondsFromNow, durationSeconds, frequency, amplitude, inputSource, this.GetShortName()));

        if (err != vrInput.Error.None)
            System.err.println("[SteamVR] TriggerHapticVibrationAction ($fullPath) error: $err handle: $handle")

        onExecute.forEach { it(vibrationAction, inputSource, secondsFromNow, durationSeconds, frequency, amplitude) }
    }
}


/** Vibration actions are used to trigger haptic feedback in vr controllers. */
interface ISteamVR_Action_Vibration : ISteamVR_Action_Out {

    /** Trigger the haptics at a certain time for a certain length
     *
     *  @param secondsFromNow: How long from the current time to execute the action (in seconds - can be 0)
     *  @param durationSeconds: How long the haptic action should last (in seconds)
     *  @param frequency: How often the haptic motor should bounce (0 - 320 in hz. The lower end being more useful)
     *  @param amplitude: How intense the haptic action should be (0 - 1)
     *  @param inputSource: The device you would like to execute the haptic action. Any if the action is not device specific. */
    fun execute(secondsFromNow: Float, durationSeconds: Float, frequency: Float, amplitude: Float, inputSource: SteamVR_Input_Sources)
}