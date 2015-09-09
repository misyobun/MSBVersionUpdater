package jp.misyobun.lib.versionupdater;

import java.io.Serializable;

/**
 * Created by usr0600259 on 15/02/05.
 */
public class MSBUpdateInfo  implements Serializable{

    /** 強制アップデート */
    public static final String TYPE_FORCE = "force";

    /** 前回の強制アップデートバージョン*/
    public String last_force_required_version;
    /** 最新バージョン */
    public String required_version;
    /** 対象URL */
    public String update_url;
    /** 強制:force 任意:optional */
    public String type;

    /**
     * コンストラクタ
     * @param last_force_required_version
     * @param requiredVersion
     * @param updateUrl
     * @param type
     */
    public MSBUpdateInfo(final String last_force_required_version,
                         final String requiredVersion,
                         final String updateUrl,
                         final String type) {
        this.required_version = requiredVersion;
        this.update_url = updateUrl;
        this.type = type;
        this.last_force_required_version = last_force_required_version;
    }
}
