package net.punchtree.minigames;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.punchtree.minigames.menu.MenuListener;

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
		
		registerEvents();
	}
	
	private void registerEvents() {
		Bukkit.getPluginManager().registerEvents(MenuListener.getInstance(), this);
	}
	
}
