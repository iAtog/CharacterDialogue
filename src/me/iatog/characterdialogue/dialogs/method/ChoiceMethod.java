package me.iatog.characterdialogue.dialogs.method;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.api.events.ChoiceSelectEvent;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.ChoiceSession;
import me.iatog.characterdialogue.session.DialogSession;

public class ChoiceMethod extends DialogMethod<CharacterDialoguePlugin> implements Listener {

	public ChoiceMethod(CharacterDialoguePlugin main) {
		super("choice", main);
	}

	@Override
	public void execute(Player player, String arg, DialogSession session) {
		CharacterDialoguePlugin main = getProvider();
		ChoiceSession choiceSession = new ChoiceSession(main, player);
		choiceSession.addChoice("", arg);
		player.sendMessage("");
	}
	
	@EventHandler
	public void onChoiceSelect(ChoiceSelectEvent event) {
		
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		String command = event.getMessage();
		// Command: /;/select 4a8d7587-38f3-35e7-b29c-88c715aa6ba8 1
		
	}
	
}
