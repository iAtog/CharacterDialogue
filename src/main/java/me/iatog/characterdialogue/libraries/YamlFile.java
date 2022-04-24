package me.iatog.characterdialogue.libraries;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class YamlFile extends YamlConfiguration {
	private final String fileName;

	private final Plugin plugin;

	private final String folder;

	public YamlFile(Plugin plugin, String fileName, String fileExtension, String folder) {
		this.folder = plugin.getDataFolder() + "/" + folder;
		this.plugin = plugin;
		this.fileName = fileName + (fileName.endsWith(fileExtension) ? "" : fileExtension);
		createFile();
	}

	public YamlFile(Plugin plugin, String fileName) {
		this(plugin, fileName, "");
	}

	public YamlFile(Plugin plugin, String fileName, String folder) {
		this(plugin, fileName, ".yml", folder);
	}

	private void createFile() {
		try {
			File file = new File(this.folder, this.fileName);
			if (file.exists()) {
				load(file);
				save(file);
				return;
			}
			if (this.plugin.getResource(this.fileName) != null) {
				this.plugin.saveResource(this.fileName, false);
			} else {
				save(file);
			}
			load(file);
		} catch (InvalidConfigurationException | IOException e) {
			this.plugin.getLogger().log(Level.SEVERE, "Failed to create \"" + this.fileName + "\" file", e);
		}
	}

	public void save() {
		// File folder = this.plugin.getDataFolder();
		File file = new File(this.folder, this.fileName);
		try {
			save(file);
		} catch (IOException e) {
			this.plugin.getLogger().log(Level.SEVERE, "Save of the file '" + this.fileName + "' failed.", e);
		}
	}

	public void reload() {
		// File folder = this.plugin.getDataFolder();
		File file = new File(this.folder, this.fileName);
		try {
			load(file);
		} catch (IOException | InvalidConfigurationException e) {
			this.plugin.getLogger().log(Level.SEVERE, "Reload of the file '" + this.fileName + "' failed.", e);
		}
	}
	
	public void setAndSave(String str, Object value) {
		this.set(str, value);
		this.save();
	}
	
}

