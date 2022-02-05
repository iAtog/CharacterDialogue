package me.iatog.characterdialogue.dialogs.choice;

import me.iatog.characterdialogue.dialogs.DialogChoice;
import me.iatog.characterdialogue.placeholders.Placeholders;
import me.iatog.characterdialogue.session.ChoiceSession;
import me.iatog.characterdialogue.session.DialogSession;

public class MessageChoice extends DialogChoice {

	public MessageChoice() {
		super("message", true);
	}

	@Override
	public void onSelect(String argument, DialogSession dialogSession, ChoiceSession choiceSession) {
		dialogSession.getPlayer().sendMessage(Placeholders.translate(null, argument)
				.replace("%npc_name%", dialogSession.getDisplayName()));
		
		if(dialogSession != null && (dialogSession.getCurrentIndex() + 1) < dialogSession.getDialogs().size() && (dialogSession.getPlayer() != null && dialogSession.getPlayer().isOnline())) {
			dialogSession.getPlayer().sendMessage("CONTINUE");
			dialogSession.start(dialogSession.getCurrentIndex() + 1);
		}
	}

}
