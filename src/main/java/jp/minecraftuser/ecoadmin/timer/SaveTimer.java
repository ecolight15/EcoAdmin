package jp.minecraftuser.ecoadmin.timer;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.TimerFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.World;

/**
 * セーブタイマー
 * @author ecolight
 */
public class SaveTimer extends TimerFrame {
    private final boolean isBroadcast;
    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ 名前
     */
    public SaveTimer(PluginFrame plg_, String name_) {
        super(plg_, name_);
        isBroadcast = conf.getBoolean("util.auto_save.broadcast_information");
    }

    /**
     * 非同期処理メイン
     */
    @Override
    public void run()
    {
        if (isBroadcast) {
            sendPluginMessage(plg, null, "定期セーブを実行中(Auto saving...)");
        }
        log.info("セーブ中[players]");
        plg.getServer().savePlayers();
        for (World w : plg.getServer().getWorlds()) {
            log.info("セーブ中[players]");
            log.info("セーブ中[world:" + w.getName() + "]");
            w.save();
        }
        if (isBroadcast) {
            sendPluginMessage(plg, null, "定期セーブを完了しました(Auto save is complete.)");
        }
    }
}
