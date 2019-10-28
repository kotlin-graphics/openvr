package openvr.assets.steamVR.input

import glm_.quat.Quat
import glm_.vec3.Vec3
import openvr.plugin2.SteamVR_Input_Sources

//======= Copyright (c) Valve Corporation, All rights reserved. ===============

class SteamVR_Skeleton_Pose {

    val leftHand = SteamVR_Skeleton_Pose_Hand(SteamVR_Input_Sources.LeftHand)
    val rightHand = SteamVR_Skeleton_Pose_Hand(SteamVR_Input_Sources.RightHand)

    protected val leftHandInputSource = SteamVR_Input_Sources.LeftHand.ordinal
    protected val rightHandInputSource = SteamVR_Input_Sources.RightHand.ordinal

    fun getHand(hand: Int): SteamVR_Skeleton_Pose_Hand? = when (hand) {
        leftHandInputSource -> leftHand
        rightHandInputSource -> rightHand
        else -> null
    }

    fun getHand(hand: SteamVR_Input_Sources): SteamVR_Skeleton_Pose_Hand? = getHand(hand.ordinal)
}

class SteamVR_Skeleton_Pose_Hand(val inputSource: SteamVR_Input_Sources) {

    val thumbFingerMovementType = SteamVR_Skeleton_FingerExtensionTypes.Static
    val indexFingerMovementType = SteamVR_Skeleton_FingerExtensionTypes.Static
    val middleFingerMovementType = SteamVR_Skeleton_FingerExtensionTypes.Static
    val ringFingerMovementType = SteamVR_Skeleton_FingerExtensionTypes.Static
    val pinkyFingerMovementType = SteamVR_Skeleton_FingerExtensionTypes.Static

    /** Get extension type for a particular finger. Thumb is 0, Index is 1, etc. */
    fun getFingerExtensionType(finger: Int): SteamVR_Skeleton_FingerExtensionTypes = when (finger) {
        0 -> thumbFingerMovementType
        1 -> indexFingerMovementType
        2 -> middleFingerMovementType
        3 -> ringFingerMovementType
        4 -> pinkyFingerMovementType
        else -> SteamVR_Skeleton_FingerExtensionTypes.Static.also {
            System.err.println("Finger not in range!")
        }
    }

    var ignoreRootPoseData = true
    var ignoreWristPoseData = true

    val position = Vec3()
    val rotation = Quat()

    var bonePositions = emptyArray<Vec3>()
    val boneRotations = emptyArray<Quat>()

    fun getMovementTypeForBone(boneIndex: Int): SteamVR_Skeleton_FingerExtensionTypes {
        val fingerIndex = JointIndex.getFingerForBone(boneIndex)

        return when (fingerIndex) {
            FingerIndex.thumb.i -> thumbFingerMovementType
            FingerIndex.index.i -> indexFingerMovementType
            FingerIndex.middle.i -> middleFingerMovementType
            FingerIndex.ring.i -> ringFingerMovementType
            FingerIndex.pinky.i -> pinkyFingerMovementType
            else -> SteamVR_Skeleton_FingerExtensionTypes.Static
        }
    }
}

enum class SteamVR_Skeleton_FingerExtensionTypes { Static, Free, Extend, Contract }

class SteamVR_Skeleton_FingerExtensionTypeLists {
    val enumList by lazy { SteamVR_Skeleton_FingerExtensionTypes.values() }
    val stringList by lazy { enumList.map { it.name } }
}