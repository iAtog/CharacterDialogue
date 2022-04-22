package com.github.iatog.characterdialogue.impl;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.iatog.characterdialogue.api.dialogue.Dialogue;
import com.github.iatog.characterdialogue.api.dialogue.DialogueLine;
import com.github.iatog.characterdialogue.api.dialogue.DialogueSession;
import com.github.iatog.characterdialogue.api.types.ClickType;
import com.github.iatog.characterdialogue.api.user.User;

public class UserImpl implements User {
    
    private UUID uuid;
    
    public UserImpl(UUID uuid) {
        this.uuid = uuid;
    }
    
    @Override
    public boolean readDialogue(String dialogue) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean readDialogue(Dialogue dialogue) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean readedDialogue(Dialogue dialogue) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean readedDialogue(String dialogue) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void runDialogue(Dialogue dialogue) {
        
    }

    @Override
    public void runDialogue(String dialogueName) {
        
    }

    @Override
    public void runDialogueExpression(String method, String argument) {
        
    }

    @Override
    public void runDialogueExpression(String method, String argument, String npcName) {
        
    }

    @Override
    public void runDialogueExpression(String method, String argument, String npcName, Consumer<DialogueLine> fail, DialogueSession session) {
        
    }

    @Override
    public void runDialogueExpressions(List<DialogueLine> lines, ClickType type, int npcId, String displayName) {
        
    }

    @Override
    public void runDialogueExpressions(List<DialogueLine> lines, String displayName) {
        
    }

    @Override
    public boolean setMovement(boolean movement) {
        return false;
    }

    @Override
    public boolean canEnableMovement() {
        return false;
    }

    @Override
    public void sendMessage(String message) {
        
    }

    @Override
    public Player toPlayer() {
        return Bukkit.getPlayer(uuid);
    }

}
