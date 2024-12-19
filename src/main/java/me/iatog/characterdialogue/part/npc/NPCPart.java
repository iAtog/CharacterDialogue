package me.iatog.characterdialogue.part.npc;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NPCPart implements ArgumentPart {

    private final String name;

    public NPCPart(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String next = stack.hasNext() ? stack.next() : null;

        if (next == null) {
            return Collections.emptyList();
        }

        List<String> suggest = new ArrayList<>();
        for (NPCRegistry registries : CitizensAPI.getNPCRegistries()) {
            registries.forEach(npc -> {
                suggest.add(String.valueOf(npc.getId()));
            });
        }

        return suggest;
    }

    @Override
    public List<NPC> parseValue(CommandContext context, ArgumentStack stack, CommandPart caller) throws ArgumentParseException {
        String possibleNpc = stack.next();

        if (! isNum(possibleNpc)) {
            return Collections.emptyList();
        }

        NPC npc = CitizensAPI.getNPCRegistry().getById(Integer.parseInt(possibleNpc));

        if (npc == null) {
            return Collections.emptyList();
        }

        return Collections.singletonList(npc);
    }

    private boolean isNum(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
