package com.yeferson_31_.ballons.inventories;

import com.yeferson_31_.ballons.balloons.EpicBalloons;
import com.yeferson_31_.ballons.balloons.EpicHeads;
import com.yeferson_31_.ballons.config.LangConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Created by Feeps on 16/08/2017
 */

public class EpicInventory {
    public static Map<Player, EpicInventory> epicInventoryMap = new HashMap<>();
    private Inventory inventory;
    private Player player;

    public EpicInventory(Player player){
        this.player = player;
        this.inventory = Bukkit.createInventory(null, 45, LangConfig.Msg.InventoryName.toString());
        epicInventoryMap.put(this.player, this);
    }

    private void create(){
        for(EpicHeads heads : EpicHeads.values()){
            if(this.player.hasPermission(heads.getPermission())){
                List<String> lore = new ArrayList<>();
                lore.add(LangConfig.Msg.InventoryLoreHasPermission.toString());
                this.setItem(heads.getItem(), heads.getSlot(), heads.getName(), lore);
            }else {
                List<String> lore = new ArrayList<>();
                lore.add(LangConfig.Msg.InventoryLoreNoPermission.toString());
                this.setItem(heads.getItem(), heads.getSlot(), heads.getName(), lore);
            }
        }
        if(EpicBalloons.epicBalloonsMap.containsKey(this.player)) {
            List<String> lore = new ArrayList<>();
            lore.add(LangConfig.Msg.InventoryLoreHasPermission.toString());
            this.setItem(new ItemStack(Material.BARRIER),40, LangConfig.Msg.InventoryItemRemoveBalloon.toString(), lore);
        }
    }


    public void onClick(InventoryClickEvent event){
        ItemStack item = event.getCurrentItem();
        if (item.getType() != Material.BARRIER) {
            if (item.hasItemMeta() && item.getItemMeta().getLore().get(0).equalsIgnoreCase(LangConfig.Msg.InventoryLoreHasPermission.toString())) {
                if (EpicBalloons.epicBalloonsMap.containsKey(this.player)) {
                    EpicBalloons.epicBalloonsMap.get(this.player).setItem(item);
                } else {
                    new EpicBalloons(this.player, item).spawn();
                }
            } else {
                this.player.sendMessage(LangConfig.Msg.MessagesNoPermission.toPrefix());
            }
            this.player.closeInventory();
        } else {
            if (EpicBalloons.epicBalloonsMap.containsKey(this.player)) {
                EpicBalloons.epicBalloonsMap.get(this.player).remove();
                this.player.closeInventory();
            }
        }
        event.setCancelled(true);
    }

    private void setItem(ItemStack item, int slot, String name, List<String> lore) {
        ItemMeta im = item.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_POTION_EFFECTS,
                ItemFlag.HIDE_UNBREAKABLE);
        if (name != null) {
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }
        if (lore != null) {
            List<String> converted = new ArrayList<>();
            for (String string : lore) {
                converted.add(ChatColor.translateAlternateColorCodes('&', string));
            }
            im.setLore(converted);
        }
        item.setItemMeta(im);
        this.inventory.setItem(slot, item);
    }

    public void openInv(){
        this.player.openInventory(this.inventory);
        this.create();
    }

    public void closeInv(){
        epicInventoryMap.remove(this.player, this);
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
