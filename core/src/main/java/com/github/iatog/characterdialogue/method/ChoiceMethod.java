package com.github.iatog.characterdialogue.method;

import com.github.iatog.characterdialogue.api.method.AbstractMethod;

public class ChoiceMethod extends AbstractMethod {

    public ChoiceMethod() {
        super("choice", method -> {
            return true;
        });
    }

}
