package me.iatog.characterdialogue.dialogs.method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.DialogSession;

public class DispatchCommandMethod extends DialogMethod {

	public DispatchCommandMethod() {
		super("dispatch_command");
	}

	@Override
	public void execute(Player player, String arg, DialogSession session) {
		Bukkit.dispatchCommand(player, arg);
	}

}
