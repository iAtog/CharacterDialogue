package com.github.iatog.characterdialogue.api;

import java.util.logging.Logger;

import com.github.iatog.characterdialogue.api.cache.CacheFactory;
import com.github.iatog.characterdialogue.api.file.DialogueFileManager;
import com.github.iatog.characterdialogue.api.file.YamlFileRegistry;

public interface PluginInstance {

    CharacterDialogueAPI getAPI();
    
    CacheFactory getCacheFactory();
    
    Logger getLogger();
    
    YamlFileRegistry getFileRegistry();
    
    DialogueFileManager getDialogueManager();
    
}
