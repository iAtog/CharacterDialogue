package me.iatog.conditionaldialog.dialogs.method;

import org.bukkit.entity.Player;

import me.iatog.conditionaldialog.dialogs.DialogMethod;
import me.iatog.conditionaldialog.nms.TitleBuilder;

public class TitleMethod extends DialogMethod {

	public TitleMethod() {
		super("title");
	}

	@Override
	public void cast(Player player, String arg) {
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
