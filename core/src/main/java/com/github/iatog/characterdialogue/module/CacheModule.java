package com.github.iatog.characterdialogue.module;

import java.util.UUID;

import com.github.iatog.characterdialogue.api.cache.Cache;
import com.github.iatog.characterdialogue.api.user.User;
import com.github.iatog.characterdialogue.impl.SimpleUserCache;

import team.unnamed.inject.Binder;
import team.unnamed.inject.Module;

public class CacheModule implements Module {
    
    private Cache<UUID, User> users;
    
    public CacheModule() {
        this.users = new SimpleUserCache();
    }
    
    @Override
    public void configure(Binder binder) {
        
        binder.bind(Cache.class)
                .named("users")
                .toInstance(users);
        
    }

}
