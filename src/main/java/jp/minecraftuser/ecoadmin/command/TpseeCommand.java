
package jp.minecraftuser.ecoadmin.command;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import jp.minecraftuser.ecoadmin.listener.TpseeListener;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * tpseeコマンドクラス
 * @author ecolight
 */
public class TpseeCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public TpseeCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    /**
     * コマンド権限文字列設定
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "ecoadmin.tpsee";
    }

    /**
     * 処理実行部
     * @param sender コマンド送信者
     * @param args パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        ((TpseeListener)plg.getPluginListerner("tpsee")).toggleTpsee((Player) sender);
        return true;
    }
    
}
