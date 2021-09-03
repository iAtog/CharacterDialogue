package me.iatog.characterdialogue.loader.plugin;

import me.iatog.characterdialogue.CharacterDialogPlugin;
import me.iatog.characterdialogue.loader.Loader;
import me.iatog.characterdialogue.loader.cache.CacheLoader;
import me.iatog.characterdialogue.loader.command.CommandLoader;
import me.iatog.characterdialogue.loader.file.FileLoader;
import me.iatog.characterdialogue.loader.listener.ListenerLoader;

public class PluginLoader implements Loader {
	
	private CharacterDialogPlugin main;
	
	public PluginLoader(CharacterDialogPlugin main) {
		this.main = main;
	}
	
	@Override
	public void load() {
		loadLoaders(
				new ListenerLoader(main),
				new FileLoader(main),
				new CacheLoader(main),
				new CommandLoader(main)
				);
		
		main.getLogger().info("§aPlugin loaded §7"+main.getDescription().getVersion());
	}
		
	private void loadLoaders(Loader...loaders) {
		for(Loader loader : loaders) {
			loader.load();
		}
	}
	
}
