package me.iatog.characterdialogue.command;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.Suggestions;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.CharacterDialogueAPI;
import me.iatog.characterdialogue.api.dialog.Dialogue;
import me.iatog.characterdialogue.util.TextUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@Command(names = "dialogue",
        desc = "Blah",
        permission = "characterdialogue.command.dialogue")
public class DialogueCommands implements CommandClass {

    @Command(names = "start", desc = "Run a dialogue")
    public void dialoguesCommand(@Sender CommandSender sender,
                                 Dialogue dialogue,
                                 @OptArg Player playerOpt) {
        Player target;
        if(dialogue == null) {
            sender.sendMessage(TextUtils.colorize("&cDialogue with that name was not found."));
            return;
        }

        if(playerOpt == null) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(TextUtils.colorize("&cYou need to specify the target player."));
                return;
            }

            target = (Player) sender;
        } else {
            target = playerOpt;
        }

        sender.sendMessage(TextUtils.colorize("&aStarted '&c" + dialogue.getName() + "&a' dialogue for &c" + target.getName() + "&a."));
        dialogue.start(target);
    }

}