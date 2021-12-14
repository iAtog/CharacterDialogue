package me.iatog.characterdialogue.dialogs;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.iatog.characterdialogue.session.DialogSession;

public abstract class DialogMethod<T extends JavaPlugin> {
	
	private String id;
	private T provider;
	
	public DialogMethod(String id, T provider) {
		this.id = id;
		this.provider = provider;
	}
	
	public DialogMethod(String id) {
		this(id, null);
	}
	
	/**
	 * Execute the method action
	 * @param player the interacting player
	 * @param arg the arguments with which it works
	 */
	public abstract void execute(Player player, String arg, DialogSession session);
	
	/**
	 * Get the id of the method
	 * @return the id
	 */
	public String getID() {
		return id.toUpperCase();
	}
	
	/**
	 * The method provider (in this case the plugin that creates it)
	 * @return the provider
	 */
	public T getProvider() {
		return provider;
	}
}
