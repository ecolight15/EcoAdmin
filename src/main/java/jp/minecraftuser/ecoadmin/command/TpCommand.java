
package jp.minecraftuser.ecoadmin.command;

import java.util.ArrayList;
import java.util.List;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * tpコマンドクラス
 * @author ecolight
 */
public class TpCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public TpCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    /**
     * コマンド権限文字列設定
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "ecoadmin.tp";
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
                Location loc = target.getLocation();
                try {
                    loc.setY(loc.getY() + Integer.parseInt(args[1]));
                    player.teleport(loc);
                } catch (NumberFormatException e) {
                    player.teleport(target);
                }
            } else {
                player.teleport(target);
            }
            sendPluginMessage(plg, sender, "player[{0}] の位置にテレポートしました", target.getName());
        } else {
            sendPluginMessage(plg, sender, "指定したプレイヤーが見つかりませんでした");
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
                if (p.getName().toLowerCase().startsWith(strings[0].toLowerCase())) {
                    list.add(p.getName());
                }
            }
        } else if (strings.length == 2) {
                list.add("[height]");
        }
        return list;
    }

}
