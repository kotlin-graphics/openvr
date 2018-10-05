package lib

import glm_.b
import glm_.set
import kool.Ptr
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.system.Pointer
import java.nio.ByteBuffer
import java.nio.LongBuffer


typealias glSharedTextureHandle = Long
typealias glInt = Int
typealias glUInt = Int


// Handle to a shared texture (HANDLE on Windows obtained using OpenSharedResource).
typealias SharedTextureHandle = Long

const val INVALID_SHARED_TEXTURE_HANDLE: SharedTextureHandle = NULL


typealias DriverId = Int

/** Used to pass device IDs to API calls */
typealias TrackedDeviceIndex = Int


typealias WebConsoleHandle = Long

const val INVALID_WEB_CONSOLE_HANDLE: WebConsoleHandle = NULL


// Refers to a single container of properties
typealias PropertyContainerHandle = Long

typealias PropertyTypeTag = Int


typealias DriverHandle = PropertyContainerHandle


/** used to refer to a single VR overlay */
typealias VROverlayHandle = Long


typealias TrackedCameraHandle = Long

// Screenshot types
typealias ScreenshotHandle = Int


// ivrsystem.h

// ivrapplications.h

// ivrsettings.h

// ivrchaperone.h

// ivrchaperonesetup.h

// ivrcompositor.h

// ivrnotifications.h

typealias VRNotificationId = Int

// ivroverlay.h

// ivrrendermodels.h

typealias VRComponentProperties = Int

/**  Session unique texture identifier. Rendermodels which share the same texture will have the same id.
IDs <0 denote the texture is not present */
typealias TextureId = Int

const val INVALID_TEXTURE_ID: TextureId = -1

// ivrextendeddisplay.h

// ivrtrackedcamera.h

// ivrscreenshots.h

// ivrresources.h

// ivrdrivermanager.h

// ivrinput.h

typealias VRActionHandle = Long
typealias VRActionSetHandle = Long
typealias VRInputValueHandle = Long

// ivriobuffer.h

typealias IOBufferHandle = Long

// Utils

typealias VRActionHandleBuffer = LongBuffer
typealias VRActionSetHandleBuffer = LongBuffer
typealias VRInputValueHandleBuffer = LongBuffer

// temp

inline val Pointer.adr: Ptr
    get() = address()

fun MemoryStack.bufferOfAscii(string: String, nullTerminated: Boolean = true): ByteBuffer {
    val bytes = malloc(string.length + if (nullTerminated) 1 else 0)
    for (i in string.indices)
        bytes[i] = string[i].b
    if (nullTerminated)
        bytes[string.length] = 0
    return bytes
}