package com.github.iatog.characterdialogue.api.dialogue;

import java.util.List;

import org.bukkit.entity.Player;

import com.github.iatog.characterdialogue.api.types.ClickType;

public interface Dialogue {
    
    String getName();
    String getDisplayName();

    ClickType getClickType();
    DialoguePermission getPermissions();
    DialogueHologram getHologram();

    List<DialogueLine> getLines();
    List<DialogueLine> getFirstInteractionLines();
    
    boolean isFirstInteractionEnabled();
    boolean start(Player player);
    boolean startFirstInteraction(Player player, boolean log);
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
