package jp.misyobun.lib.versionupdater

import android.content.Context
import android.content.SharedPreferences

/*
 * This is Japanese paper. Let's write down the information in the application.
 */
object Washi {

    /** preference　You must Edit this value */
    val SHARED_PREFERENCE_CONFIG = "MSBVersionUpdater"

    /**
     * Read String from shared reference
     * @param context      Context
     * @param key          Key
     * @return value
     */
    fun getStringValue(context: Context, key: String): String {
        return context.applicationContext.getSharedPreferences(
                Washi.SHARED_PREFERENCE_CONFIG, Context.MODE_PRIVATE).getString(key, "")
    }

    /**
     * Write String to shared reference
     * @param context  Context
     * @param key      key
     * @param value    StringValue
     */
    fun putStringValue(context: Context, key: String, value: String) {
        context.applicationContext.getSharedPreferences(
                Washi.SHARED_PREFERENCE_CONFIG, Context.MODE_PRIVATE).edit().putString(key, value).apply()
    }

    /**
     * Read Boolean from shared reference
     * @param context  Context
     * @param key      key
     * @return value
     */
    fun getBooleanValue(context: Context, key: String): Boolean {
        return context.applicationContext.getSharedPreferences(
                Washi.SHARED_PREFERENCE_CONFIG, Context.MODE_PRIVATE).getBoolean(key, false)
    }

    /**
     * Write Boolean to shared reference
     * @param context  Context
     * @param key      key
     * @param value    BooleanValue
     */
    fun putBooleanValue(context: Context, key: String, flag: Boolean) {
        context.applicationContext.getSharedPreferences(
                Washi.SHARED_PREFERENCE_CONFIG, Context.MODE_PRIVATE).edit().putBoolean(key, flag).apply()
    }

    /**
     * Read Int from shared reference
     * @param context  Context
     * @param key      key
     * @return value
     */
    fun getIntValue(context: Context, key: String): Int {
        return context.applicationContext.getSharedPreferences(
                Washi.SHARED_PREFERENCE_CONFIG, Context.MODE_PRIVATE).getInt(key, 0)
    }

    /**
     * Write Int to shared reference
     * @param context  Context
     * @param key      key
     * @param value    IntValue
     */
    fun putIntValue(context: Context, key: String, value: Int) {
        context.applicationContext.getSharedPreferences(
                Washi.SHARED_PREFERENCE_CONFIG, Context.MODE_PRIVATE).edit().putInt(key, value).apply()
    }

    /**
     * Return shared reference for application
     * @param contex context
     * @return sharedreference
     */
    fun getSharedPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(Washi.SHARED_PREFERENCE_CONFIG, Context.MODE_PRIVATE)
    }

    /**
     * Initialize shared reference for application
     * @param context context
     */
    fun clear(context: Context) {
        context.applicationContext.getSharedPreferences(
                Washi.SHARED_PREFERENCE_CONFIG, Context.MODE_PRIVATE).edit().clear().apply()

    }
}
