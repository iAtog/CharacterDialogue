package com.github.iatog.characterdialogue.api.cache;

import java.util.UUID;

import com.github.iatog.characterdialogue.api.dialogue.Dialogue;
import com.github.iatog.characterdialogue.api.dialogue.DialogueSession;
import com.github.iatog.characterdialogue.api.method.AbstractMethod;
import com.github.iatog.characterdialogue.api.user.User;

public interface CacheFactory {

    Cache<UUID, DialogueSession> getDialogueSessions();

    Cache<String, AbstractMethod> getMethods();

    Cache<UUID, User> getUsers();
    
    Cache<String, Dialogue> getDialogues();
    
}
