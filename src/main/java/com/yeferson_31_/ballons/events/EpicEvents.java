package com.yeferson_31_.ballons.events;

import com.yeferson_31_.ballons.balloons.EpicBalloons;

import com.yeferson_31_.ballons.balloons.EpicHeads;
import com.yeferson_31_.ballons.config.LangConfig;
import com.yeferson_31_.ballons.inventories.EpicInventory;
import com.yeferson_31_.ballons.utils.DbUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Feeps on 16/08/2017
 */

public class EpicEvents implements Listener{
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if (LangConfig.Msg.DatabaseConfigEnable.toString().equals("true")) {
            if (EpicBalloons.epicBalloonsMap.containsKey(player)) {
                // System.out.println(player.getName() + " disconnected using " + EpicBalloons.epicBalloonsMap.get(player).getItem().getItemMeta().getDisplayName());
                DbUtils.setPlayerBalloon(player.getUniqueId().toString(), EpicBalloons.epicBalloonsMap.get(player).getItem().getItemMeta().getDisplayName());
                EpicBalloons.epicBalloonsMap.get(player).remove();
            } else {
                DbUtils.deletePlayerBalloon(player.getUniqueId().toString());
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if (LangConfig.Msg.DatabaseConfigEnable.toString().equals("true")) {
            String balloon = DbUtils.getPlayerBalloon(player.getUniqueId().toString());
            if (balloon != null) {
                // System.out.println(player.getName() + " joined having " + balloon);
                for(EpicHeads heads : EpicHeads.values()){
                    if (heads.getName().equals(balloon) && player.hasPermission(heads.getPermission())) {
                        new EpicBalloons(player, heads.getItem()).spawn();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if (event.getInventory() == null) return;
        if (event.getCurrentItem() == null) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if(!EpicInventory.epicInventoryMap.containsKey(player)) return;
        if(!event.getInventory().getName().equals(EpicInventory.epicInventoryMap.get(player).getInventory().getName())) return;
        if (event.getCurrentItem().getType() == Material.AIR) return;

        EpicInventory.epicInventoryMap.get(player).onClick(event);
    }
}
