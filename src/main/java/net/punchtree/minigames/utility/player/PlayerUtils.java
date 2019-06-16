package net.punchtree.minigames.utility.player;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class PlayerUtils {

	public static void perfectStats(Player player){
		player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		player.setFoodLevel(20);
		player.setSaturation(20);
		player.setExhaustion(0);
		player.setFireTicks(0);
	}
	
}
