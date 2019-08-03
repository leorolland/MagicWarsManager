package net.magicwars.manager.kits;

import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

public class SorceleurKit extends Kit {

	public final static String name = "Sorceleur";
	public final static String description = "Un soldat avec des sorts plus faibles, mais qui sait manier son epée !";
	public static List<String> wands = Arrays.asList("");
	public static List<ItemStack> items = new LinkedList();

	public SorceleurKit() {
		super(name, description, wands, items);
		// TODO Auto-generated constructor stub
	}

}
