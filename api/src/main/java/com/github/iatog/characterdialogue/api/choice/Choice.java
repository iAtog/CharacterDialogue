package com.github.iatog.characterdialogue.api.choice;

import org.bukkit.entity.Player;

public interface Choice {
    
    Player getPlayer();
    String getArgument();
/*  ChoiceSession getSession();
    DialogueSession getMainSession();
*/  
}
