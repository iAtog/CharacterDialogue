package com.github.iatog.characterdialogue.modules;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.github.iatog.characterdialogue.util.Configuration;
import com.google.inject.AbstractModule;

import static com.google.inject.name.Names.named;

public class FileModule extends AbstractModule {
    
    private final CharacterDialoguePlugin main;
    
    public FileModule(CharacterDialoguePlugin main) {
        this.main = main;
    }
    
    @Override
    protected void configure() {
        this.bind(Configuration.class)
                .toInstance(new Configuration(main, "config"));
        
        this.bind(Configuration.class)
                .annotatedWith(named("dialogues"))
                .toInstance(new Configuration(main, "dialogues"));
        
        this.bind(Configuration.class)
                .annotatedWith(named("language"))
                .toInstance(new Configuration(main, "language"));
        
        this.bind(Configuration.class)
                .annotatedWith(named("player-cache"))
                .toInstance(new Configuration(main, "player-cache"));
    }

}
