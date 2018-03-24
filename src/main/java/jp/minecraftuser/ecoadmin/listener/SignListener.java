package jp.minecraftuser.ecoadmin.listener;

import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecoframework.PluginFrame;
import static jp.minecraftuser.ecoframework.Utl.repColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.SignChangeEvent;

/**
 * 看板イベント処理リスナークラス
 * @author ecolight
 */
public class SignListener extends ListenerFrame {
    
    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ 名前
     */
    public SignListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }
   
    /**
     * 看板イベント処理
     * @param e イベント情報
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onSignChange(SignChangeEvent e) {
        if (conf.getBoolean("fun.sign_colorcode_convert")) {
            for (int i = 0; i < e.getLines().length; i++) {
                String line = e.getLines()[i];
                line = repColor(line);
                e.setLine(i, line);
            }
        }
    }

}
