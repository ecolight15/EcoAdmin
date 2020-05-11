
package jp.minecraftuser.ecoadmin.command;

import jp.minecraftuser.ecoadmin.listener.PlayerConnectionListener;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.mergeStrings;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * stopコマンドクラス
 * @author ecolight
 */
public class StopCommand extends CommandFrame {
    private static int count = 0;
    private static BukkitRunnable runner;
    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public StopCommand(PluginFrame plg_, String name_) {
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
        return "ecoadmin.stop";
    }

    /**
     * 処理実行部
     * @param sender コマンド送信者
     * @param args パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        String lockmsg;
        // 多重停止抑止
        if (runner != null) {
            if (args.length == 0) {
                lockmsg = "サーバーの停止をキャンセルしました";
            } else {
                lockmsg = mergeStrings(args);
            }
            runner.cancel();
            runner = null;
            ((PlayerConnectionListener)plg.getPluginListener("plconnection")).setServerLockdown(null);
            sendPluginMessage(plg, null, lockmsg);
            return true;
        }

        // サーバー停止シーケンス処理
        if (args.length == 0) {
            lockmsg = "サーバーを停止します";
        } else {
            lockmsg = mergeStrings(args);
        }
        count = 10;
        runner = new BukkitRunnable() {
            @Override
            public void run() {
                switch (count) {
                    case 0:
                        // カウント 0 でkickする
                        plg.getPluginCommand("lock").execute(sender, args);
                        plg.getServer().savePlayers();
                        plg.getServer().getWorlds().listIterator().forEachRemaining(World::save);
                        break;
                    case -1: break;
                    case -2: break;
                    case -3: break;
                    case -4: break;
                    case -5:
                        // タイマー停止とシャットダウン
                        runner.cancel();
                        plg.getServer().shutdown();
                        break;
                    default:
                        // それ以外はカウント通知する
                        sendPluginMessage(plg, null, (count * 20) + " Tick後にサーバー停止します");
                        break;
                }
                count--;
            }
        };
        runner.runTaskTimer(plg, 0, 20);
        sendPluginMessage(plg, null, lockmsg);
        sendPluginMessage(plg, sender, "サーバー停止シーケンスを開始します");
        return true;
    }
    
}
