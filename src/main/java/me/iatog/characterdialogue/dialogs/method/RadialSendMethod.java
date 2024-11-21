package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.DialogSession;
import org.bukkit.entity.Player;

public class RadialSendMethod extends DialogMethod<CharacterDialoguePlugin> {

	public RadialSendMethod() {
		super("radial_send");
	}

	@Override
	public void execute(Player player, String arg, DialogSession session) {
		
	}

}
