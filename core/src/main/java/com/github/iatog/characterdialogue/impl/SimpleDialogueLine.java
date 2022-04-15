package com.github.iatog.characterdialogue.impl;

import org.bukkit.entity.Player;

import com.github.iatog.characterdialogue.api.dialogue.DialogueLine;
import com.github.iatog.characterdialogue.api.dialogue.DialogueSession;
import com.github.iatog.characterdialogue.api.method.Method;

public class SimpleDialogueLine implements DialogueLine {

    private final Method method;
    private final String argument;
    
    public SimpleDialogueLine(Method method, String argument) {
        this.method = method;
        this.argument = argument;
    }
    
    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public String getArgument() {
        return argument;
    }

    @Override
    public void run(Player player, DialogueSession session) {
        
    }

    @Override
    public void runSingle(Player player) {
        
    }

}
