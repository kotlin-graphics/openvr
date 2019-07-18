package openvr.steamVR_Input

import openvr.steamVR.input.*
import openvr.steamVR_Input.actionSetClasses.SteamVR_Input_ActionSet_buggy
import openvr.steamVR_Input.actionSetClasses.SteamVR_Input_ActionSet_default
import openvr.steamVR_Input.actionSetClasses.SteamVR_Input_ActionSet_mixedreality
import openvr.steamVR_Input.actionSetClasses.SteamVR_Input_ActionSet_platformer

object SteamVR_Actions {

    // SteamVR_Input_Initialization.cs

    fun preInitialize() {
        startPreInitActionSets()
        SteamVR_Input.preinitializeActionSetDictionaries()
        preInitActions()
        initializeActionArrays()
        SteamVR_Input.preinitializeActionDictionaries()
        SteamVR_Input.preinitializeFinishActionSets()
    }

    // SteamVR_Input_ActionSets.cs

    private lateinit var p__default: SteamVR_Input_ActionSet_default

    private lateinit var p_platformer: SteamVR_Input_ActionSet_platformer

    private lateinit var p_buggy: SteamVR_Input_ActionSet_buggy

    private lateinit var p_mixedreality: SteamVR_Input_ActionSet_mixedreality

    val _default: SteamVR_Input_ActionSet_default
        get() = p__default.getCopy()

    val platformer: SteamVR_Input_ActionSet_platformer
        get() = p_platformer.getCopy()

    val buggy: SteamVR_Input_ActionSet_buggy
        get() = p_buggy.getCopy()

    val mixedreality: SteamVR_Input_ActionSet_mixedreality
        get() = p_mixedreality.getCopy()

    private fun startPreInitActionSets() {
        p__default = SteamVR_ActionSet.create(SteamVR_Input_ActionSet_default::class.java, "/actions/default")
        p_platformer = SteamVR_ActionSet.create(SteamVR_Input_ActionSet_platformer::class.java, "/actions/platformer")
        p_buggy = SteamVR_ActionSet.create(SteamVR_Input_ActionSet_buggy::class.java, "/actions/buggy")
        p_mixedreality = SteamVR_ActionSet.create(SteamVR_Input_ActionSet_mixedreality::class.java, "/actions/mixedreality")
        SteamVR_Input.actionSets = arrayOf(_default, platformer, buggy, mixedreality)
    }

    // SteamVR_Input_Actions.cs

    private lateinit var p_default_InteractUI: SteamVR_Action_Boolean
    private lateinit var p_default_Teleport: SteamVR_Action_Boolean
    private lateinit var p_default_GrabPinch: SteamVR_Action_Boolean
    private lateinit var p_default_GrabGrip: SteamVR_Action_Boolean
    private lateinit var p_default_Pose: SteamVR_Action_Pose
    private lateinit var p_default_SkeletonLeftHand: SteamVR_Action_Skeleton
    private lateinit var p_default_SkeletonRightHand: SteamVR_Action_Skeleton
    private lateinit var p_default_Squeeze: SteamVR_Action_Single
    private lateinit var p_default_HeadsetOnHead: SteamVR_Action_Boolean
    private lateinit var p_default_Haptic: SteamVR_Action_Vibration
    private lateinit var p_platformer_Move: SteamVR_Action_Vector2
    private lateinit var p_platformer_Jump: SteamVR_Action_Boolean
    private lateinit var p_buggy_Steering: SteamVR_Action_Vector2
    private lateinit var p_buggy_Throttle: SteamVR_Action_Single
    private lateinit var p_buggy_Brake: SteamVR_Action_Boolean
    private lateinit var p_buggy_Reset: SteamVR_Action_Boolean
    private lateinit var p_mixedreality_ExternalCamera: SteamVR_Action_Pose

    val default_InteractUI: SteamVR_Action_Boolean
        get() = p_default_InteractUI.getCopy()

    val default_Teleport: SteamVR_Action_Boolean
        get() = p_default_Teleport.getCopy()

    val default_GrabPinch: SteamVR_Action_Boolean
        get() = p_default_GrabPinch.getCopy()

    val default_GrabGrip: SteamVR_Action_Boolean
        get() = p_default_GrabGrip.getCopy()

    val default_Pose: SteamVR_Action_Pose
        get() = p_default_Pose.getCopy()

    val default_SkeletonLeftHand: SteamVR_Action_Skeleton
        get() = p_default_SkeletonLeftHand.getCopy()

    val default_SkeletonRightHand: SteamVR_Action_Skeleton
        get() = p_default_SkeletonRightHand.getCopy()

    val default_Squeeze: SteamVR_Action_Single
        get() = p_default_Squeeze.getCopy()

    val default_HeadsetOnHead: SteamVR_Action_Boolean
        get() = p_default_HeadsetOnHead.getCopy()

    val default_Haptic: SteamVR_Action_Vibration
        get() = p_default_Haptic.getCopy()

    val platformer_Move: SteamVR_Action_Vector2
        get() = p_platformer_Move.getCopy()

    val platformer_Jump: SteamVR_Action_Boolean
        get() = p_platformer_Jump.getCopy()

    val buggy_Steering: SteamVR_Action_Vector2
        get() = p_buggy_Steering.getCopy()

    val buggy_Throttle: SteamVR_Action_Single
        get() = p_buggy_Throttle.getCopy()

    val buggy_Brake: SteamVR_Action_Boolean
        get() = p_buggy_Brake.getCopy()

    val buggy_Reset: SteamVR_Action_Boolean
        get() = p_buggy_Reset.getCopy()

    val mixedreality_ExternalCamera: SteamVR_Action_Pose
        get() = p_mixedreality_ExternalCamera.getCopy()

    private fun initializeActionArrays() {
        SteamVR_Input.actions = arrayOf(
                default_InteractUI,
                default_Teleport,
                default_GrabPinch,
                default_GrabGrip,
                default_Pose,
                default_SkeletonLeftHand,
                default_SkeletonRightHand,
                default_Squeeze,
                default_HeadsetOnHead,
                default_Haptic,
                platformer_Move,
                platformer_Jump,
                buggy_Steering,
                buggy_Throttle,
                buggy_Brake,
                buggy_Reset,
                mixedreality_ExternalCamera)

        SteamVR_Input.actionsIn = arrayOf(
                default_InteractUI,
                default_Teleport,
                default_GrabPinch,
                default_GrabGrip,
                default_Pose,
                default_SkeletonLeftHand,
                default_SkeletonRightHand,
                default_Squeeze,
                default_HeadsetOnHead,
                platformer_Move,
                platformer_Jump,
                buggy_Steering,
                buggy_Throttle,
                buggy_Brake,
                buggy_Reset,
                mixedreality_ExternalCamera)

        SteamVR_Input.actionsOut = arrayOf(default_Haptic)

        SteamVR_Input.actionsVibration = arrayOf(default_Haptic)

        SteamVR_Input.actionsPose = arrayOf(default_Pose, mixedreality_ExternalCamera)

        SteamVR_Input.actionsBoolean = arrayOf(
                default_InteractUI,
                default_Teleport,
                default_GrabPinch,
                default_GrabGrip,
                default_HeadsetOnHead,
                platformer_Jump,
                buggy_Brake,
                buggy_Reset)

        SteamVR_Input.actionsSingle = arrayOf(default_Squeeze, buggy_Throttle)

        SteamVR_Input.actionsVector2 = arrayOf(platformer_Move, buggy_Steering)

        SteamVR_Input.actionsVector3 = emptyArray()

        SteamVR_Input.actionsSkeleton = arrayOf(default_SkeletonLeftHand, default_SkeletonRightHand)

        SteamVR_Input.actionsNonPoseNonSkeletonIn = arrayOf(
                default_InteractUI,
                default_Teleport,
                default_GrabPinch,
                default_GrabGrip,
                default_Squeeze,
                default_HeadsetOnHead,
                platformer_Move,
                platformer_Jump,
                buggy_Steering,
                buggy_Throttle,
                buggy_Brake,
                buggy_Reset)
    }

    private fun preInitActions() {
        p_default_InteractUI = SteamVR_Action.create(ActionType.Boolean, "/actions/default/in/InteractUI")
        p_default_Teleport = SteamVR_Action.create(ActionType.Boolean, "/actions/default/in/Teleport")
        p_default_GrabPinch = SteamVR_Action.create(ActionType.Boolean, "/actions/default/in/GrabPinch")
        p_default_GrabGrip = SteamVR_Action.create(ActionType.Boolean, "/actions/default/in/GrabGrip")
        p_default_Pose = SteamVR_Action.create(ActionType.Pose, "/actions/default/in/Pose")
        p_default_SkeletonLeftHand = SteamVR_Action.create(ActionType.Skeleton, "/actions/default/in/SkeletonLeftHand")
        p_default_SkeletonRightHand = SteamVR_Action.create(ActionType.Skeleton, "/actions/default/in/SkeletonRightHand")
        p_default_Squeeze = SteamVR_Action.create(ActionType.Single, "/actions/default/in/Squeeze")
        p_default_HeadsetOnHead = SteamVR_Action.create(ActionType.Boolean, "/actions/default/in/HeadsetOnHead")
        p_default_Haptic = SteamVR_Action.create(ActionType.Vibration, "/actions/default/out/Haptic")
        p_platformer_Move = SteamVR_Action.create(ActionType.Vector2, "/actions/platformer/in/Move")
        p_platformer_Jump = SteamVR_Action.create(ActionType.Boolean, "/actions/platformer/in/Jump")
        p_buggy_Steering = SteamVR_Action.create(ActionType.Vector2, "/actions/buggy/in/Steering")
        p_buggy_Throttle = SteamVR_Action.create(ActionType.Single, "/actions/buggy/in/Throttle")
        p_buggy_Brake = SteamVR_Action.create(ActionType.Boolean, "/actions/buggy/in/Brake")
        p_buggy_Reset = SteamVR_Action.create(ActionType.Boolean, "/actions/buggy/in/Reset")
        p_mixedreality_ExternalCamera = SteamVR_Action.create(ActionType.Pose, "/actions/mixedreality/in/ExternalCamera")
    }
}