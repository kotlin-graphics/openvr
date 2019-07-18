package openvr.steamVR.input

import com.beust.klaxon.Klaxon
import java.io.File
import java.net.URL

//======= Copyright (c) Valve Corporation, All rights reserved. ===============

class SteamVR_Input_ActionFile {

    val actions = ArrayList<SteamVR_Input_ActionFile_Action>()
    val actionSets = ArrayList<SteamVR_Input_ActionFile_ActionSet>()
    val defaultBindings = ArrayList<SteamVR_Input_ActionFile_DefaultBinding>()
    val localization = ArrayList<MutableMap<String, String>>()

    val localizationHelperList = ArrayList<SteamVR_Input_ActionFile_LocalizationItem>()

    fun initializeHelperLists() {

        for (actionset in actionSets) {
            actions.filter { it.path.startsWith(actionset.name) && it.type in SteamVR_Input_ActionFile_ActionTypes.listIn }.toCollection(actionset.actionsInList)
            actions.filter { it.path.startsWith(actionset.name) && it.type in SteamVR_Input_ActionFile_ActionTypes.listOut }.toCollection(actionset.actionsOutList)
            actions.filter { it.path.startsWith(actionset.name) }.toCollection(actionset.actionsList)
        }

        for (item in localization)
            localizationHelperList += SteamVR_Input_ActionFile_LocalizationItem(item)
    }

    fun saveHelperLists() {

        //fix actions list
        for (actionset in actionSets) {
            actionset.actionsList.clear()
            actionset.actionsList += actionset.actionsInList
            actionset.actionsList += actionset.actionsOutList
        }

        actions.clear()

        for (actionset in actionSets) {
            actions += actionset.actionsInList
            actions += actionset.actionsOutList
        }

        localization.clear()
        for (item in localizationHelperList) {
            val localizationItem = mutableMapOf(SteamVR_Input_ActionFile_LocalizationItem.languageTagKeyName to item.language!!)

            localizationItem += item.items

            localization += localizationItem
        }
    }

    fun getFilesToCopy(throwErrors: Boolean = false): Array<String> {

        val files = ArrayList<String>()
TODO()
//        val actionFileInfo = File(SteamVR_Input.actionsFilePath)
//        string path = actionFileInfo . Directory . FullName
//
//                files.Add(SteamVR_Input.actionsFilePath)m
//
//        foreach(var binding in default_bindings)
//        {
//            string bindingPath = Path . Combine (path, binding.binding_url)
//
//            if (File.Exists(bindingPath))
//                files.Add(bindingPath)
//            else {
//                if (throwErrors) {
//                    Debug.LogError("<b>[SteamVR]</b> Could not bind binding file specified by the actions.json manifest: " + bindingPath)
//                }
//            }
//        }
//
//        return files.ToArray()
    }

    fun copyFilesToPath(toPath: String, overwrite: Boolean)    {
        TODO()
//        string[] files = SteamVR_Input . actionFile . GetFilesToCopy ()
//
//        foreach(string file in files)
//        {
//            FileInfo bindingInfo = new FileInfo(file)
//            string newFilePath = Path . Combine (toPath, bindingInfo.Name)
//
//            bool exists = false
//            if (File.Exists(newFilePath))
//                exists = true
//
//            if (exists) {
//                if (overwrite) {
//                    FileInfo existingFile = new FileInfo(newFilePath)
//                    existingFile.IsReadOnly = false
//                    existingFile.Delete()
//
//                    File.Copy(file, newFilePath)
//
//                    RemoveAppKey(newFilePath)
//
//                    Debug.Log("<b>[SteamVR]</b> Copied (overwrote) SteamVR Input file at path: " + newFilePath)
//                } else {
//                    Debug.Log("<b>[SteamVR]</b> Skipped writing existing file at path: " + newFilePath)
//                }
//            } else {
//                File.Copy(file, newFilePath)
//
//                RemoveAppKey(newFilePath)
//
//                Debug.Log("<b>[SteamVR]</b> Copied SteamVR Input file to folder: " + newFilePath)
//            }
//
//        }
    }

    fun isInStreamingAssets(): Boolean = "StreamingAssets" in SteamVR_Input.actionsFilePath!!.toExternalForm()


    private val findString_appKeyStart = "\"app_key\""
    private val findString_appKeyEnd = "\","

    companion object {

        fun getShortName(name: String): String {

            var fullName = name
            var lastSlash = fullName.lastIndexOf('/')
            if (lastSlash != -1) {
                if (lastSlash == fullName.lastIndex) {
                    fullName = fullName.substring(0, lastSlash)
                    lastSlash = fullName.lastIndexOf('/')
                    if (lastSlash == -1)
                        return getCodeFriendlyName(fullName)
                }
                return getCodeFriendlyName(fullName.substring(lastSlash + 1))
            }

            return getCodeFriendlyName(fullName)
        }

        fun getCodeFriendlyName(name_: String): String {

            var name = name_.replace('/', '_').replace(' ', '_')

            if (!name[0].isLetter())
                name = "_$name"

            for (charIndex in name.indices) {
                if (!name[charIndex].isLetterOrDigit() && name[charIndex] != '_')
                    name.replaceRange(charIndex, charIndex + 1, "_")
            }

            return name
        }

        private fun removeAppKey( newFilePath: String)        {
            TODO()
//            if (File.Exists(newFilePath)) {
//                string jsonText = System . IO . File . ReadAllText (newFilePath)
//
//                string findString = "\"app_key\""
//                int stringStart = jsonText . IndexOf (findString)
//
//                if (stringStart == -1)
//                    return //no app key
//
//                int stringEnd = jsonText . IndexOf ("\",", stringStart)
//
//                if (stringEnd == -1)
//                    return //no end?
//
//                stringEnd += findString_appKeyEnd.Length
//
//                int stringLength = stringEnd -stringStart
//
//                string newJsonText = jsonText . Remove (stringStart, stringLength)
//
//                FileInfo file = new FileInfo(newFilePath)
//                file.IsReadOnly = false
//
//                File.WriteAllText(newFilePath, newJsonText)
//            }
        }

        fun hardcodeInit() = SteamVR_Input_ActionFile().apply {
            actionSets += listOf(
                    SteamVR_Input_ActionFile_ActionSet().apply {
                        name = "/actions/default"
                        usage = "single"
                    },
                    SteamVR_Input_ActionFile_ActionSet().apply {
                        name = "/actions/platformer"
                        usage = "single"
                    },
                    SteamVR_Input_ActionFile_ActionSet().apply {
                        name = "/actions/buggy"
                        usage = "single"
                    },
                    SteamVR_Input_ActionFile_ActionSet().apply {
                        name = "/actions/mixedreality"
                        usage = "single"
                    })
            actions += listOf(
                    SteamVR_Input_ActionFile_Action().apply {
                        name = "/actions/default/in/InteractUI"
                        type = "boolean"
                    },
                    SteamVR_Input_ActionFile_Action().apply {
                        name = "/actions/default/in/Teleport"
                        type = "boolean"
                    },
                    SteamVR_Input_ActionFile_Action().apply {
                        name = "/actions/default/in/GrabPinch"
                        type = "boolean"
                    },
                    SteamVR_Input_ActionFile_Action().apply {
                        name = "/actions/default/in/GrabGrip"
                        type = "boolean"
                    },
                    SteamVR_Input_ActionFile_Action().apply {
                        name = "/actions/default/in/Pose"
                        type = "pose"
                    },
                    SteamVR_Input_ActionFile_Action().apply {
                        name = "/actions/default/in/SkeletonLeftHand"
                        type = "skeleton"
                        skeleton = "/skeleton/hand/left"
                    },
                    SteamVR_Input_ActionFile_Action().apply {
                        name = "/actions/default/in/SkeletonRightHand"
                        type = "skeleton"
                        skeleton = "/skeleton/hand/right"
                    },
                    SteamVR_Input_ActionFile_Action().apply {
                        name = "/actions/default/in/Squeeze"
                        type = "vector1"
                        requirement = "optional"
                    },
                    SteamVR_Input_ActionFile_Action().apply {
                        name = "/actions/default/in/HeadsetOnHead"
                        type = "boolean"
                        requirement = "optional"
                    },
                    SteamVR_Input_ActionFile_Action().apply {
                        name = "/actions/default/out/Haptic"
                        type = "vibration"
                    },
                    SteamVR_Input_ActionFile_Action().apply {
                        name = "/actions/platformer/in/Move"
                        type = "vector2"
                    },
                    SteamVR_Input_ActionFile_Action().apply {
                        name = "/actions/platformer/in/Jump"
                        type = "boolean"
                    },
                    SteamVR_Input_ActionFile_Action().apply {
                        name = "/actions/buggy/in/Steering"
                        type = "vector2"
                    },
                    SteamVR_Input_ActionFile_Action().apply {
                        name = "/actions/buggy/in/Brake"
                        type = "boolean"
                    },
                    SteamVR_Input_ActionFile_Action().apply {
                        name = "/actions/buggy/in/Reset"
                        type = "boolean"
                    },
                    SteamVR_Input_ActionFile_Action().apply {
                        name = "/actions/mixedreality/in/ExternalCamera"
                        type = "pose"
                    }
            )
            defaultBindings += listOf(
                    SteamVR_Input_ActionFile_DefaultBinding().apply {
                        bindingUrl = "bindings_vive_controller.json"
                        controllerType = "vive_controller"
                    },
                    SteamVR_Input_ActionFile_DefaultBinding().apply {
                        bindingUrl = "bindings_oculus_touch.json"
                        controllerType = "oculus_touch"
                    },
                    SteamVR_Input_ActionFile_DefaultBinding().apply {
                        bindingUrl = "bindings_knuckles.json"
                        controllerType = "knuckles"
                    },
                    SteamVR_Input_ActionFile_DefaultBinding().apply {
                        bindingUrl = "bindings_holographic_controller.json"
                        controllerType = "holographic_controller"
                    },
                    SteamVR_Input_ActionFile_DefaultBinding().apply {
                        bindingUrl = "binding_vive.json"
                        controllerType = "vive"
                    },
                    SteamVR_Input_ActionFile_DefaultBinding().apply {
                        bindingUrl = "binding_vive_pro.json"
                        controllerType = "vive_pro"
                    },
                    SteamVR_Input_ActionFile_DefaultBinding().apply {
                        bindingUrl = "binding_rift.json"
                        controllerType = "rift"
                    },
                    SteamVR_Input_ActionFile_DefaultBinding().apply {
                        bindingUrl = "binding_holographic_hmd.json"
                        controllerType = "holographic_hmd"
                    },
                    SteamVR_Input_ActionFile_DefaultBinding().apply {
                        bindingUrl = "binding_vive_tracker_camera.json"
                        controllerType = "vive_tracker_camera"
                    }
            )
            localization += mutableMapOf(
                    "language_tag" to "en_US",
                    "/actions/default/in/GrabGrip" to "Grab Grip",
                    "/actions/default/in/GrabPinch" to "Grab Pinch",
                    "/actions/default/in/HeadsetOnHead" to "Headset on head (proximity sensor)",
                    "/actions/default/in/InteractUI" to "Interact With UI",
                    "/actions/default/in/Pose" to "Pose",
                    "/actions/default/in/SkeletonLeftHand" to "Skeleton (Left)",
                    "/actions/default/in/SkeletonRightHand" to "Skeleton (Right)",
                    "/actions/default/in/Teleport" to "Teleport",
                    "/actions/default/out/Haptic" to "Haptic",
                    "/actions/platformer/in/Jump" to "Jump")
        }
    }

    fun save(path: URL)    {
        //SanitizeActionFile(); //todo: shouldn't we be doing this?
        File(path.toURI()).writeText(Klaxon().toJsonString(this))
    }
}

enum class SteamVR_Input_ActionFile_DefaultBinding_ControllerTypes {
    vive,
    vive_pro,
    vive_controller,
    generic,
    holographic_controller,
    oculus_touch,
    gamepad,
    knuckles,
}

class SteamVR_Input_ActionFile_DefaultBinding {
    lateinit var controllerType: String
    lateinit var bindingUrl: String

    fun getCopy() = SteamVR_Input_ActionFile_DefaultBinding().also {
        it.controllerType = controllerType
        it.bindingUrl = bindingUrl
    }
}

class SteamVR_Input_ActionFile_ActionSet {

    private val actionSetInstancePrefix = "instance_"

    lateinit var name: String
    lateinit var usage: String

    val codeFriendlyName: String
        get() = SteamVR_Input_ActionFile.getCodeFriendlyName(name)

    val shortName: String
        get() {
            val lastIndex = name.lastIndexOf('/')
            if (lastIndex == name.length - 1)
                return ""

            return SteamVR_Input_ActionFile.getShortName(name)
        }

    fun setNewShortName(newShortName: String) {
        name = getPathFromName(newShortName)
    }

    fun createNewName() = getPathFromName("NewSet")

    companion object {

        private val nameTemplate = "/actions/%s"

        fun getPathFromName(name: String) = nameTemplate.format(name)

        fun createNew() = SteamVR_Input_ActionFile_ActionSet().apply { name = createNewName() }
    }

    fun getCopy() = SteamVR_Input_ActionFile_ActionSet().also {
        it.name = this.name
        it.usage = this.usage
    }

    override fun equals(other: Any?): Boolean {
        if (other is SteamVR_Input_ActionFile_ActionSet) {
            if (other === this)
                return true

            if (other.name == name)
                return true

            return false
        }

        return super.equals(other)
    }

    override fun hashCode(): Int = super.hashCode()

    val actionsInList = ArrayList<SteamVR_Input_ActionFile_Action>()

    val actionsOutList = ArrayList<SteamVR_Input_ActionFile_Action>()

    val actionsList = ArrayList<SteamVR_Input_ActionFile_Action>()
}

enum class SteamVR_Input_ActionFile_Action_Requirements { optional, suggested, mandatory }


class SteamVR_Input_ActionFile_Action {

    lateinit var name: String
    lateinit var type: String
    var scope: String? = null
    var skeleton: String? = null
    var requirement: String? = null

    fun getCopy() = SteamVR_Input_ActionFile_Action().also {
        it.name = name
        it.type = type
        it.scope = scope
        it.skeleton = skeleton
        it.requirement = requirement
    }

    var requirementEnum: SteamVR_Input_ActionFile_Action_Requirements
        get() = requirementValues.find { it.name == requirement!!.toLowerCase() }
                ?: SteamVR_Input_ActionFile_Action_Requirements.suggested
        set(value) {
            requirement = value.name
        }

    val codeFriendlyName: String
        get() = SteamVR_Input_ActionFile.getCodeFriendlyName(name)

    val shortName: String
        get() = SteamVR_Input_ActionFile.getShortName(name)

    val path: String
        get() {
            val lastIndex = name.lastIndexOf('/')
            if (lastIndex != -1 && lastIndex + 1 < name.length)
                return name.substring(0, lastIndex + 1)

            return name
        }

    companion object {

        private val nameTemplate = "/actions/%s/%s/%s"

        private val requirementValues = SteamVR_Input_ActionFile_Action_Requirements.values()

        fun createNewName(actionSet: String, direction: String) = nameTemplate.format(actionSet, direction, "NewAction")

        fun createNewName(actionSet: String, direction: SteamVR_ActionDirections, actionName: String) =
                nameTemplate.format(actionSet, direction.name.toLowerCase(), actionName)

        fun createNew(actionSet: String, direction: SteamVR_ActionDirections, actionType: String): SteamVR_Input_ActionFile_Action =
                SteamVR_Input_ActionFile_Action().apply {
                    name = createNewName(actionSet, direction.name.toLowerCase())
                    type = actionType
                }
    }

    val direction: SteamVR_ActionDirections
        get() = when (type.toLowerCase()) {
            SteamVR_Input_ActionFile_ActionTypes.vibration -> SteamVR_ActionDirections.Out
            else -> SteamVR_ActionDirections.In
        }

    protected val prefix = "/actions/"

    val actionSet: String
        get() {
            val setEnd = name.indexOf('/', prefix.length)
            if (setEnd == -1)
                return ""
            return name.substring(0, setEnd)
        }

    fun setNewActionSet(newSetName: String) {
        name = nameTemplate.format(newSetName, direction.name.toLowerCase(), shortName)
    }

    override fun toString(): String = shortName

    override fun equals(other: Any?): Boolean {
        if (other is SteamVR_Input_ActionFile_Action) {
            if (this === other)
                return true

            if (name == other.name && type == other.type && skeleton == other.skeleton && requirement == other.requirement)
                return true

            return false
        }
        return super.equals(other)
    }

    override fun hashCode(): Int = super.hashCode()
}

class SteamVR_Input_ActionFile_LocalizationItem {

    companion object {
        val languageTagKeyName = "language_tag"
    }

    var language: String? = null
    val items = mutableMapOf<String, String>()

    constructor(newLanguage: String) {
        language = newLanguage
    }

    constructor(dictionary: MutableMap<String, String>?) {
        if (dictionary == null)
            return

        language = dictionary[languageTagKeyName]
        if (language == null)
            System.err.println("[SteamVR] Input: Error in actions file, no language_tag in localization array item.")

        for (item in dictionary)
            if (item.key != languageTagKeyName)
                items[item.key] = item.value
    }
}

class SteamVR_Input_ManifestFile(val source: String, val applications: List<SteamVR_Input_ManifestFile_Application>)

class SteamVR_Input_ManifestFile_Application {
    lateinit var app_key: String
    lateinit var launch_type: String
    lateinit var url: String
    lateinit var binary_path_windows: String
    lateinit var binary_path_linux: String
    lateinit var binary_path_osx: String
    lateinit var action_manifest_path: String
    //public List<SteamVR_Input_ManifestFile_Application_Binding> bindings = new List<SteamVR_Input_ManifestFile_Application_Binding>();
    lateinit var image_path: String
    val strings = mutableMapOf<String, SteamVR_Input_ManifestFile_ApplicationString>()
}

class SteamVR_Input_Unity_AssemblyFile_Definition {
    val name = "SteamVR_Actions"
    val references = arrayOf("SteamVR")
    val optionalUnityReferences = emptyArray<String>()
    val includePlatforms = emptyArray<String>()
    val excludePlatforms = emptyArray<String>()
    var allowUnsafeCode = false
    var overrideReferences = false
    val precompiledReferences = emptyArray<String>()
    var autoReferenced = false
    val defineConstraints = emptyArray<String>()
}

class SteamVR_Input_ManifestFile_ApplicationString(val name: String)

class SteamVR_Input_ManifestFile_Application_Binding(val controllerType: String, val bindingUrl: String)

object SteamVR_Input_ManifestFile_Application_Binding_ControllerTypes {
    val oculus_touch = "oculus_touch"
    val vive_controller = "vive_controller"
    val knuckles = "knuckles"
    val holographic_controller = "holographic_controller"
    val vive = "vive"
    val vive_pro = "vive_pro"
    val holographic_hmd = "holographic_hmd"
    val rift = "rift"
    val vive_tracker_camera = "vive_tracker_camera"
}

object SteamVR_Input_ActionFile_ActionTypes {
    val boolean = "boolean"
    val vector1 = "vector1"
    val vector2 = "vector2"
    val vector3 = "vector3"
    val vibration = "vibration"
    val pose = "pose"
    val skeleton = "skeleton"

    val skeletonLeftPath = "\\skeleton\\hand\\left"
    val skeletonRightPath = "\\skeleton\\hand\\right"

    val listAll = arrayOf(boolean, vector1, vector2, vector3, vibration, pose, skeleton)
    val listIn = arrayOf(boolean, vector1, vector2, vector3, pose, skeleton)
    val listOut = arrayOf(vibration)
    val listSkeletons = arrayOf(skeletonLeftPath, skeletonRightPath)
}

object SteamVR_Input_ActionFile_ActionSet_Usages {
    val leftright = "leftright"
    val single = "single"
    val hidden = "hidden"

    val leftrightDescription = "per hand"
    val singleDescription = "mirrored"
    val hiddenDescription = "hidden"

    val listValues = arrayOf(leftright, single, hidden)
    val listDescriptions = arrayOf(leftrightDescription, singleDescription, hiddenDescription)
}