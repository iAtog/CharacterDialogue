package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.enums.CompletedType;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.util.SingleUseConsumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DispatchCommandMethod extends DialogMethod<CharacterDialoguePlugin> {

	public DispatchCommandMethod() {
		super("dispatch_command");
	}

	@Override
	public void execute(Player player, String arg, DialogSession session, SingleUseConsumer<CompletedType> completed) {
		Bukkit.dispatchCommand(player, arg);
		completed.accept(CompletedType.CONTINUE);
	}

}
