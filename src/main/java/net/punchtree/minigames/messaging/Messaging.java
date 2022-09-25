package net.punchtree.minigames.messaging;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Messaging {
	
	// TODO get this all language independent, super fluid.
	// Idea:
	//	- Messages become an enum - Types to Strings
	//  - Strings are used to load relevant strings from a file.
	//  - One hashmap per language
	
	private final String chatPrefix;
	
	public Messaging(String chatPrefix) {
		this.chatPrefix = chatPrefix;
	}
	
	public static void send(String chatPrefix, Player player, Message message, Object... arguments) {
		player.sendMessage(chatPrefix + message.get(arguments));
		// TODO i18n of strings - not hardcoded
	}
	
	public static void broadcast(String chatPrefix, Collection<Player> players, Message message, Object... arguments) {
		players.forEach((player) -> send(chatPrefix, player, message, arguments));
	} 
	
	public static final Message ERROR_GAME_STOPPED = new Message(mapOf(Language.ENGLISH, ChatColor.RED + "This game has been stopped, you cannot join."));

	public static final Message ERROR_ALREADY_IN_LOBBY = new Message(mapOf(Language.ENGLISH, ChatColor.RED + "You are already in the lobby!"));
	
	public static final Message LOBBY_ENOUGH_PLAYERS_READY_UP = new Message(mapOf(Language.ENGLISH, ChatColor.GREEN + "Enough players in the lobby, ready up to start the countdown! (%d)"));
	
	public static final Message LOBBY_START_CONDITIONS_READY_NO_LONGER_MET = new Message(mapOf(Language.ENGLISH, ChatColor.RED + "Not enough players in lobby and ready, start aborted!"));

	public static final Message NOT_ENOUGH_PLAYERS_STOPPING_COUNTDOWN = new Message(mapOf(Language.ENGLISH, ChatColor.RED + "Not enough players, stopping countdown!"));
		
	// First parameter - lobby name
	public static final Message LOBBY_REMOVING_YOU_FROM = new Message(mapOf(Language.ENGLISH, ChatColor.RED + "" + ChatColor.ITALIC + "Removing you from %s lobby . . ."));
	
	public static final Message GAME_INTERRUPTED = new Message(mapOf(Language.ENGLISH, ChatColor.RED + "The game has been interrupted. Stopping . . ."));
	
	public static void MATCH_STARTING_IN(String chatPrefix, Player player, int seconds, String arenaName) {
		player.sendMessage(chatPrefix + ChatColor.GOLD + "Match starting in " + seconds + " second(s) on " + arenaName + "!");
	}
	
	public static void MATCH_STARTING_IN(String chatPrefix, Player player, int seconds) {
		player.sendMessage(chatPrefix + ChatColor.GOLD + "Match starting in " + seconds + " second" + (seconds == 1 ? "" : "s") + "!");
	}
	
	private static Map<Language, String> mapOf(Language l, String s){
		Map<Language, String> map = new HashMap<>();
		map.put(l,  s);
		return map;
	}
	
}
