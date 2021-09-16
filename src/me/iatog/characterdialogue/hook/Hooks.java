package me.iatog.characterdialogue.hook;

import org.bukkit.Bukkit;

import me.iatog.characterdialogue.hook.holodisplays.HolographicDisplaysHook;
import me.iatog.characterdialogue.hook.papi.PAPIHook;

public class Hooks {
	
	private PAPIHook papiHook;
	private HolographicDisplaysHook holoHook;
	
	public Hooks() {
		if(isPlaceHolderAPIEnabled()) {
			this.papiHook = new PAPIHook();
		}
		
		if(this.isHoloDisplaysEnabled()) {
			this.holoHook = new HolographicDisplaysHook();
		}
	}
	
	public PAPIHook getPlaceHolderAPIHook() {
		return papiHook;
	}
	
	public HolographicDisplaysHook getHoloDisplaysHook() {
		return holoHook;
	}
	
	public boolean isPlaceHolderAPIEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("PlaceHolderAPI");
	}
	
	public boolean isHoloDisplaysEnabled() {
		return Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays");
	}
	
}
