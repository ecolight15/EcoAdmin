package jp.minecraftuser.ecoadmin.timer;

import java.text.SimpleDateFormat;
import java.util.Date;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.TimerFrame;
import org.bukkit.command.CommandSender;

/**
 * TPS測定タイマークラス
 * @author ecolight
 */
public class CorrectionTimer extends TimerFrame {
    private static long prev = 0L;
    private static long reprev = 0L;
    private static long now = 0L;
    private static float mag = 1.0F;
    private static float prevmag = 1.0F;
    private static SimpleDateFormat sdf;

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ 名前
     */
    public CorrectionTimer(PluginFrame plg_, String name_) {
        super(plg_, name_);
        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        reprev = prev = System.currentTimeMillis();
    }

    /**
     * コマンドからのTPS情報出力指示
     * @param sender コマンドセンダーインスタンス
     */
    public void viewTPS(CommandSender sender) {
        // そのうち統計情報だせるようにしたい
        sender.sendMessage("===前回測定TPS:" + sdf.format(new Date(reprev)) + "===");
        sender.sendMessage("TPS: " + (1000.0F / (prevmag * 50.0F)) + " ( " + (1 / prevmag * 100) + "% )");
        sender.sendMessage("===今回測定TPS:" + sdf.format(new Date(prev)) + "===");
        sender.sendMessage("TPS: " + (1000.0F / (mag * 50.0F)) + " ( " + (1 / mag * 100) + "% )");
    }

    /**
     * タイマーメイン処理
     */
    @Override
    public void run()
    {
        // 現在時刻を取得
        now = System.currentTimeMillis();
        // 掛った処理時間の倍率を算出
        prevmag = mag;
        mag = (now - prev) / 2500.0F; // 差分が2.5秒であれば1、5秒であれば2が入る
        // 測定時刻を更新
        reprev = prev;
        prev = now;
    }
}
