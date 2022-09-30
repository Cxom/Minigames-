package net.punchtree.minigames.lobby;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.punchtree.minigames.MinigamesPlugin;

public class MatchmakingLobbyControls implements Listener {
	
	private static final ItemStack VOTE_FOR_MAP_ITEM;
	private static final ItemStack LEAVE_LOBBY_ITEM;
	static {
		VOTE_FOR_MAP_ITEM = new ItemStack(Material.PAPER);
		ItemMeta im = VOTE_FOR_MAP_ITEM.getItemMeta();
		im.displayName(Component.text("Vote For A Map")
				.decoration(TextDecoration.ITALIC, false)
				.color(TextColor.color(222, 222, 222)));
		im.lore(Arrays.asList(Component.text("Vote on the next map to play!")));
		VOTE_FOR_MAP_ITEM.setItemMeta(im);
		
		LEAVE_LOBBY_ITEM = new ItemStack(Material.BARRIER);
		im = LEAVE_LOBBY_ITEM.getItemMeta();
		im.setDisplayName(ChatColor.RED + "Leave the lobby");
		LEAVE_LOBBY_ITEM.setItemMeta(im);
	}
	
	private final MatchmakingLobbyWithVoting lobby;
	
	public MatchmakingLobbyControls(MatchmakingLobbyWithVoting lobby) {
		this.lobby = lobby;
		// TODO This leaks memory (when reloading) - abstract out singleton listener
		Bukkit.getServer().getPluginManager().registerEvents(this, MinigamesPlugin.getInstance());
	}
	
	public void giveLobbyControls(Player player) {
		player.getInventory().clear();

		player.getInventory().setItem(0, VOTE_FOR_MAP_ITEM);
		player.getInventory().setItem(8, LEAVE_LOBBY_ITEM);
		player.getInventory().setHeldItemSlot(0);
	}
	
	@EventHandler
	public void onToggleReadiness(PlayerInteractEvent e){
		
		Player player = e.getPlayer();
		//TODO Fix being able to move ready block in inventory?
		if(! lobby.hasPlayer(player)) return;
		
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			if (LEAVE_LOBBY_ITEM.equals(e.getItem())) {
				e.setCancelled(true);
				new BukkitRunnable() {
					public void run() {
						lobby.removePlayer(player);
					}
				}.runTaskLater(MinigamesPlugin.getInstance(), 1);
			} else if (VOTE_FOR_MAP_ITEM.equals(e.getItem())) {
				// TODO VOTING
				player.sendMessage(ChatColor.RED + "TODO implement voting here");
			}
		}
	}
	
	//Cancels dropping the readiness indicator
	@EventHandler
	public void onDropItem(PlayerDropItemEvent e){
		if (lobby.hasPlayer(e.getPlayer())){
			e.setCancelled(true);
		}
	}
	
	//Cancel pulling blocks out of the inventory
	@EventHandler
	private void onInventoryDrag(InventoryDragEvent e){
		Player whoClicked = (Player) e.getWhoClicked();
		if(lobby.hasPlayer(whoClicked)){
			e.setCancelled(true);
		}
	}

	@EventHandler
	private void onInventoryClick(InventoryClickEvent e){
		Player whoClicked = (Player) e.getWhoClicked();
		if(lobby.hasPlayer(whoClicked)){
			e.setCancelled(true);
		}
	}

	@EventHandler
	private void onPlayerLeaveServer(PlayerQuitEvent e) {
		lobby.removePlayer(e.getPlayer());
	}

	@EventHandler
	private void onLeaveCommand(PlayerCommandPreprocessEvent e) {
		if (e.getMessage().toLowerCase().startsWith("/leave") && lobby.hasPlayer(e.getPlayer())) {
			lobby.removePlayer(e.getPlayer());
			e.setCancelled(true);
		}
	}
	
}
