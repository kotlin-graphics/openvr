package openvr.steamVR.input

//======= Copyright (c) Valve Corporation, All rights reserved. ===============

enum class SteamVR_UpdateModes {
    Nothing, OnUpdate, OnFixedUpdate, OnPreCull, OnLateUpdate;

    val i = 1 shl ordinal
}