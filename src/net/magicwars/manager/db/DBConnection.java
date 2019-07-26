package com.freebuildserver.plotManager.db;

import org.bukkit.plugin.Plugin;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

public class DBConnection {

	private final String ip = "127.0.0.1";
	private final int port = 27017;

	private Morphia morphia;
	private MongoClient mc;
    private Datastore datastore;
    private PlayerDAO playerDAO;
    
	private Plugin plugin;

	public DBConnection(Plugin plugin) {
		this.plugin = plugin;
		
		try {
			mc = new MongoClient(ip, port);
		} catch (Exception e) {
			// When you end up here, the server the db is running on could not be found!
			System.out.println("Could not connect to database!");
			e.printStackTrace();
			return;
		}
		
		morphia = new Morphia();
		
		morphia.map(PlayerDTO.class);

        datastore = morphia.createDatastore(mc, "minecraft");
        datastore.ensureIndexes();

        playerDAO = new PlayerDAO(PlayerDTO.class, datastore, plugin);
		
//		// Get the database called "mcserver"
//		// If it does not exist it will be created automatically
//		// once you save something in it
//		minecraftDB = client.getDB("minecraft");
//		// Get the collection called "players" in the database "minecraft"
//		// Equivalent to the table in MySQL, you can store objects in here
//		playersDB = new PlayersDB(minecraftDB.getCollection("players"));
	}

	public PlayerDAO getPlayerDAO() {
		return playerDAO;
	}

	
	
}
