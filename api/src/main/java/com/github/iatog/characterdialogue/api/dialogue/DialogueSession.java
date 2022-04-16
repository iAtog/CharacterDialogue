package com.github.iatog.characterdialogue.api.dialogue;

import java.util.List;

import org.bukkit.entity.Player;

public interface DialogueSession {
    void start(int index);
    void start();
    
    boolean hasNext();
    
    void setIndex(int index);
    void pause();
    void destroy();
    
    List<DialogueLine> getLines();
    Player getOwner();
}
