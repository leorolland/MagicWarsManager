package net.magicwars.manager.db;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;

@Entity(value="arenas", noClassnameStored = true)
public class ArenaDTO {
	
	@Id
    @Property("id")
    protected ObjectId id;
	
	@Indexed(options = @IndexOptions(unique = true))
	private String name;
	
	private String description;
	
	private boolean enabled;
	
	private List<Map<String, String>> neutralSpawns;
	
	private List<Map<String, String>> redTeamSpawns;
	
	private List<Map<String, String>> blueTeamSpawns;
	
	private Map<String, String> blueTeamRespawn;
	private Map<String, String> redTeamRespawn;
	private Map<String, String> neutralRespawn;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Map<String, String>> getNeutralSpawns() {
		return neutralSpawns;
	}

	public void setNeutralSpawns(List<Map<String, String>> neutralSpawns) {
		this.neutralSpawns = neutralSpawns;
	}

	public List<Map<String, String>> getRedTeamSpawns() {
		return redTeamSpawns;
	}

	public void setRedTeamSpawns(List<Map<String, String>> redTeamSpawns) {
		this.redTeamSpawns = redTeamSpawns;
	}

	public List<Map<String, String>> getBlueTeamSpawns() {
		return blueTeamSpawns;
	}

	public void setBlueTeamSpawns(List<Map<String, String>> blueTeamSpawns) {
		this.blueTeamSpawns = blueTeamSpawns;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Map<String, String> getBlueTeamRespawn() {
		return blueTeamRespawn;
	}

	public void setBlueTeamRespawn(Map<String, String> blueTeamRespawn) {
		this.blueTeamRespawn = blueTeamRespawn;
	}

	public Map<String, String> getRedTeamRespawn() {
		return redTeamRespawn;
	}

	public void setRedTeamRespawn(Map<String, String> redTeamRespawn) {
		this.redTeamRespawn = redTeamRespawn;
	}

	public Map<String, String> getNeutralRespawn() {
		return neutralRespawn;
	}

	public void setNeutralRespawn(Map<String, String> neutralRespawn) {
		this.neutralRespawn = neutralRespawn;
	}
	
	

}
