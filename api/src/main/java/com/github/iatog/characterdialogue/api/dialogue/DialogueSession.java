package com.github.iatog.characterdialogue.api.dialogue;

import java.util.List;

public interface DialogueSession {
    void start(int index);
    void start();
    
    boolean hasNext();
    
    void setIndex(int index);
    void pause();
    void destroy();
    
    List<DialogueLine> getLines();
}
