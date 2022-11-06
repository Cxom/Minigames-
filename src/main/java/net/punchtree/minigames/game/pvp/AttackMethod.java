package net.punchtree.minigames.game.pvp;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Set;

public enum AttackMethod{
	
	SLAY("🗡"),
	SHOOT("🏹"),
	PUNCH("ლ"),
	UNKNOWN("☠");
	//ツ 

	private final String icon;
	
	private AttackMethod(String icon){
		this.icon = icon;
	}
	
	public String getIcon(){
		return icon;
	}

	private static final Set<Material> ITEMS_TO_COUNT_AS_PUNCHING = Set.of(
			Material.AIR,
			Material.BOW,
			Material.WHITE_CONCRETE,
			Material.ORANGE_CONCRETE,
			Material.MAGENTA_CONCRETE,
			Material.LIGHT_BLUE_CONCRETE,
			Material.YELLOW_CONCRETE,
			Material.LIME_CONCRETE,
			Material.PINK_CONCRETE,
			Material.GRAY_CONCRETE,
			Material.LIGHT_GRAY_CONCRETE,
			Material.CYAN_CONCRETE,
			Material.PURPLE_CONCRETE,
			Material.BLUE_CONCRETE,
			Material.BROWN_CONCRETE,
			Material.GREEN_CONCRETE,
			Material.RED_CONCRETE,
			Material.BLACK_CONCRETE);


	public static AttackMethod getAttackMethod(Entity damager){
		if (damager instanceof Arrow)
			return AttackMethod.SHOOT;
		else if (damager instanceof Player player)
			if (player.getInventory().getItemInMainHand().getType() == Material.STONE_SWORD)
				return AttackMethod.SLAY;
			else if (ITEMS_TO_COUNT_AS_PUNCHING.contains(player.getInventory().getItemInMainHand().getType()))
				return AttackMethod.PUNCH;
		return AttackMethod.UNKNOWN;
	}
	
}
