
package jp.minecraftuser.ecoadmin.command;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.HashSet;
import java.util.Set;

/**
 * リロードコマンドクラス
 * @author ecolight
 */
public class HideCommand extends CommandFrame {
    
    // 隠れた状態のプレイヤーを記録するためのセット
    private static Set<String> hiddenPlayers = new HashSet<>();

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
        // プレイヤーを隠れた状態として記録
        hiddenPlayers.add(p.getName());
        
        // 現在オンラインの全プレイヤーに対してhidePlayerを実行
        for (Player pl : plg.getServer().getOnlinePlayers()) {
            pl.hidePlayer(plg, p);
        }
        sendPluginMessage(plg, sender, "他プレイヤーから隠れた状態になりました");
        return true;
    }
    
    /**
     * 隠れた状態のプレイヤー一覧を取得
     * @return 隠れた状態のプレイヤー名のセット
     */
    public static Set<String> getHiddenPlayers() {
        return hiddenPlayers;
    }
    
}
