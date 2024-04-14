
package jp.minecraftuser.ecoadmin.command;

import java.util.ArrayList;
import java.util.List;
import jp.minecraftuser.ecoadmin.listener.PlayerConnectionListener;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.mergeStrings;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * lockコマンドクラス
 * @author ecolight
 */
public class LockdownCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public LockdownCommand(PluginFrame plg_, String name_) {
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
        return "ecoadmin.lock";
    }

    /**
     * 処理実行部
     * @param sender コマンド送信者
     * @param args パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        String lockmsg;
        boolean allow_op = conf.registerBoolean("cmd.lockdown.allow_op", false);
        
        if (args.length == 0) {
            lockmsg = "サーバーは現在ロック中です";
        } else {
            lockmsg = mergeStrings(args);
        }


        ((PlayerConnectionListener)plg.getPluginListener("plconnection")).setServerLockdown(lockmsg);

        //mqdbコマンドのsaveAndKickコマンドでkickする。
        String command = "mpdb saveAndkick";
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);

        sendPluginMessage(plg, sender, "サーバーを次の理由でロックダウンしました");
        sendPluginMessage(plg, sender, lockmsg);
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
            list.add("<reason of lockdown>");
        }
        return list;
    }

}
