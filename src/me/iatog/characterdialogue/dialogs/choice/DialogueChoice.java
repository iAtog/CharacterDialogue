package me.iatog.characterdialogue.dialogs.choice;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogChoice;
import me.iatog.characterdialogue.session.ChoiceSession;
import me.iatog.characterdialogue.session.DialogSession;

public class DialogueChoice extends DialogChoice {

	private CharacterDialoguePlugin main;
	
	public DialogueChoice(CharacterDialoguePlugin main) {
		super("dialogue", true);
		this.main = main;
	}

	@Override
	public void onSelect(String argument, DialogSession dialogSession, ChoiceSession choiceSession) {
		main.getApi().runDialogueExpression(dialogSession.getPlayer(), argument);
		
		if(dialogSession != null && (dialogSession.getCurrentIndex() + 1) <= dialogSession.getLines().size() && (dialogSession.getPlayer() != null && dialogSession.getPlayer().isOnline())) {
			dialogSession.start(dialogSession.getCurrentIndex() + 1);
		}
	}

}
