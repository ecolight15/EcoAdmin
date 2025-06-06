
package jp.minecraftuser.ecoadmin.command;

import java.util.ArrayList;
import java.util.List;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * strikeコマンドクラス
 * @author ecolight
 */
public class StrikeCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public StrikeCommand(PluginFrame plg_, String name_) {
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
        return "ecoadmin.strike";
    }

    /**
     * 処理実行部
     * @param sender コマンド送信者
     * @param args パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        // パラメータチェック:0～2まで
        if (!checkRange(sender, args, 0, 2)) return true;

        if (args.length >= 1) {
            // 他プレイヤー指定
            if (sender.hasPermission("ecoadmin.strike.other")) {
                Player target = null;
                for (Player s: plg.getServer().getOnlinePlayers()) {
                    if (s.getName().equals(args[0])) {
                        target = s;
                        break;
                    }
                }
                if (target != null) {
                    // 雷のタイプを決定
                    String type = "dumy"; // デフォルトは偽雷
                    if (args.length >= 2) {
                        if ("real".equals(args[1])) {
                            type = "real";
                        } else if ("dumy".equals(args[1])) {
                            type = "dumy";
                        } else {
                            // 無効な引数の場合はdumyとして扱う
                            type = "dumy";
                        }
                    }
                    
                    // 雷を落とす
                    if ("real".equals(type)) {
                        target.getWorld().strikeLightning(target.getLocation());
                    } else {
                        target.getWorld().strikeLightningEffect(target.getLocation());
                    }
                    target.damage(0);
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
                p.getWorld().strikeLightningEffect(p.getLocation());
                p.damage(0);
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
                if (p.getName().toLowerCase().startsWith(strings[0].toLowerCase())) {
                    list.add(p.getName());
                }
            }
        } else if (strings.length == 2) {
            if ("dumy".toLowerCase().startsWith(strings[1].toLowerCase())) {
                list.add("dumy");
            }
            if ("real".toLowerCase().startsWith(strings[1].toLowerCase())) {
                list.add("real");
            }
        }
        return list;
    }

}
