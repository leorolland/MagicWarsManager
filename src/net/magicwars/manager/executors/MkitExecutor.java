package net.magicwars.manager.executors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import com.google.gson.Gson;

import net.magicwars.manager.db.DBConnection;
import net.magicwars.manager.kits.Serializer;

public class MkitExecutor implements CommandExecutor {

	private DBConnection db;

	private Plugin plugin;

	public MkitExecutor(Plugin plugin, DBConnection db) {
		this.plugin = plugin;
		this.db = db;
	}

	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		
//		if (args.length == 3 && args[0].equals("give")) {
//			Player p = Bukkit.getServer().getPlayer(args[1]);
//			if (p!=null) {
//				// TO-DO
//			} else {
//				sender.sendMessage("Ce joueur n'est pas connecté sur le serveur.");
//			}
//		}
		
		if (args.length == 4 && args[0].equals("edit") && sender instanceof Player) {
			Player p = (Player) sender;
			PlayerInventory inv = p.getInventory();
			Gson gson = new Gson();
			ItemStack is = inv.getItem(0);
			Map<String, Object> map = is.serialize();
			p.sendMessage(map.toString());
			p.sendMessage("-----");
			map.forEach((String s, Object o)->{
				p.sendMessage("champ "+ s);
				p.sendMessage(gson.toJson(o));
			});
			// db.getKitDAO().updateKitSync(inv, args[1], Integer.valueOf(args[2]), args[3]);
			p.sendMessage("Le kit "+args[1]+" à bien été mis à jour. (Synchrone)");
		}

		sender.sendMessage("/mkit give <player> <kit(string)> <level(int)>");
		sender.sendMessage("/mkit edit <kit(string)> <level(int)> <desc(string)>");
		sender.sendMessage("/mkit delete <kit(string)>");
		return true;
	}

}
