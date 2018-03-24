
package jp.minecraftuser.ecoadmin.command;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * killコマンドクラス
 * @author ecolight
 */
public class KillCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public KillCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    /**
     * コマンド権限文字列設定
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "ecoadmin.kill";
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

        if (args.length >= 1) {
            // 他プレイヤー指定
            if (sender.hasPermission("ecoadmin.kill.other")) {
                Player target = null;
                for (Player s: plg.getServer().getOnlinePlayers()) {
                    if (s.getName().equals(args[0])) {
                        target = s;
                        break;
                    }
                }
                if (target != null) {
                    target.getInventory().clear();
                    target.setHealth(0);
                 } else {
                    sendPluginMessage(plg, sender, "指定したプレイヤー[{0}]は見つかりませんでした", args[0]);
                }
            } else {
                sendPluginMessage(plg, sender, "他人指定のコマンド使用権原がありません");
            }
        } else {
            // 自分指定
            if (!(sender instanceof Player)) {
                sendPluginMessage(plg, sender, "コンソールやブロックからはパラメタなしで実行できません");
            } else {
                Player p = (Player) sender;
                p.getInventory().clear();
                p.setHealth(0);
            }
        }
        return true;
    }
    
}
