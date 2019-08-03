package net.magicwars.manager;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.magicwars.manager.db.DBConnection;
import net.magicwars.manager.events.JoinLeaveEvent;
import net.magicwars.manager.events.LevelsEvents;
import net.magicwars.manager.executors.ArenaExecutor;
import net.magicwars.manager.executors.LevelsExecutor;
import net.magicwars.manager.executors.ManagerExecutor;
import net.magicwars.manager.executors.MatchExecutor;
import net.magicwars.manager.executors.MkitExecutor;
import net.magicwars.manager.matchmaking.Lobby;

public class MagicWarsManager extends JavaPlugin {

	public static String pluginName = "MagicWarsManager";
	public PluginManager pm;
	public DBConnection db;
	public Lobby lobby = null;
	private LevelsEvents le;

	@Override
	public void onEnable() {
		pm = getServer().getPluginManager(); // On récupère le PluginManager du serveur
		le = new LevelsEvents(this);
		pm.registerEvents(le, this);
		db = new DBConnection(this);
		lobby = new Lobby(this);
		this.hookExecutors();
		pm.registerEvents(new JoinLeaveEvent(this), this);
		// Run lobby
	}

	@Override
	public void onDisable() {

	}


	private void hookExecutors() {
		CommandExecutor managerE = new ManagerExecutor(this);
		getCommand("manager").setExecutor(managerE);
		CommandExecutor mkitE = new MkitExecutor();
		getCommand("mkit").setExecutor(mkitE);
		CommandExecutor matchE = new MatchExecutor(this);
		getCommand("match").setExecutor(matchE);
		CommandExecutor arenaE = new ArenaExecutor(this);
		getCommand("arena").setExecutor(arenaE);
		CommandExecutor levelsE = new LevelsExecutor(this);
		getCommand("levels").setExecutor(levelsE);
	}

	public DBConnection getDb() {
		return db;
	}

	public Lobby getLobby() {
		return lobby;
	}

	public LevelsEvents getLe() {
		return le;
	}
	
	

}