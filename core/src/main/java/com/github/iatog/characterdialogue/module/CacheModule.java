package com.github.iatog.characterdialogue.module;

import java.util.UUID;

import com.github.iatog.characterdialogue.api.cache.Cache;
import com.github.iatog.characterdialogue.api.method.AbstractMethod;
import com.github.iatog.characterdialogue.api.user.User;
import com.github.iatog.characterdialogue.impl.SimpleCache;

import team.unnamed.inject.Binder;
import team.unnamed.inject.Module;

public class CacheModule implements Module {
    
    private Cache<UUID, User> users;
    private Cache<String, AbstractMethod> methods;
    
    public CacheModule() {
        this.users = new SimpleCache<UUID, User>();
        this.methods = new SimpleCache<String, AbstractMethod>();
    }
    
    @Override
    public void configure(Binder binder) {
        
        binder.bind(Cache.class)
                .named("users")
                .toInstance(users);
        
        binder.bind(Cache.class)
                .named("methods")
                .toInstance(methods);
        
    }

}
