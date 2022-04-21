package com.github.iatog.characterdialogue.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import com.github.iatog.characterdialogue.api.dialogue.Dialogue;
import com.github.iatog.characterdialogue.api.dialogue.DialogueLine;
import com.github.iatog.characterdialogue.api.user.User;

public interface CharacterDialogueAPI {
    Dialogue getDialogue(String name);

    void reloadHolograms();

    void loadHologram(int npcId);

    Dialogue getNPCDialogue(int id);

    Map<String, Dialogue> getDialogues();

    String getNPCDialogueName(int id);

    int getBukkitVersion();

    PluginInstance getPlugin();

    User getUser(UUID uuid);

    List<DialogueLine> parseLines(List<String> lines, String npcName);
    
    default List<DialogueLine> parseLines(List<String> lines) {
        return parseLines(lines, "Jonh");
    }
    
    default User getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    public static CharacterDialogueAPI create() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        PluginInstance plugin = (PluginInstance) pluginManager.getPlugin("CharacterDialogue");
        return plugin.getAPI();
    }
}
