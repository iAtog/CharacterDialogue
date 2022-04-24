package me.iatog.characterdialogue.dialogs.method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.DialogSession;

public class BroadcastMethod extends DialogMethod<CharacterDialoguePlugin> {

	public BroadcastMethod() {
		super("broadcast");
	}

	@Override
	public void execute(Player player, String arg, DialogSession session) {
		Bukkit.broadcastMessage(arg);
	}

}
