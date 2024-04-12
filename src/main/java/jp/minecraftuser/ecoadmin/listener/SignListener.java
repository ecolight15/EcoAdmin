package jp.minecraftuser.ecoadmin.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.Utl;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import static jp.minecraftuser.ecoframework.Utl.repColor;

/**
 * 看板イベント処理リスナークラス
 * @author ecolight
 */
public class SignListener extends ListenerFrame {
    
    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ 名前
     */
    public SignListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }
   
    /**
     * 看板イベント処理
     * @param e イベント情報
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onSignChange(SignChangeEvent e) {
        if (conf.getBoolean("fun.sign_colorcode_convert")) {
            for (int i = 0; i < e.getLines().length; i++) {
                String line = e.getLines()[i];
                line = repColor(line);
                e.setLine(i, line);
            }
        }

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void PlayerInteract(PlayerInteractEvent event) {
        //右クリック以外は無視
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack usedItem = event.getItem();
        Block clickedBlock = event.getClickedBlock();

        //clickedBlockが看板である場合
        if (clickedBlock != null && isSignBlock(clickedBlock)) {
            //スニーク中でない場合
            if (!player.isSneaking()) {
                //ハニカムもしくは光るイカ墨の袋ならイベントを続行
                if (usedItem != null && (usedItem.getType() == Material.HONEYCOMB || usedItem.getType() == Material.GLOW_INK_SAC)) {
                    final Sign sign = (Sign) clickedBlock.getState();
                    if (sign.isWaxed()) {
                        //すでにワックス済みならキャンセル
                        Utl.sendPluginMessage(plg, player, "この看板はワックス済みです。");
                        event.setCancelled(true);
                        return;
                    }
                    if (usedItem.getType() == Material.GLOW_INK_SAC && sign.getTargetSide(player).isGlowingText()) {
                        //すでに発光済みならキャンセル
                        Utl.sendPluginMessage(plg, player, "この看板は発光済みです。");
                        event.setCancelled(true);
                        return;
                    }
                    //上記以外であれば続行
                    return;

                } else if (usedItem != null && usedItem.getType().toString().contains("SIGN")) {
                    //看板右クリックの場合はイベントをキャンセル
                    //(看板を設置のためのクリックか、編集のためのクリックか判断できないため)
                    Utl.sendPluginMessage(plg, player, "看板を設置するには、スニーク状態で右クリックしてください。");
                    event.setCancelled(true);
                    return;
                } else {
                    //スニーク中でなければイベントをキャンセル
                    Utl.sendPluginMessage(plg, player, "看板を編集するには、何も持たずにスニーク状態で右クリックしてください。");
                    event.setCancelled(true);
                    return;
                }

            } else {
                //スニーク中の場合
                if (usedItem != null) {
                    //スニーク中でアイテムを持っている場合はイベントを続行
                    return;
                }
                //看板編集処理開始

                //プラグインの機能で看板を開くので、イベント自体はキャンセル
                event.setCancelled(true);

                final Sign sign = (Sign) clickedBlock.getState();
                if (sign.isWaxed()) {
                    Utl.sendPluginMessage(plg, player, "この看板は編集できません。");
                    return;
                }
                //クリックした看板の向きを取得
                final Side clickSide = getSignBlockSide(clickedBlock, player);

                //看板の§を&に置換
                final String[] old_text = sign.getSide(clickSide).getLines();
                for (int i = 0; i < old_text.length; i++) {
                    String line = old_text[i];
                    line = line.replaceAll("§", "&");
                    sign.getSide(clickSide).setLine(i, line);
                }
                sign.update();

                //変更は即時反映されないため、2Tick後に看板を開く
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.openSign(sign, clickSide);
                    }
                }.runTaskLater(plg, 2);

                //看板を開いた次のtickで看板の内容を元に戻す。
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        //該当座標のブロックを取得
                        Sign restore_sign = (Sign) sign.getWorld().getBlockAt(sign.getLocation()).getState();
                        for (int i = 0; i < old_text.length; i++) {
                            String line = old_text[i];
                            restore_sign.getSide(clickSide).setLine(i, line);
                        }
                        restore_sign.update();
                    }
                }.runTaskLater(plg, 3);
            }

        }
    }

    public static Side getSignBlockSide(Block block, Player player) {
        if (block != null && isSignBlock(block)) {
            //プレイヤーが右クリックした看板向きのインスタンスが、看板の正面のインスタンスと同一である場合は正面と判断する。
            Sign sign = (Sign) block.getState();
            SignSide clickSignSide = sign.getTargetSide(player);
            if (clickSignSide == sign.getSide(Side.FRONT)) {
                return Side.FRONT;
            } else {
                return Side.BACK;
            }
        }
        return Side.FRONT;
    }

    public static boolean isSignBlock(Block block) {
        BlockData blockData = block.getBlockData();

        if (blockData instanceof org.bukkit.block.data.type.Sign
                || blockData instanceof org.bukkit.block.data.type.WallSign
                || blockData instanceof org.bukkit.block.data.type.HangingSign
                || blockData instanceof org.bukkit.block.data.type.WallHangingSign) {
            return true;
        }
        return false;
    }

}
