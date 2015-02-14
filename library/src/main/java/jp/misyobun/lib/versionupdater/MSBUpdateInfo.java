package jp.misyobun.lib.versionupdater;

/**
 * Created by usr0600259 on 15/02/05.
 */
public class MSBUpdateInfo  {

    /** 強制アップデート */
    public static final String TYPE_FORCE = "force";
    /** 最新バージョン */
    public String required_version;
    /** 対象URL */
    public String update_url;
    /** 強制:force 任意:optional */
    public String type;

    /**
     * コンストラクタ
     * @param requiredVersion
     * @param updateUrl
     * @param type
     */
    public MSBUpdateInfo(String requiredVersion, String updateUrl, String type) {
        this.required_version = requiredVersion;
        this.update_url = updateUrl;
        this.type = type;
    }
}
