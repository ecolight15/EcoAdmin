
package jp.minecraftuser.ecoadmin.command;

import java.util.HashMap;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * backコマンドクラス
 * @author ecolight
 */
public class BackCommand extends CommandFrame {
    private static HashMap<Player, Location> backMap;

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public BackCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
        backMap = new HashMap<>();
    }

    /**
     * コマンド権限文字列設定
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "ecoadmin.back";
    }

    /**
     * 処理実行部
     * @param sender コマンド送信者
     * @param args パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        // パラメータチェック:0のみ
        if (!checkRange(sender, args, 0, 0)) return true;

        if (backMap.containsKey((Player)sender)) {
            ((Player)sender).teleport(backMap.get((Player)sender));
            sendPluginMessage(plg, sender, "直前のテレポート位置に戻りました");
        } else {
            sendPluginMessage(plg, sender, "直前のテレポート記録がありません");
        }
        return true;
    }
    
    /**
     * プレイヤー最終テレポート位置保存処理
     * テレポート成功時、本メソッドでテレポート先のロケーションを登録すること
     * @param p プレイヤーインスタンス
     * @param loc 最終テレポート位置ロケーション
     */
    public void registerLoc(Player p, Location loc) {
        backMap.put(p, loc);
    }
    
}
