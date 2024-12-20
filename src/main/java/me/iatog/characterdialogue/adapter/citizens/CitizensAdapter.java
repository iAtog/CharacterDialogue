package me.iatog.characterdialogue.adapter.citizens;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.adapter.AdaptedNPC;
import me.iatog.characterdialogue.adapter.NPCAdapter;
import me.iatog.characterdialogue.listeners.NPCInteractListener;
import me.iatog.characterdialogue.listeners.NPCSpawnListener;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CitizensAdapter extends NPCAdapter<NPC> {

    @Override
    public AdaptedNPC adapt(NPC npc) {
        return new AdaptedCitizensNPC(npc);
    }

    @Override
    public AdaptedNPC getById(int id) {
        NPC npc = CitizensAPI.getNPCRegistry().getById(id);
        return npc == null ? null : adapt(npc);
    }

    @Override
    public List<AdaptedNPC> getNPCs() {
        List<AdaptedNPC> list = new ArrayList<>();
        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        Iterator<NPC> npcIterator = registry.iterator();
        npcIterator.forEachRemaining(npc -> {
            list.add(adapt(npc));
        });

        return list;
    }

    @Override
    public void registerEvents(CharacterDialoguePlugin main) {
        registerListener(main,
              new NPCInteractListener(main),
              new NPCSpawnListener(main)
        );
    }

    @Override
    public String getName() {
        return "CitizensAdapter";
    }
}
