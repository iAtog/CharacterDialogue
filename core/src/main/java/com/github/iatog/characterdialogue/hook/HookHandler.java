package com.github.iatog.characterdialogue.hook;

import org.bukkit.Bukkit;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.github.iatog.characterdialogue.api.hook.HologramHook;
import com.github.iatog.characterdialogue.hook.hologram.DecentHologramsHook;

public class HookHandler {
    
    private CharacterDialoguePlugin main;
    
    private HologramHook hologram;
    
    public HookHandler(CharacterDialoguePlugin main) {
        this.main = main;

        setup();
    }
    
    private void setup() {
        // hologram hook part
        if(isEnabled("DecentHolograms")) {
            this.hologram = new DecentHologramsHook(main);
        } else if(isEnabled("PlaceHolderAPI")) {
            this.hologram = new HolographicDisplaysHook(main);
        } else {
            this.hologram = new InvalidHologramsHook(main);
        }
    }
    
    public HologramHook getHologramHook() {
        return hologram;
    }
    
    private boolean isEnabled(String plugin) {
        return Bukkit.getPluginManager().isPluginEnabled(plugin);
    }
    
}
