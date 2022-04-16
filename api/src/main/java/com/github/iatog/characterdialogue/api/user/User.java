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

    void runDialogueExpression(String dialog);
    void runDialogueExpression(String dialog, String npcName);
    void runDialogueExpression(String dialog, String npcName, Consumer<DialogueLine> fail, DialogueSession session);
    void runDialogueExpressions(List<String> lines, ClickType type, int npcId, String displayName);
    void runDialogueExpressions(List<String> lines, String displayName);

    boolean setMovement(boolean movement);
    boolean canEnableMovement();
    void sendMessage(String message);
    
    Player toPlayer();
}
