package com.github.iatog.characterdialogue.module;

import com.github.iatog.characterdialogue.api.cache.Cache;
import com.github.iatog.characterdialogue.api.cache.CacheFactory;
import com.github.iatog.characterdialogue.api.impl.CacheFactoryImpl;

import team.unnamed.inject.Binder;
import team.unnamed.inject.Module;

public class CacheModule implements Module {

    private final CacheFactory factory;

    public CacheModule() {
        this.factory = new CacheFactoryImpl();
    }

    @Override
    public void configure(Binder binder) {

        binder.bind(CacheFactory.class)
                .toInstance(factory);

        binder.bind(Cache.class)
                .named("users")
                .toInstance(factory.getUsers());

        binder.bind(Cache.class)
                .named("dialogue-sessions")
                .toInstance(factory.getDialogueSessions());

        binder.bind(Cache.class)
                .named("methods")
                .toInstance(factory.getMethods());

    }

}
