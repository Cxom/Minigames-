package net.punchtree.minigames.lobby;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.punchtree.minigames.MinigamesPlugin;
import net.punchtree.minigames.game.PvpGame;
import net.punchtree.minigames.messaging.Messaging;
import net.punchtree.minigames.utility.player.PlayerUtils;
import net.punchtree.util.debugvar.DebugVars;

@Deprecated
public class MatchmakingLobby {

	private static Location TEST_LOBBY_SPAWN;
	private final int minPlayersToStartGame;
	private final int maxPlayers;
	private final int COUNTDOWN_LENGTH_SECONDS;

	private final PvpGame game;
	private final Consumer<Player> onPlayerLeaveLobbyOrGame;
	
	private Set<Player> waitingPlayers = new HashSet<>();
	private boolean isCountingDown = false;
	
	public MatchmakingLobby(PvpGame game, Consumer<Player> onPlayerLeaveLobbyOrGame) {
		this.game = game;
		this.onPlayerLeaveLobbyOrGame = onPlayerLeaveLobbyOrGame;
		TEST_LOBBY_SPAWN = new Location(Bukkit.getWorld("Quarantine"), 3995.5, 78, -2437.5);
		this.minPlayersToStartGame = DebugVars.getInteger("ptg-minPlayersToStartGame", 2);
		this.maxPlayers = DebugVars.getInteger("ptg-maxPlayers", 2);
		this.COUNTDOWN_LENGTH_SECONDS = DebugVars.getInteger("ptg-countdownLength", 5);
	}
	
	public boolean hasPlayer(Player player) {
		return waitingPlayers.contains(player);
	}
	
	public boolean addPlayerIfPossible(Player player){
		if (hasPlayer(player) || isFull()) {
			return false;
		}
		//Otherwise possible
		addPlayerAndInitialize(player);
		return true;
	}
	
	private void addPlayerAndInitialize(Player player) {
		waitingPlayers.add(player);
		teleportPlayerAndSetStats(player);
	}
	
	private void teleportPlayerAndSetStats(Player player){
		player.setInvulnerable(true); //No PvP in the lobby
		// TODO actually teleport the player
		
		//Runnable prevents world settings overriding gamemode change after teleport
		// TODO just make world settings Adventure mode??
		new BukkitRunnable() {
			public void run() {
				player.setGameMode(GameMode.ADVENTURE);
			}
		}.runTaskLater(MinigamesPlugin.getInstance(), 20);
		player.setFlying(false);
		player.setLevel(0);
		player.setExp(0);
		PlayerUtils.perfectStats(player);
	}
	
	private boolean enoughPlayersToStart() {
		return waitingPlayers.size() >= minPlayersToStartGame;
	}
	
	private boolean startConditionsMet() {
		return enoughPlayersToStart();
	}
	
	public void startCountdownIfStartConditionsMet() {
		if (startConditionsMet() && !isCountingDown){
			startCountdown();
		}
	}

	public void startCountdown(){
		isCountingDown = true;
		
		new BukkitRunnable(){
			
			int i = COUNTDOWN_LENGTH_SECONDS; 
			
			@Override
			public void run(){
				if (i <= 0){
					attemptStart();
					return;
				}
				countOff();
				i--;
			}
			
			private void countOff() {
				if (!startConditionsMet()) {
					Messaging.broadcast("", waitingPlayers, Messaging.NOT_ENOUGH_PLAYERS_STOPPING_COUNTDOWN);
					abortCountdown();
					return;
				}
				
				for (Player player : waitingPlayers) {
					Messaging.MATCH_STARTING_IN("", player, i);
					player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, .25f, .9f);
				}
			} 
			
			private void abortCountdown() {
				this.cancel();
				isCountingDown = false;
			}

			private void attemptStart() {
				abortCountdown();
				
				if (startConditionsMet()) {
					startNow();
				} else {
					Messaging.broadcast("", waitingPlayers, Messaging.NOT_ENOUGH_PLAYERS_STOPPING_COUNTDOWN);
				}
			}
			
		}.runTaskTimer(MinigamesPlugin.getInstance(), 20, 20);
	}
	
	public void startNow(){
		game.startGame(waitingPlayers, onPlayerLeaveLobbyOrGame);
		removeAll();
	}
	
	private void removeAll() {
		waitingPlayers.clear();
		isCountingDown = false;
	}

	public boolean isFull() {
		return waitingPlayers.size() >= maxPlayers;
	}
	
}
