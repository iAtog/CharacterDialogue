package me.iatog.characterdialogue.filter;

import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

import me.iatog.characterdialogue.dialogs.method.ChoiceMethod;

public class ConsoleFilter implements Filter {

	@Override
	public boolean isLoggable(LogRecord record) {
		return !record.getMessage().contains(ChoiceMethod.COMMAND_NAME);
	}
	
	public static void filter(Logger logger) {
		ConsoleFilter filter = new ConsoleFilter();
		logger.setFilter(filter);
		Bukkit.getLogger().setFilter(filter);
		Logger.getLogger("Minecraft").setFilter(filter);
	}

}
