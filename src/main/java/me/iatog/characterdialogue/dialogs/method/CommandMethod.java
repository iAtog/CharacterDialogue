package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.DialogSession;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandMethod extends DialogMethod<CharacterDialoguePlugin> {

	public CommandMethod() {
		super("command");
	}

	@Override
	public void execute(Player player, String arg, DialogSession session) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), arg);
	}

}
