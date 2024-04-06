
package jp.minecraftuser.ecoadmin.listener;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.ListenerFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * プレイヤー握手イベント処理リスナークラス
 * @author ecolight
 */
public class PlayerHandShakeListener extends ListenerFrame {

    private HashMap<UUID, Long> lastTime;

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ 名前
     */
    public PlayerHandShakeListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        lastTime = new HashMap<>();
    }

    /**
     * プレイヤーエンティティ間作用イベント
     * @param e イベント
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void PlayerInteractEntity(PlayerInteractEntityEvent e)
    {
        // しゃがみ右クリックでアイテムの受け渡し
        if (conf.getBoolean("fun.handshake_item_exchange")) {
            if (PlayerItemPut(e.getPlayer(), e.getRightClicked())) e.setCancelled(true);
        }
    }

    /**
     * 握手アイテム交換処理
     * @param pl プレイヤー
     * @param ent 交換相手
     * @return イベントキャンセル判定
     */
    public boolean PlayerItemPut(Player pl, Entity ent) {
        if (ent.getType() != EntityType.PLAYER) return false;
        Long time = System.currentTimeMillis();
        if (lastTime.containsKey(pl.getUniqueId())) {
            if (time - lastTime.get(pl.getUniqueId()) < 1000) {
                return true;
            }
            lastTime.remove(pl.getUniqueId());
        }
        lastTime.put(pl.getUniqueId(), time);
        Player getter = (Player)ent;
        if (!pl.isSneaking()) return false;
        // アイテムを奪い取る場合
        if (getter.getInventory().getItemInMainHand().getType() != Material.AIR) {
            if (pl.isOp()) {
                ItemStack i = getter.getInventory().getItemInMainHand();
                if (pl.getInventory().getItemInMainHand().getType() != Material.AIR) return false;
                pl.getInventory().setItemInMainHand(i);
                getter.getInventory().setItemInMainHand(null);
                sendPluginMessage(plg, pl, "プレイヤー[{0}]から[{1}x{2}]を奪い取りました", getter.getDisplayName(), i.getType().name(), Integer.toString(i.getAmount()));
                sendPluginMessage(plg, getter, "プレイヤー[{0}]に[{1}x{2}]を奪われました", pl.getDisplayName(), i.getType().name(), Integer.toString(i.getAmount()));
            } else {
                return false;
            }
        }
        // アイテムを押し付ける場合
        else {
            if (pl.getInventory().getItemInMainHand().getType() == Material.AIR) {
                sendPluginMessage(plg, pl, "手に何も持っていないため手渡せません");
                return true;
            }
            ItemStack i = pl.getInventory().getItemInMainHand();
            getter.getInventory().setItemInMainHand(i);
            pl.getInventory().setItemInMainHand(null);
            sendPluginMessage(plg, pl, "プレイヤー[{0}]に[{1}x{2}]を手渡しました", getter.getDisplayName(), i.getType().name(), Integer.toString(i.getAmount()));
            sendPluginMessage(plg, getter, "プレイヤー[{0}]から[{1}x{2}]を受け取りました", pl.getDisplayName(), i.getType().name(), Integer.toString(i.getAmount()));
        }
        return true;
    }
}
