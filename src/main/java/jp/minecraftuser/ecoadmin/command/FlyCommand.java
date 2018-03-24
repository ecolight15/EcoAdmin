
package jp.minecraftuser.ecoadmin.command;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * flyコマンドクラス
 * @author ecolight
 */
public class FlyCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public FlyCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
        setAuthBlock(true);
        setAuthConsole(true);
    }

    /**
     * コマンド権限文字列設定
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "ecoadmin.fly";
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
            if (sender.hasPermission("ecoadmin.fly.other")) {
                Player target = null;
                for (Player s: plg.getServer().getOnlinePlayers()) {
                    if (s.getName().equals(args[0])) {
                        target = s;
                        break;
                    }
                }
                if (target != null) {
                    if (!target.getAllowFlight()) {
                        target.setAllowFlight(true);
                        target.setFlying(true);
                        sendPluginMessage(plg, target, "プレイヤー[{0}]に飛行権限を付与されました", sender.getName());
                        sendPluginMessage(plg, sender, "プレイヤー[{0}]に飛行権限を付与しました", target.getName());
                    } else {
                        target.setFlying(false);
                        target.setAllowFlight(false);
                        sendPluginMessage(plg, target, "プレイヤー[{0}]によって飛行権限が解除されました", sender.getName());
                        sendPluginMessage(plg, sender, "プレイヤー[{0}]の飛行権限を解除しました", target.getName());
                    }
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
                if (!p.getAllowFlight()) {
                    p.setAllowFlight(true);
                    p.setFlying(true);
                    sendPluginMessage(plg, p, "プレイヤー[{0}]に飛行権限を付与しました", p.getName());
                } else {
                    p.setFlying(false);
                    p.setAllowFlight(false);
                    sendPluginMessage(plg, p, "プレイヤー[{0}]に飛行権限を付与しました", p.getName());
                }
            }
        }
        return true;
    }
    
}
