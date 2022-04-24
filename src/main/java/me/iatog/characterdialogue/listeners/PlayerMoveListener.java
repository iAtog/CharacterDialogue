package me.iatog.characterdialogue.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.iatog.characterdialogue.CharacterDialoguePlugin;

public class PlayerMoveListener implements Listener {
	
	private CharacterDialoguePlugin main;
	
	public PlayerMoveListener(CharacterDialoguePlugin main) {
		this.main = main;
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if(!main.getCache().getFrozenPlayers().contains(player.getUniqueId())) {
			return;
		}
		
		Location to = event.getTo();
		Location from = event.getFrom();
		double toX = to.getX();
		double toZ = to.getZ();
		double fromX = from.getX();
		double fromZ = from.getZ();
		
		if (fromX != toX || fromZ != toZ) {
			event.setCancelled(true);
		}
	}
	
}
