
package jp.minecraftuser.ecoadmin.command;

import java.util.ArrayList;
import java.util.List;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * pvpコマンドクラス
 * @author ecolight
 */
public class PvPCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public PvPCommand(PluginFrame plg_, String name_) {
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
        return "ecoadmin.pvp";
    }

    /**
     * 処理実行部
     * @param sender コマンド送信者
     * @param args パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        World w = null;
        if (args.length == 0) {
            if (sender instanceof Player) {
                w = ((Player) sender).getWorld();
            } else {
                log.info("ワールド名の指定が必要です");
            }
        } else {
            w = plg.getServer().getWorld(args[0]);
        }
        if (w != null) {
            if (w.getPVP()) {
                w.setPVP(false);
                sendPluginMessage(plg, sender, "ワールド[{0}]のPvP設定を無効にしました", w.getName());
            } else {
                w.setPVP(true);
                sendPluginMessage(plg, sender, "ワールド[{0}]のPvP設定を有効にしました", w.getName());
            }
        } else {
            log.info("指定されたワールドが不正です");
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
            for (World w : plg.getServer().getWorlds()) {
                list.add(w.getName());
            }
        }
        return list;
    }

}
