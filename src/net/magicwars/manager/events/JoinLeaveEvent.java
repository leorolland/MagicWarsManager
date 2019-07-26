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

import net.magicwars.manager.db.AsyncCallback;
import net.magicwars.manager.db.DBConnection;
import net.magicwars.manager.db.PlayerDTO;

import org.bukkit.entity.EntityType;

public class JoinLeaveEvent implements Listener {

	DBConnection db;

	public JoinLeaveEvent(DBConnection db) {
		this.db = db;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player p = event.getPlayer();
		event.setJoinMessage(p.getDisplayName() + ChatColor.YELLOW + " a rejoint le serveur FB !");
		db.getPlayerDAO().updatePlayerAsync(p, new AsyncCallback<PlayerDTO>() {
			@Override
			public void callback(PlayerDTO output, Duration elapsed) {
				System.out.println(ChatColor.GOLD + "[FB-PlotManager] updatePlayerAsync(" + p.getName() + ") a duré "
						+ elapsed.toMillis() + "ms.");
			}
		});
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		final Player p = event.getPlayer();
		event.setQuitMessage(p.getDisplayName() + ChatColor.RED + " a quitté le serveur FB !");
		db.getPlayerDAO().updatePlayerAsync(p, new AsyncCallback<PlayerDTO>() {
			@Override
			public void callback(PlayerDTO output, Duration elapsed) {
				System.out.println(ChatColor.GOLD + "[FB-PlotManager] updatePlayerAsync(" + p.getName() + ") a duré "
						+ elapsed.toMillis() + "ms.");
			}
		});
	}

}
