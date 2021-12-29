package me.iatog.characterdialogue.session;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogChoice;
import me.iatog.characterdialogue.interfaces.Session;
import me.iatog.characterdialogue.misc.Choice;

public class ChoiceSession implements Session {
	
	private CharacterDialoguePlugin main;
	private UUID player;
	private UUID uuid;
	private Map<Integer, Choice> choices;
	
	public ChoiceSession(CharacterDialoguePlugin main, Player player) {
		this.main = main;
		this.player = player.getUniqueId();
		this.uuid = UUID.randomUUID();
		this.choices = new TreeMap<>();
	}
	
	public AddChoiceResult addChoice(int index, String choice, String message) {
		Map<String, DialogChoice> cache = main.getCache().getChoices();
		
		if(choices.containsKey(index) ) {
			return AddChoiceResult.ALREADY_EXISTS;
		} else if(!cache.containsKey(choice)) {
			return AddChoiceResult.UNKNOWN_CHOICE;
		}
		
		choices.put(index, new Choice(cache.get(choice), message));
		return AddChoiceResult.SUCCESS;
	}
	
	public AddChoiceResult addChoice(String choice, String message) {
		return addChoice(choices.size() + 1, choice, message);
	}
	
	public boolean check() {
		return false;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(player);
	}
	
	public Map<Integer, Choice> getChoices() {
		return choices;
	}
	
	public UUID getUniqueId() {
		return uuid;
	}

	public enum AddChoiceResult {
		ALREADY_EXISTS, UNKNOWN_CHOICE, SUCCESS;
	}
}
