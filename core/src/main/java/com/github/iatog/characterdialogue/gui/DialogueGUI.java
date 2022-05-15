package com.github.iatog.characterdialogue.gui;

import org.bukkit.entity.Player;

import com.github.iatog.characterdialogue.gui.abstraction.AbstractGUI;

public class DialogueGUI extends AbstractGUI {

    public DialogueGUI() {
        super("dialogue");
    }

    @Override
    public void open(Player player, String... args) {
        if(args == null || args.length <= 0) {
            return;
        }
        
        String dialogueName = args[0];
        // code
    }

}
