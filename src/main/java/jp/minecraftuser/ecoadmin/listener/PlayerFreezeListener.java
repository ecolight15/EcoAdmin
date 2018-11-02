
package jp.minecraftuser.ecoadmin.listener;

import java.util.HashMap;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.ListenerFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * プレイヤー凍結イベント処理リスナークラス
 * @author ecolight
 */
public class PlayerFreezeListener extends ListenerFrame {
    private static HashMap<Player, Boolean> freezeMap = null;

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ 名前
     */
    public PlayerFreezeListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        freezeMap = new HashMap<>();
    }
    
    /**
     * プレイヤー凍結設定
     * @param event PlayerMoveEventインスタンス
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void PlayerMove(PlayerMoveEvent event)
    {
        if (freezeMap.containsKey(event.getPlayer())) {
            if (freezeMap.get(event.getPlayer())) {
                event.setCancelled(true);
            }
        } 
    }

    /**
     * 凍結状態設定
     * @param p プレイヤーインスタンス
     * @param flag 凍結状態
     */
    public void setFreeze(Player p, boolean flag) {
        if (freezeMap.containsKey(p)) {
            freezeMap.replace(p, flag);
        } else {
            freezeMap.put(p, flag);
        }
    }
    
    /**
     * 凍結状態取得
     * @param p プレイヤーインスタンス
     * @return 凍結状態
     */
    public boolean isFreeze(Player p) {
        boolean ret = false;
        if (freezeMap.containsKey(p)) {
            ret = freezeMap.get(p);
        }
        return ret;
    }
}
