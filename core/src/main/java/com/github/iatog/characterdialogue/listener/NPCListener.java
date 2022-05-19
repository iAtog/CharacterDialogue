package com.github.iatog.characterdialogue.listener;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.iatog.characterdialogue.api.CharacterDialogueAPI;
import com.github.iatog.characterdialogue.api.cache.Cache;
import com.github.iatog.characterdialogue.api.dialogue.Dialogue;
import com.github.iatog.characterdialogue.api.dialogue.Dialogue.DialoguePermission;
import com.github.iatog.characterdialogue.api.dialogue.DialogueSession;
import com.github.iatog.characterdialogue.api.types.ClickType;
import com.github.iatog.characterdialogue.api.user.User;
import com.github.iatog.characterdialogue.util.StringUtil;

import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;

public class NPCListener implements Listener {
    
    private CharacterDialogueAPI API = CharacterDialogueAPI.create();
    
    @EventHandler
    public void onLeftClick(NPCLeftClickEvent event) {
        onClick(event);
    }
    
    @EventHandler
    public void onRightClick(NPCRightClickEvent event) {
        onClick(event);
    }
    
    @EventHandler
    public void onNPCSpawn(NPCSpawnEvent event) {
        NPC npc = event.getNPC();
        int id = npc.getId();
        
        API.loadHologram(id);
    }
    
    private void onClick(NPCClickEvent event) {
        Player player = event.getClicker();
        int npcId = event.getNPC().getId();
        Dialogue dialogue = API.getNPCDialogue(npcId);
        Cache<UUID, DialogueSession> sessions = API.getPlugin().getCacheFactory().getDialogueSessions();
        User user = API.getUser(player);

        if(dialogue == null || sessions.contains(player.getUniqueId())) {
            return;
        }
        
        ClickType clickType = dialogue.getClickType();
        DialoguePermission permissions = dialogue.getPermissions();

        if (((event instanceof NPCRightClickEvent && clickType != ClickType.RIGHT)
                || (event instanceof NPCLeftClickEvent && clickType != ClickType.LEFT)) && clickType != ClickType.ALL) {
            return;
        }

        if(permissions != null && permissions.getPermission() != null) {
            String permission = permissions.getPermission();
            String message = permissions.getMessage();
            
            if(!player.hasPermission(permission)) {
                if(message != null) {
                    player.sendMessage(StringUtil.translatePlaceholders(player, message, dialogue.getDisplayName()));
                }
                return;
            }
        }

        if (dialogue.isFirstInteractionEnabled() && user.readedDialogue(dialogue)) {
            dialogue.startFirstInteraction(player, true);
            return;
        }
        
        dialogue.start(player);
    }
}
