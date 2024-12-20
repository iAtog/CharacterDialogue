package me.iatog.characterdialogue.adapter;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.adapter.citizens.CitizensAdapter;
import org.bukkit.Bukkit;

public class AdapterManager {

    private final CharacterDialoguePlugin main;
    private NPCAdapter<?> adapter;

    public AdapterManager(CharacterDialoguePlugin main) {
        this.main = main;
    }

    public void setAdapter(NPCAdapter<?> adapter) {
        this.adapter = adapter;
    }

    public NPCAdapter<?> getAdapter() {
        return adapter;
    }

    public void detectAdapter() {
        if(Bukkit.getPluginManager().isPluginEnabled("Citizens")) {
            this.adapter = new CitizensAdapter();
            main.getLogger().info("Using Citizens adapter!");
        }

        if(this.adapter != null) {
            this.adapter.registerEvents(main);
        }
    }

}
