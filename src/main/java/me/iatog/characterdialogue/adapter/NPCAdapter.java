package me.iatog.characterdialogue.adapter;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.List;

public abstract class NPCAdapter<T> {

    private List<AdaptedNPC> npcs;

    public abstract AdaptedNPC adapt(T npc);

    public abstract AdaptedNPC getById(String id);

    public abstract List<AdaptedNPC> getNPCs();

    public abstract void registerEvents(CharacterDialoguePlugin main);

    public abstract String getName();

    protected void registerListener(CharacterDialoguePlugin main, Listener ...listeners) {
        for(Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, main);
        }
    }

    public void loadNPCs() {
        npcs = getNPCs();
    }

    public List<AdaptedNPC> getInMemoryNPCs() {
        return npcs;
    }

}