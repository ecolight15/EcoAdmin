
package jp.minecraftuser.ecoadmin.command;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * リロードコマンドクラス
 * @author ecolight
 */
public class HideCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public HideCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    /**
     * コマンド権限文字列設定
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "ecoadmin.hide";
    }

    /**
     * 処理実行部
     * @param sender コマンド送信者
     * @param args パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        // 後からログインしたユーザーに対して効かないがとりあえず実装は保留
        // ・mapで設定を記録、logout or showコマンドで設定解除、joinユーザーに対してhide実行させるのをそのうちやること
        // あと他プレイヤー指定対応も
        for (Player pl : plg.getServer().getOnlinePlayers()) {
            pl.hidePlayer(p);
        }
        sendPluginMessage(plg, sender, "他プレイヤーから隠れた状態になりました");
        return true;
    }
    
}
