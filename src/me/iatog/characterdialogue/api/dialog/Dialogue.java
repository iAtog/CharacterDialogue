package me.iatog.characterdialogue.api.dialog;

import java.util.List;

import org.bukkit.entity.Player;

import me.iatog.characterdialogue.enums.ClickType;

public interface Dialogue {
	String getName();
	List<String> getLines();
	ClickType getClickType();
	String getDisplayName();
	DialogHologram getHologram();
	List<String> getFirstInteractionLines();
	boolean isFirstInteractionEnabled();
	boolean start(Player player);
	boolean startFirstInteraction(Player player, boolean log);
	DialoguePermission getPermissions();
	boolean isMovementAllowed();
	
	public class DialoguePermission {
		
		private String permission;
		private String message;
		
		public DialoguePermission(String permission, String message) {
			this.permission = permission;
			this.message = message;
		}
		
		public String getPermission() {
			return permission;
		}
		
		public String getMessage() {
			return message;
		}
		
	}
}
