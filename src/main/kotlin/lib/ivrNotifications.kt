package lib

import ab.appBuffer
import glm_.buffer.adr
import org.lwjgl.openvr.NotificationBitmap
import org.lwjgl.openvr.OpenVR.IVRNotifications
import org.lwjgl.openvr.VRNotifications
import org.lwjgl.system.MemoryUtil.NULL
import vkk.adr
import java.nio.IntBuffer

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
fun IVRNotifications.createNotification(overlayHandle: VROverlayHandle, userValue: Long, type: EVRNotificationType, text: String, style: EVRNotificationStyle, image: NotificationBitmap?, notificationId: IntBuffer): EVRNotificationError {
    val textEncoded = appBuffer.bufferOfAscii(text)
    return EVRNotificationError of VRNotifications.nVRNotifications_CreateNotification(overlayHandle, userValue, type.i, textEncoded.adr, style.i, image?.adr ?: NULL, notificationId.adr)
}

/**
 * Destroy a notification, hiding it first if it currently shown to the user.
 *
 * @param notificationId
 */
fun IVRNotifications.removeNotification(notificationId: VRNotificationId): EVRNotificationError {
    return EVRNotificationError of VRNotifications.VRNotifications_RemoveNotification(notificationId)
}