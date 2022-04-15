package com.github.iatog.characterdialogue.api;

import java.util.Map;

import com.github.iatog.characterdialogue.api.dialogue.Dialogue;

public interface CharacterDialogueAPI {
    String getDialogueByNPC(int id);

    Dialogue getDialogue(String name);

    void reloadHolograms();

    void loadHologram(int npcId);

    Dialogue getNPCDialogue(int id);

    Map<String, Dialogue> getDialogues();

    String getNPCDialogueName(int id);

    int getBukkitVersion();
}
