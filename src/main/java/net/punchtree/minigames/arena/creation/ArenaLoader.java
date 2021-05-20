package net.punchtree.minigames.arena.creation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import net.punchtree.minigames.region.Region;
import net.punchtree.util.color.PunchTreeColor;

public class ArenaLoader {
	
	/*Static loading parsers*/
	
	public static World getWorld(ConfigurationSection section){
		return Bukkit.getWorld(section.getString("world"));
	}
	
	public static World getRootWorld(ConfigurationSection section){
		return getWorld(section.getRoot());
	}
	
	public static String getName(File arenaFile){
		String name = arenaFile.getName();
        return name.substring(0, name.length() - 4);
	}
	
	public static PunchTreeColor getColor(ConfigurationSection section){
		Integer red = section.getInt("red", 255);
		Integer green = section.getInt("green", 255);
		Integer blue = section.getInt("blue", 255);
		String chatcolor = section.getString("chatcolor"); 	
		if (red == null || green == null || blue == null){
			return null;
		}
		if (chatcolor == null){
			return new PunchTreeColor(red, green, blue);
		} else {
			return new PunchTreeColor(red, green, blue, ChatColor.valueOf(chatcolor));
		}
	}
	
	public static <T> List<T> getList(ConfigurationSection section, Function<ConfigurationSection, T> loader) throws ClassCastException{
		List<T> list = new ArrayList<>();
		for(String key : section.getKeys(false)){
			try {
				list.add(loader.apply(section.getConfigurationSection(key)));
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static Location[] getLocationList(ConfigurationSection section) {
		Location[] list = new Location[section.getKeys(false).size()];
		int i = 0;
		for(String key : section.getKeys(false))
			list[i++] = (getLocation(section.getConfigurationSection(key)));
		return list;
	}
	
	public static Location getLocation(ConfigurationSection spawnInfo){
		return getLocation(spawnInfo, getRootWorld(spawnInfo));
	}
	
	public static Location getLocationWithWorld(ConfigurationSection section){
		return getLocation(section, getWorld(section));
	}
	
	public static Location getLocation(ConfigurationSection spawnInfo, World world){
		return new Location(
			world,
			spawnInfo.getDouble("x"),
			spawnInfo.getDouble("y"),
			spawnInfo.getDouble("z"),
			(float) spawnInfo.getDouble("pitch", 0),
			(float) spawnInfo.getDouble("yaw", 0) //TODO Is 0 pitch, 0 yaw looking straight forward?
		);
	}
	
	public static Region getRegion(ConfigurationSection regionInfo){
		return new Region(
			getLocation(regionInfo.getConfigurationSection("min")),
			getLocation(regionInfo.getConfigurationSection("max"))
		);
	}
	
	public static List<Region> getRegionList(ConfigurationSection section){
		return ArenaLoader.<Region>getList(section, ArenaLoader::getRegion);
	}
	
}
