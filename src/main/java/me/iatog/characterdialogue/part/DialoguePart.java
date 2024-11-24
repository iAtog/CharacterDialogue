package me.iatog.characterdialogue.part;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.dialog.Dialogue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DialoguePart implements ArgumentPart {

    private final String name;
    private final CharacterDialoguePlugin main;
    private final Map<String, Dialogue> dialogues;

    public DialoguePart(String name, CharacterDialoguePlugin main) {
        this.main = main;
        this.name = name;
        this.dialogues = main.getCache().getDialogues();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String next = stack.hasNext() ? stack.next() : null;
        main.getLogger().info("Dialogue Part 1");
        if(next == null) {
            main.getLogger().info("Dialogue Part empty 1");
            return Collections.emptyList();
        }

        List<String> suggest = new ArrayList<>();
        main.getLogger().info("Dialogue Part 2");
        for(String dialogueName : dialogues.keySet()) {
            if(next.isEmpty() || dialogueName.startsWith(next)) {
                suggest.add(dialogueName);
            }
        }
        main.getLogger().info("Dialogue Part 3: dialogues: " + suggest.size());
        /*
        if(suggest.size() == 1 && dialogues.containsKey(next)) {
            return Collections.emptyList();
        }*/

        return suggest;
    }

    @Override
    public List<Dialogue> parseValue(CommandContext commandContext, ArgumentStack argumentStack, CommandPart commandPart) throws ArgumentParseException {
        String possibleDialogue = argumentStack.next().toLowerCase();
        Map<String, Dialogue> dialogues = main.getCache().getDialogues();

        if(!dialogues.containsKey(possibleDialogue)) {
            return Collections.emptyList();
        }

        Dialogue dialogue = dialogues.get(possibleDialogue);

        return Collections.singletonList(dialogue);
    }
}
