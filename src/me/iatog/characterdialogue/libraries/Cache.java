package me.iatog.characterdialogue.libraries;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.ChoiceSession;
import me.iatog.characterdialogue.session.DialogSession;

public class Cache {

	private Map<String, DialogMethod> methods;
	private Map<UUID, DialogSession> dialogSessions;
	private Map<UUID, ChoiceSession> choiceSessions;

	public Cache() {
		this.methods = new HashMap<>();
		this.dialogSessions = new HashMap<>();
		this.choiceSessions = new HashMap<>();
	}

	public Map<String, DialogMethod> getMethods() {
		return methods;
	}

	public Map<UUID, DialogSession> getDialogSessions() {
		return dialogSessions;
	}
	
	public Map<UUID, ChoiceSession> getChoiceSessions() {
		return choiceSessions;
	}

	public void clearAll() {
		methods.clear();
		dialogSessions.clear();
		choiceSessions.clear();
	}

}
