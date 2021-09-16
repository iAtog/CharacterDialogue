package me.iatog.characterdialogue.hook;

import org.bukkit.Bukkit;

import me.iatog.characterdialogue.hook.papi.PAPIHook;

public class Hooks {
	
	private PAPIHook papiHook;
	
	public Hooks() {
		if(isPlaceHolderAPIEnabled()) {
			this.papiHook = new PAPIHook();
		}
	}
	
	public PAPIHook getPlaceHolderAPIHook() {
		return papiHook;
	}
	
	public boolean isPlaceHolderAPIEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("PlaceHolderAPI");
	}
	
	public boolean isHoloDisplaysEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays");
	}
	
}
