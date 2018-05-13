package jp.misyobun.lib.versionupdater

import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.TextUtils
import android.view.KeyEvent

/**
 *
 * Created by usr0600259 on 15/02/05.
 */
class MSBDialogFragment : DialogFragment() {
    private lateinit var updateInfo: MSBUpdateInfo
    private var dismissFlag: Boolean = false
    private var isOptional = false
    private val onKeyListener = DialogInterface.OnKeyListener { dialog, keyCode, _ ->
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val backActivity = activity
            if (backActivity != null) {
                dialog.dismiss()
                backActivity.finish()
            }
        }
        false
    }


    override fun onResume() {
        if (dismissFlag) {
            val theFragmentManager = activity?.supportFragmentManager
            if (theFragmentManager != null) {
                (theFragmentManager.findFragmentByTag(TAG) as? MSBDialogFragment)?.let {
                    it.dismiss()
                    theFragmentManager.beginTransaction()?.remove(it)?.commit()
                    dismissFlag = false
                }

            }
        }
        super.onResume()
    }

    override fun dismiss() {
        if (isResumed) {
            super.dismiss()
        } else {
            dismissFlag = true
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return buildDialog()
    }

    /**
     * build DialogFragment
     */
    private fun buildDialog(): Dialog {

        var positiveButtonText = arguments?.getString("positiveButtonText")
        var negativeButtonText = arguments?.getString("negativeButtonText")

        positiveButtonText = if (TextUtils.isEmpty(positiveButtonText))
            getString(R.string.update_ok)
        else
            positiveButtonText

        negativeButtonText = if (TextUtils.isEmpty(negativeButtonText))
            getString(R.string.update_cancel)
        else
            positiveButtonText

        var title = arguments?.getString("forceTitle")
        var message = arguments?.getString("forceMessage")

        (arguments?.get(UPDATE_INFO) as MSBUpdateInfo).let {
            updateInfo = it
            if (it.type == "optional") {
                title = arguments?.getString("title")
                message = arguments?.getString("message")
                isOptional = true
            }
        }

        val dialog = AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText) { _, _ ->

                    val appPackageName = fetchPackageName(updateInfo.update_url)
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName))

                    try {
                        val targetContext = context
                        if (targetContext != null) {
                            startActivity(intent)
                            Washi.putBooleanValue(targetContext, OPTIONAL_CANCEL_FLAG_TAG, false)
                            Washi.putStringValue(targetContext, OPTIONAL_CANCEL_VERSION_TAG, "")
                        }
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                }

        if (isOptional) {
            val targetContext = context
            if (targetContext != null) {
                dialog.setNegativeButton(negativeButtonText) { _, _ ->
                    Washi.putBooleanValue(targetContext, OPTIONAL_CANCEL_FLAG_TAG, true)
                    Washi.putStringValue(targetContext, OPTIONAL_CANCEL_VERSION_TAG, updateInfo.required_version)
                }
            }

        } else {
            this.isCancelable = false
            dialog.setOnKeyListener(onKeyListener)
        }
        return dialog.create()
    }


    /**
     * fetch url from packageName
     * @param url packageUrl
     * @return packageName
     */
    private fun fetchPackageName(url: String): String {
        val start = url.lastIndexOf("=") + 1
        val end = url.length
        return url.substring(start, end)
    }

    companion object {

        const val UPDATE_INFO = "update_info"

        const val TAG = "msb_update_confirm"

        const val OPTIONAL_CANCEL_FLAG_TAG = "OPTIONAL_CANCEL_FLAG_TAG"

        const val OPTIONAL_CANCEL_VERSION_TAG = "OPTIONAL_CANCEL_VERSION_TAG"
    }

}
