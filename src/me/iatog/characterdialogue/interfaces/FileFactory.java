package me.iatog.characterdialogue.interfaces;

import me.iatog.characterdialogue.libraries.YamlFile;

public interface FileFactory {
	YamlFile getConfig();
	YamlFile getDialogs();
	YamlFile getLanguage();
	YamlFile getPlayerCache();
	
	void reload();
}
