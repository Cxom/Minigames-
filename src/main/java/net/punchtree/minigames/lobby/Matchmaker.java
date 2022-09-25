package net.punchtree.minigames.lobby;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.entity.Player;

import net.punchtree.minigames.game.PvpGame;

public class Matchmaker {
	
	List<PvpGame> possibleMatches;
	Consumer<Player> onPlayerLeaveLobbyOrGame;
	List<MatchmakingLobbyWithVoting> lobbies = new ArrayList<>();
	
	public Matchmaker(List<PvpGame> possibleMatches, Consumer<Player> onPlayerLeaveLobbyOrGame) {
		this.possibleMatches = possibleMatches;
		this.onPlayerLeaveLobbyOrGame = onPlayerLeaveLobbyOrGame;
	}
	
	public boolean findLobbyFor(Player player) {
		for(MatchmakingLobbyWithVoting lobby : lobbies) {
			if (!lobby.isFull()) {
				boolean success = lobby.addPlayerIfPossible(player);
				if (!success) {
					throw new IllegalStateException("Lobby is not full, but could not add player!");
				}
				return success;
			}
		}
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
	
	private static int lobbyCounter = 1;
	private String getNextLobbyName() {
		String lobbyName = "Voting Lobby " + lobbyCounter;
		++lobbyCounter;
		return lobbyName;
	}
	
	
	
}
