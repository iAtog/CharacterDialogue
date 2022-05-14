package com.github.iatog.characterdialogue.hook;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.github.iatog.characterdialogue.api.hook.HologramHook;

public class InvalidHologramsHook implements HologramHook {

    private CharacterDialoguePlugin main;

    public InvalidHologramsHook(CharacterDialoguePlugin main) {
        this.main = main;
    }

    @Override
    public void reloadHolograms() {
        main.getLogger().warning("An attempt was made to reload the holograms but... there is no hologram plugin started.");
    }

    @Override
    public void loadHologram(int npcId) {
        main.getLogger().warning("An attempt was made to load a hologram but... there is no hologram plugin started.");
    }

}
