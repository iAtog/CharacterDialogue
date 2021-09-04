package me.iatog.characterdialogue;

import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.interfaces.FileFactory;
import me.iatog.characterdialogue.libraries.Cache;
import me.iatog.characterdialogue.loader.plugin.PluginLoader;

public class CharacterDialogPlugin extends JavaPlugin {

	private PluginLoader loader;
	private FileFactory fileFactory;
	private Cache cache;

	private static CharacterDialogPlugin instance;

	@Override
	public void onEnable() {
		instance = this;
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

	public void registerMethods(DialogMethod... methods) {
		Map<String, DialogMethod> mapMethods = getCache().getMethods();
		for (DialogMethod method : methods) {
			if (!mapMethods.containsKey(method.getID())) {
				mapMethods.put(method.getID(), method);
			}
		}
	}

	/**
	 * I only set this method for third party plugins, I do not use this method and
	 * even less abuse it. <br>
	 * <br>
	 * Get the main class of the ConditionalDialog.
	 * 
	 * @return the plugin main class
	 */
	public static CharacterDialogPlugin getInstance() {
		return instance;
	}

}