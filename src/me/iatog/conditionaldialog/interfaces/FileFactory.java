package me.iatog.conditionaldialog.interfaces;

import me.iatog.conditionaldialog.libraries.YamlFile;

public interface FileFactory {
	YamlFile getConfig();
	YamlFile getDialogs();
}
