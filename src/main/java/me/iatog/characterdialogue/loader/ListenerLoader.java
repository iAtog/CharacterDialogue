package me.iatog.characterdialogue.loader;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.listeners.PlayerJoinListener;
import me.iatog.characterdialogue.listeners.PlayerMoveListener;
import me.iatog.characterdialogue.listeners.PlayerQuitListener;
import me.iatog.characterdialogue.listeners.RecordListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class ListenerLoader implements Loader {

    private final CharacterDialoguePlugin main;

    public ListenerLoader(CharacterDialoguePlugin main) {
        this.main = main;
    }

    @Override
    public void load() {
        registerListeners(
              new PlayerQuitListener(main),
              new PlayerMoveListener(main),
              new PlayerJoinListener(main),
              new RecordListener()
        );
    }

    public void registerListeners(Listener... listeners) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, main);
        }
    }
}
