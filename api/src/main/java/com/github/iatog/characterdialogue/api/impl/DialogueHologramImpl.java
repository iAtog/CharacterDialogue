package com.github.iatog.characterdialogue.api.impl;

import java.util.List;

import com.github.iatog.characterdialogue.api.dialogue.DialogueHologram;

public class DialogueHologramImpl implements DialogueHologram {
    
    private boolean enabled;
    private float y;
    private List<String> lines;
    
    public DialogueHologramImpl(boolean enabled, float y, List<String> lines) {
        this.enabled = enabled;
        this.y = y;
        this.lines = lines;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public List<String> getLines() {
        return lines;
    }

}
