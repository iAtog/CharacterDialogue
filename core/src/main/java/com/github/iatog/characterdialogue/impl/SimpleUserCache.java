package com.github.iatog.characterdialogue.impl;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.github.iatog.characterdialogue.api.Cache;
import com.github.iatog.characterdialogue.api.User;

public class SimpleUserCache implements Cache<UUID, User> {
    
    private Map<UUID, User> users;
    
    public SimpleUserCache() {
        this.users = new ConcurrentHashMap<>();
    }
    
    @Override
    public User get(UUID key) {
        return users.get(key);
    }

    @Override
    public void set(UUID key, User value) {
        users.put(key, value);
    }

    @Override
    public boolean contains(UUID key) {
        return users.containsKey(key);
    }

    @Override
    public User remove(UUID key) {
        return users.remove(key);
    }

}
