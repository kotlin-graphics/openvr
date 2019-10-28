package openvr.steamVR.input

import com.beust.klaxon.Klaxon
import glm_.vec2.Vec2
import glm_.vec3.Vec3
import openvr.lib.VRInputValueHandle
import openvr.lib.vrInput
import openvr.lib.vrInput.VRInputString
import openvr.plugin2.SteamVR_Input_Source
import openvr.plugin2.SteamVR_Input_Sources
import openvr.steamVR.script.SteamVR_Behaviour
import openvr.steamVR.script.SteamVR_Settings
import openvr.steamVR.script.SteamVR_Utils
import openvr.steamVR_Input.SteamVR_Actions
import openvr.unity.Application
import openvr.unity.Time

//======= Copyright (c) Valve Corporation, All rights reserved. ===============

typealias PosesUpdatedHandler = (skipSendingEvents: Boolean) -> Unit
typealias SkeletonsUpdatedHandler = (skipSendingEvents: Boolean) -> Unit
typealias Action = () -> Unit

fun main() {
    println(org.lwjgl.openvr.VR.k_nSteamVRVersionBuild)
}

object SteamVR_Input {

    val defaultInputGameObjectName = "[SteamVR SteamVR_Input]"
    private val localizationKeyName = "localization"

    var actionsFilePath = SteamVR_Settings.actionsFilePath

    /** True if the actions file has been initialized */
    var fileInitialized = false

    /** True if the steamvr input system initialization process has completed successfully */
    var initialized = false

    /** True if the preinitialization process (setting up dictionaries, etc) has completed successfully */
    var preInitialized = false

    /** The serialized version of the actions file we're currently using (only used in editor) */
    lateinit var actionFile: SteamVR_Input_ActionFile

    /** The hash of the current action file on disk */
    var actionFileHash: String? = null

    /** An event that fires when the non visual actions (everything except poses / skeletons) have been updated */
    val onNonVisualActionsUpdated = ArrayList<Action>()

    /** An event that fires when the pose actions have been updated */
    val onPosesUpdated = ArrayList<PosesUpdatedHandler>()

    /** An event that fires when the skeleton actions have been updated */
    val onSkeletonsUpdated = ArrayList<SkeletonsUpdatedHandler>()


    private var initializing = false

    private var startupFrame = 0
    val isStartupFrame: Boolean
        get() = Time.frameCount >= (startupFrame - 1) && Time.frameCount <= (startupFrame + 1)

    /** An array of all action sets */
    lateinit var actionSets: Array<SteamVR_ActionSet>

    /** An array of all actions (in all action sets) */
    lateinit var actions: Array<SteamVR_Action>

    /** An array of all input actions */
    lateinit var actionsIn: Array<ISteamVR_Action_In>

    /** An array of all output actions (haptic) */
    lateinit var actionsOut: Array<ISteamVR_Action_Out>

    /** An array of all the boolean actions */
    lateinit var actionsBoolean: Array<SteamVR_Action_Boolean>

    /** An array of all the single actions */
    lateinit var actionsSingle: Array<SteamVR_Action_Single>

    /** An array of all the vector2 actions */
    lateinit var actionsVector2: Array<SteamVR_Action_Vector2>

    /** An array of all the vector3 actions */
    lateinit var actionsVector3: Array<SteamVR_Action_Vector3>

    /** An array of all the pose actions */
    lateinit var actionsPose: Array<SteamVR_Action_Pose>

    /** An array of all the skeleton actions */
    lateinit var actionsSkeleton: Array<SteamVR_Action_Skeleton>

    /** An array of all the vibration (haptic) actions */
    lateinit var actionsVibration: Array<SteamVR_Action_Vibration>

    /** An array of all the input actions that are not pose or skeleton actions (boolean, single, vector2, vector3) */
    lateinit var actionsNonPoseNonSkeletonIn: Array<ISteamVR_Action_In>

    private val actionSetsByPath: MutableMap<String, SteamVR_ActionSet> = mutableMapOf()
    private val actionSetsByPathLowered: MutableMap<String, SteamVR_ActionSet> = mutableMapOf()
    private val actionsByPath: MutableMap<String, SteamVR_Action> = mutableMapOf()
    private val actionsByPathLowered: MutableMap<String, SteamVR_Action> = mutableMapOf()

    private val actionSetsByPathCache: MutableMap<String, SteamVR_ActionSet?> = mutableMapOf()
    private val actionsByPathCache: MutableMap<String, SteamVR_Action?> = mutableMapOf()

    private val actionsByNameCache: MutableMap<String, SteamVR_Action> = mutableMapOf()
    private val actionSetsByNameCache: MutableMap<String, SteamVR_Action> = mutableMapOf()

    init {
//        #if !UNITY_EDITOR
        //If you want a single frame of performance increase on application start and have already generated your actions uncomment the following two lines
        SteamVR_Actions.preInitialize()
        //return;
//        #endif
//        FindPreinitializeMethod()
    }

    fun forcePreinitialize() = findPreinitializeMethod()

    private fun findPreinitializeMethod() {
//        Assembly[] assemblies = AppDomain . CurrentDomain . GetAssemblies ()
//        for (int assemblyIndex = 0; assemblyIndex < assemblies.Length; assemblyIndex++)
//        {
//            Assembly assembly = assemblies [assemblyIndex]
//            Type type = assembly . GetType (SteamVR_Input_Generator_Names.fullActionsClassName)
//            if (type != null) {
//                MethodInfo preinitMethodInfo = type . GetMethod (SteamVR_Input_Generator_Names.preinitializeMethodName)
//                if (preinitMethodInfo != null) {
//                    preinitMethodInfo.Invoke(null, null)
//                    return
//                }
//            }
//        }
    }

    /** Get all the handles for actions and action sets.
     *  Initialize our dictionaries of action / action set names.
     *  Setup the tracking space universe origin */
    fun initialize(force: Boolean = false) {

        if (initialized && !force)
            return

//        #if UNITY_EDITOR
//        CheckSetup()
//        if (IsOpeningSetup())
//            return
//        #endif

        //Debug.Log("<b>[SteamVR]</b> Initializing SteamVR input...");
        initializing = true

        startupFrame = Time.frameCount

        SteamVR_ActionSet_Manager.initialize()
        SteamVR_Input_Source.initialize()

        actions.forEach { it.initialize(true) }
        actionSets.forEach { it.initialize(true) }

        if (SteamVR_Settings.activateFirstActionSetOnStart) {
            if (actionSets.isNotEmpty())
                actionSets[0].activate()
            else
                System.err.println("[SteamVR] No action sets to activate.")
        }

        SteamVR_Action_Pose.setTrackingUniverseOrigin(SteamVR_Settings.trackingSpace)

        initialized = true
        initializing = false
        //Debug.Log("<b>[SteamVR]</b> Input initialization complete.");
    }

    fun preinitializeFinishActionSets() {
        for (actionSet in actionSets)
            actionSet.finishPreInitialize()
    }

    fun preinitializeActionSetDictionaries() {
        actionSetsByPath.clear()
        actionSetsByPathLowered.clear()
        actionSetsByPathCache.clear()

        for (actionSet in actionSets) {
            actionSetsByPath[actionSet.fullPath!!] = actionSet
            actionSetsByPathLowered[actionSet.fullPath!!.toLowerCase()] = actionSet
        }
    }

    fun preinitializeActionDictionaries() {
        actionsByPath.clear()
        actionsByPathLowered.clear()
        actionsByPathCache.clear()

        for (action in actions) {
            actionsByPath[action.fullPath] = action
            actionsByPathLowered[action.fullPath.toLowerCase()] = action
        }
    }

    /** Gets called by SteamVR_Behaviour every Update and updates actions if the steamvr settings are configured to update then. */
    fun update() {

        if (!initialized || isStartupFrame)
            return

        if (SteamVR_Settings.isInputUpdateMode(SteamVR_UpdateModes.OnUpdate))
            updateNonVisualActions()
        if (SteamVR_Settings.isPoseUpdateMode(SteamVR_UpdateModes.OnUpdate))
            updateVisualActions()
    }

    /** Gets called by SteamVR_Behaviour every LateUpdate and updates actions if the steamvr settings are configured to update then.
     *  Also updates skeletons regardless of settings are configured to so we can account for animations on the skeletons. */
    fun lateUpdate() {

        if (!initialized || isStartupFrame)
            return

        if (SteamVR_Settings.isInputUpdateMode(SteamVR_UpdateModes.OnLateUpdate))
            updateNonVisualActions()

        if (SteamVR_Settings.isPoseUpdateMode(SteamVR_UpdateModes.OnLateUpdate))
            updateVisualActions() //update poses and skeleton
        else
            updateSkeletonActions(true) //force skeleton update so animation blending sticks
    }

    /** Gets called by SteamVR_Behaviour every FixedUpdate and updates actions if the steamvr settings are configured to update then. */
    fun fixedUpdate() {

        if (!initialized || isStartupFrame)
            return

        if (SteamVR_Settings.isInputUpdateMode(SteamVR_UpdateModes.OnFixedUpdate))
            updateNonVisualActions()

        if (SteamVR_Settings.isPoseUpdateMode(SteamVR_UpdateModes.OnFixedUpdate))
            updateVisualActions()
    }

    /** Gets called by SteamVR_Behaviour every OnPreCull and updates actions if the steamvr settings are configured to update then. */
    fun onPreCull() {

        if (!initialized || isStartupFrame)
            return

        if (SteamVR_Settings.isInputUpdateMode(SteamVR_UpdateModes.OnPreCull))
            updateNonVisualActions()
        if (SteamVR_Settings.isPoseUpdateMode(SteamVR_UpdateModes.OnPreCull))
            updateVisualActions()
    }

    /** Updates the states of all the visual actions (pose / skeleton)
     *
     *  @param skipStateAndEventUpdates: Controls whether or not events are fired from this update call */
    fun updateVisualActions(skipStateAndEventUpdates: Boolean = false) {

        if (!initialized)
            return

        SteamVR_ActionSet_Manager.updateActionStates()

        updatePoseActions(skipStateAndEventUpdates)

        updateSkeletonActions(skipStateAndEventUpdates)
    }

    /** Updates the states of all the pose actions
     *
     *  @param skipSendingEvents: Controls whether or not events are fired from this update call */
    fun updatePoseActions(skipSendingEvents: Boolean = false) {

        if (!initialized)
            return

        actionsPose.forEach { it.updateValues(skipSendingEvents) }

        onPosesUpdated.forEach { it.invoke(false) }
    }


    /** Updates the states of all the skeleton actions
     *  @param skipSendingEvents: Controls whether or not events are fired from this update call */
    fun updateSkeletonActions(skipSendingEvents: Boolean = false) {

        if (!initialized)
            return

        actionsSkeleton.forEach { it.updateValue(skipSendingEvents) }

        onSkeletonsUpdated.forEach { it.invoke(skipSendingEvents) }
    }


    /** Updates the states of all the non visual actions (boolean, single, vector2, vector3) */
    fun updateNonVisualActions() {

        if (!initialized)
            return

        SteamVR_ActionSet_Manager.updateActionStates()

        actionsNonPoseNonSkeletonIn.forEach { it.updateValues() }

        onNonVisualActionsUpdated.forEach { it.invoke() }
    }


    /** Get an action's action data by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param T: The type of action you're expecting to get back
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun <T : SteamVR_Action_Source_Map> getActionDataFromPath(path: String?, caseSensitive: Boolean = false): T? =
            getBaseActionFromPath(path, caseSensitive)?.sourceMap as? T

    /** Get an action set's data by the full path to that action. Action set paths are in the format /actions/[actionSet]
     *
     *  @param path: The full path to the action you want (Action set paths are in the format /actions/[actionSet])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getActionSetDataFromPath(path: String?, caseSensitive: Boolean = false): SteamVR_ActionSet_Data? =
            getActionSetFromPath(path, caseSensitive)?.setData

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param T: The type of action you're expecting to get back
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun <T : SteamVR_Action> getActionFromPath(type: ActionType, path: String, caseSensitive: Boolean = false, returnNulls: Boolean = false): T? {
        val foundAction = getBaseActionFromPath(path, caseSensitive)
        if (foundAction != null)
            return foundAction.getCopy()

        if (returnNulls)
            return null

        return createFakeAction(type, path, caseSensitive)
    }

    // non-copy version
    fun getBaseActionFromPath(path: String?, caseSensitive: Boolean = false): SteamVR_Action? {
        if (path.isNullOrEmpty())
            return null

        return when {
            caseSensitive -> actionsByPath[path]
            else -> actionsByPathCache[path] ?: run {
                if (path in actionsByPath) {
                    actionsByPathCache[path] = actionsByPath[path]
                    actionsByPath[path]
                } else {
                    val loweredPath = path.toLowerCase()
                    actionsByPathCache[path] = actionsByPathLowered[loweredPath]
                    actionsByPath[loweredPath]
                }
            }
        }
    }

    fun hasActionPath(path: String, caseSensitive: Boolean = false): Boolean =
            getBaseActionFromPath(path, caseSensitive) != null

    fun hasAction(actionName: String, caseSensitive: Boolean = false): Boolean =
            getBaseAction(null, actionName, caseSensitive) != null

    fun hasAction(actionSetName: String, actionName: String, caseSensitive: Boolean = false) =
            getBaseAction(actionSetName, actionName, caseSensitive) != null

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getBooleanActionFromPath(path: String, caseSensitive: Boolean = false): SteamVR_Action_Boolean? =
            getActionFromPath(ActionType.Boolean, path, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getSingleActionFromPath(path: String, caseSensitive: Boolean = false): SteamVR_Action_Single? =
            getActionFromPath(ActionType.Single, path, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getVector2ActionFromPath(path: String, caseSensitive: Boolean = false): SteamVR_Action_Vector2? =
            getActionFromPath(ActionType.Vector2, path, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getVector3ActionFromPath(path: String, caseSensitive: Boolean = false): SteamVR_Action_Vector3? =
            getActionFromPath(ActionType.Vector3, path, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getVibrationActionFromPath(path: String, caseSensitive: Boolean = false): SteamVR_Action_Vibration? =
            getActionFromPath(ActionType.Vibration, path, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getPoseActionFromPath(path: String, caseSensitive: Boolean = false): SteamVR_Action_Pose? =
            getActionFromPath(ActionType.Pose, path, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getSkeletonActionFromPath(path: String, caseSensitive: Boolean = false): SteamVR_Action_Skeleton? =
            getActionFromPath(ActionType.Skeleton, path, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param T: The type of action you're expecting to get back
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster
     *  @param returnNulls: returns null if the action does not exist */
    fun <T : SteamVR_Action> getAction(type: ActionType, actionSetName: String?, actionName: String, caseSensitive: Boolean = false,
                                       returnNulls: Boolean = false): T? {
        val action = getBaseAction(actionSetName, actionName, caseSensitive)
        if (action != null)
            return action.getCopy()

        if (returnNulls)
            return null

        return createFakeAction(type, actionSetName, actionName, caseSensitive)
    }

    fun getBaseAction(actionSetName: String?, actionName: String, caseSensitive: Boolean = false): SteamVR_Action? {

        if (actions == null)
            return null

        if (actionSetName.isNullOrEmpty()) {
            for (action in actions) {
                if (caseSensitive) {
                    if (action.shortName == actionName)
                        return action
                } else {
                    if (action.shortName.equals(actionName, ignoreCase = true))
                        return action
                }
            }
        } else {
            val actionSet = getActionSet(actionSetName, caseSensitive, true)

            if (actionSet != null) {
                for (action in actionSet.allActions) {
                    if (caseSensitive) {
                        if (action.shortName == actionName)
                            return action
                    } else {
                        if (action.shortName.equals(actionName, ignoreCase = true))
                            return action
                    }
                }
            }
        }

        return null
    }

    private fun <T : SteamVR_Action> createFakeAction(type: ActionType, actionSetName: String?, actionName: String,
                                                      caseSensitive: Boolean): T {
        val direction = when (type) {
            ActionType.Vibration -> SteamVR_ActionDirections.Out
            else -> SteamVR_ActionDirections.In
        }
        return SteamVR_Action.createUninitialized(type, actionSetName!!, direction, actionName, caseSensitive) // TODO check the fucking nullability
    }

    private fun <T : SteamVR_Action> createFakeAction(type: ActionType, actionPath: String, caseSensitive: Boolean): T =
            SteamVR_Action.createUninitialized(type, actionPath, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param T: The type of action you're expecting to get back
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun <T : SteamVR_Action> getAction(type: ActionType, actionName: String, caseSensitive: Boolean = false): T? =
            getAction(type, null, actionName, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param T: The type of action you're expecting to get back
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getBooleanAction(actionSetName: String, actionName: String, caseSensitive: Boolean = false): SteamVR_Action_Boolean? =
            getAction(ActionType.Boolean, actionSetName, actionName, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param T: The type of action you're expecting to get back
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getBooleanAction(actionName: String, caseSensitive: Boolean = false): SteamVR_Action_Boolean? =
            getAction(ActionType.Boolean, null, actionName, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param T: The type of action you're expecting to get back
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getSingleAction(actionSetName: String, actionName: String, caseSensitive: Boolean = false): SteamVR_Action_Single? =
            getAction(ActionType.Single, actionSetName, actionName, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param T: The type of action you're expecting to get back
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getSingleAction(actionName: String, caseSensitive: Boolean = false): SteamVR_Action_Single? =
            getAction(ActionType.Single, null, actionName, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param T: The type of action you're expecting to get back
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getVector2Action(actionSetName: String, actionName: String, caseSensitive: Boolean = false): SteamVR_Action_Vector2? =
            getAction(ActionType.Vector2, actionSetName, actionName, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param T: The type of action you're expecting to get back
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getVector2Action(actionName: String, caseSensitive: Boolean = false): SteamVR_Action_Vector2? =
            getAction(ActionType.Vector2, null, actionName, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param T: The type of action you're expecting to get back
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getVector3Action(actionSetName: String, actionName: String, caseSensitive: Boolean = false): SteamVR_Action_Vector3? =
            getAction(ActionType.Vector3, actionSetName, actionName, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param T: The type of action you're expecting to get back
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: >case sensitive searches are faster */
    fun getVector3Action(actionName: String, caseSensitive: Boolean = false): SteamVR_Action_Vector3? =
            getAction(ActionType.Vector3, null, actionName, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param T: The type of action you're expecting to get back
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getPoseAction(actionSetName: String, actionName: String, caseSensitive: Boolean = false): SteamVR_Action_Pose? =
            getAction(ActionType.Pose, actionSetName, actionName, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param T: The type of action you're expecting to get back
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getPoseAction(actionName: String, caseSensitive: Boolean = false): SteamVR_Action_Pose? =
            getAction(ActionType.Pose, null, actionName, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param T: The type of action you're expecting to get back
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getSkeletonAction(actionSetName: String, actionName: String, caseSensitive: Boolean = false): SteamVR_Action_Skeleton? =
            getAction(ActionType.Skeleton, actionSetName, actionName, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param T: The type of action you're expecting to get back
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getSkeletonAction(actionName: String, caseSensitive: Boolean = false): SteamVR_Action_Skeleton? =
            getAction(ActionType.Skeleton, null, actionName, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param T: The type of action you're expecting to get back
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getVibrationAction(actionSetName: String, actionName: String, caseSensitive: Boolean = false): SteamVR_Action_Vibration? =
            getAction(ActionType.Vibration, actionSetName, actionName, caseSensitive)

    /** Get an action by the full path to that action. Action paths are in the format /actions/[actionSet]/[direction]/[actionName]
     *
     *  @param T: The type of action you're expecting to get back
     *  @param path: The full path to the action you want (Action paths are in the format /actions/[actionSet]/[direction]/[actionName])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getVibrationAction(actionName: String, caseSensitive: Boolean = false): SteamVR_Action_Vibration? =
            getAction(ActionType.Vibration, null, actionName, caseSensitive)

    /** Get an action set by the full path to that action set. Action set paths are in the format /actions/[actionSet]
     *
     *  @param T: The type of action set you're expecting to get back
     *  @param actionSetName: The name to the action set you want
     *  @param caseSensitive: case sensitive searches are faster
     *  @param returnNulls: returns a null if the set does not exist */
    fun <T : SteamVR_ActionSet> getActionSet(clazz: Class<T>, actionSetName: String, caseSensitive: Boolean = false, returnNulls: Boolean = false): T? {

        if (actionSets == null) {
            if (returnNulls)
                return null

            return SteamVR_ActionSet.createFromName(clazz, actionSetName)
        }

        for (actionSet in actionSets) {
            if (caseSensitive) {
                if (actionSet.shortName == actionSetName)
                    return actionSet.getCopy()
            } else {
                if (actionSet.shortName.equals(actionSetName, ignoreCase = true))
                    return actionSet.getCopy()
            }
        }

        if (returnNulls)
            return null

        return SteamVR_ActionSet.createFromName(clazz, actionSetName)
    }

    /** Get an action set by the full path to that action set. Action set paths are in the format /actions/[actionSet]
     *
     *  @param T: The type of action set you're expecting to get back
     *  @param actionSetName: The name to the action set you want
     *  @param caseSensitive: case sensitive searches are faster */
    fun getActionSet(actionSetName: String, caseSensitive: Boolean = false, returnsNulls: Boolean = false): SteamVR_ActionSet? =
            getActionSet(SteamVR_ActionSet::class.java, actionSetName, caseSensitive, returnsNulls)

    private fun hasActionSet(name: String, caseSensitive: Boolean = false): Boolean =
            getActionSet(name, caseSensitive, true) != null

    /** Get an action set by the full path to that action set. Action set paths are in the format /actions/[actionSet]
     *
     *  @typeparam name="T">The type of action set you're expecting to get back</typeparam>
     *  @param path: The full path to the action set you want (Action paths are in the format /actions/[actionSet])
     *  @param caseSensitive: case sensitive searches are faster */
    fun <T : SteamVR_ActionSet> getActionSetFromPath(clazz: Class<T>, path: String?, caseSensitive: Boolean = false,
                                                     returnsNulls: Boolean = false): T? {

        if (actionSets == null || actionSets[0] == null || path.isNullOrEmpty()) {
            if (returnsNulls)
                return null

            return SteamVR_ActionSet.create(clazz, path)
        }

        if (caseSensitive)
            actionSetsByPath[path]?.let { return it.getCopy() }
        else {
            if (path in actionSetsByPathCache) {
                return actionSetsByPathCache[path]?.getCopy()
            } else if (path in actionSetsByPath) {
                actionSetsByPathCache[path] = actionSetsByPath[path]
                return actionSetsByPath[path]!!.getCopy()
            } else {
                val loweredPath = path.toLowerCase()
                if (loweredPath in actionSetsByPathLowered) {
                    actionSetsByPathCache[path] = actionSetsByPathLowered[loweredPath]
                    return actionSetsByPath[loweredPath]!!.getCopy()
                } else
                    actionSetsByPathCache[path] = null
            }
        }

        if (returnsNulls)
            return null

        return SteamVR_ActionSet.create(clazz, path)
    }

    /** Get an action set by the full path to that action set. Action set paths are in the format /actions/[actionSet]
     *  @param path: The full path to the action set you want (Action paths are in the format /actions/[actionSet])
     *  @param caseSensitive: case sensitive searches are faster */
    fun getActionSetFromPath(path: String?, caseSensitive: Boolean = false): SteamVR_ActionSet? =
            getActionSetFromPath(SteamVR_ActionSet::class.java, path, caseSensitive)

    /** Get the state of an action by the action set name, action name, and input source. Optionally case sensitive (for faster results)
     *
     *  @param actionSet: The name of the action set the action is contained in
     *  @param action: The name of the action to get the state of
     *  @param inputSource: The input source to get the action state from
     *  @param caseSensitive: Whether or not the action set and action name searches should be case sensitive (case sensitive searches are faster) */
    fun getState(actionSet: String?, action: String, inputSource: SteamVR_Input_Sources, caseSensitive: Boolean = false): Boolean =
            getAction<SteamVR_Action_Boolean>(ActionType.Boolean, actionSet, action, caseSensitive)
                    ?.getState(inputSource) ?: false

    /** Get the state of an action by the action name and input source. Optionally case sensitive (for faster results)
     *
     *  @param action: The name of the action to get the state of
     *  @param inputSource: The input source to get the action state from
     *  @param caseSensitive: Whether or not the action set and action name searches should be case sensitive (case sensitive searches are faster) */
    fun getState(action: String, inputSource: SteamVR_Input_Sources, caseSensitive: Boolean = false): Boolean =
            getState(null, action, inputSource, caseSensitive)

    /** Get the state down of an action by the action set name, action name, and input source. Optionally case sensitive (for faster results)
     *
     *  @param actionSet: The name of the action set the action is contained in
     *  @param action: The name of the action to get the state of
     *  @param inputSource: The input source to get the action state from
     *  @param caseSensitive: Whether or not the action set and action name searches should be case sensitive (case sensitive searches are faster)
     *  @returns True when the action was false last update and is now true. Returns false again afterwards. */
    fun getStateDown(actionSet: String?, action: String, inputSource: SteamVR_Input_Sources, caseSensitive: Boolean = false): Boolean =
            getAction<SteamVR_Action_Boolean>(ActionType.Boolean, actionSet, action, caseSensitive)
                    ?.getStateDown(inputSource) ?: false

    /** Get the state down of an action by the action name and input source. Optionally case sensitive (for faster results)
     *
     *  @param action: The name of the action to get the state of
     *  @param inputSource: The input source to get the action state from
     *  @param caseSensitive: Whether or not the action set and action name searches should be case sensitive (case sensitive searches are faster)
     *  @returns True when the action was false last update and is now true. Returns false again afterwards. */
    fun getStateDown(action: String, inputSource: SteamVR_Input_Sources, caseSensitive: Boolean = false): Boolean =
            getStateDown(null, action, inputSource, caseSensitive)

    /** Get the state up of an action by the action set name, action name, and input source. Optionally case sensitive (for faster results)
     *
     *  @param actionSet: The name of the action set the action is contained in
     *  @param action: The name of the action to get the state of
     *  @param inputSource: The input source to get the action state from
     *  @param caseSensitive: Whether or not the action set and action name searches should be case sensitive (case sensitive searches are faster)
     *  @returns True when the action was true last update and is now false. Returns false again afterwards. */
    fun getStateUp(actionSet: String?, action: String, inputSource: SteamVR_Input_Sources, caseSensitive: Boolean = false): Boolean =
            getAction<SteamVR_Action_Boolean>(ActionType.Boolean, actionSet, action, caseSensitive)
                    ?.getStateUp(inputSource) ?: false


    /** Get the state up of an action by the action name and input source. Optionally case sensitive (for faster results)
     *
     *  @param action: The name of the action to get the state of
     *  @param inputSource: The input source to get the action state from
     *  @param caseSensitive: Whether or not the action set and action name searches should be case sensitive (case sensitive searches are faster)
     *  @returns True when the action was true last update and is now false. Returns false again afterwards. */
    fun getStateUp(action: String, inputSource: SteamVR_Input_Sources, caseSensitive: Boolean = false): Boolean =
            getStateUp(null, action, inputSource, caseSensitive)

    /** Get the float value of an action by the action set name, action name, and input source. Optionally case sensitive (for faster results). (same as GetSingle)
     *
     *  @param actionSet: The name of the action set the action is contained in
     *  @param action: The name of the action to get the state of
     *  @param inputSource: The input source to get the action state from
     *  @param caseSensitive: Whether or not the action set and action name searches should be case sensitive (case sensitive searches are faster) */
    fun getFloat(actionSet: String?, action: String, inputSource: SteamVR_Input_Sources, caseSensitive: Boolean = false): Float =
            getAction<SteamVR_Action_Single>(ActionType.Single, actionSet, action, caseSensitive)?.getAxis(inputSource)
                    ?: 0f

    /** Get the float value of an action by the action name and input source. Optionally case sensitive (for faster results). (same as GetSingle)
     *
     *  @param action: The name of the action to get the state of
     *  @param inputSource: The input source to get the action state from
     *  @param caseSensitive: Whether or not the action set and action name searches should be case sensitive (case sensitive searches are faster) */
    fun getFloat(action: String, inputSource: SteamVR_Input_Sources, caseSensitive: Boolean = false): Float =
            getFloat(null, action, inputSource, caseSensitive)

    /** Get the float value of an action by the action set name, action name, and input source. Optionally case sensitive (for faster results). (same as GetFloat)
     *
     *  @param actionSet: The name of the action set the action is contained in
     *  @param action: The name of the action to get the state of
     *  @param inputSource: The input source to get the action state from
     *  @param caseSensitive: Whether or not the action set and action name searches should be case sensitive (case sensitive searches are faster) */
    fun getSingle(actionSet: String?, action: String, inputSource: SteamVR_Input_Sources, caseSensitive: Boolean = false): Float =
            getAction<SteamVR_Action_Single>(ActionType.Single, actionSet, action, caseSensitive)
                    ?.getAxis(inputSource) ?: 0f

    /** Get the float value of an action by the action name and input source. Optionally case sensitive (for faster results). (same as GetFloat)
     *
     *  @param action: The name of the action to get the state of
     *  @param inputSource: The input source to get the action state from
     *  @param caseSensitive: Whether or not the action set and action name searches should be case sensitive (case sensitive searches are faster) */
    fun getSingle(action: String, inputSource: SteamVR_Input_Sources, caseSensitive: Boolean = false): Float =
            getFloat(null, action, inputSource, caseSensitive)

    /** Get the Vector2 value of an action by the action set name, action name, and input source. Optionally case sensitive (for faster results)
     *
     *  @param actionSet: The name of the action set the action is contained in
     *  @param action: The name of the action to get the state of
     *  @param inputSource: The input source to get the action state from
     *  @param caseSensitive: Whether or not the action set and action name searches should be case sensitive (case sensitive searches are faster) */
    fun getVector2(actionSet: String?, action: String, inputSource: SteamVR_Input_Sources, caseSensitive: Boolean = false): Vec2 =
            getAction<SteamVR_Action_Vector2>(ActionType.Vector2, actionSet, action, caseSensitive)
                    ?.getAxis(inputSource) ?: Vec2()

    /** Get the Vector2 value of an action by the action name and input source. Optionally case sensitive (for faster results)
     *
     *  @param action: The name of the action to get the state of
     *  @param inputSource: The input source to get the action state from
     *  @param caseSensitive: Whether or not the action set and action name searches should be case sensitive (case sensitive searches are faster) */
    fun getVector2(action: String, inputSource: SteamVR_Input_Sources, caseSensitive: Boolean = false): Vec2 =
            getVector2(null, action, inputSource, caseSensitive)

    /** Get the Vector3 value of an action by the action set name, action name, and input source. Optionally case sensitive (for faster results)
     *
     *  @param actionSet: The name of the action set the action is contained in
     *  @param action: The name of the action to get the state of
     *  @param inputSource: The input source to get the action state from
     *  @param caseSensitive: Whether or not the action set and action name searches should be case sensitive (case sensitive searches are faster) */
    fun getVector3(actionSet: String?, action: String, inputSource: SteamVR_Input_Sources, caseSensitive: Boolean = false): Vec3 =
            getAction<SteamVR_Action_Vector3>(ActionType.Vector3, actionSet, action, caseSensitive)
                    ?.getAxis(inputSource) ?: Vec3()

    /** Get the Vector3 value of an action by the action name and input source. Optionally case sensitive (for faster results)
     *
     *  @param action: The name of the action to get the state of
     *  @param inputSource: The input source to get the action state from
     *  @param caseSensitive: Whether or not the action set and action name searches should be case sensitive (case sensitive searches are faster) */
    fun getVector3(action: String, inputSource: SteamVR_Input_Sources, caseSensitive: Boolean = false): Vec3 =
            getVector3(null, action, inputSource, caseSensitive)

    /** @Returns all of the actions of the specified type. If we're in the editor, doesn't rely on the arrays being filled.
     *
     *  @param T: The type of actions you want to get */
    fun <T : SteamVR_Action> getActions(clazz: Class<T>): Array<T>? = when (clazz) {
        SteamVR_Action::class.java -> actions
        ISteamVR_Action_In::class.java -> actionsIn
        ISteamVR_Action_Out::class.java -> actionsOut
        SteamVR_Action_Boolean::class.java -> actionsBoolean
        SteamVR_Action_Single::class.java -> actionsSingle
        SteamVR_Action_Vector2::class.java -> actionsVector2
        SteamVR_Action_Vector3::class.java -> actionsVector3
        SteamVR_Action_Pose::class.java -> actionsPose
        SteamVR_Action_Skeleton::class.java -> actionsSkeleton
        SteamVR_Action_Vibration::class.java -> actionsVibration
        else -> null.also { println("[SteamVR] Wrong type.") }
    } as Array<T>

    internal val shouldMakeCopy: Boolean
        get() = !SteamVR_Behaviour.isPlaying

    /** Gets the localized name of the device that the action corresponds to.
     *
     *  @param inputSource
     *  @param localizedParts
     *      VRInputString.Hand - Which hand the origin is in. E.g. "Left Hand"
     *      VRInputString.ControllerType - What kind of controller the user has in that hand.E.g. "Vive Controller"
     *      VRInputString.InputSource - What part of that controller is the origin. E.g. "Trackpad"
     *      VRInputString.All - All of the above. E.g. "Left Hand Vive Controller Trackpad" */
    fun getLocalizedName(originHandle: VRInputValueHandle, localizedParts: Array<VRInputString>): String {
        var localizedPartsMask = 0

        for (partIndex in 0 until localizedParts.size)
            localizedPartsMask = localizedPartsMask or localizedParts[partIndex].i

        return vrInput.getOriginLocalizedName(originHandle, localizedPartsMask)
    }


    /** Tell SteamVR that we're using the actions file at the path defined in SteamVR_Settings. */
    fun identifyActionsFile(showLogs: Boolean = true) {
        val path = SteamVR_Settings.actionsFilePath
        if (path != null) {
//            if (OpenVR.Input == null)
//            {
//                Debug.LogError("<b>[SteamVR]</b> Could not instantiate OpenVR Input interface.");
//                return;
//            }
            val err = vrInput.setActionManifestPath(path)
            if (err != vrInput.Error.None)
                System.err.println("[SteamVR] Error loading action manifest into SteamVR: $err")
            else
                if (SteamVR_Input.actions != null) {
                    val numActions = SteamVR_Input.actionsSingle

                    if (showLogs)
                        println("[SteamVR] Successfully loaded $numActions actions from action manifest into SteamVR ($path)")
                } else {
                    if (showLogs)
                        System.err.println("[SteamVR] No actions found, but the action manifest was loaded. This usually means you haven't generated actions. Window -> SteamVR SteamVR_Input -> Save and Generate.")
                }
        } else {
            if (showLogs)
                System.err.println("[SteamVR] Could not find actions file at: $path")
        }
    }

    /** Does the actions file in memory differ from the one on disk as determined by a md5 hash */
    fun hasFileInMemoryBeenModified(): Boolean {

        val jsonText = actionsFilePath?.readText() ?: return true

        val newHashFromFile = SteamVR_Utils.getBadMD5Hash(jsonText)

        val newJSON = Klaxon().toJsonString(actionFile)

        val newHashFromMemory = SteamVR_Utils.getBadMD5Hash(newJSON)

        return newHashFromFile != newHashFromMemory
    }

    fun createEmptyActionsFile(completelyEmpty: Boolean = false): Boolean {
        val projectPath = Application.dataPath
        TODO()
//        int lastIndex = projectPath . LastIndexOf ("/")
//        projectPath = projectPath.Remove(lastIndex, projectPath.Length - lastIndex)
//        actionsFilePath = Path.Combine(projectPath, SteamVR_Settings.instance.actionsFilePath)
//
//        if (File.Exists(actionsFilePath)) {
//            Debug.LogErrorFormat("<b>[SteamVR]</b> Actions file already exists in project root: {0}", actionsFilePath)
//            return false
//        }
//
//        actionFile = new SteamVR_Input_ActionFile ()
//
//        if (completelyEmpty == false) {
//            actionFile.action_sets.Add(SteamVR_Input_ActionFile_ActionSet.CreateNew())
//            actionFile.actions.Add(SteamVR_Input_ActionFile_Action.CreateNew(actionFile.action_sets[0].shortName,
//                    SteamVR_ActionDirections.In, SteamVR_Input_ActionFile_ActionTypes.boolean))
//        }
//
//        string newJSON = JsonConvert . SerializeObject (actionFile, Formatting.Indented, new JsonSerializerSettings { NullValueHandling = NullValueHandling.Ignore })
//
//        File.WriteAllText(actionsFilePath, newJSON)
//
//        actionFile.InitializeHelperLists()
//        fileInitialized = true
//        return true
    }

    fun doesActionsFileExist(): Boolean = SteamVR_Settings.actionsFilePath != null

    /** Load from disk and deserialize the actions file
     *
     *  @param force: Force a refresh of this file from disk */
    fun initializeFile(force: Boolean = false, showErrors: Boolean = true): Boolean {

        val jsonText = SteamVR_Settings.actionsFilePath?.readText() ?: run {
            if (showErrors)
                System.err.println("[SteamVR] Actions file does not exist in project root: ${SteamVR_Settings.actionsFilePath}")
            return false
        }

        if (fileInitialized || (fileInitialized && !force)) {

            val newHash = SteamVR_Utils.getBadMD5Hash(jsonText)

            if (newHash == actionFileHash)
                return true

            actionFileHash = newHash
        }

        actionFile = Klaxon().parse<SteamVR_Input_ActionFile>(jsonText)!!
        actionFile.initializeHelperLists()
        fileInitialized = true
        return true
    }

    /** Deletes the action manifest file and all the default bindings it had listed in the default bindings section
     *
     *  @returns True if we deleted an action file, false if not. */
    fun deleteManifestAndBindings(): Boolean {

        if (!doesActionsFileExist())
            return false

        initializeFile()

        val filesToDelete = actionFile.getFilesToCopy()
        TODO()
//        foreach(string bindingFilePath in filesToDelete)
//        {
//            FileInfo bindingFileInfo = new FileInfo(bindingFilePath)
//            bindingFileInfo.IsReadOnly = false
//            File.Delete(bindingFilePath)
//        }
//
//        if (File.Exists(actionsFilePath)) {
//            FileInfo actionFileInfo = new FileInfo(actionsFilePath)
//            actionFileInfo.IsReadOnly = false
//            File.Delete(actionsFilePath)
//
//            actionFile = null
//            fileInitialized = false
//
//            return true
//        }

        return false
    }

//    #if UNITY_EDITOR
//    public static string GetResourcesFolderPath(bool fromAssetsDirectory = false)
//    {
//        string inputFolder = string . Format ("Assets/{0}", SteamVR_Settings.instance.steamVRInputPath)
//
//        string path = Path . Combine (inputFolder, "Resources")
//
//        bool createdDirectory = false
//        if (Directory.Exists(inputFolder) == false) {
//            Directory.CreateDirectory(inputFolder)
//            createdDirectory = true
//        }
//
//
//        if (Directory.Exists(path) == false) {
//            Directory.CreateDirectory(path)
//            createdDirectory = true
//        }
//
//        if (createdDirectory)
//            UnityEditor.AssetDatabase.Refresh()
//
//        if (fromAssetsDirectory == false)
//            return path.Replace("Assets/", "")
//        else
//            return path
//    }
//
//
//
//    private static bool checkingSetup = false
//    private static bool openingSetup = false
//    public static bool IsOpeningSetup()
//    { return openingSetup; }
//    private static void CheckSetup()
//    {
//        if (checkingSetup == false && openingSetup == false && (SteamVR_Input.actions == null || SteamVR_Input.actions.Length == 0)) {
//            checkingSetup = true
//            Debug.Break()
//
//            bool open = UnityEditor . EditorUtility . DisplayDialog ("[SteamVR]", "It looks like you haven't generated actions for SteamVR Input yet. Would you like to open the SteamVR Input window?", "Yes", "No")
//            if (open) {
//                openingSetup = true
//                UnityEditor.EditorApplication.isPlaying = false
//                Type editorWindowType = FindType ("Valve.VR.SteamVR_Input_EditorWindow")
//                if (editorWindowType != null) {
//                    var window = UnityEditor.EditorWindow.GetWindow(editorWindowType, false, "SteamVR SteamVR_Input", true)
//                    if (window != null)
//                        window.Show()
//                }
//            } else {
//                Debug.LogError("<b>[SteamVR]</b> This version of SteamVR will not work if you do not newCreateType and generate actions. Please open the SteamVR SteamVR_Input window or downgrade to version 1.2.3 (on github)")
//            }
//            checkingSetup = false
//        }
//    }
//
//    private static Type FindType(string typeName)
//    {
//        var type = Type.GetType(typeName)
//        if (type != null) return type
//        foreach(var a in AppDomain . CurrentDomain . GetAssemblies ())
//        {
//            type = a.GetType(typeName)
//            if (type != null)
//                return type
//        }
//        return null
//    }
//    #endif
}