package me.iatog.conditionaldialog.dialogs.method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.iatog.conditionaldialog.dialogs.DialogMethod;

public class CommandMethod extends DialogMethod {

	public CommandMethod() {
		super("command");
	}

	@Override
	public void cast(Player player, String arg) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), arg);
	}

}
