package me.iatog.characterdialogue.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.iatog.characterdialogue.CharacterDialogPlugin;
import me.iatog.characterdialogue.libraries.YamlFile;

public class PlayerCache {
	
	private UUID uuid;
	private List<String> readedDialogs;
	private YamlFile file;
	
	public PlayerCache(CharacterDialogPlugin main, UUID uuid, String readedDialogs) {
		this.uuid = uuid;
		this.readedDialogs = new ArrayList<>();
		this.file = new YamlFile(main, uuid.toString(), "cache");
		
		for(String dialog : readedDialogs.split(",")) {
			this.readedDialogs.add(dialog);
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
	
	public YamlFile getFile() {
		return file;
	}
	
}
