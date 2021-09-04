package me.iatog.characterdialogue.libraries;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.misc.PlayerCache;
import me.iatog.characterdialogue.session.DialogSession;

public class Cache {

	private Map<String, DialogMethod> methods;
	private Map<UUID, DialogSession> sessions;
	private Map<UUID, PlayerCache> playerCache;

	public Cache() {
		this.methods = new HashMap<>();
		this.sessions = new HashMap<>();
		this.playerCache = new HashMap<>();
	}

	public Map<String, DialogMethod> getMethods() {
		return methods;
	}

	public Map<UUID, DialogSession> getSessions() {
		return sessions;
	}

	public Map<UUID, PlayerCache> getPlayerCache() {
		return playerCache;
	}

	public void clearAll() {
		methods.clear();
		sessions.clear();
		playerCache.clear();
	}

}
