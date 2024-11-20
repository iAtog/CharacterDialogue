package me.iatog.characterdialogue.listeners;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerJoinListener implements Listener {
	
	private final CharacterDialoguePlugin main;
	
	public PlayerJoinListener(CharacterDialoguePlugin main) {
		this.main = main;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		List<UUID> cache = main.getCache().getFrozenPlayers();

        cache.remove(player.getUniqueId());
		
		if(main.getApi().canEnableMovement(player)) {
			main.getApi().enableMovement(player);
		}

		PotionEffectType slowness = PotionEffectType.SLOW;

		if(player.hasPotionEffect(slowness)) {
			PotionEffect effect = player.getPotionEffect(slowness);

			if(effect != null && effect.getAmplifier() == 4) {
				player.removePotionEffect(slowness);
			}
		}
	}
	
}
