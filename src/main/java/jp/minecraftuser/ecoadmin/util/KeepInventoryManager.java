package jp.minecraftuser.ecoadmin.util;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.TimerFrame;
import org.bukkit.GameRule;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

/**
 * keep-inventory設定の一時的な変更を管理するユーティリティクラス
 * 
 * @author ecolight
 */
public class KeepInventoryManager {
    
    // Private constructor to prevent instantiation
    private KeepInventoryManager() {
    }
    
    /**
     * タイマーと元の設定値を保持する内部クラス
     */
    private static class TimerInfo {
        public final TimerFrame timer;
        public final Boolean originalValue;
        
        public TimerInfo(TimerFrame timer, Boolean originalValue) {
            this.timer = timer;
            this.originalValue = originalValue;
        }
    }
    
    // 現在実行中のタイマー情報を管理するマップ（ワールドごと）
    private static final Map<World, TimerInfo> activeTimers = new HashMap<>();
    
    /**
     * 雷発生時にkeep-inventory設定を一時的に有効化する
     * 既存のタイマーがあれば中断し、新しいタイマーを開始する
     * 
     * @param plg プラグインインスタンス
     * @param world 対象ワールド
     * @param commandName 呼び出し元コマンド名（ログ用）
     */
    public static void enableKeepInventoryTemporarily(PluginFrame plg,  World world, String commandName) {
        // 元の設定値を決定（既存タイマーがある場合はその元値を継承）
        final Boolean originalKeepInventory;
        TimerInfo existingTimerInfo = activeTimers.get(world);
        if (existingTimerInfo != null) {
            // 既存のタイマーがある場合は、そのタイマーが保持していた元の値を継承
            originalKeepInventory = existingTimerInfo.originalValue;
            existingTimerInfo.timer.cancel();
            activeTimers.remove(world);
        } else {
            // 新規の場合は現在の設定値を保存
            originalKeepInventory = world.getGameRuleValue(GameRule.KEEP_INVENTORY);
        }
        
        // keep-inventoryを有効にする
        world.setGameRule(GameRule.KEEP_INVENTORY, true);
        
        // 5秒後（100ティック後）に元の設定に戻すタイマーを作成
        TimerFrame restoreTimer = new TimerFrame(plg, commandName + "_keepinv_restore") {
            @Override
            public void run() {
                // ワールドがまだ存在する場合のみ復元
                if (plg.getServer().getWorlds().contains(world)) {
                    world.setGameRule(GameRule.KEEP_INVENTORY, originalKeepInventory);
                }
                // タイマーをマップから削除
                activeTimers.remove(world);
            }
        };
        
        // タイマー情報をマップに登録してから実行
        activeTimers.put(world, new TimerInfo(restoreTimer, originalKeepInventory));
        restoreTimer.runTaskLater(plg, 100); // 100ティック = 5秒
    }
    
    /**
     * 指定されたワールドのkeep-inventory復元タイマーを中断する
     * 
     * @param world 対象ワールド
     */
    public static void cancelKeepInventoryTimer(World world) {
        TimerInfo timerInfo = activeTimers.get(world);
        if (timerInfo != null) {
            timerInfo.timer.cancel();
            activeTimers.remove(world);
        }
    }
    
    /**
     * 指定されたワールドでkeep-inventory復元タイマーが実行中かどうかチェック
     * 
     * @param world 対象ワールド
     * @return タイマーが実行中かどうか
     */
    public static boolean hasActiveTimer(World world) {
        return activeTimers.containsKey(world);
    }
}