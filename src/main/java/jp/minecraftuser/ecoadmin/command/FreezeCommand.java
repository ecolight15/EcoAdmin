
package jp.minecraftuser.ecoadmin.command;

import java.util.ArrayList;
import java.util.List;
import jp.minecraftuser.ecoadmin.listener.PlayerFreezeListener;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * freezeコマンドクラス
 * @author ecolight
 */
public class FreezeCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public FreezeCommand(PluginFrame plg_, String name_) {
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
        return "ecoadmin.freeze";
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
        
        PlayerFreezeListener pl = (PlayerFreezeListener) plg.getPluginListener("plfreeze");
        
        if (args.length >= 1) {
            // 他プレイヤー指定
            if (sender.hasPermission("ecoadmin.freeze.other")) {
                Player target = null;
                for (Player s: plg.getServer().getOnlinePlayers()) {
                    if (s.getName().equals(args[0])) {
                        target = s;
                        break;
                    }
                }
                if (target != null) {
                    if (!pl.isFreeze(target)) {
                        pl.setFreeze(target, true);
                        sendPluginMessage(plg, target, "プレイヤー[{0}]によって凍結状態にされました", sender.getName());
                        sendPluginMessage(plg, sender, "プレイヤー[{0}]を凍結状態にしました", target.getName());
                    } else {
                        pl.setFreeze(target, false);
                        sendPluginMessage(plg, target, "プレイヤー[{0}]によって凍結状態が解除されました", sender.getName());
                        sendPluginMessage(plg, sender, "プレイヤー[{0}]の凍結状態を解除しました", target.getName());
                    }
                } else {
                    sendPluginMessage(plg, sender, "指定したプレイヤー[{0}]は見つかりませんでした", args[0]);
                }
            } else {
                sendPluginMessage(plg, sender, "他人指定のコマンド使用権限がありません");
            }
        } else {
            // 自分指定
            if (!(sender instanceof Player)) {
                sendPluginMessage(plg, sender, "コンソールやブロックからはパラメタなしで実行できません");
            } else {
                Player p = (Player) sender;
                if (!pl.isFreeze(p)) {
                    pl.setFreeze(p, true);
                    sendPluginMessage(plg, p, "プレイヤー[{0}]を凍結状態にしました", p.getName());
                } else {
                    pl.setFreeze(p, false);
                    sendPluginMessage(plg, p, "プレイヤー[{0}]の凍結状態を解除しました", p.getName());
                }
            }
        }
        return true;
    }
    /**
     * コマンド別タブコンプリート処理
     * @param sender コマンド送信者インスタンス
     * @param cmd コマンドインスタンス
     * @param string コマンド文字列
     * @param strings パラメタ文字列配列
     * @return 保管文字列配列
     */
    @Override
    protected List<String> getTabComplete(CommandSender sender, Command cmd, String string, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            for (Player p : plg.getServer().getOnlinePlayers()) {
                list.add(p.getName());
            }
        }
        return list;
    }

}
