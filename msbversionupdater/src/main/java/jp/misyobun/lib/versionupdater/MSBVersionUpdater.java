package jp.misyobun.lib.versionupdater;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.concurrent.TimeUnit;

/**
 * Created by misyobun on 15/02/05.
 */
public class MSBVersionUpdater {

    /**
     * デバッグコード
     */
    public static final String TAG = MSBVersionUpdater.class.getSimpleName();

    /**
     * エンドポイントURL
     */
    private String endpoint;

    /**
     * 表示Activity
     */
    private Activity activity;

    /**
     * 通常時ダイアログタイトル
     */
    private String title;

    /**
     *  通常時ダイアログメッセージ
     */
    private String message;

    /**
     *  強制時ダイアログタイトル
     */
    private String forceTitle;

    /**
     *  強制時ダイアログメッセージ
     */
    private String forceMessage;

    /**
     * アップデート情報
     */
    private MSBUpdateInfo updateInfo;

    /**
     * コンンストラクタ
     * @param activity
     */
    public MSBVersionUpdater(final Activity activity){
        this.activity = activity;
        initDefaultLiteral();
    }

    /**
     * デフォルトリソースの追加
     */
    private void initDefaultLiteral(){
        Resources res = this.activity.getResources();
        setMessage(res.getString(R.string.update_message));
        setForceMessage(res.getString(R.string.update_force_message));
        setTitle(res.getString(R.string.update_title));
        setForceTitle(res.getString(R.string.force_update_title));
    }

    /**
     * エンドポイントゲッター
     * @return endpoint
     */
    public String getEndpoint() {

        return endpoint;
    }

    /**
     * エンドポイントセッター
     * @param endpoint エンドポイント
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * バージョンチェックを行う
     */
    public void executeVersionCheck() {

        initDialogIfNeeded();

        try {
            final String localEndpoint = getEndpoint();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (TextUtils.isEmpty(localEndpoint)) {
                        return;
                    }
                    OkHttpClient client = new OkHttpClient();
                    client.setConnectTimeout(10, TimeUnit.SECONDS);
                    client.setWriteTimeout(10, TimeUnit.SECONDS);
                    client.setReadTimeout(30, TimeUnit.SECONDS);
                    Request.Builder builder = new Request.Builder();
                    builder.url(localEndpoint);
                    Request request = builder.build();
                    Response response;

                    try {

                        response = client.newCall(request).execute();
                        String jsonBody = response.body().string();
                        Gson gson = new Gson();
                        updateInfo = gson.fromJson(jsonBody, MSBUpdateInfo.class);
                        showUpdateAnnounceIfNeeded();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            thread.start();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dialogがすでに表示されていたら消す
     */
    private void initDialogIfNeeded() {
        MSBDialogFragment dialogFragment = (MSBDialogFragment) activity.getFragmentManager().findFragmentByTag(MSBDialogFragment.TAG);
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }

    /**
     * バージョン判定
     * @return
     */
    private void showUpdateAnnounceIfNeeded() {

        if (!isVersionUpNeeded()) {
            return;
        }
        showUpdateAnnounce();
    }

    /**
     * バージョンアップ判定
     * @return
     */
    private boolean isVersionUpNeeded() {

        if (activity == null || !isNewerThanCancelVersion()) {
            return false;
        }

        boolean flag = false;

        try {

            PackageManager manager        = activity.getPackageManager();
            PackageInfo packageInfo       = manager.getPackageInfo(activity.getPackageName(), 0);
            String currentVersion         = packageInfo.versionName;
            String lastForceUpdateVersion = updateInfo.last_force_required_version;
            String requiredVersion        = updateInfo.required_version;

            int ret = versionCompare(currentVersion,lastForceUpdateVersion);
            if (ret < 0){
                // 過去の強制アップデートを有効にする
                updateInfo.type = "force";
                return true;
            }
            ret = versionCompare(currentVersion, requiredVersion);
            if (ret < 0) {

                return true;
            }

        }catch (PackageManager.NameNotFoundException e) {
            flag = false;
        }
        return flag;

    }


    /**
     * キャンセルしたバージョンよりも新しいバージョンかを判定
     * @return 判定結果
     */
    private boolean isNewerThanCancelVersion() {

        boolean cancelFlag = Washi.getBooleanValue(activity,MSBDialogFragment.OPTIONAL_CANCEL_FLAG_TAG);

        // キャンセルしていなかったらtrue
        if (!cancelFlag) {

            return true;
        } else {

            String cancelVersionName = Washi.getStringValue(activity,MSBDialogFragment.OPTIONAL_CANCEL_VERSION_TAG);
            if (versionCompare(cancelVersionName,updateInfo.required_version) < 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * バージョンの大きさを確認する
     * @param currentVersion  現在のバージョン
     * @param requiredVersion 要求されているバージョン
     * @return 結果がマイナス:requiredVersionの方が大きい
     *         結果がプラス:currentVersionの方が大きい
     *         結果がゼロ:同じバーション
     */
    private Integer versionCompare(final String currentVersion,
                                   final String requiredVersion) {

        String[] current  = currentVersion.split("\\.");
        String[] required = requiredVersion.split("\\.");
        int index         = 0;

        while (index < current.length && index < required.length && current[index].equals(required[index]))
        {
            index++;
        }

        int diff = 0;
        if (index < current.length && index < required.length) {

            diff = Integer.valueOf(current[index]).compareTo(Integer.valueOf(required[index]));

        } else {

            diff = current.length - required.length;
        }

        return  Integer.signum(diff);
    }

    /**
     * アナウンス用のFragmentを表示する
     */
    private void showUpdateAnnounce() {
        MSBDialogFragment updateAnnounce = new MSBDialogFragment();
        updateAnnounce.setTitle(getTitle());
        updateAnnounce.setMessage(getMessage());
        updateAnnounce.setForceTitle(getForceTitle());
        updateAnnounce.setForceMessage(getForceMessage());
        updateAnnounce.setUpdateInfo(updateInfo);
        FragmentTransaction transaction =  activity.getFragmentManager().beginTransaction();
        transaction.add(updateAnnounce,MSBDialogFragment.TAG);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 通常時ダイアログタイトルゲッター
     *
     * @return 通常時ダイアログタイトル
     */
    public String getTitle() {
        return title;
    }

    /**
     * 通常時ダイアログタイトルセッター
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 通常時ダイアログメッセージゲッター
     *
     * @return 通常時ダイアログメッセージ
     */
    public String getMessage() {
        return message;
    }

    /**
     * 通常時ダイアログメッセージセッター
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 強制時ダイアログタイトルゲッター
     *
     * @return 強制時ダイアログタイトル
     */
    public String getForceTitle() {
        return forceTitle;
    }

    /**
     * 強制時ダイアログタイトルセッター
     *
     * @param forceTitle
     */
    public void setForceTitle(String forceTitle) {
        this.forceTitle = forceTitle;
    }

    /**
     * 強制時ダイアログメッセージゲッター
     *
     * @return 強制時ダイアログメッセージ
     */
    public String getForceMessage() {
        return forceMessage;
    }

    /**
     * 強制時ダイアログメッセージセッター
     *
     * @param forceMessage
     */
    public void setForceMessage(String forceMessage) {
        this.forceMessage = forceMessage;
    }

}
