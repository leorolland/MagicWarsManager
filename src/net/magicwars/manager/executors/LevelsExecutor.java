package net.magicwars.manager.executors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.magicwars.manager.MagicWarsManager;
import net.magicwars.manager.db.ArenaDTO;
import net.magicwars.manager.db.DBConnection;
import net.magicwars.manager.events.LevelsEvents;
import net.magicwars.manager.matchmaking.Lobby;
import net.magicwars.manager.matchmaking.MatchMode;
import net.md_5.bungee.api.ChatColor;

public class LevelsExecutor implements CommandExecutor {

	private MagicWarsManager plugin;

	public LevelsExecutor(MagicWarsManager plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		
		if (args.length == 3 && args[0].equals("set")) {
			Player p = Bukkit.getPlayer(args[1]);
			if (p != null) {
				plugin.getDb().getPlayerDAO().setPlayerXpAsync(p, Long.valueOf(args[2]));
				sender.sendMessage(ChatColor.GREEN + "xp ajoutés.");
			} else
				sender.sendMessage(ChatColor.RED + "Joueur introuvable.");
			return true;
		}
		
		if (args.length == 3 && args[0].equals("add")) {
			Player p = Bukkit.getPlayer(args[1]);
			if (p != null) {
				plugin.getDb().getPlayerDAO().addPlayerXpAsync(p, Long.valueOf(args[2]));
				sender.sendMessage(ChatColor.GREEN + "Joueur modifié.");
			} else
				sender.sendMessage(ChatColor.RED + "Joueur introuvable.");
			return true;
		}
		
		sender.sendMessage("/levels add <player> <amount>");
		sender.sendMessage("/levels set <player> <amount>");
		return true;
	}

}
