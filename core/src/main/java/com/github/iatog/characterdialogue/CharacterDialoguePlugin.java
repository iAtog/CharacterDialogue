package com.github.iatog.characterdialogue;

import javax.inject.Inject;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.iatog.characterdialogue.api.CharacterDialogueAPI;
import com.github.iatog.characterdialogue.api.PluginInstance;
import com.github.iatog.characterdialogue.api.service.Service;
import com.github.iatog.characterdialogue.modules.BinderModule;

import team.unnamed.inject.Injector;

public class CharacterDialoguePlugin extends JavaPlugin implements PluginInstance {
    
    @Inject
    private Service service;
    
    @Inject
    private CharacterDialogueAPI api;
    
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
    
}
