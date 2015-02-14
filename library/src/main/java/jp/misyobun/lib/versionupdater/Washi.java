package jp.misyobun.lib.versionupdater;

import android.content.Context;
import android.content.SharedPreferences;
/*
 * これは和紙です。アプリ内の情報を書き残しましょう。
 * Created by misyobun
 */
public class Washi {

    /**
     * デバッグ用
     */
    final public static String TAG = Washi.class.getName();

    /** プリファレンス　**/
    final public static String SHARED_PREFERENCE_CONFIG = "MSBVersionUpdater";

    /**
     * 共有リファレンスから読み込み
     * @param context      Context
     * @param key          キー
     * @return キー値
     */
    public static String getStringValue(Context context, String key) {
        return context.getApplicationContext().getSharedPreferences(
                Washi.SHARED_PREFERENCE_CONFIG, Context.MODE_PRIVATE).getString(key, "");
    }

    /**
     * 共有リファレンスに書き込み
     * @param context  Context
     * @param key      キー
     * @param value    書き込み値
     */
    public static void putStringValue(Context context, String key, String value) {
        context.getApplicationContext().getSharedPreferences(
                Washi.SHARED_PREFERENCE_CONFIG, Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    /**
     * 共有リファレンスから読み込み
     * @param context      Context
     * @param key          キー
     * @return キー値
     */
    public static boolean getBooleanValue(Context context, String key) {
        return context.getApplicationContext().getSharedPreferences(
                Washi.SHARED_PREFERENCE_CONFIG, Context.MODE_PRIVATE).getBoolean(key, false);
    }

    /**
     * 共有リファレンスに書き込み
     * @param context  Context
     * @param key      キー
     * @param flag     書き込み値
     */
    public static void putBooleanValue(Context context, String key, boolean flag) {
        context.getApplicationContext().getSharedPreferences(
                Washi.SHARED_PREFERENCE_CONFIG, Context.MODE_PRIVATE).edit().putBoolean(key, flag).commit();
    }

    /**
     * 共有リファレンスから読み込み
     * @param context      Context
     * @param key          キー
     * @return キー値(初期値:0)
     */
    public static int getIntValue(Context context, String key) {
        return context.getApplicationContext().getSharedPreferences(
                Washi.SHARED_PREFERENCE_CONFIG, Context.MODE_PRIVATE).getInt(key, 0);
    }

    /**
     * 共有プリファレンスに書き込み
     * @param context  Context
     * @param key      キー
     * @param value    書き込む値
     */
    public static void putIntValue(Context context, String key, int value) {
        context.getApplicationContext().getSharedPreferences(
                Washi.SHARED_PREFERENCE_CONFIG, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    /**
     * アプリケーション用の共有リファレンスを返す
     * @param context
     * @return 共有リファレンス
     */
    public static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(Washi.SHARED_PREFERENCE_CONFIG, Context.MODE_PRIVATE);
    }

    /**
     * アプリケーション用の共有リファレンを期化する
     * @param context
     */
    public static void clear(Context context){
        context.getApplicationContext().getSharedPreferences(
                Washi.SHARED_PREFERENCE_CONFIG, Context.MODE_PRIVATE).edit().clear().commit();

    }
}
