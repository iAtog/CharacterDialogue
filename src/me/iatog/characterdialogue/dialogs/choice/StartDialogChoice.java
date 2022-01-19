package me.iatog.characterdialogue.dialogs.choice;

import me.iatog.characterdialogue.dialogs.DialogChoice;
import me.iatog.characterdialogue.session.ChoiceSession;
import me.iatog.characterdialogue.session.DialogSession;

public class StartDialogChoice extends DialogChoice {

	public StartDialogChoice() {
		super("start_dialogue", true);
	}

	@Override
	public void onSelect(String argument, DialogSession session, ChoiceSession choiceSession) {
		session.destroy();
		
		
	}

}
