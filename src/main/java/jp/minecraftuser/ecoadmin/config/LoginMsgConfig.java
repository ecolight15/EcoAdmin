
package jp.minecraftuser.ecoadmin.config;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.ConfigFrame;

/**
 * ログインメッセージ設定ファイル
 * @author ecolight
 */
public class LoginMsgConfig extends ConfigFrame {

    public LoginMsgConfig(PluginFrame plg_, String filename_, String name_) {
        super(plg_, filename_, name_);
        plg.registerNotifiable(this);
    }
}
