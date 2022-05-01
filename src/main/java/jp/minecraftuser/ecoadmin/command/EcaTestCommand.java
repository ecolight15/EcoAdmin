
package jp.minecraftuser.ecoadmin.command;

import java.util.Map;
import java.util.Map.Entry;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * eca testコマンドクラス
 * @author ecolight
 */
public class EcaTestCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public EcaTestCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
//        setAuthBlock(true);
//        setAuthConsole(true);
    }

    /**
     * コマンド権限文字列設定
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "ecoadmin.test";
    }

    /**
     * 処理実行部
     * @param sender コマンド送信者
     * @param args パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        // パラメータチェック:0のみ
//        if (!checkRange(sender, args, 0, 0)) return true;

        // 現在地点の足元のブロック情報と両手の手持ちのアイテムの情報を返す
        Player p = (Player) sender;
        sendPluginMessage(plg, sender, "===== Test details ====");
        Location l = p.getLocation();
        l.add(0, -1, 0);
        Block b = l.getBlock();
        sendPluginMessage(plg, sender, "Block loc=[" + b.getLocation().toString() + "]" );
        sendPluginMessage(plg, sender, "Block type=" + b.getType().name());
        sendPluginMessage(plg, sender, "Block biome=" + b.getBiome().name());
        if (b.getBlockData() != null) sendPluginMessage(plg, sender, "Block data=" + b.getBlockData().getAsString());
        if (b.getBlockData().getMaterial() != null) sendPluginMessage(plg, sender, "Block data material=" + b.getBlockData().getMaterial().name());
        sendPluginMessage(plg, sender, "Block power=" + b.getBlockPower());
        sendPluginMessage(plg, sender, "Block old data=" + b.getData());
        sendPluginMessage(plg, sender, "Block humidity=" + b.getHumidity());
        sendPluginMessage(plg, sender, "Block light from blocks=" + b.getLightFromBlocks());
        sendPluginMessage(plg, sender, "Block light from sky=" + b.getLightFromSky());
        sendPluginMessage(plg, sender, "Block light level=" + b.getLightLevel());
        sendPluginMessage(plg, sender, "Block temperature=" + b.getTemperature());
        PlayerInventory ii = p.getInventory();
//        sendPluginMessage(plg, sender, "Item inventory name=" + ii.getName());
//        sendPluginMessage(plg, sender, "Item inventory title=" + ii.getTitle());
        if (ii.getType() != null) sendPluginMessage(plg, sender, "Item inventory type name=" + ii.getType().name());
        if (ii.getType() != null) sendPluginMessage(plg, sender, "Item inventory default name=" + ii.getType().getDefaultTitle());
        if (ii.getHelmet() != null) sendPluginMessage(plg, sender, "Player helmet=" + ii.getHelmet().getType());
        if (ii.getChestplate() != null) sendPluginMessage(plg, sender, "Player chestplate=" + ii.getChestplate().getType());
        if (ii.getLeggings() != null) sendPluginMessage(plg, sender, "Player leggings=" + ii.getLeggings().getType());
        if (ii.getBoots() != null) sendPluginMessage(plg, sender, "Player boots=" + ii.getBoots().getType());
        if (ii.getItemInMainHand() != null) sendPluginMessage(plg, sender, "Player main hand type=" + ii.getItemInMainHand().getType());
        if (ii.getItemInMainHand() != null) sendPluginMessage(plg, sender, "Player main hand string=" + ii.getItemInMainHand().toString());
        if (ii.getItemInOffHand() != null) sendPluginMessage(plg, sender, "Player off hand type=" + ii.getItemInOffHand().getType());
        if (ii.getItemInOffHand() != null) sendPluginMessage(plg, sender, "Player off hand string=" + ii.getItemInOffHand().toString());
        for(ItemStack i : ((Player)sender).getInventory().getContents()) {
            if (i != null) {
                printItemStack(sender, i.serialize());
            }
        }
        return true;
    }
    void printItemStack(CommandSender sender, Map<String, Object> objs) {
        for (Entry<String, Object> e : objs.entrySet()) {
            if (e.getValue() instanceof String) {
                sendPluginMessage(plg, sender, "k[" + e.getKey() + "]v[" + e.getValue() + "]");
            } else if (e.getValue() instanceof Map) {
                printItemStack(sender, (Map<String, Object>)e.getValue());
            }
        }
    }
}
