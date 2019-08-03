package net.magicwars.manager.kits.kits;

import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.magicwars.manager.kits.Kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

public class MageblancKit5 implements Kit {

	@Override
	public ItemStack getHelmet() {
		return null;
	}

	@Override
	public ItemStack getChestplate() {
		ItemStack is = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName("Robe des Chérubins");
		is.setItemMeta(meta);
 		return is;
	}

	@Override
	public ItemStack getLeggings() {
		ItemStack is = new ItemStack(Material.CHAINMAIL_LEGGINGS);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName("Robe des Chérubins");
		is.setItemMeta(meta);
 		return is;
	}

	@Override
	public ItemStack getBoots() {
		ItemStack is = new ItemStack(Material.CHAINMAIL_BOOTS);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName("Bottes des Chérubins");
		meta.addEnchant(Enchantment.PROTECTION_FALL, 10, true);
		is.setItemMeta(meta);
 		return is;
	}

	@Override
	public List<String> getWands() {
		return Arrays.asList("wand_missile", "wand_stream", "wand_blob", "wand_airscooter", "wand_blast", "wand_blessing");
	}

	@Override
	public List<ItemStack> getItems() {
		return new ArrayList<ItemStack>();
	}

}
