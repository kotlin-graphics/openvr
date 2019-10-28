package openvr.steamVR_Input.actionSetClasses

import openvr.assets.steamVR.input.SteamVR_ActionSet
import openvr.assets.steamVR.input.SteamVR_Action_Boolean
import openvr.assets.steamVR.input.SteamVR_Action_Vector2
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

class SteamVR_Input_ActionSet_platformer : SteamVR_ActionSet() {

    val move: SteamVR_Action_Vector2
        get() = SteamVR_Actions.platformer_Move

    val jump:  SteamVR_Action_Boolean
        get() = SteamVR_Actions.platformer_Jump
}
