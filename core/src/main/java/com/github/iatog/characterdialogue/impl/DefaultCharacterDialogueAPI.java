package com.github.iatog.characterdialogue.impl;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.github.iatog.characterdialogue.api.CharacterDialogueAPI;
import com.github.iatog.characterdialogue.api.PluginInstance;
import com.github.iatog.characterdialogue.api.dialogue.Dialogue;
import com.github.iatog.characterdialogue.api.user.User;

public class DefaultCharacterDialogueAPI implements CharacterDialogueAPI {
    
    private CharacterDialoguePlugin PLUGIN = CharacterDialoguePlugin.getInstance();
    
    @Override
    public String getDialogueByNPC(int id) {
        return null;
    }

    @Override
    public Dialogue getDialogue(String name) {
        return null;
    }

    @Override
    public void reloadHolograms() {

    }

    @Override
    public void loadHologram(int npcId) {
        
    }

    @Override
    public Dialogue getNPCDialogue(int id) {
        return null;
    }

    @Override
    public Map<String, Dialogue> getDialogues() {
        return null;
    }

    @Override
    public String getNPCDialogueName(int id) {
        return null;
    }

    @Override
    public int getBukkitVersion() {
        return Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1]);
    }

    @Override
    public PluginInstance getPlugin() {
        return CharacterDialoguePlugin.getInstance();
    }

    @Override
    public User getUser(UUID uuid) {
        return PLUGIN.getCacheFactory().getUsers().get(uuid);
    }

    @Override
    public User getUser(Player player) {
        return getUser(player.getUniqueId());
    }
}
