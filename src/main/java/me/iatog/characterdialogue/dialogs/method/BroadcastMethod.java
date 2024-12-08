package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodContext;
import org.bukkit.Bukkit;

public class BroadcastMethod extends DialogMethod<CharacterDialoguePlugin> {

	public BroadcastMethod() {
		super("broadcast");
	}

	@Override
	public void execute(MethodContext context) {
		Bukkit.broadcastMessage(context.getConfiguration().getArgument());
		this.next(context);
	}

}
