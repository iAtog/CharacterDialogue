package com.github.iatog.characterdialogue.api;

import com.github.iatog.characterdialogue.api.cache.CacheFactory;

public interface PluginInstance {

    CharacterDialogueAPI getAPI();
    
    CacheFactory getCacheFactory();
    
}
