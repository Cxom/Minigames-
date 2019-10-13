package net.punchtree.minigames.arena.creation;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.punchtree.minigames.arena.Arena;

public class ArenaManager<ArenaType extends Arena> {
	
	/**
	 * The folder containing the Arena YAML files
	 */
	private final File arenaFolder;
	private Function<YamlConfiguration, ArenaType> arenaLoader;
	
	/**
	 * A map of the Arena names -> their YAML Configurations
	 */
	private Map<String, YamlConfiguration> arenaConfigs = new HashMap<>();
	
	/**
	 * A map of the Arena names -> their loaded Objects
	 */
	private Map<String, ArenaType> arenas = new HashMap<>();
	
	public ArenaManager(File arenaFolder, Function<YamlConfiguration, ArenaType> arenaLoader) {
		this.arenaFolder = arenaFolder;
		this.arenaLoader = arenaLoader;
	}
	
	// ---------- GETTERS ----------
	public Collection<ArenaType> getArenas(){
		return arenas.values();
	}
	
	public boolean isArena(String arenaName){
		return arenas.containsKey(arenaName) || loadArenaConfig(arenaName) != null;
	}
	
	public ArenaType getArena(String arenaName){
		if (!isArena(arenaName)) return null;
		return arenas.get(arenaName);
	}
	
	public YamlConfiguration getArenaConfig(String arenaName){
		if (!isArena(arenaName)) return null;
		return arenaConfigs.get(arenaName);
	}
	// --------------------------------
	
	public void modifyArena(String arenaName, String path, Object value){
		if(!isArena(arenaName)) return;
		YamlConfiguration arena = getArenaConfig(arenaName);
		arena.set(path, value);
		arenaConfigs.put(arenaName, arena);
		ArenaType loaded = arenaLoader.apply(arena);
		if (loaded != null){
			arenas.put(arenaName, loaded);
		}
	}
	
	public void loadArenas(){
		if(!arenaFolder.exists()){
			arenaFolder.mkdirs();
		}
		
		if(arenaFolder.listFiles() != null){
			for (File arenaf : Arrays.asList(arenaFolder.listFiles())) {
				YamlConfiguration arena = new YamlConfiguration();
				try {
					arena.load(arenaf);
					
					String arenaName = arenaf.getName().substring(0, arenaf.getName().length() - 4);
					arenaConfigs.put(arenaName, arena);
					ArenaType loaded = arenaLoader.apply(arena);
					if (loaded != null){
						arenas.put(arenaName, loaded);
					}
				} catch (IOException | InvalidConfigurationException e) {
					Bukkit.getLogger().warning("Could not load " + arenaf.getName() + "!");
					e.printStackTrace();
				}
			}
		}
	}
	
	// This loads both the configuration and the arena - is that what we want???
	public FileConfiguration loadArenaConfig(String arenaName){
		File arenaf = new File(arenaFolder.getAbsolutePath() + File.separator + arenaName);
		if(!arenaf.exists()){
			return null;
		} else {
			YamlConfiguration arena = new YamlConfiguration();
			try {
				arena.load(arenaf);
				arenaConfigs.put(arenaName, arena);
				ArenaType loaded = arenaLoader.apply(arena);
				if (loaded != null){
					arenas.put(arenaf.getName(), loaded);
				}
				return arena;
			} catch (IOException | InvalidConfigurationException e) {
				Bukkit.getLogger().warning("Could not load " + arenaf.getName() + "!");
				e.printStackTrace();
				return null;
			}
		}
		
	}
	
	public void save(String arenaName){
		if (!isArena(arenaName)) throw new IllegalArgumentException();
		File arenaf = new File(arenaFolder + File.separator + arenaName + ".yml");
		try {
			getArenaConfig(arenaName).save(arenaf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveAll(){
		for(Map.Entry<String, YamlConfiguration> arenaEntry : arenaConfigs.entrySet()){
			File arenaf = new File(arenaFolder.getAbsolutePath() + File.separator + arenaEntry.getKey() + ".yml");
			try {
				arenaEntry.getValue().save(arenaf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
