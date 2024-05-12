
package jp.minecraftuser.ecoadmin.listener;

import java.util.ArrayList;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoadmin.timer.LoginTimer;
import jp.minecraftuser.ecoframework.TimerFrame;
import jp.minecraftuser.ecoframework.Utl;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * プレイヤー接続切断イベント処理リスナークラス
 * @author ecolight
 */
public class PlayerConnectionListener extends ListenerFrame {
    private static ArrayList<String> denylist;
    private static String lockdown = null;

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ 名前
     */
    public PlayerConnectionListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        denylist = new ArrayList<>();
    }

    /**
     * プレイヤー接続終了イベントハンドラ
     * @param e イベント
     */
    @EventHandler(priority=EventPriority.LOWEST)
    public void PlayerQuit(PlayerQuitEvent e) {
        // ログアウト時に強制的に乗り物から下ろしておく
        if (conf.getBoolean("security.logout.forced_takedown_from_vehicle")) {
            Player pl = e.getPlayer();
            // 乗物からおろしとく
            if (pl == null) return;
            if (pl.isInsideVehicle()) {
                pl.getVehicle().eject();
            }
        }
    }

    /**
     * プレイヤーKICKインイベント処理
     * @param e イベント情報
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void PlayerKick(PlayerKickEvent e) {
        Player p = e.getPlayer();
        String s = e.getReason();
        String host = p.getPlayer().getAddress().getAddress().toString();
        
        // ホスト表示除外、ログイン抑止の対象外かどうかチェックする
        boolean hit = false;
        for (String ignore : conf.getArrayList("security.kick.ignore_reason_announce")) {
            if (s.contains(ignore)) {
                hit = true;
                break;
            }
        }
        if (s.equalsIgnoreCase(conf.getString("util.afk.kick_message"))) hit = true;

        // KICK時の通知が有効であればブロードキャスト通報する
        if (conf.getBoolean("security.kick.reason_announce")) {
            Utl.sendPluginMessage(plg, null, "プレイヤー[{0}]は、次の理由でサーバーから強制切断されました。", p.getName());
            Utl.sendPluginMessage(plg, null, s);
            if (!hit) {
                if (conf.getBoolean("security.kick.reason_announce_plus_host")) {
                    // その他表示対象外の指定があれば表示しない
                    if (!hit) {
                        Utl.sendPluginMessage(plg, null, "host:[{0}]", host);
                    }
                }
            }
        }

        // キックしたユーザーをしばらくログインさせない場合はリストに登録しておきLOGINイベント時に照合する
        if (!hit) {
            if (conf.getBoolean("security.kick.until_server_stop")) {
                if (!denylist.contains(p.getAddress().getAddress().getHostAddress())) {
                    denylist.add(p.getAddress().getAddress().getHostAddress());
                }
            }
        }

        // ログ有効の場合はログしておく
        if (conf.getBoolean("security.kick.logging")) {
            StringBuilder sb = new StringBuilder(p.getName());
            sb.append(" : ");
            sb.append(host);
            sb.append(" : ");
            sb.append(s);
            plg.getPluginLogger("kick").log(sb);
        }
    }

    /**
     * プレイヤーログインイベントハンドラ
     * @param e イベント情報
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerLogin(PlayerLoginEvent e) {
        // lockdown中の場合はログイン抑止する
        if (lockdown != null) {
            if (!conf.getBoolean("cmd.lockdown.allow_op") || !e.getPlayer().isOp()) {
                e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                e.setKickMessage(lockdown);
                return;
            }
        }

        // Kickしたユーザーをしばらくサーバーログインさせない設定がされている場合は蹴り出す
        // ★1.13-R0.1にて、なぜか本体側からだけgetRealAddressが削除されてるので代替手段が見つかるまでコメントアウト
        if (e != null) {
            if (e.getRealAddress() != null) {
                if (denylist.contains(e.getRealAddress().getHostAddress())) {
                    e.disallow(PlayerLoginEvent.Result.KICK_OTHER, "あなたの接続は一時的に制限されています");
                    return;
                }
            }
        } else {
            plg.getServer().broadcastMessage("Player Login event is null");
        }
        //plg.getServer().broadcastMessage("エリトラ異常飛行チェック解除");

        //エリトラの飛行チエックを無効化
        for (World w : plg.getServer().getWorlds()) {
            w.setGameRule(GameRule.DISABLE_ELYTRA_MOVEMENT_CHECK,true);
        }
        //ログイン開始から10秒間(200tick)の間はエリトラの移動チェックを無効化
        new TimerFrame(plg,name){
            @Override
            public void run() {
                for (World w:plg.getServer().getWorlds()) {
                    w.setGameRule(GameRule.DISABLE_ELYTRA_MOVEMENT_CHECK,false);
                }
                //plg.getServer().broadcastMessage("エリトラ異常飛行チェック再開");
            }
        }.runTaskLater(plg,200);
    }

    /**
     * プレイヤーサーバー参加イベントハンドラ
     * @param e 
     */
    @EventHandler(priority=EventPriority.LOWEST)
    public void PlayerJoin(PlayerJoinEvent e) {
        Player pl = e.getPlayer();

        // ログイン時IPアドレス出力
        if (conf.getBoolean("security.join.show_partof_ipaddress")) {
            String host = pl.getAddress().getHostName();
            log.info("getHostName:"+host);
            StringBuilder ip = new StringBuilder();
            int count = 0;
            for (String s: pl.getAddress().getAddress().getHostAddress().split("\\.")) {
                if (count == 3) {
                    ip.append("***");
                    break;
                }
                ip.append(s);
                ip.append(".");
                count++;
            }
            StringBuilder sb = new StringBuilder("§7[全体通知]ユーザー確認用IP:");
            sb.append(ip.toString());
            plg.getServer().broadcastMessage(sb.toString());
        }

        // ログアウト時の定義だがJOIN時にも確認しておく
        if (conf.getBoolean("security.logout.forced_takedown_from_vehicle")) {
            // 乗り物に乗ってたら降ろす
            if (pl.isInsideVehicle()) {
                pl.getVehicle().eject();
            }
        }

        // OP以外の場合、接続時に強制的に飛行状態は解除しておく
        if (conf.getBoolean("security.join.force_disable_flight_mode")) {
            if (!pl.isOp()) {
                if (pl.getAllowFlight()) {
                    pl.setFlying(false);
                    pl.setAllowFlight(false);
                }
            }
        }
        
        // 告知
        if (conf.getBoolean("fun.login_message")) {
            new LoginTimer(plg, pl, "login").runTaskLater(plg, 20L);
        }
    }

    /**
     * サーバーの入場規制メッセージ設定
     * null設定の場合は規制無し
     * @param message メッセージ
     */
    public void setServerLockdown(String message) {
        lockdown = message;
    }

}
