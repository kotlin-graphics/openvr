package openvr.plugin2

import openvr.lib.VRInputValueHandle
import openvr.lib.vrInput

object SteamVR_Input_Source {

    private val inputSourceHandlesBySource = mutableMapOf<SteamVR_Input_Sources, VRInputValueHandle>()
    private val inputSourceSourcesByHandle = mutableMapOf<VRInputValueHandle, SteamVR_Input_Sources>()

//    private var enumType: KClass<Enum<*>> = SteamVR_Input_Sources::class as KClass<Enum<*>>
//    private val descriptionType = String

    lateinit var allSources: Array<SteamVR_Input_Sources>

    infix fun getHandle(inputSource: SteamVR_Input_Sources) = inputSourceHandlesBySource[inputSource] ?: 0
    infix fun getSource(handle: VRInputValueHandle) = inputSourceSourcesByHandle[handle] ?: SteamVR_Input_Sources.Any

//    public static SteamVR_Input_Sources[] GetAllSources()
//    {
//        if (allSources == null)
//            allSources = (SteamVR_Input_Sources[])System.Enum.GetValues(typeof(SteamVR_Input_Sources));
//
//        return allSources;
//    }
//
//    private static string GetPath(string inputSourceEnumName)
//    {
//        return ((DescriptionAttribute)enumType.GetMember(inputSourceEnumName)[0].GetCustomAttributes(descriptionType, false)[0]).Description;
//    }

    fun initialize() {
        val allSourcesList = ArrayList<SteamVR_Input_Sources>()
//        val enumNames = enumType.java.enumConstants.map { it.name }
        inputSourceHandlesBySource.clear()
        inputSourceSourcesByHandle.clear()

        val enums = SteamVR_Input_Sources.values()

        for (enumIndex in enums.indices)        {

            val enum = enums[enumIndex]
            val path = enum.description

            val handle = vrInput.getInputSourceHandle(path)

            if (vrInput.error != vrInput.Error.None)
                System.err.println("""[SteamVR] getInputSourceHandle ($path) error: ${vrInput.error}""")

            if (enum == SteamVR_Input_Sources.Any) { //todo: temporary hack
                inputSourceHandlesBySource[enum] = 0
                inputSourceSourcesByHandle[0] = enum
            }
            else {
                inputSourceHandlesBySource[enum] = handle
                inputSourceSourcesByHandle[handle] = enum
            }

            allSourcesList += enum
        }

        allSourcesList.toArray(allSources)
    }
}