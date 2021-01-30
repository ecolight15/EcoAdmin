
package jp.minecraftuser.ecoadmin.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * killコマンドクラス
 * @author ecolight
 */
public class KillCommand extends CommandFrame {
    private HashMap<CommandSender, String[]> params;

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public KillCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
        params = new HashMap<>();
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
                sendPluginMessage(plg, sender, "Player[{0}]をkillしますがよろしいですか？", args[0]);
                params.put(sender, args);
                confirm(sender);
            } else {
                sendPluginMessage(plg, sender, "他人指定のコマンド使用権限がありません");
            }
        } else {
            // 自分指定
            if (!(sender instanceof Player)) {
                sendPluginMessage(plg, sender, "コンソールやブロックからはパラメタなしで実行できません");
            } else {
                sendPluginMessage(plg, sender, "killすると所持アイテムが消滅しますがよろしいですか？");
                params.put(sender, args.clone());
                confirm(sender);
            }
        }
        return true;
    }

    /**
     * accept
     * @param sender 
     */
    @Override
    protected void acceptCallback(CommandSender sender) {
        String args[] = params.get(sender);
        if (args.length >= 1) {
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
                sendPluginMessage(plg, sender, "Player[{0}]をkillしました", args[0]);
             } else {
                sendPluginMessage(plg, sender, "指定したプレイヤー[{0}]は見つかりませんでした", args[0]);
            }
        } else {
            Player p = (Player) sender;
            p.getInventory().clear();
            p.setHealth(0);
        }        
    }

    /**
     * cancel
     * @param sender 
     */
    @Override
    protected void cancelCallback(CommandSender sender) {
        sendPluginMessage(plg, sender, "killコマンドをキャンセルしました");
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
        }
        return list;
    }

}
