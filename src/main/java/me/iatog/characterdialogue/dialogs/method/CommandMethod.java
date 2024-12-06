package me.iatog.characterdialogue.dialogs.method;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.dialogs.MethodConfiguration;
import me.iatog.characterdialogue.enums.CompletedType;
import me.iatog.characterdialogue.session.DialogSession;
import me.iatog.characterdialogue.util.SingleUseConsumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandMethod extends DialogMethod<CharacterDialoguePlugin> {

	public CommandMethod() {
		super("command");
	}

	@Override
	public void execute(Player player, MethodConfiguration configuration, DialogSession session, SingleUseConsumer<CompletedType> completed) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), configuration.getArgument());
		completed.accept(CompletedType.CONTINUE);
	}

}
