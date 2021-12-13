package me.iatog.characterdialogue.dialogs;

import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.libraries.Cache;
import me.iatog.characterdialogue.session.DialogSession;

public abstract class Choice<T extends PlayerEvent> {
	
	private String id;
	private T event;
	
	public Choice(@Nullable String id, @Nullable T event) {
		this.id = id;
		this.event = event;
	}
	
	public abstract void onSelect();
	public abstract void onEvent(T event, DialogSession session);
	
	@EventHandler
	public void eventExecution(T event) {
		Player player = event.getPlayer();
		CharacterDialoguePlugin characterDialogue = CharacterDialoguePlugin.getInstance();
		Cache cache = characterDialogue.getCache();
		Map<UUID, DialogSession> sessions = cache.getSessions();
		
		if(!sessions.containsKey(player.getUniqueId())) {
			return;
		}
		
		onEvent(event, sessions.get(player.getUniqueId()));
	}
	
	public String getId() {
		return id;
	}
	
	public T getEvent() {
		return event;
	}
	
}
