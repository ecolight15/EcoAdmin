
package jp.minecraftuser.ecoadmin.command;

import jp.minecraftuser.ecoadmin.timer.LoginTimer;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * loginコマンドクラス
 * @author ecolight
 */
public class LoginCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public LoginCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    /**
     * コマンド権限文字列設定
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "ecoadmin.login";
    }

    /**
     * 処理実行部
     * @param sender コマンド送信者
     * @param args パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        // 告知
        LoginTimer t = new LoginTimer(plg, (Player) sender, "");
        t.run();
        return true;
    }
    
}
