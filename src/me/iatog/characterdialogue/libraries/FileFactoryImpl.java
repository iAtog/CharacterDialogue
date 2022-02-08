package me.iatog.characterdialogue.libraries;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.interfaces.FileFactory;

public class FileFactoryImpl implements FileFactory {
	
	private YamlFile config;
	private YamlFile dialogs;
	private YamlFile lang;
	private YamlFile playerCache;
	
	public FileFactoryImpl(CharacterDialoguePlugin main) {
		this.config = new YamlFile(main, "config");
		this.dialogs = new YamlFile(main, "dialogs");
		this.lang = new YamlFile(main, "language");
		this.playerCache = new YamlFile(main, "player-cache");
	}
	
	@Override
	public YamlFile getConfig() {
		return config;
	}

	@Override
	public YamlFile getDialogs() {
		return dialogs;
	}

	@Override
	public YamlFile getLanguage() {
		return lang;
	}
	
	@Override
	public YamlFile getPlayerCache() {
		return playerCache;
	}
	
	
	@Override
	public void reload() {
		config.reload();
		dialogs.reload();
		lang.reload();
		playerCache.reload();
	}
}
