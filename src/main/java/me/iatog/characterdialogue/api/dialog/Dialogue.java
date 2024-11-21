package me.iatog.characterdialogue.api.dialog;

import me.iatog.characterdialogue.enums.ClickType;
import org.bukkit.entity.Player;

import java.util.List;

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
		
		private final String permission;
		private final String message;
		
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
