package me.iatog.characterdialogue.libraries;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.interfaces.FileFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class FileFactoryImpl implements FileFactory {
	
	private final YamlDocument config;
	//private final YamlFile dialogs;
	private final YamlDocument lang;
	private final YamlDocument playerCache;
	private final YamlDocument choices;

	private final CharacterDialoguePlugin main;
	
	public FileFactoryImpl(CharacterDialoguePlugin main) {
		this.main = main;
		//this.config = new YamlFile(main, "config");
		//this.dialogs = new YamlFile(main, "dialogs");
		//this.lang = new YamlFile(main, "language");
		//this.playerCache = new YamlFile(main, "player-cache");

        try {
            this.config = createUpdatableYamlDocument("config.yml");
			this.lang = createUpdatableYamlDocument("language.yml");
			this.playerCache = createYamlDocument("player-cache.yml");
			this.choices = createYamlDocument("choices.yml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


	}

	private YamlDocument createUpdatableYamlDocument(String name) throws IOException {
		return YamlDocument.create(getFile(name), getResource(name, main),
				GeneralSettings.DEFAULT, LoaderSettings.builder()
						.setAutoUpdate(true).build(), DumperSettings.DEFAULT,
				UpdaterSettings.builder()
						.setAutoSave(true)
						.setVersioning(new BasicVersioning("file-version")).build());
	}

	private YamlDocument createYamlDocument(String filename) throws IOException {
		return YamlDocument.create(getFile(filename), getResource(filename, main));
	}

	private File getFile(String name) {
		return new File(main.getDataFolder() + "/" + name);
	}

	private InputStream getResource(String name, CharacterDialoguePlugin main) {
		return Objects.requireNonNull(main.getResource(name));
	}
	
	@Override
	public YamlDocument getConfig() {
		return config;
	}

	public YamlDocument getChoicesFile() {
		return choices;
	}

	@Override
	public YamlDocument getLanguage() {
		return lang;
	}
	
	@Override
	public YamlDocument getPlayerCache() {
		return playerCache;
	}
	
	
	@Override
	public void reload() throws IOException {
		config.reload();
		//dialogs.reload();
		lang.reload();
		playerCache.reload();
		choices.reload();

		//main.getAllDialogues().forEach(YamlFile::reload);
	}
}
