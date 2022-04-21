package com.github.iatog.characterdialogue.module;

import java.util.UUID;

import com.github.iatog.characterdialogue.api.cache.Cache;
import com.github.iatog.characterdialogue.api.cache.CacheFactory;
import com.github.iatog.characterdialogue.api.dialogue.Dialogue;
import com.github.iatog.characterdialogue.api.dialogue.DialogueSession;
import com.github.iatog.characterdialogue.api.impl.CacheFactoryImpl;
import com.github.iatog.characterdialogue.api.impl.SimpleCache;
import com.github.iatog.characterdialogue.api.method.AbstractMethod;
import com.github.iatog.characterdialogue.api.user.User;

import team.unnamed.inject.Binder;
import team.unnamed.inject.Module;

public class CacheModule implements Module {
    
    private final CacheFactory factory;
    
    private Cache<UUID, DialogueSession> sessions;
    private Cache<UUID, User> users;
    private Cache<String, AbstractMethod> methods;
    private Cache<String, Dialogue> dialogues;
    
    public CacheModule() {
        this.sessions = new SimpleCache<>();
        this.users = new SimpleCache<>();
        this.methods = new SimpleCache<>();
        this.dialogues = new SimpleCache<>();
        
        
        this.factory = new CacheFactoryImpl(sessions, methods, users, dialogues);
    }
    
    @Override
    public void configure(Binder binder) {
        
        binder.bind(CacheFactory.class)
                .toInstance(factory);
        
        binder.bind(Cache.class)
                .named("users")
                .toInstance(users);
        
        binder.bind(Cache.class)
                .named("dialogue-sessions")
                .toInstance(sessions);
        
        binder.bind(Cache.class)
                .named("methods")
                .toInstance(methods);
        
    }

}
