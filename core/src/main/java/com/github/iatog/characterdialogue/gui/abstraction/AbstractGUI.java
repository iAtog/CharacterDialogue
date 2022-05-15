package com.github.iatog.characterdialogue.gui.abstraction;

import org.bukkit.entity.Player;

public abstract class AbstractGUI {

    private String name;

    public AbstractGUI(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void open(Player player, String...args);

}
