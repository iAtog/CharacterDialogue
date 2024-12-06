package me.iatog.characterdialogue.part.npc;

import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class NPCPartFactory implements PartFactory {

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        return new NPCPart(name);
    }
}
