
package jp.minecraftuser.ecoadmin.command;

import jp.minecraftuser.ecoframework.CommandFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecogate.EcoGate;
import jp.minecraftuser.ecogate.config.EcoGateConfig;
import jp.minecraftuser.ecogate.config.LoaderGate;
import org.bukkit.Bukkit;
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
            String near_gate_name = null;
            String link_gate_name = null;
            Location near_gate_location = null;
            Location link_gate_location = null;
            try {
                EcoGate plugin = (EcoGate) Bukkit.getPluginManager().getPlugin("EcoGate");
                EcoGateConfig ecoGateConfig = (EcoGateConfig) plugin.getDefaultConfig();
                LoaderGate gates = ecoGateConfig.getGates();
                near_gate_name = gates.nearGateSearch(targetLoc, true);
                near_gate_location = gates.getGateLocation(near_gate_name).clone();


                if (near_gate_name != null && !near_gate_name.equals("null")) {
                    link_gate_name = gates.getLinkGateName(near_gate_name);
                    link_gate_location = gates.getGateLocation(link_gate_name).clone();
                }
            } catch (Exception e) {
                return false;
            }
            //プレイヤーとの直接距離
            Double player_distance = null;
            //プレイヤーとの直接距離(水平距離)
            Double player_surface_distance = null;
            //ゲートを経由した場合の距離
            Double gate_distance = null;
            //linkゲートまでの距離
            Double link_gate_distance = null;

            if (player.getWorld() == target.getWorld()) {
                player_distance = targetLoc.distance(playerLoc);
                player_surface_distance = targetSurfaceLoc.distance(playerSurfaceLoc);
            }
            //接続先ゲートが自分のワールドと同じ場合
            if (link_gate_location != null && playerLoc.getWorld() == link_gate_location.getWorld()) {
                link_gate_distance = playerLoc.distance(link_gate_location);
                gate_distance = link_gate_distance + targetLoc.distance(near_gate_location);
            }

            if (gate_distance != null && player_distance != null && gate_distance < player_distance && link_gate_distance < 100) {
                //ゲートを経由したほうが距離が短く かつリンクゲートが100m以内の場合
                link_gate_location.add(0.5, 0, 0.5);
                Vector vector = link_gate_location.toVector().subtract(playerLoc.toVector());
                Vector velocity = player.getVelocity();
                playerLoc.setDirection(vector);

                player.teleport(playerLoc);
                player.setVelocity(velocity);

                sendPluginMessage(plg, sender, "§c§l§n最も近い接続先ゲート[{7}]の方向を向きました。§r\nユーザー[{0}] world[{1}] X[{2}] Y[{3}] Z[{4}]距離[{5}m] \n最寄りゲート[{6}] 接続先ゲート[{7}]\n接続先ゲートworld[{8}] X[{9}] Y[{10}] Z[{11}] 距離[{12}m] \n到達予想時間[{13}s] ",
                        target.getName(),
                        targetLoc.getWorld().getName(),
                        Integer.toString(targetLoc.getBlockX()),
                        Integer.toString(targetLoc.getBlockY()),
                        Integer.toString(targetLoc.getBlockZ()),
                        String.format("%.1f", player_distance),
                        near_gate_name,
                        link_gate_name,
                        link_gate_location.getWorld().getName(),
                        Integer.toString(link_gate_location.getBlockX()),
                        Integer.toString(link_gate_location.getBlockY()),
                        Integer.toString(link_gate_location.getBlockZ()),
                        String.format("%.1f", gate_distance),
                        String.format("%.1f", gate_distance / 33.66));
            } else if (player_distance != null) {
                //上記以外
                Vector vector = targetLoc.toVector().subtract(playerLoc.toVector());
                Vector velocity = player.getVelocity();
                playerLoc.setDirection(vector);

                if (arg.isEmpty() || arg.equals("0")) {
                    //引数無し or 0 ならピッチを復元する
                    playerLoc.setPitch(player.getLocation().getPitch());
                }

                player.teleport(playerLoc);
                player.setVelocity(velocity);

                sendPluginMessage(plg, sender, "ユーザー[{0}]の方向を向きました\nworld[{1}] X[{2}] Y[{3}] Z[{4}] 距離[{5}m] 水平距離[{6}m]\n到達予想時間[{7}s] \n最寄りゲート[{8}] 接続先ゲート[{9}]",
                        target.getName(),
                        targetLoc.getWorld().getName(),
                        Integer.toString(targetLoc.getBlockX()),
                        Integer.toString(targetLoc.getBlockY()),
                        Integer.toString(targetLoc.getBlockZ()),
                        String.format("%.1f", player_distance),
                        String.format("%.1f", player_surface_distance),
                        String.format("%.1f", player_distance / 33.66),
                        near_gate_name,
                        link_gate_name);

            } else {
                sendPluginMessage(plg, sender, "ユーザー[{0}]は同じワールドにいません\nworld[{1}] X[{2}] Y[{3}] Z[{4}]\n最寄りゲート[{5}] 接続先ゲート[{6}]",
                        target.getName(),
                        targetLoc.getWorld().getName(),
                        Integer.toString(targetLoc.getBlockX()),
                        Integer.toString(targetLoc.getBlockY()),
                        Integer.toString(targetLoc.getBlockZ()),
                        near_gate_name,
                        link_gate_name);
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
