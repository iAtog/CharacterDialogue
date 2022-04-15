package com.github.iatog.characterdialogue.modules;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class BinderModule extends AbstractModule {
    
    private final CharacterDialoguePlugin main;
    
    public BinderModule(CharacterDialoguePlugin main) {
        this.main = main;
    }
    
    @Override
    protected void configure() {
        this.bind(CharacterDialoguePlugin.class)
                .toInstance(main);
        
        this.install(new FileModule(main));
        this.install(new ServiceModule());
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }
    
}
