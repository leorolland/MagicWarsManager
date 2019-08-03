package net.magicwars.manager.kits;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public abstract class Classe {

	/**
	 * Nom de la classe (ex: Assassin)
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * Description de la classe (ex: "Assassine tes ennemis !")
	 * 
	 * @return
	 */
	public abstract String getDescription();

	/**
	 * 
	 * @param lvl
	 *            Niveau du joueur dans la classe (1 à 200).
	 * @return Le kit correspondant à ce niveau
	 */
	public abstract Kit getKit(Integer lvl);

	/**
	 * Equipe la classe à un joueur en prenant en compte son niveau dans la classe.
	 * 
	 * @param p
	 */
	public void equipTo(Player p) {
		Integer lvl = 1;
		equipTo(p, lvl);
	}

	/**
	 * Equipe la classe à un joueur en forçant la valeur du niveau.
	 * 
	 * @param p
	 * @param lvl
	 *            niveau de joueur
	 */
	public void equipTo(Player p, Integer lvl) {
		giveKit(p, getKit(lvl));
	}

	/**
	 * Donne un kit a un joueur
	 * @param p
	 * @param kit
	 */
	public static void giveKit(Player p, Kit kit) {
		PlayerInventory inv = p.getInventory();
		// Clear inventory
		inv.clear();
		// Give wands
		kit.getWands().forEach((String wand) -> {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mgive "+p.getName()+" "+wand);
		});
		// Give items
		kit.getItems().forEach((ItemStack is) -> {
			inv.addItem(is);
		});
		// Equip armor
		inv.setHelmet(kit.getHelmet());
		inv.setChestplate(kit.getChestplate());
		inv.setLeggings(kit.getLeggings());
		inv.setBoots(kit.getBoots());
	}

}
