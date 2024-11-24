package me.iatog.characterdialogue.command;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.Suggestions;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.iatog.characterdialogue.api.dialog.Dialogue;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@Command(names = "dialogue", desc = "Blah")
public class DialogueCommands implements CommandClass {

    @Command(names = "run", desc = "Run dialogue")
    public void dialoguesCommand(@Sender CommandSender sender,
                                 Dialogue dialogue,
                                 @OptArg Optional<Player> playerOpt) {
        Player target;
        if(dialogue == null) {
            sender.sendMessage("No encontrado");
            return;
        }

        if(!playerOpt.isPresent()) {
            if(!(sender instanceof Player)) {
                sender.sendMessage("You arent a player");
                return;
            }
            target = (Player) sender;
        } else {
            if(!playerOpt.get().isOnline()) {
                sender.sendMessage("offlain");
                return;
            }
            target = playerOpt.get();
        }

        sender.sendMessage(dialogue.getDisplayName() + ": test - " + target.getName());
    }

}