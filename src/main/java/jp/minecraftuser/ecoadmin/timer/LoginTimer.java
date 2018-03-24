package jp.minecraftuser.ecoadmin.timer;

import java.util.List;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.TimerFrame;
import jp.minecraftuser.ecoadmin.config.EcoAdminConfig;
import jp.minecraftuser.ecoadmin.config.LoginMsgConfig;
import static jp.minecraftuser.ecoframework.Utl.sendTagMessage;
import org.bukkit.entity.Player;

/**
 * ログインタイマークラス
 * @author ecolight
 */
public class LoginTimer extends TimerFrame {
    private static EcoAdminConfig ecaConf = null;
    private static LoginMsgConfig msgConf = null;

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param player_ プレイヤー
     * @param name_ 名前
     */
    public LoginTimer(PluginFrame plg_, Player player_, String name_) {
        super(plg_, player_, name_);
        ecaConf = (EcoAdminConfig)conf;
        msgConf = (LoginMsgConfig)plg.getPluginConfig("login");
    }

    /**
     * 非同期処理メイン
     */
    @Override
    public void run()
    {
        if (msgConf != null) {
            List<String> list = msgConf.getArrayList("msg");
            if (list != null) {
                for (String s: list) {
                    sendTagMessage(plg, this.player, "Update", s);
                }
            } else {
                sendTagMessage(plg, this.player, "Update", "======================List null===================");
            }
        } else {
            sendTagMessage(plg, this.player, "Update", "====================Conf null=====================");
        }
    }
}
