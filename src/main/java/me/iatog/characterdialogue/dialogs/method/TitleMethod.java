package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodConfiguration;
import me.iatog.characterdialogue.enums.CompletedType;
import me.iatog.characterdialogue.nms.TitleBuilder;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.util.SingleUseConsumer;
import org.bukkit.entity.Player;

public class TitleMethod extends DialogMethod<CharacterDialoguePlugin> {

	public TitleMethod() {
		super("title");
	}

	@Override
	public void execute(Player player, MethodConfiguration configuration, DialogSession session, SingleUseConsumer<CompletedType> completed) {
		// TITLE: Title || Subtitle || 20 || 60 || 20
		// NEW TITLE{title=My title,subtitle=This is a subtitle, fadeIn=20, stay=60, fadeOut=20}
		String[] part = configuration.getArgument().split("||");
		
		try {
			String title = part[0].trim();
			String subtitle = part[1].trim();
			int fadeIn = Integer.parseInt(part[2].trim());
			int stay = Integer.parseInt(part[3].trim());
			int fadeOut = Integer.parseInt(part[4].trim());

			new TitleBuilder(player)
					.setText(title, subtitle)
					.setTimings(fadeIn, stay, fadeOut)
					.send();
			completed.accept(CompletedType.CONTINUE);
		}catch(EnumConstantNotPresentException ex) {
			player.sendMessage("Invalid title data");
			completed.accept(CompletedType.DESTROY);
		}
	}
}
