package net.magicwars.manager.kits;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import com.google.gson.*;

public class Serializer {
	
	public static Map<String, Object> serializeItemStack(ItemStack item) {
		return item.serialize();
	}
	
	public static ItemStack deserializeItemStack(Map<String, Object> is) {
		return ItemStack.deserialize(is);
	}

	public static Map<Integer, Map<String,String>> serializeInventory(PlayerInventory inv) {
		Gson gson = new Gson();
		Map<Integer, Map<String,String>> output = new HashMap<Integer, Map<String, String>>();
		for (int i=0 ; i<inv.getSize() ; i++) {
			if (inv.getItem(i) != null) {				
				Map<String,Object> raw = inv.getItem(i).serialize();
				Map<String,String> parsed = new HashMap<String,String>();
				raw.forEach((String s, Object o) -> {
					parsed.put(s, gson.toJson(o));
				});
				output.put(i, parsed);
			}
		}
		return output;
	}
	
	public static void equipPlayer(Map<Integer, Map<String,String>> inventory, Player p) {
		Gson gson = new Gson();
		PlayerInventory pInv = p.getInventory();
		inventory.forEach((Integer i, Map<String,String> is) -> {
			Map<String,Object> raw = new HashMap<String,Object>();
			is.forEach((String s, String o) -> {
				raw.put(s, gson.fromJson(o, Object.class));
			});
			pInv.setItem(i, ItemStack.deserialize(raw));
		});
	}
	
	
}
