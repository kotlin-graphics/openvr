package openvr.plugin2

import openvr.lib.VRInputValueHandle
import openvr.lib.vrInput

object SteamVR_Input_Source {

    val numSources = SteamVR_Input_Sources.values().size

    private val inputSourceHandlesBySource: LongArray
    private val inputSourceSourcesByHandle: MutableMap<Long, SteamVR_Input_Sources>

    private var _allSources: Array<SteamVR_Input_Sources>? = null

    infix fun getHandle(inputSource: SteamVR_Input_Sources): Long =
            inputSourceHandlesBySource.getOrElse(inputSource.ordinal) { 0 }

    infix fun getSource(handle: Long): SteamVR_Input_Sources =
            inputSourceSourcesByHandle[handle] ?: SteamVR_Input_Sources.Any

    val allSources: Array<SteamVR_Input_Sources>
        get() {
            if (_allSources == null)
                _allSources = SteamVR_Input_Sources.values()

            return _allSources!!
        }

    private fun getPath(inputSourceEnumName: String): String =
            SteamVR_Input_Sources.valueOf(inputSourceEnumName).description

    init {

        val allSourcesList = ArrayList<SteamVR_Input_Sources>()
        val enumNames = SteamVR_Input_Sources.values().map { it.name }
        inputSourceHandlesBySource = LongArray(enumNames.size)
        inputSourceSourcesByHandle = mutableMapOf()

        for (enumIndex in enumNames.indices) {

            val path = getPath(enumNames[enumIndex])

            val handle = vrInput.getInputSourceHandle(path)

            if (vrInput.error != vrInput.Error.None)
                System.err.println("[SteamVR] getInputSourceHandle (" + path + ") error: " + vrInput.error)

            if (enumNames[enumIndex] == SteamVR_Input_Sources.Any.name) { //todo: temporary hack
                inputSourceHandlesBySource[enumIndex] = 0
                inputSourceSourcesByHandle[0] = SteamVR_Input_Sources.values()[enumIndex]
            } else {
                inputSourceHandlesBySource[enumIndex] = handle
                inputSourceSourcesByHandle[handle] = SteamVR_Input_Sources.values()[enumIndex]
            }

            allSourcesList += SteamVR_Input_Sources.values()[enumIndex]
        }

        allSourcesList.toArray(_allSources!!)
    }
}