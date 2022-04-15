package com.github.iatog.characterdialogue.api.dialogue;

import org.bukkit.entity.Player;

import com.github.iatog.characterdialogue.api.method.Method;

public interface DialogueLine {
    
    Method getMethod();
    String getArgument();
    
    void run(Player player, DialogueSession session);
    void runSingle(Player player);
    
}
