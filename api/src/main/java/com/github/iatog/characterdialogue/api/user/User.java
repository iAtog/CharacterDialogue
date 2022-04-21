package com.github.iatog.characterdialogue.api.user;

import java.util.List;
import java.util.function.Consumer;

import org.bukkit.entity.Player;

import com.github.iatog.characterdialogue.api.dialogue.Dialogue;
import com.github.iatog.characterdialogue.api.dialogue.DialogueLine;
import com.github.iatog.characterdialogue.api.dialogue.DialogueSession;
import com.github.iatog.characterdialogue.api.types.ClickType;

public interface User {
    
    boolean readDialogue(String dialogue);
    boolean readDialogue(Dialogue dialogue);
    boolean readedDialogue(Dialogue dialogue);
    boolean readedDialogue(String dialogue);

    void runDialogue(Dialogue dialogue);
    void runDialogue(String dialogueName);

    void runDialogueExpression(String method, String argument);
    void runDialogueExpression(String method, String argument, String npcName);
    void runDialogueExpression(String method, String argument, String npcName, Consumer<DialogueLine> fail, DialogueSession session);
    void runDialogueExpressions(List<DialogueLine> lines, ClickType type, int npcId, String displayName);
    void runDialogueExpressions(List<DialogueLine> lines, String displayName);

    boolean setMovement(boolean movement);
    boolean canEnableMovement();
    void sendMessage(String message);
    
    Player toPlayer();
}
