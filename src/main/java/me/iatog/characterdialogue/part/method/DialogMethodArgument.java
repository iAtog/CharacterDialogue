package me.iatog.characterdialogue.part.method;

import me.iatog.characterdialogue.dialogs.DialogMethod;
import org.bukkit.plugin.java.JavaPlugin;

public class DialogMethodArgument {
    private final String name;
    private final DialogMethod<? extends JavaPlugin> method;

    public DialogMethodArgument(String name, DialogMethod<? extends JavaPlugin> method) {
        this.name = name;
        this.method = method;
    }

    public DialogMethod<? extends JavaPlugin> getMethod() {
        return method;
    }

    public String getName() {
        return name;
    }
}