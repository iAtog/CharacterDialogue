package com.github.iatog.characterdialogue.api.impl;

import java.util.List;

import org.bukkit.entity.Player;

import com.github.iatog.characterdialogue.api.dialogue.DialogueLine;
import com.github.iatog.characterdialogue.api.dialogue.DialogueSession;

public class EmptyDialogueSession implements DialogueSession {
    
    private final Player player;
    private final List<DialogueLine> lines;
    
    /**
     * This class does not serve to make a real session,
     * it is to give a fake session for temporary expressions,
     * the purpose of this is not to leave as null the session,
     * since only that one is provided.
     * @param player the session owner
     * @param lines the dialogue lines
     */
    public EmptyDialogueSession(Player player, List<DialogueLine> lines) {
        this.player = player;
        this.lines = lines;
    }
    
    @Override
    public void start(int index) {
        // TODO: nothing.
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public void setIndex(int index) {
        
    }

    @Override
    public void pause() {
        // It will never start
    }

    @Override
    public void destroy() {
        // Never registered
    }

    @Override
    public List<DialogueLine> getLines() {
        return lines;
    }

    @Override
    public Player getOwner() {
        return player;
    }

}
