package net.punchtree.minigames.gui;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import net.punchtree.minigames.MinigamesPlugin;

public class ScrollingScoreboard {

	public static final int MAX_SCOREBOARD_LENGTH = 40;
	
	private Queue<String> activeMessages = new LinkedList<>();
	
	private final Scoreboard scoreboard;
	private Objective sidebar;
	
	public ScrollingScoreboard(Scoreboard scoreboard, String title){
		this.scoreboard = scoreboard;
		sidebar = scoreboard.registerNewObjective("scroller", Criteria.DUMMY, title);
		sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
	}

	public void sendMessage(final String newMessage){
		if (activeMessages.contains(newMessage)){
			this.sendMessage("*" + newMessage);
			return;
		}
		
		Iterator<String> itr = activeMessages.iterator();
		while(itr.hasNext()){
			String message = itr.next();
			Score score = sidebar.getScore(message);
			if(score.getScore() - 1 <= 0){
				itr.remove();
				clearMessage(message);
			}else{
				score.setScore(score.getScore() - 1);
			}
		}
		final Score scroller = sidebar.getScore(newMessage);
		scroller.setScore(10);
		activeMessages.offer(newMessage);
		new BukkitRunnable(){
			public void run(){
				if(activeMessages.contains(newMessage)){
					activeMessages.remove(newMessage);	
					clearMessage(newMessage);
				}
			}
		}.runTaskLater(MinigamesPlugin.getInstance(), 150);
	}
	
	private void clearMessage(String msg){
		scoreboard.resetScores(msg);
	}
	
}
