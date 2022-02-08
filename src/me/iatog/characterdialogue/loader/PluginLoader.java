package me.iatog.characterdialogue.loader;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.filter.ConsoleFilter;

public class PluginLoader implements Loader {
	
	private CharacterDialoguePlugin main;
	private List<Loader> loaders;
	
	public PluginLoader(CharacterDialoguePlugin main) {
		this.main = main;
		this.loaders = new ArrayList<>();
	}
	
	@Override
	public void load() {
		((Logger) LogManager.getRootLogger()).addFilter(new ConsoleFilter());

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
