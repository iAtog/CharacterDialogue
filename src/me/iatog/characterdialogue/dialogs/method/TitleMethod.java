package me.iatog.characterdialogue.dialogs.method;

import org.bukkit.entity.Player;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.nms.TitleBuilder;
import me.iatog.characterdialogue.session.DialogSession;

public class TitleMethod extends DialogMethod<CharacterDialoguePlugin> {

	public TitleMethod() {
		super("title");
	}

	@Override
	public void execute(Player player, String arg, DialogSession session) {
		// TITLE: Title || Subtitle || 20 || 60 || 20
		String[] part = arg.split("||");
		
		String title = part[0].trim();
		String subtitle = part[1].trim();
		int fadeIn = Integer.valueOf(part[2].trim());
		int stay = Integer.valueOf(part[3].trim());
		int fadeOut = Integer.valueOf(part[4].trim());
		
		new TitleBuilder(player)
		    .setText(title, subtitle)
		    .setTimings(fadeIn, stay, fadeOut)
		    .send();
	}
}
