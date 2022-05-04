package com.github.iatog.characterdialogue.api.user;

import java.util.List;
import java.util.function.Consumer;

import org.bukkit.entity.Player;

import com.github.iatog.characterdialogue.api.CharacterDialogueAPI;
import com.github.iatog.characterdialogue.api.dialogue.Dialogue;
import com.github.iatog.characterdialogue.api.dialogue.DialogueLine;
import com.github.iatog.characterdialogue.api.dialogue.DialogueSession;
import com.github.iatog.characterdialogue.api.types.ClickType;

public interface User {

    boolean readDialogue(String dialogue);

    boolean readedDialogue(String dialogue);

    void runDialogue(Dialogue dialogue);

    void runDialogueExpression(String method, String argument, String npcName, Consumer<DialogueLine> fail, DialogueSession session);

    void runDialogueExpressions(List<DialogueLine> lines, String displayName);

    boolean setMovement(boolean movement);

    boolean canEnableMovement();

    void sendMessage(String message);

    Player toPlayer();

    default void runDialogueExpression(String method, String argument) {
        runDialogueExpression(method, argument, "John");
    }

    default void runDialogueExpression(String method, String argument, String npcName) {
        runDialogueExpression(method, argument, npcName, (l) -> {
        }, null);
    }

    default void runDialogue(String dialogueName) {
        runDialogue(CharacterDialogueAPI.create().getPlugin().getCacheFactory().getDialogues().get(dialogueName));
    }

    default boolean readDialogue(Dialogue dialogue) {
        return readDialogue(dialogue.getName());
    }

    default boolean readedDialogue(Dialogue dialogue) {
        return readedDialogue(dialogue.getName());
    }
}
