
package jp.minecraftuser.ecoadmin;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import jp.minecraftuser.ecoframework.ConfigFrame;
import jp.minecraftuser.ecoadmin.command.AfkCommand;
import jp.minecraftuser.ecoadmin.command.BackCommand;
import jp.minecraftuser.ecoadmin.command.ClearCommand;
import jp.minecraftuser.ecoadmin.command.EcaCommand;
import jp.minecraftuser.ecoadmin.command.EcaReloadCommand;
import jp.minecraftuser.ecoadmin.command.EcbanCommand;
import jp.minecraftuser.ecoadmin.command.EcunbanCommand;
import jp.minecraftuser.ecoadmin.command.FeedCommand;
import jp.minecraftuser.ecoadmin.command.FlyCommand;
import jp.minecraftuser.ecoadmin.command.FreezeCommand;
import jp.minecraftuser.ecoadmin.command.GmCommand;
import jp.minecraftuser.ecoadmin.command.HealCommand;
import jp.minecraftuser.ecoadmin.command.HideCommand;
import jp.minecraftuser.ecoadmin.command.KillCommand;
import jp.minecraftuser.ecoadmin.command.LocCommand;
import jp.minecraftuser.ecoadmin.command.LockdownCommand;
import jp.minecraftuser.ecoadmin.command.LoginCommand;
import jp.minecraftuser.ecoadmin.command.SetspawnCommand;
import jp.minecraftuser.ecoadmin.command.ShowCommand;
import jp.minecraftuser.ecoadmin.command.SpawnCommand;
import jp.minecraftuser.ecoadmin.command.StopCommand;
import jp.minecraftuser.ecoadmin.command.StrikeCommand;
import jp.minecraftuser.ecoadmin.command.TCommand;
import jp.minecraftuser.ecoadmin.command.TpCommand;
import jp.minecraftuser.ecoadmin.command.TphereCommand;
import jp.minecraftuser.ecoadmin.command.TppCommand;
import jp.minecraftuser.ecoadmin.command.TpseeCommand;
import jp.minecraftuser.ecoadmin.command.TpsvCommand;
import jp.minecraftuser.ecoadmin.command.UnLockdownCommand;
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
import jp.minecraftuser.ecoadmin.timer.CorrectionTimer;
import jp.minecraftuser.ecoadmin.timer.SaveTimer;
import jp.minecraftuser.ecoframework.LoggerFrame;

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
        conf.registerBoolean("util.auto_save.broadcast_information");

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
        registerPluginCommand(new EcbanCommand(this, "ecban"));
        registerPluginCommand(new EcunbanCommand(this, "ecunban"));
        registerPluginCommand(new FreezeCommand(this, "freeze"));
        registerPluginCommand(new AfkCommand(this, "afk"));
        registerPluginCommand(new FlyCommand(this, "fly"));
        registerPluginCommand(new GmCommand(this, "gm"));
        registerPluginCommand(new HideCommand(this, "hide"));
        registerPluginCommand(new ShowCommand(this, "show"));
        registerPluginCommand(new KillCommand(this, "kill"));
        registerPluginCommand(new KillCommand(this, "pkill"));
        registerPluginCommand(new LoginCommand(this, "login"));
        registerPluginCommand(new LockdownCommand(this, "lock"));
        registerPluginCommand(new UnLockdownCommand(this, "unlock"));
        registerPluginCommand(new StopCommand(this, "stop"));
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
    }

    /**
     * タイマー処理の初期化と登録
     */
    @Override
    public void initializeTimer() {
        registerPluginTimer(new CorrectionTimer(this, "correction"));
        registerPluginTimer(new SaveTimer(this, "save"));

        // TPS測定タイマー起動
        runTaskTimer("correction", 0L, 50L);            // 現在から2.5秒間隔

        // オートセーブタイマー
        ConfigFrame conf = getDefaultConfig();
        if (conf.getBoolean("util.auto_save.enable")) {
            runTaskTimer("save",
                    conf.getInt("util.auto_save.start_mergin"),
                    conf.getInt("util.auto_save.interval_tick"));
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
        if (conf.getBoolean("protection.interact.hoppercart.logging")) {
            registerPluginLogger(new LoggerFrame(this,  conf.getString("protection.interact.hoppercart.logfilename"), "hoppercartrej"));
        }
        if (conf.getBoolean("protection.place.map.logging")) {
            registerPluginLogger(new LoggerFrame(this,  conf.getString("protection.place.map.logfilename"), "maprej"));
        }
    }
}
