package lib

import ab.appBuffer
import glm_.buffer.adr
import org.lwjgl.openvr.OpenVR.IVRSettings
import org.lwjgl.openvr.VRSettings
import org.lwjgl.system.MemoryUtil.NULL
import java.nio.ByteBuffer
import java.nio.IntBuffer

// Useless on JVM
//@Nullable
//@NativeType("char const *")
//public static String VRSettings_GetSettingsErrorNameFromEnum(@NativeType("EVRSettingsError") int eError) {
//    long __result = nVRSettings_GetSettingsErrorNameFromEnum(eError);
//    return memASCIISafe(__result);
//}

/**
 * Returns true if file sync occurred (force or settings dirty).
 *
 * @param force
 * @param error ~ EVRSettingsError
 */
fun IVRSettings.sync(force: Boolean = false, error: IntBuffer? = null): Boolean {
    return VRSettings.nVRSettings_Sync(force, error?.adr ?: NULL)
}

/**
 * @param error ~EVRSettingsError
 */
fun IVRSettings.setBool(section: String, settingsKey: String, value: Boolean, error: IntBuffer? = null) {
    val sectionEncoded = appBuffer.bufferOfAscii(section)
    val settingsKeyEncoded = appBuffer.bufferOfAscii(settingsKey)
    VRSettings.nVRSettings_SetBool(sectionEncoded.adr, settingsKeyEncoded.adr, value, error?.adr ?: NULL)
}

/**
 * @param error ~EVRSettingsError
 */
fun IVRSettings.setInt(section: String, settingsKey: String, value: Int, error: IntBuffer? = null) {
    val sectionEncoded = appBuffer.bufferOfAscii(section)
    val settingsKeyEncoded = appBuffer.bufferOfAscii(settingsKey)
    VRSettings.nVRSettings_SetInt32(sectionEncoded.adr, settingsKeyEncoded.adr, value, error?.adr ?: NULL)
}

/**
 * @param error ~EVRSettingsError
 */
fun IVRSettings.setFloat(section: String, settingsKey: String, value: Float, error: IntBuffer? = null) {
    val sectionEncoded = appBuffer.bufferOfAscii(section)
    val settingsKeyEncoded = appBuffer.bufferOfAscii(settingsKey)
    VRSettings.nVRSettings_SetFloat(sectionEncoded.adr, settingsKeyEncoded.adr, value, error?.adr ?: NULL)
}

/**
 * @param error ~EVRSettingsError
 */
fun IVRSettings.setString(section: String, settingsKey: String, value: String, error: IntBuffer? = null) {
    val sectionEncoded = appBuffer.bufferOfAscii(section)
    val settingsKeyEncoded = appBuffer.bufferOfAscii(settingsKey)
    val valueEncoded = appBuffer.bufferOfAscii(value)
    VRSettings.nVRSettings_SetString(sectionEncoded.adr, settingsKeyEncoded.adr, valueEncoded.adr, error?.adr ?: NULL)
}

/**
 * @param error ~EVRSettingsError
 */
fun IVRSettings.getBool(section: String, settingsKey: String, error: IntBuffer? = null): Boolean {
    val sectionEncoded = appBuffer.bufferOfAscii(section)
    val settingsKeyEncoded = appBuffer.bufferOfAscii(settingsKey)
    return VRSettings.nVRSettings_GetBool(sectionEncoded.adr, settingsKeyEncoded.adr, error?.adr ?: NULL)
}

/**
 * @param error ~EVRSettingsError
 */
fun IVRSettings.getInt(section: String, settingsKey: String, error: IntBuffer? = null): Int {
    val sectionEncoded = appBuffer.bufferOfAscii(section)
    val settingsKeyEncoded = appBuffer.bufferOfAscii(settingsKey)
    return VRSettings.nVRSettings_GetInt32(sectionEncoded.adr, settingsKeyEncoded.adr, error?.adr ?: NULL)
}

/**
 * @param error ~EVRSettingsError
 */
fun IVRSettings.getFloat(section: String, settingsKey: String, error: IntBuffer? = null): Float {
    val sectionEncoded = appBuffer.bufferOfAscii(section)
    val settingsKeyEncoded = appBuffer.bufferOfAscii(settingsKey)
    return VRSettings.nVRSettings_GetFloat(sectionEncoded.adr, settingsKeyEncoded.adr, error?.adr ?: NULL)
}

/**
 * @param error ~EVRSettingsError
 */
fun IVRSettings.getString(section: String, settingsKey: String, value: ByteBuffer, error: IntBuffer? = null) {
    val sectionEncoded = appBuffer.bufferOfAscii(section)
    val settingsKeyEncoded = appBuffer.bufferOfAscii(settingsKey)
    VRSettings.nVRSettings_GetString(sectionEncoded.adr, settingsKeyEncoded.adr, value.adr, value.rem, error?.adr
            ?: NULL)
}

/**
 * @param error ~EVRSettingsError
 */
fun IVRSettings.removeSection(section: String, error: IntBuffer? = null) {
    val sectionEncoded = appBuffer.bufferOfAscii(section)
    VRSettings.nVRSettings_RemoveSection(sectionEncoded.adr, error?.adr ?: NULL)
}

/**
 * @param error ~EVRSettingsError
 */
fun IVRSettings.removeKeyInSection(section: String, settingsKey: String, error: IntBuffer? = null) {
    val sectionEncoded = appBuffer.bufferOfAscii(section)
    val settingsKeyEncoded = appBuffer.bufferOfAscii(settingsKey)
    VRSettings.nVRSettings_RemoveKeyInSection(sectionEncoded.adr, settingsKeyEncoded.adr, error?.adr ?: NULL)
}