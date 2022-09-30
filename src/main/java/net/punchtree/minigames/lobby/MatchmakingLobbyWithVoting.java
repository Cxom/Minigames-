package net.punchtree.minigames.lobby;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.base.Predicates;

import net.md_5.bungee.api.ChatColor;
import net.punchtree.minigames.MinigamesPlugin;
import net.punchtree.minigames.game.PvpGame;
import net.punchtree.minigames.messaging.Messaging;
import net.punchtree.minigames.utility.player.PlayerUtils;
import net.punchtree.util.debugvar.DebugVars;

public class MatchmakingLobbyWithVoting {

	private static Location TEST_LOBBY_SPAWN;

	private final int minPlayersToStartGame;
	private final int maxPlayers;
	private final int COUNTDOWN_LENGTH_SECONDS;
	private final Random random = new Random();
	
	private final String lobbyName;
	private final List<PvpGame> votingOptions;
	private final Consumer<Player> onPlayerLeaveLobbyOrGame;
	
	private Map<Player, PvpGame> waitingPlayers = new HashMap<>();
	
	private MatchmakingLobbyControls lobbyControls;
	
	private boolean isCountingDown = false;
	
	public MatchmakingLobbyWithVoting(String lobbyName, List<PvpGame> votingOptions, Consumer<Player> onPlayerLeaveLobbyOrGame) {
		TEST_LOBBY_SPAWN = new Location(Bukkit.getWorld("Quarantine"), 3995.5, 78, -2437.5);
		this.minPlayersToStartGame = DebugVars.getInteger("ptg-minPlayersToStartGame", 2);
		this.maxPlayers = DebugVars.getInteger("ptg-maxPlayers", 16);
		this.COUNTDOWN_LENGTH_SECONDS = DebugVars.getInteger("ptg-countdownLength", 5);
		
		this.lobbyName = lobbyName;
		this.votingOptions = votingOptions;
		this.onPlayerLeaveLobbyOrGame = onPlayerLeaveLobbyOrGame;
		
		this.lobbyControls = new MatchmakingLobbyControls(this);
	}
	
	void onPlayerVoteForMap(Player voter, PvpGame vote) {
		assert(waitingPlayers.containsKey(voter));
		waitingPlayers.computeIfPresent(voter, (x, y) -> vote);
	}


	public boolean hasPlayer(Player player) {
		return waitingPlayers.containsKey(player);
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
		waitingPlayers.put(player, null);
		teleportPlayerAndSetStats(player);
		lobbyControls.giveLobbyControls(player);
		
		player.sendMessage(ChatColor.GOLD + "Joining lobby " + lobbyName);
		
		startCountdownIfStartConditionsMet();
	}
	
	private void teleportPlayerAndSetStats(Player player){
		player.teleport(TEST_LOBBY_SPAWN);
		
		//Runnable prevents world settings overriding gamemode change after teleport
		// TODO just make world settings Adventure mode??
		new BukkitRunnable() {
			public void run() {
				player.setGameMode(GameMode.ADVENTURE);
			}
		}.runTaskLater(MinigamesPlugin.getInstance(), 20);
		player.setInvulnerable(true); //No PvP in the lobby
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
					Messaging.broadcast("", waitingPlayers.keySet(), Messaging.NOT_ENOUGH_PLAYERS_STOPPING_COUNTDOWN);
					abortCountdown();
					return;
				}
				
				for (Player player : waitingPlayers.keySet()) {
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
					Messaging.broadcast("", waitingPlayers.keySet(), Messaging.NOT_ENOUGH_PLAYERS_STOPPING_COUNTDOWN);
				}
			}
			
		}.runTaskTimer(MinigamesPlugin.getInstance(), 20, 20);
	}
	
	public void startNow(){
		try {
			PvpGame game = decideGame();
			game.startGame(waitingPlayers.keySet(), onPlayerLeaveLobbyOrGame);
		} catch (NoAvailableGamesException e) {
			waitingPlayers.keySet().forEach(player -> {
				player.sendMessage(ChatColor.RED + "There are no available maps to play on at this time!! Returning players to main hub.");
				player.sendMessage(ChatColor.GOLD + "Returning to the main minigames hub...");	
				onPlayerLeaveLobbyOrGame.accept(player);
			});
		} finally {
			reset();
		}
	}
	
	private PvpGame decideGame() throws NoAvailableGamesException {
		List<PvpGame> mapVotes = waitingPlayers.values().stream()
														.filter(Predicates.notNull())
														.filter(PvpGame::isWaiting)
														.collect(Collectors.toList());
		if (mapVotes.isEmpty()) {
			// choose randomly from available games
			mapVotes = votingOptions.stream().filter(PvpGame::isWaiting).collect(Collectors.toList());
			if (mapVotes.isEmpty()) {
				throw new NoAvailableGamesException();
			}
		}
		return mapVotes.get(random.nextInt(mapVotes.size()));
	}

	private void reset() {
		waitingPlayers.clear();
		isCountingDown = false;
	}

	public boolean isFull() {
		return waitingPlayers.size() >= maxPlayers;
	}
		
	public void removePlayer(Player player){
		if (!hasPlayer(player)) {
			return;
		}
		
		player.sendMessage(ChatColor.GOLD + "Returning to the main minigames hub...");	
		waitingPlayers.remove(player);
		
		onPlayerLeaveLobbyOrGame.accept(player);
	}
	
	private static class NoAvailableGamesException extends Exception {
		
	}
	
}
