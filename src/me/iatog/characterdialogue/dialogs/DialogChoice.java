package me.iatog.characterdialogue.dialogs;

import org.jetbrains.annotations.Nullable;

import me.iatog.characterdialogue.session.DialogSession;

public abstract class DialogChoice {
	
	private String id;
	
	public DialogChoice(@Nullable String id) {
		this.id = id;
	}
	
	public abstract void onSelect(DialogSession session);
	
	public String getId() {
		return id;
	}
}
