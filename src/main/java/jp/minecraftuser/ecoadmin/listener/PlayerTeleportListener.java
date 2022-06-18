
package jp.minecraftuser.ecoadmin.listener;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoadmin.command.BackCommand;
import jp.minecraftuser.ecoframework.Utl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * プレイヤーテレポートイベント処理リスナークラス
 * @author ecolight
 */
public class PlayerTeleportListener extends ListenerFrame {
    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ 名前
     */
    public PlayerTeleportListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    /**
     * プレイヤーテレポートイベント
     * @param e イベント
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void PlayerTeleport(PlayerTeleportEvent e)
    {
        // コーラスフルーツ抑止
        if (conf.getBoolean("protection.chorus_fruit.disable")) {
            if (e.getCause() == PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT) {
                if (conf.getArrayList("protection.chorus_fruit.world_list").contains(e.getFrom().getWorld().getName())) {
                    Utl.sendPluginMessage(plg, e.getPlayer(), "world[{0}]でのコーラスフルーツの使用は禁止されています", e.getTo().getWorld().getName());
                    e.setCancelled(true);
                }
            }
        }
    }
    
    /**
     * プレイヤーテレポートモニターイベントハンドラ
     * @param e イベント
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void PlayerTeleportMonitor(PlayerTeleportEvent e)
    {
        // Backコマンド用テレポート前ロケーション退避
        ((BackCommand) plg.getPluginCommand("back")).registerLoc(e.getPlayer(), e.getFrom());
    }

}
