package com.github.iatog.characterdialogue.api;

import java.util.UUID;

import com.github.iatog.characterdialogue.api.cache.Cache;
import com.github.iatog.characterdialogue.api.method.Method;

public interface PluginInstance {

    CharacterDialogueAPI getAPI();
    
    Cache<UUID, User> getUsers();
    
    Cache<UUID, Method> getMethods();
    
}
