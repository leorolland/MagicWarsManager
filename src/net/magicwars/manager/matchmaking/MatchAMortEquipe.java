package net.magicwars.manager.matchmaking;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.magicwars.manager.MagicWarsManager;
import net.magicwars.manager.db.ArenaDAO;
import net.magicwars.manager.db.ArenaDTO;
import net.magicwars.manager.db.AsyncCallback;
import net.magicwars.manager.db.DBConnection;
import net.md_5.bungee.api.ChatColor;

public class MatchAMortEquipe extends Match {

	private Set<Player> blueTeam = new HashSet<Player>();

	private Set<Player> redTeam = new HashSet<Player>();

	private Location respawnRouge;

	private Location respawnBleu;

	private final Random rand = new Random();

	public static int DURATION = 35;

	// private Map<Player, PlayerScore> scores;

	public MatchAMortEquipe(MagicWarsManager plugin, ArenaDTO arena, Set<Player> players, AsyncCallback<Void> endCallback) {
		super(DURATION, plugin, endCallback, arena, players);
		// Création de deux équipes
		players.forEach(p -> addedPlayer(p));
		this.respawnRouge = ArenaDAO.dataToLocation(arena.getRedTeamRespawn());
		this.respawnBleu = ArenaDAO.dataToLocation(arena.getBlueTeamRespawn());
		Bukkit.broadcastMessage("Equipe Bleue :");
		Bukkit.broadcastMessage(ChatColor.BLUE
				+ String.join(", ", blueTeam.stream().map(p -> p.getName()).collect(Collectors.toList())));
		Bukkit.broadcastMessage("Equipe Rouge :");
		Bukkit.broadcastMessage(
				ChatColor.RED + String.join(", ", redTeam.stream().map(p -> p.getName()).collect(Collectors.toList())));
		start();
	}

	private void start() {
		players.forEach(p -> {
			respawnPlayer(p);
		});
	}

	public Set<Player> getBlueTeam() {
		return blueTeam;
	}

	public Set<Player> getRedTeam() {
		return redTeam;
	}

	@Override
	public void respawnPlayer(Player p) {
		if (redTeam.contains(p)) {
			p.teleport(respawnRouge);
		} else {
			p.teleport(respawnBleu);
		}
	}

	@Override
	public void addedPlayer(Player p) {
		// Si les équipes sont réparties équitablement
		if (redTeam.size() == blueTeam.size()) {
			int binaryValue = rand.nextInt(2);
			if (binaryValue == 0) {
				blueTeam.add(p);
				p.sendMessage(ChatColor.BLUE + "Vous avez rejoint l'équipe Bleue. (tirage au sort)");
			} else {
				redTeam.add(p);
				p.sendMessage(ChatColor.RED + "Vous avez rejoint l'équipe Rouge. (tirage au sort)");
			}
		} else {
			if (redTeam.size() < blueTeam.size()) {
				redTeam.add(p);
				p.sendMessage(ChatColor.RED + "Vous avez rejoint l'équipe Rouge. (équilibrage)");
			} else {
				blueTeam.add(p);
				p.sendMessage(ChatColor.BLUE + "Vous avez rejoint l'équipe Bleue. (équilibrage)");
			}
		}
		// Sinon

	}

	@Override
	public void removedPlayer(Player p) {
		redTeam.remove(p);
		blueTeam.remove(p);
	}

}
