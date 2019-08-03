package net.magicwars.manager.kits.kits;

import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.magicwars.manager.kits.Kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

public class SorceleurKit2 implements Kit {

	@Override
	public ItemStack getHelmet() {
 		return null;
	}

	@Override
	public ItemStack getChestplate() {
		ItemStack is = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName("Plastron du sorceleur");
		is.setItemMeta(meta);
 		return is;
	}

	@Override
	public ItemStack getLeggings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getBoots() {
		ItemStack is = new ItemStack(Material.IRON_BOOTS);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName("Bottes du sorceleur");
		is.setItemMeta(meta);
		return is;
	}

	@Override
	public List<String> getWands() {
		return Arrays.asList("speed", "explosion");
	}

	@Override
	public List<ItemStack> getItems() {
		List<ItemStack> output = new ArrayList<ItemStack>();
		ItemStack is;
		
		is = new ItemStack(Material.IRON_SWORD);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName("Poignard du sorceleur");
		is.setItemMeta(meta);
		output.add(is);
		
		return output;
	}

}
