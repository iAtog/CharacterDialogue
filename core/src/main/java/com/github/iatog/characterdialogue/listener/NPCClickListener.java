package com.github.iatog.characterdialogue.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.iatog.characterdialogue.api.ClickType;

import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;

public class NPCClickListener implements Listener {
    
    @EventHandler
    public void onLeftClick(NPCLeftClickEvent event) {
        onClick(event, ClickType.LEFT);
    }
    
    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        onClick(event, ClickType.RIGHT);
    }
    
    private void onClick(NPCClickEvent event, ClickType clickType) {
        // TODO: run dialogue
    }
    
}
