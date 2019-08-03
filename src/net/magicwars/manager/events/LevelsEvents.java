package net.magicwars.manager.events;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
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

public class LevelsEvents implements Listener {

	private Map<Player, Long> playerXp = Collections.synchronizedMap(new HashMap<Player, Long>());

	private MagicWarsManager plugin;

	public LevelsEvents(MagicWarsManager plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		// Si on possède l'info sur le joueur
		Long xp = playerXp.get(e.getPlayer());
		if (xp != null)
			e.setFormat(generatePlayerPrefix(xpToLevel(xp)) + ChatColor.GRAY + e.getPlayer().getName()
					+ ChatColor.DARK_GRAY + " " + e.getMessage());
		else {
			e.getPlayer().sendMessage(ChatColor.RED + "Chargement de vos données, attendez quelques secondes svp...");
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onKill(PlayerDeathEvent e) {
		String killed = e.getEntity().getName();
		Player killer = e.getEntity().getKiller();
		e.setDeathMessage(ChatColor.RED + killed + " à été tué par " + killer.getName());
		e.setDroppedExp(0);
		killer.sendMessage("Vous avez tué " + killed + ", votre niveau d'expérience augmente !");
		killer.playSound(killer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
		plugin.getDb().getPlayerDAO().addPlayerXpAsync(killer, 10);		
	}

	/**
	 * Lors de la connexion, on récupère le level du joueur.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		updatePlayerLevel(event.getPlayer());
	}

	/**
	 * Fait une requete en BDD de l'xp d'un joueur et met à jour "playerXp"
	 * 
	 * @param p
	 */
	public void updatePlayerLevel(Player p) {
		AsyncCallback<PlayerDTO> callback = new AsyncCallback<PlayerDTO>() {
			@Override
			public void callback(PlayerDTO output, Duration elapsed) {
				playerXp.put(p, output.getXp());
				Bukkit.getLogger().info(ChatColor.GOLD + "[MWManager] Données de niveau de " + p.getName()
						+ " chargées. (" + elapsed.toMillis() + " ms)");
			}
		};
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				plugin.getDb().getPlayerDAO().getPlayerAsync(p, callback);
			}
		});
	}

	/**
	 * Met à jour "playerXp", si le joueur est connecté
	 */
	public void localSetPlayerLevel(Player p, Long lvl) {
		// Si le joueur est connecté
		if (playerXp.keySet().contains(p)) {
			int ancienLvl = xpToLevel(playerXp.get(p));
			playerXp.put(p, lvl);
			int nouveauLvl = xpToLevel(playerXp.get(p));
			if (ancienLvl != nouveauLvl) {
				Bukkit.broadcastMessage(ChatColor.GOLD + p.getName() + ChatColor.GRAY + " vient de passer niveau " + ChatColor.GOLD + nouveauLvl);
			}
		}
	}

	/**
	 * Lors de la déconnection, on supprime le joueur de "levels".
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		playerXp.remove(event.getPlayer());
	}

	public static int xpToLevel(long xp) {
		// 1+2*sqrt(1/20*x)
		int level = (int) (1D + 2D * Math.sqrt(1D / 20D * (long) xp));
		return level;
	}

	public static String generatePlayerPrefix(int level) {
		return ChatColor.BLUE + "[" + ChatColor.AQUA + "Niv." + level + ChatColor.BLUE + "] ";
	}

}
