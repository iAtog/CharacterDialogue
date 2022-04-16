package com.github.iatog.characterdialogue.api.method;

import java.util.function.Predicate;

import org.bukkit.entity.Player;

import com.github.iatog.characterdialogue.api.dialogue.DialogueSession;
import com.github.iatog.characterdialogue.api.impl.MethodImpl;

public abstract class AbstractMethod {
    
    private final String identifier;
    private final Predicate<Method> predicate;
    
    public AbstractMethod(String identifier, Predicate<Method> predicate) {
        this.identifier = identifier.toLowerCase();
        this.predicate = predicate;
    }
    
    public String getIdentifier() {
        return identifier;
    }
    
    public boolean run(Player player, String argument, DialogueSession session) {
        return predicate.test(new MethodImpl(player, argument, session));
    }
}
