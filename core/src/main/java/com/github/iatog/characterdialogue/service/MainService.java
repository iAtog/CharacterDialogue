package com.github.iatog.characterdialogue.service;

import java.util.Arrays;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.github.iatog.characterdialogue.api.service.Service;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class MainService implements Service {

    @Inject @Named("listener")
    private Service listenerService;

    @Inject
    private CharacterDialoguePlugin main;

    @Override
    public void start() {
        startServices(listenerService);

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
