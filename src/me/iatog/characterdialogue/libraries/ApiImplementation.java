package me.iatog.characterdialogue.libraries;

import java.util.Optional;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.CharacterDialogueAPI;

public class ApiImplementation implements CharacterDialogueAPI {
	
	private CharacterDialoguePlugin main;
	
	public ApiImplementation(CharacterDialoguePlugin main) {
		this.main = main;
	}
	
	@Override
	public Optional<String> searchDialogueByNPCId(int id) {
		YamlFile dialogsFile = main.getFileFactory().getDialogs();
		Optional<String> optional = Optional.ofNullable(null);
		for(String name : dialogsFile.getConfigurationSection("dialogs.npcs").getKeys(false)) {
			String path = "dialogs.npcs."+name;
			if(dialogsFile.getInt(path+".npc-id") == id) {
				optional = Optional.ofNullable(path);
				break;
			}
		}
		return optional;
	}

}
