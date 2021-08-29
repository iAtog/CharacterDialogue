package me.iatog.conditionaldialog.dialogs;

import org.bukkit.entity.Player;

public abstract class DialogMethod {
	
	private String id;
	
	public DialogMethod(String id) {
		this.id = id;
	}
	
	public abstract void cast(Player player, String arg);
	
	
	public String getID() {
		return id.toUpperCase();
	}
}
