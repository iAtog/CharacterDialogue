package com.github.iatog.characterdialogue.api.choice;

import java.util.function.Predicate;

import org.bukkit.entity.Player;

public abstract class AbstractChoice {
    
    protected final String identifier;
    protected final boolean requiredArgument;
    protected final Predicate<Choice> predicate;
    
    public AbstractChoice(String identifier, boolean requiredArgument, Predicate<Choice> predicate) {
        this.identifier = identifier;
        this.requiredArgument = requiredArgument;
        this.predicate = predicate;
    }
    
    public String getIdentifier() {
        return identifier;
    }
    
    public boolean isRequiredArgument() {
        return requiredArgument;
    }
    
    public boolean run(Player player, String argument) {
        return predicate.test(new Choice() {

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
