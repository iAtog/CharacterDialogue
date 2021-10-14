package me.iatog.characterdialogue.libraries;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.interfaces.FileFactory;

public class FileFactoryImpl implements FileFactory {
	
	private YamlFile config;
	private YamlFile dialogs;
	private YamlFile lang;
	private YamlFile placeholders;
	private YamlFile playerCache;
	
	public FileFactoryImpl(CharacterDialoguePlugin main) {
		//this.config = new YamlFile(main, "config");
		this.dialogs = new YamlFile(main, "dialogs");
		this.lang = new YamlFile(main, "lang");
		this.placeholders = new YamlFile(main, "placeholders");
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
	public YamlFile getLang() {
		return lang;
	}
	
	@Override
	public YamlFile getPlaceholders() {
		return placeholders;
	}
	
	@Override
	public YamlFile getPlayerCache() {
		return playerCache;
	}
	
	@Override
	public void reload() {
		//config.reload();
		dialogs.reload();
		lang.reload();
		placeholders.reload();
		playerCache.reload();
	}
}
