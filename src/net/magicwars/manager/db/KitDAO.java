package net.magicwars.manager.db;

import java.time.Duration;
import java.time.Instant;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import net.magicwars.manager.kits.Serializer;

public class KitDAO extends BasicDAO<KitDTO, String>  {
	
	public Plugin plugin;

	public KitDAO(Class<KitDTO> entityClass, Datastore ds, Plugin plugin) {
		super(entityClass, ds);
		this.plugin = plugin;
	}
	
	public KitDTO updateKitSync(PlayerInventory inv, String name, Integer level, String desc) {
		final KitDAO that = this;
		KitDTO kd = this.findOne("name", name);
		if (kd == null)
			kd = new KitDTO();
		kd.setName(name);
		kd.setDescription(desc);
		kd.setLevel(level);
		kd.setInventory(Serializer.serializeInventory(inv));
		this.save(kd);
		return kd;
	}
	
	public Boolean equipPlayerSync(String name, Player p) {
		KitDTO kd = this.findOne("name", name);
		if (kd != null) {
			Serializer.equipPlayer(kd.getInventory(), p);
			return true;
		}
		Bukkit.getLogger().warning("Le kit "+name+" n'éxiste pas.");
		return false;
	}
	
	public void equipPlayerAsync(String name, Player p, final AsyncCallback<Boolean> callback) {
		final Instant begin = Instant.now();
		final KitDAO that = this;
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			public void run() {
				Boolean out = equipPlayerSync(name, p);
				callback.callback(out, Duration.between(begin, Instant.now()));
			}
		});
	}
	
	public boolean deleteKitSync(String name) {
		KitDTO kd = this.findOne("name", name);
		if (kd !=null) {
			this.delete(kd);
			return true;
		}
		return false;
	}
	
}
