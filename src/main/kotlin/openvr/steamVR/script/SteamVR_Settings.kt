package openvr.steamVR.script

import openvr.lib.TrackingUniverseOrigin
import openvr.steamVR.input.SteamVR_Action_Pose
import openvr.steamVR.input.SteamVR_UpdateModes
import java.net.URL

//======= Copyright (c) Valve Corporation, All rights reserved. ===============

object SteamVR_Settings {

    var pauseGameWhenDashboardVisible = true
    var lockPhysicsUpdateRateToRenderFrequency = true
    var trackingSpace: TrackingUniverseOrigin
        get() = trackingSpaceOrigin
        set(newValue) {
            trackingSpaceOrigin = newValue
            if (SteamVR_Behaviour.isPlaying)
                SteamVR_Action_Pose.setTrackingUniverseOrigin(trackingSpaceOrigin)
        }

    private var trackingSpaceOrigin = TrackingUniverseOrigin.Standing

    /** Filename local to the project root (or executable, in a build) */
    var actionsFilePath: URL? = ClassLoader.getSystemResource("actions.json")

    /** Path local to the Assets folder */
    var steamVRInputPath = "SteamVR_Input"

    var inputUpdateMode = SteamVR_UpdateModes.OnUpdate.i
    var poseUpdateMode = SteamVR_UpdateModes.OnPreCull.i

    var activateFirstActionSetOnStart = true

    /** This is the app key the unity editor will use to identify your application. (can be \"steam.app.[appid]\" to persist bindings between editor steam) */
    var editorAppKey: String? = null

    /** The SteamVR Plugin can automatically make sure VR is enabled in your player settings and if not, enable it. */
    var autoEnableVR = true

    fun isInputUpdateMode(toCheck: SteamVR_UpdateModes) = (inputUpdateMode and toCheck.i) == toCheck.i

    fun isPoseUpdateMode(toCheck: SteamVR_UpdateModes) = (poseUpdateMode and toCheck.i) == toCheck.i

//    public static void VerifyScriptableObject()
//    {
//        LoadInstance()
//    }
//
//    private static void LoadInstance()
//    {
//        if (_instance == null) {
//            _instance = Resources.Load<SteamVR_Settings>("SteamVR_Settings")
//
//            if (_instance == null) {
//                _instance = SteamVR_Settings.CreateInstance<SteamVR_Settings>()
//
//                #if UNITY_EDITOR
//                string folderPath = SteamVR . GetResourcesFolderPath (true)
//                string assetPath = System . IO . Path . Combine (folderPath, "SteamVR_Settings.asset")
//
//                UnityEditor.AssetDatabase.CreateAsset(_instance, assetPath)
//                UnityEditor.AssetDatabase.SaveAssets()
//                #endif
//            }
//
//            if (string.IsNullOrEmpty(_instance.editorAppKey)) {
//                _instance.editorAppKey = SteamVR.GenerateAppKey()
//                Debug.Log("<b>[SteamVR Setup]</b> Generated you an editor app key of: " + _instance.editorAppKey + ". This lets the editor tell SteamVR what project this is. Has no effect on builds. This can be changed in Assets/SteamVR/Resources/SteamVR_Settings")
//                #if UNITY_EDITOR
//                UnityEditor.EditorUtility.SetDirty(_instance)
//                UnityEditor.AssetDatabase.SaveAssets()
//                #endif
//            }
//        }
//    }
}