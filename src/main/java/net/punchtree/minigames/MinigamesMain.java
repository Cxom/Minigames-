package net.punchtree.minigames;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MinigamesMain extends JavaPlugin {

	private static Plugin plugin;
	public static Plugin getPlugin() { return plugin; }
	
	@Override
	public void onEnable() {
		plugin = this;
	}
	
}
