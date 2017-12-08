package openvr.lib

import com.sun.jna.Callback
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.IntByReference
import java.util.*

// ivrnotifications.h =============================================================================================================================================

// Used for passing graphic data
open class NotificationBitmap_t : Structure {

    var m_pImageData: Pointer? = null
    var m_nWidth = 0
    var m_nHeight = 0
    var m_nBytesPerPixel = 0

    constructor()

    constructor(m_pImageData: Pointer?, m_nWidth: Int, m_nHeight: Int, m_nBytesPerPixel: Int) {
        this.m_pImageData = m_pImageData
        this.m_nWidth = m_nWidth
        this.m_nHeight = m_nHeight
        this.m_nBytesPerPixel = m_nBytesPerPixel
    }

    override fun getFieldOrder(): List<String> = Arrays.asList("m_pImageData", "m_nWidth", "m_nHeight", "m_nBytesPerPixel")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : NotificationBitmap_t(), Structure.ByReference
    class ByValue : NotificationBitmap_t(), Structure.ByValue
}

/** Be aware that the notification value is used as 'priority' to pick the next notification */
enum class EVRNotificationType(@JvmField val i: Int) {

    /** Transient notifications are automatically hidden after a period of time set by the user.
     *  They are used for things like information and chat messages that do not require user interaction. */
    Transient(0),

    /** Persistent notifications are shown to the user until they are hidden by calling RemoveNotification().
     *  They are used for things like phone calls and alarms that require user interaction. */
    Persistent(1),

    /** System notifications are shown no matter what. It is expected), that the ulUserValue is used as ID.
     *  If there is already a system notification in the queue with that ID it is not accepted into the queue to
     *  prevent spamming with system notification */
    Transient_SystemWithUserValue(2);

    companion object {
        fun of(i: Int) = values().first { it.i == i }
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
        fun of(i: Int) = values().first { it.i == i }
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
     *  The pImage argument may be NULL, in which case the specified overlay's icon will be used instead. */
    fun createNotification(ulOverlayHandle: VROverlayHandle, ulUserValue: Long, type: EVRNotificationType, pchText: String, style: EVRNotificationStyle,
                           pImage: NotificationBitmap_t.ByReference, /* out */ pNotificationId: VRNotificationId_ByReference)
            = EVRNotificationError.of(CreateNotification!!.invoke(ulOverlayHandle, ulUserValue, type.i, pchText, style.i, pImage, pNotificationId))

    @JvmField var CreateNotification: CreateNotification_callback? = null

    interface CreateNotification_callback : Callback {
        fun invoke(ulOverlayHandle: VROverlayHandle, ulUserValue: Long, type: Int, pchText: String, style: Int, pImage: NotificationBitmap_t.ByReference,
                /* out */ pNotificationId: VRNotificationId_ByReference): Int
    }

    /** Destroy a notification, hiding it first if it currently shown to the user. */
    fun removeNotification(notificationId: VRNotificationId) = EVRNotificationError.of(RemoveNotification!!.invoke(notificationId))

    @JvmField var RemoveNotification: RemoveNotification_callback? = null

    interface RemoveNotification_callback : Callback {
        fun invoke(notificationId: VRNotificationId): Int
    }

    constructor()

    override fun getFieldOrder(): List<String> = Arrays.asList("CreateNotification", "RemoveNotification")

    constructor(peer: Pointer) : super(peer) {
        read()
    }

    class ByReference : IVRNotifications(), Structure.ByReference
    class ByValue : IVRNotifications(), Structure.ByValue
}

val IVRNotifications_Version = "FnTable:IVRNotifications_002"