package me.iatog.characterdialogue.interfaces;

import me.iatog.characterdialogue.libraries.YamlFile;

public interface FileFactory {
	YamlFile getConfig();
	YamlFile getDialogs();
	YamlFile getLang();
	YamlFile getPlaceholders();
	YamlFile getPlayerCache();
	YamlFile getNPC();
	
	void reload();
}
