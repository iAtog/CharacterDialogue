package com.github.iatog.characterdialogue;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.iatog.characterdialogue.api.CharacterDialogueAPI;
import com.github.iatog.characterdialogue.api.PluginInstance;
import com.github.iatog.characterdialogue.api.User;
import com.github.iatog.characterdialogue.api.cache.Cache;
import com.github.iatog.characterdialogue.api.service.Service;
import com.github.iatog.characterdialogue.module.BinderModule;

import team.unnamed.inject.Injector;

public class CharacterDialoguePlugin extends JavaPlugin implements PluginInstance {
    
    @Inject 
    private Service service;
    
    @Inject 
    private CharacterDialogueAPI api;
    
    @Inject @Named("users")
    private Cache<UUID, User> users;
    
    @Override
    public void onEnable() {
        BinderModule module = new BinderModule(this);
        Injector injector = module.createInjector();

        injector.injectMembers(this);

        service.start();
    }

    @Override
    public void onDisable() {
        service.stop();
    }

    @Override
    public CharacterDialogueAPI getAPI() {
        return api;
    }

    @Override
    public Cache<UUID, User> getUsers() {
        return users;
    }
    
}
