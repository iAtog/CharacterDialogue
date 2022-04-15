package com.github.iatog.characterdialogue.modules;

import org.bukkit.event.Listener;

import com.github.iatog.characterdialogue.listener.NPCClickListener;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class ListenerModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<Listener> multibinder = Multibinder.newSetBinder(
                binder(),
                Listener.class
        );
        
        multibinder.addBinding().to(NPCClickListener.class);
    }

}
