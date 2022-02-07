package me.iatog.characterdialogue.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.iatog.characterdialogue.CharacterDialoguePlugin;

public class PlayerMoveListener implements Listener {
	
	private CharacterDialoguePlugin main;
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Location to = event.getTo();
		Location from = event.getFrom();
		int toX = (int) to.getX();
		int toY = (int) to.getY();
		int toZ = (int) to.getZ();
		int fromX = (int) from.getX();
		int fromY = (int) from.getY();
		int fromZ = (int) from.getZ();
		
		if ((fromX != toX || fromY != toY || fromZ != toZ) && false) {
			event.setCancelled(true);
		}
	}
	
}
