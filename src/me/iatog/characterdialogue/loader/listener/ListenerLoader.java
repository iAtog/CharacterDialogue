package me.iatog.characterdialogue.loader.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.listeners.NPCInteractListener;
import me.iatog.characterdialogue.listeners.NPCSpawnListener;
import me.iatog.characterdialogue.listeners.PlayerQuitListener;
import me.iatog.characterdialogue.loader.Loader;

public class ListenerLoader implements Loader {
	
	private CharacterDialoguePlugin main;
	
	public ListenerLoader(CharacterDialoguePlugin main) {
		this.main = main;
	}
	
	@Override
	public void load() {
		registerListeners(
				new NPCInteractListener(main),
				new NPCSpawnListener(main),
				new PlayerQuitListener(main)
				);
	}
	
	public void registerListeners(Listener... listeners) {
		PluginManager pluginManager = Bukkit.getPluginManager();
		for(Listener listener : listeners) {
			pluginManager.registerEvents(listener, main);
		}
	}
}
