package com.github.iatog.characterdialogue.impl;

import java.util.Map;

import org.bukkit.Bukkit;

import com.github.iatog.characterdialogue.api.CharacterDialogueAPI;
import com.github.iatog.characterdialogue.api.dialogue.Dialogue;

public class DefaultCharacterDialogueAPI implements CharacterDialogueAPI {

    @Override
    public String getDialogueByNPC(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Dialogue getDialogue(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void reloadHolograms() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void loadHologram(int npcId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Dialogue getNPCDialogue(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Dialogue> getDialogues() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getNPCDialogueName(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getBukkitVersion() {
        return Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1]);
    }


}
