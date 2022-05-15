package com.github.iatog.characterdialogue.hook.hologram;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.github.iatog.characterdialogue.api.CharacterDialogueAPI;
import com.github.iatog.characterdialogue.api.dialogue.Dialogue;
import com.github.iatog.characterdialogue.api.dialogue.DialogueHologram;
import com.github.iatog.characterdialogue.api.file.YamlFile;
import com.github.iatog.characterdialogue.api.hook.HologramHook;
import com.github.iatog.characterdialogue.util.StringUtil;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class DecentHologramsHook implements HologramHook {

    private CharacterDialoguePlugin main;
    private List<String> holograms;
    
    public DecentHologramsHook(CharacterDialoguePlugin main) {
        this.main = main;
    }
    
    @Override
    public void reloadHolograms() {
        YamlFile config = main.getFileRegistry().getFile("config");
        
        for(String hologramName : holograms) {
            DHAPI.removeHologram(hologramName);
        }
        
        config.getConfigurationSection("npc").getKeys(false).forEach((id) -> {
            this.loadHologram(Integer.parseInt(id));
        });
    }

    @Override
    public void loadHologram(int npcId) {
        CharacterDialogueAPI api = main.getAPI();
        NPC citizensNpc = CitizensAPI.getNPCRegistry().getById(npcId);

        if (citizensNpc == null) {
            return;
        }

        Dialogue dialogue = api.getNPCDialogue(npcId);

        if (dialogue == null) {
            return;
        }
        
        DialogueHologram hologram = dialogue.getHologram();
        
        if(hologram != null && hologram.isEnabled()) {
            Location location = citizensNpc.getStoredLocation();
            location.add(0, 2 + hologram.getY(), 0);
            Hologram holo = DHAPI.createHologram("characterdialogue-hologram-" + npcId, location);
            String npcName = dialogue.getDisplayName();
            List<String> lines = new ArrayList<>();
            
            for(String line : hologram.getLines()) {
                lines.add(StringUtil.colorize(line.replace("%npc_name%", npcName)));
            }
            
            DHAPI.setHologramLines(holo, lines);
            citizensNpc.setAlwaysUseNameHologram(false);
            holograms.add("characterdialogue-hologram-" + npcId);
        }
    }

}
