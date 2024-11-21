package me.iatog.characterdialogue.loader;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.DialogueImpl;
import me.iatog.characterdialogue.libraries.Cache;

public class DialogLoader implements Loader {
	
	private final CharacterDialoguePlugin main;
	
	public DialogLoader(CharacterDialoguePlugin main) {
		this.main = main;
	}
	
	@Override
	public void load() {
		//YamlFile dialogs = main.getFileFactory().getDialogs();
		Cache cache = main.getCache();
		/*
		dialogs.getConfigurationSection("dialogue").getKeys(false).forEach(name -> {
			cache.getDialogues().put(name, new DialogueImpl(main, name));
		});*/

		for(YamlDocument dialogueFile : main.getAllDialogues()) {
			dialogueFile.getSection("dialogue").getRoutesAsStrings(false).forEach(name -> {
				cache.getDialogues().put(name, new DialogueImpl(main, name, dialogueFile));
			});
		}
	}

}
