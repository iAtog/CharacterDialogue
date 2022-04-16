package com.github.iatog.characterdialogue.api;

import java.util.UUID;

public interface PluginInstance {

    CharacterDialogueAPI getAPI();
    
    Cache<UUID, User> getUsers();

}
