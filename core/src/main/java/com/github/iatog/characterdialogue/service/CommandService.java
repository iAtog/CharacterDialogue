package com.github.iatog.characterdialogue.service;

import javax.inject.Inject;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.github.iatog.characterdialogue.api.service.Service;
import com.github.iatog.characterdialogue.command.PrincipalCommand;

import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;

public class CommandService implements Service {
    
    @Inject
    private CharacterDialoguePlugin main;
    
    private AnnotatedCommandTreeBuilder builder;
    
    public CommandService() {
        PartInjector injector = PartInjector.create();
        injector.install(new DefaultsModule());
        injector.install(new BukkitModule());
        this.builder = new AnnotatedCommandTreeBuilderImpl(injector);
    }
    
    @Override
    public void start() {
        registerCommands(new PrincipalCommand(main));
        
    }
    
    private void registerCommands(CommandClass... commands) {
        for (CommandClass command : commands) {
            main.getCommandManager().registerCommands(builder.fromClass(command));
        }
    }
    
}
