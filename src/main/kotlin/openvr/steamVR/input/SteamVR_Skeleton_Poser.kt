package openvr.steamVR.input

import glm_.f
import glm_.glm
import glm_.i
import glm_.quat.Quat
import glm_.vec3.Vec3
import openvr.plugin2.SteamVR_Input_Sources
import openvr.unity.Time

//======= Copyright (c) Valve Corporation, All rights reserved. ===============

class SteamVR_Skeleton_Poser {

    var poseEditorExpanded = true
    var blendEditorExpanded = true
    var poseNames = emptyArray<String>()

//    public GameObject previewLeftHandPrefab
//    public GameObject previewRightHandPrefab

    lateinit var skeletonMainPose: SteamVR_Skeleton_Pose
    val skeletonAdditionalPoses = ArrayList<SteamVR_Skeleton_Pose>()

    protected var showLeftPreview = false

    protected var showRightPreview = true //show the right hand by default

//    protected GameObject previewLeftInstance
//
//    protected GameObject previewRightInstance

    protected var previewPoseSelection = 0

    val blendPoseCount: Int
        get() = blendPoses.size

    val blendingBehaviours = ArrayList<PoseBlendingBehaviour>()

    lateinit var blendedSnapshotL: SteamVR_Skeleton_PoseSnapshot
    lateinit var blendedSnapshotR: SteamVR_Skeleton_PoseSnapshot

    private lateinit var blendPoses: Array<SkeletonBlendablePose>

    private var boneCount = 0

    private var poseUpdatedThisFrame = false

    var scale = 0f


    protected fun awake() {
//        if (previewLeftInstance != null)
//            DestroyImmediate(previewLeftInstance)
//        if (previewRightInstance != null)
//            DestroyImmediate(previewRightInstance)

        blendPoses = Array(skeletonAdditionalPoses.size + 1) {
            SkeletonBlendablePose(getPoseByIndex(it)).apply {
                poseToSnapshots()
            }
        }

        boneCount = skeletonMainPose.leftHand.bonePositions.size
        // NOTE: Is there a better way to get the bone count? idk
        blendedSnapshotL = SteamVR_Skeleton_PoseSnapshot(boneCount, SteamVR_Input_Sources.LeftHand)
        blendedSnapshotR = SteamVR_Skeleton_PoseSnapshot(boneCount, SteamVR_Input_Sources.RightHand)
    }


    /** Set the blending value of a blendingBehaviour. Works best on Manual type behaviours. */
    fun setBlendingBehaviourValue(behaviourName: String, value: Float) {
        val behaviour = blendingBehaviours.find { it.name == behaviourName } ?: run {
            System.err.println("[SteamVR] Blending Behaviour: $behaviourName not found on Skeleton Poser: " /*+ gameObject.name*/)
            return
        }
        if (behaviour.type != PoseBlendingBehaviour.BlenderTypes.Manual)
            System.out.println("[SteamVR] Blending Behaviour: $behaviourName is not a manual behaviour. Its value will likely be overriden.")
        behaviour.value = value
    }

    /** Get the blending value of a blendingBehaviour. */
    fun getBlendingBehaviourValue(behaviourName: String): Float =
            blendingBehaviours.find { it.name == behaviourName }?.value ?: 0f.also {
                System.err.println("[SteamVR] Blending Behaviour: $behaviourName not found on Skeleton Poser: "/* + gameObject.name*/)
            }

    /** Enable or disable a blending behaviour. */
    fun setBlendingBehaviourEnabled(behaviourName: String, value: Boolean) {
        val behaviour = blendingBehaviours.find { it.name == behaviourName } ?: run {
            System.err.println("[SteamVR] Blending Behaviour: $behaviourName not found on Skeleton Poser: "/* + gameObject.name*/)
            return
        }
        behaviour.enabled = value
    }

    /** Check if a blending behaviour is enabled. */
    fun getBlendingBehaviourEnabled(behaviourName: String): Boolean =
            blendingBehaviours.find { it.name == behaviourName }?.enabled ?: false.also {
                System.err.println("[SteamVR] Blending Behaviour: $behaviourName not found on Skeleton Poser: "/* + gameObject.name*/)
            }

    /** Get a blending behaviour by name. */
    fun getBlendingBehaviour(behaviourName: String): PoseBlendingBehaviour? =
            blendingBehaviours.find { it.name == behaviourName } ?: null.also {
                System.err.println("[SteamVR] Blending Behaviour: $behaviourName not found on Skeleton Poser: "/* + gameObject.name */)
            }

    fun getPoseByIndex(index: Int): SteamVR_Skeleton_Pose = when (index) {
        0 -> skeletonMainPose
        else -> skeletonAdditionalPoses[index - 1]
    }

    private fun getHandSnapshot(inputSource: SteamVR_Input_Sources): SteamVR_Skeleton_PoseSnapshot = when (inputSource) {
        SteamVR_Input_Sources.LeftHand -> blendedSnapshotL
        else -> blendedSnapshotR
    }

    /** Retrieve the final animated pose, to be applied to a hand skeleton
     *
     *  @param forAction: The skeleton action you want to blend between
     *  @param handType: If this is for the left or right hand */
    fun getBlendedPose(skeletonAction: SteamVR_Action_Skeleton, handType: SteamVR_Input_Sources): SteamVR_Skeleton_PoseSnapshot {
        updatePose(skeletonAction, handType)
        return getHandSnapshot(handType)
    }

    /** Retrieve the final animated pose, to be applied to a hand skeleton
     *
     *  @param skeletonBehaviour: The skeleton behaviour you want to get the action/input source from to blend between */
    fun getBlendedPose(skeletonBehaviour: SteamVR_Behaviour_Skeleton): SteamVR_Skeleton_PoseSnapshot =
            getBlendedPose(skeletonBehaviour.skeletonAction!!, skeletonBehaviour.inputSource!!)


    /** Updates all pose animation and blending. Can be called from different places without performance concerns, as it will only let itself run once per frame. */
    fun updatePose(skeletonAction: SteamVR_Action_Skeleton, inputSource: SteamVR_Input_Sources) {
        // only allow this function to run once per frame
        if (poseUpdatedThisFrame)
            return

        poseUpdatedThisFrame = true

        // always do additive animation on main pose
        blendPoses[0].updateAdditiveAnimation(skeletonAction, inputSource)

        //copy from main pose as a base
        val snap = getHandSnapshot(inputSource)
        snap.copyFrom(blendPoses[0].getHandSnapshot(inputSource))

        applyBlenderBehaviours(skeletonAction, inputSource, snap)


        when (inputSource) {
            SteamVR_Input_Sources.RightHand -> blendedSnapshotR = snap
            SteamVR_Input_Sources.LeftHand -> blendedSnapshotL = snap
        }
    }

    protected fun applyBlenderBehaviours(skeletonAction: SteamVR_Action_Skeleton, inputSource: SteamVR_Input_Sources,
                                         snapshot: SteamVR_Skeleton_PoseSnapshot) {
        // apply blending for each behaviour
        for (behaviourIndex in blendingBehaviours.indices) {
            blendingBehaviours[behaviourIndex].update(Time.deltaTime, inputSource)
            // if disabled or very low influence, skip for perf
            if (blendingBehaviours[behaviourIndex].enabled && blendingBehaviours[behaviourIndex].influence * blendingBehaviours[behaviourIndex].value > 0.01f) {
                if (blendingBehaviours[behaviourIndex].pose != 0) {
                    // update additive animation only as needed
                    blendPoses[blendingBehaviours[behaviourIndex].pose].updateAdditiveAnimation(skeletonAction, inputSource)
                }

                blendingBehaviours[behaviourIndex].applyBlending(snapshot, blendPoses, inputSource)
            }
        }
    }

    protected fun lateUpdate() {
        // let the pose be updated again the next frame
        poseUpdatedThisFrame = false
    }

    /** Weighted average of n vector3s */
    protected fun blendVectors(vectors: Array<Vec3>, weights: FloatArray): Vec3 {
        val blendedVector = Vec3()
        for (i in vectors.indices)
            blendedVector += vectors[i] * weights[i]
        return blendedVector
    }

    /** Weighted average of n quaternions */
    protected fun blendQuaternions(quaternions: Array<Quat>, weights: FloatArray): Quat {
        val outquat = Quat()
        for (i in quaternions.indices)
            outquat *= glm.slerp(Quat(), quaternions[i], weights[i])
        return outquat
    }

    /** A SkeletonBlendablePose holds a reference to a Skeleton_Pose scriptableObject, and also contains some helper functions.
     *  Also handles pose-specific animation like additive finger motion. */
    class SkeletonBlendablePose {
        lateinit var pose: SteamVR_Skeleton_Pose
        lateinit var snapshotR: SteamVR_Skeleton_PoseSnapshot
        lateinit var snapshotL: SteamVR_Skeleton_PoseSnapshot

        /** Get the snapshot of this pose with effects such as additive finger animation applied. */
        fun getHandSnapshot(inputSource: SteamVR_Input_Sources): SteamVR_Skeleton_PoseSnapshot = when (inputSource) {
            SteamVR_Input_Sources.LeftHand -> snapshotL
            else -> snapshotR
        }

        //buffers for mirrored poses
        private lateinit var additivePositionBuffer: Array<Vec3>
        private lateinit var additiveRotationBuffer: Array<Quat>

        fun updateAdditiveAnimation(skeletonAction: SteamVR_Action_Skeleton, inputSource: SteamVR_Input_Sources) {

            val snapshot = getHandSnapshot(inputSource)
            val poseHand = pose.getHand(inputSource)!!

            //setup mirrored pose buffers
            if (!::additivePositionBuffer.isInitialized) additivePositionBuffer = Array(skeletonAction.boneCount) { Vec3() }
            if (!::additiveRotationBuffer.isInitialized) additiveRotationBuffer = Array(skeletonAction.boneCount) { Quat() }

            for (boneIndex in snapshotL.bonePositions.indices) {

                val fingerIndex = JointIndex.getFingerForBone(boneIndex)
                val extensionType = poseHand.getMovementTypeForBone(boneIndex)// lerp to closed pose by fingercurl

                //do target pose mirroring on left hand
                if (inputSource == SteamVR_Input_Sources.LeftHand) {
                    SteamVR_Behaviour_Skeleton.mirrorBonePosition(skeletonAction.bonePositions[boneIndex], additivePositionBuffer[boneIndex], boneIndex)
                    SteamVR_Behaviour_Skeleton.mirrorBoneRotation(skeletonAction.boneRotations[boneIndex], additiveRotationBuffer[boneIndex], boneIndex)
                } else {
                    additivePositionBuffer[boneIndex] = skeletonAction.bonePositions[boneIndex];
                    additiveRotationBuffer[boneIndex] = skeletonAction.boneRotations[boneIndex];
                }


                // lerp to open pose by fingercurl
                when (extensionType) {
                    SteamVR_Skeleton_FingerExtensionTypes.Free -> {
                        snapshot.bonePositions[boneIndex] put additivePositionBuffer[boneIndex]
                        snapshot.boneRotations[boneIndex] put additiveRotationBuffer[boneIndex]
                    }
                    SteamVR_Skeleton_FingerExtensionTypes.Extend -> {
                        // lerp to open pose by fingercurl
                        snapshot.bonePositions[boneIndex] = glm.mix(poseHand.bonePositions[boneIndex], additivePositionBuffer[boneIndex], 1 - skeletonAction.fingerCurls[fingerIndex])
                        snapshot.boneRotations[boneIndex] = glm.lerp(poseHand.boneRotations[boneIndex], additiveRotationBuffer[boneIndex], 1 - skeletonAction.fingerCurls[fingerIndex])
                    }
                    SteamVR_Skeleton_FingerExtensionTypes.Contract -> {
                        // lerp to closed pose by fingercurl
                        snapshot.bonePositions[boneIndex] = glm.mix(poseHand.bonePositions[boneIndex], additivePositionBuffer[boneIndex], skeletonAction.fingerCurls[fingerIndex])
                        snapshot.boneRotations[boneIndex] = glm.lerp(poseHand.boneRotations[boneIndex], additiveRotationBuffer[boneIndex], skeletonAction.fingerCurls[fingerIndex])
                    }
                }
            }
        }

        /** Init based on an existing Skeleton_Pose */
        constructor(p: SteamVR_Skeleton_Pose) {
            pose = p
            snapshotR = SteamVR_Skeleton_PoseSnapshot(p.rightHand.bonePositions.size, SteamVR_Input_Sources.RightHand)
            snapshotL = SteamVR_Skeleton_PoseSnapshot(p.leftHand.bonePositions.size, SteamVR_Input_Sources.LeftHand)
        }

        /** Copy the base pose into the snapshots. */
        fun poseToSnapshots() {
            snapshotR.position put pose.rightHand.position
            snapshotR.rotation put pose.rightHand.rotation
            for (i in snapshotR.bonePositions.indices)
                snapshotR.bonePositions[i] put pose.rightHand.bonePositions[i]
            for (i in snapshotR.boneRotations.indices)
                snapshotR.boneRotations[i] = pose.rightHand.boneRotations[i]

            snapshotL.position put pose.leftHand.position
            snapshotL.rotation put pose.leftHand.rotation
            for (i in snapshotL.bonePositions.indices)
                snapshotL.bonePositions[i] = pose.leftHand.bonePositions[i]
            for (i in snapshotL.boneRotations.indices)
                snapshotL.boneRotations[i] = pose.leftHand.boneRotations[i]
        }

        constructor()
    }

    /** A filter applied to the base pose. Blends to a secondary pose by a certain weight. Can be masked per-finger */
    class PoseBlendingBehaviour {
        lateinit var name: String
        var enabled = true
        var influence = 1f
        var pose = 1
        var value = 0f
        lateinit var action_single: SteamVR_Action_Single
        lateinit var action_bool: SteamVR_Action_Boolean
        var smoothingSpeed = 0f
        lateinit var type: BlenderTypes
        var useMask = false
        val mask = SteamVR_Skeleton_HandMask()

        var previewEnabled = false

        /** Performs smoothing based on deltaTime parameter. */
        fun update(deltaTime: Float, inputSource: SteamVR_Input_Sources) {
            when (type) {
                BlenderTypes.AnalogAction -> value = when (smoothingSpeed) {
                    0f -> action_single.getAxis(inputSource)
                    else -> glm.mix(value, action_single.getAxis(inputSource), deltaTime * smoothingSpeed)
                }
                BlenderTypes.BooleanAction -> value = when (smoothingSpeed) {
                    0f -> action_bool.getState(inputSource).f
                    else -> glm.mix(value, action_bool.getState(inputSource).f, deltaTime * smoothingSpeed)
                }
            }
        }

        /** Apply blending to this behaviour's pose to an existing snapshot.
         *
         *  @param snapshot: Snapshot to modify
         *  @param blendPoses: List of blend poses to get the target pose
         *  @param inputSource: Which hand to receive input from */
        fun applyBlending(snapshot: SteamVR_Skeleton_PoseSnapshot, blendPoses: Array<SkeletonBlendablePose>, inputSource: SteamVR_Input_Sources) {
            val targetSnapshot = blendPoses[pose].getHandSnapshot(inputSource)
            if (mask[0] || !useMask) {
                snapshot.position put glm.mix(snapshot.position, targetSnapshot.position, influence * value)
                snapshot.rotation put glm.slerp(snapshot.rotation, targetSnapshot.rotation, influence * value)
            }

            for (boneIndex in snapshot.bonePositions.indices)
            // verify the current finger is enabled in the mask, or if no mask is used.
                if (mask[JointIndex.getFingerForBone(boneIndex) + 1] || !useMask) {
                    snapshot.bonePositions[boneIndex] = glm.mix(snapshot.bonePositions[boneIndex], targetSnapshot.bonePositions[boneIndex], influence * value)
                    snapshot.boneRotations[boneIndex] = glm.slerp(snapshot.boneRotations[boneIndex], targetSnapshot.boneRotations[boneIndex], influence * value)
                }
        }

        fun poseBlendingBehaviour() {
            enabled = true
            influence = 1f
        }

        enum class BlenderTypes { Manual, AnalogAction, BooleanAction }
    }
}

/** PoseSnapshots hold a skeleton pose for one hand, as well as storing which hand they contain.
 *  They have several functions for combining BlendablePoses. */
class SteamVR_Skeleton_PoseSnapshot(boneCount: Int, var inputSource: SteamVR_Input_Sources) {

    val position = Vec3()
    val rotation = Quat()

    val bonePositions = Array(boneCount) { Vec3() }
    val boneRotations = Array(boneCount) { Quat() }

    /** Perform a deep copy from one poseSnapshot to another. */
    fun copyFrom(source: SteamVR_Skeleton_PoseSnapshot) {
        inputSource = source.inputSource
        position put source.position
        rotation put source.rotation
        for (boneIndex in bonePositions.indices) {
            bonePositions[boneIndex] put source.bonePositions[boneIndex]
            boneRotations[boneIndex] put source.boneRotations[boneIndex]
        }
    }
}

/** Simple mask for fingers */
class SteamVR_Skeleton_HandMask {
    var palm = false
    var thumb = false
    var index = false
    var middle = false
    var ring = false
    var pinky = false
    val values = BooleanArray(6)

    operator fun set(i: Int, value: Boolean) {
        values[i] = value
        apply()
    }

    operator fun get(i: Int): Boolean = values[i]

    init {
        reset()
    }

    /** All elements on */
    fun reset() {
        values.fill(true)
        apply()
    }

    protected fun apply() {
        palm = values[0]
        thumb = values[1]
        index = values[2]
        middle = values[3]
        ring = values[4]
        pinky = values[5]
    }

    companion object {
        val fullMask = SteamVR_Skeleton_HandMask()
    }
}