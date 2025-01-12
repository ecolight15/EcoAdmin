/**
 * 保護関連イベントリスナ―
*/

package jp.minecraftuser.ecoadmin.listener;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.ListenerFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;

/**
 * 保護系イベント処理リスナークラス
 * @author ecolight
 */
public class GuardListener extends ListenerFrame {
    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ 名前
     */
    public GuardListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    /**
     * エンティティ爆発イベントハンドラ
     * @param e イベント
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void EntityExplode(EntityExplodeEvent e) {
        // 爆発保護
        if (conf.getBoolean("protection.disable_explosion.ender_crystal")) {
            if (e.getEntityType() == EntityType.ENDER_CRYSTAL) {
                e.setCancelled(true);
            }
        }
    }

    /**
     * プレイヤーインタラクトイベントハンドラ
     * @param e イベント
     */
    @EventHandler(priority=EventPriority.LOWEST)
    public void PlayerInteract(PlayerInteractEvent e)
    {
        // エンダークリスタル設置判定
        if ((conf.getBoolean("protection.interact.ender_crystal.disable")) ||
            (conf.getBoolean("protection.interact.ender_crystal.logging"))) {
            Player p = e.getPlayer();
            if (!p.isOp()) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (e.getItem() != null) {
                        if (e.getItem().getType() == Material.END_CRYSTAL) {
                            Block b = e.getClickedBlock();
                            if (b != null) {
                                if (!b.getWorld().getName().startsWith(conf.getString("protection.interact.ender_crystal.ignore_world_prefix"))) {
                                    if (conf.getBoolean("protection.interact.ender_crystal.disable")) {
                                        sendPluginMessage(plg, p, "このワールドでのエンダークリスタルの使用は制限されています");
                                        e.setCancelled(true);
                                    }
                                    if (conf.getBoolean("protection.interact.ender_crystal.logging")) {
                                        StringBuilder sb = new StringBuilder("ENDER CRYSTAL placed[player=");
                                        sb.append(p.getName());
                                        sb.append("] worldname=");
                                        sb.append(p.getWorld().getName());
                                        sb.append(" loc=");
                                        sb.append(p.getLocation().toString());
                                        plg.getPluginLogger("ecrej").log(sb);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // スポナー変更判定
        if ((conf.getBoolean("protection.interact.mob_egg.disable")) ||
            (conf.getBoolean("protection.interact.mob_egg.logging"))) {
            Player p = e.getPlayer();
            if (!p.isOp()) {
                Block b = e.getClickedBlock();
                if (b != null) {
                    BlockData bd = b.getBlockData();
                    if ((bd != null) && (bd.getMaterial().equals(Material.SPAWNER) || bd.getMaterial().equals(Material.TRIAL_SPAWNER))) {
                        if (e.getItem() != null) {
                            System.out.println(e.getItem().toString());
                            if (e.getItem().toString().contains("SPAWN_EGG")) {
                                if (!b.getWorld().getName().startsWith(conf.getString("protection.interact.mob_egg.ignore_world_prefix"))) {
                                    if ((conf.getBoolean("protection.interact.mob_egg.disable"))) {
                                        sendPluginMessage(plg, p, "このワールドでのMOB卵によるスポナーブロックの変更は制限されています");
                                        e.setCancelled(true);
                                    }
                                    if (conf.getBoolean("protection.interact.mob_egg.logging")) {
                                        StringBuilder sb = new StringBuilder("SPAWNER BLOCK clicked[player=");
                                        sb.append(p.getName());
                                        sb.append("] worldname=");
                                        sb.append(p.getWorld().getName());
                                        sb.append(" loc=");
                                        sb.append(p.getLocation().toString());
                                        plg.getPluginLogger("screj").log(sb);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ベッド睡眠判定
        if ((conf.getBoolean("protection.interact.bed.disable")) ||
            (conf.getBoolean("protection.interact.bed.logging"))) {
            Player p = e.getPlayer();
            if (!p.isOp()) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Block b = e.getClickedBlock();
                    if (b != null) {
                        boolean isNether = false;
                        boolean isEnd = false;
                        switch (b.getBiome()) {
                            case BASALT_DELTAS:
                            case CRIMSON_FOREST:
                            case NETHER_WASTES:
                            case SOUL_SAND_VALLEY:
                            case WARPED_FOREST:
                                isNether = true;
                                break;
                            case THE_END:
                            case END_BARRENS:
                            case END_HIGHLANDS:
                            case END_MIDLANDS:
                            case SMALL_END_ISLANDS:
                                isEnd = true;
                                break;
                        }
                        if (isNether || isEnd) {
                            if ((b.getBlockData() != null) && (b.getBlockData().getMaterial() != null) &&
                                (b.getBlockData().getMaterial().name().endsWith("_BED"))) {
                                if (!b.getWorld().getName().startsWith(conf.getString("protection.interact.bed.ignore_world_prefix"))) {
                                    if ((conf.getBoolean("protection.interact.bed.disable"))) {
                                        sendPluginMessage(plg, p, "このワールドでのベッドの使用は制限されています");
                                        e.setCancelled(true);
                                    }
                                    if (conf.getBoolean("protection.interact.bed.logging")) {
                                        StringBuilder sb = new StringBuilder("BEDBLOCK Interact[player=");
                                        sb.append(p.getName());
                                        sb.append("] worldname=");
                                        sb.append(p.getWorld().getName());
                                        sb.append(" loc=");
                                        sb.append(p.getLocation().toString());
                                        plg.getPluginLogger("bedrej").log(sb);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // リスポーンアンカー
        if ((conf.getBoolean("protection.interact.respawnanchor.disable")) ||
            (conf.getBoolean("protection.interact.respawnanchor.logging"))) {
            Player p = e.getPlayer();
            if (!p.isOp()) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Block b = e.getClickedBlock();
                    if (b != null) {
                        if ((b.getBlockData() != null) && (b.getBlockData().getMaterial() != null) &&
                            (b.getBlockData().getMaterial().name().endsWith("RESPAWN_ANCHOR"))) {
                            if (!b.getWorld().getName().startsWith(conf.getString("protection.interact.respawnanchor.ignore_world_prefix"))) {
                                if ((conf.getBoolean("protection.interact.respawnanchor.disable"))) {
                                    sendPluginMessage(plg, p, "このワールドでのリスポーンアンカーの利用は制限されています");
                                    e.setCancelled(true);
                                }
                                if (conf.getBoolean("protection.interact.respawnanchor.logging")) {
                                    StringBuilder sb = new StringBuilder("RespawnAnchor Interact[player=");
                                    sb.append(p.getName());
                                    sb.append("] worldname=");
                                    sb.append(p.getWorld().getName());
                                    sb.append(" loc=");
                                    sb.append(p.getLocation().toString());
                                    plg.getPluginLogger("anchorej").log(sb);
                                }
                            }
                        }
                    }
                }
            }
        }

        // ホッパーカート手動設置抑止判定
        if ((conf.getBoolean("protection.interact.hoppercart.disable")) ||
            (conf.getBoolean("protection.interact.hoppercart.logging"))) {
            Player p = e.getPlayer();
            if (!p.isOp()) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (e.getItem() != null) {
                        if (e.getItem().getType().equals(Material.HOPPER_MINECART)) {
                            Block b = e.getClickedBlock();
                            if (b != null) {
                                if ((b.getBlockData().getMaterial().name().equals("RAILS")) ||
                                    (b.getType().equals(Material.RAIL)) ||
                                    (b.getType().equals(Material.POWERED_RAIL)) ||
                                    (b.getType().equals(Material.DETECTOR_RAIL)) ||
                                    (b.getType().equals(Material.ACTIVATOR_RAIL))) {
                                    if (!b.getWorld().getName().startsWith(conf.getString("protection.interact.hoppercart.ignore_world_prefix"))) {
                                        if ((conf.getBoolean("protection.interact.hoppercart.disable"))) {
                                            sendPluginMessage(plg, p, "このワールドでのホッパーカートの使用は制限されています");
                                            e.setCancelled(true);
                                        }
                                        if (conf.getBoolean("protection.interact.hoppercart.logging")) {
                                            StringBuilder sb = new StringBuilder("HopperCart Interact[player=");
                                            sb.append(p.getName());
                                            sb.append("] worldname=");
                                            sb.append(p.getWorld().getName());
                                            sb.append(" loc=");
                                            sb.append(p.getLocation().toString());
                                            plg.getPluginLogger("hoppercartrej").log(sb);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * ブロック設置イベントハンドラ
     * @param e イベント
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void BlockPlace(BlockPlaceEvent e)
    {
        // 濡れたスポンジの設置をキャンセルする
        if (conf.getBoolean("protection.place.wet_sponge.disable")) {
            Block b = e.getBlock();
            if (b.getType().equals(Material.WET_SPONGE)) {
                Player p = e.getPlayer();
                if (!p.isOp()) {
                    sendPluginMessage(plg, p, "濡れたスポンジを設置することはできません");
                    e.setCancelled(true);
                }
            }
        }
        
        // エンドの外縁部へ到達するのに本島からのブロック設置による到達をガードする
        if (conf.getBoolean("protection.place.endcitypath.disable")) {
            Block b = e.getBlock();
            World w = b.getWorld();
            // 指定ワールドが対象かチェック
            if (w.getName().startsWith(conf.getString("protection.place.endcitypath.end_world_prefix"))) {
                Location loc = b.getLocation();
                int x = Math.abs(loc.getBlockX());
                int z = Math.abs(loc.getBlockZ());
                if ((x < 500) && (z < 500) && (!((x < 300) && (z < 300)))) {
                    // 設置キャンセルに加えて全方位のブロックごと抹消する
                    b.getRelative(BlockFace.EAST).setType(Material.AIR);
                    b.getRelative(BlockFace.NORTH).setType(Material.AIR);
                    b.getRelative(BlockFace.SOUTH).setType(Material.AIR);
                    b.getRelative(BlockFace.WEST).setType(Material.AIR);
                    b.getRelative(BlockFace.DOWN).setType(Material.AIR);
                    b.getRelative(BlockFace.UP).setType(Material.AIR);
                    sendPluginMessage(plg, e.getPlayer(), "ブロックの設置が許可されていない領域です");
                    e.setCancelled(true);
                }
            }
        }
    }
    
    /**
     * プレイヤーインタラクトエンティティイベントハンドラ
     * @param e イベント
     */
    @EventHandler(priority=EventPriority.LOWEST)
    public void PlayerInteractEntity(PlayerInteractEntityEvent e) {
        // MAPの設置の判定
        if ((conf.getBoolean("protection.place.map.disable")) ||
            (conf.getBoolean("protection.place.map.logging"))) {
            Player p = e.getPlayer();
            if ((e.getRightClicked().getType() == EntityType.ITEM_FRAME) && 
                (!p.isOp()) && 
                (p.getInventory().getItemInMainHand().getType().equals(Material.FILLED_MAP))) {

                if (!p.getWorld().getName().startsWith(conf.getString("protection.place.map.ignore_world_prefix"))) {
                    if ((conf.getBoolean("protection.place.map.disable"))) {
                        e.getPlayer().sendMessage(ChatColor.YELLOW + "このワールドでは地図の貼り付けは禁止されています");
                        e.setCancelled(true);
                    }
                    if (conf.getBoolean("protection.place.map.logging")) {
                        StringBuilder sb = new StringBuilder("Map place[player=");
                        sb.append(p.getName());
                        sb.append("] worldname=");
                        sb.append(p.getWorld().getName());
                        sb.append(" loc=");
                        sb.append(p.getLocation().toString());
                        plg.getPluginLogger("maprej").log(sb);
                    }
                }
            }
        }
    }

    /**
     * エンティティブロック干渉イベント
     * @param e イベント
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void EntityBlockForm(EntityBlockFormEvent e) {
        // 水上歩行エンチャントの効果を許容するかどうか
        if (conf.getBoolean("protection.water_walking.disable")) {
            BlockState bs = e.getNewState();
            if (bs.getType() == Material.FROSTED_ICE) {
                if (conf.getArrayList("protection.water_walking.world_list").contains(bs.getWorld().getName())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    /**
     * 乗り物生成イベント
     * @param e イベント
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void VehicleCreate(VehicleCreateEvent e)
    {
        // ホッパーカートの生成を許容するかどうか
        if (conf.getBoolean("protection.create_hopper_cart.disable")) {
            if (e.getVehicle().getType() == EntityType.MINECART_HOPPER)
                e.getVehicle().remove();
        }
    }

    /**
     * スポーンイベント
     * @param e イベント
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void EntitySpawnEvent(EntitySpawnEvent e)
    {
        // ドラウンドの抑止判定
        if (e.getEntity() instanceof Drowned) {
            if (conf.getBoolean("spawn.drowned.disable")) {
                if (conf.getArrayList("spawn.drowned.world_list").contains(e.getEntity().getWorld().getName())) {
                    e.setCancelled(true);
                }
            }
        }
        // ファントムの抑止判定
        else if (e.getEntity() instanceof Phantom) {
            if (conf.getBoolean("spawn.phantom.disable")) {
                if (conf.getArrayList("spawn.phantom.world_list").contains(e.getEntity().getWorld().getName())) {
                    e.setCancelled(true);
                }
            }
        }
        // スケルトンホースの抑止判定
        else if (e.getEntity() instanceof SkeletonHorse) {
            if (conf.getBoolean("spawn.skeleton_horse.disable")) {
                if (conf.getArrayList("spawn.skeleton_horse.world_list").contains(e.getEntity().getWorld().getName())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    /**
     * inventoryクリックイベント
     * @param e イベント
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void InventoryClick(InventoryClickEvent e) {
        // クリエイティブでのインベントリ操作を許容するかどうか
        boolean allow_creative = conf.getBoolean("protection.inventory.creative_control");
        if (!allow_creative) {
            if (e.getWhoClicked() instanceof Player) {
                Player p = (Player) e.getWhoClicked();
                if (p.getGameMode() == GameMode.CREATIVE) {
                    if (!p.isOp()) {
                        e.setCancelled(true);
                        sendPluginMessage(plg, p, "クリエイティブにおけるインベントリ操作は抑止されています");
                    }
                }
            }
        }
    }
}
