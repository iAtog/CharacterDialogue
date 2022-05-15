package com.github.iatog.characterdialogue.module;

import java.util.Arrays;
import java.util.List;

import org.bukkit.event.Listener;

import com.github.iatog.characterdialogue.listener.NPCListener;

import team.unnamed.inject.Binder;
import team.unnamed.inject.Module;
import team.unnamed.inject.key.TypeReference;

public class ListenerModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(new TypeReference<List<Listener>>() {})
                .toInstance(Arrays.asList(new NPCListener()));
        
        
    }

}
