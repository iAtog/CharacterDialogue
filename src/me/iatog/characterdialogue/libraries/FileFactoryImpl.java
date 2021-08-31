package me.iatog.characterdialogue.libraries;

import me.iatog.characterdialogue.CharacterDialogPlugin;
import me.iatog.characterdialogue.interfaces.FileFactory;

public class FileFactoryImpl implements FileFactory {
	
	private YamlFile config;
	private YamlFile dialogs;
	
	public FileFactoryImpl(CharacterDialogPlugin main) {
		this.config = new YamlFile(main, "config");
		this.dialogs = new YamlFile(main, "dialogs");
	}
	
	@Override
	public YamlFile getConfig() {
		return config;
	}

	@Override
	public YamlFile getDialogs() {
		return dialogs;
	}
	
}
