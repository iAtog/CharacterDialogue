package me.iatog.conditionaldialog.loader.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import me.iatog.conditionaldialog.ConditionalDialogPlugin;
import me.iatog.conditionaldialog.listeners.NPCInteractEvent;
import me.iatog.conditionaldialog.loader.Loader;

public class ListenerLoader implements Loader {
	
	private ConditionalDialogPlugin main;
	
	public ListenerLoader(ConditionalDialogPlugin main) {
		this.main = main;
	}
	
	@Override
	public void load() {
		registerListeners(
				new NPCInteractEvent(main)
				);
	}
	
	public void registerListeners(Listener... listeners) {
		PluginManager pluginManager = Bukkit.getPluginManager();
		for(Listener listener : listeners) {
			pluginManager.registerEvents(listener, main);
		}
	}
}
