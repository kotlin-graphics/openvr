import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Created by GBarbieri on 07.10.2016.
 */

fun main(args: Array<String>) {

    var err = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder())

    Native().VR_Init(err, EVRApplicationType.EVRApplicationType_VRApplication_Scene.i)
}