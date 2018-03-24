
package jp.minecraftuser.ecoadmin.command;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * spawnコマンドクラス
 * @author ecolight
 */
public class SpawnCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public SpawnCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    /**
     * コマンド権限文字列設定
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "ecoadmin.spawn";
    }

    /**
     * 処理実行部
     * @param sender コマンド送信者
     * @param args パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        // パラメータチェック:0～1まで
        if (!checkRange(sender, args, 0, 1)) return true;

        Player p = (Player) sender;
        if (args.length == 0) {
            // テレポート
            World w = p.getWorld();
            p.teleport(w.getSpawnLocation());
            log.info("[" + p.getName() + "]" + "teleport ["+w.getName()+"] spawn location");
            sendPluginMessage(plg, sender, "ワールド[{0}]のスポーン位置へテレポートしました" , p.getWorld().getName());
        } else {
            // 他ワールド指定
            if (p.hasPermission("ecoadmin.spawn.other")) {
                // テレポート
                World w = plg.getServer().getWorld(args[0]);
                p.teleport(w.getSpawnLocation());
                log.info("[" + p.getName() + "]" + "teleport ["+w.getName()+"] spawn location");
                sendPluginMessage(plg, sender, "ワールド[{0}]のスポーン位置へテレポートしました" , p.getWorld().getName());
            } else {
                p.sendMessage(ChatColor.YELLOW + "[" + plg.getName() + "] コマンドのワールド指定の使用権限がありません。");
            }
        }
        return true;
    }
    
}
