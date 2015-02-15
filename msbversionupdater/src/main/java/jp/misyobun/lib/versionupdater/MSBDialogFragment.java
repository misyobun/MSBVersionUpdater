package jp.misyobun.lib.versionupdater;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by usr0600259 on 15/02/05.
 */
public class MSBDialogFragment extends DialogFragment {

    public static final String TAG = "msb_update_confirm";

    public static final String OPTIONAL_CANCEL_FLAG_TAG = "OPTIONAL_CANCEL_FLAG_TAG";

    public static final String OPTIONAL_CANCEL_VERSION_TAG = "OPTIONAL_CANCEL_VERSION_TAG";

    /**
     * Fragmentを表示するActivity
     */
    private Activity mActivity;

    /**
     * 更新情報
     */
    private MSBUpdateInfo updateInfo;

    /**
     * 強制アップデートタイトル
     */
    private String forceTitle;

    /**
     * 強制アップデートメッセージ
     */
    private String forceMessage;

    /**
     * ダイアログタイトル
     */
    private String title;

    /**
     * ダイアログメッセージ
     */
    private String message;

    /**
     * クローズフラグ
     */
    private boolean dismissFlag;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;

    }

    @Override
    public void onResume() {

        if (dismissFlag) {
            MSBDialogFragment dialogFragment = (MSBDialogFragment)getFragmentManager().findFragmentByTag(TAG);
            if (dialogFragment instanceof MSBDialogFragment ) {
                dialogFragment.dismiss();
                getFragmentManager().beginTransaction().remove(dialogFragment).commit();
                dismissFlag = false;
            }
        }

        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void dismiss() {
        if (isResumed()) {
            super.dismiss();
        } else {
            dismissFlag = true;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getForceTitle();
        String message = getForceMessage();
        boolean isOptional = false;

        if (updateInfo.type.equals("optional")) {
            title = getTitle();
            message = getMessage();
            isOptional = true;
        }

        AlertDialog.Builder dialog =  new AlertDialog.Builder(mActivity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.update_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final String appPackageName = fetchPackageName(updateInfo.update_url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));

                        try {
                            startActivity(intent);
                            Washi.putBooleanValue(mActivity.getApplicationContext(),OPTIONAL_CANCEL_FLAG_TAG,false);
                            Washi.putStringValue(mActivity.getApplicationContext(),OPTIONAL_CANCEL_VERSION_TAG,"");
                        } catch (ActivityNotFoundException e) {
                        }

                    }
                });

        if (isOptional) {

            dialog.setNegativeButton(R.string.update_cancel, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Washi.putBooleanValue(mActivity.getApplicationContext(),OPTIONAL_CANCEL_FLAG_TAG,true);
                    Washi.putStringValue(mActivity.getApplicationContext(),OPTIONAL_CANCEL_VERSION_TAG,updateInfo.required_version);
                }
            });

        } else {
            this.setCancelable(false);
        }
        return dialog.create();
    }

    /**
     * urlからpackageNameを取得する
     * @param url
     * @return packageName
     */
    private String fetchPackageName(final String url) {

        String packageName = "";
        int start = url.lastIndexOf("=") + 1;
        int end   = url.length();
        packageName = url.substring(start,end);
        return  packageName;
    }

    /**
     * 更新情報のゲッター
     * @return
     */
    public MSBUpdateInfo getUpdateInfo() {
        return updateInfo;
    }

    /**
     * 更新情報のセッター
     * @param updateInfo
     */
    public void setUpdateInfo(MSBUpdateInfo updateInfo) {
        this.updateInfo = updateInfo;
    }

    /**
     * ダイアログタイトルのゲッター
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * ダイアログタイトルのセッター
     * @return
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * ダイアログメッセージのゲッター
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * ダイアログメッセージのセッター
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 強制アップデートタイトル ゲッター
     * @return
     */
    public String getForceTitle() {
        return forceTitle;
    }

    /**
     * 強制アップデートタイトル セッター
     * @param forceTitle
     */
    public void setForceTitle(String forceTitle) {
        this.forceTitle = forceTitle;
    }

    /**
     * 強制アップデートメッセージ ゲッター
     * @return
     */
    public String getForceMessage() {
        return forceMessage;
    }

    /**
     * 強制アップデートメッセージ セッター
     * @param forceMessage
     */
    public void setForceMessage(String forceMessage) {
        this.forceMessage = forceMessage;
    }


}
