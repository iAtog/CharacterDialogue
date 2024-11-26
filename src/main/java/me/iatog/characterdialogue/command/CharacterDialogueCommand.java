package me.iatog.characterdialogue.command;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.DialogueImpl;
import me.iatog.characterdialogue.libraries.Cache;
import me.iatog.characterdialogue.session.ChoiceSession;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.util.TextUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

@Command(names = {
		"characterdialogue", "cdp"
},      permission = "characterdialogue.use",
		desc = "CharacterDialogue main command")
@SubCommandClasses({DialogueCommands.class, MethodCommands.class})
public class CharacterDialogueCommand implements CommandClass {

	/*
	* /characterdialogue
	* /characterdialogue reload
	* /characterdialogue clear-cache
	* /characterdialogue dialogue view <name>
	* /characterdialogue dialogue start <name> [player]
	* /characterdialogue dialogues
	* /characterdialogue gui
	* */

	private final CharacterDialoguePlugin main;
	private final YamlDocument language;
	
	public CharacterDialogueCommand(CharacterDialoguePlugin main) {
		this.main = main;
		this.language = this.main.getFileFactory().getLanguage();
	}
	
	@Command(names = "", desc = "Main command")
	public void mainCommand(CommandSender sender) {
		sender.sendMessage(translateList(language.getStringList("help-message")).stream().toArray(String[]::new));
	}
	
	@Command(names = "reload",
			permission = "characterdialogue.reload",
			desc = "Reload the plugin")
	public void reloadCommand(CommandSender sender) {
		Cache cache = main.getCache();

        try {
            main.getFileFactory().reload();
        } catch (IOException e) {
            main.getLogger().severe("Error reloading CharacterDialogue files.");
        }

        main.getApi().reloadHolograms();
		
		cache.getDialogues().clear();

		try {
			main.loadAllDialogues();

			for(YamlDocument dialogueFile : main.getAllDialogues()) {
				if(dialogueFile == null) continue;
				dialogueFile.getSection("dialogue").getRoutesAsStrings(false).forEach(name -> {
					cache.getDialogues().put(name, new DialogueImpl(main, name, dialogueFile));
				});
			}

		} catch(IOException exception) {
			sender.sendMessage("Error loading all dialogues");
			exception.printStackTrace();
			return;
		}

		sender.sendMessage(TextUtils.colorize("&aLoaded " + cache.getDialogues().size() + " dialogues."));
		
		sender.sendMessage(TextUtils.colorize(language.getString("reload-message")));
	}
	
	@Command(names = "clear-cache",
	         permission = "characterdialogue.clear-cache",
	         desc = "Clear a player memory cache")
	public void clearCache(CommandSender sender, Player target) {
		if(target == null || !target.isOnline()) {
			sender.sendMessage(TextUtils.colorize("&cThe player isn't online."));
			return;
		}
		
		Map<UUID, DialogSession> dialogSessions = main.getCache().getDialogSessions();
		Map<UUID, ChoiceSession> choiceSessions = main.getCache().getChoiceSessions();
		boolean done = false;
		
		if(dialogSessions.containsKey(target.getUniqueId())) {
			dialogSessions.get(target.getUniqueId()).destroy();
			done = true;
		}
		
		if(choiceSessions.containsKey(target.getUniqueId())) {
			choiceSessions.get(target.getUniqueId()).destroy();
			done = true;
		}
		
		if(!done) {
			sender.sendMessage(TextUtils.colorize("&cThat player doesn't have any data in the memory cache."));
		} else {
			sender.sendMessage(TextUtils.colorize("&aCleared " + target.getName() + "'s cache"));
		}
	}

	private List<String> translateList(List<String> list) {
		List<String> newList = new ArrayList<>();
		list.forEach((line) -> {
			newList.add(TextUtils.colorize(line));
		});
		return newList;
	}
	
}
