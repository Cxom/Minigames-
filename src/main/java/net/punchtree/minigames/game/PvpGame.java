package net.punchtree.minigames.game;

import java.util.Set;
import java.util.function.Consumer;

import org.bukkit.entity.Player;

import net.punchtree.minigames.arena.Arena;

public interface PvpGame {
	
	public String getName();
	
	public GameState getGameState();
	
//	default public boolean isAvailable() {
//		return getGameState() == GameState.WAITING;
//	}
	
	default public boolean isStopped() {
		return getGameState() == GameState.STOPPED;
	}
	
	default public boolean isWaiting() {
		return getGameState() == GameState.WAITING;
	}
	
	public Arena getArena();
	
	public void interruptAndShutdown();

	public void startGame(Set<Player> waitingPlayers, Consumer<Player> onPlayerLeaveGame);
	
}
