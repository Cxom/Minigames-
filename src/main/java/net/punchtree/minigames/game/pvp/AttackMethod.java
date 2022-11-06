package net.punchtree.minigames.game.pvp;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public enum AttackMethod{
	
	SLAY("🗡"),
	SHOOT("🏹"),
	PUNCH("ლ"),
	UNKNOWN("?");
	//ツ 

	private final String icon;
	
	private AttackMethod(String icon){
		this.icon = icon;
	}
	
	public String getIcon(){
		return icon;
	}
	
	public static AttackMethod getAttackMethod(Entity damager){
		if (damager instanceof Arrow)
			return AttackMethod.SHOOT;
		else if (damager instanceof Player player)
			if (player.getInventory().getItemInMainHand().getType() == Material.STONE_SWORD)
				return AttackMethod.SLAY;
			else if (player.getInventory().getItemInMainHand().getType() == Material.AIR || player.getInventory().getItemInMainHand().getType() == Material.BOW)
				return AttackMethod.PUNCH;
		return AttackMethod.UNKNOWN;
	}
	
}
