package com.github.iatog.characterdialogue.api.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

import org.bukkit.plugin.Plugin;

public class DialogueFileManager {
    
    private String DIALOGUES_FOLDER = "dialogues";
    
    private Plugin plugin;
    
    private Map<String, YamlFile> dialogues;
    
    private DialogueFileManager(Plugin plugin) {
        this.plugin = plugin;
        this.dialogues = new HashMap<>();
        
        fetchFiles();
    }
    
    public Map<String, YamlFile> getDialogues() {
        return Collections.unmodifiableMap(dialogues);
    }
    
    public boolean exists(String name) {
        return dialogues.containsKey(name);
    }
    
    public YamlFile getDialogue(String name) {
        return dialogues.get(name);
    }
    
    public void reloadFiles() {
        dialogues.clear();
        fetchFiles();
    }
    
    private void fetchFiles() {
        File folder = new File(plugin.getDataFolder() + "/" + DIALOGUES_FOLDER);
        
        if(!folder.isDirectory()) {
            plugin.getLogger().warning("The supposed \"folder\" of the dialogues turns out not to be a folder...");
            return;
        }
        
        String[] dialogues = list(folder, (dir, name) -> {
            return name.endsWith(".yml");
        });
        
        for(String filename : dialogues) {
            YamlFile dialogue = new YamlFile(plugin, filename, DIALOGUES_FOLDER);
            String name = filename.split(".")[0];
            
            this.dialogues.put(name, dialogue);
        }
    }
    
    private String[] list(File folder, BiPredicate<File, String> predicate) {
        return folder.list(new FileFilter(predicate));
    }
    
    public static DialogueFileManager createManager(Plugin plugin) {
        return new DialogueFileManager(plugin);
    }

    private class FileFilter implements FilenameFilter {
        private BiPredicate<File, String> predicate;
        
        public FileFilter(BiPredicate<File, String> predicate) {
            this.predicate = predicate;
        }
        
        @Override
        public boolean accept(File dir, String name) {
            return predicate.test(dir, name);
        }
        
    }
}
