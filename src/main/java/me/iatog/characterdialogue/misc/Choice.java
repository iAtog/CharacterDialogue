package me.iatog.characterdialogue.misc;

import me.iatog.characterdialogue.dialogs.DialogChoice;

public class Choice {

    private final int index;
    private final String message;
    private final Class<? extends DialogChoice> clazz;
    private final String argument;

    public Choice(int index, String message, Class<? extends DialogChoice> clazz, String argument) {
        this.index = index;
        this.message = message;
        this.clazz = clazz;
        this.argument = argument;
    }

    public int getIndex() {
        return index;
    }

    public String getMessage() {
        return message;
    }

    public String getArgument() {
        return argument;
    }

    public Class<? extends DialogChoice> getChoiceClass() {
        return clazz;
    }

}
