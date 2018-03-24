
package jp.minecraftuser.ecoadmin.command;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * setspawnコマンドクラス
 * @author ecolight
 */
public class SetspawnCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public SetspawnCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    /**
     * コマンド権限文字列設定
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "ecoadmin.setspawn";
    }

    /**
     * 処理実行部
     * @param sender コマンド送信者
     * @param args パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        // 現在位置に現在のワールドスポーン設定
        Player p = (Player) sender;
        Location loc = p.getLocation();
        p.getWorld().setSpawnLocation(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ());
        // 結果通知
        log.info("[" + p.getName() + "]" + "setspawn location " + loc.toString());
        sendPluginMessage(plg, sender, "ワールド[{0}]のスポーン位置を設定しました", loc.getWorld().getName());
        return true;
    }
    
}
