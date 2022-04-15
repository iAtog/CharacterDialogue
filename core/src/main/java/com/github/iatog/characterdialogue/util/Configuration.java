package com.github.iatog.characterdialogue.util;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Configuration extends YamlConfiguration {
    
    private final String FILE_EXTENSION = ".yml";
    
    private final String fileName;
    private final Plugin plugin;
    private final File folder;

    public Configuration(Plugin plugin, String fileName) {
        this(plugin, fileName, plugin.getDataFolder());
    }

    public Configuration(Plugin plugin, String fileName, File folder) {
        this.folder = folder;
        this.plugin = plugin;
        this.fileName = fileName + (fileName.endsWith(FILE_EXTENSION) ? "" : FILE_EXTENSION);
        this.create();
    }


    @Override
    public String getString(String path) {
        // Return the specified path in case the result is null
        return super.getString(path, path);
    }

    /**
     * Returns a String from a configuration file.
     * @param path The path of the string
     * @param colorize Whether to translate alternate color codes or not
     * @return The final string
     */
    public String getString(String path, boolean colorize) {
        String result = getString(path);
        return colorize ? ChatColor.translateAlternateColorCodes('&', result) : result;
    }

    public void create() {
        try {
            File file = new File(this.folder, this.fileName);
            if (file.exists()) {
                this.load(file);
                this.save(file);
            } else {
                if (this.plugin.getResource(this.fileName) != null) {
                    this.plugin.saveResource(this.fileName, false);
                } else {
                    this.save(file);
                }

                this.load(file);
            }
        } catch (IOException | InvalidConfigurationException exception) {
            this.plugin.getLogger().log(Level.SEVERE, "Unable to create '" + this.fileName + "'.", exception);
        }
    }

    public void save() {
        File folder = this.plugin.getDataFolder();
        File file = new File(folder, this.fileName);

        try {
            this.save(file);
        } catch (IOException exception) {
            this.plugin.getLogger().log(Level.SEVERE, "Unable to save file '" + this.fileName + "'.", exception);
        }

    }

    public void reload() {
        File folder = this.plugin.getDataFolder();
        File file = new File(folder, this.fileName);

        try {
            this.load(file);
        } catch (InvalidConfigurationException | IOException exception) {
            this.plugin.getLogger().log(Level.SEVERE, "Unable to reload file '" + this.fileName + "'.", exception);
        }
    }
}