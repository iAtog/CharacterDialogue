package me.iatog.characterdialogue.api;

import org.bukkit.entity.Player;

public abstract class GUI {
	
	private String id;
	private String permission;
	
	public GUI(String id, String permission) {
		this.id = id;
		this.permission = permission;
	}
	
	public String getId() {
		return id;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public boolean hasPermission(Player player) {
		return player.hasPermission(permission);
	}
	
	public abstract void open(Player player);
	
	
	
}
