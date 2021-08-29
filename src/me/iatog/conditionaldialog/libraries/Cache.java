package me.iatog.conditionaldialog.libraries;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.iatog.conditionaldialog.dialogs.DialogMethod;
import me.iatog.conditionaldialog.misc.WaitSession;

public class Cache {
	
	private Map<String, DialogMethod> methods;
	private Map<UUID, WaitSession> waitSessions;
	
	public Cache() {
		this.methods = new HashMap<>();
		this.waitSessions = new HashMap<>();
	}
	
	public Map<String, DialogMethod> getMethods() {
		return methods;
	}
	
	public Map<UUID, WaitSession> getWaitSessions() {
		return waitSessions;
	}
	
	public void clearAll() {
		methods.clear();
	}
	
}
