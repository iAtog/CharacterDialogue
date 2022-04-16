package com.github.iatog.characterdialogue.module;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.github.iatog.characterdialogue.util.Configuration;

import team.unnamed.inject.Binder;
import team.unnamed.inject.Module;

public class FileModule implements Module {
    
    private final CharacterDialoguePlugin main;
    
    public FileModule(CharacterDialoguePlugin main) {
        this.main = main;
    }
    
    @Override
    public void configure(Binder binder) {
        binder.bind(Configuration.class)
                .toInstance(new Configuration(main, "config"));
        
        binder.bind(Configuration.class)
                .named("dialogues")
                .toInstance(new Configuration(main, "dialogues"));
        
        binder.bind(Configuration.class)
                .named("language")
                .toInstance(new Configuration(main, "language"));
        
        binder.bind(Configuration.class)
                .named("player-cache")
                .toInstance(new Configuration(main, "player-cache"));
    }

}
