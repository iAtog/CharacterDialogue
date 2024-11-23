package me.iatog.characterdialogue.dialogs.choice;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.CharacterDialogueAPI;
import me.iatog.characterdialogue.api.dialog.Dialogue;
import me.iatog.characterdialogue.dialogs.DialogChoice;
import me.iatog.characterdialogue.session.ChoiceSession;
import me.iatog.characterdialogue.session.DialogSession;

public class StartDialogChoice extends DialogChoice {

	private final CharacterDialoguePlugin main;

	public StartDialogChoice(CharacterDialoguePlugin main) {
		super("start_dialogue", true);
		this.main = main;
	}

	@Override
	public void onSelect(String argument, DialogSession session, ChoiceSession choiceSession) {
		CharacterDialogueAPI api = CharacterDialogueAPI.get();
		Dialogue dialogue = api.getDialogue(argument);
		session.destroy();
		
		if (dialogue != null) {
			api.runDialogue(session.getPlayer(), dialogue);
		} else {
			main.getLogger().severe("The dialogue name in \"start_dialogue\" choice doesn't exists");
		}
	}

}
