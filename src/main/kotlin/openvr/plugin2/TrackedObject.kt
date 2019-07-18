package openvr.plugin2

import openvr.lib.*
import org.lwjgl.openvr.TrackedDevicePose

class TrackedObject {

    enum class Index(val i: TrackedDeviceIndex) {
        None(-1),
        Hmd(vr.trackedDeviceIndex_Hmd),
        Device1(1),
        Device2(2),
        Device3(3),
        Device4(4),
        Device5(5),
        Device6(6),
        Device7(7),
        Device8(8),
        Device9(9),
        Device10(10),
        Device11(11),
        Device12(12),
        Device13(13),
        Device14(14),
        Device15(15);
    }

    var index = Index.None

    /** If not set, relative to parent */
//    public Transform origin;

    var isValid = false

    fun onNewPoses(poses: TrackedDevicePose.Buffer) {
        if (index == Index.None)
            return

        val i = index.i

        isValid = false
        if (poses.rem <= i)
            return

        if (!poses[i].deviceIsConnected)
            return

        if (!poses[i].poseIsValid)
            return

        isValid = true

        val pose = RigidTransform2(poses[i].mDeviceToAbsoluteTracking())

//        if (origin != null) {
//            transform.position = origin.transform.TransformPoint(pose.pos)
//            transform.rotation = origin.rotation * pose.rot
//        } else {
//            transform.localPosition = pose.pos
//            transform.localRotation = pose.rot
//        }
    }

//    var newPosesAction: Events.Action = Events.NewPosesAction(OnNewPoses)

    fun awake() = onEnable()

    fun onEnable() {
//        val render = VR_Render.instance
//        if (render == null) {
//            enabled = false
//            return
//        }
//
//        newPosesAction.enabled = true
    }

    fun onDisable() {
//        newPosesAction.enabled = false
        isValid = false
    }

    fun setDeviceIndex(index: Int) {
        Index.values().find { it.i == index }?.let { this.index = it }
    }
}