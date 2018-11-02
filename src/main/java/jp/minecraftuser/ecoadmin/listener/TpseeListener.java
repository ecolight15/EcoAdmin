
package jp.minecraftuser.ecoadmin.listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.ListenerFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * プレイヤーイベント処理リスナークラス
 * @author ecolight
 */
public class TpseeListener extends ListenerFrame {
    private static HashMap<Player, Boolean> tpsee = new HashMap<>();
    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ 名前
     */
    public TpseeListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }
    
    /**
     * プレイヤーQUITイベント
     * @param e イベント
     */
    @EventHandler(priority=EventPriority.LOWEST)
    public void PlayerQuit(PlayerQuitEvent e) {
        // Tpsee解除しておく
        if (tpsee.containsKey(e.getPlayer())) {
            tpsee.remove(e.getPlayer());
        }
    }

    /**
     * プレイヤーTpseeイベントハンドラ
     * @param e イベント
     */
    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    public void PlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        // Tpsee動作
        if (!tpsee.containsKey(p)) return;
        if (e.getAction().equals(Action.LEFT_CLICK_AIR)) {
            if (tpsee.get(p)) {
                HashSet<Material> set = new HashSet<>();
                set.add(Material.AIR);
                set.add(Material.CAVE_AIR);
                set.add(Material.VOID_AIR);
                List<Block> bset = p.getLineOfSight(set, 100); // 稀にNULLポが出るけとしばらくは気にしないことにする
                if (bset != null) {
                    for (Block b : bset) {
                        if (!b.getType().equals(Material.AIR)) {

                            // 見つけたブロックの上を探す
                            Block up1;
                            Block up2;
                            up1 = b.getRelative(BlockFace.UP);
                            // 250ブロック上までにしておく
                            for (int i = 0; i < 250; i++) {
                                up2 = up1.getRelative(BlockFace.UP);
                                if ((up1.getType() == Material.AIR) &&
                                    (up2.getType() == Material.AIR)) {
                                    // 見つけたのでテレポート
                                    p.teleport(up1.getLocation());
                                    break;
                                }
                                // 見つからない場合up2をup1にして回す
                                up1 = up2;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Tpseeコマンド用toggle設定処理
     * @param p プレイヤー
     */
    public void toggleTpsee(Player p) {
        if (!tpsee.containsKey(p)) {
            tpsee.put(p, false);
        }
        if (tpsee.get(p)) {
            tpsee.replace(p, false);
            sendPluginMessage(plg, p, "tpseeを無効にしました。");
        } else {
            tpsee.replace(p, true);
            sendPluginMessage(plg, p, "tpseeを有効にしました。無効にするには再度コマンド実行してください。");
        }
    }
}
