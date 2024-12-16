package me.iatog.characterdialogue.interfaces;

import dev.dejvokep.boostedyaml.YamlDocument;

import java.io.IOException;

public interface FileFactory {
    YamlDocument getConfig();

    //YamlFile getDialogs();
    YamlDocument getLanguage();

    YamlDocument getPlayerCache();

    YamlDocument getChoicesFile();

    void reload() throws IOException;
}
