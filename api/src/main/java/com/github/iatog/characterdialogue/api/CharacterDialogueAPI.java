package com.github.iatog.characterdialogue.api;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

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
    
    public static CharacterDialogueAPI create() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        PluginInstance plugin = (PluginInstance) pluginManager.getPlugin("CharacterDialogue");
        return plugin.getAPI();
    }
}
