package me.iatog.characterdialogue.misc;

import me.iatog.characterdialogue.dialogs.DialogChoice;

public record Choice(int getIndex, String getMessage, Class<? extends DialogChoice> getChoiceClass, String getArgument) { }
