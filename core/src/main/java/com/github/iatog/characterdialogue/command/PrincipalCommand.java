package com.github.iatog.characterdialogue.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.iatog.characterdialogue.CharacterDialoguePlugin;
import com.github.iatog.characterdialogue.util.StringUtil;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;

@Command(names = "dialogue")
public class PrincipalCommand implements CommandClass {

    private CharacterDialoguePlugin main;

    public PrincipalCommand(CharacterDialoguePlugin main) {
        this.main = main;
    }

    @Command(names = { "", "help" }, desc = "Main command")
    public void mainCommand(CommandSender sender) {
        sender.sendMessage(translatedList(
                "&8[&cCharacterDialogue&8] &7Help",
                "&7/&adialogue create &8<&name&8> &f- &ecreate a new dialogue",
                "&7/&adialogue menu &f- &eopen dialogues menu",
                "&7/&adialogue reload &f- &ereload the plugin",
                "&7/&adialogue fix-cache &8<&cplayer_name&8> &f- &erestart player cache"
                ));
    }

    @Command(names = "create", desc = "create a new dialogue")
    public void create(@Sender Player player, String name) {
        
    }

    @Command(names = "menu", desc = "open the dialogues menu")
    public void menu(@Sender Player player) {
        
    }
    
    @Command(names = "reload", desc = "Reload the plugin")
    public void reload(CommandSender sender) {
        
    }
    
    @Command(names = "fix-cache", desc = "fix the cache")
    public void cache(CommandSender sender, Player target) {
        
    }
    
    private String[] translatedList(String...strings) {
        List<String> list = new ArrayList<>();
        
        for(String string : strings) {
            list.add(StringUtil.colorize(string));
        }
        
        return list.toArray(new String[0]);
    }
}
