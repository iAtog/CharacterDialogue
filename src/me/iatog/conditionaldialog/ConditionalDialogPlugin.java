package me.iatog.conditionaldialog;

import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import me.iatog.conditionaldialog.dialogs.DialogMethod;
import me.iatog.conditionaldialog.interfaces.FileFactory;
import me.iatog.conditionaldialog.libraries.Cache;
import me.iatog.conditionaldialog.loader.plugin.PluginLoader;

public class ConditionalDialogPlugin extends JavaPlugin {

	private PluginLoader loader;
	private FileFactory fileFactory;
	private Cache cache;

	private static ConditionalDialogPlugin instance;

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
	 * I only use this method for third party plugins, I do not use this method and
	 * even less abuse it. <br>
	 * <br>
	 * Get the main class of the ConditionalDialog.
	 * 
	 * @return the plugin main class
	 */
	public static ConditionalDialogPlugin getInstance() {
		return instance;
	}

}