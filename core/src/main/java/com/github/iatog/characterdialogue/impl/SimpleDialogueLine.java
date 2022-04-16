package com.github.iatog.characterdialogue.impl;

import org.bukkit.entity.Player;

import com.github.iatog.characterdialogue.api.dialogue.DialogueLine;
import com.github.iatog.characterdialogue.api.dialogue.DialogueSession;
import com.github.iatog.characterdialogue.api.method.AbstractMethod;
import com.github.iatog.characterdialogue.util.StringUtil;

public class SimpleDialogueLine implements DialogueLine {

    private final AbstractMethod method;
    private final String argument;
    private String npcName = getNPCName();
    
    public SimpleDialogueLine(AbstractMethod method, String argument) {
        this.method = method;
        this.argument = argument;
    }
    
    public SimpleDialogueLine(AbstractMethod method, String argument, String npcName) {
        this(method, argument);
        this.npcName = npcName;
    }
    
    @Override
    public AbstractMethod getMethod() {
        return method;
    }

    @Override
    public String getArgument() {
        return argument;
    }
    
    @Override
    public String getNPCName() {
        return npcName;
    }

    @Override
    public void run(Player player, DialogueSession session) {
        String translated = StringUtil.translatePlaceholders(player, argument.replace("%npc_name%", getNPCName()));
        method.run(player, translated, session);
    }

    @Override
    public void runSingle(Player player) {
        run(player, null);
    }

}
