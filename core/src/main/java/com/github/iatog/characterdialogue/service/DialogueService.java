package com.github.iatog.characterdialogue.service;

import java.util.Map;

import javax.inject.Inject;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.github.iatog.characterdialogue.api.cache.Cache;
import com.github.iatog.characterdialogue.api.dialogue.Dialogue;
import com.github.iatog.characterdialogue.api.file.DialogueFileManager;
import com.github.iatog.characterdialogue.api.file.YamlFile;
import com.github.iatog.characterdialogue.api.impl.DialogueImpl;
import com.github.iatog.characterdialogue.api.service.Service;

public class DialogueService implements Service {

    @Inject
    private CharacterDialoguePlugin main;

    @Override
    public void start() {
        Cache<String, Dialogue> dialogues = main.getCacheFactory().getDialogues();
        DialogueFileManager manager = main.getDialogueManager();
        
        for(Map.Entry<String, YamlFile> entry : manager.getDialogues().entrySet()) {
            String id = entry.getKey();
            YamlFile file = entry.getValue();
            
            dialogues.set(id, new DialogueImpl(main, id, file));
            main.getLogger().info("Loaded dialogue \"" + id + "\" succesfully");
        }
    }

}
