package me.iatog.characterdialogue.dialogs.method;

import org.bukkit.entity.Player;

import me.iatog.characterdialogue.CharacterDialoguePlugin;
import me.iatog.characterdialogue.dialogs.DialogMethod;
import me.iatog.characterdialogue.session.DialogSession;

public class ConditionalMethod extends DialogMethod<CharacterDialoguePlugin> {

	public ConditionalMethod() {
		super("conditional");
		/**
		 * PREDISEÑO DEL DIALOGO:
		 * lines:
		 * - send: hello
		 * - conditional:        CONDITIONAL    |               CONDITIONAL = TRUE                           |  CONDITIONAL = FALSE
		 * - conditional: %playername% == aatog | (RUN_DIALOGUE/STOP_AND_SEND_MESSAGE/STOP/RUN_METHOD) %arg% | (METHODS)
		 * - conditional: %time% > 5000 | STOP_AND_SEND_MESSAGE: %npc_name%: It would be better to talk about it later
		 * - conditional: %kills% == 20 |
		 */
	}

	@SuppressWarnings("unused")
	@Override
	public void execute(Player player, String arg, DialogSession session) {
		String condition = arg.split(":")[0];
		String toExecute = arg.substring(condition.length());
		
		
	}

	public boolean evaluateCondition(Player player, String condition) {
		String[] parts = condition.split("==");
		if (parts.length != 2) {
			throw new IllegalArgumentException("Invalid condition format");
		}

		String placeholder = replacePlaceholders(player, parts[0].trim());
		String value = parts[1].trim();

		return placeholder.equals(value);
	}

	public String replacePlaceholders(Player player, String text) {
		// SUPPORT PARA PLACEHOLDERAPI DESPUES
		return text
				.replace("%playername%", player.getName())
				.replace("%time%", String.valueOf(player.getWorld().getTime()));
	}

}
