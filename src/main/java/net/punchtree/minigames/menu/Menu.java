package net.punchtree.minigames.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public interface Menu {

	void onInventoryClick(InventoryClickEvent e);
	void onInventoryDrag(InventoryDragEvent e);
	
}
