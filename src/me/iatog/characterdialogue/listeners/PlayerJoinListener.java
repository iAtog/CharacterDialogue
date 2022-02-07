package me.iatog.characterdialogue.listeners;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.libraries.YamlFile;

public class PlayerJoinListener implements Listener {
	
	private CharacterDialoguePlugin main;
	
	public PlayerJoinListener(CharacterDialoguePlugin main) {
		this.main = main;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		List<UUID> cache = main.getCache().getFrozenPlayers();
		YamlFile playerCache = main.getFileFactory().getPlayerCache();
		String playerPath = "players." + player.getUniqueId();
		
		if(cache.contains(player.getUniqueId())) {
			cache.remove(player.getUniqueId());
		}
		
		if(playerCache.getBoolean(playerPath + ".remove-effect", false)) {
			float speed = Float.valueOf(playerCache.getString(playerPath + ".last-speed"));
			player.setWalkSpeed(speed);
			playerCache.set(playerPath + ".remove-effect", false);
			playerCache.save();
		}
	}
	
}
