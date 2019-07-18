//======= Copyright (c) Valve Corporation, All rights reserved. ===============
// Action implementation overview:
// Actions are split into three parts:
//     * Action: The user-accessible class that is the interface for accessing action data.
//          There may be many Action instances per Actual SteamVR Action, but these instances are just interfaces to the data and should have virtually no overhead.
//     * Action Map: This is basically a wrapper for a list of Action_Source instances.
//          The idea being there is one Map per Actual SteamVR Action.
//          These maps can be retrieved from a static store in SteamVR_Input so we're not duplicating data.
//     * Action Source: This is a collection of cached data retrieved by calls to the underlying SteamVR Input system.
//          Each Action Source has an inputSource that it is associated with.

package openvr.steamVR.input

import openvr.lib.VRActionHandle
import openvr.lib.vrInput
import openvr.plugin2.SteamVR_Input_Source
import openvr.plugin2.SteamVR_Input_Sources

/** This is the base level action for SteamVR Input Actions. All SteamVR_Action_In and SteamVR_Action_Out inherit from this.
 *  Initializes the ulong handle for the action, has some helper references that all actions will have. */
abstract class SteamVR_ActionT<SourceMap, SourceElement> : SteamVR_Action(), ISteamVR_Action
        where SourceMap : SteamVR_Action_Source_MapT<SourceElement>, SourceElement : SteamVR_Action_Source {

    /** The map to the source elements, a dictionary of source elements. Should be accessed through the action indexer */
    override var sourceMap: SourceMap? = null

    /** Access this action restricted to individual input sources.
     *  @param inputSource: The input source to access for this action */
    operator fun get(inputSource: SteamVR_Input_Sources): SourceElement? = sourceMap!![inputSource]

    override val fullPath: String
        get() = sourceMap!!.fullPath

    /** The underlying handle for this action used for native SteamVR Input calls */
    override val handle: VRActionHandle
        get() = sourceMap!!.handle

    /** The actionset this action is contained within */
    override val actionSet: SteamVR_ActionSet?
        get() = sourceMap!!.actionSet

    /** The action direction of this action (in for input - most actions, out for output - mainly haptics) */
    override val direction: SteamVR_ActionDirections
        get() = sourceMap!!.direction

    /** [Shortcut to: SteamVR_Input_Sources.Any] @Returns true if the action is bound and the actionset is active */
    override val active: Boolean
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.active

    /** [Shortcut to: SteamVR_Input_Sources.Any] @Returns true if the action was bound and the ActionSet was active during the previous update */
    override val lastActive: Boolean
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastActive

    /** [Shortcut to: SteamVR_Input_Sources.Any] @Returns true if the action is bound */
    override val activeBinding: Boolean
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.activeBinding

    /** [Shortcut to: SteamVR_Input_Sources.Any] @Returns true if the action was bound at the previous update */
    override val lastActiveBinding: Boolean
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastActiveBinding


    protected var initialized = false

    /** Prepares the action to be initialized. Creating dictionaries, finding the right existing action, etc. */
    override fun preInitialize(newActionPath: String) {
        actionPath = newActionPath

        sourceMap = newSourceMap()
        sourceMap!!.preInitialize(this, actionPath!!)

        initialized = true
    }

    override fun createUninitialized(newActionPath: String, caseSensitive: Boolean) {
        actionPath = newActionPath

        sourceMap = newSourceMap()
        sourceMap!!.preInitialize(this, actionPath!!)

        needsReinit = true
        initialized = false
    }

    override fun createUninitialized(newActionSet: String, direction: SteamVR_ActionDirections,
                                     newAction: String, caseSensitive: Boolean) {

        actionPath = SteamVR_Input_ActionFile_Action.createNewName(newActionSet, direction, newAction)

        sourceMap = newSourceMap()
        sourceMap!!.preInitialize(this, actionPath!!)

        needsReinit = true
        initialized = false
    }

    /** [Should not be called by user code] If it looks like we aren't attached to a real action then try and find the existing action for our given path. */
    override fun tryNeedsInitData(): String? {
        if (needsReinit && actionPath != null) {
            val existingAction = findExistingActionForPartialPath(actionPath)

            if (existingAction == null)
                sourceMap = null
            else {
                actionPath = existingAction.fullPath
                sourceMap = existingAction.sourceMap as SourceMap

                initialized = true
                needsReinit = false
                return actionPath
            }
        }

        return null
    }


    /** [Should not be called by user code] Initializes the individual sources as well as the base map itself.
     *  Gets the handle for the action from SteamVR and does any other SteamVR related setup that needs to be done */
    fun initialize(createNew: Boolean = false) = initialize(createNew, true)

    /** [Should not be called by user code] Initializes the individual sources as well as the base map itself.
     *  Gets the handle for the action from SteamVR and does any other SteamVR related setup that needs to be done */
    override fun initialize(createNew: Boolean, throwNotSetError: Boolean) {
        if (needsReinit)
            tryNeedsInitData()

        if (createNew)
            sourceMap!!.initialize()
        else {
            sourceMap = SteamVR_Input.getActionDataFromPath(actionPath)

            if (sourceMap == null) {
//                #if UNITY_EDITOR
//                if (throwNotSetError) {
//                    if (string.IsNullOrEmpty(actionPath)) {
//                        Debug.LogError("<b>[SteamVR]</b> Action has not been assigned.")
//                    } else {
//                        Debug.LogError("<b>[SteamVR]</b> Could not find action with path: " + actionPath)
//                    }
//                }
//                #endif
            }
        }

        initialized = true
    }

    override fun initializeCopy(newActionPath: String?, newData: SteamVR_Action_Source_Map?) {
        actionPath = newActionPath
        sourceMap = newData as SourceMap
        initialized = true
    }

    protected fun initAfterDeserialize() {
        sourceMap?.let {
            if (it.fullPath != actionPath) {
                needsReinit = true
                tryNeedsInitData()
            }

            if (actionPath.isNullOrEmpty())
                sourceMap = null
        }

        if (!initialized)
            initialize(false, false)
    }


    /** Gets a value indicating whether or not the action is currently bound and if the containing action set is active
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    override fun getActive(inputSource: SteamVR_Input_Sources): Boolean =
            sourceMap!![inputSource]!!.active

    /** Gets a value indicating whether or not the action is currently bound
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    override fun getActiveBinding(inputSource: SteamVR_Input_Sources): Boolean =
            sourceMap!![inputSource]!!.activeBinding


    /** Gets the value from the previous update indicating whether or not the action was currently bound and if the containing action set was active
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    override fun getLastActive(inputSource: SteamVR_Input_Sources): Boolean =
            sourceMap!![inputSource]!!.lastActive

    /** Gets the value from the previous update indicating whether or not the action is currently bound
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    override fun getLastActiveBinding(inputSource: SteamVR_Input_Sources): Boolean =
            sourceMap!![inputSource]!!.lastActiveBinding
}


abstract class SteamVR_Action : ISteamVR_Action {

    protected var actionPath: String? = null

    protected var needsReinit: Boolean = false

    /** [Should not be called by user code] Gets a copy of the underlying source map so we're always using the same underlying event data */
    fun <CreateType : SteamVR_Action> getCopy(): CreateType =
            newCreateType().apply {
                initializeCopy(actionPath, sourceMap)
                //return (CreateType)this; //no need to make copies in builds - will reduce memory alloc //todo: having this enabled was not working. all sets were the same (maybe actions too)
            } as CreateType

    abstract fun tryNeedsInitData(): String?

    protected abstract fun initializeCopy(newActionPath: String?, newData: SteamVR_Action_Source_Map?)

    /** [Shortcut to: SteamVR_Input_Sources.Any] Returns true if the action set that contains this action is active for Any input source. */
    fun setActive(): Boolean = actionSet!!.isActive(SteamVR_Input_Sources.Any)

    /** Prepares the action to be initialized. Creating dictionaries, finding the right existing action, etc. */
    abstract fun preInitialize(newActionPath: String)

    protected abstract fun createUninitialized(newActionPath: String, caseSensitive: Boolean)

    protected abstract fun createUninitialized(newActionSet: String, direction: SteamVR_ActionDirections, newAction: String, caseSensitive: Boolean)

    /** Initializes the individual sources as well as the base map itself. Gets the handle for the action from SteamVR and does any other SteamVR related setup that needs to be done */
    abstract fun initialize(createNew: Boolean = false, throwNotSetError: Boolean = true)

    /** Gets the last timestamp this action was changed. (by Time.realtimeSinceStartup)
     *  @param inputSource: The input source to use to select the last changed time */
    abstract fun getTimeLastChanged(inputSource: SteamVR_Input_Sources): Float

    abstract val sourceMap: SteamVR_Action_Source_Map?

    /** Gets a value indicating whether or not the action is currently bound and if the containing action set is active
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    abstract override infix fun getActive(inputSource: SteamVR_Input_Sources): Boolean

    /** Gets a value indicating whether or not the containing action set is active
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    infix fun getSetActive(inputSource: SteamVR_Input_Sources): Boolean = actionSet!!.isActive(inputSource)

    /** Gets a value indicating whether or not the action is currently bound
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    abstract fun getActiveBinding(inputSource: SteamVR_Input_Sources): Boolean

    /** Gets the value from the previous update indicating whether or not the action is currently bound and if the containing action set is active
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    abstract fun getLastActive(inputSource: SteamVR_Input_Sources): Boolean

    /** Gets the value from the previous update indicating whether or not the action is currently bound
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    abstract fun getLastActiveBinding(inputSource: SteamVR_Input_Sources): Boolean

    /** @returns the full action path for this action. */
    val path: String
        get() = actionPath!!

    /** @returns true if the data for this action is being updated for the specified input source. This can be triggered by querying the data */
    abstract fun isUpdating(inputSource: SteamVR_Input_Sources): Boolean

    override fun hashCode(): Int = actionPath?.hashCode() ?: 0

    /** Compares two SteamVR_Actions by their action path instead of references */
    override fun equals(other: Any?): Boolean = when {
        other == null -> when {
            actionPath.isNullOrEmpty() -> true //if we haven't set a path, say this action is equal to null
            sourceMap == null -> true
            else -> false
        }
        other === this -> true

        other is SteamVR_Action -> actionPath == other.actionPath

        else -> false
    }

    companion object {

        /** Not recommended. Determines if we should do a lazy-loading style of updating actions where we don't check for their data
         *  until the code asks for it. Note: You will have to manually activate actions otherwise. Not recommended. */
        var startUpdatingSourceOnAccess = true

        /** [Should not be called by user code]</strong> Creates an actual action that will later be called by user code. */
        fun <CreateType : SteamVR_Action> create(type: ActionType, newActionPath: String): CreateType =
                type.create<CreateType>().apply { preInitialize(newActionPath) }

        /** [Should not be called by user code] Creates an uninitialized action that can be saved without being attached to a real action */
        fun <CreateType : SteamVR_Action> createUninitialized(type: ActionType, setName: String,
                                                              direction: SteamVR_ActionDirections,
                                                              newActionName: String,
                                                              caseSensitive: Boolean): CreateType =
                type.create<CreateType>().apply { createUninitialized(setName, direction, newActionName, caseSensitive) }

        /** [Should not be called by user code] Creates an uninitialized action that can be saved without being attached to a real action */
        fun <CreateType : SteamVR_Action> createUninitialized(type: ActionType, actionPath: String,
                                                              caseSensitive: Boolean): CreateType =
                type.create<CreateType>().apply { createUninitialized(actionPath, caseSensitive) }

        /** Tries to find an existing action matching some subsection of an action path. More useful functions in SteamVR_Input. */
        fun findExistingActionForPartialPath(path: String?): SteamVR_Action? {

            if (path.isNullOrEmpty() || path.indexOf('/') == -1)
                return null

            //   0    1       2     3   4
            //    /actions/default/in/foobar
            val pathParts = path.split('/')
            val existingAction = when {
                pathParts.size >= 2 && pathParts[2].isEmpty() -> {
                    val set = pathParts[2]
                    val name = pathParts[4]
                    SteamVR_Input.getBaseAction(set, name)
                }
                else -> SteamVR_Input.getBaseActionFromPath(path)
            }

            return existingAction
        }
    }

    private var cachedShortName: String? = null

    /** Gets just the name of this action. The last part of the path for this action. Removes action set, and direction. */
    override val shortName: String
        get() {
            if (cachedShortName == null) {
                cachedShortName = SteamVR_Input_ActionFile.getShortName(fullPath)
            }

            return cachedShortName!!
        }
}

abstract class SteamVR_Action_Source_MapT<SourceElement : SteamVR_Action_Source> : SteamVR_Action_Source_Map() {

    /** Gets a reference to the action restricted to a certain input source. LeftHand or RightHand for example.
     *  @param inputSource: The device you would like data from */
    operator fun get(inputSource: SteamVR_Input_Sources): SourceElement? = getSourceElementForIndexer(inputSource)

    open fun onAccessSource(inputSource: SteamVR_Input_Sources) {}

    protected val sources = mutableMapOf<SteamVR_Input_Sources, SourceElement>()

    /** [Should not be called by user code] Initializes the individual sources as well as the base map itself.
     *  Gets the handle for the action from SteamVR and does any other SteamVR related setup that needs to be done */
    override fun initialize() {
        super.initialize()
        sources.values.forEach { it.initialize() }
    }

    override fun preinitializeMap(inputSource: SteamVR_Input_Sources, wrappingAction: SteamVR_Action) {
        sources[inputSource] = newSourceElement<SourceElement>().apply { preinitialize(wrappingAction, inputSource) }
    }

    /** Normally I'd just make the indexer virtual and override that but some unity versions don't like that */
    protected open fun getSourceElementForIndexer(inputSource: SteamVR_Input_Sources): SourceElement? {
        onAccessSource(inputSource)
        return sources[inputSource]
    }
}

abstract class SteamVR_Action_Source_Map {

    /** The full string path for this action (from the action manifest) */
    lateinit var fullPath: String
        protected set

    /** The underlying handle for this action used for native SteamVR Input calls. Retrieved on Initialization from SteamVR. */
    var handle: VRActionHandle = 0
        protected set

    /** The ActionSet this action is contained within */
    var actionSet: SteamVR_ActionSet? = null
        protected set

    /** The action direction of this action (in for input - most actions, out for output - haptics) */
    lateinit var direction: SteamVR_ActionDirections
        protected set

    /** The base SteamVR_Action this map corresponds to */
    lateinit var action: SteamVR_Action

    fun preInitialize(wrappingAction: SteamVR_Action, actionPath: String) {

        fullPath = actionPath
        action = wrappingAction

        actionSet = SteamVR_Input.getActionSetFromPath(getActionSetPath())

        direction = getActionDirection()

        SteamVR_Input_Source.allSources.forEach { preinitializeMap(it, wrappingAction) }
    }

    /** [Should not be called by user code] Sets up the internals of the action source before SteamVR has been initialized. */
    protected abstract fun preinitializeMap(inputSource: SteamVR_Input_Sources, wrappingAction: SteamVR_Action)

    /** [Should not be called by user code] Initializes the handle for the action and any other related SteamVR data. */
    open fun initialize() {
        handle = vrInput.getActionHandle(fullPath.toLowerCase())

        if (vrInput.error != vrInput.Error.None)
            System.err.println("[SteamVR] GetActionHandle (${fullPath.toLowerCase()}) error: ${vrInput.error}")
    }

    private fun getActionSetPath(): String {

        val actionsEndIndex = fullPath.indexOf('/', 1)
        val setStartIndex = actionsEndIndex + 1
        val setEndIndex = fullPath.indexOf('/', setStartIndex)

        return fullPath.substring(0, setEndIndex) // on c# 2nd is `size`, but since we start from 0, it's the same also on jvm
    }

    private fun getActionDirection(): SteamVR_ActionDirections {

        val actionsEndIndex = fullPath.indexOf('/', 1)
        val setStartIndex = actionsEndIndex + 1
        val setEndIndex = fullPath.indexOf('/', setStartIndex)
        val directionEndIndex = fullPath.indexOf('/', setEndIndex + 1)
        val direction = fullPath.substring(setEndIndex + 1, directionEndIndex)

        return when (direction) {
            "in" -> SteamVR_ActionDirections.In
            "out" -> SteamVR_ActionDirections.Out
            else -> SteamVR_ActionDirections.In.also {
                System.err.println("Could not find match for direction: $direction")
            }
        }
    }
}

abstract class SteamVR_Action_Source : ISteamVR_Action_Source {

    /** The full string path for this action (from the action manifest) */
    override val fullPath: String
        get() = action.fullPath

    /** The underlying handle for this action used for native SteamVR Input calls. Retrieved on Initialization from SteamVR. */
    override val handle: VRActionHandle
        get() = action.handle

    /** The ActionSet this action is contained within */
    override val actionSet: SteamVR_ActionSet?
        get() = action.actionSet

    /** The action direction of this action (in for input - most actions, out for output - haptics) */
    override val direction: SteamVR_ActionDirections
        get() = action.direction

    /** The input source that this instance corresponds to. ex. LeftHand, RightHand */
    lateinit var inputSource: SteamVR_Input_Sources
        protected set

    /** @Returns true if the action set this is contained in is active for this input source (or Any) */
    val setActive: Boolean
        get() = actionSet!!.isActive(inputSource)


    /** @Returns true if this action is bound and the ActionSet is active */
    abstract override val active: Boolean

    /** Returns true if the action is bound */
    abstract override val activeBinding: Boolean

    /** @Returns true if the action was bound and the ActionSet was active during the previous update */
    abstract override var lastActive: Boolean
        protected set

    /** @Returns true if the action was bound during the previous update */
    abstract override val lastActiveBinding: Boolean


    protected var inputSourceHandle: VRActionHandle = 0

    lateinit var action: SteamVR_Action

    /** [Should not be called by user code] Sets up the internals of the action source before SteamVR has been initialized. */
    open fun preinitialize(wrappingAction: SteamVR_Action, forInputSource: SteamVR_Input_Sources) {
        action = wrappingAction
        inputSource = forInputSource
    }

    /** [Should not be called by user code]
     *  Initializes the handle for the inputSource, and any other related SteamVR data. */
    open fun initialize() {
        inputSourceHandle = SteamVR_Input_Source.getHandle(inputSource)
    }
}


interface ISteamVR_Action : ISteamVR_Action_Source {

    /** @return the active state of the action for the specified Input Source
     *  @param inputSource: The input source to check */
    fun getActive(inputSource: SteamVR_Input_Sources): Boolean

    /** @return the name of the action without the action set or direction */
    val shortName: String
}


interface ISteamVR_Action_Source {

    /** @return true if this action is bound and the ActionSet is active */
    val active: Boolean

    /** @return true if the action is bound */
    val activeBinding: Boolean

    /** @return true if the action was bound and the ActionSet was active during the previous update */
    val lastActive: Boolean

    /** @return true if the action was bound last update */
    val lastActiveBinding: Boolean

    /** @return The full string path for this action (from the action manifest) */
    val fullPath: String

    /** @return The underlying handle for this action used for native SteamVR Input calls. Retrieved on Initialization from SteamVR. */
    val handle: VRActionHandle

    /** @return The ActionSet this action is contained within */
    val actionSet: SteamVR_ActionSet?

    /** @return The action direction of this action (in for input, out for output) */
    val direction: SteamVR_ActionDirections
}