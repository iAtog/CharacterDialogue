package me.iatog.conditionaldialog.loader.plugin;

import me.iatog.conditionaldialog.ConditionalDialogPlugin;
import me.iatog.conditionaldialog.loader.Loader;
import me.iatog.conditionaldialog.loader.cache.CacheLoader;
import me.iatog.conditionaldialog.loader.file.FileLoader;
import me.iatog.conditionaldialog.loader.listener.ListenerLoader;

public class PluginLoader implements Loader {
	
	private ConditionalDialogPlugin main;
	
	public PluginLoader(ConditionalDialogPlugin main) {
		this.main = main;
	}
	
	@Override
	public void load() {
		loadLoaders(
				new ListenerLoader(main),
				new FileLoader(main),
				new CacheLoader(main)
				);
		
		main.getLogger().info("§aPlugin loaded §7"+main.getDescription().getVersion());
	}
		
	private void loadLoaders(Loader...loaders) {
		for(Loader loader : loaders) {
			loader.load();
		}
	}
	
}
