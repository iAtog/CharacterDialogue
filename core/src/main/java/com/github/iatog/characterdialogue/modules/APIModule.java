package com.github.iatog.characterdialogue.modules;

import com.github.iatog.characterdialogue.api.CharacterDialogueAPI;
import com.github.iatog.characterdialogue.impl.DefaultCharacterDialogueAPI;

import team.unnamed.inject.Binder;
import team.unnamed.inject.Module;

public class APIModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(CharacterDialogueAPI.class)
                .toInstance(new DefaultCharacterDialogueAPI());
    }

}
