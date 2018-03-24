
package jp.minecraftuser.ecoadmin.command;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Healコマンドクラス
 * @author ecolight
 */
public class HealCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public HealCommand(PluginFrame plg_, String name_) {
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
        return "ecoadmin.heal";
    }

    /**
     * 処理実行部
     * @param sender コマンド送信者
     * @param args パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        // パラメータチェック:0～2まで
        if (!checkRange(sender, args, 0, 2)) return true;

        Player p = null;
        // パラメータ無しの場合は最大に設定
        
        // 想定のパラメタ
        // (1) heal
        // (2) heal num
        // (3) heal [player]
        // (4) heal [player] num
        // プレイヤー名が数値の場合も想定されるため、第一パラメータが数値かつ
        // プレイヤーとしてログイン中の場合にはプレイヤー名+数値省略は許容しないようにする。
        boolean numuser = false;
        if (args.length >= 1) {
            try {
                for (Player target : plg.getServer().getOnlinePlayers()) {
                    if (target.getName().equalsIgnoreCase(args[0])) {
                        // 指定した名前のプレイヤーがいることは確定なので対象を更新しておく
                        p = target;
                        // 数値ユーザーかチェック、数値でなければ即時検索中断、数値であればnumuser更新して中断
                        Integer.parseInt(args[0]);
                        numuser = true;
                        break;
                    }
                }
            } catch (NumberFormatException e) { /* nop */ }
        }
        try {
            // HITした場合、数値かつオンラインのためパラメータの省略を許容しない※(4)のみ
            if (numuser) {
                // パラメータチェック:2のみ
                if (!checkRange(sender, args, 2, 2)) return true;
                p.setHealth(p.getHealth() + Integer.parseInt(args[1]));
            }
            // HITしない場合は全パターンを想定する※(1)～(4)
            else {
                switch (args.length) {
                    case 0: // 本人指定で数値指定なし
                        // (1) feed
                        p = (Player) sender;
                        p.setHealth(p.getMaxHealth());
                        break;
                    case 1: // 本人指定で数値指定あり or 数値以外のプレイヤー指定のみ or プレイヤー名指定ミス
                        // (2) feed num
                        // (3) feed [player]
                        if (p == null) {
                            p = (Player) sender;
                            p.setHealth(p.getHealth() + Integer.parseInt(args[0]));
                        } else {
                            p.setHealth(p.getMaxHealth());
                        }
                        break;
                    case 2: // 数値以外のプレイヤー名指定ありかつ数値指定あり
                        // (4) feed [player] num
                        if (p == null) {
                            sendPluginMessage(plg, sender, "プレイヤー[{0}]が見つかりませんでした", args[0]);
                            return true;
                        }
                        p.setHealth(p.getHealth()+ Integer.parseInt(args[1]));
                        break;
                }
            }
        } catch (NumberFormatException e) {
            sendPluginMessage(plg, sender, "プレイヤー[{0}]のHealthLevel設定で数値変換に失敗しました", p.getName());
            return true;
        }        
        sendPluginMessage(plg, sender, "プレイヤー[{0}]のHealthLevelを{1}に設定しました", p.getName(), Integer.toString(p.getFoodLevel()));
        return true;
    }
    
}
