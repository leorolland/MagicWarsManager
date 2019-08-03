package net.magicwars.manager.executors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.magicwars.manager.db.DBConnection;
import net.magicwars.manager.kits.Classe;
import net.magicwars.manager.kits.classes.MageblancClasse;
import net.magicwars.manager.kits.classes.SorceleurClasse;

public class MkitExecutor implements CommandExecutor {

	public MkitExecutor() {
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		
		if (args.length >= 3 && args[0].equals("give")) {
			Player p = Bukkit.getServer().getPlayer(args[1]);
			if (p!=null) {
				Classe c = new SorceleurClasse();
				switch (args[2]) {
					case "sorceleur":
						c = new SorceleurClasse();
						break;
					case "mage_blanc":
						c = new MageblancClasse();
				}
				if (args.length==4) 
					c.equipTo(p, Integer.valueOf(args[3]));
				else 
					c.equipTo(p);
			} else {
				sender.sendMessage("Ce joueur n'est pas connecté sur le serveur.");
			}
		}

		sender.sendMessage("/mkits give <player> <kit>");
		return true;
	}

}
