package com.github.iatog.characterdialogue.api.dialogue;

import java.util.List;

public interface DialogueHologram {
    boolean isEnabled();
    float getY();
    List<String> getLines();
}
