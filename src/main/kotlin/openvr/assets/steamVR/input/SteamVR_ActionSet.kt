package openvr.assets.steamVR.input

import openvr.lib.VRActionHandle
import openvr.lib.actionSet
import openvr.lib.vrInput
import openvr.plugin2.SteamVR_Input_Source
import openvr.plugin2.SteamVR_Input_Sources
import openvr.steamVR_Input.actionSetClasses.SteamVR_Input_ActionSet_buggy
import openvr.steamVR_Input.actionSetClasses.SteamVR_Input_ActionSet_default
import openvr.steamVR_Input.actionSetClasses.SteamVR_Input_ActionSet_mixedreality
import openvr.steamVR_Input.actionSetClasses.SteamVR_Input_ActionSet_platformer
import openvr.unity.Time
import org.lwjgl.openvr.VRActiveActionSet

//======= Copyright (c) Valve Corporation, All rights reserved. ===============


/** Action sets are logical groupings of actions. Multiple sets can be active at one time. */
abstract class SteamVR_ActionSet : ISteamVR_ActionSet/*, ISerializationCallbackReceiver*/ {

    private var actionSetPath: String? = null

    var setData: SteamVR_ActionSet_Data? = null
        protected set

    /** All actions within this set (including out actions) */
    override val allActions: Array<SteamVR_Action>
        get() {
            if (!initialized) initialize()
            return setData!!.allActions
        }

    /** All IN actions within this set that are NOT pose or skeleton actions */
    override val nonVisualInActions: Array<ISteamVR_Action_In>
        get() {
            if (!initialized) initialize()
            return setData!!.nonVisualInActions
        }

    /** All pose and skeleton actions within this set */
    override val visualActions: Array<ISteamVR_Action_In>
        get() {
            if (!initialized) initialize()
            return setData!!.visualActions
        }

    /** All pose actions within this set */
    override val poseActions: Array<SteamVR_Action_Pose>
        get() {
            if (!initialized) initialize()
            return setData!!.poseActions
        }

    /** All skeleton actions within this set */
    override val skeletonActions: Array<SteamVR_Action_Skeleton>
        get() {
            if (!initialized) initialize()
            return setData!!.skeletonActions
        }

    /** All out actions within this set */
    override val outActionArray: Array<ISteamVR_Action_Out>
        get() {
            if (!initialized) initialize()
            return setData!!.outActionArray
        }


    /** <summary>The full path to this action set (ex: /actions/in/default)</summary> */
    override val fullPath: String?
        get() {
            if (!initialized) initialize()
            return setData!!.fullPath
        }

    override val usage: String
        get() {
            if (!initialized)
                initialize()
            return setData!!.usage
        }

    override val handle: VRActionHandle
        get() {
            if (!initialized)
                initialize()

            return setData!!.handle
        }

    protected var initialized = false

    companion object {

        fun <CreateType : SteamVR_ActionSet> createType(clazz: Class<CreateType>): CreateType = clazz.getDeclaredConstructor().newInstance()

        fun <CreateType : SteamVR_ActionSet> create(clazz: Class<CreateType>, newSetPath: String?): CreateType =
                createType(clazz).apply { preInitialize(newSetPath) }

        fun <CreateType : SteamVR_ActionSet> createFromName(clazz: Class<CreateType>, newSetName: String): CreateType =
                createType(clazz).apply { preInitialize(SteamVR_Input_ActionFile_ActionSet.getPathFromName(newSetName)) }
    }

    fun preInitialize(newActionPath: String?) {
        actionSetPath = newActionPath

        setData = SteamVR_ActionSet_Data().apply {
            fullPath = actionSetPath
            preInitialize()
        }

        initialized = true
    }

    fun finishPreInitialize() = setData!!.finishPreInitialize()

    /** Initializes the handle for the action */
    fun initialize(createNew: Boolean = false, throwErrors: Boolean = true) {
        if (createNew) {
            setData!!.initialize()
        } else {
            setData = SteamVR_Input.getActionSetDataFromPath(actionSetPath)

            if (setData == null)
//                #if UNITY_EDITOR
                if (throwErrors) {
                    if (actionSetPath.isNullOrEmpty())
                        System.err.println("[SteamVR] Action has not been assigned.")
                    else
                        System.err.println("[SteamVR] Could not find action with path: $actionSetPath")
                }
//                #endif
        }

        initialized = true
    }

    val path: String
        get() = actionSetPath!!

    /** @Returns whether the set is currently active or not. */
    fun isActive(): Boolean = isActive(SteamVR_Input_Sources.Any)

    /** Returns whether the set is currently active or not.
     *
     *  @param source: The device to check. Any means all devices here (not left or right, but all) */
    override fun isActive(source: SteamVR_Input_Sources): Boolean = setData!!.isActive(source)

    /** @Returns the last time this action set was changed (set to active or inactive) */
    fun getTimeLastChanged(): Float = getTimeLastChanged(SteamVR_Input_Sources.Any)

    /** @Returns the last time this action set was changed (set to active or inactive)
     *
     *  @param source: The device to check. Any means all devices here (not left or right, but all) */
    override fun getTimeLastChanged(source: SteamVR_Input_Sources): Float = setData!!.getTimeLastChanged(source)


    /** Activate this set so its actions can be called
     *
     *  @param disableAllOtherActionSets: Disable all other action sets at the same time
     *  @param priority: The priority of this action set. If you have two actions bound to the same input (button)
     *      the higher priority set will override the lower priority. If they are the same priority both will execute.
     *  @param activateForSource: Will activate this action set only for the specified source. Any if you want to activate for everything */
    fun activate(activateForSource: SteamVR_Input_Sources = SteamVR_Input_Sources.Any, priority: Int = 0) =
            activate(activateForSource, priority, false)

    /** Activate this set so its actions can be called
     *
     *  @param disableAllOtherActionSets: Disable all other action sets at the same time
     *  @param priority: The priority of this action set. If you have two actions bound to the same input (button)
     *      the higher priority set will override the lower priority. If they are the same priority both will execute.
     *  @param activateForSource: Will activate this action set only for the specified source. Any if you want to activate for everything */
    override fun activate(activateForSource: SteamVR_Input_Sources, priority: Int, disableAllOtherActionSets: Boolean) =
            setData!!.activate(activateForSource, priority, disableAllOtherActionSets)

    /** Deactivate the action set so its actions can no longer be called */
    fun deactivate() = deactivate(SteamVR_Input_Sources.Any)

    /** Deactivate the action set so its actions can no longer be called */
    override fun deactivate(forSource: SteamVR_Input_Sources) = setData!!.deactivate(forSource)

    /** Gets the last part of the path for this action. Removes "actions" and direction. */
    override val shortName: String
        get() = setData!!.shortName

    override fun readRawSetActive(inputSource: SteamVR_Input_Sources): Boolean = setData!!.readRawSetActive(inputSource)

    override fun readRawSetLastChanged(inputSource: SteamVR_Input_Sources): Float = setData!!.readRawSetLastChanged(inputSource)

    override fun readRawSetPriority(inputSource: SteamVR_Input_Sources): Int = setData!!.readRawSetPriority(inputSource)

    fun <CreateType : SteamVR_ActionSet> createType(): CreateType = when (this) {
        is SteamVR_Input_ActionSet_default -> SteamVR_Input_ActionSet_default()
        is SteamVR_Input_ActionSet_buggy -> SteamVR_Input_ActionSet_buggy()
        is SteamVR_Input_ActionSet_mixedreality -> SteamVR_Input_ActionSet_mixedreality()
        is SteamVR_Input_ActionSet_platformer -> SteamVR_Input_ActionSet_platformer()
        else -> error("Invalid")
    } as CreateType

    fun <CreateType : SteamVR_ActionSet> getCopy(): CreateType = createType<CreateType>().also {
        it.actionSetPath = actionSetPath
        it.setData = setData
        it.initialized = true
        //return (CreateType)this; //no need to make copies in builds - will reduce memory alloc //todo: having this enabled was not working. all sets were the same (maybe actions too)
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            if (actionSetPath.isNullOrEmpty()) //if we haven't set a path, say this action set is equal to null
                return true
            return false
        }

        if (this === other)
            return true

        if (other is SteamVR_ActionSet)
            return actionSetPath == other.actionSetPath

        return false
    }

    override fun hashCode(): Int = actionSetPath?.hashCode() ?: 0

//    void ISerializationCallbackReceiver.OnBeforeSerialize()
//    {
//    }
//
//    void ISerializationCallbackReceiver.OnAfterDeserialize()
//    {
//        if (setData != null) {
//            if (setData.fullPath != actionSetPath) {
//                setData = SteamVR_Input.GetActionSetDataFromPath(actionSetPath)
//            }
//        }
//
//        if (initialized == false)
//            Initialize(false, false)
//    }
}

/** Action sets are logical groupings of actions. Multiple sets can be active at one time. */
class SteamVR_ActionSet_Data : ISteamVR_ActionSet {

    /** All actions within this set (including out actions) */
    override var allActions: Array<SteamVR_Action> = emptyArray()

    /** All IN actions within this set that are NOT pose or skeleton actions */
    override var nonVisualInActions: Array<ISteamVR_Action_In> = emptyArray()

    /** All pose and skeleton actions within this set */
    override var visualActions: Array<ISteamVR_Action_In> = emptyArray()

    /** All pose actions within this set */
    override var poseActions: Array<SteamVR_Action_Pose> = emptyArray()

    /** <summary>All skeleton actions within this set</summary> */
    override var skeletonActions: Array<SteamVR_Action_Skeleton> = emptyArray()

    /** <summary>All out actions within this set</summary> */
    override var outActionArray: Array<ISteamVR_Action_Out> = emptyArray()

    /** The full path to this action set (ex: /actions/in/default) */
    override var fullPath: String? = null

    override lateinit var usage: String

    override var handle: VRActionHandle = 0

    protected val rawSetActive = BooleanArray(SteamVR_Input_Source.numSources)

    protected val rawSetLastChanged = FloatArray(SteamVR_Input_Source.numSources)

    protected val rawSetPriority = IntArray(SteamVR_Input_Source.numSources)

    protected var initialized = false

    fun preInitialize() {}

    fun finishPreInitialize() {
        val allActionsList = ArrayList<SteamVR_Action>()
        val nonVisualInActionsList = ArrayList<ISteamVR_Action_In>()
        val visualActionsList = ArrayList<ISteamVR_Action_In>()
        val poseActionsList = ArrayList<SteamVR_Action_Pose>()
        val skeletonActionsList = ArrayList<SteamVR_Action_Skeleton>()
        val outActionList = ArrayList<ISteamVR_Action_Out>()

//        if (SteamVR_Input::actions == null) {
//            Debug.LogError("<b>[SteamVR SteamVR_Input]</b> Actions not initialized!")
//            return
//        }

        for (action in SteamVR_Input.actions)

            if (action.actionSet!!.setData == this) {
                allActionsList += action
                when (action) {
                    is ISteamVR_Action_Boolean, is ISteamVR_Action_Single, is ISteamVR_Action_Vector2, is ISteamVR_Action_Vector3 -> nonVisualInActionsList += action as ISteamVR_Action_In
                    is SteamVR_Action_Pose -> {
                        visualActionsList += action as ISteamVR_Action_In
                        poseActionsList += action
                    }
                    is SteamVR_Action_Skeleton -> {
                        visualActionsList += action as ISteamVR_Action_In
                        skeletonActionsList += action
                    }
                    is ISteamVR_Action_Out -> outActionList += action
                    else -> System.err.println("[SteamVR SteamVR_Input] Action doesn't implement known interface: ${action.fullPath}")
                }
            }

        allActions = allActionsList.toArray() as Array<SteamVR_Action>
        nonVisualInActions = nonVisualInActionsList.toArray() as Array<ISteamVR_Action_In>
        visualActions = visualActionsList.toArray() as Array<ISteamVR_Action_In>
        poseActions = poseActionsList.toArray() as Array<SteamVR_Action_Pose>
        skeletonActions = skeletonActionsList.toArray() as Array<SteamVR_Action_Skeleton>
        outActionArray = outActionList.toArray() as Array<ISteamVR_Action_Out>
    }

    fun initialize() {
        handle = vrInput.getActionSetHandle(fullPath!!.toLowerCase())

        if (vrInput.error != vrInput.Error.None)
            System.err.println("[SteamVR] GetActionSetHandle ($fullPath) error: ${vrInput.error}")

        initialized = true
    }

    /** @Returns whether the set is currently active or not. */
    fun isActive(): Boolean = isActive(SteamVR_Input_Sources.Any)

    /** @Returns whether the set is currently active or not.
     *
     *  @param source: The device to check. Any means all devices here (not left or right, but all) */
    override fun isActive(source: SteamVR_Input_Sources): Boolean = when {
        initialized -> rawSetActive[source.ordinal] || rawSetActive[0]
        else -> false
    }

    /** @Returns the last time this action set was changed (set to active or inactive) */
    fun getTimeLastChanged(): Float = getTimeLastChanged(SteamVR_Input_Sources.Any)

    /** @Returns the last time this action set was changed (set to active or inactive)
     *
     *  @param source: The device to check. Any means all devices here (not left or right, but all) */
    override fun getTimeLastChanged(source: SteamVR_Input_Sources): Float = when {
        initialized -> rawSetLastChanged[source.ordinal]
        else -> 0f
    }

    /** Activate this set so its actions can be called
     *
     *  @param disableAllOtherActionSets: Disable all other action sets at the same time
     *  @param priority: The priority of this action set. If you have two actions bound to the same input (button)
     *      the higher priority set will override the lower priority. If they are the same priority both will execute.
     *  @param activateForSource: Will activate this action set only for the specified source.
     *      Any if you want to activate for everything */
    fun activate(activateForSource: SteamVR_Input_Sources = SteamVR_Input_Sources.Any, priority: Int = 0) =
            activate(activateForSource, priority, false)

    /** Activate this set so its actions can be called
     *
     *  @param disableAllOtherActionSets: Disable all other action sets at the same time
     *  @param priority: The priority of this action set. If you have two actions bound to the same input (button)
     *      the higher priority set will override the lower priority. If they are the same priority both will execute.
     *  @param activateForSource: Will activate this action set only for the specified source.
     *      Any if you want to activate for everything */
    override fun activate(activateForSource: SteamVR_Input_Sources, priority: Int, disableAllOtherActionSets: Boolean) {

        val sourceIndex = activateForSource.ordinal

        if (disableAllOtherActionSets)
            SteamVR_ActionSet_Manager.disableAllActionSets()

        if (!rawSetActive[sourceIndex]) {
            rawSetActive[sourceIndex] = true
            SteamVR_ActionSet_Manager.setChanged()

            rawSetLastChanged[sourceIndex] = Time.realtimeSinceStartup
        }

        if (rawSetPriority[sourceIndex] != priority) {
            rawSetPriority[sourceIndex] = priority
            SteamVR_ActionSet_Manager.setChanged()

            rawSetLastChanged[sourceIndex] = Time.realtimeSinceStartup
        }
    }

    /** Deactivate the action set so its actions can no longer be called */
    fun deactivate() = deactivate(SteamVR_Input_Sources.Any)

    /** Deactivate the action set so its actions can no longer be called */
    override fun deactivate(forSource: SteamVR_Input_Sources) {

        val sourceIndex = forSource.ordinal

        if (rawSetActive[sourceIndex]) {
            rawSetLastChanged[sourceIndex] = Time.realtimeSinceStartup
            SteamVR_ActionSet_Manager.setChanged()
        }

        rawSetActive[sourceIndex] = false
        rawSetPriority[sourceIndex] = 0
    }

    private var cachedShortName: String? = null

    override val shortName: String
        get() {
            if (cachedShortName == null)
                cachedShortName = SteamVR_Input_ActionFile.getShortName(fullPath!!)

            return cachedShortName!!
        }

    var emptySetCache: VRActiveActionSet.Buffer = VRActiveActionSet.calloc(0)
    var setCache: VRActiveActionSet.Buffer = VRActiveActionSet.calloc(1)

    /** Shows all the bindings for the actions in this set.
     *
     *  @param originToHighlight: Highlights the binding of the passed in action (or the first action in the set if none is specified) */
    fun showBindingHints(originToHighlight_: ISteamVR_Action_In? = null): Boolean {
        var originToHighlight = originToHighlight_
        if (originToHighlight == null)
            for (action in allActions) {
                if (action.direction == SteamVR_ActionDirections.In && action.active) {
                    originToHighlight = action as ISteamVR_Action_In
                    break
                }
            }


        if (originToHighlight != null) {
            setCache[0].actionSet = handle
            vrInput.showBindingsForActionSet(setCache, originToHighlight.activeOrigin)
            return true
        }

        return false
    }

    fun hideBindingHints() = vrInput.showBindingsForActionSet(emptySetCache, 0)

    override fun readRawSetActive(inputSource: SteamVR_Input_Sources): Boolean = rawSetActive[inputSource.ordinal]

    override fun readRawSetLastChanged(inputSource: SteamVR_Input_Sources): Float = rawSetLastChanged[inputSource.ordinal]

    override fun readRawSetPriority(inputSource: SteamVR_Input_Sources): Int = rawSetPriority[inputSource.ordinal]
}

/** Action sets are logical groupings of actions. Multiple sets can be active at one time. */
interface ISteamVR_ActionSet {

    /** All actions within this set (including out actions) */
    val allActions: Array<SteamVR_Action>

    /** All IN actions within this set that are NOT pose or skeleton actions */
    val nonVisualInActions: Array<ISteamVR_Action_In>

    /** All pose and skeleton actions within this set */
    val visualActions: Array<ISteamVR_Action_In>

    /** All pose actions within this set */
    val poseActions: Array<SteamVR_Action_Pose>

    /** All skeleton actions within this set */
    val skeletonActions: Array<SteamVR_Action_Skeleton>

    /** All out actions within this set */
    val outActionArray: Array<ISteamVR_Action_Out>


    /** The full path to this action set (ex: /actions/in/default) */
    val fullPath: String?

    /** How the binding UI should display this set */
    val usage: String

    val handle: VRActionHandle

    infix fun readRawSetActive(inputSource: SteamVR_Input_Sources): Boolean
    infix fun readRawSetLastChanged(inputSource: SteamVR_Input_Sources): Float
    infix fun readRawSetPriority(inputSource: SteamVR_Input_Sources): Int


    /** @Returns whether the set is currently active or not.
     *  @param source: The device to check. Any means all devices here (not left or right, but all) */
    fun isActive(source: SteamVR_Input_Sources = SteamVR_Input_Sources.Any): Boolean

    /** @Returns the last time this action set was changed (set to active or inactive)
     *  @param source: The device to check. Any means all devices here (not left or right, but all) */
    fun getTimeLastChanged(source: SteamVR_Input_Sources = SteamVR_Input_Sources.Any): Float

    /** Activate this set so its actions can be called
     *  @param disableAllOtherActionSets: Disable all other action sets at the same time
     *  @param priority: The priority of this action set. If you have two actions bound to the same input (button)
     *      the higher priority set will override the lower priority. If they are the same priority both will execute.
     *  @param activateForSource: Will activate this action set only for the specified source.
     *      Any if you want to activate for everything  */
    fun activate(activateForSource: SteamVR_Input_Sources = SteamVR_Input_Sources.Any, priority: Int = 0, disableAllOtherActionSets: Boolean = false)

    /** Deactivate the action set so its actions can no longer be called */
    fun deactivate(forSource: SteamVR_Input_Sources = SteamVR_Input_Sources.Any)

    /** Gets the last part of the path for this action. Removes "actions" and direction. */
    val shortName: String
}