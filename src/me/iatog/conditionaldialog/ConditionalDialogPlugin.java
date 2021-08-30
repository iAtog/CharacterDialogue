package me.iatog.conditionaldialog;

import org.bukkit.plugin.java.JavaPlugin;

import me.iatog.conditionaldialog.interfaces.FileFactory;
import me.iatog.conditionaldialog.libraries.Cache;
import me.iatog.conditionaldialog.loader.plugin.PluginLoader;

public class ConditionalDialogPlugin extends JavaPlugin {
	
	private PluginLoader loader;
	private FileFactory fileFactory;
	private Cache cache;
	
	@Override
	public void onEnable() {
		this.loader = new PluginLoader(this);
		this.cache = new Cache();
		
		loader.load();
	}
	
	@Override
	public void onDisable() {
		loader.unload();
	}

	public FileFactory getFileFactory() {
		return fileFactory;
	}
	
	public Cache getCache() {
		return cache;
	}
	
	public void setDefaultFileFactory(FileFactory factory) {
		this.fileFactory = factory;
	}
	
}