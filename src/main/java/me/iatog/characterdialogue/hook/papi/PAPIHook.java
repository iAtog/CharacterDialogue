package me.iatog.characterdialogue.hook.papi;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;

public class PAPIHook {
	
	public String translatePlaceHolders(Player player, String toTranslate) {
		return PlaceholderAPI.setPlaceholders(player, toTranslate);
	}
	
}
