package me.iatog.characterdialogue.dialogs.choice;

import me.iatog.characterdialogue.api.CharacterDialogueAPI;
import me.iatog.characterdialogue.dialogs.DialogChoice;
import me.iatog.characterdialogue.session.ChoiceSession;
import me.iatog.characterdialogue.session.DialogSession;

public class DialogueChoice extends DialogChoice {

	public DialogueChoice() {
		super("dialogue", true);
	}

	@Override
	public void onSelect(String argument, DialogSession dialogSession, ChoiceSession choiceSession) {
		CharacterDialogueAPI.get().runDialogueExpression(dialogSession.getPlayer(),
				argument,
				dialogSession.getDisplayName());

		dialogSession.startNext();
	}

}
