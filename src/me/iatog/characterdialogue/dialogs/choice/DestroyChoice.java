package me.iatog.characterdialogue.dialogs.choice;

import me.iatog.characterdialogue.dialogs.DialogChoice;
import me.iatog.characterdialogue.session.ChoiceSession;
import me.iatog.characterdialogue.session.DialogSession;

public class DestroyChoice extends DialogChoice {

	public DestroyChoice() {
		super("destroy", false);
	}

	@Override
	public void onSelect(String argument, DialogSession dialogSession, ChoiceSession choiceSession) {
		if(dialogSession == null) {
			return;
		}
		
		dialogSession.getPlayer().sendMessage("DESTROYED");
		dialogSession.destroy();
	}
	
}
