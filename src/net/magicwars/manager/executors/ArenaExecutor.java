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
			sender.sendMessage("Ar�nes disponibles :");
			sender.sendMessage(list);
			return true;
		}
		// /arena delete <arena>
		if (args.length == 2 && args[0].equals("delete")) {
			if (plugin.getDb().getArenaDAO().deleteArena(args[1])) {
				sender.sendMessage(ChatColor.GREEN + "Ar�ne supprim�e.");
			} else {
				sender.sendMessage(ChatColor.RED + "Cette ar�ne n'�xiste pas.");
			}
			return true;
		}
		// /arena enable <arena>
		if (args.length == 2 && args[0].equals("enable")) {
			if (plugin.getDb().getArenaDAO().setEnabled(args[1], true)) {
				sender.sendMessage(ChatColor.GREEN + "Ar�ne activ�e.");
			} else {
				sender.sendMessage(ChatColor.RED + "Cette ar�ne n'�xiste pas.");
			}
			return true;
		}
		// /arena disable <arena>
		if (args.length == 2 && args[0].equals("disable")) {
			if (plugin.getDb().getArenaDAO().setEnabled(args[1], false)) {
				sender.sendMessage(ChatColor.GREEN + "Ar�ne d�sactiv�e.");
			} else {
				sender.sendMessage(ChatColor.RED + "Cette ar�ne n'�xiste pas.");
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
				p.sendMessage(ChatColor.GREEN + "Arene " + arena.getName() + " edit�e avec succ�s.");
				break;
			case "addBlueSpawn":
				arena = plugin.getDb().getArenaDAO().setArenaBlueSpawn(args[1], nom, p);
				p.sendMessage(ChatColor.GREEN + "Arene " + arena.getName() + " edit�e avec succ�s.");

				break;
			case "addNeutralSpawn":
				arena = plugin.getDb().getArenaDAO().setArenaNeutralSpawn(args[1], nom, p);
				p.sendMessage(ChatColor.GREEN + "Arene " + arena.getName() + " edit�e avec succ�s.");

				break;
			case "deleteRedSpawn":
				arena = plugin.getDb().getArenaDAO().deleteArenaRedSpawn(args[1], nom);
				p.sendMessage(
						ChatColor.GREEN + "Spawn rouge " + nom + " de l'ar�ne " + arena.getName() + " supprim�s.");
				break;
			case "deleteBlueSpawn":
				arena = plugin.getDb().getArenaDAO().deleteArenaBlueSpawn(args[1], nom);
				p.sendMessage(ChatColor.GREEN + "Spawn bleu " + nom + " de l'ar�ne " + arena.getName() + " supprim�s.");
				break;
			case "deleteNeutralSpawn":
				arena = plugin.getDb().getArenaDAO().deleteArenaNeutralSpawn(args[1], nom);
				p.sendMessage(
						ChatColor.GREEN + "Spawn neutre " + nom + " de l'ar�ne " + arena.getName() + " supprim�s.");
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
				p.sendMessage(ChatColor.GREEN + "Arene " + arena.getName() + " edit�e avec succ�s.");
				break;
			case "setBlueRespawn":
				arena = plugin.getDb().getArenaDAO().setArenaBlueRespawn(args[1], p);
				p.sendMessage(ChatColor.GREEN + "Arene " + arena.getName() + " edit�e avec succ�s.");
				break;
			case "setNeutralRespawn":
				arena = plugin.getDb().getArenaDAO().setArenaNeutralRespawn(args[1], p);
				p.sendMessage(ChatColor.GREEN + "Arene " + arena.getName() + " edit�e avec succ�s.");
				break;
			}
			return true;
		}
		sender.sendMessage(ChatColor.GOLD + "Activer/D�sactiver une ar�ne");
		sender.sendMessage("/arena enable <arena>");
		sender.sendMessage("/arena disable <arena>");
		sender.sendMessage(ChatColor.GOLD + "D�finir le point de r�apparition apr�s la mort");
		sender.sendMessage("/arena edit <arena> setRedRespawn");
		sender.sendMessage("/arena edit <arena> setBlueRespawn");
		sender.sendMessage("/arena edit <arena> setNeutralRespawn");
		sender.sendMessage(ChatColor.GOLD + "D�finir les choix de spawn dans l'ar�ne");
		sender.sendMessage("/arena edit <arena> addRedSpawn <name>");
		sender.sendMessage("/arena edit <arena> addBlueSpawn <name>");
		sender.sendMessage("/arena edit <arena> addNeutralSpawn <name>");
		sender.sendMessage("/arena edit <arena> deleteRedSpawn <name>");
		sender.sendMessage("/arena edit <arena> deleteBlueSpawn <name>");
		sender.sendMessage("/arena edit <arena> deleteNeutralSpawn <name>");
		sender.sendMessage(ChatColor.GOLD + "Supprimer une ar�ne");
		sender.sendMessage("/arena delete <arena>");
		return true;
	}

}
