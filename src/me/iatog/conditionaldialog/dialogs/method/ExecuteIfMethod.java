package me.iatog.conditionaldialog.dialogs.method;

import org.bukkit.entity.Player;

import me.iatog.conditionaldialog.dialogs.DialogMethod;

public class ExecuteIfMethod extends DialogMethod {

	public ExecuteIfMethod() {
		super("execute_if");
	}

	@Override
	public void cast(Player player, String arg) {
		String condition = arg.split(":")[0];
		String toExecute = arg.substring(condition.length());
		
		
	}
}
