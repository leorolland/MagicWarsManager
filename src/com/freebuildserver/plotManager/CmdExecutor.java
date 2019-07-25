package com.freebuildserver.plotManager;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.freebuildserver.plotManager.db.DBConnection;
import com.freebuildserver.plotManager.gui.*;
import com.github.intellectualsites.plotsquared.api.PlotAPI;
import com.github.intellectualsites.plotsquared.plot.object.Plot;
import com.github.intellectualsites.plotsquared.plot.object.PlotPlayer;

public class CmdExecutor implements CommandExecutor {
	
	private Plugin plugin;
	private PlotAPI plotAPI;
	
	public CmdExecutor(Plugin plugin, PlotAPI plotAPI, DBConnection db) {
		this.plugin = plugin;
		this.plotAPI = plotAPI;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player)sender;
			
			if (cmd.getName().equalsIgnoreCase("menu")) {
				openMainMenu(p);
			}
			
		}
		return false;
	}
	
	private void openMainMenu(final Player p) {
		IconMenu menu = new IconMenu("Menu", 27, new IconMenu.OptionClickEventHandler() {
            @Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                event.setWillClose(false);
                if (event.getName() == "§aMes parcelles") {
                	openMesParcellesMenu(p);
                } else if (event.getName() == "§aNouvelle parcelle") {
                	openNouvelleParcelleMenu(p);
                } else if (event.getName() == "§aKits d'outils") {
                	openOutilsConception(p);
                }
            }
        }, plugin);
        menu.setOption(10, new ItemStack(Material.GRASS_BLOCK, 1), "§aMes parcelles");
        menu.setOption(11, new ItemStack(Material.PODZOL), "§aNouvelle parcelle");
        menu.setOption(12, new ItemStack(Material.ANVIL), "§aKits d'outils");
        menu.setOption(13, new ItemStack(Material.ENCHANTED_BOOK), "§aAide");
        menu.open(p);
	}
	
	private void openMesParcellesMenu(final Player p) {
		// Player plots as list
		@SuppressWarnings({"unchecked", "rawtypes"})
		final List<Plot> plotsList= new ArrayList(this.plotAPI.getPlayerPlots(PlotPlayer.wrap(p)));
		// Menu creation
		IconMenu menu = new IconMenu("Menu > Mes Parcelles", 27, new IconMenu.OptionClickEventHandler() {
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                event.setWillClose(false);
                if (event.getName() == "§cRetour") {
                	openMainMenu(p);
                	return;
                }
                ItemStack item = event.getEvent().getCurrentItem(); // Clicked item
                if (item.getType() == Material.GRASS_BLOCK) {
                	if (item.getAmount() <= plotsList.size()) {
                		openMaParcelleMenu(p, plotsList.get(item.getAmount()-1), item.getAmount()-1);
                	} else {
                    	p.sendMessage("§cUne erreur est survenue, contactez un administrateur.");
                    	Bukkit.getLogger().warning("PlotManager > CMDExecutor.java > openMesParcellesMenu() > erreur d'index : locations[" + (item.getAmount()-1) + "] sur " + plotsList.size() );
                	}
                	return;
                }
            }
        }, plugin);
		// Back button
		menu.setOption(0, new ItemStack(Material.REDSTONE_BLOCK), "§cRetour");
        // Plot items Iteration
		int counter = 1;
        for (Plot plot: plotsList) {
        	menu.setOption(counter, new ItemStack(Material.GRASS_BLOCK, counter), "§aPlot n°"+ String.valueOf(counter));
        	counter++;
        }
        menu.open(p);
	}
	
	private void openMaParcelleMenu(final Player p, final Plot plot, int nParcelle) {
		IconMenu menu = new IconMenu("Menu > Mes Parcelles > " + (nParcelle+1), 27, new IconMenu.OptionClickEventHandler() {
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                if (event.getName() == "§aTéléportation") {
                	event.setWillClose(true);
                	Location loc = new Location(
    					Bukkit.getServer().getWorld(plot.getWorldName()), 
    					plot.getHome().getX(), 
    					plot.getHome().getY(), 
    					plot.getHome().getZ(), 
    					plot.getHome().getYaw(), 
    					plot.getHome().getPitch()
    				);
                	p.teleport(loc);
                } else if (event.getName() == "§cRetour") {
                	event.setWillClose(false);
                	openMesParcellesMenu(p);
                } else {
                	event.setWillClose(false);
                }
            }
        }, plugin);
		menu.setOption(0, new ItemStack(Material.REDSTONE_BLOCK), "§cRetour");
        menu.setOption(1, new ItemStack(Material.END_PORTAL_FRAME), "§aTéléportation");
        menu.setOption(2, new ItemStack(Material.REDSTONE_TORCH), "§aInformations", 
        		"Id : §e"+plot.getId(),
        		"Biome : §e"+plot.getBiome(), 
        		"Dist. du spawn : §e"+plot.getDistanceFromOrigin()
        );
        menu.open(p);
	}
	
	private void openNouvelleParcelleMenu(final Player p) {
		IconMenu menu = new IconMenu("Menu > Nouvelle Parcelle", 27, new IconMenu.OptionClickEventHandler() {
            public void onOptionClick(IconMenu.OptionClickEvent event) {
            	switch (event.getName()) {
				case "§cRetour":
					event.setWillClose(false);
					openMainMenu(p);
					break;
				case "§aParcelle au hasard":
					event.setWillClose(true);
					p.performCommand("p auto");
					break;
				case "§aChoisir cette parcelle":
					event.setWillClose(true);
					p.performCommand("p claim");
					break;
				}
            }
        }, plugin);
		menu.setOption(0, new ItemStack(Material.REDSTONE_BLOCK), "§cRetour");
        menu.setOption(11, new ItemStack(Material.NAME_TAG), "§aParcelle au hasard", "Vous attribue une parcelle au hasard", "sur la map.");
        menu.setOption(12, new ItemStack(Material.NAME_TAG), "§aChoisir cette parcelle", "Vous attribue la parcelle sur laquelle", "vous vous situez actuellement.");
        menu.open(p);
	}

	private void openOutilsConception(final Player p) {
		IconMenu menu = new IconMenu("Menu > Outils de conception", 27, new IconMenu.OptionClickEventHandler() {
            @Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {
            	switch (event.getName()) {
				case "§cRetour":
					event.setWillClose(false);
					openMainMenu(p);
					break;
            	}
            }
        }, plugin);
		menu.setOption(0, new ItemStack(Material.REDSTONE_BLOCK), "§cRetour");
        menu.setOption(10, new ItemStack(Material.WHITE_STAINED_GLASS), "§f(Aucun)");
        menu.setOption(12, new ItemStack(Material.GREEN_STAINED_GLASS), "§aTerraforming", "§eKit d'outils pour modéliser votre parcelle.");
        menu.setOption(13, new ItemStack(Material.RED_STAINED_GLASS), "§aZones", "§eDélimitez des zones de pvp et autres.");
        menu.setOption(14, new ItemStack(Material.YELLOW_STAINED_GLASS), "§aAnimation", "§eGérez les joueurs présents", "§esur votre plot.");
        menu.setOption(15, new ItemStack(Material.LIME_STAINED_GLASS), "§aCréation d'évent", "§ePoint de spawn, blocs de", "§etéléportation, de dégats,", "§espawners etc.");
        menu.setOption(16, new ItemStack(Material.BLUE_STAINED_GLASS), "§aCréation de jump/parcours", "§eDéfinir un point de départ,", "§ed'arrivée, de checkpoints");
        menu.open(p);
	}
	
}
