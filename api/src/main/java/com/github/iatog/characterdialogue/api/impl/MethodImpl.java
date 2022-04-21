package com.github.iatog.characterdialogue.api.impl;

import org.bukkit.entity.Player;

import com.github.iatog.characterdialogue.api.dialogue.DialogueSession;
import com.github.iatog.characterdialogue.api.method.Method;

public class MethodImpl implements Method {

    private final Player player;
    private final String argument;
    private final DialogueSession session;

    public MethodImpl(Player player, String argument, DialogueSession session) {
        this.player = player;
        this.argument = argument;
        this.session = session;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String getArgument() {
        return argument;
    }

    @Override
    public DialogueSession getSession() {
        return session;
    }
}
