package net.magicwars.manager.executors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.magicwars.manager.MagicWarsManager;
import net.magicwars.manager.db.ArenaDTO;
import net.magicwars.manager.db.DBConnection;
import net.magicwars.manager.matchmaking.Lobby;
import net.magicwars.manager.matchmaking.MatchMode;
import net.md_5.bungee.api.ChatColor;

public class MatchExecutor implements CommandExecutor {

	private MagicWarsManager plugin;

	public MatchExecutor(MagicWarsManager plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		// /match run mme
		if (args.length == 2 && args[0].equals("run")) {
			switch (args[1]) {
			case "mme":
				ArenaDTO arena = plugin.getDb().getArenaDAO().findOne("name", "test");
				if (arena != null) {
					plugin.getLobby().runMatch(arena, MatchMode.MME);
				}
			}
			return true;
		}
		// /match join
		if (args.length == 1 && args[0].equals("join") && sender instanceof Player) {
			Player p = (Player) sender;
			plugin.getLobby().joinPlayer(p);
			return true;
		}
		// /match leave
		if (args.length == 1 && args[0].equals("leave") && sender instanceof Player) {
			Player p = (Player) sender;
			plugin.getLobby().kickPlayer(p, "Vous avez quitté la partie");
			return true;
		}
		// /match stop
		if (args.length == 1 && args[0].equals("stop")) {
			plugin.getLobby().stopMatch();
			return true;
		}
		// /match enable
		if (args.length == 1 && args[0].equals("enable")) {
			plugin.getLobby().setEnabled(true);
			return true;
		}
		// /match disable
		if (args.length == 1 && args[0].equals("disable")) {
			plugin.getLobby().setEnabled(false);;
			return true;
		}
		sender.sendMessage("/match run mme");
		sender.sendMessage("/match stop");
		sender.sendMessage("/match join");
		sender.sendMessage("/match leave");
		return true;
	}

}
