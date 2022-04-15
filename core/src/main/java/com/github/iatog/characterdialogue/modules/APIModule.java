package com.github.iatog.characterdialogue.modules;

import com.github.iatog.characterdialogue.api.CharacterDialogueAPI;
import com.github.iatog.characterdialogue.impl.DefaultCharacterDialogueAPI;
import com.google.inject.AbstractModule;

public class APIModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(CharacterDialogueAPI.class)
                .toInstance(new DefaultCharacterDialogueAPI());
    }

}
