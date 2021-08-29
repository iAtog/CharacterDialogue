package me.iatog.conditionaldialog.libraries;

import me.iatog.conditionaldialog.ConditionalDialogPlugin;
import me.iatog.conditionaldialog.interfaces.FileFactory;

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
