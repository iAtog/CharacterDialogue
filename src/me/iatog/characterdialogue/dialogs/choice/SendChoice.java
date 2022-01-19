package me.iatog.characterdialogue.dialogs.choice;

import org.bukkit.entity.Player;

import me.iatog.characterdialogue.dialogs.DialogChoice;
import me.iatog.characterdialogue.placeholders.Placeholders;
import me.iatog.characterdialogue.session.ChoiceSession;
import me.iatog.characterdialogue.session.DialogSession;
import net.md_5.bungee.api.ChatColor;

public class SendChoice extends DialogChoice {
	public SendChoice() {
		super("send", true);
	}

	@Override
	public void onSelect(String argument, DialogSession session, ChoiceSession choiceSession) {
		if(session == null) {
			return;
		}
		String colored = ChatColor.translateAlternateColorCodes('&', argument);
		Player player = session.getPlayer();
		player.sendMessage(Placeholders.translate(player, colored));
	}
}
