package net.magicwars.manager;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.magicwars.manager.db.DBConnection;
import net.magicwars.manager.events.JoinLeaveEvent;
import net.magicwars.manager.executors.ManagerExecutor;
import net.magicwars.manager.executors.MkitExecutor;

public class MagicWarsManager extends JavaPlugin {

	public static String pluginName = "MagicWarsManager";
	public PluginManager pm;
	public DBConnection db;

	@Override
	public void onEnable() {
		pm = getServer().getPluginManager(); // On récupère le PluginManager du serveur
		db = new DBConnection(this);
		this.hookExecutors();
		pm.registerEvents(new JoinLeaveEvent(db), this);
	}

	@Override
	public void onDisable() {

	}


	private void hookExecutors() {
		CommandExecutor managerE = new ManagerExecutor(this, db);
		getCommand("manager").setExecutor(managerE);
		CommandExecutor mkitE = new MkitExecutor(this, db);
		getCommand("mkit").setExecutor(mkitE);
	}

}