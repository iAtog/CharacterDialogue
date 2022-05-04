package com.github.iatog.characterdialogue.api.dialogue;

import java.util.List;

import org.bukkit.entity.Player;

public interface DialogueSession {
    void start(int index);
    
    default void start() {
        start(0);
    }
    
    boolean hasNext();
    
    void setIndex(int index);
    void pause();
    void destroy();
    
    List<DialogueLine> getLines();
    Player getOwner();
}
