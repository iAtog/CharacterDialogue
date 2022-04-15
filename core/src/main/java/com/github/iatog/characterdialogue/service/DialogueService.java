package com.github.iatog.characterdialogue.service;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.github.iatog.characterdialogue.api.service.Service;
import com.google.inject.Inject;

public class DialogueService implements Service {
    
    @Inject
    private CharacterDialoguePlugin main;
    
    @Override
    public void start() {
        
    }

}
