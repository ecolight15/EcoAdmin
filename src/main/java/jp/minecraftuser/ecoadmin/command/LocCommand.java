
package jp.minecraftuser.ecoadmin.command;

import java.util.logging.Level;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * locコマンドクラス
 * @author ecolight
 */
public class LocCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public LocCommand(PluginFrame plg_, String name_) {
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
        return "ecoadmin.loc";
    }

    /**
     * 処理実行部
     * @param sender コマンド送信者
     * @param args パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        // パラメータチェック:0～1まで
        if (!checkRange(sender, args, 0, 1)) return true;

        if (args.length >= 1) {
            // 他プレイヤー指定
            if (sender.hasPermission("ecoadmin.loc.other")) {
                Player target = null;
                for (Player s: plg.getServer().getOnlinePlayers()) {
                    if (s.getName().equals(args[0])) {
                        target = s;
                        break;
                    }
                }
                if (target != null) {
                    // 対象ユーザーの座標を通知
                    log.log(Level.INFO, "[{0}]get location:{1}", new Object[]{target, target.getLocation().toString()});
                    // 結果通知
                    Location loc = target.getLocation();
                    sendPluginMessage(plg, sender, "ユーザー[{0}]の現在位置は world[{1}] X[{2}] Y[{3}] Z[{4}]",
                            target.getName(), loc.getWorld().getName(),
                            Integer.toString(loc.getBlockX()), Integer.toString(loc.getBlockY()), Integer.toString(loc.getBlockZ()));
                 } else {
                    sendPluginMessage(plg, sender, "指定したプレイヤー[{0}]は見つかりませんでした", args[0]);
                }
            } else {
                sendPluginMessage(plg, sender, "他人指定のコマンド使用権限がありません");
            }
        } else {
            // 自分指定
            if (!(sender instanceof Player)) {
                sendPluginMessage(plg, sender, "コンソールやブロックからはパラメタなしで実行できません");
            } else {
                Player p = (Player) sender;
                // 対象ユーザーの座標を通知
                log.log(Level.INFO, "[{0}]get location:{1}", new Object[]{p, p.getLocation().toString()});
                // 結果通知
                Location loc = p.getLocation();
                sendPluginMessage(plg, sender, "ユーザー[{0}]の現在位置は world[{1}] X[{2}] Y[{3}] Z[{4}]",
                        p.getName(), loc.getWorld().getName(),
                        Integer.toString(loc.getBlockX()), Integer.toString(loc.getBlockY()), Integer.toString(loc.getBlockZ()));
            }
        }
        return true;
    }
    
}
