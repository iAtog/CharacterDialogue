package com.github.iatog.characterdialogue;

import javax.inject.Inject;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.iatog.characterdialogue.api.CharacterDialogueAPI;
import com.github.iatog.characterdialogue.api.PluginInstance;
import com.github.iatog.characterdialogue.api.cache.CacheFactory;
import com.github.iatog.characterdialogue.api.file.DialogueFileManager;
import com.github.iatog.characterdialogue.api.file.YamlFileRegistry;
import com.github.iatog.characterdialogue.api.service.Service;
import com.github.iatog.characterdialogue.module.BinderModule;

import team.unnamed.inject.Injector;

public class CharacterDialoguePlugin extends JavaPlugin implements PluginInstance {

    private static CharacterDialoguePlugin INSTANCE;

    @Inject
    private Service service;

    @Inject
    private CharacterDialogueAPI api;

    @Inject
    private CacheFactory cache;

    @Inject
    private YamlFileRegistry registry;
    
    private DialogueFileManager dialogueManager;

    @Override
    public void onLoad() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        BinderModule module = new BinderModule(this);
        Injector injector = module.createInjector();

        injector.injectMembers(this);

        this.dialogueManager = DialogueFileManager.createManager(this);
        service.start();
    }

    public static CharacterDialoguePlugin getInstance() {
        return INSTANCE;
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
    public CacheFactory getCacheFactory() {
        return cache;
    }

    @Override
    public YamlFileRegistry getFileRegistry() {
        return registry;
    }

    @Override
    public DialogueFileManager getDialogueManager() {
        return dialogueManager;
    }
    
}