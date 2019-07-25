package com.freebuildserver.plotManager;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.freebuildserver.events.JoinLeaveEvent;
import com.freebuildserver.plotManager.db.DBConnection;
import com.github.intellectualsites.plotsquared.api.PlotAPI;

public class PlotManagerPlugin extends JavaPlugin {

	public static String pluginName = "FB-PlotManager";
	public PlotAPI api;
	public PluginManager pm;
	public DBConnection db;

	@Override
	public void onEnable() {
		pm = getServer().getPluginManager(); // On récupère le PluginManager du serveur
		db = new DBConnection(this);
		this.hookPlotSquarred();
		this.hookExecutors();
		pm.registerEvents(new JoinLeaveEvent(db), this);
	}

	@Override
	public void onDisable() {

	}

	private void hookPlotSquarred() {
		final Plugin plotsquared = pm.getPlugin("PlotSquared");
		if (plotsquared != null && !plotsquared.isEnabled()) {
			Bukkit.getLogger().warning("Désactivation de " + pluginName);
			this.pm.disablePlugin(this);
			return;
		}
		api = new PlotAPI();
	}

	private void hookExecutors() {
		CommandExecutor cmdExecutor = new CmdExecutor(this, api, db);
		CommandExecutor managerExecutor = new ManagerExecutor(this, db);
		getCommand("menu").setExecutor(cmdExecutor);
		getCommand("manager").setExecutor(managerExecutor);

	}

}