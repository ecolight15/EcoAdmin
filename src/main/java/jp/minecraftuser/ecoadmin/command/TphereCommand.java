
package jp.minecraftuser.ecoadmin.command;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * tphereコマンドクラス
 * @author ecolight
 */
public class TphereCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public TphereCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    /**
     * コマンド権限文字列設定
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "ecoadmin.tphere";
    }

    /**
     * 処理実行部
     * @param sender コマンド送信者
     * @param args パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        // パラメータチェック:1～2まで
        if (!checkRange(sender, args, 1, 2)) return true;

        Player target = null;
        for (Player s: plg.getServer().getOnlinePlayers()) {
            if (s.getName().equals(args[0])) {
                target = s;
                break;
            }
        }
        if (target != null) {
            Player player = (Player) sender;
            if (args.length > 1) {
                Location loc = player.getLocation();
                try {
                    loc.setY(loc.getY() + Integer.parseInt(args[1]));
                    target.teleport(loc);
                } catch (NumberFormatException e) {
                    target.teleport(player);
                }
            } else {
                target.teleport(player);
            }
            sendPluginMessage(plg, target, "player[{0}] の位置にテレポートしました", player.getName());
            sendPluginMessage(plg, sender, "target[{0}] を現在の位置にテレポートしました", target.getName());
        } else {
            sendPluginMessage(plg, sender, "指定したプレイヤーが見つかりませんでした");
        }
        return true;
    }
    
}
