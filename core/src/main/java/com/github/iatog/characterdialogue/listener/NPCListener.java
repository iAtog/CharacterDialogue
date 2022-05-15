package com.github.iatog.characterdialogue.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.iatog.characterdialogue.api.CharacterDialogueAPI;
import com.github.iatog.characterdialogue.api.dialogue.Dialogue;
import com.github.iatog.characterdialogue.api.types.ClickType;

import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;

public class NPCListener implements Listener {
    
    private CharacterDialogueAPI API = CharacterDialogueAPI.create();
    
    @EventHandler
    public void onLeftClick(NPCLeftClickEvent event) {
        onClick(event, ClickType.LEFT);
    }
    
    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        onClick(event, ClickType.RIGHT);
    }
    
    @EventHandler
    public void onNPCSpawn(NPCSpawnEvent event) {
        NPC npc = event.getNPC();
        int id = npc.getId();
        
        API.loadHologram(id);
    }
    
    private void onClick(NPCClickEvent event, ClickType clickType) {
        Player player = event.getClicker();
        int npcId = event.getNPC().getId();
        Dialogue dialogue = API.getNPCDialogue(npcId);
        
        
    }
    
    
    
}
