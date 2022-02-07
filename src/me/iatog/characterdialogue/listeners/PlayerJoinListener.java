package me.iatog.characterdialogue.listeners;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.iatog.characterdialogue.CharacterDialoguePlugin;

public class PlayerJoinListener implements Listener {
	
	private CharacterDialoguePlugin main;
	
	public PlayerJoinListener(CharacterDialoguePlugin main) {
		this.main = main;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		List<UUID> cache = main.getCache().getFrozenPlayers();

		if(cache.contains(player.getUniqueId())) {
			cache.remove(player.getUniqueId());
		}
		
		main.getApi().enableMovement(player);
	}
	
}
