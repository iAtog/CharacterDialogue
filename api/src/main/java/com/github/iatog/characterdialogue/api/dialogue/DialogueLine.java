package com.github.iatog.characterdialogue.api.dialogue;

import org.bukkit.entity.Player;

import com.github.iatog.characterdialogue.api.method.AbstractMethod;

public interface DialogueLine {
    
    AbstractMethod getMethod();
    String getArgument();
    default String getNPCName() {
        return "Jonh Doe";
    }
    
    void run(Player player, DialogueSession session);
    void runSingle(Player player);
    
}
