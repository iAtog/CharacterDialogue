package me.iatog.characterdialogue.loader.plugin;

import java.util.ArrayList;
import java.util.List;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.loader.Loader;
import me.iatog.characterdialogue.loader.cache.CacheLoader;
import me.iatog.characterdialogue.loader.command.CommandLoader;
import me.iatog.characterdialogue.loader.dialogue.DialogLoader;
import me.iatog.characterdialogue.loader.file.FileLoader;
import me.iatog.characterdialogue.loader.listener.ListenerLoader;

public class PluginLoader implements Loader {
	
	private CharacterDialoguePlugin main;
	private List<Loader> loaders;
	
	public PluginLoader(CharacterDialoguePlugin main) {
		this.main = main;
		this.loaders = new ArrayList<>();
	}
	
	@Override
	public void load() {
		loadLoaders(
				new ListenerLoader(main),
				new FileLoader(main),
				new CacheLoader(main),
				new CommandLoader(main),
				new DialogLoader(main)
				);
		
		main.getLogger().info("§a"+main.getDescription().getName()+" loaded. §7"+main.getDescription().getVersion());
	}
	
	@Override
	public void unload() {
		loaders.forEach(loader -> {
			loader.unload();
		});
		loaders.clear();
	}
	
	public List<Loader> getLoaders() {
		return loaders;
	}
	
	private void loadLoaders(Loader...loaders) {
		for(Loader loader : loaders) {
			loader.load();
			this.loaders.add(loader);
		}
	}
	
}
