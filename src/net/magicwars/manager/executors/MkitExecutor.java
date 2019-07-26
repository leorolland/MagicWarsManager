package net.magicwars.manager.executors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.magicwars.manager.db.DBConnection;

public class MkitExecutor implements CommandExecutor {

	private DBConnection db;

	private Plugin plugin;

	public MkitExecutor(Plugin plugin, DBConnection db) {
		this.plugin = plugin;
		this.db = db;
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		
//		if (args.length == 3 && args[0].equals("give")) {
//			Player p = Bukkit.getServer().getPlayer(args[1]);
//			if (p!=null) {
//				// TO-DO
//			} else {
//				sender.sendMessage("Ce joueur n'est pas connecté sur le serveur.");
//			}
//		}

		sender.sendMessage("/mkits give <player> <kit>");
		return true;
	}

}
