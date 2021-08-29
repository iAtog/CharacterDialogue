package me.iatog.conditionaldialog.dialogs.method;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.iatog.conditionaldialog.dialogs.DialogMethod;

public class SendMethod extends DialogMethod {

	public SendMethod() {
		super("send");
	}

	@Override
	public void cast(Player player, String arg) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', arg));
	}

}
