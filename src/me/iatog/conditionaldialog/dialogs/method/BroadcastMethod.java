package me.iatog.conditionaldialog.dialogs.method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.iatog.conditionaldialog.dialogs.DialogMethod;

public class BroadcastMethod extends DialogMethod {

	public BroadcastMethod() {
		super("broadcast");
	}

	@Override
	public void cast(Player player, String arg) {
		Bukkit.broadcastMessage(arg);
	}

}
