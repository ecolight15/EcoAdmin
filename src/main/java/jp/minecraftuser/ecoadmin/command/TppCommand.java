
package jp.minecraftuser.ecoadmin.command;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * tppコマンドクラス
 * @author ecolight
 */
public class TppCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public TppCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    /**
     * コマンド権限文字列設定
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "ecoadmin.tpp";
    }

    /**
     * 処理実行部
     * @param sender コマンド送信者
     * @param args パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        // パラメータチェック:3～4まで
        if (!checkRange(sender, args, 3, 4)) return true;

        if (args.length == 3) {
            // 座標指定テレポート
            Player p = (Player) sender;
            try {
                int x = Integer.parseInt(args[0]);
                int y = Integer.parseInt(args[1]);
                int z = Integer.parseInt(args[2]);
                p.teleport(new Location(p.getWorld(), x, y, z));
                sendPluginMessage(plg, sender, "world[{0}] のX={1} Y={2} Z={3} にテレポートしました", p.getWorld().getName(), Integer.toString(x), Integer.toString(y), Integer.toString(z));
            } catch (NumberFormatException e) {
                sendPluginMessage(plg, sender, "座標指定の数値解析中にエラーが発生しました");
                sendPluginMessage(plg, sender, e.getLocalizedMessage());
            }
        } else {
            World w = plg.getServer().getWorld(args[0]);
            if (w != null) {
                // ワールド座標指定テレポート
                try {
                    int x = Integer.parseInt(args[1]);
                    int y = Integer.parseInt(args[2]);
                    int z = Integer.parseInt(args[3]);
                    Player p = (Player) sender;
                    p.teleport(new Location(w, x, y, z));
                    sendPluginMessage(plg, sender, "world[{0}] のX={1} Y={2} Z={3} にテレポートしました", p.getWorld().getName(), Integer.toString(x), Integer.toString(y), Integer.toString(z));
                } catch (NumberFormatException e) {
                    sendPluginMessage(plg, sender, "座標指定の数値解析中にエラーが発生しました");
                    sendPluginMessage(plg, sender, e.getLocalizedMessage());
                }
            } else {
                // ワールド指定かつ取得失敗
                sendPluginMessage(plg, sender, "world[{0}] が見つかりませんでした");
            }
        }
        return true;
    }
    
}
