package net.magicwars.manager.executors;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.magicwars.manager.MagicWarsManager;
import net.magicwars.manager.db.ArenaDTO;
import net.magicwars.manager.db.DBConnection;
import net.md_5.bungee.api.ChatColor;

public class ArenaExecutor implements CommandExecutor {


	private MagicWarsManager plugin;

	public ArenaExecutor(MagicWarsManager plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		// /arena list
		if (args.length == 1 && args[0].equals("list")) {
			List<String> arenes = plugin.getDb().getArenaDAO().find().asList().stream().map((ArenaDTO ad)->ad.getName()).collect(Collectors.toList());
			String list = String.join(", ", arenes);
			sender.sendMessage("Arènes disponibles :");
			sender.sendMessage(list);
			return true;
		}
		// /arena delete <arena>
		if (args.length == 2 && args[0].equals("delete")) {
			if (plugin.getDb().getArenaDAO().deleteArena(args[1])) {
				sender.sendMessage(ChatColor.GREEN + "Arène supprimée.");
			} else {
				sender.sendMessage(ChatColor.RED + "Cette arène n'éxiste pas.");
			}
			return true;
		}
		// /arena enable <arena>
		if (args.length == 2 && args[0].equals("enable")) {
			if (plugin.getDb().getArenaDAO().setEnabled(args[1], true)) {
				sender.sendMessage(ChatColor.GREEN + "Arène activée.");
			} else {
				sender.sendMessage(ChatColor.RED + "Cette arène n'éxiste pas.");
			}
			return true;
		}
		// /arena disable <arena>
		if (args.length == 2 && args[0].equals("disable")) {
			if (plugin.getDb().getArenaDAO().setEnabled(args[1], false)) {
				sender.sendMessage(ChatColor.GREEN + "Arène désactivée.");
			} else {
				sender.sendMessage(ChatColor.RED + "Cette arène n'éxiste pas.");
			}
			return true;
		}
		// /arena edit <arena> <action> <name>
		if (args.length >= 4 && args[0].equals("edit") && sender instanceof Player) {
			Player p = (Player) sender;
			String nom = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
			p.sendMessage(ChatColor.GOLD + "nom : " + nom);
			ArenaDTO arena;
			switch (args[2]) {
			case "addRedSpawn":
				arena = plugin.getDb().getArenaDAO().setArenaRedSpawn(args[1], nom, p);
				p.sendMessage(ChatColor.GREEN + "Arene " + arena.getName() + " editée avec succès.");
				break;
			case "addBlueSpawn":
				arena = plugin.getDb().getArenaDAO().setArenaBlueSpawn(args[1], nom, p);
				p.sendMessage(ChatColor.GREEN + "Arene " + arena.getName() + " editée avec succès.");

				break;
			case "addNeutralSpawn":
				arena = plugin.getDb().getArenaDAO().setArenaNeutralSpawn(args[1], nom, p);
				p.sendMessage(ChatColor.GREEN + "Arene " + arena.getName() + " editée avec succès.");

				break;
			case "deleteRedSpawn":
				arena = plugin.getDb().getArenaDAO().deleteArenaRedSpawn(args[1], nom);
				p.sendMessage(
						ChatColor.GREEN + "Spawn rouge " + nom + " de l'arène " + arena.getName() + " supprimés.");
				break;
			case "deleteBlueSpawn":
				arena = plugin.getDb().getArenaDAO().deleteArenaBlueSpawn(args[1], nom);
				p.sendMessage(ChatColor.GREEN + "Spawn bleu " + nom + " de l'arène " + arena.getName() + " supprimés.");
				break;
			case "deleteNeutralSpawn":
				arena = plugin.getDb().getArenaDAO().deleteArenaNeutralSpawn(args[1], nom);
				p.sendMessage(
						ChatColor.GREEN + "Spawn neutre " + nom + " de l'arène " + arena.getName() + " supprimés.");
				break;
			}
			return true;
		}
		// /arena edit <arena> set<team>Respawn
		if (args.length == 3 && args[0].equals("edit") && sender instanceof Player) {
			Player p = (Player) sender;
			ArenaDTO arena;
			switch (args[2]) {
			case "setRedRespawn":
				arena = plugin.getDb().getArenaDAO().setArenaRedRespawn(args[1], p);
				p.sendMessage(ChatColor.GREEN + "Arene " + arena.getName() + " editée avec succès.");
				break;
			case "setBlueRespawn":
				arena = plugin.getDb().getArenaDAO().setArenaBlueRespawn(args[1], p);
				p.sendMessage(ChatColor.GREEN + "Arene " + arena.getName() + " editée avec succès.");
				break;
			case "setNeutralRespawn":
				arena = plugin.getDb().getArenaDAO().setArenaNeutralRespawn(args[1], p);
				p.sendMessage(ChatColor.GREEN + "Arene " + arena.getName() + " editée avec succès.");
				break;
			}
			return true;
		}
		sender.sendMessage(ChatColor.GOLD + "Activer/Désactiver une arène");
		sender.sendMessage("/arena enable <arena>");
		sender.sendMessage("/arena disable <arena>");
		sender.sendMessage(ChatColor.GOLD + "Définir le point de réapparition après la mort");
		sender.sendMessage("/arena edit <arena> setRedRespawn");
		sender.sendMessage("/arena edit <arena> setBlueRespawn");
		sender.sendMessage("/arena edit <arena> setNeutralRespawn");
		sender.sendMessage(ChatColor.GOLD + "Définir les choix de spawn dans l'arène");
		sender.sendMessage("/arena edit <arena> addRedSpawn <name>");
		sender.sendMessage("/arena edit <arena> addBlueSpawn <name>");
		sender.sendMessage("/arena edit <arena> addNeutralSpawn <name>");
		sender.sendMessage("/arena edit <arena> deleteRedSpawn <name>");
		sender.sendMessage("/arena edit <arena> deleteBlueSpawn <name>");
		sender.sendMessage("/arena edit <arena> deleteNeutralSpawn <name>");
		sender.sendMessage(ChatColor.GOLD + "Supprimer une arène");
		sender.sendMessage("/arena delete <arena>");
		return true;
	}

}
