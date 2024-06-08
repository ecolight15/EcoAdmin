
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

import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;

/**
 * lookposコマンドクラス
 *
 * @author ecolight
 */
public class LookPosCommand extends CommandFrame {

    /**
     * コンストラクタ
     *
     * @param plg_  プラグインインスタンス
     * @param name_ コマンド名
     */
    public LookPosCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    /**
     * コマンド権限文字列設定
     *
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "ecoadmin.lookpos";
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
        //executeコマンドはパラーメーターチェック除外(F3+Cでの座標コピー用)
        if (!(args.length >= 1 && args[0].equals("/execute") == true)) {
            // パラメータチェック:1～3
            if (!checkRange(sender, args, 1, 3)) return true;
        }
        Player player = (Player) sender;

        Location playerLoc = player.getLocation();
        //水平距離計算用
        Location playerSurfaceLoc = playerLoc.getBlock().getLocation();
        playerSurfaceLoc.setY(0);

        double x = 0;
        double y = player.getLocation().getY();//Y座標は指定されない限りプレイヤーのY座標を流用する
        double z = 0;

        boolean parseSuccess = false;

        try {
            if (args.length == 1) {
                //DynMapURLパース
                String text = args[0];
                if (text.contains("&x=")) {
                    String[] items = text.split("&");
                    for (String item : items) {
                        item = item.trim();
                        if (item.startsWith("x=")) {
                            x = Double.parseDouble(item.substring(2));
                            continue;
                        }
                        if (item.startsWith("z=")) {
                            z = Double.parseDouble(item.substring(2));
                            continue;
                        }
                    }
                    //パース処理完了
                    parseSuccess = true;
                } else {
                    //カンマでの座標指定の場合は配列に分割する(12,-34,56)
                    args = text.split(",");
                }
            }
            //DynMapパースに失敗した場合は座標直打ちとしてパースする
            if (parseSuccess == false) {

                if (args.length == 3) {
                    x = Double.parseDouble(args[0]);
                    y = Double.parseDouble(args[1]);
                    z = Double.parseDouble(args[2]);
                    parseSuccess = true;
                } else if (args.length == 2) {
                    x = Double.parseDouble(args[0]);
                    z = Double.parseDouble(args[1]);
                    parseSuccess = true;
                } else if (args.length >= 9) {
                    //F3+Cでの座標クリップボードコピーの場合(/execute in minecraft:overworld run tp @s 368.36 68.00 -2.65 293.97 17.85)
                    x = Double.parseDouble(args[6]);
                    z = Double.parseDouble(args[8]);
                    parseSuccess = true;
                }
            }
        } catch (NumberFormatException e) {
            sendPluginMessage(plg, sender, "引数が不正です");
            return true;
        }
        //パースに成功した場合
        if (parseSuccess == true) {
            //念のため値の上限をINTの最大or最小値にする
            x = Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, x));
            y = Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, y));
            z = Math.max(Integer.MIN_VALUE, Math.min(Integer.MAX_VALUE, z));
            Location targetLoc = new Location(player.getWorld(), x, y, z);

            //水平距離計算用
            Location targetSurfaceLoc = targetLoc.getBlock().getLocation();
            targetSurfaceLoc.setY(0);

            double distance = targetLoc.distance(playerLoc);
            double surfaceDistance = targetSurfaceLoc.distance(playerSurfaceLoc);

            Vector vector = targetLoc.toVector().subtract(playerLoc.toVector());
            Vector velocity = player.getVelocity();
            playerLoc.setDirection(vector);

            player.teleport(playerLoc);
            player.setVelocity(velocity);

            String target_near_gate_name = null;
            String link_gate_name = null;
            Location target_near_gate_location = null;
            Location link_gate_location = null;
            try {
                EcoGate plugin = (EcoGate) Bukkit.getPluginManager().getPlugin("EcoGate");
                EcoGateConfig ecoGateConfig = (EcoGateConfig) plugin.getDefaultConfig();
                LoaderGate gates = ecoGateConfig.getGates();
                target_near_gate_name = gates.nearGateSearch(targetLoc, true);
                target_near_gate_location = gates.getGateLocation(target_near_gate_name).clone();


                if (target_near_gate_name != null && !target_near_gate_name.equals("null")) {
                    link_gate_name = gates.getLinkGateName(target_near_gate_name);
                    link_gate_location = gates.getGateLocation(link_gate_name).clone();
                }
            } catch (Exception e) {
                return false;
            }

            //結果通知
            sendPluginMessage(plg, sender, "X[{0}] Y[{1}] Z[{2}]の方向を向きました。\n距離[{3}m] 水平距離[{4}m] 到達予想時間[{5}s]\n最寄りゲート[{6}] 接続先ゲート[{7}]]",
                    Integer.toString(targetLoc.getBlockX()),
                    Integer.toString(targetLoc.getBlockY()),
                    Integer.toString(targetLoc.getBlockZ()),
                    String.format("%.1f", distance),
                    String.format("%.1f", surfaceDistance),
                    String.format("%.1f", surfaceDistance / 33.66),
                    target_near_gate_name,
                    link_gate_name
            );
            return true;
        } else {
            sendPluginMessage(plg, sender, "引数が不正です");
            return true;
        }
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
            list.add("<x or dynmap URL or F3+C ClipBord>");
        } else if (strings.length == 2) {
            list.add("<y or z>");
        } else if (strings.length == 3) {
            list.add("<z>");
        }
        return list;
    }
}
