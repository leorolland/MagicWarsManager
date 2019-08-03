package net.magicwars.manager.kits;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public interface Kit {
	
	public ItemStack getHelmet();
	public ItemStack getChestplate();
	public ItemStack getLeggings();
	public ItemStack getBoots();
	public List<String> getWands();
	public List<ItemStack> getItems();
	
}
