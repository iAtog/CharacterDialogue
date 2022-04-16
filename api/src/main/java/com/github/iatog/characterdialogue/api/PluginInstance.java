package com.github.iatog.characterdialogue.api;

import java.util.UUID;

import com.github.iatog.characterdialogue.api.cache.Cache;

public interface PluginInstance {

    CharacterDialogueAPI getAPI();
    
    Cache<UUID, User> getUsers();

}
