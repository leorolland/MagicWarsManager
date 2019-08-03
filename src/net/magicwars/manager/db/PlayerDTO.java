package net.magicwars.manager.db;

import java.util.Date;
import java.util.Map;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.IndexOptions;

@Entity(value="players", noClassnameStored = true)
public class PlayerDTO {
	
	@Id
    @Property("id")
    protected ObjectId id;
	
	@Indexed(options = @IndexOptions(unique = true))
	private String uuid;
	
	@Indexed
	private String username;
	
	private String ip;

	private long reputation;
	
	private long xp;
	
	private Date lastLogin;
	
	private Map<String, String> location;
	
	private Map<String, Long> xpClasses;
	
	// GETTERS AND SETTERS
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public long getReputation() {
		return reputation;
	}

	public void setReputation(long reputation) {
		this.reputation = reputation;
	}

	public long getXp() {
		return xp;
	}

	public void setXp(long xp) {
		this.xp = xp;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Map<String, String> getLocation() {
		return location;
	}

	public void setLocation(Map<String, String> location) {
		this.location = location;
	}

	public Map<String, Long> getXpClasses() {
		return xpClasses;
	}

	public void setXpClasses(Map<String, Long> xpClasses) {
		this.xpClasses = xpClasses;
	}
	
}
