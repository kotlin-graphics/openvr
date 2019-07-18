package openvr.steamVR_Input.actionSetClasses

import openvr.steamVR.input.SteamVR_ActionSet
import openvr.steamVR.input.SteamVR_Action_Boolean
import openvr.steamVR.input.SteamVR_Action_Single
import openvr.steamVR.input.SteamVR_Action_Vector2
import openvr.steamVR_Input.SteamVR_Actions

//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:4.0.30319.42000
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

class SteamVR_Input_ActionSet_buggy : SteamVR_ActionSet() {

    val steering:  SteamVR_Action_Vector2
        get() = SteamVR_Actions.buggy_Steering

    val throttle:  SteamVR_Action_Single
        get() = SteamVR_Actions.buggy_Throttle

    val brake:  SteamVR_Action_Boolean
        get() = SteamVR_Actions.buggy_Brake

    val  reset: SteamVR_Action_Boolean
        get() = SteamVR_Actions.buggy_Reset
}