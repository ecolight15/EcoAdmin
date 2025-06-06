
package jp.minecraftuser.ecoadmin.command;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * showコマンドクラス
 * @author ecolight
 */
public class ShowCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public ShowCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    /**
     * コマンド権限文字列設定
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "ecoadmin.show";
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
        // プレイヤーを隠れた状態から除外
        HideCommand.getHiddenPlayers().remove(p.getUniqueId());
        
        // 現在オンラインの全プレイヤーに対してshowPlayerを実行
        for (Player pl : plg.getServer().getOnlinePlayers()) {
            pl.showPlayer(plg, p);
        }
        sendPluginMessage(plg, sender, "他プレイヤーから見える状態になりました");
        return true;
    }
    
}
