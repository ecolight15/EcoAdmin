
package jp.minecraftuser.ecoadmin.listener;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.ListenerFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * 死亡関連イベント処理リスナークラス
 * @author ecolight
 */
public class DeathListener extends ListenerFrame {
    
    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ 名前
     */
    public DeathListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }
    
    /**
     * プレイヤー死亡イベント
     * @param e 
     */
    @EventHandler(priority=EventPriority.LOWEST)
    public void PlayerDeath(PlayerDeathEvent e)
    {
        // 事由無しの死亡メッセージは出さない
        boolean killmsg = conf.getBoolean("cmd.kill.disable_kill_message");
        if (killmsg) {
            if (e.getDeathMessage() == null) return;
            Player p = e.getEntity();
            if (e.getDeathMessage().equals(p.getName()+" died")) {
                log.info(e.getDeathMessage());
                e.setDeathMessage(null);
            }
        }
    }
}
