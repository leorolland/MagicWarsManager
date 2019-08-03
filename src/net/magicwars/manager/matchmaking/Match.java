package net.magicwars.manager.matchmaking;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.magicwars.manager.db.ArenaDTO;
import net.magicwars.manager.db.AsyncCallback;

public abstract class Match {

	protected int totalDuration;
	protected int elapsedTime = 0;
	private int taskId;
	private AsyncCallback<Void> endCallback;
	protected ArenaDTO arena;
	protected Set<Player> players;

	public Match(int totalDuration, Plugin plugin, AsyncCallback<Void> endCallback, ArenaDTO arena, Set<Player> players) {
		this.endCallback = endCallback;
		this.totalDuration = totalDuration;
		this.arena = arena;
		this.players = players;
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendTitle("", "Durée: "+totalDuration+"s", 0, 40, 20);
		}
		taskId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				tick();
			}
		}, 0L, 20L);
	}

	public void tick() {
		elapsedTime++;
		int rest = (totalDuration - elapsedTime);
		String restString = String.valueOf(rest);
		// xpBar timer
		if ( rest == 30 || rest == 60 ) {			
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendTitle("", restString+" secondes", 0, 20, 0);
			}
		}
		if ( rest<10 ) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.sendTitle("", restString, 0, 2, 10);
			}
		}
		// catch end
		if ((totalDuration - elapsedTime) < 1)
			end();
	};

	public void end() {
		endCallback.callback(null, null);
		Bukkit.getServer().getScheduler().cancelTask(taskId);
	}

	public abstract void respawnPlayer(Player p);
	public abstract void addedPlayer(Player p);
	public abstract void removedPlayer(Player p);

}
