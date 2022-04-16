package com.github.iatog.characterdialogue.modules;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;

import team.unnamed.inject.Binder;
import team.unnamed.inject.Injector;
import team.unnamed.inject.Module;

public class BinderModule implements Module {
    
    private final CharacterDialoguePlugin main;
    
    public BinderModule(CharacterDialoguePlugin main) {
        this.main = main;
    }
    
    @Override
    public void configure(Binder binder) {
        binder.bind(CharacterDialoguePlugin.class)
                .toInstance(main);
        
        binder.install(new FileModule(main));
        binder.install(new ServiceModule());
        binder.install(new ListenerModule());
        binder.install(new APIModule());
        binder.install(new CacheModule());
    }

    public Injector createInjector() {
        return Injector.create(this);
    }

    
}
