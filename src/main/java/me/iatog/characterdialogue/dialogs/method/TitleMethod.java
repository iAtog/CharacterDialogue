package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodConfiguration;
import me.iatog.characterdialogue.dialogs.MethodContext;
import me.iatog.characterdialogue.nms.TitleBuilder;

public class TitleMethod extends DialogMethod<CharacterDialoguePlugin> {

	public TitleMethod() {
		super("title");
	}

	@Override
	public void execute(MethodContext context) {
		MethodConfiguration configuration = context.getConfiguration();
		// NEW TITLE{title="My title",subtitle="This is a subtitle, yay!", fadeIn=20, stay=60, fadeOut=20}
		
		try {
			String title = configuration.getString("title");
			String subtitle = configuration.getString("subtitle", "");
			int fadeIn = configuration.getInteger("fadeIn", 20);
			int stay = configuration.getInteger("stay", 60);
			int fadeOut = configuration.getInteger("fadeOut", 20);

			new TitleBuilder(context.getPlayer())
					.setText(title, subtitle)
					.setTimings(fadeIn, stay, fadeOut)
					.send();
			this.next(context);
		}catch(Exception ex) {
			context.getPlayer().sendMessage("Invalid title data");
			this.destroy(context);
		}
	}
}
