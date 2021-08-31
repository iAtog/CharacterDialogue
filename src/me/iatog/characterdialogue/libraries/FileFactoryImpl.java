package me.iatog.characterdialogue.libraries;

import me.iatog.characterdialogue.ConditionalDialogPlugin;
import me.iatog.characterdialogue.interfaces.FileFactory;

public class FileFactoryImpl implements FileFactory {
	
	private YamlFile config;
	private YamlFile dialogs;
	
	public FileFactoryImpl(ConditionalDialogPlugin main) {
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
