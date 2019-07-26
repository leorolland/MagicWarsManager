package net.magicwars.manager.kits;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Kit {
	
	public String name;
	
	public String description;
	
	public List<String> wands;
	
	public List<ItemStack> items;
	
	public Kit(String name, String description, List<String> wands, List<ItemStack> items) {
		this.name = name;
		this.wands = wands;
	}
	
	public void give(String username) {
		Player p = Bukkit.getServer().getPlayer(username);
		if (p!=null) {			
			for (String wand : wands) {
				mgive(username, wand);
			}
			for (ItemStack item : items) {
				p.getInventory().addItem(item);
			}
			p.sendMessage("Le kit " + name + " vous a été donné !");
			p.sendMessage(description);
		}
	}
	
	private void mgive(String username, String item) {
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "mgive "+username+" "+item);
	}
	
}
