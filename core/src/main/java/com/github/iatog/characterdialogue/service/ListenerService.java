package com.github.iatog.characterdialogue.service;

import java.util.Set;

import org.bukkit.event.Listener;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.google.inject.Inject;

public class ListenerService implements Service {

    @Inject
    private Set<Listener> listeners;
    
    @Inject
    private CharacterDialoguePlugin main;
    
    @Override
    public void start() {
        listeners.forEach(listener -> main.getServer().getPluginManager().registerEvents(listener, main));
    }

}
