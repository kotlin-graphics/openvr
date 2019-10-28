package openvr.assets.steamVR.input

import kool.set
import openvr.lib.*
import openvr.plugin2.SteamVR_Input_Source
import openvr.plugin2.SteamVR_Input_Sources
import openvr.unity.Application
import openvr.unity.Time
import org.lwjgl.openvr.VRActiveActionSet
import uno.kotlin.plusAssign

//======= Copyright (c) Valve Corporation, All rights reserved. ===============

/** Action sets are logical groupings of actions. Multiple sets can be active at one time. */
object SteamVR_ActionSet_Manager {

    lateinit var rawActiveActionSetArray: VRActiveActionSet.Buffer

    private var changed = false

    fun initialize() {}

    /** Disable all known action sets. */
    fun disableAllActionSets() {
        for (actionSet in SteamVR_Input.actionSets) {
            actionSet.deactivate(SteamVR_Input_Sources.Any)
            actionSet.deactivate(SteamVR_Input_Sources.LeftHand)
            actionSet.deactivate(SteamVR_Input_Sources.RightHand)
        }
    }

    private var lastFrameUpdated = 0

    fun updateActionStates(force: Boolean = false) {

        if (force || Time.frameCount != lastFrameUpdated) {
            lastFrameUpdated = Time.frameCount

            if (changed)
                updateActionSetsArray()

            if (rawActiveActionSetArray != null && rawActiveActionSetArray.hasRemaining()) {
                val err = vrInput updateActionState rawActiveActionSetArray
                if (err != vrInput.Error.None)
                    System.err.println("[SteamVR] UpdateActionState error: ${vrInput.error}")
                //else Debug.Log("Action sets activated: " + activeActionSets.Length);
            } else {
                //Debug.LogWarning("No sets active");
            }
        }
    }

    fun setChanged() {
        changed = true
    }

    private fun updateActionSetsArray() {

        val activeActionSetsList = ArrayList<VRActiveActionSet>()

        val sources = SteamVR_Input_Source.allSources

        for (set in SteamVR_Input.actionSets) {

            for (source in sources) {

                if (set readRawSetActive source) {
                    val activeSet = VRActiveActionSet.calloc().apply {
                        actionSet = set.handle
                        priority = set readRawSetPriority source
                        restrictedToDevice = SteamVR_Input_Source getHandle source
                    }
                    val insertionIndex = activeActionSetsList.indexOfFirst { it.priority > activeSet.priority }
                    activeActionSetsList[insertionIndex] = activeSet
                }
            }
        }

        changed = false

        if (::rawActiveActionSetArray.isInitialized)
            rawActiveActionSetArray.free()
        rawActiveActionSetArray = VRActiveActionSet.calloc(activeActionSetsList.size)
        activeActionSetsList.forEachIndexed { i, set -> rawActiveActionSetArray[i] = set }

        if (Application.isEditor || updateDebugTextInBuilds)
            updateDebugText()
    }

    fun getSetFromHandle(handle: VRActionHandle): SteamVR_ActionSet? = SteamVR_Input.actionSets.firstOrNull { it.handle == handle }

    lateinit var debugActiveSetListText: String
    var updateDebugTextInBuilds = false

    private fun updateDebugText()    {

        val stringBuilder = StringBuilder()

        rawActiveActionSetArray.forEach { set ->
            stringBuilder += set.priority
            stringBuilder += '\t'
            stringBuilder += SteamVR_Input_Source.getSource(set.restrictedToDevice)
            stringBuilder += '\t'
            stringBuilder += getSetFromHandle(set.actionSet)!!.shortName
            stringBuilder += '\n'
        }

        debugActiveSetListText = stringBuilder.toString()
    }
}