package me.iatog.characterdialogue;

import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import me.iatog.characterdialogue.api.CharacterDialogueAPI;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.hook.Hooks;
import me.iatog.characterdialogue.interfaces.FileFactory;
import me.iatog.characterdialogue.libraries.ApiImplementation;
import me.iatog.characterdialogue.libraries.Cache;
import me.iatog.characterdialogue.loader.plugin.PluginLoader;

public class CharacterDialoguePlugin extends JavaPlugin {

	private PluginLoader loader;
	private FileFactory fileFactory;
	private Cache cache;
	private CharacterDialogueAPI api;
	private Hooks hooks;
	
	private static CharacterDialoguePlugin instance;

	@Override
	public void onEnable() {
		instance = this;
		this.cache = new Cache();
		this.loader = new PluginLoader(this);
		this.hooks = new Hooks();
		
		this.api = new ApiImplementation(this);

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
	
	public CharacterDialogueAPI getApi() {
		return api;
	}

	public void setDefaultFileFactory(FileFactory factory) {
		this.fileFactory = factory;
	}
	
	public Hooks getHooks() {
		return hooks;
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
	public static CharacterDialoguePlugin getInstance() {
		return instance;
	}

}