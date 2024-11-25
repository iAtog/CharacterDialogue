package me.iatog.characterdialogue.misc;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.iatog.characterdialogue.CharacterDialoguePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PlayerCache {
	
	private final UUID uuid;
	private final List<String> readedDialogs;
	private final YamlDocument file;
	
	public PlayerCache(CharacterDialoguePlugin main, UUID uuid) throws IOException {
		this.uuid = uuid;
        try {
            this.file = YamlDocument.create(new File("cache/"+uuid.toString()+".yml"));
        } catch (IOException e) {
            throw new IOException("No file found");
        }
        //this.file = new YamlFile(main, uuid.toString(), "cache");
		
		if(file.contains("readed")) {
			this.readedDialogs = file.getStringList("readed");
		} else {
			this.readedDialogs = new ArrayList<>();
		}
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}
	
	public List<String> getReadedDialogs() {
		return Collections.unmodifiableList(readedDialogs);
	}
	
	public void addReaded(String id) {
		readedDialogs.add(id);
	}
	
	public void removeReaded(String id) {
		readedDialogs.remove(id);
	}
	
	public String getReaded(int index) {
		return readedDialogs.get(index);
	}
	
	public YamlDocument getFile() {
		return file;
	}
	
	public void save() {
		file.set("readed", readedDialogs);
        try {
            file.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
	
}
