
package jp.minecraftuser.ecoadmin.command;

import jp.minecraftuser.ecoframework.CommandFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;

/**
 * lookコマンドクラス
 *
 * @author ecolight
 */
public class LookCommand extends CommandFrame {

    /**
     * コンストラクタ
     *
     * @param plg_  プラグインインスタンス
     * @param name_ コマンド名
     */
    public LookCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    /**
     * コマンド権限文字列設定
     *
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "ecoadmin.look";
    }

    /**
     * 処理実行部
     *
     * @param sender コマンド送信者
     * @param args   パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        // パラメータチェック:1～2まで
        if (!checkRange(sender, args, 1, 2)) return true;
        String arg = "";
        if (args.length >= 2) {
            arg = args[1];
        }
        Player player = (Player) sender;

        Player target = null;

        for (Player s : plg.getServer().getOnlinePlayers()) {
            if (s.getName().equals(args[0])) {
                target = s;
                break;
            }
        }

        if (target != null) {
            if (player == target) {
                sendPluginMessage(plg, sender, "自分を指定することは出来ません");
                return true;
            }
            // 対象ユーザーの座標を通知
            log.log(Level.INFO, "[{0}]look location:{1}", new Object[]{target, target.getLocation().toString()});

            Location targetLoc = target.getLocation();
            Location playerLoc = player.getLocation();
            //水平距離用
            Location targetSurfaceLoc = targetLoc.getBlock().getLocation();
            Location playerSurfaceLoc = playerLoc.getBlock().getLocation();
            targetSurfaceLoc.setY(0);
            playerSurfaceLoc.setY(0);

            if (player.getWorld() == target.getWorld()) {

                Double distance = targetLoc.distance(playerLoc);
                Double surfaceDistance = targetSurfaceLoc.distance(playerSurfaceLoc);

                Vector vector = targetLoc.toVector().subtract(playerLoc.toVector());
                Vector velocity= player.getVelocity();
                playerLoc.setDirection(vector);

                if (arg.equals("") || arg.equals("0")) {
                    //引数無し or 0 ならピッチは変化させない
                    playerLoc.setPitch(player.getLocation().getPitch());
                }

                player.teleport(playerLoc);
                player.setVelocity(velocity);
                // 結果通知
                sendPluginMessage(plg, sender, "ユーザー[{0}]の方向を向きました world[{1}] X[{2}] Y[{3}] Z[{4}] 距離[{5}m] 水平距離[{6}m]",
                        target.getName(), targetLoc.getWorld().getName(),
                        Integer.toString(targetLoc.getBlockX()), Integer.toString(targetLoc.getBlockY()), Integer.toString(targetLoc.getBlockZ()),
                        String.format("%.1f", distance),
                        String.format("%.1f", surfaceDistance)
                );

            } else {
                sendPluginMessage(plg, sender, "ユーザー[{0}]は同じワールドにいません world[{1}] X[{2}] Y[{3}] Z[{4}]",
                        target.getName(), targetLoc.getWorld().getName(),
                        Integer.toString(targetLoc.getBlockX()), Integer.toString(targetLoc.getBlockY()), Integer.toString(targetLoc.getBlockZ()));
            }
        } else {
            sendPluginMessage(plg, sender, "指定したプレイヤー[{0}]は見つかりませんでした", args[0]);
        }
        return true;
    }

    /**
     * コマンド別タブコンプリート処理
     *
     * @param sender  コマンド送信者インスタンス
     * @param cmd     コマンドインスタンス
     * @param string  コマンド文字列
     * @param strings パラメタ文字列配列
     * @return 保管文字列配列
     */
    @Override
    protected List<String> getTabComplete(CommandSender sender, Command cmd, String string, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            for (Player p : plg.getServer().getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(strings[0].toLowerCase())) {
                    list.add(p.getName());
                }
            }
        }
        return list;
    }

}
