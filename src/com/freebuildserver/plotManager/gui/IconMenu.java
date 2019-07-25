package com.freebuildserver.plotManager.gui;

import java.util.Arrays;


//import net.minecraft.server.NBTTagCompound;
//import net.minecraft.server.NBTTagList;
//import net.minecraft.server.NBTTagString;
 
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
 
public class IconMenu implements Listener {
 
    private String                  name;
    private int                    size;
    private OptionClickEventHandler handler;
    private Plugin                  plugin;
 
    private String[]                optionNames;
    private ItemStack[]            optionIcons;
    
    private Inventory inventory;
 
    public IconMenu(String name, int size, OptionClickEventHandler handler, Plugin plugin) {
        this.name = name;
        this.size = size;
        this.handler = handler;
        this.plugin = plugin;
        this.optionNames = new String[size];
        this.optionIcons = new ItemStack[size];
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
 
    public IconMenu setOption(int position, ItemStack icon, String name, String... info) {
        optionNames[position] = name;
        optionIcons[position] = setItemNameAndLore(icon, name, info);
        return this;
    }
 
    public void open(Player player) {
        inventory = Bukkit.createInventory(player, this.size, this.name);
        for (int i = 0; i < optionIcons.length; i++) {
            if (optionIcons[i] != null) {
                inventory.setItem(i, optionIcons[i]);
            }
        }
        player.openInventory(inventory);
    }
 
    public void destroy() {
        HandlerList.unregisterAll(this);
        handler = null;
        plugin = null;
        optionNames = null;
        optionIcons = null;
        inventory = null;
    }
 
    @EventHandler(priority = EventPriority.MONITOR)
    void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() == this.inventory) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < size && optionNames[slot] != null) {
                Plugin plugin = this.plugin;
                OptionClickEvent e = new OptionClickEvent(event, slot, optionNames[slot]);
                handler.onOptionClick(e);
                if (e.willClose()) {
                    final Player p = (Player) event.getWhoClicked();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
 
                        public void run() {
                            p.closeInventory();
                        }
                    }, 1);
                }
                if (e.willDestroy()) {
                    destroy();
                }
            }
        }
    }
 
    public interface OptionClickEventHandler {
 
        public void onOptionClick(OptionClickEvent event);
    }
 
    public class OptionClickEvent {
 
        private Player  player;
        private int    position;
        private String  name;
        private boolean close;
        private boolean destroy;
        private InventoryClickEvent event;
 
        public OptionClickEvent(InventoryClickEvent event, int position, String name) {
            this.setEvent(event);
        	this.player = (Player) event.getWhoClicked();
            this.position = position;
            this.name = name;
            this.close = true;
            this.destroy = false;
        }
 
        public Player getPlayer() {
            return player;
        }
 
        public int getPosition() {
            return position;
        }
 
        public String getName() {
            return name;
        }
 
        public boolean willClose() {
            return close;
        }
 
        public boolean willDestroy() {
            return destroy;
        }
 
        public void setWillClose(boolean close) {
            this.close = close;
        }
 
        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }

		public InventoryClickEvent getEvent() {
			return event;
		}

		public void setEvent(InventoryClickEvent event) {
			this.event = event;
		}
    }
 
    
 // Nice little method to create a gui item with a custom name, and description
    public static ItemStack setItemNameAndLore(ItemStack item, String name, String[] info) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(info));
        item.setItemMeta(meta);
        return item;
    }
    
 
}