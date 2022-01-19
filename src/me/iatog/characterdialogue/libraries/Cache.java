package me.iatog.characterdialogue.libraries;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import me.iatog.characterdialogue.api.dialog.Dialogue;
import me.iatog.characterdialogue.dialogs.DialogChoice;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.ChoiceSession;
import me.iatog.characterdialogue.session.DialogSession;

public class Cache {

	private Map<String, DialogMethod<? extends JavaPlugin>> methods;
	private Map<String, DialogChoice> choices;
	private Map<UUID, DialogSession> dialogSessions;
	private Map<UUID, ChoiceSession> choiceSessions;
	private Map<String, Dialogue> dialogues;
	
	public Cache() {
		this.methods = new HashMap<>();
		this.dialogSessions = new HashMap<>();
		this.choiceSessions = new HashMap<>();
		this.choices = new HashMap<>();
		this.dialogues = new HashMap<>();
	}

	public Map<String, DialogMethod<? extends JavaPlugin>> getMethods() {
		return methods;
	}
	
	public Map<String, DialogChoice> getChoices() {
		return choices;
	}

	public Map<UUID, DialogSession> getDialogSessions() {
		return dialogSessions;
	}
	
	public Map<UUID, ChoiceSession> getChoiceSessions() {
		return choiceSessions;
	}
	
	public Map<String, Dialogue> getDialogues() {
		return dialogues;
	}
	
	public void clearAll() {
		methods.clear();
		dialogSessions.clear();
		choiceSessions.clear();
	}
}
