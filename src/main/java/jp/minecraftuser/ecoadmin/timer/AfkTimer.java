package jp.minecraftuser.ecoadmin.timer;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.TimerFrame;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * セーブタイマー
 * @author ecolight
 */
public class AfkTimer extends TimerFrame {
    private final HashMap<UUID, Long> afkmap;
    private final HashMap<UUID, Location> locmap;
    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ 名前
     */
    public AfkTimer(PluginFrame plg_, String name_) {
        super(plg_, name_);
        afkmap = new HashMap<>();
        locmap = new HashMap<>();
    }

    /**
     * 非同期処理メイン
     */
    @Override
    public void run()
    {
        // オンラインユーザーを全員走査し、規定時間を超えていたらkickする
        long now = Calendar.getInstance().getTime().getTime();
        for (Player p : plg.getServer().getOnlinePlayers()) {
            if (afkmap.containsKey(p.getUniqueId())) {
                // ロケーションチェック
                // 元の位置と変化がない場合のみ時間のチェックをする
                if (p.getLocation().equals(locmap.get(p.getUniqueId()))) {
                    // 時間格納有り
                    if (now - afkmap.get(p.getUniqueId()) >= conf.getLong("util.afk.limit_milliseconds")) {
                        // 規定時間を超えた
                        p.kickPlayer(conf.getString("util.afk.kick_message"));
                        afkmap.remove(p.getUniqueId());
                        locmap.remove(p.getUniqueId());
                    }
                    // または位置は変わっていないが、まだ規定時間を超えない
                    continue;
                }
            }
            // 現在の時間でログを開始する
            afkmap.remove(p.getUniqueId());
            locmap.remove(p.getUniqueId());
            afkmap.put(p.getUniqueId(), now);
            locmap.put(p.getUniqueId(), p.getLocation());
        }
        // ログアウト済みユーザーの情報消去は検索コストがかかるので要検討
        // 現時点は未実装とし、再ログイン後は継続カウントとする
    }
}
