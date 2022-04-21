package com.github.iatog.characterdialogue.module;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.github.iatog.characterdialogue.api.file.YamlFileRegistry;

import team.unnamed.inject.Binder;
import team.unnamed.inject.Module;

public class FileModule implements Module {
    
    private final YamlFileRegistry registry;
    
    public FileModule(CharacterDialoguePlugin main) {
        this.registry = YamlFileRegistry.create(main, "config", "language", "player-cache");
    }
    
    @Override
    public void configure(Binder binder) {
        binder.bind(YamlFileRegistry.class)
                .toInstance(registry);
    }

}
