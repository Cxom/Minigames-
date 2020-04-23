package net.punchtree.minigames.utility.player;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class PlayerUtils {

	public static void perfectStats(Player player){
		player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		player.setFoodLevel(20);
		player.setSaturation(20);
		player.setExhaustion(0);
		player.setFireTicks(0);
	}
	
	public static void giveHeartRows(Player player, int heartRows) {
		player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(heartRows * 20);
		player.setHealth(heartRows * 20);
	}
	
	public static void resetHeartRows(Player player) {
		AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		maxHealthAttribute.setBaseValue(maxHealthAttribute.getDefaultValue());
		player.setHealth(maxHealthAttribute.getDefaultValue());
	}
	
}
