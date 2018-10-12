package openvr.plugin

import glm_.mat4x4.Mat4
import glm_.quat.Quat
import glm_.vec3.Vec3
import lib.m
import org.lwjgl.openvr.HmdMatrix34
import org.lwjgl.openvr.HmdMatrix44

object Utils {

    class RigidTransform {

        val pos = Vec3()
        val rot = Quat()

        constructor()

        constructor(pos: Vec3, rot: Quat) {
            this.pos put pos
            this.rot put rot
        }

        constructor(pose: HmdMatrix34) {

            val m = Mat4()

            m[0, 0] = +pose.m[0]
            m[0, 1] = +pose.m[1]
            m[0, 2] = -pose.m[2]
            m[0, 3] = +pose.m[3]

            m[1, 0] = +pose.m[4]
            m[1, 1] = +pose.m[5]
            m[1, 2] = -pose.m[6]
            m[1, 3] = +pose.m[7]

            m[2, 0] = -pose.m[8]
            m[2, 1] = -pose.m[9]
            m[2, 2] = +pose.m[10]
            m[2, 3] = -pose.m[11]

            pos put m.position
            rot put m.toQuat()
        }

        constructor(pose: HmdMatrix44) {

            val m = Mat4()

            m[0, 0] = +pose.m[0]
            m[0, 1] = +pose.m[1]
            m[0, 2] = -pose.m[2]
            m[0, 3] = +pose.m[3]

            m[1, 0] = +pose.m[4]
            m[1, 1] = +pose.m[5]
            m[1, 2] = -pose.m[6]
            m[1, 3] = +pose.m[7]

            m[2, 0] = -pose.m[8]
            m[2, 1] = -pose.m[9]
            m[2, 2] = +pose.m[10]
            m[2, 3] = -pose.m[11]

            m[3, 0] = +pose.m[12]
            m[3, 1] = +pose.m[13]
            m[3, 2] = -pose.m[14]
            m[3, 3] = +pose.m[15]

            pos put m.position
            rot put m.toQuat()
        }

        fun toHmdMatrix44(): HmdMatrix44 {
            TODO()
//            val m = Matrix4x4.TRS(pos, rot, Vector3.one)
//            var pose = new HmdMat44 ()
//
//            pose.m0 = m[0, 0]
//            pose.m1 = m[0, 1]
//            pose.m2 = -m[0, 2]
//            pose.m3 = m[0, 3]
//
//            pose.m4 = m[1, 0]
//            pose.m5 = m[1, 1]
//            pose.m6 = -m[1, 2]
//            pose.m7 = m[1, 3]
//
//            pose.m8 = -m[2, 0]
//            pose.m9 = -m[2, 1]
//            pose.m10 = m[2, 2]
//            pose.m11 = -m[2, 3]
//
//            pose.m12 = m[3, 0]
//            pose.m13 = m[3, 1]
//            pose.m14 = -m[3, 2]
//            pose.m15 = m[3, 3]
//
//            return pose
        }

        fun toHmdMatrix34(): HmdMatrix34 {
            TODO()
//            var m = Matrix4x4.TRS(pos, rot, Vector3.one)
//            var pose = new HmdMat34 ()
//
//            pose.m0 = m[0, 0]
//            pose.m1 = m[0, 1]
//            pose.m2 = -m[0, 2]
//            pose.m3 = m[0, 3]
//
//            pose.m4 = m[1, 0]
//            pose.m5 = m[1, 1]
//            pose.m6 = -m[1, 2]
//            pose.m7 = m[1, 3]
//
//            pose.m8 = -m[2, 0]
//            pose.m9 = -m[2, 1]
//            pose.m10 = m[2, 2]
//            pose.m11 = -m[2, 3]
//
//            return pose
        }

        override fun equals(other: Any?) = if (other is RigidTransform) pos == other.pos && rot == other.rot else false
        override fun hashCode() = pos.hashCode() xor rot.hashCode()
        operator fun times(b: RigidTransform) = RigidTransform(pos + rot * b.pos, rot * b.rot)
        fun inverse_(): RigidTransform {
            rot.inverseAssign()
            pos put (rot * pos).negateAssign()
            return this
        }

        fun inverse(res: RigidTransform = RigidTransform()) = res.put(pos, rot).inverse_()

        fun put(pos: Vec3, quat: Quat): RigidTransform {
            this.pos put pos
            this.rot put rot
            return this
        }

        infix fun put(pose: HmdMatrix34): RigidTransform {

            val m = Mat4()

            m[0, 0] = +pose.m[0]
            m[0, 1] = +pose.m[1]
            m[0, 2] = -pose.m[2]
            m[0, 3] = +pose.m[3]

            m[1, 0] = +pose.m[4]
            m[1, 1] = +pose.m[5]
            m[1, 2] = -pose.m[6]
            m[1, 3] = +pose.m[7]

            m[2, 0] = -pose.m[8]
            m[2, 1] = -pose.m[9]
            m[2, 2] = +pose.m[10]
            m[2, 3] = -pose.m[11]

            pos put m.position
            rot put m.toQuat()

            return this
        }

        infix fun put(m: Mat4): RigidTransform {
//        infix fun put(pose: HmdMatrix44): RigidTransform {
//
//            val m = Mat4()
//
//            m[0, 0] = +pose.m[0]
//            m[0, 1] = +pose.m[1]
//            m[0, 2] = -pose.m[2]
//            m[0, 3] = +pose.m[3]
//
//            m[1, 0] = +pose.m[4]
//            m[1, 1] = +pose.m[5]
//            m[1, 2] = -pose.m[6]
//            m[1, 3] = +pose.m[7]
//
//            m[2, 0] = -pose.m[8]
//            m[2, 1] = -pose.m[9]
//            m[2, 2] = +pose.m[10]
//            m[2, 3] = -pose.m[11]
//
//            m[3, 0] = +pose.m[12]
//            m[3, 1] = +pose.m[13]
//            m[3, 2] = -pose.m[14]
//            m[3, 3] = +pose.m[15]

            pos put m.position
            rot put m.toQuat()

            return this
        }

        fun inverseTransformPoint(point: Vec3) = rot.inverse() * (point - pos)
        fun transformPoint(point: Vec3) = pos + (rot * point)
        operator fun times(v: Vec3) = transformPoint(v)

        fun interpolate(b: RigidTransform, t: Float): Nothing = TODO()//RigidTransform (Vec3.lerp(a.pos, b.pos, t), Quaternion.Slerp(a.rot, b.rot, t))
    }
}

val Mat4.position get() = Vec3(this[0, 3], this[1, 3], this[2, 3])