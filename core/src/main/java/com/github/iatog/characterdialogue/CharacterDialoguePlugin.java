package com.github.iatog.characterdialogue;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.iatog.characterdialogue.api.service.Service;
import com.github.iatog.characterdialogue.modules.BinderModule;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class CharacterDialoguePlugin extends JavaPlugin {
    
    @Inject
    private Service service;
    
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
    
}
