package com.github.iatog.characterdialogue.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;

@Command(names = { "characterdialogue", "dialogue" })
public class PrincipalCommand implements CommandClass {

    private CharacterDialoguePlugin main;

    public PrincipalCommand(CharacterDialoguePlugin main) {
        this.main = main;
    }

    @Command(names = { "", "help" }, desc = "Main command")
    public void mainCommand(CommandSender sender) {
        sender.sendMessage("hello");
    }

    @Command(names = "create", desc = "Create a new dialogue")
    public void create(@Sender Player player) {

    }

    @Command(names = "menu", desc = "Open the dialogues menu")
    public void menu(@Sender Player player) {

    }
    
    @Command(names = "reload", desc = "Reload the plugin")
    public void reload(CommandSender sender) {
        
    }
}
