package me.iatog.characterdialogue.dialogs.choice;

import me.iatog.characterdialogue.dialogs.DialogChoice;
import me.iatog.characterdialogue.session.ChoiceSession;
import me.iatog.characterdialogue.session.DialogSession;

public class ContinueChoice extends DialogChoice {

	public ContinueChoice() {
		super("continue", false);
	}

	@Override
	public void onSelect(String argument, DialogSession dialogSession, ChoiceSession choiceSession) {
		if(dialogSession != null && (dialogSession.getCurrentIndex() + 1) < dialogSession.getDialogs().size() && (dialogSession.getPlayer() != null && dialogSession.getPlayer().isOnline())) {
			dialogSession.getPlayer().sendMessage("CONTINUE");
			dialogSession.start(dialogSession.getCurrentIndex() + 1);
		}
	}
}
