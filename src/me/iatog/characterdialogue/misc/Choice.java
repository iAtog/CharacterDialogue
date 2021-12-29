package me.iatog.characterdialogue.misc;

import me.iatog.characterdialogue.dialogs.DialogChoice;

public class Choice {
	
	private DialogChoice choice;
	private String message;
	
	public Choice(DialogChoice choice, String message) {
		this.choice = choice;
		this.message = message;
	}

	public DialogChoice getChoice() {
		return choice;
	}

	public String getMessage() {
		return message;
	}
	
}
