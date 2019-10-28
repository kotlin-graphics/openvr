package openvr.assets.steamVR.input

//======= Copyright (c) Valve Corporation, All rights reserved. ===============

abstract class SteamVR_Action_OutT<SourceMap, SourceElement> : SteamVR_ActionT<SourceMap, SourceElement>(), ISteamVR_Action_Out
        where SourceMap : SteamVR_Action_Source_MapT<SourceElement>,  SourceElement : SteamVR_Action_Out_Source

abstract class SteamVR_Action_Out_Source : SteamVR_Action_Source(), ISteamVR_Action_Out_Source

interface ISteamVR_Action_Out : ISteamVR_Action, ISteamVR_Action_Out_Source

interface ISteamVR_Action_Out_Source : ISteamVR_Action_Source