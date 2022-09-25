package net.punchtree.minigames.utility.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.punchtree.minigames.MinigamesPlugin;

public class PlayerProfile {

	private static Map<UUID, PlayerProfile> saved = new HashMap<>();
		
	public static void save(Player player){
		saved.put(player.getUniqueId(), new PlayerProfile(player, false, null));
	}
	
	/** 
	 * This teleports them to a specified location before saving their inventory
	 * **/
	public static void saveTeleportFirst(Player player, Location teleportLocation) {
		saved.put(player.getUniqueId(), new PlayerProfile(player, true, teleportLocation));
	}
	
	public static boolean isSaved(Player player){
		return saved.containsKey(player.getUniqueId());
	}
	
	public static void restore(Player player){ restore(player.getUniqueId()); }
	public static void restore(UUID uuid){
		if (saved.containsKey(uuid)){
			saved.get(uuid).restore();
			saved.remove(uuid);
		}
	}
	
	public static void restoreAll(){
		for (PlayerProfile pp : saved.values()){
			pp.restore();
		}
		saved.clear();
	}
	
	private final UUID uuid;
	private final ItemStack[] inventory;
	private final ItemStack[] armor;
	private final Location location;
	private final GameMode gamemode;
	private final boolean canFly;
	private final boolean flying;
	private final boolean glowing;
	private final boolean invulnerable;
	private final int xpLvl;
	private final float xp;
	private final double health;
	private final int hunger;
	private final float saturation;
	private final float exhaustion;
	
	private boolean teleportFirst;
	
	private PlayerProfile(Player player,  boolean teleportFirst, Location teleportLocation){
		this.uuid = player.getUniqueId();
		
		this.gamemode = player.getGameMode();
		this.canFly = player.getAllowFlight();
		this.flying = player.isFlying();
		this.glowing = player.isGlowing();
		this.invulnerable = player.isInvulnerable();
		this.xpLvl = player.getLevel();
		this.xp = player.getExp();
		this.health = player.getHealth();
		this.hunger = player.getFoodLevel();
		this.saturation = player.getSaturation();
		this.exhaustion = player.getExhaustion();
		
		// SAVE the location first
		this.location = player.getLocation();
		
		// save the request so we know which order to restore inventory & location in
		this.teleportFirst = teleportFirst;
		if (teleportFirst) {
			// teleport if asked, to save a different world inventory
			player.teleport(teleportLocation);
		}
		
		// backup inventory last
		this.inventory = player.getInventory().getContents();
		this.armor = player.getInventory().getArmorContents();
		InventoryUtils.backupInventory(player);
	}
	
	private void restore(){
		Player player = Bukkit.getPlayer(uuid);
		
		//Teleport must happen before stats to prevent world settings overriding stored player
		// but we restore location in the reverse order as it was saved
		if (teleportFirst) {
			player.getInventory().setContents(inventory);
			player.getInventory().setArmorContents(armor);
			player.teleport(location); 
		} else {
			player.teleport(location); 
			player.getInventory().setContents(inventory);
			player.getInventory().setArmorContents(armor);
		}
		
		player.setGlowing(glowing);
		player.setInvulnerable(invulnerable);
		player.setLevel(xpLvl);
		player.setExp(xp);
		player.setHealth(health);
		player.setFoodLevel(hunger);
		player.setSaturation(saturation);
		player.setExhaustion(exhaustion);
		
		new BukkitRunnable() {
			public void run() {
				player.setGameMode(gamemode);
				player.setAllowFlight(canFly);
				player.setFlying(flying);
				player.setGlowing(glowing);
		        player.setInvulnerable(invulnerable);
		        player.setLevel(xpLvl);
		        player.setExp(xp);
		        player.setHealth(health);
		        player.setFoodLevel(hunger);
		        player.setSaturation(saturation);
		        player.setExhaustion(exhaustion);
			}
		}.runTaskLater(MinigamesPlugin.getInstance(), 3);
		
	}
	
}
