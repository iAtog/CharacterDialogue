package com.github.iatog.characterdialogue.method;

import java.util.function.Predicate;

import org.bukkit.entity.Player;

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
    
    public boolean run(Player player, String argument) {
        return predicate.test(new Method() {

            @Override
            public Player getPlayer() {
                return player;
            }

            @Override
            public String getArgument() {
                return argument;
            }
        });
    }
}
