
package jp.minecraftuser.ecoadmin;

import java.util.logging.Level;

import jp.minecraftuser.ecoadmin.command.*;
import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import jp.minecraftuser.ecoframework.ConfigFrame;
import jp.minecraftuser.ecoadmin.config.EcoAdminConfig;
import jp.minecraftuser.ecoadmin.config.LoginMsgConfig;
import jp.minecraftuser.ecoadmin.listener.DeathListener;
import jp.minecraftuser.ecoadmin.listener.GuardListener;
import jp.minecraftuser.ecoadmin.listener.PlayerConnectionListener;
import jp.minecraftuser.ecoadmin.listener.PlayerFreezeListener;
import jp.minecraftuser.ecoadmin.listener.PlayerHandShakeListener;
import jp.minecraftuser.ecoadmin.listener.PlayerTeleportListener;
import jp.minecraftuser.ecoadmin.listener.SignListener;
import jp.minecraftuser.ecoadmin.listener.TpseeListener;
import jp.minecraftuser.ecoadmin.listener.TListener;
import jp.minecraftuser.ecoadmin.timer.AfkTimer;
import jp.minecraftuser.ecoadmin.timer.CorrectionTimer;
import jp.minecraftuser.ecoadmin.timer.SaveTimer;
import jp.minecraftuser.ecoframework.LoggerFrame;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
/**
 * EcoAdminプラグインメインクラス
 * @author ecolight
 */
public class EcoAdmin  extends PluginFrame {

    /**
     * プラグイン開始処理
     */
    @Override
    public void onEnable() {
        initialize();
        worldSetting();
    }

    /**
     * プラグイン終了処理
     */
    @Override
    public void onDisable()
    {
        disable();
    }

    /**
     * 設定ファイル処理の初期化と登録
     */
    @Override
    public void initializeConfig() {
        ConfigFrame conf;

        // デフォルトコンフィグ
        conf = new EcoAdminConfig(this);

        // DeathListener
        conf.registerBoolean("cmd.kill.disable_kill_message");

        // GuardListener
        conf.registerBoolean("protection.disable_explosion.ender_crystal");
        conf.registerBoolean("protection.interact.ender_crystal.disable");
        conf.registerBoolean("protection.interact.ender_crystal.logging");
        conf.registerString("protection.interact.ender_crystal.ignore_world_prefix");
        conf.registerString("protection.interact.ender_crystal.logfilename");
        conf.registerBoolean("protection.interact.mob_egg.disable");
        conf.registerBoolean("protection.interact.mob_egg.logging");
        conf.registerString("protection.interact.mob_egg.ignore_world_prefix");
        conf.registerString("protection.interact.mob_egg.logfilename");
        conf.registerBoolean("protection.interact.bed.disable");
        conf.registerBoolean("protection.interact.bed.logging");
        conf.registerString("protection.interact.bed.ignore_world_prefix");
        conf.registerString("protection.interact.bed.logfilename");
        conf.registerBoolean("protection.interact.respawnanchor.disable");
        conf.registerBoolean("protection.interact.respawnanchor.logging");
        conf.registerString("protection.interact.respawnanchor.ignore_world_prefix");
        conf.registerString("protection.interact.respawnanchor.logfilename");
        conf.registerBoolean("protection.interact.hoppercart.disable");
        conf.registerBoolean("protection.interact.hoppercart.logging");
        conf.registerString("protection.interact.hoppercart.ignore_world_prefix");
        conf.registerString("protection.interact.hoppercart.logfilename");
        conf.registerBoolean("protection.place.wet_sponge.disable");
        conf.registerBoolean("protection.place.endcitypath.disable");
        conf.registerString("protection.place.endcitypath.end_world_prefix");
        conf.registerBoolean("protection.place.map.disable");
        conf.registerBoolean("protection.place.map.logging");
        conf.registerString("protection.place.map.ignore_world_prefix");
        conf.registerString("protection.place.map.logfilename");
        conf.registerBoolean("protection.water_walking.disable");
        conf.registerArrayString("protection.water_walking.world_list");
        conf.registerBoolean("protection.create_hopper_cart.disable");
        conf.registerBoolean("protection.inventory.creative_control");

        // PlayerConnectionListener
        conf.registerBoolean("security.join.show_partof_ipaddress");
        conf.registerBoolean("security.join.force_disable_flight_mode");
        conf.registerBoolean("security.logout.forced_takedown_from_vehicle");
        conf.registerBoolean("security.kick.reason_announce");
        conf.registerBoolean("security.kick.reason_announce_plus_host");
        conf.registerArrayString("security.kick.ignore_reason_announce");
        conf.registerBoolean("security.kick.until_server_stop");
        conf.registerBoolean("security.kick.logging");
        conf.registerString("security.kick.logfilename");
        conf.registerBoolean("fun.login_message");

        // PlayerHandShakeListener
        conf.registerBoolean("fun.handshake_item_exchange");

        // PlayerTeleportListener
        conf.registerBoolean("protection.chorus_fruit.disable");
        conf.registerArrayString("protection.chorus_fruit.world_list");

        // SignListener
        conf.registerBoolean("fun.sign_colorcode_convert");

        // command
        conf.registerBoolean("cmd.lockdown.allow_op");

        // timer
        conf.registerBoolean("util.auto_save.enable");
        conf.registerInt("util.auto_save.interval_tick");
        conf.registerInt("util.auto_save.start_mergin");
        conf.registerInt("util.auto_save.delay_tick");
        conf.registerBoolean("util.auto_save.broadcast_information");

        // afk
        conf.registerBoolean("util.afk.enable");
        conf.registerInt("util.afk.interval_tick");
        conf.registerInt("util.afk.start_mergin");
        conf.registerLong("util.afk.limit_milliseconds");
        conf.registerString("util.afk.kick_message");

        // spawn
        conf.registerBoolean("spawn.drowned.disable");
        conf.registerArrayString("spawn.drowned.world_list");
        conf.registerBoolean("spawn.phantom.disable");
        conf.registerArrayString("spawn.phantom.world_list");
        conf.registerBoolean("spawn.skeleton_horse.disable");
        conf.registerArrayString("spawn.skeleton_horse.world_list");

        // difficulty
        conf.registerArrayString("difficulty.peaceful.world_list");
        conf.registerArrayString("difficulty.peaceful.world_prefix");
        conf.registerArrayString("difficulty.easy.world_list");
        conf.registerArrayString("difficulty.easy.world_prefix");
        conf.registerArrayString("difficulty.normal.world_list");
        conf.registerArrayString("difficulty.normal.world_prefix");
        conf.registerArrayString("difficulty.hard.world_list");
        conf.registerArrayString("difficulty.hard.world_prefix");

        // pvp
        conf.registerArrayString("pvp.true.world_list");
        conf.registerArrayString("pvp.true.world_prefix");
        conf.registerArrayString("pvp.false.world_list");
        conf.registerArrayString("pvp.false.world_prefix");

        // ganerule:disableRaids
        conf.registerArrayString("gamerule.disableRaids.true.world_list");
        conf.registerArrayString("gamerule.disableRaids.true.world_prefix");
        conf.registerArrayString("gamerule.disableRaids.false.world_list");
        conf.registerArrayString("gamerule.disableRaids.false.world_prefix");


        registerPluginConfig(conf);

        // ログインメッセージコンフィグ
        conf = new LoginMsgConfig(this, "loginmsg.yml", "login");
        conf.registerArrayString("msg");
        registerPluginConfig(conf);

    }

    /**
     * コマンド処理の初期化と登録
     */
    @Override
    public void initializeCommand() {
        CommandFrame cmd = new EcaCommand(this, "eca");
        cmd.addCommand(new EcaReloadCommand(this, "reload"));
        cmd.addCommand(new EcaTestCommand(this, "test"));
        registerPluginCommand(cmd);
        registerPluginCommand(new LocCommand(this, "loc"));
        registerPluginCommand(new HealCommand(this, "heal"));
        registerPluginCommand(new FeedCommand(this, "feed"));
        registerPluginCommand(new ClearCommand(this, "clear"));
        registerPluginCommand(new SetspawnCommand(this, "setspawn"));
        registerPluginCommand(new SpawnCommand(this, "spawn"));
        registerPluginCommand(new TpCommand(this, "tp"));
        registerPluginCommand(new TppCommand(this, "tpp"));
        registerPluginCommand(new TphereCommand(this, "tphere"));
        registerPluginCommand(new TpseeCommand(this, "tpsee"));
        registerPluginCommand(new BackCommand(this, "back"));
        registerPluginCommand(new StrikeCommand(this, "strike"));
        registerPluginCommand(new TCommand(this, "t"));
        registerPluginCommand(new TpsvCommand(this, "tpsv"));
        registerPluginCommand(new FreezeCommand(this, "freeze"));
        registerPluginCommand(new FlyCommand(this, "fly"));
        registerPluginCommand(new GmCommand(this, "gm"));
        registerPluginCommand(new HideCommand(this, "hide"));
        registerPluginCommand(new ShowCommand(this, "show"));
        registerPluginCommand(new KillCommand(this, "kill"));
        registerPluginCommand(new KillCommand(this, "pkill"));
        registerPluginCommand(new LoginCommand(this, "login"));
        registerPluginCommand(new PvPCommand(this, "pvp"));
        registerPluginCommand(new LockdownCommand(this, "lock"));
        registerPluginCommand(new UnLockdownCommand(this, "unlock"));
        registerPluginCommand(new StopCommand(this, "stop"));
        registerPluginCommand(new ForceStopCommand(this, "fstop"));
        registerPluginCommand(new WorldRuleCommand(this, "wrule"));
        registerPluginCommand(new LookCommand(this, "look"));
        registerPluginCommand(new LookCommand(this, "zlook"));
        registerPluginCommand(new LookPosCommand(this, "xlook"));
    }

    /**
     * イベントリスナ―処理の初期化と登録
     */
    @Override
    public void initializeListener() {
        registerPluginListener(new DeathListener(this, "death"));
        registerPluginListener(new GuardListener(this, "guard"));
        registerPluginListener(new PlayerConnectionListener(this, "plconnection"));
        registerPluginListener(new PlayerFreezeListener(this, "plfreeze"));
        registerPluginListener(new PlayerHandShakeListener(this, "plhandshake"));
        registerPluginListener(new PlayerTeleportListener(this, "plteleport"));
        registerPluginListener(new SignListener(this, "sign"));
        registerPluginListener(new TpseeListener(this, "tpsee"));
        registerPluginListener(new TListener(this, "t"));
    }

    /**
     * タイマー処理の初期化と登録
     */
    @Override
    public void initializeTimer() {
        registerPluginTimer(new CorrectionTimer(this, "correction"));
        registerPluginTimer(new SaveTimer(this, "save"));
        registerPluginTimer(new AfkTimer(this, "afk"));

        // TPS測定タイマー起動
        runTaskTimer("correction", 0L, 50L);            // 現在から2.5秒間隔 

        // オートセーブタイマー
        ConfigFrame conf = getDefaultConfig();
        if (conf.getBoolean("util.auto_save.enable")) {
            runTaskTimer("save",
                    conf.getInt("util.auto_save.start_mergin"),
                    conf.getInt("util.auto_save.interval_tick"));
        }
        // AFKタイマー
        if (conf.getBoolean("util.afk.enable")) {
            runTaskTimer("afk",
                    conf.getInt("util.afk.start_mergin"),
                    conf.getInt("util.afk.interval_tick"));
        }
    }

    /**
     * Logger登録
     */
    @Override
    protected void initializeLogger() {
        ConfigFrame conf = getDefaultConfig();
        if (conf.getBoolean("security.kick.logging")) {
            registerPluginLogger(new LoggerFrame(this, conf.getString("security.kick.logfilename"), "kick"));
        }
        if (conf.getBoolean("protection.interact.mob_egg.logging")) {
            registerPluginLogger(new LoggerFrame(this, conf.getString("protection.interact.mob_egg.logfilename"), "screj"));
        }
        if (conf.getBoolean("protection.interact.ender_crystal.logging")) {
            registerPluginLogger(new LoggerFrame(this,  conf.getString("protection.interact.ender_crystal.logfilename"), "ecrej"));
        }
        if (conf.getBoolean("protection.interact.bed.logging")) {
            registerPluginLogger(new LoggerFrame(this,  conf.getString("protection.interact.bed.logfilename"), "bedrej"));
        }
        if (conf.getBoolean("protection.interact.respawnanchor.logging")) {
            registerPluginLogger(new LoggerFrame(this,  conf.getString("protection.interact.respawnanchor.logfilename"), "anchorej"));
        }
        if (conf.getBoolean("protection.interact.hoppercart.logging")) {
            registerPluginLogger(new LoggerFrame(this,  conf.getString("protection.interact.hoppercart.logfilename"), "hoppercartrej"));
        }
        if (conf.getBoolean("protection.place.map.logging")) {
            registerPluginLogger(new LoggerFrame(this,  conf.getString("protection.place.map.logfilename"), "maprej"));
        }
    }

    /**
     * 全ワールドの設定変更
     */
    private void worldSetting(){

        ConfigFrame conf = getDefaultConfig();
        getServer().getWorlds().forEach(world -> {

            // 難易度設定
            Difficulty difficulty = null;
            if (conf.getArrayList("difficulty.peaceful.world_list").contains(world.getName())) {
                difficulty =  Difficulty.PEACEFUL;
            } else if (conf.getArrayList("difficulty.easy.world_list").contains(world.getName())) {
                difficulty =  Difficulty.EASY;
            } else if (conf.getArrayList("difficulty.normal.world_list").contains(world.getName())) {
                difficulty =  Difficulty.NORMAL;
            } else if (conf.getArrayList("difficulty.hard.world_list").contains(world.getName())) {
                difficulty =  Difficulty.HARD;
            } else {
                for (String name : conf.getArrayList("difficulty.peaceful.world_prefix")) {
                    if (world.getName().startsWith(name)) {
                        difficulty =  Difficulty.PEACEFUL;
                        break;
                    }
                }
                if (difficulty == null) {
                    for (String name : conf.getArrayList("difficulty.easy.world_prefix")) {
                        if (world.getName().startsWith(name)) {
                            difficulty =  Difficulty.EASY;
                            break;
                        }
                    }
                }
                if (difficulty == null) {
                    for (String name : conf.getArrayList("difficulty.normal.world_prefix")) {
                        if (world.getName().startsWith(name)) {
                            difficulty =  Difficulty.NORMAL;
                            break;
                        }
                    }
                }
                if (difficulty == null) {
                    for (String name : conf.getArrayList("difficulty.hard.world_prefix")) {
                        if (world.getName().startsWith(name)) {
                            difficulty =  Difficulty.HARD;
                            break;
                        }
                    }
                }
            }
            if (difficulty != null) {
                world.setDifficulty(difficulty);
                log.log(Level.INFO, "Set {0} difficulty to {1}", new Object[]{world.getName(), difficulty.name()});
            }

            // PvP設定
            Boolean pvp = null;
            if (conf.getArrayList("pvp.true.world_list").contains(world.getName())) {
                pvp = true;
            } else if (conf.getArrayList("pvp.false.world_list").contains(world.getName())) {
                pvp = false;
            } else {
                for (String name : conf.getArrayList("pvp.true.world_prefix")) {
                    if (world.getName().startsWith(name)) {
                        pvp = true;
                        break;
                    }
                }
                if (pvp == null) {
                    for (String name : conf.getArrayList("pvp.false.world_prefix")) {
                        if (world.getName().startsWith(name)) {
                            pvp = false;
                            break;
                        }
                    }
                }
            }
            if (pvp != null) {
                world.setPVP(pvp);
                log.log(Level.INFO, "Set {0} pvp setting to {1}", new Object[]{world.getName(), pvp.booleanValue()});
            }

            // ゲームルール：レイド無効設定
            Boolean raid = null;
            if (conf.getArrayList("gamerule.disableRaids.true.world_list").contains(world.getName())) {
                raid = true;
            } else if (conf.getArrayList("gamerule.disableRaids.false.world_list").contains(world.getName())) {
                raid = false;
            } else {
                for (String name : conf.getArrayList("gamerule.disableRaids.true.world_prefix")) {
                    if (world.getName().startsWith(name)) {
                        raid = true;
                        break;
                    }
                }
                if (raid == null) {
                    for (String name : conf.getArrayList("gamerule.disableRaids.false.world_prefix")) {
                        if (world.getName().startsWith(name)) {
                            raid = false;
                            break;
                        }
                    }
                }
            }
            if (raid != null) {
                world.setGameRule(GameRule.DISABLE_RAIDS, raid);
                log.log(Level.INFO, "Set {0} disableRaids setting to {1}", new Object[]{world.getName(), raid.booleanValue()});
            }
        });
    }
}
