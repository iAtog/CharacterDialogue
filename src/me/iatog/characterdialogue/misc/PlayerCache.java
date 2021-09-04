package me.iatog.characterdialogue.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerCache {
	
	private UUID uuid;
	private List<String> readedDialogs;
	
	public PlayerCache(UUID uuid, String readedDialogs) {
		this.uuid = uuid;
		this.readedDialogs = new ArrayList<>();
		
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
	
}
