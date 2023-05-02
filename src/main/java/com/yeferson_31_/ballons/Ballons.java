package com.yeferson_31_.ballons;

import com.yeferson_31_.ballons.balloons.EpicBalloons;
import com.yeferson_31_.ballons.commands.EpicCommands;
import com.yeferson_31_.ballons.config.LangConfig;
import com.yeferson_31_.ballons.events.EpicEvents;
import com.yeferson_31_.ballons.utils.DbUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Feeps on 16/08/2017
 */

public class Ballons extends JavaPlugin {
    public static Ballons instance;
    public String version;

    @Override
    public void onEnable() {
        instance = this;
        this.version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

        new LangConfig();

        this.getCommand("balloons").setExecutor(new EpicCommands());
        Bukkit.getPluginManager().registerEvents(new EpicEvents(), this);

        if (LangConfig.Msg.DatabaseConfigEnable.toString().equals("true")) {
            if(DbUtils.setupDatabase()) {
                getLogger().info("Database created successfully.");
            } else {
                getLogger().warning("Error creating database. is mysql server online? database disabled to prevent issues.");
                LangConfig.Msg.DatabaseConfigEnable.setValue("false");
            }
        }
    }


    @Override
    public void onDisable() {
        EpicBalloons.epicBalloonsMap.values().forEach(EpicBalloons::remove);
    }


}
