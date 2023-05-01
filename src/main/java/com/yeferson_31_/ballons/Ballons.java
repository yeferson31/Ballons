package com.yeferson_31_.ballons;

import com.yeferson_31_.ballons.balloons.EpicBalloons;
import com.yeferson_31_.ballons.commands.EpicCommands;
import com.yeferson_31_.ballons.config.LangConfig;
import com.yeferson_31_.ballons.events.EpicEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Feeps on 16/08/2017
 */

public class Ballons extends JavaPlugin{
    public static Ballons instance;
    public String version;

    @Override
    public void onEnable() {
        instance = this;
        this.version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

        new LangConfig();

        this.getCommand("balloons").setExecutor(new EpicCommands());
        Bukkit.getPluginManager().registerEvents(new EpicEvents(), this);
    }

    @Override
    public void onDisable() {
        EpicBalloons.epicBalloonsMap.values().forEach(EpicBalloons::remove);
    }


}
