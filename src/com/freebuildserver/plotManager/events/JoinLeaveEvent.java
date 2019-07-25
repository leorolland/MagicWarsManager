package com.freebuildserver.plotManager.events;

import java.time.Duration;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.freebuildserver.plotManager.db.AsyncCallback;
import com.freebuildserver.plotManager.db.DBConnection;
import com.freebuildserver.plotManager.db.PlayerDTO;

public class JoinLeaveEvent implements Listener {
	
	DBConnection db;
	
	public JoinLeaveEvent(DBConnection db) {
		this.db = db;
	}
	
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
		final Player p = event.getPlayer();
        event.setJoinMessage(p.getDisplayName() + ChatColor.YELLOW + " a rejoint le serveur FB !");
        db.getPlayerDAO().updatePlayerAsync(p, new AsyncCallback<PlayerDTO>() {
			@Override
			public void callback(PlayerDTO output, Duration elapsed) {
				System.out.println(ChatColor.GOLD + "[FB-PlotManager] updatePlayerAsync("+p.getName()+") a duré "+elapsed.toMillis()+"ms.");
			}
        });
    }
	
	@EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
		final Player p = event.getPlayer();
        event.setQuitMessage(p.getDisplayName() + ChatColor.RED + " a quitté le serveur FB !");
        db.getPlayerDAO().updatePlayerAsync(p, new AsyncCallback<PlayerDTO>() {
			@Override
			public void callback(PlayerDTO output, Duration elapsed) {
				System.out.println(ChatColor.GOLD + "[FB-PlotManager] updatePlayerAsync("+p.getName()+") a duré "+elapsed.toMillis()+"ms.");
			}
        });
    }
}
