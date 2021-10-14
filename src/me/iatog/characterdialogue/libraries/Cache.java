package me.iatog.characterdialogue.libraries;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.DialogSession;

public class Cache {

	private Map<String, DialogMethod> methods;
	private Map<UUID, DialogSession> sessions;

	public Cache() {
		this.methods = new HashMap<>();
		this.sessions = new HashMap<>();
	}

	public Map<String, DialogMethod> getMethods() {
		return methods;
	}

	public Map<UUID, DialogSession> getSessions() {
		return sessions;
	}

	public void clearAll() {
		methods.clear();
		sessions.clear();
	}

}
