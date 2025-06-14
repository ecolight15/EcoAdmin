
package jp.minecraftuser.ecoadmin.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import jp.minecraftuser.ecoframework.Utl;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * loginコマンドクラス
 * @author ecolight
 */
public class WorldRuleCommand extends CommandFrame {

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ コマンド名
     */
    public WorldRuleCommand(PluginFrame plg_, String name_) {
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
        return "ecoadmin.worldrule";
    }

    /**
     * 処理実行部
     * @param sender コマンド送信者
     * @param args パラメタ
     * @return コマンド処理成否
     */
    @Override
    public boolean worker(CommandSender sender, String[] args) {
        // パラメタがない場合
        if (args.length == 0) {
            Utl.sendPluginMessage(plg, sender, "usage: wrule <world name> <rule> <value>");
            return true;
        }

        // 該当ワールドを検索
        World w;
        if (args.length >= 1) {
            w = Bukkit.getServer().getWorld(args[0]);
            if (w == null) {
                Utl.sendPluginMessage(plg, sender, "World not found.");
                return true;
            }
        } else {
            Utl.sendPluginMessage(plg, sender, "require world name.");
            return true;
        }
        
        // ゲームルールの存在をチェック
        if (args.length >= 2) {
            if (!Arrays.asList(w.getGameRules()).contains(args[1])) {
                Utl.sendPluginMessage(plg, sender, "invalid gamerule name.");
                return true;
            }
        } else {
            Utl.sendPluginMessage(plg, sender, "list of valid gamerule string.");
            for (String s : w.getGameRules()) {
                Utl.sendPluginMessage(plg, sender, s);
            }
            Utl.sendPluginMessage(plg, sender, "require gamerule name.");
            return true;
        }
        
        // ゲームルールを取得
        GameRule g = GameRule.getByName(args[1]);
        if (g == null) {
            Utl.sendPluginMessage(plg, sender, "invalid gamerule name.");
            return true;
        }
        
        // ゲームルールの値設定
        if (args.length >= 3) {
            boolean ret = false;
            if (args[2].equalsIgnoreCase("true")) {
                ret = w.setGameRule(g, true);
            } else if (args[2].equalsIgnoreCase("false")) {
                ret = w.setGameRule(g, false);
            } else {
                try {
                    int v = Integer.parseInt(args[2]);
                    ret = w.setGameRule(g, v);
                } catch (NumberFormatException ex) {
                    Utl.sendPluginMessage(plg, sender, "invalid gamerule value.");
                    return true;
                }
            }
            if (ret) {
                Utl.sendPluginMessage(plg, sender, "GameRule setting complete.[" + w.getName() + "] " + g.getName() + " " + w.getGameRuleValue(g).toString());
            } else {
                Utl.sendPluginMessage(plg, sender, "GameRule setting failed.[" + w.getName() + "] " + g.getName() + " " + args[2] + "(current=" + w.getGameRuleValue(g).toString() + ")");
            }
        } else {
            Utl.sendPluginMessage(plg, sender, "require gamerule value.");
            return true;
        }

        return true;
    }
    /**
     * コマンド別タブコンプリート処理
     * @param sender コマンド送信者インスタンス
     * @param cmd コマンドインスタンス
     * @param string コマンド文字列
     * @param strings パラメタ文字列配列
     * @return 保管文字列配列
     */
    @Override
    protected List<String> getTabComplete(CommandSender sender, Command cmd, String string, String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            // List all world names
            for (World w : plg.getServer().getWorlds()) {
                list.add(w.getName());
            }
        } else if (strings.length == 2) {
            // List all game rules for the specified world
            World w = Bukkit.getServer().getWorld(strings[0]);
            if (w != null) {
                for (String ruleName : w.getGameRules()) {
                    list.add(ruleName);
                }
            }
        } else if (strings.length == 3) {
            // Get current value of the specified game rule for the specified world
            World w = Bukkit.getServer().getWorld(strings[0]);
            if (w != null && Arrays.asList(w.getGameRules()).contains(strings[1])) {
                GameRule g = GameRule.getByName(strings[1]);
                if (g != null) {
                    Object currentValue = w.getGameRuleValue(g);
                    if (currentValue != null) {
                        list.add(currentValue.toString());
                    }
                }
            }
        }
        return list;
    }

}
