
package jp.minecraftuser.ecoadmin.config;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.ConfigFrame;

/**
 * ecoadmin設定ファイル
 * @author ecolight
 */
public class EcoAdminConfig extends ConfigFrame {

    public EcoAdminConfig(PluginFrame plg_) {
        super(plg_);
        plg.registerNotifiable(this);
    }

    /**
     * リロード検知
     * @param base 基底クラス動作指定
     */
    @Override
    public void reloadNotify(boolean base) {
        if (base) {
            super.reload();
        } else {
            
        }
    }
}
