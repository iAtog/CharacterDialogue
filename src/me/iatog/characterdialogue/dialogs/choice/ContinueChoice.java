package me.iatog.characterdialogue.dialogs.choice;

import me.iatog.characterdialogue.dialogs.DialogChoice;
import me.iatog.characterdialogue.session.DialogSession;

public class ContinueChoice extends DialogChoice {

	public ContinueChoice() {
		super("continue", false);
	}

	@Override
	public void onSelect(DialogSession session) {
		session.start(session.getCurrentIndex() + 1);
	}
}
