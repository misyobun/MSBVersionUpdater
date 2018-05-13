package jp.misyobun.lib.versionupdater

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import java.util.concurrent.TimeUnit

/**
 *
 * Created by misyobun on 15/02/05.
 */

class MSBVersionUpdater
(
    private val activity: AppCompatActivity?) {

    private lateinit var updateInfo: MSBUpdateInfo

    var title: String? = null

    var message: String? = null

    var forceTitle: String? = null

    var forceMessage: String? = null

    var positiveButtonText: String? = null

    var negativeButtonText: String? = null

    var endpoint: String? = null

    /**
     * Compare version and Enable forced update in the past.
     * @return result
     */
    private
    val isVersionUpNeeded: Boolean
        get() {

            if (activity == null || !isNewerThanCancelVersion) {
                return false
            }

            try {
                val manager = activity.packageManager
                val packageInfo = manager.getPackageInfo(activity.packageName, 0)
                val currentVersion = packageInfo.versionName
                val lastForceUpdateVersion = updateInfo.last_force_required_version
                val requiredVersion = updateInfo.required_version

                var ret = versionCompare(currentVersion, lastForceUpdateVersion)
                if (ret < 0) {
                    updateInfo.last_force_required_version = "force"
                    return true
                }
                ret = versionCompare(currentVersion, requiredVersion)
                if (ret < 0) {

                    return true
                }

            } catch (t: Throwable) {
                t.printStackTrace()
            }
            return false
        }


    /**
     * Determine whether the version is newer than the canceled version
     * no cancel is true
     * @return result
     */
    private
    val isNewerThanCancelVersion: Boolean
        get() {
            updateInfo.let {
                val cancelFlag = Washi.getBooleanValue(activity!!, MSBDialogFragment.OPTIONAL_CANCEL_FLAG_TAG)
                if (!cancelFlag) {

                    return true
                } else {

                    val cancelVersionName = Washi.getStringValue(activity, MSBDialogFragment.OPTIONAL_CANCEL_VERSION_TAG)
                    if (versionCompare(cancelVersionName, it.required_version) < 0) {
                        return true
                    }
                }
            }
            return false
        }

    init {
        initDefaultLiteral()
    }

    /**
     * Set default resource
     */
    private fun initDefaultLiteral() {
        val res = this.activity!!.resources
        message = res.getString(R.string.update_message)
        forceMessage = res.getString(R.string.update_force_message)
        title = res.getString(R.string.update_title)
        forceTitle = res.getString(R.string.force_update_title)
    }

    /**
     * Check Version
     */
    fun executeVersionCheck() {

        initDialogIfNeeded()

        try {
            val localEndpoint = endpoint
            val thread = Thread(Runnable {
                if (TextUtils.isEmpty(localEndpoint)) {
                    return@Runnable
                }
                val client = OkHttpClient()
                client.setConnectTimeout(10, TimeUnit.SECONDS)
                client.setWriteTimeout(10, TimeUnit.SECONDS)
                client.setReadTimeout(30, TimeUnit.SECONDS)
                val builder = Request.Builder()
                builder.url(localEndpoint!!)
                val request = builder.build()
                val response: Response

                try {
                    response = client.newCall(request).execute()
                    val jsonBody = response.body().string()
                    val gson = Gson()
                    updateInfo = gson.fromJson(jsonBody, MSBUpdateInfo::class.java)

                    activity?.runOnUiThread({
                        showUpdateAnnounceIfNeeded()
                    })

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })

            thread.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * Dismiss Dialog if it is already displayed
     */
    private fun initDialogIfNeeded() {
        val dialogFragment = activity?.supportFragmentManager?.findFragmentByTag(MSBDialogFragment.TAG)
        if (dialogFragment != null && dialogFragment is MSBDialogFragment) {
            dialogFragment.dismiss()
        }
    }

    /**
     * Determinate whether showing update Annoucement
     */
    private fun showUpdateAnnounceIfNeeded() {

        if (!isVersionUpNeeded) {
            return
        }
        showUpdateAnnounce()
    }

    /**
     * Check the version
     * @param currentVersion  now version
     * @param requiredVersion Requested version
     * @return -1:requiredVersion is bigger
     * +:currentVersion is bigger
     * 0:same version
     */
    private fun versionCompare(currentVersion: String,
                               requiredVersion: String): Int {

        val current = currentVersion.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val required = requiredVersion.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var index = 0

        while (index < current.size && index < required.size && current[index] == required[index]) {
            index++
        }

        var diff = 0

        if (index < current.size && index < required.size) {
            diff = Integer.valueOf(current[index]).compareTo(Integer.valueOf(required[index]))
        } else {
            diff = current.size - required.size
        }

        return Integer.signum(diff)
    }

    /**
     * Display Fragment for announcement
     */
    private fun showUpdateAnnounce() {
        updateInfo.let {
            val theActivity = activity as? FragmentActivity ?: return
            val updateAnnounce = MSBDialogFragment()
            val args = Bundle()
            args.putString("title", title)
            args.putString("message", message)
            args.putString("forceTitle", forceTitle)
            args.putString("forceMessage", forceMessage)
            args.putString("positiveButtonText", positiveButtonText)
            args.putString("negativeButtonText", negativeButtonText)
            args.putSerializable(MSBDialogFragment.UPDATE_INFO, it)
            updateAnnounce.arguments = args
            updateAnnounce.show(theActivity.supportFragmentManager, MSBDialogFragment.TAG)
        }
    }
}
