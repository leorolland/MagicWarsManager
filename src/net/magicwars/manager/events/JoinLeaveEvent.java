package net.magicwars.manager.events;

import java.time.Duration;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

import net.magicwars.manager.MagicWarsManager;
import net.magicwars.manager.db.AsyncCallback;
import net.magicwars.manager.db.DBConnection;
import net.magicwars.manager.db.PlayerDTO;
import net.magicwars.manager.matchmaking.Lobby;

import org.bukkit.entity.EntityType;

public class JoinLeaveEvent implements Listener {

	private MagicWarsManager plugin;

	public JoinLeaveEvent(MagicWarsManager plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		event.setJoinMessage(p.getDisplayName() + ChatColor.YELLOW + " a rejoint le serveur");
		plugin.getDb().getPlayerDAO().updatePlayerAsync(p, new AsyncCallback<PlayerDTO>() {
			@Override
			public void callback(PlayerDTO output, Duration elapsed) {
				System.out.println(ChatColor.GOLD + "[MWManager] updatePlayerAsync(" + p.getName() + ") a duré "
						+ elapsed.toMillis() + "ms.");
			}
		});
		// Téléportation du joueur au spawn
		plugin.getLobby().respawnPlayer(p);
		plugin.getLobby().clearPlayer(p);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		final Player p = event.getPlayer();
		event.setQuitMessage(p.getDisplayName() + ChatColor.RED + " a quitté le serveur");
		plugin.getDb().getPlayerDAO().updatePlayerAsync(p, new AsyncCallback<PlayerDTO>() {
			@Override
			public void callback(PlayerDTO output, Duration elapsed) {
				System.out.println(ChatColor.GOLD + "[MWManager] updatePlayerAsync(" + p.getName() + ") a duré "
						+ elapsed.toMillis() + "ms.");
			}
		});

		// Si le joueur était dans une partie, on l'exclue
		if (plugin.getLobby().getPlayers().contains(p)) {
			plugin.getLobby().kickPlayer(p, "Vous avez quitté la partie.");
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		final Player p = event.getPlayer();
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				// Si le joueur était dans une partie, on le remet
				if (plugin.getLobby().getPlayers().contains(p)) {
					plugin.getLobby().respawnPlayer(p);
				}
			}
		}, 1);
	}

}
