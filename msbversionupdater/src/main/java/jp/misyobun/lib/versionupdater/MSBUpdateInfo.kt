package jp.misyobun.lib.versionupdater

import java.io.Serializable

/**
 *
 * Created by misyobun on 15/02/05.
 */
data class MSBUpdateInfo
/**
 * @param last_force_required_version
 * @param required_version
 * @param update_url
 * @param type
 */
(
        /** Last compulsory update version */
        var last_force_required_version: String,
        /** New Version  */
        var required_version: String,
        /** url  */
        var update_url: String,
        /** `force` or `optional`  */
        var type: String) : Serializable
