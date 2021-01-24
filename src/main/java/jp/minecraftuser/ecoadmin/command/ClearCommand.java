
package jp.minecraftuser.ecoadmin.command;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * clearコマンドクラス
 * @author ecolight
 */
public class ClearCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public ClearCommand(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    /**
     * コマンド権限文字列設定
     * @return 権限文字列
     */
    @Override
    public String getPermissionString() {
        return "ecoadmin.clear";
    }

    /**
     * 処理実行部
     * @param sender コマンド送信者
     * @param args パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        // パラメータチェック:0のみ
        if (!checkRange(sender, args, 0, 0)) return true;
        sendPluginMessage(plg, sender, "手持ちのアイテムを全て空にしますがよろしいですか？");
        confirm(sender);
        return true;
    }

    /**
     * accept
     * @param sender 
     */
    @Override
    protected void acceptCallback(CommandSender sender) {
        Player p = (Player) sender;
        for (ItemStack i : p.getInventory()) {
            if (i != null) {
                p.getInventory().removeItem(i);
            }
        }
        sendPluginMessage(plg, sender, "手持ちのアイテムを全て空にしました");
    }

    /**
     * cancel
     * @param sender 
     */
    @Override
    protected void cancelCallback(CommandSender sender) {
        sendPluginMessage(plg, sender, "clearコマンドをキャンセルしました");
    }
    
    
}
