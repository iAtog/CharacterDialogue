package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.util.TextUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.DialogSession;

public class SendMethod extends DialogMethod<CharacterDialoguePlugin> {

	public SendMethod() {
		super("send");
	}

	@Override
	public void execute(Player player, String arg, DialogSession session) {
		player.sendMessage(arg);
	}

}
