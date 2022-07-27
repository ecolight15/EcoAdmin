package jp.minecraftuser.ecoadmin.timer;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.TimerFrame;
import static jp.minecraftuser.ecoframework.Utl.sendPluginMessage;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * セーブタイマー
 * @author ecolight
 */
public class SaveTimer extends TimerFrame {
    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ 名前
     */
    public SaveTimer(PluginFrame plg_, String name_) {
        super(plg_, name_);
    }

    /**
     * 非同期処理メイン
     */
    @Override
    public void run() {

        //非同期で処理するのでWorldリストではなくWorldUUIDのリストを作成
        List<UUID> world_uuid_list = new ArrayList<UUID>();

        for (World w : plg.getServer().getWorlds()) {
            world_uuid_list.add(w.getUID());
        }
        //コンフィグで指定したTick間隔で各WorldをSaveする
        new DelaySaveAllTimer(plg, name, world_uuid_list).runTaskTimer(plg, 1, conf.getInt("util.auto_save.delay_tick"));
    }
}

/**
 * コンフィグで指定したTick間隔で各WorldをSaveする
 * @author ecolight
 */
class DelaySaveAllTimer extends TimerFrame {
    int index = 0;
    List<UUID> world_uuid_list;
    private final boolean isBroadcast;

    /**
     * コンストラクタ
     *
     * @param plg_  プラグインインスタンス
     * @param name_ 名前
     */
    public DelaySaveAllTimer(PluginFrame plg_, String name_, List<UUID> world_UUID_List) {
        super(plg_, name_);
        this.world_uuid_list = world_UUID_List;

        isBroadcast = conf.getBoolean("util.auto_save.broadcast_information");
        if (isBroadcast) {
            sendPluginMessage(plg, null, "定期セーブを実行中(Auto saving...)");
        }
        //Playerはコンストラクタ実行の時点でセーブする｡
        log.info("セーブ中[players]");
        plg.getServer().savePlayers();
    }

    /**
     * 非同期処理メイン
     */
    @Override
    public void run() {
        if (index < world_uuid_list.size()) {
            UUID world_uuid = world_uuid_list.get(index++);
            World world = plg.getServer().getWorld(world_uuid);
            //非同期で処理しているので一応nullチェック
            if (world != null) {
                log.info("セーブ中[world:" + world.getName() + "]");
                world.save();
            } else {
                //Worldが存在しない場合
                log.info("セーブ対象のWorld[" + world_uuid + "]が存在しません");
            }

            if (index < world_uuid_list.size()) {
                //indexが最後の要素でなければセーブを続行する｡
                return;
            } else {
                //indexが最後の要素なら全ワールドセーブ完了
                if (isBroadcast) {
                    sendPluginMessage(plg, null, "定期セーブを完了しました(Auto save is complete.)");
                }
                //Timerをキャンセルさせる
                this.cancel();
            }
        } else {
            //通常は通らない
            log.info("WorldListが読み込まれていません｡");
            //Timerをキャンセルさせる
            this.cancel();
        }
    }
}
