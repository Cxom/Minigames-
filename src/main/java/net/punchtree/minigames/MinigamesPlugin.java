package net.punchtree.minigames;

import org.bukkit.plugin.java.JavaPlugin;

public class MinigamesPlugin extends JavaPlugin {

	private static MinigamesPlugin plugin;
	public static MinigamesPlugin getInstance() { 
		return plugin; 
	}
	
	public String getDataFolderPath() {
		return getDataFolder().getAbsolutePath();
	}
	
	@Override
	public void onEnable() {
		plugin = this;
	}
	
}
