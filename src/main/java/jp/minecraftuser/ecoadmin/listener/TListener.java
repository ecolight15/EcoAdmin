package jp.minecraftuser.ecoadmin.listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.ListenerFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import jp.minecraftuser.ecoadmin.listener.TpseeListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * プレイヤーThorイベント処理リスナークラス
 * @author ecolight
 */
public class TListener extends ListenerFrame {
    private static HashMap<Player, Boolean> strike = new HashMap<>();
    private static HashMap<Player, String> strikeType = new HashMap<>();
    
    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ 名前
     */
    public TListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }
    
    /**
     * プレイヤーQUITイベント
     * @param e イベント
     */
    @EventHandler(priority=EventPriority.LOWEST)
    public void PlayerQuit(PlayerQuitEvent e) {
        // Strike解除しておく
        if (strike.containsKey(e.getPlayer())) {
            strike.remove(e.getPlayer());
        }
        if (strikeType.containsKey(e.getPlayer())) {
            strikeType.remove(e.getPlayer());
        }
    }

    /**
     * プレイヤーStrikeイベントハンドラ
     * @param e イベント
     */
    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    public void PlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        // Strike動作
        if (!strike.containsKey(p)) return;
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (strike.get(p)) {
                HashSet<Material> set = new HashSet<>();
                set.add(Material.AIR);
                set.add(Material.CAVE_AIR);
                set.add(Material.VOID_AIR);
                List<Block> bset = p.getLineOfSight(set, 100);
                if (bset != null) {
                    for (Block b : bset) {
                        if (!b.getType().equals(Material.AIR)) {
                            // 見つけたブロックの位置に雷を落とす
                            String type = strikeType.get(p);
                            if ("real".equals(type)) {
                                b.getWorld().strikeLightning(b.getLocation());
                            } else {
                                // "dumy"またはnullの場合はエフェクトのみ
                                b.getWorld().strikeLightningEffect(b.getLocation());
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Tコマンド用toggle設定処理
     * @param p プレイヤー
     * @param type 雷タイプ ("real" or "dumy"/null)
     */
    public void toggleStrike(Player p, String type) {
        // TpseeCommandが有効の場合は抑止
        try {
            TpseeListener tpseeListener = (TpseeListener)plg.getPluginListener("tpsee");
            if (tpseeListener != null && tpseeListener.isTpseeEnabled(p)) {
                sendPluginMessage(plg, p, "tpseeが有効のため、Strikeモードを開始できません。");
                return;
            }
        } catch (Exception e) {
            // TpseeListenerが登録されていない場合は無視
        }
        
        if (!strike.containsKey(p)) {
            strike.put(p, false);
        }
        if (strike.get(p)) {
            strike.replace(p, false);
            strikeType.remove(p);
            sendPluginMessage(plg, p, "Strikeモードを無効にしました。");
        } else {
            strike.replace(p, true);
            strikeType.put(p, type);
            if ("real".equals(type)) {
                sendPluginMessage(plg, p, "Strikeモード(実際の雷)を有効にしました。無効にするには再度コマンド実行してください。");
            } else {
                sendPluginMessage(plg, p, "Strikeモード(エフェクトのみ)を有効にしました。無効にするには再度コマンド実行してください。");
            }
        }
    }
    
    /**
     * Strikeモードが有効かどうかチェック
     * @param p プレイヤー
     * @return 有効かどうか
     */
    public boolean isStrikeEnabled(Player p) {
        return strike.containsKey(p) && strike.get(p);
    }
}