package net.punchtree.minigames.lobby;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.punchtree.minigames.game.PvpGame;

public class Matchmaker {
	
	String name;
	List<PvpGame> possibleMatches;
	Consumer<Player> onPlayerLeaveLobbyOrGame;
	List<MatchmakingLobbyWithVoting> lobbies = new ArrayList<>();
	
	public Matchmaker(String name, List<PvpGame> possibleMatches, Consumer<Player> onPlayerLeaveLobbyOrGame) {
		this.name = name;
		this.possibleMatches = possibleMatches;
		this.onPlayerLeaveLobbyOrGame = onPlayerLeaveLobbyOrGame;
	}
	
	public boolean findLobbyFor(Player player) {
		player.sendMessage(ChatColor.GOLD + "Matchmaking (" + name + ") for player " + player.getName() + " - currently " + lobbies.size() + " lobb" + (lobbies.size() == 1 ? "y" : "ies"));
		for(MatchmakingLobbyWithVoting lobby : lobbies) {
			if (!lobby.isFull()) {
				boolean success = lobby.addPlayerIfPossible(player);
				if (!success) {
					throw new IllegalStateException("Lobby is not full, but could not add player!");
				}
				return success;
			}
		}
		player.sendMessage(ChatColor.GOLD + "Creating a new lobby...");
		MatchmakingLobbyWithVoting lobby = createNewLobby();
		boolean success = lobby.addPlayerIfPossible(player);
		if (!success) {
			throw new IllegalStateException("Lobby is not full, but could not add player!");
		}
		return success;
	}

	
	private MatchmakingLobbyWithVoting createNewLobby() {
		var newLobby = new MatchmakingLobbyWithVoting(getNextLobbyName(), possibleMatches, onPlayerLeaveLobbyOrGame);
		lobbies.add(newLobby);
		return newLobby;
	}
	
	private int lobbyCounter = 1;
	private String getNextLobbyName() {
		String lobbyName = "Voting Lobby " + name + " " + lobbyCounter;
		++lobbyCounter;
		return lobbyName;
	}
	
	
	
}
