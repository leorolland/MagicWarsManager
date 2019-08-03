package net.magicwars.manager.matchmaking;

import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.magicwars.manager.MagicWarsManager;
import net.magicwars.manager.db.ArenaDTO;
import net.magicwars.manager.db.AsyncCallback;
import net.magicwars.manager.db.DBConnection;
import net.md_5.bungee.api.ChatColor;

public class Lobby {

	private Set<Player> players = new HashSet<Player>();

	private Location lobbyLoc = new Location(Bukkit.getServer().getWorld("world"), 185.0, 4.0, 218.0);

	private Match currentMatch = null;

	private MagicWarsManager plugin;

	private boolean enabled = false;

	// Lobby task
	public static int LOBBY_WAIT_TIME = 10;
	public int elapsedTime = 0;
	private Integer taskId = null;

	public Lobby(MagicWarsManager plugin) {
		this.plugin = plugin;
		Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) plugin, new Runnable() {
			@Override
			public void run() {
				setEnabled(true);
			}
		}, 400L);
	}

	private void addPlayer(Player p) {
		players.add(p);
		if (currentMatch != null)
			currentMatch.addedPlayer(p);
		players.forEach(j -> j.sendMessage(ChatColor.GOLD + p.getName() + " à rejoint le match."));
	}

	private void removePlayer(Player p) {
		players.remove(p);
		if (currentMatch != null)
			currentMatch.removedPlayer(p);
		players.forEach(j -> j.sendMessage(ChatColor.GRAY + p.getName() + " à quitté le match."));
	}

	public Set<Player> getPlayers() {
		return this.players;
	}

	public void respawnPlayer(Player p) {
		if (players.contains(p)) {
			if (this.currentMatch != null) {
				this.currentMatch.respawnPlayer(p);
			} else {
				p.teleport(lobbyLoc);
			}
		} else
			p.teleport(Bukkit.getWorld("world").getSpawnLocation());
	}

	public void runMatch() {
		if (enabled) {
			if (currentMatch == null) {
				// pick random arena
				// OPTIMISATION A FAIRE
				List<ArenaDTO> arenas = plugin.getDb().getArenaDAO().find().asList();
				Random rand = new Random();
				ArenaDTO pickedArena = arenas.get(rand.nextInt(arenas.size()));
				// pick random mode
				int x = rand.nextInt(MatchMode.values().length);
				// MatchMode pickedMode = MatchMode.values()[x];
				MatchMode pickedMode = MatchMode.MME;
				Bukkit.broadcastMessage(ChatColor.GOLD + "Arene tirée aléatoirement : " + pickedArena.getName());
				Bukkit.broadcastMessage(ChatColor.GOLD + "Mode de jeu tiré aléatoirement : " + pickedMode.toString());
				runMatch(pickedArena, pickedMode);
			}
		} else {
			Bukkit.broadcastMessage(ChatColor.RED + "Le matchmaking est désactivé.");
		}
	}

	public void runMatch(ArenaDTO arena, MatchMode mode) {
		if (enabled) {
			if (currentMatch == null) {
				AsyncCallback<Void> callback = new AsyncCallback<Void>() {
					@Override
					public void callback(Void output, Duration elapsed) {
						currentMatch = null;
						Bukkit.broadcastMessage(ChatColor.GOLD + "Le match est terminé, retour au lobby");
						players.forEach((Player p) -> {
							p.teleport(lobbyLoc);
							clearPlayer(p);
						});
						runLobbyTask();
					}
				};
				switch (mode) {
				case MME:
					currentMatch = new MatchAMortEquipe(plugin, arena, players, callback);
					break;
				case FFA:
					// TO-DO
					break;
				}
				;
			}
		} else {
			Bukkit.broadcastMessage(ChatColor.RED + "Le matchmaking est désactivé.");
		}
	}

	public void stopMatch() {
		if (currentMatch != null) {
			currentMatch.end();
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		if (enabled == this.enabled)
			return;
		if (!enabled) {
			stopMatch();
			kickAllPlayers();
			stopLobbyTask();
			Bukkit.broadcastMessage(ChatColor.RED + "MatchMaking desactivé.");
		} else {
			this.runLobbyTask();
			Bukkit.broadcastMessage(ChatColor.GREEN + "MatchMaking activé, tapez " + ChatColor.WHITE + "/match join"
					+ ChatColor.GREEN + " pour rejoindre la partie.");
		}
		this.enabled = enabled;
	}

	public void kickPlayer(Player p, String message) {
		if (players.contains(p)) {
			clearPlayer(p);
			p.sendMessage(ChatColor.RED + message);
			removePlayer(p);
			respawnPlayer(p);
		} else
			p.sendMessage(ChatColor.RED + "Vous n'êtes pas dans la partie.");
	}

	public void joinPlayer(Player p) {
		if (enabled) {
			if (!players.contains(p)) {
				clearPlayer(p);
				p.sendMessage(ChatColor.GREEN + "Vous avez rejoint la partie.");
				addPlayer(p);
				respawnPlayer(p);
			} else
				p.sendMessage(ChatColor.RED + "Vous êtes déjà dans la partie.");
		} else {
			p.sendMessage(ChatColor.RED + "Le matchmaking est désactivé.");
		}
	}

	public void clearPlayer(Player p) {
		p.getInventory().clear();
		p.setHealth(20.0);
		p.setFoodLevel(20);
		p.setFireTicks(0);
	}
	
	public void kickAllPlayers() {
		players.forEach((Player p) -> {
			kickPlayer(p, "Tous les joueurs ont été exclus de la partie.");
		});
		players = new HashSet<Player>();
	}

	/*
	 * LOBBY TASK (Timer)
	 */
	public void runLobbyTask() {
		if (taskId == null) {
			elapsedTime = 0;
			taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
				public void run() {
					tick();
				}
			}, 0L, 20L);
		}
	}

	public void stopLobbyTask() {
		if (taskId != null) {
			Bukkit.getServer().getScheduler().cancelTask(taskId);
			taskId = null;
		}
	}

	// Every second loop
	private void tick() {
		if (enabled) {
			elapsedTime++;
			int rest = LOBBY_WAIT_TIME - elapsedTime;
			// xpBar timer
			float restPercentage = (float) rest / (float) LOBBY_WAIT_TIME;
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.setExp(restPercentage);
				p.setLevel(rest);
			}
			// BroadCasts
			if (rest == 2)
				Bukkit.broadcastMessage(ChatColor.GOLD + "La partie va commencer ...");
			if (rest < 1) {
				runMatch();
				stopLobbyTask();
				return;
			}
		} else {
			stopLobbyTask();
		}
	}

}
