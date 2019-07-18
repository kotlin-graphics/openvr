package openvr.lib

import kool.adr
import org.lwjgl.openvr.NotificationBitmap
import org.lwjgl.openvr.VRNotifications.VRNotifications_RemoveNotification
import org.lwjgl.openvr.VRNotifications.nVRNotifications_CreateNotification
import org.lwjgl.system.MemoryUtil.NULL
import java.nio.IntBuffer

object vrNotifications : vrInterface {

    const val textMaxSize = 256

    /** error codes for notifications */
    enum class Error(@JvmField val i: Int) {

        OK(0),
        InvalidNotificationId(100),
        NotificationQueueFull(101),
        InvalidOverlayHandle(102),
        SystemWithUserValueAlreadyExists(103);

        companion object {
            infix fun of(i: Int) = values().first { it.i == i }
        }
    }

    /** Be aware that the notification value is used as 'priority' to pick the next notification */
    enum class Type(@JvmField val i: Int) {

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

    enum class Style(@JvmField val i: Int) {

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

    /**
     * Create a notification and enqueue it to be shown to the user.
     *
     * <p>An overlay handle is required to create a notification, as otherwise it would be impossible for a user to act on it. To create a two-line notification,
     * use a line break ('\n') to split the text into two lines. The {@code image} argument may be {@code NULL}, in which case the specified overlay's icon will be
     * used instead.</p>
     *
     * @param overlayHandle
     * @param userValue
     * @param type            one of:<br><table><tr><td>{@link VR#EVRNotificationType_Transient}</td><td>{@link VR#EVRNotificationType_Persistent}</td></tr><tr><td>{@link VR#EVRNotificationType_Transient_SystemWithUserValue}</td></tr></table>
     * @param text
     * @param style           one of:<br><table><tr><td>{@link VR#EVRNotificationStyle_None}</td><td>{@link VR#EVRNotificationStyle_Application}</td></tr><tr><td>{@link VR#EVRNotificationStyle_Contact_Disabled}</td><td>{@link VR#EVRNotificationStyle_Contact_Enabled}</td></tr><tr><td>{@link VR#EVRNotificationStyle_Contact_Active}</td></tr></table>
     * @param image
     * @param notificationId ~VRNotificationId
     */
    fun createNotification(overlayHandle: VROverlayHandle, userValue: Long, type: Type, text: String,
                           style: Style, image: NotificationBitmap?, notificationId: IntBuffer): Error =
            stak {
                val pText = it.addressOfAscii(text)
                Error of nVRNotifications_CreateNotification(overlayHandle, userValue, type.i, pText, style.i, image?.adr
                        ?: NULL, notificationId.adr)
            }

    /**
     * Destroy a notification, hiding it first if it currently shown to the user.
     *
     * @param notificationId
     */
    fun removeNotification(notificationId: VRNotificationId): Error =
            Error of VRNotifications_RemoveNotification(notificationId)

    override val version: String
        get() = "IVRNotifications_002"
}