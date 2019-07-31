package net.magicwars.manager.db;

import java.util.Map;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;

@Entity(value="mkits", noClassnameStored = true)
public class KitDTO {
	
	@Id
    private int id;
	
	@Indexed(options = @IndexOptions(unique = true))
	private String name;
	
	private String description;
	
	private Integer level;
	
	private Map<Integer, Map<String, String>> inventory;
	
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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Map<Integer, Map<String, String>> getInventory() {
		return inventory;
	}

	public void setInventory(Map<Integer, Map<String, String>> inventory) {
		this.inventory = inventory;
	}
	
}
