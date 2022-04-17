package com.github.iatog.characterdialogue.api.impl;

import java.util.UUID;

import com.github.iatog.characterdialogue.api.cache.Cache;
import com.github.iatog.characterdialogue.api.cache.CacheFactory;
import com.github.iatog.characterdialogue.api.dialogue.DialogueSession;
import com.github.iatog.characterdialogue.api.method.AbstractMethod;
import com.github.iatog.characterdialogue.api.user.User;

public class CacheFactoryImpl implements CacheFactory {

    private final Cache<UUID, DialogueSession> sessions;
    private final Cache<String, AbstractMethod> methods;
    private final Cache<UUID, User> users;

    public CacheFactoryImpl(Cache<UUID, DialogueSession> sessions, Cache<String, AbstractMethod> methods,
            Cache<UUID, User> users) {
        this.sessions = sessions;
        this.methods = methods;
        this.users = users;
    }

    @Override
    public Cache<UUID, DialogueSession> getDialogueSessions() {
        return sessions;
    }

    @Override
    public Cache<String, AbstractMethod> getMethods() {
        return methods;
    }

    @Override
    public Cache<UUID, User> getUsers() {
        return users;
    }

}
