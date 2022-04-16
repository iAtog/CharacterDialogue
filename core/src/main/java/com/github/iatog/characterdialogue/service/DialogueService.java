package com.github.iatog.characterdialogue.service;

import javax.inject.Inject;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.github.iatog.characterdialogue.api.service.Service;

public class DialogueService implements Service {

    @Inject
    private CharacterDialoguePlugin main;

    @Override
    public void start() {
        // load dialogues
    }

}
