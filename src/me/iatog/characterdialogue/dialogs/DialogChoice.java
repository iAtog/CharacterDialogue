package me.iatog.characterdialogue.dialogs;

import org.jetbrains.annotations.Nullable;

import me.iatog.characterdialogue.session.DialogSession;

public abstract class DialogChoice {
	
	private String id;
	private boolean requireArgument;
	
	public DialogChoice(String id, boolean requireArgument) {
		this.id = id;
		this.requireArgument = requireArgument;
	}
	
	public abstract void onSelect(DialogSession session);
	
	public String getId() {
		return id;
	}
	
	public boolean isArgumentRequired() {
		return requireArgument;
	}
}
