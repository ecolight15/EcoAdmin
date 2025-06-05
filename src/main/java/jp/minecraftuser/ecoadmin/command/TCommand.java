
package jp.minecraftuser.ecoadmin.command;

import java.util.ArrayList;
import java.util.List;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import jp.minecraftuser.ecoadmin.listener.TListener;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * thorコマンドクラス
 * @author ecolight
 */
public class TCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public TCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    /**
     * コマンド権限文字列設定
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "ecoadmin.thor";
    }

    /**
     * 処理実行部
     * @param sender コマンド送信者
     * @param args パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        // プレイヤーのみ実行可能
        if (!(sender instanceof Player)) {
            sendPluginMessage(plg, sender, "このコマンドはプレイヤーのみ実行できます");
            return true;
        }
        
        Player player = (Player) sender;
        String type = "dumy"; // デフォルトはエフェクトのみ
        
        // 引数処理
        if (args.length >= 1) {
            if ("real".equals(args[0])) {
                type = "real";
            } else if ("dumy".equals(args[0])) {
                type = "dumy";
            } else {
                // 無効な引数の場合はdumyとして扱う
                type = "dumy";
            }
        }
        
        ((TListener)plg.getPluginListener("t")).toggleStrike(player, type);
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
            if ("dumy".toLowerCase().startsWith(strings[0].toLowerCase())) {
                list.add("dumy");
            }
            if ("real".toLowerCase().startsWith(strings[0].toLowerCase())) {
                list.add("real");
            }
        }
        return list;
    }
    
}
