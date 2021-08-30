package me.iatog.conditionaldialog.dialogs.method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.iatog.conditionaldialog.dialogs.DialogMethod;

public class DispatchCommandMethod extends DialogMethod {

	public DispatchCommandMethod() {
		super("dispatch_command");
	}

	@Override
	public void cast(Player player, String arg) {
		Bukkit.dispatchCommand(player, arg);
	}

}
