package openvr.steamVR.input

import openvr.lib.*
import openvr.plugin2.SteamVR_Input_Source
import openvr.plugin2.SteamVR_Input_Sources
import openvr.unity.Time
import org.lwjgl.openvr.InputOriginInfo

//======= Copyright (c) Valve Corporation, All rights reserved. ===============

/** In actions are all input type actions. Boolean, Single, Vector2, Vector3, Skeleton, and Pose. */
abstract class SteamVR_Action_In<SourceMap, SourceElement> : SteamVR_ActionT<SourceMap, SourceElement>(), ISteamVR_Action_In
        where SourceMap : SteamVR_Action_In_Source_Map<SourceElement>, SourceElement : SteamVR_Action_In_Source {

    /** [Shortcut to: SteamVR_Input_Sources.Any] @Returns true if the action has been changed since the previous update */
    override val changed: Boolean
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.changed

    /** [Shortcut to: SteamVR_Input_Sources.Any] @Returns true if the action was changed for the previous update cycle */
    override val lastChanged: Boolean
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.changed

    /** [Shortcut to: SteamVR_Input_Sources.Any] The time the action was changed (Time.realtimeSinceStartup) */
    override var changedTime: Float = 0f
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.changedTime

    /** [Shortcut to: SteamVR_Input_Sources.Any] The time the action was updated (Time.realtimeSinceStartup) */
    override val updateTime: Float
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.updateTime

    /** [Shortcut to: SteamVR_Input_Sources.Any] The handle to the component that triggered the action to be changed */
    override val activeOrigin: VRInputValueHandle
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.activeOrigin

    /** [Shortcut to: SteamVR_Input_Sources.Any] The handle to the component that triggered the action to be changed in the previous update */
    override val lastActiveOrigin: VRInputValueHandle
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.lastActiveOrigin

    /** [Shortcut to: SteamVR_Input_Sources.Any] The input source that triggered the action to be changed */
    override val activeDevice: SteamVR_Input_Sources
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.activeDevice

    /** [Shortcut to: SteamVR_Input_Sources.Any] The device index (used by Render Models) used by the device that triggered the action to be changed */
    override val trackedDeviceIndex: TrackedDeviceIndex
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.trackedDeviceIndex

    /** [Shortcut to: SteamVR_Input_Sources.Any] The name of the component on the render model that caused the action to be changed (not localized) */
    override val renderModelComponentName: String
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.renderModelComponentName

    /** [Shortcut to: SteamVR_Input_Sources.Any] The full localized name for the component, controller, and hand that caused the action to be changed */
    override val localizedOriginName: String?
        get() = sourceMap!![SteamVR_Input_Sources.Any]!!.localizedOriginName

    /** [Should not be called by user code]
     *  Updates the data for all the input sources the system has detected need to be updated. */
    override fun updateValues() = sourceMap!!.updateValues()

    /** The name of the component on the render model that caused the action to be updated (not localized)
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    override fun getRenderModelComponentName(inputSource: SteamVR_Input_Sources): String =
            sourceMap!![inputSource]!!.renderModelComponentName

    /** The input source that triggered the action to be updated last
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    override fun getActiveDevice(inputSource: SteamVR_Input_Sources): SteamVR_Input_Sources =
            sourceMap!![inputSource]!!.activeDevice

    /** Gets the device index for the controller this action is bound to. This can be used for render models or the pose tracking system.
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    override fun getDeviceIndex(inputSource: SteamVR_Input_Sources): TrackedDeviceIndex =
            sourceMap!![inputSource]!!.trackedDeviceIndex

    /** Indicates whether or not the data for this action and specified input source has changed since the last update. Determined by SteamVR or 'changeTolerance'.
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    override fun getChanged(inputSource: SteamVR_Input_Sources): Boolean =
            sourceMap!![inputSource]!!.changed

    /** The time the action was changed (Time.realtimeSinceStartup)
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    override fun getTimeLastChanged(inputSource: SteamVR_Input_Sources): Float =
            sourceMap!![inputSource]!!.changedTime


    /** Gets the localized name of the device that the action corresponds to. Include as many EVRInputStringBits as you want to add to the localized string
     *
     *  @param inputSource
     *  @param localizedParts:
     *      VRInputString.Hand - Which hand the origin is in. ex: "Left Hand".
     *      VRInputString.ControllerType - What kind of controller the user has in that hand. ex: "Vive Controller".
     *      VRInputString.InputSource - What part of that controller is the origin. ex: "Trackpad".
     *      VRInputString.All - All of the above. ex: "Left Hand Vive Controller Trackpad". */
    override fun getLocalizedOriginPart(inputSource: SteamVR_Input_Sources, localizedParts: Array<vrInput.VRInputString>): String? =
            sourceMap!![inputSource]!!.getLocalizedOriginPart(localizedParts)

    /** Gets the localized full name of the device that the action was updated by. ex: "Left Hand Vive Controller Trackpad"
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    override fun getLocalizedOrigin(inputSource: SteamVR_Input_Sources): String? =
            sourceMap!![inputSource]!!.getLocalizedOrigin()

    /** [Should not be called by user code]
     *  @Returns whether the system has determined this source should be updated (based on code calls)
     *  Should only be used if you've set SteamVR_Action.startUpdatingSourceOnAccess to false.
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    override fun isUpdating(inputSource: SteamVR_Input_Sources): Boolean =
            sourceMap!!.isUpdating(inputSource)

    /** [Should not be called by user code]
     *  Forces the system to start updating the data for this action and the specified input source.
     *  Should only be used if you've set SteamVR_Action.startUpdatingSourceOnAccess to false.
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun forceAddSourceToUpdateList(inputSource: SteamVR_Input_Sources) =
            sourceMap!!.forceAddSourceToUpdateList(inputSource)
}

abstract class SteamVR_Action_In_Source_Map<SourceElement : SteamVR_Action_In_Source> : SteamVR_Action_Source_MapT<SourceElement>() {

    protected val updatingSources = ArrayList<SteamVR_Input_Sources>()

    /** [Should not be called by user code]
     *  @Returns whether the system has determined this source should be updated (based on code calls)
     *  Should only be used if you've set SteamVR_Action.startUpdatingSourceOnAccess to false.
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun isUpdating(inputSource: SteamVR_Input_Sources): Boolean = updatingSources.any { it == inputSource }

    override fun onAccessSource(inputSource: SteamVR_Input_Sources) {
        if (SteamVR_Action.startUpdatingSourceOnAccess)
            forceAddSourceToUpdateList(inputSource)
    }

    /** [Should not be called by user code]
     *  Forces the system to start updating the data for this action and the specified input source.
     *  Should only be used if you've set SteamVR_Action.startUpdatingSourceOnAccess to false.
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun forceAddSourceToUpdateList(inputSource: SteamVR_Input_Sources) {
        if (!sources[inputSource]!!.isUpdating) {
            updatingSources += inputSource
            sources[inputSource]!!.isUpdating = true

            if (!SteamVR_Input.isStartupFrame)
                sources[inputSource]!!.updateValue()
        }
    }

    /** [Should not be called by user code]
     *  Updates the data for all the input sources the system has detected need to be updated. */
    fun updateValues() = updatingSources.forEach { sources[it]!!.updateValue() }
}

/** In actions are all input type actions. Boolean, Single, Vector2, Vector3, Skeleton, and Pose.
 *  This class fires onChange and onUpdate events. */
abstract class SteamVR_Action_In_Source : SteamVR_Action_Source(), ISteamVR_Action_In_Source {

    /** [Should not be called by user code]
     *  Forces the system to start updating the data for this action and the specified input source.
     *  Should only be used if you've set SteamVR_Action.startUpdatingSourceOnAccess to false. */
    var isUpdating: Boolean = false

    /** The time the action was updated (Time.realtimeSinceStartup) */
    override var updateTime: Float = 0f
        protected set

    /** The handle to the component that triggered the action to be changed */
    abstract override val activeOrigin: VRInputValueHandle

    /** The handle to the component that triggered the action to be changed in the previous update */
    abstract override val lastActiveOrigin: VRInputValueHandle

    /** @Returns true if the action has been changed since the previous update */
    abstract override var changed: Boolean
        protected set

    /** @Returns true if the action was changed for the previous update cycle */
    abstract override var lastChanged: Boolean
        protected set

    /** The input source that triggered the action to be updated */
    override val activeDevice: SteamVR_Input_Sources
        get() {
            updateOriginTrackedDeviceInfo()
            return SteamVR_Input_Source.getSource(inputOriginInfo.devicePath)
        }

    /** The device index (used by Render Models) used by the device that triggered the action to be updated */
    override val trackedDeviceIndex: TrackedDeviceIndex
        get() {
            updateOriginTrackedDeviceInfo()
            return inputOriginInfo.trackedDeviceIndex
        }

    /** The name of the component on the render model that caused the action to be updated (not localized) */
    override val renderModelComponentName: String
        get() {
            updateOriginTrackedDeviceInfo()
            return inputOriginInfo.renderModelComponentName
        }

    /** Gets the localized full name of the device that the action was updated by. ex: "Left Hand Vive Controller Trackpad" */
    override val localizedOriginName: String?
        get() {
            updateOriginTrackedDeviceInfo()
            return getLocalizedOrigin()
        }


    /** The Time.realtimeSinceStartup that this action was last changed. */
    override var changedTime: Float = 0f
        protected set

    protected var lastOriginGetFrame = 0

    protected val inputOriginInfo = InputOriginInfo.calloc()
    protected lateinit var lastInputOriginInfo: InputOriginInfo

    /** [Should not be called by user code] Updates the data for this action and this input source */
    abstract fun updateValue()

    protected fun updateOriginTrackedDeviceInfo() {
        if (lastOriginGetFrame != Time.frameCount) //only get once per frame
        {
            val err = vrInput.getOriginTrackedDeviceInfo(activeOrigin, inputOriginInfo)

            if (err != vrInput.Error.None)
                System.err.println("[SteamVR] GetOriginTrackedDeviceInfo error ($fullPath): $err handle: $handle activeOrigin: $activeOrigin active: $active")

            lastInputOriginInfo = inputOriginInfo
            lastOriginGetFrame = Time.frameCount
        }
    }

    /** Gets the localized name of the device that the action corresponds to. Include as many EVRInputStringBits as you want to add to the localized string
     *
     *  @param inputSource
     *  @param localizedParts
     *      VRInputString.Hand - Which hand the origin is in. ex: "Left Hand".
     *      VRInputString.ControllerType - What kind of controller the user has in that hand. ex: "Vive Controller".
     *      VRInputString.InputSource - What part of that controller is the origin. ex: "Trackpad".
     *      VRInputString.All - All of the above. ex: "Left Hand Vive Controller Trackpad". */
    fun getLocalizedOriginPart(localizedParts: Array<vrInput.VRInputString>): String? {
        updateOriginTrackedDeviceInfo()
        return when {
            active -> SteamVR_Input.getLocalizedName(activeOrigin, localizedParts)
            else -> null
        }
    }

    /** Gets the localized full name of the device that the action was updated by. ex: "Left Hand Vive Controller Trackpad" */
    fun getLocalizedOrigin(): String? {
        updateOriginTrackedDeviceInfo()
        return when {
            active -> SteamVR_Input.getLocalizedName(activeOrigin, arrayOf(vrInput.VRInputString.All))
            else -> null
        }
    }
}

interface ISteamVR_Action_In : ISteamVR_Action, ISteamVR_Action_In_Source {

    /** [Should not be called by user code]
     *  Updates the data for all the input sources the system has detected need to be updated. */
    fun updateValues()

    /** The name of the component on the render model that caused the action to be updated (not localized)
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getRenderModelComponentName(inputSource: SteamVR_Input_Sources): String

    /** The input source that triggered the action to be updated last
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getActiveDevice(inputSource: SteamVR_Input_Sources): SteamVR_Input_Sources

    /** Gets the device index for the controller this action is bound to. This can be used for render models or the pose tracking system.
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getDeviceIndex(inputSource: SteamVR_Input_Sources): Int

    /** Indicates whether or not the data for this action and specified input source has changed since the last update. Determined by SteamVR or 'changeTolerance'.
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getChanged(inputSource: SteamVR_Input_Sources): Boolean


    /** Gets the localized name of the device that the action corresponds to. Include as many EVRInputStringBits as you want to add to the localized string
     *
     *  @param inputSource
     *  @param localizedParts
     *      VRInputString.Hand - Which hand the origin is in. ex: "Left Hand".
     *      VRInputString.ControllerType - What kind of controller the user has in that hand. ex: "Vive Controller".
     *      VRInputString.InputSource - What part of that controller is the origin. ex: "Trackpad".
     *      VRInputString.All - All of the above. ex: "Left Hand Vive Controller Trackpad". */
    fun getLocalizedOriginPart(inputSource: SteamVR_Input_Sources, localizedParts: Array<vrInput.VRInputString>): String?

    /** Gets the localized full name of the device that the action was updated by. ex: "Left Hand Vive Controller Trackpad"
     *
     *  @param inputSource: The device you would like to get data from. Any if the action is not device specific. */
    fun getLocalizedOrigin(inputSource: SteamVR_Input_Sources): String?
}

interface ISteamVR_Action_In_Source : ISteamVR_Action_Source {

    /** @Returns true if the action has been changed in the most recent update */
    val changed: Boolean

    /** @Returns true if the action was changed for the previous update cycle */
    val lastChanged: Boolean

    /** The Time.realtimeSinceStartup that this action was last changed. */
    val changedTime: Float

    /** The time the action was updated (Time.realtimeSinceStartup) */
    val updateTime: Float

    /** The handle to the component that triggered the action to be changed */
    val activeOrigin: VRInputValueHandle

    /** The handle to the component that triggered the action to be changed in the previous update */
    val lastActiveOrigin: VRInputValueHandle

    /** The input source that triggered the action to be updated */
    val activeDevice: SteamVR_Input_Sources

    /** The device index (used by Render Models) used by the device that triggered the action to be updated */
    val trackedDeviceIndex: TrackedDeviceIndex

    /** The name of the component on the render model that caused the action to be updated (not localized) */
    val renderModelComponentName: String

    /** Gets the localized full name of the device that the action was updated by. ex: "Left Hand Vive Controller Trackpad" */
    val localizedOriginName: String?
}