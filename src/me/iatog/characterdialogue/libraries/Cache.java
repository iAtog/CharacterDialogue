package me.iatog.characterdialogue.libraries;

import java.util.HashMap;
import java.util.Map;

import me.iatog.characterdialogue.dialogs.DialogMethod;

public class Cache {
	
	private Map<String, DialogMethod> methods;
	
	public Cache() {
		this.methods = new HashMap<>();
	}
	
	public Map<String, DialogMethod> getMethods() {
		return methods;
	}
		
	public void clearAll() {
		methods.clear();
	}
	
}
