package me.iatog.characterdialogue.dialogs.method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.DialogSession;

public class CommandMethod extends DialogMethod {

	public CommandMethod() {
		super("command");
	}

	@Override
	public void execute(Player player, String arg, DialogSession session) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), arg);
	}

}
