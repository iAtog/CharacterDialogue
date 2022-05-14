package com.github.iatog.characterdialogue.api.method;

import org.bukkit.entity.Player;

import com.github.iatog.characterdialogue.api.dialogue.DialogueSession;

public interface MethodContext {
    
    public Player getPlayer();
    public String getArgument();
    public DialogueSession getSession();
    
}
