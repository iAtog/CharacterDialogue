package com.github.iatog.characterdialogue.service;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.github.iatog.characterdialogue.api.service.Service;

public class MainService implements Service {

    @Inject @Named("listener")
    private Service listenerService;
    
    @Inject @Named("dialogue")
    private Service dialogueService;

    @Inject
    private CharacterDialoguePlugin main;

    @Override
    public void start() {
        startServices(listenerService, dialogueService);

        main.getLogger().info(
                String.format("§fCharacterDialogue version %s has been enabled.", main.getDescription().getVersion())
        );
    }

    @Override
    public void stop() {
        stopServices();

        main.getLogger().info(
                String.format("§fCharacterDialogue version %s has been disabled.", main.getDescription().getVersion())
        );
    }

    private void startServices(Service... services) {
        Arrays.stream(services).forEach(Service::start);
    }

    private void stopServices(Service... services) {
        Arrays.stream(services).forEach(Service::stop);
    }

}
