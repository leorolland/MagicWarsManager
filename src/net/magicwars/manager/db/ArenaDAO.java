package net.magicwars.manager.db;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import net.magicwars.manager.matchmaking.Lobby;

public class ArenaDAO extends BasicDAO<ArenaDTO, String> {

	Plugin plugin;

	public ArenaDAO(Class<ArenaDTO> entityClass, Datastore ds, Plugin plugin) {
		super(entityClass, ds);
		this.plugin = plugin;
	}
	
	public static Map<String, String> locationToStringMap(Location l, String name) {
		Map<String, String> output = new HashMap<String, String>();
		output.put("name", name);
		output.put("world", l.getWorld().getName());
		output.put("x", String.valueOf(l.getBlockX()));
		output.put("y", String.valueOf(l.getBlockY()));
		output.put("z", String.valueOf(l.getBlockZ()));
		output.put("yaw", String.valueOf(l.getYaw()));
		output.put("pitch", String.valueOf(l.getPitch()));
		return output;
	}

	public ArenaDTO createArenaIfNotExists(String name) {
		ArenaDTO ap = this.findOne("name", name);
		if (ap == null) {			
			ap = new ArenaDTO();
			List<Map<String,String>> emptyListMap = new ArrayList<Map<String,String>>();
			ap.setBlueTeamSpawns( emptyListMap.stream().collect(Collectors.toList()) ); // Separate instances of emptyListMap
			ap.setNeutralSpawns( emptyListMap.stream().collect(Collectors.toList()) );
			ap.setRedTeamSpawns( emptyListMap.stream().collect(Collectors.toList()) );
			ap.setBlueTeamRespawn( new HashMap<String,String>() );
			ap.setRedTeamRespawn( new HashMap<String,String>() );
			ap.setNeutralRespawn( new HashMap<String,String>() );
			ap.setEnabled(false);
			ap.setDescription("");
			ap.setName(name);
			this.save(ap);
		}		
		return ap;
	}
	
	public ArenaDTO setArenaRedSpawn(String arenaName, String name, Player p) {
		ArenaDTO arena = createArenaIfNotExists(arenaName);
		List<Map<String, String>> spawns = arena.getRedTeamSpawns();
		// Delete old spawns with same name
		spawns.removeIf( (Map<String, String> spawn) -> {
			return spawn.get("name").equals(name);
		});
		// Create new one
		spawns.add(locationToStringMap(p.getLocation(), name));
		arena.setRedTeamSpawns(spawns);
		this.save(arena);
		return arena;
	}
	
	public ArenaDTO deleteArenaRedSpawn(String arenaName, String name) {
		ArenaDTO arena = createArenaIfNotExists(arenaName);
		List<Map<String, String>> spawns = arena.getRedTeamSpawns();
		// Delete old spawns with same name
		spawns.removeIf( (Map<String, String> spawn) -> {
			return spawn.get("name").equals(name);
		});
		arena.setRedTeamSpawns(spawns);
		this.save(arena);
		return arena;
	}
	
	public ArenaDTO setArenaBlueSpawn(String arenaName, String name, Player p) {
		ArenaDTO arena = createArenaIfNotExists(arenaName);
		List<Map<String, String>> spawns = arena.getBlueTeamSpawns();
		// Delete old spawns with same name
		spawns.removeIf( (Map<String, String> spawn) -> {
			return spawn.get("name").equals(name);
		});
		// Create new one
		spawns.add(locationToStringMap(p.getLocation(), name));
		arena.setBlueTeamSpawns(spawns);
		this.save(arena);
		return arena;
	}
	
	public ArenaDTO deleteArenaBlueSpawn(String arenaName, String name) {
		ArenaDTO arena = createArenaIfNotExists(arenaName);
		List<Map<String, String>> spawns = arena.getBlueTeamSpawns();
		// Delete old spawns with same name
		spawns.removeIf( (Map<String, String> spawn) -> {
			return spawn.get("name").equals(name);
		});
		arena.setBlueTeamSpawns(spawns);
		this.save(arena);
		return arena;
	}

	public ArenaDTO setArenaNeutralSpawn(String arenaName, String name, Player p) {
		ArenaDTO arena = createArenaIfNotExists(arenaName);
		List<Map<String, String>> spawns = arena.getNeutralSpawns();
		// Delete old spawns with same name
		spawns.removeIf( (Map<String, String> spawn) -> {
			return spawn.get("name").equals(name);
		});
		// Create new one
		spawns.add(locationToStringMap(p.getLocation(), name));
		arena.setNeutralSpawns(spawns);
		this.save(arena);
		return arena;
	}
	
	public ArenaDTO deleteArenaNeutralSpawn(String arenaName, String name) {
		ArenaDTO arena = createArenaIfNotExists(arenaName);
		List<Map<String, String>> spawns = arena.getNeutralSpawns();
		// Delete old spawns with same name
		spawns.removeIf( (Map<String, String> spawn) -> {
			return spawn.get("name").equals(name);
		});
		arena.setNeutralSpawns(spawns);
		this.save(arena);
		return arena;
	}
	
	public ArenaDTO setArenaNeutralRespawn(String arenaName, Player p) {
		ArenaDTO arena = createArenaIfNotExists(arenaName);
		arena.setNeutralRespawn(locationToStringMap(p.getLocation(), "Respawn neutre"));
		this.save(arena);
		return arena;
	}
	public ArenaDTO setArenaBlueRespawn(String arenaName, Player p) {
		ArenaDTO arena = createArenaIfNotExists(arenaName);
		arena.setBlueTeamRespawn(locationToStringMap(p.getLocation(), "Respawn bleu"));
		this.save(arena);
		return arena;
	}
	public ArenaDTO setArenaRedRespawn(String arenaName, Player p) {
		ArenaDTO arena = createArenaIfNotExists(arenaName);
		arena.setRedTeamRespawn(locationToStringMap(p.getLocation(), "Respawn rouge"));
		this.save(arena);
		return arena;
	}

	public boolean deleteArena(String arenaName) {
		ArenaDTO ap = this.findOne("name", arenaName);
		if (ap != null) {
			this.delete(ap);
			return true;
		}
		return false;
	}
	
	public boolean setEnabled(String arenaName, boolean enabled) {
		ArenaDTO ap = this.findOne("name", arenaName);
		if (ap!=null) {
			ap.setEnabled(enabled);
			this.save(ap);
			return true;
		}
		return false;
	}
	
	public static Location dataToLocation(Map<String, String> data) {
		World world = Bukkit.getWorld(data.get("world"));
		float yaw = Float.valueOf(data.get("yaw"));
		float pitch = Float.valueOf(data.get("pitch"));
		return new Location(world, Double.valueOf(data.get("x")), Double.valueOf(data.get("y")), Double.valueOf(data.get("z")), yaw, pitch);
	}
	
}
