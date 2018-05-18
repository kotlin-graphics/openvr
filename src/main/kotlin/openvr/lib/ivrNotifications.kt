package openvr.lib

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.IntByReference
import java.util.*

// ivrnotifications.h =============================================================================================================================================

// Used for passing graphic data
open class NotificationBitmap : Structure {

    @JvmField
    var imageData: Pointer? = null
    @JvmField
    var width = 0
    var height = 0
    var bytesPerPixel = 0

    constructor()

    constructor(imageData: Pointer?, width: Int, height: Int, bytesPerPixel: Int) {
        this.imageData = imageData
        this.width = width
        this.height = height
        this.bytesPerPixel = bytesPerPixel
    }

    override fun getFieldOrder()= listOf("imageData", "width", "height", "bytesPerPixel")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : NotificationBitmap(), Structure.ByReference
    class ByValue : NotificationBitmap(), Structure.ByValue
}

/** Be aware that the notification value is used as 'priority' to pick the next notification */
enum class EVRNotificationType(@JvmField val i: Int) {

    /** Transient notifications are automatically hidden after a period of time set by the user.
     *  They are used for things like information and chat messages that do not require user interaction. */
    Transient(0),

    /** Persistent notifications are shown to the user until they are hidden by calling RemoveNotification().
     *  They are used for things like phone calls and alarms that require user interaction. */
    Persistent(1),

    /** System notifications are shown no matter what. It is expected), that the ::userValue is used as ID.
     *  If there is already a system notification in the queue with that ID it is not accepted into the queue to
     *  prevent spamming with system notification */
    Transient_SystemWithUserValue(2);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

enum class EVRNotificationStyle(@JvmField val i: Int) {

    /** Creates a notification with minimal external styling. */
    None(0),

    /** Used for notifications about overlay-level status. In Steam this is used for events like downloads completing. */
    Application(100),

    /** Used for notifications about contacts that are unknown or not available. In Steam this is used for friend
     * invitations and offline friends. */
    Contact_Disabled(200),

    /** Used for notifications about contacts that are available but inactive. In Steam this is used for friends that
     * are online but not playing a game. */
    Contact_Enabled(201),

    /** Used for notifications about contacts that are available and active. In Steam this is used for friends that
     * are online and currently running a game. */
    Contact_Active(202);

    companion object {
        infix fun of(i: Int) = values().first { it.i == i }
    }
}

val k_unNotificationTextMaxSize = 256

typealias VRNotificationId = Int
typealias VRNotificationId_ByReference = IntByReference

/** Allows notification sources to interact with the VR system
This current interface is not yet implemented. Do not use yet. */
open class IVRNotifications : Structure {

    /** Create a notification and enqueue it to be shown to the user.
     *  An overlay handle is required to create a notification, as otherwise it would be impossible for a user to act on it.
     *  To create a two-line notification, use a line break ('\n') to split the text into two lines.
     *  The image argument may be NULL, in which case the specified overlay's icon will be used instead. */
    fun createNotification(overlayHandle: VROverlayHandle, userValue: Long, type: EVRNotificationType, text: String, style: EVRNotificationStyle, image: NotificationBitmap.ByReference, /* out */ notificationId: VRNotificationId_ByReference) = EVRNotificationError.of(CreateNotification!!(overlayHandle, userValue, type.i, text, style.i, image, notificationId))

    @JvmField var CreateNotification: CreateNotification_callback? = null

    interface CreateNotification_callback : Callback {
        operator fun invoke(ulOverlayHandle: VROverlayHandle, ulUserValue: Long, type: Int, pchText: String, style: Int, pImage: NotificationBitmap.ByReference, /* out */ pNotificationId: VRNotificationId_ByReference): Int
    }

    /** Destroy a notification, hiding it first if it currently shown to the user. */
    infix fun removeNotification(notificationId: VRNotificationId) = EVRNotificationError.of(RemoveNotification!!(notificationId))

    @JvmField var RemoveNotification: RemoveNotification_callback? = null

    interface RemoveNotification_callback : Callback {
        operator fun invoke(notificationId: VRNotificationId): Int
    }

    constructor()

    override fun getFieldOrder()= listOf("CreateNotification", "RemoveNotification")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRNotifications(), Structure.ByReference
    class ByValue : IVRNotifications(), Structure.ByValue
}

val IVRNotifications_Version = "FnTable:IVRNotifications_002"