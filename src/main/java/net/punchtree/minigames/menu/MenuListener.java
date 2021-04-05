package net.punchtree.minigames.menu;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

/**
 * The point of this class is to not leak menu objects
 * via having registered event handlers
 * @author Cxom
 */
public class MenuListener implements Listener {

	private MenuListener() {}
	private static final MenuListener instance = new MenuListener();
	public static MenuListener getInstance() {
		return instance;
	}
	
	Set<Menu> weakMenuReferences = Collections.newSetFromMap(new WeakHashMap<>());
	
	public static void registerMenu(Menu menu) {
		instance.weakMenuReferences.add(menu);
	}
	
	public static void deregister(Menu menu) {
		instance.weakMenuReferences.remove(menu);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		weakMenuReferences.forEach(menu -> menu.onInventoryClick(e));
	}
	
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		weakMenuReferences.forEach(menu -> menu.onInventoryDrag(e));
	}
	
	
}
