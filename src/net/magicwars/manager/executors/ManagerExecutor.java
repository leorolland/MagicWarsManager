package net.magicwars.manager.executors;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.magicwars.manager.db.AsyncCallback;
import net.magicwars.manager.db.DBConnection;
import net.magicwars.manager.db.PlayerDTO;

public class ManagerExecutor implements CommandExecutor {

	private DBConnection db;

	private Plugin plugin;

	public ManagerExecutor(Plugin plugin, DBConnection db) {
		this.plugin = plugin;
		this.db = db;
	}

	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (args.length > 0) {
			if (args[0].equals("info")) {
				
				if (args.length == 2) {
					// Un autre que le joueur pseudo est entr�
					db.getPlayerDAO().getPlayerAsync(args[1], new AsyncCallback<Optional<PlayerDTO>>() {
						public void callback(Optional<PlayerDTO> opd, Duration elapsed) {
							if (opd.isPresent()) {
								sendInformations(sender, opd.get());
							} else {
								sender.sendMessage("Le joueur " + args[1] + " ne s'est jamais connect�. ("+elapsed.toMillis()+"ms)");
							}
						}
					});
					
				} else {
					if (sender instanceof Player) {
						// Le joueur concern� est le lanceur de la commande						
						db.getPlayerDAO().getPlayerAsync((Player) sender, new AsyncCallback<PlayerDTO>() {
							public void callback(PlayerDTO pd, Duration elapsed) {
								sendInformations(sender, pd);
								sender.sendMessage("("+elapsed.toMillis()+"ms)");
							}
						});
					} else {
						sender.sendMessage("Vous ne pouvez pas ex�cuter cette commande ici.");
					}
				}
				return true;

			} else if (args[0].equals("update")) {
				if (args.length == 2) {
					Player p = plugin.getServer().getPlayer(args[1]);
					if (p == null) {
						sender.sendMessage("Ce joueur n'est pas connect� !");
					} else {
						db.getPlayerDAO().updatePlayerAsync(p, new AsyncCallback<PlayerDTO>() {
							public void callback(PlayerDTO p, Duration elapsed) {
								Instant end = Instant.now();
								sender.sendMessage("Donn�es du joueur mises � jour ! (" + elapsed.toMillis() +"ms)");
							}
						});
						sender.sendMessage("Mise a jour des donn�es ...");
					}
				} else {
					sender.sendMessage("/manager update <username>");
				}
				return true;
			}
		}
		sender.sendMessage("Aide du manager :");
		sender.sendMessage("�a/manager info �eR�cup�re les informations sur vous.");
		sender.sendMessage("�a/manager info <username> �eR�cup�re les informations sur un joueur.");
		sender.sendMessage("�a/manager update <username> �eMet a jour les informations en BDD d'un joueur connect�.");
		return true;
	}

	public void sendInformations(CommandSender sender, PlayerDTO pd) {
		sender.sendMessage("- Informations sur le joueur �a" + pd.getUsername());
		sender.sendMessage("- UUID : �a" + pd.getUuid());
		sender.sendMessage("- Derni�re connexion : �a" + pd.getLastLogin());
		sender.sendMessage("- Adresse IP : �a" + pd.getIp());
		sender.sendMessage("- XP : �a" + pd.getXp());
		sender.sendMessage("- R�putation : �a" + pd.getReputation());
		sender.sendMessage("- Localisation : �a" + pd.getLocation().toString());
	}

}
