package me.iatog.characterdialogue.loader.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import me.iatog.characterdialogue.CharacterDialogPlugin;
import me.iatog.characterdialogue.listeners.NPCInteractListener;
import me.iatog.characterdialogue.loader.Loader;

public class ListenerLoader implements Loader {
	
	private CharacterDialogPlugin main;
	
	public ListenerLoader(CharacterDialogPlugin main) {
		this.main = main;
	}
	
	@Override
	public void load() {
		registerListeners(
				new NPCInteractListener(main)
				);
	}
	
	public void registerListeners(Listener... listeners) {
		PluginManager pluginManager = Bukkit.getPluginManager();
		for(Listener listener : listeners) {
			pluginManager.registerEvents(listener, main);
		}
	}
}
